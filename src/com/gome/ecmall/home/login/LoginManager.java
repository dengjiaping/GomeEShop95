package com.gome.ecmall.home.login;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.UserInfo;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.DES;
import com.gome.ecmall.util.MobileMD5;
import com.gome.ecmall.util.NetUtility;
import com.gome.ecmall.util.PreferenceUtils;

/**
 * @author qinxudong
 * 
 */
public class LoginManager {

    private static final String TAG = "LoginManager";

    public static String getKey() {
        String result = NetUtility.sendHttpRequestByGet(Constants.URL_SUPPLEMENT_ENCRYPT_KEY);
        if ((result == null) || (result.equals(""))) {
            return null;
        } else {
            try {
                JSONObject obj = new JSONObject(result);
                String isSuccess = obj.isNull(JsonInterface.JK_IS_SUCCESS) ? "" : obj
                        .getString(JsonInterface.JK_IS_SUCCESS);
                if (isSuccess.equalsIgnoreCase(JsonInterface.JV_YES)) {
                    String key = (String) (obj.isNull(JsonInterface.JK_KEY) ? "" : obj.get(JsonInterface.JK_KEY));
                    return key;
                } else {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 不定字符获取密文
     * 
     * @param args
     * @return
     */
    public static String getSigns(String... args) {
        StringBuffer sign = new StringBuffer();
        for (int i = 0; i < args.length; i++) {
            sign.append(args[i]);
        }
        BDebug.e(TAG, sign.toString());
        String str = MobileMD5.encrypt(sign.toString(), "utf-8");
        return str;
    }

    public static String getSign(String user, String pwd, String key) {
        String sign = null;
        if (key == null || key.equals("")) {
            sign = null;
        } else {
            if (key.length() > 3) {
                key = key.substring(0, 3);
                String str = user + pwd + Constants.PRIVATE_KEY + key;
                sign = MobileMD5.encrypt(str, "utf-8");
            } else {
                sign = null;
            }
        }
        return sign;
    }

    public static void saveUser(Context ctx, String name, String pwd) {
        GlobalConfig.userName = name;
        PreferenceUtils.getInstance(ctx.getApplicationContext());
        try {
            String encryptUser = DES.encryptDES(name, Constants.LOGINDESKEY);
            String encryptPwd = DES.encryptDES(pwd, Constants.LOGINDESKEY);
            PreferenceUtils.setStringValue(new String[] { GlobalConfig.USER_NAME, GlobalConfig.PASSWORD }, new String[] {
                    encryptUser, encryptPwd });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUserInfo(UserInfo userInfo) {
        if (userInfo == null)
            return;
        GlobalConfig.points = userInfo.getPoints();
        GlobalConfig.balance = userInfo.getBalance();
        GlobalConfig.WAIT_PAY_ORDER_NUM = userInfo.getWaitPayOrderNum();
        GlobalConfig.WAIT_CONFIRM_ORDER_NUM = userInfo.getWaitConfirmOrderNum();
    }

    public static void saveUser(Context ctx, UserInfo user) {
        if (user == null)
            return;
        GlobalConfig.points = user.getPoints();
        GlobalConfig.balance = user.getBalance();
        GlobalConfig.WAIT_PAY_ORDER_NUM = user.getWaitPayOrderNum();
        GlobalConfig.WAIT_CONFIRM_ORDER_NUM = user.getWaitConfirmOrderNum();

        PreferenceUtils.getInstance(ctx.getApplicationContext());
        PreferenceUtils.setStringValue(
                new String[] { JsonInterface.JK_POINTS, JsonInterface.JK_BALANCE, JsonInterface.JK_WAIT_PAY_ORDER_NUM,
                        JsonInterface.JK_WAIT_CONFIRM_ORDER_NUM, JsonInterface.JK_PROFILE_ID,
                        JsonInterface.JK_USER_NAME },
                new String[] { String.valueOf(user.getPoints()), user.getBalance(),
                        String.valueOf(user.getWaitPayOrderNum()), String.valueOf(user.getWaitConfirmOrderNum()),
                        user.getProfileID(), user.getLoginName() });
    }

    // public static boolean isFirstLogin(Context ctx) {
    // PreferenceUtils.getInstance(ctx);
    // return PreferenceUtils.getBoolValue(GobalConfig.IS_FIRST_LOGIN, false);
    // }
    //
    // public static boolean isAutoLogin(Context ctx) {
    // PreferenceUtils.getInstance(ctx);
    // return PreferenceUtils.getBoolValue(GobalConfig.IS_AUTO_LOGIN, false);
    // }

    public static void setAutoLogin(Context ctx, boolean flag) {
        PreferenceUtils.getInstance(ctx.getApplicationContext());
        PreferenceUtils.setBooleanValue(GlobalConfig.IS_AUTO_LOGIN, flag);
    }

    public static void setFirstLogin(Context ctx) {
        PreferenceUtils.getInstance(ctx.getApplicationContext());
        PreferenceUtils.setBooleanValue(GlobalConfig.IS_FIRST_LOGIN, false);
    }

    public static String createJson(String encryptUser, String encryptPwd, String sign, String mCode) {
        if (encryptUser == null || encryptUser.equals(""))
            return null;
        if (encryptPwd == null || encryptPwd.equals(""))
            return null;
        if (sign == null || sign.equals(""))
            return null;
        JSONObject obj = new JSONObject();
        try {
            obj.put(JsonInterface.JK_LOGIN_NAME, encryptUser);
            obj.put(JsonInterface.JK_PASSWORD, encryptPwd);
            obj.put(JsonInterface.JK_CAPTCHA, mCode);
            obj.put(JsonInterface.JK_SIGN, sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj.toString();
    }
    
    /**
     * 创建支付宝钱包用户快速登录json
     * @param alipayId
     * @param authCode
     * @return
     */
    public static String createAlipayAutoLoginJson(String alipayId,String authCode){
        JSONObject obj = new JSONObject();
        try {
            obj.put(JsonInterface.JK_ALIPAY_USER_ID, alipayId);
            obj.put(JsonInterface.JK_ALIPAY_AUTH_CODE, authCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    // 进入之前已经判断获取的用户名和密码不为空
    public static String createAutoLoginJson(Context ctx) {
        PreferenceUtils.getInstance(ctx.getApplicationContext());
        String encryptUser = PreferenceUtils.getStringValue(GlobalConfig.USER_NAME, "");
        String encryptPwd = PreferenceUtils.getStringValue(GlobalConfig.PASSWORD, "");
        String key = getKey();
        try {
            String user = DES.decryptDES(encryptUser, Constants.LOGINDESKEY);
            String pwd = DES.decryptDES(encryptPwd, Constants.LOGINDESKEY);
            String sign = getSign(user, pwd, key);
            return createJson(encryptUser, encryptPwd, sign, "");
        } catch (Exception e) {
            BDebug.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String creatLoginJson(String user, String pwd, String mCode) {
        try {
            String encryptUser = DES.encryptDES(user, Constants.LOGINDESKEY);
            String encryptPwd = DES.encryptDES(pwd, Constants.LOGINDESKEY);
            String key = getKey();
            String sign = getSign(user, pwd, key);
            return createJson(encryptUser, encryptPwd, sign, mCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}
