package com.gome.ecmall.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *	崩溃日志【业务类】
 */
public class CrashLogs implements JsonInterface {
    // 组织请求字符串
    public static String cerateCrashLogsJson(String softwareVersionCode, String systemVersionCode, String phoneMobel,
            String uuid, String phonePlatform, String phoneScreen, String crashLogs, String appName, String others) {
        JSONObject json = new JSONObject();
        try {
            json.put(JK_SOFTWARE_VERSION_CODE, softwareVersionCode);
            json.put(JK_SYSTEM_VERSION_CODE, systemVersionCode);
            json.put(JK_PHONE_MOBEL, phoneMobel);
            json.put(JK_UUID, uuid);
            json.put(JK_PHONE_PLATFORM, phonePlatform);
            json.put(JK_PHONE_SCREEN, phoneScreen);
            json.put(JK_CRASH_LOGS, crashLogs);
            json.put(JK_APP_NAME, appName);
            json.put(JK_OTHERS, others);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    // 解析返回结果
    public static boolean parseCrashLogsResult(String json) {
        if (json == null || json.length() == 0) {
            return false;
        }
        JsonResult jsonResult = new JsonResult(json);
        if (jsonResult.isSuccess()) {
            return true;
        } else {
            return false;
        }
    }
}
