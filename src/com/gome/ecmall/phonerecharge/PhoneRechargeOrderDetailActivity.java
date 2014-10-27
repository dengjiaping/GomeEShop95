package com.gome.ecmall.phonerecharge;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.PhoneRecharge;
import com.gome.ecmall.bean.PhoneRechargeOrderDetail;
import com.gome.ecmall.bean.PhoneRechargeOrderDetail.OrderTraces;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.ecmall.util.PreferenceUtils;
import com.gome.eshopnew.R;

/**
 * @author qinxudong 手机充值订单详情页
 */
public class PhoneRechargeOrderDetailActivity extends Activity implements OnClickListener {

    private static final String TAG = "PhoneRechargeOrderDetailActivity";

    /** 订单编号 */
    private TextView phoneOrderNum;
    /** 订单状态 */
    private TextView phoneOrderState;
    /** 订单描述 */
    // private TextView phoneOrderDescribe;
    /** 电话号码 */
    private TextView phoneOrderPhoneNum;
    /** 充值面额 */
    private TextView phoneOrderDenomination;
    /** 支付方式 */
    private TextView phoneOrderPayMethod;
    /** 下单时间 */
    private TextView phoneOrderTime;
    /** 商品编号 */
    private TextView phoneOrderProductNum;
    /** 商品名称 */
    private TextView phoneOrderProductName;
    /** 国美价 */
    private TextView phoneOrderGomePrice;
    /** 订单优惠 */
    private TextView phoneOrderFavorable;
    /** 订单优惠内容 */
    private TextView phoneOrderFavorableContent;
    /** 商品金额 */
    private TextView phoneOrderProductPrice;
    /** 余额支付 */
    private TextView phoneOrderVirtualaccountPay;
    /** 优惠金额 */
    private TextView phoneOrderProm;
    /** 订单金额 */
    private TextView phoneOrderPrice;
    // private Button phoneOrderBtn;
    /** 订单优惠布局 */
    private LinearLayout favorableLayout;
    /** 标题左侧按钮 */
    private Button title_left_btn;
    /** 标题 */
    private TextView titleTextView;
    /** 标题右侧按钮 */
    private Button title_right_btn;
    /** 手机充值订单详情对象 */
    private PhoneRechargeOrderDetail detail;
    /** 用户id profileId */
    private String profileId;
    /** 订单id orderId */
    private String orderId;
    private ScrollView scrollView;
    private ImageView imageView;
    /** 网络标识 */
    private boolean isNetWork;
    /**
     * 订单状态 充值处理中：0 ；等待支付：1 ；订单取消：2 ；充值成功：3 ； 充值失败：4
     */
    private String orderState;
    private LinearLayout toPayLayout;
    private TextView toPayText;
    private Button toPayBtn;

