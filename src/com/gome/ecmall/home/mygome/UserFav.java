package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import com.gome.ecmall.bean.Gift;
import com.gome.ecmall.bean.Product.BBCShopInfo;
import com.gome.ecmall.bean.Promotions;

/**
 * 用户收藏列表的实体类
 * 
 * @author Administrator
 * 
 */
public class UserFav {
    private static final String TAG = "UserFav";

    private String isSuccess;
    private String id;
    private String goodsNo;
    private String skuID;
    private String skuName;
    private String productImgUrl;
    private String salePrice;
    private String isOnsale;
    private String collectionTime;

    private double reducedPrice;

    private String address;// 到货通知地址
    private String stockState;// 库存状况

    // 是否支持BBC商品 bo.yang
    private String isBBc;
    private BBCShopInfo bbcShopInfo;

    private ArrayList<Promotions> promList;
    private ArrayList<Gift> giftList;
    private boolean isLoadImg;

    public UserFav() {
        promList = new ArrayList<Promotions>();
    }

    public UserFav(String isSuccess, String id, String goodsNo, String skuID, String skuName, String productImgUrl,
            String salePrice, String isOnsale, String collectionTime, ArrayList<Promotions> promList,
            ArrayList<Gift> giftList) {
        super();
        this.isSuccess = isSuccess;
        this.id = id;
        this.goodsNo = goodsNo;
        this.skuID = skuID;
        this.skuName = skuName;
        this.productImgUrl = productImgUrl;
        this.salePrice = salePrice;
        this.isOnsale = isOnsale;
        this.collectionTime = collectionTime;
        this.promList = promList;
        this.giftList = giftList;
    }

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getProductImgUrl() {
        return productImgUrl;
    }

    public void setProductImgUrl(String productImgUrl) {
        this.productImgUrl = productImgUrl;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getIsOnsale() {
        return isOnsale;
    }

    public void setIsOnsale(String isOnsale) {
        this.isOnsale = isOnsale;
    }

    public String getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(String collectionTime) {
        this.collectionTime = collectionTime;
    }

    public ArrayList<Promotions> getPromList() {
        return promList;
    }

    public void setPromList(ArrayList<Promotions> promList) {
        this.promList = promList;
    }

    public ArrayList<Gift> getGiftList() {
        return giftList;
    }

    public void setGiftList(ArrayList<Gift> giftList) {
        this.giftList = giftList;
    }

    public void addPromotion(Promotions promotion) {
        promList.add(promotion);
    }

    public boolean isLoadImg() {
        return isLoadImg;
    }

    public void setLoadImg(boolean isLoadImg) {
        this.isLoadImg = isLoadImg;
    }

    public double getReducedPrice() {
        return reducedPrice;
    }

    public void setReducedPrice(double reducedPrice) {
        this.reducedPrice = reducedPrice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStockState() {
        return stockState;
    }

    public void setStockState(String stockState) {
        this.stockState = stockState;
    }

    public String getIsBBc() {
        return isBBc;
    }

    public void setIsBBc(String isBBc) {
        this.isBBc = isBBc;
    }

    public BBCShopInfo getBbcShopInfo() {
        return bbcShopInfo;
    }

    public void setBbcShopInfo(BBCShopInfo bbcShopInfo) {
        this.bbcShopInfo = bbcShopInfo;
    }

    @Override
    public String toString() {
        return "UserFav [isSuccess=" + isSuccess + ", id=" + id + ", goodsNo=" + goodsNo + ", skuID=" + skuID
                + ", skuName=" + skuName + ", productImgUrl=" + productImgUrl + ", salePrice=" + salePrice
                + ", isOnsale=" + isOnsale + ", collectionTime=" + collectionTime + ", promList=" + promList
                + ", giftList=" + giftList + "]";
    }

}
