package com.gome.ecmall.bean;

import java.util.ArrayList;

/**
 * 套购商品
 */
public class SuiteGoods {

    private String suiteName;
    private String goodsNo;
    /** 套购标识 */
    private String commerceSelected;
    private String suitePrice;
    private String suiteCount;
    private String suiteSkuCount;
    /** 套购商品列表 */
    private ArrayList<Goods> goodsList;
    // private ArrayList<Gift> giftList;

    /** 套购商品的促销信息 */
    private ArrayList<Promotions> promsList;
    /* 商品属性 */
    private ArrayList<Attributes> attrList;

    public SuiteGoods() {
    }

    public SuiteGoods(String suiteName, String goodsNo, String commerceSelected, String suitePrice, String suiteCount,
            String suiteSkuCount, ArrayList<Goods> suiteGoodsList, ArrayList<Promotions> suiteProms) {
        super();
        this.suiteName = suiteName;
        this.goodsNo = goodsNo;
        this.commerceSelected = commerceSelected;
        this.suitePrice = suitePrice;
        this.suiteCount = suiteCount;
        this.suiteSkuCount = suiteSkuCount;
        this.goodsList = suiteGoodsList;
        this.promsList = suiteProms;
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

    public String getCommerceSelected() {
        return commerceSelected;
    }

    public void setCommerceSelected(String commerceSelected) {
        this.commerceSelected = commerceSelected;
    }

    public String getSuitePrice() {
        return suitePrice;
    }

    public void setSuitePrice(String suitePrice) {
        this.suitePrice = suitePrice;
    }

    public String getSuiteCount() {
        return suiteCount;
    }

    public void setSuiteCount(String suiteCount) {
        this.suiteCount = suiteCount;
    }

    public String getSuiteSkuCount() {
        return suiteSkuCount;
    }

    public void setSuiteSkuCount(String suiteSkuCount) {
        this.suiteSkuCount = suiteSkuCount;
    }

    public ArrayList<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(ArrayList<Goods> suiteGoodsList) {
        this.goodsList = suiteGoodsList;
    }

    public ArrayList<Promotions> getPromsList() {
        return promsList;
    }

    public void setPromsList(ArrayList<Promotions> suiteProms) {
        this.promsList = suiteProms;
    }

    /*
     * public ArrayList<Gift> getGiftList() { return giftList; }
     * 
     * public void setGiftList(ArrayList<Gift> giftList) { this.giftList = giftList; }
     */

    public ArrayList<Attributes> getAttrList() {
        return attrList;
    }

    public void setAttrList(ArrayList<Attributes> attrList) {
        this.attrList = attrList;
    }

}
