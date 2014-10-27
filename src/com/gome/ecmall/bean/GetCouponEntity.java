package com.gome.ecmall.bean;

import java.util.ArrayList;

/**
 * @author qinxudong
 *
 */
public class GetCouponEntity {

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动规则说明
     */
    private String activityDesc;
    
    /**
     * 页面数据
     */
    private ArrayList<GetCouponBean> couponBeanList ;

    /**
     * @return 活动名称
     */
    public String getActivityName() {
        return activityName;
    }

    /**
     * 设置活动名称
     * 
     * @param activityName
     */
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    /**
     * @return 活动规则说明
     */
    public String getActivityDesc() {
        return activityDesc;
    }

    /**
     * 活动规则说明
     * 
     * @param activityDesc
     */
    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    /**
     * @return
     */
    public ArrayList<GetCouponBean> getCouponBeanList() {
        return couponBeanList;
    }

    /**
     * @param couponBeanList
     */
    public void setCouponBeanList(ArrayList<GetCouponBean> couponBeanList) {
        this.couponBeanList = couponBeanList;
    }


    
}
