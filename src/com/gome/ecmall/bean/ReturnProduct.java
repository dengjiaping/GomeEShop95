package com.gome.ecmall.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.gome.ecmall.bean.OrderList.Order;

/**
 * 退换货业务bean
 * 
 * @author qiudongchao
 * 
 */
public class ReturnProduct {
    /**
     * 退换货订单
     * 
     * @author qiudongchao
     * 
     */
    public static class ReturnOrder extends Order {
        private ArrayList<ShipInfo> shippingList;

        public ArrayList<ShipInfo> getShippingList() {
            return shippingList;
        }

        public void setShippingList(ArrayList<ShipInfo> shippingList) {
            this.shippingList = shippingList;
        }
    }

    /**
     * 配送信息
     * 
     * @author qiudongchao
     * 
     */
    public static class ShipInfo {
        private String shippingID;
        private String price;
        private String showApplyButton;
        private ArrayList<ReturnGoods> goodsList;

        public String getShippingID() {
            return shippingID;
        }

        public void setShippingID(String shippingID) {
            this.shippingID = shippingID;
        }

        public ArrayList<ReturnGoods> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(ArrayList<ReturnGoods> goodsList) {
            this.goodsList = goodsList;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getShowApplyButton() {
            return showApplyButton;
        }

        public void setShowApplyButton(String showApplyButton) {
            this.showApplyButton = showApplyButton;
        }
    }

    /**
     * 退换货商品
     * 
     * @author qiudongchao
     */
    public static class ReturnGoods extends Goods {
        private String returnPrice;
        private String showApplyButton;
        private String desc;
        private String index;

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getReturnPrice() {
            return returnPrice;
        }

        public void setReturnPrice(String returnPrice) {
            this.returnPrice = returnPrice;
        }

        public String getShowApplyButton() {
            return showApplyButton;
        }

        public void setShowApplyButton(String showApplyButton) {
            this.showApplyButton = showApplyButton;
        }
    }

    /**
     * 退换货记录
     * 
     * @author qiudongchao
     * 
     */
    public static class ReturnRecord {
        private String orderID;
        private String returnNO;
        private String returnStatus;
        private String returnType;
        private String returnApplayTime;
        private ArrayList<ReturnGoods> goodsList;

        public String getOrderID() {
            return orderID;
        }

        public void setOrderID(String orderID) {
            this.orderID = orderID;
        }

        public String getReturnNO() {
            return returnNO;
        }

        public void setReturnNO(String returnNO) {
            this.returnNO = returnNO;
        }

        public String getReturnStatus() {
            return returnStatus;
        }

        public void setReturnStatus(String returnStatus) {
            this.returnStatus = returnStatus;
        }

        public String getReturnType() {
            return returnType;
        }

        public void setReturnType(String returnType) {
            this.returnType = returnType;
        }

        public String getReturnApplayTime() {
            return returnApplayTime;
        }

        public void setReturnApplayTime(String returnApplayTime) {
            this.returnApplayTime = returnApplayTime;
        }

        public ArrayList<ReturnGoods> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(ArrayList<ReturnGoods> goodsList) {
            this.goodsList = goodsList;
        }
    }

    /**
     * 进度查询
     * 
     * @author qiudongchao
     * 
     */
    public static class ReturnRate extends ReturnRecord {
        private String skuID;
        private String goodsNo;
        private String skuName;
        private String skuThumbImgUrl;
        private ArrayList<Deal> dealList;

        public String getSkuID() {
            return skuID;
        }

        public void setSkuID(String skuID) {
            this.skuID = skuID;
        }

        public String getGoodsNo() {
            return goodsNo;
        }

        public void setGoodsNo(String goodsNo) {
            this.goodsNo = goodsNo;
        }

        public String getSkuName() {
            return skuName;
        }

        public void setSkuName(String skuName) {
            this.skuName = skuName;
        }

        public String getSkuThumbImgUrl() {
            return skuThumbImgUrl;
        }

        public void setSkuThumbImgUrl(String skuThumbImgUrl) {
            this.skuThumbImgUrl = skuThumbImgUrl;
        }

        public ArrayList<Deal> getDealList() {
            return dealList;
        }

        public void setDealList(ArrayList<Deal> dealList) {
            this.dealList = dealList;
        }

    }

    /**
     * 流程
     * 
     * @author qiudongchao
     * 
     */
    public static class Deal {
        private String dealTime;
        private String dealDesc;
        private String dealUser;

