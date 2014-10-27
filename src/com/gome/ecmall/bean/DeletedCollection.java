package com.gome.ecmall.bean;

/**
 * 我的收藏-删除结过-实体类
 */
public class DeletedCollection {
    private String isSuccess;
    private String result;
    private String fail;

    public DeletedCollection() {

    }

    public DeletedCollection(String isSuccess, String result, String fail) {
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
