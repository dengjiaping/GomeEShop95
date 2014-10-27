package com.gome.ecmall.bean;

import java.io.Serializable;

/**
 * 用户收藏
 * 
 * @author Lang
 * 
 */
public class UserFav implements Serializable {

    /** 商品productID */
    private String goodsNo;

    /** 商品skuID */
    private String skuID;

    /** 商品skuName */
    private String skuName;

    /** 收藏ID */
    private String favId;

    /** 商品图片 */
    private String productImgUrl;

    /** 商品价格 */
    private double salePrice;

    /** 是否下架 */
    private boolean isOnSale;

    private Promotions mPromotions;

    private double promPrice;

    /** 收藏时间 格式为yyyy-MM-dd hh:mm:ss */
    private String collectionTime;

    public UserFav() {
    }

    public Promotions getmPromotions() {
        return mPromotions;
    }

    public void setmPromotions(Promotions mPromotions) {
        this.mPromotions = mPromotions;
    }

    public double getPromPrice() {
        return promPrice;
    }

    public void setmPromoDesc(double promPrice) {
        this.promPrice = promPrice;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getSkuID() {
        return skuID;
    }

    public void setSkuID(String skuID) {
        this.skuID = skuID;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getFavId() {
        return favId;
    }

    public void setFavId(String favId) {
        this.favId = favId;
    }

    public String getProductImgUrl() {
        return productImgUrl;
    }

    public void setProductImgUrl(String productImgUrl) {
        this.productImgUrl = productImgUrl;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public boolean isOnSale() {
        return isOnSale;
    }

    public void setOnSale(boolean isOnSale) {
        this.isOnSale = isOnSale;
    }

    public String getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(String collectionTime) {
        this.collectionTime = collectionTime;
    }

    @Override
    public String toString() {
        return "UserFav [goodsNo=" + goodsNo + ", skuID=" + skuID + ", skuName=" + skuName + ", favId=" + favId
                + ", productImgUrl=" + productImgUrl + ", salePrice=" + salePrice + ", isOnSale=" + isOnSale
                + ", collectionTime=" + collectionTime + "]";
    }

}
