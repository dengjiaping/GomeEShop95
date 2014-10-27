package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gome.ecmall.bean.Attributes;
import com.gome.ecmall.bean.Gift;
import com.gome.ecmall.bean.GomeStoreInfo;
import com.gome.ecmall.bean.Goods;
import com.gome.ecmall.bean.Invoice;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.JsonResult;
import com.gome.ecmall.bean.Promotions;
import com.gome.ecmall.bean.Shipping;
import com.gome.ecmall.bean.ShopInfo;
import com.gome.ecmall.bean.ShopUsedCoupon;
import com.gome.ecmall.bean.Traces;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.NetUtility;

public class SubOrderDetailsService {
    static final String TAG = "SubOrderDetailsService";

    public static String createRequest(String orderID, String sgID) {
        if (orderID == null || orderID.equals(""))
            return null;
        if (sgID == null || orderID.equals(""))
            return null;
        JSONObject obj = new JSONObject();
        try {
            obj.put(JsonInterface.JK_ORDER_ID, orderID);
            obj.put("sgID", sgID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    public static SubOrderDetails parseJsonSubOrderDeatils(String json) {
        if (json == null || json.equals("") || NetUtility.NO_CONN.equals(json)) {
            return null;
        }
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        SubOrderDetails details = new SubOrderDetails();
        try {
            // JSONObject obj = new JSONObject(json);
            JSONObject obj = result.getJsContent();
            String isSuccess = obj.optString(JsonInterface.JK_IS_SUCCESS);
            details.setIsSuccess(isSuccess);
            if (isSuccess.equalsIgnoreCase(JsonInterface.JV_YES)) {
                details.setSgID(obj.optString(JsonInterface.JK_SG_ID_S));
                details.setSgStatus(obj.optString(JsonInterface.JK_SG_STATUS_S));
                details.setSgStatusId(obj.optString(JsonInterface.JK_SG_STATUS_ID_S));
                details.setSgStatusTime(obj.optString(JsonInterface.JK_SG_STATUS_TIME));
                details.setSgProcess(obj.optInt(JsonInterface.JK_SG_PROCESS));
                details.setSgAmount(obj.optString(JsonInterface.JK_SG_AMOUNT));
                details.setSgPayAmount(obj.optString(JsonInterface.JK_SG_PAY_AMOUNT));
                details.setDeliveryMode(obj.optString(JsonInterface.JK_DELIVERY_MODE));
                details.setFreight(obj.optString(JsonInterface.JK_FREIGHT));
                details.setPrePayment(obj.optString(JsonInterface.JK_PRE_PAYMENT));
                details.setDiscountPayment(obj.optString(JsonInterface.JK_DISCOUNT_PAYMENT));
                details.setSgSubmitTime(obj.optString(JsonInterface.JK_SG_SUBMIT_TIME));
                details.setAcceptanceCode(obj.optString(JsonInterface.JK_SG_ACCEPTANCE_CODE));
                details.setTracesList(parseJsonTraceList(obj.optJSONArray(JsonInterface.JK_TRACES)));
                details.setShopInfo(parseJsonShopInfo(obj.optJSONObject(JsonInterface.JK_SHOPINFO)));
                details.setIsGome(obj.optString(JsonInterface.JK_ISGOME));
                details.setTotalCount(obj.optInt(JsonInterface.JK_TOTAL_COUNT));
                details.setSubtotalAmount(obj.optString(JsonInterface.JK_SUBTOTAL_AMOUNT));
                details.setTotalAmount(obj.optString(JsonInterface.JK_TOTAL_AMOUNT));
                details.setGoodsList(parseJsonGoodsList(obj.optJSONArray(JsonInterface.JK_GOODS_LIST)));
                details.setShippingPromList(parseJsonShippingPromList(obj
                        .optJSONArray(JsonInterface.JK_SHIPPING_PROM_LIST)));
                details.setShopUsedCouponList(parseJsonShopUsedCouponList(obj
                        .optJSONArray(JsonInterface.JK_SHIPPING_USED_COUPON_LIST)));
                details.setInvoice(parseJsonInvoice(obj.optJSONObject(JsonInterface.JK_INVOICEINFO)));
                details.setShipping(parseJsonShipping(obj.optJSONObject(JsonInterface.JK_SHIPPINGINFO)));
                details.setIsGomePickingupOrder(obj.optString(JsonInterface.JK_IS_GOME_PICKINGUP_ORDER));
            } else if (isSuccess.equalsIgnoreCase(JsonInterface.JV_NO)) {
                details.setFail(obj.optString(JsonInterface.JK_FAIL_REASON));
            } else {
                return null;
            }
            return details;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析使用优惠劵列表
     * 
     * @param shopUsedCouponListObject
     * @return
     */
    private static ArrayList<ShopUsedCoupon> parseJsonShopUsedCouponList(JSONArray array) {
        if (array == null || array.length() == 0)
            return null;
        ArrayList<ShopUsedCoupon> list = new ArrayList<ShopUsedCoupon>();
        int len = array.length();
        try {
            for (int i = 0; i < len; i++) {
                JSONObject obj = array.getJSONObject(i);
                list.add(parseJsonShopUsedCoupon(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 解析单张优惠劵
     * 
     * @param obj
     * @return
     */
    private static ShopUsedCoupon parseJsonShopUsedCoupon(JSONObject obj) {
        if (obj == null) {
            return null;
        }
        ShopUsedCoupon suc = new ShopUsedCoupon();
        suc.setIsGomeCoupon(obj.optString(JsonInterface.JK_IS_GOME_COUPON));
        suc.setName(obj.optString(JsonInterface.JK_NAME));
        suc.setAmount(obj.optString(JsonInterface.JK_AMOUNT));
        return suc;
    }

    /**
     * 解析配送单的促销信息
     * 
     * @param array
     * @return
     */
    public static ArrayList<Promotions> parseJsonShippingPromList(JSONArray array) {
        if (array == null || array.length() == 0) {
            return null;
        }
        ArrayList<Promotions> shippingPromList = new ArrayList<Promotions>();
        int len = array.length();
        try {
            for (int i = 0; i < len; i++) {
                shippingPromList.add(parseJsonShippingProm(array.getJSONObject(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shippingPromList;
    }

    /**
     * 解析单挑配送单促销信息
     * 
     * @param obj
     * @return
     */
    public static Promotions parseJsonShippingProm(JSONObject obj) {
        if (obj == null || obj.length() == 0)
            return null;
        Promotions prom = new Promotions();
        prom.setPromId(obj.optString(JsonInterface.JK_PROM_ID));
        prom.setPromDesc(obj.optString(JsonInterface.JK_PROM_DESC));
        prom.setPromType(obj.optString(JsonInterface.JK_PROM_TYPE));
        prom.setPromTitle(obj.optString(JsonInterface.JK_PROM_TITLE));
        prom.setPromPrice(obj.optString(JsonInterface.JK_PROM_PRICE));
        return prom;
    }

    public static ArrayList<Traces> parseJsonTraceList(JSONArray array) {
        if (array == null || array.length() == 0) {
            return null;
        }
        ArrayList<Traces> list = new ArrayList<Traces>();
        int len = array.length();
        try {
            for (int i = 0; i < len; i++) {
                list.add(parseJsonTraces(array.getJSONObject(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static Traces parseJsonTraces(JSONObject obj) {
        if (obj == null || obj.length() == 0)
            return null;
        Traces traces = new Traces();
        traces.setDealTime(obj.optString(JsonInterface.JK_DEAL_TIME));
        traces.setDealType(obj.optString(JsonInterface.JK_DEAL_TYPE));
        traces.setDealValue(obj.optString(JsonInterface.JK_DEAL_VALUE));
        return traces;
    }

    /**
     * 解析店铺信息
     * 
     * @param jsonObject
     * @return
     */
    public static ShopInfo parseJsonShopInfo(JSONObject jsonObject) {
        if (jsonObject == null || jsonObject.length() == 0)
            return null;
        ShopInfo shopInfo = new ShopInfo();
        shopInfo.setBbcShopId(jsonObject.optString(JsonInterface.JK_BBCSHOPID));
        shopInfo.setBbcShopName(jsonObject.optString(JsonInterface.JK_BBCSHOPNAME));
        shopInfo.setBbcShopImgURL(jsonObject.optString(JsonInterface.JK_BBCSHOPIMGURL));
        return shopInfo;
    }

    /**
     * 解析配送信息
     * 
     * @param shippingInfoObject
     * @return
     */
    private static Shipping parseJsonShipping(JSONObject shippingInfoObject) {
        if (shippingInfoObject == null) {
            return null;
        }
        Shipping shopping = new Shipping();
        shopping.setShippingType(shippingInfoObject.optString(JsonInterface.JK_SHIPPING_TYPE));
        shopping.setShippingFreight(shippingInfoObject.optString(JsonInterface.JK_SHIPPING_FREIGHT));
        shopping.setShippingTime(shippingInfoObject.optString(JsonInterface.JK_SHIPPING_TIME));
        shopping.setTelBefShipping(shippingInfoObject.optString(JsonInterface.JK_TEL_BEF_SHIPPING));
        shopping.setElecConfmCode(shippingInfoObject.optString(JsonInterface.JK_ELECCONFM_CODE));
        JSONObject gomeStoreInfoObject = null;
        gomeStoreInfoObject = shippingInfoObject.optJSONObject(JsonInterface.JK_GOME_STORE_INFO);
        /*
         * try { gomeStoreInfoObject = shippingInfoObject.getJSONObject(JsonInterface.JK_GOME_STORE_INFO); } catch
         * (JSONException e) { e.printStackTrace(); }
         */
        shopping.setGomeStoreInfo(parseJsonGomeStoreInfo(gomeStoreInfoObject));
        return shopping;
    }

    /**
     * 解析门店自提信息
     * 
     * @param gomeStoreInfoObject
     * @return
     */
    private static GomeStoreInfo parseJsonGomeStoreInfo(JSONObject gomeStoreInfoObject) {
        if (gomeStoreInfoObject == null) {
            return null;
        }
        GomeStoreInfo gomeStoreInfo = new GomeStoreInfo();
        gomeStoreInfo.setStoreId(gomeStoreInfoObject.optString(JsonInterface.JK_STORE_ID));
        gomeStoreInfo.setStoreName(gomeStoreInfoObject.optString(JsonInterface.JK_STORE_NAME));
        gomeStoreInfo.setStoreAddress(gomeStoreInfoObject.optString(JsonInterface.JK_STORE_ADDRESS));
        gomeStoreInfo.setStorePhone(gomeStoreInfoObject.optString(JsonInterface.JK_STORE_PHONE));
        return gomeStoreInfo;
    }

    /** 解析发票信息JSON */
    private static Invoice parseJsonInvoice(JSONObject obj) {
        if (obj == null) {
            return null;
        }
        Invoice invoice = new Invoice();
        invoice.setInvoiceType(obj.optString(JsonInterface.JK_INVOICE_TYPE));
        invoice.setInvoiceTitleType(obj.optString(JsonInterface.JK_INVOICE_TITLE_TYPE));
        invoice.setInvoiceTitle(obj.optString(JsonInterface.JK_INVOICE_TITLE));
        invoice.setInvoiceContent(obj.optString(JsonInterface.JK_INVOICE_CONTENT));
        invoice.setCompanyName(obj.optString(JsonInterface.JK_COMPANY_NAME));
        invoice.setTaxPayerNo(obj.optString(JsonInterface.JK_TAX_PAYER_NO));
        invoice.setRegAdress(obj.optString(JsonInterface.JK_REG_ADDRESS));
        invoice.setRegTel(obj.optString(JsonInterface.JK_REG_TEL));
        invoice.setBankName(obj.optString(JsonInterface.JK_BANK_NAME));
        invoice.setBankAccount(obj.optString(JsonInterface.JK_BANK_ACCOUNT));
        return invoice;
    }

    /**
     * 解析商品清单
     * 
     * @param array
     * @return
     */
    public static ArrayList<Goods> parseJsonGoodsList(JSONArray array) {
        if (array == null || array.length() == 0) {
            return null;
        }
        ArrayList<Goods> list = new ArrayList<Goods>();
        int len = array.length();
        try {
            for (int i = 0; i < len; i++) {
                list.add(parseJsonGoods(array.getJSONObject(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Goods parseJsonGoods(JSONObject obj) {
        if (obj == null) {
            return null;
        }
        Goods goods = new Goods();
        try {
            goods.setSkuID(obj.optString(JsonInterface.JK_SKU_ID));
            goods.setGoodsNo(obj.optString(JsonInterface.JK_GOODS_NO));
            goods.setSkuNo(obj.optString(JsonInterface.JK_SKU_NO));
            goods.setSkuName(obj.optString(JsonInterface.JK_SKU_NAME));
            goods.setCommerceItemID(obj.optString(JsonInterface.JK_COMMERCE_ITEM_ID));
            goods.setSkuThumbImgUrl(obj.optString(JsonInterface.JK_SKU_THUMB_IMG_URL));
            goods.setCommerceItemID(obj.optString(JsonInterface.JK_COMMERCE_ITEM_ID));
            goods.setGoodsType(obj.optString(JsonInterface.JK_GOODS_TYPE));
            goods.setGoodsCount(obj.optInt(JsonInterface.JK_GOODS_COUNT));
            goods.setTotalPrice(obj.optString(JsonInterface.JK_TOTAL_PRICE));
            goods.setOriginalPrice(obj.optString(JsonInterface.JK_ORIGINAL_PRICE));
            goods.setPromList(parseJsonPromList(obj.optJSONArray(JsonInterface.JK_ITEM_PROM_LIST)));
            // goods.setGiftList(parseJsonGiftList(obj.optJSONArray(JsonInterface.JK_GIFT_LIST)));
            goods.setAttrList(parseJsonAttrsList(obj.optJSONArray(JsonInterface.JK_ATTRIBUTES)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goods;
    }

    /**
     * 解析商品属性
     * 
     * @param arr
     * @return
     */
    public static ArrayList<Attributes> parseJsonAttrsList(JSONArray arr) {
        if (arr != null && arr.length() > 0) {
            ArrayList<Attributes> list = new ArrayList<Attributes>();
            for (int i = 0 , length = arr.length(); i < length; i++) {
                list.add(parseJsonAttributes(arr.optJSONObject(i)));
            }
            return list;
        }
        return null;
    }

    public static Attributes parseJsonAttributes(JSONObject obj) {
        if (obj != null) {
            Attributes att = new Attributes();
            att.setName(obj.optString(JsonInterface.JK_NAME));
            att.setValue(obj.optString(JsonInterface.JK_VALUE));
            return att;
        }
        return null;
    }

    public static ArrayList<Promotions> parseJsonPromList(JSONArray array) {
        if (array == null || array.length() == 0)
            return null;
        ArrayList<Promotions> list = new ArrayList<Promotions>();
        int len = array.length();
        try {
            for (int i = 0; i < len; i++) {
                JSONObject obj = array.getJSONObject(i);
                list.add(parseJsonProm(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static Promotions parseJsonProm(JSONObject obj) {
        if (obj == null) {
            return null;
        }
        Promotions prom = new Promotions();
        prom.setPromId(obj.optString(JsonInterface.JK_PROM_ID));
        prom.setPromDesc(obj.optString(JsonInterface.JK_PROM_DESC));
        prom.setPromType(obj.optString(JsonInterface.JK_PROM_TYPE));
        prom.setPromPrice(obj.optString(JsonInterface.JK_PROM_PRICE));
        return prom;
    }

    public static ArrayList<Gift> parseJsonGiftList(JSONArray array) {
        if (array == null) {
            return null;
        }
        ArrayList<Gift> list = new ArrayList<Gift>();
        int len = array.length();
        for (int i = 0; i < len; i++) {
            list.add(parseJsonGift(array.optJSONObject(i)));
        }
        return list;
    }

    public static Gift parseJsonGift(JSONObject obj) {
        if (obj == null) {
            return null;
        }
        Gift gift = new Gift();
        gift.setSkuID(obj.optString(JsonInterface.JK_SKU_ID));
        gift.setGoodsNo(obj.optString(JsonInterface.JK_GOODS_NO));
        gift.setSkuNo(obj.optString(JsonInterface.JK_SKU_NO));
        gift.setSkuName(obj.optString(JsonInterface.JK_SKU_NAME));
        gift.setGoodsType(obj.optInt(JsonInterface.JK_GOODS_TYPE));
        gift.setGoodsCount(obj.optInt(JsonInterface.JK_GOODS_COUNT));
        gift.setOriginalPrice(obj.optDouble(JsonInterface.JK_ORIGINAL_PRICE));
        gift.setTotalPrice(obj.optDouble(JsonInterface.JK_TOTAL_PRICE));
        BDebug.d(TAG, "obj.optInt(JsonInterface.JK_GOODS_TYPE)===" + obj.optInt(JsonInterface.JK_GOODS_TYPE));
        return gift;
    }
}
