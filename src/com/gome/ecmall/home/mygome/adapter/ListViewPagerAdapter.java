package com.gome.ecmall.home.mygome.adapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.OrderList;
import com.gome.ecmall.bean.OrderList.Order;
import com.gome.ecmall.home.mygome.MyOrderAdapter;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.ecmall.util.PreferenceUtils;
import com.gome.eshopnew.R;

/**
 * 订单类别改版
 * 
 * @author qinxudong
 * 
 */
public class ListViewPagerAdapter extends PagerAdapter implements OnScrollListener {

    private Activity activity;
    private Context context;
    private List<LoadDataTask> loadTaskList = new LinkedList<LoadDataTask>();

    // 页面个数
    private int length;
    /** 当前页签 */
    private int selectNum = 0;

    /** 标题状态 全部订单(默认)：0；待支付订单：1；收货待确认订单：2； */
    private int titleStatu;
    /** 订单时限（类型）typeOrder 订单时限(1=一个月内、2=一个月前、3=老用户)；0:默认全部（包括待支付订单、收货待确认订单） */

    private int[] typeOrders;

    /** 筛选标识 ，默认为false */
    private boolean typeFlag = false;

    /** 无更多订单 */
    private String[] no_more;

    /**
     * 为了同步数据，当前页正在加载数据时，不允许使用筛选
     * <p>
     * 有3中状态 默认状态：0；正在加载状态：1；加载完成状态：2 在viewPager 切换标签时充值当前页的状态为默认值
     */
    private String[] status;

    private int[] currents;

    /** 存放footview 的数组 */
    private View[] footViews;

    public String getStatu(int selectNum) {
        return status[selectNum];
    }

    public void setStatu(int selectNum, String statu) {
        status[selectNum] = statu;
    }

    /**
     * 筛选 传入viewpager 的当前视图
     * 
     * @param typeOrder
     * @param childView
     *            viewpager 的当前视图
     */
    public void setTypeOrder(int typeOrder, View childView, int position) {
        typeOrders[position] = typeOrder;
        currents[position] = 1;
        ListView listView = (ListView) childView.findViewById(R.id.new_my_order_list);
        ProgressBar progressBar = (ProgressBar) childView.findViewById(R.id.new_my_order_progressbar);
        typeFlag = true;
        LoadDataTask loadDataTask = new LoadDataTask(progressBar, listView, position);
        loadDataTask.execute();
        addTaskToList(loadDataTask);
    }

