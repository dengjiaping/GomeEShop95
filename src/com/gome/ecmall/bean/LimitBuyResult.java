package com.gome.ecmall.bean;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.gome.ecmall.util.UrlMatcher;

/**
 * 限时抢购【复合类】
 */
public class LimitBuyResult implements JsonInterface {

    private static final String JK_SERVERTIME = "serverTime";
    private static final String JK_RUSHBUYDATETYPE = "rushBuyDateType";
    private static final String JK_RUSHBUIYGROUPlIST = "rushBuyGroupList";
    private static final String JK_RUSHBUYBEGINTIME = "rushBuyBeginTime";
    private static final String JK_RUSHBUYGOODSLIST = "rushBuyGoodsList";
    private static final String JK_RUSHBUYITEMID = "rushBuyItemId";
    private static final String JK_SKURUSHBUYPRICE = "skuRushBuyPrice";
    private static final String JK_LIMITNUM = "limitNum";
    private static final String JK_REMAINNUM = "remainNum";
    private static final String JK_DELAYTIME = "delayTime";
    private static final String JK_RUSHBUYSTATE = "rushBuyState";
    private static final String JK_RUSHBUYAPPRAISECOUNT = "appraiseCount";
    private static final String JK_RUSHBUYAPPRAISEGRADE = "appraiseGrade";

    public static String SERVERTIME = "";// 抢购页返回数据时带的系统时间
    public static String HomESERVERTIME = "";// 首页抢购返回数据时带的系统时间

    public static String PUSH_ACTIVE_NAME = "";// 消息推送跳到抢购页面是的活动名称

