package com.gome.ecmall.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 抢购-详情-帮助中心-帮助【单独类】
 */
public class Helper {
    private String title;
    private String htmlURL;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static String createRequestCouponJson(String helperType) {
        JSONObject json = new JSONObject();
        try {
            json.put("typeId", helperType);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public static Helper parseHelper(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        Helper helper = new Helper();
        try {
            JSONObject jsContent = result.getJsContent();
            helper.setTitle(jsContent.optString("title"));
            helper.setContent(jsContent.optString("htmlContent"));
            helper.setHtmlURL(jsContent.optString("htmlURL"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return helper;
    }

    public String getHtmlURL() {
        return htmlURL;
    }

    public void setHtmlURL(String htmlURL) {
        this.htmlURL = htmlURL;
    }

}
