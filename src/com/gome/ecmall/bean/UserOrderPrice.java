package com.gome.ecmall.bean;

/**
 * 订单价格信息
 */
public class UserOrderPrice {
    /** 订单总额 */
    private double orderPrice;

    /** 订单折扣 */
    private double discountAmount;

    /** 运费 */
    private double freight;

    /** 红券支付金额 */
    private double redTicketAmount;

    /** 蓝券支付金额 */
    private double blueTicketAmount;

    /** 虚拟帐户支付金额 */
    private double virtualAmount;

    /** 应付金额 */
    private double orderPayPrice;

    /** 支付状态 */
    private String payState;

    public UserOrderPrice() {
        super();
    }

    public UserOrderPrice(double orderPrice, double discountAmount, double freight, double redTicketAmount,
            double blueTicketAmount, double virtualAmount, double orderPayPrice, String payState) {
        super();
        this.orderPrice = orderPrice;
        this.discountAmount = discountAmount;
        this.freight = freight;
        this.redTicketAmount = redTicketAmount;
        this.blueTicketAmount = blueTicketAmount;
        this.virtualAmount = virtualAmount;
        this.orderPayPrice = orderPayPrice;
        this.payState = payState;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getFreight() {
        return freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }

    public double getRedTicketAmount() {
        return redTicketAmount;
    }

    public void setRedTicketAmount(double redTicketAmount) {
        this.redTicketAmount = redTicketAmount;
    }

    public double getBlueTicketAmount() {
        return blueTicketAmount;
    }

    public void setBlueTicketAmount(double blueTicketAmount) {
        this.blueTicketAmount = blueTicketAmount;
    }

    public double getVirtualAmount() {
        return virtualAmount;
    }

    public void setVirtualAmount(double virtualAmount) {
        this.virtualAmount = virtualAmount;
    }

    public double getOrderPayPrice() {
        return orderPayPrice;
    }

    public void setOrderPayPrice(double orderPayPrice) {
        this.orderPayPrice = orderPayPrice;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

}