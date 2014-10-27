package com.gome.ecmall.home;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.ShoppingCartManager;
import com.gome.ecmall.dao.ActivityRecommendDao;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CrashHandler;
import com.gome.ecmall.util.CrashSend;
import com.gome.ecmall.util.PreferenceUtils;
import com.gome.ecmall.util.ScreenManager;
import com.gome.ecmall.util.UrlMatcher;

public class GlobalApplication extends Application {

    public static final String KEY_NAME = "image_load_flag";
    public static final String SP_NAME = "golbalConfig";
    public static final int CONNECT_DEFAUL = -1;
    public static final int CONNECT_SUCCESS = 0;
    public static final int CONNECT_FAIL = 1;
    public static int POSITION;
    public static final String IPPOSITION = "ip_position";
    public static final String[] URLS = { "SIT-http://10.57.4.13:8480/mobile",//
            "UAT-http://10.58.13.51:7028/mobile",//
            "PRE-http://10.58.8.22/mobile", //
            "PRE-http://10.58.8.22:7013/mobile",//
            "PRE-http://10.58.8.22:7003/mobile",//
            "PRD-http://mobile.gome.com.cn/mobile",//
            "PRD-http://10.58.50.171:7023/mobile", //
            "PRD-http://10.58.50.171:7013/mobile",//
            "PRD-http://10.58.50.171:7003/mobile",//
            "PRD-http://10.58.22.24:7023/mobile", //
            "PRD-http://10.58.22.24:7013/mobile",//
            "PRD-http://10.58.22.22:7023/mobile",//
            "PRD-http://10.58.22.22:7013/mobile", //
            "PRD-http://10.58.50.97:7003/mobile", //
            "PRD-http://10.58.50.97:7013/mobile",//
            "PRD-http://10.58.50.98:7003/mobile", //
            "PRD-http://10.58.50.98:7013/mobile",//
    };

    /** 是否用https登录 是否https登录，false HTTPS登录，true HTTP 登录 */
    public static boolean isSupportedHttps = false;

    /** 是否总是校验验证码 true不校验验证码，false每次都校验验证码 */
    public boolean isAlwaysCaptcha = false;

    /** 服务器是否链接成功 -1默认未请求；0请求成功返回可以登录；1请求成功返回不可以登录 */
    public int connect = CONNECT_DEFAUL;

    /** APP Icon URL */
    public String appStartImageURL;

    private SharedPreferences sp = null;
    // 百度地图
    static GlobalApplication mDemoApp;
    // 百度MapAPI的管理类
    public BMapManager mBMapMan = null;

    // 已经存在的图片地址
    public String oldSplashPath;

    // 授权Key
    // TODO: 请输入您的Key,
    // 申请地址: http://dev.baidu.com/wiki/static/imap/key/
    public String mStrKey = "6F83E84019F9CD1E7EA2640F1A76C97522DF0193"; // Z3iCeq9sHFbRBnoRTlmgQyX6
    boolean m_bKeyRight = true; // 授权Key正确，验证通过
    public static long limitLastRefresh;
    /**
     * 界面管理
     */
    public static ScreenManager screenManager = null;

    // 常用事件监听，用来处理通常的网络错误，授权验证错误等
    static class MyGeneralListener implements MKGeneralListener {
        @Override
        public void onGetNetworkState(int iError) {
            BDebug.e("百度定位", "无法访问网络！");
        }

        @Override
        public void onGetPermissionState(int iError) {
            if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
                // 授权Key错误：
                BDebug.e("百度定位", "请在GlobalApplication.java文件输入正确的授权Key！");
                GlobalApplication.mDemoApp.m_bKeyRight = false;
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 切换ip
        if (BDebug.DEBUG) {
            PreferenceUtils.getInstance(getApplicationContext());
            POSITION = PreferenceUtils.getIntValue(IPPOSITION, 0);
        }
        // 异常收集
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());

        // 初始化URL匹配工具类
        UrlMatcher.initUrlMatcher(getBaseContext());
        ShoppingCartManager.getInstance();
        sp = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        GlobalConfig.getInstance().setNeedLocation(true);
        GlobalConfig.getInstance().setNeedLoadImage(getImageLoadFlag());
        screenManager = ScreenManager.getScreenManager();
        // CommonUtility.startLocation(getBaseContext());
        mDemoApp = this;
        mBMapMan = new BMapManager(this);
        // 如果百度地图服务开启
        boolean isSuccess = mBMapMan.init(this.mStrKey, null);
        if (isSuccess) {
            mBMapMan.getLocationManager().setNoitifyInternal(60, 60);
        } else {
            BDebug.e("百度定位", "百度地图初始化失败！");
        }
        if (!BDebug.DEBUG) {
            // 异常收集
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(getApplicationContext());
            try {
                // 发送异常信息
                CrashSend crashSend = new CrashSend(this);
                crashSend.sendCrashlogs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 开启消息推送服务
        // PushService.actionStart(getApplicationContext());
    }

    /**
     * 设置是否加载图片的标志
     * 
     * @param isNeedLoad
     */
    public void saveImageLoadFlag(boolean isNeedLoad) {
        sp.edit().putBoolean(KEY_NAME, isNeedLoad).commit();
        GlobalConfig.getInstance().setNeedLoadImage(getImageLoadFlag());
    }

    /**
     * 获取是否加载图片
     * 
     * @return
     */
    public boolean getImageLoadFlag() {
        return sp.getBoolean(KEY_NAME, true);
    }

    public boolean hasCache() {

        int size = new ActivityRecommendDao(getBaseContext()).getAllActivityRecommends().size();
        if (size > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onTerminate() {

        if (mBMapMan != null) {
            mBMapMan.destroy();
            mBMapMan = null;
        }
        super.onTerminate();
    }


}
