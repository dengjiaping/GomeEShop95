package com.gome.ecmall.bean;

/**
 * 用户信息
 * 
 * @author Administrator
 * 
 */
public class UserInfo {

    private String isSuccess;
    private String isActivated;
    private String mobile;
    private String email;
    private String profileID;
    private String loginName;
    /** 是否需要加载验证码图片 */
    private boolean isNeedCaptcha;

    /** 积分 */
    private int points;

    /** 帐户余额 */
    private String balance;

    /***/
    private String gradeName;

    /** 待支付订单数量 */
    private int waitPayOrderNum;

    /** 待收货确认订单数量 */
    private int waitConfirmOrderNum;

    /** 到货通知 */
    private int arrGoodsNoticeNum;

    /** 降价通知 */
    private int reduPriceNoticeNum;

    /** 返回的错误信息 */
    private String failReason;
    
    /** 返回的错误码 */
    private String failCode;

    public UserInfo() {
        super();
    }

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getWaitPayOrderNum() {
        return waitPayOrderNum;
    }

    public void setWaitPayOrderNum(int waitPayOrderNum) {
        this.waitPayOrderNum = waitPayOrderNum;
    }

    public int getWaitConfirmOrderNum() {
        return waitConfirmOrderNum;
    }

    public void setWaitConfirmOrderNum(int waitConfirmOrderNum) {
        this.waitConfirmOrderNum = waitConfirmOrderNum;
    }

    public int getArrGoodsNoticeNum() {
        return arrGoodsNoticeNum;
    }

    public void setArrGoodsNoticeNum(int arrGoodsNoticeNum) {
        this.arrGoodsNoticeNum = arrGoodsNoticeNum;
    }

    public int getReduPriceNoticeNum() {
        return reduPriceNoticeNum;
    }

    public void setReduPriceNoticeNum(int reduPriceNoticeNum) {
        this.reduPriceNoticeNum = reduPriceNoticeNum;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public String getIsActivated() {
        return isActivated;
    }

    public void setIsActivated(String isActivated) {
        this.isActivated = isActivated;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * @return true 总是校验验证码，false不校验验证码
     */
    public boolean isNeedCaptcha() {
        return isNeedCaptcha;
    }

    public void setNeedCaptcha(boolean isNeedCaptcha) {
        this.isNeedCaptcha = isNeedCaptcha;
    }

    public String getFailCode() {
        return failCode;
    }

    public void setFailCode(String failCode) {
        this.failCode = failCode;
    }

}
