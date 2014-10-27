package com.gome.ecmall.bean;

/**
 * 门店自提实时门店信息【门店】
 */
public class GomeStoreInfo {
    private String storeId;// 门店id
    private String storeName;// 门店名
    private String storeAddress;// 门店地址
    private String storePhone;// 门店电话

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

}
