package com.gome.ecmall.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.gome.ecmall.bean.AppInfo;
import com.gome.eshopnew.R;

/**
 * 桌面快捷方式--工具类
 */
public class Tools {
    /**
     * 创建桌面快捷方式
     * 
     * @param context
     * @param pkg
     *            包名
     * @return
     */
    public static boolean addShortCut(Activity context, String pkg) {
        // 快捷方式名称
        String name = "unknown";
        String mainAct = null;
        // 快捷图标ID
        int iconIdentifier = -1;
        PackageManager pkManager = context.getPackageManager();
        // 创建Intent，用来Activity的查询
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 得到Activity的list
        List<ResolveInfo> list = pkManager.queryIntentActivities(intent, PackageManager.GET_ACTIVITIES);
        for (ResolveInfo info : list) {
            // 和传入的pkg一致时，进行处理
            if (TextUtils.equals(info.activityInfo.packageName, pkg)) {
                // 得到应用名称，做为快捷名
                name = info.loadLabel(pkManager).toString();
                // 得到应用图标
                iconIdentifier = info.activityInfo.applicationInfo.icon;
                // 得到该应用入口类的全类名
                mainAct = info.activityInfo.name;
                break;
            }
        }

        if (TextUtils.isEmpty(mainAct)) {
            return false;
        }

        Intent intents = new Intent(context, com.gome.ecmall.home.LaunchActivity.class);
        // Intent intents = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        // 图标和应用绑定，以便卸载时图标同时卸载
        intents.setAction(Intent.ACTION_MAIN);
        intents.addCategory(Intent.CATEGORY_LAUNCHER);
        // 这俩个flags必须添加
        intents.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intents.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);

        // 创建快捷方式的Intent
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        // 不允许重复创建快捷方式
        shortcut.putExtra("duplicate", false);
        // ComponentName comp = new ComponentName(pkg, mainAct);

        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intents);

        // shortcut.putExtra(
        // Intent.EXTRA_SHORTCUT_INTENT,
        // new Intent(Intent.ACTION_MAIN).setComponent(comp).setAction(Intent.ACTION_MAIN)
        // .addCategory(Intent.CATEGORY_LAUNCHER));

        Context pkgContext = null;
        if (TextUtils.equals(pkg, context.getPackageName())) {
            pkgContext = context;
        } else {
            try {
                // 利用对应的PKG名称，构建Context
                pkgContext = context.createPackageContext(pkg, Context.CONTEXT_IGNORE_SECURITY
                        | Context.CONTEXT_INCLUDE_CODE);
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        // 添加快捷方式的图标
        if (pkgContext != null) {
            ShortcutIconResource iconResource = Intent.ShortcutIconResource.fromContext(pkgContext, iconIdentifier);
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);
        }

        // 发送创建快捷方式的广播
        // 需要在AndroidManifest.xml添加创建快捷方式的权限
        // <uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
        context.sendBroadcast(shortcut);
        return true;
    }

    /**
     * 判断桌面是否已添.加快捷方式
     * 
     * @param cx
     * @param titleName
     *            快捷方式名称
     * @return
     */
    public static boolean hasShortcut(Activity mActivity) {
        boolean result = false;
        // 获取当前应用名称
        String title = null;
        try {
            final PackageManager pm = mActivity.getPackageManager();
            title = pm.getApplicationLabel(
                    pm.getApplicationInfo(mActivity.getPackageName(), PackageManager.GET_META_DATA)).toString();
        } catch (Exception e) {
        }

        final String uriStr;
        if (android.os.Build.VERSION.SDK_INT < 8) {
            uriStr = "content://com.android.launcher.settings/favorites?notify=true";
        } else {
            uriStr = "content://com.android.launcher2.settings/favorites?notify=true";
        }
        final Uri CONTENT_URI = Uri.parse(uriStr);
        final Cursor c = mActivity.getContentResolver().query(CONTENT_URI, null, "title=?", new String[] { title },
                null);
        if (c != null && c.getCount() > 0) {
            result = true;
        }
        return result;
    }

    public static void delShortcut(Activity mActivity) {

        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");

        // 快捷方式的名称

        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, mActivity.getString(R.string.app_name));

        Intent intents = new Intent(mActivity, com.gome.ecmall.home.LaunchActivity.class);
        // Intent intents = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        // 图标和应用绑定，以便卸载时图标同时卸载
        intents.setAction(Intent.ACTION_MAIN);
        intents.addCategory(Intent.CATEGORY_LAUNCHER);
        // 这俩个flags必须添加
        intents.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intents.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);

        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intents);

        mActivity.sendBroadcast(shortcut);

        Intent delete = mActivity.getPackageManager().getLaunchIntentForPackage(mActivity.getPackageName());

        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, delete);

        mActivity.sendBroadcast(shortcut);

    }

    /**
     * 获取有分享功能的应用信息 该方法过滤了蓝牙和人人 人人分享功能有bug
     * 
     * @return
     */
    public static List<AppInfo> getShareAppList(Activity mActivity, boolean isSDCardFlag, boolean isShareImage) {
        List<AppInfo> shareAppInfos = new ArrayList<AppInfo>();
        PackageManager packageManager = mActivity.getPackageManager();
        List<ResolveInfo> resolveInfos = getResolveInfoList(mActivity, isSDCardFlag, isShareImage);
        if (null == resolveInfos) {
            return null;
        } else {
            for (int i = resolveInfos.size(); i > 0; i--) {
                ResolveInfo resolveInfo = resolveInfos.get(i - 1);
                if (resolveInfo.activityInfo.packageName.equals("com.android.bluetooth")
                        || resolveInfo.activityInfo.packageName.equals("com.renren.mobile.android"))
                    continue;
                AppInfo appInfo = new AppInfo();
                appInfo.setAppPkgName(resolveInfo.activityInfo.packageName);
                appInfo.setAppLauncherClassName(resolveInfo.activityInfo.name);
                appInfo.setAppName(resolveInfo.loadLabel(packageManager).toString());
                appInfo.setAppIcon(resolveInfo.loadIcon(packageManager));
                if (resolveInfo.activityInfo.packageName.equals("com.sina.weibo")) {
                    shareAppInfos.add(0, appInfo);
                } else if (resolveInfo.activityInfo.packageName.equals("com.tencent.WBlog")) {
                    shareAppInfos.add(0, appInfo);
                } else {
                    shareAppInfos.add(appInfo);
                }

            }
        }
        return shareAppInfos;
    }

    /**
     * 获取提供分享功能的应用信息 根据是否有sdcard,是否有图片 确定分享类型，一种为图片文本型，一种为文本型
     * 
     * @param ctx
     * @return
     */
    private static List<ResolveInfo> getResolveInfoList(Activity mActivity, boolean isSDCardFlag, boolean isShareImage) {

        List<ResolveInfo> mApps = new ArrayList<ResolveInfo>();
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        if (isSDCardFlag && isShareImage) {
            intent.setType("image/*");
        } else {
            intent.setType("text/plain");
        }

        PackageManager pManager = mActivity.getPackageManager();
        mApps = pManager.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        return mApps;

    }

    /**
     * 将半角全部转化为全角
     * 
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**   
     * 去除特殊字符或将所有中文标号替换为英文标号   
     *    
     * @param str   
     * @return   
     */   
    public static String stringFilter(String str) {    
        str = str.replaceAll("【", "[").replaceAll("】", "]")    
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号    
        String regEx = "[『』]"; // 清除掉特殊字符    
        Pattern p = Pattern.compile(regEx);    
        Matcher m = p.matcher(str);    
        return m.replaceAll("").trim();    
    }  
    
}
