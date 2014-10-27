package com.gome.ecmall.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.gome.ecmall.bean.AbleLoginEntity;
import com.gome.ecmall.bean.ActivityEntity;
import com.gome.ecmall.bean.Category;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.HotWordSearch;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.KeyWordSearch;
import com.gome.ecmall.bean.LimitBuyResult;
import com.gome.ecmall.bean.LimitBuyResult.LimitBuyGoods;
import com.gome.ecmall.custom.ClickNotTransferLinearlayout;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.custom.PageIndicator;
import com.gome.ecmall.custom.RefreshScrollView;
import com.gome.ecmall.custom.RefreshScrollView.OnRefreshListener;
import com.gome.ecmall.custom.RefreshableView;
import com.gome.ecmall.dao.ActivityRecommendDao;
import com.gome.ecmall.dao.SearchHistoryDao;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.HomeAdPageAdapter.OnPageItemClickListener;
import com.gome.ecmall.home.barcode.BarcodeScanResultHistoryActivity;
import com.gome.ecmall.home.barcode.BarcodeScanReusltListActivity;
import com.gome.ecmall.home.category.ProductShowActivity;
import com.gome.ecmall.home.coupon.GetCouponActivity;
import com.gome.ecmall.home.groupbuy.NewGroupBuyActivity;
import com.gome.ecmall.home.groupbuy.NewGroupBuyDetailActivity;
import com.gome.ecmall.home.homepage.SearchHistoryAdapter;
import com.gome.ecmall.home.homepage.SearchResultActivity;
import com.gome.ecmall.home.hotproms.HotPromTheTemActivity;
import com.gome.ecmall.home.hotproms.HotPromotions2Activity;
import com.gome.ecmall.home.hotproms.PreSellActivity;
import com.gome.ecmall.home.hotproms.PromtionActivitiesActivity;
import com.gome.ecmall.home.limitbuy.LimitbuyDetailActivity;
import com.gome.ecmall.home.limitbuy.NewLimitbuyActivity;
import com.gome.ecmall.home.login.LoginActivity;
import com.gome.ecmall.home.more.AnnouncementDetailActivity;
import com.gome.ecmall.home.more.AnnouncementListActivity;
import com.gome.ecmall.home.more.CityListActivity;
import com.gome.ecmall.home.more.NearStoreListActivity;
import com.gome.ecmall.home.more.ProductBrowseHistoryActivity;
import com.gome.ecmall.home.mygome.MyFavActivity;
import com.gome.ecmall.home.mygome.MyOrderActivity;
import com.gome.ecmall.phonerecharge.PhoneRechargeActivity;
import com.gome.ecmall.util.AppMeasurementUtils;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.FileUtils;
import com.gome.ecmall.util.ImageUtils;
import com.gome.ecmall.util.NetUtility;
import com.gome.ecmall.util.PreferenceUtils;
import com.gome.eshopnew.R;

