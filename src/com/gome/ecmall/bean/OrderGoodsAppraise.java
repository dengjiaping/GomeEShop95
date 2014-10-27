package com.gome.ecmall.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gome.ecmall.bean.Product.BBCShopInfo;

/**
 * 订单商品评价
 */
public class OrderGoodsAppraise {

    /**
     * 解析订单商品评价列表
     * 
     * @param json
     * @return
     */
    public static ArrayList<OrderGoodsAppraise> parseOrderGoodsAppraiseList(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        ArrayList<OrderGoodsAppraise> appraises = new ArrayList<OrderGoodsAppraise>();
        try {
            JSONObject jsContent = result.getJsContent();
            JSONArray array = jsContent.getJSONArray(JsonInterface.JK_GOODS_LIST);
            for (int i = 0, size = array.length(); i < size; i++) {
                JSONObject item = array.getJSONObject(i);
                OrderGoodsAppraise appraise = new OrderGoodsAppraise();
                appraise.setSkuId(item.getString(JsonInterface.JK_SKU_ID));
                appraise.setGoodsNo(item.getString(JsonInterface.JK_GOODS_NO));
                appraise.setSkuName(item.getString(JsonInterface.JK_SKU_NAME));
                appraise.setProductImgUrl(item.getString(JsonInterface.JK_PRODUCT_IMG_URL));
                appraise.setUserReviewId(item.getString(JsonInterface.JK_USER_REVIEW_ID));
                appraise.setOrderId(item.getString(JsonInterface.JK_ORDER_ID));
                appraise.setFinishTime(item.getString(JsonInterface.JK_FINISH_TIME));
                // 是否支持BBC商品
                appraise.setIsBbc(item.optString(JsonInterface.JK_ISBBC));
                JSONObject bbcObj = item.optJSONObject(JsonInterface.JK_BBCSHOPINFO);
                if (bbcObj != null) {
                    BBCShopInfo bbcShopInfo = new BBCShopInfo();
                    bbcShopInfo.setBbcShopId(bbcObj.optString(JsonInterface.JK_BBCSHOPID));
                    bbcShopInfo.setBbcShopName(bbcObj.optString(JsonInterface.JK_BBCSHOPNAME));
                    bbcShopInfo.setBbcShopImgURL(bbcObj.optString(JsonInterface.JK_BBCSHOPIMGURL));
                    appraise.setBbcShopInfo(bbcShopInfo);
                }
                appraises.add(appraise);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return appraises;
    }

    private String skuId;
    private String goodsNo;
    private String skuName;
    private String productImgUrl;
    private String userReviewId;
    private String orderId;
    private String finishTime;
    private String isBbc;
    private BBCShopInfo bbcShopInfo;
    private boolean isLoadImg;

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getProductImgUrl() {
        return productImgUrl;
    }

    public void setProductImgUrl(String productImgUrl) {
        this.productImgUrl = productImgUrl;
    }

    public String getUserReviewId() {
        return userReviewId;
    }

    public void setUserReviewId(String userReviewId) {
        this.userReviewId = userReviewId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public boolean isLoadImg() {
        return isLoadImg;
    }

    public void setLoadImg(boolean isLoadImg) {
        this.isLoadImg = isLoadImg;
    }

    public String getIsBbc() {
        return isBbc;
    }

    public void setIsBbc(String isBbc) {
        this.isBbc = isBbc;
    }

    public BBCShopInfo getBbcShopInfo() {
        return bbcShopInfo;
    }

    public void setBbcShopInfo(BBCShopInfo bbcShopInfo) {
        this.bbcShopInfo = bbcShopInfo;
    }

}
