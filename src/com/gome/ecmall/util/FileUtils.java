package com.gome.ecmall.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.content.Context;
import android.webkit.URLUtil;

import com.gome.eshopnew.R;

/**
 * 文件操作/时间格式化--工具类
 */
public class FileUtils {

    /**
     * 读取文件内容  未使用
     * @param fileName
     * @return
     */
    public static String read(String fileName) {
        InputStream is = null;
        String result = null;
        try {
            is = new FileInputStream(fileName);
            int len = is.available();
            byte[] buf = new byte[len];
            is.read(buf);
            String str = new String(buf);
            result = str;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 将毫秒转成 时 分 秒
     * @param i 毫秒数
     * @return 时 分 秒
     */
    public static String secToTime(long i) {
        String retStr = null;
        long hour = 0;
        long minute = 0;
        long second = 0;
        if (i <= 0)
            return "00:00:00";
        else {
            minute = i / 60;
            if (minute < 60) {
                second = i % 60;
                retStr = unitFormat(0) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                // if (hour > 99)
                // return "99:59:59";
                minute = minute % 60;
                second = i - hour * 3600 - minute * 60;
                retStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return retStr;
    }

    /**
     * 将毫秒转成 时 分 秒
     * @param i
     * @return
     */
    public static String limitSecToTime(long i) {
        String retStr = null;
        long hour = 0;
        long minute = 0;
        long second = 0;
        if (i <= 0)
            return "00:00:00";
        else {
            minute = i / 60;
            if (minute < 60) {
                second = i % 60;
                retStr = unitFormat(0) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                minute = minute % 60;
                second = i - hour * 3600 - minute * 60;
                if (hour > 99)
                    hour = 99;
                retStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return retStr;
    }

    /**
     * 将毫秒数转换为   天   时    分   秒
     * @param i
     * @return
     */
    public static String secToTimeWithDay(long i) {
        String retStr = null;
        long day = 0;
        long hour = 0;
        long minute = 0;
        long second = 0;
        if (i <= 0)
            return "00:00:00:00";
        else {
            minute = i / 60;
            if (minute < 60) {
                second = i % 60;
                retStr = unitFormat(0) + ":" + unitFormat(0) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 24) {
                    day = hour / 24;
                    hour = hour % 24;
                }
                // if (day > 99)
                // return "99:23:59:59";
                minute = minute % 60;
                second = i - day * (24 * 3600) - hour * 3600 - minute * 60;
                retStr = unitFormat(day) + ":" + unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return retStr;
    }

    /**
     * 将数字占两位数表示 09:09:09
     * @param i
     * @return
     */
    private static String unitFormat(long i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Long.toString(i);
        else
            retStr = Long.toString(i);
        return retStr;
    }

    /**
     * 数字格式化
     * @param num
     * @return
     */
    public static String formatDouble(double num) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        return format.format(num);
    }

    /**
     * 格式化价格数字
     * @param ctx
     * @param str
     * @return
     */
    public static String getFormatAmount(Context ctx, String str) {
        String yuan = ctx.getString(R.string.yuan_sign);
        if (str == null || str.length() == 0) {
            return yuan + "0.00";
        } else {
            return yuan + str;
        }
    }

    /**
     * 未使用
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
     * 替换中文标号 清除特殊字符    未使用
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String StringFilter(String str) throws PatternSyntaxException {
        str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static SimpleDateFormat hm = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat md = new SimpleDateFormat("MM-dd HH:mm");
    public static SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
    
    /**
     * 首页下拉刷新显示 日期
     * @param time
     * @return
     */
    public static String getBlogTimestampFormat(long time) {
        Long publishTime = Long.parseLong(time + "");
        Long lon = System.currentTimeMillis() - publishTime;
        Date currentDay = new Date();
        long minutes = lon / (1000 * 60);
        if (minutes <= 0) {
            return "刚刚";
        } else if (minutes <= 59) {
            return minutes + "分钟前";
        } else {
            if (minutes < currentDay.getHours() * 60 + currentDay.getMinutes()) {
                return "今天" + hm.format(new Date(publishTime));
            } else if (minutes < (24 + currentDay.getHours()) * 60 + currentDay.getMinutes()) {
                return "昨天" + hm.format(new Date(publishTime));
            } else if (minutes < (24 * 365) * 60 + currentDay.getMinutes()) {
                return md.format(new Date(publishTime)) + "";
            } else {
                return ymd.format(new Date(publishTime)) + "";
            }
        }
    }

    /**
     * 计算缓存的索引key,此key用来確定Cache的唯一性
     * 
     * @param filePath
     * @return
     */
    public static String getCacheKey(String filePath) {
        String keyPath = filePath;
        try {
            if (URLUtil.isHttpUrl(filePath)) {
                URL url = new URL(filePath);
                keyPath = url.getPath();// path部分不包含地址重写的参数，可通过url.getQuery()获得
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return getDigestCode(keyPath);
    }

    /**
     * Md5
     * @param msg
     * @return 
     */
    public static String getDigestCode(String msg) {
        String digest = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(msg.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            digest = buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return digest;
    }

    /**
     * 复制文件
     * @param fromFileUrl 源文件路径
     * @param toFileUrl 目标文件路径
     * @return
     */
    public static boolean copyFile(String fromFileUrl, String toFileUrl) {
        File fromFile = null;
        File toFile = null;
        try {
            fromFile = new File(fromFileUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        try {
            toFile = new File(toFileUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // if (!fromFile.exists()) {
        // return false ;
        // }
        //
        // if (! fromFile.isFile()) {
        // return false ;
        // }
        //
        // if (!fromFile.canRead()) {
        // return false ;
        // }
        //
        // if (!toFile.getParentFile().exists()) {
        // toFile.getParentFile().mkdirs() ;
        // }
        //
        if (toFile.exists()) {
            toFile.delete();
        }

        try {
            FileInputStream fisfrom = new FileInputStream(fromFile);
            BufferedInputStream bisfrom = new BufferedInputStream(fisfrom);
            FileOutputStream fosto = new FileOutputStream(toFile);
            BufferedOutputStream bosto = new BufferedOutputStream(fosto);
            byte[] bt = new byte[5 * 1024];
            int index;
            while ((index = bisfrom.read(bt)) != -1) {
                bosto.write(bt, 0, index);
            }
            bosto.flush();

            fisfrom.close();
            bisfrom.close();
            fosto.close();
            bosto.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
