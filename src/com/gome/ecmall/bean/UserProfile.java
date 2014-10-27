package com.gome.ecmall.bean;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gome.ecmall.home.login.LoginManager;
import com.gome.ecmall.util.Constants;

/**
 * 用户信息
 */
public class UserProfile implements Serializable {

    /**
     * 解析用户信息
     * 
     * @param json
     * @return
     */
    public static UserProfile parseUserProfile(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        UserProfile profile = null;
        try {
            JSONObject jsContent = result.getJsContent();
            profile = new UserProfile();
            profile.setPoints(jsContent.getString(JsonInterface.JK_POINTS));
            profile.setBalance(jsContent.getString(JsonInterface.JK_BALANCE));
            profile.setLoginName(jsContent.optString(JsonInterface.JK_LOGIN_NAME));
            String waitPay = jsContent.getString(JsonInterface.JK_WAIT_PAY_ORDER_NUM);
            String waitConfirm = jsContent.getString(JsonInterface.JK_WAIT_CONFIRM_ORDER_NUM);
            profile.setWaitPayOrderNum((waitPay.equals("0")) ? "" : waitPay);
            profile.setWaitConfirmOrderNum((waitConfirm.equals("0")) ? "" : waitConfirm);
            profile.setGradeName(jsContent.getString(JsonInterface.JK_GRADE_NAME));
            profile.setMemberIcon(jsContent.getString(JsonInterface.JK_MEMBER_ICON));
            profile.setMobile(jsContent.optString("mobile"));
            profile.setProfileId(jsContent.getString(JsonInterface.JK_PROFILE_ID));
            profile.setWaitReadMessageNum(jsContent.optString(JsonInterface.JK_WAIT_READ_MESSAGE_NUM));
            profile.setWaitEvaluateGoodsNum(jsContent.optString(JsonInterface.JK_WAIT_EVALUATE_GOODS_NUM));
            profile.setHasCouponExpires(jsContent.optString(JsonInterface.JK_WAIT_EXPIRINGCOUPONNUM));// 有快过期的商品
            profile.setCouponNum(jsContent.optString(JsonInterface.JK_COUPONNUM));
            profile.setIsSavedPaypassword(jsContent.optString(JsonInterface.JK_ISSAVEDPAYPASSWORD));
            profile.setVirtualAccountStatusDesc(jsContent.optString(JsonInterface.JK_VIRTUAL_ACCOUNT_STATUS_DESC));
            profile.setIsHaveExpiringGroupCoupon(jsContent.optString(JsonInterface.JK_IS_HAVE_EXPIRING_GROUPCOUPON));
            profile.setActivated(result.getIsActivated());
            // profile.setArrGoodsNoticeNum(jsContent.getString(JsonInterface.JK_ARR_GOODS_NOTICE_NUM));
            // profile.setOffSaleNoticeNum(jsContent.getString(JsonInterface.JK_REDU_PRICE_NOTICE_NUM));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return profile;
    }

    public static UserProfile parseCouponList(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        UserProfile profile = null;
        try {
            JSONObject jsContent = result.getJsContent();
            profile = new UserProfile();
            profile.setPoints(jsContent.optString(JsonInterface.JK_POINTS));
            profile.setExchangeCouponList(parseRedCouponList(jsContent.optJSONArray("ticketList")));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return profile;
    }

    public static ArrayList<Coupon> parseRedCouponList(JSONArray array) {
        if (array == null || array.length() == 0) {
            return null;
        }

        ArrayList<Coupon> redCoupons = new ArrayList<Coupon>();
        try {
            for (int i = 0, size = array.length(); i < size; i++) {
                JSONObject item = array.getJSONObject(i);
                Coupon brandCoupon = new Coupon();
                brandCoupon.setCouponName(item.optString("ticketName"));
                brandCoupon.setCouponAmount(item.optString("ticketAmount"));
                brandCoupon.setCouponDesc(item.optString("desc"));
                String isExpired = item.optString("isAllowedSelect");
                if (JsonInterface.JV_YES.equals(isExpired)) {
                    brandCoupon.setSelect(true);
                } else {
                    brandCoupon.setSelect(false);
                }
                redCoupons.add(brandCoupon);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return redCoupons;
    }

    private String points;
    private String balance;
    private String waitPayOrderNum;
    private String waitConfirmOrderNum;
    private String arrGoodsNoticeNum;
    private String offSaleNoticeNum;
    private String loginName;
    private String gradeName;
    private String memberIcon;
    private String mobile;

    private String waitEvaluateGoodsNum;
    private String waitReadMessageNum;
    private String hasCouponExpires;
    private String profileId;
    private String couponNum;
    private String isSavedPaypassword;
    private String virtualAccountStatusDesc;
    private boolean isActivated;
    private ArrayList<Coupon> exchangeCouponList;
    
    private String isHaveExpiringGroupCoupon;//是否有即将到期的团购劵
    

    public String getIsHaveExpiringGroupCoupon() {
        return isHaveExpiringGroupCoupon;
    }

    public void setIsHaveExpiringGroupCoupon(String isHaveExpiringGroupCoupon) {
        this.isHaveExpiringGroupCoupon = isHaveExpiringGroupCoupon;
    }

    public String getHasCouponExpires() {
        return hasCouponExpires;
    }

    public void setHasCouponExpires(String hasCouponExpires) {
        this.hasCouponExpires = hasCouponExpires;
    }

    public String getWaitEvaluateGoodsNum() {
        return waitEvaluateGoodsNum;
    }

    public void setWaitEvaluateGoodsNum(String waitEvaluateGoodsNum) {
        this.waitEvaluateGoodsNum = waitEvaluateGoodsNum;
    }

    public String getWaitReadMessageNum() {
        return waitReadMessageNum;
    }

    public void setWaitReadMessageNum(String waitReadMessageNum) {
        this.waitReadMessageNum = waitReadMessageNum;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getMemberIcon() {
        return memberIcon;
    }

    public void setMemberIcon(String memberIcon) {
        this.memberIcon = memberIcon;
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

    public String getWaitConfirmOrderNum() {
        return waitConfirmOrderNum;
    }

    public void setWaitConfirmOrderNum(String waitConfirmOrderNum) {
        this.waitConfirmOrderNum = waitConfirmOrderNum;
    }

    public String getArrGoodsNoticeNum() {
        return arrGoodsNoticeNum;
    }

    public void setArrGoodsNoticeNum(String arrGoodsNoticeNum) {
        this.arrGoodsNoticeNum = arrGoodsNoticeNum;
    }

    public String getOffSaleNoticeNum() {
        return offSaleNoticeNum;
    }

    public void setOffSaleNoticeNum(String offSaleNoticeNum) {
        this.offSaleNoticeNum = offSaleNoticeNum;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getCouponNum() {
        return couponNum;
    }

    public void setCouponNum(String couponNum) {
        this.couponNum = couponNum;
    }

    public ArrayList<Coupon> getExchangeCouponList() {
        return exchangeCouponList;
    }

    public void setExchangeCouponList(ArrayList<Coupon> exchangeCouponList) {
        this.exchangeCouponList = exchangeCouponList;
    }

    /**
     * 创建手机充值待支付订单请求json串
     * 
     * @return
     */
    public static String creatPhoneRequestJson(String profileId) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(JsonInterface.JK_PROFILE_ID, profileId);
            String sign = LoginManager.getSigns(profileId, Constants.PHONE_RECHARGE_SECRET_KEY);
            obj.put(JsonInterface.JK_SIGN, sign);
            return obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int parsePhoneToPayOrderNum(String json) {
        if (json == null || json.length() == 0) {
            return 0;
        }
        try {
            JSONObject object = new JSONObject(json);
            boolean isSuccess = object.optString(JsonInterface.JK_SUCCESS).equals("Y") ? true : false;
            if (isSuccess) {
                int num = object.optInt("orderCount");
                return num;
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;
    }

    public String getIsSavedPaypassword() {
        return isSavedPaypassword;
    }

    public void setIsSavedPaypassword(String isSavedPaypassword) {
        this.isSavedPaypassword = isSavedPaypassword;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean isActivated) {
        this.isActivated = isActivated;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVirtualAccountStatusDesc() {
        return virtualAccountStatusDesc;
    }

    public void setVirtualAccountStatusDesc(String virtualAccountStatusDesc) {
        this.virtualAccountStatusDesc = virtualAccountStatusDesc;
    }

}
