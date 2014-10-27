package com.gome.ecmall.bean;

import java.io.Serializable;

/**
 * 配送信息
 * 
 * @author liuyang-ds
 * 
 */
public class Shipping implements Serializable {
    // 配送方式
    private String shippingType;
    // 送货时间
    private String shippingTime;
    // 发货前是否电话通知
    private String telBefShipping;
    // 运费
    private String shippingFreight;
    // 电子签收码
    private String elecConfmCode;
    // 门店信息
    private GomeStoreInfo gomeStoreInfo;

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getShippingTime() {
        return shippingTime;
    }

    public void setShippingTime(String shippingTime) {
        this.shippingTime = shippingTime;
    }

    public String getTelBefShipping() {
        return telBefShipping;
    }

    public void setTelBefShipping(String telBefShipping) {
        this.telBefShipping = telBefShipping;
    }

    public String getShippingFreight() {
        return shippingFreight;
    }

    public void setShippingFreight(String shippingFreight) {
        this.shippingFreight = shippingFreight;
    }

    public String getElecConfmCode() {
        return elecConfmCode;
    }

    public void setElecConfmCode(String elecConfmCode) {
        this.elecConfmCode = elecConfmCode;
    }

    public GomeStoreInfo getGomeStoreInfo() {
        return gomeStoreInfo;
    }

    public void setGomeStoreInfo(GomeStoreInfo gomeStoreInfo) {
        this.gomeStoreInfo = gomeStoreInfo;
    }

}
