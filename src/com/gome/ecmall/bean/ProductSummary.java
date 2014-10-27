package com.gome.ecmall.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 商品介绍-参数-售后  相关【单独类】
 */
public class ProductSummary implements JsonInterface {

    public static String createRequestProductSummaryJson(String goodsNo) {
        JSONObject object = new JSONObject();
        try {
            object.put(JK_GOODS_NO, goodsNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public static ProductSummary parseProductSummary(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject jsContent = result.getJsContent();
        ProductSummary summary = new ProductSummary();
        summary.setGoodsDesc(jsContent.optString(JK_GOODS_DESC));
        summary.setSpecification(jsContent.optString(JK_SPECIFICATIONS));
        summary.setPackingList(jsContent.optString(JK_PACKE_LIST));
        summary.setAfterSale(jsContent.optString(JK_SERVICE));
        return summary;
    }

    // 商品介绍
    private String goodsDesc;
    // 规格参数
    private String specification;
    // 包装清单
    private String packingList;
    // 售后服务
    private String afterSale;

    public ProductSummary() {

    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getPackingList() {
        return packingList;
    }

    public void setPackingList(String packingList) {
        this.packingList = packingList;
    }

    public String getAfterSale() {
        return afterSale;
    }

    public void setAfterSale(String afterSale) {
        this.afterSale = afterSale;
    }

}
