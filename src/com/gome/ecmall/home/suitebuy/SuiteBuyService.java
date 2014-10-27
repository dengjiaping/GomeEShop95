package com.gome.ecmall.home.suitebuy;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.SuiteBuy;
import com.gome.ecmall.bean.SuiteBuyEntity;
import com.gome.ecmall.bean.SuiteBuyFilter;
import com.gome.ecmall.bean.SuiteBuyGoods;
import com.gome.ecmall.util.UrlMatcher;

public class SuiteBuyService implements JsonInterface {

    public static String createJson(int currentPage, int pageSize, String selectIndex) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(JsonInterface.JK_CURRENT_PAGE, currentPage);
            obj.put(JsonInterface.JK_PAGE_SIZE, pageSize);
            obj.put(JsonInterface.JK_SELECT_INDEX, selectIndex);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return obj.toString();
    }

    public static SuiteBuy parseJsonSuiteBuy(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }

        SuiteBuy suiteBuy = new SuiteBuy();
        try {
            JSONObject obj = new JSONObject(json);
            suiteBuy.setSelectIndexName(obj.optString(JsonInterface.JK_SELECT_INDEX_NAME));
            suiteBuy.setSuiteList(parseJsonSuiteGoodsList(obj.optJSONArray(JsonInterface.JK_SUITE_GOODS_LIST)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return suiteBuy;
    }

    public static ArrayList<SuiteBuyEntity> parseJsonSuiteGoodsList(JSONArray optJSONArray) {
        ArrayList<SuiteBuyEntity> list = null;
        if (optJSONArray != null && optJSONArray.length() > 0) {
            list = new ArrayList<SuiteBuyEntity>();
            int l = optJSONArray.length();

            for (int i = 0; i < l; i++) {
                list.add(parseJsonSuiteBuyEntity(optJSONArray.optJSONObject(i)));
            }
        }
        return list;
    }

    public static SuiteBuyEntity parseJsonSuiteBuyEntity(JSONObject obj) {
        if (obj == null) {
            return null;
        }
        SuiteBuyEntity buyEntity = new SuiteBuyEntity();
        buyEntity.setSuiteName(obj.optString(JsonInterface.JK_SUITE_NAME));
        buyEntity.setGoodsNo(obj.optString(JsonInterface.JK_GOODS_NO));
        buyEntity.setDefaultSelNum(obj.optInt(JsonInterface.JK_DEFAULT_SEL_NUM));
        buyEntity.setSuiteOrigPrice(obj.optString(JsonInterface.JK_SUITE_ORIG_PRICE));
        buyEntity.setSuitePrice(obj.optString(JsonInterface.JK_SUITE_PRICE));
        buyEntity.setDelayTime(obj.optLong(JsonInterface.JK_DELAY_TIME));
        buyEntity.setGoodsList(parseJsonGoodsList(obj.optJSONArray(JsonInterface.JK_GOODS_LIST)));
        return buyEntity;
    }

    public static ArrayList<SuiteBuyGoods> parseJsonGoodsList(JSONArray optJSONArray) {
        ArrayList<SuiteBuyGoods> list = null;
        if (optJSONArray != null && optJSONArray.length() > 0) {
            list = new ArrayList<SuiteBuyGoods>();
            int l = optJSONArray.length();
            for (int i = 0; i < l; i++) {
                list.add(parseJsonSuiteBuyGoods(optJSONArray.optJSONObject(i)));
            }
        }
        return list;
    }

    private static SuiteBuyGoods parseJsonSuiteBuyGoods(JSONObject obj) {

        if (obj == null) {
            return null;
        }
        SuiteBuyGoods suiteBuyGoods = new SuiteBuyGoods();
        suiteBuyGoods.setSkuID(obj.optString(JsonInterface.JK_SKU_ID));
        suiteBuyGoods.setGoodsNo(obj.optString(JsonInterface.JK_GOODS_NO));
        suiteBuyGoods.setSkuNo(obj.optString(JsonInterface.JK_SKU_NO));
        suiteBuyGoods.setSkuName(obj.optString(JsonInterface.JK_SKU_NAME));
        suiteBuyGoods.setGoodsType(obj.optString(JsonInterface.JK_GOODS_TYPE));
        if (suiteBuyGoods.getGoodsType() != null) {
            if (suiteBuyGoods.getGoodsType().equals("0")) {
                suiteBuyGoods.setSkuThumbImgUrl(UrlMatcher.getFitGalleryThumbUrl(obj
                        .optString(JsonInterface.JK_SKU_THUMB_IMG_URL)));
            } else {
                suiteBuyGoods.setSkuThumbImgUrl(UrlMatcher.getFitListThumbUrl(obj
                        .optString(JsonInterface.JK_SKU_THUMB_IMG_URL)));
            }
        }
        suiteBuyGoods.setSkuOriginalPrice(obj.optString(JsonInterface.JK_SKU_ORIGINAL_PRICE));
        suiteBuyGoods.setSkuSuitePrice(obj.optString(JsonInterface.JK_SKU_SUITE_PRICE));
        return suiteBuyGoods;
    }

    public ArrayList<SuiteBuyFilter> parseSuiteBuyFilterList(String json) {
        if (json == null) {
            return null;
        }
        ArrayList<SuiteBuyFilter> list = null;
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray arr = obj.optJSONArray(JsonInterface.JK_SUITE_FILTER_LIST);
            if (arr != null && arr.length() > 0) {
                list = new ArrayList<SuiteBuyFilter>();
                for (int i = 0, length = arr.length(); i < length; i++) {
                    JSONObject filterObj = arr.optJSONObject(i);
                    SuiteBuyFilter filter = new SuiteBuyFilter();
                    filter.setSelectIndex(filterObj.optString(JsonInterface.JK_SELECT_INDEX));
                    filter.setSelectIndexName(filterObj.optString(JsonInterface.JK_SELECT_INDEX_NAME));
                    list.add(filter);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;

    }

    public static ArrayList<SuiteBuyFilter> parseJsonSuiteBuyFilter(String json) {
        if (json == null) {
            return null;
        }
        ArrayList<SuiteBuyFilter> list = null;
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray arr = obj.optJSONArray(JsonInterface.JK_SUITE_FILTER_LIST);
            if (arr != null && arr.length() > 0) {
                list = new ArrayList<SuiteBuyFilter>();
                for (int i = 0, len = arr.length(); i < len; i++) {
                    JSONObject filterObj = arr.optJSONObject(i);
                    SuiteBuyFilter filter = new SuiteBuyFilter();
                    filter.setSelectIndex(filterObj.optString(JsonInterface.JK_SELECT_INDEX));
                    filter.setSelectIndexName(filterObj.optString(JsonInterface.JK_SELECT_INDEX_NAME));
                    list.add(filter);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

}
