package com.gome.ecmall.bean;

/**
 * 商品的促销信息
 * 
 * @author zhenyu.fang
 * @date 2012-7-10
 */
public class Promotion implements Promotionable {

    private int type;
    private String price;
    private String desc;
    private int level;

    /*
     * 直降 public static final int TYPE_DOWN = 1; 打折 public static final int TYPE_DISCOUNT = 2; 红券 public static final
     * int TYPE_RED_COUPON = 3; 蓝券 public static final int TYPE_BLUE_COUPON = 4; 节能补贴 public static final int
     * TYPE_ENERGY_SUBSIDIES = 6; 赠品 public static final int TYPE_GIFT = 5;
     */

    // 促销级别--SKU级别
    public static final int LEVEL_SKU = 1;
    // 促销级别--Product级别
    public static final int LEVEL_PRODUCT = 2;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String getPromotionDesc() {
        return desc;
    }

    @Override
    public int getPromotionType() {
        return getType();
    }

    @Override
    public int getPromotionLevel() {
        return getLevel();
    }
}