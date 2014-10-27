package com.gome.ecmall.home.login;

import org.json.JSONException;
import org.json.JSONObject;

import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.JsonResult;
import com.gome.ecmall.bean.PhoneActivation;
import com.gome.ecmall.bean.UserInfo;

public class Register implements JsonInterface {

    // private static final String TAG = "";

    // public static final String JK_REGISTER_URL =
    // "http://mobile.gome.com.cn/mobile/profile/userRegister.jsp";

    // public static final String JK_LOGIN_NAME = "loginName";
    // public static final String JK_PASSWORD = "passWord";
    // public static final String JK_CONFIRM_WORD = "confirmWord";
    // public static final String JK_KEY = "key";
    // public static final String JK_EMAIL = "email";
    //
    // public static final String JK_IS_SUCCESS = "isSuccess";
    // public static final String JK_PROFILE_ID = "profileID";
    // public static final String JK_POINTS = "points";
    // public static final String JK_BALANCE = "balance";
    // public static final String JK_FAIL_REASON = "failReason";

    public static String createRequestRegister(String name, String mobel, String pwd, String cfPwd, String key,
            String email) {
        JSONObject object = new JSONObject();
        try {
            object.put(JK_LOGIN_NAME, name);
            object.put(JK_MOBILE, mobel);
            object.put(JK_PASSWORD, pwd);
            object.put(JK_CONFIRM_WORD, cfPwd);
            object.put(JK_KEY, key);
            object.put(JK_EMAIL, email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public static String createRequestNewPassword(String userType, String loginName, String passWord,
            String confirmWord, String sign, String token) {
        JSONObject object = new JSONObject();
        try {
            object.put("userType", userType);
            object.put("loginName", loginName);
            object.put(JK_PASSWORD, passWord);
            object.put(JK_CONFIRM_WORD, confirmWord);
            object.put("sign", sign);
            object.put("token", token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public static String createRequestPaymentPassword(String oldPayPassWord, String payPassWord,
            String confirmPayPassWord, String token) {
        JSONObject object = new JSONObject();
        try {
            object.put("oldPayPassWord", oldPayPassWord);
            object.put("payPassWord", payPassWord);
            object.put("confirmPayPassWord", confirmPayPassWord);
            object.put("token", token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object.toString();
    }
    public static String createRequestPassword(String oldPassword, String newPassword, String confirmWord) {
        JSONObject object = new JSONObject();
        try {
            object.put("oldPassword", oldPassword);
            object.put("newPassword", newPassword);
            object.put("confirmWord", confirmWord);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    /**
     * 解析用户信息
     * 
     * @param json
     * @return
     */
    public static UserInfo parseJsonUserInfo(String json) {//
        if (json == null || json.equals("")) {
            return null;
        }
        JsonResult result = new JsonResult(json);
        UserInfo info = new UserInfo();
        try {
            JSONObject obj = result.getJsContent();
            info.setIsSuccess(obj.optString(JK_IS_SUCCESS));
            if (JV_YES.equals(info.getIsSuccess())) {
                info.setProfileID(obj.optString(JK_PROFILE_ID));
                info.setPoints(obj.optInt(JK_POINTS));
                info.setBalance(obj.optString(JK_BALANCE));
                info.setFailReason(obj.optString(JK_FAIL_REASON));
                info.setWaitPayOrderNum(obj.optInt(JK_WAIT_PAY_ORDER_NUM));
                info.setWaitConfirmOrderNum(obj.optInt(JK_WAIT_CONFIRM_ORDER_NUM));
                info.setArrGoodsNoticeNum(obj.optInt(JK_ARR_GOODS_NOTICE_NUM));
                info.setIsActivated(obj.optString(JK_IS_ACTIVATED));
                // info.setMobile(obj.optString(JK_MOBILE));

            } else if (JV_NO.equals(info.getIsSuccess())) {
                info.setFailReason(obj.getString(JK_FAIL_REASON));
                info.setFailCode(obj.getString(JK_FAIL_CODE));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 手机激活Json
     * 
     * @param mobile
     *            手机号
     * @param verifyCode
     *            手机激活码
     * @return JsonObject 的字符串表示
     */
    public static String createActivateMobile(String mobile, String verifyCode) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(JK_MOBILE, mobile);
            obj.put(JK_VERIFY_CODE, verifyCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }
    /**
     * 虚拟团购手机号验证
     * 
     */
    public static String createActivateGroupMobile(String operateType, String verifyMobile,String verifyCode,String modMobile) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(JK_OPERATETYPE, operateType);
            obj.put(JK_VERIFY_MOBILE, verifyMobile);
            obj.put(JK_VERIFY_CODE, verifyCode);
            obj.put(JK_MOD_MOBILE, modMobile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }
    /**
     * 快速注册Json
     * 
     * @param mobile
     *            手机号
     * @param 登录密码
     *            手机激活码
     * @return JsonObject 的字符串表示
     */
    public static String createFasterRegister(String mobile, String passWord) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(JK_MOBILE, mobile);
            obj.put(JK_PASSWORD, passWord);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    /**
     * 解析手机激活返回数据
     * 
     * @param json
     * @return
     */
    public static PhoneActivation parseActivateMobile(String json) {
        if (json == null || json.equals("")) {
            return null;
        }
        PhoneActivation pa = null;
        try {
            JSONObject obj = new JSONObject(json);
            pa = new PhoneActivation();
            pa.setIsSuccess(obj.optString(JK_IS_SUCCESS));
            pa.setLoginName(obj.optString(JK_LOGIN_NAME));
            pa.setMobile(obj.optString(JK_MOBILE));
            pa.setFailReason(obj.optString(JK_FAIL_REASON));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pa;
    }

    /**
     * 获取手机激活码Json
     * 
     * @param mobile
     * @return
     */
    public static String createGetsPhoneActivateCode(String mobile) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(JK_MOBILE, mobile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    // public static RegisterReply parseReply(String json) {
    // RegisterReply reply = null;
    // if ((json == null) || (json.length() == 0)) {
    // return null;
    // }
    // JSONObject jsonObj;
    // try {
    // jsonObj = new JSONObject(json);
    //
    // String success = jsonObj.getString(JK_IS_SUCCESS);
    // if (success.equals("") || success == null)
    // return null;
    // reply = new RegisterReply();
    // if (success.equalsIgnoreCase("Y")) {
    // reply.setIsSuccess(jsonObj.getString(JK_IS_SUCCESS));
    // reply.setProfileID(jsonObj.getString(JK_PROFILE_ID));
    // reply.setPoints(jsonObj.getInt(JK_POINTS));
    // reply.setBalance(jsonObj.getDouble(JK_BALANCE));
    // reply.setFailReason(jsonObj.getString(JK_FAIL_REASON));
    // reply.setWaitPayOrderNum(jsonObj.optInt(JK_WAIT_PAY_ORDER_NUM));
    // reply.setWaitConfirmOrderNum(jsonObj.optInt(JK_WAIT_CONFIRM_ORDER_NUM));
    // reply.setArrGoodsNotice(jsonObj.optInt(JK_ARR_GOODS_NOTICE_NUM));
    // }
    // if (success.equalsIgnoreCase("N")) {
    // reply.setIsSuccess(jsonObj.getString(JK_IS_SUCCESS));
    // reply.setFailReason(jsonObj.getString(JK_FAIL_REASON));
    // }
    //
    // } catch (JSONException e1) {
    // e1.printStackTrace();
    // }
    //
    // return reply;
    // }

    public static class RegisterRequest {
        private String loginName;
        private int mobel;
        private String pwd;
        private String cfPwd;// 确认密码
        private String checkCode;// 验证码
        private String email;

        public RegisterRequest() {
            super();
        }

        public RegisterRequest(String loginName, int mobel, String pwd, String cfPwd, String checkCode, String email) {
            super();
            this.loginName = loginName;
            this.mobel = mobel;
            this.pwd = pwd;
            this.cfPwd = cfPwd;
            this.checkCode = checkCode;
            this.email = email;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getCheckCode() {
            return checkCode;
        }

        public void setCheckCode(String checkCode) {
            this.checkCode = checkCode;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getMobel() {
            return mobel;
        }

        public void setMobel(int mobel) {
            this.mobel = mobel;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        public String getCfPwd() {
            return cfPwd;
        }

        public void setCfPwd(String cfPwd) {
            this.cfPwd = cfPwd;
        }

        @Override
        public String toString() {
            return "RegisterRequest [mobel=" + mobel + ", pwd=" + pwd + ", cfPwd=" + cfPwd + "]";
        }

    }

    // public static class RegisterReply implements Parcelable {
    // private String isSuccess;
    // private String profileID;
    // private int points;
    // private double balance;
    // private String gradeName;
    // private String failReason;
    // private int waitPayOrderNum;
    // private int waitConfirmOrderNum;
    // private int arrGoodsNotice;
    // private int reduPriceNotice;
    //
    // public RegisterReply() {
    // super();
    // }
    //
    // public RegisterReply(Parcel source) {
    // setIsSuccess(source.readString());
    // setProfileID(source.readString());
    // setPoints(source.readInt());
    // setBalance(source.readDouble());
    // setFailReason(source.readString());
    // setWaitPayOrderNum(source.readInt());
    // setWaitConfirmOrderNum(source.readInt());
    // setArrGoodsNotice(source.readInt());
    // setReduPriceNotice(source.readInt());
    // setGradeName(source.readString());
    // }
    //
    // public String getIsSuccess() {
    // return isSuccess;
    // }
    //
    // public void setIsSuccess(String isSuccess) {
    // this.isSuccess = isSuccess;
    // }
    //
    // public String getProfileID() {
    // return profileID;
    // }
    //
    // public void setProfileID(String profileID) {
    // this.profileID = profileID;
    // }
    //
    // public int getPoints() {
    // return points;
    // }
    //
    // public void setPoints(int points) {
    // this.points = points;
    // }
    //
    // public double getBalance() {
    // return balance;
    // }
    //
    // public void setBalance(double balance) {
    // this.balance = balance;
    // }
    //
    // public String getGradeName() {
    // return gradeName;
    // }
    //
    // public void setGradeName(String gradeName) {
    // this.gradeName = gradeName;
    // }
    //
    // public String getFailReason() {
    // return failReason;
    // }
    //
    // public void setFailReason(String failReason) {
    // this.failReason = failReason;
    // }
    //
    // public int getWaitPayOrderNum() {
    // return waitPayOrderNum;
    // }
    //
    // public void setWaitPayOrderNum(int waitPayOrderNum) {
    // this.waitPayOrderNum = waitPayOrderNum;
    // }
    //
    // public int getWaitConfirmOrderNum() {
    // return waitConfirmOrderNum;
    // }
    //
    // public void setWaitConfirmOrderNum(int waitConfirmOrderNum) {
    // this.waitConfirmOrderNum = waitConfirmOrderNum;
    // }
    //
    // public int getArrGoodsNotice() {
    // return arrGoodsNotice;
    // }
    //
    // public void setArrGoodsNotice(int arrGoodsNotice) {
    // this.arrGoodsNotice = arrGoodsNotice;
    // }
    //
    // public int getReduPriceNotice() {
    // return reduPriceNotice;
    // }
    //
    // public void setReduPriceNotice(int reduPriceNotice) {
    // this.reduPriceNotice = reduPriceNotice;
    // }
    //
    // @Override
    // public int describeContents() {
    // return 0;
    // }
    //
    // @Override
    // public void writeToParcel(Parcel dest, int flags) {
    //
    // }
    //
    // private static Parcelable.Creator<RegisterReply> CREATORS = new
    // Creator<Register.RegisterReply>() {
    //
    // @Override
    // public RegisterReply[] newArray(int size) {
    // return new RegisterReply[size];
    // }
    //
    // @Override
    // public RegisterReply createFromParcel(Parcel source) {
    // return new RegisterReply(source);
    // }
    // };
    //
    // @Override
    // public String toString() {
    // return "RegisterReply [isSuccess=" + isSuccess + ", profileID=" +
    // profileID + ", points=" + points
    // + ", balance=" + balance + ", failReason=" + failReason +
    // ", waitPayOrderNum=" + waitPayOrderNum
    // + ", waitConfirmOrderNum=" + waitConfirmOrderNum + ", arrGoodsNotice=" +
    // arrGoodsNotice
    // + ", reduPriceNotice=" + reduPriceNotice + "]";
    // }
    //
    // }

}