    private TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.phone_recharge_order_detail);
        initView();
        getData();
    }

    @Override
    protected void onDestroy() {
        detail = null;
        super.onDestroy();
    }

    /**
     * 初始化视图
     */
    void initView() {
        title_left_btn = (Button) findViewById(R.id.common_title_btn_back);
        title_left_btn.setOnClickListener(this);
        titleTextView = (TextView) findViewById(R.id.common_title_tv_text);
        title_right_btn = (Button) findViewById(R.id.common_title_btn_right);
        title_left_btn.setText(R.string.back);
        title_left_btn.setVisibility(View.VISIBLE);
        titleTextView.setVisibility(View.VISIBLE);
        titleTextView.setText(getString(R.string.order_details));
        title_right_btn.setVisibility(View.VISIBLE);
        title_right_btn.setText(R.string.phone_recharge_common_question);
        title_right_btn.setOnClickListener(this);

        empty = (TextView) findViewById(R.id.empty);

        scrollView = (ScrollView) findViewById(R.id.phone_recharge_scrollview);
        toPayLayout = (LinearLayout) findViewById(R.id.to_pay_layout);
        toPayBtn = (Button) findViewById(R.id.phone_order_btn_to_pay);
        toPayText = (TextView) findViewById(R.id.to_pay_text);
        imageView = (ImageView) findViewById(R.id.no_network_img);
        phoneOrderNum = (TextView) findViewById(R.id.phone_orderNum);
        phoneOrderState = (TextView) findViewById(R.id.phone_orderState);
        phoneOrderPhoneNum = (TextView) findViewById(R.id.phone_recharge_phone_num);
        phoneOrderDenomination = (TextView) findViewById(R.id.phone_recharge_order_amount);
        phoneOrderPayMethod = (TextView) findViewById(R.id.phone_recharge_order_payMethod);
        phoneOrderTime = (TextView) findViewById(R.id.phone_recharge_order_time);
        phoneOrderProductNum = (TextView) findViewById(R.id.phone_recharge_order_productNum);
        phoneOrderProductName = (TextView) findViewById(R.id.phone_recharge_order_productName);
        phoneOrderGomePrice = (TextView) findViewById(R.id.phone_recharge_order_gomePrice);
        phoneOrderFavorable = (TextView) findViewById(R.id.phone_recharge_favorable);
        phoneOrderFavorableContent = (TextView) findViewById(R.id.phone_recharge_favorable_content);
        phoneOrderProductPrice = (TextView) findViewById(R.id.phone_recharge_order_productAmount);
        phoneOrderVirtualaccountPay = (TextView) findViewById(R.id.phone_recharge_order_virtualaccount_pay);
        phoneOrderProm = (TextView) findViewById(R.id.phone_recharge_order_prom);
        phoneOrderPrice = (TextView) findViewById(R.id.phone_recharge_order_price);
        favorableLayout = (LinearLayout) findViewById(R.id.phone_recharge_layout);
        toPayBtn.setOnClickListener(this);
    }

    /**
     * 数据异步加载
     */
    void getData() {

        Intent intent = getIntent();
        orderId = intent.getStringExtra(JsonInterface.JK_ORDER_ID_LOW);
        profileId = PreferenceUtils.getInstance(getApplicationContext()).getString(JsonInterface.JK_PROFILE_ID, "");
        // profileId = "44580000" ;
        new AsyncTask<Void, Void, PhoneRechargeOrderDetail>() {
            LoadingDialog dialog = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (!NetUtility.isNetworkAvailable(PhoneRechargeOrderDetailActivity.this)) {
                    imageView.setVisibility(View.VISIBLE);
                    isNetWork = false;
                    return;
                } else {
                    isNetWork = true;
                }

                dialog = CommonUtility.showLoadingDialog(PhoneRechargeOrderDetailActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                dialog.cancel();
                            }
                        });
            }

            @Override
            protected PhoneRechargeOrderDetail doInBackground(Void... params) {
                String orderDetailJson = PhoneRechargeOrderDetail.createOrderDetailJson(profileId, orderId);
                String response = NetUtility.sendHttpRequestByPost(Constants.PHONE_RECHARGE_ORDER_DETAIL_URL,
                        orderDetailJson);
                BDebug.e(TAG, response);
                return PhoneRechargeOrderDetail.parsePhoneRechargeOrderDetail(response);
            }

            @Override
            protected void onPostExecute(PhoneRechargeOrderDetail result) {
                super.onPostExecute(result);
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                detail = result;
                bindData();
            }

        }.execute();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            Intent intent = new Intent();
            intent.putExtra(GlobalConfig.GOHOME, GlobalConfig.GO_HOME);
            setResult(2, intent);
            this.finish();
        }
    }

    /**
     * 视图数据绑定
     */
    void bindData() {

        if (!isNetWork) {
            scrollView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
            return;
        }

        if (detail == null && isNetWork) {
            CommonUtility.showMiddleToast(PhoneRechargeOrderDetailActivity.this, null,
                    getString(R.string.data_load_fail_exception));
            scrollView.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            return;
        }

        scrollView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.GONE);
        empty.setVisibility(View.GONE);
        orderState = detail.getPhoneRechargeOrderClientStatu();
        String describe = detail.getOrderTracesList().get(0).getDealValue();
        OrderTraces traces = detail.getOrderTracesList().get(0);
        describe = traces.getDealTime() + "   " + traces.getDealValue();
        if (orderState.equals("3")) {// 支付成功，再购买
            toPayLayout.setVisibility(View.VISIBLE);
            toPayText.setText(describe);
            toPayBtn.setVisibility(View.VISIBLE);
            toPayBtn.setText(R.string.phone_recharge_re_purchase);
            toPayBtn.setTextColor(Color.BLACK);
            toPayBtn.setBackgroundResource(R.drawable.phone_recharge_order_detail_btn_selector);
        } else if (orderState.equals("2")) {// 订单取消
            toPayLayout.setVisibility(View.VISIBLE);
            toPayBtn.setVisibility(View.GONE);
            toPayText.setText(describe);
        } else if (orderState.equals("4")) {// 支付失败
            toPayLayout.setVisibility(View.VISIBLE);
            toPayBtn.setVisibility(View.GONE);
            toPayText.setText(describe);
        } else if (orderState.equals("1")) {// 待支付，去支付
            toPayLayout.setVisibility(View.VISIBLE);
            toPayText.setText(describe);
            toPayBtn.setText(R.string.phone_recharge_wait);
            toPayBtn.setVisibility(View.VISIBLE);
            toPayBtn.setTextColor(Color.WHITE);
            toPayBtn.setBackgroundResource(R.drawable.phone_recharge_order_detail_to_pay_selector);
        } else if (orderState.equals("0")) {// 正在处理中
            toPayLayout.setVisibility(View.VISIBLE);
            toPayBtn.setVisibility(View.GONE);
            toPayText.setText(describe);
        }

        phoneOrderDenomination.setText(detail.getPhoneRechargeOrderProductPrice());
        phoneOrderFavorable.setText(detail.getPhoneRechargeOrderFavorable());
        phoneOrderFavorableContent.setText(detail.getPhoneRechargeOrderFavorableContent());
        phoneOrderGomePrice.setText(detail.getPhoneRechargeOrderAmount());
        phoneOrderNum.setText(detail.getPhoneRechargeOrderNum());
        phoneOrderPayMethod.setText(detail.getPhoneRechargeOrderPayMethod());
        phoneOrderPhoneNum.setText(detail.getPhoneRechargeOrderPhoneNum() + "(" + detail.getAreaOperators() + ")");
        phoneOrderPrice.setText(detail.getPhoneRechargeOrderAmount());
        phoneOrderProductName.setText(detail.getPhoneRechargeOrderProductName());
        phoneOrderProductNum.setText(detail.getPhoneRechargeOrderProductNum());
        phoneOrderProductPrice.setText(detail.getPhoneRechargeOrderProductPrice());
        phoneOrderProm.setText(detail.getPhoneRechargeOrderProm());
        phoneOrderState.setText(detail.getPhoneRechargeOrderState());
        phoneOrderTime.setText(detail.getPhoneRechargeOrderTime());
        phoneOrderVirtualaccountPay.setText(detail.getPhoneRechargeOrderVirtualaccountPay());

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        if (v == title_left_btn) {
            // setResult(2);
            this.finish();

        } else if (v == toPayBtn) {
            if (orderState.equals("1")) {// 待支付
                intent.setClass(PhoneRechargeOrderDetailActivity.this, PhoneRechargeOrderSubmitSuccessActivity.class);
                intent.putExtra("orderNum", detail.getPhoneRechargeOrderNum());
                String price = detail.getPhoneRechargeOrderAmount();
                price = price.replace("￥", "");
                intent.putExtra("payMoney", price);
                intent.putExtra("num", detail.getPhoneRechargeOrderPhoneNum());
                intent.putExtra(PhoneRecharge.FROMPAGE, "PhoneRechargeOrderDetailActivity");
                startActivityForResult(intent, GlobalConfig.GO_TO_HOME);
            } else if (orderState.equals("3")) {// 再次购买
                intent.setClass(PhoneRechargeOrderDetailActivity.this, PhoneRechargeActivity.class);
                startActivityForResult(intent, GlobalConfig.GO_TO_HOME);
            }

        } else if (v == title_right_btn) {
            intent.setClass(PhoneRechargeOrderDetailActivity.this, PhoneRechargeHelpActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // setResult(2);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
