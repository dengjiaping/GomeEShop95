package com.gome.ecmall.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.gome.ecmall.bean.ShoppingCart.Goods;
import com.gome.ecmall.bean.ShoppingCart.OrderProm;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_Recently_nvoiceDetail;
import com.gome.ecmall.bean.ShoppingCart.SuiteGoods;

/**
 * 店铺购物车
 * 
 * @author
 * 
 */
public class ShopingCartInfo implements Serializable {

    public boolean isExpend() {
        return isExpend;
    }

    public void setExpend(boolean isExpend) {
        this.isExpend = isExpend;
    }

    private static final long serialVersionUID = 1L;
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
     * 已参加的店铺促销信息
     */
    private ArrayList<OrderProm> shopPromList;
    /**
     * 还可参加的店铺促销信息
     */
    private ArrayList<OrderProm> shopPromUnappliedList;
    /**
     * 可选店铺券 ( 店铺级)
     */
    private ArrayList<Coupon> shopCouponSelectList;
    /**
     * 发票信息
     */
    private Invoice invoiceInfo;
    // 店铺金额小计
    private String subtotalAmount;

    /**
     * 可选品牌券
     */
    private ArrayList<Coupon> brandCouponSelectList;
    // 当前使用品牌券数量
    private String usedBrandCouponNum = "0";
    // 当前使用品牌券金额
    private String usedBrandCouponAmount = "0.00";

    public ArrayList<Coupon> getBrandCouponSelectList() {
        return brandCouponSelectList;
    }

    public void setBrandCouponSelectList(ArrayList<Coupon> brandCouponSelectList) {
        this.brandCouponSelectList = brandCouponSelectList;
    }

    public ShoppingCart_Recently_nvoiceDetail getInvoiceInfoOrder() {
        return invoiceInfoOrder;
    }

    public void setInvoiceInfoOrder(ShoppingCart_Recently_nvoiceDetail invoiceInfoOrder) {
        this.invoiceInfoOrder = invoiceInfoOrder;
    }

    public shippingInfo getShippingInfoOrder() {
        return shippingInfoOrder;
    }

    public void setShippingInfoOrder(shippingInfo shippingInfoOrder) {
        this.shippingInfoOrder = shippingInfoOrder;
    }

    /**
     * 填写订单发票信息
     */
    private ShoppingCart_Recently_nvoiceDetail invoiceInfoOrder;

    /**
     * 配送方式-填写订单
     */
    private shippingInfo shippingInfoOrder;

    /**
     * 配送信息
     */
    private Shipping shippingInfo;

    private boolean isExpend = false;

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

    public ArrayList<OrderProm> getShopPromList() {
        return shopPromList;
    }

    public void setShopPromList(ArrayList<OrderProm> arrayList) {
        this.shopPromList = arrayList;
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

    public ArrayList<OrderProm> getShopPromUnappliedList() {
        return shopPromUnappliedList;
    }

    public void setShopPromUnappliedList(ArrayList<OrderProm> shopPromUnappliedList) {
        this.shopPromUnappliedList = shopPromUnappliedList;
    }

    public ArrayList<Coupon> getShopCouponSelectList() {
        return shopCouponSelectList;
    }

    public void setShopCouponSelectList(ArrayList<Coupon> shopCouponSelectList) {
        this.shopCouponSelectList = shopCouponSelectList;
    }

    public String getSubtotalAmount() {
        return subtotalAmount;
    }

    public void setSubtotalAmount(String subtotalAmount) {
        this.subtotalAmount = subtotalAmount;
    }

    public String getUsedBrandCouponNum() {
        return usedBrandCouponNum;
    }

    public void setUsedBrandCouponNum(String usedBrandCouponNum) {
        this.usedBrandCouponNum = usedBrandCouponNum;
    }

    public String getUsedBrandCouponAmount() {
        return usedBrandCouponAmount;
    }

    public void setUsedBrandCouponAmount(String usedBrandCouponAmount) {
        this.usedBrandCouponAmount = usedBrandCouponAmount;
    }

}
