package com.gome.ecmall.bean;

/**
 * 商品赠品信息
 */
public class Gift {

    /** 商品sku */
    private String skuID;

    /** 商品id */
    private String goodsNo;

    /** 商品skuNo */
    private String skuNo;

    /** 商品名称 */
    private String skuName;

    private String commerceItemID;

    private int goodsType;

    private int goodsCount;

    private double originalPrice;

    private double totalPrice;

    public Gift() {
        super();
    }

    public Gift(String skuID, String goodsNo, String skuNo, String skuName, String commerceItemID, int goodsType,
            int goodsCount, double originalPrice, double totalPrice) {
        super();
        this.skuID = skuID;
        this.goodsNo = goodsNo;
        this.skuNo = skuNo;
        this.skuName = skuName;
        this.commerceItemID = commerceItemID;
        this.goodsType = goodsType;
        this.goodsCount = goodsCount;
        this.originalPrice = originalPrice;
        this.totalPrice = totalPrice;
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

    public String getSkuNo() {
        return skuNo;
    }

    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getCommerceItemID() {
        return commerceItemID;
    }

    public void setCommerceItemID(String commerceItemID) {
        this.commerceItemID = commerceItemID;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Gift [skuID=" + skuID + ", goodsNo=" + goodsNo + ", skuNo=" + skuNo + ", skuName=" + skuName
                + ", commerceItemID=" + commerceItemID + ", goodsType=" + goodsType + ", goodsCount=" + goodsCount
                + ", originalPrice=" + originalPrice + ", totalPrice=" + totalPrice + "]";
    }

}
