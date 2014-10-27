package com.gome.ecmall.home;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.gome.ecmall.framework.AbsActivityGroup;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.mygome.MyGomeActivity;
import com.gome.ecmall.push.MalarmManagers;
import com.gome.ecmall.push.Push;
import com.gome.ecmall.push.PushService;
import com.gome.ecmall.push.PushUtils;
import com.gome.ecmall.util.AppMeasurementUtils;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.ecmall.util.PreferenceUtils;
import com.gome.ecmall.util.VersionUpdateUtils;
import com.gome.eshopnew.R;

/**
 * 国美商城主页面，控制主页面的五个切换按钮和对应的页面
 * 
 * @author zhouxiaoming
 * 
 */
public class GomeEMallActivity extends AbsActivityGroup {

    public static final String TAG = "GomeEMallActivity";
    private int[] bottomBtnIds = new int[] { R.id.main_group_bottom_btn_home, R.id.main_group_bottom_btn_category,
            R.id.main_group_bottom_btn_search, R.id.main_group_bottom_btn_shopcart, R.id.main_group_bottom_btn_more };

    @SuppressWarnings("unchecked")
    private Class<? extends Activity>[] classes = new Class[] { HomeActivity.class, CategoryActivity.class,
            SearchActivity.class, ShoppingCartActivity.class, MyGomeActivity.class };

    @Override
    protected int getGroupLayoutId() {
        return R.layout.main_frame_layout;
    }

    @Override
    protected int[] getBottomBtnIds() {
        return bottomBtnIds;
    }

    @Override
    public Class<? extends Activity>[] getClasses() {
        return classes;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // BDebug.d(TAG, "onKeyDown------>");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Activity currentActivity = getLocalActivityManager().getCurrentActivity();
            if (currentActivity != null && currentActivity instanceof AbsSubActivity) {
                AbsSubActivity absSubActivity = (AbsSubActivity) currentActivity;
                // 获得AbsSubActivity实例的Start Intent，查看启动它的是不是AbsSubActivity
                String subTag = absSubActivity.getIntent().getStringExtra("fromSubActivity");
                if (subTag != null) {
                    absSubActivity.goback();
                    return true;
                } else {
                    // 是每个Tab页面最外层的界面，无法再返回
                    // BDebug.i(TAG, "have no requestActivity--------->");
                    // 子Activity是主界面的Activity,弹出退出
                    if (currentActivity instanceof HomeActivity) {
                        if (HomeActivity.etInput != null && HomeActivity.btnSearch != null
                                && HomeActivity.btnSearch.isShown()) {
                            CommonUtility.hideSoftKeyboard(this, HomeActivity.etInput);
                            return false;
                        } else {
                            CommonUtility.showConfirmDialog(this, "退出", "确定退出国美在线吗?", "确定",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            GomeEMallActivity.this.finish();
                                            android.os.Process.killProcess(android.os.Process.myPid());
                                        }
                                    }, "取消", null);
                        }
                    } else {
                        // 其他标签下下面的主Tab首界面,跳到主页界面
                        if (currentActivity instanceof SearchActivity) {
                            if (SearchActivity.etInput != null && SearchActivity.btnSearch != null
                                    && SearchActivity.btnSearch.isShown()) {
                                CommonUtility.hideSoftKeyboard(this, SearchActivity.etInput);
                                return false;
                            }
                        }
                        switchMainTab(0);
                    }
                    return true;
                }
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private int st;
    private GlobalApplication applcation;
    private String oldSplashPath;
    private int sendCount;// 到达后发送到达数据的次数

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ShoppingCartActivity.getTotalShoppingNumber();
        VersionUpdateUtils versonUpdateUtils = new VersionUpdateUtils(this);
        versonUpdateUtils.versonUpdate("N");
        applcation = (GlobalApplication) getApplication();
        // 判断是否是点击推送通知进来的
        String messageId = getIntent().getStringExtra("messageId");
        st = getIntent().getIntExtra("st", -1);
        switch (st) {
        case 0://首页
            BDebug.d("push_arrive", "首页");
            String title = getIntent().getStringExtra("title");
            PushUtils.AsynFeedbackArrivedMessage(GomeEMallActivity.this,messageId,title,"3");
            break;
        case 3://限时抢购列表
        case 4://热门促销列表
        case 5://团购列表
            HomeActivity.title = getIntent().getStringExtra("title");
            HomeActivity.st = st;
            HomeActivity.messageId = messageId;
            //AsynWatchMessage(messageId, titles);
            break;
        case 1://商城公告
        case 6://商品详情
            HomeActivity.newId = getIntent().getStringExtra("newsId");
            HomeActivity.st = st;
            HomeActivity.messageId = messageId;
            HomeActivity.title = getIntent().getStringExtra("title");
            break;
        case 2://活动列表
            HomeActivity.st = st;
            HomeActivity.activityId = getIntent().getStringExtra("activityId");
            HomeActivity.activityType = getIntent().getStringExtra("activityType");
            HomeActivity.title = getIntent().getStringExtra("title");
            HomeActivity.messageId = messageId;
            break;
        default:
            break;
        }
        // 开启消息推送服务
        PreferenceUtils.getInstance(getApplicationContext());
        String is_register_success = PreferenceUtils.getStringValue("push_is_register_success", "");
        if (!"Y".equals(is_register_success)) {
            AsynRegisterPushMessage();
        }
        MalarmManagers.AlarmManagers(this, 1);// 先清除闹钟

