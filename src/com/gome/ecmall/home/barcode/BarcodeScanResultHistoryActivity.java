package com.gome.ecmall.home.barcode;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gome.ecmall.bean.BarcodeScan.BarcodeHistory;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.dao.BarcodeScanHistoryDao;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.category.ProductShowActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.eshopnew.R;

/**
 * 条码购浏览历史
 */
public class BarcodeScanResultHistoryActivity extends AbsSubActivity implements
		OnClickListener, BarcodeScanResultHistoryAdapter.OnItemDeleteListener {

	public static final String barAction = "BarcodeScanResultHistoryActivity";
	private Button btnBack;
	private TextView tvTitle;
	private Button btnEdit, barcode_history_scpan_btn,
			barcode_history_input_btn;
	private ListView listView;
	private BarcodeScanResultHistoryAdapter adapter;
	private BarcodeScanHistoryDao historyDao;
	private LinearLayout empty;
	private LinearLayout history;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		historyDao = new BarcodeScanHistoryDao(this);
		setContentView(R.layout.barcode_scpan_history);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setData();
	}

	/**
	 * 初始化页面视图
	 */
	private void initView() {
		btnBack = (Button) findViewById(R.id.common_title_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setText(R.string.back);
		btnBack.setOnClickListener(this);
		tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
		tvTitle.setText(R.string.barcode_buy);
		tvTitle.setOnClickListener(this);
		btnEdit = (Button) findViewById(R.id.common_title_btn_right);
		btnEdit.setVisibility(View.VISIBLE);
		btnEdit.setText(R.string.edit);
		btnEdit.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.barcode_history_list);
		empty = (LinearLayout) findViewById(R.id.empty);
		history = (LinearLayout) findViewById(R.id.history);
		barcode_history_scpan_btn = (Button) findViewById(R.id.barcode_history_scpan_btn);
		barcode_history_scpan_btn.setOnClickListener(this);
		barcode_history_input_btn = (Button) findViewById(R.id.barcode_history_input_btn);
		barcode_history_input_btn.setOnClickListener(this);
		adapter = new BarcodeScanResultHistoryAdapter(
				BarcodeScanResultHistoryActivity.this);
	}

	/**
	 * 初始化页面数据
	 */
	private void setData() {
		new AsyncTask<Object, Void, ArrayList<BarcodeHistory>>() {

			LoadingDialog loadingDialog;

			protected void onPreExecute() {
				loadingDialog = CommonUtility.showLoadingDialog(
						BarcodeScanResultHistoryActivity.this, "正在读取记录...",
						true, new DialogInterface.OnCancelListener() {

							@Override
							public void onCancel(DialogInterface dialog) {
								cancel(true);
							}
						});
			};

			@Override
			protected ArrayList<BarcodeHistory> doInBackground(Object... params) {
				return historyDao.getAllBarcodeHistory();
			}

			protected void onPostExecute(ArrayList<BarcodeHistory> result) {
				loadingDialog.dismiss();
				bindData(result);
			}

		}.execute();
	}

	/**
	 * 绑定页面视图
	 * 
	 * @param result
	 */
	private void bindData(ArrayList<BarcodeHistory> result) {
		if (result == null) {
			CommonUtility.showToast(BarcodeScanResultHistoryActivity.this,
					"加载失败!");
			empty.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
			return;
		}
		if (result.size() > 0) {
			history.setVisibility(View.VISIBLE);
		} else {
			history.setVisibility(View.GONE);
		}
		listView.setAdapter(adapter);
		adapter.refresh(result);
		listView.setEmptyView(empty);

		adapter.setDeleteListener(BarcodeScanResultHistoryActivity.this);
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_title_btn_back:
			goback();
			break;
		case R.id.common_title_tv_text:
			setListViewSelectionFirst();
			break;
		case R.id.common_title_btn_right:
			setEditMode();
			break;
		case R.id.barcode_history_scpan_btn:
			setScanBarCode();
			break;
		case R.id.barcode_history_input_btn:
			setInputBarCode();
			break;
		default:
			break;
		}
	}

	/**
	 * 设置输入条码
	 */
	private void setInputBarCode() {
		Intent intent = new Intent(this, BarcodeInputStreamActivity.class);
		intent.setAction(barAction);
		startActivityForResult(intent, 0);
	}

	/**
	 * 设置扫描条码
	 */
	private void setScanBarCode() {
		Intent barcodeIntent = new Intent(this, CaptureActivity.class);
		this.startActivityForResult(barcodeIntent, 0);
	}

	/**
	 * 设置编辑状态
	 */
	private void setEditMode() {
		// 适配器有数据且数量大于零才可编辑
		if (adapter != null && adapter.getCount() > 0) {
			boolean isEditMode = !adapter.isEditMode();
			if (isEditMode) {
				btnEdit.setText(R.string.finish);
			} else {
				btnEdit.setText(R.string.edit);
			}
			adapter.setEditMode(isEditMode);
		}
	}

	/**
	 * 设置ListView跳到第一项
	 */
	private void setListViewSelectionFirst() {
		if (listView.getAdapter() != null) {
			listView.setSelection(0);
		}
	}

	@Override
	public void onItemDeleted(final int postion) {
		final BarcodeHistory barcodeHistory = adapter
				.getHistoryAtPosition(postion);

		new AsyncTask<Object, Void, Boolean>() {
			LoadingDialog loadingDialog = null;

			@Override
			protected void onPreExecute() {
				loadingDialog = CommonUtility.showLoadingDialog(
						BarcodeScanResultHistoryActivity.this, "正在删除...",
						false, new DialogInterface.OnCancelListener() {

							@Override
							public void onCancel(DialogInterface dialog) {
								cancel(true);
							}
						});
			}

			@Override
			protected Boolean doInBackground(Object... params) {
				return historyDao.deleteBarcodeHistory(barcodeHistory
						.getBarcodeId());
			}

			@Override
			protected void onPostExecute(Boolean result) {
				loadingDialog.dismiss();
				if (result) {
					adapter.deleteItem(postion);
					if (adapter.getCount() == 0) {
						adapter.setEditMode(false);
						btnEdit.setText(R.string.edit);
						if (adapter.getCount() > 0) {
							history.setVisibility(View.VISIBLE);
						} else {
							history.setVisibility(View.GONE);
						}
					}
				} else {
					CommonUtility.showToast(
							BarcodeScanResultHistoryActivity.this, "删除失败");
				}
			}

		}.execute();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case 1: {
			if (data != null) {
				Intent intent = new Intent(
						BarcodeScanResultHistoryActivity.this,
						CaptureActivity.class);
				intent.setAction(data.getAction());
				if ("ProductShowActivity".equals(data.getAction())) {
					intent.putExtra("goodsNo", data.getStringExtra("goodsNo"));
					intent.putExtra("skuId", data.getStringExtra("skuId"));
				} else if ("BarcodeScanReusltListActivity".equals(data
						.getAction())) {
					intent.putExtra("BarCodeGoodsResult",
							data.getSerializableExtra("BarCodeGoodsResult"));
				}
				setResult(0, intent);
			}
		}
			break;
		case 2: {
			if (data != null) {
				if ("ProductShowActivity".equals(data.getAction())) {
					Intent productShowIntent = new Intent(
							BarcodeScanResultHistoryActivity.this,
							ProductShowActivity.class);
					String goodsNo = data.getStringExtra("goodsNo");
					String skuId = data.getStringExtra("skuId");
					String imgPath = data.getStringExtra("imgPath");
					productShowIntent.putExtra(
							ProductShowActivity.INTENT_KEY_GOODS_NO, goodsNo);
					productShowIntent.putExtra(
							ProductShowActivity.INTENT_KEY_SKU_ID, skuId);
					productShowIntent.putExtra("imgPath", imgPath);
					startActivity(productShowIntent);
				} else if ("BarcodeScanReusltListActivity".equals(data
						.getAction())) {
					Intent barcodeResultIntent = new Intent(
							BarcodeScanResultHistoryActivity.this,
							BarcodeScanReusltListActivity.class);
					barcodeResultIntent
							.putExtra(
									BarcodeScanReusltListActivity.BARCODEGOODSRESULT,
									data.getSerializableExtra(BarcodeScanReusltListActivity.BARCODEGOODSRESULT));
					startActivity(barcodeResultIntent);
				}
			}
		}
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		adapter.clear();
		adapter = null;
		historyDao = null;
	}
}
