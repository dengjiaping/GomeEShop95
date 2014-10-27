package com.gome.ecmall.util;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.gome.eshopnew.R;
import com.omniture.AppMeasurement;

/**
 * 数据统计--工具类
 */
public class AppMeasurementUtils {

    private static final String Tag = "AppMeasurementUtils";
    public static final String NETWORK_TYPE_NONE = "-1"; // 断网情况
    public static final String NETWORK_TYPE_WIFI = "WIFI"; // WiFi模式
    public static final String NETWOKR_TYPE_MOBILE = "GPRS"; // gprs模式
    public static final String EVENT_LOGIN_SUCCESS = "event5"; //
    public static final String EVENT_REG_SUCCESS = "event4";
    public static final String EVENT_SHOPPINGCART = "scView";
    public static final String EVENT_SHOPPINGORDER = "scCheckout";
    public static final String EVENT_SHOPPINGORDER_SUCCESS = "purchase";
    public static final String EVENT_PRODUCEDETAIL = "prodView,event3";
    public static final String EVENT_ADDSHOPPINGCART = "scAdd";
    public static final String EVENT_SEARCHRESULT = "event1";
    public static final String EVENT_SEARCHRESULT_NORESULT = "event2";
    public static final String TRANK_LINK_PROM = "intPromotion";
    public static final String TRANK_LINK_SHOPPINGADD = "shoppingcart_add";
    private static String androidVersonCode;
    private static String operators;
    private static String versonCode;
    private static String imei;
    private static String mac;
    private static String uuid;
    private String netConType;
    private String userName;
    private String mobile;
    private String userId;
    private String loginType;
    // 渠道名称
    private String channalName;
    private Context mContext;

    /**
     * 八叉乐统计使用的工具类
     * 
     * @param mContext
     */
    public AppMeasurementUtils(Context context) {
        this.mContext = context;
        if (TextUtils.isEmpty(androidVersonCode)) {
            androidVersonCode = MobileDeviceUtil.getInstance(context.getApplicationContext()).getSystemVersion();
        }
        if (TextUtils.isEmpty(operators)) {
            operators = MobileDeviceUtil.getInstance(context.getApplicationContext()).getOperator();
        }
        if (TextUtils.isEmpty(versonCode)) {
            versonCode = MobileDeviceUtil.getInstance(context.getApplicationContext()).getVersonCode();
        }
        if (TextUtils.isEmpty(imei)) {
            imei = MobileDeviceUtil.getInstance(context.getApplicationContext()).getMobileImei();
        }
        if (TextUtils.isEmpty(mac)) {
            mac = MobileDeviceUtil.getInstance(context.getApplicationContext()).getMacAddress();
        }
        if (TextUtils.isEmpty(uuid)) {

            uuid = MobileDeviceUtil.getInstance(context.getApplicationContext()).getUUID();
        }
        netConType = MobileDeviceUtil.getNetType(context.getApplicationContext());
        if (TextUtils.isEmpty(channalName)) {
            channalName = MobileDeviceUtil.getInstance(context.getApplicationContext()).getChannalName();
        }

    }

