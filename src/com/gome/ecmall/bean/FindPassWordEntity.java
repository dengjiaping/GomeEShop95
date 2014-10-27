package com.gome.ecmall.bean;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 找回密码【复合类】
 */
public class FindPassWordEntity {

    public static String createRequestJson(Map<String, String> map) {
        JSONObject obj = new JSONObject();
        for (String s : map.keySet()) {
            try {
                obj.put(s, map.get(s));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return obj.toString();

    }

    public static FindPasswordStep1 parseStep1Json(String json) {
        if ((json == null) || (json.length() == 0) || json.equalsIgnoreCase("FAIL")) {
            return null;
        }
        FindPasswordStep1 step1 = new FindPasswordStep1();
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            step1.setFailReason(result.getFailReason());
            return step1;
        }
        JSONObject obj = result.getJsContent();
        step1.setPhoneNum(obj.optString(JsonInterface.JK_MOBILE));
        step1.setUserName(obj.optString(JsonInterface.JK_LOGIN_NAME));
        step1.setToken(obj.optString("token"));
        return step1;
    }

    /**
     * 找回密码-第一步
     */
    public static class FindPasswordStep1 {

        private String phoneNum;
        private String userName;
        private String failReason;
        private String token;

        public String getFailReason() {
            return failReason;
        }

        public void setFailReason(String failReason) {
            this.failReason = failReason;
        }

        public String getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

    }

}
