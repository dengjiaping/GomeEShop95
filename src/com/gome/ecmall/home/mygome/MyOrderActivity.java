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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gome.ecmall.bean.OrderList;
import com.gome.ecmall.bean.OrderList.Order;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.custom.SegmentTabs;
import com.gome.ecmall.custom.SegmentTabs.OnTabSelectChangedListener;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class MyOrderActivity extends AbsSubActivity implements OnItemClickListener, OnScrollListener, OnClickListener {

    private static final String TAG = "MyOrderActivity";
    private TextView mTitle;
    private Button mBackBtn;
    private ListView mOrderList;
    private MyOrderAdapter mOrderAdapter;
    private View mFootView;
    private TextView emptyView;
    private SegmentTabs mTabs;
    private LinearLayout mTabLayout;
    private String mTitleStr;
    private int mCurrentPage = 1;
    private int mPageSize = 10;
    private int mTypeOrder = 0;
    private int mOrderStatus = 2;

    private ArrayList<Order> mData;
    private static final int PAGE_SIZE_DEFAULT = 10;
    private boolean hasMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mygome_my_orders);
        int titleId = R.string.mygome_take_order;
        mTitleStr = (titleId == 0) ? "" : getString(titleId);
        // Intent intent = getIntent();
        // if (intent != null) {
        // int titleId = intent.getIntExtra(MyGomeActivity.TITLE_ID, 0);
        // mTitleStr = (titleId == 0) ? "" : getString(titleId);
        // mOrderStatus = intent.getIntExtra(JsonInterface.JK_ORDER_STATUS, 0);
        // }
        initView();
        reload();
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.common_title_tv_text);
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText(mTitleStr);

        mBackBtn = (Button) findViewById(R.id.common_title_btn_back);
        mBackBtn.setVisibility(View.VISIBLE);
        mBackBtn.setText(R.string.back);
        mBackBtn.setOnClickListener(this);

        mOrderList = (ListView) findViewById(R.id.mygome_myorder_order_list);

        mFootView = LayoutInflater.from(MyOrderActivity.this).inflate(R.layout.common_loading_layout, null);
        emptyView = (TextView) findViewById(android.R.id.empty);
        mFootView.setClickable(false);
        mFootView.setFocusable(false);
        mFootView.setFocusableInTouchMode(false);

        mTabs = (SegmentTabs) findViewById(R.id.mygome_myorder_type_order_segments);
        mTabs.setTabSelectChangedListener(new TabSelectListener());
        mTabs.setSelected(-1);
        mTabLayout = (LinearLayout) findViewById(R.id.mygome_myorder_type_order_segments_layout);
        if (mOrderStatus != 0) {
            mTabLayout.setVisibility(View.GONE);
        }

    }

    class TabSelectListener implements OnTabSelectChangedListener {

        @Override
        public void onTabSelectChanged(SegmentTabs tabs, View item, int lastIndex, int currentIndex) {
            mTypeOrder = currentIndex + 1;
            if (loadMoreTask != null) {
                if (loadMoreTask.isCancelled()) {
                    loadMoreTask.cancel(true);
                }
                loadMoreTask = null;
            }
            reload();
        }
    }

    void setData() {
        mOrderList.setFocusable(true);
        mOrderList.setFocusableInTouchMode(true);
        mOrderList.setOnItemClickListener(this);
        mOrderList.setOnScrollListener(this);
    }

    private OrderListTask loadTask = null;

    public void reload() {
        BDebug.d(TAG, "reload/loadTask and loadMoreTask is null" + (loadTask == null) + "/" + (loadMoreTask == null));
        if (loadMoreTask != null) {
            loadMoreTask.cancel(true);
        }
        if (loadTask != null) {
            loadTask.cancel(true);
        }
        mCurrentPage = 1;
        loadTask = new OrderListTask(MyOrderActivity.this, mTypeOrder, mCurrentPage, mPageSize, mOrderStatus);
        loadTask.execute();
    }

    private class OrderListTask extends AsyncTask<Void, Void, ArrayList<Order>> {
        private Context mContext;
        private String requestJson;
        private LoadingDialog dialog;

        public OrderListTask(Context ctx, int typeOrder, int currentPage, int pageSize, int orderStatus) {
            mContext = ctx;
            requestJson = OrderList.createRequest(typeOrder, currentPage, pageSize, orderStatus,0);
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
        protected ArrayList<Order> doInBackground(Void... params) {
            String result = NetUtility.sendHttpRequestByPost(Constants.URL_PROFILE_ORDER_LIST, requestJson);
            if (result == null || result.length() == 0)
                return null;
            ArrayList<Order> orderList = OrderList.parseJson(result, null);
            return orderList;
        }

        @Override
        protected void onPostExecute(ArrayList<Order> result) {
            if (MyOrderActivity.this != null && !MyOrderActivity.this.isFinishing() && dialog != null
                    && dialog.isShowing()) {
                dialog.dismiss();
            }

            if (isCancelled()) {
                return;
            }

            ArrayList<Order> orderList = result;

            if (result == null) {
                if (mOrderAdapter != null) {
                    mOrderAdapter.clear();
                }
                CommonUtility.showMiddleToast(mContext, null, mContext.getString(R.string.data_load_fail_exception));
            } else {
                orderList = result;
                if (result.size() < mPageSize) {
                    hasMore = false;
                    if (mOrderList.getFooterViewsCount() > 0) {
                        // mOrderList.removeFooterView(mFootView);
                        ((TextView) mFootView.findViewById(R.id.common_loading_title)).setText(R.string.no_more);
                        ((ProgressBar) mFootView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                    }
                } else {
                    hasMore = true;
                    if (mOrderList.getFooterViewsCount() == 0) {
                        mOrderList.addFooterView(mFootView);
                    } else {
                        ((TextView) mFootView.findViewById(R.id.common_loading_title)).setText(R.string.load_more);
                        ((ProgressBar) mFootView.findViewById(R.id.loadingbar)).setVisibility(View.VISIBLE);
                    }
                    mCurrentPage++;
                }
            }

            mOrderAdapter = new MyOrderAdapter(MyOrderActivity.this, orderList, null);
            emptyView.setText(R.string.non_order);
            emptyView.setVisibility(View.VISIBLE);
            mOrderList.setAdapter(mOrderAdapter);
            mOrderList.setEmptyView(emptyView);
            afterTimeSelectListView();
            setData();
            loadTask = null;
        }
    }

    public class LoadMoreTask extends AsyncTask<Void, Void, ArrayList<Order>> {
        private Context mContext;
        private String requestJson;

        public LoadMoreTask(Context ctx, int typeOrder, int currentPage, int pageSize, int orderStatus) {
            mContext = ctx;
            requestJson = OrderList.createRequest(typeOrder, currentPage, pageSize, orderStatus,0);
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
        protected ArrayList<Order> doInBackground(Void... params) {
            String result = NetUtility.sendHttpRequestByPost(Constants.URL_PROFILE_ORDER_LIST, requestJson);
            if (result == null)
                return null;
            ArrayList<Order> orderList = OrderList.parseJson(result, null);
            return orderList;
        }

        @Override
        protected void onCancelled() {
            loadMoreTask = null;
        }

        @Override
        protected void onPostExecute(ArrayList<Order> result) {

            if (isCancelled()) {
                cancel(true);
            }
            if (result == null) {
                CommonUtility.showMiddleToast(MyOrderActivity.this, "", getString(R.string.data_load_fail_exception));
                return;
            }

            if (result.size() < PAGE_SIZE_DEFAULT) {
                hasMore = false;
                if (mOrderList.getFooterViewsCount() > 0) {
                    // mOrderList.removeFooterView(mFootView);
                    ((TextView) mFootView.findViewById(R.id.common_loading_title)).setText(R.string.no_more);
                    ((ProgressBar) mFootView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                }
            } else {
                hasMore = true;
                mCurrentPage++;
                if (mOrderList.getFooterViewsCount() == 0) {
                    mOrderList.addFooterView(mFootView);
                } else {
                    ((TextView) mFootView.findViewById(R.id.common_loading_title)).setText(R.string.load_more);
                    ((ProgressBar) mFootView.findViewById(R.id.loadingbar)).setVisibility(View.VISIBLE);
                }
            }
            mOrderAdapter.addItem(result);
            loadMoreTask = null;

        }

    }

    private LoadMoreTask loadMoreTask = null;

    public void loadMore() {
        BDebug.d(TAG, "loadMore/loadTask and loadMoreTask is null==" + (loadTask == null) + "/"
                + (loadMoreTask == null));
        if (loadMoreTask != null) {
            return;
        }

        loadMoreTask = new LoadMoreTask(MyOrderActivity.this, mTypeOrder, mCurrentPage, mPageSize, mOrderStatus);
        loadMoreTask.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // if (view != mFootView) {
        // mData = mOrderAdapter.getOrders();
        // Intent intent = new Intent(MyOrderActivity.this, OrderDetailsActivity.class);
        // String orderId = "";
        // String orderAmount = "";
        // String submitTime = "";
        // if (mData != null) {
        // Order order = mData.get(position);
        // orderId = order.getOrderID();
        // orderAmount = order.getOrderAmount();
        // submitTime = order.getOrderSubmitTime();
        // if ("Y".equals(order.getIsBbc())) {
        // return;
        // }
        // }
        // intent.putExtra(JsonInterface.JK_ORDER_ID, orderId);
        // intent.putExtra(JsonInterface.JK_ORDER_AMOUNT, orderAmount);
        // intent.putExtra(JsonInterface.JK_ORDER_SUBMIT_TIME, submitTime);
        // startActivityForResult(intent, 0);
        // }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // BDebug.d("==========",
        // "scrollState=OnScrollListener.SCROLL_STATE_IDLE============"
        // + scrollState + "="
        // + OnScrollListener.SCROLL_STATE_IDLE);
        // if (scrollState == OnScrollListener.SCROLL_STATE_IDLE &&canRefrash) {
        // loadMore();
        // }
    }

    private boolean canRefrash = false;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if ((firstVisibleItem + visibleItemCount) == totalItemCount && hasMore) {
            if (hasMore && mOrderAdapter != null && mOrderAdapter.getCount() > 0) {
                loadMore();
            }
        }

        // if ((firstVisibleItem + visibleItemCount) == totalItemCount&&hasMore)
        // {
        // canRefrash = true;
        // } else {
        // canRefrash = false;
        // }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
        case 6: {
            mOrderStatus = 0;
            mCurrentPage = 1;
            mTitle.setText(R.string.mygome_all_order);
            setData();
            break;
        }

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
                    mOrderList.setSelection(0);
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

    @Override
    protected void onStart() {

        super.onStart();
        BDebug.d(TAG, "==onStart==");
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onRestart() {

        super.onRestart();
        BDebug.d(TAG, "==onRestart==");
    }

    @Override
    protected void onPause() {

        super.onPause();
        BDebug.d(TAG, "==onPause==");
    }

    @Override
    protected void onStop() {

        super.onStop();
        BDebug.d(TAG, "==onStop==");
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        BDebug.d(TAG, "==onDestroy==");
    }
}
