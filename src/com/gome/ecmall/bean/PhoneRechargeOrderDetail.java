package com.gome.ecmall.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gome.ecmall.home.login.LoginManager;
import com.gome.ecmall.util.Constants;

/**
 * 手机充值订单详情
 */
public class PhoneRechargeOrderDetail {

    /** 订单编号 */
    private String phoneRechargeOrderNum;
    /** 订单状态标识 */
    private String phoneRechargeOrderClientStatu;
    /** 订单状态 */
    private String phoneRechargeOrderState;
    /** 订单描述 */
    private String phoneRechargeOrderDescribe;
    /** 电话号码 */
    private String phoneRechargeOrderPhoneNum;
    /** 充值面额 */
    private String phoneRechargeOrderAmount;
    /** 支付方式 */
    private String phoneRechargeOrderPayMethod;
    /** 下单时间 */
    private String phoneRechargeOrderTime;
    /** 商品编号 */
    private String phoneRechargeOrderProductNum;
    /** 商品名称 */
    private String phoneRechargeOrderProductName;
    /** 国美价 */
    private String phoneRechargeOrderGomePrice;
    /** 订单优惠 */
    private String phoneRechargeOrderFavorable;
    /** 订单优惠内容 */
    private String phoneRechargeOrderFavorableContent;
    /** 商品金额 */
    private String phoneRechargeOrderProductPrice;
    /** 余额支付 */
    private String phoneRechargeOrderVirtualaccountPay;
    /** 优惠金额 */
    private String phoneRechargeOrderProm;
    /** 订单金额 */
    private String phoneRechargeOrderPrice;
    /** 商品skuId */
    private String phoneRechargeProductSkuId;
    /** 是否已支付 */
    private boolean isPayAble;
    /** 充值区域名称 */
    private String areaOperators;
    /** 订单追踪list */
    private List<OrderTraces> orderTracesList;

    /**
     * @return 订单编号
     */
    public String getPhoneRechargeOrderNum() {
        return phoneRechargeOrderNum;
    }

    public void setPhoneRechargeOrderNum(String phoneRechargeOrderNum) {
        this.phoneRechargeOrderNum = phoneRechargeOrderNum;
    }

    /**
     * @return 订单状态
     */
    public String getPhoneRechargeOrderState() {
        return phoneRechargeOrderState;
    }

    public void setPhoneRechargeOrderState(String phoneRechargeOrderState) {
        this.phoneRechargeOrderState = phoneRechargeOrderState;
    }

    /**
     * @return 订单描述
     */
    public String getPhoneRechargeOrderDescribe() {
        return phoneRechargeOrderDescribe;
    }

    public void setPhoneRechargeOrderDescribe(String phoneRechargeOrderDescribe) {
        this.phoneRechargeOrderDescribe = phoneRechargeOrderDescribe;
    }

    /**
     * @return 电话号码
     */
    public String getPhoneRechargeOrderPhoneNum() {
        return phoneRechargeOrderPhoneNum;
    }

    public void setPhoneRechargeOrderPhoneNum(String phoneRechargeOrderPhoneNum) {
        this.phoneRechargeOrderPhoneNum = phoneRechargeOrderPhoneNum;
    }

    /**
     * @return 充值面额
     */
    public String getPhoneRechargeOrderAmount() {
        return phoneRechargeOrderAmount;
    }

    public void setPhoneRechargeOrderAmount(String phoneRechargeOrderAmount) {
        this.phoneRechargeOrderAmount = phoneRechargeOrderAmount;
    }

    /**
     * @return 支付方式
     */
    public String getPhoneRechargeOrderPayMethod() {
        return phoneRechargeOrderPayMethod;
    }

    public void setPhoneRechargeOrderPayMethod(String phoneRechargeOrderPayMethod) {
        this.phoneRechargeOrderPayMethod = phoneRechargeOrderPayMethod;
    }

    /**
     * @return 下单时间
     */
    public String getPhoneRechargeOrderTime() {
        return phoneRechargeOrderTime;
    }

    public void setPhoneRechargeOrderTime(String phoneRechargeOrderTime) {
        this.phoneRechargeOrderTime = phoneRechargeOrderTime;
    }

