package com.gome.ecmall.bean;

import java.util.ArrayList;

/**
 * 商品清单
 */
public class Goods {

    protected String skuID;
    protected String goodsNo;// 商品ID
    protected String skuNo;
    protected String skuName;
    protected String goodsType;// 商品类型
    protected String skuThumbImgUrl;// 商品小图URL
    private Integer goodsCount;// 购买数量
    protected String originalPrice;// 原价
    private String totalPrice;// 总金额
    private String promPrice;// 促销价
    private String productImgURL;
    private String userReviewId;
    private String commerceItemID;
    private ArrayList<Attributes> attrList;
    private ArrayList<Promotions> promList;
    // private ArrayList<Gift> giftList;
    /** 无图版本长按加载后，true显示加载的图片，false显示长按加载图标*/
    private boolean isLoadImg;

    public Goods() {
    }

    public Goods(String skuID, String goodsNo, String skuNo, String skuName, String goodsType, String skuThumbImgUrl,
            Integer goodsCount, String originalPrice, String totalPrice) {
        this.skuID = skuID;
        this.goodsNo = goodsNo;
        this.skuNo = skuNo;
        this.skuName = skuName;
        this.goodsType = goodsType;
        this.skuThumbImgUrl = skuThumbImgUrl;
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

    public Integer getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
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

    public boolean isLoadImg() {
        return isLoadImg;
    }

    public void setLoadImg(boolean isLoadImg) {
        this.isLoadImg = isLoadImg;
    }

    @Override
    public String toString() {
        return "Goods [skuID=" + skuID + ", goodsNo=" + goodsNo + ", skuNo=" + skuNo + ", skuName=" + skuName
                + ", goodsType=" + goodsType + ", skuThumbImgUrl=" + skuThumbImgUrl + ", goodsCount=" + goodsCount
                + ", originalPrice=" + originalPrice + ", totalPrice=" + totalPrice + "]";
    }

    public String getProductImgURL() {
        return productImgURL;
    }

    public void setProductImgURL(String productImgURL) {
        this.productImgURL = productImgURL;
    }

    public String getUserReviewId() {
        return userReviewId;
    }

    public void setUserReviewId(String userReviewId) {
        this.userReviewId = userReviewId;
    }

    public String getCommerceItemID() {
        return commerceItemID;
    }

    public void setCommerceItemID(String commerceItemID) {
        this.commerceItemID = commerceItemID;
    }

    public ArrayList<Attributes> getAttrList() {
        return attrList;
    }

    public void setAttrList(ArrayList<Attributes> attrList) {
        this.attrList = attrList;
    }

    public ArrayList<Promotions> getPromList() {
        return promList;
    }

    public void setPromList(ArrayList<Promotions> promList) {
        this.promList = promList;
    }

    // public ArrayList<Gift> getGiftList() {
    // return giftList;
    // }
    //
    // public void setGiftList(ArrayList<Gift> giftList) {
    // this.giftList = giftList;
    // }

    public String getPromPrice() {
        return promPrice;
    }

    public void setPromPrice(String promPrice) {
        this.promPrice = promPrice;
    }

}
