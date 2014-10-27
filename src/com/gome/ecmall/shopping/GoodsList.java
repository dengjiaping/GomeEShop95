package com.gome.ecmall.shopping;

/**
 * 商品清单
 * 
 * @author Administrator
 * 
 */
public class GoodsList {

    private String skuID;
    private String goodsNo;// 商品ID
    private String skuName;
    private String goodsType;// 商品类型
    private String skuThumbImgUrl;// 商品小图URL
    private int goodsCount;// 购买数量
    private double originalPirce;// 原价
    private double totalPrice;// 总金额
    private String commerceItemID;

    public GoodsList() {
        super();
    }

    public GoodsList(String skuID, String goodsNo, String skuName, String commerceItemID, String goodsType,
            String skuThumbImgUrl, int goodsCount, double originalPirce, double totalPrice) {
        super();
        this.skuID = skuID;
        this.goodsNo = goodsNo;
        this.skuName = skuName;
        this.commerceItemID = commerceItemID;
        this.goodsType = goodsType;
        this.skuThumbImgUrl = skuThumbImgUrl;
        this.goodsCount = goodsCount;
        this.originalPirce = originalPirce;
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

    public String getskuName() {
        return skuName;
    }

    public void setskuName(String skuName) {
        this.skuName = skuName;
    }

    public String getCommerceItemID() {
        return commerceItemID;
    }

    public void setCommerceItemID(String commerceItemID) {
        this.commerceItemID = commerceItemID;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getSkuThumbImgUrl() {
        return skuThumbImgUrl;
    }

    public void setSkuThumbImgUrl(String skuThumbImgUrl) {
        this.skuThumbImgUrl = skuThumbImgUrl;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }

    public double getOriginalPirce() {
        return originalPirce;
    }

    public void setOriginalPirce(double originalPirce) {
        this.originalPirce = originalPirce;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

}