    /**
     * @return 商品编号
     */
    public String getPhoneRechargeOrderProductNum() {
        return phoneRechargeOrderProductNum;
    }

    public void setPhoneRechargeOrderProductNum(String phoneRechargeOrderProductNum) {
        this.phoneRechargeOrderProductNum = phoneRechargeOrderProductNum;
    }

    /**
     * @return 商品名称
     */
    public String getPhoneRechargeOrderProductName() {
        return phoneRechargeOrderProductName;
    }

    public void setPhoneRechargeOrderProductName(String phoneRechargeOrderProductName) {
        this.phoneRechargeOrderProductName = phoneRechargeOrderProductName;
    }

    /**
     * @return 国美价
     */
    public String getPhoneRechargeOrderGomePrice() {
        return phoneRechargeOrderGomePrice;
    }

    public void setPhoneRechargeOrderGomePrice(String phoneRechargeOrderGomePrice) {
        this.phoneRechargeOrderGomePrice = phoneRechargeOrderGomePrice;
    }

    /**
     * @return 订单优惠
     */
    public String getPhoneRechargeOrderFavorable() {
        return phoneRechargeOrderFavorable;
    }

    public void setPhoneRechargeOrderFavorable(String phoneRechargeOrderFavorable) {
        this.phoneRechargeOrderFavorable = phoneRechargeOrderFavorable;
    }

    /**
     * @return 订单优惠内容
     */
    public String getPhoneRechargeOrderFavorableContent() {
        return phoneRechargeOrderFavorableContent;
    }

    public void setPhoneRechargeOrderFavorableContent(String phoneRechargeOrderFavorableContent) {
        this.phoneRechargeOrderFavorableContent = phoneRechargeOrderFavorableContent;
    }

    /**
     * @return 商品金额
     */
    public String getPhoneRechargeOrderProductPrice() {
        return phoneRechargeOrderProductPrice;
    }

    public void setPhoneRechargeOrderProductPrice(String phoneRechargeOrderProductPrice) {
        this.phoneRechargeOrderProductPrice = phoneRechargeOrderProductPrice;
    }

    /**
     * @return 余额支付
     */
    public String getPhoneRechargeOrderVirtualaccountPay() {
        return phoneRechargeOrderVirtualaccountPay;
    }

    public void setPhoneRechargeOrderVirtualaccountPay(String phoneRechargeOrderVirtualaccountPay) {
        this.phoneRechargeOrderVirtualaccountPay = phoneRechargeOrderVirtualaccountPay;
    }

    /**
     * @return 优惠金额
     */
    public String getPhoneRechargeOrderProm() {
        return phoneRechargeOrderProm;
    }

    public void setPhoneRechargeOrderProm(String phoneRechargeOrderProm) {
        this.phoneRechargeOrderProm = phoneRechargeOrderProm;
    }

    /**
     * @return 订单金额
     */
    public String getPhoneRechargeOrderPrice() {
        return phoneRechargeOrderPrice;
    }

    public void setPhoneRechargeOrderPrice(String phoneRechargeOrderPrice) {
        this.phoneRechargeOrderPrice = phoneRechargeOrderPrice;
    }

    /**
     * @return 商品skuId
     */
    public String getPhoneRechargeProductSkuId() {
        return phoneRechargeProductSkuId;
    }

    public void setPhoneRechargeProductSkuId(String phoneRechargeProductSkuId) {
        this.phoneRechargeProductSkuId = phoneRechargeProductSkuId;
    }

    /**
     * @return 是否已支付
     */
    public boolean isPayAble() {
        return isPayAble;
    }

    public void setPayAble(boolean isPayAble) {
        this.isPayAble = isPayAble;
    }

    /**
     * @return 充值区域
     */
    public String getAreaOperators() {
        return areaOperators;
    }

    public void setAreaOperators(String areaOperators) {
        this.areaOperators = areaOperators;
    }

    /**
     * @return 订单追踪信息
     */
    public List<OrderTraces> getOrderTracesList() {
        return orderTracesList;
    }

    public void setOrderTracesList(List<OrderTraces> orderTracesList) {
        this.orderTracesList = orderTracesList;
    }

