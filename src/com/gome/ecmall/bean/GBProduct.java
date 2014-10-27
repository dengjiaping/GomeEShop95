package com.gome.ecmall.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gome.ecmall.bean.Product.ImgUrl;
import com.gome.ecmall.util.UrlMatcher;

/**
 * 团购商品【复合类】 
 */
public class GBProduct implements JsonInterface {

    private static final String JK_TODAYGROUPONBUYLIST = "todayGrouponBuyList";
    private static final String JK_DIVISIONCODE = "divisionCode";
    private static final String JK_CATONE = "catOne";
    private static final String JK_CATTWO = "catTwo";
    private static final String JK_SORTBYCLAUSE = "sortByClause";
    private static final String JK_SORT = "sort";
    private static final String JK_SORTNAME = "sortName";
    private static final String JK_CURRENTPAGE = "currentPage";
    private static final String JK_SKUID = "skuID";
    private static final String JK_GOODSNO = "goodsNo";
    private static final String JK_SKUNAME = "skuName";
    private static final String JK_SALEPROMOITEM = "salePromoItemId";
    private static final String JK_SALEPROMO = "salePromoItem";
    private static final String JK_MOBILENUM = "mobileNum";
    private static final String JK_BUYCOUNT = "buyCount";
    private static final String JK_PAYMODEID = "payModeID";
    private static final String JK_SKUTHUMBIMGURL = "skuThumbImgUrl";
    private static final String JK_GROUPONGOODSMARK = "grouponGoodsMark";
    private static final String JK_SKUORIGINALPRICE = "skuOriginalPrice";
    private static final String JK_SKUGROUPONBUYPRICE = "skuGrouponBuyPrice";
    private static final String JK_PRICEGROUPONBUYPRICE = "priceDiscount";
    private static final String JK_BOUGHTNUM = "boughtNum";
    private static final String JK_STARTNUM = "startNum";
    private static final String JK_EVERYONELIMITBUYNUM = "everyoneLimitBuyNum";
    private static final String JK_RAMAINTIME = "ramainTime";
    private static final String JK_SALESTATE = "saleState";
    private static final String JK_CATONELIST = "catOneList";
    private static final String JK_CATONEID = "catOneId";
    private static final String JK_CATONENAME = "catOneName";
    private static final String JK_CATTWOLIST = "catTwoList";
    private static final String JK_CATTWOID = "catTwoId";
    private static final String JK_CATTWONAME = "catTwoName";
    private static final String JK_SORTLIST = "sortList";
    private static final String JK_SORTKEY = "sortKey";
    private static final String JK_GROUPONGOODSDESC = "grouponGoodsDesc";
    private static final String JK_GROUPONPROPERTY = "grouponProperty";

