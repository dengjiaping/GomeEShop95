package com.gome.ecmall.bean;

import java.util.ArrayList;

import com.gome.ecmall.util.PreferenceUtils;

/**
 * 全局配置【单独类】
 */
public class GlobalConfig {

    private static GlobalConfig config = new GlobalConfig();
    public static final String JSESSIONID = "JSESSIONID";
    public static final String GO_HOME = "go_home";
    public static final String GOHOME = "goHome";
    public static final int GO_TO_HOME = 1000;
    private String jSessionId = null;
    private String cookieInfo = null;
    private ArrayList<InventoryDivision> inventoryDivisions;
    private InventoryDivision preferenceDivision;
    private boolean isNeedLoadImage = true;
    private ArrayList<String> hotKeyWordsList;
    private int seachIndex = 0;

    public int getSeachIndex() {
        return seachIndex;
    }

    public void setSeachIndex(int seachIndex) {
        this.seachIndex = seachIndex;
    }

    // 经纬度
    private double log;
    private double lat;
    // 定位的城市code
    private double cityCode;
    // 定位的城市name
    private String cityName;
    // 定位城市的区code
    private String dependentLocalityCode;
    // 定位城市的区name
    private String dependentLocalityName;
    private boolean isNeedLocation;
    // 团购，抢购标识
    private int GroupLimitType;

    /** 登录Action */
    public static final String ACTION_LOGIN = "com.gome.ecmall.ACTION_LOGIN";
    /** 注册Action */
    public static final String ACTION_REGISTER = "com.gome.ecmall.ACTION_REGISTER";
    /** 收藏Action */
    public static final String ACTION_FAVO = "com.gome.ecmall.ACTION_FAVO";

    /**
     * 判断用户是否登录
     */
    public static boolean isLogin;
    public static String userName;
    public static String password;
    public static int points;
    public static String balance;
    public static boolean isRegister;

    public static final String GOME_PREFS = "gome_prefs";
    public static final String CATEGORY = "category_prefs";
    public static final String IS_FIRST_LOGIN = "isFirstLogin";
    public static final String IS_AUTO_LOGIN = "isAutoLogin";

    /** 订单类型数组,0默认全部，1一个月内，2一个月前，3老用户 */
    public static final int[] ORDER_TYPE = { 0, 1, 2, 3 };
    /** 订单状态数组,0默认全部，1待支付订单，2收货确认订单 */
    public static final int[] ORDER_STATUS = { 0, 1, 2 };
    public static final String USER_NAME = "userName";
    public static final String PASSWORD = "password";
    public static final String CLASS_NAME = "className";
    public static final String KEY = "key";
    public static final String SIGN = "sign";
    public static final String IS_FIRST_RUN = "isFirstRun";
    public static final String IS_FIRST_USE = "isFirstUse";
    public static final String IS_ALLOW_LOCATION = "isAllowLocation";

    /** 待支付订单 */
    public static int WAIT_PAY_ORDER_NUM;

    /** 收货确认订单 */
    public static int WAIT_CONFIRM_ORDER_NUM;
    public static boolean isFirstRun;
    public static boolean isFirstUse;
    public static boolean isFirstShow;

    private GlobalConfig(){

    }

    public static GlobalConfig getInstance() {
        return config;
    }

    public void setjSessionId(String jSessionId) {
        this.jSessionId = jSessionId;
    }

    public String getjSessionId() {
        return jSessionId;
    }

    public String getCookieInfo() {
        return cookieInfo;
    }

    public void setCookieInfo(String cookieInfo) {
        this.cookieInfo = cookieInfo;
    }

    public String getUserName() {
        return userName;
    }

    public void setNeedLoadImage(boolean isNeedLoadImage) {
        this.isNeedLoadImage = isNeedLoadImage;
    }

    public boolean isNeedLoadImage() {
        return isNeedLoadImage;
    }

    public ArrayList<InventoryDivision> getInventoryDivisions() {
        return inventoryDivisions;
    }

    public void setInventoryDivisions(ArrayList<InventoryDivision> inventoryDivisions) {
        this.inventoryDivisions = inventoryDivisions;
    }

    public InventoryDivision getPreferenceDivision() {
        preferenceDivision = PreferenceUtils.getPreferenceDivision();
        return preferenceDivision;
    }

    public void setPreferenceDivision(InventoryDivision preferenceDivision) {
        this.preferenceDivision = preferenceDivision;
        PreferenceUtils.savePreferenceDivision(preferenceDivision);
    }

    public void setHotKeyWordsList(ArrayList<String> hotKeyWordsList) {
        this.hotKeyWordsList = hotKeyWordsList;
    }

    public ArrayList<String> getHotKeyWordsList() {
        return hotKeyWordsList;
    }

    public double getLog() {
        return log;
    }

    public void setLog(double log) {
        this.log = log;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getCityCode() {
        return cityCode;
    }

    public void setCityCode(double cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDependentLocalityCode() {
        return dependentLocalityCode;
    }

    public void setDependentLocalityCode(String dependentLocalityCode) {
        this.dependentLocalityCode = dependentLocalityCode;
    }

    public String getDependentLocalityName() {
        return dependentLocalityName;
    }

    public void setDependentLocalityName(String dependentLocalityName) {
        this.dependentLocalityName = dependentLocalityName;
    }

    public int getGroupLimitType() {
        return GroupLimitType;
    }

    public void setGroupLimitType(int groupLimitType) {
        GroupLimitType = groupLimitType;
    }

    public boolean isNeedLocation() {
        return isNeedLocation;
    }

    public void setNeedLocation(boolean isNeedLocation) {
        this.isNeedLocation = isNeedLocation;
    }

}
