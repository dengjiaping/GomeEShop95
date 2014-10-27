package com.gome.ecmall.push;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.gome.ecmall.util.BDebug;

/**
 * 管理闹钟的类
 * 
 * @author liuyang-ds
 * 
 */
public class MalarmManagers {
    public static final int DATE_MODE_FIX = 0;
    public static final int DATE_MODE_DAY = 1;
    public static final int DATE_MODE_WEEK = 2;
    public static final int DATE_MODE_MONTH = 3;
    public static final int DATE_MODE_YEAR = 4;

    /**
     * 设置闹钟
     * 
     * @param context
     *            setOrCancel:1:取消闹钟，0：设置闹钟
     */
    public static void AlarmManagers(Context context, int setOrCancel) {
        try {
            Intent intent = new Intent(context, PushBroadCastReceiver.class);
            intent.setAction("com.gome.ecmall.push.service.alarms.set");
            PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
            // 开始时间
            long firstime = SystemClock.elapsedRealtime();

            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);// 5秒一个周期，不停的发送广播
            if (setOrCancel == 1) {
                am.cancel(sender);
            } else if (setOrCancel == 0) {
                am.setRepeating(AlarmManager.RTC_WAKEUP, firstime, 60 * 1000 * 5, sender);
            }
        } catch (Exception e) {
            BDebug.d("liuyang", "设置闹钟或关闭闹钟失败了" + e.getMessage());
            e.printStackTrace();
        }
        // AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // Intent i = new Intent(context, PushBroadCastReceiver.class);
        // List<String> time24 = new ArrayList<String>();
        // time24.add("10:45:00");
        // time24.add("10:47:00");
        // time24.add("10:50:00");
        // time24.add("11:20:00");
        // time24.add("12:00:00");
        // time24.add("14:00:00");
        // time24.add("15:00:00");
        // time24.add("16:00:00");
        // time24.add("17:00:00");
        // time24.add("18:00:00");
        // time24.add("19:00:00");
        // time24.add("20:00:00");
        // for (int j = 0; j < time24.size(); j++) {
        // if (setOrCancel == 1) {
        // PendingIntent pi = PendingIntent.getBroadcast(context, j, i, 0);
        // mgr.cancel(pi);
        // } else {
        // i.setAction("com.gome.ecmall.push.service.alarms.set");
        // PendingIntent pi = PendingIntent.getBroadcast(context, j, i, 0);
        // mgr.set(AlarmManager.RTC_WAKEUP, getNextAlarmTime(DATE_MODE_WEEK, "1,2,3,4,5,6,7", time24.get(j)), pi);
        // }
        // }
    }

    /**
     * 闹钟三种设置模式（dateMode）： 1、DATE_MODE_FIX：指定日期，如20120301 , 参数dateValue格式：2012-03-01 2、DATE_MODE_WEEK：按星期提醒，如星期一、星期三 ,
     * 参数dateValue格式：1,3 3、DATE_MODE_MONTH：按月提醒，如3月2、3号，4月2、3号, 参数dateValue格式：3,4|2,3
     * 
     * startTime:为当天开始时间，如上午9点, 参数格式为09:00
     */
    public static long getNextAlarmTime(int dateMode, String dateValue, String startTime) {
        final SimpleDateFormat fmt = new SimpleDateFormat();
        final Calendar c = Calendar.getInstance();
        final long now = System.currentTimeMillis();

        // 设置开始时间
        try {
            if (DATE_MODE_FIX == dateMode) {
                fmt.applyPattern("yyyy-MM-dd");
                Date d = fmt.parse(dateValue);
                c.setTimeInMillis(d.getTime());
            }

            fmt.applyPattern("HH:mm:ss");
            Date d = fmt.parse(startTime);
            c.set(Calendar.HOUR_OF_DAY, d.getHours());
            c.set(Calendar.MINUTE, d.getMinutes());
            c.set(Calendar.SECOND, d.getSeconds());
            c.set(Calendar.MILLISECOND, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long nextTime = 0;
        if (DATE_MODE_FIX == dateMode) { // 按指定日期
            nextTime = c.getTimeInMillis();
            // 指定日期已过
            if (now >= nextTime)
                nextTime = 0;
        } else if (DATE_MODE_WEEK == dateMode) { // 按周
            final long[] checkedWeeks = parseDateWeeks(dateValue);
            if (null != checkedWeeks) {
                for (long week : checkedWeeks) {
                    c.set(Calendar.DAY_OF_WEEK, (int) (week + 1));

                    long triggerAtTime = c.getTimeInMillis();
                    if (triggerAtTime <= now) { // 下周
                        triggerAtTime += AlarmManager.INTERVAL_DAY * 7;
                    }
                    // 保存最近闹钟时间
                    if (0 == nextTime) {
                        nextTime = triggerAtTime;
                    } else {
                        nextTime = Math.min(triggerAtTime, nextTime);
                    }
                }
            }
        } else if (DATE_MODE_MONTH == dateMode) { // 按月
            final long[][] items = parseDateMonthsAndDays(dateValue);
            final long[] checkedMonths = items[0];
            final long[] checkedDays = items[1];

            if (null != checkedDays && null != checkedMonths) {
                boolean isAdd = false;
                for (long month : checkedMonths) {
                    c.set(Calendar.MONTH, (int) (month - 1));
                    for (long day : checkedDays) {
                        c.set(Calendar.DAY_OF_MONTH, (int) day);

                        long triggerAtTime = c.getTimeInMillis();
                        if (triggerAtTime <= now) { // 下一年
                            c.add(Calendar.YEAR, 1);
                            triggerAtTime = c.getTimeInMillis();
                            isAdd = true;
                        } else {
                            isAdd = false;
                        }
                        if (isAdd) {
                            c.add(Calendar.YEAR, -1);
                        }
                        // 保存最近闹钟时间 www.2cto.com
                        if (0 == nextTime) {
                            nextTime = triggerAtTime;
                        } else {
                            nextTime = Math.min(triggerAtTime, nextTime);
                        }
                    }
                }
            }
        }
        return nextTime;
    }

    public static long[] parseDateWeeks(String value) {
        long[] weeks = null;
        try {
            final String[] items = value.split(",");
            weeks = new long[items.length];
            int i = 0;
            for (String s : items) {
                weeks[i++] = Long.valueOf(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weeks;
    }

    public static long[][] parseDateMonthsAndDays(String value) {
        long[][] values = new long[2][];
        try {
            final String[] items = value.split("\\|");
            final String[] monthStrs = items[0].split(",");
            final String[] dayStrs = items[1].split(",");
            values[0] = new long[monthStrs.length];
            values[1] = new long[dayStrs.length];

            int i = 0;
            for (String s : monthStrs) {
                values[0][i++] = Long.valueOf(s);
            }
            i = 0;
            for (String s : dayStrs) {
                values[1][i++] = Long.valueOf(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }
}
