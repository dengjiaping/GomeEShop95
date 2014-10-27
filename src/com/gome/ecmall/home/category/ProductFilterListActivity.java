package com.gome.ecmall.home.category;

import java.util.ArrayList;
import java.util.Timer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.LimitBuyResult;
import com.gome.ecmall.bean.Product;
import com.gome.ecmall.bean.Product.FilterCondition;
import com.gome.ecmall.custom.FilterDialog;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.custom.SegmentTabs;
import com.gome.ecmall.custom.SegmentTabs.OnTabSelectChangedListener;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.category.ProductListAdapter.OnProductClickListener;
import com.gome.ecmall.util.AppMeasurementUtils;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.ConvertUtils;
import com.gome.ecmall.util.NetUtility;
import com.gome.ecmall.util.ReleaseUtils;
import com.gome.eshopnew.R;

/**
 * 分类商品筛选列表 来源为ProductListActivity类似结构 如修改，两处都对等修改
 */
public class ProductFilterListActivity extends AbsSubActivity implements OnClickListener,// OnItemClickListener,
        OnScrollListener, OnTabSelectChangedListener {

    private TextView tvTitle;
    private Button btnBack;
    private Button btnFilter;
    private Button btnList;
    private Button btnGird;
    private boolean isGridMode = true;
    private LinearLayout layoutSwitch;
    private ListView listView;
    private ProductListAdapter productListAdapter;
    private LinearLayout loadingView, tabs_linearlayout;
    private TextView tvEmpty;
    private ImageView no_net_img;
    public static final String INTENT_KEY_TITLE = "title";
    public static final String INTENT_KEY_GOODS_TYPE_ID = "goodsTypeId";
    public static final String INTENT_KEY_FLITER_CONDITION_LIST = "filterConditions";
    public static final String INTENT_KEY_CURRENT_CHECK_INDEX = "checkedIndex";
    public static final String INTENT_KEY_IS_GRID_MODE = "isGridMode";

    public static final String calssType = "ProductListActivity";
    private String title;
    private String goodsTypeId;
    private int currentPage = 1;
    private int currentSortBy = Product.SORTBY_HOT;
    private LayoutInflater inflater;
    private boolean hasMoreData = true;
    private FilterDialog filterDialog;
    private ArrayList<FilterCondition> filterConditions;
    private final int[] LABEL_IDS = new int[] { R.string.product_hot, R.string.product_sale, R.string.product_price,
            R.string.product_time };
    private final int[] PRODUCT_ORDERS = new int[] { Product.SORTBY_HOT, Product.SORTBY_SALE_DESC,
            Product.SORTBY_PRICE_ASC, Product.SORTBY_TIME_DESC };
    private final int[] TAB_BTNS_ID = new int[] { R.id.product_list_tab_item_hot, R.id.product_list_tab_item_sale,
            R.id.product_list_tab_item_price, R.id.product_list_tab_item_time };
    /** 分类列表顶部的四个排序按钮，热度、销量、价格、时间 */
    private TextView[] tabBtns = new TextView[LABEL_IDS.length];
    private int checkedIndex = 0;
    private Drawable downDrawable;
    private Drawable upDrawable;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = LayoutInflater.from(this);
        setContentView(R.layout.category_product_list);
        title = getIntent().getStringExtra(INTENT_KEY_TITLE);
        goodsTypeId = getIntent().getStringExtra(INTENT_KEY_GOODS_TYPE_ID);
        checkedIndex = getIntent().getIntExtra(INTENT_KEY_CURRENT_CHECK_INDEX, -1);
        filterConditions = (ArrayList<FilterCondition>) getIntent().getSerializableExtra(
                INTENT_KEY_FLITER_CONDITION_LIST);
        isGridMode = getIntent().getBooleanExtra(INTENT_KEY_IS_GRID_MODE, false);
        setupView();
        setupData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent() != null) {
            String fromPage = getIntent().getStringExtra("fromPage");
            AppMeasurementUtils appMeasurementUtils = new AppMeasurementUtils(ProductFilterListActivity.this);
            appMeasurementUtils.getUrl(getString(R.string.appMeas_productlist) + ":" + title,
                    getString(R.string.appMeas_productlist), getString(R.string.appMeas_productlist) + ":" + title,
                    getString(R.string.appMeas_productlist_page), fromPage, "", "", "", "", "", "", "",
                    getString(R.string.appMeas_categoryPage), "", "", "", null);
        }
    }

    private void setupView() {
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setText(R.string.back);
        btnBack.setOnClickListener(this);
        btnFilter = (Button) findViewById(R.id.common_title_btn_right);
        tabs_linearlayout = (LinearLayout) findViewById(R.id.tabs_linearlayout);
        if ("PromtionActivitiesActivity".equals(getIntent().getAction())) {
            tvTitle.setText(getIntent().getStringExtra(JsonInterface.JK_ACTIVITY_NAME));
            btnFilter.setVisibility(View.INVISIBLE);
            btnFilter.setOnTouchListener(null);
            tabs_linearlayout.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
            btnFilter.setVisibility(View.VISIBLE);
            btnFilter.setText(R.string.filter);
        }
        layoutSwitch = (LinearLayout) inflater.inflate(R.layout.common_product_display_switch_layout, null);
        btnGird = (Button) layoutSwitch.findViewById(R.id.category_product_display_type_grid);
        btnList = (Button) layoutSwitch.findViewById(R.id.category_product_display_type_list);
        listView = (ListView) findViewById(R.id.category_lv_product_list);
        loadingView = (LinearLayout) inflater.inflate(R.layout.common_loading_layout, null);
        listView.addHeaderView(layoutSwitch);
        tvEmpty = (TextView) findViewById(android.R.id.empty);
        no_net_img = (ImageView) findViewById(R.id.no_net_img);
        downDrawable = getResources().getDrawable(R.drawable.product_rank_down_bg_selector);
        upDrawable = getResources().getDrawable(R.drawable.product_rank_up_bg_selector);
        initRankTabs();
    }

    private void initRankTabs() {
        for (int i = 0; i < TAB_BTNS_ID.length; i++) {
            tabBtns[i] = (TextView) findViewById(TAB_BTNS_ID[i]);
        }
    }

    private void setupData() {
        btnGird.setSelected(false);
		btnList.setSelected(true);
		setListViewMode(true);
        currentSortBy = PRODUCT_ORDERS[checkedIndex];
        tabBtns[checkedIndex].setSelected(true);
        reloadData(goodsTypeId, currentSortBy, filterConditions);
    }

    private void reloadData(final String goodsTypeId, final int sortBy, final ArrayList<FilterCondition> conditions) {
        if (!NetUtility.isNetworkAvailable(ProductFilterListActivity.this)) {
            CommonUtility.showMiddleToast(ProductFilterListActivity.this, "",
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
        new AsyncTask<Object, Void, ArrayList<Product>>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(ProductFilterListActivity.this,
                        getString(R.string.loading), true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected ArrayList<Product> doInBackground(Object... params) {
                Intent intent = getIntent();
                String response = NetUtility.NO_CONN;
                if ("PromtionActivitiesActivity".equals(intent.getAction())) {// 判断是否是活动专题
                    String activityId = intent.getStringExtra(JsonInterface.JK_ACTIVITY_ID);
                    String activityType = intent.getStringExtra(JsonInterface.JK_ACTIVITY_TYPE);
                    String activityHtmlurl = intent.getStringExtra(JsonInterface.JK_ACTIVITY_HTML_URL);
                    String request = LimitBuyResult.createRequestLimitBuyPrmListJson(activityId, activityType,
                            activityHtmlurl);
                    response = NetUtility.sendHttpRequestByPost(Constants.URL_ACTIVITIES_GENERAL_ACTIVITY_GOODS,
                            request);
                    if (NetUtility.NO_CONN.equals(response)) {
                        return null;
                    }
                    return Product.parseProductSkuListJson(response);
                } else {
                    String request = Product.createRequestProductListJson(goodsTypeId, currentPage,
                            Constants.PAGE_SIZE, sortBy, conditions);
                    if (conditions != null && conditions.size() > 0) {// 有筛选条件
                        response = NetUtility.sendHttpRequestByPost(Constants.URL_PRODUCT_LIST, request);
                    } else {// 没有筛选条件
                        response = NetUtility.sendHttpRequestByPost(Constants.URL_PRODUCT_TYPE_SEARCH, request);
                    }
                    if (NetUtility.NO_CONN.equals(response)) {
                        return null;
                    }
                    return Product.parseProductListJson(response);
                }
            }

            @Override
            protected void onPostExecute(ArrayList<Product> result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                ArrayList<Product> products = result;
                if (result == null) {
                    products = new ArrayList<Product>(0);
                    CommonUtility.showMiddleToast(ProductFilterListActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                } else {
                    products = result;
                }
                if (!"PromtionActivitiesActivity".equals(getIntent().getAction())) {
                    if (products.size() < Constants.PAGE_SIZE) {// 没有商品可取
                        hasMoreData = false;
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setVisibility(View.INVISIBLE);
                        if (listView.getFooterViewsCount() > 0) {
                            // listView.removeFooterView(loadingView);
                            ((TextView) loadingView.findViewById(R.id.common_loading_title)).setText(R.string.no_more);
                            ((ProgressBar) loadingView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);

                        } else {
                            ((TextView) loadingView.findViewById(R.id.common_loading_title)).setText(R.string.no_more);
                            ((ProgressBar) loadingView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                            listView.addFooterView(loadingView);
                        }
                    } else {// 还可以继续取
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setVisibility(View.VISIBLE);
                        if (listView.getFooterViewsCount() == 0) {
                            listView.addFooterView(loadingView);
                        } else {
                            ((TextView) loadingView.findViewById(R.id.common_loading_title))
                                    .setText(R.string.load_more);
                            ((ProgressBar) loadingView.findViewById(R.id.loadingbar)).setVisibility(View.VISIBLE);
                        }
                        hasMoreData = true;
                        currentPage++;
                    }
                }
                if (productListAdapter == null) {
                    productListAdapter = new ProductListAdapter(ProductFilterListActivity.this, products, calssType);
                    productListAdapter.setToGridMode(isGridMode);
                    listView.setAdapter(productListAdapter);
                    tvTitle.setOnClickListener(ProductFilterListActivity.this);
                    btnFilter.setOnClickListener(ProductFilterListActivity.this);
                    btnGird.setOnClickListener(ProductFilterListActivity.this);
                    btnList.setOnClickListener(ProductFilterListActivity.this);
                    tvEmpty.setText(R.string.empty);
                    listView.setEmptyView(tvEmpty);
                    // list item 的点击事件此处没有设置，取而代之的是给adapter自定义的监听OnProductClickListener
                    // listView.setOnItemClickListener(ProductListActivity.this);
                    listView.setOnScrollListener(ProductFilterListActivity.this);
                    productListAdapter.setClickListener(new OnProductClickListener() {
                        // 接口回调，监听器接口定义、监听和调用在ProductListAdapter中
                        @Override
                        public void onProductClick(Product product) {
                            Intent intent = new Intent(ProductFilterListActivity.this, ProductShowActivity.class);
                            intent.putExtra(ProductShowActivity.INTENT_KEY_GOODS_NO, product.getGoodsNo());
                            intent.putExtra("fromPage", getString(R.string.appMeas_categoryPage));
                            recordProductBrowseHistory(product);
                            startActivity(intent);
                        }
                    });
                    for (int i = 0; i < tabBtns.length; i++) {
                        tabBtns[i].setOnClickListener(ProductFilterListActivity.this);
                    }
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
    private void loadMoreData(final String goodsTypeId, final int page, final int sortBy,
            final ArrayList<FilterCondition> conditions) {
        if (!NetUtility.isNetworkAvailable(ProductFilterListActivity.this)) {
            CommonUtility.showMiddleToast(ProductFilterListActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        if (asyncTask != null) {
            return;
        }
        asyncTask = new AsyncTask<Object, Void, ArrayList<Product>>() {

            @Override
            protected ArrayList<Product> doInBackground(Object... params) {
                String response = NetUtility.NO_CONN;
                String request = Product.createRequestProductListJson(goodsTypeId, page, Constants.PAGE_SIZE, sortBy,
                        conditions);
                if (conditions != null && conditions.size() > 0) {
                    response = NetUtility.sendHttpRequestByPost(Constants.URL_PRODUCT_LIST, request);
                } else {
                    response = NetUtility.sendHttpRequestByPost(Constants.URL_PRODUCT_TYPE_SEARCH, request);
                }
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
                    CommonUtility.showMiddleToast(ProductFilterListActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                if (result.size() < Constants.PAGE_SIZE) {// 没有商品可取
                    hasMoreData = false;
                    ((TextView) loadingView.findViewById(R.id.common_loading_title)).setVisibility(View.INVISIBLE);
                    if (listView.getFooterViewsCount() > 0) {
                        // listView.removeFooterView(loadingView);
                        ((TextView) loadingView.findViewById(R.id.common_loading_title)).setText(R.string.no_more);
                        ((ProgressBar) loadingView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);
                    }
                } else {// 还可以继续取
                    ((TextView) loadingView.findViewById(R.id.common_loading_title)).setVisibility(View.VISIBLE);
                    hasMoreData = true;
                    currentPage++;
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

    private void initFilterDialog() {
        filterDialog = new FilterDialog(ProductFilterListActivity.this);
        filterDialog.show();
        filterDialog.setTitle(getString(R.string.filter));
        filterDialog.setButtonListener(getString(R.string.confirm), new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                filterDialog.dismiss();
                reloadData(goodsTypeId, currentSortBy, filterConditions);

            }
        });
        filterDialog.setData(filterConditions, new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                FilterConditionsAdapter adapter = (FilterConditionsAdapter) parent.getExpandableListAdapter();
                FilterCondition filterCondition = adapter.getGroup(groupPosition);
                filterCondition.toggleValueAtPostion(childPosition);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
    	
    	switch (v.getId()) {
		case R.id.common_title_btn_back: {
			goback();
		}
			break;
		case R.id.common_title_btn_right: {
			setFilterDialog();
		}
			break;
		case R.id.category_product_display_type_grid: {
			setGirdMode();
		}
			break;
		case R.id.category_product_display_type_list: {
			setListMode();
		}
			break;
		case R.id.common_title_tv_text: {
			setTitle();
		}
			break;
		default: {
			setTabCheck(v);
		}
			break;
		}
    	
    }
    
    /**
	 * 设置TAB事件
	 * 
	 * @param v
	 */
	private void setTabCheck(View v) {
		// 分类列表顶部的四个排序按钮，热度、销量、价格、时间
		for (int i = 0; i < tabBtns.length; i++) {
			if (v == tabBtns[i]) {
				if (checkedIndex != i) {// 点击了不同的标签卡
					currentSortBy = PRODUCT_ORDERS[i];
					if (currentSortBy == Product.SORTBY_PRICE_ASC) {
						tabBtns[i].setCompoundDrawablesWithIntrinsicBounds(
								null, null, upDrawable, null);
					}
					checkedIndex = i;
					tabBtns[i].setSelected(true);
					reloadData(goodsTypeId, currentSortBy, filterConditions);
				} else {
					if (i == 2) {// 价格可以切换状态
						if (currentSortBy == Product.SORTBY_PRICE_DESC) {
							currentSortBy = Product.SORTBY_PRICE_ASC;
							tabBtns[i].setCompoundDrawablesWithIntrinsicBounds(
									null, null, upDrawable, null);
							reloadData(goodsTypeId, currentSortBy,
									filterConditions);
						} else if (currentSortBy == Product.SORTBY_PRICE_ASC) {
							currentSortBy = Product.SORTBY_PRICE_DESC;
							tabBtns[i].setCompoundDrawablesWithIntrinsicBounds(
									null, null, downDrawable, null);
							reloadData(goodsTypeId, currentSortBy,
									filterConditions);
						}
					}
				}
			} else {
				tabBtns[i].setSelected(false);
			}
		}
	}

	/**
	 * 设置标题
	 */
	private void setTitle() {
		// 点击标题栏定位到第一个
		if (productListAdapter != null && productListAdapter.getCount() > 0) {
			listView.setSelection(1);
		}
	}

	/**
	 * 设置列表模式
	 */
	private void setListMode() {
		btnList.setSelected(true);
		btnGird.setSelected(false);
		if (productListAdapter != null) {
			setListViewMode(true);
			productListAdapter.setToGridMode(false);
		}
	}

	/**
	 * 设置九宫格模式
	 */
	private void setGirdMode() {
		btnGird.setSelected(true);
		btnList.setSelected(false);
		if (productListAdapter != null) {
			setListViewMode(false);
			productListAdapter.setToGridMode(true);
		}
	}

	/**
	 * 过滤条件弹框
	 */
	private void setFilterDialog() {
		if (filterDialog == null) {
			if (filterConditions != null && filterConditions.size() == 0) {
				CommonUtility.showToast(ProductFilterListActivity.this, "没有筛选条件");
				return;
			}
			initFilterDialog();
		} else {
			filterDialog.clearAllRankingListSelected();
			// filterDialog.clearAllGroupBuySelected();
			filterDialog.clearAllSelected();
			filterDialog.show();
		}
	}

    // 点击事件在adapter中实现
    // public void onItemClick(AdapterView<?> parent, View view, int position,
    // long id) {
    // ArrayList<Product> list = productListAdapter.getItem(position - 1);
    // if (list.size() > 0) {
    // Intent intent = new Intent(this, ProductShowActivity.class);
    // intent.putExtra(ProductShowActivity.INTENT_KEY_GOODS_NO,
    // list.get(0).getGoodsNo());
    // recordProductBrowseHistory(list.get(0));
    // startActivity(intent);
    // }
    // }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Intent intent = getIntent();
        if (!"PromtionActivitiesActivity".equals(intent.getAction())) {
            if (firstVisibleItem + visibleItemCount == totalItemCount) {// 滑动到底部
                if (hasMoreData && productListAdapter != null && productListAdapter.getCount() > 0) {
                    loadMoreData(goodsTypeId, currentPage, currentSortBy, filterConditions);
                }
            }
        }
    }

    @Override
    public void onTabSelectChanged(SegmentTabs tabs, View item, int lastIndex, int currentIndex) {
        if (currentIndex != lastIndex) {
            currentSortBy = PRODUCT_ORDERS[currentIndex];
            reloadData(goodsTypeId, currentSortBy, filterConditions);
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
    
	/**
	 * 判断是否为列表模式
	 * 
	 * @param isList
	 */
	private void setListViewMode(boolean isList) {
		if (isList) {
			listView.setPadding(0, 0, 0, 0);
		} else {
			int size = ConvertUtils.dip2px(8.0f, this);
			listView.setPadding(size, 0, size, 0);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ReleaseUtils.releaseList(filterConditions);
		productListAdapter.cleanList();
		productListAdapter = null;
	}
}
