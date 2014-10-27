package com.gome.ecmall.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.Html;

import com.gome.ecmall.bean.Product.BBCShopInfo;
import com.gome.ecmall.bean.Product.FilterType;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.UrlMatcher;

/**
 * 搜索结果
 */
public class SearchResult implements JsonInterface {

    /**
     * 创建请求搜索结果的Json
     * 
     * @param keyWord
     * @param filterTypeId
     * @param currentPage
     * @param pageSize
     * @param sortBy
     * @return
     */
    public static final String createRequestSearchResultJson(String keyWord, String filterTypeId, int currentPage,
            int pageSize, int sortBy) {
        JSONObject object = new JSONObject();
        try {
            object.put(JK_KEYWORD, keyWord);
            object.put(JK_FILTER_TYPE_ID, filterTypeId);
            object.put(JK_CURRENT_PAGE, currentPage);
            object.put(JK_PAGE_SIZE, pageSize);
            object.put(JK_SORT_BY, sortBy);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public static SearchResult parseSearchResult(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject content = result.getJsContent();
        SearchResult searchResult = new SearchResult();
        try {
            searchResult.setCount(content.optString(JK_SEARCHABLE_COUNT));
            JSONArray array = content.optJSONArray(JK_FILTER_TYPE_LIST);
            if (array != null) {
                for (int i = 0, length = array.length(); i < length; i++) {
                    JSONObject item = array.getJSONObject(i);
                    FilterType filterType = new FilterType();
                    filterType.setTypeId(item.getString(JK_FILTER_TYPE_ID));
                    filterType.setTypeName(item.getString(JK_FILTER_TYPE_NAME));
                    searchResult.addFilterType(filterType);
                }
            }
            JSONArray goodsArray = content.optJSONArray(JK_GOODS_LIST);
            if (goodsArray != null) {
                for (int i = 0, length = goodsArray.length(); i < length; i++) {
                    JSONObject item = goodsArray.getJSONObject(i);
                    Product product = new Product();
                    product.setGoodsNo(item.optString(JK_GOODS_NO));
                    product.setGoodsName(item.optString(JK_GOODS_NAME));
                    product.setGoodsAd(item.optString(JK_AD));
                    product.setGoodsDiaplayName(Html.fromHtml(CommonUtility.ToDBC(product.getGoodsName()
                            + CommonUtility.getColorText(product.getGoodsAd(), Product.AD_COLOR))));
                    String productImgUrl = item.optString(JK_PRODUCT_IMG_URL);
                    product.setProductImgUrl(productImgUrl);
                    product.setImgListUrl(UrlMatcher.getFitListThumbUrl(productImgUrl));
                    product.setImgGridUrl(UrlMatcher.getFitGridThumbUrl(productImgUrl));
                    String highestPrice = item.optString(JK_HIGHEST_SALE_PRICE);
                    String lowestPrice = item.optString(JK_LOWEST_SALE_PRICE);
                    StringBuffer price = new StringBuffer(Product.YUAN);
                    price.append(lowestPrice);
                    if (!highestPrice.equals(lowestPrice)) {
                        price.append(" - ").append(Product.YUAN).append(highestPrice);
                    }
                    product.setHighestPrice(highestPrice);
                    product.setLowestPrice(lowestPrice);
                    product.setDisplayPrice(price.toString());
                    // 是否BBC商品及信息
                    product.setIsBbc(item.optString(JK_ISBBC));
                    JSONObject bbcJson = item.optJSONObject(JK_BBCSHOPINFO);
                    if (bbcJson != null) {
                        BBCShopInfo bbcShopInfo = new BBCShopInfo();
                        bbcShopInfo.setBbcShopId(bbcJson.optString(JK_BBCSHOPID));
                        bbcShopInfo.setBbcShopName(bbcJson.optString(JK_BBCSHOPNAME));
                        bbcShopInfo.setBbcShopImgURL(bbcJson.optString(JK_BBCSHOPIMGURL));
                        product.setBbcShopInfo(bbcShopInfo);
                    }
                    // 促销信息
                    JSONArray promArray = item.optJSONArray(JK_PROMOTION_LIST);
                    if (promArray != null) {
                        for (int m = 0, size = promArray.length(); m < size; m++) {
                            JSONObject promJson = promArray.getJSONObject(m);
                            Promotion promotion = new Promotion();
                            promotion.setType(promJson.getInt(JK_PROMOTION_TYPE));
                            promotion.setPrice(promJson.optString(JK_PROMOTION_PRICE));
                            promotion.setDesc(promJson.optString(JK_PROMOTION_DESC));
                            product.addPromotion(promotion);
                        }
                    }
                    searchResult.addProduct(product);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return searchResult;
    }

    private String count;
    private ArrayList<FilterType> filterTypeList;
    private ArrayList<Product> goodsList;

    public SearchResult() {
        filterTypeList = new ArrayList<Product.FilterType>();
        goodsList = new ArrayList<Product>();
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCount() {
        return count;
    }

    public void addFilterType(FilterType filterType) {
        filterTypeList.add(filterType);
    }

    public ArrayList<FilterType> getFilterTypeList() {
        ArrayList<FilterType> arrayList = new ArrayList<FilterType>();
        for (FilterType type : filterTypeList) {
            arrayList.add(type);
        }
        return arrayList;
    }

    public void addProduct(Product product) {
        goodsList.add(product);
    }

    public ArrayList<Product> getProductList() {
        ArrayList<Product> arrayList = new ArrayList<Product>();
        for (Product product : goodsList) {
            arrayList.add(product);
        }
        return arrayList;
    }

}
