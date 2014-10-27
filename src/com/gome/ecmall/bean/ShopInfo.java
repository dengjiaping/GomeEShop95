package com.gome.ecmall.bean;

import java.io.Serializable;

/**
 * 店铺信息
 * 
 * @author liuyang-ds
 * 
 */
public class ShopInfo implements Serializable {
    /**
     * 店铺id
     */
    private String bbcShopId;
    /**
     * 店铺名称
     */
    private String bbcShopName;
    /**
     * 店铺图片
     */
    private String bbcShopImgURL;

    public String getBbcShopId() {
        return bbcShopId;
    }

    public void setBbcShopId(String bbcShopId) {
        this.bbcShopId = bbcShopId;
    }

    public String getBbcShopName() {
        return bbcShopName;
    }

    public void setBbcShopName(String bbcShopName) {
        this.bbcShopName = bbcShopName;
    }

    public String getBbcShopImgURL() {
        return bbcShopImgURL;
    }

    public void setBbcShopImgURL(String bbcShopImgURL) {
        this.bbcShopImgURL = bbcShopImgURL;
    }

}
