package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gome.ecmall.bean.JsonResult;
import com.gome.ecmall.bean.ReturnProduct.Deal;
import com.gome.ecmall.bean.ReturnProduct.EmailAddress;
import com.gome.ecmall.bean.ReturnProduct.PostAddress;
import com.gome.ecmall.bean.ReturnProduct.Refund;
import com.gome.ecmall.bean.ReturnProduct.ReturnAddress;
import com.gome.ecmall.bean.ReturnProduct.ReturnEntity;
import com.gome.ecmall.bean.ReturnProduct.ReturnGoods;
import com.gome.ecmall.bean.ReturnProduct.ReturnOrder;
import com.gome.ecmall.bean.ReturnProduct.ReturnRate;
import com.gome.ecmall.bean.ReturnProduct.ReturnReason;
import com.gome.ecmall.bean.ReturnProduct.ReturnRecord;
import com.gome.ecmall.bean.ReturnProduct.ReturnSubmitEntity;
import com.gome.ecmall.bean.ReturnProduct.ShipInfo;
import com.gome.ecmall.bean.ReturnProduct.StoreAddress;

/**
 * 退换货业务处理
 * 
 * @author qiudongchao
 * 
 */
public class MyReturnServer {
    public static String errorMessage = "";

    // ---------------------------------------------------
    /**
     * 请求退换货列表
     * 
     * @param profileID
     *            用户Id
     * @return
     */
    public static String build_Request_MyGome_Return_List(int currentPage, int pageSize) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("currentPage", currentPage);
            requestJson.put("pageSize", pageSize);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 退换货申请记录
     * 
     * @param profileID
     *            用户Id
     * @return
     */
    public static String build_Request_MyGome_Return_Record_List(String profileID) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("profileID", profileID);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 退换货申请进度查询
     * 
     * @param returnNO
     *            返修编号
     * @param orderID
     *            订单编号
     * @return
     */
    public static String build_Request_MyGome_Return_Rate(String returnNO, String orderID) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("returnNO", returnNO);
            requestJson.put("orderID", orderID);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 申请退换货
     * 
     * @param shippingID
     *            配送单号
     * @param orderID
     *            订单编号
     * @param skuID
     *            申请退货商品号
     * @return
     */
    public static String build_Request_MyGome_Return_Apply_For(String shippingID, String orderID, String skuID,
            String index) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("shippingID", shippingID);
            requestJson.put("orderID", orderID);
            requestJson.put("skuID", skuID);
            requestJson.put("index", index);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取邮寄和自提地址
     * 
     * @param serviceProvinceCode
     *            售后服务地区省代码
     * @param serviceCityCode
     *            售后服务地区市代码
     * @param servicecountyCode
     *            售后服务地区县代码
     * @return
     */
    public static String build_Request_MyGome_Return_Address(String serviceProvinceCode, String serviceCityCode,
            String servicecountyCode, String orderId, String skuId) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("serviceProvinceCode", serviceProvinceCode);
            requestJson.put("serviceCityCode", serviceCityCode);
            requestJson.put("servicecountyCode", servicecountyCode);
            requestJson.put("orderID", orderId);
            requestJson.put("skuID", skuId);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 提交退货申请
     * 
     * @param obj
     * @return
     */
    public static String build_Request_MyGome_Return_Submit(ReturnSubmitEntity rs) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("shippingID", rs.getShippingID());
            requestJson.put("orderID", rs.getOrderID());
            requestJson.put("skuID", rs.getSkuID());
            requestJson.put("returnType", rs.getReturnType());
            requestJson.put("returnReasonCode", rs.getReturnReasonCode());
            requestJson.put("serviceProvinceCode", rs.getServiceProvinceCode());
            requestJson.put("serviceCityCode", rs.getServiceCityCode());
            requestJson.put("servicecountyCode", rs.getServicecountyCode());
            requestJson.put("returnShippingMethod", rs.getReturnShippingMethod());
            requestJson.put("returnShippingValue", rs.getReturnShippingValue());
            requestJson.put("attachment", rs.getAttachment());
            requestJson.put("surface", rs.getSurface());
            requestJson.put("goodsPackage", rs.getPackages());
            requestJson.put("hasInvoice", rs.getHasInvoice());
            requestJson.put("invoiceNO", rs.getInvoiceNO());
            requestJson.put("isReport", rs.getIsReport());
            requestJson.put("questionDesc", rs.getQuestionDesc());
            requestJson.put("user", rs.getUser());
            requestJson.put("phoneNumber", rs.getPhoneNumber());
            requestJson.put("addressDetail", rs.getAddressDetail());
            requestJson.put("zipCode", rs.getZipCode());
            requestJson.put("index", rs.getIndex());
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // -------------------------------------------------

