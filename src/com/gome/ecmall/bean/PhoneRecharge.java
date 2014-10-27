package com.gome.ecmall.bean;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

/**
 * 充值实体
 * 
 * @author liuyang-ds
 * 
 */
public class PhoneRecharge {

    // 所有充值面额价格区间
    // public static final String URL_ALL_PRODUCT = Constants.PHONE_RECHARGE_URL+"getProduct.ashx";
    // public static final String URL_NUM_PRICE = Constants.PHONE_RECHARGE_URL+"searchMobile.ashx";
    // public static final String URL_SUBMIT_ORDER = Constants.PHONE_RECHARGE_URL+"subOrder.ashx";
    // public static final String URL_ORDER_MESSAGE = Constants.PHONE_RECHARGE_URL+"pay.ashx";
    // public static final String KEY = "94fff89e-9ac4-41b1-be0d-e9714b891dcd";
    public static final String MONEY_10 = "10.00";
    public static final String MONEY_20 = "20.00";
    public static final String MONEY_30 = "30.00";
    public static final String MONEY_50 = "50.00";
    public static final String MONEY_100 = "100.00";
    public static final String MONEY_200 = "200.00";
    public static final String NUM = "num";
    public static final String NUMLOCATION = "numLocation";
    public static final String SELECTEDMONEY = "selectedMoney";
    public static final String PAYMONEY = "payMoney";
    public static final String FROMPAGE = "fromPage";
    public static final String GOODNAME = "goodName";
    public static final String SKUID = "skuId";
    public static final String ORDERNUM = "orderNum";
    public static final String PAYTYPE = "payType";
    private static final String JK_MOBILE = "mobile";
    private static final String JK_SIGN = "sign";
    private static final String JK_DATA = "data";
    private static final String JK_SKUID = "skuId";
    private static final String JK_DENOMINATION_PRICE = "denominationPrice";
    private static final String JK_SALE_PRICES = "salePrices";
    private static final String JK_PRICE = "price";
    private static final String JK_OPERATORS = "operators";
    private static final String JK_AREA = "area";
    private static final String JK_PROFILE_ID = "profileID";
    private static final String JK_LOGIN_NAME = "loginName";
    private static final String JK_ORDERID = "orderId";
    private static final String JK_PAY_TYPE = "payType";
    private static final String JK_ORDER_SUBMIT_TIME = "orderSubmitTime";
    private static final String JK_SKU_DESC = "skuDesc";

    private static final String JK_ORDER_ID = "orderId";
    private static final String JK_PAYID = "payId";
    private static final String JK_SKU_NAME = "skuName";
    private static final String JK_SKU_BODY = "skuBody";
    private static final String JK_ORDER_AMOUNT = "orderAmount";
    private static final String JK_DATE = "date";
    public static String failReason = "";

