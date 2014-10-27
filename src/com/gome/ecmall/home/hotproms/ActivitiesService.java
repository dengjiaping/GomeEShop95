package com.gome.ecmall.home.hotproms;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gome.ecmall.bean.ActivityEntity;
import com.gome.ecmall.bean.HotPromGoods;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.Promotions;
import com.gome.ecmall.bean.RushBuyGoods;

/**
 * 活动专题操作类
 * 
 * @author Administrator
 * 
 */
public class ActivitiesService {
    /** 普通活动 */
    public static final int ACTIVITY_TYPE_COMMON_ACTIVITY = 0;
    /** 专题活动 */
    public static final int ACTIVITY_TYPE_RUSH_BUY = 1;
    public static final int RUSH_BUY_STATE_NOT_START = 0;
    public static final int RUSH_BUY_STATE_START = 1;
    /** 抢光了 */
    public static final int RUSH_BUY_STATE_LOOT = 2;
    public static final int RUSH_BUY_STATE_END = 3;

    public static String createJson(int currentPage, int pageSize) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(JsonInterface.JK_CURRENT_PAGE, currentPage);
            obj.put(JsonInterface.JK_PAGE_SIZE, pageSize);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    public static ArrayList<ActivityEntity> parseJsonActivityEntityList(String json) {
        if (json == null || json.equals("")) {
            return null;
        }
        ArrayList<ActivityEntity> list = new ArrayList<ActivityEntity>();
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray arr = obj.optJSONArray(JsonInterface.JK_ACTIVITY_LIST);
            if (arr != null) {
                int len = arr.length();
                for (int i = 0; i < len; i++) {
                    ActivityEntity activity = parseJsonActivityEntity(arr.optJSONObject(i));
                    list.add(activity);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    /**
     * @param obj
     * @return
     */
    public static ActivityEntity parseJsonActivityEntity(JSONObject obj) {
        if (obj == null) {
            return null;
        }
        ActivityEntity activity = new ActivityEntity();
        activity.setActivityHtmlUrl(obj.optString(JsonInterface.JK_ACTIVITY_HTML_URL));
        activity.setActivityId(obj.optString(JsonInterface.JK_ACTIVITY_ID));
        activity.setActivityImgUrl(obj.optString(JsonInterface.JK_ACTIVITY_IMG_URL));// UrlMatcher.getFitPageAdUrl()
        activity.setActivityName(obj.optString(JsonInterface.JK_ACTIVITY_NAME));
        activity.setActivityType(obj.optString(JsonInterface.JK_ACTIVITY_TYPE));
        activity.setEndDate(obj.optString(JsonInterface.JK_END_DATE));
        activity.setStartDate(obj.optString(JsonInterface.JK_START_DATE));
        return activity;
    }

    /**
     * 解析活动专题普通商品列表JSON数据
     * 
     * @param json
     * @return
     */
    public static ArrayList<HotPromGoods> parseCommonGoodsList(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        ArrayList<HotPromGoods> list = null;
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray arr = obj.optJSONArray(JsonInterface.JK_GOODS_LIST);
            int len;
            if (arr != null && (len = arr.length()) > 0) {
                list = new ArrayList<HotPromGoods>();
                for (int i = 0; i < len; i++) {
                    JSONObject goodsObj = arr.optJSONObject(i);
                    HotPromGoods goods = new HotPromGoods();
                    goods.setSkuID(goodsObj.optString(JsonInterface.JK_SKU_ID));
                    goods.setGoodsNo(goodsObj.optString(JsonInterface.JK_GOODS_NO));
                    goods.setSkuNo(goodsObj.optString(JsonInterface.JK_SKU_NO));
                    goods.setSkuName(goodsObj.optString(JsonInterface.JK_SKU_NAME));
                    goods.setSkuThumbImgUrl(goodsObj.optString(JsonInterface.JK_SKU_THUMB_IMG_URL));
                    goods.setSkuOriginalPrice(goodsObj.optString(JsonInterface.JK_SKU_ORIGINAL_PRICE));
                    goods.setPromPrice(goodsObj.optString(JsonInterface.JK_PROM_PRICE));
                    ArrayList<Promotions> promList = null;
                    JSONArray promArr = goodsObj.optJSONArray(JsonInterface.JK_PROM_LIST);
                    if (promArr != null && promArr.length() > 0) {
                        promList = new ArrayList<Promotions>();
                        for (int j = 0,length = promArr.length(); j < length; j++) {
                            JSONObject promObj = promArr.optJSONObject(j);
                            Promotions prom = new Promotions();
                            prom.setPromType(promObj.optString(JsonInterface.JK_PROM_TYPE));
                            prom.setPromDesc(promObj.optString(JsonInterface.JK_PROM_DESC));
                            promList.add(prom);
                        }
                    }
                    goods.setPromList(promList);
                    list.add(goods);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 创建活动专题商品列表JSON数据
     * 
     * @param activityId
     *            促销内容衔接ID
     * @param activityType
     *            0: 普通活动；1: 抢购；
     * @param activityHtmlUrl
     *            促销内容衔接htmlUrl
     * @return JSONObject.toString();
     */
    public static String createJsonGoodsList(String activityId, String activityType, String activityHtmlUrl) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(JsonInterface.JK_ACTIVITY_ID, activityId);
            obj.put(JsonInterface.JK_ACTIVITY_TYPE, activityType);
            obj.put(JsonInterface.JK_ACTIVITY_HTML_URL, activityHtmlUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    /**
     * 解析活动专题抢购商品列表
     */
    public static ArrayList<RushBuyGoods> parseRushBuyGoodsList(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        ArrayList<RushBuyGoods> list = null;
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray arr = obj.optJSONArray(JsonInterface.JK_RUSH_GOODS_LIST);
            int len = 0;
            if (arr != null && (len = arr.length()) > 0) {
                list = new ArrayList<RushBuyGoods>();
                for (int i = 0; i < len; i++) {
                    JSONObject rushBuyGoodsObj = arr.optJSONObject(i);
                    RushBuyGoods goods = new RushBuyGoods();
                    goods.setSkuID(rushBuyGoodsObj.optString(JsonInterface.JK_SKU_ID));
                    goods.setGoodsNo(rushBuyGoodsObj.optString(JsonInterface.JK_GOODS_NO));
                    goods.setSkuNo(rushBuyGoodsObj.optString(JsonInterface.JK_SKU_NO));
                    goods.setSkuName(rushBuyGoodsObj.optString(JsonInterface.JK_SKU_NAME));
                    goods.setRushBuyItemId(rushBuyGoodsObj.optString(JsonInterface.JK_RUSH_BUY_ITEM_ID));
                    goods.setSkuThumbImgUrl(rushBuyGoodsObj.optString(JsonInterface.JK_SKU_THUMB_IMG_URL));
                    goods.setSkuOrignalPrice(rushBuyGoodsObj.optString(JsonInterface.JK_SKU_ORIGINAL_PRICE));
                    goods.setSkuRushBuyPrice(rushBuyGoodsObj.optString(JsonInterface.JK_SKU_RUSH_BUY_PRICE));
                    goods.setLimitNum(rushBuyGoodsObj.optInt(JsonInterface.JK_ACTIVITY_LIMIT_NUM));
                    goods.setRemainNum(rushBuyGoodsObj.optInt(JsonInterface.JK_REMAIN_NUM));
                    goods.setDelayTime(rushBuyGoodsObj.optLong(JsonInterface.JK_DELAY_TIME));
                    goods.setRushBuyState(rushBuyGoodsObj.optString(JsonInterface.JK_RUSH_BUY_STATE));
                    list.add(goods);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

}