public class HomeActivity extends AbsSubActivity implements OnItemClickListener, OnClickListener,
        OnEditorActionListener, RefreshableView.RefreshListener, OnPageChangeListener, OnFocusChangeListener,
        TextWatcher {

    /**
     * log 日志键
     */
    private static final String TAG = "HomeActivity";

    private ViewPager viewPager;
    private HomeAdPageAdapter pagerAdapter;

    /**
     * 首页焦点图点视图
     */
    private PageIndicator pageIndicator;
    // private GridView gridView;
    // private HomeEntriesAdapter entriesAdapter;

    /**
     * 下拉刷新组件
     */
    private RefreshScrollView refreshableView;
    // private ScrollView scrollView;

    /**
     * 搜索输入框
     */
    public static EditText etInput;
    private Button btnCategory;
    // private Button btnRefresh;
    /**
     * 搜索按钮
     */
    public static Button btnSearch;
    private int currentSearchType;
    private String[] search_category;
    private ArrayList<Category> search_category_list;
    private View searchTipsLayout;
    private TextView[] tvHotWords;
    private LinearLayout keyWordsLayout;
    private ListView historySearchList;
    
    private AsyncTask<Object, Void, ArrayList<ActivityEntity>> asyncActivityRecommendTask;
    public static final int MSG_ACTION_SLIDE_PAGE = 999;
    public static final int MSG_FINSIH_REFRESH = 1000;
    private RotateAnimation rotateAnimation;
    private Timer timer;
    private TimerTask timerTask;
    public static int selectButtonIndex = -1;// 记录按钮的点击
    private ArrayList<String> mSearchHotKeyWordsList;
    private ArrayList<String> mSearchHistoryList;
    private int seachIndex = GlobalConfig.getInstance().getSeachIndex();
    private int currentPage = 1;
    private View clearAllHistory;// 清空搜索历史
    private TextView deletImage;
    private TextView barSearch;
    /**
     * 首页抢购推荐视图
     */
    private RelativeLayout homeRushbuyCommend;
    private TextView hourTextView, miniteTextView, secondTextView, goodsName, rushbuyPrice, originalPrice;
    private ImageView rushbuyGoodsImage;
    private ImageView homeTopLogo;
    private LimitBuyGoods limitGoods;

    private LinearLayout home_add_layout;
    private GlobalApplication application;

    private boolean canLogin = true;

    /**
     * 0=普通活动；1=抢购；2=预售广告图；3=预售广告电话；4=热门促销广告；5=热门促销商品；
     * <p>
     * 6=今日限时抢购广告；7=限时抢购商品；8=优惠团购列表；9=团购商品；10=团购列表；
     * <p>
     * 12=获取优惠券入口
     */
    private String[] types = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "12" };

    public static final String FORMFLAG = "formFlag";// 点击首页单挑抢购跳入抢购页面时的标记
    public static final String SERVERTIME = "serverTime";// 点击首页单挑抢购跳入抢购页面时的标记
    public static final String RUSH_BUY_ITEM_ID = "rushBuyItemId";// 抢购商品rushbuyId
    // private String comeFromPage;

    public static int st;
    public static String newId;
    public static String activityId;
    public static String activityType;
    public static String title;
    public static String messageId;

    // 各个模块按钮
    private ClickNotTransferLinearlayout ll_home_limit_buy;// 限时抢购
    private ClickNotTransferLinearlayout ll_home_group_buy;// 优惠团购
    private ClickNotTransferLinearlayout ll_home_hot_proms;// 热门促销
    private ClickNotTransferLinearlayout ll_home_activitys;// 活动专区
    private ClickNotTransferLinearlayout ll_home_phone_recharge;// 手机充值
    private ClickNotTransferLinearlayout ll_home_order_query;// 活动专区
    private ClickNotTransferLinearlayout ll_home_my_fav;// 我的收藏
    private ClickNotTransferLinearlayout ll_home_history;// 浏览历史
    private ClickNotTransferLinearlayout ll_home_gome_shop;// gome门店
    private ClickNotTransferLinearlayout ll_home_announcement;// 商城公告
    private ClickNotTransferLinearlayout ll_home_settings_about;// 设置关于
    private LinearLayout ll_bg;// 国美社区

    private Time t;// 24点以后刷新用

    private class AutoScrollTimerTask extends TimerTask {
        @Override
        public void run() {
            handler.sendEmptyMessage(MSG_ACTION_SLIDE_PAGE);
        }
    };

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_ACTION_SLIDE_PAGE:
                if (currentPageState == ViewPager.SCROLL_STATE_IDLE) {
                    if (viewPager.getAdapter() != null) {
                        // int count = viewPager.getAdapter().getCount();
                        int current = viewPager.getCurrentItem();

                        // if (current + 1 < count) {
                        // viewPager.setCurrentItem(current + 1, true);
                        // } else {
                        // viewPager.setCurrentItem(0, true);
                        // }

                        // 获取当前的索引加一，以滑动到下一项
                        viewPager.setCurrentItem((current + 1), true);
                    }
                }
                break;
            case MSG_FINSIH_REFRESH:
                refreshableView.onRefreshComplete();
                break;
            case 0:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(homeTopLogo.getWindowToken(), 0);
                break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_indexpage_layout);
        application = (GlobalApplication) getApplication();
        initView();
        initData();
        // View decorView = getWindow().getDecorView();
        // decorView.requestLayout();
        // decorView.invalidate();
        // 24点以后刷新用到
        t = new Time();
        t.setToNow();
        int year = t.year;
        int month = t.month;
        int monthday = t.monthDay;
        t.set(0, 0, 24, monthday, month, year);

    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        home_add_layout = (LinearLayout) inflater.inflate(R.layout.home_add_layout, null);
        viewPager = (ViewPager) home_add_layout.findViewById(R.id.home_viewpager);
        viewPager.setOnPageChangeListener(this);
        pageIndicator = (PageIndicator) home_add_layout.findViewById(R.id.home_page_indictor);
        pageIndicator.setPageOrginal(true);
        // gridView = (GridView) home_add_layout.findViewById(R.id.home_gv_entries);
        // gridView.setOnItemClickListener(this);
        deletImage = (TextView) findViewById(R.id.login_code_del_imageView);
        deletImage.setOnClickListener(this);
        barSearch = (TextView) findViewById(R.id.tv_home_bar_search);
        barSearch.setOnClickListener(this);
        btnCategory = (Button) findViewById(R.id.home_homepage_btn_search_category);
        btnCategory.setOnClickListener(this);
        btnSearch = (Button) findViewById(R.id.home_homepage_search_btn);
        btnSearch.setOnClickListener(this);
        btnSearch.setText(getString(R.string.search));
        etInput = (EditText) findViewById(R.id.home_homepage_et_input);
        etInput.setHint(getSearchHintContent());
        etInput.setOnEditorActionListener(this);
        etInput.setOnFocusChangeListener(this);
        etInput.addTextChangedListener(this);
        homeTopLogo = (ImageView) findViewById(R.id.home_homepage_top_logo);
        // btnRefresh = (Button) findViewById(R.id.home_homepage_top_btn_refresh);
        // btnRefresh.setOnClickListener(this);
        refreshableView = (RefreshScrollView) findViewById(R.id.home_refreshable_view);

        refreshableView.addChild(home_add_layout, 1);
        refreshableView.setonRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                performLimitRecommend();
                performAsyncUpdateAds();
            }
        });
        // scrollView = (ScrollView) findViewById(R.id.home_indexpage_sv);
        // refreshableView.sv = scrollView;
        // refreshableView.setRefreshListener(this);
        // 实例化搜索提示view
        searchTipsLayout = findViewById(R.id.home_search_tips_layout);
        searchTipsLayout.setOnClickListener(null);// 防止事件穿透
        keyWordsLayout = (LinearLayout) findViewById(R.id.home_search_tips_hot_layout);
        tvHotWords = new TextView[5];
        tvHotWords[0] = (TextView) findViewById(R.id.home_search_tips_hot_words_first);
        tvHotWords[1] = (TextView) findViewById(R.id.home_search_tips_hot_words_second);
        tvHotWords[2] = (TextView) findViewById(R.id.home_search_tips_hot_words_third);
        tvHotWords[3] = (TextView) findViewById(R.id.home_search_tips_hot_words_forth);
        tvHotWords[4] = (TextView) findViewById(R.id.home_search_tips_hot_words_five);
        historySearchList = (ListView) findViewById(R.id.home_search_tips_place_holder_list);
        clearAllHistory = View.inflate(this, R.layout.search_clear_all_history, null);
        rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(-1);
        // 首页抢购推荐
        homeRushbuyCommend = (RelativeLayout) home_add_layout.findViewById(R.id.home_rushbuy_commend);
        homeRushbuyCommend.setOnClickListener(this);
        rushbuyGoodsImage = (ImageView) homeRushbuyCommend.findViewById(R.id.home_limit_goodsImage);
        hourTextView = (TextView) homeRushbuyCommend.findViewById(R.id.home_limit_hour);
        miniteTextView = (TextView) homeRushbuyCommend.findViewById(R.id.home_limit_minite);
        secondTextView = (TextView) homeRushbuyCommend.findViewById(R.id.home_limit_second);
        goodsName = (TextView) homeRushbuyCommend.findViewById(R.id.home_limit_goodsName);
        rushbuyPrice = (TextView) homeRushbuyCommend.findViewById(R.id.home_limit_price);
        originalPrice = (TextView) homeRushbuyCommend.findViewById(R.id.home_limit_original_price);
        // 以下是首页进入各个模块的按钮
        ll_home_limit_buy = (ClickNotTransferLinearlayout) home_add_layout.findViewById(R.id.ll_home_limit_buy);
        ll_home_group_buy = (ClickNotTransferLinearlayout) home_add_layout.findViewById(R.id.ll_home_group_buy);
        ll_home_hot_proms = (ClickNotTransferLinearlayout) home_add_layout.findViewById(R.id.ll_home_hot_proms);
        ll_home_activitys = (ClickNotTransferLinearlayout) home_add_layout.findViewById(R.id.ll_home_activitys);
        ll_home_phone_recharge = (ClickNotTransferLinearlayout) home_add_layout
                .findViewById(R.id.ll_home_phone_recharge);
        ll_home_order_query = (ClickNotTransferLinearlayout) home_add_layout.findViewById(R.id.ll_home_order_query);
        ll_home_my_fav = (ClickNotTransferLinearlayout) home_add_layout.findViewById(R.id.ll_home_my_fav);
        ll_home_history = (ClickNotTransferLinearlayout) home_add_layout.findViewById(R.id.ll_home_history);
        ll_home_gome_shop = (ClickNotTransferLinearlayout) home_add_layout.findViewById(R.id.ll_home_gome_shop);
        ll_home_announcement = (ClickNotTransferLinearlayout) home_add_layout.findViewById(R.id.ll_home_announcement);
        ll_home_settings_about = (ClickNotTransferLinearlayout) home_add_layout
                .findViewById(R.id.ll_home_settings_about);
        ll_bg = (LinearLayout) home_add_layout.findViewById(R.id.ll_bg);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.home_block_bg);
        BitmapDrawable drawable = new BitmapDrawable(bitmap);
        drawable.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
        drawable.setDither(true);
        ll_bg.setBackgroundDrawable(drawable);
        ll_home_limit_buy.setOnClickListener(this);
        ll_home_group_buy.setOnClickListener(this);
        ll_home_hot_proms.setOnClickListener(this);
        ll_home_activitys.setOnClickListener(this);
        ll_home_phone_recharge.setOnClickListener(this);
        ll_home_order_query.setOnClickListener(this);
        ll_home_my_fav.setOnClickListener(this);
        ll_home_history.setOnClickListener(this);
        ll_home_gome_shop.setOnClickListener(this);
        ll_home_announcement.setOnClickListener(this);
        ll_home_settings_about.setOnClickListener(this);

    }

    private void initData() {
        currentSearchType = 0;
        btnCategory.setText(getString(R.string.all));
        // 异步加载广告条历史记录
        performAsyncLoadRecommendHistory();
        // 获取首页抢购推荐
        performLimitRecommend();
        // 时间计时器开始倒计时
        time.schedule(timerTaskLimit, 0, 1000);
    }

    /**
     * 首页抢购推荐
     */
    private void performLimitRecommend() {
        new AsyncTask<Object, Void, LimitBuyGoods>() {
            protected LimitBuyGoods doInBackground(Object... params) {
                String response = NetUtility.sendHttpRequestByGet(Constants.URL_HOME_RUSHBUY_COMMEND);
                BDebug.e("HomeActivity:==", response);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                if (TextUtils.isEmpty(response)) {
                    return null;
                }
                return LimitBuyResult.parseLimitBuyGoods(response);
            };

            @Override
            protected void onPostExecute(LimitBuyGoods result) {
                if (result != null && result.getGoodsNo() != null && result.getRushBuyItemId() != null) {
                    BDebug.e("首页抢购推荐", "首页抢购推荐：" + result);
                    limitGoods = result;
                    goodsName.setText(result.getSkuName());
                    if (CommonUtility.isOrNoZero(result.getSkuRushBuyPrice(), false)) {
                        rushbuyPrice.setText(HomeActivity.this.getString(R.string.now_not_have_price));
                    } else {
                        rushbuyPrice.setText("￥" + result.getSkuRushBuyPrice());
                    }
                    // rushbuyPrice.setText("￥" + result.getSkuRushBuyPrice());
                    originalPrice.setText("原价：￥" + result.getSkuOriginalPrice());
                    originalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    String imageUrl = result.getSkuThumbImgUrl();
                    if (!GlobalConfig.getInstance().isNeedLoadImage() && !result.isLoadImg()) {
                        rushbuyGoodsImage.setImageResource(R.drawable.category_product_tapload_bg);
                        rushbuyGoodsImage.setOnLongClickListener(new MyOnLongClickListener(rushbuyGoodsImage, imageUrl,
                                result));
                    } else {
                        result.setLoadImg(true);
                        ImageUtils.with(HomeActivity.this).loadImage(imageUrl, rushbuyGoodsImage);
                        rushbuyGoodsImage.setOnLongClickListener(null);
                    }
                    changerLimit();
                    homeRushbuyCommend.setVisibility(View.VISIBLE);
                } else {
                    homeRushbuyCommend.setVisibility(View.GONE);
                }

            }
        }.execute();
    }

    /**
     * 无图模式长按图片加载图片监听
     * 
     * @author qinxudong
     */
    public class MyOnLongClickListener implements OnLongClickListener {
        ImageView imageView;
        String imageUrl;
        LimitBuyGoods result;

        public MyOnLongClickListener(ImageView imageView, String myimgUrl, LimitBuyGoods result) {
            this.imageView = imageView;
            this.imageUrl = myimgUrl;
            this.result = result;
        }

        @Override
        public boolean onLongClick(View v) {
            result.setLoadImg(true);
            ImageUtils.with(HomeActivity.this).loadImage(imageUrl, imageView);
            return false;
        }
    }

    /**
     * 异步加载首页焦点图
     * <p>
     * 首页焦点图加载逻辑为：先从数据库加载首页焦点图的历史记录-->再次请求服务器获取最新首页焦点图数据
     */
    private void performAsyncLoadRecommendHistory() {

        new AsyncTask<Object, Void, ArrayList<ActivityEntity>>() {

            protected ArrayList<ActivityEntity> doInBackground(Object... params) {

                return filterActivitys(new ActivityRecommendDao(HomeActivity.this).getAllActivityRecommends());
            };

            @Override
            protected void onPostExecute(ArrayList<ActivityEntity> result) {
                pagerAdapter = new HomeAdPageAdapter(HomeActivity.this, result);
                pageIndicator.setTotalPageSize(result.size());
                viewPager.setAdapter(pagerAdapter);
                pagerAdapter.setOnPageItemClickListener(new OnPageItemClickListener() {

                    @Override
                    public void onPageItemClick(ViewGroup parent, View item, int position) {
                        // 点击ViewPager的Page，跳转到活动推荐页面
                        HomeAdPageAdapter adapter = (HomeAdPageAdapter) viewPager.getAdapter();
                        ActivityEntity activityRecommend = adapter.getItem(position);

                        if (activityRecommend != null) {
                            String activityType = activityRecommend.getActivityType();
                            Intent intent = null;
                            int type = Integer.valueOf(activityType);

                            switch (type) {
                            case 0:
                                // 普通商品,团购列表数据
                                ArrayList<ActivityEntity> listAll = adapter.getList();
                                List<ActivityEntity> normalList = null;// 类型为普通的
                                int size = listAll.size();
                                if (listAll != null && listAll.size() > 0) {
                                    normalList = new ArrayList<ActivityEntity>();
                                    for (int i = 0; i < size; i++) {
                                        ActivityEntity singleActivityRecommend = listAll.get(i);
                                        if (singleActivityRecommend != null
                                                && ("0".equals(singleActivityRecommend.getActivityType()) || "10"
                                                        .equals(singleActivityRecommend.getActivityType()))) {
                                            normalList.add(singleActivityRecommend);
                                        }
                                    }
                                }
                                intent = new Intent(HomeActivity.this, HotPromTheTemActivity.class);
                                intent.putExtra("fromPage", HomeActivity.this.getString(R.string.appMeas_firstPage));
                                intent.putExtra("hotPromList", (Serializable) normalList);
                                break;
                            case 1:// 抢购商品

                                intent = new Intent(HomeActivity.this, NewLimitbuyActivity.class);

                                break;
                            case 2:// 跳到预售广告

                                intent = new Intent(HomeActivity.this, PreSellActivity.class);

                                break;
                            case 3:// 跳到预售广告

                                intent = new Intent(HomeActivity.this, PreSellActivity.class);

                                break;
                            case 4:// 热门促销

                                intent = new Intent(HomeActivity.this, HotPromotions2Activity.class);

                                break;
                            case 5:// 热门促销商品详情页ProductShowActivity

                                intent = new Intent(HomeActivity.this, ProductShowActivity.class);
                                intent.putExtra("skuId", activityRecommend.getRelatedID());
                                intent.putExtra("goodsNo", "");

                                break;
                            case 6:// 抢购商品

                                intent = new Intent(HomeActivity.this, NewLimitbuyActivity.class);
                                startActivity(intent);
                                return;
                            case 7:// 抢购商品详情页面LimitbuyDetailActivity

                                if (!TextUtils.isEmpty(activityRecommend.getRelatedID())) {
                                    LimitBuyGoods rushBuy = new LimitBuyGoods();
                                    rushBuy.setRushBuyItemId(activityRecommend.getRelatedID());
                                    intent = new Intent(HomeActivity.this, LimitbuyDetailActivity.class);
                                    intent.putExtra("rushBuy", rushBuy);
                                } else {
                                    intent = new Intent(HomeActivity.this, NewLimitbuyActivity.class);
                                }

                                break;
                            case 8:// 团购商品列表

                                intent = new Intent(HomeActivity.this, NewGroupBuyActivity.class);

                                break;
                            case 9:// 团购商品详情页面NewGroupBuyDetailActivity

                                if (!TextUtils.isEmpty(activityRecommend.getRelatedID())) {
                                    intent = new Intent(HomeActivity.this, NewGroupBuyDetailActivity.class);
                                    intent.putExtra(NewGroupBuyDetailActivity.SALEPROMOITEM,
                                            activityRecommend.getRelatedID());
                                } else {
                                    intent = new Intent(HomeActivity.this, NewGroupBuyActivity.class);
                                }

                                break;
                            case 10:// 团购列表页面，只能有首页、活动专区进入

                                // 普通商品,团购列表数据
                                listAll = adapter.getList();
                                normalList = null;// 类型为普通的
                                size = listAll.size();
                                if (listAll != null && listAll.size() > 0) {
                                    normalList = new ArrayList<ActivityEntity>();
                                    for (int i = 0; i < size; i++) {
                                        ActivityEntity singleActivityRecommend = listAll.get(i);
                                        if (singleActivityRecommend != null
                                                && ("0".equals(singleActivityRecommend.getActivityType()) || "10"
                                                        .equals(singleActivityRecommend.getActivityType()))) {
                                            normalList.add(singleActivityRecommend);
                                        }
                                    }
                                }
                                intent = new Intent(HomeActivity.this, HotPromTheTemActivity.class);
                                intent.putExtra("fromPage", HomeActivity.this.getString(R.string.appMeas_firstPage));
                                intent.putExtra("hotPromList", (Serializable) normalList);

                                break;
                            case 12:// 跳转至获取优惠券页面

                                intent = new Intent(HomeActivity.this, GetCouponActivity.class);

                                break;
                            }
                            intent.setAction("HomeActivity");
                            intent.putExtra(JsonInterface.JK_ACTIVITY_ID, activityRecommend.getActivityId());
                            intent.putExtra(JsonInterface.JK_ACTIVITY_TYPE, activityRecommend.getActivityType());
                            intent.putExtra(JsonInterface.JK_ACTIVITY_HTML_URL, activityRecommend.getActivityHtmlUrl());
                            intent.putExtra(JsonInterface.JK_ACTIVITY_NAME, activityRecommend.getActivityName());
                            startActivity(intent);
                        }
                    }

                });
                // 定义65535，设置起始位置为一个大数，在滑动时候以实现“无限滑动”
                if (result.size() != 0) {
                    int maxSize = 65535;
                    // 设置起始位置为一底部圆点索引的第一项
                    int pos = (maxSize / 2) - (maxSize / 2) % result.size(); // 计算初始位置
                    viewPager.setCurrentItem(pos, true);
                }
                performAsyncUpdateAds();
            }
        }.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppMeasurementUtils appMeasurementUtils = new AppMeasurementUtils(HomeActivity.this);
        appMeasurementUtils.getUrl(getString(R.string.appMeas_firstPage), getString(R.string.appMeas_firstPage),
                getString(R.string.appMeas_firstPage), getString(R.string.appMeas_firstPage), "", "", "", "", "", "",
                "", "", getString(R.string.appMeas_firstPage_tuijian), "", "", "", null);
        if (pagerAdapter != null) {
            pagerAdapter.notifyDataSetChanged();
        }
        if (timer == null) {
            timer = new Timer();
            timerTask = new AutoScrollTimerTask();
            timer.schedule(timerTask, 5000, 5000);
        }
        etInput.setText(null);
        if (etInput.getHint() == null || TextUtils.isEmpty(etInput.getHint().toString())) {
            etInput.setHint(getSearchHintContent());
        }
        // 关闭软键盘
        handler.sendEmptyMessageDelayed(0, 50);

        // 消息推送处理
        switch (st) {
        case 0:// 首页
               // 不作处理在gomeEmallActivity类已经发送过了查看请求
            break;
        case 3:// 限时抢购列表
            Intent intent = new Intent(this, NewLimitbuyActivity.class);
            intent.setAction("pushSertvice_no_sp");
            intent.putExtra("messageId", messageId);
            intent.putExtra("title", title);
            startActivity(intent);
            st = 0;
            title = "";
            messageId = "";
            break;
        case 4:// 热门促销列表
            Intent intent1 = new Intent(this, HotPromotions2Activity.class);
            intent1.setAction("pushSertvice_no_sp");
            intent1.putExtra("messageId", messageId);
            intent1.putExtra("title", title);
            startActivity(intent1);
            st = 0;
            title = "";
            messageId = "";
            break;
        case 5:// 团购列表
            Intent intent2 = new Intent(this, NewGroupBuyActivity.class);
            intent2.setAction("pushSertvice_no_sp");
            intent2.putExtra("messageId", messageId);
            intent2.putExtra("title", title);
            startActivity(intent2);
            st = 0;
            title = "";
            messageId = "";
            break;
        case 1:// 商城公告
            Intent intent3 = new Intent(this, AnnouncementDetailActivity.class);
            intent3.putExtra("comeFromPage", "pushSertvice");
            intent3.putExtra("newsId", newId);
            intent3.putExtra("messageId", messageId);
            intent3.putExtra("title", title);
            startActivity(intent3);
            st = 0;
            newId = "";
            title = "";
            messageId = "";
            break;
        case 6:// 商品详情
            Intent intent4 = new Intent(this, ProductShowActivity.class);
            intent4.putExtra("comeFromPage", "pushSertvice");
            intent4.putExtra("newsId", newId);
            intent4.putExtra("messageId", messageId);
            intent4.putExtra("title", title);
            startActivity(intent4);
            st = 0;
            newId = "";
            title = "";
            messageId = "";
            break;
        case 2:// 活动列表
            try {
                int at = Integer.parseInt(activityType);
                Intent intent5 = new Intent();
                intent5.putExtra("activityId", activityId);
                intent5.putExtra("title", title);
                intent5.putExtra("messageId", messageId);
                switch (at) {
                case 0:// 首页焦点图普通活动
                    intent5.setAction("pushSertvice");
                    intent5.putExtra("activityType", "0");
                    intent5.setClass(this, HotPromTheTemActivity.class);
                    startActivity(intent5);
                    break;
                case 1:// 首页焦点图抢购活动
                    intent5.setAction("pushSertvice");
                    intent5.putExtra("activityType", "1");
                    intent5.setClass(this, NewLimitbuyActivity.class);
                    startActivity(intent5);
                    break;
                case 2:// 首页焦点图团购活动
                    intent5.setAction("pushSertvice");
                    intent5.putExtra("activityType", "10");
                    intent5.setClass(this, HotPromTheTemActivity.class);
                    startActivity(intent5);
                    break;
                case 3:// 活动专区普通活动
                    intent5.setAction("push_PromtionActivitiesActivity");
                    intent5.putExtra("activityType", "0");
                    intent5.setClass(this, HotPromTheTemActivity.class);
                    startActivity(intent5);
                    break;
                case 4:// 活动专区抢购活动
                    intent5.setAction("push_PromtionActivitiesActivity");
                    intent5.putExtra("activityType", "1");
                    intent5.setClass(this, NewLimitbuyActivity.class);
                    startActivity(intent5);
                    break;

                default:
                    break;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            activityId = "";
            activityType = "";
            title = "";
            messageId = "";
            break;
        default:
            break;
        }
        Time t2 = new Time();
        t2.setToNow();
        boolean isorNo = t2.after(t);
        if (isorNo) {
            performAsyncUpdateAds();
            BDebug.d("liuyang", "首页24点以后刷新了");
            t.set(0, 0, 24, t2.monthDay, t2.month, t2.year);
        }
    }

    @Override
    protected void onPause() {
        CommonUtility.hideSoftKeyboard(this, etInput);
        super.onPause();
        if (timer != null) {
            timerTask.cancel();
            timer.cancel();
            timer = null;
            timerTask = null;
        }
    }

    /**
     * 执行异步更新活动广告的操作
     */
    private void performAsyncUpdateAds() {
        if (!NetUtility.isNetworkAvailable(HomeActivity.this)) {
            CommonUtility.showMiddleToast(HomeActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            handler.sendEmptyMessageDelayed(MSG_FINSIH_REFRESH, 1000);
            return;
        }

        if (asyncActivityRecommendTask != null) {
            return;
        }
        
        //热词搜索
        asyncActivityRecommendTask = new AsyncLoadActivityRecommendTask() {

            @Override
            protected void onPreExecute() {
                // btnRefresh.startAnimation(rotateAnimation);
            }

            @Override
            protected ArrayList<ActivityEntity> doInBackground(Object... params) {
                // 获取搜索推荐热词
                ArrayList<String> wordsList = getHotKeyWords();
                if (wordsList != null && wordsList.size() > 0) {
                    GlobalConfig.getInstance().setHotKeyWordsList(wordsList);
                }
                return super.doInBackground(params);
            }

            @Override
            protected void onPostExecute(ArrayList<ActivityEntity> result) {
                // refreshableView.mRefreshState = RefreshableView.RELEASE_TO_REFRESH;
                refreshableView.onRefreshComplete();
                // btnRefresh.clearAnimation();
                asyncActivityRecommendTask = null;
                mSearchHotKeyWordsList = GlobalConfig.getInstance().getHotKeyWordsList();
                // 如果获取推荐热词成功，则设置搜索框的提示内容
                etInput.setHint(getSearchHintContent());
                if (result == null) {
                    CommonUtility.showMiddleToast(HomeActivity.this, "", getString(R.string.data_load_fail_exception));
                    return;
                }
                if (result.size() == 0) {
                    return;
                }
                ActivityRecommendDao recommendDao = new ActivityRecommendDao(HomeActivity.this);
                // 移除所有的历史记录
                recommendDao.removeAllHistory();
                // 加入新的推荐记录
                result = filterActivitys(result);// 过滤掉不显示的
                for (ActivityEntity recommend : result) {
                    recommendDao.addActivityRecommend(recommend);
                }
                // 重新加载数据
                pagerAdapter.reload(result);
                // 设置底部圆点索引的个数
                pageIndicator.setTotalPageSize(pagerAdapter.getItemCount());

                // viewPager.setCurrentItem(0, true);
                // pageIndicator.setCurrentPage(0);
                // if (result.size() != 0) {
                // int maxSize = 65535; // int pos = (maxSize/2) - (maxSize/2)%result.size(); // 计算初始位置
                // // viewPager.setCurrentItem(pos); viewPager.setCurrentItem(pos, true);
                // pageIndicator.setCurrentPage(0);
                // }

            }
        };
        asyncActivityRecommendTask.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * 判断登录后启动新的activity
     * 
     * @param target
     */
    public void launchTargetActivity(Class<?> target) {
        if (!GlobalConfig.isLogin) {
            // application.connect = GlobalApplication.CONNECT_SUCCESS;
            if (application.connect != GlobalApplication.CONNECT_SUCCESS) {
                // CommonUtility.showMiddleToast(HomeActivity.this, null, getString(R.string.server_busy));
                if (canLogin) {
                    canLogin = false;
                    isAbleLogin();
                }
            }
            Intent intent = new Intent();
            intent.setClass(HomeActivity.this, LoginActivity.class);
            intent.putExtra(GlobalConfig.CLASS_NAME, target.getName());
            startActivityForResult(intent, 1);
        } else {
            Intent intent = new Intent();
            intent.setClass(HomeActivity.this, target);
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            String className = data.getStringExtra(GlobalConfig.CLASS_NAME);
            Intent targetIntent = new Intent();
            targetIntent.setClassName(HomeActivity.this, className);
            startActivity(targetIntent);
        } else if (resultCode == 2) {
            if (data != null) {
                if ("ProductShowActivity".equals(data.getAction())) {
                    Intent productShowIntent = new Intent(HomeActivity.this, ProductShowActivity.class);
                    String goodsNo = data.getStringExtra("goodsNo");
                    String skuId = data.getStringExtra("skuId");
                    productShowIntent.putExtra(ProductShowActivity.INTENT_KEY_GOODS_NO, goodsNo);
                    productShowIntent.putExtra(ProductShowActivity.INTENT_KEY_SKU_ID, skuId);
                    productShowIntent.putExtra("fromPage", getString(R.string.appMeas_firstPage));
                    startActivity(productShowIntent);
                } else if ("BarcodeScanReusltListActivity".equals(data.getAction())) {
                    Intent barcodeResultIntent = new Intent(HomeActivity.this, BarcodeScanReusltListActivity.class);
                    barcodeResultIntent.putExtra(BarcodeScanReusltListActivity.BARCODEGOODSRESULT,
                            data.getSerializableExtra(BarcodeScanReusltListActivity.BARCODEGOODSRESULT));
                    startActivity(barcodeResultIntent);
                }
            }

        } else if (resultCode == 5) {
            // 团抢订单提交成功后点击我的订单
            Intent intent = new Intent(this, MyOrderActivity.class);
            intent.putExtra("titleId", R.string.mygome_all_order);
            intent.putExtra(JsonInterface.JK_ORDER_STATUS, 0);
            intent.setAction(this.getClass().getName());
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();

        switch (viewId) {
        // 搜索分类
        case R.id.home_homepage_btn_search_category:
            // 搜索分类
            setCategary();
            break;
        // 输入框右边取消按钮
        case R.id.home_homepage_search_btn:
            // 输入框右边取消按钮
            startSearch();
            // CommonUtility.hideSoftKeyboard(this, etInput);
            // 点击取消后 设置 搜索框 提示文字 为热词 第一个
            seachIndex--;
            if (seachIndex < 0) {
                seachIndex = 0;
            }
            etInput.setHint(getSearchHintContent());
            break;
        case R.id.login_code_del_imageView:
            etInput.setText("");
            break;
        // 限时抢购点击
        case R.id.home_rushbuy_commend:
            Intent limitIntent = new Intent();
            limitIntent.putExtra(FORMFLAG, 1);
            limitIntent.putExtra(SERVERTIME, LimitBuyResult.HomESERVERTIME);
            limitIntent.putExtra(RUSH_BUY_ITEM_ID, limitGoods.getRushBuyItemId());
            limitIntent.setClass(HomeActivity.this, NewLimitbuyActivity.class);
            startActivity(limitIntent);
            break;
        // 去条码购
        case R.id.tv_home_bar_search:
            Intent barcodeIntent = new Intent(HomeActivity.this, BarcodeScanResultHistoryActivity.class);
            HomeActivity.this.startActivity(barcodeIntent);
            break;
        // 限时抢购
        case R.id.ll_home_limit_buy:
            Intent lIntent = new Intent(HomeActivity.this, NewLimitbuyActivity.class);
            HomeActivity.this.startActivity(lIntent);
            break;
        // 优惠团购
        case R.id.ll_home_group_buy:
            Intent groupBuyIntent = new Intent(HomeActivity.this, NewGroupBuyActivity.class);
            HomeActivity.this.startActivityForResult(groupBuyIntent, 4);
            break;
        // 热门促销
        case R.id.ll_home_hot_proms:
            Intent intent = new Intent();
            intent.setClass(HomeActivity.this, HotPromotions2Activity.class);
            HomeActivity.this.startActivity(intent);
            break;
        // 活动专区
        case R.id.ll_home_activitys:
            Intent promtionIntent = new Intent();
            promtionIntent.setClass(HomeActivity.this, PromtionActivitiesActivity.class);
            HomeActivity.this.startActivity(promtionIntent);
            break;
        // 手机充值
        case R.id.ll_home_phone_recharge:
            Intent rankingRecharge = new Intent(HomeActivity.this, PhoneRechargeActivity.class);
            HomeActivity.this.startActivity(rankingRecharge);
            break;
        // 快递查询
        case R.id.ll_home_order_query:
            HomeActivity.this.launchTargetActivity(MyOrderActivity.class);
            break;
        // 我的收藏
        case R.id.ll_home_my_fav:
            HomeActivity.this.launchTargetActivity(MyFavActivity.class);
            break;
        // 浏览历史
        case R.id.ll_home_history:
            Intent historyIntent = new Intent(HomeActivity.this, ProductBrowseHistoryActivity.class);
            HomeActivity.this.startActivity(historyIntent);
            break;
        // gome门店
        case R.id.ll_home_gome_shop:
            boolean isAllow = PreferenceUtils.isAllowLocation();
            if (isAllow) {
                if (!TextUtils.isEmpty(String.valueOf(GlobalConfig.getInstance().getLat()))
                        && !TextUtils.isEmpty(String.valueOf(GlobalConfig.getInstance().getLog()))) {
                    Intent intents = new Intent(HomeActivity.this, NearStoreListActivity.class);
                    intents.putExtra(NearStoreListActivity.ISALLOWUSELOACTION, true);
                    intents.setAction("MoreActivity");
                    HomeActivity.this.startActivity(intents);
                } else {
                    CommonUtility.showConfirmDialog(HomeActivity.this, "",
                            HomeActivity.this.getString(R.string.isopenloaction),
                            HomeActivity.this.getString(R.string.confirm), openleftListener,
                            HomeActivity.this.getString(R.string.cancel), openrightListener);
                }
            } else {
                CommonUtility.showConfirmDialog(HomeActivity.this, "",
                        HomeActivity.this.getString(R.string.is_use_you_now_loaction),
                        HomeActivity.this.getString(R.string.is_use_you_now_yes), leftListenerGomeShop,
                        HomeActivity.this.getString(R.string.is_use_you_now_no), rightListenerGomeShop);
            }
            break;
        // 商城公告
        case R.id.ll_home_announcement:
            Intent announceIntent = new Intent(HomeActivity.this, AnnouncementListActivity.class);
            HomeActivity.this.startActivity(announceIntent);
            break;
        // 设置关于
        case R.id.ll_home_settings_about:
            Intent moreSettingIntent = new Intent(HomeActivity.this, MoreActivity.class);
            HomeActivity.this.startActivity(moreSettingIntent);
            break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // 点击软键盘的搜索按钮，执行搜索操作
        if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            if (etInput.getText() == null || TextUtils.isEmpty(etInput.getText().toString().trim())) {
                CommonUtility.showToast(this, "请输入搜索条件！");
                return true;
            }
            searchResult(etInput.getText().toString().trim());
            return true;
        }
        return false;
    }

    private void searchResult(String input) {
        CommonUtility.hideSoftKeyboard(this, etInput);
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra(SearchResultActivity.INTENT_KEY_WORDS, input);
        intent.putExtra("fromPage", getString(R.string.appMeas_firstPage));
        if (currentSearchType > 0 && search_category_list != null && search_category_list.size() != 0) {
            intent.putExtra(SearchResultActivity.CURRENTFITERTYPEID, search_category_list.get(currentSearchType - 1)
                    .getTypeId());
        } else {
            intent.putExtra(SearchResultActivity.CURRENTFITERTYPEID, "");
        }
        startActivity(intent);
    }

    /**
     * 下拉刷新
     */
    public void onRefresh() {
        performAsyncUpdateAds();
    }

    private int currentPageState = ViewPager.SCROLL_STATE_IDLE;

    @Override
    public void onPageScrollStateChanged(int state) {
        currentPageState = state;
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int index) {
        pageIndicator.setCurrentPage(index % pagerAdapter.getItemCount());
    }

    /**
     * 获取首页焦点图更新
     * @author qinxudong
     *
     */
    private class AsyncLoadActivityRecommendTask extends AsyncTask<Object, Void, ArrayList<ActivityEntity>> {

        @Override
        protected ArrayList<ActivityEntity> doInBackground(Object... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String requestJson = ActivityEntity
                    .createRequestActivityRecommendListJson(currentPage, Constants.PAGE_SIZE);
            String response = NetUtility.sendHttpRequestByPost(Constants.URL_PRODUCT_HOMEPAGE_RECOMMENDATIONS,
                    requestJson);
            if (NetUtility.NO_CONN.equals(response)) {
                return null;
            }
            return ActivityEntity.parseActivityRecommendList(response);
        }

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            // 获取焦点后 清空 输入狂的 提示信息
            etInput.setHint(null);
            btnSearch.setVisibility(View.VISIBLE);
            barSearch.setVisibility(View.GONE);
            homeTopLogo.setVisibility(View.GONE);

            asyncLoadSearchHint();
        } else {
            btnSearch.setVisibility(View.GONE);
            deletImage.setVisibility(View.GONE);
            searchTipsLayout.setVisibility(View.GONE);
            barSearch.setVisibility(View.VISIBLE);
            homeTopLogo.setVisibility(View.VISIBLE);
            if (asyncTask != null) {
                asyncTask.cancel(true);
                asyncTask = null;
            }
        }
    }

    /**
     * 
     * @param list
     *            显示内容
     * @param hasHistory
     *            是否有内容
     * @param isHistory
     *            是否是历史记录/智能搜索记录
     */
    private TextView headView;// 头记录

    private void showSearchHint(ArrayList<String> list, boolean hasHistory, boolean isHistory) {

        if (hasHistory) {
            // 有历史记录时只显示历史记录，不显示热词
            keyWordsLayout.setVisibility(View.GONE);
            historySearchList.setVisibility(View.VISIBLE);
            if (historySearchList.getHeaderViewsCount() == 0) {
                TextView tvHistoryHint = (TextView) getLayoutInflater().inflate(
                        R.layout.home_search_tips_layout_list_header_view, null);
                headView = tvHistoryHint;
                historySearchList.addHeaderView(tvHistoryHint);
            }
            if (isHistory && headView != null) {
                headView.setText(R.string.intell_prom);
            } else if (!isHistory && headView != null) {
                headView.setText(R.string.near_search);
            }
            final SearchHistoryAdapter adapter = new SearchHistoryAdapter(list, HomeActivity.this, isHistory);
            historySearchList.setAdapter(adapter);
            if (!isHistory) {
                if (mSearchHistoryList != null && mSearchHistoryList.size() > 0
                        && historySearchList.getFooterViewsCount() == 0) {
                    historySearchList.addFooterView(clearAllHistory);
                }
            }
            historySearchList.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // headerView占据了一个position，所以要减一然后getItem()
                    int actualPosition = position - 1;
                    if (actualPosition >= 0 && actualPosition < adapter.getCount()) {
                        // etInput.setText(adapter.getItem(actualPosition));
                        if (!TextUtils.isEmpty(adapter.getItem(actualPosition))) {
                            searchResult(adapter.getItem(actualPosition));
                        }
                    }
                    if (actualPosition == adapter.getCount()) {
                        CommonUtility.showConfirmDialog(HomeActivity.this, "",
                                HomeActivity.this.getString(R.string.is_or_no_clear_all_history),
                                HomeActivity.this.getString(R.string.confirm), leftListener,
                                HomeActivity.this.getString(R.string.cancel), rightListener);
                    }
                }
            });
        } else {
            // 没有历史记录时显示热词
            if (list.size() > 0) {
                keyWordsLayout.setVisibility(View.VISIBLE);
                historySearchList.setVisibility(View.GONE);
                for (int i = 0, size = list.size(); i < size; i++) {
                    if (i < tvHotWords.length) {
                        tvHotWords[i].setText(list.get(i));
                        tvHotWords[i].setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView tvLabel = (TextView) v;
                                // etInput.setText(tvLabel.getText());
                                searchResult(tvLabel.getText().toString());
                            }
                        });
                    }// end if
                }// end for
            }
        }
        searchTipsLayout.setVisibility(View.VISIBLE);
    }

    private AsyncTask<Object, Void, ArrayList<String>> asyncTask = null;

    /**
     * 异步加载热词搜索
     */
    private void asyncLoadSearchHint() {
        asyncTask = new AsyncTask<Object, Void, ArrayList<String>>() {
            private boolean hasHistory = false;

            @Override
            protected ArrayList<String> doInBackground(Object... params) {
                SearchHistoryDao searchHistoryDao = new SearchHistoryDao(HomeActivity.this);
                mSearchHistoryList = searchHistoryDao.getSearchHistoryList(10);
                if (mSearchHistoryList.size() > 0) {
                    hasHistory = true;
                    return mSearchHistoryList;
                }
                mSearchHotKeyWordsList = GlobalConfig.getInstance().getHotKeyWordsList();
                if (mSearchHotKeyWordsList != null && mSearchHotKeyWordsList.size() > 0) {
                    return mSearchHotKeyWordsList;
                }
                mSearchHotKeyWordsList = getHotKeyWords();
                GlobalConfig.getInstance().setHotKeyWordsList(mSearchHotKeyWordsList);
                return mSearchHotKeyWordsList;
            }

            @Override
            protected void onCancelled() {
                asyncTask = null;
            }

            @Override
            protected void onPostExecute(ArrayList<String> result) {
                if (isCancelled()) {
                    return;
                }
                asyncTask = null;
                if (result == null) {
                    CommonUtility.showToast(HomeActivity.this, "获取推荐词失败!");
                    return;
                } else {
                    int size = result.size();
                    if (size > 0) {
                        if (hasHistory) { // 有历史记录，只取第一个推荐热词
                            showSearchHint(result, hasHistory, false);

                        } else { // 无历史记录
                            // if (!etInput.hasFocus()) {
                            // etInput.setHint(getSearchHintContent());
                            // }
                            ArrayList<String> newResult = new ArrayList<String>();
                            for (int i = 0; i < size; i++) {
                                newResult.add(result.get(i));
                            }
                            showSearchHint(newResult, hasHistory, false);
                        }
                    }
                }// end else
            }// end method
        };
        asyncTask.execute();
    }

    /**
     * 获取推荐热词
     * 
     * @return
     */
    private ArrayList<String> getHotKeyWords() {
        String request = KeyWordSearch.createRequestHotKeyWordsJson(1, 6);
        String response = NetUtility.sendHttpRequestByPost(Constants.URL_HOT_KEY_WORDS, request);
        if (NetUtility.NO_CONN.equals(response)) {
            return null;
        }
        return KeyWordSearch.parseHotKeyWords(response);
    }

    /**
     * 获取首页分类
     */
    private void setCategary() {
        final String resCache = PreferenceUtils.readSharePreferFile();
        if (!NetUtility.isNetworkAvailable(HomeActivity.this) && TextUtils.isEmpty(resCache)) {
            CommonUtility.showMiddleToast(HomeActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, ArrayList<Category>>() {
            private LoadingDialog loadingDialog;

            @Override
            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(HomeActivity.this, getString(R.string.loading), true,
                        new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            protected ArrayList<Category> doInBackground(Object... params) {
                String response = NetUtility.sendHttpRequestByGet(Constants.URL_PRODUCT_ALL_CATEGORIES);
                if (NetUtility.NO_CONN.equals(response)) {
                    if (TextUtils.isEmpty(resCache)) {
                        return null;
                    } else {
                        return Category.parseAllCategories(resCache);
                    }
                }
                PreferenceUtils.writeToSharePreferFile(response);
                return Category.parseAllCategories(response);
            };

            @Override
            protected void onPostExecute(ArrayList<Category> result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                if (result == null) {
                    CommonUtility.showMiddleToast(HomeActivity.this, "", getString(R.string.data_load_fail_exception));
                    return;
                }
                search_category_list = result;
                search_category = new String[search_category_list.size() + 1];
                search_category[0] = getString(R.string.all);
                for (int i = 0, categorySize = search_category_list.size(); i < categorySize; i++) {
                    Category category = search_category_list.get(i);
                    search_category[i + 1] = category.getTypeName();
                }
                if (search_category != null && search_category.length > 1) {
                    CommonUtility.showSingleChioceDialog(HomeActivity.this, getString(R.string.choice_search_category),
                            search_category, currentSearchType, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int checkIndex) {
                                    dialog.dismiss();
                                    currentSearchType = checkIndex;
                                    btnCategory.setText(search_category[currentSearchType]);
                                }
                            }, null, null, null, null);
                } else {
                    CommonUtility.showMiddleToast(HomeActivity.this, "", getString(R.string.no_result_category));
                }
            }
        }.execute();
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (historySearchList.getFooterViewsCount() > 0) {
            historySearchList.removeFooterView(clearAllHistory);
        }
        getKeywordInClude(s.toString());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (etInput.getText().length() > 0) {
            deletImage.setVisibility(View.VISIBLE);
        } else {
            deletImage.setVisibility(View.GONE);
        }
    }

    private AsyncTask<Object, Void, ArrayList<String>> includeasyncTask = null;

    /**
     * 搜索自动提示加载
     * @param keyword
     */
    private void getKeywordInClude(final String keyword) {
        if (!NetUtility.isNetworkAvailable(HomeActivity.this)) {
            CommonUtility.showMiddleToast(HomeActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        if (includeasyncTask != null) {
            includeasyncTask.cancel(true);
            includeasyncTask = null;
        }
        includeasyncTask = new AsyncTask<Object, Void, ArrayList<String>>() {
            protected ArrayList<String> doInBackground(Object... params) {
                String goodTypeId = "";
                if (currentSearchType > 0 && search_category_list != null && search_category_list.size() != 0) {
                    goodTypeId = search_category_list.get(currentSearchType - 1).getTypeId();
                }
                String request = HotWordSearch.createKeyWordInClude(keyword, goodTypeId);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_PRODUCT_KEYWORDS_PROMPT, request);
                if (NetUtility.NO_CONN.equals(response) || !NetUtility.isNetworkAvailable(HomeActivity.this)) {
                    return null;
                }
                return HotWordSearch.parseAllKeyWordList(response);
            };

            @Override
            protected void onCancelled() {
                includeasyncTask = null;
            }

            @Override
            protected void onPostExecute(ArrayList<String> result) {
                if (isCancelled()) {
                    return;
                }
                if (result != null && result.size() > 0) {
                    showSearchHint(result, true, true);
                } else {
                    if (historySearchList != null && historySearchList.isShown()) {
                        asyncLoadSearchHint();
                    }
                }
            }
        };
        includeasyncTask.execute();
    }

    /** 设置搜索框中的输入提示提示 */
    private String getSearchHintContent() {
        String s = "";
        if (mSearchHotKeyWordsList != null && mSearchHotKeyWordsList.size() != 0 && !etInput.hasFocus()
                && TextUtils.isEmpty(etInput.getText().toString())) {
            int size = mSearchHotKeyWordsList.size();
            s = mSearchHotKeyWordsList.get(seachIndex);
            GlobalConfig.getInstance().setSeachIndex(seachIndex);
            seachIndex++;
            if (seachIndex >= size) {
                seachIndex = 0;
            }
        }
        return s;
    }

    private android.content.DialogInterface.OnClickListener leftListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            SearchHistoryDao searchHistoryDao = new SearchHistoryDao(HomeActivity.this);
            searchHistoryDao.removeAllHistory();
            if (historySearchList.getFooterViewsCount() > 0) {
                historySearchList.removeFooterView(clearAllHistory);
            }
            if (mSearchHistoryList != null) {
                mSearchHistoryList = null;
            }
            asyncLoadSearchHint();
        }
    };
    private android.content.DialogInterface.OnClickListener rightListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    };

    // 搜索按钮
    private void startSearch() {
        if (etInput.getText() == null || TextUtils.isEmpty(etInput.getText().toString().trim())) {
            CommonUtility.showToast(this, "请输入搜索条件！");
            return;
        }
        searchResult(etInput.getText().toString().trim());
    }

    /**
     * 时间自减更新(抢购倒计时)
     */
    private static final int COUNT_DOWN_TIME = 100;
    // 设置自动滚动计时器................................................
    private Timer time = new Timer();
    private TimerTask timerTaskLimit = new TimerTask() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(COUNT_DOWN_TIME);
        }
    };
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
            case COUNT_DOWN_TIME:
                if (limitGoods != null) {
                    try {
                        limitGoods.setDelayTime(String.valueOf(Long.valueOf(limitGoods.getDelayTime()) - 1));
                        // 倒计时结束时，请求更新数据
                        if (Long.valueOf(limitGoods.getDelayTime()) == 1l) {
                            performLimitRecommend();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    changerLimit();
                }
                break;
            default:
                break;
            }

        }
    };

    private void changerLimit() {
        if (limitGoods != null) {
            long timeSecound = 0;

            try {
                timeSecound = Long.valueOf(limitGoods.getDelayTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
            String hourMinSecond = FileUtils.secToTime(timeSecound);
            String[] hmsStrs = hourMinSecond.split(":");
            if (hmsStrs != null && hmsStrs.length == 3) {
                hourTextView.setText(hmsStrs[0]);
                miniteTextView.setText(hmsStrs[1]);
                secondTextView.setText(hmsStrs[2]);
            }
        }
    }

    private void isAbleLogin() {
        new AsyncTask<Void, Void, AbleLoginEntity>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                application.connect = GlobalApplication.CONNECT_DEFAUL;
            }

            @Override
            protected AbleLoginEntity doInBackground(Void... params) {
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_GLOBAL_CONFIG, "");
                return AbleLoginEntity.parseJson(response);
            }

            @Override
            protected void onPostExecute(AbleLoginEntity result) {
                super.onPostExecute(result);
                canLogin = true;
                application.isAlwaysCaptcha = result.isAlwaysCaptcha();
                GlobalApplication.isSupportedHttps = result.isSupportedHttps();
                if (result.isConnectState()) {
                    application.connect = GlobalApplication.CONNECT_SUCCESS;
                } else {
                    application.connect = GlobalApplication.CONNECT_FAIL;
                }
            }

        }.execute();
    }

    @Override
    protected void onDestroy() {

        time.cancel();
        timerTaskLimit.cancel();
        time = null;
        timerTaskLimit = null;
        super.onDestroy();

    }

    private DialogInterface.OnClickListener leftListenerGomeShop = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            PreferenceUtils.setAllowLocation(true);
            if (!TextUtils.isEmpty(String.valueOf(GlobalConfig.getInstance().getLat()))
                    && !TextUtils.isEmpty(String.valueOf(GlobalConfig.getInstance().getLog()))) {
                Intent intent = new Intent(HomeActivity.this, NearStoreListActivity.class);
                intent.putExtra(NearStoreListActivity.ISALLOWUSELOACTION, true);
                intent.setAction("MoreActivity");
                HomeActivity.this.startActivity(intent);
            } else {
                CommonUtility.showConfirmDialog(HomeActivity.this, "",
                        HomeActivity.this.getString(R.string.isopenloaction),
                        HomeActivity.this.getString(R.string.confirm), openleftListener,
                        HomeActivity.this.getString(R.string.cancel), openrightListener);
            }
        }
    };
    private DialogInterface.OnClickListener rightListenerGomeShop = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            Intent intent = new Intent(HomeActivity.this, CityListActivity.class);
            intent.setAction("MoreActivity");
            HomeActivity.this.startActivity(intent);
        }
    };
    private DialogInterface.OnClickListener openleftListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            HomeActivity.this.startActivity(intent);
        }
    };
    private DialogInterface.OnClickListener openrightListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    };

    // 过滤首页广告图显示
    public ArrayList<ActivityEntity> filterActivitys(ArrayList<ActivityEntity> sourses) {
        ArrayList<ActivityEntity> filters = new ArrayList<ActivityEntity>();
        for (ActivityEntity activityEntity : sourses) {
            // if (!("0".equals(activityEntity.getActivityType()) || "1".equals(activityEntity.getActivityType())
            // || "2".equals(activityEntity.getActivityType()) || "3".equals(activityEntity.getActivityType()))) {
            // filters.add(activityEntity);
            // }
            if (!getFilterFlag(activityEntity.getActivityType())) {
                filters.add(activityEntity);
            }
        }
        if (filters.size() > 0) {
            sourses.removeAll(filters);
            BDebug.e(TAG, filters.size() + "****************" + sourses.size());
        }
        return sourses;
    }

    private boolean getFilterFlag(String type) {
        if (TextUtils.isEmpty(type))
            return false;

        for (int i = 0; i < types.length; i++) {
            if (type.equals(types[i])) {
                return true;
            }
        }
        return false;
    }

}
