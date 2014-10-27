package com.gome.ecmall.bean;

/**
 * 收货人信息
 */
public class Consignee {
    /** 收货人地址 */
    private String address;
    /** 收货人姓名 */
    private String name;
    /** 邮编 */
    private String zipCode;
    /** 手机 */
    private String mobile;
    /** 固定电话 */
    private String phone;
    /** Email */
    private String email;
    /** 配送方式 */
    private String shippingType;
    /** 送货时间 */
    private String shippingTime;
    /** 发货前是否电话通知 */
    private String telBefShipping;

    public Consignee() {
        super();
    }

    public Consignee(String address, String name, String zipCode, String mobile, String phone, String email,
            String shippingType, String shippingTime, String telBefShipping) {
        super();
        this.address = address;
        this.name = name;
        this.zipCode = zipCode;
        this.mobile = mobile;
        this.phone = phone;
        this.email = email;
        this.shippingType = shippingType;
        this.shippingTime = shippingTime;
        this.telBefShipping = telBefShipping;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public void settelBefShipping(String telBefShipping) {
        this.telBefShipping = telBefShipping;
    }

}