    /**
     * 请求退换货列表
     * 
     * @return
     */
    public static ArrayList<ReturnOrder> paser_Response_MyGome_Return_List(String json) {
        ArrayList<ReturnOrder> list = new ArrayList<ReturnOrder>();
        if (json == null || json.length() == 0) {
            return null;
        }
        JsonResult jsResult = new JsonResult(json);
        if (!jsResult.isSuccess()) {
            return null;
        }
        JSONObject obj = jsResult.getJsContent();
        JSONArray jaOrders = obj.optJSONArray("orders");
        if (jaOrders != null && jaOrders.length() > 0) {
            int size = jaOrders.length();
            list.ensureCapacity(size);
            for (int i = 0; i < size; i++) {
                ReturnOrder order = null;
                try {
                    order = new ReturnOrder();
                    JSONObject objOrder = jaOrders.getJSONObject(i);
                    order.setOrderID(objOrder.optString("orderID"));
                    order.setOrderAmount(objOrder.optString("orderAmount"));
                    order.setOrderStatus(objOrder.optString("orderStatus"));
                    order.setOrderSubmitTime(objOrder.optString("orderSubmitTime"));
                    order.setShippingList(parserShipInfo(objOrder.optJSONArray("shippingList")));
                } catch (JSONException e) {
                }
                if (order != null) {
                    list.add(order);
                }
            }
        }
        return list;
    }

