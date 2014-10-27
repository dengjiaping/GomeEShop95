package com.gome.ecmall.bean;

/**
 * 支付宝钱包登陆单例实体
 * @author qinxd
 *
 */
public class AlipayUserInfo {
    
    /** 是否支付宝钱包登录，true该用户来自支付宝；false用户来自 */
    private boolean isAlipayLogin = false;

    /** 支付宝钱包用户userId */
    private String alipayUserId;

    /** 支付宝钱包授权码 */
    private String alipayAuthCode;
    
    private static AlipayUserInfo alipayUserInfo ;
    
    private AlipayUserInfo(){
    }
    
    public static AlipayUserInfo getInstance(){
        if(alipayUserInfo==null){
            alipayUserInfo = new AlipayUserInfo() ;
        }
        return alipayUserInfo ;
    }

    /**
     * 
     * @return 是否为支付宝钱包登陆用户：true为支付宝钱包登陆用户；false为非支付宝登陆用户
     */
    public boolean isAlipayLogin() {
        return isAlipayLogin;
    }

    /**
     * 设置支付宝钱包用户登陆状态
     * @param isAlipayLogin
     */
    public void setAlipayLogin(boolean isAlipayLogin) {
        this.isAlipayLogin = isAlipayLogin;
    }

    /**
     * @return 获取支付宝用户的userId
     */
    public String getAlipayUserId() {
        return alipayUserId;
    }

    /**
     *  设置支付宝用户的userId
     * 该id为支付宝的userIde
     * @param alipayUserId
     */
    public void setAlipayUserId(String alipayUserId) {
        this.alipayUserId = alipayUserId;
    }

    /**
     * 获取验证码
     * @return
     */
    public String getAlipayAuthCode() {
        return alipayAuthCode;
    }

    /**
     * 设置验证码
     * @param alipayAuthCode
     */
    public void setAlipayAuthCode(String alipayAuthCode) {
        this.alipayAuthCode = alipayAuthCode;
    }
    
    

}
