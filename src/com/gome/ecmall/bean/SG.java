package com.gome.ecmall.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 配送单
 * 
 * @author Administrator
 * 
 */
public class SG implements Parcelable {

    private String sgId;
    private String sgStatusId;
    private String sgStatus;

    public SG() {
        super();
    }

    public SG(String sgId, String sgStatusId, String sgStatus) {
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

    public static Parcelable.Creator<SG> CREATER = new Creator<SG>() {

        @Override
        public SG[] newArray(int size) {
            return new SG[size];
        }

        @Override
        public SG createFromParcel(Parcel source) {
            return new SG(source);
        }
    };

    private SG(Parcel in) {
        sgId = in.readString();
        sgStatus = in.readString();
        sgStatusId = in.readString();
    }

}