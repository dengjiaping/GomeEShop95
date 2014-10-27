package com.gome.ecmall.bean;

/**
 * 订单跟踪信息
 * 
 * @author Administrator
 * 
 */
public class OrderProcess {
    private String dealTime;
    private String dealValue;
    private String dealType;

    public OrderProcess() {
        super();
    }

    public OrderProcess(String dealTime, String dealValue, String dealType) {
        super();
        this.dealTime = dealTime;
        this.dealValue = dealValue;
        this.dealType = dealType;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }

    public String getDealValue() {
        return dealValue;
    }

    public void setDealValue(String dealValue) {
        this.dealValue = dealValue;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

}