package com.gome.ecmall.home.mygome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gome.ecmall.bean.JsonInterface;

public class OrderGoodsAppraiseService {

    public static ArrayList<Map<String, Object>> parseJson(String json) {
        if (json == null || json.length() == 0)
            return null;
        try {
            JSONObject obj = new JSONObject(json);

            JSONArray arr = obj.isNull(JsonInterface.JK_GOODS_LIST) ? null : obj
                    .getJSONArray(JsonInterface.JK_GOODS_LIST);

            if (arr != null && arr.length() > 0) {
                ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                for (int i = 0 , length = arr.length(); i < length; i++) {
                    JSONObject subObj = arr.getJSONObject(i);

                    String skuID = (String) (subObj.isNull("skuID") ? null : subObj.get("skuID"));
                    String goodsNo = (String) (subObj.isNull(JsonInterface.JK_GOODS_NO) ? null : subObj
                            .get(JsonInterface.JK_GOODS_NO));

                    String skuName = (String) (subObj.isNull(JsonInterface.JK_SKU_NAME) ? null : subObj
                            .get(JsonInterface.JK_SKU_NAME));

                    String productImgURL = (String) (subObj.isNull(JsonInterface.JK_PRODUCT_IMG_URL) ? null : subObj
                            .get(JsonInterface.JK_PRODUCT_IMG_URL));
                    String userReviewId = (String) (subObj.isNull(JsonInterface.JK_USER_REVIEW_ID) ? null : subObj
                            .get(JsonInterface.JK_USER_REVIEW_ID));

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(JsonInterface.JK_GOODS_NO, goodsNo);
                    map.put(JsonInterface.JK_SKU_ID, skuID);
                    map.put(JsonInterface.JK_SKU_NAME, skuName);
                    map.put(JsonInterface.JK_PRODUCT_IMG_URL, productImgURL);
                    map.put(JsonInterface.JK_USER_REVIEW_ID, userReviewId);
                    list.add(map);
                }
                return list;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
