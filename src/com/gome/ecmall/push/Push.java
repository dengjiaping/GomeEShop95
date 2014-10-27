package com.gome.ecmall.push;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.JsonResult;
import com.gome.ecmall.dao.PushKeepAliveTimeDao;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.ecmall.util.PreferenceUtils;

public class Push {
    public static final String JK_TOKEN = "token";
    public static final String JK_LOCATION = "location";
    public static final String JK_MOBILE_MODEL = "mobileModel";
    public static final String JK_SYSTEM_VERSION = "systemVersion";
    public static final String JK_OPERATOR = "operator";
    public static final String JK_SOFTWARE_VERSION = "softwareVersion";
    public static final String JK_MOBILE_IMEI = "mobileImei";
    public static final String JK_MOBILE_MAC = "mobileMac";
    public static final String JK_MOBILE_BLUETOOTH = "mobileBluetooth";
    public static final String JK_NET_TYPE = "netType";
    public static final String JK_SCREEN_RESOLUTION = "screenResolution";
    public static final String JK_DEVICE_TYPE = "deviceType";
    public static final String JK_MOBILE_MANUFACTURER = "mobileManufacturer";
    public static final String JK_BOUND_TIME = "boundTime";
    public static final String JK_DEVICE_SOLEID = "deviceSoleId";
    public static final String JK_IS_ARRIVED_OR_WATCH = "isArrivedOrWatch";
    public static final String JK_MESSAGE_ID = "messageId";

