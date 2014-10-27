package com.gome.ecmall.home.login;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import com.gome.ecmall.bean.AuthenticCode;
import com.gome.ecmall.bean.GetActivateCode;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.JsonResult;
import com.gome.ecmall.bean.ThirdLogin;
import com.gome.ecmall.bean.UserInfo;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.VersionUpdateUtils;

public class Login implements JsonInterface {
    public static final String TAG = "Login";

    public static final String LOGIN_URL = "http://mobile.gome.com.cn/mobile/profile/userLogin.jsp";

    public static final String JK_LOGIN_REPLY = "loginReply";

    public static final String THIRD_LOGIN_INFO = "quickloginResponse";

    public static final String THIRD_LOGIN_CODE = "quickLoginCode";

    public static final String THIRD_LOGIN_NAME = "quickLoginName";

    public static final String THIRD_LOGIN_ICON = "quickLoginIcon";

    /** 创建请求Login的json */
    public static String createRequestLogin(String name, String pwd, String sign) {
        if (name == null || name.equals(""))
            return null;

        if (pwd.equals("") || pwd == null)
            return null;

        if (sign.equals("") || sign == null)
            return null;

        JSONObject object = new JSONObject();
        try {
            object.put(JsonInterface.JK_LOGIN_NAME, name);
            object.put(JsonInterface.JK_PASSWORD, pwd);
            object.put(JsonInterface.JK_SIGN, sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public static LoginReply parseLoginResponse(String json) {

        LoginReply reply = new LoginReply();

        if ((json == null) || (json.length() == 0)) {
            return null;
        }

        try {
            JSONObject obj = new JSONObject(json);

            if (json.contains(JsonInterface.JK_IS_SUCCESS)) {

                String isSuccess = obj.getString(JsonInterface.JK_IS_SUCCESS);

                if (isSuccess.equalsIgnoreCase("Y")) {
                    GlobalConfig.isLogin = true;
                    int points = 0;
                    double balance = 0.0;
                    int waitPayOrderNum = 0;
                    int waitConfirmOrderNum = 0;
                    String gradeName = null;

                    if (json.contains(JsonInterface.JK_POINTS)) {
                        points = obj.getInt(JsonInterface.JK_POINTS);
                    }
                    if (json.contains(JsonInterface.JK_BALANCE)) {
                        balance = obj.getDouble(JsonInterface.JK_BALANCE);
                    }
                    if (json.contains(JsonInterface.JK_WAIT_PAY_ORDER_NUM)) {
                        waitPayOrderNum = obj.getInt(JsonInterface.JK_WAIT_PAY_ORDER_NUM);
                    }
                    if (json.contains(JsonInterface.JK_WAIT_CONFIRM_ORDER_NUM)) {
                        waitConfirmOrderNum = obj.getInt((String) JsonInterface.JK_WAIT_CONFIRM_ORDER_NUM);
                    }
                    if (json.contains(JsonInterface.JK_GRADE_NAME)) {
                        gradeName = obj.getString(JsonInterface.JK_GRADE_NAME);
                    }

                    reply.setIsSuccess(isSuccess);
                    reply.setPoints(points);
                    reply.setBalance(balance);
                    reply.setWaitConfirmOrderNum(waitConfirmOrderNum);
                    reply.setWaitPayOrderNum(waitPayOrderNum);
                    reply.setGradeName(gradeName);

                } else if (isSuccess.equalsIgnoreCase("N")) {
                    GlobalConfig.isLogin = false;
                    if (json.contains(JsonInterface.JK_FAIL_REASON)) {
                        String fail = obj.getString(JsonInterface.JK_FAIL_REASON);
                        reply.setIsSuccess("N");
                        reply.setFailReason(fail);
                    }
                } else {
                    GlobalConfig.isLogin = false;
                    reply = null;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reply;
    }

    /** 解析响应json为LoginReply实体 */
    public static LoginReply parseLoginReply(String json) {
        LoginReply reply = new LoginReply();
        if ((json == null) || (json.length() == 0)) {
            return null;
        }
        JsonResult result = new JsonResult(json);

        JSONObject content = result.getJsContent();
        if (content == null) {
            return null;
        }
        try {
            if (!result.isSuccess()) {
                reply.setIsSuccess("N");
                reply.setFailReason(content.getString(JsonInterface.JK_FAIL_REASON));
            } else {
                reply.setIsSuccess(content.getString(JsonInterface.JK_IS_SUCCESS));
                reply.setProfileID(content.getString(JsonInterface.JK_PROFILE_ID));
                if (json.contains(JsonInterface.JK_POINTS)) {
                    reply.setPoints(content.getInt(JsonInterface.JK_POINTS));
                }
                reply.setBalance(content.getDouble(JsonInterface.JK_BALANCE));
                reply.setFailReason(content.getString(JsonInterface.JK_FAIL_REASON));
                reply.setGradeName(content.getString(JsonInterface.JK_GRADE_NAME));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reply;
    }

    /** 解析响应json为UserInfo实体 */
    public static UserInfo parseJson(String json) {
        if ((json == null) || (json.length() == 0) || json.equalsIgnoreCase("FAIL")) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        try {
            JSONObject obj = new JSONObject(json);
            userInfo.setIsSuccess(obj.optString(JsonInterface.JK_IS_SUCCESS));
            userInfo.setProfileID(obj.optString(JsonInterface.JK_PROFILE_ID));
            userInfo.setPoints(obj.optInt(JsonInterface.JK_POINTS));
            userInfo.setBalance(obj.optString(JsonInterface.JK_BALANCE));
            userInfo.setNeedCaptcha("Y".equals(obj.opt(JsonInterface.JK_IS_NEED_CAPTCHA)) ? true : false);
            userInfo.setFailReason(obj.optString(JsonInterface.JK_FAIL_REASON));
            userInfo.setGradeName(obj.optString(JsonInterface.JK_GRADE_NAME));
            userInfo.setWaitPayOrderNum(obj.optInt(JsonInterface.JK_WAIT_PAY_ORDER_NUM));
            userInfo.setWaitConfirmOrderNum(obj.optInt(JsonInterface.JK_WAIT_CONFIRM_ORDER_NUM));
            userInfo.setArrGoodsNoticeNum(obj.optInt(JsonInterface.JK_ARR_GOODS_NOTICE_NUM));
            userInfo.setReduPriceNoticeNum(obj.optInt(JsonInterface.JK_REDU_PRICE_NOTICE_NUM));
            userInfo.setMobile(obj.optString(JsonInterface.JK_MOBILE));
            userInfo.setIsActivated(obj.optString(JsonInterface.JK_IS_ACTIVATED));
            userInfo.setEmail(obj.optString(JsonInterface.JK_EMAIL));
            userInfo.setLoginName(obj.optString(JsonInterface.JK_LOGIN_NAME));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return userInfo;
    }

    public class LoginBean {

        private String userName;
        private String password;
        private String sign;

        public LoginBean() {
            super();
        }

        public LoginBean(String userName, String password, String signStr) {
            this.userName = userName;
            this.password = password;
            this.sign = signStr;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String name) {
            this.userName = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String pwd) {
            this.password = pwd;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

    }

    public static class LoginReply implements Parcelable {

        private String isSuccess;
        private String profileID;
        private int points;
        private double balance;
        private String failReason;
        private String gradeName;
        private int waitPayOrderNum;// 待支付订单
        private int waitConfirmOrderNum;// 待确认订单

        private static final String JK_IS_SUCCESS = "isSuccess";

        public LoginReply() {
            super();
        }

        public LoginReply(Parcel source) {
            setIsSuccess(source.readString());
            setProfileID(source.readString());
            setPoints(source.readInt());
            setBalance(source.readDouble());
            setFailReason(source.readString());
            setGradeName(source.readString());
            setWaitConfirmOrderNum(source.readInt());
            setWaitPayOrderNum(source.readInt());
        }

        public int getWaitPayOrderNum() {
            return waitPayOrderNum;
        }

        public void setWaitPayOrderNum(int waitPayOrderNum) {
            this.waitPayOrderNum = waitPayOrderNum;
        }

        public int getWaitConfirmOrderNum() {
            return waitConfirmOrderNum;
        }

        public void setWaitConfirmOrderNum(int waitConfirmOrderNum) {
            this.waitConfirmOrderNum = waitConfirmOrderNum;
        }

        public String getIsSuccess() {
            return isSuccess;
        }

        public void setIsSuccess(String isSuccess) {
            this.isSuccess = isSuccess;
        }

        public String getProfileID() {
            return profileID;
        }

        public void setProfileID(String profileID) {
            this.profileID = profileID;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double bal) {
            this.balance = bal;
        }

        public String getFailReason() {
            return failReason;
        }

        public void setFailReason(String failReason) {
            this.failReason = failReason;
        }

        public String getGradeName() {
            return gradeName;
        }

        public void setGradeName(String gradeName) {
            this.gradeName = gradeName;
        }

        public static String getJkIsSuccess() {
            return JK_IS_SUCCESS;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(isSuccess);

        }

        private Parcelable.Creator<LoginReply> CREATOR = new Parcelable.Creator<LoginReply>() {

            @Override
            public LoginReply createFromParcel(Parcel source) {
                return new LoginReply(source);
            }

            @Override
            public LoginReply[] newArray(int size) {
                return new LoginReply[size];
            }
        };

        @Override
        public String toString() {
            return "LoginReply [isSuccess=" + isSuccess + ", profileID=" + profileID + ", points=" + points
                    + ", balance=" + balance + ", failReason=" + failReason + ", gradeName=" + gradeName
                    + ", waitPayOrderNum=" + waitPayOrderNum + ", waitConfirmOrderNum=" + waitConfirmOrderNum
                    + ", CREATOR=" + CREATOR + "]";
        }

    }

    public static List<ThirdLogin> parseJsonThirdLoginInfo(String json) {
        List<ThirdLogin> mList = new ArrayList<ThirdLogin>();
        if (json == null || json.length() == 0) {
            return mList;
        }
        try {
            JSONObject object = new JSONObject(json);
            String isSuccess = object.getString("isSuccess");
            if (isSuccess != null && isSuccess.equals("Y")) {
                JSONArray array = object.getJSONArray(THIRD_LOGIN_INFO);
                if (array != null && array.length() > 0) {
                    for (int i = 0 , length = array.length(); i < length; i++) {
                        object = array.getJSONObject(i);
                        ThirdLogin login = new ThirdLogin();
                        login.setLoginName(object.getString(THIRD_LOGIN_NAME));
                        login.setImgPath(object.getString(THIRD_LOGIN_ICON));
                        login.setLoginCode(object.getString(THIRD_LOGIN_CODE));
                        mList.add(login);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mList;
    }

    public static String parseJsonLoginOut(String json) {
        String isSuccess = "";
        if (json == null || json.equals(""))
            return null;
        try {
            JSONObject obj = new JSONObject(json);
            isSuccess = (String) (obj.isNull(JsonInterface.JK_IS_SUCCESS) ? "" : obj.get(JsonInterface.JK_IS_SUCCESS));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    public static AuthenticCode parseAuthenticCode(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }

        try {
            JSONObject obj = new JSONObject(json);
            AuthenticCode ac = new AuthenticCode();
            String isSuccess = obj.optString(JsonInterface.JK_IS_SUCCESS);
            if (JsonInterface.JV_YES.equals(isSuccess)) {
                ac.setIsSuccess(isSuccess);
                ac.setPhotoUrl(obj.optString(JsonInterface.JK_PHOTO_URL));
                ac.setJsessionId(obj.optString(JsonInterface.JK_JSESSION));
                return ac;
            } else {
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static final int REQUEST_TIMEOUT = 30 * 1000;// 设置请求超时30秒
    private static final int SO_TIMEOUT = 30 * 1000; // 设置等待数据超时时间30秒钟
    public static GlobalConfig config = GlobalConfig.getInstance();

    public static Bitmap downloadNetworkBitmap(String url) {
        byte[] data = downloadImageFromNetwork(url);
        if (data == null || data.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public static byte[] downloadImageFromNetwork(String url) {
        InputStream is = null;
        byte[] data = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            if (config.getCookieInfo() != null) {
                httpGet.setHeader("Cookie", config.getCookieInfo());
                httpGet.setHeader("User-Agent", "Android GomeShopApp " + VersionUpdateUtils.USERUPDATEVERSONCODE);
            }
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient(httpParams);
            HttpResponse httpResponse = new DefaultHttpClient(httpParams).execute(httpGet);
            int responseCode = httpResponse.getStatusLine().getStatusCode();

            List<Cookie> cookies = defaultHttpClient.getCookieStore().getCookies();
            StringBuffer cookiesInfo = new StringBuffer();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(GlobalConfig.JSESSIONID)) {
                    String value = cookie.getValue();
                    if (!value.equals(config.getjSessionId())) {
                        config.setjSessionId(value);
                    }
                }
                cookiesInfo.append(cookie.getName() + "=" + cookie.getValue()).append(";");
            }

            if (responseCode == HttpURLConnection.HTTP_OK) {
                is = httpResponse.getEntity().getContent();
                data = transStreamToBytes(is, 4096);
                BDebug.i(TAG, "downloadImageFromNetwork()  rspCode:" + responseCode + "  URL:" + url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static byte[] transStreamToBytes(InputStream is, int buffSize) {
        if (is == null) {
            return null;
        }
        if (buffSize <= 0) {
            throw new IllegalArgumentException("buffSize can not less than zero.....");
        }
        byte[] data = null;
        byte[] buffer = new byte[buffSize];
        int actualSize = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            while ((actualSize = is.read(buffer)) != -1) {
                baos.write(buffer, 0, actualSize);
            }
            data = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static String parseImgUrl(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }

        try {
            JSONObject obj = new JSONObject(json);
            String isSuccess = obj.optString(JsonInterface.JK_IS_SUCCESS);
            if (JsonInterface.JV_YES.equals(isSuccess)) {
                return obj.optString(JsonInterface.JK_PHOTO_URL);
            } else {
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取手机激活码的请求
     */
    public static String createJsonMobile(String mobile, String type) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(JK_MOBILE, mobile);
            obj.put(JK_OPERATETYPE, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    /**
     * 手机激活码的Response
     */
    public static GetActivateCode parseJsonMobile(String json) {
        GetActivateCode gac = null;
        if (json != null && json.length() > 0) {
            try {
                JSONObject obj = new JSONObject(json);
                gac = new GetActivateCode();
                gac.setIsSuccess(obj.optString(JK_IS_SUCCESS));
                gac.setFailReason(obj.optString(JK_FAIL_REASON));
                gac.setSuccessReason(obj.optString("successMessage"));
                gac.setFailCode(obj.optString(JK_FAIL_CODE));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return gac;
    }

}
