package com.gome.ecmall.shopping;

/**
 * 套购商品清单
 * 
 * @author Administrator
 * 
 */
public class SuiteGoodsList {

    /** 套购名称 */
    private String suiteName;

    /** 套购商品id */
    private String goodsNo;

    /** 套购标识 */
    private String commerceSelected;

    /** 套购价格 */
    private double suitePrice;

    /** 套购数量 */
    private int suiteCount;

    /** 套购商品数量 */
    private int suiteSkuCount;

    public SuiteGoodsList() {
        super();
    }

    public SuiteGoodsList(String suiteName, String goodsNo, String commerceSelected, double suitePrice, int suiteCount,
            int suiteSkuCount) {
        super();
        this.suiteName = suiteName;
        this.goodsNo = goodsNo;
        this.commerceSelected = commerceSelected;
        this.suitePrice = suitePrice;
        this.suiteCount = suiteCount;
        this.suiteSkuCount = suiteSkuCount;
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

    public double getSuitePrice() {
        return suitePrice;
    }

    public void setSuitePrice(double suitePrice) {
        this.suitePrice = suitePrice;
    }

    public int getSuiteCount() {
        return suiteCount;
    }

    public void setSuiteCount(int suiteCount) {
        this.suiteCount = suiteCount;
    }

    public int getSuiteSkuCount() {
        return suiteSkuCount;
    }

    public void setSuiteSkuCount(int suiteSkuCount) {
        this.suiteSkuCount = suiteSkuCount;
    }

}
