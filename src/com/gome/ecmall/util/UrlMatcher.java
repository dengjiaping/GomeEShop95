package com.gome.ecmall.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * URL匹配--工具类
 */
public class UrlMatcher {

    // public static final int DENSITY_XHIGH = 320;
    // public static final int DENSITY_XXHIGH = 480;

    public static final String PRODUCT_LIST_THUMB_LDPI = "_60.";
    public static final String PRODUCT_LIST_THUMB_MDPI = "_100."; // _116
    public static final String PRODUCT_LIST_THUMB_HDPI = "_140."; // _140
    public static final String PRODUCT_LIST_THUMB_XHDPI = "_210.";// _180

    public static final String PRODUCT_GRID_THUMB_LDPI = "_120.";// _125
    public static final String PRODUCT_GRID_THUMB_MDPI = "_160.";// _170
    public static final String PRODUCT_GRID_THUMB_HDPI = "_260.";// _240
    public static final String PRODUCT_GRID_THUMB_XHDPI = "_360.";// _340

    public static final String PRODUCT_GALLERY_THUMB_LDPI = "_120.";// _125
    public static final String PRODUCT_GALLERY_THUMB_MDPI = "_160.";// _170
    public static final String PRODUCT_GALLERY_THUMB_HDPI = "_260.";// _240
    public static final String PRODUCT_GALLERY_THUMB_XHDPI = "_360.";// _340

    public static final String PRODUCT_GALLERY_SOURCE_LDPI = "_260.";// _200
    public static final String PRODUCT_GALLERY_SOURCE_MDPI = "_360.";// _340
    public static final String PRODUCT_GALLERY_SOURCE_HDPI = "_400.";// _480
    public static final String PRODUCT_GALLERY_SOURCE_XHDPI = "_800.";

    public static final String HOMEPAGE_PAGE_AD_LDPI = "_320.";
    public static final String HOMEPAGE_PAGE_AD_MDPI = "_320.";
    public static final String HOMEPAGE_PAGE_AD_HDPI = "_480.";
    public static final String HOMEPAGE_PAGE_AD_XHDPI = "_640.";

    public static final String HOT_PROMOTION_IMG_LDPI = "_120.";
    public static final String HOT_PROMOTION_IMG_MDPI = "_160.";
    public static final String HOT_PROMOTION_IMG_HDPI = "_260.";
    public static final String HOT_PROMOTION_IMG_XHDPI = "_400.";

    public static final String PRESELL_IMG_LDPI = "_320.";// 320
    public static final String PRESELL_IMG_MDPI = "_480.";// 480
    public static final String PRESELL_IMG_HDPI = "_720.";// 720
    public static final String PRESELL_IMG_XDPI = "_1080.";// 1080

    public static final String LOGO_IMG_LDPI = "_320_480.";// 320
    public static final String LOGO_IMG_MDPI = "_480_800.";// 480
    public static final String LOGO_IMG_HDPI = "_720_1280.";// 720

    private static int currentDPI;
    public static final String TAG = "UrlMatcher";

    public static void initUrlMatcher(Context context) {
        currentDPI = MobileDeviceUtil.getInstance(context.getApplicationContext()).getScreenDensityDpi();
    }
    /**
     * 根据屏幕密度小图适配
     * @param url
     * @return 适配的小图url
     */
    public static String getFitListThumbUrl(String url) {
        String newUrl = url;
        switch (currentDPI) {
        case DisplayMetrics.DENSITY_LOW:
            newUrl = replaceSuffix(url, PRODUCT_LIST_THUMB_LDPI);
            break;
        case DisplayMetrics.DENSITY_MEDIUM:
            newUrl = replaceSuffix(url, PRODUCT_LIST_THUMB_MDPI);
            break;
        case DisplayMetrics.DENSITY_HIGH:
            newUrl = replaceSuffix(url, PRODUCT_LIST_THUMB_HDPI);
            break;
        case DisplayMetrics.DENSITY_XHIGH:
            newUrl = replaceSuffix(url, PRODUCT_LIST_THUMB_XHDPI);
            break;
        case DisplayMetrics.DENSITY_XXHIGH:
            newUrl = replaceSuffix(url, PRODUCT_LIST_THUMB_XHDPI);
            break;
        }
        // BDebug.d(TAG, "getFitProductThumbUrl:" + newUrl);
        return newUrl;
    }

