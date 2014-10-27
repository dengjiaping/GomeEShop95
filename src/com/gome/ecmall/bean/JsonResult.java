package com.gome.ecmall.bean;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

/**
 * Response JSON数据返回结果
 */
public class JsonResult {

    private boolean isSuccess = false;
    private JSONObject jsContent;
    private String failReason;
    private String failCode;
    private String errorMessage;
    private String isSessionExpired;
    private boolean isActivated;
    private String successMessage;
    public static GlobalConfig config = GlobalConfig.getInstance();

    public JsonResult(String msg) {
        try {
            JSONObject json = new JSONObject(msg);
            String result = json.optString(JsonInterface.JK_SUCCESS);
            isSessionExpired = json.optString(JsonInterface.JK_ISSESSIONEXPIRIED);
            String activated = json.optString(JsonInterface.JK_ISACTIVATE);
            String successMes = json.optString(JsonInterface.JK_SUCCESSMESSAGE);
            if (JsonInterface.JV_YES.equals(result)) {
                isSuccess = true;
                jsContent = json;
                successMessage = successMes;
            } else if (result.equals(JsonInterface.JV_NO)) {
                isSuccess = false;
                jsContent = json;
                failReason = json.optString(JsonInterface.JK_FAIL_REASON);
                failCode = json.optString(JsonInterface.JK_FAIL_CODE);
                errorMessage = json.optString(JsonInterface.JK_ERROR_MESSAGE);
            }
            if (JsonInterface.JV_YES.equals(activated)) {
                isActivated = true;
            } else if (JsonInterface.JV_NO.equals(activated)) {
                isActivated = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public JSONObject getJsContent() {
        return jsContent;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getFailReason() {
        return failReason;
    }

    public String getIsSessionExpired() {
        if (TextUtils.isEmpty(isSessionExpired)) {
            return "N";
        }
        return isSessionExpired;
    }

    public void setIsSessionExpired(String isSessionExpired) {
        this.isSessionExpired = isSessionExpired;
    }

    public boolean getIsActivated() {
        return isActivated;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public String getFailCode() {
        return failCode;
    }

    public void setFailCode(String failCode) {
        this.failCode = failCode;
    }
}
