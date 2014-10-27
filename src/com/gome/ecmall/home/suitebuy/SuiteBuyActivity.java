package com.gome.ecmall.home.suitebuy;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.SuiteBuy;
import com.gome.ecmall.bean.SuiteBuyEntity;
import com.gome.ecmall.bean.SuiteBuyFilter;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.custom.SuiteFilterDialog;
import com.gome.ecmall.custom.VerticalViewPager;
import com.gome.ecmall.custom.VerticalViewPager.OnPageChangeListener;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.util.AppMeasurementUtils;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 套购列表页 <br>
 * 筛选后为SuiteBuyFilterActivity类似结构 如修改，两处都对等修改 对应的筛选对话框为SuiteFilterDialog
 */
public class SuiteBuyActivity extends AbsSubActivity implements OnClickListener, OnPageChangeListener {
    private static final String TAG = "SuiteBuyActivity";
    private Button mBackBtn;
    private TextView mTitleText;
    private Button mFilterBtn;
    private LayoutInflater mInflater;
    private SuiteFilterDialog filterDialog = null;
    private VerticalViewPager mViewPager;
    private SuiteBuyAdapter mAdapter;
    private LinearLayout mFooterView;
    private boolean mSettleState;
    private boolean mIsLast;
    private LinearLayout mLayout;

