package com.gome.ecmall.bean;

import java.util.ArrayList;

/**
 * 套购实体类
 */
public class SuiteBuyEntity {
    private String suiteName;
    private String goodsNo;
    private int defaultSelNum;
    private String suiteOrigPrice;
    private String suitePrice;
    private long delayTime;
    private ArrayList<SuiteBuyGoods> suiteGoodsList;

    public SuiteBuyEntity() {
        super();
    }

    public SuiteBuyEntity(String suiteName, String goodsNo, int defaultSelNum, String suiteOrigPrice,
            String suitePrice, long delayTime, ArrayList<SuiteBuyGoods> suiteGoodsList) {
        super();
        this.suiteName = suiteName;
        this.goodsNo = goodsNo;
        this.defaultSelNum = defaultSelNum;
        this.suiteOrigPrice = suiteOrigPrice;
        this.suitePrice = suitePrice;
        this.delayTime = delayTime;
        this.suiteGoodsList = suiteGoodsList;
    }

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public int getDefaultSelNum() {
        return defaultSelNum;
    }

    public void setDefaultSelNum(int defaultSelNum) {
        this.defaultSelNum = defaultSelNum;
    }

    public String getSuiteOrigPrice() {
        return suiteOrigPrice;
    }

    public void setSuiteOrigPrice(String suiteOrigPrice) {
        this.suiteOrigPrice = suiteOrigPrice;
    }

    public String getSuitePrice() {
        return suitePrice;
    }

    public void setSuitePrice(String suitePrice) {
        this.suitePrice = suitePrice;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    public ArrayList<SuiteBuyGoods> getGoodsList() {
        return suiteGoodsList;
    }

    public void setGoodsList(ArrayList<SuiteBuyGoods> suiteGoodsList) {
        this.suiteGoodsList = suiteGoodsList;
    }

    @Override
    public String toString() {
        return "SuiteBuyEntity [suiteName=" + suiteName + ", goodsNo=" + goodsNo + ", defaultSelNum=" + defaultSelNum
                + ", suiteOrigPrice=" + suiteOrigPrice + ", suitePrice=" + suitePrice + ", delayTime=" + delayTime
                + ", suiteGoodsList=" + suiteGoodsList + "]";
    }

}
