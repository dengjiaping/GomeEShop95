package com.gome.ecmall.push;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.text.TextUtils;
import android.text.format.Time;
import android.widget.RemoteViews;

import com.gome.ecmall.dao.PushHistoryDao;
import com.gome.ecmall.dao.PushKeepAliveTimeDao;
import com.gome.ecmall.home.GomeEMallActivity;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.eshopnew.R;
import com.ibm.mqtt.MqttClient;
import com.ibm.mqtt.MqttException;
import com.ibm.mqtt.MqttSimpleCallback;

/**
 * 消息推送服务
 * 
 * @author liuyang-ds
 * 
 */
public class PushService extends Service {
    /**
     * tag
     */
    public static final String TAG = "PushService";
    /**
     * 连接地址
     */
    private static String MQTT_HOST = Constants.URL_PUSH_SERVER;
    /**
     * 要注册的主题
     */
    private String[] topics = null;
    /**
     * mqtt id 需要唯一，并且不能大于23bytes
     */
    private String clientID = "";
    /**
     * 服务器端是否在连接断开后马上清除掉token
     */
    private static boolean MQTT_CLEAN_START = false;
    /**
     * 心跳时间（单位：秒） 最大好像不能超过30分钟
     */
    private static short MQTT_KEEP_ALIVE = 30;
    /**
     * 注册的主题类型
     */
    private static int[] MQTT_QUALITIES_OF_SERVICE = { 1, 1, 1 };
    private static int MQTT_QUALITY_OF_SERVICE = 0;
    /**
     * 服务气短是否保留消息
     */
    private static boolean MQTT_RETAINED_PUBLISH = false;

    public static String MQTT_CLIENT_ID = "gome";

    private static final String ACTION_START = MQTT_CLIENT_ID + ".START";
    private static final String ACTION_STOP = MQTT_CLIENT_ID + ".STOP";
    private static final String ACTION_KEEPALIVE = MQTT_CLIENT_ID + ".KEEP_ALIVE";
    private static final String ACTION_RECONNECT = MQTT_CLIENT_ID + ".RECONNECT";

    private ConnectivityManager mConnMan;
    /**
     * 是否正在连接
     */
    private boolean mStarted;

    private static final long KEEP_ALIVE_INTERVAL = 1000 * 60 * 28;

    private static final long INITIAL_RETRY_INTERVAL = 1000 * 10;

    private static final long MAXIMUM_RETRY_INTERVAL = 1000 * 60 * 30;

    private SharedPreferences mPrefs;

    public static final String PREF_STARTED = "isStarted";

    public static final String PREF_DEVICE_ID = "deviceID";

    public static final String PREF_RETRY = "retryInterval";

    public static String NOTIF_TITLE = "Tokudu";

    private static MQTTConnection mConnection;

    private long mStartTime;

    private int notificationNB = 0;

    private PushHistoryDao pushHistoryDao;

    private static MqttClient mqttClient = null;

    private ConnectionLog mLog;// 记录日志用

    public static void actionStart(Context ctx) {
        Intent i = new Intent(ctx, PushService.class);
        i.setAction(ACTION_START);
        ctx.startService(i);
    }

    public static void actionStop(Context ctx) {
        Intent i = new Intent(ctx, PushService.class);
        i.setAction(ACTION_STOP);
        ctx.startService(i);
    }

    public static void actionPing(Context ctx) {
        Intent i = new Intent(ctx, PushService.class);
        i.setAction(ACTION_KEEPALIVE);
        ctx.startService(i);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        pushHistoryDao = new PushHistoryDao(this);
        BDebug.d(TAG, "服务被创建了");
        mStartTime = System.currentTimeMillis();
        mPrefs = getSharedPreferences(TAG, MODE_PRIVATE);
        mConnMan = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        try {
            mLog = new ConnectionLog(MQTT_CLIENT_ID);

        } catch (IOException e) {

        }
        handleCrashedService();
    }

