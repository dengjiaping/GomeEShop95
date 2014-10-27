package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.gome.ecmall.bean.Allowance;
import com.gome.ecmall.bean.Attributes;
import com.gome.ecmall.bean.ConfirmReceipt;
import com.gome.ecmall.bean.Consignee;
import com.gome.ecmall.bean.Gift;
import com.gome.ecmall.bean.GomeStoreInfo;
import com.gome.ecmall.bean.Goods;
import com.gome.ecmall.bean.Invoice;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.JsonResult;
import com.gome.ecmall.bean.OrderCancel;
import com.gome.ecmall.bean.OrderDetails;
import com.gome.ecmall.bean.OrderGoods;
import com.gome.ecmall.bean.OrderOper;
import com.gome.ecmall.bean.OrderPrice;
import com.gome.ecmall.bean.Promotions;
import com.gome.ecmall.bean.SG;
import com.gome.ecmall.bean.Shipping;
import com.gome.ecmall.bean.ShopCartInfo;
import com.gome.ecmall.bean.ShopInfo;
import com.gome.ecmall.bean.ShopUsedCoupon;
import com.gome.ecmall.bean.SuiteGoods;
import com.gome.ecmall.bean.Traces;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.ecmall.util.UrlMatcher;
import com.gome.eshopnew.R;