    /**
     * 组建获取充值面额价格请求字符串
     * 
     * @param sign
     * @return
     */
    public static String createRequestNumLocationAndPricesJson(String mobile, String sign) {
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put(JK_MOBILE, mobile);
            requestJson.put(JK_SIGN, sign);
            return requestJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 号码归属地和充值面额的国美价
     */
    public static class NumlocationAndPrice {
        private String operators;
        private String area;
        private ArrayList<PriceRegion> allPrices;

        public String getOperators() {
            return operators;
        }

        public void setOperators(String operators) {
            this.operators = operators;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public ArrayList<PriceRegion> getAllPrices() {
            return allPrices;
        }

        public void setAllPrices(ArrayList<PriceRegion> allPrices) {
            this.allPrices = allPrices;
        }

    }

    /**
     * 充值面额价钱区间
     * 
     * @author liuyang-ds
     * 
     */
    public static class PriceRegion {
        private String denominationPrice;
        private String salePrices;
        private String skuId;

        public String getDenominationPrice() {
            return denominationPrice;
        }

        public void setDenominationPrice(String denominationPrice) {
            this.denominationPrice = denominationPrice;
        }

        public String getSalePrices() {
            return salePrices;
        }

        public void setSalePrices(String salePrices) {
            this.salePrices = salePrices;
        }

        public String getSkuId() {
            return skuId;
        }

        public void setSkuId(String skuId) {
            this.skuId = skuId;
        }
    }

    /**
     * 订单提交成功信息
     */
    public static class RechargeOrder {
        private String orderId;
        private String orderPrice;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getOrderPrice() {
            return orderPrice;
        }

        public void setOrderPrice(String orderPrice) {
            this.orderPrice = orderPrice;
        }

    }

    /**
     * 在线支付订单信息获取
     * 
     * @author liuyang-ds
     * 
     */
    public static class PayOrderMessage {
        private String orderId;
        private String orderAmount;
        private String orderSubmitTime;
        private String skuName;
        private String skuDesc;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getOrderAmount() {
            return orderAmount;
        }

        public void setOrderAmount(String orderAmount) {
            this.orderAmount = orderAmount;
        }

        public String getOrderSubmitTime() {
            return orderSubmitTime;
        }

        public void setOrderSubmitTime(String orderSubmitTime) {
            this.orderSubmitTime = orderSubmitTime;
        }

        public String getSkuName() {
            return skuName;
        }

        public void setSkuName(String skuName) {
            this.skuName = skuName;
        }

        public String getSkuDesc() {
            return skuDesc;
        }

        public void setSkuDesc(String skuDesc) {
            this.skuDesc = skuDesc;
        }

    }

    /**
     * 解析充值面额价格区间list
     * 
     * @param response
     * @return
     */
    public static ArrayList<PriceRegion> parsePhoneRechargeList(String response) {
        JsonResult result = new JsonResult(response);
        if (!result.isSuccess()) {
            failReason = result.getFailReason();
            return null;
        }
        JSONObject content = result.getJsContent();
        try {
            JSONArray priceRegionArray = content.optJSONArray(JK_DATA);
            if (priceRegionArray != null) {
                ArrayList<PriceRegion> priceRegionList = new ArrayList<PriceRegion>();
                for (int i = 0, length = priceRegionArray.length(); i < length; i++) {
                    JSONObject item = priceRegionArray.optJSONObject(i);
                    PriceRegion priceRegion = new PriceRegion();
                    priceRegion.setDenominationPrice(item.optString(JK_DENOMINATION_PRICE));
                    priceRegion.setSalePrices(item.optString(JK_SALE_PRICES));
                    priceRegion.setSkuId(item.optString(JK_SKUID));

                    priceRegionList.add(priceRegion);
                }
                return priceRegionList;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 解析归属地以及价格信息
     * 
     * @param response
     * @return
     */
    public static NumlocationAndPrice parseNumlocationAndPrice(String response) {

        JsonResult result = new JsonResult(response);
        failReason = "";
        if (!result.isSuccess()) {
            failReason = result.getFailReason();
            return null;
        }
        JSONObject content = result.getJsContent();
        try {
            if (content != null) {
                NumlocationAndPrice numlocationAndPrice = new NumlocationAndPrice();
                numlocationAndPrice.setOperators(content.optString(JK_OPERATORS));
                numlocationAndPrice.setArea(content.optString(JK_AREA));
                JSONArray priceRegionArray = content.optJSONArray(JK_DATA);
                if (priceRegionArray != null) {
                    ArrayList<PriceRegion> priceRegionList = new ArrayList<PriceRegion>();
                    for (int i = 0, length = priceRegionArray.length(); i < length; i++) {
                        JSONObject item = priceRegionArray.optJSONObject(i);
                        PriceRegion priceRegion = new PriceRegion();
                        priceRegion.setDenominationPrice(item.optString(JK_DENOMINATION_PRICE));
                        priceRegion.setSalePrices(item.optString(JK_PRICE));
                        priceRegion.setSkuId(item.optString(JK_SKUID));

                        priceRegionList.add(priceRegion);
                    }
                    numlocationAndPrice.setAllPrices(priceRegionList);
                }
                return numlocationAndPrice;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 创建确认订单请求字符串
     * 
     * @param profileID
     * @param loginName
     * @param mobile
     * @param skuId
     * @param sign
     * @return
     */
    public static String createConfirmOrderJson(String profileID, String loginName, String mobile, String skuId,
            String sign) {
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put(JK_PROFILE_ID, profileID);
            requestJson.put(JK_LOGIN_NAME, loginName);
            requestJson.put(JK_MOBILE, mobile);
            requestJson.put(JK_SKUID, skuId);
            requestJson.put(JK_SIGN, sign);
            return requestJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static RechargeOrder parseConfirmOrder(String response) {
        JsonResult result = new JsonResult(response);
        failReason = "";
        if (!result.isSuccess()) {
            failReason = result.getFailReason();
            return null;
        }
        JSONObject content = result.getJsContent();
        try {
            if (content != null) {
                RechargeOrder rechargeOrder = new RechargeOrder();
                rechargeOrder.setOrderId(content.optString(JK_ORDERID));
                rechargeOrder.setOrderPrice(content.optString(JK_PRICE));
                return rechargeOrder;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 价钱换算
     */
    public static String priceConversion(String fen) {
        String yuan = "";
        if (!TextUtils.isEmpty(fen)) {
            double f;
            try {
                f = Double.parseDouble(fen);
                f = (double) f / 100;
                BigDecimal bg = new BigDecimal(f);
                double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                DecimalFormat df = null;
                if (fen.length() < 3) {
                    df = new DecimalFormat("0.00");
                } else {
                    df = new DecimalFormat("#.00");
                }
                yuan = df.format(f1);
                return yuan;
            } catch (NumberFormatException e) {
                yuan = fen;
                e.printStackTrace();
            }

        }
        return yuan;

    }

    /**
     * 组装在线支付订单信息获取接口
     * 
     * @param orderId
     * @param profileID
     * @param payType
     * @param sign
     * @return
     */
    public static String reqOnLinePayOrderMessage(String orderId, String profileID, int payType) {
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put(JK_PROFILE_ID, profileID);
            requestJson.put(JK_ORDERID, orderId);
            requestJson.put(JK_PAYID, payType);
            // requestJson.put(JK_SIGN, sign);
            return requestJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
