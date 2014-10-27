package com.gome.ecmall.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 订单跟踪信息
 * 
 * @author Administrator
 * 
 */
public class Traces implements Parcelable {
    private String dealTime;
    private String dealValue;
    private String dealType;

    public Traces() {
        super();
    }

    public Traces(String dealTime, String dealValue, String dealType) {
        super();
        this.dealTime = dealTime;
        this.dealValue = dealValue;
        this.dealType = dealType;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }

    public String getDealValue() {
        return dealValue;
    }

    public void setDealValue(String dealValue) {
        this.dealValue = dealValue;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dealType);
        dest.writeString(dealTime);
        dest.writeString(dealValue);
    }

    public static final Parcelable.Creator<Traces> CREATER = new Creator<Traces>() {

        @Override
        public Traces[] newArray(int size) {
            return new Traces[size];
        }

        @Override
        public Traces createFromParcel(Parcel source) {
            return new Traces(source);
        }
    };

    public Traces(Parcel in) {
        dealTime = in.readString();
        dealType = in.readString();
        dealValue = in.readString();
    }

}
