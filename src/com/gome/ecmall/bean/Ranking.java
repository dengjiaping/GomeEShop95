package com.gome.ecmall.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.gome.ecmall.bean.Product.BBCShopInfo;
import com.gome.ecmall.util.UrlMatcher;

/**
 * 排行榜
 */
public class Ranking implements JsonInterface {

    /******************** 排行榜 *************************/

    /******************** 服务器响应字段 *************************/

    // 商品列表
    public static final String JK_RANKING_LIST = "rankingList";
    // 排名
    public static final String JK_ORDER_NO = "orderNo";
    // 商品SKU
    public static final String JK_SKU_ID = "skuID";
    // 商品ID
    public static final String JK_GOODS_NO = "goodsNo";
    // 商品skuNo
    public static final String JK_SKU_NO = "skuNo";
    // 商品名称
    public static final String JK_SKU_NAME = "skuName";
    // 商品sku小图url
    public static final String JK_SKU_THUMB_IMB_URL = "skuThumbImgUrl";
    // 商品原价格
    public static final String JK_SKU_ORIGINAL_PRICE = "skuOriginalPrice";

    // 商品排名升降
    public static final String JK_RANKING_STATE = "rankingState";
    // 评价分数
    public static final String JK_APPRAISE_SOCG = "appraiseSocg";
    // 评价等级
    public static final String JK_APPRAISE_GRADE = "appraiseGrade";
    // 商品浏览量
    public static final String JK_VIEW_NUM = "viewNum";

    /******************** 客户端请求字段 *************************/

    // 商品排行类别
    public static final String JK_SORT_TYPE = "sortType";
    // 商品分类
    public static final String JK_CATEGORY_ID = "categoryId";
    // 当前页
    public static final String JK_CURRENT_PAGE = "currentPage";
    // 每页条数
    public static final String JK_PAGE_SIZE = "pageSize";

    /******************** 筛选字段 *************************/

    // 商品主分类列表
    public static final String JK_CATEGORY_LIST = "categoryList";
    // 商品分类ID
    public static final String JK_FILTER_CATEGORY_ID = "categoryId";
    // 商品分类名称
    public static final String JK_FILTER_CATEGORY_NAME = "categoryName";

    /* 热销榜 */
    public static final int SORT_TYPE_SALE = 0;
    /* 降价榜 */
    public static final int SORT_TYPE_PRICE = 1;
    /* 热点榜 */
    public static final int SORT_TYPE_HOT = 2;
    public static String errorMessage;

