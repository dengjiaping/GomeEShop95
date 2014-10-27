package com.gome.ecmall.bean;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gome.ecmall.bean.GBProductNew.GroupBuyProduct;
import com.gome.ecmall.util.UrlMatcher;

/**
 * 活动专区-主题模版【复合类】
 */
public class HotPromTheTem implements JsonInterface {

    public static final String JK_ACTIVITYEXTENDATTR = "activityExtendAttr";
    public static final String JK_BANANERIMGURL = "bananerImgUrl";
    public static final String JK_BGIMGURL = "bgImgUrl";
    public static final String JK_BGCOLOR = "bgColor";
    public static final String JK_GOODSBGCOLOR = "goodsbgColor";
    public static final String JK_SKUORGPRICECOLOR = "skuOrgPriceColor";
    public static final String JK_PROMPRICECOLOR = "promPriceColor";
    public static final String JK_SKUNAMECOLOR = "skuNameColor";
    public static final String JK_GOODSBORDERCOLOR = "goodsBorderColor";

    /**
     * 活动专题模版【属性】
     */
    public static class HotPromTheTemBean implements Serializable {

        private static final long serialVersionUID = 1L;
        private String bananerImgUrl;
        private String bgImgUrl;
        private String bgColor;
        private String goodsbgColor;
        private String skuOrgPriceColor;
        private String promPriceColor;
        private String skuNameColor;
        private String goodsBorderColor;
        private ActivityInfo activityInfo;

        private ArrayList<Goods> goodslist;

        public String getBananerImgUrl() {
            return bananerImgUrl;
        }

        public void setBananerImgUrl(String bananerImgUrl) {
            this.bananerImgUrl = bananerImgUrl;
        }

        public String getBgImgUrl() {
            return bgImgUrl;
        }

        public void setBgImgUrl(String bgImgUrl) {
            this.bgImgUrl = bgImgUrl;
        }

        public String getBgColor() {
            return bgColor;
        }

        public void setBgColor(String bgColor) {
            this.bgColor = bgColor;
        }

        public String getGoodsbgColor() {
            return goodsbgColor;
        }

        public void setGoodsbgColor(String goodsbgColor) {
            this.goodsbgColor = goodsbgColor;
        }

        public String getSkuOrgPriceColor() {
            return skuOrgPriceColor;
        }

        public void setSkuOrgPriceColor(String skuOrgPriceColor) {
            this.skuOrgPriceColor = skuOrgPriceColor;
        }

        public String getPromPriceColor() {
            return promPriceColor;
        }

        public void setPromPriceColor(String promPriceColor) {
            this.promPriceColor = promPriceColor;
        }

        public String getSkuNameColor() {
            return skuNameColor;
        }

        public void setSkuNameColor(String skuNameColor) {
            this.skuNameColor = skuNameColor;
        }

        public ArrayList<Goods> getGoodslist() {
            return goodslist;
        }

        public void setGoodslist(ArrayList<Goods> goodslist) {
            this.goodslist = goodslist;
        }

        public String getGoodsBorderColor() {
            return goodsBorderColor;
        }

        public void setGoodsBorderColor(String goodsBorderColor) {
            this.goodsBorderColor = goodsBorderColor;
        }

        public ActivityInfo getActivityInfo() {
            return activityInfo;
        }

