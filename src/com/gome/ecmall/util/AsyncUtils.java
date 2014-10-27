package com.gome.ecmall.util;

import android.content.Context;

import com.gome.ecmall.util.CustomAsyncTask.ApiListener;
import com.gome.eshopnew.R;

/**
 * 异步操作工具类
 * 
 * @author qiudongchao
 * 
 */
public class AsyncUtils {

	/**
	 * 请求异步操作
	 * 
	 * @param context
	 * @param listener
	 * @param showProgress
	 * @return
	 */
	public static CustomAsyncTask execAsync(Context context,
			ApiListener listener, boolean showProgress) {
		if (!NetUtility.isNetworkAvailable(context)) {
			CommonUtility
					.showMiddleToast(
							context,
							"",
							context.getString(R.string.can_not_conntect_network_please_check_network_settings));
			return null;
		}
		CustomAsyncTask task = new CustomAsyncTask(context, listener,
				showProgress);
		task.execute();
		return task;
	}

	/**
	 * 通用异常提示方法
	 * @param context
	 * @param statusCode
	 */
	public static void handErrorCode(Context context, int statusCode) {
		String errorMessage = "";
		switch (statusCode) {
		case CustomAsyncTask.BUSSINESS_ERROR:
			errorMessage = "业务异常";
			break;
		case CustomAsyncTask.NO_NETWORK:
			errorMessage = context
					.getString(R.string.can_not_conntect_network_please_check_network_settings);
			break;
		}
		CommonUtility.showMiddleToast(context, "", errorMessage);
	}
}
