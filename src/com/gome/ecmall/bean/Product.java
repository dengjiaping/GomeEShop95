package com.gome.ecmall.bean;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.TextUtils;

import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.UrlMatcher;

/**
 * 商品相关【复合类】
 */
public class Product implements JsonInterface {

    /* 价格升序 */
    public static final int SORTBY_PRICE_ASC = 1;
    /* 价格降序 */
    public static final int SORTBY_PRICE_DESC = 2;
    /* 销量降序 */
    public static final int SORTBY_SALE_DESC = 3;
    /* 热度降序 */
    public static final int SORTBY_HOT = 4;
    /* 上架时间降序 */
    public static final int SORTBY_TIME_DESC = 5;

    public static final String AD_COLOR = "FF6928";
    public static final String PROM_COLOR = "CC0000";
    public static final String YUAN = "￥";

    /**
     * 创建获取产品筛选条件的json
     * 
     * @param goodsTypeId
     *            产品类型ID
     * @return
     */
    public static String createRequestProductFilterConditionJson(String goodsTypeId) {
        JSONObject json = new JSONObject();
        try {
            json.put(JK_GOODS_TYPE_ID, goodsTypeId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    /**
     * 创建筛选分类的json
     * 
     * @param goodsTypeId
     * @param currentPage
     * @param pageSize
     * @param sortBy
     * @param filterConditions
     * @return
     */
    public static String createRequestProductListJson(String goodsTypeId, int currentPage, int pageSize, int sortBy,
            ArrayList<FilterCondition> filterConditions) {
        JSONObject json = new JSONObject();
        try {
            json.put(JK_GOODS_TYPE_ID, goodsTypeId);
            json.put(JK_CURRENT_PAGE, currentPage);
            json.put(JK_PAGE_SIZE, pageSize);
            json.put(JK_SORT_BY, sortBy);
            if (filterConditions != null && filterConditions.size() > 0) {
                JSONArray array = new JSONArray();
                for (FilterCondition condition : filterConditions) {
                    ConditionValue conditionValue = condition.getSelectedValue();
                    // 获得被选中的条件
                    if (conditionValue != null) {
                        JSONObject item = new JSONObject();
                        item.put(JK_FILTER_CONDITION_ID, condition.getFilterConId());
                        item.put(JK_FILTER_VALUE_VALUE, conditionValue.getValue());
                        array.put(item);
                    }
                }
                json.put(JK_FILTER_VALUE_LIST, array);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public static ArrayList<Product> parseProductListJson(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject content = result.getJsContent();
        ArrayList<Product> productList = new ArrayList<Product>();
        try {
            JSONArray goodsArray = content.optJSONArray(JK_GOODS_LIST);
            if (goodsArray != null) {
                for (int i = 0, length = goodsArray.length(); i < length; i++) {
                    JSONObject item = goodsArray.getJSONObject(i);
                    Product product = new Product();
                    product.setGoodsNo(item.optString(JK_GOODS_NO));
                    product.setGoodsName(item.optString(JK_GOODS_NAME));
                    product.setGoodsAd(item.optString(JK_AD));
                    product.setGoodsDiaplayName(Html.fromHtml(CommonUtility.ToDBC(product.getGoodsName()
                            + (TextUtils.isEmpty(product.getGoodsAd()) ? "" : CommonUtility.getColorText(
                                    product.getGoodsAd(), Product.AD_COLOR)))));
                    String productImgUrl = item.optString(JK_PRODUCT_IMG_URL);
                    product.setProductImgUrl(productImgUrl);
                    product.setImgListUrl(UrlMatcher.getFitListThumbUrl(productImgUrl));
                    product.setImgGridUrl(UrlMatcher.getFitGridThumbUrl(productImgUrl));
                    String highestPrice = item.optString(JK_HIGHEST_SALE_PRICE);
                    String lowestPrice = item.optString(JK_LOWEST_SALE_PRICE);
                    product.setHighestPrice(highestPrice);
                    product.setLowestPrice(lowestPrice);
                    // 如果最高价和最低价相同则只显示一个价格，
                    // 如果不一样则显示价格区间从低到高
                    StringBuffer price = new StringBuffer(Product.YUAN);
                    price.append(lowestPrice);
                    if (!highestPrice.equals(lowestPrice)) {
                        price.append(" - ").append(Product.YUAN).append(highestPrice);
                    }
                    // 是否支持BBC商品
                    product.setIsBbc(item.optString(JK_ISBBC));
                    JSONObject bbcJson = item.optJSONObject(JK_BBCSHOPINFO);
                    if (bbcJson != null) {
                        BBCShopInfo bbcShopInfo = new BBCShopInfo();
                        bbcShopInfo.setBbcShopId(bbcJson.optString(JK_BBCSHOPID));
                        bbcShopInfo.setBbcShopName(bbcJson.optString(JK_BBCSHOPNAME));
                        bbcShopInfo.setBbcShopImgURL(bbcJson.optString(JK_BBCSHOPIMGURL));
                        product.setBbcShopInfo(bbcShopInfo);
                    }
                    product.setDisplayPrice(price.toString());
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
                    productList.add(product);
                }// end for
            }// end if

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return productList;
    }

    /**
     * sku级别列表
     * 
     * @param json
     * @return
     */
    public static ArrayList<Product> parseProductSkuListJson(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject content = result.getJsContent();
        ArrayList<Product> productList = new ArrayList<Product>();
        try {
            JSONArray goodsArray = content.optJSONArray(JK_GOODS_LIST);
            if (goodsArray != null) {
                for (int i = 0, length = goodsArray.length(); i < length; i++) {
                    JSONObject item = goodsArray.getJSONObject(i);
                    Product product = new Product();
                    product.setGoodsNo(item.optString(JK_GOODS_NO));
                    product.setGoodsName(item.optString(JK_SKU_NAME));
                    product.setGoodsAd(item.optString(JK_AD));
                    product.setGoodsDiaplayName(Html.fromHtml(CommonUtility.ToDBC(product.getGoodsName()
                            + CommonUtility.getColorText(product.getGoodsAd(), Product.AD_COLOR))));
                    String productImgUrl = item.optString(JK_SKU_THUMB_IMG_URL);
                    product.setProductImgUrl(productImgUrl);
                    product.setImgListUrl(UrlMatcher.getFitListThumbUrl(productImgUrl));
                    product.setImgGridUrl(UrlMatcher.getFitGridThumbUrl(productImgUrl));
                    String highestPrice = item.optString(JK_PROMOTION_PRICE);
                    String lowestPrice = item.optString(JK_PROMOTION_PRICE);
                    product.setHighestPrice(highestPrice);
                    product.setLowestPrice(lowestPrice);
                    // 如果最高价和最低价相同则只显示一个价格，
                    // 如果不一样则显示价格区间从低到高
                    StringBuffer price = new StringBuffer(Product.YUAN);
                    price.append(lowestPrice);
                    if (!highestPrice.equals(lowestPrice)) {
                        price.append(" - ").append(Product.YUAN).append(highestPrice);
                    }
                    product.setDisplayPrice(price.toString());
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
                    productList.add(product);
                }// end for
            }// end if

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return productList;
    }

    /**
     * 解析商品筛选的条件列表
     * 
     * @param json
     * @return
     */
    public static ArrayList<FilterCondition> parseFilterConditionList(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject content = result.getJsContent();
        ArrayList<FilterCondition> filterConditions = new ArrayList<FilterCondition>();
        try {
            JSONArray conditionArray = content.optJSONArray(JK_FILTER_CONDITION_LIST);
            if (conditionArray != null) {
                for (int i = 0, length = conditionArray.length(); i < length; i++) {
                    FilterCondition filterCondition = new FilterCondition();
                    JSONObject conditionObject = conditionArray.getJSONObject(i);
                    filterCondition.setFilterConId(conditionObject.optString(JK_FILTER_CONDITION_ID));
                    filterCondition.setFilterConName(conditionObject.optString(JK_FILTER_CONDITION_NAME));
                    JSONArray valueArray = conditionObject.optJSONArray(JK_FILTER_VALUE_LIST);
                    if (valueArray != null) {
                        for (int m = 0, size = valueArray.length(); m < size; m++) {
                            JSONObject valueObject = valueArray.getJSONObject(m);
                            ConditionValue conditionValue = new ConditionValue();
                            conditionValue.setValue(valueObject.optString(JK_FILTER_VALUE_VALUE));
                            conditionValue.setName(valueObject.optString(JK_FILTER_VALUE_DISPLAY_NAME));
                            filterCondition.addConditionValue(conditionValue);
                        }
                    }
                    filterConditions.add(filterCondition);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return filterConditions;
    }

    /**
     * 商品的筛选类型
     * 
     * @author zhenyu.fang
     * @date 2012-7-10
     */
    public static class FilterType {

        private String typeId;
        private String typeName;

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

    }

    /**
     * 商品分类
     * 
     * @author zhenyu.fang
     * @date 2012-7-16
     */
    public static class GoodsType implements Parcelable {

        public GoodsType() {

        }

        public GoodsType(Parcel in) {
            this.typeId = in.readString();
            this.typeName = in.readString();
        }

        private String typeId;
        private String typeName;

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(typeId);
            dest.writeString(typeName);
        }

        public static final Parcelable.Creator<GoodsType> CREATOR = new Parcelable.Creator<GoodsType>() {

            @Override
            public GoodsType createFromParcel(Parcel source) {
                return new GoodsType(source);
            }

            @Override
            public GoodsType[] newArray(int size) {
                return new GoodsType[size];
            }
        };
    }

    private String goodsNo;
    private String goodsName;
    private String goodsAd;
    private String productImgUrl;
    private String imgListUrl;
    private String imgGridUrl;
    private String imgGalleryUrl;
    private String lowestPrice;
    private String highestPrice;
    private ArrayList<Promotion> promList;
    private String isBbc; // 是否BBC商品
    private BBCShopInfo bbcShopInfo;
    // 用来显示在列表中的商品名称
    private CharSequence goodsDiaplayName;
    // 用来显示在列表中的价格
    private String displayPrice;
    // 无图片版本，是否已经加载过图片
    public boolean isLoadImg;

    public Product() {
        promList = new ArrayList<Promotion>();
    }

    public void addPromotion(Promotion promotion) {
        promList.add(promotion);
    }

    public ArrayList<Promotion> getPromList() {
        return promList;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsAd() {
        return goodsAd;
    }

    public void setGoodsAd(String goodsAd) {
        this.goodsAd = goodsAd;
    }

    public String getProductImgUrl() {
        return productImgUrl;
    }

    public void setProductImgUrl(String productImgUrl) {
        this.productImgUrl = productImgUrl;
    }

    public String getImgListUrl() {
        return imgListUrl;
    }

    public void setImgListUrl(String imgListUrl) {
        this.imgListUrl = imgListUrl;
    }

    public void setImgGalleryUrl(String imgGalleryUrl) {
        this.imgGalleryUrl = imgGalleryUrl;
    }

    public String getImgGalleryUrl() {
        return imgGalleryUrl;
    }

    public String getImgGridUrl() {
        return imgGridUrl;
    }

    public void setImgGridUrl(String imgGridUrl) {
        this.imgGridUrl = imgGridUrl;
    }

    public String getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(String lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public String getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(String highestPrice) {
        this.highestPrice = highestPrice;
    }

    public CharSequence getGoodsDiaplayName() {
        return goodsDiaplayName;
    }

    public void setGoodsDiaplayName(CharSequence goodsDiaplayName) {
        this.goodsDiaplayName = goodsDiaplayName;
    }

    public String getDisplayPrice() {
        return displayPrice;
    }

    public void setDisplayPrice(String displayPrice) {
        this.displayPrice = displayPrice;
    }

    public Promotion getPriorPromtion() {
        if (promList.size() == 0) {
            return null;
        }
        return promList.get(0);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        Product other = (Product) o;
        return goodsNo.equals(other.goodsNo);
    }

    /**
     * BBC商品信息
     */
    public static class BBCShopInfo implements Serializable {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;
        private String bbcShopId;
        private String bbcShopName;
        private String bbcShopImgURL;

        public String getBbcShopId() {
            return bbcShopId;
        }

        public void setBbcShopId(String bbcShopId) {
            this.bbcShopId = bbcShopId;
        }

        public String getBbcShopName() {
            return bbcShopName;
        }

        public void setBbcShopName(String bbcShopName) {
            this.bbcShopName = bbcShopName;
        }

        public String getBbcShopImgURL() {
            return bbcShopImgURL;
        }

        public void setBbcShopImgURL(String bbcShopImgURL) {
            this.bbcShopImgURL = bbcShopImgURL;
        }

    }

    /**
     * 图片URL
     */
    public static class ImgUrl implements Parcelable {
        private String thumbUrl;
        private String sourceUrl;
        private boolean isLoadImg;

        public ImgUrl() {

        }

        public ImgUrl(Parcel in) {
            thumbUrl = in.readString();
            sourceUrl = in.readString();
        }

        public String getThumbUrl() {
            return thumbUrl;
        }

        public void setThumbUrl(String thumbUrl) {
            this.thumbUrl = thumbUrl;
        }

        public String getSourceUrl() {
            return sourceUrl;
        }

        public void setSourceUrl(String sourceUrl) {
            this.sourceUrl = sourceUrl;
        }

        public boolean isLoadImg() {
            return isLoadImg;
        }

        public void setLoadImg(boolean isLoadImg) {
            this.isLoadImg = isLoadImg;
        }

        @Override
        public int describeContents() {

            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(thumbUrl);
            dest.writeString(sourceUrl);
        }

        public static final Parcelable.Creator<ImgUrl> CREATOR = new Parcelable.Creator<ImgUrl>() {
            @Override
            public ImgUrl createFromParcel(Parcel source) {
                return new ImgUrl(source);
            }

            @Override
            public ImgUrl[] newArray(int size) {
                return new ImgUrl[size];
            }
        };
    }

    /**
     * 产品筛选过滤条件
     * 
     * @author zhenyu.fang
     * @date 2012-7-21
     */
    public static class FilterCondition {
        private String filterConId;
        private String filterConName;
        private ArrayList<ConditionValue> valueList;

        public FilterCondition() {
            valueList = new ArrayList<Product.ConditionValue>();
        }

        public void addConditionValue(ConditionValue value) {
            valueList.add(value);
        }

        public String getFilterConId() {
            return filterConId;
        }

        public void setFilterConId(String filterConId) {
            this.filterConId = filterConId;
        }

        public String getFilterConName() {
            return filterConName;
        }

        public void setFilterConName(String filterConName) {
            this.filterConName = filterConName;
        }

        public ArrayList<ConditionValue> getConditionValues() {
            ArrayList<ConditionValue> list = new ArrayList<Product.ConditionValue>(valueList);
            return list;
        }

        public ConditionValue getSelectedValue() {
            for (ConditionValue value : valueList) {
                if (value.isSelected()) {
                    return value;
                }
            }
            return null;
        }

        // 清除所有属性列表的选中状态
        public void clearAllValueSelected() {
            for (ConditionValue value : valueList) {
                value.selected = false;
            }
        }

        public void toggleValueAtPostion(int postion) {
            int size = valueList.size();
            if (postion >= size || postion < 0) {
                return;
            }
            for (int i = 0; i < size; i++) {
                if (i == postion) {
                    boolean selected = valueList.get(i).selected;
                    valueList.get(i).selected = !selected;
                } else {
                    valueList.get(i).selected = false;
                }
            }
        }

        public void setValueSelected(int postion, boolean selected) {
            int size = valueList.size();
            if (postion >= size || postion < 0) {
                return;
            }
            for (int i = 0; i < size; i++) {
                if (i == postion) {
                    valueList.get(i).selected = selected;
                } else {
                    valueList.get(i).selected = false;
                }
            }
        }

    }

    /**
     * 产品筛选过滤条件的值
     * 
     * @author Administrator
     * @date 2012-7-21
     */
    public static class ConditionValue {
        private String value;
        private String name;
        private boolean selected;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return selected;
        }
    }

    @Override
    public String toString() {
        return "Product [goodsNo=" + goodsNo + ", goodsName=" + goodsName + ", goodsAd=" + goodsAd + ", productImgUrl="
                + productImgUrl + ", imgListUrl=" + imgListUrl + ", imgGridUrl=" + imgGridUrl + ", imgGalleryUrl="
                + imgGalleryUrl + ", lowestPrice=" + lowestPrice + ", highestPrice=" + highestPrice + ", promList="
                + promList + ", isBbc=" + isBbc + ", bbcShopInfo=" + bbcShopInfo + ", goodsDiaplayName="
                + goodsDiaplayName + ", displayPrice=" + displayPrice + ", isLoadImg=" + isLoadImg + "]";
    }
}