        public String getDealTime() {
            return dealTime;
        }

        public void setDealTime(String dealTime) {
            this.dealTime = dealTime;
        }

        public String getDealDesc() {
            return dealDesc;
        }

        public void setDealDesc(String dealDesc) {
            this.dealDesc = dealDesc;
        }

        public String getDealUser() {
            return dealUser;
        }

        public void setDealUser(String dealUser) {
            this.dealUser = dealUser;
        }

    }

    /**
     * 退换货
     * 
     * @author qiudongchao
     * 
     */
    public static class ReturnEntity {
        private String orderID;
        private String returnType;
        private String skuID;
        private String shippingID;
        private String returnDesc;
        private String isBBC;

        private String serviceProvinceName;
        private String serviceCityName;
        private String serviceProvinceCode;
        private String serviceCityCode;
        private String serviceCountyName;
        private String serviceCountyCode;

        private String isPingByGome;
        private String isReturnMethodStore;
        private String isReturnMethodCustome;
        private String isNeedInvoiceNO;
        private String isNeedReturnReason;

        private ArrayList<ReturnReason> returnReason;
        private ReturnAddress address;
        private ArrayList<ReturnGoods> goodsList;

        public String getServiceCountyName() {
            return serviceCountyName;
        }

        public void setServiceCountyName(String serviceCountyName) {
            this.serviceCountyName = serviceCountyName;
        }

        public String getServiceCountyCode() {
            return serviceCountyCode;
        }

        public void setServiceCountyCode(String serviceCountyCode) {
            this.serviceCountyCode = serviceCountyCode;
        }

        public String getReturnDesc() {
            return returnDesc;
        }

        public void setReturnDesc(String returnDesc) {
            this.returnDesc = returnDesc;
        }

        public String getIsBBC() {
            return isBBC;
        }

        public void setIsBBC(String isBBC) {
            this.isBBC = isBBC;
        }

        public String getIsNeedInvoiceNO() {
            return isNeedInvoiceNO;
        }

        public void setIsNeedInvoiceNO(String isNeedInvoiceNO) {
            this.isNeedInvoiceNO = isNeedInvoiceNO;
        }

        public String getIsNeedReturnReason() {
            return isNeedReturnReason;
        }

        public void setIsNeedReturnReason(String isNeedReturnReason) {
            this.isNeedReturnReason = isNeedReturnReason;
        }

        public String getSkuID() {
            return skuID;
        }

        public void setSkuID(String skuID) {
            this.skuID = skuID;
        }

        public String getShippingID() {
            return shippingID;
        }

        public void setShippingID(String shippingID) {
            this.shippingID = shippingID;
        }

        public String getOrderID() {
            return orderID;
        }

        public void setOrderID(String orderID) {
            this.orderID = orderID;
        }

        public String getReturnType() {
            return returnType;
        }

        public void setReturnType(String returnType) {
            this.returnType = returnType;
        }

        public String getServiceProvinceName() {
            return serviceProvinceName;
        }

        public void setServiceProvinceName(String serviceProvinceName) {
            this.serviceProvinceName = serviceProvinceName;
        }

        public String getServiceCityName() {
            return serviceCityName;
        }

        public void setServiceCityName(String serviceCityName) {
            this.serviceCityName = serviceCityName;
        }

        public String getServiceProvinceCode() {
            return serviceProvinceCode;
        }

        public void setServiceProvinceCode(String serviceProvinceCode) {
            this.serviceProvinceCode = serviceProvinceCode;
        }

        public String getServiceCityCode() {
            return serviceCityCode;
        }

        public void setServiceCityCode(String serviceCityCode) {
            this.serviceCityCode = serviceCityCode;
        }

        public String getIsPingByGome() {
            return isPingByGome;
        }

        public void setIsPingByGome(String isPingByGome) {
            this.isPingByGome = isPingByGome;
        }

        public String getIsReturnMethodStore() {
            return isReturnMethodStore;
        }

        public void setIsReturnMethodStore(String isReturnMethodStore) {
            this.isReturnMethodStore = isReturnMethodStore;
        }

        public String getIsReturnMethodCustome() {
            return isReturnMethodCustome;
        }

        public void setIsReturnMethodCustome(String isReturnMethodCustome) {
            this.isReturnMethodCustome = isReturnMethodCustome;
        }

        public ArrayList<ReturnReason> getReturnReason() {
            return returnReason;
        }

