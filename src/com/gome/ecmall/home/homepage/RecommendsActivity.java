package com.gome.ecmall.home.homepage;

import java.util.ArrayList;
import java.util.Timer;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gome.ecmall.bean.Product;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.category.ProductListAdapter;
import com.gome.ecmall.home.category.ProductListAdapter.OnProductClickListener;
import com.gome.ecmall.home.category.ProductShowActivity;
import com.gome.ecmall.util.AppMeasurementUtils;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class RecommendsActivity extends AbsSubActivity implements OnClickListener, OnItemClickListener,
        OnScrollListener {
    private TextView tvTitle;
    private Button btnBack;
    private Button btnList;
    private Button btnGird;
    private LinearLayout layoutSwitch;
    private ListView listView;
    private LinearLayout loadingView;
    private TextView tvEmpty;
    private ImageView no_net_img;
    private String goodsTypeId;
    private String title;
    public static final String INTENT_KEY_GOODS_TYPE_ID = "goodsTypeId";
    public static final String INTENT_KEY_TITLE = "title";
    public static final String classType = "RecommendsActivity";
    private int currentPage = 1;
    private LayoutInflater inflater;
    private boolean hasMoreData = true;
    private ProductListAdapter productListAdapter;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_activity_recommend_list_main);
        title = getIntent().getStringExtra(INTENT_KEY_TITLE);
        goodsTypeId = getIntent().getStringExtra(INTENT_KEY_GOODS_TYPE_ID);
        AppMeasurementUtils appMeasurementProm = new AppMeasurementUtils(RecommendsActivity.this);
        appMeasurementProm.getUrl(getString(R.string.appMeas_promDetail) + ":" + title,
                getString(R.string.appMeas_promDetail), getString(R.string.appMeas_promDetail) + ":" + title,
                getString(R.string.appMeas_promDetailPage), "", "", "", "", "", "", "", "",
                getString(R.string.appMeas_myprom), goodsTypeId, "", "", new String[] { null, "o",
                        AppMeasurementUtils.TRANK_LINK_PROM });
        if (goodsTypeId == null) {
            CommonUtility.showToast(this, "没有");
            return;
        }
        setupView();
        setupData(goodsTypeId, Product.SORTBY_HOT);
    }

    @Override
    protected void onResume() {

        super.onResume();

    }

    private void setupView() {
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(title);
        tvTitle.setOnClickListener(this);
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setText(R.string.back);
        btnBack.setOnClickListener(this);
        inflater = LayoutInflater.from(this);
        layoutSwitch = (LinearLayout) inflater.inflate(R.layout.common_product_display_switch_layout, null);
        btnGird = (Button) layoutSwitch.findViewById(R.id.category_product_display_type_grid);
        btnList = (Button) layoutSwitch.findViewById(R.id.category_product_display_type_list);
        listView = (ListView) findViewById(R.id.home_search_result_product_list);
        loadingView = (LinearLayout) inflater.inflate(R.layout.common_loading_layout, null);
        listView.addHeaderView(layoutSwitch);
        tvEmpty = (TextView) findViewById(android.R.id.empty);
        no_net_img = (ImageView) findViewById(R.id.no_net_img);
    }

    private void setupData(final String goodsTypeId, final int sortBy) {
        if (!NetUtility.isNetworkAvailable(RecommendsActivity.this)) {
            CommonUtility.showMiddleToast(RecommendsActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            no_net_img.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            return;
        }
        no_net_img.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
        btnGird.setSelected(true);
        new AsyncTask<Object, Void, ArrayList<Product>>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(RecommendsActivity.this, getString(R.string.loading),
                        true, new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected ArrayList<Product> doInBackground(Object... params) {
                String request = Product.createRequestProductListJson(goodsTypeId, currentPage, Constants.PAGE_SIZE,
                        sortBy, null);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_PRODUCT_TYPE_SEARCH, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return Product.parseProductListJson(response);
            }

            @Override
            protected void onPostExecute(ArrayList<Product> result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                if (result == null) {
                    CommonUtility.showMiddleToast(RecommendsActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                if (result.size() < Constants.PAGE_SIZE) {// 没有商品可取
                    hasMoreData = false;
                    if (listView.getFooterViewsCount() > 0) {
                        // listView.removeFooterView(loadingView);
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setVisibility(View.INVISIBLE);
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setText(R.string.no_more);
                        ((ProgressBar) loadingView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                    }
                } else {// 还可以继续取
                    ((TextView) loadingView.findViewById(R.id.common_loading_title)).setVisibility(View.VISIBLE);
                    if (listView.getFooterViewsCount() == 0) {
                        listView.addFooterView(loadingView);
                    } else {
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setText(R.string.load_more);
                        ((ProgressBar) loadingView.findViewById(R.id.loadingbar)).setVisibility(View.VISIBLE);
                    }
                    hasMoreData = true;
                    currentPage++;
                }
                if (productListAdapter == null) {
                    productListAdapter = new ProductListAdapter(RecommendsActivity.this, result, classType);
                    listView.setAdapter(productListAdapter);
                    tvTitle.setOnClickListener(RecommendsActivity.this);
                    btnGird.setOnClickListener(RecommendsActivity.this);
                    btnList.setOnClickListener(RecommendsActivity.this);
                    tvEmpty.setText("没有相应的商品");
                    listView.setEmptyView(tvEmpty);
                    listView.setOnItemClickListener(RecommendsActivity.this);
                    listView.setOnScrollListener(RecommendsActivity.this);
                    productListAdapter.setClickListener(new OnProductClickListener() {

                        @Override
                        public void onProductClick(Product product) {
                            Intent intent = new Intent(RecommendsActivity.this, ProductShowActivity.class);
                            intent.putExtra(ProductShowActivity.INTENT_KEY_GOODS_NO, product.getGoodsNo());
                            intent.putExtra("fromPage", getString(R.string.appMeas_firstPage));
                            recordProductBrowseHistory(product);
                            startActivity(intent);
                        }
                    });
                    afterTimeSelectListView();
                } else {
                    productListAdapter.reload(result);
                    listView.setSelection(1);
                }
            }
        }.execute();
    }

    private AsyncTask<Object, Void, ArrayList<Product>> asyncTask = null;

    /**
     * 加载更多列表项
     * 
     * @param goodsTypeId
     *            商品类型ID
     * @param page
     *            页码
     * @param sortBy
     *            排序方式
     */
    private void loadMoreData(final String goodsTypeId, final int page, final int sortBy) {
        if (!NetUtility.isNetworkAvailable(RecommendsActivity.this)) {
            CommonUtility.showMiddleToast(RecommendsActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        if (asyncTask != null) {
            return;
        }
        asyncTask = new AsyncTask<Object, Void, ArrayList<Product>>() {

            @Override
            protected ArrayList<Product> doInBackground(Object... params) {
                String request = Product.createRequestProductListJson(goodsTypeId, page, Constants.PAGE_SIZE, sortBy,
                        null);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_PRODUCT_TYPE_SEARCH, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return Product.parseProductListJson(response);
            }

            @Override
            protected void onCancelled() {
                asyncTask = null;
            }

            @Override
            protected void onPostExecute(ArrayList<Product> result) {
                if (isCancelled()) {
                    return;
                }
                if (result == null) {
                    CommonUtility.showMiddleToast(RecommendsActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                if (result.size() < Constants.PAGE_SIZE) {// 没有商品可取
                    hasMoreData = false;
                    if (listView.getFooterViewsCount() > 0) {
                        // listView.removeFooterView(loadingView);
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setVisibility(View.INVISIBLE);
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setText(R.string.no_more);
                        ((ProgressBar) loadingView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                    }
                } else {// 还可以继续取
                    hasMoreData = true;
                    currentPage++;
                    ((TextView) loadingView.findViewById(R.id.common_loading_title)).setVisibility(View.VISIBLE);
                    if (listView.getFooterViewsCount() == 0) {
                        listView.addFooterView(loadingView);
                    } else {
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setText(R.string.load_more);
                        ((ProgressBar) loadingView.findViewById(R.id.loadingbar)).setVisibility(View.VISIBLE);
                    }
                }
                productListAdapter.addList(result);
                asyncTask = null;
            }
        };
        asyncTask.execute();
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            goback();
        } else if (v == tvTitle) {
            if (listView.getAdapter() != null) {
                listView.setSelection(1);
            }
        } else if (v == btnList) {
            btnList.setSelected(true);
            btnGird.setSelected(false);
            productListAdapter.setToGridMode(false);
        } else if (v == btnGird) {
            btnGird.setSelected(true);
            btnList.setSelected(false);
            productListAdapter.setToGridMode(true);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // ArrayList<Product> list = productListAdapter.getItem(position - 1);
        // if (list.size() > 0) {
        // Intent intent = new Intent(this, ProductShowActivity.class);
        // intent.putExtra(ProductShowActivity.INTENT_KEY_GOODS_NO, list.get(0).getGoodsNo());
        // recordProductBrowseHistory(list.get(0));
        // startActivity(intent);
        // }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount) {// 滑动到底部
            if (hasMoreData && productListAdapter != null && productListAdapter.getCount() > 0) {
                loadMoreData(goodsTypeId, currentPage, Product.SORTBY_HOT);
            }
        }
    }

    /**
     * 启动Timer执行
     */
    public void afterTimeSelectListView() {
        Timer timer = new Timer();
        initHandler(timer);
        // 启动timer任务
        timer.schedule(new MyTask(), 1000);
    }

    private void initHandler(final Timer timer) {
        if (handler == null) {
            handler = new Handler() {
                @Override
                public void dispatchMessage(Message msg) {

                    super.dispatchMessage(msg);
                    listView.setSelection(1);
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
}
