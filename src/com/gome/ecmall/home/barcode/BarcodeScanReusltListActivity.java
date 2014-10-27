package com.gome.ecmall.home.barcode;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gome.ecmall.bean.BarcodeScan.BarCodeGoods;
import com.gome.ecmall.bean.BarcodeScan.BarCodeGoodsResult;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.eshopnew.R;

/**
 * 二维码扫描结果页面
 */
public class BarcodeScanReusltListActivity extends AbsSubActivity implements
		OnClickListener {

	public static final String Tag = "BarcodeScanReusltListActivity:";
	public static final String BARCODE = "barCode";
	public static final String BARCODEGOODSRESULT = "BarCodeGoodsResult";
	private Button common_title_btn_back, common_title_btn_right;
	private TextView tvTitle, tvEmpty, no_result_text;
	private ImageView no_net_img;
	private ListView listView;
	private LinearLayout resultlinearlayout;
	private BarcodeScanReusltListAdapter barcodeSanResultListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.barcode_input_result_layout);
		BarCodeGoodsResult result = (BarCodeGoodsResult) getIntent()
				.getSerializableExtra(BARCODEGOODSRESULT);
		initView();
		initData(result);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_title_btn_back: {
			goback();
		}
			break;
		case R.id.common_title_btn_right: {

		}
			break;
		default:
			break;
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
		common_title_btn_right.setVisibility(View.INVISIBLE);
		common_title_btn_right.setOnClickListener(null);
		tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
		tvTitle.setText(R.string.barcode_result);
		listView = (ListView) findViewById(R.id.barcode_result_listview);
		no_net_img = (ImageView) findViewById(R.id.no_net_img);
		tvEmpty = (TextView) findViewById(android.R.id.empty);
		resultlinearlayout = (LinearLayout) findViewById(R.id.resultlinearlayout);
		no_result_text = (TextView) findViewById(R.id.no_result_text);
	}

	/**
	 * 初始化页面数据
	 * 
	 * @param result
	 */
	private void initData(BarCodeGoodsResult result) {
		if ("1".equals(result.getScanResultType())) {
			tvTitle.setText(getString(R.string.barcode_result) + "（"
					+ result.getBarCodeGoodsList().size() + "）");
			resultlinearlayout.setVisibility(View.GONE);
			bindListView(result);
		} else if ("2".equals(result.getScanResultType())) {
			tvTitle.setText(getString(R.string.barcode_result) + "（0）");
			resultlinearlayout.setVisibility(View.VISIBLE);
			no_result_text
					.setText(Html.fromHtml("<font color=\"#333333\">"
							+ getString(R.string.barcode_gome_no_result)
							+ "\"</font>"));
			bindListView(result);
		} else if ("3".equals(result.getScanResultType())) {
			tvTitle.setText(getString(R.string.barcode_result) + "（0）");
			resultlinearlayout.setVisibility(View.VISIBLE);
			no_result_text.setText(Html.fromHtml("<font color=\"#333333\">"
					+ String.format(getString(R.string.barcode_gome_no_rec),
							result.getBarCode()) + "</font>"));
			bindListView(result);
		}
	}

	/**
	 * 绑定ListView
	 * 
	 * @param result
	 */
	private void bindListView(BarCodeGoodsResult result) {
		if (result.getBarCodeGoodsList() == null) {
			result.setBarCodeGoodsList(new ArrayList<BarCodeGoods>());
		}
		if (barcodeSanResultListAdapter == null) {
			barcodeSanResultListAdapter = new BarcodeScanReusltListAdapter(
					BarcodeScanReusltListActivity.this);
			listView.setAdapter(barcodeSanResultListAdapter);
			barcodeSanResultListAdapter.appendToList(result
					.getBarCodeGoodsList());
			tvEmpty.setText("暂无相关商品");
			listView.setEmptyView(tvEmpty);
			common_title_btn_right
					.setOnClickListener(BarcodeScanReusltListActivity.this);
		} else {
			barcodeSanResultListAdapter.refresh(result.getBarCodeGoodsList());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		goback();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		barcodeSanResultListAdapter.clear();
		barcodeSanResultListAdapter = null;
	}
}
