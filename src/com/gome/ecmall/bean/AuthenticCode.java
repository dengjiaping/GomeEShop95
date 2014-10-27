package com.gome.ecmall.bean;

/**
 * 图片-验证码【单独类】
 */
public class AuthenticCode {
    public String isSuccess;
    private String photoUrl;
    private String jsessionId;

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getJsessionId() {
        return jsessionId;
    }

    public void setJsessionId(String jsessionId) {
        this.jsessionId = jsessionId;
    }

    @Override
    public String toString() {
        return "AuthenticCode [isSuccess=" + isSuccess + ", photoUrl=" + photoUrl + ", jsessionId=" + jsessionId + "]";
    }

}