    /**
     * 
     * @param pageName
     * @param channel
     * @param prop1
     * @param prop4
     * @param eVar27
     * @param eVar12
     * @param events
     * @param products
     * @param eVar11
     * @param purchaseID
     * @param eVar5
     * @param eVar6
     */
    public void getUrl(String pageName, String channel, String prop1, String prop4, String eVar27, String eVar12,
            String events, String products, String eVar11, String purchaseID, String eVar5, String eVar6, String eVar3,
            String eVar2, String eVar1, String eVar7, String[] trankLinks) {
        try {
            AppMeasurement s = new AppMeasurement(((Activity) mContext).getApplication());
            s.account = "gome-app";
            s.currencyCode = "CNY";
            s.visitorNamespace = "gome";
            s.trackingServer = "gome.122.2o7.net";
            if (!mContext.getString(R.string.appMeas_intalPage).equals(pageName)) {
                s.pageName = pageName;
                BDebug.e(Tag, "pageName=" + pageName);
                s.channel = channel;
                BDebug.e(Tag, "channel=" + channel);
                s.prop1 = prop1;
                BDebug.e(Tag, "prop1=" + prop1);
                s.prop4 = prop4;
                BDebug.e(Tag, "prop4=" + prop4);
            }
            s.eVar21 = androidVersonCode;
            BDebug.e(Tag, "eVar21=" + androidVersonCode);
            s.eVar22 = operators;
            BDebug.e(Tag, "operators=" + operators);
            s.eVar23 = versonCode;
            BDebug.e(Tag, "eVar23=" + versonCode);
            s.eVar24 = imei;
            BDebug.e(Tag, "eVar24=" + imei);
            s.eVar26 = uuid;
            BDebug.e(Tag, "uuid=" + uuid);
            // 渠道名称
            s.eVar28 = channalName;
            BDebug.e(Tag, "eVar28=" + channalName);
            s.eVar29 = mac;
            BDebug.e(Tag, "eVar29=" + mac);
            // 设备类型
            s.eVar36 = "Android";
            BDebug.e(Tag, "eVar36=" + "Android");
            if (!TextUtils.isEmpty(netConType)) {
                s.eVar25 = netConType;
                BDebug.e(Tag, "eVar25=" + netConType);
            }
            if (!TextUtils.isEmpty(eVar27)) {
                s.eVar27 = eVar27;
                BDebug.e(Tag, "eVar27=" + eVar27);
            }
            if (!TextUtils.isEmpty(events)) {
                if ("event7,event9".equals(events)) {
                    s.events = events + ",event10:" + imei;
                } else {
                    s.events = events;
                }
                BDebug.e(Tag, "events=" + s.events);
            }
            if (!TextUtils.isEmpty(products)) {
                s.products = products;
                BDebug.e(Tag, "products=" + products);
            }
            if (!TextUtils.isEmpty(eVar12)) {
                s.eVar12 = eVar12;
                BDebug.e(Tag, "eVar12=" + eVar12);
            }
            if (!TextUtils.isEmpty(eVar11)) {
                s.eVar11 = eVar11;
                BDebug.e(Tag, "eVar11=" + eVar11);
            }
            if (!TextUtils.isEmpty(purchaseID)) {
                s.purchaseID = purchaseID;
                BDebug.e(Tag, "purchaseID=" + purchaseID);
            }
            if (!TextUtils.isEmpty(eVar5)) {
                s.eVar5 = eVar5;
                BDebug.e(Tag, "eVar5=" + eVar5);
            }
            if (!TextUtils.isEmpty(eVar6)) {
                s.eVar6 = eVar6;
                BDebug.e(Tag, "eVar6=" + eVar6);
            }
            if (!TextUtils.isEmpty(eVar3)) {
                s.eVar3 = eVar3;
                BDebug.e(Tag, "eVar3=" + eVar3);
            }
            if (!TextUtils.isEmpty(eVar2)) {
                s.eVar2 = eVar2;
                BDebug.e(Tag, "eVar2=" + eVar2);
            }
            if (!TextUtils.isEmpty(eVar1)) {
                s.eVar1 = eVar1;
                BDebug.e(Tag, "eVar1=" + eVar1);
            }
            if (!TextUtils.isEmpty(eVar7)) {
                s.eVar7 = eVar7;
                BDebug.e(Tag, "eVar7=" + eVar7);
            }
            if (!TextUtils.isEmpty(getMobile())) {
                s.eVar19 = getMobile();
                BDebug.e(Tag, "eVar19=" + getMobile());
            }
            if (!TextUtils.isEmpty(getUserName())) {
                s.eVar20 = getUserName();
                BDebug.e(Tag, "eVar20=" + getUserName());
            }
            if (!TextUtils.isEmpty(getUserId())) {
                s.eVar14 = getUserId();
                BDebug.e(Tag, "eVar14=" + getUserId());
            }
            if (!TextUtils.isEmpty(getLoginType())) {
                s.eVar30 = getLoginType();
                BDebug.e(Tag, "eVar30=" + getLoginType());
            }
            if(!BDebug.DEBUG){
                if (trankLinks == null) {
                    s.track();
                } else if (trankLinks.length == 3) {
                    s.trackLink(trankLinks[0], trankLinks[1], trankLinks[2]);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

}
