package com.gome.ecmall.bean;

/**
 * 
 * @author qinxudong 优惠券实体类
 *         <p>
 *         主要属性有：
 *         <p>
 *         优惠券批次号：couponId ；优惠券促销活动号：promoId；
 *         <p>
 *         优惠券类型：couponType；优惠券名称：couponName；
 *         <p>
 *         优惠券面额：couponAmount；优惠券领取说明：desc；
 *         <p>
 *         优惠券领取活动时间：fetchDate；优惠券领取状态：fetchState
 */
public class CouponBean {

    /**
     * 国美红券
     */
    public static final String COUPON_RED = "0";

    /**
     * 国美蓝券
     */
    public static final String COUPON_BLUE = "1";

    /**
     * 店铺券
     */
    public static final String COUPON_SHOP = "2";

    /**
     * 品牌券
     */
    public static final String COUPON_BRAND = "3";

    /**
     * 优惠券批次号
     */
    private String couponId;

    /**
     * 优惠券促销活动号
     */
    private String promoId;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 优惠券类型
     */
    private String couponType;

    /**
     * 优惠券面额
     */
    private String couponAmount;

    /**
     * 优惠券领取说明
     */
    private String desc;

    /**
     * 优惠券领取活动时间
     */
    private String fetchDate;

    /**
     * 优惠券领取状态
     */
    private String fetchState;

    /**
     * 优惠券领取活动时间字体颜色 十六进位制颜色值
     */
    private String fetchDateColor;

    /**
     * 优惠券背景色 十六进位制颜色值
     */
    private String couponBgColor;

    /**
     * 获取优惠券列表页，标识改优惠券是否可点击
     */
    private boolean isClickAble = true;

    /**
     * @return 优惠券批次号
     */
    public String getCouponId() {
        return couponId;
    }

    /**
     * 设置优惠券批次号
     * 
     * @param couponId
     */
    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    /**
     * @return 优惠券促销活动号
     */
    public String getPromoId() {
        return promoId;
    }

    /**
     * 设置优惠券促销活动号
     * 
     * @param promoId
     */
    public void setPromoId(String promoId) {
        this.promoId = promoId;
    }

    /**
     * @return 获取优惠券名字
     */
    public String getCouponName() {
        return couponName;
    }

    /**
     * 设置优惠券名字
     * 
     * @param couponName
     */
    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    /**
     * @return 获取优惠券类型
     */
    public String getCouponType() {
        return couponType;
    }

    /**
     * 设置优惠券类型
     * 
     * @param couponType
     */
    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    /**
     * @return 返回优惠券金额
     */
    public String getCouponAmount() {
        return couponAmount;
    }

    /**
     * 设置优惠券金额
     * 
     * @param couponAmount
     */
    public void setCouponAmount(String couponAmount) {
        this.couponAmount = couponAmount;
    }

    /**
     * @return 优惠券领取说明
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 设置优惠券领取说明
     * 
     * @param desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @return 优惠券领取活动时间
     */
    public String getFetchDate() {
        return fetchDate;
    }

    /**
     * 设置优惠券领取活动时间
     * 
     * @param fetchDate
     */
    public void setFetchDate(String fetchDate) {
        this.fetchDate = fetchDate;
    }

    /**
     * @return 优惠券领取状态
     */
    public String getFetchState() {
        return fetchState;
    }

    /**
     * 设置优惠券领取状态
     * 
     * @param fetchState
     */
    public void setFetchState(String fetchState) {
        this.fetchState = fetchState;
    }

    /**
     * @return 获取优惠券活动时间字体颜色
     */
    public String getFetchDateColor() {
        return fetchDateColor;
    }

    /**
     * 设置优惠券活动时间字体颜色
     * 
     * @param fetchDateColor
     */
    public void setFetchDateColor(String fetchDateColor) {
        this.fetchDateColor = fetchDateColor;
    }

    /**
     * @return 优惠券背景颜色
     */
    public String getCouponBgColor() {
        return couponBgColor;
    }

    /**
     * 设置优惠券背景颜色
     * 
     * @param couponBgColor
     */
    public void setCouponBgColor(String couponBgColor) {
        this.couponBgColor = couponBgColor;
    }

    /**
     * @return 优惠券是否可点击
     */
    public boolean isClickAble() {
        return isClickAble;
    }

    /**
     * 设置优惠券可点击状态
     * 
     * @param isClickAble
     */
    public void setClickAble(boolean isClickAble) {
        this.isClickAble = isClickAble;
    }

}
