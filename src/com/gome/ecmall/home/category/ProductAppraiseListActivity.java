package com.gome.ecmall.home.category;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gome.ecmall.bean.ProductAppraise;
import com.gome.ecmall.bean.ProductAppraise.Appraise;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 商品评价列表页
 */
public class ProductAppraiseListActivity extends AbsSubActivity implements
		OnClickListener, OnScrollListener {

	private TextView tvTitle;
	private ListView listview;
	private ImageView no_net_img;
	private AppraiseListAdapter mAdapter;
	private Button btnBack;
	private LinearLayout loadingView;
	private TextView tvEmpty;
	private int currentPage = 1;
	private String goodsNo;
	private boolean hasMoreData = true;
	public static final String INTENT_KEY_GOODS_NO = "goodsNo";
	public static final String TAG = "ProductAppraiseListActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_product_appraise_main);
		getWindow().getDecorView().setBackgroundDrawable(null);
		goodsNo = getIntent().getStringExtra(INTENT_KEY_GOODS_NO);
		initView();
		initData();
	}

	/**
	 * 初始化页面视图
	 */
	private void initView() {
		tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
		tvTitle.setText(R.string.product_appraise);
		tvTitle.setOnClickListener(this);
		btnBack = (Button) findViewById(R.id.common_title_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setText(R.string.back);
		btnBack.setOnClickListener(this);
		tvEmpty = (TextView) findViewById(android.R.id.empty);
		tvEmpty.setText(R.string.current_have_no_product_appraise);
		loadingView = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.common_loading_layout, null);
		listview = (ListView) findViewById(R.id.category_product_appraise_listview);
		no_net_img = (ImageView) findViewById(R.id.no_net_img);
		listview.addFooterView(loadingView);
	}

	/**
	 * 初始化页面数据
	 */
	private void initData() {
		if (!NetUtility.isNetworkAvailable(ProductAppraiseListActivity.this)) {
			CommonUtility
					.showMiddleToast(
							ProductAppraiseListActivity.this,
							"",
							getString(R.string.can_not_conntect_network_please_check_network_settings));
			no_net_img.setVisibility(View.VISIBLE);
			listview.setVisibility(View.GONE);
			return;
		}
		no_net_img.setVisibility(View.GONE);
		listview.setVisibility(View.VISIBLE);
		new AsyncTask<Object, Void, ArrayList<Appraise>>() {
			LoadingDialog loadingDialog;

			protected void onPreExecute() {
				loadingDialog = CommonUtility.showLoadingDialog(
						ProductAppraiseListActivity.this,
						getString(R.string.loading), true,
						new DialogInterface.OnCancelListener() {

							@Override
							public void onCancel(DialogInterface dialog) {
								cancel(true);
							}
						});
			};

			protected ArrayList<Appraise> doInBackground(Object... params) {
				String json = ProductAppraise.createRequestAppraiseListJson(
						goodsNo, currentPage, Constants.PAGE_SIZE);
				String result = NetUtility.sendHttpRequestByPost(
						Constants.URL_PRODUCT_APPRAISE, json);
				if (NetUtility.NO_CONN.equals(result)) {
					return null;
				}
				return ProductAppraise.paserResponseAppraiseList(result);
			};

			protected void onPostExecute(ArrayList<Appraise> result) {
				loadingDialog.dismiss();
				if (result == null) {
					CommonUtility.showMiddleToast(
							ProductAppraiseListActivity.this, "",
							getString(R.string.data_load_fail_exception));
					return;
				}
				bindFirstData(result);
			}

		}.execute();
	}

	/**
	 * 处理第一次加载数据
	 * 
	 * @param result
	 */
	private void bindFirstData(ArrayList<Appraise> result) {
		mAdapter = new AppraiseListAdapter(ProductAppraiseListActivity.this);
		listview.setAdapter(mAdapter);
		mAdapter.appendToList(result);
		listview.setEmptyView(tvEmpty);
		listview.setOnScrollListener(ProductAppraiseListActivity.this);
		if (mAdapter.getCount() < Constants.PAGE_SIZE) {
			// listview.removeFooterView(loadingView);
			((TextView) loadingView.findViewById(R.id.common_loading_title))
					.setText(R.string.no_more);
			((TextView) loadingView.findViewById(R.id.common_loading_title))
					.setVisibility(View.INVISIBLE);
			((ProgressBar) loadingView.findViewById(R.id.loadingbar))
					.setVisibility(View.GONE);
			hasMoreData = false;
		} else {
			((TextView) loadingView.findViewById(R.id.common_loading_title))
					.setVisibility(View.VISIBLE);
			((TextView) loadingView.findViewById(R.id.common_loading_title))
					.setText(R.string.load_more);
			((ProgressBar) loadingView.findViewById(R.id.loadingbar))
					.setVisibility(View.VISIBLE);
		}
		currentPage++;
	};

	// ----------------------------more-------------------------------

	private AsyncTask<Object, Void, ArrayList<Appraise>> asyncTask = null;

	private void loadMoreData(final String goodsId, final int page) {
		if (!NetUtility.isNetworkAvailable(ProductAppraiseListActivity.this)) {
			CommonUtility
					.showMiddleToast(
							ProductAppraiseListActivity.this,
							"",
							getString(R.string.can_not_conntect_network_please_check_network_settings));
			return;
		}
		if (asyncTask != null
				&& asyncTask.getStatus() != AsyncTask.Status.FINISHED) {
			return;
		}
		asyncTask = new AsyncTask<Object, Void, ArrayList<Appraise>>() {

			protected ArrayList<Appraise> doInBackground(Object... params) {
				String json = ProductAppraise.createRequestAppraiseListJson(
						goodsNo, currentPage, Constants.PAGE_SIZE);
				String result = NetUtility.sendHttpRequestByPost(
						Constants.URL_PRODUCT_APPRAISE, json);
				if (NetUtility.NO_CONN.equals(result)) {
					return null;
				}
				return ProductAppraise.paserResponseAppraiseList(result);
			};

			protected void onPostExecute(ArrayList<Appraise> result) {
				if (result == null) {
					CommonUtility.showMiddleToast(
							ProductAppraiseListActivity.this, "",
							getString(R.string.data_load_fail_exception));
					return;
				}
				bindMoreData(result);
			}

		};
		asyncTask.execute();
	}

	/**
	 * 处理更多数据
	 * 
	 * @param result
	 */
	private void bindMoreData(ArrayList<Appraise> result) {
		mAdapter.appendToList(result);
		if (result.size() < Constants.PAGE_SIZE) {
			// listview.removeFooterView(loadingView);
			((TextView) loadingView.findViewById(R.id.common_loading_title))
					.setVisibility(View.INVISIBLE);
			((TextView) loadingView.findViewById(R.id.common_loading_title))
					.setText(R.string.no_more);
			((ProgressBar) loadingView.findViewById(R.id.loadingbar))
					.setVisibility(View.GONE);
			hasMoreData = false;
		} else {
			((TextView) loadingView.findViewById(R.id.common_loading_title))
					.setVisibility(View.VISIBLE);
			((TextView) loadingView.findViewById(R.id.common_loading_title))
					.setText(R.string.load_more);
			((ProgressBar) loadingView.findViewById(R.id.loadingbar))
					.setVisibility(View.VISIBLE);
		}
		currentPage++;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_title_btn_back:
			goback();
			break;
		case R.id.common_title_tv_text:
			// 返回顶部
			if (listview.getAdapter() != null) {
				listview.setSelection(0);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (firstVisibleItem + visibleItemCount == totalItemCount
				&& mAdapter != null && hasMoreData) {
			loadMoreData(goodsNo, currentPage);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mAdapter != null) {
			mAdapter.clear();
			mAdapter = null;
		}
	}
}