    /**
     * 解析配送信息
     * 
     * @param optJSONArray
     * @return
     */
    private static ArrayList<ShipInfo> parserShipInfo(JSONArray optJSONArray) {
        ArrayList<ShipInfo> list = null;
        if (optJSONArray != null && optJSONArray.length() > 0) {
            int size = optJSONArray.length();
            list = new ArrayList<ShipInfo>();
            list.ensureCapacity(size);
            for (int i = 0; i < size; i++) {
                ShipInfo si = null;
                try {
                    si = new ShipInfo();
                    JSONObject jo = optJSONArray.getJSONObject(i);
                    si.setShippingID(jo.optString("shippingID"));
                    si.setPrice(jo.optString("price"));
                    si.setShowApplyButton(jo.optString("showApplyButton"));
                    si.setGoodsList(parserOrders(jo.optJSONArray("goodsList")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (si != null) {
                    list.add(si);
                }
            }
        }
        return list;
    }

    /**
     * 解析退换货商品
     * 
     * @param optJSONArray
     * @return
     */
    private static ArrayList<ReturnGoods> parserOrders(JSONArray optJSONArray) {
        ArrayList<ReturnGoods> list = new ArrayList<ReturnGoods>();
        if (optJSONArray != null && optJSONArray.length() > 0) {
            int size = optJSONArray.length();
            list.ensureCapacity(size);
            for (int i = 0; i < size; i++) {
                ReturnGoods good = null;
                try {
                    good = new ReturnGoods();
                    JSONObject jo = optJSONArray.getJSONObject(i);
                    good.setSkuID(jo.optString("skuID"));
                    good.setSkuName(jo.optString("skuName"));
                    good.setGoodsNo(jo.optString("goodsNo"));
                    good.setSkuThumbImgUrl(jo.optString("skuThumbImgUrl"));
                    good.setReturnPrice(jo.optString("returnPrice"));
                    good.setShowApplyButton(jo.optString("showApplyButton"));
                    good.setDesc(jo.optString("desc"));
                    good.setIndex(jo.optString("index"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (good != null) {
                    list.add(good);
                }
            }
        }
        return list;
    }

    /**
     * 提交退货申请
     * 
     * @return
     */
    public static Object[] paser_Response_MyGome_Return_Submit(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        JsonResult result = new JsonResult(json);
        if (result.isSuccess()) {
            return new Object[] { true };
        } else {
            return new Object[] { false, result.getFailReason() };
        }
    }

    /**
     * 请求退换货列表
     * 
     * @return
     */
    public static ArrayList<ReturnRecord> paser_Response_MyGome_Record_List(String json) {
        ArrayList<ReturnRecord> list = new ArrayList<ReturnRecord>();
        if (json == null || json.length() == 0) {
            return null;
        }
        JsonResult jsResult = new JsonResult(json);
        if (!jsResult.isSuccess()) {
            return null;
        }
        JSONObject obj = jsResult.getJsContent();
        JSONArray jaOrders = obj.optJSONArray("returnRecords");
        if (jaOrders != null && jaOrders.length() > 0) {
            int size = jaOrders.length();
            list.ensureCapacity(size);
            for (int i = 0; i < size; i++) {
                ReturnRecord order = null;
                try {
                    order = new ReturnRecord();
                    JSONObject objOrder = jaOrders.getJSONObject(i);
                    order.setOrderID(objOrder.optString("orderID"));
                    order.setReturnApplayTime(objOrder.optString("returnApplayTime"));
                    order.setReturnNO(objOrder.optString("returnNO"));
                    order.setReturnStatus(objOrder.optString("returnStatus"));
                    order.setReturnType(objOrder.optString("returnType"));
                    order.setGoodsList(parserOrders(objOrder.optJSONArray("goodsList")));
                } catch (JSONException e) {
                }
                if (order != null) {
                    list.add(order);
                }
            }
        }
        return list;
    }

    /**
     * 请求进度查询
     * 
     * @return
     */
    public static ReturnRate paser_Response_MyGome_Return_Rate(String json) {
        ReturnRate pr = null;
        if (json == null || json.length() == 0) {
            return null;
        }
        JsonResult jsResult = new JsonResult(json);
        if (!jsResult.isSuccess()) {
            return null;
        }
        JSONObject obj = jsResult.getJsContent();
        pr = new ReturnRate();
        pr.setOrderID(obj.optString("orderID"));
        pr.setReturnNO(obj.optString("returnNO"));
        pr.setReturnStatus(obj.optString("returnStatus"));
        pr.setReturnType(obj.optString("returnType"));
        pr.setReturnApplayTime(obj.optString("returnApplayTime"));
        pr.setGoodsNo(obj.optString("goodsNo"));
        pr.setSkuID(obj.optString("skuID"));
        pr.setSkuName(obj.optString("skuName"));
        pr.setSkuThumbImgUrl(obj.optString("skuThumbImgUrl"));
        pr.setDealList(parserDeal(obj.optJSONArray("dealList")));

        return pr;
    }

    /**
     * 解析进度列表
     * 
     * @param optJSONArray
     * @return
     */
    private static ArrayList<Deal> parserDeal(JSONArray optJSONArray) {
        ArrayList<Deal> list = new ArrayList<Deal>();
        if (optJSONArray != null && optJSONArray.length() > 0) {
            int size = optJSONArray.length();
            list.ensureCapacity(size);
            for (int i = 0; i < size; i++) {
                Deal si = null;
                try {
                    si = new Deal();
                    JSONObject jo = optJSONArray.getJSONObject(i);
                    si.setDealDesc(jo.optString("dealDesc"));
                    si.setDealTime(jo.optString("dealTime"));
                    si.setDealUser(jo.optString("dealUser"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (si != null) {
                    list.add(si);
                }
            }
        }
        return list;
    }

    /**
     * 请求进度查询
     * 
     * @return
     */
    public static ReturnEntity paser_Response_MyGome_Return_Entity(String json) {
        ReturnEntity re = null;
        if (json == null || json.length() == 0) {
            return null;
        }
        JsonResult jsResult = new JsonResult(json);
        if (!jsResult.isSuccess()) {
            return null;
        }
        JSONObject obj = jsResult.getJsContent();
        re = new ReturnEntity();
        re.setIsPingByGome(obj.optString("isPingByGome"));
        re.setIsReturnMethodCustome(obj.optString("isReturnMethodCustome"));
        re.setIsReturnMethodStore(obj.optString("isReturnMethodStore"));
        re.setOrderID(obj.optString("orderID"));
        re.setReturnType(obj.optString("returnType"));
        re.setIsBBC(obj.optString("isBBC"));
        re.setReturnDesc(obj.optString("returnDesc"));

        re.setSkuID(obj.optString("skuID"));
        re.setShippingID(obj.optString("shippingID"));
        re.setIsNeedInvoiceNO(obj.optString("isNeedInvoiceNO"));
        re.setIsNeedReturnReason(obj.optString("isNeedReturnReason"));

        re.setServiceCityCode(obj.optString("serviceCityCode"));
        re.setServiceCityName(obj.optString("serviceCityName"));
        re.setServiceProvinceCode(obj.optString("serviceProvinceCode"));
        re.setServiceProvinceName(obj.optString("serviceProvinceName"));
        re.setServiceCountyCode(obj.optString("serviceCountyCode"));
        re.setServiceCountyName(obj.optString("serviceCountyName"));

        re.setAddress(parserReturnAddress(obj.optJSONObject("address")));
        re.setGoodsList(parserOrders(obj.optJSONArray("goodsList")));
        re.setReturnReason(parserReturnReason(obj.optJSONArray("returnReasons")));
        return re;
    }

    /**
     * 退换货原因
     * 
     * @param optJSONArray
     * @return
     */
    private static ArrayList<ReturnReason> parserReturnReason(JSONArray optJSONArray) {

        ArrayList<ReturnReason> list = new ArrayList<ReturnReason>();
        if (optJSONArray != null && optJSONArray.length() > 0) {
            int size = optJSONArray.length();
            list.ensureCapacity(size);
            for (int i = 0; i < size; i++) {
                ReturnReason si = null;
                try {
                    si = new ReturnReason();
                    JSONObject jo = optJSONArray.getJSONObject(i);
                    si.setReasonCode(jo.optString("code"));
                    si.setReasonDesc(jo.optString("desc"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (si != null) {
                    list.add(si);
                }
            }
        }
        return list;
    }

    /**
     * 收货地址
     * 
     * @param optJSONArray
     * @return
     */
    private static ReturnAddress parserReturnAddress(JSONObject jo) {
        ReturnAddress si = null;
        if (jo != null) {
            si = new ReturnAddress();
            si.setAddressDetail(jo.optString("addressDetail"));
            si.setPhoneNumber(jo.optString("phoneNumber"));
            si.setUser(jo.optString("user"));
            si.setZipCode(jo.optString("zipCode"));
            si.setServiceCityCode(jo.optString("serviceCityCode"));
            si.setServiceCityName(jo.optString("serviceCityName"));
            si.setServiceProvinceCode(jo.optString("serviceProvinceCode"));
            si.setServiceProvinceName(jo.optString("serviceProvinceName"));
            si.setServiceCountyCode(jo.optString("serviceCountyCode"));
            si.setServiceCountyName(jo.optString("serviceCountyName"));
        }
        return si;
    }

    /**
     * 请求进度查询
     * 
     * @return
     */
    public static EmailAddress paser_Response_MyGome_Return_A(String json) {
        EmailAddress re = null;
        if (json == null || json.length() == 0) {
            return null;
        }
        JsonResult jsResult = new JsonResult(json);
        if (!jsResult.isSuccess()) {
            return null;
        }
        JSONObject obj = jsResult.getJsContent();
        re = new EmailAddress();
        re.setIsPingByGome(obj.optString("isPingByGome"));
        re.setIsReturnMethodCustome(obj.optString("isReturnMethodCustome"));
        re.setIsReturnMethodStore(obj.optString("isReturnMethodStore"));

        re.setPostAddressList(parserPost(obj.optJSONArray("postAddressList")));
        re.setStoreAddressList(parserStore(obj.optJSONArray("storeAddressList")));
        return re;
    }

    /**
     * 解析自提地址
     * 
     * @param optJSONArray
     * @return
     */
    private static ArrayList<StoreAddress> parserStore(JSONArray optJSONArray) {

        ArrayList<StoreAddress> list = new ArrayList<StoreAddress>();
        if (optJSONArray != null && optJSONArray.length() > 0) {
            int size = optJSONArray.length();
            list.ensureCapacity(size);
            for (int i = 0; i < size; i++) {
                StoreAddress si = null;
                try {
                    si = new StoreAddress();
                    JSONObject jo = optJSONArray.getJSONObject(i);
                    si.setStoreCode(jo.optString("code"));
                    si.setStoreDesc(jo.optString("desc"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (si != null) {
                    list.add(si);
                }
            }
        }
        return list;
    }

    /**
     * 解析邮寄地址
     * 
     * @param optJSONArray
     * @return
     */
    private static ArrayList<PostAddress> parserPost(JSONArray optJSONArray) {
        ArrayList<PostAddress> list = new ArrayList<PostAddress>();
        if (optJSONArray != null && optJSONArray.length() > 0) {
            int size = optJSONArray.length();
            list.ensureCapacity(size);
            for (int i = 0; i < size; i++) {
                PostAddress si = null;
                try {
                    si = new PostAddress();
                    JSONObject jo = optJSONArray.getJSONObject(i);
                    si.setPostCode(jo.optString("code"));
                    si.setPostDesc(jo.optString("desc"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (si != null) {
                    list.add(si);
                }
            }
        }
        return list;
    }

    /**
     * 退款请求
     * 
     * @return
     */
    public static String build_Request_MyGome_Refund(int pagesize, int index) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("pageSize", pagesize);
            requestJson.put("currentPage", index);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 退款请求--解析 TODO
     * 
     * @return
     */
    public static ArrayList<Refund> parser_Request_MyGome_Refund(String json) {
        ArrayList<Refund> list = new ArrayList<Refund>();
        if (json == null || json.length() == 0) {
            return null;
        }
        JsonResult jsResult = new JsonResult(json);
        if (!jsResult.isSuccess()) {
            return null;
        }
        JSONObject obj = jsResult.getJsContent();
        // ///////////////////////
        JSONArray optJSONArray;
        try {
            optJSONArray = obj.getJSONArray("refundRecords");
            if (optJSONArray != null && optJSONArray.length() > 0) {
                int size = optJSONArray.length();
                list.ensureCapacity(size);
                for (int i = 0; i < size; i++) {
                    Refund si = null;
                    try {
                        si = new Refund();
                        JSONObject jo = optJSONArray.getJSONObject(i);
                        si.setMethod(jo.optString("refundRequestMethod"));
                        si.setOrderCount(jo.optString("suggestedRefundAmount"));
                        si.setOrderDate(jo.optString("createDate"));
                        si.setOrderNum(jo.optString("orderId"));
                        si.setReason(jo.optString("reason"));
                        si.setStatus(jo.optString("status"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (si != null) {
                        list.add(si);
                    }
                }
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return list;
    }
}
