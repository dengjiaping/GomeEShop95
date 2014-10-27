package com.gome.ecmall.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 配送单
 * 
 * @author Administrator
 * 
 */
public class SGList implements Parcelable {

    private String sgId;
    private String sgStatusId;
    private String sgStatus;

    public SGList() {
        super();
    }

    public SGList(String sgId, String sgStatusId, String sgStatus) {
        super();
        this.sgId = sgId;
        this.sgStatusId = sgStatusId;
        this.sgStatus = sgStatus;
    }

    public String getSgId() {
        return sgId;
    }

    public void setSgId(String sgId) {
        this.sgId = sgId;
    }

    public String getSgStatusId() {
        return sgStatusId;
    }

    public void setSgStatusId(String sgStatusId) {
        this.sgStatusId = sgStatusId;
    }

    public String getSgStatus() {
        return sgStatus;
    }

    public void setSgStatus(String sgStatus) {
        this.sgStatus = sgStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sgId);
        dest.writeString(sgStatus);
        dest.writeString(sgStatusId);
    }

    public static Parcelable.Creator<SGList> CREATER = new Creator<SGList>() {

        @Override
        public SGList[] newArray(int size) {
            return new SGList[size];
        }

        @Override
        public SGList createFromParcel(Parcel source) {
            return new SGList(source);
        }
    };

    private SGList(Parcel in) {
        sgId = in.readString();
        sgStatus = in.readString();
        sgStatusId = in.readString();
    }

}