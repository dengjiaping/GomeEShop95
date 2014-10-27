package com.gome.ecmall.bean;

/**
 * 用户主订单详情
 * 
 * @author Administrator
 * 
 */
public class UserOrderDetails {

    /** 订单号 */
    private String orderID;

    /** 订单是否可以取消 */
    private String cancelAble;

    /** 是否显示收货按钮 */
    private String displayOrderConfirmButton;

    /** 是否可以在线支付 */
    private String onlinePayAble;

    /** 创建时间 */
    private String orderSubmitTime;

    /** 是否拆单 */
    private String isSplitedOrder;

    /** 订单跟踪进度 */
    private int orderProcess;

    /** 状态ID */
    private String orderStatus;

    /** 订单状态变更时间 */
    private String orderStatusTime;

    /** 状态说明 */
    private String orderStatusDes;

    /** 支付方式 */
    private String payMode;

    /** 支付方式名称 */
    private String payModeName;

    /** 订单备注 */
    private String orderRemark;

    /** 订单跟踪 */
    private UserOrderProcess mOrderProcess;

    /**
     * 订单价格信息
     */
    public UserOrderPrice mOrderPrice;

    /**
     * 收货人信息
     * 
     * @author Administrator
     */
    public Consignee mConsignee;

    /** 发票信息 */
    private Invoice mInvoice;

    public UserOrderDetails() {
        super();
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

    public String getOrderStatusDes() {
        return orderStatusDes;
    }

    public void setOrderStatusDes(String orderStatusDes) {
        this.orderStatusDes = orderStatusDes;
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

    public UserOrderProcess getmOrderProcess() {
        return mOrderProcess;
    }

    public void setmOrderProcess(UserOrderProcess mOrderProcess) {
        this.mOrderProcess = mOrderProcess;
    }

    public UserOrderPrice getmOrderPrice() {
        return mOrderPrice;
    }

    public void setmOrderPrice(UserOrderPrice mOrderPrice) {
        this.mOrderPrice = mOrderPrice;
    }

    public Consignee getmConsignee() {
        return mConsignee;
    }

    public void setmConsignee(Consignee mConsignee) {
        this.mConsignee = mConsignee;
    }

    public Invoice getmInvoice() {
        return mInvoice;
    }

    public void setmInvoice(Invoice mInvoice) {
        this.mInvoice = mInvoice;
    }

}
