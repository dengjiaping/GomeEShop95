package com.gome.ecmall.parse;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gome.ecmall.bean.Coupon;
import com.gome.ecmall.bean.CouponBean;
import com.gome.ecmall.bean.GetCouponBean;
import com.gome.ecmall.bean.GetCouponEntity;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.JsonResult;

/**
 * @author qinxudong 获取优惠券解析类
 * 
 */
public class GetCouponParse {

    /**
     * 解析获取优惠券页面信息
     * 
     * @param json
     * @return
     */
    public static GetCouponEntity parseJsonToGetCouponBean(String json) {
        GetCouponEntity entity = new GetCouponEntity();

        if (json == null || json.length() == 0) {
            return null;
        }
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        try {
            JSONObject jsContent = result.getJsContent();
            entity.setActivityName(jsContent.optString(JsonInterface.JK_ACTIVITY_NAME));
            entity.setActivityDesc(jsContent.optString(JsonInterface.JK_ACTIVITY_RULE));
            entity.setCouponBeanList(parseJsonToGetCouponBeanList(jsContent.optJSONArray(JsonInterface.JK_GET_COUPON_LIST)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return entity;
    }

    private static ArrayList<GetCouponBean> parseJsonToGetCouponBeanList(JSONArray array) {
        ArrayList<GetCouponBean> beanList = new ArrayList<GetCouponBean>();
        if (array == null) {
            return null;
        }

        try {
            for (int i = 0, size = array.length(); i < size; i++) {
                GetCouponBean bean = new GetCouponBean();
                JSONObject item = array.getJSONObject(i);
                bean.setType(item.optString(JsonInterface.JK_COUPON_TAG_NAME));
                bean.setCouponList(parseJsonToCouponBean(item.optJSONArray(JsonInterface.JK_COUPON_LIST)));
                beanList.add(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return beanList;
    }
    
    private static ArrayList<CouponBean> parseJsonToCouponBean(JSONArray array){
        ArrayList<CouponBean> beanList = new ArrayList<CouponBean>();
        if (array == null) {
            return null;
        }
        
        try {
            for (int i = 0, size = array.length(); i < size; i++) {
                CouponBean bean = new CouponBean();
                JSONObject item = array.getJSONObject(i);
                bean.setCouponId(item.optString(JsonInterface.JK_COUPON_ID));
                bean.setCouponName(item.optString(JsonInterface.JK_COUPON_NAME));
                bean.setCouponType(item.optString(JsonInterface.JK_COUPON_TYPE));
                bean.setDesc(item.optString(JsonInterface.JK_COUPON_DESC));
                bean.setCouponAmount(item.optString(JsonInterface.JK_COUPON_AMOUNT));
                bean.setFetchDate(item.optString(JsonInterface.JK_COUPON_DATA));
                bean.setFetchState(item.optString(JsonInterface.JK_COUPON_STATE));
                bean.setPromoId(item.optString(JsonInterface.JK_COUPON_PROMO_ID));
                bean.setCouponBgColor(item.optString(JsonInterface.JK_COUPON_BG_COLOR));
                bean.setFetchDateColor(item.optString(JsonInterface.JK_COUPON_DATA_COLOR));
                beanList.add(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        
        return beanList;
    }

}
