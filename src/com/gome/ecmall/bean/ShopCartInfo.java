package com.gome.ecmall.bean;

import java.util.ArrayList;

/**
 * 店铺购物车
 * 
 * @author liuyang-ds
 * 
 */
public class ShopCartInfo {
    /**
     * 店铺信息
     */
    private ShopInfo shopInfo;
    /**
     * 是否是国美在线商品 Y:国美在线商品 N:店铺
     */
    private String isGome;
    /**
     * 配送id
     */
    private String shippingId;
    /**
     * 商品总数
     */
    private int totalCount;
    /**
     * 商品清单
     */
    private ArrayList<Goods> gomeGoodsList;
    /**
     * 套购商品清单
     */
    private ArrayList<SuiteGoods> suiteGoodsList;
    /**
     * 可选促销信息
     */
    private ArrayList<Promotions> shopPromSelectList;
    /**
     * 店铺促销信息
     */
    private ArrayList<Promotions> shopPromList;
    /**
     * 发票信息
     */
    private Invoice invoiceInfo;
    /**
     * 配送信息
     */
    private Shipping shippingInfo;
    /**
     * 是否展开(用于判断是否展开显示优惠信息，配送信息，发票信息)
     * 
     */
    private boolean openOrNo;
    /**
     * 店铺金额小计 subtotalAmount
     * 
     * @return
     */
    private String subtotalAmount;
    /**
     * 店铺金额合计 totalAmount
     * 
     * @return
     */
    private String totalAmount;
    /**
     * 优惠劵列表
     */
    private ArrayList<ShopUsedCoupon> shopUsedCouponList;

    public ShopInfo getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
    }

    public String getIsGome() {
        return isGome;
    }

    public void setIsGome(String isGome) {
        this.isGome = isGome;
    }

    public String getShippingId() {
        return shippingId;
    }

    public void setShippingId(String shippingId) {
        this.shippingId = shippingId;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public ArrayList<Goods> getGomeGoodsList() {
        return gomeGoodsList;
    }

    public void setGomeGoodsList(ArrayList<Goods> gomeGoodsList) {
        this.gomeGoodsList = gomeGoodsList;
    }

    public ArrayList<SuiteGoods> getSuiteGoodsList() {
        return suiteGoodsList;
    }

    public void setSuiteGoodsList(ArrayList<SuiteGoods> suiteGoodsList) {
        this.suiteGoodsList = suiteGoodsList;
    }

    public ArrayList<Promotions> getShopPromSelectList() {
        return shopPromSelectList;
    }

    public void setShopPromSelectList(ArrayList<Promotions> shopPromSelectList) {
        this.shopPromSelectList = shopPromSelectList;
    }

    public ArrayList<Promotions> getShopPromList() {
        return shopPromList;
    }

    public void setShopPromList(ArrayList<Promotions> shopPromList) {
        this.shopPromList = shopPromList;
    }

    public Invoice getInvoiceInfo() {
        return invoiceInfo;
    }

    public void setInvoiceInfo(Invoice invoiceInfo) {
        this.invoiceInfo = invoiceInfo;
    }

    public Shipping getShippingInfo() {
        return shippingInfo;
    }

    public void setShippingInfo(Shipping shippingInfo) {
        this.shippingInfo = shippingInfo;
    }

    public boolean isOpenOrNo() {
        return openOrNo;
    }

    public void setOpenOrNo(boolean openOrNo) {
        this.openOrNo = openOrNo;
    }

    public String getSubtotalAmount() {
        return subtotalAmount;
    }

    public void setSubtotalAmount(String subtotalAmount) {
        this.subtotalAmount = subtotalAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ArrayList<ShopUsedCoupon> getShopUsedCouponList() {
        return shopUsedCouponList;
    }

    public void setShopUsedCouponList(ArrayList<ShopUsedCoupon> shopUsedCouponList) {
        this.shopUsedCouponList = shopUsedCouponList;
    }

}
