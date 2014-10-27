package com.gome.ecmall.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 降价通知【业务类】
 */
public class BelowPrice implements JsonInterface {

    private static final String JK_PROVINCEID = "provinceId";
    private static final String JK_DISTRICTID = "districtId";
    private static String errorMessage = "";

    /**
     * 降价通知请求数据
     * 
     * @param goodsNo
     * @param skuID
     * @param mobile
     * @param email
     * @return
     */
    public static String createRequestBelowPriceJson(String goodsNo, String skuID, String mobile, String email) {
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put(JK_GOODS_NO, goodsNo);
            requestJson.put(JK_SKU_ID, skuID);
            requestJson.put(JK_MOBILE, mobile);
            requestJson.put(JK_EMAIL, email);
            return requestJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 降价通知返回数据
     * 
     * @param json
     * @return
     */
    public static String parseBelowPriceRes(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject content = result.getJsContent();
        try {
            String jsonResult = content.optString(JK_RESULT);
            setErrorMessage(content.optString(JK_ERROR_MESSAGE));
            return jsonResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getErrorMessage() {
        return errorMessage;
    }

    public static void setErrorMessage(String errorMessage) {
        BelowPrice.errorMessage = errorMessage;
    }

    public static String createRequestArriNoticeJson(String divisionCode, String cityCode, String districtCode,
            String goodsNo, String skuID, String mobile, String email) {
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put(JK_PROVINCEID, divisionCode);
            requestJson.put(JK_CITY_ID, cityCode);
            requestJson.put(JK_DISTRICTID, districtCode);
            requestJson.put(JK_GOODS_NO, goodsNo);
            requestJson.put(JK_SKU_ID, skuID);
            requestJson.put(JK_MOBILE, mobile);
            requestJson.put(JK_EMAIL, email);
            return requestJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
