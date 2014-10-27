package com.gome.ecmall.bean;

import java.util.ArrayList;

import com.gome.ecmall.util.BDebug;

/**
 * 套购
 * 
 * @author Administrator
 * 
 */
public class SuiteBuy {

    private static final String TAG = "SuiteBuy";

    private String selectIndexName;
    private ArrayList<SuiteBuyEntity> suiteList;

    public SuiteBuy() {
        super();
    }

    public SuiteBuy(String selectIndexName, ArrayList<SuiteBuyEntity> suiteList) {
        super();
        this.selectIndexName = selectIndexName;
        this.suiteList = suiteList;
    }

    public String getSelectIndexName() {
        return selectIndexName;
    }

    public void setSelectIndexName(String selectIndexName) {
        this.selectIndexName = selectIndexName;
    }

    public ArrayList<SuiteBuyEntity> getSuiteList() {
        return suiteList;
    }

    public void setSuiteList(ArrayList<SuiteBuyEntity> suiteList) {
        this.suiteList = suiteList;
    }

    @Override
    public String toString() {

        if (suiteList != null) {
            for (int i = 0,size = suiteList.size() ; i < size; i++) {
                BDebug.d(TAG, suiteList.get(i).toString());
            }
        }
        return "SuiteBuy [selectIndexName=" + selectIndexName + ", suiteList=" + suiteList + "]";
    }

}
