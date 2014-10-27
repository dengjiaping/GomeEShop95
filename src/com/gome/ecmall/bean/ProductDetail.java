package com.gome.ecmall.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;

import com.gome.ecmall.bean.Product.BBCShopInfo;
import com.gome.ecmall.bean.Product.ImgUrl;
import com.gome.ecmall.bean.ProductSKU.SkuAttribute;
import com.gome.ecmall.bean.ProductSKU.SkuGift;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.UrlMatcher;

/**
 * 商品详情【复合类】
 */
public class ProductDetail implements JsonInterface {

    public static final String JK_PREPRICE = "prePrice";
    public static final String JK_AUTHOR = "author";
    public static final String JK_COMPILE = "compile";
    public static final String JK_PUBLISHERS = "publishers";
    public static final String JK_ISBN = "ISBN";
    public static final String JK_PACK = "pack";
    public static final String JK_FORMAT = "format";
    public static final String JK_PUBLICATIONTIME = "publicationTime";
    public static final String JK_PRINTINGTIME = "PrintingTime";
    public static final String JK_EDITION = "edition";
    public static final String JK_IMPRESSION = "impression";
    public static final String JK_PAGENUM = "pageNum";
    public static final String JK_GOODS_SHARE_URL = "goodsShareUrl";

    // 商品类型(默认:1;图书:101)
    public static final int GOODS_TYPE_DEFAULT = 1;
    public static final int GOODS_TYPE_BOOK = 101;
    private String goodsName;
    private String promWords;
    private String ad;
    private String displayPrice;
    private String lowestPrice;
    private String highestPrice;
    private String appraiseNum;
    private String consultationNum;
    private String goodsNo;

    private int goodsType = GOODS_TYPE_DEFAULT;
    private ArrayList<ImgUrl> imgUrlList;
    private ArrayList<Promotion> promList;
    private ArrayList<SkuGift> giftList;
    private ArrayList<ProductSKU> skuList;
    private boolean onSale = false;
    // 图书信息
    private String prePrice;
    private String author;
    private String compile;
    private String publishers;
    private String ISBN;
    private String pack;
    private String format;
    private String publicationTime;
    private String PrintingTime;
    private String edition;
    private String impression;
    private String pageNum;

    // 是否BBC商品
    private String isBBCshop;
    private BBCShopInfo bbcShopInfo;

    // 分享路径
    private String goodsShareUrl;

    public String getGoodsShareUrl() {
        return goodsShareUrl;
    }

    public void setGoodsShareUrl(String goodsShareUrl) {
        this.goodsShareUrl = goodsShareUrl;
    }