    /**
     * @return 订单状态标识 充值处理中：0 ；等待支付：1 ；订单取消：2 ；充值成功：3 ； 充值失败：4
     */
    public String getPhoneRechargeOrderClientStatu() {
        return phoneRechargeOrderClientStatu;
    }

    public void setPhoneRechargeOrderClientStatu(String phoneRechargeOrderClientStatu) {
        this.phoneRechargeOrderClientStatu = phoneRechargeOrderClientStatu;
    }

    /**
     * @param json
     * @return 获取订单详情对象
     */
    public static PhoneRechargeOrderDetail parsePhoneRechargeOrderDetail(String json) {
        PhoneRechargeOrderDetail detail = new PhoneRechargeOrderDetail();
        if (json == null || json.length() == 0) {
            return null;
        }
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject object = result.getJsContent();
        detail.setPhoneRechargeOrderAmount("￥" + object.optString(JsonInterface.JK_ORDER_AMOUNT));
        detail.setPhoneRechargeOrderState(object.optString(JsonInterface.JK_ORDER_STATUS));
        detail.setPhoneRechargeOrderPhoneNum(object.optString(JsonInterface.JK_PHONE_RECHARGE_PHONE_NUM));
        detail.setPhoneRechargeOrderTime(object.optString(JsonInterface.JK_ORDER_SUBMIT_TIME));
        detail.setPhoneRechargeProductSkuId(object.optString(JsonInterface.JK_PRODUCT_SKUID));
        detail.setPhoneRechargeOrderProductNum(object.optString(JsonInterface.JK_PRODUCT_SKUID));
        detail.setPhoneRechargeOrderProductName(object.optString(JsonInterface.JK_SKU_NAME));
        detail.setPhoneRechargeOrderClientStatu(object.optString(JsonInterface.JK_PHONE_RECHARGE_ORDER_CLIENT_STATU));
        detail.setPhoneRechargeOrderProductPrice("￥"
                + object.optString(JsonInterface.JK_PHONE_RECHARGE_DENOMINATION_PRICE));
        detail.setPhoneRechargeOrderPayMethod(object.optString(JsonInterface.JK_PAY_MODE_NAME));
        detail.setPayAble(object.opt(JsonInterface.JK_ONLINE_PAY_ABLE).equals("Y") ? true : false);
        detail.setAreaOperators(object.optString(JsonInterface.JK_PHONE_RECHARGE_AREA_OPERATORS));
        detail.setPhoneRechargeOrderNum(object.optString(JsonInterface.JK_ORDER_ID_LOW));
        JSONArray array = object.optJSONArray(JsonInterface.JK_TRACES);
        List<OrderTraces> traces = new ArrayList<OrderTraces>();
        for (int i = 0,length = array.length(); i < length; i++) {
            object = array.optJSONObject(i);
            OrderTraces trace = new OrderTraces();
            trace.setDealTime(object.optString(JsonInterface.JK_DEAL_TIME));
            trace.setDealValue(object.optString(JsonInterface.JK_DEAL_VALUE));
            traces.add(trace);
        }
        detail.setOrderTracesList(traces);
        return detail;
    }

    /**
     * 订单追踪
     */
    public static class OrderTraces {
        private String dealTime;
        private String dealValue;

        public OrderTraces() {

        }

        public String getDealTime() {
            return dealTime;
        }

        public void setDealTime(String dealTime) {
            this.dealTime = dealTime;
        }

        public String getDealValue() {
            return dealValue;
        }

        public void setDealValue(String dealValue) {
            this.dealValue = dealValue;
        }

    }

    /**
     * @param orderId
     * @return 构建请求json串
     */
    public static String createOrderDetailJson(String profileId, String orderId) {
        JSONObject object = new JSONObject();
        try {
            object.put(JsonInterface.JK_ORDER_ID_LOW, orderId);
            object.put(JsonInterface.JK_PROFILE_ID, profileId);
            String sign = LoginManager.getSigns(profileId, orderId, Constants.PHONE_RECHARGE_SECRET_KEY);
            object.put(JsonInterface.JK_SIGN, sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

}
