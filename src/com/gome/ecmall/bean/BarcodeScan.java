package com.gome.ecmall.bean;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gome.ecmall.bean.Product.BBCShopInfo;

/**
 * 二维码扫描【复合类】
 */
public class BarcodeScan implements JsonInterface {

    private static final String JK_BARCODE = "barCode";
    private static final String JK_GSCNPRODUCTNAME = "gscnProductName";
    private static final String JK_SCANRESULTTYPE = "scanResultType";
    private static final String JK_SKUORIGINALPRICE = "skuOriginalPrice";
    private static String errorMessage;

    public static String getErrorMessage() {
        return errorMessage;
    }

    public static void setErrorMessage(String errorMessage) {
        BarcodeScan.errorMessage = errorMessage;
    }

    /**
     * 解析条码扫描商品列表
     * 
     * @param json
     * @return
     */
    public static BarCodeGoodsResult parseBarCodeGoodsList(String json, String barCode) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            setErrorMessage(result.getFailReason());
            return null;
        }
        JSONObject content = result.getJsContent();
        try {
            BarCodeGoodsResult barCodeGoodsResult = new BarCodeGoodsResult();
            barCodeGoodsResult.setGscnProductName(content.optString(JK_GSCNPRODUCTNAME));
            barCodeGoodsResult.setScanResultType(content.optString(JK_SCANRESULTTYPE));
            barCodeGoodsResult.setBarCode(barCode);
            JSONArray goodslistArray = content.optJSONArray(JK_GOODS_LIST);
            if (goodslistArray != null) {
                ArrayList<BarCodeGoods> barCodeGoodsList = new ArrayList<BarCodeGoods>();
                for (int i = 0, length = goodslistArray.length(); i < length; i++) {
                    JSONObject jsonItem = goodslistArray.optJSONObject(i);
                    BarCodeGoods barCodeGoods = new BarCodeGoods();
                    barCodeGoods.setSkuID(jsonItem.optString(JK_SKU_ID));
                    barCodeGoods.setGoodsNo(jsonItem.optString(JK_GOODS_NO));
                    barCodeGoods.setSkuNo(jsonItem.optString(JK_SKU_NO));
                    barCodeGoods.setSkuName(jsonItem.optString(JK_SKU_NAME));
                    barCodeGoods.setSkuThumbImgUrl(jsonItem.optString(JK_SKU_THUMB_IMG_URL));
                    barCodeGoods.setSkuOriginalPrice(jsonItem.optString(JK_SKUORIGINALPRICE));
                    barCodeGoods.setBarCode(jsonItem.optString(JK_BARCODE));
                    barCodeGoods.setAd(jsonItem.optString(JK_AD));
                    // 是否支持BBC商品
                    barCodeGoods.setIsBbc(jsonItem.optString(JK_ISBBC));
                    JSONObject bbcObj = jsonItem.optJSONObject(JK_BBCSHOPINFO);
                    if (bbcObj != null) {
                        BBCShopInfo bbcShopInfo = new BBCShopInfo();
                        bbcShopInfo.setBbcShopId(bbcObj.optString(JK_BBCSHOPID));
                        bbcShopInfo.setBbcShopName(bbcObj.optString(JK_BBCSHOPNAME));
                        bbcShopInfo.setBbcShopImgURL(bbcObj.optString(JK_BBCSHOPIMGURL));
                        barCodeGoods.setBbcShopInfo(bbcShopInfo);
                    }
                    // 商品促销
                    JSONArray promArray = jsonItem.optJSONArray(JK_PROM_LIST);
                    if (promArray != null) {
                        ArrayList<OrderProm> orderPromList = new ArrayList<OrderProm>();
                        for (int j = 0, promlength = promArray.length(); j < promlength; j++) {
                            JSONObject promJsonItem = promArray.optJSONObject(j);
                            OrderProm orderProm = new OrderProm();
                            orderProm.setPromType(promJsonItem.optString(JK_PROM_TYPE));
                            orderProm.setPromDesc(promJsonItem.optString(JK_PROM_DESC));
                            orderPromList.add(orderProm);
                        }
                        barCodeGoods.setOrderPromList(orderPromList);
                    }
                    barCodeGoodsList.add(barCodeGoods);
                }
                barCodeGoodsResult.setBarCodeGoodsList(barCodeGoodsList);
            }
            return barCodeGoodsResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 条码购商品请求数据
     * 
     * @param barCode
     * @return
     */
    public static String createRequestBarCodeResultListJson(String barCode) {
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put(JK_BARCODE, barCode);
            return requestJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 条码扫描结过【复合类】
     */
    public static class BarCodeGoodsResult implements Serializable {
        private static final long serialVersionUID = 1L;

        private String gscnProductName;
        private String scanResultType;
        private String barCode;// 条码
        private ArrayList<BarCodeGoods> barCodeGoodsList;

        public String getBarCode() {
            return barCode;
        }

        public void setBarCode(String barCode) {
            this.barCode = barCode;
        }

        public String getGscnProductName() {
            return gscnProductName;
        }

        public void setGscnProductName(String gscnProductName) {
            this.gscnProductName = gscnProductName;
        }

        public String getScanResultType() {
            return scanResultType;
        }

        public void setScanResultType(String scanResultType) {
            this.scanResultType = scanResultType;
        }

        public ArrayList<BarCodeGoods> getBarCodeGoodsList() {
            return barCodeGoodsList;
        }

        public void setBarCodeGoodsList(ArrayList<BarCodeGoods> barCodeGoodsList) {
            this.barCodeGoodsList = barCodeGoodsList;
        }

    }

    /**
     * 条码扫描结果商品类
     */
    public static class BarCodeGoods implements Serializable {

        private static final long serialVersionUID = 1L;
        private String skuID;
        private String goodsNo;
        private String skuNo;
        private String skuName;
        private String skuThumbImgUrl;
        private String skuOriginalPrice;
        private String barCode;
        private String ad;
        private String isBbc;
        private BBCShopInfo bbcShopInfo;

        private boolean isLoadImg; // 下载图片
        private ArrayList<OrderProm> orderPromList;

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

        public String getBarCode() {
            return barCode;
        }

        public void setBarCode(String barCode) {
            this.barCode = barCode;
        }

        public String getAd() {
            return ad;
        }

        public void setAd(String ad) {
            this.ad = ad;
        }

        public ArrayList<OrderProm> getOrderPromList() {
            return orderPromList;
        }

        public void setOrderPromList(ArrayList<OrderProm> orderPromList) {
            this.orderPromList = orderPromList;
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

    /**
     * 促销信息
     */
    public static class OrderProm implements Serializable {
        private static final long serialVersionUID = 1L;
        private String promType;
        private String promDesc;

        public String getPromType() {
            return promType;
        }

        public void setPromType(String promType) {
            this.promType = promType;
        }

        public String getPromDesc() {
            return promDesc;
        }

        public void setPromDesc(String promDesc) {
            this.promDesc = promDesc;
        }

    }

    /**
     * 条码历史记录
     */
    public static class BarcodeHistory {

        private int barcodeId;
        private String barcode;
        private String number;
        private String date;
        private String timestamp;
        private String imgurl;
        private boolean isLoadImg;

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public int getBarcodeId() {
            return barcodeId;
        }

        public void setBarcodeId(int barcodeId) {
            this.barcodeId = barcodeId;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public boolean isLoadImg() {
            return isLoadImg;
        }

        public void setLoadImg(boolean isLoadImg) {
            this.isLoadImg = isLoadImg;
        }

    }

}
