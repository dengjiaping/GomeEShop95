package com.gome.ecmall.home.hotproms;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.PreSellProductBean;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * @author qinxudong 预售模块
 */
public class PreSellActivity extends Activity implements OnClickListener {

    private static final String TAG = "PreSellActivity";

    /** 电话预定 */
    private static final String PHONE_BOOK = "3";

    /** 普通广告 */
    private static final String NORMOL = "2";

    private ImageView preSell_img;
    private String activityId;
    private String activityType;
    private float density;
    /** 屏幕宽度 */
    private int screenWidth;
    /** 屏幕高度 */
    private int screenHeight;
    private ImageLoaderManager loaderManager;
    private LayoutInflater inflater;
    private ColorDrawable transparentDrawable;
    private ImageView rotateImage;

    private Button backBtn;
    private Button ruleBtn;
    private TextView titleText;
    private String rule;
    private String titleName;
    // private int lastY = 0;

    private LinearLayout telephone_layout;
    private LinearLayout phone_layout;
    private LinearLayout phone_img_layout;

    // private final int touchEventId = -9983761;
    // private final int index = 100;

    // private int y1, y2;

    private Animation animation;
    
    private Time t;//24点以后刷新用

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            rotateImage.clearAnimation();
            rotateImage.setVisibility(View.INVISIBLE);
            // View scroller = null;
            // if (msg.obj != null && (msg.obj instanceof View))
            // scroller = (View) msg.obj;
            switch (msg.what) {
            // case touchEventId:
            // if (lastY == scroller.getScrollY()) {
            // telephone_layout.clearAnimation();
            // Animation fromTopIn = AnimationUtils.loadAnimation(PreSellActivity.this, R.anim.from_bottom_in);
            // fromTopIn.setDuration(500);
            // if (!telephone_layout.isShown()) {
            // telephone_layout.startAnimation(fromTopIn);
            // telephone_layout.setVisibility(View.VISIBLE);
            // }
            // } else {
            // handler.sendMessageDelayed(handler.obtainMessage(touchEventId, scroller), 5);
            // lastY = scroller.getScrollY();
            // if (telephone_layout.isShown()) {
            // telephone_layout.clearAnimation();
            // Animation fromTopout = AnimationUtils.loadAnimation(PreSellActivity.this,
            // R.anim.from_bottom_out);
            // fromTopout.setDuration(1000);
            // telephone_layout.startAnimation(fromTopout);
            // telephone_layout.setVisibility(View.INVISIBLE);
            // }
            // }
            //
            // break;
            // case index:
            // if (telephone_layout.isShown()) {
            // telephone_layout.clearAnimation();
            // Animation fromTopout = AnimationUtils.loadAnimation(PreSellActivity.this, R.anim.from_bottom_out);
            // fromTopout.setDuration(1000);
            // telephone_layout.startAnimation(fromTopout);
            // telephone_layout.setVisibility(View.INVISIBLE);
            // }
            // break;
            case 1:
                ruleBtn.setText(R.string.hot_prom_rule);
                ruleBtn.setVisibility(View.VISIBLE);
                break;
            case 2:
                ruleBtn.setText(R.string.hot_rule);
                ruleBtn.setVisibility(View.INVISIBLE);
                break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presell);
        Intent intent = this.getIntent();
        activityId = intent.getStringExtra(JsonInterface.JK_ACTIVITY_ID);
        activityType = intent.getStringExtra(JsonInterface.JK_ACTIVITY_TYPE);
        density = MobileDeviceUtil.getInstance(getApplicationContext()).getScreenDensity();
        screenWidth = MobileDeviceUtil.getInstance(getApplicationContext()).getScreenWidth();
        screenHeight = MobileDeviceUtil.getInstance(getApplicationContext()).getScreenHeight();
        initView();
        initData(false);
        //24点以后刷新用
        t = new Time();
        t.setToNow();
        int year = t.year;
        int month = t.month;
        int monthday = t.monthDay;
        t.set(0, 0, 24, monthday, month, year);
    }

    void initView() {
        inflater = LayoutInflater.from(this);
        animation = AnimationUtils.loadAnimation(this, R.anim.progress_bar_rotate);
        // scrollView = (ScrollView) findViewById(R.id.presell_scroll_view);
        // scrollView 滚动到底部监听
        // scrollView.setOnTouchListener(new OnTouchListener() {
        //
        // @Override
        // public boolean onTouch(View v, MotionEvent event) {
        //
        // switch (event.getAction()) {
        // case MotionEvent.ACTION_DOWN:
        // y1 = (int) event.getY();
        // break;
        // case MotionEvent.ACTION_UP:
        // y2 = (int) event.getY();
        // break;
        // }
        // int y = y1 - y2;
        // if (event.getAction() == MotionEvent.ACTION_UP && y > 0) {
        // handler.sendMessageDelayed(handler.obtainMessage(touchEventId, v), 5);
        // } else if (event.getAction() == MotionEvent.ACTION_UP && y <= 0) {
        // handler.sendMessage(handler.obtainMessage(index));
        // }
        //
        // return false;
        //
        // }
        // });
        preSell_img = (ImageView) findViewById(R.id.presell_img);

        telephone_layout = (LinearLayout) findViewById(R.id.telephone_layout);
        telephone_layout.setVisibility(View.INVISIBLE);
        phone_img_layout = (LinearLayout) findViewById(R.id.pre_sell_img_layout);

        preSell_img.setScaleType(ScaleType.CENTER);

        if (PHONE_BOOK.equals(activityType)) {// 判断是否是电话订购
            telephone_layout.setVisibility(View.VISIBLE);
            phone_layout = (LinearLayout) findViewById(R.id.phone_layout);
            phone_layout.setOnClickListener(this);
            phone_img_layout.setVisibility(View.VISIBLE);
            LayoutParams param = new LayoutParams(screenWidth, screenHeight - Math.round(density * 135 + 0.5f));
            preSell_img.setLayoutParams(param);
        } else if (NORMOL.equals(activityType)) {
            LayoutParams param = new LayoutParams(screenWidth, screenHeight - Math.round(density * 65 + 0.5f));
            preSell_img.setLayoutParams(param);
            telephone_layout.setVisibility(View.GONE);
            phone_img_layout.setVisibility(View.GONE);
        }

        backBtn = (Button) findViewById(R.id.common_title_btn_back);
        backBtn.setText(R.string.back);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(this);

        titleText = (TextView) findViewById(R.id.common_title_tv_text);
        titleText.requestFocus();

        ruleBtn = (Button) findViewById(R.id.common_title_btn_right);
        ruleBtn.setText(R.string.hot_rule);
        ruleBtn.setOnClickListener(this);

        rotateImage = (ImageView) findViewById(R.id.rotate_image);

        loaderManager = ImageLoaderManager.initImageLoaderManager(this);
        transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
        // String url = "http://image.tianjimedia.com/uploadImages/2013/066/2429K93R69R5.jpg";
        // asyncLoadImage(preSell_img, url);
    }

    void initData(final boolean is24Judge) {
        loaderManager = ImageLoaderManager.initImageLoaderManager(this);
        transparentDrawable = new ColorDrawable(Color.TRANSPARENT);

        if (!NetUtility.isNetworkAvailable(PreSellActivity.this)) {
            CommonUtility.showMiddleToast(PreSellActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }

        ruleBtn.setVisibility(View.INVISIBLE);

        new AsyncTask<Void, Void, PreSellProductBean>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = CommonUtility.showLoadingDialog(PreSellActivity.this, getString(R.string.loading),
                        true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected PreSellProductBean doInBackground(Void... params) {

                if (TextUtils.isEmpty(activityId) || TextUtils.isEmpty(activityType)) {
                    return null;
                }
                String request = PreSellProductBean.getRequestJson(activityId, activityType);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_PRESELL_DETAIL, request);
                return PreSellProductBean.parseJson(response);
            }

            @Override
            protected void onPostExecute(PreSellProductBean result) {
                super.onPostExecute(result);
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                if (result == null) {
                    if(is24Judge){
                        finish();//关闭当前页返回上一级
                    }else{
                        CommonUtility.showMiddleToast(PreSellActivity.this, "",
                                getString(R.string.data_load_fail_exception)); 
                    }
                    return;
                }
                bindData(result);
            }

        }.execute();

    }

    void bindData(PreSellProductBean preSellProduct) {
        String name = preSellProduct.getTitleName();
        String imgUrl = preSellProduct.getImgUrl();
        titleName = name;
        rule = preSellProduct.getRule();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(imgUrl)) {
            CommonUtility.showMiddleToast(PreSellActivity.this, "", getString(R.string.data_load_fail_exception));
            return;
        }
        titleText.setText(name);
        loadGone();
        BDebug.e(TAG, imgUrl);
        asyncLoadImage(preSell_img, imgUrl);
    }

    private void asyncLoadImage(final ImageView imageView, String imageUrl) {
        // 不需要加载图片时将imageview的bitmap置空
        if (TextUtils.isEmpty(imageUrl))
            return;
        Bitmap bitmap = loaderManager.getCacheBitmap(imageUrl);

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }

        if (bitmap == null) {

            loaderManager.asyncLoad(new ImageLoadTask(imageUrl) {
                private static final long serialVersionUID = -5068460719652209430L;

                @Override
                protected Bitmap doInBackground() {
                    return NetUtility.downloadNetworkBitmap(filePath);
                }

                @Override
                public void onImageLoaded(ImageLoadTask task, final Bitmap bitmap) {
                    if (bitmap != null) {

                        final int width = bitmap.getWidth();
                        final int height = bitmap.getHeight();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int imgHeight = height * screenWidth / width;
                                imageView.setScaleType(ScaleType.FIT_XY);
                                LayoutParams param = new LayoutParams(screenWidth, imgHeight);
                                if (PHONE_BOOK.equals(activityType)) {
                                    phone_img_layout.setVisibility(View.VISIBLE);
                                    // BDebug.e(TAG,
                                    // telephone_layout.getHeight() + "*****" + Math.round(dm.density * 65 + 0.5f));
                                    // param.setMargins(0, 0, 0, Math.round(dm.density * 65 + 0.5f) - 40);
                                }
                                imageView.setLayoutParams(param);
                                BitmapDrawable destDrawable = new BitmapDrawable(inflater.getContext().getResources(),
                                        bitmap);
                                TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[] {
                                        transparentDrawable, destDrawable });
                                imageView.setImageDrawable(transitionDrawable);
                                transitionDrawable.startTransition(300);
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v == backBtn) {
            this.finish();
        } else if (v == ruleBtn) {
            Intent intent = new Intent(this, HotPromotionsRuleActivity.class);
            intent.putExtra("title", titleName);
            intent.putExtra("content", rule);
            startActivity(intent);
        } else if (v == phone_layout) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:4008-199-777"));
            startActivity(intent);
        }
    }

    /**
     * 活动有活动规则，则显示，否则隐藏活动规则按钮 隐藏加载progressbar，显示活动规则按钮
     */
    void loadGone() {
        rotateImage.setVisibility(View.VISIBLE);
        rotateImage.setAnimation(animation);
        ruleBtn.setVisibility(View.INVISIBLE);
        boolean flag = rule != null && rule.length() > 0;
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
            initData(true);
            BDebug.d("liuyang", "我也执行了");
            t.set(0, 0, 24, t2.monthDay, t2.month, t2.year);
        }
    }
}
