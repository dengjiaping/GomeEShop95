package com.gome.ecmall.shopping;

import com.gome.ecmall.bean.JsonInterface;

public class ShoppingCartDetails implements JsonInterface {

    private static final String JK_TOTAL_COUNT = "totalCount";// 商品总数
    private static final String JK_ORDER_AMOUNT = "orderAmount";// 商品总价
    private static final String JK_PROM_TYPE = "promType";// 促销类型
    private static final String JK_PROM_DESC = "promDesc";// 促销说明
    private static final String JK_SKU_ID = "skuID";// 商品SKU
    private static final String JK_GOODS_NO = "googsNo";// 商品id
    private static final String JK_SKU_NO = "skuNo";
    private static final String JK_SKU_NAME = "skuName";
    private static final String JK_COMMERCE_ID = "commerceItemID";
    private static final String JK_GOODS_TYPE = "googsType";
    private static final String JK_GOODS_COUNT = "goodsCount";
    private static final String JK_ORIGINAL_PRICE = "originalPrice";
    private static final String JK_TOTAL_PRICE = "totalPrice";

    private int googsNums;// 商品总数
    private double googsSum;// 商品总价

    /** 商品实体类 */
    public class Goods {

    }

    /** 套购商品实体类 */
    public class SuiteGoods {
        private String suiteName;
        private int googsNo;
        private String commerceSelected;
        private double suitePrice;
        private int suiteCount;
        private int suiteSkuCount;
        private String skuID;
        private String skuNo;
        private String skuName;
        private String commercelID;

        public String getSuiteName() {
            return suiteName;
        }

        public void setSuiteName(String suiteName) {
            this.suiteName = suiteName;
        }

        public int getGoogsNo() {
            return googsNo;
        }

        public void setGoogsNo(int googsNo) {
            this.googsNo = googsNo;
        }

        public String getCommerceSelected() {
            return commerceSelected;
        }

        public void setCommerceSelected(String commerceSelected) {
            this.commerceSelected = commerceSelected;
        }

        public double getSuitePrice() {
            return suitePrice;
        }

        public void setSuitePrice(double suitePrice) {
            this.suitePrice = suitePrice;
        }

        public int getSuiteCount() {
            return suiteCount;
        }

        public void setSuiteCount(int suiteCount) {
            this.suiteCount = suiteCount;
        }

        public int getSuiteSkuCount() {
            return suiteSkuCount;
        }

        public void setSuiteSkuCount(int suiteSkuCount) {
            this.suiteSkuCount = suiteSkuCount;
        }

        public String getSkuID() {
            return skuID;
        }

        public void setSkuID(String skuID) {
            this.skuID = skuID;
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

        public String getCommercelID() {
            return commercelID;
        }

        public void setCommercelID(String commercelID) {
            this.commercelID = commercelID;
        }

    }

}
