package com.gome.ecmall.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式-工具类
 */
public class RegexUtils {

    /**
     * 检查收货人输入格式
     * @param cons_name_data 收货人
     * @return
     */
    public static boolean isConsNameYes(String cons_name_data) {
        String strPattern = "^(?!·)(?!.*?·$)(?!•)(?!.*?•$)[A-Za-z•·\u4e00-\u9fa5]+$";
        return isBase(cons_name_data, strPattern);
    }

    /**
     * 检查电话号码(固话+手机号)
     * @param mobile_numer 电话号码
     * @return
     */
    public static boolean isPhone(String mobile_numer) {
        String strPattern = "^(0(10|2\\d|[3-9]\\d\\d)[- ]{0,3}\\d{7,8}|0?1[3584]\\d{9})$";
        return isBase(mobile_numer, strPattern);
    }

    /**
     * 检查邮箱
     * @param strEmail 邮箱地址
     * @return
     */
    public static boolean isEmail(String strEmail) {
        String strPattern = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        return isBase(strEmail, strPattern);
    }
    
    /**
     * 基础正则表达式
     * @param message
     * @param pattern
     * @return
     */
    private static boolean isBase(String message,String pattern){
    	 Pattern p = Pattern.compile(pattern);
         Matcher m = p.matcher(message);
         return m.matches();
    }

}
