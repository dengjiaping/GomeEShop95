package com.gome.ecmall.home.hotproms;

import java.util.ArrayList;
import java.util.Timer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gome.ecmall.bean.ActivityEntity;
import com.gome.ecmall.bean.GBProductNew.GroupBuyProduct;
import com.gome.ecmall.bean.Goods;
import com.gome.ecmall.bean.HotPromTheTem;
import com.gome.ecmall.bean.HotPromTheTem.HotPromTheTemBean;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.LimitBuyResult;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.custom.PullToRefreshListView;
import com.gome.ecmall.custom.PullToRefreshListView.OnFootRefreshListener;
import com.gome.ecmall.custom.PullToRefreshListView.OnRefreshListener;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.category.ProductShowActivity;
import com.gome.ecmall.home.groupbuy.NewGroupBuyActivity;
import com.gome.ecmall.home.groupbuy.NewGroupBuyDetailActivity;
import com.gome.ecmall.home.hotproms.HotPromTheTemAdapter.OnProductClickListener;
import com.gome.ecmall.push.Push;
import com.gome.ecmall.push.PushUtils;
import com.gome.ecmall.util.AppMeasurementUtils;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class HotPromTheTemActivity extends AbsSubActivity implements OnClickListener {

    private PullToRefreshListView listView;
    private HotPromTheTemAdapter hotPromTheTemAdapter;
    private Button btnBack;
    private TextView textTitle;
    private int currentPage;
    private String activityId, activityType, activityHtmlurl;
    private ArrayList<ActivityEntity> mList;
    private LayoutInflater inflater;
    private FrameLayout list_bgFrameLayout;
    private Animation moveTopanimation, moveBottemanimation;
    private ImageLoaderManager loaderManager;
    public static final String INTENT_KEY_GOODS_NO = "goodsNo";
    public static final String INTENT_KEY_SKU_ID = "skuId";
    private ColorDrawable transparentDrawable;
    private Handler myHandler;
    private String action;

    private ImageView rotateImage;
    private Button btnRule;
    private Animation animation;
    private ActivityEntity entity;
    private HotPromTheTemBean hotPromTheTemBean;

    private Time t;// 24点以后刷新用
    
    //private int sendCount;// 到达后发送到达数据的次数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotprom_thetem_list);
        animation = AnimationUtils.loadAnimation(this, R.anim.progress_bar_rotate);
        mList = (ArrayList<ActivityEntity>) getIntent().getSerializableExtra("hotPromList");
        action = getIntent().getAction();
        activityId = getIntent().getStringExtra(JsonInterface.JK_ACTIVITY_ID);
        activityType = getIntent().getStringExtra(JsonInterface.JK_ACTIVITY_TYPE);
        activityHtmlurl = getIntent().getStringExtra(JsonInterface.JK_ACTIVITY_HTML_URL);
        setSelectProm();
        moveTopanimation = AnimationUtils.loadAnimation(HotPromTheTemActivity.this, R.anim.move_top_anim);
        moveBottemanimation = AnimationUtils.loadAnimation(HotPromTheTemActivity.this, R.anim.move_bottem_anim);
        loaderManager = ImageLoaderManager.initImageLoaderManager(HotPromTheTemActivity.this);
        transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
        initView();
        setData(false);
        String messageId = getIntent().getStringExtra("messageId");
        String titles = getIntent().getStringExtra("title");
        if ("pushSertvice".equals(action)||"push_PromtionActivitiesActivity".equals(action)) {
            BDebug.d("push_arrive", "到达活动页面"+action);
            PushUtils.AsynFeedbackArrivedMessage(HotPromTheTemActivity.this,messageId,titles,"3");
        }
        // 24点以后刷新用
        t = new Time();
        t.setToNow();
        int year = t.year;
        int month = t.month;
        int monthday = t.monthDay;
        t.set(0, 0, 24, monthday, month, year);
    }
    private void initView() {
        inflater = LayoutInflater.from(this);
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setText(R.string.back);
        btnBack.setOnClickListener(this);

        btnRule = (Button) findViewById(R.id.common_title_btn_right);
        btnRule.setVisibility(View.INVISIBLE);
        btnRule.setText(R.string.hot_rule);
        btnRule.setOnClickListener(this);

        rotateImage = (ImageView) findViewById(R.id.rotate_image);

        textTitle = (TextView) findViewById(R.id.common_title_tv_text);
        textTitle.setVisibility(View.VISIBLE);
        textTitle.requestFocus();

        entity = null;
        if (mList != null && mList.size() > 0) {
            entity = mList.get(currentPage);
            if (entity != null)
                textTitle.setText(entity.getActivityName());
        }
        listView = (PullToRefreshListView) findViewById(R.id.hotprom_thetem_list);
        list_bgFrameLayout = (FrameLayout) findViewById(R.id.list_bg);

        // 八叉乐统计
        if ("HomeActivity".equals(action)) {
            AppMeasurementUtils appMeasurementProm = new AppMeasurementUtils(HotPromTheTemActivity.this);
            appMeasurementProm.getUrl(getString(R.string.appMeas_promDetail) + ":" + entity.getActivityName(),
                    getString(R.string.appMeas_promDetail),
                    getString(R.string.appMeas_promDetail) + ":" + entity.getActivityName(),
                    getString(R.string.appMeas_promDetailPage), "", "", "", "", "", "", "", "",
                    getString(R.string.appMeas_myprom), entity.getActivityName(), "", "", new String[] { null, "o",
                            AppMeasurementUtils.TRANK_LINK_PROM });
        } else if ("PromtionActivitiesActivity".equals(action)) {
            AppMeasurementUtils appMeasurementProm = new AppMeasurementUtils(HotPromTheTemActivity.this);
            appMeasurementProm.getUrl(getString(R.string.appMeas_projectactivitys) + ":" + entity.getActivityName(),
                    getString(R.string.appMeas_projectactivitys), getString(R.string.appMeas_projectactivitys) + ":"
                            + entity.getActivityName(), getString(R.string.appMeas_projectactivitys), "", "", "", "",
                    "", "", "", "", getString(R.string.appMeas_myprom), entity.getActivityName(), "", "", new String[] {
                            null, "o", AppMeasurementUtils.TRANK_LINK_PROM });
        }

    }

    private void setSelectProm() {
        if (mList != null && !TextUtils.isEmpty(activityId)) {
            for (int i = 0, size = mList.size(); i < size; i++) {
                ActivityEntity mEntity = mList.get(i);
                if (activityId.equals(mEntity.getActivityId())) {
                    currentPage = i;
                    break;
                }
            }
        }
    }

    private void setData(final boolean is24Judge) {
        if (!NetUtility.isNetworkAvailable(HotPromTheTemActivity.this)) {
            CommonUtility.showMiddleToast(HotPromTheTemActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        btnRule.setVisibility(View.INVISIBLE);
        new AsyncTask<Object, Void, HotPromTheTemBean>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(HotPromTheTemActivity.this,
                        getString(R.string.loading), true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected HotPromTheTemBean doInBackground(Object... params) {
                return getHotPromTheTemBean();
            }

            @Override
            protected void onPostExecute(HotPromTheTemBean result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                if (result == null) {
                    if (is24Judge) {
                        goback(); // 关闭当前页并返回上一级
                    } else {
                        CommonUtility.showMiddleToast(HotPromTheTemActivity.this, "",
                                getString(R.string.data_load_fail_exception));
                    }

                    return;
                }
                hotPromTheTemBean = result;
                loadGone();
                if (result.getGoodslist() == null || result.getGoodslist().size() == 0) {
                    return;
                }
                if (mList != null) {
                    if (currentPage == mList.size() - 1) {// 没有商品可取
                        listView.setUpdateFootText(getString(R.string.hot_prom_foot));
                    }
                }
                // 设置活动名称（消息推送过来时）
                if ("pushSertvice".equals(action)) {
                    if (hotPromTheTemBean.getActivityInfo() != null) {
                        if (!TextUtils.isEmpty(hotPromTheTemBean.getActivityInfo().getActivityName())) {
                            textTitle.setText(hotPromTheTemBean.getActivityInfo().getActivityName());
                        }
                    }
                }

                if (hotPromTheTemAdapter == null) {
                    hotPromTheTemAdapter = new HotPromTheTemAdapter(HotPromTheTemActivity.this, result);
                    hotPromTheTemAdapter.setClickListener(new OnProductClickListener() {
                        @Override
                        public void onProductClick(Goods goods) {

                            if (goods != null) {
                                // Intent intent1 = new Intent(HotPromTheTemActivity.this, GomeEMallActivity.class);
                                // startActivity(intent1);1
                                if (activityType.equals("10")) {// 跳转团购详情页面
                                    Intent intent = new Intent(HotPromTheTemActivity.this, NewGroupBuyDetailActivity.class) ;
                                    intent.putExtra(NewGroupBuyDetailActivity.SALEPROMOITEM, ((GroupBuyProduct) goods).getSalePromoItem());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(HotPromTheTemActivity.this, ProductShowActivity.class);
                                    intent.putExtra("fromPage", getString(R.string.appMeas_promDetailPage));
                                    intent.putExtra(INTENT_KEY_GOODS_NO, goods.getGoodsNo());
                                    intent.putExtra(INTENT_KEY_SKU_ID, goods.getSkuID());
                                    startActivity(intent);
                                }
                            }
                        }
                    });
                    listView.setAdapter(hotPromTheTemAdapter);
                    listView.setOnRefreshListener(new OnRefreshListener() {
                        @Override
                        public void onRefresh() {

                            if (currentPage == 0) {
                                listView.setUpdateText(getString(R.string.hot_prom_header));
                                // 修改滑动状态为“带滑动”，以实现继续下拉
                                listView.mRefreshState = listView.RELEASE_TO_REFRESH;
                                afterTimeSelectListView();
                            } else if (currentPage > 0) {
                                loadPreData();
                            }
                        }
                    });
                    listView.setOnFootRefreshListener(new OnFootRefreshListener() {
                        @Override
                        public void onRefresh() {

                            if (mList != null && currentPage == mList.size() - 1) {
                                listView.setUpdateFootText(getString(R.string.hot_prom_foot));
                            } else if (currentPage > -1) {
                                loadMoreData();
                            }
                        }
                    });
                } else {
                    hotPromTheTemAdapter.reload(result);
                }
                try {
                    list_bgFrameLayout.setBackgroundColor(Color.parseColor(result.getBgColor()));
                    listView.setBackgroundColor(Color.parseColor(result.getBgColor()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                asyncLoadImage(result.getBgImgUrl());
            }
        }.execute();
    }

    private HotPromTheTemBean getHotPromTheTemBean() {

        String request = null;
        String response = "";
        if (!TextUtils.isEmpty(activityType)) {
            if (activityType.equals("0")) {
                request = LimitBuyResult.createRequestLimitBuyPrmListJson(activityId, activityType, activityHtmlurl);
                if ("HomeActivity".equals(action) || "pushSertvice".equals(action)) {
                    response = NetUtility
                            .sendHttpRequestByPost(Constants.URL_PROMOTION_GENERAL_ACTIVITY_GOODS, request);
                } else if ("PromtionActivitiesActivity".equals(action)||"push_PromtionActivitiesActivity".equals(action)) {
                    response = NetUtility.sendHttpRequestByPost(Constants.URL_ACTIVITIES_GENERAL_ACTIVITY_GOODS,
                            request);
                }
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return HotPromTheTem.parseHotPromTheTemJson(response);
            } else if (activityType.equals("10")) {
                request = LimitBuyResult.createRequestLimitBuyPrmListJson(activityId, activityType, null);
                response = NetUtility.sendHttpRequestByPost(Constants.URL_GROUPBUY_GOODS_LIST, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return HotPromTheTem.parseGroupBuyListJson(response);
            }
        }
        return null;
    }

    private void setRequestData() {
        if (mList != null && currentPage < mList.size()) {
            ActivityEntity entity = mList.get(currentPage);
            if (entity != null) {
                activityId = entity.getActivityId();
                activityType = entity.getActivityType();
                activityHtmlurl = entity.getActivityHtmlUrl();
            }
        }
    }

    /**
     * 返回首页
     */
    private void comeBackHome() {
        // if ("pushSertvice".equals(action)) {
        // Intent intent = new Intent(HotPromTheTemActivity.this, GomeEMallActivity.class);
        // startActivity(intent);
        // finish();
        // }else{
        goback();
        // }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ("pushSertvice".equals(action)) {
            switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                comeBackHome();

                break;

            default:
                break;
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.common_title_btn_back:
            comeBackHome();

            break;
        case R.id.common_title_btn_right:
            Intent intent = new Intent(this, HotPromotionsRuleActivity.class);
            intent.putExtra("title", hotPromTheTemBean.getActivityInfo().getActivityName());
            intent.putExtra("content", hotPromTheTemBean.getActivityInfo().getActivityRule());
            startActivity(intent);
            break;
        }
    }

    /**
     * 加载上一条
     */
    private AsyncTask<Object, Void, HotPromTheTemBean> preasyncTask = null;

    private void loadPreData() {
        if (!NetUtility.isNetworkAvailable(HotPromTheTemActivity.this)) {
            CommonUtility.showMiddleToast(HotPromTheTemActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        if (preasyncTask != null) {
            return;
        }
        btnRule.setVisibility(View.INVISIBLE);
        if (nextasyncTask != null) {
            nextasyncTask.cancel(true);
            listView.onFootRefreshComplete();
        }
        preasyncTask = new AsyncTask<Object, Void, HotPromTheTemBean>() {

            @Override
            protected HotPromTheTemBean doInBackground(Object... params) {
                currentPage--;
                setRequestData();
                return getHotPromTheTemBean();
            }

            @Override
            protected void onCancelled() {
                preasyncTask = null;
            }

            @Override
            protected void onPostExecute(HotPromTheTemBean result) {
                if (isCancelled()) {
                    return;
                }

                if (result == null) {
                    currentPage++;
                    CommonUtility.showMiddleToast(HotPromTheTemActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    listView.onRefreshComplete();
                    listView.onFootRefreshComplete();
                    return;
                }
                hotPromTheTemBean = result;
                loadGone();
                if (result.getGoodslist() == null || result.getGoodslist().size() == 0) {
                    listView.onRefreshComplete();
                    listView.onFootRefreshComplete();
                    return;
                }
                try {
                    list_bgFrameLayout.setBackgroundColor(Color.parseColor(result.getBgColor()));
                    listView.setBackgroundColor(Color.parseColor(result.getBgColor()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                asyncLoadImage(result.getBgImgUrl());
                hotPromTheTemAdapter.reload(result);
                listView.onRefreshComplete();
                listView.onFootRefreshComplete();
                listView.clearAnimation();
                listView.startAnimation(moveTopanimation);
                if (mList != null) {
                    entity = mList.get(currentPage);
                    if (entity != null)
                        textTitle.setText(entity.getActivityName());
                    // 八叉乐统计
                    if ("HomeActivity".equals(action)) {
                        AppMeasurementUtils appMeasurementProm = new AppMeasurementUtils(HotPromTheTemActivity.this);
                        appMeasurementProm.getUrl(
                                getString(R.string.appMeas_promDetail) + ":" + entity.getActivityName(),
                                getString(R.string.appMeas_promDetail), getString(R.string.appMeas_promDetail) + ":"
                                        + entity.getActivityName(), getString(R.string.appMeas_promDetailPage), "", "",
                                "", "", "", "", "", "", getString(R.string.appMeas_myprom), entity.getActivityName(),
                                "", "", new String[] { null, "o", AppMeasurementUtils.TRANK_LINK_PROM });
                    } else if ("PromtionActivitiesActivity".equals(action)) {
                        AppMeasurementUtils appMeasurementProm = new AppMeasurementUtils(HotPromTheTemActivity.this);
                        appMeasurementProm.getUrl(
                                getString(R.string.appMeas_projectactivitys) + ":" + entity.getActivityName(),
                                getString(R.string.appMeas_projectactivitys),
                                getString(R.string.appMeas_projectactivitys) + ":" + entity.getActivityName(),
                                getString(R.string.appMeas_projectactivitys), "", "", "", "", "", "", "", "",
                                getString(R.string.appMeas_myprom), entity.getActivityName(), "", "", new String[] {
                                        null, "o", AppMeasurementUtils.TRANK_LINK_PROM });
                    }
                }
                preasyncTask = null;
            }
        };
        preasyncTask.execute();
    }

    // 加载下一条
    private AsyncTask<Object, Void, HotPromTheTemBean> nextasyncTask = null;

    private void loadMoreData() {
        if (!NetUtility.isNetworkAvailable(HotPromTheTemActivity.this)) {
            CommonUtility.showMiddleToast(HotPromTheTemActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        if (nextasyncTask != null) {
            return;
        }
        btnRule.setVisibility(View.INVISIBLE);
        if (preasyncTask != null) {
            preasyncTask.cancel(true);
            listView.onRefreshComplete();
        }
        nextasyncTask = new AsyncTask<Object, Void, HotPromTheTemBean>() {

            @Override
            protected HotPromTheTemBean doInBackground(Object... params) {
                currentPage++;
                setRequestData();
                return getHotPromTheTemBean();
            }

            @Override
            protected void onCancelled() {
                nextasyncTask = null;
            }

            @Override
            protected void onPostExecute(HotPromTheTemBean result) {
                if (isCancelled()) {
                    return;
                }
                if (result == null) {
                    currentPage--;
                    CommonUtility.showMiddleToast(HotPromTheTemActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    listView.onRefreshComplete();
                    listView.onFootRefreshComplete();
                    return;
                }
                hotPromTheTemBean = result;
                loadGone();
                if (result.getGoodslist() == null || result.getGoodslist().size() == 0) {
                    listView.onRefreshComplete();
                    listView.onFootRefreshComplete();
                    return;
                }
                try {
                    list_bgFrameLayout.setBackgroundColor(Color.parseColor(result.getBgColor()));
                    listView.setBackgroundColor(Color.parseColor(result.getBgColor()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                asyncLoadImage(result.getBgImgUrl());
                hotPromTheTemAdapter.reload(result);
                listView.onRefreshComplete();
                listView.onFootRefreshComplete();
                listView.clearAnimation();
                listView.startAnimation(moveBottemanimation);
                if (mList != null) {
                    entity = mList.get(currentPage);
                    if (entity != null)
                        textTitle.setText(entity.getActivityName());
                    // 八叉乐统计
                    if ("HomeActivity".equals(action)) {
                        AppMeasurementUtils appMeasurementProm = new AppMeasurementUtils(HotPromTheTemActivity.this);
                        appMeasurementProm.getUrl(
                                getString(R.string.appMeas_promDetail) + ":" + entity.getActivityName(),
                                getString(R.string.appMeas_promDetail), getString(R.string.appMeas_promDetail) + ":"
                                        + entity.getActivityName(), getString(R.string.appMeas_promDetailPage), "", "",
                                "", "", "", "", "", "", getString(R.string.appMeas_myprom), entity.getActivityName(),
                                "", "", new String[] { null, "o", AppMeasurementUtils.TRANK_LINK_PROM });
                    } else if ("PromtionActivitiesActivity".equals(action)) {
                        AppMeasurementUtils appMeasurementProm = new AppMeasurementUtils(HotPromTheTemActivity.this);
                        appMeasurementProm.getUrl(
                                getString(R.string.appMeas_projectactivitys) + ":" + entity.getActivityName(),
                                getString(R.string.appMeas_projectactivitys),
                                getString(R.string.appMeas_projectactivitys) + ":" + entity.getActivityName(),
                                getString(R.string.appMeas_projectactivitys), "", "", "", "", "", "", "", "",
                                getString(R.string.appMeas_myprom), entity.getActivityName(), "", "", new String[] {
                                        null, "o", AppMeasurementUtils.TRANK_LINK_PROM });
                    }
                }
                nextasyncTask = null;
            }
        };
        nextasyncTask.execute();
    }

    private void asyncLoadImage(String imgUrl) {
        // 不需要加载图片时将imageview的bitmap置空
        if (TextUtils.isEmpty(imgUrl))
            return;
        Bitmap bitmap = loaderManager.getCacheBitmap(imgUrl);
        BitmapDrawable destDrawable = new BitmapDrawable(inflater.getContext().getResources(), bitmap);
        destDrawable.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
        destDrawable.setDither(true);
        list_bgFrameLayout.setBackgroundDrawable(destDrawable);
        listView.setBackgroundDrawable(destDrawable);
        if (bitmap == null) {
            loaderManager.asyncLoad(new ImageLoadTask(imgUrl) {

                private static final long serialVersionUID = -5068460719652209430L;

                @Override
                protected Bitmap doInBackground() {
                    return NetUtility.downloadNetworkBitmap(filePath);
                }

                @Override
                public void onImageLoaded(ImageLoadTask task, Bitmap bitmap) {
                    if (bitmap != null) {
                        BitmapDrawable destDrawable = new BitmapDrawable(inflater.getContext().getResources(), bitmap);
                        TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[] {
                                transparentDrawable, destDrawable });
                        destDrawable.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
                        destDrawable.setDither(true);
                        if (list_bgFrameLayout != null) {
                            list_bgFrameLayout.setBackgroundDrawable(transitionDrawable);
                        }
                        if (listView != null) {
                            listView.setBackgroundDrawable(transitionDrawable);
                        }
                        transitionDrawable.startTransition(300);
                    }
                }

            });
        }
    }

    /**
     * 启动Timer执行
     */
    public void afterTimeSelectListView() {
        Timer timer = new Timer();
        initHandler(timer);
        // 启动timer任务
        timer.schedule(new MyTask(), 2000);
    }

    private void initHandler(final Timer timer) {
        if (myHandler == null) {
            myHandler = new Handler() {
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

            myHandler.sendEmptyMessage(0);
        }

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            rotateImage.clearAnimation();
            rotateImage.setVisibility(View.INVISIBLE);
            int index = msg.what;
            switch (index) {
            case 1:
                btnRule.setText(R.string.hot_prom_rule);
                btnRule.setVisibility(View.VISIBLE);
                break;
            case 2:
                btnRule.setText(R.string.hot_rule);
                btnRule.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
            }
        }

    };

    /**
     * 活动有活动规则，则显示，否则隐藏活动规则按钮 隐藏加载progressbar，显示活动规则按钮
     */
    void loadGone() {
        rotateImage.setVisibility(View.VISIBLE);
        rotateImage.setAnimation(animation);
        btnRule.setVisibility(View.INVISIBLE);
        boolean flag = hotPromTheTemBean != null && hotPromTheTemBean.getActivityInfo() != null
                && hotPromTheTemBean.getActivityInfo().getActivityRule() != null
                && hotPromTheTemBean.getActivityInfo().getActivityRule().length() > 0;
        if (flag) {
            handler.sendEmptyMessageDelayed(1, 2000);
        } else
            handler.sendEmptyMessageDelayed(2, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Time t2 = new Time();
        t2.setToNow();
        boolean isorNo = t2.after(t);
        if (isorNo) {
            setData(true);
            BDebug.d("liuyang", "我也执行了");
            t.set(0, 0, 24, t2.monthDay, t2.month, t2.year);
        }
    }

}
