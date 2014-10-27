package com.gome.ecmall.home.mygome;

import java.io.Serializable;

/**
 * 商品评价
 * 
 * @author Administrator
 * 
 */
public class UserProductReview implements Serializable {

    /**
     * 未评价主键ID
     */
    private String userRreviewId;

    /**
     * skuID
     */
    private String skuID;

    /**
     * 商品productID
     */
    private String goodsNo;

    /**
     * 优点
     */
    private String advantage;

    /**
     * 缺点
     */
    private String disadvantage;

    /**
     * 使用心得
     */
    private String summary;

    /**
     * 评分
     */
    private int score;

    private String title;

    public UserProductReview() {
    }

    public UserProductReview(String userRreviewId, String skuID, String goodsNo, String advantage, String disadvantage,
            String summary, int score, String title) {
        super();
        this.userRreviewId = userRreviewId;
        this.skuID = skuID;
        this.goodsNo = goodsNo;
        this.advantage = advantage;
        this.disadvantage = disadvantage;
        this.summary = summary;
        this.score = score;
        this.title = title;
    }

    public String getUserRreviewId() {
        return userRreviewId;
    }

    public void setUserRreviewId(String userRreviewId) {
        this.userRreviewId = userRreviewId;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
