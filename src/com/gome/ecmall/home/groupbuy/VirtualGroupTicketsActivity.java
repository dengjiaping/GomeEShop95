package com.gome.ecmall.home.groupbuy;

import java.util.ArrayList;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gome.ecmall.bean.VirtualGroupTickets;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.custom.SegmentTabs;
import com.gome.ecmall.custom.SegmentTabs.OnTabSelectChangedListener;
import com.gome.ecmall.home.mygome.CouponRuleDetailActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;
/**
 * 新版团购团购劵
 * @author liuyang-ds
 *
 */
public class VirtualGroupTicketsActivity extends Activity implements OnClickListener {
    private Button bt_back;
    private TextView tv_title;
    private Button bt_right;
    private LinearLayout ll_virtual_type_tickets_segments_layout;
    private SegmentTabs virtual_type_tickets_segments;
    private LinearLayout ll_expire_tickets;
    private ListView lv_tickets_already_used;
    private ListView lv_tickets_no_used;
    private TextView tv_no_data_used;
    private TextView tv_no_data_not_used;
    private VirtualGroupTicketsAdapter usedAdapter;
    private LinearLayout loadingView1;// 加载更多view
    private TextView tv_loadMore1;// 加载更多里的textView
    private ProgressBar pb_loadMore1;// 加载更多里的动画
    private boolean hasMoreData1;// 是否还有更多

