package com.gome.ecmall.home.groupbuy;

import java.util.Comparator;

import com.gome.ecmall.bean.GBProductNew.City;

/**
 * 新版团购城市排序规则
 * 
 * @author liuyang-ds
 * 
 */
public class ContactItemComparator implements Comparator<City> {
    @Override
    public int compare(City lhs, City rhs) {
        if (lhs.getDivisionPinyin() == null || rhs.getDivisionPinyin() == null)
            return -1;

        return (lhs.getDivisionPinyin().compareTo(rhs.getDivisionPinyin()));

    }
}