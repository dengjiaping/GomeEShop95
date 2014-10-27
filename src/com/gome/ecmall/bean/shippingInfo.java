package com.gome.ecmall.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gome.ecmall.bean.MoreGomeStore.Store;

/**
 * 配送信息
 */
public class shippingInfo implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = -5259792568286249868L;
    /**
     * 运费
     */
    private String shippingFreight;
    /**
     * 选择的送货时间
     */
    private String shippingTime;
    /**
     * 是否送货前电话确认
     */
    private String isNeedConfirm;
    /**
     * 配送方式
     */
    private String shippingMethod;
    /**
     * 配送方式名称
     */
    private String shippingMethodName;
    /*
     * 8 预计送达时间说明
     */
    private String deliveryTimeDesc;
    /**
     * 可选配送方式
     */
    private List<ShippingMethod> shippingMethodArray;

    private Store gomeStoreInfo;

    public Store getGomeStoreInfo() {
        return gomeStoreInfo;
    }

    public void setGomeStoreInfo(Store gomeStoreInfo) {
        this.gomeStoreInfo = gomeStoreInfo;
    }

    public String getShippingFreight() {
        return shippingFreight;
    }

    public void setShippingFreight(String shippingFreight) {
        this.shippingFreight = shippingFreight;
    }

    public String getShippingTime() {
        return shippingTime;
    }

    public void setShippingTime(String shippingTime) {
        this.shippingTime = shippingTime;
    }

    public String getIsNeedConfirm() {
        return isNeedConfirm;
    }

    public void setIsNeedConfirm(String isNeedConfirm) {
        this.isNeedConfirm = isNeedConfirm;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getShippingMethodName() {
        return shippingMethodName;
    }

    public void setShippingMethodName(String shippingMethodName) {
        this.shippingMethodName = shippingMethodName;
    }

    public List<ShippingMethod> getShippingMethodArray() {
        return shippingMethodArray;
    }

    public void setShippingMethodArray(List<ShippingMethod> shippingMethodArray) {
        this.shippingMethodArray = shippingMethodArray;
    }

    public String getDeliveryTimeDesc() {
        return deliveryTimeDesc;
    }

    public void setDeliveryTimeDesc(String deliveryTimeDesc) {
        this.deliveryTimeDesc = deliveryTimeDesc;
    }

    /**
     * 配送方式
     */
    public static class ShippingMethod implements Serializable {
        /**
		 * 
		 */
        private static final long serialVersionUID = -5747102751937296007L;
        /**
         * 配送方式
         */
        public String shippingMethod;
        /**
         * 配送方式名称
         */
        public String shippingMethodName;
        /**
         * 是否选中
         */
        public boolean isCheck = false;

        /**
         * 配送费用
         */
        public String shippingFreight;
        /**
         * 预计到达时间
         */
        public String deliveryTimeDesc;

        private ArrayList<Store> gomeStoreList;

        /**
         * 国美门店自提点列表
         * 
         * @return
         */
        public ArrayList<Store> getGomeStoreList() {
            return gomeStoreList;

        }

        public void setGomeStoreList(ArrayList<Store> gomeStoreList) {
            this.gomeStoreList = gomeStoreList;
        }

    }
}
