package com.gome.ecmall.bean;

/**
 * 订单价格信息
 */
public class OrderPrice {
    /** 订单总额 */
    private String orderPrice;

    /** 订单折扣 */
    private String discountAmount;

    /** 运费 */
    private String freight;

    /** 红券支付金额 */
    private String redTicketAmount;

    /** 蓝券支付金额 */
    private String blueTicketAmount;

    /** 虚拟帐户支付金额 */
    private String virtualAmount;

    /** 应付金额 */
    private String orderPayPrice;

    /** 支付状态 */
    private String payState;

    public OrderPrice() {
        super();
    }

    public OrderPrice(String orderPrice, String discountAmount, String freight, String redTicketAmount,
            String blueTicketAmount, String virtualAmount, String orderPayPrice, String payState) {
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

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getRedTicketAmount() {
        return redTicketAmount;
    }

    public void setRedTicketAmount(String redTicketAmount) {
        this.redTicketAmount = redTicketAmount;
    }

    public String getBlueTicketAmount() {
        return blueTicketAmount;
    }

    public void setBlueTicketAmount(String blueTicketAmount) {
        this.blueTicketAmount = blueTicketAmount;
    }

    public String getVirtualAmount() {
        return virtualAmount;
    }

    public void setVirtualAmount(String virtualAmount) {
        this.virtualAmount = virtualAmount;
    }

    public String getOrderPayPrice() {
        return orderPayPrice;
    }

    public void setOrderPayPrice(String orderPayPrice) {
        this.orderPayPrice = orderPayPrice;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

}