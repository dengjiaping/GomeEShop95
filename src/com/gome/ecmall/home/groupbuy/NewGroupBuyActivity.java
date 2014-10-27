package com.gome.ecmall.home.groupbuy;

import java.util.ArrayList;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.GBProductNew;
import com.gome.ecmall.bean.GBProductNew.Category;
import com.gome.ecmall.bean.GBProductNew.CategroyAndSort;
import com.gome.ecmall.bean.GBProductNew.GroupBuyProduct;
import com.gome.ecmall.bean.GBProductNew.Sort;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.HomeActivity;
import com.gome.ecmall.push.PushUtils;
import com.gome.ecmall.util.AnimationUtils;
import com.gome.ecmall.util.AppMeasurementUtils;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 新版团购activity
 * 
 * @author liuyang-ds
 * 
 */
public class NewGroupBuyActivity extends Activity implements OnScrollListener, OnClickListener {
    private Button bt_groupbuy_back;
    private RelativeLayout rl_groupbuy_title;
    private TextView tv_groupbuy_title;
    private TextView tv_groupbuy_location;
    private ImageView iv_groupbuy_location;
    private Button bt_groupbuy_show_change;
    private LinearLayout ll_groupbuy_categroy;
    private TextView tv_groupbuy_categroy;
    private ImageView iv_groupbuy_categroy;
    private LinearLayout ll_groupbuy_sort;
    private TextView tv_groupbuy_sort;
    private ImageView iv_groupbuy_sort;
    private LinearLayout ll_groupbuy_search;
    private ImageView iv_groupbuy_search;
    private ListView lv_groupbuy_product;
    private TextView tv_groupbuy_empty;
    private ImageView iv_groupbuy_nonet;
    private LinearLayout loadingView;// 加载更多view
    private TextView tv_loadMore;// 加载更多里的textView
    private ProgressBar pb_loadMore;// 加载更多里的动画

    private PopupWindow categoryPopuWindow; // 筛选分类弹出的popuwindow
    private View v_categroy;
    private ListView lv_catrgroy_one;// 第一级listView
    private ListView lv_catrgroy_two;// 第二级listView
    private ImageView iv_category_two_bottom_arrow;
    private RelativeLayout rl_category_three;// 第三级分类父控件
    private ListView lv_catrgroy_three;// 第三级listView
    private ImageView iv_category_three_bottom_arrow;
    private ImageView iv_category_two_zhezhao;
    private NewGroupBuyCategoryAdapter categoryOneAdapter;
    private NewGroupBuyCategoryAdapter categoryTwoAdapter;
    private NewGroupBuyCategoryAdapter categoryThreeAdapter;

    private LinearLayout ll_category;
    private RelativeLayout rl_sort;
    private ListView lv_sort;
    private NewGroupBuySortAdapter sortAdapter;

    private boolean hasMoreData = true;// 是否还有更多数据
    private int currentPage;// 当前页
    private String divisionCode, divisionName, categoryId, catOne, catTwo, question;
    private String sortByClause ="0", sort = "0";
    public static String cityName;// 当前城市名

    public static int totalCount;
    public static int totalPage;
    public static int pageSize;
    public static int ser_currentPage;
    public static String show_city_name;//标题上展示的城市名字

    private NewGroupBuyAdapter newGroupBuyAdapter; // 团购列表adapter

    private ArrayList<Category> categroyList;// 一级分类
    private ArrayList<Category> categroyListTwo;// 二级分类
    private ArrayList<Category> categroyListThree;// 三级分类
    private ArrayList<Sort> sortList;// 排序
    private Time t;// 24点以后刷新用
    
    private String action;
    
    private boolean isRuning;//是否正在去分类数据
    private Handler mMainHandler;
    private LoadingDialog loadingDialog;
    
