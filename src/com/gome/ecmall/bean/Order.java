package com.gome.ecmall.bean;

/**
 * 用户订单信息
 * 
 * @author Administrator
 * 
 */
public class Order {

    /** 订单时限 */
    private int typeOrder;

    /** 当前页 */
    private int currentPage;

    /** 每页条数 */
    private int pageSize;

    /** 订单状态 */
    private int orderStatus;

    /** 订单号 */
    private String orderID;

    /** 订单金额 */
    private double orderAmount;

    /** 下单时间 */
    private String orderSubmitTime;

    /** 商品skuID */
    private String skuID;

    /** 商品id */
    private String goodsNo;

    /** 商品名称 */
    private String skuName;

    /** 商品小图 */
    private String skuThumblm;

    public int getTypeOrder() {
        return typeOrder;
    }

    public void setTypeOrder(int typeOrder) {
        this.typeOrder = typeOrder;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderSubmitTime() {
        return orderSubmitTime;
    }

    public void setOrderSubmitTime(String orderSubmitTime) {
        this.orderSubmitTime = orderSubmitTime;
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

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuThumblm() {
        return skuThumblm;
    }

    public void setSkuThumblm(String skuThumblm) {
        this.skuThumblm = skuThumblm;
    }

}
