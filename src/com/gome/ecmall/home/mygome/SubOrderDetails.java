package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import com.gome.ecmall.bean.Gift;
import com.gome.ecmall.bean.Goods;
import com.gome.ecmall.bean.Invoice;
import com.gome.ecmall.bean.Promotions;
import com.gome.ecmall.bean.Shipping;
import com.gome.ecmall.bean.ShopInfo;
import com.gome.ecmall.bean.ShopUsedCoupon;
import com.gome.ecmall.bean.Traces;

/**
 * 
 */
public class SubOrderDetails {

    private String isSuccess;
    private String fail;

    /**
     * 订单号
     */
    private String orderID;
    /**
     * 配送单号
     */
    private String sgID;

    /** 配送单状态描述 */
    private String sgStatus;

    /** 配送单状态对应数字 */
    private String sgStatusId;

    private String sgStatusTime;

    private String sgAmount = "0";

    private String sgPayAmount = "0";

    private String deliveryMode;

    private String freight = "0";

    private String prePayment = "0";

    private String sgSubmitTime;

    private String acceptanceCode;

    private String discountPayment = "0";

    private int sgProcess;

    private ArrayList<Goods> goodsList;

    private ArrayList<Promotions> promList;

    private ArrayList<Gift> giftList;

    private ArrayList<Traces> tracesList;

    /** 配送单销信息 */
    private ArrayList<Promotions> shippingPromList;

    /**
     * 店铺信息
     */
    private ShopInfo shopInfo;
    /**
     * 是否是国美在线
     */
    private String isGome;
    /**
     * 商品总数
     */
    private int totalCount;
    /**
     * 发票信息
     */
    private Invoice invoice;
    /**
     * 配送信息
     */
    private Shipping shipping;
    /**
     * 是否是门店自提
     */
    private String isGomePickingupOrder;

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

    public SubOrderDetails() {
    }

    public SubOrderDetails(String orderID, String sgID, String sgStatus, String sgStatusId, String sgStatusTime,
            String sgAmount, String sgPayAmount, String deliveryMode, String freight, String prePayment,
            String sgSubmitTime, String acceptanceCode, String discountPayment, int sgProcess,
            ArrayList<Goods> goodsList, ArrayList<Promotions> promList, ArrayList<Gift> giftList,
            ArrayList<Traces> tracesList) {
        this.orderID = orderID;
        this.sgID = sgID;
        this.sgStatus = sgStatus;
        this.sgStatusId = sgStatusId;
        this.sgStatusTime = sgStatusTime;
        this.sgAmount = sgAmount;
        this.sgPayAmount = sgPayAmount;
        this.deliveryMode = deliveryMode;
        this.freight = freight;
        this.prePayment = prePayment;
        this.sgSubmitTime = sgSubmitTime;
        this.acceptanceCode = acceptanceCode;
        this.discountPayment = discountPayment;
        this.sgProcess = sgProcess;
        this.goodsList = goodsList;
        this.promList = promList;
        this.giftList = giftList;
        this.tracesList = tracesList;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getSgID() {
        return sgID;
    }

    public void setSgID(String sgID) {
        this.sgID = sgID;
    }

    public String getSgStatus() {
        return sgStatus;
    }

    public void setSgStatus(String sgStatus) {
        this.sgStatus = sgStatus;
    }

    public String getSgStatusId() {
        return sgStatusId;
    }

    public void setSgStatusId(String sgStatusId) {
        this.sgStatusId = sgStatusId;
    }

    public String getSgStatusTime() {
        return sgStatusTime;
    }

    public void setSgStatusTime(String sgStatusTime) {
        this.sgStatusTime = sgStatusTime;
    }

    public String getSgAmount() {
        return sgAmount;
    }

    public void setSgAmount(String sgAmount) {
        this.sgAmount = sgAmount;
    }

    public String getSgPayAmount() {
        return sgPayAmount;
    }

    public void setSgPayAmount(String sgPayAmount) {
        this.sgPayAmount = sgPayAmount;
    }

    public String getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getPrePayment() {
        return prePayment;
    }

    public void setPrePayment(String prePayment) {
        this.prePayment = prePayment;
    }

    public String getSgSubmitTime() {
        return sgSubmitTime;
    }

    public void setSgSubmitTime(String sgSubmitTime) {
        this.sgSubmitTime = sgSubmitTime;
    }

    public String getAcceptanceCode() {
        return acceptanceCode;
    }

    public void setAcceptanceCode(String acceptanceCode) {
        this.acceptanceCode = acceptanceCode;
    }

    public String getDiscountPayment() {
        return discountPayment;
    }

    public void setDiscountPayment(String discountPayment) {
        this.discountPayment = discountPayment;
    }

    public int getSgProcess() {
        return sgProcess;
    }

    public void setSgProcess(int sgProcess) {
        this.sgProcess = sgProcess;
    }

    public ArrayList<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(ArrayList<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public ArrayList<Promotions> getPromList() {
        return promList;
    }

    public void setPromList(ArrayList<Promotions> promList) {
        this.promList = promList;
    }

    public ArrayList<Gift> getGiftList() {
        return giftList;
    }

    public void setGiftList(ArrayList<Gift> giftList) {
        this.giftList = giftList;
    }

    public ArrayList<Traces> getTracesList() {
        return tracesList;
    }

    public void setTracesList(ArrayList<Traces> tracesList) {
        this.tracesList = tracesList;
    }

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getFail() {
        return fail;
    }

    public void setFail(String fail) {
        this.fail = fail;
    }

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

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public ArrayList<Promotions> getShippingPromList() {
        return shippingPromList;
    }

    public void setShippingPromList(ArrayList<Promotions> shippingPromList) {
        this.shippingPromList = shippingPromList;
    }

    public String getIsGomePickingupOrder() {
        return isGomePickingupOrder;
    }

    public void setIsGomePickingupOrder(String isGomePickingupOrder) {
        this.isGomePickingupOrder = isGomePickingupOrder;
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