    /**
     * 创建请求产品详情的Json
     * 
     * @param goodsNo
     *            产品ID
     * @param skuId
     * @return
     */
    public static String createRequestProductDetailJson(String goodsNo, String skuId) {
        JSONObject object = new JSONObject();
        try {
            object.put(JK_GOODS_NO, goodsNo);
            if (skuId != null) {
                object.put(JK_SKU_ID, skuId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    /**
     * 创建添加商品收藏的json
     * 
     * @param goodsNo
     * @param skuId
     * @return
     */
    public static String createReuestProductAddCollectionJson(String goodsNo, String skuId) {
        JSONObject json = new JSONObject();
        try {
            json.put(JK_GOODS_NO, goodsNo);
            json.put(JK_SKU_ID, skuId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    /**
     * 创建删除商品收藏的ID
     * 
     * @param goodsNo
     * @param skuId
     * @param favoriteId
     *            收藏ID
     * @return
     */
    public static String createRequestProductDelCollectionJson(String goodsNo, String skuId, String favoriteId) {
        JSONObject json = new JSONObject();
        try {
            JSONArray array = new JSONArray();
            JSONObject item = new JSONObject();
            if (favoriteId != null && favoriteId.length() > 0) {
                item.put(JK_ID, favoriteId);
            } else {
                item.put(JK_GOODS_NO, goodsNo);
                item.put(JK_SKU_ID, skuId);
            }
            array.put(item);
            json.put(JK_DEL_COLLECTION_LIST, array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    /**
     * 创建添加购物车的json
     * 
     * @param skuId
     * @param goodsNo
     * @param number
     *            商品数量
     * @param goodsType
     *            商品类型(商品:"Y";赠品:"N")
     * @return
     */
    public static final String createAddShoppingCartJson(String skuId, String goodsNo, int number, String goodsType,
            String districtCode) {
        JSONObject json = new JSONObject();
        try {
            json.put(JK_SKU_ID, skuId);
            json.put(JK_GOODS_NO, goodsNo);
            json.put(JK_NUMBER, number);
            json.put(JK_GOODS_TYPE, goodsType);
            if (!TextUtils.isEmpty(districtCode)) {
                json.put(JK_DISTRICT_CODE, districtCode);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public static ShopCartAddedResult parseAddShopCartResult(String msg) {
        if (msg == null || msg.length() == 0) {
            return null;
        }
        ShopCartAddedResult addedResult = null;
        try {
            JSONObject json = new JSONObject(msg);
            String result = json.getString(JsonInterface.JK_SUCCESS);
            addedResult = new ShopCartAddedResult();
            if (result.equals(JsonInterface.JV_YES)) {
                addedResult.setSuccess(true);
            } else {
                addedResult.setSuccess(false);
            }
            String submit = json.getString(JK_IS_SUBMIT);
            if (JV_YES.equals(submit)) {
                addedResult.setSubmit(true);
                String totalCount = json.getString(JK_TOTAL_COUNT);
                addedResult.setTotalCount(Integer.parseInt(totalCount));
            } else {
                addedResult.setSubmit(false);
                addedResult.setErrorMsg(json.getString(JK_FAIL_REASON));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return addedResult;
    }

    public static CharSequence combineDisplayName(Context context, String name, String ad, String promWords,
            String isBBc) {
        boolean hasPromWords = (promWords != null && promWords.length() > 0);
        return Html.fromHtml(("Y".equals(isBBc) ? CommonUtility.getColorText("", Product.PROM_COLOR) : "")
                + CommonUtility.ToDBC(name + CommonUtility.getColorText(ad, Product.AD_COLOR))
                + (hasPromWords ? "<br>" : "") + CommonUtility.getColorText(promWords, Product.PROM_COLOR));
    }

    /**
     * 解析商品详情json
     * 
     * @param json
     * @return
     */
    public static ProductDetail parseProductDetail(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        ProductDetail detail = new ProductDetail();
        try {
            JSONObject content = result.getJsContent();
            String goodsName = content.optString(JK_GOODS_NAME);
            String goodsNo = content.optString(JK_GOODS_NO);
            // String goodsShareUrl = content.getString(JK_GOODS_SHARE_URL) ;
            String promWords = content.optString(JK_PROMOTION_WORDS);
            String ad = content.optString(JK_AD);
            detail.setGoodsName(goodsName);
            detail.setGoodsNo(goodsNo);
            detail.setPromWords(promWords);
            detail.setAd(ad);
            // detail.setGoodsShareUrl(goodsShareUrl) ;
            String lowestPrice = content.getString(JK_LOWEST_SALE_PRICE);
            String highestPrice = content.getString(JK_HIGHEST_SALE_PRICE);
            StringBuffer price = new StringBuffer(Product.YUAN);
            price.append(lowestPrice);
            if (!highestPrice.equals(lowestPrice)) {
                price.append(" - ").append(Product.YUAN).append(highestPrice);
            }
            detail.setLowestPrice(lowestPrice);
            detail.setHighestPrice(highestPrice);
            detail.setDisplayPrice(price.toString());
            detail.setAppraiseNum(content.optString(JK_APPRAISE_NUM));
            detail.setConsultationNum(content.optString(JK_CONSULATION_NUM));
            String onSale = content.optString(JK_ON_SALE);
            if (onSale != null && onSale.equals(JV_YES)) {
                detail.setOnSale(true);
            } else {
                detail.setOnSale(false);
            }
            detail.setGoodsType(content.getInt(JK_GOODS_TYPE));
            // 是否BBC商品
            detail.setIsBBCshop(content.optString(JK_ISBBC));
            JSONObject bbcObj = content.optJSONObject(JK_BBCSHOPINFO);
            if (bbcObj != null) {
                BBCShopInfo bbcShopInfo = new BBCShopInfo();
                bbcShopInfo.setBbcShopId(bbcObj.optString(JK_BBCSHOPID));
                bbcShopInfo.setBbcShopName(bbcObj.optString(JK_BBCSHOPNAME));
                bbcShopInfo.setBbcShopImgURL(bbcObj.optString(JK_BBCSHOPIMGURL));
                detail.setBbcShopInfo(bbcShopInfo);
            }
            // Product级别的图片列表
            JSONArray imgUrlArray = content.optJSONArray(JK_GOODS_IMG_URL_LIST);
            if (imgUrlArray != null) {
                for (int i = 0, length = imgUrlArray.length(); i < length; i++) {
                    JSONObject item = imgUrlArray.getJSONObject(i);
                    ImgUrl imgUrl = new ImgUrl();
                    String thumbUrl = item.optString(JK_THUMB_IMG_URL);
                    String sourceUrl = item.optString(JK_SCOURCE_IMG_URL);
                    imgUrl.setThumbUrl(UrlMatcher.getFitGalleryThumbUrl(thumbUrl));
                    imgUrl.setSourceUrl(UrlMatcher.getFitGallerySourceUrl(sourceUrl));
                    detail.addImgUrl(imgUrl);
                }
            }
            // 解析产品级别的促销信息
            JSONArray productPromArray = content.optJSONArray(JK_PROMOTION_LIST);
            if (productPromArray != null) {
                for (int i = 0, length = productPromArray.length(); i < length; i++) {
                    JSONObject promItem = productPromArray.getJSONObject(i);
                    Promotion productProm = new Promotion();
                    // Product级别的促销信息
                    productProm.setLevel(Promotion.LEVEL_PRODUCT);
                    productProm.setType(Integer.parseInt(promItem.getString(JK_PROMOTION_TYPE)));
                    productProm.setDesc(promItem.getString(JK_PROMOTION_DESC));
                    detail.addProductPromotion(productProm);
                }
            }
            // 解析产品级别的赠品信息
            JSONArray productGiftArray = content.optJSONArray(JK_GIFT_LIST);
            if (productGiftArray != null) {
                for (int i = 0, length = productGiftArray.length(); i < length; i++) {
                    JSONObject giftItem = productGiftArray.getJSONObject(i);
                    SkuGift skuGift = new SkuGift();
                    // Product级别的赠品信息
                    skuGift.setGiftLevel(Promotion.LEVEL_PRODUCT);
                    skuGift.setSkuID(giftItem.getString(JK_SKU_ID));
                    skuGift.setGoodsNo(giftItem.getString(JK_GOODS_NO));
                    skuGift.setSkuNo(giftItem.getString(JK_SKU_NO));
                    skuGift.setSkuName(giftItem.getString(JK_SKU_NAME));
                    skuGift.setGoodsCount(giftItem.optString(JK_GOODS_COUNT));
                    skuGift.setOriginalPrice(giftItem.optString(JK_ORIGINAL_PRICE));
                    skuGift.setTotalPrice(giftItem.optString(JK_TOTAL_PRICE));
                    detail.addProductGift(skuGift);
                }
            }
            // 解析Product的SKU列表
            JSONArray skuArray = content.optJSONArray(JK_SKU_LIST);
            if (skuArray != null) {
                for (int i = 0, size = skuArray.length(); i < size; i++) {
                    ProductSKU productSKU = new ProductSKU();
                    JSONObject item = skuArray.getJSONObject(i);
                    productSKU.setId(item.optString(JK_SKU_ID));
                    productSKU.setSkuNo(item.optString(JK_SKU_NO));
                    String skuName = item.optString(JK_SKU_NAME);
                    productSKU.setSkuName(skuName);
                    productSKU.setSkuPrice(Product.YUAN + item.optString(JK_SKU_PRICE));
                    // productSKU.setSkuThumbImgUrl(item.optString(JK_SKU_THUMB_IMG_URL));
                    String skuOnSale = item.optString(JK_ON_SALE);
                    if (skuOnSale != null && skuOnSale.equals(JV_YES)) {
                        productSKU.setOnSale(true);
                    } else {
                        productSKU.setOnSale(false);
                    }
                    String favoriteId = item.optString(JK_FAVORITE_ID);
                    productSKU.setFavoriteId(favoriteId);
                    if (favoriteId != null && favoriteId.length() > 0) {
                        productSKU.setCollectioned(true);// 已收藏
                    } else {
                        productSKU.setCollectioned(false);// 未收藏
                    }
                    productSKU.setPromWords(item.optString(JK_PROMOTION_WORDS));
                    productSKU.setAd(item.optString(JK_AD));
                    // 解析SKU级别的产品图片链接集合
                    JSONArray srcImgUrlArray = item.getJSONArray(JK_SKU_SOURCE_IMG_URL);
                    if (srcImgUrlArray != null) {
                        for (int m = 0, count = srcImgUrlArray.length(); m < count; m++) {
                            String srcImgUrl = srcImgUrlArray.getString(m);
                            ImgUrl imgUrl = new ImgUrl();
                            imgUrl.setThumbUrl(UrlMatcher.getFitGalleryThumbUrl(srcImgUrl));
                            imgUrl.setSourceUrl(UrlMatcher.getFitGallerySourceUrl(srcImgUrl));
                            productSKU.addSkuImgUrl(imgUrl);
                        }
                    }// end parse imgUrls
                     // 解析SKU级别的产品属性
                    JSONArray attrsArray = item.getJSONArray(JK_ATTRIBUTES);
                    if (attrsArray != null) {
                        for (int m = 0, count = attrsArray.length(); m < count; m++) {
                            SkuAttribute attribute = new SkuAttribute(productSKU);
                            JSONObject attJson = attrsArray.getJSONObject(m);
                            attribute.setName(attJson.optString(JK_NAME));
                            attribute.setValue(attJson.optString(JK_VALUE));
                            productSKU.addSkuAttribute(attribute);
                        }
                    }// end parse attrs
                     // 解析SKU级别的促销信息
                    JSONArray promArray = item.getJSONArray(JK_PROMOTION_LIST);
                    for (int m = 0, count = promArray.length(); m < count; m++) {
                        Promotion promotion = new Promotion();
                        // Product Sku级别的促销信息
                        promotion.setLevel(Promotion.LEVEL_SKU);
                        JSONObject promItem = promArray.getJSONObject(m);
                        promotion.setType(promItem.getInt(JK_PROMOTION_TYPE));
                        promotion.setDesc(promItem.getString(JK_PROMOTION_DESC));
                        productSKU.addSkuPromotion(promotion);
                    }
                    // 解析SKU级别的赠品信息
                    JSONArray giftArray = item.optJSONArray(JK_GIFT_LIST);
                    if (giftArray != null) {
                        for (int m = 0, count = productGiftArray.length(); m < count; m++) {
                            JSONObject giftItem = productGiftArray.getJSONObject(m);
                            SkuGift gift = new SkuGift();
                            // SKU级别的赠品信息
                            gift.setGiftLevel(Promotion.LEVEL_SKU);
                            gift.setSkuID(giftItem.getString(JK_SKU_ID));
                            gift.setGoodsNo(giftItem.getString(JK_GOODS_NO));
                            gift.setSkuNo(giftItem.getString(JK_SKU_NO));
                            gift.setSkuName(giftItem.getString(JK_SKU_NAME));
                            gift.setGoodsCount(giftItem.optString(JK_GOODS_COUNT));
                            gift.setOriginalPrice(giftItem.optString(JK_ORIGINAL_PRICE));
                            gift.setTotalPrice(giftItem.optString(JK_TOTAL_PRICE));
                            // 加入SKU级别的Product中
                            productSKU.addSkuGift(gift);
                        }
                    }
                    detail.addProductSku(productSKU);
                }
            }// end skuList
             // 图书信息
            detail.setPrePrice(content.optString(JK_PREPRICE));
            detail.setAuthor(content.optString(JK_AUTHOR));
            detail.setCompile(content.optString(JK_COMPILE));
            detail.setPublishers(content.optString(JK_PUBLISHERS));
            detail.setISBN(content.optString(JK_ISBN));
            detail.setPack(content.optString(JK_PACK));
            detail.setFormat(content.optString(JK_FORMAT));
            detail.setPublicationTime(content.optString(JK_PUBLICATIONTIME));
            detail.setPrintingTime(content.optString(JK_PRINTINGTIME));
            detail.setEdition(content.optString(JK_EDITION));
            detail.setImpression(content.optString(JK_IMPRESSION));
            detail.setPageNum(content.optString(JK_PAGENUM));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return detail;
    }

    public ProductDetail() {
        imgUrlList = new ArrayList<Product.ImgUrl>();
        promList = new ArrayList<Promotion>();
        giftList = new ArrayList<ProductSKU.SkuGift>();
        skuList = new ArrayList<ProductSKU>();
    }

    public void addProductPromotion(Promotion promotion) {
        promList.add(promotion);
    }

    public void addProductGift(SkuGift gift) {
        giftList.add(gift);
    }

    public void addImgUrl(ImgUrl imgUrl) {
        imgUrlList.add(imgUrl);
    }

    public void addProductSku(ProductSKU sku) {
        skuList.add(sku);
    }

    public CharSequence getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getPromWords() {
        return promWords;
    }

    public void setPromWords(String promWords) {
        this.promWords = promWords;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
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

    public String getAppraiseNum() {
        return appraiseNum;
    }

    public String getConsultationNum() {
        return consultationNum;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setAppraiseNum(String appraiseNum) {
        this.appraiseNum = appraiseNum;
    }

    public void setConsultationNum(String consultationNum) {
        this.consultationNum = consultationNum;
    }

    public String getDisplayPrice() {
        return displayPrice;
    }

    public void setDisplayPrice(String displayPrice) {
        this.displayPrice = displayPrice;
    }

    public ArrayList<ImgUrl> getImgUrlList() {
        ArrayList<ImgUrl> arrayList = new ArrayList<ImgUrl>(imgUrlList.size());
        for (ImgUrl imgUrl : imgUrlList) {
            arrayList.add(imgUrl);
        }
        return arrayList;
    }

    public String getPrePrice() {
        return prePrice;
    }

    public void setPrePrice(String prePrice) {
        this.prePrice = prePrice;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCompile() {
        return compile;
    }

    public void setCompile(String compile) {
        this.compile = compile;
    }

    public String getPublishers() {
        return publishers;
    }

    public void setPublishers(String publishers) {
        this.publishers = publishers;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String iSBN) {
        ISBN = iSBN;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getPublicationTime() {
        return publicationTime;
    }

    public void setPublicationTime(String publicationTime) {
        this.publicationTime = publicationTime;
    }

    public String getPrintingTime() {
        return PrintingTime;
    }

    public void setPrintingTime(String printingTime) {
        PrintingTime = printingTime;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getImpression() {
        return impression;
    }

    public void setImpression(String impression) {
        this.impression = impression;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    /**
     * 获得SKU级别的产品列表
     * 
     * @return
     */
    public ArrayList<ProductSKU> getSkuList() {
        ArrayList<ProductSKU> arrayList = new ArrayList<ProductSKU>(skuList.size());
        for (ProductSKU sku : skuList) {
            arrayList.add(sku);
        }
        return arrayList;
    }

    /**
     * 返回Product级别的促销列表
     * 
     * @return
     */
    public ArrayList<Promotionable> getProductPromotionList() {
        ArrayList<Promotionable> arrayList = new ArrayList<Promotionable>();
        for (Promotion promotion : promList) {
            arrayList.add(promotion);
        }
        return arrayList;
    }

    /**
     * 返回Product级别的赠品列表
     * 
     * @return
     */
    public ArrayList<Promotionable> getProductGiftList() {
        ArrayList<Promotionable> arrayList = new ArrayList<Promotionable>(giftList.size());
        for (SkuGift gift : giftList) {
            arrayList.add(gift);
        }
        return arrayList;
    }

    public boolean isOnSale() {
        return onSale;
    }

    public void setOnSale(boolean onSale) {
        this.onSale = onSale;
    }

    /**
     * 购物车-添加-返回结果
     */
    public static class ShopCartAddedResult {
        private boolean isSuccess;
        private boolean isSubmit;
        private int totalCount;
        private String errorMsg;

        public ShopCartAddedResult() {

        }

        public boolean isSubmit() {
            return isSubmit;
        }

        public void setSubmit(boolean isSubmit) {
            this.isSubmit = isSubmit;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean isSuccess) {
            this.isSuccess = isSuccess;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

    }

    public String getIsBBCshop() {
        return isBBCshop;
    }

    public void setIsBBCshop(String isBBCshop) {
        this.isBBCshop = isBBCshop;
    }

    public BBCShopInfo getBbcShopInfo() {
        return bbcShopInfo;
    }

    public void setBbcShopInfo(BBCShopInfo bbcShopInfo) {
        this.bbcShopInfo = bbcShopInfo;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

}
