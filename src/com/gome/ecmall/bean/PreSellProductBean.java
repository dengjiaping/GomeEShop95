package com.gome.ecmall.bean;

import org.json.JSONObject;

import com.gome.ecmall.util.UrlMatcher;

/**
 * 预售商品实体
 */
public class PreSellProductBean {
    private String titleName;
    private String imgUrl;
    private String rule;
    private String activityId;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public static String getRequestJson(String activityId, String activityType) {
        JSONObject json = new JSONObject();
        try {
            json.put(JsonInterface.JK_ACTIVITY_ID, activityId);
            json.put(JsonInterface.JK_ACTIVITY_TYPE, activityType);
            return json.toString();
        } catch (Exception e) {
        }
        return null;
    }

    public static PreSellProductBean parseJson(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject content = result.getJsContent();
        PreSellProductBean preSellProduct = null;
        try {
            JSONObject activityInfo = content.optJSONObject(JsonInterface.JK_PROMOTION_ACTIVITY_INFO);
            preSellProduct = new PreSellProductBean();
            if (activityInfo != null) {
                preSellProduct.setActivityId(activityInfo.optString(JsonInterface.JK_ACTIVITY_ID));
                preSellProduct.setTitleName(activityInfo.optString(JsonInterface.JK_ACTIVITY_NAME));
                preSellProduct.setRule(activityInfo.optString(JsonInterface.JK_ACTIVITY_RULE));
                preSellProduct.setImgUrl(UrlMatcher.getFitImageForPreSell(activityInfo
                        .optString(JsonInterface.JK_ACTIVITY_HTML_URL)));

            }
        } catch (Exception e) {
        }

        return preSellProduct;
    }

}
