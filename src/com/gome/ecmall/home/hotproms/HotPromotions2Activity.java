package com.gome.ecmall.home.hotproms;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gome.ecmall.bean.HotPromGoods;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.category.ProductShowActivity;
import com.gome.ecmall.home.hotproms.HotPromotionsListAdapter.OnProductClickListener;
import com.gome.ecmall.home.limitbuy.NewLimitbuyActivity;
import com.gome.ecmall.push.PushUtils;
import com.gome.ecmall.util.AppMeasurementUtils;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 
 * 热门促销新版本
 * 
 * @author qinxudong
 * 
 */
public class HotPromotions2Activity extends AbsSubActivity implements OnClickListener {

    private static final String TAG = "HotPromotions2Activity";

    /** 当前页 */
    private int mCurrPage = 1;

    private LoadingDialog dialog;

    /** true:为小图模式；false:大图模式 */
    private boolean selector = true;

    private LoadTask loadTask;

    private ImageView back_image;

    public ImageView selector_image;

    // 小图listview
    private ListView mListView;

    // 大图listview
    private ListView bigStyleListView;

    // 小图listview适配器
    private HotPromotionsListAdapter listAdapter;

    // 大图listview适配器
    private HotPromotionsListBigStyleAdapter bigStyleAdapter;

    private LinearLayout empty_layout;

    private TextView empty_text;

    private ArrayList<HotPromGoods> goodsList;

    /** 0：可以加载数据；1：正在加载数据 */
    private int loadMoreIndex = 0;

    private View mFootView;

    private boolean hasMore = false;