    private LinearLayout nonNetLayout;
    private TextView empty;
    private int mCurrentPage = 1;
    private String mSelectIndex;
    public boolean hasMoreData;
    public static final String SUITE_BUY_ALL = "0";
    public static final String IS_FIRST_SUITE_BUY = "isFirstSuiteBuy";
    public static final String INTENT_KEY_SELECT_INDEX = "selectIndex";
    public static final String INTENT_KEY_FILTER_LIST = "filterList";
    public static final int LOAD_SUCCESS = 1000;
    private Timer timer;
    private TimerTask task = new TimerTask() {

        @Override
        public void run() {
            setDisplayList();
            handler.sendMessage(handler.obtainMessage(LOAD_SUCCESS));
        }
    };
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == LOAD_SUCCESS) {
                msg.getTarget().removeMessages(LOAD_SUCCESS);
                mAdapter.notifyDataSetChanged();
            }
        }

    };

    private void setDisplayList() {
        for (SuiteBuyEntity suiteBuyEntity : mAdapter.getMList()) {
            long time = suiteBuyEntity.getDelayTime();
            time -= 60;
            suiteBuyEntity.setDelayTime(time);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suite_buy_main);
        initView();
        reload(mSelectIndex);
    }

    @Override
    protected void onResume() {

        super.onResume();
        AppMeasurementUtils appMeasurementProm = new AppMeasurementUtils(SuiteBuyActivity.this);
        appMeasurementProm.getUrl(getString(R.string.appMeas_actiCenter) + ":" + getString(R.string.appMeas_suitebuy),
                getString(R.string.appMeas_actiCenter), getString(R.string.appMeas_actiCenter) + ":"
                        + getString(R.string.appMeas_suitebuy), getString(R.string.appMeas_suitebuy), "", "", "", "",
                "", "", "", "", getString(R.string.appMeas_myprom), "", "", "", null);
    }

    private void initView() {
        mInflater = LayoutInflater.from(this);
        mLayout = (LinearLayout) findViewById(R.id.suite_buy_layout);
        mBackBtn = (Button) findViewById(R.id.common_title_btn_back);
        mBackBtn.setVisibility(View.VISIBLE);
        mBackBtn.setText(R.string.back);
        mBackBtn.setOnClickListener(this);

        mTitleText = (TextView) findViewById(R.id.common_title_tv_text);
        mTitleText.setText(R.string.suite_buy_group);
        mTitleText.setVisibility(View.VISIBLE);

        mFilterBtn = (Button) findViewById(R.id.common_title_btn_right);
        mFilterBtn.setText(R.string.filter);
        mFilterBtn.setVisibility(View.VISIBLE);
        mFilterBtn.setOnClickListener(this);

        mViewPager = (VerticalViewPager) findViewById(R.id.suite_buy_vertical_view_pager);
        mFooterView = (LinearLayout) mInflater.inflate(R.layout.common_loading_layout, null);
        empty = (TextView) findViewById(R.id.empty);
        nonNetLayout = (LinearLayout) findViewById(R.id.non_net_layout);

        if (isFirstSuite()) {
            showFirstNotice(this);
            setFirstSuite();
        }
        timer = new Timer();
    }

    public void showFirstNotice(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.suite_buy_look_more_dialog, null);
        Toast toast = new Toast(context);
        toast.setView(view);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    public boolean isFirstSuite() {
        SharedPreferences prefs = getSharedPreferences(GlobalConfig.GOME_PREFS, 0);
        return prefs.getBoolean(IS_FIRST_SUITE_BUY, true);
    }

    public void setFirstSuite() {
        SharedPreferences prefs = getSharedPreferences(GlobalConfig.GOME_PREFS, 0);
        Editor edit = prefs.edit();
        edit.putBoolean(IS_FIRST_SUITE_BUY, false);
        edit.commit();
    }

    private LoadTask loadTask = null;

    public void reload(String selectIndex) {
        if (!isNetConn()) {
            nonNetLayout.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.GONE);
            empty.setVisibility(View.GONE);
            return;
        }
        if (loadTask != null) {
            loadTask.cancel(true);
            loadTask = null;
        }
        mCurrentPage = 1;
        loadTask = new LoadTask(this, mCurrentPage, Constants.PAGE_SIZE, selectIndex);
        loadTask.execute();
    }

    private class LoadTask extends AsyncTask<Void, Void, SuiteBuy> {

        LoadingDialog dialog = null;
        Activity activity;

        private int currentPage;
        private int pageSize;
        private String selectIndex;

        public LoadTask(Activity act, int curPage, int pagSize, String index) {
            activity = act;
            currentPage = curPage;
            pageSize = pagSize;
            selectIndex = index;
        }

        @Override
        protected void onPreExecute() {
            dialog = CommonUtility.showLoadingDialog(activity, activity.getString(R.string.loading), true,
                    new OnCancelListener() {

                        @Override
                        public void onCancel(DialogInterface dialog) {
                            cancel(true);
                        }
                    });
        }

        @Override
        protected void onCancelled(SuiteBuy result) {
            cancel(true);
        }

        @Override
        protected SuiteBuy doInBackground(Void... params) {
            String request = SuiteBuyService.createJson(currentPage, pageSize, selectIndex);
            String response = NetUtility.sendHttpRequestByPost(Constants.URL_PRODUCT_SUITE_GOODS_LIST, request);
            if (response == null || JsonInterface.JV_FAIL.equals(response)) {
                return null;
            }
            return SuiteBuyService.parseJsonSuiteBuy(response);
        }

        @Override
        protected void onCancelled() {
            cancel(true);
        }

        @Override
        protected void onPostExecute(SuiteBuy result) {
            if (activity != null && !activity.isFinishing() && dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }

            if (result == null) {
                empty.setText(R.string.non_suite_buy);
                empty.setVisibility(View.VISIBLE);
                mViewPager.setVisibility(View.GONE);

                return;
            }

            empty.setVisibility(View.GONE);
            mViewPager.setVisibility(View.VISIBLE);
            nonNetLayout.setVisibility(View.GONE);

            ArrayList<SuiteBuyEntity> suiteList = result.getSuiteList();
            if (suiteList == null) {
                return;
            }
            if (suiteList.size() < pageSize) {
                if (mViewPager.getFooterViewCount() > 0) {
                    // mViewPager.removeFooterView(mFooterView);
                    ((TextView) mFooterView.findViewById(R.id.common_loading_title)).setText(R.string.no_more);
                    ((ProgressBar) mFooterView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                }
                hasMoreData = false;
            } else {
                if (mViewPager.getFooterViewCount() == 0) {
                    mViewPager.addFooterView(mFooterView);
                } else {
                    ((TextView) mFooterView.findViewById(R.id.common_loading_title)).setText(R.string.load_more);
                    ((ProgressBar) mFooterView.findViewById(R.id.loadingbar)).setVisibility(View.VISIBLE);
                }
                hasMoreData = true;
                mCurrentPage++;
            }
            mAdapter = new SuiteBuyAdapter(SuiteBuyActivity.this, suiteList);
            mViewPager.setAdapter(mAdapter);

            timer.schedule(task, 0, 1000 * 60);
            mViewPager.setOnPageChangeListener(SuiteBuyActivity.this);
        }

    }

    /** 网络是否已连接 */
    private boolean isNetConn() {
        if (!NetUtility.isNetworkAvailable(SuiteBuyActivity.this)) {
            CommonUtility.showMiddleToast(SuiteBuyActivity.this, null,
                    SuiteBuyActivity.this.getString(R.string.data_load_fail_please_check_network_settings));
            return false;

        }
        return true;
    }

    // private void setData(SuiteBuy suiteBuy) {
    // if (suiteBuy == null) {
    // return;
    // }
    // String titleName = suiteBuy.getSelectIndexName();
    // mTitleText.setText(titleName);
    //
    // mGallery.setPaddingHeight(5);
    // mGallery.setIsGalleryCircular(false);
    //
    // ArrayList<SuiteBuyEntity> list = suiteBuy.getSuiteList();
    // mAdapter = new SuiteBuyAdapter(SuiteBuyActivity.this, list);
    // mAdapter.setSuiteTitle(suiteBuy.getSelectIndexName());
    // mAdapter.setmGallery(mGallery);
    // mGallery.setAdapter(mAdapter);
    //
    // }

    // private float x1, x2;
    // private float y1, y2;
    //
    // @Override
    // public boolean onTouchEvent(MotionEvent event) {
    // if (event.getAction() == MotionEvent.ACTION_DOWN) {
    // x1 = event.getX();
    // y1 = event.getY();
    // }
    // if (event.getAction() == MotionEvent.ACTION_MOVE) {
    //
    // }
    // if (event.getAction() == MotionEvent.ACTION_UP) {
    // x2 = event.getX();
    // y2 = event.getY();
    // if (Math.abs(y1 - y2) < 6) {
    // if (mAdapter != null) {
    // mAdapter.showGoodImgDetail();
    // }
    // return false;// 距离较小，当作click事件来处理
    // }
    // if (mAdapter != null) {
    // int totalCount = mAdapter.getCount();
    // int currentPos = mGallery.getCurrentPosition();
    // if (currentPos == (totalCount - 1) && hasMoreData) {
    // LinearLayout lastLinearLayout = mAdapter
    // .getLastLinearLayout();
    // if (lastLinearLayout != null) {
    // lastLinearLayout.setVisibility(View.VISIBLE);
    // }
    // loadMore(mCurrentPage, Constants.PAGE_SIZE, mSelectIndex);
    // } else {
    // LinearLayout lastLinearLayout = mAdapter
    // .getLastLinearLayout();
    // if (lastLinearLayout != null) {
    // lastLinearLayout.setVisibility(View.GONE);
    // }
    // }
    // }
    // }
    //
    // return false;
    //
    // }

    private AsyncTask<Void, Void, SuiteBuy> asyncTask = null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        task.cancel();
        timer.cancel();

    }

    private void loadMore(final int currPage, final int pageSize, final String index) {
        // 判断网络，未连接返回，已连接加载
        if (!NetUtility.isNetworkAvailable(SuiteBuyActivity.this)) {
            return;
        }

        if (asyncTask != null && asyncTask.getStatus() != AsyncTask.Status.FINISHED) {
            return;
        }

        asyncTask = new AsyncTask<Void, Void, SuiteBuy>() {

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected SuiteBuy doInBackground(Void... params) {
                String json = SuiteBuyService.createJson(currPage, pageSize, index);
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_PRODUCT_SUITE_GOODS_LIST, json);
                if (result == null) {
                    return null;
                }
                return SuiteBuyService.parseJsonSuiteBuy(result);
            }

            @Override
            protected void onPostExecute(SuiteBuy result) {

                LinearLayout lastLinearLayout = mAdapter.getLastLinearLayout();
                if (lastLinearLayout != null) {
                    lastLinearLayout.setVisibility(View.GONE);
                }
                if (isCancelled()) {
                    return;
                }
                if (result == null) {
                    CommonUtility.showMiddleToast(SuiteBuyActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }

                ArrayList<SuiteBuyEntity> suiteList = result.getSuiteList();
                if (suiteList == null) {
                    return;
                } else {
                    if (suiteList.size() < Constants.PAGE_SIZE) {// 没有商品可取
                        if (mViewPager.getFooterViewCount() > 0) {
                            // mViewPager.removeFooterView(mFooterView);
                            ((TextView) mFooterView.findViewById(R.id.common_loading_title)).setText(R.string.no_more);
                            ((ProgressBar) mFooterView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                        }
                        hasMoreData = false;
                    } else {// 还可以继续取
                        if (mViewPager.getFooterViewCount() == 0) {
                            mViewPager.addFooterView(mFooterView);
                        } else {
                            ((TextView) mFooterView.findViewById(R.id.common_loading_title))
                                    .setText(R.string.load_more);
                            ((ProgressBar) mFooterView.findViewById(R.id.loadingbar)).setVisibility(View.VISIBLE);
                        }
                        hasMoreData = true;
                        mCurrentPage++;
                    }
                }
                mAdapter.addItem(suiteList);
            }
        };
        asyncTask.execute();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.common_title_btn_back:
            goback();
            break;
        case R.id.common_title_btn_right:
            if (filterDialog != null) {
                filterDialog.clearAllSelected();
                filterDialog.show();
            } else {
                showFilterDialog();
            }
            break;
        default:
            break;
        }
    }

    private void showFilterDialog() {
        if (!NetUtility.isNetworkAvailable(SuiteBuyActivity.this)) {
            CommonUtility.showMiddleToast(SuiteBuyActivity.this, null,
                    getString(R.string.data_load_fail_please_check_network_settings));
            return;
        }
        new AsyncTask<Void, Void, ArrayList<SuiteBuyFilter>>() {
            LoadingDialog dialog = null;

            protected void onPreExecute() {

                dialog = CommonUtility.showLoadingDialog(SuiteBuyActivity.this, getString(R.string.loading), true,
                        new OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            protected void onCancelled() {
                cancel(true);
            }

            @Override
            protected ArrayList<SuiteBuyFilter> doInBackground(Void... params) {
                String result = NetUtility.sendHttpRequestByGet(Constants.URL_PRODUCT_SUITE_CATEGORY);
                if (result == null) {
                    return null;
                }
                ArrayList<SuiteBuyFilter> list = SuiteBuyService.parseJsonSuiteBuyFilter(result);
                if (list != null) {
                    SuiteBuyFilter filter = new SuiteBuyFilter(SUITE_BUY_ALL, getString(R.string.suite_buy_all));
                    list.add(0, filter);
                }
                return list;
            }

            protected void onPostExecute(java.util.ArrayList<SuiteBuyFilter> result) {
                closeDialog(SuiteBuyActivity.this, dialog);

                if (result == null) {
                    return;
                }
                filterDialog = SuiteFilterDialog.show(SuiteBuyActivity.this, result);
                filterDialog.setActivity(SuiteBuyActivity.this);
            }
        }.execute();
    }

    public String getSelectIndex() {
        if (filterDialog != null) {
            String index = filterDialog.getSelectIndex();
            mSelectIndex = index;
        }
        return mSelectIndex;
    }

    public static void closeDialog(Activity act, Dialog dialog) {
        if (act != null && !act.isFinishing() && dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        LinearLayout lastLayout = mAdapter != null ? mAdapter.getLastLinearLayout() : null;

        if ((mAdapter != null) && (mAdapter.getCount() - 1) == position && hasMoreData) {
            if (lastLayout != null) {
                if (lastLayout.getVisibility() == View.GONE) {
                    lastLayout.setVisibility(View.VISIBLE);
                }
            }
        } else {
            if (lastLayout != null) {
                if (lastLayout.getVisibility() == View.VISIBLE) {
                    lastLayout.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        mIsLast = mAdapter != null ? (mAdapter.getCount() - 1) == position : false;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mSettleState = (state == VerticalViewPager.SCROLL_STATE_IDLE);
        if (hasMoreData && mIsLast && mSettleState) {
            loadMore(mCurrentPage, Constants.PAGE_SIZE, mSelectIndex);
        }
    }

    // @Override
    // public boolean onCreateOptionsMenu(Menu menu) {
    // getMenuInflater().inflate(R.menu.vertical_seek_bar, menu);
    // return true;
    // }
}
