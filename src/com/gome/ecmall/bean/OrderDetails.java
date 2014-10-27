package com.gome.ecmall.bean;

import java.util.ArrayList;

/**
 * 用户主订单详情
 * 
 * @author Administrator
 * 
 */
public class OrderDetails {

    private OrderOper orderOper;

    /** 订单号 */
    private String orderID;

    /** 订单是否可以取消 */
    private String cancelAble;

    /** 是否显示收货按钮 */
    private String displayOrderConfirmButton;

    /** 是否可以在线支付 */
    private String onlinePayAble;

    /** 创建时间 */
    private String orderSubmitTime;

    /** 是否拆单 */
    private String isSplitedOrder;

    /** 订单跟踪进度 */
    private int orderProcess;

    /** 状态ID */
    private String orderStatus;

    /** 状态说明 */
    private String orderStatusDes;

    /** 订单状态变更时间,格式为yyyy-MM-dd HH:mm:ss */
    private String orderStatusTime;

    /** 支付方式 */
    private String payMode;

    /** 支付方式名称 */
    private String payModeName;

    /** 订单备注 */
    private String orderRemark;

    /** 订单跟踪信息 */
    private ArrayList<Traces> mTracesList;

    /**
     * 订单价格信息
     */
    public OrderPrice mOrderPrice;

    /**
     * 收货人信息
     * 
     * @author Administrator
     */
    public Consignee mConsignee;

    /** 配送信息列表 */
    private ArrayList<SG> sgLists;

    /* *//** 发票信息 */
    /*
     * private Invoice mInvoice;
     */

    /** 订单促销信息 */
    private ArrayList<Promotions> orderProms;

    /** 商品清单 */
    /*
     * private ArrayList<Goods> goodsList;
     */

    /** 商品促销信息 */
    /*
     * private ArrayList<Promotions> orderPromList;
     *//** 商品的赠品信息 */
    /*
     * private ArrayList<Gift> goodsGiftList;
     *//** 套购商品清单 */
    /*
     * private ArrayList<SuiteGoods> suiteGoodList;
     */
    /**
     * 国美店铺
     */
    private ShopCartInfo gomeShopCartInfo;
    /**
     * 其他店铺
     */
    private ArrayList<ShopCartInfo> otherShopCartInfoList;

    /**
     * 店铺购物车
     */
    /*
     * private ArrayList<ShopCartInfo> shopCartInfoList;
     * 
     * //private SuiteGoods suiteGoods;
     * 
     * public ArrayList<ShopCartInfo> getShopCartInfoList() { return shopCartInfoList; }
     * 
     * public void setShopCartInfoList(ArrayList<ShopCartInfo> shopCartInfoList) { this.shopCartInfoList =
     * shopCartInfoList; }
     */
    private Allowance allowance;

    private String isGomePickingupOrder;// 是否是门店自提 Y=国美门店自提订单 ; N=普通订单（默认）

    private String elecConfmCode;// 电子签收码
    
    private String isFixedtimeOrder;//是否运能管理订单
    
    private String isDatedPay;//是否超期支付（运能管理）
    
    private String setPayTime;//运能管理规定支付时间
    
    

    public String getIsFixedtimeOrder() {
        return isFixedtimeOrder;
    }

    public void setIsFixedtimeOrder(String isFixedtimeOrder) {
        this.isFixedtimeOrder = isFixedtimeOrder;
    }

    public String getIsDatedPay() {
        return isDatedPay;
    }

    public void setIsDatedPay(String isDatedPay) {
        this.isDatedPay = isDatedPay;
    }

    public String getSetPayTime() {
        return setPayTime;
    }

    public void setSetPayTime(String setPayTime) {
        this.setPayTime = setPayTime;
    }

