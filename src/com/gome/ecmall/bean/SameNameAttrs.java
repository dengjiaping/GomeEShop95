package com.gome.ecmall.bean;

import java.util.ArrayList;

import com.gome.ecmall.bean.ProductSKU.SkuAttribute;

/**
 * 用来记录相同属性名的SKU属性列表
 * 
 * @author zhenyu.fang
 * @date 2012-7-25
 */
public class SameNameAttrs {

    private String attrName;
    // 总列表
    private ArrayList<SkuAttribute> attrTotalList = new ArrayList<SkuAttribute>();
    // 不重复列表
    private ArrayList<SkuAttribute> attrUnrepeatList = new ArrayList<SkuAttribute>();

    public SameNameAttrs(String attrName) {
        if (attrName == null) {
            throw new NullPointerException("");
        }
        this.attrName = attrName;
    }

    public String getName() {
        return this.attrName;
    }

    /**
     * 添加相同类型的属性,要求属性名相同
     * 
     * @param attribute
     */
    public void add(SkuAttribute attribute) {
        if (attrName.equals(attribute.getName())) {// 判断属性名是否相同
            attrTotalList.add(attribute);
            if (!attrUnrepeatList.contains(attribute)) {
                attrUnrepeatList.add(attribute);
            }
        }
    }

    public int getTotalSize() {
        return attrTotalList.size();
    }

    public int getUnRepeatSize() {
        return attrUnrepeatList.size();
    }

    public SkuAttribute getTotalItem(int position) {
        return attrTotalList.get(position);
    }

    public SkuAttribute getUnRepeatItem(int position) {
        return attrUnrepeatList.get(position);
    }

    public void setItemState(SkuAttribute attribute, int state) {
        for (SkuAttribute item : attrTotalList) {
            if (item.equals(attribute)) {
                item.setState(state);
            }
        }
    }

    public SkuAttribute getCheckedAttribute() {
        for (SkuAttribute item : attrUnrepeatList) {
            if (item.getState() == SkuAttribute.STATE_CHECKED) {
                return item;
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        return attrName.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SameNameAttrs)) {
            return false;
        }
        SameNameAttrs other = (SameNameAttrs) o;
        return attrName.equals(other.getName());
    }

}
