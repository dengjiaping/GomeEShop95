package com.gome.ecmall.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VirtualGroupApplyRefund {
    public static final String JK_ORDERID = "orderId";
    public static final String JK_TICKET_NUM = "ticketNum";
    public static final String JK_REFOUND_CAUSE_LIST = "refoundCauseList";
    public static final String JK_REFOUND_CAUSE_CODE = "refoundCauseCode";
    public static final String JK_REFOUND_CAUSE = "refoundCause";
    public static final String JK_PRICE = "price";
    public static final String JK_SKUID = "skuId";
    public static final String JK_DETAILID = "detailId";
    private ArrayList<RefoundCause> list;
    private String orderId; 
    private String ticketNum; 
    private String price; 
    private String skuId;
    private String detailId;
    
    
    public String getDetailId() {
        return detailId;
    }
    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
    public ArrayList<RefoundCause> getList() {
        return list;
    }
    public void setList(ArrayList<RefoundCause> list) {
        this.list = list;
    }
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getTicketNum() {
        return ticketNum;
    }
    public void setTicketNum(String ticketNum) {
        this.ticketNum = ticketNum;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getSkuId() {
        return skuId;
    }
    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }
    /**
     * 退款原因
     * @author liuyang-ds
     *
     */
    public static class RefoundCause{
        private String refoundCauseCode; 
        private String refoundCause;
        public String getRefoundCauseCode() {
            return refoundCauseCode;
        }
        public void setRefoundCauseCode(String refoundCauseCode) {
            this.refoundCauseCode = refoundCauseCode;
        }
        public String getRefoundCause() {
            return refoundCause;
        }
        public void setRefoundCause(String refoundCause) {
            this.refoundCause = refoundCause;
        }
    }
    /**
     * 创建请求申请退款原因json
     * @param orderId2
     * @param ticketNum2
     * @return
     */
    public static String createRequestApplyRefundJson(String orderId2, String ticketNum2,String detailId) {
        JSONObject json = new JSONObject();
        try {
            json.put(JK_ORDERID, orderId2);
            json.put(JK_TICKET_NUM, ticketNum2);
            json.put(JK_DETAILID, detailId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }
    /**
     * 解析申请退款原因
     * @param response
     * @return
     */
    public static VirtualGroupApplyRefund parseApplyRefundt(String response) {
        JsonResult result = new JsonResult(response);
        if (!result.isSuccess()) {
            return null;
        }
        try {
            JSONObject content = result.getJsContent();
            VirtualGroupApplyRefund refund = new VirtualGroupApplyRefund();
            JSONArray causeArray = content.optJSONArray(JK_REFOUND_CAUSE_LIST);
            if (causeArray != null) {
                ArrayList<RefoundCause> causeList = new ArrayList<RefoundCause>();
                for (int i = 0, length = causeArray.length(); i < length; i++) {
                    JSONObject item = causeArray.getJSONObject(i);
                    RefoundCause cause = new RefoundCause();
                   cause.setRefoundCause(item.optString(JK_REFOUND_CAUSE));
                   cause.setRefoundCauseCode(item.optString(JK_REFOUND_CAUSE_CODE));
                   causeList.add(cause);
                }
                refund.setList(causeList);
            }
            refund.setOrderId(content.optString(JK_ORDERID));
            refund.setTicketNum(content.optString(JK_TICKET_NUM));
            refund.setPrice(content.optString(JK_PRICE));
            refund.setSkuId(content.optString(JK_SKUID));
            refund.setDetailId(content.optString(JK_DETAILID));
            return refund;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 创建提交退款申请json
     * @param orderId2
     * @param ticketNum2
     * @param refoundCauseCode
     * @return
     */
    public static String createRequestApplyRefundSubmitJson(String orderId2, String ticketNum2, String refoundCauseCode,String skuId,String detailId) {
        JSONObject json = new JSONObject();
        try {
            json.put(JK_ORDERID, orderId2);
            json.put(JK_TICKET_NUM, ticketNum2);
            json.put(JK_REFOUND_CAUSE_CODE, refoundCauseCode);
            json.put(JK_SKUID, skuId);
            json.put(JK_DETAILID, detailId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    } 
    

}
