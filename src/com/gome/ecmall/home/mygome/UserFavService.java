package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gome.ecmall.bean.DeletedCollection;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.Product.BBCShopInfo;
import com.gome.ecmall.bean.Promotions;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.JsonUtils;
import com.gome.ecmall.util.UrlMatcher;

public class UserFavService {
    private static final String TAG = "UserFavService";

    public static DeletedCollection parseJsonDelFav(String json) {
        if (json == null)
            return null;
        DeletedCollection collection = new DeletedCollection();
        try {
            JSONObject obj = new JSONObject(json);
            collection.setIsSuccess(obj.optString(JsonInterface.JK_IS_SUCCESS));
            collection.setResult(obj.optString(JsonInterface.JK_RESULT));
            collection.setFail(obj.optString(JsonInterface.JK_FAIL_REASON));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return collection;
    }

    public static String createRequestDelFav(String skuId, String goodsNo, String id) {
        if (skuId == null || skuId.equals("") || goodsNo.equals("") || goodsNo == null)
            return null;

        JSONObject obj = JsonUtils.createJsonObject(new String[] { JsonInterface.JK_SKU_ID, JsonInterface.JK_GOODS_NO,
                JsonInterface.JK_ID }, new String[] { skuId, goodsNo, id });
        JSONArray array = JsonUtils.createJsonArray(obj);
        JSONObject delFavObj = JsonUtils.createJsonObject(JsonInterface.JK_DEL_COLLECTION_LIST, array);
        return delFavObj.toString();
    }

    /**
     * 创建删除到货通知json
     * 
     * @param id
     * @return
     */
    public static String createJsonDeleteArrival(String id) {
        if (id == null || id.equals("")) {
            return null;
        }
        JSONObject obj = new JSONObject();
        try {
            obj.put(JsonInterface.JK_ID, id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();

    }

    public static String createFavReauest(int currentPage, int pageSize) {
        if (currentPage == 0 || pageSize == 0)
            return null;

        JSONObject obj = new JSONObject();
        try {
            obj.put(JsonInterface.JK_CURRENT_PAGE, currentPage);
            obj.put(JsonInterface.JK_PAGE_SIZE, pageSize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    public static UserFav parseJsonUserFav(JSONObject obj) {
        if (obj == null || obj.length() == 0) {
            return null;
        }
        UserFav fav = new UserFav();

        fav.setId(obj.optString(JsonInterface.JK_ID));
        fav.setSkuID(obj.optString(JsonInterface.JK_SKU_ID));
        fav.setGoodsNo(obj.optString(JsonInterface.JK_GOODS_NO));
        fav.setSkuName(obj.optString(JsonInterface.JK_SKU_NAME));
        fav.setProductImgUrl(obj.optString(JsonInterface.JK_PRODUCT_IMG_URL));
        fav.setSalePrice(obj.optString(JsonInterface.JK_SALE_PRICE));
        fav.setIsOnsale(obj.optString(JsonInterface.JK_IS_ON_SALE));
        fav.setCollectionTime(obj.optString(JsonInterface.JK_COLLECTION_TIME));
        return fav;
    }

    public static ArrayList<UserFav> parseJson(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        // JsonResult result = new JsonResult(json);
        // if (!result.isSuccess()) {
        // return null;
        // }

        ArrayList<UserFav> list = null;
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray userFavArr = obj.optJSONArray(JsonInterface.JK_GOODS_LIST);
            if (userFavArr != null) {
                list = new ArrayList<UserFav>();
                int len = userFavArr.length();

                for (int i = 0; i < len; i++) {
                    JSONObject goodsObj = userFavArr.optJSONObject(i);

                    if (goodsObj != null) {
                        UserFav fav = new UserFav();
                        String id = (String) goodsObj.get(JsonInterface.JK_ID);
                        String skuID = goodsObj.optString(JsonInterface.JK_SKU_ID);
                        String goodsNo = goodsObj.optString(JsonInterface.JK_GOODS_NO);
                        String skuName = goodsObj.optString(JsonInterface.JK_SKU_NAME);
                        String imgUrl = UrlMatcher.getFitListThumbUrl(goodsObj
                                .optString(JsonInterface.JK_PRODUCT_IMG_URL));
                        String salePrice = goodsObj.optString(JsonInterface.JK_SALE_PRICE);
                        String isOnSale = goodsObj.optString(JsonInterface.JK_IS_ON_SALE);
                        String collTime = goodsObj.optString(JsonInterface.JK_COLLECTION_TIME);
                        double reducedPrice = goodsObj.optDouble(JsonInterface.JK_REDUCED_PRICE);
                        String address = goodsObj.optString(JsonInterface.JK_ADDRESS);
                        String stockState = goodsObj.optString(JsonInterface.JK_STOCK_STATE);
                        // 是否支持BBC商品
                        fav.setIsBBc(goodsObj.optString(JsonInterface.JK_ISBBC));
                        JSONObject bbcObj = goodsObj.optJSONObject(JsonInterface.JK_BBCSHOPINFO);
                        if (bbcObj != null) {
                            BBCShopInfo bbcShopInfo = new BBCShopInfo();
                            bbcShopInfo.setBbcShopId(bbcObj.optString(JsonInterface.JK_BBCSHOPID));
                            bbcShopInfo.setBbcShopName(bbcObj.optString(JsonInterface.JK_BBCSHOPNAME));
                            bbcShopInfo.setBbcShopImgURL(bbcObj.optString(JsonInterface.JK_BBCSHOPIMGURL));
                            fav.setBbcShopInfo(bbcShopInfo);
                        }
                        JSONArray promArr = goodsObj.optJSONArray(JsonInterface.JK_PROMOTION_LIST);

                        /** 获取促销列表 */
                        ArrayList<Promotions> promList = null;
                        if (promArr != null) {
                            BDebug.d("json", "promArr === " + promArr.length());
                            promList = new ArrayList<Promotions>();
                            for (int j = 0 , length = promArr.length(); j < length; j++) {
                                JSONObject promObj = promArr.getJSONObject(j);
                                String promType = promObj.optString(JsonInterface.JK_PROMOTION_TYPE);

                                String promDesc = promObj.optString(JsonInterface.JK_PROMOTION_DESC);
                                String promPrice = promObj.optString(JsonInterface.JK_PROMOTION_PRICE);
                                Promotions proms = new Promotions();
                                proms.setPromType(promType);
                                proms.setPromDesc(promDesc);
                                proms.setPromPrice(promPrice);
                                promList.add(proms);
                            }
                        }

                        fav.setId(id);
                        fav.setSkuID(skuID);
                        fav.setGoodsNo(goodsNo);
                        fav.setSkuName(skuName);
                        fav.setProductImgUrl(imgUrl);
                        fav.setSalePrice(salePrice);
                        fav.setIsOnsale(isOnSale);
                        fav.setCollectionTime(collTime);
                        fav.setReducedPrice(reducedPrice);
                        fav.setAddress(address);
                        fav.setStockState(stockState);
                        fav.setPromList(promList);
                        list.add(fav);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return list;
    }
}