    /**
     * 根据屏幕密度匹配网格图片
     * @param url
     * @return
     */
    public static String getFitGridThumbUrl(String url) {
        String newUrl = url;
        switch (currentDPI) {
        case DisplayMetrics.DENSITY_LOW:
            newUrl = replaceSuffix(url, PRODUCT_GRID_THUMB_LDPI);
            break;
        case DisplayMetrics.DENSITY_MEDIUM:
            newUrl = replaceSuffix(url, PRODUCT_GRID_THUMB_MDPI);
            break;
        case DisplayMetrics.DENSITY_HIGH:
            newUrl = replaceSuffix(url, PRODUCT_GRID_THUMB_HDPI);
            break;
        case DisplayMetrics.DENSITY_XHIGH:
            newUrl = replaceSuffix(url, PRODUCT_GRID_THUMB_XHDPI);
            break;
        case DisplayMetrics.DENSITY_XXHIGH:
            newUrl = replaceSuffix(url, PRODUCT_GRID_THUMB_XHDPI);
            break;
        }
        return newUrl;
    }

    /**
     * 根据屏幕密度匹配图库图片
     * @param url
     * @return
     */
    public static String getFitGalleryThumbUrl(String url) {
        String newUrl = url;
        switch (currentDPI) {
        case DisplayMetrics.DENSITY_LOW:
            newUrl = replaceSuffix(url, HOT_PROMOTION_IMG_LDPI);
            break;
        case DisplayMetrics.DENSITY_MEDIUM:
            newUrl = replaceSuffix(url, HOT_PROMOTION_IMG_MDPI);
            break;
        case DisplayMetrics.DENSITY_HIGH:
            newUrl = replaceSuffix(url, HOT_PROMOTION_IMG_HDPI);
            break;
        case DisplayMetrics.DENSITY_XHIGH:
            newUrl = replaceSuffix(url, HOT_PROMOTION_IMG_XHDPI);
            break;
        case DisplayMetrics.DENSITY_XXHIGH:
            newUrl = replaceSuffix(url, HOT_PROMOTION_IMG_XHDPI);
            break;
        }
        return newUrl;
    }

    /**
     * @param url
     *            适配热门促销图片大小
     * @return
     */
    public static String getFitHotPromotionThumbUrl(String url) {
        String newUrl = url;
        switch (currentDPI) {
        case DisplayMetrics.DENSITY_LOW:
            newUrl = replaceSuffix(url, PRODUCT_GALLERY_THUMB_LDPI);
            break;
        case DisplayMetrics.DENSITY_MEDIUM:
            newUrl = replaceSuffix(url, PRODUCT_GALLERY_THUMB_MDPI);
            break;
        case DisplayMetrics.DENSITY_HIGH:
            newUrl = replaceSuffix(url, PRODUCT_GALLERY_THUMB_HDPI);
            break;
        case DisplayMetrics.DENSITY_XHIGH:
            newUrl = replaceSuffix(url, PRODUCT_GALLERY_THUMB_XHDPI);
            break;
        case DisplayMetrics.DENSITY_XXHIGH:
            newUrl = replaceSuffix(url, PRODUCT_GALLERY_THUMB_XHDPI);
            break;
        }
        return newUrl;
    }

    /**
     * 根据屏幕密度匹配图库大图图片
     * @param url
     * @return
     */
    public static String getFitGallerySourceUrl(String url) {
        String newUrl = url;
        switch (currentDPI) {
        case DisplayMetrics.DENSITY_LOW:
            newUrl = replaceSuffix(url, PRODUCT_GALLERY_SOURCE_LDPI);
            break;
        case DisplayMetrics.DENSITY_MEDIUM:
            newUrl = replaceSuffix(url, PRODUCT_GALLERY_SOURCE_MDPI);
            break;
        case DisplayMetrics.DENSITY_HIGH:
            newUrl = replaceSuffix(url, PRODUCT_GALLERY_SOURCE_HDPI);
            break;
        case DisplayMetrics.DENSITY_XHIGH:
            newUrl = replaceSuffix(url, PRODUCT_GALLERY_SOURCE_XHDPI);
            break;
        case DisplayMetrics.DENSITY_XXHIGH:
            newUrl = replaceSuffix(url, PRODUCT_GALLERY_SOURCE_XHDPI);
            break;
        }
        return newUrl;
    }

    /**
     * 根据屏幕密度匹配首页活动广告图片的URL
     * 
     * @param url
     * @return
     */
    public static String getFitPageAdUrl(String url) {
        String newUrl = url;
        switch (currentDPI) {
        case DisplayMetrics.DENSITY_LOW:
            newUrl = replaceSuffix(url, HOMEPAGE_PAGE_AD_LDPI);
            break;
        case DisplayMetrics.DENSITY_MEDIUM:
            newUrl = replaceSuffix(url, HOMEPAGE_PAGE_AD_MDPI);
            break;
        case DisplayMetrics.DENSITY_HIGH:
            newUrl = replaceSuffix(url, HOMEPAGE_PAGE_AD_HDPI);
            break;
        case DisplayMetrics.DENSITY_XHIGH:
            newUrl = replaceSuffix(url, HOMEPAGE_PAGE_AD_XHDPI);
            break;
        case DisplayMetrics.DENSITY_XXHIGH:
            newUrl = replaceSuffix(url, HOMEPAGE_PAGE_AD_XHDPI);
            break;
        }
        return newUrl;
    }