    /**
     * 服务以外被关闭时，再次开启时关闭需要关闭的
     */
    private void handleCrashedService() {
        if (wasStarted() == true) {
            BDebug.d(TAG, "服务被意外关闭了");
            stopKeepAlives();
        }
    }

    @Override
    public void onDestroy() {
        BDebug.d(TAG, "服务被销毁了started=" + mStarted);
        if (mStarted == true) {
            stop();
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        BDebug.d(TAG, "开启服务的Intent是----" + intent);
        if (intent != null) {
            if (intent.getAction().equals(ACTION_STOP) == true) {
                stop();
                stopSelf();

            } else if (intent.getAction().equals(ACTION_START) == true) {
                new Thread() {
                    public void run() {
                        mqttStart();
                    }

                }.start();

            } else if (intent.getAction().equals(ACTION_KEEPALIVE) == true) {
                keepAlive();
            } else if (intent.getAction().equals(ACTION_RECONNECT) == true) {
                if (isNetworkAvailable()) {
                    reconnectIfNecessary();
                }
            }
        } else {
            new Thread() {
                public void run() {
                    mqttStart();
                }

            }.start();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean wasStarted() {
        return mPrefs.getBoolean(PREF_STARTED, false);
    }

    private void setStarted(boolean started) {
        mPrefs.edit().putBoolean(PREF_STARTED, started).commit();
        mStarted = started;
    }

    private synchronized void mqttStart() {
        BDebug.d(TAG, "进入开启连接的方法");
        if (mStarted == true) {
            if (mqttClient != null) {
                if (mqttClient.isConnected()) {
                    BDebug.d(TAG, "连接还保持着,不需要重新创建");
                    return;
                }
            }
            stop();
            BDebug.d(TAG, "mStarted虽然为true但是连接已经断了,需要重新重新创建");
        }

        // 创建一个 MQTT connection
        connect();
       
        // 注册网络状态监听
        registerReceiver(mConnectivityChanged, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    /**
     * 关闭连接。反注册一些监听，取消闹钟，设置状态为false
     */
    private synchronized void stop() {
        if (mStarted == false) {
            return;
        }
        setStarted(false);

        unregisterReceiver(mConnectivityChanged);

        cancelReconnect();

        if (mConnection != null) {
            mConnection.disconnect();
            mConnection = null;
        }
        BDebug.d(TAG, "进入stop()关掉一个该关闭的" + mStarted);
    }

    /**
     * 创建连接
     */
    private synchronized void connect() {
        BDebug.d(TAG, "连接服务器的地址是---"+MQTT_HOST);
        try {
            if (mConnection != null) {
                mConnection.disconnect();
                mConnection = null;
            }
            mConnection = new MQTTConnection(MQTT_HOST, topics);
        } catch (MqttException e) {
            BDebug.d(TAG, "创建一个新连接时发生了异常----" +e.getLocalizedMessage());
            if (isNetworkAvailable()) {
                scheduleReconnect(mStartTime);
            }
        }
        setStarted(true);
    }
/**
 * 发送保持连接
 */
    private synchronized void keepAlive() {
        try {
            // Send a keep alive, if there is a connection.
            if (mStarted == true && mConnection != null) {
                mConnection.sendKeepAlive();
            }else{
                BDebug.d(TAG,"没异常但是也没能保持连接");
            }
        } catch (MqttException e) {
            BDebug.d(TAG, "保持链接时发生了异常"+ (e.getMessage() != null ? e.getMessage() : "NULL"));

            mConnection.disconnect();
            mConnection = null;
            cancelReconnect();
        }
    }

    // 注册一个闹钟，每个28分钟发送一次keepAlive（）
    private void startKeepAlives() {
        Intent i = new Intent();
        i.setClass(this, PushService.class);
        i.setAction(ACTION_KEEPALIVE);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + KEEP_ALIVE_INTERVAL,
                KEEP_ALIVE_INTERVAL, pi);
    }

    // 取消上面的闹钟
    private void stopKeepAlives() {
        Intent i = new Intent();
        i.setClass(this, PushService.class);
        i.setAction(ACTION_KEEPALIVE);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmMgr.cancel(pi);
    }

    /**
     * 定义一个闹钟，在有网络发生异常时尝试重新连接
     * 
     * @param startTime
     */
    public void scheduleReconnect(long startTime) {

        long interval = mPrefs.getLong(PREF_RETRY, INITIAL_RETRY_INTERVAL);
        long now = System.currentTimeMillis();
        long elapsed = now - startTime;

        if (elapsed < interval) {
            interval = Math.min(interval * 4, MAXIMUM_RETRY_INTERVAL);
        } else {
            interval = INITIAL_RETRY_INTERVAL;
        }
        BDebug.d(TAG, "MQTT连接发生异常了，正在尝试重连----" + interval + "ms.");
        mPrefs.edit().putLong(PREF_RETRY, interval).commit();
        // 注册闹钟
        Intent i = new Intent();
        i.setClass(this, PushService.class);
        i.setAction(ACTION_RECONNECT);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, now + interval, pi);
    }

    /**
     * 取消重连闹钟
     */
    public void cancelReconnect() {
        Intent i = new Intent();
        i.setClass(this, PushService.class);
        i.setAction(ACTION_RECONNECT);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmMgr.cancel(pi);
    }

    /**
     * 监听到网络状态变为true时是否重连
     */
    private synchronized void reconnectIfNecessary() {
        if (mStarted == true && mConnection == null) {
            BDebug.d(TAG, "监听到网络状态变为true,mStarted==true,mConnection == null,开始重新创建连接");
           //connect();
            //直接通过上面connect()方法一直会失败，暂时找不到原因，故通过广播形式再次开启
            Intent intent = new Intent();  
            intent.setAction("com.gome.ecmall.push.service.restart");  
            PushService.this.sendBroadcast(intent);  
        } else {
            BDebug.d(TAG, "监听到网络状态变为true,mStarted==" + mStarted + ",mConnection" + mConnection);
        }
    }

    /**
     * 网络监听广播
     */
    private BroadcastReceiver mConnectivityChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            NetworkInfo info = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            boolean hasConnectivity = (info != null && info.isConnected()) ? true : false;
            BDebug.d(TAG, "检测到网络状态发生改变了----" + hasConnectivity);
            if (hasConnectivity) {
                PushKeepAliveTimeDao dao = new PushKeepAliveTimeDao(PushService.this);
                String isSendNetState = dao.getIsSendNetState();
                BDebug.d(TAG, "service中是否发送网络状态" + isSendNetState);
                if ("Y".equalsIgnoreCase(isSendNetState)) {
                    if (mPrefs != null) {
                        try {
                            Time t1 = new Time();
                            t1.setToNow();
                            t1.set(t1.monthDay, t1.month, t1.year);
                            String tt = mPrefs.getString("is_send_net_time", "");
                            if (TextUtils.isEmpty(tt)) {
                                BDebug.d(TAG, "service中发送了网络状态");
                                PushUtils.AsynSendNetState(PushService.this);
                            } else {
                                String bb[] = tt.split(",");
                                Time t2 = new Time();
                                t2.set(Integer.parseInt(bb[2]), Integer.parseInt(bb[1]), Integer.parseInt(bb[0]));
                                if (t1.after(t2)) {
                                    PushUtils.AsynSendNetState(PushService.this);
                                    BDebug.d(TAG, "service中发送了网络状态");
                                } else {
                                    BDebug.d(TAG, "service中时间是同一天不发送网络状态");
                                }
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                    }

                }
                reconnectIfNecessary();

            } else if (mConnection != null) {
                mConnection.disconnect();
                cancelReconnect();
                mConnection = null;
            }
        }
    };

    /**
     * 显示通知到通知栏
     * 
     * @param title
     * @param body
     * @param sp
     * @throws JSONException
     */
    private void showNotification(String title, String body, SkipParameters sp) throws JSONException {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        long when = System.currentTimeMillis(); // 通知产生的时间，会在通知信息里显示
        Notification notification = new Notification(R.drawable.icon, title, when);
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        Intent notificationIntent = new Intent(this, GomeEMallActivity.class);// 点击该通知后要跳转的Activity
        int st = 0;
        try {
            st = Integer.parseInt(sp.getSkipType());
        } catch (NumberFormatException e1) {
            e1.printStackTrace();
        }
        switch (st) {
        case 0:// 首页
        case 3:// 限时抢购列表
        case 4:// 热门促销列表
        case 5:// 团购列表
            notificationIntent.putExtra("st", st);
            notificationIntent.putExtra("title", title);
            break;
        case 1:// 商城公告
        case 6:// 商品详情
            notificationIntent.putExtra("st", st);
            notificationIntent.putExtra("newsId", sp.getNewId());
            notificationIntent.putExtra("title", title);
            break;
        case 2:// 活动列表
            notificationIntent.putExtra("st", st);
            notificationIntent.putExtra("activityId", sp.getActiveId());
            notificationIntent.putExtra("activityType", sp.getActiveType());
            notificationIntent.putExtra("title", title);
            break;
        default:
            break;
        }
        notificationIntent.putExtra("messageId", sp.getMessageId());
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, notificationNB, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        // 自定义通知栏
        notification.contentIntent = contentIntent;
        RemoteViews rv = new RemoteViews(getPackageName(), R.layout.notification_layout);
        try {
            ContentResolver cv = PushService.this.getContentResolver();
            String strTimeFormat = android.provider.Settings.System.getString(cv,
                    android.provider.Settings.System.TIME_12_24);
            SimpleDateFormat formatter = null;
            if (strTimeFormat.equals("24")) {
                formatter = new SimpleDateFormat("HH:mm");
            } else {
                formatter = new SimpleDateFormat("hh:mm");
            }
            Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
            String strTime = formatter.format(curDate);
            rv.setTextViewText(R.id.tv_time, strTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        rv.setTextViewText(R.id.tv_title, title);
        rv.setTextViewText(R.id.tv_body, body);
        notification.contentView = rv;
        // 把Notification传递给NotificationManager
        mNotificationManager.notify(notificationNB, notification);
        notificationNB++;
        // 存进历史库
        if (pushHistoryDao != null) {
            pushHistoryDao.addPushHistory(sp.getMessageId());
        }
        // 消息推送发送到达反馈
        PushUtils.AsynFeedbackArrivedMessage(getApplicationContext(), sp.getMessageId(), "", "2");
    }

    /**
     * 检查网络是否可用
     * 
     * @return
     */
    private boolean isNetworkAvailable() {
        NetworkInfo info = mConnMan.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        return info.isConnected();
    }

    // This inner class is a wrapper on top of MQTT client.
    private class MQTTConnection implements MqttSimpleCallback {
        public MQTTConnection(String brokerHostName, String[] initTopic) throws MqttException {
            mqttClient = new MqttClient(brokerHostName);
            // 绑定唯一号
            clientID = Push.getClientId(PushService.this);
            BDebug.d(TAG, "建立连接时使用的tokenId---" + clientID);
            try {
                PushKeepAliveTimeDao dao = new PushKeepAliveTimeDao(PushService.this);
                String keepAlivetime = dao.getkeepAliveTime();
                MQTT_KEEP_ALIVE = Short.parseShort(keepAlivetime);
                BDebug.d(TAG, "建立连接时获取的心跳时间---" + MQTT_KEEP_ALIVE);
            } catch (Exception e) {
                BDebug.d(TAG, "建立连接时获取心跳时间发生异常---" + e.getMessage());
                e.printStackTrace();
            }
            mqttClient.connect(clientID, MQTT_CLEAN_START, MQTT_KEEP_ALIVE);

            BDebug.d(TAG, "建立连接时使用的心跳时间---" + MQTT_KEEP_ALIVE);
            // 注册此MQTT连接能够收到消息
            mqttClient.registerSimpleHandler(this);

            // 订阅消息主题
            subscribeToTopic(initTopic);

            mStartTime = System.currentTimeMillis();
            // Star the keep-alives
            startKeepAlives();
        }

        /**
         * 端开连接
         */
        public void disconnect() {
            try {
                stopKeepAlives();
                mqttClient.terminate();
            } catch (Exception e) {
                BDebug.d(TAG,"断开连接时发生异常"+ (e.getMessage() != null ? e.getMessage() : " NULL"));
            }
        }

        /*
         * 注册消息接收主题
         */
        private void subscribeToTopic(String[] topicName) throws MqttException {

            if ((mqttClient == null) || (mqttClient.isConnected() == false)) {
                BDebug.d(
                        TAG,
                        "注册主题时发生异常---mqttClient==" + mqttClient + "mqttClient.isConnected()=="
                                + (mqttClient==null?"NULL":mqttClient.isConnected()));
            } else {
                String devidTopic = Push.getClientId(PushService.this);
                topicName = new String[] { "gomeshop", MobileDeviceUtil.getInstance(getApplicationContext()).getVersonCode(),
                        devidTopic };
                mqttClient.subscribe(topicName, MQTT_QUALITIES_OF_SERVICE);
                if(topicName!=null&&topicName.length==3){
                    BDebug.d(TAG, "建立连接时注册的主题---" + topicName[0] + topicName[1] + topicName[2]);
                }
            }
        }

        /*
         * 保持心跳
         */
        private void publishToTopic(String topicName, String message) throws MqttException {
            if ((mqttClient == null) || (mqttClient.isConnected() == false)) {
                BDebug.d(
                        TAG,
                        "保持心跳时发生异常---mqttClient==" + mqttClient + "mqttClient.isConnected()=="
                                + (mqttClient==null?"NULL":mqttClient.isConnected()));
            } else {
                mqttClient.publish(topicName, message.getBytes(), MQTT_QUALITY_OF_SERVICE, MQTT_RETAINED_PUBLISH);
            }
        }

        /*
         * 检查连接是否还存在.
         */
        public void connectionLost() throws Exception {
            BDebug.d(TAG, "连接已经挂掉了");
            stopKeepAlives();
            mConnection = null;
            if (isNetworkAvailable() == true) {
                BDebug.d(TAG, "连接已经挂掉了,此时网络可用，尝试重新连接");
                reconnectIfNecessary();
            }
        }

        /*
         * 消息到达客户端
         */
        public void publishArrived(String topicName, byte[] payload, int qos, boolean retained) {
            String reData = new String(payload);
            if (TextUtils.isEmpty(reData)) {
                return;
            }
            try {
                reData = DESUtils.decrypt(reData, Constants.URL_PUSH_KEY);
                JSONObject json = new JSONObject(reData);
                SkipParameters skipParameters = Push.parseSkipParameters(json);
                if (skipParameters != null && !TextUtils.isEmpty(skipParameters.getMessageId())) {
                    String messageId = skipParameters.getMessageId();
                    if (pushHistoryDao != null) {
                        ArrayList<String> messageIds = pushHistoryDao.getPartPushHistory();
                        if (!messageIds.contains(messageId)) {
                            showNotification(json.optString("title"), json.optString("body"), skipParameters);
                        }
                    } else {
                        showNotification(json.optString("title"), json.optString("body"), skipParameters);
                    }

                }
            } catch (Exception e) {
            }

        }

        public void sendKeepAlive() throws MqttException {
            BDebug.d(TAG, "保持心跳连接");
            publishToTopic(MQTT_CLIENT_ID + "/keepalive", "keepalive");
        }
    }

}