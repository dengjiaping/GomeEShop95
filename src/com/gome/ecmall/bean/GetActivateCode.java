package com.gome.ecmall.bean;

/**
 * 获取手机激活码Response结果实体
 */
public class GetActivateCode {
    private String isSuccess;
    private String failReason;
    private String failCode;
    private String successReason;

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public String getSuccessReason() {
        return successReason;
    }

    public void setSuccessReason(String successReason) {
        this.successReason = successReason;
    }

    public String getFailCode() {
        return failCode;
    }

    public void setFailCode(String failCode) {
        this.failCode = failCode;
    }

}
