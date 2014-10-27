package com.gome.ecmall.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 意见反馈【业务类】
 */
public class FeedBack implements JsonInterface {
    public static String errorMessage;

    public static String cerateFeedbackJson(String softwareVersionCode, String systemVersionCode, String phoneMobel,
            String uuid, String phonePlatform, String phoneScreen, String userName, String userFeed, String userEmail) {
        JSONObject json = new JSONObject();
        try {
            json.put(JK_SOFTWARE_VERSION_CODE, softwareVersionCode);
            json.put(JK_SYSTEM_VERSION_CODE, systemVersionCode);
            json.put(JK_PHONE_MOBEL, phoneMobel);
            json.put(JK_UUID, uuid);
            json.put(JK_PHONE_PLATFORM, phonePlatform);
            json.put(JK_PHONE_SCREEN, phoneScreen);
            json.put(JK_USER_NAME, userName);
            json.put(JK_USER_FEED, userFeed);
            json.put(JK_USER_EMAIL, userEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public static boolean parseFeedbackResult(String json) {
        if (json == null || json.length() == 0) {
            return false;
        }
        JsonResult jsonResult = new JsonResult(json);
        if (jsonResult.isSuccess()) {
            return true;
        } else {
            errorMessage = jsonResult.getErrorMessage();
            return false;
        }
    }

}
