package com.gome.ecmall.bean;

import java.io.Serializable;

/**
 * 促销
 */
public class Promotions implements Serializable {

    private String promId;

    /**
     * 促销类型
     */
    private String promType;
    /**
     * 促销类型修饰名
     */
    private String promTypeName;
    /**
     * 促销描述
     */
    private String promDesc;
    /**
     * 促销金额
     */
    private String promPrice;
    /**
     * 促销标题（店铺促销中存在）
     */
    private String promTitle;
    /**
     * 是否选择
     */
    private boolean isSelect;

    public Promotions() {
    }

    /*
     * @Override public int describeContents() { return 0; }
     * 
     * @Override public void writeToParcel(Parcel out, int flags) { out.writeString(promId); out.writeString(promDesc);
     * out.writeString(promType); out.writeString(promPrice); out.writeString(promTypeName);
     * 
     * }
     * 
     * public static final Parcelable.Creator<Promotions> CREATOR = new Creator<Promotions>() {
     * 
     * @Override public Promotions[] newArray(int size) { return new Promotions[size]; }
     * 
     * @Override public Promotions createFromParcel(Parcel in) { return new Promotions(in); } };
     * 
     * private Promotions(Parcel in) { promDesc = in.readString(); promId = in.readString(); promPrice =
     * in.readString(); promType = in.readString(); promTypeName = in.readString(); }
     */

    public String getPromId() {
        return promId;
    }

    public void setPromId(String promId) {
        this.promId = promId;
    }

    public String getPromType() {
        return promType;
    }

    public void setPromType(String promType) {
        this.promType = promType;
    }

    public String getPromTypeName() {
        return promTypeName;
    }

    public void setPromTypeName(String promTypeName) {
        this.promTypeName = promTypeName;
    }

    public String getPromDesc() {
        return promDesc;
    }

    public void setPromDesc(String promDesc) {
        this.promDesc = promDesc;
    }

    public String getPromPrice() {
        return promPrice;
    }

    public void setPromPrice(String promPrice) {
        this.promPrice = promPrice;
    }

    public String getPromTitle() {
        return promTitle;
    }

    public void setPromTitle(String promTitle) {
        this.promTitle = promTitle;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

}
