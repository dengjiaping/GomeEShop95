package com.gome.ecmall.util;

import java.io.Serializable;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间/日期--工具类
 */
public final class DateUtil implements Serializable {

	private static final long serialVersionUID = -3098985139095632110L;

	private DateUtil() {
	}
	
	/**
	 * 格式化时间【PromtionActivitiesAdapter】
	 * @param str
	 * @return
	 */
	public static String formatTimes(String str) {
		String result = "";
		if (str != null && str.length() > 10) {
			str = str.substring(0, 10);
			if (str.contains("-")) {
				String[] times = str.split("-");
				if (times == null || times.length <= 0) {
					return result;
				}
				for (int i = 1; i < times.length; i++) {
					if (i != (times.length - 1)) {
						result += times[i] + ".";
					} else {
						result += times[i];
					}
				}
			}
		}
		return result;
	}

	/**
	 * 日期格式化
	 * @param sdate
	 * @param format
	 * @return
	 */
	public static String dateFormat(String sdate, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		java.sql.Date date = java.sql.Date.valueOf(sdate);
		String dateString = formatter.format(date);

		return dateString;
	}

	/**
	 * 获取格式化的日期对象
	 * @param sDate
	 * @param dateFormat
	 * @return
	 */
	public static Date getDate(String sDate, String dateFormat) {
		SimpleDateFormat fmt = new SimpleDateFormat(dateFormat);
		ParsePosition pos = new ParsePosition(0);

		return fmt.parse(sDate, pos);
	}

	/**
	 * 获取当前时间【年】
	 * @return
	 */
	public static String getCurrentYear() {
		return getFormatCurrentTime("yyyy");
	}

	/**
	 * 获取当前时间【月】
	 * @return
	 */
	public static String getCurrentMonth() {
		return getFormatCurrentTime("MM");
	}

	/**
	 * 获取当前时间【日】
	 * @return
	 */
	public static String getCurrentDay() {
		return getFormatCurrentTime("dd");
	}

	/**
	 * 获取当前日期
	 * @return
	 */
	public static String getCurrentDate() {
		return getFormatDateTime(new Date(), "yyyy-MM-dd");
	}

	/**
	 * 获取当前详细时间
	 * @return
	 */
	public static String getCurrentDateTime() {
		return getFormatDateTime(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 格式化日期
	 * @param date
	 * @return
	 */
	public static String getFormatDate(Date date) {
		return getFormatDateTime(date, "yyyy-MM-dd");
	}

	/**
	 * 格式化当前日期
	 * @param format
	 * @return
	 */
	public static String getFormatDate(String format) {
		return getFormatDateTime(new Date(), format);
	}

	/**
	 * 获取当前详细时间
	 * @return
	 */
	public static String getCurrentTime() {
		return getFormatDateTime(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 格式化Date的详细时间
	 * @param date
	 * @return
	 */
	public static String getFormatTime(Date date) {
		return getFormatDateTime(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 格式化Date的详细日期
	 * @param date
	 * @return
	 */
	public static String getFormatShortTime(Date date) {
		return getFormatDateTime(date, "yyyy-MM-dd");
	}

	/**
	 * 格式化当前日期
	 * @param format
	 * @return
	 */
	public static String getFormatCurrentTime(String format) {
		return getFormatDateTime(new Date(), format);
	}

	/**
	 * 格式化时间
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getFormatDateTime(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

}