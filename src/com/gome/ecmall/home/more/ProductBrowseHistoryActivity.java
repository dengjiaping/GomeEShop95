package com.gome.ecmall.home.more;

import java.util.ArrayList;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gome.ecmall.bean.Product;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.dao.ProductHistoryDao;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.util.AppMeasurementUtils;
import com.gome.ecmall.util.CommonUtility;
import com.gome.eshopnew.R;

/**
 * 商品浏览历史
 */
public class ProductBrowseHistoryActivity extends AbsSubActivity implements
		OnClickListener, ProductHistoryListAdapter.OnItemDeleteListener {

	private Button btnBack;
	private TextView tvTitle;
	private Button btnEdit;
	private TextView tvEmpty;
	private View lineFooter;
	private TextView ivClear;

	private LayoutInflater mInflater;
	private ListView mListView;
	private ProductHistoryListAdapter mAdapter;
	private ProductHistoryDao mHistoryDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHistoryDao = new ProductHistoryDao(this);
		setContentView(R.layout.more_product_browse_history);
		initView();
		initData();
		AppMeasurementUtils appMeasurementProm = new AppMeasurementUtils(this);
		appMeasurementProm.getUrl(getString(R.string.more) + ":"
				+ getString(R.string.appMeas_history),
				getString(R.string.more), getString(R.string.more) + ":"
						+ getString(R.string.appMeas_history),
				getString(R.string.more), getString(R.string.more), "", "", "",
				"", "", "", "", "", "", "", "", null);
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
		tvTitle.setText(R.string.browser_history);
		tvTitle.setOnClickListener(this);
		btnEdit = (Button) findViewById(R.id.common_title_btn_right);
		btnEdit.setVisibility(View.INVISIBLE);
		btnEdit.setText(R.string.edit);
		btnEdit.setOnClickListener(null);

		mListView = (ListView) findViewById(R.id.more_product_history_list);
		tvEmpty = (TextView) findViewById(android.R.id.empty);
		tvEmpty.setText(R.string.you_have_no_product_browse_history);
		mInflater = (LayoutInflater) this
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * 初始化页面数据
	 */
	private void initData() {
		new AsyncTask<Object, Void, ArrayList<Product>>() {

			LoadingDialog loadingDialog;

			protected void onPreExecute() {
				loadingDialog = CommonUtility.showLoadingDialog(
						ProductBrowseHistoryActivity.this, "正在读取记录...", true,
						new DialogInterface.OnCancelListener() {

							@Override
							public void onCancel(DialogInterface dialog) {
								cancel(true);
							}
						});
			};

			@Override
			protected ArrayList<Product> doInBackground(Object... params) {
				return mHistoryDao.getAllProductHistory(10);
			}

			protected void onPostExecute(ArrayList<Product> result) {
				loadingDialog.dismiss();
				if (result == null) {
					CommonUtility.showToast(ProductBrowseHistoryActivity.this,
							"加载浏览历史失败!");
					return;
				}
				bindData(result);
			}

		}.execute();
	}

	/**
	 * 数据绑定视图
	 * 
	 * @param result
	 */
	private void bindData(ArrayList<Product> result) {
		if (result.size() == 0) {
			btnEdit.setVisibility(View.INVISIBLE);
			// 去除清空按钮
			if (mListView.getFooterViewsCount() > 0) {
				mListView.removeFooterView(lineFooter);
			}
		} else {
			btnEdit.setVisibility(View.INVISIBLE);
			// 添加清空按钮
			lineFooter = mInflater.inflate(
					R.layout.more_product_history_list_footer, null);
			ivClear = (TextView) lineFooter.findViewById(R.id.iv_clear);
			ivClear.setOnClickListener(ProductBrowseHistoryActivity.this);
			mListView.addFooterView(lineFooter);
		}
		mAdapter = new ProductHistoryListAdapter(
				ProductBrowseHistoryActivity.this);

		mListView.setAdapter(mAdapter);
		mListView.setEmptyView(tvEmpty);
		mAdapter.appendToList(result);
		mAdapter.setDeleteListener(ProductBrowseHistoryActivity.this);
	};

	@Override
	public void onClick(View v) {
		if (v == btnBack) {
			goback();
		} else if (v == tvTitle) {
			if (mListView.getAdapter() != null) {
				mListView.setSelection(0);
			}
		} else if (v == btnEdit) {
			// 适配器有数据且数量大于零才可编辑
			if (mAdapter != null && mAdapter.getCount() > 0) {
				boolean isEditMode = !mAdapter.isEditMode();
				if (isEditMode) {
					btnEdit.setText(R.string.finish);
				} else {
					btnEdit.setText(R.string.edit);
				}
				mAdapter.setEditMode(isEditMode);
			} else {
				CommonUtility.showToast(this,
						getString(R.string.you_have_no_product_browse_history));
			}
		} else if (v == ivClear) {
			mHistoryDao.removeAllProductHistory();
			mAdapter.clear();
			btnEdit.setVisibility(View.INVISIBLE);
			CommonUtility.showMiddleToast(this, "",
					getString(R.string.browse_history_has_remvoe));
		}
	}

	@Override
	public void onItemDeleted(final int postion) {
		final Product product = mAdapter.getProductByPosition(postion);

		new AsyncTask<Object, Void, Boolean>() {
			LoadingDialog loadingDialog = null;

			@Override
			protected void onPreExecute() {
				loadingDialog = CommonUtility.showLoadingDialog(
						ProductBrowseHistoryActivity.this, "正在删除...", false,
						new DialogInterface.OnCancelListener() {

							@Override
							public void onCancel(DialogInterface dialog) {
								cancel(true);
							}
						});
			}

			@Override
			protected Boolean doInBackground(Object... params) {
				return mHistoryDao.deleteProductHistory(product.getGoodsNo());
			}

			@Override
			protected void onPostExecute(Boolean result) {
				loadingDialog.dismiss();
				bindDelData(postion, result);
			}

		}.execute();
	}

	/**
	 * 删除完成数据处理
	 * @param postion
	 * @param result
	 */
	private void bindDelData(final int postion, Boolean result) {
		if (result) {
			mAdapter.deleteItem(postion);
			if (mAdapter.getCount() == 0) {
				mAdapter.setEditMode(false);
				btnEdit.setText(R.string.edit);
				btnEdit.setVisibility(View.INVISIBLE);
			}
		} else {
			CommonUtility.showToast(ProductBrowseHistoryActivity.this, "删除失败");
		}
	}
}
