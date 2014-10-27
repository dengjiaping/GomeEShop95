package com.gome.ecmall.bean;

/**
 * 套购筛选
 * 
 * @author Administrator
 * 
 */
public class SuiteBuyFilter {

    private String selectIndex;
    private String selectIndexName;
    private boolean isSelected;

    public SuiteBuyFilter() {
    }

    public SuiteBuyFilter(String selectIndex, String selectIndexName) {
        super();
        this.selectIndex = selectIndex;
        this.selectIndexName = selectIndexName;
    }

    public String getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(String selectIndex) {
        this.selectIndex = selectIndex;
    }

    public String getSelectIndexName() {
        return selectIndexName;
    }

    public void setSelectIndexName(String selectIndexName) {
        this.selectIndexName = selectIndexName;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public String toString() {
        return "SuiteBuyFilter [selectIndex=" + selectIndex + ", selectIndexName=" + selectIndexName + "]";
    }

}
