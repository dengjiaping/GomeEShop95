package com.gome.ecmall.bean;

/**
 * 活动专题抢购商品
 */
public class RushBuyGoods extends Goods {

    private String rushBuyItemId;
    private String skuOrignalPrice;
    private String skuRushBuyPrice;
    private int limitNum;
    private int remainNum;
    private long delayTime;
    private String rushBuyState;

    public String getRushBuyItemId() {
        return rushBuyItemId;
    }

    public void setRushBuyItemId(String rushBuyItemId) {
        this.rushBuyItemId = rushBuyItemId;
    }

    public String getSkuOrignalPrice() {
        return skuOrignalPrice;
    }

    public void setSkuOrignalPrice(String skuOrignalPrice) {
        this.skuOrignalPrice = skuOrignalPrice;
    }

    public String getSkuRushBuyPrice() {
        return skuRushBuyPrice;
    }

    public void setSkuRushBuyPrice(String skuRushBuyPrice) {
        this.skuRushBuyPrice = skuRushBuyPrice;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }

    public int getRemainNum() {
        return remainNum;
    }

    public void setRemainNum(int remainNum) {
        this.remainNum = remainNum;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    public String getRushBuyState() {
        return rushBuyState;
    }

    public void setRushBuyState(String rushBuyState) {
        this.rushBuyState = rushBuyState;
    }

}
