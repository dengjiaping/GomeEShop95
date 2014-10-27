package com.gome.ecmall.bean;

/**
 * 确认收货-订单详情【单独类】
 */
public class ConfirmReceipt {
    private String status;
    private String errorMessage;
    private String failReason;

    public ConfirmReceipt() {
    }

    public ConfirmReceipt(String status, String errorMessage, String failReason) {
        super();
        this.status = status;
        this.errorMessage = errorMessage;
        this.failReason = failReason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

}