    /**
     * 团购列表
     * 
     * @param json
     * @return
     */
    public static ArrayList<GroupBuyProduct> parseGroupBuYProductList(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject content = result.getJsContent();
        try {
            JSONArray gbgoodsArray = content.optJSONArray(JK_TODAYGROUPONBUYLIST);
            if (gbgoodsArray != null) {
                ArrayList<GroupBuyProduct> gbproductList = new ArrayList<GroupBuyProduct>();
                for (int i = 0, length = gbgoodsArray.length(); i < length; i++) {
                    JSONObject item = gbgoodsArray.optJSONObject(i);
                    GroupBuyProduct gbproduct = new GroupBuyProduct();
                    gbproduct.setSkuID(item.getString(JK_SKUID));
                    gbproduct.setGoodsNo(item.optString(JK_GOODS_NO));
                    gbproduct.setSkuNo(item.optString(JK_SKU_NO));
                    gbproduct.setSkuName(item.optString(JK_SKUNAME));
                    gbproduct.setSalePromoItem(item.optString(JK_SALEPROMOITEM));
                    gbproduct.setSkuThumbImgUrl(UrlMatcher.getFitListThumbUrl(item.optString(JK_SKUTHUMBIMGURL)));
                    gbproduct.setGrouponGoodsMark(item.optString(JK_GROUPONGOODSMARK));
                    gbproduct.setSkuOriginalPrice(item.optString(JK_SKUORIGINALPRICE));
                    gbproduct.setSkuGrouponBuyPrice(item.optString(JK_SKUGROUPONBUYPRICE));
                    gbproduct.setPriceDiscount(item.optString(JK_PRICEGROUPONBUYPRICE));
                    gbproduct.setBoughtNum(item.optString(JK_BOUGHTNUM));
                    gbproduct.setStartNum(item.optString(JK_STARTNUM));
                    gbproduct.setEveryoneLimitBuyNum(item.optString(JK_EVERYONELIMITBUYNUM));
                    gbproduct.setRamainTime(item.optString(JK_RAMAINTIME));
                    gbproduct.setSaleState(item.optString(JK_SALESTATE));
                    gbproductList.add(gbproduct);
                }// end for
                return gbproductList;
            }// end if

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 团购请求数据
     * 
     * @param divisionCode
     *            地区
     * @param catOne
     *            一级分类
     * @param catTwo
     *            二级分类
     * @param sortByClause
     *            排序字段
     * @param sort
     *            排序方式
     * @param currentPage
     *            当前页
     * @param pageSize
     *            每页条数
     * @return
     */
    public static String createRequestGroupBuyProductListJson(String divisionCode, String divisionName, String catOne,
            String catTwo, String sortByClause, String sort, int currentPage, int pageSize) {
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put(JK_DIVISIONCODE, divisionCode);
            requestJson.put(JK_DIVISION_NAME, divisionName);
            requestJson.put(JK_CATONE, catOne);
            requestJson.put(JK_CATTWO, catTwo);
            requestJson.put(JK_SORTBYCLAUSE, sortByClause);
            requestJson.put(JK_SORT, sort);
            requestJson.put(JK_CURRENTPAGE, currentPage);
            requestJson.put(JK_PAGE_SIZE, pageSize);
            return requestJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 团购分类列表
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
            JSONArray filterArray = content.optJSONArray(JK_CATONELIST);
            JSONArray softArray = content.optJSONArray(JK_SORTLIST);
            if (filterArray != null) {
                ArrayList<FilterCondition> filterConditionList = new ArrayList<FilterCondition>();
                for (int i = 0, length = filterArray.length() + 1; i < length; i++) {
                    if (i == 0) {
                        FilterCondition filterCondition = new FilterCondition();
                        filterCondition.setCatId("");
                        filterCondition.setCatName("全部");
                        filterCondition.setCatType(FilterCondition.FilterCodeOne);
                        ArrayList<FilterCondition> filterTwoConditionList = new ArrayList<FilterCondition>();
                        FilterCondition twofilterCondition = new FilterCondition();
                        twofilterCondition.setCatId("");
                        twofilterCondition.setCatName("全部");
                        twofilterCondition.setCatType(FilterCondition.FilterCodeTwo);
                        filterTwoConditionList.add(twofilterCondition);
                        filterCondition.setFilterConditionList(filterTwoConditionList);
                        filterConditionList.add(filterCondition);
                    } else {
                        JSONObject item = filterArray.optJSONObject(i - 1);
                        FilterCondition filterCondition = new FilterCondition();
                        filterCondition.setCatId(item.optString(JK_CATONEID));
                        filterCondition.setCatName(item.optString(JK_CATONENAME));
                        filterCondition.setCatType(FilterCondition.FilterCodeOne);
                        JSONArray filterTwoArray = item.optJSONArray(JK_CATTWOLIST);
                        if (filterTwoArray != null) {
                            ArrayList<FilterCondition> filterTwoConditionList = new ArrayList<FilterCondition>();
                            for (int j = 0, twolength = filterTwoArray.length(); j < twolength; j++) {
                                JSONObject twoitem = filterTwoArray.getJSONObject(j);
                                FilterCondition twofilterCondition = new FilterCondition();
                                twofilterCondition.setCatId(twoitem.optString(JK_CATTWOID));
                                twofilterCondition.setCatName(twoitem.optString(JK_CATTWONAME));
                                twofilterCondition.setCatType(FilterCondition.FilterCodeTwo);
                                filterTwoConditionList.add(twofilterCondition);
                            }
                            filterCondition.setFilterConditionList(filterTwoConditionList);
                        }
                        filterConditionList.add(filterCondition);
                    }
                }
                filterSoft.setFilterConditionList(filterConditionList);
            }
            if (softArray != null) {
                ArrayList<SortCon> sortConList = new ArrayList<SortCon>();
                for (int i = 0, length = softArray.length(); i < length; i++) {
                    JSONObject item = softArray.getJSONObject(i);
                    SortCon softCon = new SortCon();
                    softCon.setSortKey(item.optString(JK_SORTKEY));
                    softCon.setSort(item.optString(JK_SORTNAME));
                    sortConList.add(softCon);
                }
                filterSoft.setSoftConList(sortConList);
            }
            return filterSoft;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 团购详情请求数据
     * 
     * @param goodsNo
     * @return
     */
    public static String createRequestGroupBuyProductDetailJson(String goodsNo) {
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put(JK_SALEPROMOITEM, goodsNo);
            return requestJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 验证团购是否能去结算
     * 
     * @param skuID
     * @param goodsNo
     * @param salePromoItem
     * @return
     */
    public static String createRequestGroupBuyCheckJson(String skuID, String goodsNo, String salePromoItem) {
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put(JK_SKUID, skuID);
            requestJson.put(JK_GOODSNO, goodsNo);
            requestJson.put(JK_SALEPROMO, salePromoItem);
            return requestJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 虚拟团购确认订单
     * 
     * @param skuID
     * @param goodsNo
     * @param salePromoItem
     * @return
     */
    public static String createRequestGroupBuySubmitJson(String skuID, String goodsNo, String salePromoItem,String buyCount,String payModeID) {
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put(JK_SKUID, skuID);
            requestJson.put(JK_GOODSNO, goodsNo);
            requestJson.put(JK_SALEPROMO, salePromoItem);
            requestJson.put(JK_BUYCOUNT, buyCount);
            requestJson.put(JK_PAYMODEID, payModeID);
            return requestJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 验证抢购是否能去结算
     * 
     * @param skuID
     * @param goodsNo
     * @param salePromoItem
     * @return
     */
    public static String createRequestLimitBuyCheckJson(String skuID, String goodsNo, String rushBuyItemId) {
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put(JK_SKUID, skuID);
            requestJson.put(JK_GOODSNO, goodsNo);
            requestJson.put(JK_RUSH_BUY_ITEM_ID, rushBuyItemId);
            return requestJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static GroupBuyProduct parseGroupBuyProduct(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject content = result.getJsContent();
        GroupBuyProduct gbproduct = new GroupBuyProduct();
        try {
            gbproduct.setSkuID(content.optString(JK_SKUID));
            gbproduct.setGoodsNo(content.optString(JK_GOODS_NO));
            gbproduct.setSkuNo(content.optString(JK_SKU_NO));
            gbproduct.setSkuName(content.optString(JK_SKUNAME));
            gbproduct.setSalePromoItem(content.optString(JK_SALEPROMOITEM));
            gbproduct.setSkuThumbImgUrl(UrlMatcher.getFitGalleryThumbUrl(content.optString(JK_SKUTHUMBIMGURL)));
            gbproduct.setGrouponGoodsMark(content.optString(JK_GROUPONGOODSMARK));
            gbproduct.setSkuOriginalPrice(content.optString(JK_SKUORIGINALPRICE));
            gbproduct.setSkuGrouponBuyPrice(content.optString(JK_SKUGROUPONBUYPRICE));
            gbproduct.setPriceDiscount(content.optString(JK_PRICEGROUPONBUYPRICE));
            gbproduct.setBoughtNum(content.optString(JK_BOUGHTNUM));
            gbproduct.setStartNum(content.optString(JK_STARTNUM));
            gbproduct.setEveryoneLimitBuyNum(content.optString(JK_EVERYONELIMITBUYNUM));
            gbproduct.setRamainTime(content.optString(JK_RAMAINTIME));
            gbproduct.setSaleState(content.optString(JK_SALESTATE));
            gbproduct.setGrouponGoodsDesc(content.optString(JK_GROUPONGOODSDESC));
            gbproduct.setGrouponProperty(content.optString(JK_GROUPONPROPERTY));
            return gbproduct;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 团购商品类
     */
    public static class GroupBuyProduct {
        private String skuID;
        private String goodsNo;
        private String skuNo;
        private String skuName;
        private String salePromoItem;
        private String skuThumbImgUrl;
        private String grouponGoodsMark;
        private String skuOriginalPrice;
        private String skuGrouponBuyPrice;
        private String priceDiscount;
        private String boughtNum;
        private String startNum;
        private String everyoneLimitBuyNum;
        private String ramainTime;
        private String saleState;
        private String grouponGoodsDesc;
        private String grouponProperty;

        private ArrayList<ImgUrl> imgUrlList;

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

        public String getSalePromoItem() {
            return salePromoItem;
        }

        public void setSalePromoItem(String salePromoItem) {
            this.salePromoItem = salePromoItem;
        }

        public String getSkuThumbImgUrl() {
            return skuThumbImgUrl;
        }

        public void setSkuThumbImgUrl(String skuThumbImgUrl) {
            this.skuThumbImgUrl = skuThumbImgUrl;
        }

        public String getGrouponGoodsMark() {
            return grouponGoodsMark;
        }

        public void setGrouponGoodsMark(String grouponGoodsMark) {
            this.grouponGoodsMark = grouponGoodsMark;
        }

        public String getSkuOriginalPrice() {
            return skuOriginalPrice;
        }

        public void setSkuOriginalPrice(String skuOriginalPrice) {
            this.skuOriginalPrice = skuOriginalPrice;
        }

        public String getSkuGrouponBuyPrice() {
            return skuGrouponBuyPrice;
        }

        public void setSkuGrouponBuyPrice(String skuGrouponBuyPrice) {
            this.skuGrouponBuyPrice = skuGrouponBuyPrice;
        }

        public String getPriceDiscount() {
            return priceDiscount;
        }

        public void setPriceDiscount(String priceDiscount) {
            this.priceDiscount = priceDiscount;
        }

        public String getBoughtNum() {
            return boughtNum;
        }

        public void setBoughtNum(String boughtNum) {
            this.boughtNum = boughtNum;
        }

        public String getStartNum() {
            return startNum;
        }

        public void setStartNum(String startNum) {
            this.startNum = startNum;
        }

        public String getEveryoneLimitBuyNum() {
            return everyoneLimitBuyNum;
        }

        public void setEveryoneLimitBuyNum(String everyoneLimitBuyNum) {
            this.everyoneLimitBuyNum = everyoneLimitBuyNum;
        }

        public String getRamainTime() {
            return ramainTime;
        }

        public void setRamainTime(String ramainTime) {
            this.ramainTime = ramainTime;
        }

        public String getSaleState() {
            return saleState;
        }

        public void setSaleState(String saleState) {
            this.saleState = saleState;
        }

        private boolean isLoadImg; // 下载图片

        public boolean isLoadImg() {
            return isLoadImg;
        }

        public void setLoadImg(boolean isLoadImg) {
            this.isLoadImg = isLoadImg;
        }

        public ArrayList<ImgUrl> getImgUrlList() {
            return imgUrlList;
        }

        public void setImgUrlList(ArrayList<ImgUrl> imgUrlList) {
            this.imgUrlList = imgUrlList;
        }

        public String getGrouponGoodsDesc() {
            return grouponGoodsDesc;
        }

        public void setGrouponGoodsDesc(String grouponGoodsDesc) {
            this.grouponGoodsDesc = grouponGoodsDesc;
        }

        public String getGrouponProperty() {
            return grouponProperty;
        }

        public void setGrouponProperty(String grouponProperty) {
            this.grouponProperty = grouponProperty;
        }

    }

    /**
     * 分类以及排序
     */
    public static class FilterSoft {

        private ArrayList<FilterCondition> filterConditionList;
        private ArrayList<SortCon> softConList;

        public ArrayList<FilterCondition> getFilterConditionList() {
            return filterConditionList;
        }

        public void setFilterConditionList(ArrayList<FilterCondition> filterConditionList) {
            this.filterConditionList = filterConditionList;
        }

        public ArrayList<SortCon> getSoftConList() {
            return softConList;
        }

        public void setSoftConList(ArrayList<SortCon> softConList) {
            this.softConList = softConList;
        }

    }

    /**
     * 团购过滤条件
     */
    public static class FilterCondition {

        public static final int FilterCodeOne = 1; // 一级
        public static final int FilterCodeTwo = 2; // 二级
        private String catId;
        private String catName;
        private int catType;
        private boolean selected;
        private ArrayList<FilterCondition> filterConditionList;
        private ArrayList<SortCon> sortList;

        public String getCatId() {
            return catId;
        }

        public void setCatId(String catId) {
            this.catId = catId;
        }

        public String getCatName() {
            return catName;
        }

        public void setCatName(String catName) {
            this.catName = catName;
        }

        public int getCatType() {
            return catType;
        }

        public void setCatType(int catType) {
            this.catType = catType;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public ArrayList<FilterCondition> getFilterConditionList() {
            return filterConditionList;
        }

        public void setFilterConditionList(ArrayList<FilterCondition> filterConditionList) {
            this.filterConditionList = filterConditionList;
        }

        public ArrayList<SortCon> getSortList() {
            return sortList;
        }

        public void setSortList(ArrayList<SortCon> sortList) {
            this.sortList = sortList;
        }

        public FilterCondition getSelectedValue() {
            for (FilterCondition value : filterConditionList) {
                if (value.isSelected()) {
                    return value;
                }
            }
            return null;
        }

        // 清除所有属性列表的选中状态
        public void clearAllValueSelected() {
            for (FilterCondition value : filterConditionList) {
                value.selected = false;
            }
        }

        public void toggleValueAtPostion(int postion) {
            int size = filterConditionList.size();
            if (postion >= size || postion < 0) {
                return;
            }
            for (int i = 0; i < size; i++) {
                if (i == postion) {
                    boolean selected = filterConditionList.get(i).selected;
                    filterConditionList.get(i).selected = !selected;
                } else {
                    filterConditionList.get(i).selected = false;
                }
            }
        }

        public void setValueSelected(int postion, boolean selected) {
            int size = filterConditionList.size();
            if (postion >= size || postion < 0) {
                return;
            }
            for (int i = 0; i < size; i++) {
                if (i == postion) {
                    filterConditionList.get(i).selected = selected;
                } else {
                    filterConditionList.get(i).selected = false;
                }
            }
        }
    }

    /**
     * 团购排序
     */
    public static class SortCon {

        private String sortKey;
        private String sort;
        private boolean selected;

        public String getSortKey() {
            return sortKey;
        }

        public void setSortKey(String sortKey) {
            this.sortKey = sortKey;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}