    public OrderDetails() {
        super();
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCancelAble() {
        return cancelAble;
    }

    public void setCancelAble(String cancelAble) {
        this.cancelAble = cancelAble;
    }

    public String getDisplayOrderConfirmButton() {
        return displayOrderConfirmButton;
    }

    public void setDisplayOrderConfirmButton(String displayOrderConfirmButton) {
        this.displayOrderConfirmButton = displayOrderConfirmButton;
    }

    public String getOnlinePayAble() {
        return onlinePayAble;
    }

    public void setOnlinePayAble(String onlinePayAble) {
        this.onlinePayAble = onlinePayAble;
    }

    public String getOrderSubmitTime() {
        return orderSubmitTime;
    }

    public void setOrderSubmitTime(String orderSubmitTime) {
        this.orderSubmitTime = orderSubmitTime;
    }

    public String getIsSplitedOrder() {
        return isSplitedOrder;
    }

    public void setIsSplitedOrder(String isSplitedOrder) {
        this.isSplitedOrder = isSplitedOrder;
    }

    public int getOrderProcess() {
        return orderProcess;
    }

    public void setOrderProcess(int orderProcess) {
        this.orderProcess = orderProcess;
    }

    public ArrayList<Promotions> getOrderProms() {
        return orderProms;
    }

    public void setOrderProms(ArrayList<Promotions> orderProms) {
        this.orderProms = orderProms;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusTime() {
        return orderStatusTime;
    }

    public void setOrderStatusTime(String orderStatusTime) {
        this.orderStatusTime = orderStatusTime;
    }

    public String getOrderStatusDes() {
        return orderStatusDes;
    }

    public void setOrderStatusDes(String orderStatusDes) {
        this.orderStatusDes = orderStatusDes;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getPayModeName() {
        return payModeName;
    }

    public void setPayModeName(String payModeName) {
        this.payModeName = payModeName;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public void setTracesList(ArrayList<Traces> traces) {
        mTracesList = traces;
    }

    public ArrayList<Traces> getTracesList() {
        return mTracesList;
    }

    public OrderPrice getOrderPrice() {
        return mOrderPrice;
    }

    public void setOrderPrice(OrderPrice orderPrice) {
        this.mOrderPrice = orderPrice;
    }

    public Consignee getConsignee() {
        return mConsignee;
    }

    public void setConsignee(Consignee mConsignee) {
        this.mConsignee = mConsignee;
    }

    /*
     * public Invoice getInvoice() { return mInvoice; }
     * 
     * public void setInvoice(Invoice invoice) { this.mInvoice = invoice; }
     */

    public ArrayList<SG> getSgLists() {
        return sgLists;
    }

    public void setSgLists(ArrayList<SG> sgLists) {
        this.sgLists = sgLists;
    }

    /*
     * public ArrayList<Goods> getGoodsList() { return goodsList; }
     * 
     * public void setGoodsList(ArrayList<Goods> goodsList) { this.goodsList = goodsList; }
     * 
     * public ArrayList<Promotions> getOrderPromList() { return orderPromList; }
     * 
     * public void setOrderPromList(ArrayList<Promotions> orderPromList) { this.orderPromList = orderPromList; }
     * 
     * public ArrayList<SuiteGoods> getSuiteGoodList() { return suiteGoodList; }
     * 
     * public void setSuiteGoodList(ArrayList<SuiteGoods> suiteGoodList) { this.suiteGoodList = suiteGoodList; }
     */
    public OrderOper getOrderOper() {
        return orderOper;
    }

    public void setOrderOper(OrderOper orderOper) {
        this.orderOper = orderOper;
    }

    /*
     * public SuiteGoods getSuiteGoods() { return suiteGoods; }
     * 
     * public void setSuiteGoods(SuiteGoods suiteGoods) { this.suiteGoods = suiteGoods; }
     */

    public Allowance getAllowance() {
        return allowance;
    }

    public void setAllowance(Allowance allowance) {
        this.allowance = allowance;
    }

    public ShopCartInfo getGomeShopCartInfo() {
        return gomeShopCartInfo;
    }

    public void setGomeShopCartInfo(ShopCartInfo gomeShopCartInfo) {
        this.gomeShopCartInfo = gomeShopCartInfo;
    }

    public ArrayList<ShopCartInfo> getOtherShopCartInfoList() {
        return otherShopCartInfoList;
    }

    public void setOtherShopCartInfoList(ArrayList<ShopCartInfo> otherShopCartInfoList) {
        this.otherShopCartInfoList = otherShopCartInfoList;
    }

    public String getIsGomePickingupOrder() {
        return isGomePickingupOrder;
    }

    public void setIsGomePickingupOrder(String isGomePickingupOrder) {
        this.isGomePickingupOrder = isGomePickingupOrder;
    }

    public String getElecConfmCode() {
        return elecConfmCode;
    }

    public void setElecConfmCode(String elecConfmCode) {
        this.elecConfmCode = elecConfmCode;
    }

}
