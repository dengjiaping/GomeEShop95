package com.gome.ecmall.home.mygome;

import org.json.JSONException;
import org.json.JSONObject;

import com.gome.ecmall.bean.JsonInterface;

public class UserInfo {
    private String points;
    private String balance;
    private String waitPayOrderNum;
    private String waitConfirmOrder;
    private String arrGoodsNoticeNum;
    private String reduPriceNum;

    public UserInfo() {
        super();
    }

    public UserInfo(String points, String balance, String waitPayOrderNum, String waitConfirmOrder,
            String arrGoodsNoticeNum, String reduPriceNum) {
        super();
        this.points = points;
        this.balance = balance;
        this.waitPayOrderNum = waitPayOrderNum;
        this.waitConfirmOrder = waitConfirmOrder;
        this.arrGoodsNoticeNum = arrGoodsNoticeNum;
        this.reduPriceNum = reduPriceNum;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getWaitPayOrderNum() {
        return waitPayOrderNum;
    }

    public void setWaitPayOrderNum(String waitPayOrderNum) {
        this.waitPayOrderNum = waitPayOrderNum;
    }

    public String getWaitConfirmOrder() {
        return waitConfirmOrder;
    }

    public void setWaitConfirmOrder(String waitConfirmOrder) {
        this.waitConfirmOrder = waitConfirmOrder;
    }

    public String getArrGoodsNoticeNum() {
        return arrGoodsNoticeNum;
    }

    public void setArrGoodsNoticeNum(String arrGoodsNoticeNum) {
        this.arrGoodsNoticeNum = arrGoodsNoticeNum;
    }

    public String getReduPriceNum() {
        return reduPriceNum;
    }

    public void setReduPriceNum(String reduPriceNum) {
        this.reduPriceNum = reduPriceNum;
    }

    public static UserInfo parseJson(String json) {
        if (json == null || json.length() == 0)
            return null;
        try {
            JSONObject obj = new JSONObject(json);
            String points = obj.isNull(JsonInterface.JK_POINTS) ? null : obj.getString(JsonInterface.JK_POINTS);
            String balance = obj.isNull(JsonInterface.JK_BALANCE) ? null : obj.getString(JsonInterface.JK_BALANCE);
            String waitPayOrderNum = obj.isNull(JsonInterface.JK_WAIT_PAY_ORDER_NUM) ? null : obj
                    .getString(JsonInterface.JK_WAIT_PAY_ORDER_NUM);
            String waitConfOrderNum = obj.isNull(JsonInterface.JK_WAIT_CONFIRM_ORDER_NUM) ? null : obj
                    .getString(JsonInterface.JK_WAIT_CONFIRM_ORDER_NUM);
            String arrGoodsNoticeNum = obj.isNull(JsonInterface.JK_ARR_GOODS_NOTICE_NUM) ? null : obj
                    .getString(JsonInterface.JK_ARR_GOODS_NOTICE_NUM);
            String reduPriceNoteNum = obj.isNull(JsonInterface.JK_REDU_PRICE_NOTICE_NUM) ? null : obj
                    .getString(JsonInterface.JK_REDU_PRICE_NOTICE_NUM);
            UserInfo userInfo = new UserInfo(points, balance, waitPayOrderNum, waitConfOrderNum, arrGoodsNoticeNum,
                    reduPriceNoteNum);
            return userInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
