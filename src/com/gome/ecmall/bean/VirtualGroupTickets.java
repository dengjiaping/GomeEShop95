package com.gome.ecmall.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.gome.ecmall.bean.GBProductNew.GroupBuyProduct;
import com.gome.ecmall.home.groupbuy.VirtualGroupTicketsActivity;

/**
 * 虚拟团购劵
 * @author liuyang-ds
 *
 */
public class VirtualGroupTickets {

    public static final String JK_CURRENTPAGE = "currentPage";
    public static final String JK_PAGESIZE = "pageSize";
    public static final String JK_ORDERID = "orderId";
    public static final String JK_STATUS = "status";
    public static final String JK_GROUP_TICKETS = "groupTickets";
    
    
    public static final String JK_MOBILENUM = "mobileNum";
    public static final String JK_GROUPTICKETNUM = "groupTicketNum";
    public static final String JK_SKUID = "skuID";
    public static final String JK_SALEPROMOITEM = "salePromoItem";
    public static final String JK_ISALLOWREFUND = "isAllowRefund";
    public static final String JK_PRODUCTNAME = "productName";
    public static final String JK_SKUTHUMBIMGURL = "skuThumbImgUrl";
    public static final String JK_BUYTIME = "buyTime";
    public static final String JK_DEADLINE = "deadline";
    public static final String JK_GOODSNO = "goodsNo";
    public static final String JK_ISEXPIRING = "isExpiring";
    public static final String JK_ISBUY = "isbuy";
    public static final String JK_DETAILID = "detailId";
    public static final String JK_ISHAVEEXPIRING = "isHaveExpiring";
    public static final String JK_TICKETNO = "ticketNo";
    public static final String JK_FAILREASON = "failReason";
    
    private String orderId;
    private String mobileNum;
    private String groupTicketNum;
    private String skuID;
    private String salePromoItem;
    private String isAllowRefund;
    private String productName;
    private String skuThumbImgUrl;
    private String buyTime;
    private String deadline;
    private String goodsNo;
    private String Status;
    private String isExpiring;
    private String isbuy;
    private String detailId;
    private String isHaveExpiring;
    
    private boolean isLoadImg; // 下载图片

    public boolean isLoadImg() {
        return isLoadImg;
    }