        String notification_set = PreferenceUtils.getStringValue("notification_set", "");
        if (!"N".equals(notification_set)) {
            BDebug.d("liuyang", "进入应用服务启动");
            PushService.actionStart(getApplicationContext());//重新开启服务
        }
        AsynGetKeepAliveTime();// 获取消息推送的心跳时间，并设置

        PreferenceUtils.getInstance(getApplicationContext());
        PreferenceUtils.setFirstUse(false);

        File[] files = getSplashCachePath(GomeEMallActivity.this).listFiles();
        if (files.length > 0) {
            oldSplashPath = files[0].getPath();
        }
        URL url = null;
        try {
            url = new URL(applcation.appStartImageURL);

        } catch (MalformedURLException e) {
            // e.printStackTrace();
        }

        if (!TextUtils.isEmpty(applcation.appStartImageURL)
                && (!url.getPath().equals(PreferenceUtils.getLogoUrl()) || TextUtils.isEmpty(oldSplashPath))) {
            getNewSplashPicture(applcation.appStartImageURL);
        }
    }

    /**
     * 为消息推送心跳时间获取接口
     */
    public void AsynGetKeepAliveTime() {
        MalarmManagers.AlarmManagers(GomeEMallActivity.this, 0);// 设置唤醒服务闹钟
        if (!NetUtility.isNetworkAvailable(GomeEMallActivity.this)) {
            return;
        }
        new AsyncTask<Object, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Object... params) {
               String response = NetUtility.sendHttpRequestByGet(Constants.URL_PUSH_KEEP_ALIVE_TIME);
               //String response = "{\"isSuccess\":\"Y\",\"keepAliveTime\":\"10\",\"isSendNetState\":\"N\"}";
                BDebug.d("liuyang", response);
                if (NetUtility.NO_CONN.equals(response)) {
                    return false;
                }
                return Push.parseKeepAliveTime(GomeEMallActivity.this, response);
            }

            protected void onPostExecute(Boolean result) {
                
                if(result){
                    PreferenceUtils.getInstance(getApplicationContext());
                    String notification_set = PreferenceUtils.getStringValue("notification_set", "");
                    if (!"N".equals(notification_set)) {
                        BDebug.d("liuyang", "心跳时间改变服务重启");
                        PushService.actionStop(getApplicationContext());// 停止服务
                        PushService.actionStart(getApplicationContext());//重新开启服务
                    }
                }
                
            }
        }.execute();
    }

    private void getNewSplashPicture(final String imageUrl) {
        new Thread() {
            public void run() {
                try {
                    URL url = new URL(imageUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    // InputStream is = (new URL(imageUrl).openStream());
                    Bitmap temp_bitmap = BitmapFactory.decodeStream(is);
                    String imgName = cutOutName(imageUrl);
                    File splashFile = new File(getSplashCachePath(GomeEMallActivity.this), imgName);
                    splashFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(splashFile);
                    temp_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.close();
                    if (oldSplashPath != null && oldSplashPath.length() > 0) {
                        File oldSplash = new File(oldSplashPath);
                        if (oldSplash.exists()) {
                            oldSplash.delete();
                        }
                        oldSplashPath = null;
                    }
                    PreferenceUtils.setLogoUrl(url.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            };
        }.start();

    }

    /**
     * 从url中截取图片名
     * 
     * @param imgUrl
     * @return
     */
    public String cutOutName(String imgUrl) {
        String imgName = null;
        if (imgUrl != null) {
            int lastFlag = imgUrl.lastIndexOf("/");
            imgName = imgUrl.substring(lastFlag + 1);
        }

        return imgName;

    }

    public static File getSplashCachePath(Context context) {
        File file = new File(context.getCacheDir().getAbsolutePath() + "/splash/");
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    /**
     * 为消息推送注册信息
     */
    public void AsynRegisterPushMessage() {
        if (!NetUtility.isNetworkAvailable(GomeEMallActivity.this)) {
            return;
        }
        new AsyncTask<Object, Void, Void>() {
            @Override
            protected Void doInBackground(Object... params) {
                String request = Push.createRegisterJson(GomeEMallActivity.this);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_PUSH_REGISTER, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return Push.parseRegister(GomeEMallActivity.this, response);
            }
        }.execute();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        // 判断是否是点击推送通知进来的
        String messageId = intent.getStringExtra("messageId");
        st = intent.getIntExtra("st", -1);
        switch (st) {
        case 0://首页
            BDebug.d("push_arrive", "首页");
            String title = intent.getStringExtra("title");
            PushUtils.AsynFeedbackArrivedMessage(GomeEMallActivity.this,messageId,title,"3");
            break;
        case 3://限时抢购列表
        case 4://热门促销列表
        case 5://团购列表
            HomeActivity.title = intent.getStringExtra("title");
            HomeActivity.st = st;
            HomeActivity.messageId = messageId;
            //AsynWatchMessage(messageId, titles);
            break;
        case 1://商城公告
        case 6://商品详情
            HomeActivity.newId = intent.getStringExtra("newsId");
            HomeActivity.st = st;
            HomeActivity.messageId = messageId;
            HomeActivity.title = intent.getStringExtra("title");
            break;
        case 2://活动列表
            HomeActivity.st = st;
            HomeActivity.activityId = intent.getStringExtra("activityId");
            HomeActivity.activityType = intent.getStringExtra("activityType");
            HomeActivity.title = intent.getStringExtra("title");
            HomeActivity.messageId = messageId;
            break;
        default:
            break;
        }
        super.onNewIntent(intent);
    }

    /**
     * 点击消息时先关闭其他的
     */
    public void pushCloseAll() {

        boolean isOut = true;
        while (isOut) {
            Activity currentActivity = getLocalActivityManager().getCurrentActivity();
            if (currentActivity != null && currentActivity instanceof AbsSubActivity) {
                AbsSubActivity absSubActivity = (AbsSubActivity) currentActivity;
                // 获得AbsSubActivity实例的Start Intent，查看启动它的是不是AbsSubActivity
                String subTag = absSubActivity.getIntent().getStringExtra("fromSubActivity");
                if (subTag != null && !"HomeActivity".equals(subTag)) {
                    absSubActivity.goback();
                } else {
                    if (currentActivity instanceof HomeActivity) {
                        isOut = false;
                    } else {
                        switchMainTab(0);
                    }
                }
            } else if (currentActivity != null && !(currentActivity instanceof AbsSubActivity)) {
                currentActivity.finish();
            } else {
                isOut = false;
            }
        }

    }
}