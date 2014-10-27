package com.gome.ecmall.bean;

/**
 * 套购商品
 */
public class SuiteBuyGoods extends Goods {
    private String skuOriginalPrice;
    private String skuSuitePrice;
    private boolean isSelected = true;
    /**
     * 该属性用于标识无图选项套购图片长按加载图片
     */
    private boolean isFlag = true;

    public SuiteBuyGoods() {
        super();
    }

    public String getSkuOriginalPrice() {
        return skuOriginalPrice;
    }

    public void setSkuOriginalPrice(String skuOriginalPrice) {
        this.skuOriginalPrice = skuOriginalPrice;
    }

    public boolean isFlag() {
        return isFlag;
    }

    public void setFlag(boolean isFlag) {
        this.isFlag = isFlag;
    }

    public String getSkuSuitePrice() {
        return skuSuitePrice;
    }

    public void setSkuSuitePrice(String skuSuitePrice) {
        this.skuSuitePrice = skuSuitePrice;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public String toString() {
        return "SuiteBuyGoods [skuOriginalPrice=" + skuOriginalPrice + ", skuSuitePrice=" + skuSuitePrice
                + ", isSelected=" + isSelected + "]";
    }

}