        public void setActivityInfo(ActivityInfo activityInfo) {
            this.activityInfo = activityInfo;
        }

    }
    
    
    /**
     * 团购商品列表
     * @param json
     * @return
     */
    public static HotPromTheTemBean parseGroupBuyListJson(String json){
        
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject content = result.getJsContent();
        HotPromTheTemBean hotPromThem = new HotPromTheTemBean();
        
        try {
            ActivityInfo info = new ActivityInfo();

            JSONObject activityInfoObject = content.optJSONObject(JK_PROMOTION_ACTIVITY_INFO);
            if (activityInfoObject != null) {
                info.setActivityId(activityInfoObject.optString(JK_ACTIVITY_ID));
                info.setActivityName(activityInfoObject.optString(JK_ACTIVITY_NAME));
                info.setActivityRule(activityInfoObject.optString(JK_ACTIVITY_RULE));
            }
            hotPromThem.setActivityInfo(info);

            JSONObject extendAttrObj = content.optJSONObject(JK_ACTIVITYEXTENDATTR);
            if (extendAttrObj != null) {
                hotPromThem.setBananerImgUrl(extendAttrObj.optString(JK_BANANERIMGURL));//
                hotPromThem.setBgImgUrl(extendAttrObj.optString(JK_BGIMGURL));//
                hotPromThem.setBgColor(extendAttrObj.optString(JK_BGCOLOR));//
                hotPromThem.setGoodsbgColor(extendAttrObj.optString(JK_GOODSBGCOLOR));//
                hotPromThem.setSkuOrgPriceColor(extendAttrObj.optString(JK_SKUORGPRICECOLOR));//
                hotPromThem.setPromPriceColor(extendAttrObj.optString(JK_PROMPRICECOLOR));//
                hotPromThem.setSkuNameColor(extendAttrObj.optString(JK_SKUNAMECOLOR));// ;
                hotPromThem.setGoodsBorderColor(extendAttrObj.optString(JK_GOODSBORDERCOLOR));
            }
            JSONArray goodsArray = content.optJSONArray(JK_GROUP_BUY_LIST);
            if (goodsArray != null) {
                ArrayList<Goods> goodList = new ArrayList<Goods>();
                for (int i = 0, length = goodsArray.length(); i < length; i++) {
                    JSONObject item = goodsArray.getJSONObject(i);
                    GroupBuyProduct goods = new GroupBuyProduct();
                    goods.setGoodsNo(item.optString(JK_GOODS_NO));
                    goods.setSkuID(item.optString(JK_SKU_ID));
                    goods.setSkuNo(item.optString(JK_SKU_NO));
                    goods.setSkuName(item.optString(JK_SKU_NAME));
                    String skuThumbImgUrl = item.optString(JK_SKU_THUMB_IMG_URL);
                    goods.setSkuThumbImgUrl(UrlMatcher.getFitGridThumbUrl(skuThumbImgUrl));
                    goods.setOriginalPrice(item.optString(JK_SKU_ORIGINAL_PRICE));
                    goods.setPromPrice(item.optString(JK_GROUP_BUY_SALE_PRICE));
                    goods.setSalePromoItem(item.optString(JK_GROUP_BUY_SALE_PROM_ID));
                    goodList.add(goods);
                }// end for
                hotPromThem.setGoodslist(goodList);
            }// end if

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        
        return hotPromThem ;
    }

    public static HotPromTheTemBean parseHotPromTheTemJson(String json) {

        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject content = result.getJsContent();
        HotPromTheTemBean hotPromThem = new HotPromTheTemBean();
        try {
            ActivityInfo info = new ActivityInfo();

            JSONObject activityInfoObject = content.optJSONObject(JK_PROMOTION_ACTIVITY_INFO);
            if (activityInfoObject != null) {
                info.setActivityId(activityInfoObject.optString(JK_ACTIVITY_ID));
                info.setActivityName(activityInfoObject.optString(JK_ACTIVITY_NAME));
                info.setActivityRule(activityInfoObject.optString(JK_ACTIVITY_RULE));
            }
            hotPromThem.setActivityInfo(info);

            JSONObject extendAttrObj = content.optJSONObject(JK_ACTIVITYEXTENDATTR);
            if (extendAttrObj != null) {
                hotPromThem.setBananerImgUrl(extendAttrObj.optString(JK_BANANERIMGURL));//
                hotPromThem.setBgImgUrl(extendAttrObj.optString(JK_BGIMGURL));//
                hotPromThem.setBgColor(extendAttrObj.optString(JK_BGCOLOR));//
                hotPromThem.setGoodsbgColor(extendAttrObj.optString(JK_GOODSBGCOLOR));//
                hotPromThem.setSkuOrgPriceColor(extendAttrObj.optString(JK_SKUORGPRICECOLOR));//
                hotPromThem.setPromPriceColor(extendAttrObj.optString(JK_PROMPRICECOLOR));//
                hotPromThem.setSkuNameColor(extendAttrObj.optString(JK_SKUNAMECOLOR));// ;
                hotPromThem.setGoodsBorderColor(extendAttrObj.optString(JK_GOODSBORDERCOLOR));
            }
            JSONArray goodsArray = content.optJSONArray(JK_GOODS_LIST);
            if (goodsArray != null) {
                ArrayList<Goods> goodList = new ArrayList<Goods>();
                for (int i = 0, length = goodsArray.length(); i < length; i++) {
                    JSONObject item = goodsArray.getJSONObject(i);
                    Goods goods = new Goods();
                    goods.setGoodsNo(item.optString(JK_GOODS_NO));
                    goods.setSkuID(item.optString(JK_SKU_ID));
                    goods.setSkuNo(item.optString(JK_SKU_NO));
                    goods.setSkuName(item.optString(JK_SKU_NAME));
                    String skuThumbImgUrl = item.optString(JK_SKU_THUMB_IMG_URL);
                    goods.setSkuThumbImgUrl(UrlMatcher.getFitGridThumbUrl(skuThumbImgUrl));
                    goods.setOriginalPrice(item.optString(JK_ORIGINAL_PRICE));
                    goods.setPromPrice(item.optString(JK_PROM_PRICE));
                    // 商品促销信息
                    JSONArray promarray = item.optJSONArray(JK_PROMOTION_LIST);
                    if (promarray != null) {
                        ArrayList<Promotions> goodspromlist = new ArrayList<Promotions>();
                        for (int j = 0, goodpromsize = promarray.length(); j < goodpromsize; j++) {
                            JSONObject goodspromitem = promarray.optJSONObject(j);
                            Promotions goodsProm = new Promotions();
                            goodsProm.setPromType(goodspromitem.optString(JK_PROM_TYPE));
                            goodsProm.setPromDesc(goodspromitem.optString(JK_PROM_DESC));
                            goodspromlist.add(goodsProm);
                        }
                        goods.setPromList(goodspromlist);
                    }
                    goodList.add(goods);
                }// end for
                hotPromThem.setGoodslist(goodList);
            }// end if

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return hotPromThem;
    }

    /**
     * 活动专题活动信息
     */
    public static class ActivityInfo {
        private String activityName;
        private String activityRule;
        private String activityId;

        public String getActivityName() {
            return activityName;
        }

        public void setActivityName(String activityName) {
            this.activityName = activityName;
        }

        public String getActivityRule() {
            return activityRule;
        }

        public void setActivityRule(String activityRule) {
            this.activityRule = activityRule;
        }

        public String getActivityId() {
            return activityId;
        }

        public void setActivityId(String activityId) {
            this.activityId = activityId;
        }

    }

}
