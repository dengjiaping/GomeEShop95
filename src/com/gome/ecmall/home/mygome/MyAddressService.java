package com.gome.ecmall.home.mygome;

import org.json.JSONObject;

import com.gome.ecmall.bean.JsonResult;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_Recently_address;

public class MyAddressService {
    public static String errorMessage = "";
    private static final String JK_SHOPPINGCART_RECENTLY_ADDRESS = "address";
    private static final String JK_SHOPPINGCART_RECENTLY_ID = "id";
    private static final String JK_SHOPPINGCART_RECENTLY_NAME = "name";
    private static final String JK_SHOPPINGCART_RECENTLY_CONSIGNEE = "consignee";
    private static final String JK_SHOPPINGCART_RECENTLY_ZIPCODE = "zipCode";
    private static final String JK_SHOPPINGCART_RECENTLY_MOBILE = "mobile";
    private static final String JK_SHOPPINGCART_RECENTLY_EMAIL = "email";
    public static final String JK_SHOPPINGCART_RECENTLY_PROVINCEID = "provinceId";
    public static final String JK_SHOPPINGCART_RECENTLY_CITYID = "cityId";
    public static final String JK_SHOPPINGCART_RECENTLY_DISTRICTID = "districtId";

    // /////////////////构造////////////////////

    /**
     * 构造删除收货地址
     * 
     * @return
     */
    public static String buildRequestMyGome_Address_del(String id) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("id", id);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 构造添加收货地址
     * 
     * @return
     */
    public static String buildRequestMyGome_Address_add(ShoppingCart_Recently_address consInfoAddress) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put(JK_SHOPPINGCART_RECENTLY_NAME, consInfoAddress.getConsignee());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_CONSIGNEE, consInfoAddress.getConsignee());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_PROVINCEID, consInfoAddress.getProvinceId());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_CITYID, consInfoAddress.getCityId());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_DISTRICTID, consInfoAddress.getDistrictId());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_ADDRESS, consInfoAddress.getAddress());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_ZIPCODE, consInfoAddress.getZipCode());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_MOBILE, consInfoAddress.getPhone());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_EMAIL, consInfoAddress.getEmail());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_ID, consInfoAddress.getId());
            /*
             * requestJson.put(JK_SHOPPINGCART_RECENTLY_ADDTOUSEDADDRESS, TextUtils.isEmpty(addtoUsedaddress) ? "N" :
             * addtoUsedaddress);
             */
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 构造编辑收货地址
     * 
     * @return
     */
    public static String buildRequestMyGome_Address_edit(ShoppingCart_Recently_address consInfoAddress) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put(JK_SHOPPINGCART_RECENTLY_NAME, consInfoAddress.getConsignee());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_CONSIGNEE, consInfoAddress.getConsignee());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_PROVINCEID, consInfoAddress.getProvinceId());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_CITYID, consInfoAddress.getCityId());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_DISTRICTID, consInfoAddress.getDistrictId());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_ADDRESS, consInfoAddress.getAddress());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_ZIPCODE, consInfoAddress.getZipCode());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_MOBILE, consInfoAddress.getPhone());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_EMAIL, consInfoAddress.getEmail());
            requestJson.put(JK_SHOPPINGCART_RECENTLY_ID, consInfoAddress.getId());
            /*
             * requestJson.put(JK_SHOPPINGCART_RECENTLY_ADDTOUSEDADDRESS, TextUtils.isEmpty(addtoUsedaddress) ? "N" :
             * addtoUsedaddress);
             */
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    // /////////////////解析////////////////////

    /**
     * 删除收货地址
     * 
     * @return
     */
    public static Object[] paserResponseMyGome_Address_del(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        JsonResult result = new JsonResult(json);
        if (result.isSuccess()) {
            return new Object[] { true };
        } else {
            return new Object[] { false, result.getFailReason() };
        }
    }

    /**
     * 添加收货地址
     * 
     * @return
     */
    public static Object[] paserResponseMyGome_Address_add(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        JsonResult result = new JsonResult(json);
        if (result.isSuccess()) {
            return new Object[] { true };
        } else {
            return new Object[] { false, result.getFailReason() };
        }
    }

    /**
     * 编辑收货地址
     * 
     * @return
     */
    public static Object[] paserResponseMyGome_Address_edit(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        JsonResult result = new JsonResult(json);
        if (result.isSuccess()) {
            return new Object[] { true };
        } else {
            return new Object[] { false, result.getFailReason() };
        }
    }
}
