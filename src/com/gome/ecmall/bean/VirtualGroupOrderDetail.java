package com.gome.ecmall.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 虚拟团购订单详情
 * @author liuyang-ds
 *
 */
public class VirtualGroupOrderDetail {
    public static final String JK_ORDERSTATUE = "orderStatue";
    public static final String JK_SUMITTIME = "sumitTime";
    public static final String JK_ORDERPRICE = "orderPrice";
    public static final String JK_GOODSNO = "goodsNo";
    public static final String JK_GOODNAME = "goodName";
    public static final String JK_GOODSCOUNT = "goodsCount";
    public static final String JK_GOODPRICE = "goodPrice";
    public static final String JK_TOTALPRICE = "totalPrice";
    public static final String JK_PAYMENTMETHOD = "paymentMethod";
    public static final String JK_PAYMENTMETHODDESC = "paymentMethodDesc";
    public static final String JK_GOODTOTALPRICE = "goodTotalPrice";
    public static final String JK_BALANCE = "balance";
    public static final String JK_SALE_PROMO_ITEM = "salePromoItem";
    private String orderID;
    private String orderStatue;
    private String sumitTime;
    private String orderPrice;
    private String goodsNo;
    private String goodName;
    private String goodsCount;
    private String goodPrice;
    private String totalPrice;
    private String paymentMethod;
    private String paymentMethodDesc;
    private String goodTotalPrice;
    private String balance;
    private String salePromoItem;
    
    public String getSalePromoItem() {
        return salePromoItem;
    }
    public void setSalePromoItem(String salePromoItem) {
        this.salePromoItem = salePromoItem;
    }
    public String getOrderID() {
        return orderID;
    }
    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
    public String getOrderStatue() {
        return orderStatue;
    }
    public void setOrderStatue(String orderStatue) {
        this.orderStatue = orderStatue;
    }
    public String getSumitTime() {
        return sumitTime;
    }
    public void setSumitTime(String sumitTime) {
        this.sumitTime = sumitTime;
    }
    public String getOrderPrice() {
        return orderPrice;
    }
    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }
    public String getGoodsNo() {
        return goodsNo;
    }
    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }
    public String getGoodName() {
        return goodName;
    }
    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }
    public String getGoodsCount() {
        return goodsCount;
    }
    public void setGoodsCount(String goodsCount) {
        this.goodsCount = goodsCount;
    }
    public String getGoodPrice() {
        return goodPrice;
    }
    public void setGoodPrice(String goodPrice) {
        this.goodPrice = goodPrice;
    }
    public String getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public String getPaymentMethodDesc() {
        return paymentMethodDesc;
    }
    public void setPaymentMethodDesc(String paymentMethodDesc) {
        this.paymentMethodDesc = paymentMethodDesc;
    }
    public String getGoodTotalPrice() {
        return goodTotalPrice;
    }
    public void setGoodTotalPrice(String goodTotalPrice) {
        this.goodTotalPrice = goodTotalPrice;
    }
    public String getBalance() {
        return balance;
    }
    public void setBalance(String balance) {
        this.balance = balance;
    }
    /**
     * @param orderId
     * @return 构建请求json串
     */
    public static String createOrderDetailJson(String orderId) {
        JSONObject object = new JSONObject();
        try {
            object.put(JsonInterface.JK_ORDER_ID, orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }
    /**
     * 解析虚拟商品订单详情
     * @param response
     * @return
     */
    public static VirtualGroupOrderDetail parseOrderDetail(String response) {
        if (response == null || response.length() == 0) {
            return null;
        }
        JsonResult result = new JsonResult(response);
        if (!result.isSuccess()) {
            return null;
        }
        VirtualGroupOrderDetail detail = null;
        try {
            JSONObject object = result.getJsContent();
            detail = new VirtualGroupOrderDetail();
            detail.setSalePromoItem(object.optString(JK_SALE_PROMO_ITEM));
            detail.setOrderID(object.optString(JsonInterface.JK_ORDER_ID));
            detail.setOrderStatue(object.optString(JK_ORDERSTATUE));
            detail.setSumitTime(object.optString(JK_SUMITTIME));
            detail.setOrderPrice(object.optString(JK_ORDERPRICE));
            detail.setGoodsNo(object.optString(JK_GOODSNO));
            detail.setGoodName(object.optString(JK_GOODNAME));
            detail.setGoodsCount(object.optString(JK_GOODSCOUNT));
            detail.setGoodPrice(object.optString(JK_GOODPRICE));
            detail.setTotalPrice(object.optString(JK_TOTALPRICE));
            detail.setPaymentMethod(object.optString(JK_PAYMENTMETHOD));
            detail.setPaymentMethodDesc(object.optString(JK_PAYMENTMETHODDESC));
            detail.setGoodTotalPrice(object.optString(JK_GOODTOTALPRICE));
            detail.setBalance(object.optString(JK_BALANCE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }
 
    
}
