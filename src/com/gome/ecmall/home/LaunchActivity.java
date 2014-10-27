package com.gome.ecmall.home;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.LocationListener;
import com.gome.ecmall.bean.AbleLoginEntity;
import com.gome.ecmall.bean.AlipayUserInfo;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.home.login.AlipayAutoLoginTask;
import com.gome.ecmall.home.login.AutoLoginTask;
import com.gome.ecmall.home.login.AutoLoginTask.AutoLoginListener;
import com.gome.ecmall.home.more.UseCourseActivity;
import com.gome.ecmall.util.AppMeasurementUtils;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.ecmall.util.NetUtility;
import com.gome.ecmall.util.PreferenceUtils;
import com.gome.ecmall.util.Tools;
import com.gome.eshopnew.R;

public class LaunchActivity extends Activity {
    private static final String TAG = "LaunchActivity";
    private LocationListener mLocationListener = null;
    private GlobalApplication application;
    private AlipayUserInfo alipayUserInfo;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                CommonUtility.showToast(LaunchActivity.this,
                        LaunchActivity.this.getString(R.string.update_shortcut_key));
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (GlobalApplication) getApplication();
        alipayUserInfo = AlipayUserInfo.getInstance();
        Intent intent = getIntent();
        String alipay_user_id = intent.getStringExtra("alipay_user_id");
        String auth_code = intent.getStringExtra("auth_code");
        String app_id = intent.getStringExtra("app_id");
        String version = intent.getStringExtra("version");
        String alipay_client_version = intent.getStringExtra("alipay_client_version");
        String source = intent.getStringExtra("source");

        BDebug.e(TAG, "alipay_user_id-->" + alipay_user_id + "***auth_code-->" + auth_code);
        BDebug.e(TAG, "app_id-->" + app_id + "***version-->" + version);
        BDebug.e(TAG, "alipay_client_version-->" + alipay_client_version + "***source-->" + source);
        if (!TextUtils.isEmpty(alipay_user_id) && !TextUtils.isEmpty(auth_code) && !TextUtils.isEmpty(app_id)
                && !TextUtils.isEmpty(version) && !TextUtils.isEmpty(alipay_client_version)) {
            if (!TextUtils.isEmpty(source)) {// source字段不为空，支付宝钱包7.2以后有该字段
                if (source.equals("alipay_wallet")) {
                    setAlipayConfig(alipay_user_id, auth_code);
                }
            } else {// 支付宝钱包7.2以前版本的没有source字段
                setAlipayConfig(alipay_user_id, auth_code);
            }
        }
        isAbleLogin();

        new Thread() {
            public void run() {

                // ----
                PreferenceUtils.getInstance(getApplicationContext());
                // ----增量升级判断----
                String diffVersion = PreferenceUtils.getDiffUpdate();
                if (!TextUtils.isEmpty(diffVersion)
                        && !MobileDeviceUtil.getInstance(getApplicationContext()).getVersonCode().equalsIgnoreCase(diffVersion)) {
                    PreferenceUtils.setDiffUpdate("");
                }
                BDebug.e("增量升级", String.format("version:%s", PreferenceUtils.getDiffUpdate()));

                String channelName = MobileDeviceUtil.getInstance(getApplicationContext()).getChannalName();
                BDebug.e("channelName", channelName + "****" + alipayUserInfo.isAlipayLogin());
                if (channelName.equalsIgnoreCase("A302")) {// 国美官方包不创建快捷方式 A302代码为支付宝渠道包

                } else {// 渠道包创建桌面快捷方式
                    if (!alipayUserInfo.isAlipayLogin()) {// 非支付宝钱包用户，第一次启动创建、更新桌面快捷启动方式
                        createShortcut();
                    }

                }

            }
        }.start();

