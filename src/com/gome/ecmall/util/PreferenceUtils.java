package com.gome.ecmall.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.InventoryDivision;

/**
 *  SharedPreferences 工具类
 */
public class PreferenceUtils {

    private static SharedPreferences prefs = null;

    public static SharedPreferences getInstance(Context ctx) {

        if (ctx == null) {
            BDebug.e("Context is null ", "ctx == null");
        }

        if (prefs == null) {
            prefs = ctx.getSharedPreferences(GlobalConfig.GOME_PREFS, Context.MODE_PRIVATE);
        }
        return prefs;
    }

    public static boolean getBoolValue(String key, boolean value) {
        return prefs.getBoolean(key, value);
    }

    public static String getStringValue(String key, String value) {
        return prefs.getString(key, value);
    }

    public static void setBooleanValue(String key, boolean value) {
        Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void setStringValue(String key, String value) {
        Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setStringValue(String[] keys, String[] values) {
        Editor editor = prefs.edit();
        for (int i = 0; i < keys.length; i++) {
            editor.putString(keys[i], values[i]);
        }
        editor.commit();
    }

    public static boolean isFirstLogin() {
        return getBoolValue(GlobalConfig.IS_FIRST_LOGIN, true);
    }

    public static boolean isAutoLogin() {
        return getBoolValue(GlobalConfig.IS_AUTO_LOGIN, true);
    }

    public static void setFirstLogin(boolean isFirstLogin) {
        setBooleanValue(GlobalConfig.IS_FIRST_LOGIN, isFirstLogin);
    }

    public static void setAutoLogin(boolean isAutoLogin) {
        setBooleanValue(GlobalConfig.IS_AUTO_LOGIN, isAutoLogin);
    }

    public static void clearData() {
        setBooleanValue(GlobalConfig.IS_AUTO_LOGIN, false);
    }

    public static String getUserName() {
        String userName = "";
        try {
            userName = DES.decryptDES(getStringValue(GlobalConfig.USER_NAME, ""), Constants.LOGINDESKEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userName;
    }

    public static void setLogoUrl(String url) {
        setStringValue("appLogoUrl", url);
    }

    public static String getLogoUrl() {
        return getStringValue("appLogoUrl", "");
    }

    public static boolean isFirstRun() {
        return getBoolValue(GlobalConfig.IS_FIRST_RUN, true);
    }

    public static void setFirstRun(boolean isFirstRun) {
        setBooleanValue(GlobalConfig.IS_FIRST_RUN, isFirstRun);
    }

    public static void setIntValue(String key, int value) {
        Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getIntValue(String key, int value) {
        return prefs.getInt(key, value);
    }

    public static boolean isFirstUse() {
        return getBoolValue(GlobalConfig.IS_FIRST_USE, true);
    }

    public static void setFirstUse(boolean isFirstUse) {
        setBooleanValue(GlobalConfig.IS_FIRST_USE, isFirstUse);
    }
    /**
     * 保存地区偏好设置
     * @param preferenceDivision 地区信息
     */
    public static void savePreferenceDivision(InventoryDivision preferenceDivision) {
        Editor editor = prefs.edit();
        // 偏好地区
        editor.putString("divisionCode", preferenceDivision.getDivisionCode());
        editor.putString("divisionName", preferenceDivision.getDivisionName());
        // 偏好地区父级 城市
        editor.putString("parentDivisionCode", preferenceDivision.getParentDivision().getDivisionCode());
        editor.putString("parentDivisionName", preferenceDivision.getParentDivision().getDivisionName());
        // 偏好地区父级的父级 省份
        editor.putString("parentParentDivisionCode", preferenceDivision.getParentDivision().getParentDivision()
                .getDivisionCode());
        editor.putString("parentParentDivisionName", preferenceDivision.getParentDivision().getParentDivision()
                .getDivisionName());

        editor.commit();
    }
    /**
     * 获取地区信息
     * @return 地区信息
     */
    public static InventoryDivision getPreferenceDivision() {
        InventoryDivision preferenceDivision = null;
        InventoryDivision parentPreferenceDivision = null;
        InventoryDivision parentParentPreferenceDivision = null;
        if (!TextUtils.isEmpty(prefs.getString("divisionCode", ""))) {
            preferenceDivision = new InventoryDivision(InventoryDivision.DIVISION_LEVEL_COUNTRY);
            parentPreferenceDivision = new InventoryDivision(InventoryDivision.DIVISION_LEVEL_CITY);
            parentParentPreferenceDivision = new InventoryDivision(InventoryDivision.DIVISION_LEVEL_PROVINCE);
            // 偏好地区
            preferenceDivision.setDivisionCode(prefs.getString("divisionCode", ""));
            preferenceDivision.setDivisionName(prefs.getString("divisionName", ""));
            // 偏好地区父级 城市
            parentPreferenceDivision.setDivisionCode(prefs.getString("parentDivisionCode", ""));
            parentPreferenceDivision.setDivisionName(prefs.getString("parentDivisionName", ""));
            // 偏好地区父级的父级 省份
            parentParentPreferenceDivision.setDivisionCode(prefs.getString("parentParentDivisionCode", ""));
            parentParentPreferenceDivision.setDivisionName(prefs.getString("parentParentDivisionName", ""));

            parentPreferenceDivision.setParentDivision(parentParentPreferenceDivision);

            preferenceDivision.setParentDivision(parentPreferenceDivision);
        }
        return preferenceDivision;
    }

    /**
     * 获取是否允许 定位偏好
     * @return
     */
    public static boolean isAllowLocation() {
        return getBoolValue(GlobalConfig.IS_ALLOW_LOCATION, false);
    }

    /**
     * 设置是否允许定位偏好
     * @param isFirstUse
     */
    public static void setAllowLocation(boolean isFirstUse) {
        setBooleanValue(GlobalConfig.IS_ALLOW_LOCATION, isFirstUse);
    }

    // 分类数据缓存
    public static final String CATEGORY_RESULT = "category_result";

    /**
     * 缓存分类数据
     * @param result
     */
    public static void writeToSharePreferFile(String result) {
        setStringValue(CATEGORY_RESULT, result);
    }

    /**
     * 读取缓存数据
     * @return
     */
    public static String readSharePreferFile() {
        return getStringValue(CATEGORY_RESULT, "");
    }
    
    //增量升级是否出错，容错机制
    private static final String DIFF = "diff";
    
    public static void setDiffUpdate(String version){
    	setStringValue(DIFF, version);
    }
    
    public static String getDiffUpdate(){
    	return getStringValue(DIFF, "");
    }
}
