package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gome.ecmall.bean.ReturnProduct.ReturnOrder;
import com.gome.ecmall.bean.ReturnProduct.ReturnRecord;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 退换货列表页
 * 
 * @author qiudongchao
 * 
 */
public class MyReturnListActivity extends Activity implements OnClickListener, OnScrollListener {
    // 页面控件
    private TextView tvTitle;
    private Button btnBack, btnRight;
    private TextView tvDetails, tvSearch;
    private ListView lvDetails, lvSearch;
    private View mFootView;
    private TextView emptyView;
    // 其他变量
    private String mTitleStr;
    private boolean isHuan = true;

    private int mReturnListIndex = 1;
    private int mReturnRecordIndex = 1;

    private boolean mReturnListHasMore = false;
    private boolean mReturnRecordHasMore = false;

    private MyReturnProAdapter mReturnListAdapter = null;
    private MyReturnSearAdapter mReturnRecordAdapter = null;

    private static final int PAGE_SIZE = 4;// 页面数据条数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        mTitleStr = getString(intent.getIntExtra("titleId", 0));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mygome_return_list);
        initView();
        initData();
    }

    /**
     * 初始化页面控件
     */
    private void initView() {
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnRight = (Button) findViewById(R.id.common_title_btn_right);
        tvDetails = (TextView) findViewById(R.id.mygome_return_detail);
        tvSearch = (TextView) findViewById(R.id.mygome_return_search);
        lvDetails = (ListView) findViewById(R.id.mygome_return_listView1);
        lvSearch = (ListView) findViewById(R.id.mygome_return_listView2);
        emptyView = (TextView)findViewById(android.R.id.empty);

        mFootView = LayoutInflater.from(MyReturnListActivity.this).inflate(R.layout.common_loading_layout, null);
        mFootView.setClickable(false);
        mFootView.setFocusable(false);
        mFootView.setFocusableInTouchMode(false);

        btnBack.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        tvDetails.setOnClickListener(this);
        tvSearch.setOnClickListener(this);

        btnRight.setVisibility(View.INVISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.VISIBLE);

        btnRight.setText("进度");
        tvTitle.setText(mTitleStr);
        btnBack.setText(R.string.back);

        setListViewEvent();
        updateUI();
    }

    /**
     * 初始化页面数据
     */
    private void initData() {
        mReturnListIndex = 1;
        requestReturnList(mReturnListIndex);
    }

    /**
     * 更新UI
     */
    private void updateUI() {
        tvDetails.setSelected(isHuan);
        tvSearch.setSelected(!isHuan);
        lvDetails.setVisibility(isHuan ? View.VISIBLE : View.GONE);
        lvSearch.setVisibility(!isHuan ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (btnBack == v) {
            finish();
        } else if (tvDetails == v) {
            mReturnListIndex = 1;
            requestReturnList(mReturnListIndex);
        } else if (tvSearch == v) {
            mReturnRecordIndex = 1;
            requestReturnHistoryList(mReturnRecordIndex);
        } else if (btnRight == v) {
        }
    }

    /**
     * 获取返修列表异步栈
     * 
     * @author qiudongchao
     * 
     */
    private class ReturnListTask extends AsyncTask<Void, Void, ArrayList<ReturnOrder>> {
        LoadingDialog dialog;
        private int index;

        public ReturnListTask(int currentPage) {
            this.index = currentPage;
        }

        @Override
        protected void onPreExecute() {
            if (index == 1) {
                dialog = CommonUtility.showLoadingDialog(MyReturnListActivity.this, getString(R.string.loading), true,
                        new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }
        }

        @Override
        protected ArrayList<ReturnOrder> doInBackground(Void... params) {
            String json = MyReturnServer.build_Request_MyGome_Return_List(index, PAGE_SIZE);
            BDebug.i("hyxf", json);
            String result = NetUtility.sendHttpRequestByPost(Constants.URL_RETURN_LIST, json);
            BDebug.i("hyxf", result);
            return MyReturnServer.paser_Response_MyGome_Return_List(result);
        }

        protected void onPostExecute(ArrayList<ReturnOrder> result) {
            if (isCancelled()) {
                return;
            }
            if (index == 1) {
                dialog.dismiss();
            }
            if (result == null) {
                CommonUtility.showToast(MyReturnListActivity.this, getString(R.string.data_load_fail_exception));
                return;
            }

            // ---分页逻辑---
            if (result.size() == 0) {
                mReturnListHasMore = false;
                if (lvDetails.getFooterViewsCount() > 0) {
                    ((TextView) mFootView.findViewById(R.id.common_loading_title)).setText(R.string.no_more_return);
                    ((ProgressBar) mFootView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                    // 去掉底部显示
                    lvDetails.removeFooterView(mFootView);
                }
            } else {
                mReturnListHasMore = true;
                if (lvDetails.getFooterViewsCount() == 0) {
                    lvDetails.addFooterView(mFootView);
                } else {
                    ((TextView) mFootView.findViewById(R.id.common_loading_title)).setText(R.string.loading);
                    ((ProgressBar) mFootView.findViewById(R.id.loadingbar)).setVisibility(View.VISIBLE);
                }
                mReturnListIndex++;
            }
            // ----------------
            if (index == 1) {
                isHuan = true;
                updateUI();
            }
            emptyView.setText("您没有订单记录");
            if (mReturnListAdapter == null || mReturnListIndex == 1) {
                mReturnListAdapter = new MyReturnProAdapter(MyReturnListActivity.this, result);
                lvDetails.setAdapter(mReturnListAdapter);
                emptyView.setVisibility(View.VISIBLE);
                lvDetails.setEmptyView(emptyView);
            } else {
                if (index == 1) {
                    mReturnListAdapter.reload(result);
                } else {
                    mReturnListAdapter.addItem(result);
                }
            }
            returnListTask = null;
        };
    }

    private ReturnListTask returnListTask = null;

    /**
     * 请求退换货列表
     */
    private void requestReturnList(int index) {
        if (!NetUtility.isNetworkAvailable(MyReturnListActivity.this)) {
            CommonUtility.showMiddleToast(MyReturnListActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        if (returnListTask == null) {
            returnListTask = new ReturnListTask(index);
        } else {
            returnListTask.cancel(true);
            returnListTask = new ReturnListTask(index);
        }

        if (returnRecordListTask != null) {
            returnRecordListTask.cancel(true);
        }
        returnListTask.execute();
    }

    /**
     * 退换货记录列表
     * 
     * @author qiudongchao
     * 
     */
    private class ReturnRecordListTask extends AsyncTask<Void, Void, ArrayList<ReturnRecord>> {
        LoadingDialog dialog;
        private int index;

        public ReturnRecordListTask(int currentPage) {
            this.index = currentPage;
        }

        @Override
        protected void onPreExecute() {
            if (index == 1) {
                dialog = CommonUtility.showLoadingDialog(MyReturnListActivity.this, getString(R.string.loading), true,
                        new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }
        }

        @Override
        protected ArrayList<ReturnRecord> doInBackground(Void... params) {
            String json = MyReturnServer.build_Request_MyGome_Return_List(index, PAGE_SIZE);
            String result = NetUtility.sendHttpRequestByPost(Constants.URL_RETURN_RECORD_LIST, json);
            return MyReturnServer.paser_Response_MyGome_Record_List(result);
        }

        protected void onPostExecute(ArrayList<ReturnRecord> result) {
            if (isCancelled()) {
                return;
            }
            if (index == 1) {
                dialog.dismiss();
            }
            if (result == null) {
                CommonUtility.showToast(MyReturnListActivity.this, getString(R.string.data_load_fail_exception));
                return;
            }

            // ---分页逻辑---
            if (result.size() < PAGE_SIZE) {
                mReturnRecordHasMore = false;
                if (lvSearch.getFooterViewsCount() > 0) {
                    ((TextView) mFootView.findViewById(R.id.common_loading_title)).setText(R.string.no_more_return);
                    ((ProgressBar) mFootView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                    lvSearch.removeFooterView(mFootView);
                }
            } else {
                mReturnRecordHasMore = true;
                if (lvSearch.getFooterViewsCount() == 0) {
                    lvSearch.addFooterView(mFootView);
                } else {
                    ((TextView) mFootView.findViewById(R.id.common_loading_title)).setText(R.string.loading);
                    ((ProgressBar) mFootView.findViewById(R.id.loadingbar)).setVisibility(View.VISIBLE);
                }
                mReturnRecordIndex++;
            }
            // -------------
            if (index == 1) {
                isHuan = false;
                updateUI();
            }
            emptyView.setText("您没有退换货记录");
            if (mReturnRecordAdapter == null) {
                mReturnRecordAdapter = new MyReturnSearAdapter(MyReturnListActivity.this, result);
                lvSearch.setAdapter(mReturnRecordAdapter);
                emptyView.setVisibility(View.VISIBLE);
                lvSearch.setEmptyView(emptyView);
            } else {
                BDebug.i("hyxf", "执行了数据的加载-additem");
                if (index == 1) {
                    mReturnRecordAdapter.reload(result);
                } else {
                    mReturnRecordAdapter.addItem(result);
                }
            }
            returnRecordListTask = null;
        };
    }

    private ReturnRecordListTask returnRecordListTask = null;

    /**
     * 请求退换货历史列表
     */
    private void requestReturnHistoryList(int index) {
        if (!NetUtility.isNetworkAvailable(MyReturnListActivity.this)) {
            CommonUtility.showMiddleToast(MyReturnListActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        if (returnRecordListTask == null) {
            returnRecordListTask = new ReturnRecordListTask(index);
        } else {
            returnRecordListTask.cancel(true);
            returnRecordListTask = new ReturnRecordListTask(index);
        }
        if (returnListTask != null) {
            returnListTask.cancel(true);
        }
        returnRecordListTask.execute();
    }

    private void setListViewEvent() {
        lvDetails.setFocusable(true);
        lvDetails.setFocusableInTouchMode(true);
        lvDetails.setOnScrollListener(this);
        lvSearch.setFocusable(true);
        lvSearch.setFocusableInTouchMode(true);
        lvSearch.setOnScrollListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 100) {
            initData();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (lvDetails == view) {
            if ((firstVisibleItem + visibleItemCount) == totalItemCount && mReturnListHasMore) {
                if (mReturnListHasMore && mReturnListAdapter != null && mReturnListAdapter.getCount() > 0) {
                    requestReturnList(mReturnListIndex);
                }
            }
        }
        if (lvSearch == view) {
            if ((firstVisibleItem + visibleItemCount) == totalItemCount && mReturnRecordHasMore) {
                if (mReturnRecordHasMore && mReturnRecordAdapter != null && mReturnRecordAdapter.getCount() > 0) {
                    requestReturnHistoryList(mReturnRecordIndex);
                    BDebug.i("hyxf", "执行了滑动时候的加载");
                }
            }
        }
    }
}