        public void setReturnReason(ArrayList<ReturnReason> returnReason) {
            this.returnReason = returnReason;
        }

        public ReturnAddress getAddress() {
            return address;
        }

        public void setAddress(ReturnAddress address) {
            this.address = address;
        }

        public ArrayList<ReturnGoods> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(ArrayList<ReturnGoods> goodsList) {
            this.goodsList = goodsList;
        }

    }

    /**
     * 退换货地址
     * 
     * @author qiudongchao
     * 
     */
    public static class ReturnAddress implements Serializable {
        private String user;
        private String phoneNumber;
        private String addressDetail;
        private String zipCode;
        private String serviceProvinceName;
        private String serviceCityName;
        private String serviceProvinceCode;
        private String serviceCityCode;
        private String serviceCountyName;
        private String serviceCountyCode;

        public String getServiceCountyName() {
            return serviceCountyName;
        }

        public void setServiceCountyName(String serviceCountyName) {
            this.serviceCountyName = serviceCountyName;
        }

        public String getServiceCountyCode() {
            return serviceCountyCode;
        }

        public void setServiceCountyCode(String serviceCountyCode) {
            this.serviceCountyCode = serviceCountyCode;
        }

        public String getServiceProvinceName() {
            return serviceProvinceName;
        }

        public void setServiceProvinceName(String serviceProvinceName) {
            this.serviceProvinceName = serviceProvinceName;
        }

        public String getServiceCityName() {
            return serviceCityName;
        }

        public void setServiceCityName(String serviceCityName) {
            this.serviceCityName = serviceCityName;
        }

        public String getServiceProvinceCode() {
            return serviceProvinceCode;
        }

        public void setServiceProvinceCode(String serviceProvinceCode) {
            this.serviceProvinceCode = serviceProvinceCode;
        }

        public String getServiceCityCode() {
            return serviceCityCode;
        }

        public void setServiceCityCode(String serviceCityCode) {
            this.serviceCityCode = serviceCityCode;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getAddressDetail() {
            return addressDetail;
        }

        public void setAddressDetail(String addressDetail) {
            this.addressDetail = addressDetail;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }
    }

    /**
     * 退换货原因
     * 
     * @author qiudongchao
     * 
     */
    public static class ReturnReason {
        private String code;
        private String desc;

        public String getReasonCode() {
            return code;
        }

        public void setReasonCode(String reasonCode) {
            this.code = reasonCode;
        }

        public String getReasonDesc() {
            return desc;
        }

        public void setReasonDesc(String reasonDesc) {
            this.desc = reasonDesc;
        }
    }

    /**
     * 获取邮寄和自提地址
     * 
     * @author qiudongchao
     * 
     */
    public static class EmailAddress {
        private String isPingByGome;
        private String isReturnMethodStore;
        private String isReturnMethodCustome;
        private ArrayList<PostAddress> postAddressList;
        private ArrayList<StoreAddress> storeAddressList;

        public String getIsPingByGome() {
            return isPingByGome;
        }

        public void setIsPingByGome(String isPingByGome) {
            this.isPingByGome = isPingByGome;
        }

        public String getIsReturnMethodStore() {
            return isReturnMethodStore;
        }

        public void setIsReturnMethodStore(String isReturnMethodStore) {
            this.isReturnMethodStore = isReturnMethodStore;
        }

        public String getIsReturnMethodCustome() {
            return isReturnMethodCustome;
        }

        public void setIsReturnMethodCustome(String isReturnMethodCustome) {
            this.isReturnMethodCustome = isReturnMethodCustome;
        }

        public ArrayList<PostAddress> getPostAddressList() {
            return postAddressList;
        }

        public void setPostAddressList(ArrayList<PostAddress> postAddressList) {
            this.postAddressList = postAddressList;
        }

        public ArrayList<StoreAddress> getStoreAddressList() {
            return storeAddressList;
        }

        public void setStoreAddressList(ArrayList<StoreAddress> storeAddressList) {
            this.storeAddressList = storeAddressList;
        }
    }

    /**
     * 邮寄
     * 
     * @author qiudongchao
     * 
     */
    public static class PostAddress {
        private String code;
        private String desc;

        public String getPostCode() {
            return code;
        }

        public void setPostCode(String postCode) {
            this.code = postCode;
        }

        public String getPostDesc() {
            return desc;
        }

