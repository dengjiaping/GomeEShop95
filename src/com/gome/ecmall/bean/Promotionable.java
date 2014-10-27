package com.gome.ecmall.bean;

public interface Promotionable {

    /**
     * 获取促销界别(Product或SKU)
     * 
     * @return
     */
    public int getPromotionLevel();

    /**
     * 获取促销描述信息
     * 
     * @return
     */
    public String getPromotionDesc();

    /**
     * 获取促销类型
     * 
     * @return
     */
    public int getPromotionType();

}