    public static String replaceSuffix(String srcUrl, String newSuffix) {
        if (srcUrl == null || srcUrl.length() == 0) {
            return srcUrl;
        }
        int start = srcUrl.lastIndexOf("_");
        int end = srcUrl.lastIndexOf(".");
        if (start < 0 || end < 0 || start > end) {
            return srcUrl;
        }
        String tag = srcUrl.substring(start, end + 1);
        return srcUrl.replace(tag, newSuffix);
    }

    /**
     * 根据屏幕密度匹配预售图片
     * @param url
     * @return
     */
    public static String getFitImageForPreSell(String url) {
        String newUrl = url;
        switch (currentDPI) {
        case DisplayMetrics.DENSITY_LOW:
            newUrl = replaceSuffix(url, PRESELL_IMG_LDPI);
            break;
        case DisplayMetrics.DENSITY_MEDIUM:
            newUrl = replaceSuffix(url, PRESELL_IMG_MDPI);
            break;
        case DisplayMetrics.DENSITY_HIGH:
            newUrl = replaceSuffix(url, PRESELL_IMG_HDPI);
            break;
        case DisplayMetrics.DENSITY_XHIGH:
            newUrl = replaceSuffix(url, PRESELL_IMG_XDPI);
            break;
        case DisplayMetrics.DENSITY_XXHIGH:
            newUrl = replaceSuffix(url, PRESELL_IMG_XDPI);
            break;
        }
        return newUrl;
    }

    /**
     * 根据屏幕大小匹配开启应用图片
     * @param url
     * @return
     */
    public static String getFitImageForLogo(String url) {
        String newUrl = url;
        switch (currentDPI) {
        case DisplayMetrics.DENSITY_LOW:
            newUrl = replaceSuffix(url, LOGO_IMG_LDPI);
            break;
        case DisplayMetrics.DENSITY_MEDIUM:
            newUrl = replaceSuffix(url, LOGO_IMG_LDPI);
            break;
        case DisplayMetrics.DENSITY_HIGH:
            newUrl = replaceSuffix(url, LOGO_IMG_MDPI);
            break;
        case DisplayMetrics.DENSITY_XHIGH:
            newUrl = replaceSuffix(url, LOGO_IMG_HDPI);
            break;
        case DisplayMetrics.DENSITY_XXHIGH:
            newUrl = replaceSuffix(url, LOGO_IMG_HDPI);
            break;
        }
        return newUrl;
    }
    
    /**热门促销viewPager大图
     * @param url
     * @return
     */
    public static String getFitHotPromPagerUrl(String url){
        String newUrl = url;
        
        switch (currentDPI) {
        case DisplayMetrics.DENSITY_LOW:
            newUrl = replaceSuffix(url, PRODUCT_GALLERY_SOURCE_LDPI);
            break;
        case DisplayMetrics.DENSITY_MEDIUM:
            newUrl = replaceSuffix(url, PRODUCT_GALLERY_SOURCE_MDPI);
            break;
        case DisplayMetrics.DENSITY_HIGH:
            newUrl = replaceSuffix(url, PRODUCT_GALLERY_SOURCE_HDPI);
            break;
        case DisplayMetrics.DENSITY_XHIGH:
            newUrl = replaceSuffix(url, PRODUCT_GALLERY_SOURCE_XHDPI);
            break;
        case DisplayMetrics.DENSITY_XXHIGH:
            newUrl = replaceSuffix(url, PRODUCT_GALLERY_SOURCE_XHDPI);
            break;
        }
        
        return  newUrl ;
    }
    
    /**
     * 热门促销listview图片大小
     * @param url
     * @return
     */
    public static String getFitHotPromListUrl(String url){
        String newUrl = url;
        
        switch (currentDPI) {
        case DisplayMetrics.DENSITY_LOW:
            newUrl = replaceSuffix(url, PRODUCT_GALLERY_THUMB_LDPI);
            break;
        case DisplayMetrics.DENSITY_MEDIUM:
            newUrl = replaceSuffix(url, PRODUCT_GALLERY_THUMB_MDPI);
            break;
        case DisplayMetrics.DENSITY_HIGH:
            newUrl = replaceSuffix(url, PRODUCT_GALLERY_THUMB_HDPI);
            break;
        case DisplayMetrics.DENSITY_XHIGH:
            newUrl = replaceSuffix(url, PRODUCT_GALLERY_THUMB_XHDPI);
            break;
        case DisplayMetrics.DENSITY_XXHIGH:
            newUrl = replaceSuffix(url, PRODUCT_GALLERY_THUMB_XHDPI);
            break;
        }
        
        return newUrl ;
    }

}