    /**
     * 创建获取产品筛选条件的json
     * 
     * @param categoryId
     *            产品类型ID
     * @return
     */
    // public static String createRequestRankingFilterConditionJson(String categoryId) {
    // JSONObject json = new JSONObject();
    // try {
    // json.put(JK_CATEGORY_ID, categoryId);
    // } catch (JSONException e) {
    // e.printStackTrace();
    // }
    // return json.toString();
    // }

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
    public static String createRequestRankingListJson(String categoryId, int currentPage, int pageSize,
            int currentSortType, ArrayList<FilterType> filterConditions) {
        JSONObject json = new JSONObject();
        try {
            json.put(JK_CATEGORY_ID, categoryId);
            json.put(JK_CURRENT_PAGE, currentPage);
            json.put(JK_PAGE_SIZE, pageSize);
            json.put(JK_SORT_TYPE, currentSortType);
            if (filterConditions != null && filterConditions.size() > 0) {
                JSONArray array = new JSONArray();
                for (FilterType condition : filterConditions) {
                    // 获得被选中的条件

                    JSONObject item = new JSONObject();
                    item.put(JK_FILTER_CATEGORY_ID, condition.getCategoryId());
                    item.put(JK_FILTER_CATEGORY_NAME, condition.getCategoryName());
                    array.put(item);
                }

                json.put(JK_CATEGORY_LIST, array);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public static ArrayList<Ranking> parseRankingListJson(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            setErrorMessage(result.getFailReason());
            return null;
        }
        JSONObject content = result.getJsContent();
        ArrayList<Ranking> rankingList = new ArrayList<Ranking>();
        try {
            JSONArray goodsArray = content.optJSONArray(JK_RANKING_LIST);
            if (goodsArray != null) {
                for (int i = 0, length = goodsArray.length(); i < length; i++) {
                    JSONObject item = goodsArray.getJSONObject(i);
                    Ranking ranking = new Ranking();
                    ranking.setGoodsNo(item.optString(JK_GOODS_NO));
                    ranking.setSkuId(item.optString(JK_SKU_ID));
                    ranking.setNum(item.optInt(JK_ORDER_NO));
                    ranking.setSkuThumbImgUrl(UrlMatcher.getFitListThumbUrl(item.optString(JK_SKU_THUMB_IMB_URL)));
                    ranking.setSkuNo(item.optString(JK_SKU_NO));
                    ranking.setSkuName(item.optString(JK_SKU_NAME));
                    ranking.setSkuOriginalPrice(item.optString(JK_SKU_ORIGINAL_PRICE));
                    ranking.setRankingState(item.optString(JK_RANKING_STATE));
                    ranking.setAppraiseSocg(item.optString(JK_APPRAISE_SOCG));
                    ranking.setAppraiseGrade(item.optString(JK_APPRAISE_GRADE));
                    ranking.setViewNum(item.optString(JK_VIEW_NUM));
                    // 是否支持BBC
                    ranking.setIsBbc(item.optString(JK_ISBBC));
                    JSONObject bbcObj = item.optJSONObject(JK_BBCSHOPINFO);
                    if (bbcObj != null) {
                        BBCShopInfo bbcShopInfo = new BBCShopInfo();
                        bbcShopInfo.setBbcShopId(bbcObj.optString(JK_BBCSHOPID));
                        bbcShopInfo.setBbcShopName(bbcObj.optString(JK_BBCSHOPNAME));
                        bbcShopInfo.setBbcShopImgURL(bbcObj.optString(JK_BBCSHOPIMGURL));
                        ranking.setBbcShopInfo(bbcShopInfo);
                    }
                    rankingList.add(ranking);
                }// end for
            }// end if

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return rankingList;
    }

    /**
     * 解析商品筛选的条件列表
     * 
     * @param json
     * @return
     */
    public static FilterSoft parseFilterConditionList(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject content = result.getJsContent();
        try {
            FilterSoft filterSoft = new FilterSoft();
            JSONArray conditionArray = content.optJSONArray(JK_CATEGORY_LIST);
            if (conditionArray != null) {
                ArrayList<FilterType> sortConList = new ArrayList<FilterType>();
                for (int i = 0, length = conditionArray.length(); i < length; i++) {
                    FilterType filterCondition = new FilterType();
                    JSONObject conditionObject = conditionArray.getJSONObject(i);
                    filterCondition.setCategoryId(conditionObject.getString(JK_FILTER_CATEGORY_ID));
                    filterCondition.setCategoryName(conditionObject.getString(JK_FILTER_CATEGORY_NAME));

                    sortConList.add(filterCondition);
                }
                filterSoft.setSoftConList(sortConList);
            }
            return filterSoft;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 商品的筛选类型
     * 
     * @author zhenyu.fang
     * @date 2012-7-10
     */
    public static class FilterType {

        private String categoryId;
        private String categoryName;
        private boolean selected;

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

    /**
     * 商品筛选条件列表【实体】
     */
    public static class FilterSoft {
        private ArrayList<FilterType> softConList;

        public ArrayList<FilterType> getSoftConList() {
            return softConList;
        }

        public void setSoftConList(ArrayList<FilterType> softConList) {
            this.softConList = softConList;
        }
    }

    /**
     * 商品分类
     * 
     * @author zhenyu.fang
     * @date 2012-7-16
     */
    public static class SortType implements Parcelable {

        public SortType() {

        }

        public SortType(Parcel in) {
            this.categoryId = in.readString();
            this.categoryName = in.readString();
        }

        private String categoryId;
        private String categoryName;

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(categoryId);
            dest.writeString(categoryName);
        }

        public static final Parcelable.Creator<SortType> CREATOR = new Parcelable.Creator<SortType>() {

            @Override
            public SortType createFromParcel(Parcel source) {
                return new SortType(source);
            }

            @Override
            public SortType[] newArray(int size) {
                return new SortType[size];
            }
        };
    }

    private int num;
    private String skuId;
    private String goodsNo;
    private String skuNo;
    private String skuName;
    private String skuThumbImgUrl;
    private String skuOriginalPrice;
    private String rankingState;
    private String appraiseSocg;
    private String appraiseGrade;
    private String viewNum;
    // 是否支持BBC商品
    private String isBbc;
    private BBCShopInfo bbcShopInfo;
    // 用来显示在列表中的商品名称
    private CharSequence goodsDiaplayName;
    // 用来显示在列表中的价格
    private String displayPrice;
    // 无图片版本，是否已经加载过图片
    public boolean isLoadImg;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

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

    public void setSkuOriginalPrice(String skuOriginalPrice) {
        this.skuOriginalPrice = skuOriginalPrice;
    }

    public String getSkuOriginalPrice() {
        return skuOriginalPrice;
    }

    public String getRankingState() {
        return rankingState;
    }

    public void setRankingState(String rankingState) {
        this.rankingState = rankingState;
    }

    public String getAppraiseSocg() {
        return appraiseSocg;
    }

    public void setAppraiseSocg(String appraiseSocg) {
        this.appraiseSocg = appraiseSocg;
    }

    public String getAppraiseGrade() {
        return appraiseGrade;
    }

    public void setAppraiseGrade(String appraiseGrade) {
        this.appraiseGrade = appraiseGrade;
    }

    public String getViewNum() {
        return viewNum;
    }

    public void setViewNum(String viewNum) {
        this.viewNum = viewNum;
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
        if (!(o instanceof Ranking)) {
            return false;
        }
        Ranking other = (Ranking) o;
        return goodsNo.equals(other.goodsNo);
    }

    /**
     * 图片URL【重复】
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
        private String categoryId;
        private String categoryName;
        private ArrayList<FilterCondition> valueList;
        private ArrayList<FilterType> sortList;
        private boolean selected;

        public ArrayList<FilterType> getSortList() {
            return sortList;
        }

        public void setSortList(ArrayList<FilterType> sortList) {
            this.sortList = sortList;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public boolean isSelected() {
            return selected;
        }

        public ArrayList<FilterCondition> getFilterCondition() {
            ArrayList<FilterCondition> list = new ArrayList<Ranking.FilterCondition>(valueList);
            return list;
        }

        public FilterCondition getSelectedValue() {
            for (FilterCondition value : valueList) {
                if (value.isSelected()) {
                    return value;
                }
            }
            return null;
        }

        // 清除所有属性列表的选中状态
        public void clearAllValueSelected() {
            for (FilterCondition value : valueList) {
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

    public static String getErrorMessage() {
        return errorMessage;
    }

    public static void setErrorMessage(String errorMessage) {
        Ranking.errorMessage = errorMessage;
    }

}
