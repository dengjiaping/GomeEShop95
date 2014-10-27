package com.gome.ecmall.home.barcode;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gome.ecmall.bean.BarcodeScan;
import com.gome.ecmall.bean.BarcodeScan.BarCodeGoodsResult;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.DaoUtils;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 二维码-输入页面
 */
public class BarcodeInputStreamActivity extends Activity implements
		OnClickListener {

	private Button common_title_btn_back, common_title_btn_right;
	private TextView tvTitle;
	private EditText barcode_input_edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.barcode_input_layout);
		initView();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_title_btn_back: {
			finish();
		}
			break;
		case R.id.common_title_btn_right: {
			requestBarCode();
		}
			break;
		default:
			break;
		}
	}

	/**
	 * 根据输入的条码，执行条码检索
	 */
	private void requestBarCode() {
		String barcode = barcode_input_edit.getText().toString();
		if (TextUtils.isEmpty(barcode)) {
			CommonUtility.showMiddleToast(BarcodeInputStreamActivity.this, "",
					getString(R.string.barcode_inputstream_barcode_error));
		} else {
			requestData(barcode);
		}
	}

	/**
	 * 初始化页面视图
	 */
	private void initView() {
		common_title_btn_back = (Button) findViewById(R.id.common_title_btn_back);
		common_title_btn_back.setText(R.string.back);
		common_title_btn_back.setVisibility(View.VISIBLE);
		common_title_btn_back.setOnClickListener(this);
		common_title_btn_right = (Button) findViewById(R.id.common_title_btn_right);
		common_title_btn_right.setText(R.string.shopping_cart_finish);
		common_title_btn_right.setVisibility(View.VISIBLE);
		common_title_btn_right.setOnClickListener(this);
		tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
		tvTitle.setText(R.string.barcode_inputstream_barcode_title);
		barcode_input_edit = (EditText) findViewById(R.id.barcode_input_edit);
	}

	/**
	 * 异步请求数据
	 * 
	 * @param barCode
	 *            条码
	 */
	private void requestData(final String barCode) {
		if (!NetUtility.isNetworkAvailable(BarcodeInputStreamActivity.this)) {
			CommonUtility
					.showMiddleToast(
							BarcodeInputStreamActivity.this,
							"",
							getString(R.string.can_not_conntect_network_please_check_network_settings));
			return;
		}
		new AsyncTask<Object, Void, BarCodeGoodsResult>() {
			LoadingDialog loadingDialog = null;

			@Override
			protected void onPreExecute() {
				loadingDialog = CommonUtility.showLoadingDialog(
						BarcodeInputStreamActivity.this,
						getString(R.string.loading), true,
						new DialogInterface.OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								cancel(true);
							}
						});
			}

			@Override
			protected BarCodeGoodsResult doInBackground(Object... params) {
				String request = BarcodeScan
						.createRequestBarCodeResultListJson(barCode);
				String response = NetUtility.sendHttpRequestByPost(
						Constants.URL_BARCODE_BARCODE, request);
				if (NetUtility.NO_CONN.equals(response)) {
					return null;
				}
				return BarcodeScan.parseBarCodeGoodsList(response, barCode);
			}

			@Override
			protected void onPostExecute(BarCodeGoodsResult result) {
				if (isCancelled()) {
					return;
				}
				loadingDialog.dismiss();
				bindData(barCode, result);
			}
		}.execute();
	}

	/**
	 * 数据返回处理
	 * 
	 * @param barCode
	 * @param result
	 */
	private void bindData(final String barCode, BarCodeGoodsResult result) {
		if (result == null) {
			if (TextUtils.isEmpty(BarcodeScan.getErrorMessage())) {
				CommonUtility.showMiddleToast(BarcodeInputStreamActivity.this,
						"", getString(R.string.data_load_fail_exception));
			} else {
				CommonUtility.showMiddleToast(BarcodeInputStreamActivity.this,
						"", BarcodeScan.getErrorMessage());
			}
			return;
		}

		if (result.getBarCodeGoodsList() != null
				&& result.getBarCodeGoodsList().size() == 1
				&& "1".equals(result.getScanResultType())) {
			String imgurl = result.getBarCodeGoodsList().get(0)
					.getSkuThumbImgUrl();
			DaoUtils.with(this).recordBarcodeHistory(result, barCode, imgurl,true);
			if (BarcodeScanResultHistoryActivity.barAction.equals(getIntent()
					.getAction())) {
				Intent intent = new Intent(BarcodeInputStreamActivity.this,
						BarcodeScanResultHistoryActivity.class);
				intent.setAction("ProductShowActivity");
				intent.putExtra("goodsNo", result.getBarCodeGoodsList().get(0)
						.getGoodsNo());
				intent.putExtra("skuId", result.getBarCodeGoodsList().get(0)
						.getSkuID());
				setResult(2, intent);
				finish();
			} else if (CaptureActivity.capAction
					.equals(getIntent().getAction())) {
				Intent intent = new Intent(BarcodeInputStreamActivity.this,
						CaptureActivity.class);
				intent.setAction("ProductShowActivity");
				intent.putExtra("goodsNo", result.getBarCodeGoodsList().get(0)
						.getGoodsNo());
				intent.putExtra("skuId", result.getBarCodeGoodsList().get(0)
						.getSkuID());
				setResult(0, intent);
				finish();
			}
		} else {
			String imgurl = "";
			DaoUtils.with(this).recordBarcodeHistory(result, barCode, imgurl,true);
			if (BarcodeScanResultHistoryActivity.barAction.equals(getIntent()
					.getAction())) {
				Intent intent = new Intent(BarcodeInputStreamActivity.this,
						BarcodeScanResultHistoryActivity.class);
				intent.setAction("BarcodeScanReusltListActivity");
				intent.putExtra("BarCodeGoodsResult", result);
				setResult(2, intent);
				finish();
			} else if (CaptureActivity.capAction
					.equals(getIntent().getAction())) {
				Intent intent = new Intent(BarcodeInputStreamActivity.this,
						CaptureActivity.class);
				intent.setAction("BarcodeScanReusltListActivity");
				intent.putExtra("BarCodeGoodsResult", result);
				setResult(0, intent);
				finish();
			}
		}
	}

}
