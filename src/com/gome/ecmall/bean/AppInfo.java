package com.gome.ecmall.bean;

import android.graphics.drawable.Drawable;

/**
 * [分享] 应用信息【分享功能】【单独类】
 */
public class AppInfo {

    private String appPkgName;
    private String appLauncherClassName;
    private String appName;
    private Drawable appIcon;

    public String getAppPkgName() {
        return appPkgName;
    }

    public void setAppPkgName(String appPkgName) {
        this.appPkgName = appPkgName;
    }

    public String getAppLauncherClassName() {
        return appLauncherClassName;
    }

    public void setAppLauncherClassName(String appLauncherClassName) {
        this.appLauncherClassName = appLauncherClassName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

}
