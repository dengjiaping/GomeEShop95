package com.gome.ecmall.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 热门促销商品
 * 
 * @author Administrator
 * 
 */
public class HotPromGoods extends Goods implements Serializable {
    private String promPrice;// 促销价格
    private String promWords;// sku级促销语
    private String ad;// 广告语
    private String rushBuyItemId;
    private String skuOriginalPrice;
    private String favoriteId ;
    private String goodsShareUrl ;
    private ArrayList<Promotions> promList;

    // @Override
    // public int describeContents() {
    // return 0;
    // }
    //
    // @Override
    // public void writeToParcel(Parcel out, int flags) {
    // out.writeString(skuID);
    // out.writeString(goodsNo);
    // out.writeString(skuNo);
    // out.writeString(skuName);
    // out.writeString(originalPrice);
    // out.writeString(promPrice);
    // out.writeString(promWords);
    // out.writeString(ad);
    // out.writeTypedList(promList);
    // }
    //
    // public static final Parcelable.Creator<HotPromGoods> CREATOR = new Creator<HotPromGoods>() {
    //
    // @Override
    // public HotPromGoods[] newArray(int size) {
    // return new HotPromGoods[size];
    // }
    //
    // @Override
    // public HotPromGoods createFromParcel(Parcel in) {
    // return new HotPromGoods(in);
    // }
    // };
    //
    // private HotPromGoods(Parcel in) {
    // skuID = in.readString();
    // goodsNo = in.readString();
    // skuNo = in.readString();
    // skuName = in.readString();
    // originalPrice = in.readString();
    // promPrice = in.readString();
    // promWords = in.readString();
    // ad = in.readString();
    // // in.readTypedList(promList, Promotions.CREATOR);
    //
    // }

    public HotPromGoods() {
        super();
    }

    public String getPromPrice() {
        return promPrice;
    }

    public void setPromPrice(String promPrice) {
        this.promPrice = promPrice;
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

    public String getRushBuyItemId() {
        return rushBuyItemId;
    }

    public void setRushBuyItemId(String rushBuyItemId) {
        this.rushBuyItemId = rushBuyItemId;
    }

    public String getSkuOriginalPrice() {
        return skuOriginalPrice;
    }

    public void setSkuOriginalPrice(String skuOriginalPrice) {
        this.skuOriginalPrice = skuOriginalPrice;
    }

    public ArrayList<Promotions> getPromList() {
        return promList;
    }

    public void setPromList(ArrayList<Promotions> promList) {
        this.promList = promList;
    }
    
    public String getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(String favoriteId) {
        this.favoriteId = favoriteId;
    }
    
    

    public String getGoodsShareUrl() {
        return goodsShareUrl;
    }

    public void setGoodsShareUrl(String goodsShareUrl) {
        this.goodsShareUrl = goodsShareUrl;
    }

    @Override
    public String toString() {
        return "HotPromGoods [promPrice=" + promPrice + ", promWords=" + promWords + ", ad=" + ad + ", rushBuyItemId="
                + rushBuyItemId + ", promList=" + promList + ", skuID=" + skuID + ", goodsNo=" + goodsNo + ", skuNo="
                + skuNo + ", skuName=" + skuName + ", goodsType=" + goodsType + ", skuThumbImgUrl=" + skuThumbImgUrl
                + ", originalPrice=" + originalPrice + "]";
    }

}