    private VirtualGroupTicketsAdapter notUsedAdapter;
    private LinearLayout loadingView2;// 加载更多view
    private TextView tv_loadMore2;// 加载更多里的textView
    private ProgressBar pb_loadMore2;// 加载更多里的动画
    private boolean hasMoreData2;// 是否还有更多
    private int currentPageAlreadyUsed;
    private int currentPageNotUsed;
    private int currentPage;
    private String orderId;
    private String status;
    public static String no_used_isHaveExpiring = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.virtual_group_tickets);
        orderId = getIntent().getStringExtra(VirtualGroupTickets.JK_ORDERID);
        initializeViews();// 初始化控件
        setData(2);// 设置数据1：已使用；2：未使用。 默认 2
        isHaveExpiring();//是否有即将过期的团购劵
    }

    private void isHaveExpiring() {
        if (!NetUtility.isNetworkAvailable(VirtualGroupTicketsActivity.this)) {
            CommonUtility.showMiddleToast(VirtualGroupTicketsActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            ll_expire_tickets.setVisibility(View.GONE);
            return;
        }
        new AsyncTask<Object, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Object... params) {
                String response = NetUtility.sendHttpRequestByPost(
                        Constants.URL_NEW_GROUPBUY_GROUP_TICKET_ISHAVE_EXPIRING_SOON, null);
                if (NetUtility.NO_CONN.equals(response)) {
                    return false;
                }
                return VirtualGroupTickets.parseIsHaveExpiring(response);
            }

            protected void onPostExecute(Boolean result) {
                if (result) {
                    ll_expire_tickets.setVisibility(View.VISIBLE);
                } else {
                    ll_expire_tickets.setVisibility(View.GONE);
                }

            };
        }.execute();

    }

    /**
     * 设置数据
     */
    private void setData(final int flag) {
        if (!NetUtility.isNetworkAvailable(VirtualGroupTicketsActivity.this)) {
            CommonUtility.showMiddleToast(VirtualGroupTicketsActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            lv_tickets_already_used.setVisibility(View.GONE);
            lv_tickets_no_used.setVisibility(View.GONE);
            tv_no_data_used.setVisibility(View.GONE);
            tv_no_data_not_used.setVisibility(View.GONE);
            ll_expire_tickets.setVisibility(View.GONE);
            return;
        }
        // 获取列表
        if (flag == 1) {
            currentPageAlreadyUsed = 1;
            currentPage = currentPageAlreadyUsed;
            status = "1";
        } else if (flag == 2) {
            currentPageNotUsed = 1;
            currentPage = currentPageNotUsed;
            status = "2";
        }

        new AsyncTask<Object, Void, ArrayList<VirtualGroupTickets>>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(VirtualGroupTicketsActivity.this,
                        getString(R.string.loading), true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected ArrayList<VirtualGroupTickets> doInBackground(Object... params) {
                String request = VirtualGroupTickets.createRequestTicketsJson(currentPage, Constants.PAGE_SIZE,
                        orderId, status);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_NEW_GROUPBUY_GROUP_TICKETS, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return VirtualGroupTickets.parseTicketsList(response);
            }

            @Override
            protected void onPostExecute(final ArrayList<VirtualGroupTickets> result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                if (result == null) {
                    CommonUtility.showMiddleToast(VirtualGroupTicketsActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    lv_tickets_already_used.setVisibility(View.GONE);
                    lv_tickets_no_used.setVisibility(View.GONE);
                    if (flag == 1) {
                        tv_no_data_used.setVisibility(View.VISIBLE);
                        tv_no_data_used.setText("暂无已使用的团购劵");
                    } else if (flag == 2) {
                        tv_no_data_not_used.setVisibility(View.VISIBLE);
                        tv_no_data_not_used.setText("暂无未使用的团购劵");
                    }
                    return;
                }else if(result != null && result.size() == 0){
                    lv_tickets_already_used.setVisibility(View.GONE);
                    lv_tickets_no_used.setVisibility(View.GONE);
                    if (flag == 1) {
                        tv_no_data_used.setVisibility(View.VISIBLE);
                        tv_no_data_used.setText("暂无已使用的团购劵");
                    } else if (flag == 2) {
                        tv_no_data_not_used.setVisibility(View.VISIBLE);
                        tv_no_data_not_used.setText("暂无未使用的团购劵");
                    }
                    return;
                }
                isAutomaticLoad(result, flag);// 判断是否还有更多数据
                if (flag == 1) {
                    lv_tickets_already_used.setVisibility(View.VISIBLE);
                    if (usedAdapter == null) {
                        usedAdapter = new VirtualGroupTicketsAdapter(VirtualGroupTicketsActivity.this, result);
                        lv_tickets_already_used.setAdapter(usedAdapter);
                    } else {
                        usedAdapter.reload(result);
                    }

                } else if (flag == 2) {
                    lv_tickets_no_used.setVisibility(View.VISIBLE);
                    if (notUsedAdapter == null) {
                        notUsedAdapter = new VirtualGroupTicketsAdapter(VirtualGroupTicketsActivity.this, result);
                        lv_tickets_no_used.setAdapter(notUsedAdapter);
                    } else {
                        notUsedAdapter.reload(result);
                    }

                }

            }

        }.execute();
    }

    // 加载更多
    private AsyncTask<Object, Void, ArrayList<VirtualGroupTickets>> asyncTask = null;

    private void loadMoreData(final int flag) {
        if (!NetUtility.isNetworkAvailable(VirtualGroupTicketsActivity.this)) {
            CommonUtility.showMiddleToast(VirtualGroupTicketsActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        if (asyncTask != null) {
            return;
        }
        if (flag == 1) {
            currentPage = currentPageAlreadyUsed;
            status = "1";
        } else if (flag == 2) {
            currentPage = currentPageNotUsed;
            status = "2";
        }
        asyncTask = new AsyncTask<Object, Void, ArrayList<VirtualGroupTickets>>() {
            @Override
            protected ArrayList<VirtualGroupTickets> doInBackground(Object... params) {
                String request = VirtualGroupTickets.createRequestTicketsJson(currentPage, Constants.PAGE_SIZE,
                        orderId, status);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_NEW_GROUPBUY_GROUP_TICKETS, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return VirtualGroupTickets.parseTicketsList(response);
            }

            @Override
            protected void onCancelled() {
                asyncTask = null;
            }

            @Override
            protected void onPostExecute(final ArrayList<VirtualGroupTickets> result) {
                if (isCancelled()) {
                    return;
                }
                if (result == null) {
                    CommonUtility.showMiddleToast(VirtualGroupTicketsActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                isAutomaticLoad(result, flag);// 判断是否还有更多数据
                if (flag == 1) {
                    lv_tickets_already_used.setVisibility(View.VISIBLE);
                    usedAdapter.addlist(result);

                } else if (flag == 2) {
                    lv_tickets_no_used.setVisibility(View.VISIBLE);
                    notUsedAdapter.addlist(result);

                }
                asyncTask = null;
            }
        };
        asyncTask.execute();
    }

    // 判断是否还有更多数据
    private void isAutomaticLoad(ArrayList<VirtualGroupTickets> result, int flag) {
        switch (flag) {
        case 1:
            loadingView1.setVisibility(View.VISIBLE);
            currentPageAlreadyUsed++;
            if (result.size() < Constants.PAGE_SIZE) {// 没有商品可取
                hasMoreData1 = false;
                if (lv_tickets_already_used.getFooterViewsCount() == 0) {
                    lv_tickets_already_used.addFooterView(loadingView1);
                }
                tv_loadMore1.setText(R.string.no_more);
                tv_loadMore1.setVisibility(View.VISIBLE);
                pb_loadMore1.setVisibility(View.GONE);
            } else {// 还可以继续取
                hasMoreData1 = true;
                tv_loadMore1.setVisibility(View.VISIBLE);
                tv_loadMore1.setText(R.string.load_more);
                pb_loadMore1.setVisibility(View.VISIBLE);
                if (lv_tickets_already_used.getFooterViewsCount() == 0) {
                    lv_tickets_already_used.addFooterView(loadingView1);
                }
            }
            break;
        case 2:
            loadingView2.setVisibility(View.VISIBLE);
            currentPageNotUsed++;
            if (result.size() < Constants.PAGE_SIZE) {// 没有商品可取
                hasMoreData2 = false;
                if (lv_tickets_no_used.getFooterViewsCount() == 0) {
                    lv_tickets_no_used.addFooterView(loadingView2);
                }
                tv_loadMore2.setText(R.string.no_more);
                tv_loadMore2.setVisibility(View.VISIBLE);
                pb_loadMore2.setVisibility(View.GONE);
            } else {// 还可以继续取
                hasMoreData2 = true;
                tv_loadMore2.setVisibility(View.VISIBLE);
                tv_loadMore2.setText(R.string.load_more);
                pb_loadMore2.setVisibility(View.VISIBLE);
                if (lv_tickets_no_used.getFooterViewsCount() == 0) {
                    lv_tickets_no_used.addFooterView(loadingView2);
                }
            }
            break;

        default:
            break;
        }

    }

    /**
     * 初始化控件
     */
    private void initializeViews() {
        bt_back = (Button) this.findViewById(R.id.common_title_btn_back);
        tv_title = (TextView) this.findViewById(R.id.common_title_tv_text);
        bt_right = (Button) this.findViewById(R.id.common_title_btn_right);
        lv_tickets_already_used = (ListView) this.findViewById(R.id.lv_tickets_already_used);
        lv_tickets_no_used = (ListView) this.findViewById(R.id.lv_tickets_no_used);
        tv_no_data_used = (TextView) this.findViewById(R.id.tv_no_data_used);
        tv_no_data_not_used = (TextView) this.findViewById(R.id.tv_no_data_not_used);
        virtual_type_tickets_segments = (SegmentTabs) findViewById(R.id.virtual_type_tickets_segments);
        virtual_type_tickets_segments.setTabSelectChangedListener(new TabSelectListener());
        virtual_type_tickets_segments.setSelected(-1);
        ll_virtual_type_tickets_segments_layout = (LinearLayout) findViewById(R.id.ll_virtual_type_tickets_segments_layout);
        ll_expire_tickets = (LinearLayout) findViewById(R.id.ll_expire_tickets);

        loadingView1 = (LinearLayout) View.inflate(this, R.layout.common_loading_layout, null);
        tv_loadMore1 = (TextView) loadingView1.findViewById(R.id.common_loading_title);
        pb_loadMore1 = (ProgressBar) loadingView1.findViewById(R.id.loadingbar);

        loadingView2 = (LinearLayout) View.inflate(this, R.layout.common_loading_layout, null);
        tv_loadMore2 = (TextView) loadingView2.findViewById(R.id.common_loading_title);
        pb_loadMore2 = (ProgressBar) loadingView2.findViewById(R.id.loadingbar);

        bt_back.setVisibility(View.VISIBLE);
        bt_back.setText("返回");
        bt_back.setOnClickListener(this);
        bt_right.setVisibility(View.VISIBLE);
        bt_right.setText("使用帮助");
        bt_right.setOnClickListener(this);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText("团购劵");
        lv_tickets_already_used.setOnScrollListener(new AlreadyUsedScrollListener());
        lv_tickets_no_used.setOnScrollListener(new NotUsedScrollListener());

    }

    class TabSelectListener implements OnTabSelectChangedListener {

        @Override
        public void onTabSelectChanged(SegmentTabs tabs, View item, int lastIndex, int currentIndex) {
            tv_no_data_used.setVisibility(View.GONE);
            tv_no_data_not_used.setVisibility(View.GONE);
            if (currentIndex == 0) {
                // 未使用
                lv_tickets_already_used.setVisibility(View.GONE);
                lv_tickets_no_used.setVisibility(View.VISIBLE);
                if (notUsedAdapter == null) {
                    setData(2);
                }
            } else if (currentIndex == 1) {
                // 已使用
                lv_tickets_already_used.setVisibility(View.VISIBLE);
                lv_tickets_no_used.setVisibility(View.GONE);
                if (usedAdapter == null) {
                    setData(1);
                }

            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.common_title_btn_back:
            // 我的国美
            finish();
            break;
        case R.id.common_title_btn_right:
          //团购卷使用流程
            Intent intent = new Intent(VirtualGroupTicketsActivity.this,CouponRuleDetailActivity.class);
            intent.putExtra(CouponRuleDetailActivity.TYPE, "2");
            startActivity(intent);
            break;

        default:
            break;
        }
    }

    /**
     * 已经使用的列表滑动监听
     * 
     * @author liuyang-ds
     * 
     */
    public class AlreadyUsedScrollListener implements OnScrollListener {

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem + visibleItemCount == totalItemCount) {// 滑动到底部
                if (hasMoreData1 && usedAdapter != null && usedAdapter.getCount() > 0) {
                    loadMoreData(1);
                }
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

    }

    /**
     * 未使用的列表滑动监听
     * 
     * @author liuyang-ds
     * 
     */
    public class NotUsedScrollListener implements OnScrollListener {

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem + visibleItemCount == totalItemCount) {// 滑动到底部
                if (hasMoreData2 && notUsedAdapter != null && notUsedAdapter.getCount() > 0) {
                    loadMoreData(2);
                }
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

    }
}
