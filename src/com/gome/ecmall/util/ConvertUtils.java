package com.gome.ecmall.util;

import android.content.Context;

/**
 * 数据转换工具类
 */
public class ConvertUtils {
	
	/**
	 * dip 转换为 px
	 * @param dipValue
	 * @param context
	 * @return
	 */
	public static int dip2px(float dipValue,Context context) {
		float density = MobileDeviceUtil.getInstance(context.getApplicationContext()).getScreenDensity();
		return (int) (dipValue * density + 0.5f);
	}
}
