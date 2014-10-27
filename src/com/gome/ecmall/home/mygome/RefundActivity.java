package com.gome.ecmall.home.mygome;

import java.util.ArrayList;
import java.util.Timer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gome.ecmall.bean.ReturnProduct.Refund;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 退款记录
 * 
 * @author qiudongchao
 * 
 */
public class RefundActivity extends AbsSubActivity implements OnScrollListener, OnClickListener {

    private static final String TAG = "RefundActivity";
    private TextView mTitle;
    private Button mBackBtn;
    private ListView mRefundList;
    private RefundAdapter mReFundAdapter;
    private View mFootView;
    private TextView emptyView;
    private String mTitleStr;
    private int mCurrentPage = 1;
    private int mPageSize = 10;

    private static final int PAGE_SIZE_DEFAULT = 10;
    private boolean hasMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mygome_refund);
        Intent intent = getIntent();
        if (intent != null) {
            int titleId = intent.getIntExtra(MyGomeActivity.TITLE_ID, 0);
            mTitleStr = (titleId == 0) ? "" : getString(titleId);
        }
        initView();
        initDataload();
    }

    /**
     * 初始化页面控件
     */
    private void initView() {
        mTitle = (TextView) findViewById(R.id.common_title_tv_text);
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(mTitleStr);

        mBackBtn = (Button) findViewById(R.id.common_title_btn_back);
        mBackBtn.setVisibility(View.VISIBLE);
        mBackBtn.setText(R.string.back);
        mBackBtn.setOnClickListener(this);

        mRefundList = (ListView) findViewById(R.id.mygome_myorder_order_list);

        mFootView = LayoutInflater.from(RefundActivity.this).inflate(R.layout.common_loading_layout, null);
        emptyView = (TextView) findViewById(android.R.id.empty);
        mFootView.setClickable(false);
        mFootView.setFocusable(false);
        mFootView.setFocusableInTouchMode(false);

    }

    /**
     * 设置listview事件
     */
    void setListViewEven() {
        mRefundList.setFocusable(true);
        mRefundList.setFocusableInTouchMode(true);
        mRefundList.setOnScrollListener(this);
    }

    private RefundListTask loadTask = null;

    /**
     * 初始化页面数据
     */
    public void initDataload() {

        if (loadMoreTask != null) {
            loadMoreTask.cancel(true);
        }
        if (loadTask != null) {
            loadTask.cancel(true);
        }
        mCurrentPage = 1;
        loadTask = new RefundListTask(RefundActivity.this, mCurrentPage, mPageSize);
        loadTask.execute();
    }

    /**
     * 异步请求--加载
     * 
     * @author qiudongchao
     * 
     */
    private class RefundListTask extends AsyncTask<Void, Void, ArrayList<Refund>> {
        private Context mContext;
        private String requestJson;
        private LoadingDialog dialog;

        public RefundListTask(Context ctx, int currentPage, int pageSize) {
            mContext = ctx;
            requestJson = MyReturnServer.build_Request_MyGome_Refund(pageSize, currentPage);
        }

        @Override
        protected void onPreExecute() {
            if (!NetUtility.isNetworkAvailable(mContext)) {
                CommonUtility.showToast(mContext, getString(R.string.data_load_fail_please_check_network_settings));
                cancel(true);
                return;
            }

            dialog = CommonUtility.showLoadingDialog(mContext, getString(R.string.login_loading), true,
                    new DialogInterface.OnCancelListener() {

                        @Override
                        public void onCancel(DialogInterface dialog) {
                            cancel(true);
                        }
                    });
        }

        @Override
        protected ArrayList<Refund> doInBackground(Void... params) {
            String result = NetUtility.sendHttpRequestByPost(Constants.URL_MYGOME_REFUND, requestJson);
            if (result == null || result.length() == 0)
                return null;
            ArrayList<Refund> orderList = MyReturnServer.parser_Request_MyGome_Refund(result);
            return orderList;
            // return getData();
        }

        @Override
        protected void onPostExecute(ArrayList<Refund> result) {
            if (RefundActivity.this != null && !RefundActivity.this.isFinishing() && dialog != null
                    && dialog.isShowing()) {
                dialog.dismiss();
            }

            if (isCancelled()) {
                return;
            }

            if (result == null) {
                if (mReFundAdapter != null) {
                    mReFundAdapter.clear();
                }
                CommonUtility.showMiddleToast(mContext, null, mContext.getString(R.string.data_load_fail_exception));
            } else {
                if (result.size() < mPageSize) {
                    hasMore = false;
                    if (mRefundList.getFooterViewsCount() > 0) {
                        ((TextView) mFootView.findViewById(R.id.common_loading_title)).setText(R.string.no_more_money);
                        ((ProgressBar) mFootView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                    }
                } else {
                    hasMore = true;
                    if (mRefundList.getFooterViewsCount() == 0) {
                        mRefundList.addFooterView(mFootView);
                    } else {
                        ((TextView) mFootView.findViewById(R.id.common_loading_title)).setText(R.string.load_more);
                        ((ProgressBar) mFootView.findViewById(R.id.loadingbar)).setVisibility(View.VISIBLE);
                    }
                    mCurrentPage++;
                }
            }

            setListViewEven();
            mReFundAdapter = new RefundAdapter(RefundActivity.this, result);
            mRefundList.setAdapter(mReFundAdapter);
            emptyView.setText("暂无退款信息");
            emptyView.setVisibility(View.VISIBLE);
            mRefundList.setEmptyView(emptyView);
            // afterTimeSelectListView();
            loadTask = null;
        }
    }

    /**
     * 异步请求加载--更多
     * 
     * @author qiudongchao
     * 
     */
    public class RefundLoadMoreTask extends AsyncTask<Void, Void, ArrayList<Refund>> {
        private Context mContext;
        private String requestJson;

        public RefundLoadMoreTask(Context ctx, int currentPage, int pageSize) {
            mContext = ctx;
            requestJson = MyReturnServer.build_Request_MyGome_Refund(pageSize, currentPage);
        }

        @Override
        protected void onPreExecute() {
            if (!NetUtility.isNetworkAvailable(mContext)) {
                CommonUtility.showToast(mContext, getString(R.string.data_load_fail_please_check_network_settings));
                cancel(true);
                return;
            }

        }

        @Override
        protected ArrayList<Refund> doInBackground(Void... params) {
            String result = NetUtility.sendHttpRequestByPost(Constants.URL_MYGOME_REFUND, requestJson);
            if (result == null)
                return null;
            ArrayList<Refund> orderList = MyReturnServer.parser_Request_MyGome_Refund(result);
            return orderList;
        }

        @Override
        protected void onCancelled() {
            loadMoreTask = null;
        }

        @Override
        protected void onPostExecute(ArrayList<Refund> result) {

            if (isCancelled()) {
                cancel(true);
            }
            if (result == null) {
                CommonUtility.showMiddleToast(RefundActivity.this, "", getString(R.string.data_load_fail_exception));
                return;
            }

            if (result.size() < PAGE_SIZE_DEFAULT) {
                hasMore = false;
                if (mRefundList.getFooterViewsCount() > 0) {
                    // mOrderList.removeFooterView(mFootView);
                    ((TextView) mFootView.findViewById(R.id.common_loading_title)).setText(R.string.no_more);
                    ((ProgressBar) mFootView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                }
            } else {
                hasMore = true;
                mCurrentPage++;
                if (mRefundList.getFooterViewsCount() == 0) {
                    mRefundList.addFooterView(mFootView);
                } else {
                    ((TextView) mFootView.findViewById(R.id.common_loading_title)).setText(R.string.load_more);
                    ((ProgressBar) mFootView.findViewById(R.id.loadingbar)).setVisibility(View.VISIBLE);
                }
            }
            mReFundAdapter.addItem(result);
            loadMoreTask = null;

        }

    }

    private RefundLoadMoreTask loadMoreTask = null;

    public void loadMore() {

        if (loadMoreTask != null) {
            return;
        }

        loadMoreTask = new RefundLoadMoreTask(RefundActivity.this, mCurrentPage, mPageSize);
        loadMoreTask.execute();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    private boolean canRefrash = false;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if ((firstVisibleItem + visibleItemCount) == totalItemCount && hasMore) {
            if (hasMore && mReFundAdapter != null && mReFundAdapter.getCount() > 0) {
                loadMore();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.common_title_btn_back:
            goback();
            break;
        case R.id.mygome_myorder_type_order_segments:

        default:
            break;

        }
    }

    public void afterTimeSelectListView() {
        Timer timer = new Timer();
        initHandler(timer);
        // 启动timer任务
        timer.schedule(new MyTask(), 1000);
    }

    private Handler handler;

    private void initHandler(final Timer timer) {
        if (handler == null) {
            handler = new Handler() {
                @Override
                public void dispatchMessage(Message msg) {
                    super.dispatchMessage(msg);
                    mRefundList.setSelection(0);
                    timer.cancel();
                }
            };
        }
    }

    class MyTask extends java.util.TimerTask {

        @Override
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }

    // ------------------------------------------------
    public ArrayList<Refund> getData() {
        ArrayList<Refund> list = new ArrayList<Refund>();
        for (int i = 0; i < 10; i++) {
            Refund ref = new Refund();
            ref.setMethod("虚拟账户" + i);
            ref.setOrderCount("￥100.00");
            ref.setOrderDate("2013-02-12 10:20");
            ref.setOrderNum("12345678" + i);
            ref.setReason("手机测试故障，申请退货。手机测试故障，申请退货。手机测试故障申请退货。手机测试故障。" + i);
            ref.setStatus("等待财务审核" + i);
            list.add(ref);
        }
        return list;
    }
}
