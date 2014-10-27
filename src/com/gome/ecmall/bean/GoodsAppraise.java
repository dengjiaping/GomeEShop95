package com.gome.ecmall.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 商品评价实体类
 * 
 * @author Administrator
 * 
 */
public class GoodsAppraise {
    private String userReviewId;
    private String skuID;
    private String goodsNo;
    private String advantage;
    private String disadvantage;
    private String summary;
    private float score;
    private String title;

    public GoodsAppraise() {
    }

    public GoodsAppraise(String userReviewId, String skuID, String goodsNo, String advantage, String disadvantage,
            String summary, float score, String title) {
        super();
        this.userReviewId = userReviewId;
        this.skuID = skuID;
        this.goodsNo = goodsNo;
        this.advantage = advantage;
        this.disadvantage = disadvantage;
        this.summary = summary;
        this.score = score;
        this.title = title;
    }

    public String getUserReviewId() {
        return userReviewId;
    }

    public void setUserReviewId(String userReviewId) {
        this.userReviewId = userReviewId;
    }

    public String getSkuID() {
        return skuID;
    }

    public void setSkuID(String skuID) {
        this.skuID = skuID;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getAdvantage() {
        return advantage;
    }

    public void setAdvantage(String advantage) {
        this.advantage = advantage;
    }

    public String getDisadvantage() {
        return disadvantage;
    }

    public void setDisadvantage(String disadvantage) {
        this.disadvantage = disadvantage;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static String createJson(GoodsAppraise appraise) {
        if (appraise == null)
            return null;

        String userReviewId = appraise.getUserReviewId();
        String skuID = appraise.getSkuID();
        String goodsNo = appraise.getGoodsNo();
        String advantage = appraise.getAdvantage();
        String disadvantage = appraise.getDisadvantage();
        String summary = appraise.getSummary();
        float score = appraise.getScore();
        String title = appraise.getTitle();
        return createJson(userReviewId, skuID, goodsNo, advantage, disadvantage, summary, score, title);
    }

    public static String createJson(String userReviewId, String skuID, String goodsNo, String advantage,
            String disadvantage, String summary, float score, String title) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(JsonInterface.JK_USER_REVIEW_ID, userReviewId);
            obj.put("skuID", skuID);
            obj.put(JsonInterface.JK_GOODS_NO, goodsNo);
            obj.put(JsonInterface.JK_ADVANTAGE, advantage);
            obj.put(JsonInterface.JK_DISADVANTAGE, disadvantage);
            obj.put(JsonInterface.JK_SUMMARY, summary);
            obj.put(JsonInterface.JK_SCORE, score);
            obj.put(JsonInterface.JK_TITLE, title);
            return obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parseJson(String json) {
        if (json == null)
            return null;
        try {
            JSONObject obj = new JSONObject(json);
            return obj.optString(JsonInterface.JK_IS_COMMENT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
