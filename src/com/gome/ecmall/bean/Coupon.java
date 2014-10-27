package com.gome.ecmall.bean;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

/**
 * 优惠券 实体bean
 */
public class Coupon implements Serializable {

    public static final String COUPON_RED = "0";
    public static final String COUPON_BLUE = "1";
    public static final String COUPON_SHOP = "2";
    public static final String COUPON_BRAND = "3";
    private String couponId;
    private String couponName;
    private String couponAmount;
    private String couponExpirationDate;
    private boolean isExpiring;
    private String couponDesc;
    private boolean isSelect;
    private boolean isSuccess;
    private String errorMessage;
    private ArrayList<Coupon> redCouponList;
    private ArrayList<Coupon> blueCouponList;
    private ArrayList<Coupon> shopCouponList;
    private ArrayList<Coupon> brandCouponList;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(String couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getCouponExpirationDate() {
        return couponExpirationDate;
    }

    public void setCouponExpirationDate(String couponExpirationDate) {
        this.couponExpirationDate = couponExpirationDate;
    }

    public boolean isExpiring() {
        return isExpiring;
    }

    public void setExpiring(boolean isExpiring) {
        this.isExpiring = isExpiring;
    }

    public String getCouponDesc() {
        return couponDesc;
    }

    public void setCouponDesc(String couponDesc) {
        this.couponDesc = couponDesc;
    }

    public ArrayList<Coupon> getRedCouponList() {
        return redCouponList;
    }

    public void setRedCouponList(ArrayList<Coupon> redCouponList) {
        this.redCouponList = redCouponList;
    }

    public ArrayList<Coupon> getBlueCouponList() {
        return blueCouponList;
    }

    public void setBlueCouponList(ArrayList<Coupon> blueCouponList) {
        this.blueCouponList = blueCouponList;
    }

    public ArrayList<Coupon> getShopCouponList() {
        return shopCouponList;
    }

    public void setShopCouponList(ArrayList<Coupon> shopCouponList) {
        this.shopCouponList = shopCouponList;
    }

    public ArrayList<Coupon> getBrandCouponList() {
        return brandCouponList;
    }

    public void setBrandCouponList(ArrayList<Coupon> brandCouponList) {
        this.brandCouponList = brandCouponList;
    }

    public static String createRequestCouponJson(String couponType, String status) {
        JSONObject json = new JSONObject();
        try {
            if (!TextUtils.isEmpty(couponType)) {
                json.put(JsonInterface.JK_TICKET_TYPE, couponType);
            }
            if (!TextUtils.isEmpty(status)) {
                json.put(JsonInterface.JK_TICKET_STATUS, status);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public static String createRequestActivateCouponJson(String couponNo, String activeCode, String captcha) {
        JSONObject json = new JSONObject();
        try {
            json.put("couponNo", couponNo);
            json.put("activeCode", activeCode);
            json.put("captcha", captcha);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public static String createRequestExchangeCouponJson(String amount) {
        JSONObject json = new JSONObject();
        try {
            json.put("amount", amount);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public static Coupon parseCouponList(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        Coupon coupon = new Coupon();
        try {
            JSONObject jsContent = result.getJsContent();
            coupon.setRedCouponList(parseRedCouponList(jsContent.optJSONArray(JsonInterface.JK_RED_TICKET_LIST)));
            coupon.setBlueCouponList(parseBlueCouponList(jsContent.optJSONArray(JsonInterface.JK_BLUE_TICKET_LIST)));
            coupon.setShopCouponList(parseBrandCouponList(jsContent.optJSONArray("shopTicketList")));
            coupon.setBrandCouponList(parseBrandCouponList(jsContent.optJSONArray("brandTicketList")));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return coupon;
    }

    public static ArrayList<Coupon> parseRedCouponList(JSONArray array) {
        if (array == null || array.length() == 0) {
            return null;
        }

        ArrayList<Coupon> redCoupons = new ArrayList<Coupon>();
        try {
            for (int i = 0, size = array.length(); i < size; i++) {
                JSONObject item = array.getJSONObject(i);
                Coupon redCoupon = new Coupon();
                redCoupon.setCouponId(item.getString(JsonInterface.JK_RED_TICKET_ID));
                redCoupon.setCouponName(item.getString(JsonInterface.JK_RED_TICKET_NAME));
                redCoupon.setCouponAmount(item.getString(JsonInterface.JK_RED_TICKET_AMOUNT));
                redCoupon.setCouponExpirationDate(item.getString(JsonInterface.JK_RED_TICKET_EXPIRATION_DATE));
                redCoupon.setCouponDesc(item.getString(JsonInterface.JK_RED_TICKET_DESC));
                String isExpired = item.getString(JsonInterface.JK_IS_EXPIRING);
                if (JsonInterface.JV_YES.equals(isExpired)) {
                    redCoupon.setExpiring(true);
                } else {
                    redCoupon.setExpiring(false);
                }
                redCoupons.add(redCoupon);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return redCoupons;
    }

    public static ArrayList<Coupon> parseBrandCouponList(JSONArray array) {
        if (array == null || array.length() == 0) {
            return null;
        }
        ArrayList<Coupon> brandCoupons = new ArrayList<Coupon>();
        try {
            for (int i = 0, size = array.length(); i < size; i++) {
                JSONObject item = array.getJSONObject(i);
                Coupon brandCoupon = new Coupon();
                brandCoupon.setCouponId(item.getString("ticketID"));
                brandCoupon.setCouponName(item.getString("ticketName"));
                brandCoupon.setCouponAmount(item.getString("ticketAmount"));
                brandCoupon.setCouponExpirationDate(item.getString("ticketExpirationDate"));
                brandCoupon.setCouponDesc(item.optString("ticketDesc"));
                String isExpired = item.getString("isExpiring");
                if (JsonInterface.JV_YES.equals(isExpired)) {
                    brandCoupon.setExpiring(true);
                } else {
                    brandCoupon.setExpiring(false);
                }
                brandCoupons.add(brandCoupon);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return brandCoupons;
    }

    public static ArrayList<Coupon> parseBlueCouponList(JSONArray array) {
        if (array == null || array.length() == 0) {
            return null;
        }
        ArrayList<Coupon> blueCoupons = new ArrayList<Coupon>();
        try {
            for (int i = 0, size = array.length(); i < size; i++) {
                JSONObject item = array.getJSONObject(i);
                Coupon blueCoupon = new Coupon();
                blueCoupon.setCouponId(item.getString(JsonInterface.JK_BLUE_TICKET_ID));
                blueCoupon.setCouponName(item.getString(JsonInterface.JK_BLUE_TICKET_NAME));
                blueCoupon.setCouponAmount(item.getString(JsonInterface.JK_BLUE_TICKET_AMOUNT));
                blueCoupon.setCouponExpirationDate(item.optString(JsonInterface.JK_BLUE_TICKET_EXPIRATION_DATE));
                blueCoupon.setCouponDesc(item.getString(JsonInterface.JK_BLUE_TICKET_DESC));
                String isExpired = item.getString(JsonInterface.JK_IS_EXPIRING);
                if (JsonInterface.JV_YES.equals(isExpired)) {
                    blueCoupon.setExpiring(true);
                } else {
                    blueCoupon.setExpiring(false);
                }
                blueCoupons.add(blueCoupon);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return blueCoupons;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public static Coupon parseCouponDetail(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        JsonResult result = new JsonResult(json);
        Coupon brandCoupon = new Coupon();
        brandCoupon.setSuccess(result.isSuccess());
        if (!result.isSuccess()) {
            brandCoupon.setErrorMessage(result.getFailReason());
            return brandCoupon;
        }
        JSONObject item = result.getJsContent();
        try {
            brandCoupon.setCouponId(item.getString("ticketID"));
            brandCoupon.setCouponName(item.getString("ticketName"));
            brandCoupon.setCouponAmount(item.getString("ticketAmount"));
            brandCoupon.setCouponExpirationDate(item.getString("ticketExpirationDate"));
            brandCoupon.setCouponDesc(item.getString("ticketDesc"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return brandCoupon;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static String parseCoupon(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        JsonResult result = new JsonResult(json);
        Coupon coupon = new Coupon();
        if (!result.isSuccess()) {
            coupon.setErrorMessage(result.getFailReason());
        } else {
            coupon.setErrorMessage(result.getSuccessMessage());
        }

        return coupon.getErrorMessage();
    }

}
