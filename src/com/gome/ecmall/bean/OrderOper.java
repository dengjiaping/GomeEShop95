package com.gome.ecmall.bean;

/**
 *订单详情, 订单信息
 */
public class OrderOper {

    private String orderID;
    private String cancelAble;
    private String displayOrderConfirmButton;
    private String onlinePayAble;
    private String orderSubmitTime;
    private String isSplitedOrder;
    private int orderProcess;
    private String orderStatus;
    private String orderStatusTime;
    private String orderStatusDes;
    private String payMode;
    private String payModeName;
    private String orderRemark;

    public OrderOper() {
    }

    public OrderOper(String orderID, String cancelAble, String displayOrderConfirmButton, String onlinePayAble,
            String orderSubmitTime, String isSplitedOrder, int orderProcess, String orderStatus,
            String orderStatusTime, String orderStatusDes, String payMode, String payModeName, String orderRemark) {
        this.orderID = orderID;
        this.cancelAble = cancelAble;
        this.displayOrderConfirmButton = displayOrderConfirmButton;
        this.onlinePayAble = onlinePayAble;
        this.orderSubmitTime = orderSubmitTime;
        this.isSplitedOrder = isSplitedOrder;
        this.orderProcess = orderProcess;
        this.orderStatus = orderStatus;
        this.orderStatusTime = orderStatusTime;
        this.orderStatusDes = orderStatusDes;
        this.payMode = payMode;
        this.payModeName = payModeName;
        this.orderRemark = orderRemark;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCancelAble() {
        return cancelAble;
    }

    public void setCancelAble(String cancelAble) {
        this.cancelAble = cancelAble;
    }

    public String getDisplayOrderConfirmButton() {
        return displayOrderConfirmButton;
    }

    public void setDisplayOrderConfirmButton(String displayOrderConfirmButton) {
        this.displayOrderConfirmButton = displayOrderConfirmButton;
    }

    public String getOnlinePayAble() {
        return onlinePayAble;
    }

    public void setOnlinePayAble(String onlinePayAble) {
        this.onlinePayAble = onlinePayAble;
    }

    public String getOrderSubmitTime() {
        return orderSubmitTime;
    }

    public void setOrderSubmitTime(String orderSubmitTime) {
        this.orderSubmitTime = orderSubmitTime;
    }

    public String getIsSplitedOrder() {
        return isSplitedOrder;
    }

    public void setIsSplitedOrder(String isSplitedOrder) {
        this.isSplitedOrder = isSplitedOrder;
    }

    public int getOrderProcess() {
        return orderProcess;
    }

    public void setOrderProcess(int orderProcess) {
        this.orderProcess = orderProcess;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusTime() {
        return orderStatusTime;
    }

    public void setOrderStatusTime(String orderStatusTime) {
        this.orderStatusTime = orderStatusTime;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getPayModeName() {
        return payModeName;
    }

    public void setPayModeName(String payModeName) {
        this.payModeName = payModeName;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public String getOrderStatusDes() {
        return orderStatusDes;
    }

    public void setOrderStatusDes(String orderStatusDes) {
        this.orderStatusDes = orderStatusDes;
    }

    @Override
    public String toString() {
        return "OrderOper [orderID=" + orderID + ", cancelAble=" + cancelAble + ", displayOrderConfirmButton="
                + displayOrderConfirmButton + ", onlinePayAble=" + onlinePayAble + ", orderSubmitTime="
                + orderSubmitTime + ", isSplitedOrder=" + isSplitedOrder + ", orderProcess=" + orderProcess
                + ", orderStatus=" + orderStatus + ", orderStatusTime=" + orderStatusTime + ", orderStatusDes="
                + orderStatusDes + ", payMode=" + payMode + ", payModeName=" + payModeName + ", orderRemark="
                + orderRemark + "]";
    }

}
