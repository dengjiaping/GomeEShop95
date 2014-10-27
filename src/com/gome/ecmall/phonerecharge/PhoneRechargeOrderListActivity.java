package com.gome.ecmall.phonerecharge;

import java.util.ArrayList;
import java.util.Timer;

import android.app.Activity;
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
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.OrderList;
import com.gome.ecmall.bean.OrderList.Order;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.custom.SegmentTabs;
import com.gome.ecmall.custom.SegmentTabs.OnTabSelectChangedListener;
import com.gome.ecmall.home.mygome.MyOrderAdapter;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.ecmall.util.PreferenceUtils;
import com.gome.eshopnew.R;

/**
 * @author qinxudong 手机充值订单列表 测试账号 44580000
 */
public class PhoneRechargeOrderListActivity extends Activity implements OnItemClickListener, OnScrollListener,
        OnClickListener {

    private static final String TAG = "PhoneRechargeOrderListActivity";
    /** 筛选条件tab */
    private SegmentTabs mTabs;
    /** 订单listview */
    private ListView listView;
    /** 没有数据提示textView */
    private TextView tipView;
    /** 标题左侧按钮 */
    private Button title_left_btn;
    /** 标题 */
    private TextView titleTextView;
    /** 标题右侧按钮 */
    private Button title_right_btn;
    /** 加载更多footerview */
    private View mFootView;
    private String mTitleStr;
    private int mCurrentPage = 1;
    private int mPageSize = 10;
    private int mTypeOrder = 1;
    private int mOrderStatus;
    private MyOrderAdapter mOrderAdapter;
    private ArrayList<Order> mData;
    private static final int PAGE_SIZE_DEFAULT = 10;
    private boolean hasMore;
    private String profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.phone_recharge_order_list);
        initView();
        reload();
    }

    private void initView() {
        title_left_btn = (Button) findViewById(R.id.common_title_btn_back);
        titleTextView = (TextView) findViewById(R.id.common_title_tv_text);
        title_right_btn = (Button) findViewById(R.id.common_title_btn_right);
        titleTextView.setText(getString(R.string.phone_recharge_order));
        titleTextView.setVisibility(View.VISIBLE);
        title_left_btn.setVisibility(View.VISIBLE);
        title_left_btn.setOnClickListener(this);
        title_left_btn.setText(R.string.back);
        title_right_btn.setVisibility(View.INVISIBLE);

        mTabs = (SegmentTabs) findViewById(R.id.mygome_myorder_type_order_segments);
        mTabs.setTabSelectChangedListener(new TabSelectListener());
        mTabs.setSelected(-1);
        listView = (ListView) findViewById(R.id.mygome_myorder_order_list);
        tipView = (TextView) findViewById(android.R.id.empty);
        mFootView = LayoutInflater.from(PhoneRechargeOrderListActivity.this).inflate(R.layout.common_loading_layout,
                null);
        mFootView.setClickable(false);
        mFootView.setFocusable(false);
        mFootView.setFocusableInTouchMode(false);
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
        profileId = PreferenceUtils.getStringValue(JsonInterface.JK_PROFILE_ID, "");
        // profileId = "44580000";
        loadTask = new OrderListTask(PhoneRechargeOrderListActivity.this, mTypeOrder, mCurrentPage, mPageSize,
                mOrderStatus, profileId);
        loadTask.execute();
    }

    private class OrderListTask extends AsyncTask<Void, Void, ArrayList<Order>> {
        private Context mContext;
        private String requestJson;
        private LoadingDialog dialog;

        public OrderListTask(Context ctx, int typeOrder, int currentPage, int pageSize, int orderStatus,
                String profileId) {
            mContext = ctx;
            requestJson = OrderList.createPhoneRechargeOrderListRequest(typeOrder, currentPage, pageSize, orderStatus,
                    profileId);
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
            String result = NetUtility.sendHttpRequestByPost(Constants.PHONE_RECHARGE_ORDER_URL, requestJson);
            BDebug.e(TAG, result + "@@@@@@@@@@ " + requestJson);
            if (result == null || result.length() == 0)
                return null;
            ArrayList<Order> orderList = OrderList.parseJson(result, OrderList.ORDERTYPE);
            return orderList;
        }

        @Override
        protected void onPostExecute(ArrayList<Order> result) {
            if (PhoneRechargeOrderListActivity.this != null && !PhoneRechargeOrderListActivity.this.isFinishing()
                    && dialog != null && dialog.isShowing()) {
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
                    if (listView.getFooterViewsCount() > 0) {
                        // listView.removeFooterView(mFootView);
                        ((TextView) mFootView.findViewById(R.id.common_loading_title)).setText(R.string.no_more);
                        ((ProgressBar) mFootView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                    }
                } else {
                    hasMore = true;
                    if (listView.getFooterViewsCount() == 0) {
                        listView.addFooterView(mFootView);
                    } else {
                        ((TextView) mFootView.findViewById(R.id.common_loading_title)).setText(R.string.load_more);
                        ((ProgressBar) mFootView.findViewById(R.id.loadingbar)).setVisibility(View.VISIBLE);
                    }
                    mCurrentPage++;
                }
            }

            mOrderAdapter = new MyOrderAdapter(PhoneRechargeOrderListActivity.this, orderList, MyOrderAdapter.GOODSTYPE);
            tipView.setText(R.string.non_order);
            tipView.setVisibility(View.VISIBLE);
            listView.setAdapter(mOrderAdapter);
            listView.setEmptyView(tipView);
            afterTimeSelectListView();
            setData();
            loadTask = null;
        }
    }

    void setData() {
        listView.setFocusable(true);
        listView.setFocusableInTouchMode(true);
        listView.setClickable(true);
        // listView.setOnItemClickListener(new OnItemClickListener() {
        //
        // @Override
        // public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // // TODO Auto-generated method stub
        // Intent intent = new Intent(PhoneRechargeOrderListActivity.this, PhoneRechargeOrderDetailActivity.class);
        // startActivity(intent);
        // }
        // });
        // listView.setOnItemClickListener(this);
        listView.setOnScrollListener(this);
    }

    public class LoadMoreTask extends AsyncTask<Void, Void, ArrayList<Order>> {
        private Context mContext;
        private String requestJson;

        public LoadMoreTask(Context ctx, int typeOrder, int currentPage, int pageSize, int orderStatus, String profileId) {
            mContext = ctx;
            requestJson = OrderList.createPhoneRechargeOrderListRequest(typeOrder, currentPage, pageSize, orderStatus,
                    profileId);
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
            String result = NetUtility.sendHttpRequestByPost(Constants.PHONE_RECHARGE_ORDER_URL, requestJson);
            if (result == null)
                return null;
            ArrayList<Order> orderList = OrderList.parseJson(result, OrderList.ORDERTYPE);
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
                CommonUtility.showMiddleToast(PhoneRechargeOrderListActivity.this, "",
                        getString(R.string.data_load_fail_exception));
                return;
            }

            if (result.size() < PAGE_SIZE_DEFAULT) {
                hasMore = false;
                if (listView.getFooterViewsCount() > 0) {
                    // listView.removeFooterView(mFootView);
                    ((TextView) mFootView.findViewById(R.id.common_loading_title)).setText(R.string.no_more);
                    ((ProgressBar) mFootView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                }
            } else {
                hasMore = true;
                mCurrentPage++;
                if (listView.getFooterViewsCount() == 0) {
                    listView.addFooterView(mFootView);
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
        // profileId = "44580000";
        loadMoreTask = new LoadMoreTask(PhoneRechargeOrderListActivity.this, mTypeOrder, mCurrentPage, mPageSize,
                mOrderStatus, profileId);
        loadMoreTask.execute();
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
            this.finish();
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
        case 6:
            mOrderStatus = 0;
            mCurrentPage = 1;
            titleTextView.setText(R.string.phone_recharge_order);
            setData();
            break;
        case 2:
            if (data != null && data.getStringExtra(GlobalConfig.GOHOME).equals(GlobalConfig.GO_HOME)) {
                Intent intent = new Intent();
                intent.putExtra(GlobalConfig.GOHOME, GlobalConfig.GO_HOME);
                setResult(2, intent);
                this.finish();
            }
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
                    listView.setSelection(0);
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
        if(mData!=null){
            mData.clear();
            mData = null;
        }
        super.onDestroy();
        BDebug.d(TAG, "==onDestroy==");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CommonUtility.showMiddleToast(PhoneRechargeOrderListActivity.this, null, position + "");
        // if (view != mFootView) {
        // mData = mOrderAdapter.getOrders();
        // Intent intent = new Intent(PhoneRechargeOrderListActivity.this, PhoneRechargeOrderDetailActivity.class);
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
        // startActivity(intent) ;
        // startActivityForResult(intent, 0);
        // }

    }
}
