package com.gome.ecmall.bean;

import java.util.ArrayList;

import com.gome.ecmall.bean.Product.ImgUrl;
import com.gome.ecmall.util.CommonUtility;

/**
 * SKU商品
 */
public class ProductSKU {

    private String Id;
    private String skuNo;
    private String skuName;
    private String skuPrice;
    private String skuThumbImgUrl;
    private boolean onSale = false;
    private String favoriteId;
    private boolean isCollectioned;
    private String promWords;
    private String ad;
    private ArrayList<ImgUrl> skuImgUrlList;
    private ArrayList<SkuAttribute> skuAttrsList;
    private ArrayList<Promotion> promList;
    private ArrayList<SkuGift> skuGiftList;

    public ProductSKU() {
        skuImgUrlList = new ArrayList<ImgUrl>();
        skuAttrsList = new ArrayList<ProductSKU.SkuAttribute>();
        skuGiftList = new ArrayList<SkuGift>();
        promList = new ArrayList<Promotion>();
    }

    public void addSkuPromotion(Promotion promotion) {
        promList.add(promotion);
    }

    public void addSkuGift(SkuGift gift) {
        skuGiftList.add(gift);
    }

    public void addSkuImgUrl(ImgUrl imgUrl) {
        skuImgUrlList.add(imgUrl);
    }

    public ArrayList<Promotionable> getSkuPromotions() {
        ArrayList<Promotionable> arrayList = new ArrayList<Promotionable>(promList.size());
        for (Promotion promotion : promList) {
            arrayList.add(promotion);
        }
        return arrayList;
    }

    public void addSkuAttribute(SkuAttribute attr) {
        skuAttrsList.add(attr);
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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

    public String getSkuPrice() {
        return skuPrice;
    }

    public void setSkuPrice(String skuPrice) {
        this.skuPrice = skuPrice;
    }

    public String getSkuThumbImgUrl() {
        return skuThumbImgUrl;
    }

    public void setSkuThumbImgUrl(String skuThumbImgUrl) {
        this.skuThumbImgUrl = skuThumbImgUrl;
    }

    public boolean isOnSale() {
        return onSale;
    }

    public void setOnSale(boolean onSale) {
        this.onSale = onSale;
    }

    public String getPromWords() {
        return promWords;
    }

    public String getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(String favoriteId) {
        this.favoriteId = favoriteId;
    }

    public void setCollectioned(boolean isCollectioned) {
        this.isCollectioned = isCollectioned;
    }

    public boolean isCollectioned() {
        return isCollectioned;
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

    public ArrayList<ImgUrl> getSkuImgUrlList() {
        ArrayList<ImgUrl> list = new ArrayList<ImgUrl>(skuImgUrlList.size());
        for (ImgUrl item : skuImgUrlList) {
            list.add(item);
        }
        return list;
    }

    public ArrayList<SkuAttribute> getSkuAttrsList() {
        ArrayList<SkuAttribute> list = new ArrayList<SkuAttribute>(skuAttrsList.size());
        for (SkuAttribute attribute : skuAttrsList) {
            list.add(attribute);
        }
        return list;
    }

    public String printSkuAttrs() {
        StringBuffer sb = new StringBuffer();
        for (SkuAttribute attr : skuAttrsList) {
            sb.append("name:" + attr.name + " value:" + attr.value + "  checked:" + attr.getCheckDesc() + "  ");
        }
        return sb.toString();
    }

    public boolean isSkuAttrsAllChecked() {
        for (SkuAttribute attr : skuAttrsList) {
            // BDebug.e("====attr.getValue()=======", attr.getValue());
            // BDebug.e("====attr.getState()=======", "" + attr.getState());
            if (attr.getState() != SkuAttribute.STATE_CHECKED) {
                return false;
            }
        }
        return true;
    }

    public void setAllAttrsChecked() {
        for (SkuAttribute attr : skuAttrsList) {
            attr.setState(SkuAttribute.STATE_CHECKED);
        }
    }

    public ArrayList<Promotionable> getSkuGiftList() {
        ArrayList<Promotionable> arrayList = new ArrayList<Promotionable>(skuGiftList.size());
        for (SkuGift gift : skuGiftList) {
            arrayList.add(gift);
        }
        return arrayList;
    }

    /**
     * SKU属性
     */
    public static class SkuAttribute {

        /**
         * 属性状态值
         */
        // 正常状态
        public static final int STATE_NORMAL = 0;
        // 被选中状态
        public static final int STATE_CHECKED = 1;
        // 不可选状态
        public static final int STATE_DISABLE = 2;
        private String name;
        private String value;
        private int state = STATE_NORMAL;
        // 代表该属性属于哪一个ProductSku
        private ProductSKU productSku;

        public SkuAttribute(ProductSKU productSKU) {
            if (productSKU == null) {
                new NullPointerException("nullPointer.......");
            }
            this.productSku = productSKU;
        }

        public ProductSKU getProductSku() {
            return productSku;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getCheckDesc() {
            String desc = "";
            switch (state) {
            case STATE_CHECKED:
                desc = "CHECKED";
                break;
            case STATE_DISABLE:
                desc = "DISABLE";
                break;
            case STATE_NORMAL:
                desc = "NORMAL";
                break;
            }
            return desc;
        }

        @Override
        public int hashCode() {
            return name.hashCode() + value.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof SkuAttribute)) {
                return false;
            }
            SkuAttribute attribute = (SkuAttribute) o;
            return this.name.equals(attribute.name) && this.value.equals(attribute.value);
        }

    }

    /**
     * Sku礼品
     */
    public static class SkuGift implements Promotionable {

        /** 商品sku */
        private String skuID;

        /** 商品id */
        private String goodsNo;

        /** 商品skuNo */
        private String skuNo;

        /** 商品名称 */
        private String skuName;

        private String goodsCount;

        private String originalPrice;

        private String totalPrice;

        private int giftLevel;

        public String getSkuID() {
            return skuID;
        }

        public void setSkuID(String skuID) {
            this.skuID = skuID;
        }

        public int getGiftLevel() {
            return giftLevel;
        }

        public void setGiftLevel(int giftLevel) {
            this.giftLevel = giftLevel;
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

        public String getGoodsCount() {
            return goodsCount;
        }

        public void setGoodsCount(String goodsCount) {
            this.goodsCount = goodsCount;
        }

        public String getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(String originalPrice) {
            this.originalPrice = originalPrice;
        }

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }

        /**
         * 获取促销描述
         */
        @Override
        public String getPromotionDesc() {
            StringBuffer desc = new StringBuffer();
            if (skuName != null) {
                desc.append(skuName);
            }
            if (goodsCount != null && goodsCount.length() > 0) {
                desc.append(" x").append(goodsCount);
            }
            return desc.toString();
        }

        /**
         * 获取促销类型
         */
        @Override
        public int getPromotionType() {
            return Integer.parseInt(CommonUtility.GIFT);
        }

        @Override
        public int getPromotionLevel() {
            return getGiftLevel();
        }
    }

}
