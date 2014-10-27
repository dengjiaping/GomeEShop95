package com.gome.ecmall.home.limitbuy;

import java.util.ArrayList;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.GBProduct;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.LimitBuyResult;
import com.gome.ecmall.bean.LimitBuyResult.LimitBuyGoods;
import com.gome.ecmall.bean.ShoppingCart;
import com.gome.ecmall.bean.ShoppingCart.Goods;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_ConsInfo_address;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_Recently;
import com.gome.ecmall.bean.ShoppingCart.ShoppingGo;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.category.ProductSummaryActivity;
import com.gome.ecmall.home.groupbuy.GroupLimitOrderActivity;
import com.gome.ecmall.home.login.ActivateAccountActivity;
import com.gome.ecmall.home.login.LoginActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.FileUtils;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class LimitbuyDetailActivity extends Activity implements OnClickListener {

    private TextView limitbuy_surtime_state, limitbuy_surtime_hour_data, limitbuy_surtime_min_data,
            limitbuy_surtime_second_data;
    private TextView limitbuy_surtime_hour_data_unit, limitbuy_surtime_min_data_unit,
            limitbuy_surtime_second_data_unit;
    private TextView limitbuy_total_num;
    private TextView limitbuy_detail, limitbuy_parameter, limitCommentNum;
    private TextView limitbuy_esoterica, limitbuy_address;
    private TextView limitbuy_name, limitbuy_limit_price_int, limitPriceUnittext, limitbuy_limit_price_float,
            limitbuy_gome_price;
    private ProgressBar progessBar;
    private ImageView limitbuyImg;
    private Button backBtn, refreshBtn, limitBtn;
    private RelativeLayout limit_image_parent, limitbuy_appraise_rl;
    private LinearLayout limitbuy_detail_time_ll;
    private RatingBar limitRatingBar;
    // 抢购ID
    private LimitBuyGoods rushBuy = new LimitBuyGoods();
    private ImageLoaderManager imageLoaderManager;
    private LimitCountDownTread startThread;
    private RotateAnimation rotateAnimation;
    private StringBuffer sbString = new StringBuffer();

    private ImageView limit_time_now_clock;
    private long lastTime;
    private long nowTime;
    private Time t;//24小时刷新用
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.limitbuy_detail);
        imageLoaderManager = ImageLoaderManager.initImageLoaderManager(this);
        rushBuy = (LimitBuyGoods) getIntent().getSerializableExtra("rushBuy");
        initTitleButton();
        setData(false);
        t = new Time();
        t.setToNow();
        int year = t.year;
        int month = t.month;
        int monthday = t.monthDay;
        t.set(0, 0, 24, monthday, month, year);
    }

    // 初始化控件
    private void initTitleButton() {
        rotateAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation.setDuration(500);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(-1);
        backBtn = (Button) findViewById(R.id.common_title_btn_back);
        refreshBtn = (Button) findViewById(R.id.common_title_btn_right);
        limitBtn = (Button) findViewById(R.id.limitbuy_limitbuy_btn);
        backBtn.setOnClickListener(this);
        refreshBtn.setOnClickListener(this);
        limitbuyImg = (ImageView) findViewById(R.id.limitbuy_detail_img);
        progessBar = (ProgressBar) findViewById(R.id.limitbuy_state_probar);
        limitbuy_surtime_state = (TextView) findViewById(R.id.limitbuy_surtime_state);
        limitbuy_surtime_hour_data = (TextView) findViewById(R.id.limitbuy_surtime_hour_data);
        limitbuy_surtime_min_data = (TextView) findViewById(R.id.limitbuy_surtime_min_data);
        limitbuy_surtime_second_data = (TextView) findViewById(R.id.limitbuy_surtime_second_data);
        limitbuy_surtime_hour_data_unit = (TextView) findViewById(R.id.limitbuy_surtime_hour_data_unit);
        limitbuy_surtime_min_data_unit = (TextView) findViewById(R.id.limitbuy_surtime_min_data_unit);
        limitbuy_surtime_second_data_unit = (TextView) findViewById(R.id.limitbuy_surtime_second_data_unit);
        limitbuy_total_num = (TextView) findViewById(R.id.limitbuy_total_num);
        // limitbuy_remain_num = (TextView) findViewById(R.id.limitbuy_remain_num);
        limitbuy_esoterica = (TextView) findViewById(R.id.limitbuy_esoterica);
        limitbuy_address = (TextView) findViewById(R.id.limitbuy_address);
        limitbuy_esoterica.setOnClickListener(this);
        limitbuy_address.setOnClickListener(this);
        limitbuy_name = (TextView) findViewById(R.id.limitbuy_name);
        limitbuy_limit_price_int = (TextView) findViewById(R.id.limit_buy_limit_price_tv);
        limitbuy_limit_price_float = (TextView) findViewById(R.id.limit_buy_limit_price_float);
        limitbuy_gome_price = (TextView) findViewById(R.id.limit_buy_gome_price_tv);
        limit_image_parent = (RelativeLayout) findViewById(R.id.limit_image_parent);
        limitbuy_detail = (TextView) findViewById(R.id.product_show_goods_detail);
        limitbuy_parameter = (TextView) findViewById(R.id.product_show_goods_parameter);
        limitbuy_appraise_rl = (RelativeLayout) findViewById(R.id.product_show_goods_appraise_rl);
        limitbuy_detail_time_ll = (LinearLayout) findViewById(R.id.limitbuy_detail_time_ll);
        limit_time_now_clock = (ImageView) findViewById(R.id.limit_time_now_clock);
        limitPriceUnittext = (TextView) findViewById(R.id.limit_buy_limit_price_unit);
        limitCommentNum = (TextView) findViewById(R.id.product_show_goods_comment_num);
        limitRatingBar = (RatingBar) findViewById(R.id.category_product_appraise_list_item_rate);
    }

    private void setData(final boolean is24judge) {
        new AsyncTask<Object, Void, LimitBuyGoods>() {
            @Override
            protected void onPreExecute() {
                refreshBtn.startAnimation(rotateAnimation);
                lastTime = System.currentTimeMillis();
            }

            @Override
            protected LimitBuyGoods doInBackground(Object... params) {
                String request = LimitBuyResult.createRequestLimitBuyJson(rushBuy.getRushBuyItemId());
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_RUSHBUY_GOODS, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return LimitBuyResult.parseLimitBuy(response);
            }

            @Override
            protected void onPostExecute(LimitBuyGoods result) {
                nowTime = System.currentTimeMillis();
                if (nowTime - lastTime < 1000) {
                    rotateAnimation.setRepeatCount(1);
                } else {
                    refreshBtn.clearAnimation();
                }
                if (isCancelled()) {
                    return;
                }
                if (result == null) {
                    if(is24judge){
                       finish(); //关闭当前页并返回上一级
                    }
                    return;
                }
                rushBuy = result;
                rushBuy.setRemainNum(result.getRemainNum());
                rushBuy.setDelayTime(result.getDelayTime());
                rushBuy.setRushBuyState(result.getRushBuyState());
                rushBuy.setAppraiseCount(result.getAppraiseCount());
                rushBuy.setAppraiseGrade(result.getAppraiseGrade());
                initView(result);
            }
        }.execute();

    }

    protected void initView(LimitBuyGoods limitbuy) {
        limitBtn.setOnClickListener(this);
        limitbuy_detail.setOnClickListener(this);
        limitbuy_parameter.setOnClickListener(this);
        limitbuy_appraise_rl.setOnClickListener(this);
        if (!TextUtils.isEmpty(limitbuy.getSkuName())) {
            limitbuy_name.setText(Html.fromHtml(limitbuy.getSkuName()));
        }
        if (TextUtils.isEmpty(limitbuy.getRemainNum())) {
            limitbuy.setRemainNum("0");
        }
        if (TextUtils.isEmpty(limitbuy.getLimitNum())) {
            limitbuy.setLimitNum("0");
        }
        try {
            int progress = (int) (Integer.valueOf(limitbuy.getRemainNum()) * 100 / Integer.valueOf(limitbuy
                    .getLimitNum()));
            progessBar.setProgress(progress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        limitbuy_total_num.setText(getString(R.string.limitbuy_remainnum) + rushBuy.getRemainNum()
                + getString(R.string.limitbuy_item_unit));
        if (!TextUtils.isEmpty(limitbuy.getRushBuyState())) {
            switch (Integer.valueOf(limitbuy.getRushBuyState())) {
            case 0: {
                limitBtn.setEnabled(false);
                limitbuy_surtime_state.setText(R.string.limitbuy_disstart);
                limitBtn.setBackgroundResource(R.drawable.limitbuy_limitbuy_not_start_btn);
                limit_time_now_clock.setBackgroundResource(R.drawable.limit_time_now_bg);
                limitbuy_detail_time_ll.setVisibility(View.VISIBLE);

            }
                break;
            case 1: {
                if (!CommonUtility.isOrNoZero(limitbuy.getSkuRushBuyPrice(), false)) {
                    limitBtn.setEnabled(true);
                } else {
                    limitBtn.setEnabled(false);
                }
                limitBtn.setBackgroundResource(R.drawable.limitbuy_limitbuy_bt_selector);
                limitbuy_surtime_state.setText(R.string.limitbuy_disend);
                limit_time_now_clock.setBackgroundResource(R.drawable.limit_time_now_bg);
                limitbuy_detail_time_ll.setVisibility(View.VISIBLE);

            }
                break;
            case 2: {
                limitBtn.setEnabled(false);
                limitbuy_surtime_state.setText(R.string.limitbuy_always_steal_warn);
                limitBtn.setBackgroundResource(R.drawable.limitbuy_limitbuy_null_btn);
                limit_time_now_clock.setBackgroundResource(R.drawable.limit_time_null_bg);
                limitbuy_detail_time_ll.setVisibility(View.GONE);
            }
                break;
            case 3: {
                limitBtn.setEnabled(false);
                limitbuy_surtime_state.setText(R.string.limitbuy_has_end_warn);
                limitBtn.setText(R.string.limitbuy_has_end);
                limitBtn.setBackgroundResource(R.drawable.common_orange_btn_disable);
                limit_time_now_clock.setBackgroundResource(R.drawable.limit_time_null_bg);
                limitbuy_detail_time_ll.setVisibility(View.GONE);
            }
                break;
            }
        } else {
            limitbuy.setRushBuyState("0");
        }
        if (!TextUtils.isEmpty(limitbuy.getDelayTime())) {
            String hourMinSecond = FileUtils.limitSecToTime(Long.valueOf(limitbuy.getDelayTime()));
            String[] hmsStrs = hourMinSecond.split(":");
            if (hmsStrs != null && hmsStrs.length == 3) {
                limitbuy_surtime_hour_data.setText(hmsStrs[0].substring(0, 1));
                limitbuy_surtime_hour_data_unit.setText(hmsStrs[0].substring(1, 2));
                limitbuy_surtime_min_data.setText(hmsStrs[1].substring(0, 1));
                limitbuy_surtime_min_data_unit.setText(hmsStrs[1].substring(1, 2));
                limitbuy_surtime_second_data.setText(hmsStrs[2].substring(0, 1));
                limitbuy_surtime_second_data_unit.setText(hmsStrs[2].substring(1, 2));
            }
            if (!"3".equals(limitbuy.getRushBuyState()) && startThread == null) {
                startThread = new LimitCountDownTread(new MyHandler(limitbuy, new TextView[] {
                        limitbuy_surtime_hour_data, limitbuy_surtime_min_data, limitbuy_surtime_second_data,
                        limitbuy_surtime_hour_data_unit, limitbuy_surtime_min_data_unit,
                        limitbuy_surtime_second_data_unit }, Integer.valueOf(limitbuy.getRushBuyState())), false);
                startThread.setHourMinSecond(hourMinSecond);
                startThread.start();
            }
        }
        if (!CommonUtility.isOrNoZero(limitbuy.getSkuRushBuyPrice(), false)) {
            limitPriceUnittext.setText("￥");
            limitbuy_limit_price_int.setVisibility(View.VISIBLE);
            limitbuy_limit_price_float.setVisibility(View.VISIBLE);
            limitbuy_limit_price_int.setText(limitbuy.getSkuRushBuyPrice().split("\\.")[0].toString());
            limitbuy_limit_price_float.setText("." + limitbuy.getSkuRushBuyPrice().split("\\.")[1].toString());
        } else {
            limitPriceUnittext.setText(getString(R.string.now_not_have_price));
            limitbuy_limit_price_int.setVisibility(View.GONE);
            limitbuy_limit_price_float.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(limitbuy.getAppraiseCount()) && !"0".equals(limitbuy.getAppraiseCount())) {
            limitCommentNum.setText(limitbuy.getAppraiseCount());
            limitCommentNum.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(limitbuy.getAppraiseGrade()) && !"0".equals(limitbuy.getAppraiseGrade())) {
            limitRatingBar.setRating(Float.valueOf(limitbuy.getAppraiseGrade()));
            limitRatingBar.setVisibility(View.VISIBLE);
        } else {
            limitRatingBar.setVisibility(View.INVISIBLE);
            limitCommentNum.setVisibility(View.INVISIBLE);
        }
        limitbuy_gome_price.setText("￥" + limitbuy.getSkuOriginalPrice());
        limitbuy_gome_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        /*
         * limitbuy_total_num.setText(limitbuy.getLimitNum()); limitbuy_remain_num.setText(limitbuy.getRemainNum());
         */
        if (!GlobalConfig.getInstance().isNeedLoadImage() && !limitbuy.isLoadImg()) {
            limitbuyImg.setImageResource(R.drawable.category_product_tapload_bg);
            limitbuyImg.setOnLongClickListener(new MyOnLongClickListener(limitbuy, limitbuyImg, limit_image_parent));
        } else {
            asyncLoadThumbImage(limitbuy, limitbuyImg, limit_image_parent);
        }

    }

    private class MyOnLongClickListener implements OnLongClickListener {

        LimitBuyGoods limitbuy;
        ImageView imageView;
        ViewGroup parent;

        public MyOnLongClickListener(LimitBuyGoods limitbuy, ImageView imageView, ViewGroup parent) {
            this.limitbuy = limitbuy;
            this.imageView = imageView;
            this.parent = parent;
        }

        @Override
        public boolean onLongClick(View v) {
            asyncLoadThumbImage(limitbuy, imageView, parent);
            return false;
        }

    }

    public class MyHandler extends Handler {
        private TextView myTimeText[];
        private LimitBuyGoods limitbuy;
        private int type;

        public MyHandler(LimitBuyGoods limitbuy, TextView timeText[], int type) {
            this.myTimeText = timeText;
            this.limitbuy = limitbuy;
            this.type = type;
        }

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
            case 0: {
                String[] times = (String[]) msg.obj;
                if (myTimeText != null && myTimeText.length == 6 && times != null && times.length == 3) {
                    myTimeText[0].setText(times[0].substring(0, 1));
                    myTimeText[1].setText(times[1].substring(0, 1));
                    myTimeText[2].setText(times[2].substring(0, 1));
                    myTimeText[3].setText(times[0].substring(1, 2));
                    myTimeText[4].setText(times[1].substring(1, 2));
                    myTimeText[5].setText(times[2].substring(1, 2));
                    if ("00".equals(times[0]) && "00".equals(times[1]) && "00".equals(times[2])) {
                        switch (type) {
                        case 0: {
                            if (!CommonUtility.isOrNoZero(limitbuy.getSkuRushBuyPrice(), false)) {
                                limitBtn.setEnabled(true);
                            } else {
                                limitBtn.setEnabled(false);
                            }
                            limitbuy_surtime_state.setText(R.string.limitbuy_disend);
                            limitbuy.setDelayTime(limitbuy.getDelayEndTime());
                            limitbuy.setRushBuyState("1");
                            limitBtn.setBackgroundResource(R.drawable.limitbuy_limitbuy_bt_selector);
                            startThread(myTimeText[0], myTimeText[1], myTimeText[2], myTimeText[3], myTimeText[4],
                                    myTimeText[5], limitbuy, type);
                        }
                            break;
                        case 1: {
                            limitBtn.setEnabled(false);
                            limitBtn.setText(R.string.limitbuy_has_end);
                            limitbuy.setRushBuyState("3");
                            limitBtn.setBackgroundResource(R.drawable.common_orange_btn_disable);
                        }
                            break;
                        case 2: {
                            limitBtn.setEnabled(false);
                            limitBtn.setText(R.string.limitbuy_has_end);
                            limitbuy.setRushBuyState("3");
                            limitBtn.setBackgroundResource(R.drawable.common_orange_btn_disable);
                        }
                            break;
                        }
                    }
                    limitbuy.setDelayTime(Integer.toString(Integer.valueOf(times[0]) * 3600 + Integer.valueOf(times[1])
                            * 60 + Integer.valueOf(times[2])));
                }
            }
                break;
            }
        }
    };

    private void startThread(TextView hourtext, TextView mintext, TextView secondtext, TextView hourUnitText,
            TextView minUnitText, TextView secondUnitText, LimitBuyGoods limitbuy, int type) {
        if (!TextUtils.isEmpty(limitbuy.getDelayTime())) {
            String hourMinSecond = FileUtils.limitSecToTime(Long.valueOf(limitbuy.getDelayTime()));
            String[] hmsStrs = hourMinSecond.split(":");
            if (hmsStrs != null && hmsStrs.length == 3) {
                hourtext.setText(hmsStrs[0].substring(0, 1));
                hourUnitText.setText(hmsStrs[0].substring(1, 2));
                mintext.setText(hmsStrs[1].substring(0, 1));
                minUnitText.setText(hmsStrs[1].substring(1, 2));
                secondtext.setText(hmsStrs[2].substring(0, 1));
                secondUnitText.setText(hmsStrs[2].substring(1, 2));
            }
            if (Long.valueOf(limitbuy.getDelayTime()) > 0) {
                LimitCountDownTread startThread = new LimitCountDownTread(new MyHandler(limitbuy, new TextView[] {
                        hourtext, mintext, secondtext, hourUnitText, minUnitText, secondUnitText }, type), false);
                startThread.setHourMinSecond(hourMinSecond);
                startThread.start();
            }
        }
    }

    private void asyncLoadThumbImage(LimitBuyGoods limitbuy, ImageView imageView, final ViewGroup parent) {
        if (TextUtils.isEmpty(limitbuy.getSkuThumbImgUrl()))
            return;
        limitbuy.setLoadImg(true);
        Bitmap bitmap = imageLoaderManager.getCacheBitmap(limitbuy.getSkuThumbImgUrl());
        imageView.setImageBitmap(bitmap);
        if (bitmap == null) {
            imageView.setTag(limitbuy.getSkuThumbImgUrl());
            // imageView.setImageDrawable(new BitmapDrawable(getResources(), bitmap));
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.product_list_grid_item_icon_bg));
            imageLoaderManager.asyncLoad(new ImageLoadTask(limitbuy.getSkuThumbImgUrl()) {
                private static final long serialVersionUID = -5068460719652209430L;

                @Override
                protected Bitmap doInBackground() {
                    return NetUtility.downloadNetworkBitmap(filePath);
                }

                @Override
                public void onImageLoaded(ImageLoadTask task, Bitmap bitmap) {
                    if (bitmap != null) {
                        View tagedView = parent.findViewWithTag(task.filePath);
                        if (tagedView != null) {
                            ((ImageView) tagedView).setImageBitmap(bitmap);
                        }
                    }
                }

            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v == backBtn) {
            // 返回
            finish();
        } else if (v == limitbuy_appraise_rl) {
            // 进入商品评价界面
            Intent intent = new Intent(this, LimitBuyAppraiseListActivity.class);
            intent.putExtra(LimitBuyAppraiseListActivity.INTENT_KEY_GOODS_NO, rushBuy.getGoodsNo());
            startActivity(intent);
        } else if (v == limitbuy_detail) {
            // 点击商品名称，进入商品详情界面
            Intent intent = new Intent(this, LimitBuySummaryActivity.class);
            intent.putExtra(ProductSummaryActivity.INTENT_KEY_GOODS_NO, rushBuy.getGoodsNo());
            intent.putExtra(ProductSummaryActivity.INTENT_KEY_GOODS_TYPE, "");
            startActivity(intent);
        } else if (v == limitbuy_parameter) {
            // 点击商品名称，进入商品参数界面
            Intent intent = new Intent(this, LimitBuySummaryActivity.class);
            intent.putExtra(ProductSummaryActivity.INTENT_KEY_GOODS_NO, rushBuy.getGoodsNo());
            intent.putExtra(ProductSummaryActivity.INTENT_KEY_GOODS_TYPE, "wvSpecification");
            startActivity(intent);
        } else if (v == limitBtn) {
            if (GlobalConfig.isLogin) {
                if (rushBuy != null)
                    isGoShoppingOrder();
            } else {
                Intent intent = new Intent();
                intent.setClass(this, LoginActivity.class);
                intent.setAction(this.getClass().getName());
                startActivity(intent);
            }
        } else if (v == refreshBtn) {
            setData(false);
        } else if (v == limitbuy_esoterica) {
            Intent intent = new Intent();
            intent.setClass(this, LimitbuyEsotericaActivity.class);
            startActivity(intent);
        } else if (v == limitbuy_address) {
            if (GlobalConfig.isLogin) {
                if (rushBuy != null)
                    goToConsInfoData();
            } else {
                Intent intent = new Intent();
                intent.setClass(this, LoginActivity.class);
                intent.setAction(this.getClass().getName());
                startActivity(intent);
            }
        }
    }

    /**
     * 获取收货人信息
     */
    private void goToConsInfoData() {
        if (!NetUtility.isNetworkAvailable(this)) {
            CommonUtility.showMiddleToast(this, "",
                    this.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, ShoppingCart_ConsInfo_address>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(LimitbuyDetailActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected ShoppingCart_ConsInfo_address doInBackground(Object... params) {
                String result = NetUtility.NO_CONN;
                result = NetUtility.sendHttpRequestByGet(Constants.URL_RUSHBUY_CHECKOUT_ADDRESS_LIST);
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
                    return null;
                }
                return ShoppingCart.paserResponseShoppingCart_Address(result);
            };

            protected void onPostExecute(ShoppingCart_ConsInfo_address shoppingCart_consInfo_address) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }

                if (shoppingCart_consInfo_address == null) {
                    CommonUtility.showMiddleToast(LimitbuyDetailActivity.this, "", ShoppingCart.getErrorMessage());
                    return;
                }
                if (shoppingCart_consInfo_address.getAddressList() == null
                        || shoppingCart_consInfo_address.getAddressList().size() == 0) {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), LimitbuyConsInfoAddActivity.class);
                    startActivityForResult(intent, 0);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("shoppingCart_consInfo_address", shoppingCart_consInfo_address);
                    intent.setClass(getApplicationContext(), LimitbuyConsInfoActivity.class);
                    startActivityForResult(intent, 0);
                }
            };
        }.execute();
    }

    private void isGoShoppingOrder() {
        if (!NetUtility.isNetworkAvailable(this)) {
            CommonUtility.showMiddleToast(this, "",
                    this.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, ShoppingGo>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(LimitbuyDetailActivity.this,
                        LimitbuyDetailActivity.this.getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected ShoppingGo doInBackground(Object... params) {
                // BDebug.d(TAG, json);
                String request = GBProduct.createRequestLimitBuyCheckJson(rushBuy.getSkuID(), rushBuy.getGoodsNo(),
                        rushBuy.getRushBuyItemId());
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_RUSHBUY_RUSHBUYCHECK, request);
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(LimitbuyDetailActivity.this
                            .getString(R.string.data_load_fail_exception));
                    return null;
                }
                return ShoppingCart.go_ShoppingOrder(result);
            };

            protected void onPostExecute(ShoppingGo shoppingGo) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                if (shoppingGo == null)
                    return;
                if (shoppingGo.isSuccess()) {
                    Intent grouplimitIntent = new Intent();
                    //未设置快速抢购
                    if (!shoppingGo.isFinishedFlashBuyConfig()) {

                        grouplimitIntent.putExtra(GroupLimitOrderActivity.GroupLimitOrderActivitySKUID,
                                rushBuy.getSkuID());
                        grouplimitIntent.putExtra(GroupLimitOrderActivity.GroupLimitOrderActivityGoodsNo,
                                rushBuy.getGoodsNo());
                        grouplimitIntent.putExtra(GroupLimitOrderActivity.GroupLimitOrderActivitySkuNo,
                                rushBuy.getSkuNo());
                        grouplimitIntent.putExtra(GroupLimitOrderActivity.GroupLimitOrderActivityRushbuyitemId,
                                rushBuy.getRushBuyItemId());
                        GlobalConfig.getInstance().setGroupLimitType(GroupLimitOrderActivity.LimitType);
                        grouplimitIntent.setClass(LimitbuyDetailActivity.this.getApplicationContext(),
                                GroupLimitOrderActivity.class);
                        LimitbuyDetailActivity.this.startActivityForResult(grouplimitIntent, 0);
                    } else {
                        //走快速抢购逻辑
                        gotoFlashPurchase();
                    }

                    return;
                } else if (!shoppingGo.isSuccess() && !shoppingGo.isActivated()) {
                    // 跳转激活界面
                    Intent intent = new Intent();
                   // intent.putExtra(ShoppingGo.SHOPPING_GO, shoppingGo);
                    intent.putExtra(JsonInterface.JK_MOBILE, shoppingGo.getMobile());
                    intent.setClass(LimitbuyDetailActivity.this, ActivateAccountActivity.class);
                    LimitbuyDetailActivity.this.startActivityForResult(intent, 0);
                } else if (!shoppingGo.isSuccess() && shoppingGo.isSessionExpired()) {
                    // 服务器返回登录超时
                    Intent intent = new Intent();
                    intent.setClass(LimitbuyDetailActivity.this.getApplicationContext(), LoginActivity.class);
                    intent.setAction(this.getClass().getName());
                    LimitbuyDetailActivity.this.startActivity(intent);
                } else if (!shoppingGo.isSuccess()) {
                    CommonUtility.showMiddleToast(LimitbuyDetailActivity.this, "", ShoppingCart.getErrorMessage());
                    setData(false);
                    return;
                }
            };

        }.execute();

    }

    protected void gotoFlashPurchase() {
        if (!NetUtility.isNetworkAvailable(this)) {
            CommonUtility.showMiddleToast(this, "",
                    this.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, ShoppingCart_Recently>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(LimitbuyDetailActivity.this,
                        LimitbuyDetailActivity.this.getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected ShoppingCart_Recently doInBackground(Object... params) {
                // BDebug.d(TAG, json);
                String request = ShoppingCart.createRequestLimitOrderListJson(rushBuy.getSkuID(), rushBuy.getGoodsNo(),
                        rushBuy.getRushBuyItemId(), "");

                String result = NetUtility.sendHttpRequestByPost(
                        Constants.URL_RUSHBUY_CART_RUSHBUY_FLASHBUYCHECKOUTDETAIL, request);
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(LimitbuyDetailActivity.this
                            .getString(R.string.data_load_fail_exception));
                    return null;
                }
                return ShoppingCart.paserResponseGroupLimitShoppingCart_Recently(result);
            };

            protected void onPostExecute(ShoppingCart_Recently shoppingCart_recently) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                if (shoppingCart_recently == null) {
                    CommonUtility.showMiddleToast(LimitbuyDetailActivity.this, "", ShoppingCart.getErrorMessage());
                    return;
                }
                sbString.append(";");
                sbString.append(rushBuy.getSkuID());
                sbString.append(";");
                sbString.append("1");
                sbString.append(";");
                sbString.append(rushBuy.getSkuRushBuyPrice());
                sbString.append(",");
                Goods goods = new Goods();
                goods.setSkuID(rushBuy.getSkuID());
                goods.setSkuName(rushBuy.getSkuName());
                goods.setGoodsNo(rushBuy.getGoodsNo());
                goods.setSkuNo(rushBuy.getSkuNo());
                goods.setCommerceItemID(rushBuy.getRushBuyItemId());
                goods.setSkuRushBuyPrice(rushBuy.getSkuRushBuyPrice());
                if ("Y".equals(shoppingCart_recently.getHasAllowance())) {
                    Intent intent = new Intent();
                    intent.putExtra(ShoppingCart.JK_SHOPPINGCART_ALLOWANCEINFO, shoppingCart_recently.getWanceInfo());
                    intent.putExtra("limitgoods", goods);
                    intent.putExtra("orderMark", "");
                    if (!TextUtils.isEmpty(sbString))
                        intent.putExtra("shoppingCartOctree", sbString.toString());
                    intent.setClass(getApplicationContext(), LimitFlashWanceInfoActivity.class);
                    startActivity(intent);
                } else {

                    Intent intent = new Intent();
                    intent.putExtra("limitgoods", goods);
                    intent.putExtra("orderMark", "");
                    if (!TextUtils.isEmpty(sbString))
                        intent.putExtra("shoppingCartOctree", sbString.toString());
                    intent.setClass(getApplicationContext(), LimitFlashConfirmActivity.class);
                    startActivity(intent);
                }
            };

        }.execute();

    }
    @Override
    protected void onResume() {
        super.onResume();
        Time t2 = new Time();
        t2.setToNow();
        boolean isorNo = t2.after(t);
        if (isorNo) {
            setData(true);
            t.set(0, 0, 24, t2.monthDay, t2.month, t2.year);
        }
    }

}