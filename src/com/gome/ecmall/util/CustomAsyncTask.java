package com.gome.ecmall.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

import com.gome.ecmall.custom.LoadingDialog;
import com.gome.eshopnew.R;

/**
 * 封装异步栈
 * @author qiudongchao
 *
 */
public class CustomAsyncTask extends AsyncTask<Void, Void, Object> {

	/**
	 * 上下文context
	 */
	private Context mContext;
	/**
	 * 回调接口
	 */
	private ApiListener mListener;
	/**
	 * 是否显示进度框
	 */
	private boolean isShowProgress;

	/**
	 * 进度框
	 */
	private LoadingDialog progressDialog;

	/**
	 * 网络异常
	 */
	public static final int NO_NETWORK = 404;
	/**
	 * 业务异常
	 */
	public static final int BUSSINESS_ERROR = 300;  

	public CustomAsyncTask(Context context, ApiListener listener,
			boolean showProgress) {
		mContext = context;
		mListener = listener;
		isShowProgress = showProgress;
	}

	@Override
	protected void onPreExecute() {
		if (isShowProgress) {
			progressDialog = CommonUtility.showLoadingDialog(mContext,
					mContext.getString(R.string.loading), true,
					new OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							cancel(true);
						}
					});
		}
	}

	@Override
	protected Object doInBackground(Void... params) {
		if (!NetUtility.isNetworkAvailable(mContext)) {
			return NO_NETWORK;
		}
		return mListener.onDoing();
	}

	@Override
	protected void onPostExecute(Object response) {
		if (isShowProgress) {
			progressDialog.dismiss();
		}
		if(isCancelled()){
			return;
		} 
		if (mListener == null) {
			return;
		}
		if (mContext instanceof Activity && ((Activity) mContext).isFinishing()) {
			return;
		}
		if (response == null) {
			mListener.onError(BUSSINESS_ERROR);
			return;
		} else if (response instanceof Integer) {

			Integer statusCode = (Integer) response;
			if (!handleCommonError(statusCode)) {
				mListener.onError((Integer) response);
				
				return;
			}
		}
		mListener.onSuccess(response);
	}

	/**
	 * 异常码过滤
	 * @param statusCode
	 * @return
	 */
	private boolean handleCommonError(int statusCode) {

		if (statusCode == 200) {
			return true;
		}
		return false;
	}

	/**
	 *回调接口
	 */
	public interface ApiListener {

		Object onDoing();

		void onSuccess(Object obj);

		void onError(int statusCode);
	}
}
