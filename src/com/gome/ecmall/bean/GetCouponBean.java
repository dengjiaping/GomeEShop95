package com.gome.ecmall.bean;

import java.util.ArrayList;

/**
 * @author qinxudong 优惠券实体类
 */
public class GetCouponBean {

    
    /**
     * 一级分类
     */
    private String type;

    /**
     * 某批次优惠券集合
     */
    private ArrayList<CouponBean> couponList;


    /**
     * @return 获取一级分类集合
     */
    public String getType() {
        return type;
    }

    /**
     * 设置一级分类
     * 
     * @param typeList
     */
    public void setType(String typeList) {
        this.type = typeList;
    }

    /**
     * @return 某批次优惠券集合
     */
    public ArrayList<CouponBean> getCouponList() {
        return couponList;
    }

    /**
     * 设置 某批次优惠券集合
     * 
     * @param couponList
     */
    public void setCouponList(ArrayList<CouponBean> couponList) {
        this.couponList = couponList;
    }
    

}
