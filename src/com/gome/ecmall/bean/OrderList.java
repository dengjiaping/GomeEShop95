package com.gome.ecmall.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.gome.ecmall.home.login.LoginManager;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.UrlMatcher;

/**
 * 用户订单列表
 * 
 * @author Administrator
 * 
 */
public class OrderList {

    public static final String ORDERTYPE = "phoneRechargeOrder";

    /** 订单时限类型 */
    private int typeOrder;

    /** 当前页 */
    private int currentPage;

    /** 每页条数 */
    private int pageSize;

    /** 订单状态 */
    private int orderStatus;

    private ArrayList<Order> orderList;

    public OrderList(int typeOrder, int currentPage, int pageSize, int orderStatus) {
        this.typeOrder = typeOrder;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.orderStatus = orderStatus;
    }

    public OrderList(ArrayList<Order> orderList) {
        super();
        this.orderList = orderList;
    }

    public int getTypeOrder() {
        return typeOrder;
    }

    public void setTypeOrder(int typeOrder) {
        this.typeOrder = typeOrder;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public ArrayList<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(ArrayList<Order> orderList) {
        this.orderList = orderList;
    }

    /**
     * 订单,可以有多个商品
     * 
     * @author Administrator
     * 
     */
    public static class Order {
        /** 订单号 */
        private String orderID;

        /** 订单状态 */
        private String orderStatus;

        /** 订单金额 */
        private String orderAmount;

        /** 下单时间 */
        private String orderSubmitTime;
        // 是否支持BBC商品--bo.yang
        private String isBbc;
        /** 商品列表 */
        private ArrayList<Goods> goodsList;

        /** 手机充值商品 */
        private Goods phoneGoods;
        
        private String grouponType;//团购商品类型标示0：实体团购商品，1：虚拟团购商品
        private int orderGoodsCount;//订单中商品数量
        

        public int getOrderGoodsCount() {
            return orderGoodsCount;
        }

        public void setOrderGoodsCount(int orderGoodsCount) {
            this.orderGoodsCount = orderGoodsCount;
        }

        public String getGrouponType() {
            return grouponType;
        }

        public void setGrouponType(String grouponType) {
            this.grouponType = grouponType;
        }

        public Order() {
        }

        public Order(String orderID, String orderStatus, String orderAmount, String orderSubmitTime,
                ArrayList<Goods> goodsList) {
            super();
            this.orderID = orderID;
            this.orderStatus = orderStatus;
            this.orderAmount = orderAmount;
            this.orderSubmitTime = orderSubmitTime;
            this.goodsList = goodsList;
        }

        public ArrayList<Goods> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(ArrayList<Goods> goodsList) {
            this.goodsList = goodsList;
        }

        public String getOrderID() {
            return orderID;
        }

        public void setOrderID(String orderID) {
            this.orderID = orderID;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getOrderAmount() {
            return orderAmount;
        }

        public void setOrderAmount(String orderAmount) {
            this.orderAmount = orderAmount;
        }

        public String getOrderSubmitTime() {
            return orderSubmitTime;
        }

        public void setOrderSubmitTime(String orderSubmitTime) {
            this.orderSubmitTime = orderSubmitTime;
        }

        public String getIsBbc() {
            return isBbc;
        }

        public void setIsBbc(String isBbc) {
            this.isBbc = isBbc;
        }

        public Goods getPhoneGoods() {
            return phoneGoods;
        }

        public void setPhoneGoods(Goods phoneGoods) {
            this.phoneGoods = phoneGoods;
        }

    }

    /**
     * 手机支付商品【继承Goods】
     */
    public static class PhoneRechargeGoods extends Goods {
        /** mobile number */
        private String phoneNum;

        public String getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
        }
    }

   

    /**
     * 构造手机充值订单列表请求json串
     * 
     * @return
     */
    public static String createPhoneRechargeOrderListRequest(int orderType, int currentPage, int pageSize,
            int orderStatus, String profileId) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(JsonInterface.JK_PROFILE_ID, profileId);
            obj.put(JsonInterface.JK_CURRENT_PAGE, currentPage);
            obj.put(JsonInterface.JK_PAGE_SIZE, pageSize);
            obj.put(JsonInterface.JK_DAY_TYPE_ORDER, orderType);
            String sign = LoginManager.getSigns(profileId, currentPage + "", pageSize + "", orderType + "",
                    Constants.PHONE_RECHARGE_SECRET_KEY);

            obj.put(JsonInterface.JK_SIGN, sign);
            return obj.toString();
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * @param orderType
     *            订单时限（类型）typeOrder 订单时限(1=一个月内、2=一个月前、3=老用户)；0:默认全部（包括待支付订单、收货待确认订单）
     * @param currentPage
     *            当前页
     * @param pageSize
     *            每页条数
     * @param orderStatus
     *            订单状态
     * @return
     */
    public static String createRequest(int duration, int currentPage, int pageSize, int orderStatus,int orderType) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(JsonInterface.JK_DURATION, duration);
            obj.put(JsonInterface.JK_CURRENT_PAGE, currentPage);
            obj.put(JsonInterface.JK_PAGE_SIZE, pageSize);
            obj.put(JsonInterface.JK_ORDER_STATUS, orderStatus);
            obj.put(JsonInterface.JK_ORDER_TYPE, orderType);//0：普通订单（默认）；1：团购订单
            return obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Order> parseJsonOrderList(JSONArray array, String orderType) {
        if (array == null) {
            return null;
        }
        int len = array.length();
        ArrayList<Order> list = new ArrayList<OrderList.Order>();
        for (int i = 0; i < len; i++) {
            list.add(parseJsonOrder(array.optJSONObject(i), orderType));
        }
        return list;
    }

    public static Order parseJsonOrder(JSONObject obj, String orderType) {
        if (obj == null) {
            return null;
        }
        Order order = new Order();
        order.setOrderStatus(obj.optString(JsonInterface.JK_ORDER_STATUS));
        if (orderType != null && orderType.equals(ORDERTYPE)) {
            order.setOrderID(obj.optString(JsonInterface.JK_ORDER_ID_LOW));
            PhoneRechargeGoods goods = new PhoneRechargeGoods();
            goods.setSkuID(obj.optString(JsonInterface.JK_SKU_ID));
            goods.setSkuName(obj.optString(JsonInterface.JK_SKU_NAME));
            String imgPath = obj.optString(JsonInterface.JK_SKU_THUMB_IMG_URL);
            imgPath = UrlMatcher.replaceSuffix(imgPath, "_50.");
            goods.setSkuThumbImgUrl(imgPath);
            goods.setPhoneNum(obj.optString(JsonInterface.JK_PHONE_RECHARGE_PHONE_NUM));
            order.setPhoneGoods(goods);
        } else {
            order.setOrderID(obj.optString(JsonInterface.JK_ORDER_ID));
            order.setGoodsList(parseJsonGoodsList(obj.optJSONArray(JsonInterface.JK_GOODS_LIST)));
            order.setIsBbc(obj.optString(JsonInterface.JK_ISBBC));// 普通订单添加是否为bbc标识，而充值订单则不需要
            order.setGrouponType(obj.optString(JsonInterface.JK_GROUPON_TYPE));
            order.setOrderGoodsCount(obj.optInt(JsonInterface.JK_ORDER_GOODS_COUNT));
        }
        order.setOrderAmount(obj.optString(JsonInterface.JK_ORDER_AMOUNT));
        order.setOrderSubmitTime(obj.optString(JsonInterface.JK_ORDER_SUBMIT_TIME));
        return order;
    }

    /**
     * @param json
     *            普通订单列表
     * @return
     */
    public static ArrayList<Order> parseJson(String json, String orderType) {
        if (json == null || json.length() == 0) {
            return null;
        }
        JsonResult jsResult = new JsonResult(json);
        if (!jsResult.isSuccess()) {
            return null;
        }
        JSONObject obj = jsResult.getJsContent();
        return parseJsonOrderList(obj.optJSONArray(JsonInterface.JK_ORDERS), orderType);
    }

    public static ArrayList<Goods> parseJsonGoodsList(JSONArray array) {
        if (array == null) {
            return null;
        }
        int len = array.length();
        ArrayList<Goods> list = new ArrayList<Goods>();
        for (int i = 0; i < len; i++) {
            list.add(parseJsonGoods(array.optJSONObject(i)));
        }
        return list;
    }

    public static Goods parseJsonGoods(JSONObject obj) {
        if (obj == null)
            return null;
        try {
            Goods goods = new Goods();
            goods.setSkuID(obj.optString(JsonInterface.JK_SKU_ID));
            goods.setGoodsNo(obj.optString(JsonInterface.JK_GOODS_NO));
            goods.setSkuName(obj.optString(JsonInterface.JK_SKU_NAME));
            goods.setSkuThumbImgUrl(UrlMatcher.getFitListThumbUrl(obj.optString(JsonInterface.JK_SKU_THUMB_IMG_URL)));
            return goods;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
