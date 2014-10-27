package com.gome.ecmall.bean;

/**
 * 优惠劵信息类
 * 
 * @author liuyang-ds
 * 
 */
public class ShopUsedCoupon {
    private String isGomeCoupon;
    private String name;
    private String amount;

    public String getIsGomeCoupon() {
        return isGomeCoupon;
    }

    public void setIsGomeCoupon(String isGomeCoupon) {
        this.isGomeCoupon = isGomeCoupon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