        public void setPostDesc(String postDesc) {
            this.desc = postDesc;
        }

    }

    /**
     * 自提地址
     * 
     * @author qiudongchao
     * 
     */
    public static class StoreAddress {
        private String code;
        private String desc;

        public String getStoreCode() {
            return code;
        }

        public void setStoreCode(String storeCode) {
            this.code = storeCode;
        }

        public String getStoreDesc() {
            return desc;
        }

        public void setStoreDesc(String storeDesc) {
            desc = storeDesc;
        }
    }

    /**
     * 数据提交实体
     * 
     * @author qiudongchao
     */
    public static class ReturnSubmitEntity {
        private String shippingID;
        private String orderID;
        private String skuID;
        private String returnType;
        private String returnReasonCode;
        private String serviceProvinceCode;
        private String serviceCityCode;
        private String servicecountyCode;
        private String returnShippingMethod;
        private String returnShippingValue;
        private String attachment;
        private String surface;
        private String packages;
        private String hasInvoice;
        private String invoiceNO;
        private String isReport;
        private String questionDesc;
        private String user;
        private String phoneNumber;
        private String addressDetail;
        private String zipCode;
        private String index;

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public String getReturnShippingValue() {
            return returnShippingValue;
        }

        public void setReturnShippingValue(String returnShippingValue) {
            this.returnShippingValue = returnShippingValue;
        }

        public String getShippingID() {
            return shippingID;
        }

        public void setShippingID(String shippingID) {
            this.shippingID = shippingID;
        }

        public String getOrderID() {
            return orderID;
        }

        public void setOrderID(String orderID) {
            this.orderID = orderID;
        }

        public String getSkuID() {
            return skuID;
        }

        public void setSkuID(String skuID) {
            this.skuID = skuID;
        }

        public String getReturnType() {
            return returnType;
        }

        public void setReturnType(String returnType) {
            this.returnType = returnType;
        }

        public String getReturnReasonCode() {
            return returnReasonCode;
        }

        public void setReturnReasonCode(String returnReasonCode) {
            this.returnReasonCode = returnReasonCode;
        }

        public String getServiceProvinceCode() {
            return serviceProvinceCode;
        }

        public void setServiceProvinceCode(String serviceProvinceCode) {
            this.serviceProvinceCode = serviceProvinceCode;
        }

        public String getServiceCityCode() {
            return serviceCityCode;
        }

        public void setServiceCityCode(String serviceCityCode) {
            this.serviceCityCode = serviceCityCode;
        }

        public String getServicecountyCode() {
            return servicecountyCode;
        }

        public void setServicecountyCode(String servicecountyCode) {
            this.servicecountyCode = servicecountyCode;
        }

        public String getReturnShippingMethod() {
            return returnShippingMethod;
        }

        public void setReturnShippingMethod(String returnShippingMethod) {
            this.returnShippingMethod = returnShippingMethod;
        }

        public String getAttachment() {
            return attachment;
        }

        public void setAttachment(String attachment) {
            this.attachment = attachment;
        }

        public String getSurface() {
            return surface;
        }

        public void setSurface(String surface) {
            this.surface = surface;
        }

        public String getPackages() {
            return packages;
        }

        public void setPackages(String packages) {
            this.packages = packages;
        }

        public String getHasInvoice() {
            return hasInvoice;
        }

        public void setHasInvoice(String hasInvoice) {
            this.hasInvoice = hasInvoice;
        }

        public String getInvoiceNO() {
            return invoiceNO;
        }

        public void setInvoiceNO(String invoiceNO) {
            this.invoiceNO = invoiceNO;
        }

        public String getIsReport() {
            return isReport;
        }

        public void setIsReport(String isReport) {
            this.isReport = isReport;
        }

        public String getQuestionDesc() {
            return questionDesc;
        }

        public void setQuestionDesc(String questionDesc) {
            this.questionDesc = questionDesc;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getAddressDetail() {
            return addressDetail;
        }

        public void setAddressDetail(String addressDetail) {
            this.addressDetail = addressDetail;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }
    }

    /**
     * 退款记录
     * 
     * @author qiudongchao
     * 
     */
    public static class Refund {
        private String orderNum;
        private String status;
        private String method;
        private String orderCount;
        private String orderDate;
        private String reason;

        public String getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getOrderCount() {
            return orderCount;
        }

        public void setOrderCount(String orderCount) {
            this.orderCount = orderCount;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

    }
}