    public static ArrayList<LimitBuy> parseLimitBuyList(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject content = result.getJsContent();
        try {
            JSONArray limitbuyJsonArray = content.optJSONArray(JK_RUSHBUIYGROUPlIST);
            SERVERTIME = content.optString(JK_SERVERTIME);
            if (limitbuyJsonArray != null) {
                ArrayList<LimitBuy> limitBuyList = new ArrayList<LimitBuy>();
                for (int i = 0, length = limitbuyJsonArray.length(); i < length; i++) {
                    LimitBuy limitbuy = new LimitBuy();
                    JSONObject jsonObjItem = limitbuyJsonArray.optJSONObject(i);
                    String rushBuyBeginTime = jsonObjItem.optString(JK_RUSHBUYBEGINTIME);
                    limitbuy.setRushBuyBeginTime(rushBuyBeginTime);
                    JSONArray jsonArray = jsonObjItem.optJSONArray(JK_RUSHBUYGOODSLIST);
                    if (jsonArray != null) {
                        ArrayList<LimitBuyGoods> limitBuyGoodsList = new ArrayList<LimitBuyGoods>();
                        for (int j = 0, goodsLength = jsonArray.length(); j < goodsLength; j++) {
                            LimitBuyGoods limitBuyGoods = new LimitBuyGoods();
                            JSONObject goodsObj = jsonArray.optJSONObject(j);
                            limitBuyGoods.setSkuID(goodsObj.optString(JK_SKU_ID));
                            limitBuyGoods.setGoodsNo(goodsObj.optString(JK_GOODS_NO));
                            limitBuyGoods.setSkuNo(goodsObj.optString(JK_SKU_NO));
                            limitBuyGoods.setSkuName(goodsObj.optString(JK_SKU_NAME));
                            limitBuyGoods.setRushBuyItemId(goodsObj.optString(JK_RUSHBUYITEMID));
                            limitBuyGoods.setSkuThumbImgUrl(UrlMatcher.getFitGallerySourceUrl(goodsObj
                                    .optString(JK_SKU_THUMB_IMG_URL)));
                            limitBuyGoods.setSkuOriginalPrice(goodsObj.optString(JK_SKU_ORIGINAL_PRICE));
                            limitBuyGoods.setSkuRushBuyPrice(goodsObj.optString(JK_SKU_RUSH_BUY_PRICE));
                            limitBuyGoods.setLimitNum(goodsObj.optString(JK_LIMITNUM));
                            limitBuyGoods.setRemainNum(goodsObj.optString(JK_REMAINNUM));
                            limitBuyGoods.setDelayTime(goodsObj.optString(JK_DELAYTIME));
                            limitBuyGoods.setRushBuyState(goodsObj.optString(JK_RUSH_BUY_STATE));
                            limitBuyGoods.setDelayEndTime(goodsObj.optString(JK_DELAYENDTIME));
                            limitBuyGoods.setDiscountRate(goodsObj.optString("discountRate"));
                            limitBuyGoodsList.add(limitBuyGoods);
                        }
                        limitbuy.setRushBuyGoodsList(limitBuyGoodsList);
                    }
                    limitBuyList.add(limitbuy);
                }
                return limitBuyList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 首页推荐抢购数据
     * 
     * @param json
     * @return
     */

    public static LimitBuyGoods parseLimitBuyGoods(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject content = result.getJsContent();
        LimitBuyGoods limitBuyGoods = new LimitBuyGoods();
        HomESERVERTIME = content.optString(JK_SERVERTIME);
        limitBuyGoods.setSkuID(content.optString(JK_SKU_ID));
        limitBuyGoods.setGoodsNo(content.optString(JK_GOODS_NO));
        limitBuyGoods.setSkuNo(content.optString(JK_SKU_NO));
        limitBuyGoods.setSkuName(content.optString(JK_SKU_NAME));
        limitBuyGoods.setRushBuyItemId(content.optString(JK_RUSH_BUY_ITEM_ID));
        limitBuyGoods.setSkuThumbImgUrl(UrlMatcher.getFitListThumbUrl(content.optString(JK_SKU_THUMB_IMG_URL)));
        limitBuyGoods.setSkuOriginalPrice(content.optString(JK_SKU_ORIGINAL_PRICE));
        limitBuyGoods.setSkuRushBuyPrice(content.optString(JK_SKU_RUSH_BUY_PRICE));
        limitBuyGoods.setLimitNum(content.optString(JK_LIMITNUM));
        limitBuyGoods.setRemainNum(content.optString(JK_REMAINNUM));
        limitBuyGoods.setDelayTime(content.optString(JK_DELAYTIME));
        limitBuyGoods.setDelayEndTime(content.optString(JK_DELAYENDTIME));
        limitBuyGoods.setRushBuyState(content.optString(JK_RUSH_BUY_STATE));

        // limitBuyGoods.setSkuID("456465456");
        // limitBuyGoods.setGoodsNo("456456546");
        // limitBuyGoods.setSkuNo("456789789789");
        // limitBuyGoods.setSkuName("剃须刀");
        // limitBuyGoods.setRushBuyItemId("");
        // limitBuyGoods.setSkuThumbImgUrl("http://img2.gomein.net.cn/image/prodimg/production_image/201301/1104470020/1000350340_125.jpg");
        // limitBuyGoods.setSkuOriginalPrice("1998");
        // limitBuyGoods.setSkuRushBuyPrice("998");
        // limitBuyGoods.setLimitNum("1232");
        // limitBuyGoods.setRemainNum("1232");
        // limitBuyGoods.setDelayTime("3700");
        // limitBuyGoods.setDelayEndTime("10");
        // limitBuyGoods.setRushBuyState("1");
        return limitBuyGoods;
    }

    /**
     * 组建限时抢购请求数据
     * 
     * @param rushbuydatetype
     * @return
     */
    public static String createRequestLimitBuyListJson(String rushbuydatetype) {
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put(JK_RUSHBUYDATETYPE, rushbuydatetype);
            return requestJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 促销活动专题普通商品列表
     * 
     * @param activityId
     * @param activityType
     * @param activityHtmlUrl
     * @return
     */
    public static String createRequestLimitBuyPrmListJson(String activityId, String activityType, String activityHtmlUrl) {
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put(JK_ACTIVITY_ID, activityId);
            requestJson.put(JK_ACTIVITY_TYPE, activityType);
            if (!TextUtils.isEmpty(activityHtmlUrl)) {
                requestJson.put(JK_ACTIVITY_HTML_URL, activityHtmlUrl);
            }
            return requestJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 促销活动专题抢购列表
     */

    public static ArrayList<LimitBuy> parseLimitBuyPromList(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject content = result.getJsContent();
        JSONObject activityInfoObject = content.optJSONObject(JK_PROMOTION_ACTIVITY_INFO);
        if (activityInfoObject != null) {
            PUSH_ACTIVE_NAME = "";
            PUSH_ACTIVE_NAME = activityInfoObject.optString(JK_ACTIVITY_NAME);
        }
        try {
            ArrayList<LimitBuy> limitBuyList = new ArrayList<LimitBuy>();
            LimitBuy limitBuy = new LimitBuy();
            JSONArray jsonArray = content.optJSONArray(JK_RUSHBUYGOODSLIST);
            if (jsonArray != null) {
                ArrayList<LimitBuyGoods> limitBuyGoodsList = new ArrayList<LimitBuyGoods>();
                for (int j = 0, goodsLength = jsonArray.length(); j < goodsLength; j++) {
                    LimitBuyGoods limitBuyGoods = new LimitBuyGoods();
                    JSONObject goodsObj = jsonArray.optJSONObject(j);
                    limitBuyGoods.setSkuID(goodsObj.optString(JK_SKU_ID));
                    limitBuyGoods.setGoodsNo(goodsObj.optString(JK_GOODS_NO));
                    limitBuyGoods.setSkuNo(goodsObj.optString(JK_SKU_NO));
                    limitBuyGoods.setSkuName(goodsObj.optString(JK_SKU_NAME));
                    limitBuyGoods.setRushBuyItemId(goodsObj.optString(JK_RUSHBUYITEMID));
                    limitBuyGoods.setSkuThumbImgUrl(UrlMatcher.getFitGallerySourceUrl(goodsObj
                            .optString(JK_SKU_THUMB_IMG_URL)));
                    limitBuyGoods.setSkuOriginalPrice(goodsObj.optString(JK_SKU_ORIGINAL_PRICE));
                    limitBuyGoods.setSkuRushBuyPrice(goodsObj.optString(JK_SKU_RUSH_BUY_PRICE));
                    limitBuyGoods.setLimitNum(goodsObj.optString(JK_LIMITNUM));
                    limitBuyGoods.setRemainNum(goodsObj.optString(JK_REMAINNUM));
                    limitBuyGoods.setDelayTime(goodsObj.optString(JK_DELAYTIME));
                    limitBuyGoods.setRushBuyState(goodsObj.optString(JK_RUSH_BUY_STATE));
                    limitBuyGoods.setDelayEndTime(goodsObj.optString(JK_DELAYENDTIME));
                    limitBuyGoods.setDiscountRate(goodsObj.optString("discountRate"));
                    limitBuyGoodsList.add(limitBuyGoods);
                }
                limitBuy.setRushBuyGoodsList(limitBuyGoodsList);
            }
            limitBuyList.add(limitBuy);
            return limitBuyList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取单个抢购信息
     * 
     * @param json
     * @return
     */
    public static LimitBuyGoods parseLimitBuy(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject content = result.getJsContent();
        try {
            LimitBuyGoods limitBuyGoods = new LimitBuyGoods();
            limitBuyGoods.setRemainNum(content.optString(JK_REMAINNUM));
            limitBuyGoods.setDelayTime(content.optString(JK_DELAYTIME));
            limitBuyGoods.setRushBuyState(content.optString(JK_RUSHBUYSTATE));
            limitBuyGoods.setAppraiseCount(content.optString(JK_RUSHBUYAPPRAISECOUNT));
            limitBuyGoods.setAppraiseGrade(content.optString(JK_RUSHBUYAPPRAISEGRADE));

            limitBuyGoods.setSkuID(content.optString(JK_SKU_ID));
            limitBuyGoods.setGoodsNo(content.optString(JK_GOODS_NO));
            limitBuyGoods.setSkuNo(content.optString(JK_SKU_NO));
            limitBuyGoods.setSkuName(content.optString(JK_SKU_NAME));
            limitBuyGoods.setRushBuyItemId(content.optString(JK_RUSHBUYITEMID));
            limitBuyGoods.setSkuThumbImgUrl(UrlMatcher.getFitGallerySourceUrl(content.optString(JK_SKU_THUMB_IMG_URL)));
            limitBuyGoods.setSkuOriginalPrice(content.optString(JK_SKU_ORIGINAL_PRICE));
            limitBuyGoods.setSkuRushBuyPrice(content.optString(JK_SKU_RUSH_BUY_PRICE));
            limitBuyGoods.setLimitNum(content.optString(JK_LIMITNUM));
            limitBuyGoods.setDelayEndTime(content.optString(JK_DELAYENDTIME));
            return limitBuyGoods;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 单个商品抢购信息请求数据
     * 
     * @param rushBuyItemId
     * @return
     */
    public static String createRequestLimitBuyJson(String rushBuyItemId) {
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put(JK_RUSHBUYITEMID, rushBuyItemId);
            return requestJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *限时抢购时间段列表【实体】
     */
    public static class LimitBuy {

        private String rushBuyBeginTime;
        private ArrayList<LimitBuyGoods> rushBuyGoodsList;

        public String getRushBuyBeginTime() {
            return rushBuyBeginTime;
        }

        public void setRushBuyBeginTime(String rushBuyBeginTime) {
            this.rushBuyBeginTime = rushBuyBeginTime;
        }

        public ArrayList<LimitBuyGoods> getRushBuyGoodsList() {
            return rushBuyGoodsList;
        }

        public void setRushBuyGoodsList(ArrayList<LimitBuyGoods> rushBuyGoodsList) {
            this.rushBuyGoodsList = rushBuyGoodsList;
        }

    }

    /**
     *限时抢购商品
     */
    public static class LimitBuyGoods implements Serializable {

        private String skuID;
        private String goodsNo;
        private String skuNo;
        private String skuName;
        private String rushBuyItemId;
        private String skuThumbImgUrl;
        private String skuOriginalPrice;
        private String skuRushBuyPrice;
        private String limitNum;
        private String remainNum;
        private String delayTime;
        private String rushBuyState;
        private String delayEndTime;
        private boolean isLoadImg;// 是否加载图片
        private String appraiseGrade;
        private String appraiseCount;
        private String discountRate;

        public String getSkuID() {
            return skuID;
        }

        public void setSkuID(String skuID) {
            this.skuID = skuID;
        }

        public String getGoodsNo() {
            return goodsNo;
        }

        public void setGoodsNo(String goodsNo) {
            this.goodsNo = goodsNo;
        }

        public String getSkuNo() {
            return skuNo;
        }

        public void setSkuNo(String skuNo) {
            this.skuNo = skuNo;
        }

        public String getSkuName() {
            return skuName;
        }

        public void setSkuName(String skuName) {
            this.skuName = skuName;
        }

        public String getRushBuyItemId() {
            return rushBuyItemId;
        }

        public void setRushBuyItemId(String rushBuyItemId) {
            this.rushBuyItemId = rushBuyItemId;
        }

        public String getSkuThumbImgUrl() {
            return skuThumbImgUrl;
        }

        public void setSkuThumbImgUrl(String skuThumbImgUrl) {
            this.skuThumbImgUrl = skuThumbImgUrl;
        }

        public String getSkuOriginalPrice() {
            return skuOriginalPrice;
        }

        public void setSkuOriginalPrice(String skuOriginalPrice) {
            this.skuOriginalPrice = skuOriginalPrice;
        }

        public String getSkuRushBuyPrice() {
            return skuRushBuyPrice;
        }

        public void setSkuRushBuyPrice(String skuRushBuyPrice) {
            this.skuRushBuyPrice = skuRushBuyPrice;
        }

        public String getLimitNum() {
            return limitNum;
        }

        public void setLimitNum(String limitNum) {
            this.limitNum = limitNum;
        }

        public String getRemainNum() {
            return remainNum;
        }

        public void setRemainNum(String remainNum) {
            this.remainNum = remainNum;
        }

        public String getDelayTime() {
            return delayTime;
        }

        public void setDelayTime(String delayTime) {
            this.delayTime = delayTime;
        }

        public String getRushBuyState() {
            return rushBuyState;
        }

        public void setRushBuyState(String rushBuyState) {
            this.rushBuyState = rushBuyState;
        }

        public boolean isLoadImg() {
            return isLoadImg;
        }

        public void setLoadImg(boolean isLoadImg) {
            this.isLoadImg = isLoadImg;
        }

        public String getDelayEndTime() {
            return delayEndTime;
        }

        public void setDelayEndTime(String delayEndTime) {
            this.delayEndTime = delayEndTime;
        }

        public String getAppraiseGrade() {
            return appraiseGrade;
        }

        public void setAppraiseGrade(String appraiseGrade) {
            this.appraiseGrade = appraiseGrade;
        }

        public String getAppraiseCount() {
            return appraiseCount;
        }

        public void setAppraiseCount(String appraiseCount) {
            this.appraiseCount = appraiseCount;
        }

        public String getDiscountRate() {
            return discountRate;
        }

        public void setDiscountRate(String discountRate) {
            this.discountRate = discountRate;
        }

    }

}
