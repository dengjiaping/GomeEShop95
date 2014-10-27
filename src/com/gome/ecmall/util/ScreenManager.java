package com.gome.ecmall.util;

import java.util.Stack;

import android.app.Activity;

/**
 * activity管理
 * 
 */
public class ScreenManager {
    // activity栈
    private static Stack<Activity> activityStack;
    private static ScreenManager instance;

    private ScreenManager() {
    }

    public static ScreenManager getScreenManager() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    /**
     * 销毁最顶层的activity
     */
    public void popActivity() {
        Activity activity = activityStack.lastElement();
        if (activity != null) {
            activity.finish();
            activity = null;
            System.gc();
        }
    }

    /**
     * 销毁指定actiivty
     * 
     * @param activity
     */
    public void popActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 返回最顶层activity实例
     * 
     * @return
     */
    public Activity currentActivity() {
        Activity activity = null;
        if (activityStack != null && !activityStack.isEmpty()) {
            activity = activityStack.lastElement();
        }
        return activity;
    }

    /**
     * 将指定activity压入栈中
     * 
     * @param activity
     */
    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 除了指定activity其余的全部销毁
     * 
     * @param cls
     */
    public void popAllActivityExceptOne(Class<?> cls) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (cls != null && activity.getClass().equals(cls)) {
                break;
            }
            popActivity(activity);
        }
    }

    /**
     * 销毁指定Activity
     */
    public void popThisActivity(String name) {
        Activity activity = currentActivity();
        String className = activity.getClass().getSimpleName();
        if (className.equals(name)) {
            popActivity(activity);
        } else {
            return;
        }
    }

    /**
     * 销毁全部指定Activity
     * @param clsStr
     */
    public void popAllActivitys(String... clsStr) {
        int count = 0;
        int len = clsStr.length;
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            for (int i = 0; i < len; i++) {
                if (activity.getClass().getSimpleName().equals(clsStr[i])) {
                    popActivity(activity);
                    count++;
                }
            }
            if (count == len) {
                break;
            }
        }
    }
}
