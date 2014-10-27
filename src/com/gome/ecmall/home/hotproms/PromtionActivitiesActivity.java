package com.gome.ecmall.home.hotproms;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gome.ecmall.bean.ActivityEntity;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.util.AppMeasurementUtils;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 活动专题
 * 
 * @author Administrator
 * 
 */
public class PromtionActivitiesActivity extends AbsSubActivity implements OnScrollListener, OnClickListener {
    private Button btnBack;
    private TextView textTitle;
    private ListView listView;
    private TextView empty;
    private LinearLayout loadView;
    private LinearLayout layoutMain;
    private LinearLayout layoutNonNet;
    private PromtionActivitiesAdapter mAdapter;

    private int mCurrentPage = 1;
    private static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private boolean hasMore = true;
    private Time t;//24点以后刷新用
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promtion_activities);
        initView();
        loadData();
        t = new Time();
        t.setToNow();
        int year = t.year;
        int month = t.month;
        int monthday = t.monthDay;
        t.set(0, 0, 24, monthday, month, year);
    }

    private void initView() {
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setText(R.string.back);
        btnBack.setOnClickListener(this);

        textTitle = (TextView) findViewById(R.id.common_title_tv_text);
        textTitle.setText(R.string.promtion_activities);
        textTitle.setVisibility(View.VISIBLE);

        listView = (ListView) findViewById(R.id.promtion_activities_list);
        listView.setOnScrollListener(this);
        empty = (TextView) findViewById(android.R.id.empty);
        loadView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.common_loading_layout, null);
        layoutMain = (LinearLayout) findViewById(R.id.promtion_activities_layout);
        layoutNonNet = (LinearLayout) findViewById(R.id.non_net_layout);
    };

    private AsyncTask<Void, Void, ArrayList<ActivityEntity>> asyncTask = null;

    private void loadData() {
        mCurrentPage = 1;
        if (asyncTask != null) {
            asyncTask.cancel(true);
            asyncTask = null;
        }

        new LoadTask(PromtionActivitiesActivity.this, mCurrentPage, PAGE_SIZE).execute();

    }

    private class LoadTask extends AsyncTask<Void, Void, ArrayList<ActivityEntity>> {

        private Dialog dialog;
        private Context mContext;
        private int currentPage;
        private int pageSize;

        public LoadTask(Context context, int currentPage, int pageSize) {
            mContext = context;
            this.currentPage = currentPage;
            this.pageSize = pageSize;
        }

        @Override
        protected void onPreExecute() {
            boolean isNetAvaiable = NetUtility.isNetworkAvailable(mContext);
            if (!isNetAvaiable) {
                layoutNonNet.setVisibility(View.VISIBLE);
                return;
            }

            dialog = CommonUtility.showLoadingDialog(mContext, mContext.getString(R.string.loading), true,
                    new OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            cancel(true);
                        }
                    });
        }

        @Override
        protected ArrayList<ActivityEntity> doInBackground(Void... params) {
            String json = ActivitiesService.createJson(currentPage, pageSize);
            String result = NetUtility.sendHttpRequestByPost(Constants.URL_ACTIVITIES_ACTIVITIES_LIST, json);

            if (result == null || result.length() == 0) {
                return null;
            }

            return ActivitiesService.parseJsonActivityEntityList(result);
        }

        @Override
        protected void onPostExecute(ArrayList<ActivityEntity> result) {
            super.onPostExecute(result);
            if (PromtionActivitiesActivity.this != null && !PromtionActivitiesActivity.this.isFinishing()
                    && dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }

            if (isCancelled()) {
                cancel(true);
            }

            ArrayList<ActivityEntity> entities = result;
            if (result == null) {
                entities = new ArrayList<ActivityEntity>();
                CommonUtility.showMiddleToast(mContext, null, mContext.getString(R.string.data_load_fail_exception));
                return;
            } else {
                entities = result;
            }

            if (result.size() < Constants.PAGE_SIZE) {
                hasMore = false;
                if (listView.getFooterViewsCount() > 0) {
                    listView.removeFooterView(loadView);
                    // ((TextView) loadView.findViewById(R.id.common_loading_title)).setText(R.string.no_more);
                    // ((ProgressBar) loadView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                }
            } else {
                hasMore = true;
                if (listView.getFooterViewsCount() == 0) {
                    listView.addFooterView(loadView);
                } else {
                    ((TextView) loadView.findViewById(R.id.common_loading_title)).setText(R.string.load_more);
                    ((ProgressBar) loadView.findViewById(R.id.loadingbar)).setVisibility(View.VISIBLE);
                }
                mCurrentPage++;
            }
            if(mAdapter == null){
            	mAdapter = new PromtionActivitiesAdapter(PromtionActivitiesActivity.this);
            	listView.setAdapter(mAdapter);
            }
            setData(entities);
        }
    }

    private void setData(ArrayList<ActivityEntity> list) {
        mAdapter.appendToList(list);
    }

    private AsyncTask<Void, Void, ArrayList<ActivityEntity>> loadMoreAsyncTask = null;

    private void loadMore() {
        if (loadMoreAsyncTask != null) {
            return;
        }
        loadMoreAsyncTask = new AsyncTask<Void, Void, ArrayList<ActivityEntity>>() {

            @Override
            protected ArrayList<ActivityEntity> doInBackground(Void... params) {

                String json = ActivitiesService.createJson(mCurrentPage, PAGE_SIZE);
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_ACTIVITIES_ACTIVITIES_LIST, json);

                if (result == null || result.length() == 0) {
                    return null;
                }

                return ActivitiesService.parseJsonActivityEntityList(result);
            }

            @Override
            protected void onPostExecute(ArrayList<ActivityEntity> result) {
                if (result == null) {
                    CommonUtility.showMiddleToast(PromtionActivitiesActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                if (result.size() < Constants.PAGE_SIZE) {
                    hasMore = false;
                    if (listView.getFooterViewsCount() > 0) {
                        listView.removeFooterView(loadView);
                        // ((TextView) loadView.findViewById(R.id.common_loading_title)).setText(R.string.no_more);
                        // ((ProgressBar) loadView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                    }
                } else {
                    hasMore = true;
                    mCurrentPage++;
                    if (listView.getFooterViewsCount() == 0) {
                        listView.addFooterView(loadView);
                    } else {
                        ((TextView) loadView.findViewById(R.id.common_loading_title)).setText(R.string.load_more);
                        ((ProgressBar) loadView.findViewById(R.id.loadingbar)).setVisibility(View.VISIBLE);
                    }
                }
                mAdapter.appendToList(result);
                loadMoreAsyncTask = null;
            }
        };
        loadMoreAsyncTask.execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        AppMeasurementUtils appMeasurementProm = new AppMeasurementUtils(PromtionActivitiesActivity.this);
        appMeasurementProm.getUrl(getString(R.string.appMeas_hotProm) + ":"
                + getString(R.string.appMeas_projectactivitys), getString(R.string.appMeas_hotProm),
                getString(R.string.appMeas_hotProm) + ":" + getString(R.string.appMeas_projectactivitys),
                getString(R.string.appMeas_projectactivitys), "", "", "", "", "", "", "", "",
                getString(R.string.appMeas_myprom), "", "", "", null);
        Time t2 = new Time();
        t2.setToNow();
        boolean isorNo = t2.after(t);
        if (isorNo) {
            loadData();
            BDebug.d("liuyang", "24点以后刷新执行了");
            t.set(0, 0, 24, t2.monthDay, t2.month, t2.year);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
            if (hasMore && mAdapter != null && mAdapter.getCount() > 0) {
                loadMore();
            }
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.common_title_btn_back:
            goback();
            break;

        default:
            break;
        }

    }
}
