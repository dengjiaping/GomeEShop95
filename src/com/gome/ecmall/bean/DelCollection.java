package com.gome.ecmall.bean;

/**
 * 【暂无调用】
 */
public class DelCollection {
    private String isSuccess;
    private String result;
    private String fail;

    public DelCollection() {

    }

    public DelCollection(String isSuccess, String result, String fail) {
        super();
        this.isSuccess = isSuccess;
        this.result = result;
        this.fail = fail;
    }

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getFail() {
        return fail;
    }

    public void setFail(String fail) {
        this.fail = fail;
    }

}