    private int pageSize;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hot_promotions_main2);
        initView();
        pageSize = Constants.PAGE_SIZE;
        action = getIntent().getAction();
        String messageId = getIntent().getStringExtra("messageId");
        String titles = getIntent().getStringExtra("title");
        if ("pushSertvice_no_sp".equals(action)) {
            BDebug.d("push_arrive", "到达热门促销页" + action);
            PushUtils.AsynFeedbackArrivedMessage(HotPromotions2Activity.this, messageId, titles, "3");
        }
        getData();
    }

    private void initView() {

        back_image = (ImageView) findViewById(R.id.back_iamge);
        back_image.setOnClickListener(this);

        selector_image = (ImageView) findViewById(R.id.image_title_right);
        selector_image.setOnClickListener(this);

        empty_text = (TextView) findViewById(R.id.empty);

        empty_layout = (LinearLayout) findViewById(R.id.empty_image);

        mListView = (ListView) findViewById(R.id.hot_promotion_list);

        mListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((firstVisibleItem + visibleItemCount) == totalItemCount && hasMore) {
                    if (hasMore && listAdapter != null && listAdapter.getCount() > 0) {
                        hasMore = false;
                        loadMore(HotPromotions2Activity.this);
                    }
                }
            }
        });

        bigStyleListView = (ListView) findViewById(R.id.hot_promotion_big_style_list);

        bigStyleListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((firstVisibleItem + visibleItemCount) == totalItemCount && hasMore) {
                    if (hasMore && bigStyleAdapter != null && bigStyleAdapter.getCount() > 0) {
                        hasMore = false;
                        loadMore(HotPromotions2Activity.this);
                    }
                }
            }
        });

        bigStyleAdapter = new HotPromotionsListBigStyleAdapter(this, goodsList);
        bigStyleListView.setAdapter(bigStyleAdapter);

        mFootView = LayoutInflater.from(this).inflate(R.layout.common_loading_layout, null);
        mFootView.setClickable(false);
        mFootView.setFocusable(false);
        mFootView.setFocusableInTouchMode(false);

        mListView.addFooterView(mFootView);
        bigStyleListView.addFooterView(mFootView);

    }

    /**
     * 获取数据
     */
    private void getData() {
        mCurrPage = 1;
        goodsList = new ArrayList<HotPromGoods>();
        loadTask = new LoadTask(HotPromotions2Activity.this, mCurrPage, pageSize);
        loadTask.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppMeasurementUtils appMeasurementProm = new AppMeasurementUtils(HotPromotions2Activity.this);
        appMeasurementProm.getUrl(getString(R.string.appMeas_actiCenter) + ":" + getString(R.string.appMeas_hotProm),
                getString(R.string.appMeas_actiCenter), getString(R.string.appMeas_actiCenter) + ":"
                        + getString(R.string.appMeas_hotProm), getString(R.string.appMeas_hotProm), "", "", "", "", "",
                "", "", "", getString(R.string.appMeas_myprom), "", "", "", null);
        // 记录listView滚动状态

        if (listAdapter != null) {
            Parcelable state1 = mListView.onSaveInstanceState();
            mListView.setAdapter(listAdapter);
            mListView.onRestoreInstanceState(state1);
        }

        if (bigStyleAdapter != null) {
            Parcelable state2 = bigStyleListView.onSaveInstanceState();
            bigStyleListView.setAdapter(bigStyleAdapter);
            bigStyleListView.onRestoreInstanceState(state2);
        }

    }

    /**
     * @param result
     *            视图数据绑定
     */
    void bindData(ArrayList<HotPromGoods> result) {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (result == null) {
            if (goodsList.size() == 0) {
                empty_text.setText(R.string.empty);
                empty_text.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
                CommonUtility.showMiddleToast(this, null, getString(R.string.data_load_fail_exception));
                return;
            }
            ((TextView) mFootView.findViewById(R.id.common_loading_title)).setText(R.string.data_load_fail_exception);
            ((ProgressBar) mFootView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);

            return;
        }

        BDebug.e(TAG, result.size() + "*****");
        if (result.size() == Constants.PAGE_SIZE) {
            mCurrPage++;
            hasMore = true;
            if (selector) {
                mListView.setVisibility(View.VISIBLE);
                if (mListView != null && mListView.getFooterViewsCount() == 0) {
                    mListView.addFooterView(mFootView);
                }
            } else {
                bigStyleListView.setVisibility(View.VISIBLE);
                if (bigStyleListView != null && bigStyleListView.getFooterViewsCount() == 0) {
                    bigStyleListView.addFooterView(mFootView);
                }
            }
            ((TextView) mFootView.findViewById(R.id.common_loading_title)).setText(R.string.load_more);
            ((ProgressBar) mFootView.findViewById(R.id.loadingbar)).setVisibility(View.VISIBLE);

        } else {
            hasMore = false;
            if (mListView != null && mListView.getFooterViewsCount() > 0) {
                HeaderViewListAdapter adapter = (HeaderViewListAdapter) mListView.getAdapter();
                adapter.removeFooter(mFootView);
            }
            if (bigStyleListView != null && bigStyleListView.getFooterViewsCount() > 0) {
                HeaderViewListAdapter adapter = (HeaderViewListAdapter) bigStyleListView.getAdapter();
                adapter.removeFooter(mFootView);
            }
        }

        for (HotPromGoods goods : result) {
            goodsList.add(goods);
        }

        if (selector) {// list列表,小图模式
            if (listAdapter != null) {
                listAdapter.addItem(result);
            } else {
                listAdapter = new HotPromotionsListAdapter(this, goodsList);
                mListView.setAdapter(listAdapter);
                if (listAdapter.getClickListener() == null) {

                    listAdapter.setClickListener(new OnProductClickListener() {

                        @Override
                        public void onProductClick(HotPromGoods goods) {
                            if (goods != null) {
                                Intent intent = new Intent(HotPromotions2Activity.this, ProductShowActivity.class);
                                intent.putExtra("fromPage", getString(R.string.appMeas_hotProm));
                                intent.putExtra(JsonInterface.JK_GOODS_NO, goods.getGoodsNo());
                                intent.putExtra(JsonInterface.JK_SKU_ID, goods.getSkuID());
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        } else {// 大图模式
            if (bigStyleAdapter != null) {
                bigStyleAdapter.addItem(result);
            } else {
                bigStyleAdapter = new HotPromotionsListBigStyleAdapter(this, goodsList);
                bigStyleListView.setAdapter(bigStyleAdapter);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mListView = null;
        listAdapter = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    private class LoadTask extends AsyncTask<Void, Void, ArrayList<HotPromGoods>> {

        private Activity activity;
        private int currPage;
        private int pageSize;

        public LoadTask(Activity act, int currPage, int pageSize) {

            this.currPage = currPage;
            this.activity = act;
            this.pageSize = pageSize;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!NetUtility.isNetworkAvailable(activity)) {
                cancel(true);
                CommonUtility.showMiddleToast(activity, null,
                        activity.getString(R.string.data_load_fail_please_check_network_settings));
                empty_layout.setVisibility(View.VISIBLE);

                return;
            }

            dialog = CommonUtility.showLoadingDialog(HotPromotions2Activity.this, getString(R.string.loading), true,
                    new OnCancelListener() {

                        @Override
                        public void onCancel(DialogInterface dialog) {
                            loadTask.cancel(true);
                        }
                    });

        }

        @Override
        protected ArrayList<HotPromGoods> doInBackground(Void... params) {
            String request = HotPromotionService.createJson(currPage, pageSize);
            String response = NetUtility.sendHttpRequestByPost(Constants.URL_NEW_HOT_PROMOTION, request);

            if (response == null || NetUtility.NO_CONN.equals(response)) {
                return null;
            }
            return HotPromotionService.parseHotPromGoodsList(response);
        }

        @Override
        protected void onPostExecute(ArrayList<HotPromGoods> result) {
            super.onPostExecute(result);
            bindData(result);
            loadTask = null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            cancel(true);
        }

    }

    /**
     * 加载更多
     * 
     * @param ctx
     */
    public void loadMore(Context ctx) {

        if (!NetUtility.isNetworkAvailable(ctx)) {
            CommonUtility.showMiddleToast(ctx, null,
                    ctx.getString(R.string.data_load_fail_please_check_network_settings));
            hasMore = false;
            ((TextView) mFootView.findViewById(R.id.common_loading_title))
                    .setText(R.string.data_load_fail_please_check_network_settings);
            ((ProgressBar) mFootView.findViewById(R.id.loadingbar)).setVisibility(View.GONE);
            return;
        }
        new AsyncTask<Void, Void, ArrayList<HotPromGoods>>() {

            @Override
            protected ArrayList<HotPromGoods> doInBackground(Void... params) {
                loadMoreIndex = 1;
                String request = HotPromotionService.createJson(mCurrPage, Constants.PAGE_SIZE);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_NEW_HOT_PROMOTION, request);

                if (response == null || NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return HotPromotionService.parseHotPromGoodsList(response);
            }

            @Override
            protected void onPostExecute(ArrayList<HotPromGoods> result) {
                loadMoreIndex = 0;
                BDebug.e(TAG, "loadMoreIndex***" + loadMoreIndex);
                bindData(result);
            }
        }.execute();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
        case R.id.back_iamge:
            goback();
            break;
        case R.id.image_title_right:
            imageSelector();
            break;
        }

    }

    /**
     * 大小图切换
     */
    private void imageSelector() {

        if (selector) {// 大图模式展示
            selector_image.setImageResource(R.drawable.bg_hot_prom_pager);
            selector = false;
            if (goodsList.size() == 0)
                return;

            bigStyleAdapter.reload(goodsList);

        } else {// 小图模式
            selector_image.setImageResource(R.drawable.bg_hot_prom_list);
            selector = true;
            if (goodsList.size() == 0)
                return;
            if (listAdapter == null) {
                listAdapter = new HotPromotionsListAdapter(this, goodsList);
                mListView.setAdapter(listAdapter);
                if (listAdapter.getClickListener() == null) {

                    listAdapter.setClickListener(new OnProductClickListener() {

                        @Override
                        public void onProductClick(HotPromGoods goods) {
                            if (goods != null) {
                                Intent intent = new Intent(HotPromotions2Activity.this, ProductShowActivity.class);
                                intent.putExtra("fromPage", getString(R.string.appMeas_hotProm));
                                intent.putExtra(JsonInterface.JK_GOODS_NO, goods.getGoodsNo());
                                intent.putExtra(JsonInterface.JK_SKU_ID, goods.getSkuID());
                                startActivity(intent);
                            }
                        }
                    });
                }
            } else {
                listAdapter.reload(goodsList);
            }

        }
        translateAnimation(selector);
    }

    private void translateAnimation(boolean flag) {

        if (flag) {
            Animation fromBottomOut = AnimationUtils.loadAnimation(this, R.anim.hot_prom_bottom_out);
            fromBottomOut.setDuration(400);
            bigStyleListView.startAnimation(fromBottomOut);
            fromBottomOut.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    mListView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    bigStyleListView.setVisibility(View.GONE);
                }
            });

        } else {

            Animation fromBottomIn = AnimationUtils.loadAnimation(this, R.anim.hot_prom_bottom_in);
            fromBottomIn.setDuration(400);
            bigStyleListView.startAnimation(fromBottomIn);
            fromBottomIn.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    bigStyleListView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mListView.setVisibility(View.GONE);
                }
            });
        }
    }

    public ArrayList<HotPromGoods> getGoodsList() {
        return goodsList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) {
            pageSize = goodsList.size();
            goodsList.clear();
            if (listAdapter != null)
                listAdapter.clearAllData();
            getData();
        }
    }

}