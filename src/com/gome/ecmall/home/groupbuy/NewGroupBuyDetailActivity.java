package com.gome.ecmall.home.groupbuy;

import java.io.File;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gome.ecmall.bean.AppInfo;
import com.gome.ecmall.bean.GBProductNew;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.GBProductNew.GroupBuyProduct;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.ShoppingCart;
import com.gome.ecmall.bean.ShoppingCart.ShoppingGo;
import com.gome.ecmall.cache.DiskCache;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.custom.DisScrollListView;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.limitbuy.LimitBuyAppraiseListActivity;
import com.gome.ecmall.home.limitbuy.LimitBuySummaryActivity;
import com.gome.ecmall.home.limitbuy.LimitCountDownTread;
import com.gome.ecmall.home.login.ActivateAccountActivity;
import com.gome.ecmall.home.login.LoginActivity;
import com.gome.ecmall.home.mygome.CouponRuleDetailActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.FileUtils;
import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.ecmall.util.NetUtility;
import com.gome.ecmall.util.Tools;
import com.gome.eshopnew.R;

/**
 * 新版团购详情activity
 * 
 * @author liuyang-ds
 * 
 */
public class NewGroupBuyDetailActivity extends Activity implements OnClickListener {
    public static final String SALEPROMOITEM = "salePromoItem";
    public static final String STATE_ZERO = "0";// 未开始
    public static final String STATE_ONE = "1";// 在售
    public static final String STATE_TWO = "2";// 抢光了
    public static final String STATE_THREE = "3";// 已结束

    private RelativeLayout rl_detail_title;
    private Button bt_detail_back;
    private Button bt_detail_share;
    private TextView tv_dratil_title;
    private ScrollView sv_detail_main;
    private RelativeLayout rl_detail_goods_img;
    private ImageView iv_detail_first;
    private ImageView iv_detail_goodsimg;
    private RelativeLayout rl_groupbuy_detail_price_background;
    private TextView tv_detail_price_sign;
    private TextView tv_detail_price_full;
    private TextView tv_detail_price_zero;
    private TextView tv_detail_cost_price;
    private TextView tv_detail_no_price;
    private Button bt_detail_right_now_go;
    private TextView tv_detail_limitbuy_num;
    private TextView tv_detail_people_buy;
    private LinearLayout ll_detail_alarm_main;
    private TextView tv_detail_day_data;
    private TextView tv_detail_day_data_str;
    private TextView tv_detail_hour_data;
    private TextView tv_detail_minute_data;
    private TextView tv_detail_second_data;
    private TextView tv_detail_goods_name;
    private ImageView iv_detail_can_refund_img;
    private TextView tv_detail_can_refund_str;
    // 实体团购
    private LinearLayout ll_detail_entity_main;
    private TextView tv_detail_goods_assess_num;
    private TextView tv_detail_goods_assess_percent;
    private RelativeLayout rl_detail_entity_assess;
    private RelativeLayout rl_detail_entity_goods_message;
    private RelativeLayout rl_detail_entity_standard;
    // 虚拟团购
    private LinearLayout ll_detail_virtual_main;
    private DisScrollListView lv_detail_virtual_storeAddress;
    private RelativeLayout rl_detail_virtual_detail_message;
    private RelativeLayout rl_detail_virtual_coupon_help;

    // 浮动层控件
    private RelativeLayout rl_groupbuy_detail_price_background_float;
    private TextView tv_detail_no_price_float;
    private TextView tv_detail_price_sign_float;
    private TextView tv_detail_price_full_float;
    private TextView tv_detail_price_zero_float;
    private TextView tv_detail_cost_price_float;
    private Button bt_detail_right_now_go_float;
    private TextView tv_detail_limitbuy_num_float;

    private String salePromoItem;
    private int[] location = new int[2];
    private int[] location2 = new int[2];

    private GroupBuyProduct gbproduct;
    private ImageLoaderManager imageLoaderManager;