        setContentView(R.layout.launch);
        initSplashBg();// 初始化splash背景
        init();
        InitBaiDu();
    }

    private void isAbleLogin() {
        new AsyncTask<Void, Void, AbleLoginEntity>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                application.connect = GlobalApplication.CONNECT_DEFAUL;
            }

            @Override
            protected AbleLoginEntity doInBackground(Void... params) {
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_GLOBAL_CONFIG, "");
                return AbleLoginEntity.parseJson(response);
            }

            @Override
            protected void onPostExecute(AbleLoginEntity result) {
                super.onPostExecute(result);
                application.isAlwaysCaptcha = result.isAlwaysCaptcha();
                GlobalApplication.isSupportedHttps = result.isSupportedHttps();
                application.appStartImageURL = result.getAppStartImageURL();

                // 自动登录
                PreferenceUtils.getInstance(getApplicationContext());
                String encryptUser = PreferenceUtils.getStringValue(GlobalConfig.USER_NAME, "");
                String encryptPwd = PreferenceUtils.getStringValue(GlobalConfig.PASSWORD, "");
                if (PreferenceUtils.isAutoLogin() && !TextUtils.isEmpty(encryptUser) && !TextUtils.isEmpty(encryptPwd)) {// 可以进行自动登录
                    autoLogin();
                } else {// 如果是支付宝钱包用户则支付宝用户快捷登录
                    if (alipayUserInfo.isAlipayLogin())
                        // 没有用户登录时，支付宝钱包用户登录
                        new AlipayAutoLoginTask(LaunchActivity.this).execute();
                }

                if (result.isConnectState()) {
                    application.connect = GlobalApplication.CONNECT_SUCCESS;
                } else {
                    application.connect = GlobalApplication.CONNECT_FAIL;
                }
                File[] files = getSplashCachePath(getApplicationContext()).listFiles();
                if (files.length > 0) {
                    application.oldSplashPath = files[0].getPath();
                }
                if (TextUtils.isEmpty(application.appStartImageURL)) {
                    return;
                }
                URL url = null;
                try {
                    url = new URL(application.appStartImageURL);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                if (!TextUtils.isEmpty(application.appStartImageURL) && url != null) {
                    if (!url.getPath().equals(PreferenceUtils.getLogoUrl())
                            || TextUtils.isEmpty(application.oldSplashPath)) {
                        getNewSplashPicture(application.appStartImageURL);
                    }

                }
            }

        }.execute();
    }

    public void autoLogin() {
        AutoLoginTask autoLoginTask = new AutoLoginTask(this.getApplicationContext());
        autoLoginTask.setAutoLoginListener(new AutoLoginListener() {

            @Override
            public void callBack(String state) {
                if (alipayUserInfo.isAlipayLogin())
                    // 有国美账号登录的只绑定支付宝钱包用户的token,没有国美账号登录的则以支付宝钱包用户账号登录
                    new AlipayAutoLoginTask(LaunchActivity.this).execute();
            }
        });
        autoLoginTask.execute();
    }

    private void getNewSplashPicture(final String imageUrl) {
        new Thread() {
            public void run() {
                HttpURLConnection conn = null;
                InputStream is = null;
                Bitmap temp_bitmap = null;
                FileOutputStream fos = null;
                try {
                    URL url = new URL(imageUrl);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    is = conn.getInputStream();
                    // InputStream is = (new URL(imageUrl).openStream());
                    temp_bitmap = BitmapFactory.decodeStream(is);
                    String imgName = cutOutName(imageUrl);
                    File splashFile = new File(getSplashCachePath(getApplicationContext()), imgName);
                    splashFile.createNewFile();
                    fos = new FileOutputStream(splashFile);
                    temp_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.close();
                    if (application.oldSplashPath != null && application.oldSplashPath.length() > 0) {
                        File oldSplash = new File(application.oldSplashPath);
                        if (oldSplash.exists()) {
                            oldSplash.delete();
                        }
                        application.oldSplashPath = null;
                    }
                    PreferenceUtils.setLogoUrl(url.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {// 释放资源
                    /** 关闭输入流*/
                    if (is != null)
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    /** 关闭网络连接*/
                    if (conn != null)
                        conn.disconnect();
                    /** 释放bitmap对象*/
                    if (temp_bitmap != null)
                        temp_bitmap.recycle();
                    /** 关闭输出流*/
                    if (fos != null)
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

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

    /**
     * 设置支付宝钱包登录配置参数
     * 
     * @param alipayAuthCode
     * @param alipayUserId
     */
    private void setAlipayConfig(String alipayUserId, String alipayAuthCode) {
        alipayUserInfo.setAlipayLogin(true);
        alipayUserInfo.setAlipayAuthCode(alipayAuthCode);
        alipayUserInfo.setAlipayUserId(alipayUserId);
    }

    /**
     * 创建、更新桌面快捷方式
     */
    private void createShortcut() {
        String name = PreferenceUtils.getStringValue("versionName", "");
        String versionName = MobileDeviceUtil.getInstance(getApplicationContext()).getVersonCode();
        boolean flag = PreferenceUtils.getBoolValue("Launcher", true);
        if (!name.equals(versionName)) {
            if (Tools.hasShortcut(LaunchActivity.this)) {
                Tools.delShortcut(LaunchActivity.this);
                mHandler.sendMessage(mHandler.obtainMessage(0));
                Tools.addShortCut(LaunchActivity.this, "com.gome.eshopnew");
            } else {
                if (flag) {
                    Tools.addShortCut(LaunchActivity.this, "com.gome.eshopnew");
                    PreferenceUtils.setBooleanValue("Launcher", false);
                }
            }
            PreferenceUtils.setStringValue("versionName", versionName);
        }
    }

    private void initSplashBg() {
        try {
            File[] files = getSplashCachePath(LaunchActivity.this).listFiles();
            if (files.length > 0) {
                if (files[0].isFile()) {
                    File splashFile = new File(files[0].getPath());
                    if (splashFile.exists()) {
                        Bitmap bmp = BitmapFactory.decodeFile(splashFile.getAbsolutePath());
                        if (bmp != null) {
                            setSplashBitmap(bmp);
                            return;
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 没有新的Splash页，使用默认图
        setSplashBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.launch));
    }

    public static File getSplashCachePath(Context context) {
        File file = new File(context.getCacheDir().getAbsolutePath() + "/splash/");
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    private void setSplashBitmap(Bitmap bmp) {
        // 针对不同分辨率的屏幕做Splash的适配
        Bitmap scaledBitmap = sacleBitmap(getApplicationContext(), bmp);
        ImageView v = ((ImageView) findViewById(R.id.imageView1));
        v.setImageBitmap(scaledBitmap);
    }

    /**
     * 获取适应屏幕大小的图
     */
    public static Bitmap sacleBitmap(Context context, Bitmap bitmap) {
        int screenWidth = MobileDeviceUtil.getInstance(context.getApplicationContext()).getScreenWidth();
        int screenHeight = MobileDeviceUtil.getInstance(context.getApplicationContext()).getScreenHeight();
        Bitmap scaledBitmap = null;
        try {
            scaledBitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, screenHeight, false);
        } catch (OutOfMemoryError e) {
        }
        return scaledBitmap;
    }

    private void InitBaiDu() {
        GlobalApplication app = (GlobalApplication) this.getApplication();
        if (app.mBMapMan == null) {
            app.mBMapMan = new BMapManager(getApplication());
            app.mBMapMan.init(app.mStrKey, new GlobalApplication.MyGeneralListener());
        }
        app.mBMapMan.start();
        // 注册定位事件
        if (mLocationListener == null) {
            mLocationListener = new LocationListener() {
                Thread myStartThread;

                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        final double log = location.getLongitude();
                        final double lat = location.getLatitude();

                        BDebug.e("=gps=log=baidu=", "" + log);
                        BDebug.e("=gps=lat=baidu=", "" + lat);

                        GlobalConfig.getInstance().setLog(log);
                        GlobalConfig.getInstance().setLat(lat);
                        // 启动线程去网上获取城市
                        if (myStartThread == null) {
                            myStartThread = new Thread() {
                                public void run() {
                                    CommonUtility.reverseGeocodeBaidu(lat, log);
                                };
                            };
                            myStartThread.start();
                        } else {
                            myStartThread = null;
                            myStartThread = new Thread() {
                                public void run() {
                                    CommonUtility.reverseGeocodeBaidu(lat, log);
                                };
                            };
                            myStartThread.start();
                        }
                    }
                }
            };
        }
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            try {
                boolean isFirstUse = PreferenceUtils.isFirstUse();
                Thread.sleep(2000);
                // 如果是第一次运行，则跳转到应用教程，否则跳转到首页
                // isFirstUse=false;//不进入教程!!!!!!!!!!!
                if (isFirstUse) {
                    AppMeasurementUtils appMeasurementUtils = new AppMeasurementUtils(LaunchActivity.this);
                    appMeasurementUtils.getUrl(getString(R.string.appMeas_intalPage),
                            getString(R.string.appMeas_intalPage), getString(R.string.appMeas_intalPage),
                            getString(R.string.appMeas_intalPage), "", "", "event7,event9", "", "", "", "", "", "", "",
                            "", "", null);
                    Intent useCourseIntent = new Intent(getApplicationContext(), UseCourseActivity.class);
                    startActivity(useCourseIntent);
                    finish();

                    /*
                     * Intent intent = new Intent(getApplicationContext(), GomeEMallActivity.class);
                     * startActivityForResult(intent, 1); finish();
                     */
                } else {
                    AppMeasurementUtils appMeasurementUtils = new AppMeasurementUtils(LaunchActivity.this);
                    appMeasurementUtils.getUrl(getString(R.string.appMeas_intalPage),
                            getString(R.string.appMeas_intalPage), getString(R.string.appMeas_intalPage),
                            getString(R.string.appMeas_intalPage), "", "", "event7", "", "", "", "", "", "", "", "",
                            "", null);
                    Intent intent = new Intent(getApplicationContext(), GomeEMallActivity.class);
                    startActivityForResult(intent, 1);
                    finish();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 载入页面时做初始化工作,检查网络状态，
     */
    private AlertDialog mAlertDialog;

    private void init() {
        PreferenceUtils.getInstance(getApplicationContext());
        boolean isFirstRun = PreferenceUtils.isFirstRun();

        boolean hasCache = application.hasCache();
        PreferenceUtils.setFirstRun(false);
        if (!NetUtility.isNetworkAvailable(this)) {
            if (isFirstRun || !hasCache) {// 第一次登录 或是 不是第一次登陆并且没有缓存
                mAlertDialog = CommonUtility.showConfirmDialog(LaunchActivity.this, getString(R.string.init_err),
                        getString(R.string.check_network_setting), getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                                return;
                            }
                        }, getString(R.string.setting_network), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivityForResult(new Intent(Settings.ACTION_SETTINGS), 1);
                            }
                        });
            } else {
                startActivityForResult(new Intent(LaunchActivity.this, GomeEMallActivity.class), 1);
                finish();
            }
        } else {
            launch();
        }
    }

    public void launch() {
        new Thread(r).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.ACTION_DOWN) {
            startActivity(new Intent(getApplicationContext(), LaunchActivity.class));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (NetUtility.isNetworkAvailable(getApplicationContext())) {
            launch();
        } else {
            if (LaunchActivity.this != null && !LaunchActivity.this.isFinishing() && mAlertDialog != null
                    && !mAlertDialog.isShowing()) {
                mAlertDialog.show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetUtility.isNetworkAvailable(this)) {
            GlobalApplication app = (GlobalApplication) this.getApplication();
            // 注册Listener
            app.mBMapMan.getLocationManager().requestLocationUpdates(mLocationListener);
            app.mBMapMan.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