    public ListViewPagerAdapter(Activity activity, int title, int length) {
        this.activity = activity;
        this.titleStatu = title;
        no_more = this.activity.getResources().getStringArray(R.array.no_more_order);
        this.length = length;
        status = new String[this.length];
        currents = new int[this.length];
        footViews = new View[this.length];
        typeOrders = new int[this.length];
        for (int i = 0; i < this.length; i++) {
            status[i] = "0";
            currents[i] = 1;
            footViews[i] = null;
            if (i == 1) {
                typeOrders[i] = 4;
            } else {
                typeOrders[i] = 1;
            }
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        context = container.getContext();

        View mFootView = LayoutInflater.from(context).inflate(R.layout.common_loading_layout, null);
        mFootView.setClickable(false);
        mFootView.setFocusable(false);
        mFootView.setFocusableInTouchMode(false);

        footViews[position] = mFootView;

        View view = LayoutInflater.from(context).inflate(R.layout.new_my_order_view, container, false);
        ListView listView = (ListView) view.findViewById(R.id.new_my_order_list);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.new_my_order_progressbar);
        LoadDataTask loadDataTask = new LoadDataTask(progressBar, listView, position);
        loadDataTask.execute();
        listView.setOnScrollListener(this);
        addTaskToList(loadDataTask);
        view.setTag(position + "");
        container.addView(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return length;

    }

    void addTaskToList(LoadDataTask task) {
        if (loadTaskList.size() < 2) {
            loadTaskList.add(task);
        } else {
            LoadDataTask lTask = loadTaskList.get(0);
            if (lTask != null) {
                lTask.cancel(true);
            }
            loadTaskList.remove(0);
            loadTaskList.add(task);
        }
    }

    class LoadDataTask extends AsyncTask<Void, Void, ArrayList<Order>> {

        private ProgressBar progressBar;
        private ListView listView;
        private MyOrderAdapter adapter;
        private int position;
        private String requestJson;
        private String goodsType = "";

        public LoadDataTask(ProgressBar progressBar, ListView listView, int position) {

            if (position == 1) {
                goodsType = MyOrderAdapter.GOODSTYPE;
            } else if (position == 2) {
                goodsType = MyOrderAdapter.GROUPBUY;
            } else {
                goodsType = "";
            }

            this.progressBar = progressBar;
            this.listView = listView;
            this.position = position;
            progressBar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }

        public LoadDataTask(ListView listView, int position) {
            this.listView = listView;
            HeaderViewListAdapter headAdapter = (HeaderViewListAdapter) listView.getAdapter();
            adapter = (MyOrderAdapter) headAdapter.getWrappedAdapter();
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            status[position] = "1";
            if (position == 0) {// 普通订单
                requestJson = OrderList.createRequest(typeOrders[position], currents[position], 10, titleStatu, 0);
            } else if (position == 2) {// 团购订单
                requestJson = OrderList.createRequest(typeOrders[position], currents[position], 10, titleStatu, 1);
            }

            switch (titleStatu) {
            case 1:// 待支付订单
                if (position == 1) {
                    String profileId = PreferenceUtils.getInstance(context.getApplicationContext()).getString(JsonInterface.JK_PROFILE_ID, "");
                    requestJson = OrderList
                            .createPhoneRechargeOrderListRequest(3, currents[position], 10, 0, profileId);
                }
                break;
            case 2:// 待确认收货订单
                break;
            case 0:// 全部订单
                if (position == 1) {// 手机充值请求
                    String profileId = PreferenceUtils.getInstance(context.getApplicationContext()).getString(JsonInterface.JK_PROFILE_ID, "");
                    requestJson = OrderList.createPhoneRechargeOrderListRequest(typeOrders[position],
                            currents[position], 10, 0, profileId);
                }
                break;
            }

        }

        @Override
        protected ArrayList<Order> doInBackground(Void... params) {
            String result = "";

            if (position == 0) {
                result = NetUtility.sendHttpRequestByPost(Constants.URL_PROFILE_ORDER_LIST, requestJson);
                return OrderList.parseJson(result, "");
            } else if (position == 1) {
                result = NetUtility.sendHttpRequestByPost(Constants.PHONE_RECHARGE_ORDER_URL, requestJson);
                return OrderList.parseJson(result, OrderList.ORDERTYPE);
            } else {
                result = NetUtility.sendHttpRequestByPost(Constants.URL_PROFILE_ORDER_LIST, requestJson);
                return OrderList.parseJson(result, "");
            }

            // return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Order> result) {

            super.onPostExecute(result);
            status[position] = "2";

            if (progressBar != null)
                progressBar.setVisibility(View.GONE);

            if (listView.getFooterViewsCount() == 0) {
                listView.addFooterView(footViews[position]);
            }

            if (adapter == null) {
                ArrayList<Order> orders = new ArrayList<Order>();
                adapter = new MyOrderAdapter(activity, orders, goodsType);
            }

            if (listView.getAdapter() == null || typeFlag) {
                listView.setAdapter(adapter);
                typeFlag = false;
            }

            listView.setVisibility(View.VISIBLE);

            if (result == null) {
                if (adapter != null) {
                    adapter.clear();
                }
                ((TextView) footViews[position].findViewById(R.id.common_loading_title))
                        .setText(R.string.data_load_fail_exception);
                ((ProgressBar) footViews[position].findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                return;
            } else {
                if (typeFlag) {
                    adapter.reload(result);
                } else {
                    adapter.addItem(result);
                }
            }

            if (result.size() == Constants.PAGE_SIZE) {
                adapter.hasMore = true;
            } else {
                adapter.hasMore = false;
            }

            if (adapter.hasMore) {// 有更多商品可加载
                ((TextView) footViews[position].findViewById(R.id.common_loading_title)).setText(R.string.load_more);
                ((ProgressBar) footViews[position].findViewById(R.id.loadingbar)).setVisibility(View.VISIBLE);
            } else {//
                if (listView.getFooterViewsCount() != 0) {
                    if (adapter.getCount() == 0) {
                        ((TextView) footViews[position].findViewById(R.id.common_loading_title))
                                .setText(no_more[position]);
                        ((ProgressBar) footViews[position].findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                    } else {
                        HeaderViewListAdapter headAdapter = (HeaderViewListAdapter) listView.getAdapter();
                        headAdapter.removeFooter(footViews[position]);
                    }

                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progressBar = null;
            listView = null;
            adapter = null;
        }

    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount && (totalItemCount - 1) % 10 == 0
                && totalItemCount > 10) {
            MyOrderAdapter adapter = (MyOrderAdapter) ((HeaderViewListAdapter) view.getAdapter()).getWrappedAdapter();
            if (adapter.hasMore) {
                adapter.hasMore = false;
                currents[selectNum]++;
                LoadDataTask loadDataTask = new LoadDataTask((ListView) view, selectNum);
                loadDataTask.execute();
                addTaskToList(loadDataTask);
            }

        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    public int getSelectNum() {
        return selectNum;
    }

    public void setSelectNum(int selectNum) {
        this.selectNum = selectNum;
    }

}