public class OrderDetailsService implements JsonInterface {
    public static OrderCancel parseJsonCancelOrder(String json) {
        if (json == null || json.equals("")) {
            return null;
        }
        OrderCancel cancel = new OrderCancel();
        try {
            JSONObject obj = new JSONObject(json);
            cancel.setFlag(obj.optString(JK_FLAG));
            cancel.setErrorMessage(obj.optString(JsonInterface.JK_ERROR_MESSAGE));
            cancel.setFailReason((obj.optString(JsonInterface.JK_FAIL_REASON)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cancel;
    }

    public static String createCancelOrderJson(String orderId) {
        if (orderId == null || orderId.length() == 0)
            return null;
        try {
            JSONObject obj = new JSONObject();
            // obj.put(JsonInterface.JK_ORDER_ID, orderId);
            obj.put("orderId", orderId);
            return obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String createOrderDetailsJson(String orderId) {
        if (orderId == null || orderId.length() == 0)
            return null;
        try {
            JSONObject obj = new JSONObject();
            obj.put(JsonInterface.JK_ORDER_ID, orderId);
            return obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 解析订单头信息 JSON */
    public static OrderOper parseJsonOrderOper(JSONObject obj) {
        if (obj == null) {
            return null;
        }
        OrderOper orderOper = new OrderOper();
        try {
            orderOper.setOrderID(obj.optString(JsonInterface.JK_ORDER_ID));
            orderOper.setCancelAble(obj.optString(JsonInterface.JK_CANCEL_ABLE));
            orderOper.setDisplayOrderConfirmButton(obj.optString(JsonInterface.JK_DISPLAY_ORDER_CONFIRM_BUTTON));
            orderOper.setOnlinePayAble(obj.optString(JsonInterface.JK_ONLINE_PAY_ABLE));
            orderOper.setOrderSubmitTime(obj.optString(JsonInterface.JK_ORDER_SUBMIT_TIME));
            orderOper.setIsSplitedOrder(obj.optString(JsonInterface.JK_IS_SPLITED_ORDER));
            orderOper.setOrderProcess(obj.optInt(JsonInterface.JK_ORDER_PROCESS));
            orderOper.setOrderStatus(obj.optString(JsonInterface.JK_ORDER_STATUS));

            orderOper.setOrderStatusTime(obj.optString(JsonInterface.JK_ORDER_STATUS_TIME));
            orderOper.setOrderStatusDes(obj.optString(JsonInterface.JK_ORDER_STATUS_DES));
            orderOper.setPayMode(obj.optString(JsonInterface.JK_PAY_MODE));
            orderOper.setPayModeName(obj.optString(JsonInterface.JK_PAY_MODE_NAME));
            orderOper.setOrderRemark(obj.optString(JsonInterface.JK_ORDER_REMARK));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderOper;
    }

    /** 解析价格信息 */
    public static OrderPrice parseJsonOrderPrice(JSONObject obj) {
        if (obj == null)
            return null;
        try {
            OrderPrice mOrderPrice = new OrderPrice();
            mOrderPrice.setOrderPrice(obj.optString(JsonInterface.JK_ORDER_PRICE));
            mOrderPrice.setDiscountAmount(obj.optString(JsonInterface.JK_DISCOUNT_AMOUNT));
            mOrderPrice.setFreight(obj.optString(JsonInterface.JK_FREIGHT));
            mOrderPrice.setRedTicketAmount(obj.optString(JsonInterface.JK_RED_TICKET_AMOUNT));
            mOrderPrice.setBlueTicketAmount(obj.optString(JsonInterface.JK_BLUE_TICKET_AMOUNT));
            mOrderPrice.setVirtualAmount(obj.optString(JsonInterface.JK_VIRTUAL_AMOUNT));
            mOrderPrice.setPayState(obj.optString(JsonInterface.JK_ORDER_PAY_PRICE));
            mOrderPrice.setPayState(JsonInterface.JK_PAY_STATE);
            return mOrderPrice;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 解析收货人信息JSON */
    public static Consignee parseJsonConsignee(JSONObject obj) {
        if (obj == null) {
            return null;
        }
        Consignee consignee = new Consignee();
        consignee.setAddress(obj.optString(JsonInterface.JK_ADDRESS));
        consignee.setName(obj.optString(JsonInterface.JK_NAME));
        consignee.setZipCode(obj.optString(JsonInterface.JK_ZIP_CODE));
        consignee.setMobile(obj.optString(JsonInterface.JK_MOBILE));
        consignee.setPhone(obj.optString(JsonInterface.JK_PHONE));
        consignee.setEmail(obj.optString(JsonInterface.JK_EMAIL));
        /*
         * consignee .setShippingType(obj.optString(JsonInterface.JK_SHIPPING_TYPE)); consignee
         * .setShippingTime(obj.optString(JsonInterface.JK_SHIPPING_TIME)); consignee.settelBefShipping(obj
         * .optString(JsonInterface.JK_TEL_BEF_SHIPPING));
         */
        return consignee;
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

    /** 优惠信息 */
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

    /** 优惠信息对象 */
    public static Promotions parseJsonProm(JSONObject obj) {
        if (obj == null) {
            return null;
        }
        Promotions prom = new Promotions();
        prom.setPromId(obj.optString(JsonInterface.JK_PROM_ID));
        prom.setPromDesc(obj.optString(JsonInterface.JK_PROM_DESC));
        prom.setPromType(obj.optString(JsonInterface.JK_PROM_TYPE));
        prom.setPromTitle(obj.optString(JsonInterface.JK_PROM_TITLE));
        prom.setPromPrice(obj.optString(JsonInterface.JK_PROM_PRICE));

        return prom;
    }

    /** 商品清单集合 */
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

    /** 商品清单里商品对象 */
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
            goods.setSkuThumbImgUrl(UrlMatcher.getFitListThumbUrl(obj.optString(JsonInterface.JK_SKU_THUMB_IMG_URL)));
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

    /** 商品的赠品信息 */
    public static ArrayList<Gift> parseJsonGiftList(JSONArray array) {
        if (array == null || array.length() == 0) {
            return null;
        }
        ArrayList<Gift> list = new ArrayList<Gift>();
        int len = array.length();
        for (int i = 0; i < len; i++) {
            try {
                list.add(parseJsonGift(array.getJSONObject(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    /** 赠品信息对象 */
    public static Gift parseJsonGift(JSONObject obj) {
        if (obj == null)
            return null;
        try {
            Gift gift = new Gift();
            gift.setSkuID(obj.optString("skuID"));
            gift.setGoodsNo(obj.optString(JsonInterface.JK_GOODS_NO));
            gift.setSkuNo(obj.optString(JsonInterface.JK_SKU_NO));
            gift.setSkuName(obj.getString(JsonInterface.JK_SKU_NAME));
            gift.setGoodsType(obj.optInt(JsonInterface.JK_GOODS_TYPE));
            gift.setGoodsCount(obj.optInt(JsonInterface.JK_GOODS_COUNT));
            gift.setOriginalPrice(obj.optDouble(JsonInterface.JK_ORIGINAL_PRICE));
            gift.setTotalPrice(obj.optDouble(JsonInterface.JK_TOTAL_PRICE));
            return gift;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Attributes> parseJsonAttrsList(JSONArray arr) {
        if (arr != null && arr.length() > 0) {
            ArrayList<Attributes> list = new ArrayList<Attributes>();
            for (int i = 0 , size = arr.length(); i < size; i++) {
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

    /** 套购商品清单 */
    public static ArrayList<SuiteGoods> parseJsonSuiteGoodsList(JSONArray array) {
        if (array == null || array.length() == 0)
            return null;
        ArrayList<SuiteGoods> list = new ArrayList<SuiteGoods>();
        int len = array.length();
        for (int i = 0; i < len; i++) {
            list.add(parseJsonSuiteGoods(array.optJSONObject(i)));
        }
        return list;
    }

    /** 套购商品 */
    public static SuiteGoods parseJsonSuiteGoods(JSONObject obj) {
        if (obj == null) {
            return null;
        }
        SuiteGoods suiteGoods = new SuiteGoods();
        suiteGoods.setSuiteName(obj.optString(JsonInterface.JK_SUITE_NAME));
        suiteGoods.setGoodsNo(obj.optString(JsonInterface.JK_GOODS_NO));
        suiteGoods.setCommerceSelected(obj.optString(JsonInterface.JK_COMMERCE_SELECTED));
        suiteGoods.setSuitePrice(obj.optString(JsonInterface.JK_SUITE_PRICE));
        suiteGoods.setSuiteCount(obj.optString(JsonInterface.JK_SUITE_COUNT));
        suiteGoods.setSuiteSkuCount(obj.optString(JsonInterface.JK_SUITE_SKU_COUNT));
        suiteGoods.setGoodsList(parseJsonGoodsList(obj.optJSONArray(JsonInterface.JK_GOODS_LIST)));
        suiteGoods.setPromsList(parseJsonPromList(obj.optJSONArray(JsonInterface.JK_ITEM_PROM_LIST)));
        suiteGoods.setAttrList(parseJsonAttrsList(obj.optJSONArray(JsonInterface.JK_ATTRIBUTES)));
        return suiteGoods;
    }

    public static OrderDetails parseJsonOrderDetails(String json) {
        if (json == null || json.equals("") || NetUtility.NO_CONN.equals(json)) {
            return null;
        }
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        OrderDetails details = new OrderDetails();
        try {
            // JSONObject obj = new JSONObject(json);
            JSONObject obj = result.getJsContent();
            details.setOrderOper(parseJsonOrderOper(obj));
            details.setTracesList(parseJsonTraceList(obj.optJSONArray(JsonInterface.JK_TRACES)));
            details.setOrderPrice(parseJsonOrderPrice(obj));
            details.setConsignee(parseJsonConsignee(obj));
            details.setSgLists(parseJsonSGList(obj.optJSONArray(JsonInterface.JK_SG_LIST)));
            details.setOrderProms(parseJsonPromList(obj.optJSONArray(JsonInterface.JK_ORDER_PROM_LIST)));
            details.setAllowance(parseJsonAllowance(obj));
            // 把返回的列表分成两部分，一部分是国美店铺，剩下的是其他店铺集合
            ShopCartInfo gomeShopCartInfo = null;
            ArrayList<ShopCartInfo> otherShopCartInfoList = parseJsonShopCartInfo(obj
                    .optJSONArray(JsonInterface.JK_SHOP_CARTINFO_LIST));
            if (otherShopCartInfoList != null) {
                for (ShopCartInfo shopCartInfo : otherShopCartInfoList) {
                    if ("Y".equals(shopCartInfo.getIsGome())) {
                        gomeShopCartInfo = shopCartInfo;

                    }
                }
                if (gomeShopCartInfo != null) {
                    otherShopCartInfoList.remove(gomeShopCartInfo);
                }
            }
            details.setGomeShopCartInfo(gomeShopCartInfo);
            details.setOtherShopCartInfoList(otherShopCartInfoList);
            details.setIsGomePickingupOrder(obj.optString(JsonInterface.JK_IS_GOME_PICKINGUP_ORDER));
            details.setElecConfmCode(obj.optString(JsonInterface.JK_ELECCONFM_CODE));
            details.setIsFixedtimeOrder(obj.optString(JsonInterface.JK_IS_FIXED_TIME_ORDER));
            details.setIsDatedPay(obj.optString(JsonInterface.JK_IS_DATED_PAY));
            details.setSetPayTime(obj.optString(JsonInterface.JK_SET_PAY_TIME));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return details;
    }

    private static ArrayList<ShopCartInfo> parseJsonShopCartInfo(JSONArray optJSONArray) {
        if (optJSONArray == null || optJSONArray.length() == 0) {
            return null;
        }
        ArrayList<ShopCartInfo> ShopCartInfoList = new ArrayList<ShopCartInfo>();
        int len = optJSONArray.length();
        try {
            for (int i = 0; i < len; i++) {
                JSONObject jsonObject = optJSONArray.getJSONObject(i);
                // 店铺对象
                ShopCartInfo shopCartInfo = new ShopCartInfo();

                // 解析店铺信息
                shopCartInfo.setShopInfo(parseJsonShipInfo(jsonObject));

                // 解析是否是国美在线
                shopCartInfo.setIsGome(jsonObject.optString(JsonInterface.JK_ISGOME));

                // 解析商品总数
                shopCartInfo.setTotalCount(Integer.parseInt(jsonObject.optString(JsonInterface.JK_TOTAL_COUNT)));

                // 解析店铺金额小计
                shopCartInfo.setSubtotalAmount(jsonObject.optString(JsonInterface.JK_SUBTOTAL_AMOUNT));

                // 解析店铺金额合计
                shopCartInfo.setTotalAmount(jsonObject.optString(JsonInterface.JK_TOTAL_AMOUNT));

                // 解析商品清单
                JSONArray gomeGoodsListArray = jsonObject.optJSONArray(JsonInterface.JK_SHOP_GOODS_LIST);
                shopCartInfo.setGomeGoodsList(parseJsonGoodsList(gomeGoodsListArray));

                // 解析套购商品清单（只有国美有套购商品）
                JSONArray suiteGoodsListArray = jsonObject.optJSONArray(JsonInterface.JK_SUITE_GOODS_LIST);
                shopCartInfo.setSuiteGoodsList(parseJsonSuiteGoodsList(suiteGoodsListArray));

                // 解析使用优惠劵
                JSONArray shopUsedCouponListObject = jsonObject.optJSONArray(JsonInterface.JK_SHOP_USED_COUPON_LIST);
                shopCartInfo.setShopUsedCouponList(parseJsonShopUsedCouponList(shopUsedCouponListObject));

                // 解析店铺促销信息
                JSONArray shopPromListObject = jsonObject.optJSONArray(JsonInterface.JK_SHOP_PROM_LIST);
                shopCartInfo.setShopPromList(parseJsonPromList(shopPromListObject));

                // 解析发票信息
                JSONObject invoiceInfoObject = jsonObject.optJSONObject(JsonInterface.JK_INVOICEINFO);
                shopCartInfo.setInvoiceInfo(parseJsonInvoice(invoiceInfoObject));

                // 解析配送信息
                JSONObject shippingInfoObject = jsonObject.optJSONObject(JsonInterface.JK_SHIPPINGINFO);
                shopCartInfo.setShippingInfo(parseJsonShipping(shippingInfoObject));

                // 添加进店铺信息列表里
                ShopCartInfoList.add(shopCartInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return ShopCartInfoList;
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
     * 解析店铺信息
     * 
     * @param shippingInfoObject
     * @return
     */
    private static ShopInfo parseJsonShipInfo(JSONObject shipInfoObject) {
        if (shipInfoObject == null) {
            return null;
        }
        ShopInfo shopInfo = new ShopInfo();
        JSONObject ShopInfoObject = shipInfoObject.optJSONObject(JsonInterface.JK_SHOPINFO);
        if (ShopInfoObject != null) {
            shopInfo.setBbcShopId(ShopInfoObject.optString(JsonInterface.JK_BBCSHOPID));
            shopInfo.setBbcShopName(ShopInfoObject.optString(JsonInterface.JK_BBCSHOPNAME));
            shopInfo.setBbcShopImgURL(ShopInfoObject.optString(JsonInterface.JK_BBCSHOPIMGURL));
        }

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
        // shopping.setElecConfmCode(shippingInfoObject.optString(JsonInterface.JK_ELECCONFM_CODE));
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

    private static Promotions parseJsonOrderProm(JSONObject obj) {
        if (obj == null)
            return null;
        try {
            Promotions prom = new Promotions();
            prom.setPromId(obj.optString(JsonInterface.JK_PROM_ID));
            prom.setPromType(obj.optString(JsonInterface.JK_PROM_TYPE));
            prom.setPromDesc(obj.optString(JsonInterface.JK_PROM_DESC));
            prom.setPromPrice(obj.optString(JsonInterface.JK_PROM_PRICE));
            return prom;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class OrderDetailsTask extends AsyncTask<String, Void, String> {
        private Context mContext;
        private String mOrderId;
        private LoadingDialog mDialog;

        public OrderDetailsTask(Context ctx, String orderId) {
            mContext = ctx;
            mOrderId = orderId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = CommonUtility.showLoadingDialog(mContext, mContext.getString(R.string.login_loading), true, null);
        }

        @Override
        protected String doInBackground(String... params) {
            String json = createOrderDetailsJson(mOrderId);
            String result = NetUtility.sendHttpRequestByPost(Constants.URL_ORDER_MAIN_DETAIL, json);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }

    }

    public static OrderGoods parseJsonOrderGoods(JSONObject obj) {
        if (obj == null)
            return null;
        try {
            OrderGoods goods = new OrderGoods();
            goods.setSkuID(obj.optString("skuID"));
            goods.setGoodsNo(obj.optString(JsonInterface.JK_GOODS_NO));
            goods.setSkuNo(obj.optString(JsonInterface.JK_SKU_NO));
            goods.setSkuName(obj.getString(JsonInterface.JK_SKU_NAME));
            goods.setGoodsType(obj.optString(JsonInterface.JK_GOODS_TYPE));
            goods.setGoodsCount(obj.optInt(JsonInterface.JK_GOODS_COUNT));
            goods.setOriginalPrice(obj.optString(JsonInterface.JK_ORIGINAL_PRICE));
            goods.setTotalPrice(obj.optString(JsonInterface.JK_TOTAL_PRICE));

            ArrayList<Promotions> promList = new ArrayList<Promotions>();
            JSONArray promArray = obj.optJSONArray(JsonInterface.JK_PROM_LIST);
            if (promArray != null && promArray.length() > 0) {
                int len = promArray.length();
                for (int i = 0; i < len; i++) {
                    JSONObject promObj = promArray.optJSONObject(i);
                    Promotions prom = parseJsonOrderProm(promObj);
                    promList.add(prom);
                }
            }
            ArrayList<Gift> giftList = new ArrayList<Gift>();
            JSONArray giftArray = obj.optJSONArray(JsonInterface.JK_GIFT_LIST);
            if (giftArray != null && giftArray.length() > 0) {
                int len = giftArray.length();
                for (int i = 0; i < len; i++) {
                    JSONObject giftObj = giftArray.optJSONObject(i);
                    Gift gift = parseJsonGift(giftObj);
                    giftList.add(gift);
                }
            }
            goods.setPromotions(promList);
            goods.setGiftList(giftList);

            return goods;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SG parseJsonSG(JSONObject obj) {
        if (obj == null) {
            return null;
        }
        SG sg = new SG();
        sg.setSgId(obj.optString(JsonInterface.JK_SG_ID_P));
        sg.setSgStatus(obj.optString(JsonInterface.JK_SG_STATUS_P));
        sg.setSgStatusId(obj.optString(JsonInterface.JK_SG_STATUS_ID_P));
        /*
         * sg.setSGID(obj.optString(JsonInterface.JK_SG_ID));
         * sg.setSgStatusId(obj.optString(JsonInterface.JK_SG_STATUS_ID));
         * sg.setSGStatus(obj.optString(JsonInterface.JK_SG_STATUS));
         */
        return sg;
    }

    public static ArrayList<SG> parseJsonSGList(JSONArray array) {
        if (array == null || array.length() == 0) {
            return null;
        }
        ArrayList<SG> list = new ArrayList<SG>();
        int len = array.length();
        try {
            for (int i = 0; i < len; i++) {
                list.add(parseJsonSG(array.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
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
        if (obj == null)
            return null;
        Traces traces = new Traces();
        traces.setDealTime(obj.optString(JsonInterface.JK_DEAL_TIME));
        traces.setDealType(obj.optString(JsonInterface.JK_DEAL_TYPE));
        traces.setDealValue(obj.optString(JsonInterface.JK_DEAL_VALUE));

        return traces;
    }

    public static String createJsonConfirm(String orderId) {
        if (orderId == null || orderId.equals("")) {
            return null;
        }
        JSONObject obj = new JSONObject();
        try {
            obj.put("orderId", orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    public static ConfirmReceipt parseJsonConfirmReceipt(String json) {
        if (json == null || json.length() == 0)
            return null;
        ConfirmReceipt receipt = new ConfirmReceipt();

        try {
            JSONObject obj = new JSONObject(json);

            if (json.equalsIgnoreCase("FAIL")) {
                receipt.setFailReason(json);
            } else {
                receipt.setStatus(obj.optString(JsonInterface.JK_STATUS));
                receipt.setFailReason(obj.optString(JsonInterface.JK_FAIL_REASON));
                receipt.setErrorMessage(obj.optString(JsonInterface.JK_ERROR_MESSAGE));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receipt;
    }

    public static Allowance parseJsonAllowance(JSONObject jsonObj) {
        if (jsonObj == null) {
            return null;
        }
        try {
            JSONObject obj = jsonObj.optJSONObject(JsonInterface.JK_ALLOWANCE_INFO);
            Allowance a = new Allowance();
            a.setHeadType(obj.optString(JsonInterface.JK_HEAD_TYPE));
            a.setHead(obj.optString(JsonInterface.JK_HEAD));
            a.setPayerID(obj.optString(JsonInterface.JK_PAYER_ID));
            a.setBank(obj.optString(JsonInterface.JK_BANK));
            a.setAccount(obj.optString(JsonInterface.JK_ACCOUNT));
            // a.setIsForegoAllowance(obj
            // .optString(JsonInterface.JK_IS_FOREGO_ALLOWANCE));
            return a;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