    public void setLoadImg(boolean isLoadImg) {
        this.isLoadImg = isLoadImg;
    }

    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getMobileNum() {
        return mobileNum;
    }
    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }
    public String getGroupTicketNum() {
        return groupTicketNum;
    }
    public void setGroupTicketNum(String groupTicketNum) {
        this.groupTicketNum = groupTicketNum;
    }
    public String getSkuID() {
        return skuID;
    }
    public void setSkuID(String skuID) {
        this.skuID = skuID;
    }
    public String getSalePromoItem() {
        return salePromoItem;
    }
    public void setSalePromoItem(String salePromoItem) {
        this.salePromoItem = salePromoItem;
    }
    public String getIsAllowRefund() {
        return isAllowRefund;
    }
    public void setIsAllowRefund(String isAllowRefund) {
        this.isAllowRefund = isAllowRefund;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getSkuThumbImgUrl() {
        return skuThumbImgUrl;
    }
    public void setSkuThumbImgUrl(String skuThumbImgUrl) {
        this.skuThumbImgUrl = skuThumbImgUrl;
    }
    public String getBuyTime() {
        return buyTime;
    }
    public void setBuyTime(String buyTime) {
        this.buyTime = buyTime;
    }
    public String getDeadline() {
        return deadline;
    }
    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
    public String getGoodsNo() {
        return goodsNo;
    }
    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }
    public String getStatus() {
        return Status;
    }
    public void setStatus(String status) {
        Status = status;
    }
    public String getIsExpiring() {
        return isExpiring;
    }
    public void setIsExpiring(String isExpiring) {
        this.isExpiring = isExpiring;
    }
    public String getIsbuy() {
        return isbuy;
    }
    public void setIsbuy(String isbuy) {
        this.isbuy = isbuy;
    }
    public String getDetailId() {
        return detailId;
    }
    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
    public String getIsHaveExpiring() {
        return isHaveExpiring;
    }
    public void setIsHaveExpiring(String isHaveExpiring) {
        this.isHaveExpiring = isHaveExpiring;
    }
    /**
     * 创建请求我的团购卷的数据
     * @param currentPage_used
     * @param pageSize
     * @param orderId2
     * @param status2
     * @return
     */
    public static String createRequestTicketsJson(int currentPage_used, int pageSize, String orderId2, String status2) {
        
        JSONObject json = new JSONObject();
        try {
            json.put(JK_CURRENTPAGE, currentPage_used);
            json.put(JK_PAGESIZE, pageSize);
            if(!TextUtils.isEmpty(orderId2)){
                json.put(JK_ORDERID, orderId2);
            }
            json.put(JK_STATUS, status2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }
    
    
    /**
     * 解析我的团购卷数据
     * @param response
     * @return
     */
    public static ArrayList<VirtualGroupTickets> parseTicketsList(String response) {
        
        JsonResult result = new JsonResult(response);
        if (!result.isSuccess()) {
            return null;
        }
        try {
            JSONObject content = result.getJsContent();
            JSONArray ticketsArray = content.optJSONArray(JK_GROUP_TICKETS);
            if (ticketsArray != null) {
                ArrayList<VirtualGroupTickets> ticketsList = new ArrayList<VirtualGroupTickets>();
                for (int i = 0, length = ticketsArray.length(); i < length; i++) {
                    JSONObject item = ticketsArray.getJSONObject(i);
                    VirtualGroupTickets ticket = new VirtualGroupTickets();
                    ticket.setOrderId(item.optString(JK_ORDERID));
                    ticket.setMobileNum(item.optString(JK_MOBILENUM));
                    ticket.setGroupTicketNum(item.optString(JK_GROUPTICKETNUM));
                    ticket.setSkuID(item.optString(JK_SKUID));
                    ticket.setSalePromoItem(item.optString(JK_SALEPROMOITEM));
                    ticket.setIsAllowRefund(item.optString(JK_ISALLOWREFUND));
                    ticket.setProductName(item.optString(JK_PRODUCTNAME));
                    ticket.setSkuThumbImgUrl(item.optString(JK_SKUTHUMBIMGURL));
                    ticket.setBuyTime(item.optString(JK_BUYTIME));
                    ticket.setDeadline(item.optString(JK_DEADLINE));
                    ticket.setGoodsNo(item.optString(JK_GOODSNO));
                    ticket.setStatus(item.optString(JK_STATUS));
                    ticket.setIsExpiring(item.optString(JK_ISEXPIRING));
                    ticket.setIsbuy(item.optString(JK_ISBUY));
                    ticket.setDetailId(item.optString(JK_DETAILID));
                    ticketsList.add(ticket);
                }
                return ticketsList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
/**
 * 创建发送短信json
 * @param orderId2
 * @param groupTicketNum2
 * @param productName2
 * @param deadline2
 * @param detailId2
 * @return
 */
    public static String createRequestSmsJson(String orderId2, String groupTicketNum2, String productName2,
            String deadline2, String detailId2) {
        JSONObject json = new JSONObject();
        try {
            json.put(JK_ORDERID, orderId2);
            json.put(JK_TICKETNO, groupTicketNum2);
            json.put(JK_PRODUCTNAME, productName2);
            json.put(JK_DEADLINE, deadline2);
            json.put(JK_DETAILID, detailId2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    /**
     * 解析发送短信
     * @param response
     * @return
     */
public static String parseRequestSms(String response) {
    JsonResult result = new JsonResult(response);
    String str = "";
    try {
        if(result.isSuccess()){
            str = "Y";
           
        }else{
            JSONObject content = result.getJsContent();
            String failReason  = content.optString(JK_FAILREASON);
            str = "N"+failReason;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return str;
}
/**
 * 解析是否有将要过期的团购劵
 * @param response
 * @return
 */
    public static Boolean parseIsHaveExpiring(String response) {
        JsonResult result = new JsonResult(response);
        if (result.isSuccess()) {
            JSONObject content = result.getJsContent();
            String str = content.optString(JK_ISHAVEEXPIRING);
            if("Y".equalsIgnoreCase(str)){
                return true;
            }
        }
        return false;
    }
    

}
