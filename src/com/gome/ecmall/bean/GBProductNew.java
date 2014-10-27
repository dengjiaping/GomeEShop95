package com.gome.ecmall.bean;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gome.ecmall.bean.Product.ImgUrl;
import com.gome.ecmall.home.groupbuy.NewGroupBuyActivity;

/**
 * 团购/虚拟团购【复合类】【业务类】
 */
public class GBProductNew implements JsonInterface {

    private static final String JK_TODAYGROUPONBUYLIST = "todayGrouponBuyList";
    private static final String JK_DIVISIONCODE = "divisionCode";
    private static final String JK_DIVISIONNAME = "divisionName";
    private static final String JK_CATEGORY_ID = "categoryId";
    private static final String JK_CATONE = "catOne";
    private static final String JK_CATTWO = "catTwo";
    private static final String JK_SORTBYCLAUSE = "sortByClause";
    private static final String JK_SORT = "sort";
    private static final String JK_CURRENTPAGE = "currentPage";
    private static final String JK_SKUID = "skuID";
    private static final String JK_GOODSNO = "goodsNo";
    private static final String JK_SKUNAME = "skuName";
    private static final String JK_PRODUCTNAME = "productName";
    private static final String JK_SALEPROMOITEM = "salePromoItemId";
    private static final String JK_SALEPROMO = "salePromoItem";
    private static final String JK_SKUTHUMBIMGURL = "skuThumbImgUrl";
    private static final String JK_GROUPONGOODSMARK = "grouponGoodsMark";
    private static final String JK_SKUORIGINALPRICE = "skuOriginalPrice";
    private static final String JK_SKUGROUPONBUYPRICE = "skuGrouponBuyPrice";
    private static final String JK_PRICEGROUPONBUYPRICE = "priceDiscount";
    private static final String JK_BOUGHTNUM = "boughtNum";
    private static final String JK_STARTNUM = "startNum";
    private static final String JK_CELLPHONE = "cellphone";
    private static final String JK_EVERYONELIMITBUYNUM = "everyoneLimitBuyNum";
    private static final String JK_LIMITBUYNUM = "limitBuyNum";
    private static final String JK_RAMAINTIME = "ramainTime";
    private static final String JK_SALESTATE = "saleState";
    private static final String JK_GROUPON_GOODS_IMGURL = "grouponGoodsImgUrl";
    private static final String JK_CATEGORY_COLOR = "categoryColor";
    private static final String JK_CATEGORY_LIST = "categoryList";
    private static final String JK_CATEGORY_NAME = "categoryName";
    private static final String JK_IMAGE_URL = "imageUrl";
    private static final String JK_GOODS_COUNT = "goodsCount";
    private static final String JK_HOT_DIVISION_LIST = "hotDivisionList";
    private static final String JK_DIVISION_LIST = "divisionList";
    private static final String JK_DIVISION_PINYIN = "divisionPinyin";
    private static final String JK_GROUPONGOODSDESC = "grouponGoodsDesc";
    private static final String JK_GOODS_SHAREURL = "goodsShareUrl";
    private static final String JK_GROUPONPROPERTY = "grouponProperty";
    private static final String JK_SPECIFICATIONS = "specifications";
    private static final String JK_APPRAISE_COUNT = "appraiseCount";
    private static final String JK_HIGH_PRAISE = "highPraise";
    private static final String JK_GROUPON_TYPE = "grouponType";
    private static final String JK_ISSUPPORT_REFUND = "isSupportRefund";
    private static final String JK_STORE_ADDRESS_LIST = "storeAddressList";
    private static final String JK_STORE_NAME = "storeName";
    private static final String JK_ADDRESS = "address";
    private static final String JK_TELEPHONE = "telephone";
    private static final String JK_LONGITUDE = "longitude";
    private static final String JK_LATITUDE = "latitude";
    private static final String JK_HOTKEY_WORDS = "hotKeyWords";
    private static final String JK_WORD = "word";
    private static final String JK_TOTAL_COUNT = "totalCount";
    private static final String JK_TOTAL_PAGE = "totalPage";
    private static final String JK_PAGE_SIZE = "pageSize";
    private static final String JK_CURRENT_PAGE = "currentPage";
    private static final String JK_DIVISION_NAME = "divisionName";

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

