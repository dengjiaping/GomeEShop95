package com.gome.ecmall.home.rankinglist;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gome.ecmall.bean.Ranking;
import com.gome.ecmall.bean.Ranking.FilterSoft;
import com.gome.ecmall.bean.Ranking.FilterType;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.custom.SegmentTabs;
import com.gome.ecmall.custom.SegmentTabs.OnTabSelectChangedListener;
import com.gome.ecmall.custom.SoftDialog;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.util.AppMeasurementUtils;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 排行榜列表 筛选后有新RankingFilterListActivity类似结构 如修改，两处都对等修改
 */
public class RankingListActivity extends AbsSubActivity implements OnClickListener, OnItemClickListener,
        OnScrollListener, OnTabSelectChangedListener {

    private TextView tvTitle, tvEmpty;
    private ImageView no_net_img;
    private Button common_title_btn_back, common_title_btn_right;
    private String categoryId = "";
    private int currentPage = 1;
    private int currentSortType = Ranking.SORT_TYPE_SALE;
    private LayoutInflater inflater;
    private boolean hasMoreData = true;
    private LinearLayout loadingView;
    private RankingListAdapter rankingListAdapter;
    private ListView listView;
    private ArrayList<FilterType> filterConditions;
    private final int[] LABEL_IDS = new int[] { R.string.hot_sell, R.string.lower_price, R.string.hot_spot };
    private final int[] RANKING_ORDERS = new int[] { Ranking.SORT_TYPE_SALE, Ranking.SORT_TYPE_PRICE,
            Ranking.SORT_TYPE_HOT };
    private final int[] TAB_BTNS_ID = new int[] { R.id.hot_sell, R.id.lower_price, R.id.hot_spot };
    private TextView[] tabBtns = new TextView[LABEL_IDS.length];
    private int checkedIndex = 0;
    private String categoryName;
    private SoftDialog softDialog;
    // 排序列表
    private ArrayList<FilterType> softConList;
    public static final String INTENT_KEY_CATEGORY_ID = "categoryId";
    public static final String INTENT_KEY_FLITER_CONDITION_LIST = "filterConditions";
    public static final String INTENT_KEY_CURRENT_CHECK_INDEX = "checkedIndex";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = LayoutInflater.from(this);
        setContentView(R.layout.home_ranking_list);
        setupView();
        setupData();
    }

    private void setupView() {
        common_title_btn_back = (Button) findViewById(R.id.common_title_btn_back);
        common_title_btn_back.setText(R.string.back);
        common_title_btn_back.setVisibility(View.VISIBLE);
        common_title_btn_back.setOnClickListener(this);
        common_title_btn_right = (Button) findViewById(R.id.common_title_btn_right);
        common_title_btn_right.setVisibility(View.VISIBLE);
        common_title_btn_right.setText(R.string.rankinglist_btn_select);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        listView = (ListView) findViewById(R.id.ranking_list_listview);
        loadingView = (LinearLayout) inflater.inflate(R.layout.common_loading_layout, null);
        tvEmpty = (TextView) findViewById(android.R.id.empty);
        no_net_img = (ImageView) findViewById(R.id.no_net_img);
        tvTitle.setText(getString(R.string.rank_board));
        initRankTabs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppMeasurementUtils appMeasurementProm = new AppMeasurementUtils(RankingListActivity.this);
        appMeasurementProm.getUrl(getString(R.string.appMeas_actiCenter) + ":" + getString(R.string.appMeas_rankPage),
                getString(R.string.appMeas_actiCenter), getString(R.string.appMeas_actiCenter) + ":"
                        + getString(R.string.appMeas_rankPage), getString(R.string.appMeas_rankPage), "", "", "", "",
                "", "", "", "", getString(R.string.appMeas_myprom), "", "", "", null);
    }

    private void initRankTabs() {
        for (int i = 0; i < TAB_BTNS_ID.length; i++) {
            tabBtns[i] = (TextView) findViewById(TAB_BTNS_ID[i]);
        }
    }

    private void setupData() {
        currentSortType = RANKING_ORDERS[0];
        tabBtns[0].setSelected(true);
        checkedIndex = 0;
        reloadData(categoryId, currentSortType, filterConditions);
    }

    private void reloadData(final String categoryId, final int currentSortType, final ArrayList<FilterType> conditions) {
        if (!NetUtility.isNetworkAvailable(RankingListActivity.this)) {
            CommonUtility.showMiddleToast(RankingListActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            no_net_img.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            return;
        }
        no_net_img.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        // 重新加载数据时取消加载更多的操作
        if (asyncTask != null) {
            asyncTask.cancel(true);
        }
        currentPage = 1;
        new AsyncTask<Object, Void, ArrayList<Ranking>>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(RankingListActivity.this, getString(R.string.loading),
                        true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected ArrayList<Ranking> doInBackground(Object... params) {
                String request = Ranking.createRequestRankingListJson("", currentPage, Constants.PAGE_SIZE,
                        currentSortType, conditions);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_RUSHBUY_RANKING_GOODS_LIST, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return Ranking.parseRankingListJson(response);
            }

            @Override
            protected void onPostExecute(ArrayList<Ranking> result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                ArrayList<Ranking> rankings = result;
                if (result == null) {
                    if (rankingListAdapter != null) {
                        rankingListAdapter.clear();
                    }
                    rankings = new ArrayList<Ranking>(0);
                    if (TextUtils.isEmpty(Ranking.getErrorMessage())) {
                        CommonUtility.showMiddleToast(RankingListActivity.this, "",
                                getString(R.string.data_load_fail_exception));
                    } else {
                        CommonUtility.showMiddleToast(RankingListActivity.this, "", Ranking.getErrorMessage());
                    }
                } else {
                    rankings = result;
                }
                if (rankings.size() < Constants.PAGE_SIZE) {// 没有商品可取
                    hasMoreData = false;
                    if (listView.getFooterViewsCount() > 0 && loadingView != null) {
                        // listView.removeFooterView(loadingView);
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setVisibility(View.INVISIBLE);
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setText(R.string.no_more);
                        ((ProgressBar) loadingView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                    }
                } else {// 还可以继续取
                    ((TextView) loadingView.findViewById(R.id.common_loading_title)).setVisibility(View.VISIBLE);
                    if (listView.getFooterViewsCount() == 0 && loadingView != null) {
                        listView.addFooterView(loadingView);
                    } else {
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setText(R.string.load_more);
                        ((ProgressBar) loadingView.findViewById(R.id.loadingbar)).setVisibility(View.VISIBLE);
                    }
                    hasMoreData = true;
                    currentPage++;
                }
                if (rankingListAdapter == null) {
                    rankingListAdapter = new RankingListAdapter(RankingListActivity.this, rankings, currentSortType);
                    listView.setAdapter(rankingListAdapter);
                    tvTitle.setOnClickListener(RankingListActivity.this);
                    common_title_btn_right.setOnClickListener(RankingListActivity.this);
                    tvEmpty.setText(R.string.empty);
                    listView.setEmptyView(tvEmpty);
                    listView.setOnItemClickListener(RankingListActivity.this);
                    listView.setOnScrollListener(RankingListActivity.this);
                    for (int i = 0; i < tabBtns.length; i++) {
                        tabBtns[i].setOnClickListener(RankingListActivity.this);
                    }
                } else {
                    rankingListAdapter.reload(result);
                    listView.setSelection(0);
                }
            }
        }.execute();
    }

    private AsyncTask<Object, Void, ArrayList<Ranking>> asyncTask = null;

    /**
     * 加载更多列表项
     * 
     * @param categoryId
     *            商品分类ID
     * @param page
     *            页码
     * @param currentSortType
     *            标签榜单
     */
    private void loadMoreData(final String categoryId, final int page, final int currentSortType,
            final ArrayList<FilterType> conditions) {
        if (!NetUtility.isNetworkAvailable(RankingListActivity.this)) {
            CommonUtility.showMiddleToast(RankingListActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        if (asyncTask != null) {
            return;
        }
        asyncTask = new AsyncTask<Object, Void, ArrayList<Ranking>>() {
            @Override
            protected ArrayList<Ranking> doInBackground(Object... params) {
                String request = Ranking.createRequestRankingListJson("", currentPage, Constants.PAGE_SIZE,
                        currentSortType, conditions);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_RUSHBUY_RANKING_GOODS_LIST, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return Ranking.parseRankingListJson(response);
            }

            @Override
            protected void onCancelled() {
                asyncTask = null;
            }

            @Override
            protected void onPostExecute(ArrayList<Ranking> result) {
                if (isCancelled()) {
                    return;
                }
                if (result == null) {
                    CommonUtility.showMiddleToast(RankingListActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                if (result.size() < Constants.PAGE_SIZE) {// 没有商品可取
                    hasMoreData = false;
                    if (listView.getFooterViewsCount() > 0 && loadingView != null) {
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setVisibility(View.INVISIBLE);
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setText(R.string.no_more);
                        ((ProgressBar) loadingView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                    }
                } else {// 还可以继续取
                    hasMoreData = true;
                    currentPage++;
                    ((TextView) loadingView.findViewById(R.id.common_loading_title)).setVisibility(View.VISIBLE);
                    if (listView.getFooterViewsCount() == 0 && loadingView != null) {
                        listView.addFooterView(loadingView);
                    } else {
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setText(R.string.load_more);
                        ((ProgressBar) loadingView.findViewById(R.id.loadingbar)).setVisibility(View.VISIBLE);
                    }
                }
                rankingListAdapter.addList(result);
                asyncTask = null;
            }
        };
        asyncTask.execute();
    }

    private void initSoftDialog() {
        if (softConList != null) {
            initSoftDialogView();
        } else {
            if (!NetUtility.isNetworkAvailable(RankingListActivity.this)) {
                CommonUtility.showMiddleToast(RankingListActivity.this, "",
                        getString(R.string.can_not_conntect_network_please_check_network_settings));
                return;
            }
            new AsyncTask<Object, Void, FilterSoft>() {
                LoadingDialog loadingDialog;

                @Override
                protected void onPreExecute() {
                    loadingDialog = CommonUtility.showLoadingDialog(RankingListActivity.this, "正在获取筛选条件...", true,
                            new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    cancel(true);
                                }
                            });
                }

                @Override
                protected FilterSoft doInBackground(Object... params) {
                    String response = NetUtility.sendHttpRequestByGet(Constants.URL_RUSHBUY_RANKING_CATEGORY);
                    if (NetUtility.NO_CONN.equals(response)) {
                        return null;
                    }
                    return Ranking.parseFilterConditionList(response);
                }

                @Override
                protected void onPostExecute(final FilterSoft result) {
                    if (isCancelled()) {
                        return;
                    }
                    loadingDialog.dismiss();
                    if (result == null) {
                        CommonUtility.showMiddleToast(RankingListActivity.this, "",
                                getString(R.string.data_load_fail_exception));
                        return;
                    }
                    softConList = result.getSoftConList();
                    initSoftDialogView();
                };
            }.execute();
        }
    }

    private void initSoftDialogView() {
        if (softConList == null || softConList.size() == 0) {
            CommonUtility.showToast(RankingListActivity.this, "没有筛选条件");
            return;
        }
        softDialog = new SoftDialog(RankingListActivity.this);
        softDialog.show();
        softDialog.setTitle(getString(R.string.filter));
        softDialog.setButtonListener(getString(R.string.confirm), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                softDialog.dismiss();
                // 判断是否有筛选条件
                boolean flag = false;
                for (int i = 0; i < softConList.size(); i++) {
                    if (softConList.get(i).isSelected()) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    Intent intent = new Intent();
                    intent.setClass(RankingListActivity.this, RankingFilterListActivity.class);
                    intent.putExtra(RankingListActivity.INTENT_KEY_CATEGORY_ID, categoryId);
                    intent.putExtra(RankingListActivity.INTENT_KEY_CURRENT_CHECK_INDEX, checkedIndex);
                    intent.putExtra(RankingListActivity.INTENT_KEY_FLITER_CONDITION_LIST, softConList);
                    startActivity(intent);
                }
                // reloadData(categoryId, currentSortType, filterConditions);
            }
        });
        softDialog.setData_Ranking(softConList, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int positon, long id) {

                FilterAdapter filterAdapter = (FilterAdapter) adapter.getAdapter();
                filterAdapter.clearAllSelectedState();
                FilterType selectSortCon = (FilterType) filterAdapter.getItem(positon);
                if (selectSortCon != null) {
                    categoryName = selectSortCon.getCategoryName();
                    categoryId = selectSortCon.getCategoryId();
                    selectSortCon.setSelected(!selectSortCon.isSelected());
                }
                filterAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v == common_title_btn_back) {
            goback();
        } else if (v == common_title_btn_right) {
            if (softDialog == null) {
                if (filterConditions != null && filterConditions.size() == 0) {
                    CommonUtility.showToast(RankingListActivity.this, "没有筛选条件");
                    return;
                }
                initSoftDialog();
            } else {
                // softDialog.clearAllGroupBuySelected();
                // softDialog.clearAllRankingSelected();
                softDialog.clearAllSelected();
                softDialog.show();
            }
        } else {
            for (int i = 0; i < tabBtns.length; i++) {
                if (v == tabBtns[i]) {
                    if (checkedIndex != i) {// 点击了不同的标签卡
                        checkedIndex = i;
                        if (i == 0) {
                            common_title_btn_right.setVisibility(View.VISIBLE);
                        } else {
                            common_title_btn_right.setVisibility(View.INVISIBLE);
                        }
                        tabBtns[i].setSelected(true);
                        currentSortType = RANKING_ORDERS[i];
                        rankingListAdapter.setClassType(currentSortType);
                        reloadData(categoryId, currentSortType, filterConditions);
                    }
                } else {
                    tabBtns[i].setSelected(false);
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

    }

    @Override
    public void onTabSelectChanged(SegmentTabs tabs, View item, int lastIndex, int currentIndex) {

        if (currentIndex != lastIndex) {
            currentSortType = RANKING_ORDERS[currentIndex];
            reloadData(categoryId, currentSortType, filterConditions);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (firstVisibleItem + visibleItemCount == totalItemCount) {// 滑动到底部
            if (hasMoreData && rankingListAdapter != null && rankingListAdapter.getCount() > 0) {
                loadMoreData(categoryId, currentPage, currentSortType, filterConditions);
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }
}