    private WindowManager manager;
    private PopupWindow popWindow;
    private ListView popListView;
    private int screen_width;// 屏幕的宽度
    private int screen_height;// 屏幕的高度
    private int topView_height;// 顶部导航条高度，分享按钮用到
    private StringBuffer shareContent;// 分享内容字符串
    private ShareAdapter shareAdapter;
    private String share_pic_path = "";// 分享图片路径
    private boolean isSDCardFlag = false; // 是否有sdcard
    private boolean isShareImage = false;// 是否有可以分享的图片
    private static final String SHARE_PART1_URL = "http://www.gome.com.cn/ec/homeus/jump/product/";
    private static final String CLIENT_DOWNLODER_URL = "http://shouji.gome.com.cn/";
    private static final String SHARE_PART2_URL = ".html";
    private static final String SHARE_IMG_FILE_PATH = "/sdcard/eshop/share";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_groupbuy_detail);
        String statu = Environment.getExternalStorageState();
        isSDCardFlag = statu.equals(Environment.MEDIA_MOUNTED);
        manager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        screen_width = manager.getDefaultDisplay().getWidth();
        screen_height = manager.getDefaultDisplay().getHeight();
        Rect rect = new Rect();
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        screen_height = rect.bottom;
        imageLoaderManager = ImageLoaderManager.initImageLoaderManager(this);
        salePromoItem = getIntent().getStringExtra(SALEPROMOITEM);
        initializeViews();
        setData(true);
    }

    @Override
    protected void onDestroy() {
        gbproduct = null;
        super.onDestroy();
    }

    /**
     * 设置数据
     * 
     * @param showDialog
     */
    private void setData(final boolean showDialog) {

        if (!NetUtility.isNetworkAvailable(NewGroupBuyDetailActivity.this)) {
            CommonUtility.showMiddleToast(NewGroupBuyDetailActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, GroupBuyProduct>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                if (showDialog) {
                    loadingDialog = CommonUtility.showLoadingDialog(NewGroupBuyDetailActivity.this,
                            getString(R.string.loading), true, new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    cancel(true);
                                }
                            });
                }

            }

            @Override
            protected GroupBuyProduct doInBackground(Object... params) {
                String request = GBProductNew.createRequestGroupBuyProductDetailJson(salePromoItem);
                // String request = GBProductNew.createRequestGroupBuyProductDetailJson("shGroupOnItem100004900539");
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_NEW_GROUPBUY_DETAILS, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return GBProductNew.parseGroupBuyProduct(response);
            }

            @Override
            protected void onPostExecute(GroupBuyProduct result) {
                // 模拟虚拟团购
                // result.setGrouponType("1");
                // result.setGoodsShareUrl(result.getGrouponGoodsDesc());
                // result.setSpecifications(result.getGrouponGoodsDesc());
                // result.setAppraiseCount(1200);
                // result.setHighPraise("90%");
                // result.setIsSupportRefund("Y");
                // ArrayList<StoreAddress> storeAddressList = new ArrayList<StoreAddress>();
                // for(int i =0;i<6;i++){
                // StoreAddress storeAddress = new StoreAddress();
                // storeAddress.setAddress("北京市海淀区10号大街");
                // storeAddress.setLatitude(39.990548);
                // storeAddress.setLongitude(116.321197);
                // storeAddress.setStoreName("中关村鼎好点");
                // storeAddress.setTelephone("101-4564561");
                // storeAddressList.add(storeAddress);
                // }
                // result.setStoreAddressList(storeAddressList);

                if (isCancelled()) {
                    return;
                }
                if (showDialog) {
                    loadingDialog.dismiss();
                }
                if (result == null) {
                    CommonUtility.showMiddleToast(NewGroupBuyDetailActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                gbproduct = result;
                setViewData(gbproduct);
            }

        }.execute();

    }

    // 给控件设置数值
    private void setViewData(GroupBuyProduct gbproduct) {
        sv_detail_main.setVisibility(View.VISIBLE);
        tv_detail_goods_name.setText(Html.fromHtml("<font color=\"#333333\">" + gbproduct.getSkuName() + "</font>  "
                + "<font color=\"#999999\">" + gbproduct.getGrouponProperty() + "</font>"));
        String nowPrice = gbproduct.getSkuGrouponBuyPrice();
        String nowPrice_full = nowPrice;// 整数部分
        String nowPrice_zero = "";// 小数部分
        if (nowPrice != null) {
            int flag = nowPrice.indexOf(".");
            if (-1 != flag) {
                nowPrice_full = nowPrice.substring(0, flag + 1);
                nowPrice_zero = nowPrice.substring(flag + 1);
            }
        }
        // 暂无售价判断
        if (CommonUtility.isOrNoZero(nowPrice, false)) {
            tv_detail_no_price.setVisibility(View.VISIBLE);
            tv_detail_price_sign.setVisibility(View.GONE);
            tv_detail_price_full.setVisibility(View.GONE);
            tv_detail_price_zero.setVisibility(View.GONE);
            tv_detail_no_price.setText(getString(R.string.now_not_have_price));
            // 浮层上的设置
            tv_detail_no_price_float.setVisibility(View.VISIBLE);
            tv_detail_price_sign_float.setVisibility(View.GONE);
            tv_detail_price_full_float.setVisibility(View.GONE);
            tv_detail_price_zero_float.setVisibility(View.GONE);
            tv_detail_no_price_float.setText(getString(R.string.now_not_have_price));
        } else {
            tv_detail_no_price.setVisibility(View.GONE);
            tv_detail_price_sign.setVisibility(View.VISIBLE);
            tv_detail_price_full.setVisibility(View.VISIBLE);
            tv_detail_price_zero.setVisibility(View.VISIBLE);
            tv_detail_price_full.setText(nowPrice_full);
            tv_detail_price_zero.setText(nowPrice_zero + " ");
            // 浮层上的设置
            tv_detail_no_price_float.setVisibility(View.GONE);
            tv_detail_price_sign_float.setVisibility(View.VISIBLE);
            tv_detail_price_full_float.setVisibility(View.VISIBLE);
            tv_detail_price_zero_float.setVisibility(View.VISIBLE);
            tv_detail_price_full_float.setText(nowPrice_full);
            tv_detail_price_zero_float.setText(nowPrice_zero + " ");
        }
        if (!TextUtils.isEmpty(gbproduct.getSkuOriginalPrice())) {
            tv_detail_cost_price.setText("￥" + gbproduct.getSkuOriginalPrice());
            tv_detail_cost_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tv_detail_cost_price_float.setText("￥" + gbproduct.getSkuOriginalPrice());
            tv_detail_cost_price_float.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        tv_detail_people_buy.setText(gbproduct.getBoughtNum() + getString(R.string.groupbuy_already_buy));
        // 判断是虚拟还是实体。然后相应的显示控件
        if ("0".equalsIgnoreCase(gbproduct.getGrouponType())) {
            tv_detail_limitbuy_num.setVisibility(View.VISIBLE);
            tv_detail_limitbuy_num.setText(getString(R.string.groupbuy_limit_everyone)
                    + gbproduct.getEveryoneLimitBuyNum() + getString(R.string.groupbuy_limit_unit));
            tv_detail_limitbuy_num_float.setVisibility(View.VISIBLE);
            tv_detail_limitbuy_num_float.setText(getString(R.string.groupbuy_limit_everyone)
                    + gbproduct.getEveryoneLimitBuyNum() + getString(R.string.groupbuy_limit_unit));
            tv_detail_goods_assess_num.setText("商品评价(" + gbproduct.getAppraiseCount() + ")");
            tv_detail_goods_assess_percent.setText(gbproduct.getHighPraise());
            iv_detail_can_refund_img.setVisibility(View.GONE);
            tv_detail_can_refund_str.setVisibility(View.GONE);
            ll_detail_entity_main.setVisibility(View.VISIBLE);
            ll_detail_virtual_main.setVisibility(View.GONE);
            // 注册相应点击事件
            rl_detail_entity_assess.setOnClickListener(this);
            rl_detail_entity_goods_message.setOnClickListener(this);
            rl_detail_entity_standard.setOnClickListener(this);
        } else if ("1".equalsIgnoreCase(gbproduct.getGrouponType())) {
            tv_detail_limitbuy_num.setVisibility(View.GONE);
            tv_detail_limitbuy_num_float.setVisibility(View.GONE);
            iv_detail_can_refund_img.setVisibility(View.VISIBLE);
            tv_detail_can_refund_str.setVisibility(View.VISIBLE);
            if ("Y".equals(gbproduct.getIsSupportRefund())) {
                iv_detail_can_refund_img.setBackgroundResource(R.drawable.groupbuy_can_refund);
                tv_detail_can_refund_str.setText("支持退款");
            } else if ("N".equals(gbproduct.getIsSupportRefund())) {
                iv_detail_can_refund_img.setBackgroundResource(R.drawable.groupbuy_not_can_refund);
                tv_detail_can_refund_str.setText("不支持退款");
            }
            ll_detail_entity_main.setVisibility(View.GONE);
            ll_detail_virtual_main.setVisibility(View.VISIBLE);
            rl_detail_virtual_detail_message.setOnClickListener(this);
            rl_detail_virtual_coupon_help.setOnClickListener(this);
            NewGroupBuyStoreAdapter adapter = new NewGroupBuyStoreAdapter(NewGroupBuyDetailActivity.this,
                    gbproduct.getStoreAddressList());
            lv_detail_virtual_storeAddress.setAdapter(adapter);
        } else {
            tv_detail_limitbuy_num.setVisibility(View.GONE);
            tv_detail_limitbuy_num_float.setVisibility(View.GONE);
            iv_detail_can_refund_img.setVisibility(View.GONE);
            tv_detail_can_refund_str.setVisibility(View.GONE);
        }

        if ("1".equals(gbproduct.getGrouponGoodsMark())) {
            iv_detail_first.setVisibility(View.VISIBLE);
        } else {
            iv_detail_first.setVisibility(View.GONE);
        }
        // / 0：未开始，1：在售 2：卖光了 3：已结束
        // 根据状态显示不同控件
        setStartShowBySaleState(gbproduct.getSaleState());

        // 剩余时间
        if (!TextUtils.isEmpty(gbproduct.getRamainTime()) && !"3".equals(gbproduct.getSaleState())) {
            long timeSecound = 0;
            try {
                timeSecound = Long.valueOf(gbproduct.getRamainTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
            String hourMinSecond = FileUtils.secToTimeWithDay(timeSecound);
            String[] hmsStrs = hourMinSecond.split(":");
            if (hmsStrs != null && hmsStrs.length == 4) {
                tv_detail_day_data.setText(hmsStrs[0]);
                tv_detail_hour_data.setText(hmsStrs[1]);
                tv_detail_minute_data.setText(hmsStrs[2]);
                tv_detail_second_data.setText(hmsStrs[3]);
            }
            LimitCountDownTread startThread = new LimitCountDownTread(new MyHandler(gbproduct, new TextView[] {
                    tv_detail_day_data, tv_detail_hour_data, tv_detail_minute_data, tv_detail_second_data }), true);
            startThread.setHourMinSecond(hourMinSecond);
            startThread.start();
        }
        if (!GlobalConfig.getInstance().isNeedLoadImage() && !gbproduct.isLoadImg()) {
            iv_detail_goodsimg.setImageResource(R.drawable.category_product_tapload_bg);
            iv_detail_goodsimg.setOnLongClickListener(new MyOnLongClickListener(gbproduct, iv_detail_goodsimg,
                    rl_detail_goods_img));
        } else {
            asyncLoadThumbImage(gbproduct, iv_detail_goodsimg, rl_detail_goods_img);
        }

    }

    /**
     * 初始时根据状态显示不同的标题隐藏不同的按钮
     * 
     * @param SaleState
     */
    public void setStartShowBySaleState(String SaleState) {
        if (STATE_ZERO.equals(SaleState)) {// 0：未开始
            ll_detail_alarm_main.setVisibility(View.VISIBLE);
            bt_detail_right_now_go.setVisibility(View.VISIBLE);
            bt_detail_right_now_go.setText(getString(R.string.at_once_start));
            bt_detail_right_now_go.setOnClickListener(null);
            bt_detail_right_now_go.setBackgroundResource(R.drawable.common_orange_btn_disable);
            // 设置浮层按钮样式
            bt_detail_right_now_go_float.setVisibility(View.VISIBLE);
            bt_detail_right_now_go_float.setText(getString(R.string.at_once_start));
            bt_detail_right_now_go_float.setOnClickListener(null);
            bt_detail_right_now_go_float.setBackgroundResource(R.drawable.common_orange_btn_disable);
        } else if (STATE_ONE.equals(SaleState)) { // 1：在售
            ll_detail_alarm_main.setVisibility(View.VISIBLE);
            bt_detail_right_now_go.setVisibility(View.VISIBLE);
            bt_detail_right_now_go.setText("马上参团");
            bt_detail_right_now_go.setOnClickListener(this);
            bt_detail_right_now_go.setBackgroundResource(R.drawable.common_orange_btn);

            bt_detail_right_now_go_float.setVisibility(View.VISIBLE);
            bt_detail_right_now_go_float.setText("马上参团");
            bt_detail_right_now_go_float.setOnClickListener(this);
            bt_detail_right_now_go_float.setBackgroundResource(R.drawable.common_orange_btn);
        } else if (STATE_TWO.equals(SaleState)) { // 2：卖光了
            ll_detail_alarm_main.setVisibility(View.GONE);
            bt_detail_right_now_go.setVisibility(View.VISIBLE);
            bt_detail_right_now_go.setText("卖光了");
            bt_detail_right_now_go.setOnClickListener(null);
            bt_detail_right_now_go.setBackgroundResource(R.drawable.common_orange_btn_disable);

            bt_detail_right_now_go_float.setVisibility(View.VISIBLE);
            bt_detail_right_now_go_float.setText("卖光了");
            bt_detail_right_now_go_float.setOnClickListener(null);
            bt_detail_right_now_go_float.setBackgroundResource(R.drawable.common_orange_btn_disable);
        } else if (STATE_THREE.equals(SaleState)) { // 3：已结束
            ll_detail_alarm_main.setVisibility(View.GONE);
            bt_detail_right_now_go.setVisibility(View.VISIBLE);
            bt_detail_right_now_go.setText("已结束");
            bt_detail_right_now_go.setOnClickListener(null);
            bt_detail_right_now_go.setBackgroundResource(R.drawable.common_orange_btn_disable);

            bt_detail_right_now_go_float.setVisibility(View.VISIBLE);
            bt_detail_right_now_go_float.setText("已结束");
            bt_detail_right_now_go_float.setOnClickListener(null);
            bt_detail_right_now_go_float.setBackgroundResource(R.drawable.common_orange_btn_disable);
        }

    }

    /**
     * 后来根据状态显示不同的标题隐藏不同的按钮
     * 
     * @param SaleState
     */
    public void setEndShowBySaleState(GroupBuyProduct gbproduct) {
        String SaleState = gbproduct.getSaleState();
        bt_detail_right_now_go.setVisibility(View.VISIBLE);
        bt_detail_right_now_go.setOnClickListener(this);
        if (STATE_ZERO.equals(SaleState)) {// 0：未开始
            // 去服务器取距离结束时间
            setData(false);
        } else if (STATE_ONE.equals(SaleState)) { // 1：在售
            gbproduct.setSaleState("3");// 设置状态为3便于图片变灰处理
            ll_detail_alarm_main.setVisibility(View.GONE);
            bt_detail_right_now_go.setVisibility(View.VISIBLE);
            bt_detail_right_now_go.setText("结束了");
            bt_detail_right_now_go.setOnClickListener(null);
            bt_detail_right_now_go.setBackgroundResource(R.drawable.common_orange_btn_disable);
            if (!GlobalConfig.getInstance().isNeedLoadImage() && !gbproduct.isLoadImg()) {
                iv_detail_goodsimg.setImageResource(R.drawable.category_product_tapload_bg);
                iv_detail_goodsimg.setOnLongClickListener(new MyOnLongClickListener(gbproduct, iv_detail_goodsimg,
                        rl_detail_goods_img));

            } else {
                asyncLoadThumbImage(gbproduct, iv_detail_goodsimg, rl_detail_goods_img);
            }
            // rl_groupbuy_detail_price_background.setBackgroundColor(Color.parseColor(getString(R.string.state_grey)));
        } else if (STATE_TWO.equals(SaleState)) { // 2：卖光了
            ll_detail_alarm_main.setVisibility(View.GONE);
            bt_detail_right_now_go.setVisibility(View.VISIBLE);
            bt_detail_right_now_go.setText("卖光了");
            bt_detail_right_now_go.setOnClickListener(null);
            bt_detail_right_now_go.setBackgroundResource(R.drawable.common_orange_btn_disable);
        } else if (STATE_THREE.equals(SaleState)) { // 3：已结束
            ll_detail_alarm_main.setVisibility(View.GONE);
            bt_detail_right_now_go.setVisibility(View.VISIBLE);
            bt_detail_right_now_go.setText("结束了");
            bt_detail_right_now_go.setOnClickListener(null);
            bt_detail_right_now_go.setBackgroundResource(R.drawable.common_orange_btn_disable);
        }

    }

    // 初始化控件
    private void initializeViews() {
        rl_detail_title = (RelativeLayout) this.findViewById(R.id.rl_detail_title);
        bt_detail_back = (Button) this.findViewById(R.id.bt_detail_back);
        bt_detail_share = (Button) this.findViewById(R.id.bt_detail_share);
        tv_dratil_title = (TextView) this.findViewById(R.id.tv_dratil_title);
        sv_detail_main = (ScrollView) this.findViewById(R.id.sv_detail_main);
        rl_detail_goods_img = (RelativeLayout) this.findViewById(R.id.rl_detail_goods_img);
        iv_detail_first = (ImageView) this.findViewById(R.id.iv_detail_first);
        iv_detail_goodsimg = (ImageView) this.findViewById(R.id.iv_detail_goodsimg);
        rl_groupbuy_detail_price_background = (RelativeLayout) this
                .findViewById(R.id.rl_groupbuy_detail_price_background);
        tv_detail_price_sign = (TextView) this.findViewById(R.id.tv_detail_price_sign);
        tv_detail_price_full = (TextView) this.findViewById(R.id.tv_detail_price_full);
        tv_detail_price_zero = (TextView) this.findViewById(R.id.tv_detail_price_zero);
        tv_detail_cost_price = (TextView) this.findViewById(R.id.tv_detail_cost_price);
        tv_detail_no_price = (TextView) this.findViewById(R.id.tv_detail_no_price);
        bt_detail_right_now_go = (Button) this.findViewById(R.id.bt_detail_right_now_go);
        tv_detail_limitbuy_num = (TextView) this.findViewById(R.id.tv_detail_limitbuy_num);
        tv_detail_people_buy = (TextView) this.findViewById(R.id.tv_detail_people_buy);
        ll_detail_alarm_main = (LinearLayout) this.findViewById(R.id.ll_detail_alarm_main);
        tv_detail_day_data = (TextView) this.findViewById(R.id.tv_detail_day_data);
        tv_detail_day_data_str = (TextView) this.findViewById(R.id.tv_detail_day_data_str);
        tv_detail_hour_data = (TextView) this.findViewById(R.id.tv_detail_hour_data);
        tv_detail_minute_data = (TextView) this.findViewById(R.id.tv_detail_minute_data);
        tv_detail_second_data = (TextView) this.findViewById(R.id.tv_detail_second_data);
        tv_detail_goods_name = (TextView) this.findViewById(R.id.tv_detail_goods_name);
        iv_detail_can_refund_img = (ImageView) this.findViewById(R.id.iv_detail_can_refund_img);
        tv_detail_can_refund_str = (TextView) this.findViewById(R.id.tv_detail_can_refund_str);
        // 实体团购
        ll_detail_entity_main = (LinearLayout) this.findViewById(R.id.ll_detail_entity_main);
        rl_detail_entity_assess = (RelativeLayout) this.findViewById(R.id.rl_detail_entity_assess);
        rl_detail_entity_goods_message = (RelativeLayout) this.findViewById(R.id.rl_detail_entity_goods_message);
        rl_detail_entity_standard = (RelativeLayout) this.findViewById(R.id.rl_detail_entity_standard);
        tv_detail_goods_assess_num = (TextView) this.findViewById(R.id.tv_detail_goods_assess_num);
        tv_detail_goods_assess_percent = (TextView) this.findViewById(R.id.tv_detail_goods_assess_percent);
        // 虚拟团购
        ll_detail_virtual_main = (LinearLayout) this.findViewById(R.id.ll_detail_virtual_main);
        lv_detail_virtual_storeAddress = (DisScrollListView) this.findViewById(R.id.lv_detail_virtual_storeAddress);
        rl_detail_virtual_detail_message = (RelativeLayout) this.findViewById(R.id.rl_detail_virtual_detail_message);
        rl_detail_virtual_coupon_help = (RelativeLayout) this.findViewById(R.id.rl_detail_virtual_coupon_help);
        // 浮动层控件
        rl_groupbuy_detail_price_background_float = (RelativeLayout) this
                .findViewById(R.id.rl_groupbuy_detail_price_background_float);
        tv_detail_no_price_float = (TextView) this.findViewById(R.id.tv_detail_no_price_float);
        tv_detail_price_sign_float = (TextView) this.findViewById(R.id.tv_detail_price_sign_float);
        tv_detail_price_full_float = (TextView) this.findViewById(R.id.tv_detail_price_full_float);
        tv_detail_price_zero_float = (TextView) this.findViewById(R.id.tv_detail_price_zero_float);
        tv_detail_cost_price_float = (TextView) this.findViewById(R.id.tv_detail_cost_price_float);
        bt_detail_right_now_go_float = (Button) this.findViewById(R.id.bt_detail_right_now_go_float);
        tv_detail_limitbuy_num_float = (TextView) this.findViewById(R.id.tv_detail_limitbuy_num_float);
        sv_detail_main.setOnTouchListener(new svOntachListener());
        bt_detail_back.setOnClickListener(this);
        bt_detail_share.setOnClickListener(this);
        bt_detail_right_now_go.setOnClickListener(this);
        bt_detail_right_now_go_float.setOnClickListener(this);

    }

    public class svOntachListener implements OnTouchListener {
        private int lastY = 0;
        private int touchEventId = -9983761;
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == touchEventId) {
                    if (lastY != sv_detail_main.getScrollY()) {
                        // scrollview一直在滚动，会触发
                        handler.sendMessageDelayed(handler.obtainMessage(touchEventId, sv_detail_main), 5);
                        lastY = sv_detail_main.getScrollY();
                        rl_groupbuy_detail_price_background.getLocationOnScreen(location);
                        rl_groupbuy_detail_price_background_float.getLocationOnScreen(location2);
                        // 动的到静的位置时，静的显示。动的实际上还是网上滚动，但我们看到的是静止的那个
                        if (location[1] <= location2[1]) {
                            rl_groupbuy_detail_price_background_float.setVisibility(View.VISIBLE);
                        } else {
                            // 静止的隐藏了
                            rl_groupbuy_detail_price_background_float.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        };

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // 必须两个都搞上，不然会有瑕疵。
            // 没有这段，手指按住拖动的时候没有效果
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                handler.sendMessageDelayed(handler.obtainMessage(touchEventId, v), 5);
            }
            // 没有这段，手指松开scroll继续滚动的时候，没有效果
            if (event.getAction() == MotionEvent.ACTION_UP) {
                handler.sendMessageDelayed(handler.obtainMessage(touchEventId, v), 5);
            }
            return false;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.bt_detail_back:
            // 后退键
            finish();
            break;
        case R.id.bt_detail_share:
            // 分享键进入分享界面
            if (gbproduct == null) {
                CommonUtility.showToast(this, getString(R.string.share_no_product));
                return;
            }
            topView_height = rl_detail_title.getHeight();
            doShare();
            if (getPopupWindow()) {
                popWindow.showAsDropDown(bt_detail_share, (int) (screen_width / 1.5), 0);
                // startAnimation();
            } else {
                CommonUtility.showToast(this, getString(R.string.share_no_method));
            }
            break;
        case R.id.rl_detail_entity_assess:
            // 实体团购商品评价
            Intent intent = new Intent(NewGroupBuyDetailActivity.this, LimitBuyAppraiseListActivity.class);
            intent.putExtra(LimitBuySummaryActivity.INTENT_KEY_GOODS_NO, gbproduct.getGoodsNo());
            startActivity(intent);
            break;
        case R.id.rl_detail_entity_goods_message:
            // 实体团购商品介绍
            Intent intent1 = new Intent(NewGroupBuyDetailActivity.this, LimitBuySummaryActivity.class);
            intent1.putExtra(LimitBuySummaryActivity.INTENT_KEY_GOODS_NO, gbproduct.getGoodsNo());
            startActivity(intent1);

            break;
        case R.id.rl_detail_entity_standard:
            // 实体团购规格参数
            Intent intent2 = new Intent(NewGroupBuyDetailActivity.this, LimitBuySummaryActivity.class);
            intent2.putExtra(LimitBuySummaryActivity.INTENT_KEY_GOODS_NO, gbproduct.getGoodsNo());
            intent2.putExtra(LimitBuySummaryActivity.INTENT_KEY_GOODS_TYPE, "wvSpecification");
            startActivity(intent2);
            break;
        case R.id.rl_detail_virtual_detail_message:
            // 虚拟团购查看详情
            Intent intent3 = new Intent(NewGroupBuyDetailActivity.this, LimitBuySummaryActivity.class);
            intent3.putExtra(LimitBuySummaryActivity.INTENT_KEY_GOODS_NO, gbproduct.getGoodsNo());
            startActivity(intent3);
            break;
        case R.id.rl_detail_virtual_coupon_help:
            // 虚拟团购团购卷使用流程
            Intent intent5 = new Intent(NewGroupBuyDetailActivity.this, CouponRuleDetailActivity.class);
            intent5.putExtra(CouponRuleDetailActivity.TYPE, "2");
            startActivity(intent5);
            break;
        case R.id.bt_detail_right_now_go:
        case R.id.bt_detail_right_now_go_float:
            // 进入填写订单页
            if (GlobalConfig.isLogin) {
                if (gbproduct != null)
                    isGoShoppingOrder();
            } else {
                Intent intent4 = new Intent();
                intent4.setClass(getApplicationContext(), LoginActivity.class);
                intent4.setAction(this.getClass().getName());
                startActivity(intent4);
            }
            break;
        default:
            break;
        }

    }

    /**
     * 初始化分享窗口 popuptwindow 必须设置背景色及setBackgroundDrawable 才能响应返回按钮事件
     */
    private boolean initPopuptWindow() {
        float density = MobileDeviceUtil.getInstance(getApplicationContext()).getScreenDensity();
        List<AppInfo> list = Tools.getShareAppList(this, isSDCardFlag, isShareImage);
        if (list.size() == 0) {
            return false;
        }

        int pop_height = 0;
        if ((list.size() * 54 * density + 42 * density + (list.size() - 1) * 1.5 * density) < (screen_height - 3 * topView_height)) {
            pop_height = (int) (list.size() * 54 * density + 42.5 * density + (list.size() - 1) * 1.5
                    * density);
        } else {
            pop_height = screen_height - 3 * topView_height;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                pop_height - (int) (42.5 * density));
        View popuptWindow_view = this.getLayoutInflater().inflate(R.layout.share_popwindow, null);
        // View headView = this.getLayoutInflater().inflate(R.layout.pop_head_view, null);
        popWindow = new PopupWindow(popuptWindow_view, (int) (2 * screen_width / 3), pop_height, true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());// 设置该属性响应返回键，不设置将不会响应返回键
        popListView = (ListView) popuptWindow_view.findViewById(R.id.share_listview);
        shareAdapter = new ShareAdapter(NewGroupBuyDetailActivity.this, list);
        // listView.addHeaderView(headView);
        popListView.setAdapter(shareAdapter);
        popListView.setLayoutParams(params);
        popWindow.setAnimationStyle(R.style.AnimationFade);
        popuptWindow_view.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                shareAdapter.notifyDataSetChanged();
                if (popWindow != null && popWindow.isShowing()) {
                    popWindow.dismiss();
                    popWindow = null;
                }
                return false;
            }
        });
        popListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // popWindow.dismiss() ;
                String content = "";
                Intent intent = new Intent(Intent.ACTION_SEND);
                AppInfo appInfo = (AppInfo) shareAdapter.getItem(position);
                intent.setComponent(new ComponentName(appInfo.getAppPkgName(), appInfo.getAppLauncherClassName()));
                if (appInfo.getAppPkgName().equals("com.tencent.WBlog")) {// 腾讯微博比较特殊
                    content = shareContent.toString().replace("@国美在线", "#国美在线#");
                } else if (appInfo.getAppPkgName().equals("com.sina.weibo")) {
                    content = shareContent.toString();
                } else if (appInfo.getAppPkgName().equals("com.netease.wb")) {
                    content = shareContent.toString().split("下载国美在线android客户端")[0];
                } else {
                    content = shareContent.toString().replace("@国美在线", "国美在线");
                }
                intent.putExtra(Intent.EXTRA_TEXT, content);
                if (isSDCardFlag && isShareImage) {
                    intent.setType("image/*");
                    Uri uri = Uri.parse("file://" + share_pic_path);
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                } else {
                    intent.setType("text/plain");
                }
                NewGroupBuyDetailActivity.this.startActivity(intent);
                if (popWindow != null && popWindow.isShowing()) {
                    popWindow.dismiss();
                    popWindow = null;
                }
            }
        });
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (popWindow != null && popWindow.isShowing()) {
                popWindow.dismiss();
                popWindow = null;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 初始化分享内容以及图片
     * 
     */
    @SuppressWarnings("null")
    @SuppressLint("SdCardPath")
    private void doShare() {
        shareContent = new StringBuffer();
        shareContent.append(getString(R.string.share_content_part1)).append(gbproduct.getSkuName().toString())
                .append(getString(R.string.share_content_part2));
        if (gbproduct.getGoodsShareUrl() != null && gbproduct.getGoodsShareUrl().length() > 0) {
            shareContent.append(gbproduct.getGoodsShareUrl()).append(getString(R.string.share_content_part3))
                    .append(CLIENT_DOWNLODER_URL);
        } else {
            shareContent.append(SHARE_PART1_URL).append(gbproduct.getGoodsNo()).append(SHARE_PART2_URL)
                    .append(getString(R.string.share_content_part3)).append(CLIENT_DOWNLODER_URL);
        }
        if (isSDCardFlag) {// 有sdcard,进行分享图片处理
            DiskCache diskCache = new DiskCache();
            DiskCache.initDiskCache(NewGroupBuyDetailActivity.this);
            File file = diskCache.getAbsFile();
            String cacheName = FileUtils.getCacheKey(gbproduct.getSkuThumbImgUrl());
            File cacheFile = new File(file, cacheName);
            if (TextUtils.isEmpty(gbproduct.getSkuThumbImgUrl()) || cacheFile == null || !cacheFile.exists()) {
                isShareImage = false;
            } else {
                isShareImage = true;
            }

            File shareFile = new File(SHARE_IMG_FILE_PATH);
            File shareContentFile = null;
            if (null != cacheFile || cacheFile.exists()) {
                shareFile.delete();
                if (!shareFile.exists()) {
                    shareFile.mkdirs();
                }
                shareContentFile = new File(shareFile, "share.jpg");
                // cacheFile.renameTo(shareContentFile.getAbsoluteFile()) ;
                FileUtils.copyFile(cacheFile.getAbsolutePath(), shareContentFile.getAbsolutePath());
                share_pic_path = shareContentFile.getAbsolutePath();
            }
        }
        if (shareAdapter != null) {
            List<AppInfo> list = Tools.getShareAppList(this, isSDCardFlag, isShareImage);
            shareAdapter.setList(list);
            shareAdapter.notifyDataSetChanged();
        }

    }

    /**
     * 获取分享弹出窗口
     */
    private boolean getPopupWindow() {
        if (null != popWindow) {
            popWindow.dismiss();
            return true;
        } else {
            return initPopuptWindow();
        }
    }

    /**
     * 是否能够参团
     */
    private void isGoShoppingOrder() {
        if (!NetUtility.isNetworkAvailable(NewGroupBuyDetailActivity.this)) {
            CommonUtility.showMiddleToast(NewGroupBuyDetailActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, ShoppingGo>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(NewGroupBuyDetailActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            };

            protected ShoppingGo doInBackground(Object... params) {
                String request = GBProductNew.createRequestGroupBuyCheckJson(gbproduct.getSkuID(),
                        gbproduct.getGoodsNo(), gbproduct.getSalePromoItem());
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_GROUPON_GROUPONCHECK, request);
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
                    return null;
                }
                return ShoppingCart.go_ShoppingOrder(result);
            };

            protected void onPostExecute(ShoppingGo shoppingGo) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                if (shoppingGo == null) {
                    return;
                }
                if (shoppingGo.isSuccess()) {
                    Intent grouplimitIntent = new Intent();
                    if (gbproduct != null) {
                        grouplimitIntent.putExtra(GroupLimitOrderActivity.GroupLimitOrderActivitySKUID,
                                gbproduct.getSkuID());
                        grouplimitIntent.putExtra(GroupLimitOrderActivity.GroupLimitOrderActivityGoodsNo,
                                gbproduct.getGoodsNo());
                        grouplimitIntent.putExtra(GroupLimitOrderActivity.GroupLimitOrderActivityRushbuyitemId,
                                gbproduct.getSalePromoItem());
                        GlobalConfig.getInstance().setGroupLimitType(GroupLimitOrderActivity.GroupType);
                        if ("N".equalsIgnoreCase(shoppingGo.getIsVirtualGroupon())) {
                            grouplimitIntent.setClass(getApplicationContext(), GroupLimitOrderActivity.class);
                        } else if ("Y".equalsIgnoreCase(shoppingGo.getIsVirtualGroupon())) {
                            grouplimitIntent.putExtra(GroupLimitOrderActivity.GroupLimitOrderActivityGoodName,
                                    gbproduct.getSkuName());
                            grouplimitIntent.setClass(getApplicationContext(), VirtualGroupOrderActivity.class);
                        }
                    }
                    startActivityForResult(grouplimitIntent, 0);
                    // startActivity(grouplimitIntent);
                    return;
                } else if (!shoppingGo.isSuccess() && !shoppingGo.isActivated()) {
                    Intent intent = new Intent();
                    // intent.putExtra(ShoppingGo.SHOPPING_GO, shoppingGo);
                    intent.putExtra(JsonInterface.JK_MOBILE, shoppingGo.getMobile());
                    intent.setClass(NewGroupBuyDetailActivity.this, ActivateAccountActivity.class);
                    startActivityForResult(intent, 0);
                } else if (!shoppingGo.isSuccess() && shoppingGo.isSessionExpired()) {
                    // 服务器返回登录超时
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), LoginActivity.class);
                    intent.setAction(this.getClass().getName());
                    startActivity(intent);
                } else if (!shoppingGo.isSuccess()) {
                    CommonUtility.showMiddleToast(NewGroupBuyDetailActivity.this, "", ShoppingCart.getErrorMessage());
                    return;
                }
            };

        }.execute();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
        case 3:// 继续购物跳转到团购列表
            finish();
            break;
        case 4:// 到团购券，将此模块界面都关闭
            setResult(4);
            finish();
            break;
        case 5:
            Intent intent = new Intent(this, NewGroupBuyActivity.class);
            setResult(5, intent);
            finish();
            break;
        }
    }

    public class MyHandler extends Handler {
        private TextView myTimeText[];
        private GroupBuyProduct gbproduct;

        public MyHandler(GroupBuyProduct gbproduct, TextView timeText[]) {
            this.myTimeText = timeText;
            this.gbproduct = gbproduct;
        }

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
            case 0: {
                String[] times = (String[]) msg.obj;
                if (myTimeText != null && myTimeText.length == 4 && times != null && times.length == 4) {
                    myTimeText[0].setText(times[0]);
                    myTimeText[1].setText(times[1]);
                    myTimeText[2].setText(times[2]);
                    myTimeText[3].setText(times[3]);
                    if ("00".equals(times[0]) && "00".equals(times[1]) && "00".equals(times[2])
                            && "00".equals(times[3])) {
                        // 根据不同状态显示相应控件
                        setEndShowBySaleState(gbproduct);
                    }
                }
            }
                break;
            }
        }
    };

    private void asyncLoadThumbImage(final GroupBuyProduct gbproduct, ImageView imageView, final ViewGroup parent) {
        if (TextUtils.isEmpty(gbproduct.getSkuThumbImgUrl()))
            return;
        gbproduct.setLoadImg(true);
        Bitmap bitmap = imageLoaderManager.getCacheBitmap(gbproduct.getSkuThumbImgUrl());
        if ("3".equals(gbproduct.getSaleState())) {
            imageView.setImageBitmap(bitmap2Gray(bitmap));
        } else {
            imageView.setImageBitmap(bitmap);
        }
        if (bitmap == null) {
            imageView.setTag(gbproduct.getSkuThumbImgUrl());
            // imageView.setImageDrawable(new BitmapDrawable(getResources(), bitmap));
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.product_list_grid_item_icon_bg));
            imageLoaderManager.asyncLoad(new ImageLoadTask(gbproduct.getSkuThumbImgUrl()) {
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
                            if ("3".equals(gbproduct.getSaleState())) {
                                ((ImageView) tagedView).setImageBitmap(bitmap2Gray(bitmap));
                            } else {
                                ((ImageView) tagedView).setImageBitmap(bitmap);
                            }

                        }
                    }
                }

            });
        }
    }

    /**
     * 图片转灰度
     * 
     * @param bmSrc
     * @return
     */
    private Bitmap bitmap2Gray(Bitmap bmSrc) {
        if (bmSrc != null) {
            int width, height;
            height = bmSrc.getHeight();
            width = bmSrc.getWidth();
            Bitmap bmpGray = null;
            bmpGray = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            Canvas c = new Canvas(bmpGray);
            Paint paint = new Paint();
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
            paint.setColorFilter(f);
            c.drawBitmap(bmSrc, 0, 0, paint);
            return bmpGray;
        } else {
            return null;
        }

    }

    private class MyOnLongClickListener implements OnLongClickListener {

        GroupBuyProduct gbproduct;
        ImageView imageView;
        ViewGroup parent;

        public MyOnLongClickListener(GroupBuyProduct gbproduct, ImageView imageView, ViewGroup parent) {
            this.gbproduct = gbproduct;
            this.imageView = imageView;
            this.parent = parent;
        }

        @Override
        public boolean onLongClick(View v) {
            asyncLoadThumbImage(gbproduct, imageView, parent);
            return false;
        }

    }

    /**
     * 分享弹出框中listview的适配器
     * 
     * @author qinxudong
     * 
     */
    public class ShareAdapter extends BaseAdapter {

        private List<AppInfo> mList;
        private Context ctx;
        private ShareViewHolder holder;

        public ShareAdapter(Context context, List<AppInfo> list) {
            this.ctx = context;
            this.mList = list;
        }

        public void setList(List<AppInfo> list) {
            this.mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public AppInfo getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ShareViewHolder();
                convertView = View.inflate(ctx, R.layout.share_item, null);
                holder.appTextView = (TextView) convertView.findViewById(R.id.share_name);
                holder.iconImage = (ImageView) convertView.findViewById(R.id.share_icon);
                convertView.setTag(holder);
            } else {
                holder = (ShareViewHolder) convertView.getTag();
            }
            holder.appTextView.setText(getItem(position).getAppName());
            holder.iconImage.setImageDrawable(getItem(position).getAppIcon());
            return convertView;
        }

    }

    /**
     * 静态类
     * 
     * @author qinxudong
     * 
     */
    public static class ShareViewHolder {
        TextView appTextView;
        ImageView iconImage;
    }

}