    /**
     * 创建注册请求字符串
     * 
     * @param context
     * @return
     */
    public static String createRegisterJson(Context context) {
        JSONObject json = new JSONObject();
        try {
            json.put(JK_TOKEN, getClientId(context));
            json.put(JK_LOCATION, GlobalConfig.getInstance().getCityName());
            json.put(JK_MOBILE_MODEL, MobileDeviceUtil.getInstance(context.getApplicationContext()).getMobileModel());
            json.put(JK_SYSTEM_VERSION, MobileDeviceUtil.getInstance(context.getApplicationContext()).getSystemVersion());
            json.put(JK_OPERATOR, MobileDeviceUtil.getInstance(context.getApplicationContext()).getOperator());
            json.put(JK_SOFTWARE_VERSION, MobileDeviceUtil.getInstance(context.getApplicationContext()).getVersonName());
            json.put(JK_MOBILE_IMEI, MobileDeviceUtil.getInstance(context.getApplicationContext()).getMobileImei());
            json.put(JK_MOBILE_MAC, MobileDeviceUtil.getInstance(context.getApplicationContext()).getMacAddress());
            json.put(JK_MOBILE_BLUETOOTH, "");// 不再获取蓝牙地址
            json.put(JK_NET_TYPE, MobileDeviceUtil.getNetType(context.getApplicationContext()));
            json.put(JK_SCREEN_RESOLUTION, MobileDeviceUtil.getInstance(context.getApplicationContext()).getScreenWidth() + "*"
                    + MobileDeviceUtil.getInstance(context.getApplicationContext()).getScreenHeight());
            json.put(JK_DEVICE_TYPE, MobileDeviceUtil.getInstance(context.getApplicationContext()).getDeviceType());
            json.put(JK_MOBILE_MANUFACTURER, MobileDeviceUtil.getInstance(context.getApplicationContext()).getMobileProduct());
            json.put(JK_BOUND_TIME, PushUtils.getBoundTime());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    /**
     * 创建反馈请求字符串
     * 
     * @param context
     * @return
     */
    public static String createFeedbackrJson(Context context, String isArrivedOrWatch, String messageId) {
        JSONObject json = new JSONObject();
        try {
            String clientID = getClientId(context);
            json.put(JK_DEVICE_SOLEID, clientID);
            json.put(JK_IS_ARRIVED_OR_WATCH, isArrivedOrWatch);
            json.put(JK_MESSAGE_ID, messageId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    /**
     * 创建反馈请求字符串
     * 
     * @param context
     * @return
     */
    public static String createIsSendNetStateJson(Context context, String isArrivedOrWatch, String messageId) {
        JSONObject json = new JSONObject();
        try {
            String clientID = getClientId(context);
            json.put(JK_DEVICE_SOLEID, clientID);
            json.put(JK_SOFTWARE_VERSION, isArrivedOrWatch);
            json.put(JK_NET_TYPE, messageId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    /**
     * 获取消息推送tokenID
     * 
     * @param context
     * @return
     */
    public static String getClientId(Context context) {
        String clientID = "";
        try {
            SharedPreferences mPrefs = context.getSharedPreferences(PushService.TAG, context.MODE_PRIVATE);
            if (!TextUtils.isEmpty(MobileDeviceUtil.getInstance(context.getApplicationContext()).getMobileImei())) {
                clientID = MobileDeviceUtil.getInstance(context.getApplicationContext()).getMobileImei();
            } else if (!TextUtils.isEmpty(MobileDeviceUtil.getInstance(context.getApplicationContext()).getMacAddress())) {
                clientID = MobileDeviceUtil.getInstance(context.getApplicationContext()).getMacAddress();
            } else if (!TextUtils.isEmpty(MobileDeviceUtil.getInstance(context.getApplicationContext()).getAndroidId())) {
                clientID = MobileDeviceUtil.getInstance(context.getApplicationContext()).getAndroidId();
            } else if (!TextUtils.isEmpty(MobileDeviceUtil.getInstance(context.getApplicationContext()).getMobileImsi())) {
                clientID = MobileDeviceUtil.getInstance(context.getApplicationContext()).getMobileImsi();
            } else {
                clientID = mPrefs.getString("push_uuid", "404");
            }
            if ("404".equals(clientID)) {
                clientID = UUID.randomUUID().toString();
                mPrefs.edit().putString("push_uuid", clientID).commit();
                BDebug.d("liuyang", "存进去的token" + getClientId(context));
            }
            if (clientID.length() > 23) {
                clientID = clientID.substring(0, 22);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientID;
    }

    /**
     * 解析注册结果
     * 
     * @param response
     * @return
     */
    public static Void parseRegister(Context context, String response) {
        JsonResult result = new JsonResult(response);
        if (result.isSuccess()) {
            PreferenceUtils.getInstance(context.getApplicationContext());
            PreferenceUtils.setStringValue("push_is_register_success", "Y");
            return null;
        }
        return null;
    }

    /**
     * 消息推送收到信息后解析
     * 
     * @param json
     * @return
     */
    public static SkipParameters parseSkipParameters(JSONObject json) {
        if (json != null) {
            SkipParameters sp = new SkipParameters();
            sp.setSkipType(json.optString("sT"));
            sp.setMessageId(json.optString("mId"));
            JSONObject json1 = json.optJSONObject("sP");
            if (json1 != null) {
                if ("1".equals(json.optString("sT"))) {
                    sp.setNewId(json1.optString("id"));
                } else if ("2".equals(json.optString("sT"))) {
                    sp.setActiveId(json1.optString("id"));
                }
                sp.setActiveType(json1.optString("t"));
            }
            return sp;
        }
        return null;
    }

    /**
     * 解析心跳时间
     * 
     * @param gomeEMallActivity
     * @param response
     * @return
     */
    public static boolean parseKeepAliveTime(Context context, String response) {
        boolean isOrNoRestart = false;
        JsonResult result = new JsonResult(response);
        if (result.isSuccess()) {
            try {
                JSONObject json = new JSONObject(response);
                if (json != null) {
                    String seconds = json.optString("keepAliveTime");
                    String isSendNetState = json.optString("isSendNetState");
                    if (!TextUtils.isEmpty(seconds) && !TextUtils.isEmpty(isSendNetState)) {
                        PushKeepAliveTimeDao dao = new PushKeepAliveTimeDao(context);
                        String oldTime = dao.getkeepAliveTime();
                        if (TextUtils.isEmpty(oldTime)) {
                            if ("30".equals(seconds)) {
                                isOrNoRestart = false;
                            } else {
                                isOrNoRestart = true;
                            }
                        } else {
                            if (!seconds.equals(oldTime)) {
                                isOrNoRestart = true;
                            } else {
                                isOrNoRestart = false;
                            }
                        }
                        dao.removeKeepAlvieTime();
                        dao.addKeepAlvieTime(seconds, isSendNetState);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return isOrNoRestart;
    }

    /**
     * 判断是否到达成功,打开成功
     * 
     * @param gomeEMallActivity
     * @param response
     * @return
     */
    public static boolean parseArriveOrLook(String response) {
        JsonResult result = new JsonResult(response);
        return result.isSuccess();
    }

}