    private String firstSelectedCategroy;//当前被选中的第一级分类名称
    private String secondSelectedCategroy;//当前被选中的第二级分类名称
    //private String thirdSelectedCategroy;//当前被选中的第三级分类名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_groupbuy_product_type_bigpicture);
        initializeViews();// 初始化控件
        setTitleCity(getResources().getString(R.string.nationwide));// 设置团购城市
        setData(false);// 初始化数据
        getFilterData();//获取分类排序数据
        t = new Time();
        t.setToNow();
        int year = t.year;
        int month = t.month;
        int monthday = t.monthDay;
        t.set(0, 0, 24, monthday, month, year);
        
        action = getIntent().getAction();
        String messageId = getIntent().getStringExtra("messageId");
        String titles = getIntent().getStringExtra("title");
        if ("pushSertvice_no_sp".equals(action)) {
            BDebug.d("push_arrive", "到达团购页"+action);
            PushUtils.AsynFeedbackArrivedMessage(NewGroupBuyActivity.this,messageId,titles,"3");
        }
    }
    /**
     * 设置数据
     * 
     * @param is24Judge
     *            是否是24小时刷新
     */
    private void setData(final boolean is24Judge) {
        if (!NetUtility.isNetworkAvailable(NewGroupBuyActivity.this)) {
            CommonUtility.showMiddleToast(NewGroupBuyActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            lv_groupbuy_product.setVisibility(View.GONE);
            iv_groupbuy_nonet.setVisibility(View.VISIBLE);
            return;
        }
        lv_groupbuy_product.setVisibility(View.VISIBLE);
        iv_groupbuy_nonet.setVisibility(View.GONE);
        currentPage = 1;
        new AsyncTask<Object, Void, ArrayList<GroupBuyProduct>>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(NewGroupBuyActivity.this, getString(R.string.loading),
                        true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected ArrayList<GroupBuyProduct> doInBackground(Object... params) {
                String request = GBProductNew.createRequestGroupBuyProductListJson(divisionCode, divisionName,
                        categoryId, catOne, catTwo, sortByClause, sort, currentPage, Constants.PAGE_SIZE, question);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_NEW_GROUPBUY_PRODUCTS, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return GBProductNew.parseGroupBuYProductList(response);
            }

            @Override
            protected void onPostExecute(final ArrayList<GroupBuyProduct> result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                if (result == null) {
                    if (is24Judge) {
                        if ("HomeActivity".equals(NewGroupBuyActivity.this.getIntent().getAction())) {
                            finish();// 关闭当前页
                        }
                    } else {
                        CommonUtility.showMiddleToast(NewGroupBuyActivity.this, "",
                                getString(R.string.data_load_fail_exception));
                        tv_groupbuy_empty.setText(R.string.empty);
                        lv_groupbuy_product.setEmptyView(tv_groupbuy_empty);
                    }
                    return;
                }
                //设置城市
                if(!TextUtils.isEmpty(show_city_name)){
                    setTitleCity(show_city_name);
                    cityName = show_city_name;
                }
                
                
                isAutomaticLoad();// 判断是否还有更多数据
                if (newGroupBuyAdapter == null) {
                    lv_groupbuy_product.setLayoutAnimation(AnimationUtils.groupBuyListAnimation());
                    newGroupBuyAdapter = new NewGroupBuyAdapter(NewGroupBuyActivity.this, result);
                    newGroupBuyAdapter.setFlag_big_little_picture(0);
                    lv_groupbuy_product.setAdapter(newGroupBuyAdapter);
                    tv_groupbuy_empty.setText(R.string.empty);
                    lv_groupbuy_product.setEmptyView(tv_groupbuy_empty);
                    lv_groupbuy_product.setOnScrollListener(NewGroupBuyActivity.this);
                } else {
                    newGroupBuyAdapter.reload(result);
                }

            }

        }.execute();
    }

    // 加载更多
    private AsyncTask<Object, Void, ArrayList<GroupBuyProduct>> asyncTask = null;

    private void loadMoreData() {
        if (!NetUtility.isNetworkAvailable(NewGroupBuyActivity.this)) {
            CommonUtility.showMiddleToast(NewGroupBuyActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        if (asyncTask != null) {
            return;
        }
        asyncTask = new AsyncTask<Object, Void, ArrayList<GroupBuyProduct>>() {
            @Override
            protected ArrayList<GroupBuyProduct> doInBackground(Object... params) {
                String request = GBProductNew.createRequestGroupBuyProductListJson(divisionCode, divisionName,
                        categoryId, catOne, catTwo, sortByClause, sort, currentPage, Constants.PAGE_SIZE, question);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_NEW_GROUPBUY_PRODUCTS, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return GBProductNew.parseGroupBuYProductList(response);
            }

            @Override
            protected void onCancelled() {
                asyncTask = null;
            }

            @Override
            protected void onPostExecute(final ArrayList<GroupBuyProduct> result) {
                if (isCancelled()) {
                    return;
                }
                if (result == null) {
                    CommonUtility.showMiddleToast(NewGroupBuyActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                //设置城市
                if(!TextUtils.isEmpty(show_city_name)){
                    setTitleCity(show_city_name);
                    cityName = show_city_name;
                }
                isAutomaticLoad();
                newGroupBuyAdapter.addList(result);
                asyncTask = null;
            }
        };
        asyncTask.execute();
    }

    // 设置团购标题城市
    private void setTitleCity(String city) {
        int length = city.length();
        if (length > 4) {
            city = city.substring(0, 4) + "...";
        }
        tv_groupbuy_location.setText(city);
    }

    // 初始化View控件
    private void initializeViews() {
        bt_groupbuy_back = (Button) this.findViewById(R.id.bt_groupbuy_back);
        rl_groupbuy_title = (RelativeLayout) this.findViewById(R.id.rl_groupbuy_title);
        tv_groupbuy_title = (TextView) this.findViewById(R.id.tv_groupbuy_title);
        tv_groupbuy_location = (TextView) this.findViewById(R.id.tv_groupbuy_location);
        iv_groupbuy_location = (ImageView) this.findViewById(R.id.iv_groupbuy_location);
        bt_groupbuy_show_change = (Button) this.findViewById(R.id.bt_groupbuy_show_change);
        ll_groupbuy_categroy = (LinearLayout) this.findViewById(R.id.ll_groupbuy_categroy);
        tv_groupbuy_categroy = (TextView) this.findViewById(R.id.tv_groupbuy_categroy);
        iv_groupbuy_categroy = (ImageView) this.findViewById(R.id.iv_groupbuy_categroy);
        ll_groupbuy_sort = (LinearLayout) this.findViewById(R.id.ll_groupbuy_sort);
        tv_groupbuy_sort = (TextView) this.findViewById(R.id.tv_groupbuy_sort);
        iv_groupbuy_sort = (ImageView) this.findViewById(R.id.iv_groupbuy_sort);
        ll_groupbuy_search = (LinearLayout) this.findViewById(R.id.ll_groupbuy_search);
        iv_groupbuy_search = (ImageView) this.findViewById(R.id.iv_groupbuy_search);
        lv_groupbuy_product = (ListView) this.findViewById(R.id.lv_groupbuy_product);
        tv_groupbuy_empty = (TextView) this.findViewById(android.R.id.empty);
        iv_groupbuy_nonet = (ImageView) this.findViewById(R.id.iv_groupbuy_nonet);
        loadingView = (LinearLayout) View.inflate(this, R.layout.common_loading_layout, null);
        tv_loadMore = (TextView) loadingView.findViewById(R.id.common_loading_title);
        pb_loadMore = (ProgressBar) loadingView.findViewById(R.id.loadingbar);
        // 注册点击事件
        bt_groupbuy_back.setOnClickListener(this);
        tv_groupbuy_title.setOnClickListener(this);
        tv_groupbuy_location.setOnClickListener(this);
        iv_groupbuy_location.setOnClickListener(this);
        bt_groupbuy_show_change.setOnClickListener(this);
        bt_groupbuy_show_change.setTag(0);
        ll_groupbuy_categroy.setOnClickListener(this);
        ll_groupbuy_sort.setOnClickListener(this);
        ll_groupbuy_search.setOnClickListener(this);
        lv_groupbuy_product.setOnItemClickListener(new GroupbuyProductsItemClick());
    }

    @Override
    protected void onDestroy() {
        totalCount = 0;
        totalPage = 0;
        pageSize = 0;
        ser_currentPage = 0;
        categoryPopuWindow = null; // 筛选分类弹出的popuwindow
        v_categroy = null;
        lv_catrgroy_one = null;// 第一级listView
        lv_catrgroy_two = null;// 第二级listView
        lv_catrgroy_three = null;// 第三级listView
        super.onDestroy();
    }

    // 判断是否还有更多数据
    private void isAutomaticLoad() {

        loadingView.setVisibility(View.VISIBLE);
        currentPage++;
        if (currentPage > totalPage) {// 没有商品可取
            hasMoreData = false;
            if (lv_groupbuy_product.getFooterViewsCount() == 0) {
                lv_groupbuy_product.addFooterView(loadingView);
            }
            tv_loadMore.setText(R.string.no_more);
            tv_loadMore.setVisibility(View.VISIBLE);
            pb_loadMore.setVisibility(View.GONE);
        } else {// 还可以继续取
            hasMoreData = true;
            tv_loadMore.setVisibility(View.VISIBLE);
            tv_loadMore.setText(R.string.load_more);
            pb_loadMore.setVisibility(View.VISIBLE);
            if (lv_groupbuy_product.getFooterViewsCount() == 0) {
                lv_groupbuy_product.addFooterView(loadingView);
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount) {// 滑动到底部
            if (hasMoreData && newGroupBuyAdapter != null && newGroupBuyAdapter.getCount() > 0) {
                loadMoreData();
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.bt_groupbuy_back:
            finish();// 关闭当前页
            break;
        case R.id.tv_groupbuy_title:
        case R.id.tv_groupbuy_location:
        case R.id.iv_groupbuy_location:
            // 进入切换城市界面
            Intent intent = new Intent(NewGroupBuyActivity.this, NewGroupBuyChangeCityActivity.class);
            startActivityForResult(intent, 10);
            break;
        case R.id.bt_groupbuy_show_change:
            // 大小图切换
            if (newGroupBuyAdapter != null) {
                int tag = (Integer) bt_groupbuy_show_change.getTag();
                if (1 == tag) {
                    lv_groupbuy_product.setLayoutAnimation(AnimationUtils.groupBuyListAnimation());
                    newGroupBuyAdapter.setBigPictureOrLittlePicture(0);
                    bt_groupbuy_show_change.setBackgroundResource(R.drawable.bg_topbar_switch_mini_false);
                    bt_groupbuy_show_change.setTag(0);
                } else if (0 == tag) {
                    lv_groupbuy_product.setLayoutAnimation(AnimationUtils.groupBuyListAnimation());
                    newGroupBuyAdapter.setBigPictureOrLittlePicture(1);
                    bt_groupbuy_show_change.setBackgroundResource(R.drawable.bg_hot_prom_pager);
                    bt_groupbuy_show_change.setTag(1);
                }
            }
            break;
        case R.id.ll_groupbuy_categroy:
            tv_groupbuy_categroy.setTextColor(Color.parseColor("#cc1000"));
            tv_groupbuy_sort.setTextColor(Color.parseColor("#333333"));
            iv_groupbuy_categroy.setBackgroundResource(R.drawable.groupbuy_arrow_open_up);
            // 设置按钮选择状态
            ll_groupbuy_categroy.setSelected(true);
            ll_groupbuy_sort.setSelected(false);
            // 弹出分类popuwindow
            getCategroyData(1);
            break;
        case R.id.ll_groupbuy_sort:
            tv_groupbuy_sort.setTextColor(Color.parseColor("#cc1000"));
            tv_groupbuy_categroy.setTextColor(Color.parseColor("#333333"));
            iv_groupbuy_sort.setBackgroundResource(R.drawable.groupbuy_arrow_open_up);
            // 设置按钮选择状态
            ll_groupbuy_sort.setSelected(true);
            ll_groupbuy_categroy.setSelected(false);
            // 弹出排序popuwindow
            getCategroyData(2);
            break;
        case R.id.ll_groupbuy_search:
            // 进入搜索页面
            Intent intentSearch = new Intent(NewGroupBuyActivity.this, NewGroupBuySearchActivity.class);
            startActivityForResult(intentSearch, 11);
            break;
        default:
            break;
        }

    }

    // 获取分类数据categoryOrSort代表是分类还是排序1：分类，2：排序
    private void getCategroyData(final int categoryOrSort) {
        if(isRuning){
            loadingDialog = CommonUtility.showLoadingDialog(NewGroupBuyActivity.this, "正在获取分类条件...", true,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            //cancel(true);
                        }
                    });
            mMainHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                    case 1:
                        loadingDialog.dismiss();
                        getCategroyData(categoryOrSort);
                        break;

                    default:
                        break;
                    }
                }
            };
            
        }else{
            if (categoryOrSort == 2 && sortList != null && sortList.size() > 0) {
                showCategroy(categoryOrSort, sortList);// 弹出排序窗口
            } else if (categoryOrSort == 1 && categroyList != null && categroyList.size() > 0) {
                showCategroy(categoryOrSort, categroyList);// 弹出分类窗口
            } else {
               if(categoryOrSort == 2){
                   CommonUtility.showToast(NewGroupBuyActivity.this, "获取排序数据失败");
               }else if(categoryOrSort == 1){
                   CommonUtility.showToast(NewGroupBuyActivity.this, "获取分类数据失败");
               }
            }
        }
       

    }
    /**
     * 获取分类和排序的数据
     */
    private void getFilterData(){
        if (!NetUtility.isNetworkAvailable(NewGroupBuyActivity.this)) {
            CommonUtility.showMiddleToast(NewGroupBuyActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, CategroyAndSort>() {

            @Override
            protected void onPreExecute() {
                isRuning = true;
            }

            @Override
            protected CategroyAndSort doInBackground(Object... params) {
                String response = NetUtility.sendHttpRequestByGet(Constants.URL_NEW_GROUPBUY_CATEGORYS);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return GBProductNew.parseFilterConditionList(response);
            }

            @Override
            protected void onPostExecute(CategroyAndSort result) {
                if (isCancelled()) {
                    isRuning = false;
                    return;
                }
                if (result == null) {
                    isRuning = false;
                    CommonUtility.showMiddleToast(NewGroupBuyActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                categroyList = result.getCategroyList();
                sortList = result.getSortList();
                isRuning = false;
                if(mMainHandler!=null){
                    mMainHandler.sendEmptyMessage(1);  
                }
                
            }
            }.execute();
        
    }
    // 弹出分类菜单
    private void showCategroy(int categoryOrSort, Object object) {
        if (v_categroy == null) {
            v_categroy = this.getLayoutInflater().inflate(R.layout.groupbuy_category_popuwindow, null);
            categoryPopuWindow = new PopupWindow(v_categroy, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            categoryPopuWindow.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
            categoryPopuWindow.setOutsideTouchable(true);// 点击外侧能够取消
            categoryPopuWindow.setFocusable(true);// 获取焦点不然列表点击无效
            ll_category = (LinearLayout) v_categroy.findViewById(R.id.ll_category);
            lv_catrgroy_one = (ListView) v_categroy.findViewById(R.id.lv_category_one);
            lv_catrgroy_two = (ListView) v_categroy.findViewById(R.id.lv_category_two);
            iv_category_two_bottom_arrow = (ImageView) v_categroy.findViewById(R.id.iv_category_two_bottom_arrow);
            iv_category_two_zhezhao = (ImageView) v_categroy.findViewById(R.id.iv_category_two_zhezhao);
            rl_category_three = (RelativeLayout) v_categroy.findViewById(R.id.rl_category_three);
            lv_catrgroy_three = (ListView) v_categroy.findViewById(R.id.lv_category_three);
            iv_category_three_bottom_arrow = (ImageView) v_categroy.findViewById(R.id.iv_category_three_bottom_arrow);
            rl_sort = (RelativeLayout) v_categroy.findViewById(R.id.rl_sort);
            lv_sort = (ListView) v_categroy.findViewById(R.id.lv_sort);
            lv_sort.setOnItemClickListener(new MyGroupbuySortItemClick());
            lv_catrgroy_one.setOnItemClickListener(new MyGroupbuyCategoryOneItemClick());
            lv_catrgroy_two.setOnItemClickListener(new MyGroupbuyCategoryTwoItemClick());
            lv_catrgroy_two.setOnScrollListener(new MyGroupbuyCategroyTwoScrollListener());
            lv_catrgroy_three.setOnItemClickListener(new MyGroupbuyCategoryThreeItemClick());
            lv_catrgroy_three.setOnScrollListener(new MyGroupbuyCategroyThreeScrollListener());
            categoryPopuWindow.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss() {
                    tv_groupbuy_sort.setTextColor(Color.parseColor("#333333"));
                    iv_groupbuy_categroy.setBackgroundResource(R.drawable.groupbuy_arrow_pick_up);
                    tv_groupbuy_categroy.setTextColor(Color.parseColor("#333333"));
                    iv_groupbuy_sort.setBackgroundResource(R.drawable.groupbuy_arrow_pick_up);
                    ll_groupbuy_categroy.setSelected(false);
                    ll_groupbuy_sort.setSelected(false);

                }
            });
        }
        if (categoryOrSort == 1) {
            if (ll_category.getVisibility() == View.GONE) {
                ll_category.setVisibility(View.VISIBLE);
            }
            if (rl_sort.getVisibility() == View.VISIBLE) {
                rl_sort.setVisibility(View.GONE);
            }
            if (categoryOneAdapter == null) {
                categoryOneAdapter = new NewGroupBuyCategoryAdapter(this, (ArrayList<Category>) object);
                categoryOneAdapter.setFlag(1);
                categroyList.get(0).setSelected(true);
                lv_catrgroy_one.setAdapter(categoryOneAdapter);
            } else {
                categoryOneAdapter.setFlag(1);
                lv_catrgroy_one.setAdapter(categoryOneAdapter);
            }
        } else if (categoryOrSort == 2) {
            if (rl_sort.getVisibility() == View.GONE) {
                rl_sort.setVisibility(View.VISIBLE);
            }
            if (ll_category.getVisibility() == View.VISIBLE) {
                ll_category.setVisibility(View.GONE);
            }
            if (sortAdapter == null) {
                sortAdapter = new NewGroupBuySortAdapter(this, (ArrayList<Sort>) object);
                sortList.get(0).setSelected(true);
                lv_sort.setAdapter(sortAdapter);
            } else {
                lv_sort.setAdapter(sortAdapter);
            }
        }
        // categoryPopuWindow.setAnimationStyle(R.style.groupbuy_popuwindow_animation);
        categoryPopuWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        if (!categoryPopuWindow.isShowing()) {
            categoryPopuWindow.showAsDropDown(ll_groupbuy_categroy);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
        case 2:// 城市列表页返回
            divisionCode = data.getStringExtra("divisionCode");
            if (!TextUtils.isEmpty(cityName)) {
                divisionName = cityName;
                //setTitleCity(divisionName);
                setData(false);
            }
            break;
        case 4:// 到团购券，将此模块界面都关闭
            finish();
            break;
        case 5:
            Intent intent = new Intent(this, HomeActivity.class);
            setResult(5, intent);
            finish();
            break;
        }
    }

    /**
     * 团购列表ListView item点击时的处理事件
     * 
     * @author liuyang-ds
     * 
     */
    public class GroupbuyProductsItemClick implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position <= newGroupBuyAdapter.getCount() - 1) {
                GroupBuyProduct gbproduct = (GroupBuyProduct) newGroupBuyAdapter.getItem(position);
                if (gbproduct != null) {
                    Intent groupBuyIntent = new Intent(NewGroupBuyActivity.this, NewGroupBuyDetailActivity.class);
                    groupBuyIntent.putExtra(NewGroupBuyDetailActivity.SALEPROMOITEM, gbproduct.getSalePromoItem());
                    // groupBuyIntent.putExtra(NewGroupBuyDetailActivity.CATEGORYCOLOR, gbproduct.getCategoryColor());
                    startActivityForResult(groupBuyIntent, 4);
                }
            }
        }

    }

    /**
     * 分类ListView 一级点击时的处理事件
     * 
     * @author liuyang-ds
     * 
     */
    public class MyGroupbuyCategoryOneItemClick implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (rl_category_three.getVisibility() == View.VISIBLE) {
                rl_category_three.setVisibility(View.GONE);
                if (categoryThreeAdapter != null) {
                    categoryThreeAdapter.reloadData(null);
                }
            }
            if (categroyListTwo != null) {
                for (int i = 0 , size = categroyListTwo.size(); i < size; i++) {
                    categroyListTwo.get(i).setSelected(false);
                }
            }

            if (iv_category_two_zhezhao.getVisibility() == View.VISIBLE) {
                iv_category_two_zhezhao.setVisibility(View.GONE);
            }
            // 设置被选择状态
            for (int i = 0 , size = categroyList.size(); i < size; i++) {
                if (i == position) {
                    categroyList.get(i).setSelected(true);
                } else {
                    categroyList.get(i).setSelected(false);
                }
            }
            Category category = categroyList.get(position);
            
            categroyListTwo = category.getSunCategroyList();
            if (categroyListTwo != null && categroyListTwo.size() > 0) {
                if (categoryTwoAdapter == null) {
                    categoryTwoAdapter = new NewGroupBuyCategoryAdapter(NewGroupBuyActivity.this, categroyListTwo);
                    categoryTwoAdapter.setFlag(2);
                    lv_catrgroy_two.setAdapter(categoryTwoAdapter);
                } else {
                    categoryTwoAdapter.setFlag(2);
                    categoryTwoAdapter.reloadData(categroyListTwo);
                }

            } else {
                if (categoryTwoAdapter != null) {
                    categoryTwoAdapter.reloadData(null);
                }
                iv_category_two_bottom_arrow.setVisibility(View.GONE);
                //标题设置为选中的一级分类
                tv_groupbuy_categroy.setText(category.getCategoryName());
                // 进入商品列表检索
                categoryId = category.getCategoryId();
                setData(false);
                categoryPopuWindow.dismiss();

            }
            //给当前被选中的分类赋值
            firstSelectedCategroy = category.getCategoryName();
            categoryOneAdapter.notifyDataSetChanged();// 刷新一级分类列表

        }

    }

    /**
     * 分类ListView 二级点击时的处理事件
     * 
     * @author liuyang-ds
     * 
     */
    public class MyGroupbuyCategoryTwoItemClick implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // 设置被选择状态
            for (int i = 0 , size = categroyListTwo.size(); i < size; i++) {
                if (i == position) {
                    categroyListTwo.get(i).setSelected(true);
                } else {
                    categroyListTwo.get(i).setSelected(false);
                }
            }
            if (categroyListThree != null) {
                for (int i = 0 , size = categroyListThree.size(); i < size; i++) {
                    categroyListThree.get(i).setSelected(false);
                }
            }

            Category category = categroyListTwo.get(position);
            
            categroyListThree = category.getSunCategroyList();
            if (categroyListThree != null && categroyListThree.size() > 0) {
                rl_category_three.setVisibility(View.VISIBLE);
                if (categoryThreeAdapter == null) {
                    categoryThreeAdapter = new NewGroupBuyCategoryAdapter(NewGroupBuyActivity.this, categroyListThree);
                    categoryThreeAdapter.setFlag(3);
                    lv_catrgroy_three.setAdapter(categoryThreeAdapter);
                } else {
                    categoryThreeAdapter.setFlag(3);
                    categoryThreeAdapter.reloadData(categroyListThree);
                }

            } else {
                if (categoryThreeAdapter != null) {
                    categoryThreeAdapter.reloadData(null);
                }
                if("全部".equals(category.getCategoryName())){
                    tv_groupbuy_categroy.setText(firstSelectedCategroy);
                }else{
                    tv_groupbuy_categroy.setText(category.getCategoryName());
                }
                // 进入商品列表检索
                categoryId = category.getCategoryId();
                setData(false);
                categoryPopuWindow.dismiss();
            }
            //给当前被选中的分类赋值
            secondSelectedCategroy = category.getCategoryName();
            
            categoryTwoAdapter.notifyDataSetChanged();// 刷新二级级分类列表
        }

    }

    /**
     * 分类ListView 三级点击时的处理事件
     * 
     * @author liuyang-ds
     * 
     */
    public class MyGroupbuyCategoryThreeItemClick implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            for (int i = 0 , size = categroyListThree.size(); i < size; i++) {
                if (i == position) {
                    categroyListThree.get(i).setSelected(true);
                } else {
                    categroyListThree.get(i).setSelected(false);
                }
            }
            categoryThreeAdapter.notifyDataSetChanged();// 刷新三级级分类列表
            // 进入商品列表检索
            Category category = categroyListThree.get(position);
            if("全部".equals(category.getCategoryName())){
                tv_groupbuy_categroy.setText(secondSelectedCategroy);
            }else{
                tv_groupbuy_categroy.setText(category.getCategoryName());
            }
            categoryId = category.getCategoryId();
            setData(false);
            categoryPopuWindow.dismiss();
        }

    }

    /**
     * 分类ListView 三级点击时的处理事件
     * 
     * @author liuyang-ds
     * 
     */
    public class MyGroupbuySortItemClick implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            for (int i = 0 , size = sortList.size(); i < size; i++) {
                if (i == position) {
                    sortList.get(i).setSelected(true);
                } else {
                    sortList.get(i).setSelected(false);
                }
            }
            sortAdapter.notifyDataSetChanged();// 刷新三级级分类列表
            // 进入商品列表检索
            Sort sort = sortList.get(position);
            sortByClause = sort.getSortKey();
            setData(false);
            categoryPopuWindow.dismiss();
        }

    }

    /**
     * 二级分类滑动时间监听
     * 
     * @author liuyang-ds
     * 
     */
    public class MyGroupbuyCategroyTwoScrollListener implements OnScrollListener {

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (categroyListTwo != null && categroyListTwo.size() > 0) {
                if (firstVisibleItem + visibleItemCount == categroyListTwo.size()) {
                    iv_category_two_bottom_arrow.setVisibility(View.GONE);
                } else {
                    iv_category_two_bottom_arrow.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

    }

    /**
     * 三级分类滑动时间监听
     * 
     * @author liuyang-ds
     * 
     */
    public class MyGroupbuyCategroyThreeScrollListener implements OnScrollListener {

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (categroyListThree != null && categroyListThree.size() > 0) {
                if (firstVisibleItem + visibleItemCount == categroyListThree.size()) {
                    iv_category_three_bottom_arrow.setVisibility(View.GONE);
                } else {
                    iv_category_three_bottom_arrow.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        AppMeasurementUtils appMeasurementProm = new AppMeasurementUtils(NewGroupBuyActivity.this);
        appMeasurementProm.getUrl(getString(R.string.appMeas_actiCenter) + ":" + getString(R.string.appMeas_groupprom),
                getString(R.string.appMeas_actiCenter), getString(R.string.appMeas_actiCenter) + ":"
                        + getString(R.string.appMeas_groupprom), getString(R.string.appMeas_groupprom), "", "", "", "",
                "", "", "", "", getString(R.string.appMeas_myprom), "", "", "", null);
        Time t2 = new Time();
        t2.setToNow();
        boolean isorNo = t2.after(t);
        if (isorNo) {
            setData(true);
            t.set(0, 0, 24, t2.monthDay, t2.month, t2.year);
        }
    }
}
