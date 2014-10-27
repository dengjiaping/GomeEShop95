package com.gome.ecmall.home.hotproms;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gome.ecmall.bean.HotPromGoods;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.Promotions;

public class HotPromotionService implements JsonInterface {
    
    public static String createJson( int currentPage, int pageSize) {
        JSONObject obj = new JSONObject();
        try {
            // obj.put(JsonInterface.JK_PROM_TYPE, promType);
            obj.put(JsonInterface.JK_CURRENT_PAGE, currentPage);
            obj.put(JsonInterface.JK_PAGE_SIZE, pageSize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    public static ArrayList<HotPromGoods> parseHotPromGoodsList(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        ArrayList<HotPromGoods> list = new ArrayList<HotPromGoods>() ;
        JSONObject obj;
        try {
            obj = new JSONObject(json);
            JSONArray arr = obj.optJSONArray(JsonInterface.JK_GOODS_LIST);
            if (arr != null) {
                int len = arr.length();
                if (len > 0) {
                    for (int i = 0; i < len; i++) {
                        HotPromGoods goods = parseHotPromGoods(arr.optJSONObject(i));
                        list.add(goods);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static HotPromGoods parseHotPromGoods(JSONObject obj) {
        if (obj == null) {
            return null;
        }
        HotPromGoods goods = new HotPromGoods();
        goods.setSkuID(obj.optString(JsonInterface.JK_SKU_ID));
        goods.setGoodsNo(obj.optString(JsonInterface.JK_GOODS_NO));
        goods.setSkuNo(obj.optString(JsonInterface.JK_SKU_NO));
        goods.setSkuName(obj.optString(JsonInterface.JK_SKU_NAME));
        goods.setOriginalPrice(obj.optString(JsonInterface.JK_ORIGINAL_PRICE));
        goods.setPromPrice(obj.optString(JsonInterface.JK_PROM_PRICE));
        goods.setPromWords(obj.optString(JsonInterface.JK_PROM_WORDS));
        goods.setAd(obj.optString(JsonInterface.JK_AD));
        goods.setRushBuyItemId(obj.optString(JsonInterface.JK_RUSH_BUY_ITEM_ID));
        goods.setPromList(parsePromList(obj.optJSONArray(JsonInterface.JK_PROM_LIST)));
        goods.setSkuThumbImgUrl(obj.optString(JK_SKU_THUMB_IMG_URL));
        goods.setFavoriteId(obj.optString(JsonInterface.JK_FAVORITE_ID));
        goods.setGoodsShareUrl(obj.optString(JsonInterface.JK_GOODS_SHARE_URL));
        return goods;
    }

    private static ArrayList<Promotions> parsePromList(JSONArray array) {
        if (array == null) {
            return null;
        }
        ArrayList<Promotions> list = null;
        int len = array.length();
        if (len > 0) {
            list = new ArrayList<Promotions>();
            for (int i = 0; i < len; i++) {
                list.add(parsePromotion(array.optJSONObject(i)));
            }
        }
        return list;
    }

    public static Promotions parsePromotion(JSONObject jsonObj) {
        if (jsonObj == null) {
            return null;
        }
        Promotions proms = new Promotions();
        proms.setPromType(jsonObj.optString(JsonInterface.JK_PROM_TYPE));
        proms.setPromDesc(jsonObj.optString(JsonInterface.JK_PROM_DESC));
        return proms;
    }

}