            NewGroupBuyActivity.totalCount = content.optInt(JK_TOTAL_COUNT);
            NewGroupBuyActivity.totalPage = content.optInt(JK_TOTAL_PAGE);
            NewGroupBuyActivity.pageSize = content.optInt(JK_PAGE_SIZE);
            NewGroupBuyActivity.ser_currentPage = content.optInt(JK_CURRENT_PAGE);
            NewGroupBuyActivity.show_city_name = content.optString(JK_DIVISION_NAME);
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
                    gbproduct.setSkuThumbImgUrl(item.optString(JK_SKUTHUMBIMGURL));
                    gbproduct.setGrouponGoodsMark(item.optString(JK_GROUPONGOODSMARK));
                    gbproduct.setSkuOriginalPrice(item.optString(JK_SKUORIGINALPRICE));
                    gbproduct.setSkuGrouponBuyPrice(item.optString(JK_SKUGROUPONBUYPRICE));
                    gbproduct.setPriceDiscount(item.optString(JK_PRICEGROUPONBUYPRICE));
                    gbproduct.setBoughtNum(item.optString(JK_BOUGHTNUM));
                    gbproduct.setStartNum(item.optString(JK_STARTNUM));
                    gbproduct.setEveryoneLimitBuyNum(item.optString(JK_EVERYONELIMITBUYNUM));
                    gbproduct.setRamainTime(item.optString(JK_RAMAINTIME));
                    gbproduct.setSaleState(item.optString(JK_SALESTATE));
                    gbproduct.setGrouponGoodsImgUrl(item.optString(JK_GROUPON_GOODS_IMGURL));
                    gbproduct.setCategoryColor(item.optString(JK_CATEGORY_COLOR));
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
    public static String createRequestGroupBuyProductListJson(String divisionCode, String divisionName,
            String categoryId, String catOne, String catTwo, String sortByClause, String sort, int currentPage,
            int pageSize,String question) {
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put(JK_DIVISIONCODE, divisionCode);
            requestJson.put(JK_DIVISION_NAME, divisionName);
            requestJson.put(JK_CATEGORY_ID, categoryId);
            requestJson.put(JK_CATONE, catOne);
            requestJson.put(JK_CATTWO, catTwo);
            requestJson.put(JK_SORTBYCLAUSE, sortByClause);
            requestJson.put(JK_SORT, sort);
            requestJson.put(JK_CURRENTPAGE, currentPage);
            requestJson.put(JK_PAGE_SIZE, pageSize);
            requestJson.put(JK_QUESTION, question);
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
    public static CategroyAndSort parseFilterConditionList(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject content = result.getJsContent();
        try {
            CategroyAndSort categroyAndSort = new CategroyAndSort();
            ArrayList<Sort> sortList = null;
            ArrayList<Category> categoryList = null;

            JSONArray sortArray = content.optJSONArray("sortList");
            if (sortArray != null) {
                sortList = new ArrayList<Sort>();
                for (int i = 0, length = sortArray.length(); i < length; i++) {
                    JSONObject item = sortArray.getJSONObject(i);
                    Sort sort = new Sort();
                    sort.setSortKey(item.optString("sortKey"));
                    sort.setSortName(item.optString("sortName"));
                    sortList.add(sort);
                }
                categroyAndSort.setSortList(sortList);
            }
            JSONArray categoryOne = content.optJSONArray("categoryList");
            if (categoryOne != null) {
                categoryList = new ArrayList<Category>();
                for (int i = 0, length = categoryOne.length(); i < length; i++) {
                    JSONObject item = categoryOne.getJSONObject(i);
                    Category category = new Category();
                    category.setCategoryId(item.optString("categoryId"));
                    category.setCategoryName(item.optString("categoryName"));
                    JSONArray categorytwoArray = item.optJSONArray("childCategoryList");
                    category.setSunCategroyList(getCategorys(categorytwoArray));
                    categoryList.add(category);
                }
                categroyAndSort.setCategroyList(categoryList);
            }
            return categroyAndSort;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 递归循环三级
    public static ArrayList<Category> getCategorys(JSONArray categoryArray) {
        if (categoryArray != null) {
            ArrayList<Category> list = new ArrayList<Category>();
            for (int j = 0,length = categoryArray.length(); j < length; j++) {
                JSONObject item = categoryArray.optJSONObject(j);
                Category category = new Category();
                category.setCategoryId(item.optString("categoryId"));
                category.setCategoryName(item.optString("categoryName"));
                JSONArray categoryThreeArray = item.optJSONArray("childCategoryList");
                category.setSunCategroyList(getCategorys(categoryThreeArray));
                list.add(category);
            }
            return list;
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

    public static VirtualGroupCart parseGroupBuyCart(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject content = result.getJsContent();
        VirtualGroupCart gbproduct = new VirtualGroupCart();
        try {
            gbproduct.setSkuID(content.optString(JK_SKUID));
            gbproduct.setGoodsNo(content.optString(JK_GOODS_NO));
            gbproduct.setSalePromoItem(content.optString(JK_SALEPROMOITEM));
            gbproduct.setSkuThumbImgUrl(content.optString(JK_SKUTHUMBIMGURL));
            gbproduct.setSkuGrouponBuyPrice(content.optString(JK_SKUGROUPONBUYPRICE));
            gbproduct.setStartNum(content.optString(JK_STARTNUM));
            gbproduct.setCellphone(content.optString(JK_CELLPHONE));
            gbproduct.setLimitBuyNum(content.optString(JK_LIMITBUYNUM));
            gbproduct.setProductName(content.optString(JK_PRODUCTNAME));
            return gbproduct;
        } catch (Exception e) {
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
            gbproduct.setSkuThumbImgUrl(content.optString(JK_SKUTHUMBIMGURL));
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
            gbproduct.setGoodsShareUrl(content.optString(JK_GOODS_SHAREURL));
            gbproduct.setGrouponProperty(content.optString(JK_GROUPONPROPERTY));
            gbproduct.setSpecifications(content.optString(JK_SPECIFICATIONS));
            gbproduct.setAppraiseCount(content.optInt(JK_APPRAISE_COUNT));
            gbproduct.setHighPraise(content.optString(JK_HIGH_PRAISE));
            gbproduct.setGrouponType(content.optString(JK_GROUPON_TYPE));
            gbproduct.setIsSupportRefund(content.optString(JK_ISSUPPORT_REFUND));
            JSONArray storeAddressArray = content.optJSONArray(JK_STORE_ADDRESS_LIST);
            if (storeAddressArray != null) {
                ArrayList<StoreAddress> storeAddressList = new ArrayList<StoreAddress>();
                for (int i = 0, length = storeAddressArray.length(); i < length; i++) {
                    JSONObject item = storeAddressArray.getJSONObject(i);
                    StoreAddress storeAddress = new StoreAddress();
                    storeAddress.setStoreName(item.optString(JK_STORE_NAME));
                    storeAddress.setAddress(item.optString(JK_ADDRESS));
                    storeAddress.setTelephone(item.optString(JK_TELEPHONE));
                    storeAddress.setLatitude(item.optDouble(JK_LATITUDE));
                    storeAddress.setLongitude(item.optDouble(JK_LONGITUDE));
                    storeAddressList.add(storeAddress);
                }
                gbproduct.setStoreAddressList(storeAddressList);
            }
            return gbproduct;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 虚拟团购商家信息
     */
    public static class StoreAddress implements Serializable{
        private String storeName;
        private String address;
        private String telephone;
        private double longitude;
        private double latitude;
        public String getStoreName() {
            return storeName;
        }
        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }
        public String getAddress() {
            return address;
        }
        public void setAddress(String address) {
            this.address = address;
        }
        public String getTelephone() {
            return telephone;
        }
        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }
        public double getLongitude() {
            return longitude;
        }
        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
        public double getLatitude() {
            return latitude;
        }
        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }
        
    }

    /**
     * 团购商品类
     */
    public static class GroupBuyProduct extends Goods {

        private String salePromoItem;
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
        private String grouponGoodsImgUrl;// 大图图片
        private String categoryColor;// 背景色
        
        private String grouponType;//团购类型
        private String isSupportRefund;//是否支持退款
        private ArrayList<StoreAddress> storeAddressList;//商家地址列表
        private String goodsShareUrl;//分享地址
        private String specifications;//规格参数
        private int appraiseCount;//评价数量
        private String highPraise;//好评度
        

        public String getGoodsShareUrl() {
            return goodsShareUrl;
        }

        public void setGoodsShareUrl(String goodsShareUrl) {
            this.goodsShareUrl = goodsShareUrl;
        }

        public String getSpecifications() {
            return specifications;
        }

        public void setSpecifications(String specifications) {
            this.specifications = specifications;
        }

        public int getAppraiseCount() {
            return appraiseCount;
        }

        public void setAppraiseCount(int appraiseCount) {
            this.appraiseCount = appraiseCount;
        }

        public String getHighPraise() {
            return highPraise;
        }

        public void setHighPraise(String highPraise) {
            this.highPraise = highPraise;
        }

        public String getGrouponType() {
            return grouponType;
        }

        public void setGrouponType(String grouponType) {
            this.grouponType = grouponType;
        }

        public String getIsSupportRefund() {
            return isSupportRefund;
        }

        public void setIsSupportRefund(String isSupportRefund) {
            this.isSupportRefund = isSupportRefund;
        }

        public ArrayList<StoreAddress> getStoreAddressList() {
            return storeAddressList;
        }

        public void setStoreAddressList(ArrayList<StoreAddress> storeAddressList) {
            this.storeAddressList = storeAddressList;
        }

        public String getGrouponGoodsImgUrl() {
            return grouponGoodsImgUrl;
        }

        public void setGrouponGoodsImgUrl(String grouponGoodsImgUrl) {
            this.grouponGoodsImgUrl = grouponGoodsImgUrl;
        }

        public String getCategoryColor() {
            return categoryColor;
        }

        public void setCategoryColor(String categoryColor) {
            this.categoryColor = categoryColor;
        }

        private ArrayList<ImgUrl> imgUrlList;

        public String getSalePromoItem() {
            return salePromoItem;
        }

        public void setSalePromoItem(String salePromoItem) {
            this.salePromoItem = salePromoItem;
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

    public static class VirtualGroupCart{
        private String cellphone;//绑定的手机号
        private String productName;//商品名称
        private String limitBuyNum;//团购每人限购数量
        private String skuGrouponBuyPrice;//商品团购价格              
        private String startNum;//商品团购最低数量
        private String skuID;
        private String goodsNo;
        private String salePromoItem;
        private String skuThumbImgUrl;//商品sku小图URL
        public String getCellphone() {
            return cellphone;
        }
        public void setCellphone(String cellphone) {
            this.cellphone = cellphone;
        }
        public String getProductName() {
            return productName;
        }
        public void setProductName(String productName) {
            this.productName = productName;
        }
        public String getLimitBuyNum() {
            return limitBuyNum;
        }
        public void setLimitBuyNum(String limitBuyNum) {
            this.limitBuyNum = limitBuyNum;
        }
        public String getSkuGrouponBuyPrice() {
            return skuGrouponBuyPrice;
        }
        public void setSkuGrouponBuyPrice(String skuGrouponBuyPrice) {
            this.skuGrouponBuyPrice = skuGrouponBuyPrice;
        }
        public String getStartNum() {
            return startNum;
        }
        public void setStartNum(String startNum) {
            this.startNum = startNum;
        }
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
        
    }
    // 排序类
    /**
     * 排序类
     */
    public static class Sort {
        private String sortKey;
        private String sortName;
        private boolean selected;

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public String getSortKey() {
            return sortKey;
        }

        public void setSortKey(String sortKey) {
            this.sortKey = sortKey;
        }

        public String getSortName() {
            return sortName;
        }

        public void setSortName(String sortName) {
            this.sortName = sortName;
        }

    }

    /**
     * 新版团购分类
     */
    public static class Category {
        private String categoryId;
        private String categoryName;
        private ArrayList<Category> sunCategroyList;
        private boolean selected;

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
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

        public ArrayList<Category> getSunCategroyList() {
            return sunCategroyList;
        }

        public void setSunCategroyList(ArrayList<Category> sunCategroyList) {
            this.sunCategroyList = sunCategroyList;
        }

    }

    /**
     * 新版团购分类排序
     */
    public static class CategroyAndSort {
        private ArrayList<Sort> sortList;
        private ArrayList<Category> categroyList;

        public ArrayList<Sort> getSortList() {
            return sortList;
        }

        public void setSortList(ArrayList<Sort> sortList) {
            this.sortList = sortList;
        }

        public ArrayList<Category> getCategroyList() {
            return categroyList;
        }

        public void setCategroyList(ArrayList<Category> categroyList) {
            this.categroyList = categroyList;
        }

    }

    /**
     * 团购-分类-过滤
     */
    public static class CategoryFilter {
        private String categoryId;
        private String categoryName;
        private String imageUrl;
        private String goodsCount;
        private String categoryColor;
        private boolean selected;
        // 无图片版本，是否已经加载过图片
        public boolean isLoadImg;

        public CategoryFilter() {
        }

        public CategoryFilter(String categoryId, String categoryName, String imageUrl, String goodsCount,
                String categoryColor) {
            super();
            this.categoryId = categoryId;
            this.categoryName = categoryName;
            this.imageUrl = imageUrl;
            this.goodsCount = goodsCount;
            this.categoryColor = categoryColor;
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

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getGoodsCount() {
            return goodsCount;
        }

        public void setGoodsCount(String goodsCount) {
            this.goodsCount = goodsCount;
        }

        public String getCategoryColor() {
            return categoryColor;
        }

        public void setCategoryColor(String categoryColor) {
            this.categoryColor = categoryColor;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public boolean isLoadImg() {
            return isLoadImg;
        }

        public void setLoadImg(boolean isLoadImg) {
            this.isLoadImg = isLoadImg;
        }

    }

    /**
     * 城市列表
     */
    public static class CityList {
        private ArrayList<City> hotDivisionList;// 热门城市列表
        private ArrayList<City> divisionList;// 普通城市列表

        public ArrayList<City> getHotDivisionList() {
            return hotDivisionList;
        }

        public void setHotDivisionList(ArrayList<City> hotDivisionList) {
            this.hotDivisionList = hotDivisionList;
        }

        public ArrayList<City> getDivisionList() {
            return divisionList;
        }

        public void setDivisionList(ArrayList<City> divisionList) {
            this.divisionList = divisionList;
        }

    }

    /**
     * 城市信息
     */
    public static class City {
        private String divisionCode;
        private String divisionName;
        private String divisionPinyin;
        private boolean isSlected;

        public City() {
        }

        public City(String divisionCode, String divisionName, String divisionPinyin) {
            super();
            this.divisionCode = divisionCode;
            this.divisionName = divisionName;
            this.divisionPinyin = divisionPinyin;
        }

        public String getDivisionCode() {
            return divisionCode;
        }

        public void setDivisionCode(String divisionCode) {
            this.divisionCode = divisionCode;
        }

        public String getDivisionName() {
            return divisionName;
        }

        public void setDivisionName(String divisionName) {
            this.divisionName = divisionName;
        }

        public String getDivisionPinyin() {
            return divisionPinyin;
        }

        public void setDivisionPinyin(String divisionPinyin) {
            this.divisionPinyin = divisionPinyin;
        }

        public boolean isSlected() {
            return isSlected;
        }

        public void setSlected(boolean isSlected) {
            this.isSlected = isSlected;
        }

    }

    /*
     * // 分类以及排序 public static class FilterSoft {
     * 
     * private ArrayList<FilterCondition> filterConditionList; private ArrayList<SortCon> softConList;
     * 
     * public ArrayList<FilterCondition> getFilterConditionList() { return filterConditionList; }
     * 
     * public void setFilterConditionList(ArrayList<FilterCondition> filterConditionList) { this.filterConditionList =
     * filterConditionList; }
     * 
     * public ArrayList<SortCon> getSoftConList() { return softConList; }
     * 
     * public void setSoftConList(ArrayList<SortCon> softConList) { this.softConList = softConList; }
     * 
     * }
     * 
     * public static class FilterCondition {
     * 
     * public static final int FilterCodeOne = 1; // 一级 public static final int FilterCodeTwo = 2; // 二级 private String
     * catId; private String catName; private int catType; private boolean selected; private ArrayList<FilterCondition>
     * filterConditionList; private ArrayList<SortCon> sortList;
     * 
     * public String getCatId() { return catId; }
     * 
     * public void setCatId(String catId) { this.catId = catId; }
     * 
     * public String getCatName() { return catName; }
     * 
     * public void setCatName(String catName) { this.catName = catName; }
     * 
     * public int getCatType() { return catType; }
     * 
     * public void setCatType(int catType) { this.catType = catType; }
     * 
     * public boolean isSelected() { return selected; }
     * 
     * public void setSelected(boolean selected) { this.selected = selected; }
     * 
     * public ArrayList<FilterCondition> getFilterConditionList() { return filterConditionList; }
     * 
     * public void setFilterConditionList(ArrayList<FilterCondition> filterConditionList) { this.filterConditionList =
     * filterConditionList; }
     * 
     * public ArrayList<SortCon> getSortList() { return sortList; }
     * 
     * public void setSortList(ArrayList<SortCon> sortList) { this.sortList = sortList; }
     * 
     * public FilterCondition getSelectedValue() { for (FilterCondition value : filterConditionList) { if
     * (value.isSelected()) { return value; } } return null; }
     * 
     * // 清除所有属性列表的选中状态 public void clearAllValueSelected() { for (FilterCondition value : filterConditionList) {
     * value.selected = false; } }
     * 
     * public void toggleValueAtPostion(int postion) { int size = filterConditionList.size(); if (postion >= size ||
     * postion < 0) { return; } for (int i = 0; i < size; i++) { if (i == postion) { boolean selected =
     * filterConditionList.get(i).selected; filterConditionList.get(i).selected = !selected; } else {
     * filterConditionList.get(i).selected = false; } } }
     * 
     * public void setValueSelected(int postion, boolean selected) { int size = filterConditionList.size(); if (postion
     * >= size || postion < 0) { return; } for (int i = 0; i < size; i++) { if (i == postion) {
     * filterConditionList.get(i).selected = selected; } else { filterConditionList.get(i).selected = false; } } } }
     * 
     * public static class SortCon {
     * 
     * private String sortKey; private String sort; private boolean selected;
     * 
     * public String getSortKey() { return sortKey; }
     * 
     * public void setSortKey(String sortKey) { this.sortKey = sortKey; }
     * 
     * public String getSort() { return sort; }
     * 
     * public void setSort(String sort) { this.sort = sort; }
     * 
     * public boolean isSelected() { return selected; }
     * 
     * public void setSelected(boolean selected) { this.selected = selected; } }
     */
    /**
     * 解析城市列表
     * 
     * @param response
     * @return
     */
    public static CityList parseCityList(String response) {
        JsonResult result = new JsonResult(response);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject content = result.getJsContent();
        try {
            CityList cityList = new CityList();
            JSONArray hotDivisionArray = content.optJSONArray(JK_HOT_DIVISION_LIST);
            JSONArray divisionArray = content.optJSONArray(JK_DIVISION_LIST);
            if (hotDivisionArray != null) {
                ArrayList<City> hotDivisionList = new ArrayList<City>();
                for (int i = 0, length = hotDivisionArray.length(); i < length; i++) {
                    JSONObject item = hotDivisionArray.getJSONObject(i);
                    City city = new City();
                    city.setDivisionCode(item.optString(JK_DIVISIONCODE));
                    city.setDivisionName(item.optString(JK_DIVISIONNAME));
                    city.setDivisionPinyin(item.optString(JK_DIVISION_PINYIN));
                    hotDivisionList.add(city);
                }
                cityList.setHotDivisionList(hotDivisionList);
            }
            if (divisionArray != null) {
                ArrayList<City> divisionList = new ArrayList<City>();
                for (int i = 0, length = divisionArray.length(); i < length; i++) {
                    JSONObject item = divisionArray.getJSONObject(i);
                    City city = new City();
                    city.setDivisionCode(item.optString(JK_DIVISIONCODE));
                    city.setDivisionName(item.optString(JK_DIVISIONNAME));
                    city.setDivisionPinyin(item.optString(JK_DIVISION_PINYIN));
                    divisionList.add(city);
                }
                cityList.setDivisionList(divisionList);
            }
            return cityList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
/**
 * 解析团购的搜索热词
 * @param response
 * @return
 */
    public static ArrayList<String> parseGroupbuySearchWords(String response) {
        JsonResult result = new JsonResult(response);
        if (!result.isSuccess()) {
            return null;
        }
        try {
            JSONObject content = result.getJsContent();
            JSONArray hotkeyArray = content.optJSONArray(JK_HOTKEY_WORDS);
            if (hotkeyArray != null) {
                ArrayList<String> hotkeyList = new ArrayList<String>();
                for (int i = 0, length = hotkeyArray.length(); i < length; i++) {
                    JSONObject item = hotkeyArray.getJSONObject(i);
                    hotkeyList.add(item.optString("word"));
                }
                return hotkeyList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
