package com.gome.ecmall.home.more;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gome.ecmall.bean.Announcement;
import com.gome.ecmall.bean.Announcement.ReplyAnnInfo;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 商城公告
 */
public class AnnouncementListActivity extends AbsSubActivity implements
		OnClickListener {

	private TextView tvTitle;
	private Button btnBack;
	private ListView listView;
	private TextView tvEmpty;
	private LinearLayout empty_image_layout;
	private AnnouncementListAdapter adapter;
	public static final String TAG = "AnnouncementListActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_announce_list);
		initView();
		initData();
	}

	/**
	 * 初始化页面视图
	 */
	private void initView() {
		tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
		tvTitle.setText(R.string.announcement);
		btnBack = (Button) findViewById(R.id.common_title_btn_back);
		btnBack.setVisibility(View.VISIBLE);
		btnBack.setText(R.string.back);
		btnBack.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.more_announce_list);
		tvEmpty = (TextView) findViewById(android.R.id.empty);
		tvEmpty.setText("暂无公告");
		empty_image_layout = (LinearLayout) findViewById(R.id.empty_image);
	}

	/**
	 * 初始化页面数据
	 */
	private void initData() {
		new AsyncTask<Object, Void, ArrayList<ReplyAnnInfo>>() {

			private LoadingDialog loadingDialog;

			@Override
			protected void onPreExecute() {
				if (!NetUtility
						.isNetworkAvailable(AnnouncementListActivity.this)) {
					CommonUtility
							.showMiddleToast(
									AnnouncementListActivity.this,
									"",
									getString(R.string.can_not_conntect_network_please_check_network_settings));
					cancel(true);
					empty_image_layout.setVisibility(View.VISIBLE);
					listView.setVisibility(View.GONE);
					return;
				}
				empty_image_layout.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				loadingDialog = CommonUtility.showLoadingDialog(
						AnnouncementListActivity.this, "正在请求数据...", true, null);
			}

			@Override
			protected ArrayList<ReplyAnnInfo> doInBackground(Object... params) {
				String json = Announcement.createRequestAnnListJson(1, 5);
				String result = NetUtility.sendHttpRequestByPost(
						Constants.URL_SUPPLEMENT_ANNOUNCE_LIST, json);
				if (result.equals(NetUtility.NO_CONN)) {
					return null;
				}
				return Announcement.parseReplayAnnInfos(result);
			}

			@Override
			protected void onPostExecute(ArrayList<ReplyAnnInfo> result) {
				if (isCancelled()) {
					return;
				}
				loadingDialog.dismiss();
				bindListView(result);
			}

		}.execute();
	}

	/**
	 * 数据绑定ListView
	 * 
	 * @param result
	 */
	private void bindListView(ArrayList<ReplyAnnInfo> result) {
		if (result == null) {
			CommonUtility.showMiddleToast(AnnouncementListActivity.this, "",
					getString(R.string.data_load_fail_exception));
			return;
		}
		adapter = new AnnouncementListAdapter(AnnouncementListActivity.this);
		listView.setAdapter(adapter);
		adapter.appendToList(result);
		listView.setEmptyView(tvEmpty);
	}

	@Override
	public void onClick(View v) {
		if (v == btnBack) {
			goback();
		}
	}

}
