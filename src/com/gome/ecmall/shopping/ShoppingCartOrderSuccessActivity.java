package com.gome.ecmall.shopping;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.AlipayUserInfo;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.ShoppingCart;
import com.gome.ecmall.bean.ShoppingCart.OrderPayment;
import com.gome.ecmall.bean.ShoppingCart.OrderSuccess;
import com.gome.ecmall.bean.ShoppingCart.YinLian;
import com.gome.ecmall.bean.ShoppingCart.ZhiFuBao;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.groupbuy.GroupLimitOrderActivity;
import com.gome.ecmall.home.limitbuy.LimitConfirmActivity;
import com.gome.ecmall.home.limitbuy.LimitFlashConfirmActivity;
import com.gome.ecmall.home.mygome.OrderDetailsActivity;
import com.gome.ecmall.util.AppMeasurementUtils;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.ecmall.zhifubao.BaseHelper;
import com.gome.ecmall.zhifubao.MobileSecurePayHelper;
import com.gome.ecmall.zhifubao.MobileSecurePayHelper.UpdateAlipayWalletListener;
import com.gome.ecmall.zhifubao.MobileSecurePayer;
import com.gome.ecmall.zhifubao.ResultChecker;
import com.gome.eshopnew.R;
import com.unionpay.UPPayAssistEx;

/**
 * 订单提交成功
 * 
 * @author bo.yang
 * 
 */
public class ShoppingCartOrderSuccessActivity extends Activity implements OnClickListener {

    private static final String Tag = "ShoppingCartOrderSuccessActivity:";
    // 支付宝
    private static final int PAYMENT_ONLINEPAYMENT_PLAT_ZHIFUBAO = 1;
    // 银联
    private static final int PAYMENT_ONLINEPAYMENT_PLAT_YILIAN = 3;

    private static final String PAYMENTHODETAIL_PAYMENT_ONLINEPAYMENT = "onlinePayment";// 在线支付
    private static final String PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY = "cashOnDelivery";// 货到付款
    private static final String PAYMENTHODETAIL_PAYMENT_VIRTUALACCOUNT = "virtualAccount";// 账户余额支付
    // 银联支付控件状态
    private static final int PLUGIN_NOT_INSTALLED = -1;
    private static final int PLUGIN_NEED_UPGRADE = 2;

    private Button common_title_btn_back, common_title_btn_right, shopping_cart_look_button, shopping_cart_go_button;
    private TextView tvTitle, shopping_goods_order_success_ordernum_data, shopping_goods_order_success_orderprice_data,
            shopping_goods_order_success_paymethod_data, shopping_cart_store_mes;
    private Intent myIntent;
    private static String orderText;
    public static OrderSuccess orderSuccess;
    private Button shopping_goods_order_success_gopayment, shopping_cart_go_help;
    private LinearLayout sumbit_success_button_linearlayout, sumbit_payment_online_linearlayout;
    private RelativeLayout shopping_zhifubao_relative, shopping_yinlian_relative;
    private RadioButton shopping_zhifubao_radiobutton, shopping_yilian_radiobutton, lastRadioButton;
    // 支付宝
    private ProgressDialog mProgress = null;

    private TextView tv_keep_24_hours;// 当时在线支付时在中间显示的提示语

    private TextView tv_shopping_goods_order_success_thanks;// 订单提交成功后提示语

    private String isSelfStore = "N";

    private boolean isPaySuccess = false;// 是否支付成功

    private boolean isPayOnline = false;

    // private GlobalApplication application;
    private AlipayUserInfo alipayUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart_order_success);
        myIntent = getIntent();
        initTitleButton();
        initDataView();
    }

    private void initTitleButton() {
        common_title_btn_back = (Button) findViewById(R.id.common_title_btn_back);
        common_title_btn_back.setVisibility(View.VISIBLE);
        common_title_btn_back.setText(R.string.back);
        common_title_btn_back.setBackgroundResource(R.drawable.common_top_title_btn_bg_selector);
        common_title_btn_back.setOnClickListener(this);
        common_title_btn_right = (Button) findViewById(R.id.common_title_btn_right);
        common_title_btn_right.setVisibility(View.INVISIBLE);
        common_title_btn_right.setText(R.string.shopping_goods_order_success_gopayment);
        common_title_btn_right.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tv_shopping_goods_order_success_thanks = (TextView) findViewById(R.id.shopping_goods_order_success_thanks);
        shopping_goods_order_success_ordernum_data = (TextView) findViewById(R.id.shopping_goods_order_success_ordernum_data);
        shopping_goods_order_success_orderprice_data = (TextView) findViewById(R.id.shopping_goods_order_success_orderprice_data);
        shopping_goods_order_success_paymethod_data = (TextView) findViewById(R.id.shopping_goods_order_success_paymethod_data);
        sumbit_success_button_linearlayout = (LinearLayout) findViewById(R.id.sumbit_success_button_linearlayout);
        tv_keep_24_hours = (TextView) findViewById(R.id.tv_keep_24_hours);
        sumbit_payment_online_linearlayout = (LinearLayout) findViewById(R.id.sumbit_payment_online_linearlayout);
        shopping_goods_order_success_gopayment = (Button) findViewById(R.id.shopping_goods_order_success_gopayment);
        shopping_goods_order_success_gopayment.setOnClickListener(this);
        shopping_zhifubao_relative = (RelativeLayout) findViewById(R.id.shopping_zhifubao_relative);
        shopping_zhifubao_relative.setOnClickListener(this);
        shopping_yinlian_relative = (RelativeLayout) findViewById(R.id.shopping_yinlian_relative);
        shopping_yinlian_relative.setOnClickListener(this);
        shopping_zhifubao_radiobutton = (RadioButton) findViewById(R.id.shopping_zhifubao_radiobutton);
        shopping_zhifubao_radiobutton.setOnClickListener(this);
        shopping_yilian_radiobutton = (RadioButton) findViewById(R.id.shopping_yilian_radiobutton);
        shopping_yilian_radiobutton.setOnClickListener(this);
        // 将默认银联支付方式
        shopping_yilian_radiobutton.setChecked(true);
        lastRadioButton = shopping_yilian_radiobutton;
        shopping_cart_look_button = (Button) findViewById(R.id.shopping_cart_look_button);
        shopping_cart_look_button.setOnClickListener(this);
        shopping_cart_go_button = (Button) findViewById(R.id.shopping_cart_go_button);
        shopping_cart_go_button.setOnClickListener(this);
        shopping_cart_go_help = (Button) findViewById(R.id.shopping_cart_go_help);
        shopping_cart_go_help.setOnClickListener(this);
        shopping_cart_store_mes = (TextView) findViewById(R.id.shopping_cart_store_mes);
        if (myIntent != null && myIntent.getAction() != null) {
            String action = myIntent.getAction();
            if ("OrderDetailsActivity".equals(action)) {
                tvTitle.setText(R.string.shopping_goods_order_payment_onlinepay);
                orderText = getString(R.string.shopping_goods_order_payment_onlinepay);
            } else if ("ShoppingCartOrderActivity".equals(action) || "GroupLimitOrderActivity".equals(action)) {
                ShoppingButtonView.setTotalNumber(0);
                tvTitle.setText(R.string.shopping_goods_order_goods_order_submit);
                orderText = getString(R.string.shopping_goods_order_goods_order_submit);
            }
        } else {
            tvTitle.setText(orderText);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.common_title_btn_back: {
            if (myIntent != null) {
                Intent intent = null;
                String action = myIntent.getAction();
                if ("OrderDetailsActivity".equals(action)) {
                    intent = new Intent(this, OrderDetailsActivity.class);
                    setResult(5, intent);
                } else if ("ShoppingCartOrderActivity".equals(action)) {
                    intent = new Intent(this, ShoppingCartOrderActivity.class);
                    setResult(5, intent);
                } else if ("GroupLimitOrderActivity".equals(action)) {
                    intent = new Intent(this, GroupLimitOrderActivity.class);
                    setResult(5, intent);
                } else if ("LimitConfirmActivity".equals(action)) {
                    intent = new Intent(this, LimitConfirmActivity.class);
                    setResult(5, intent);
                }
                finish();
            }
            // orderSuccess = null;
        }
            break;
        case R.id.common_title_btn_right:
        case R.id.shopping_goods_order_success_gopayment: {
            if (shopping_zhifubao_radiobutton.isChecked()) {
                onlinePayment(PAYMENT_ONLINEPAYMENT_PLAT_ZHIFUBAO);
            } else if (shopping_yilian_radiobutton.isChecked()) {
                onlinePayment(PAYMENT_ONLINEPAYMENT_PLAT_YILIAN);
            }
        }
            break;
        case R.id.shopping_zhifubao_relative:
        case R.id.shopping_zhifubao_radiobutton: {
            shopping_zhifubao_radiobutton.setChecked(true);
            if (lastRadioButton != null && lastRadioButton != shopping_zhifubao_radiobutton) {
                lastRadioButton.setChecked(false);
            }
            lastRadioButton = shopping_zhifubao_radiobutton;
        }
            break;
        case R.id.shopping_yinlian_relative:
        case R.id.shopping_yilian_radiobutton: {
            shopping_yilian_radiobutton.setChecked(true);
            if (lastRadioButton != null && lastRadioButton != shopping_yilian_radiobutton) {
                lastRadioButton.setChecked(false);
            }
            lastRadioButton = shopping_yilian_radiobutton;
        }
            break;
        case R.id.shopping_cart_look_button: {
            if (myIntent != null) {
                Intent intent = null;
                String action = myIntent.getAction();
                if ("OrderDetailsActivity".equals(action)) {
                    intent = new Intent(this, OrderDetailsActivity.class);
                    setResult(5, intent);
                } else if ("ShoppingCartOrderActivity".equals(action)) {
                    intent = new Intent(this, ShoppingCartOrderActivity.class);
                    setResult(4, intent);
                } else if ("GroupLimitOrderActivity".equals(action)) {
                    intent = new Intent(this, GroupLimitOrderActivity.class);
                    setResult(5, intent);
                } else if ("LimitConfirmActivity".equals(action)) {
                    intent = new Intent(this, LimitConfirmActivity.class);
                    setResult(5, intent);
                }
                finish();
            }
            // orderSuccess = null;
            finish();
        }

            break;
        case R.id.shopping_cart_go_button: {
            if (myIntent != null && myIntent.getAction() != null) {
                String action = myIntent.getAction();
                Intent intent = null;
                if ("OrderDetailsActivity".equals(action)) {
                    intent = new Intent(this, OrderDetailsActivity.class);
                } else if ("ShoppingCartOrderActivity".equals(action)) {
                    intent = new Intent(this, ShoppingCartOrderActivity.class);
                } else if ("GroupLimitOrderActivity".equals(action)) {
                    intent = new Intent(this, GroupLimitOrderActivity.class);
                } else if ("LimitConfirmActivity".equals(action)) {
                    intent = new Intent(this, LimitConfirmActivity.class);
                }
                setResult(6, intent);
                finish();
            }

        }
            break;
        case R.id.shopping_cart_go_help: {
            if (myIntent != null && myIntent.getAction() != null) {
                String action = myIntent.getAction();
                Intent intent = null;
                if ("ShoppingCartOrderActivity".equals(action)) {
                    intent = new Intent(this, ShoppingCartOrderActivity.class);
                }
                setResult(9, intent);
                finish();
            }
        }
            break;
        default:
            break;
        }
    }

    private void initDataView() {

        alipayUserInfo = AlipayUserInfo.getInstance();

        if (alipayUserInfo.isAlipayLogin()) {// 支付宝钱包用户，屏蔽银联支付
            shopping_yinlian_relative.setVisibility(View.GONE);
            shopping_zhifubao_radiobutton.setChecked(true);
            shopping_zhifubao_relative.setBackgroundResource(R.drawable.more_item_single_bg_selector);
        } else {
            shopping_yinlian_relative.setVisibility(View.VISIBLE);
            shopping_zhifubao_relative.setBackgroundResource(R.drawable.more_item_last_bg_selector);
        }

        if (myIntent != null) {
            isSelfStore = myIntent.getStringExtra("isSelfStore");// 是否为门店自提
            isSelfStore = isSelfStore == null ? "N" : isSelfStore;
            // 门店自提
            if ("Y".equalsIgnoreCase(isSelfStore)) {
                shopping_cart_go_help.setVisibility(View.VISIBLE);
                shopping_cart_store_mes.setText(Html.fromHtml(getString(R.string.shopping_cart_store_mess)));
            } else {
                shopping_cart_go_help.setVisibility(View.GONE);
            }
            // ......................
            if (orderSuccess == null)
                orderSuccess = (OrderSuccess) myIntent.getSerializableExtra("orderSuccess");
            shopping_goods_order_success_ordernum_data.setText(" " + orderSuccess.getOrderId());
            shopping_goods_order_success_orderprice_data.setText("￥" + orderSuccess.getPayAmount());
            List<OrderPayment> orderpaymentList = orderSuccess.getOrderpaymentList();
            if (orderpaymentList != null && orderpaymentList.size() != 0) {
                OrderPayment orderPayment = orderpaymentList.get(0);
                OrderPayment orderPayment1 = null;
                if (1 < orderpaymentList.size()) {
                    orderPayment1 = orderpaymentList.get(1);
                }
                if (PAYMENTHODETAIL_PAYMENT_ONLINEPAYMENT.equals(orderPayment.getPayModelID())) {
                    isPayOnline = true;
                    if (orderPayment1 != null
                            && PAYMENTHODETAIL_PAYMENT_VIRTUALACCOUNT.equals(orderPayment1.getPayModelID())) {
                        shopping_goods_order_success_paymethod_data.setText(" "
                                + getString(R.string.shopping_goods_order_payment_onlinepay) + "、"
                                + getString(R.string.shopping_goods_order_virtualaccount_pay));
                    } else {
                        shopping_goods_order_success_paymethod_data.setText(" "
                                + getString(R.string.shopping_goods_order_payment_onlinepay));
                    }
                    sumbit_success_button_linearlayout.setVisibility(View.GONE);
                    tv_keep_24_hours.setVisibility(View.VISIBLE);

                    if ("OrderDetailsActivity".equals(myIntent.getAction())) {// 运能提示判断
                        String isFixedtimeOrderStr = myIntent.getStringExtra("isFixedtimeOrderStr");// 是否为运能管理订单
                        if ("Y".equalsIgnoreCase(isFixedtimeOrderStr)) {
                            String setPayTime = myIntent.getStringExtra("setPayTime");
                            if (!TextUtils.isEmpty(setPayTime)) {
                                tv_shopping_goods_order_success_thanks.setText(Html
                                        .fromHtml("<font color=\"#FF6928\">您的订单已提交，为确保订单在您选择的送货时间内送达，请在\"</font>"
                                                + "<font color=\"#CC0000\">" + setPayTime + "</font>"
                                                + "<font color=\"#FF6928\">前付款\"</font>"));
                            } else {
                                tv_shopping_goods_order_success_thanks
                                        .setText(R.string.shopping_goods_order_success_thanks_onlinepayment);
                            }

                        } else {
                            tv_shopping_goods_order_success_thanks
                                    .setText(R.string.shopping_goods_order_success_thanks_onlinepayment);
                        }
                    } else {
                        tv_shopping_goods_order_success_thanks
                                .setText(R.string.shopping_goods_order_success_thanks_onlinepayment);
                    }
                    // tv_shopping_goods_order_success_thanks
                    // .setText(R.string.shopping_goods_order_success_thanks_onlinepayment);
                    sumbit_payment_online_linearlayout.setVisibility(View.VISIBLE);
                    common_title_btn_right.setVisibility(View.VISIBLE);
                } else if (PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY.equals(orderPayment.getPayModelID())) {
                    String subPayModelID = orderPayment.getSubPayModelID();
                    if ("CASH".equals(subPayModelID)) {
                        if (orderPayment1 != null
                                && PAYMENTHODETAIL_PAYMENT_VIRTUALACCOUNT.equals(orderPayment1.getPayModelID())) {
                            shopping_goods_order_success_paymethod_data.setText(" "
                                    + getString(R.string.shopping_goods_order_payment_cashpayment) + "("
                                    + getString(R.string.shopping_goods_order_payment_cash) + ")、"
                                    + getString(R.string.shopping_goods_order_virtualaccount_pay));
                        } else {
                            shopping_goods_order_success_paymethod_data.setText(" "
                                    + getString(R.string.shopping_goods_order_payment_cashpayment) + "("
                                    + getString(R.string.shopping_goods_order_payment_cash) + ")");
                        }
                    } else if ("POS".equals(subPayModelID)) {
                        if (orderPayment1 != null
                                && PAYMENTHODETAIL_PAYMENT_VIRTUALACCOUNT.equals(orderPayment1.getPayModelID())) {
                            shopping_goods_order_success_paymethod_data.setText(" "
                                    + getString(R.string.shopping_goods_order_payment_cashpayment) + "(POS),"
                                    + getString(R.string.shopping_goods_order_virtualaccount_pay));
                        } else {
                            shopping_goods_order_success_paymethod_data.setText(" "
                                    + getString(R.string.shopping_goods_order_payment_cashpayment) + "(POS)");
                        }
                    }
                    sumbit_success_button_linearlayout.setVisibility(View.VISIBLE);
                    tv_keep_24_hours.setVisibility(View.GONE);
                    tv_shopping_goods_order_success_thanks
                            .setText(R.string.shopping_goods_order_success_thanks_cashondelivery);
                    sumbit_payment_online_linearlayout.setVisibility(View.GONE);
                    common_title_btn_right.setVisibility(View.INVISIBLE);
                } else if (PAYMENTHODETAIL_PAYMENT_VIRTUALACCOUNT.equals(orderPayment.getPayModelID())) {
                    if (orderPayment1 != null
                            && PAYMENTHODETAIL_PAYMENT_ONLINEPAYMENT.equals(orderPayment1.getPayModelID())) {
                        shopping_goods_order_success_paymethod_data.setText(" "
                                + getString(R.string.shopping_goods_order_payment_onlinepay) + "、"
                                + getString(R.string.shopping_goods_order_virtualaccount_pay));
                        sumbit_success_button_linearlayout.setVisibility(View.GONE);
                        tv_keep_24_hours.setVisibility(View.VISIBLE);
                        tv_shopping_goods_order_success_thanks
                                .setText(R.string.shopping_goods_order_success_thanks_onlinepayment);
                        sumbit_payment_online_linearlayout.setVisibility(View.VISIBLE);
                        common_title_btn_right.setVisibility(View.VISIBLE);
                    } else if (orderPayment1 != null
                            && PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY.equals(orderPayment1.getPayModelID())) {
                        if ("CASH".equals(orderPayment1.getSubPayModelID())) {
                            shopping_goods_order_success_paymethod_data.setText(" "
                                    + getString(R.string.shopping_goods_order_payment_cashpayment) + "("
                                    + getString(R.string.shopping_goods_order_payment_cash) + ")、"
                                    + getString(R.string.shopping_goods_order_virtualaccount_pay));
                        } else if ("POS".equals(orderPayment1.getSubPayModelID())) {
                            shopping_goods_order_success_paymethod_data.setText(" "
                                    + getString(R.string.shopping_goods_order_payment_cashpayment) + "(POS)、"
                                    + getString(R.string.shopping_goods_order_virtualaccount_pay));
                        }
                        sumbit_success_button_linearlayout.setVisibility(View.VISIBLE);
                        tv_keep_24_hours.setVisibility(View.GONE);
                        tv_shopping_goods_order_success_thanks
                                .setText(R.string.shopping_goods_order_success_thanks_cashondelivery);
                        sumbit_payment_online_linearlayout.setVisibility(View.GONE);
                        common_title_btn_right.setVisibility(View.INVISIBLE);
                    } else {
                        shopping_goods_order_success_paymethod_data.setText(" "
                                + getString(R.string.shopping_goods_order_virtualaccount_pay));
                        sumbit_success_button_linearlayout.setVisibility(View.VISIBLE);
                        tv_keep_24_hours.setVisibility(View.GONE);
                        tv_shopping_goods_order_success_thanks
                                .setText(R.string.shopping_goods_order_success_thanks_cashondelivery);
                        sumbit_payment_online_linearlayout.setVisibility(View.GONE);
                        common_title_btn_right.setVisibility(View.INVISIBLE);
                    }
                }
            }
            if (!"OrderDetailsActivity".equals(myIntent.getAction())) {
                String buyType = "";
                if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.GroupType) {
                    buyType = getString(R.string.appMeas_groupbuy);
                } else if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.LimitType) {
                    buyType = getString(R.string.appMeas_mylimitbuy);
                } else if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.GoodOrderType) {
                    buyType = getString(R.string.appMeas_nomalpay);
                }
                AppMeasurementUtils appMeasurementUtils = new AppMeasurementUtils(ShoppingCartOrderSuccessActivity.this);
                appMeasurementUtils.getUrl(getString(R.string.appMeas_shoppingCartProcess) + ":"
                        + getString(R.string.appMeas_shoppingOrderSuccess),
                        getString(R.string.appMeas_shoppingCartProcess) + ":"
                                + getString(R.string.appMeas_shoppingOrderSuccess),
                        getString(R.string.appMeas_shoppingCartProcess) + ":"
                                + getString(R.string.appMeas_shoppingOrderSuccess),
                        getString(R.string.appMeas_shoppingCartProcess) + ":"
                                + getString(R.string.appMeas_shoppingOrderSuccess), "", buyType,
                        AppMeasurementUtils.EVENT_SHOPPINGORDER_SUCCESS, myIntent.getStringExtra("shoppingCartOctree"),
                        orderSuccess.getOrderId(), orderSuccess.getOrderId(),
                        shopping_goods_order_success_paymethod_data.getText().toString(),
                        getString(R.string.shopping_goods_order_payment_cash_gomeexmp), "", "", "", "", null);
            }

        }
    }

    private void onlinePayment(final int payment_plat_type) {
        if (!NetUtility.isNetworkAvailable(ShoppingCartOrderSuccessActivity.this)) {
            CommonUtility.showMiddleToast(ShoppingCartOrderSuccessActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, Object>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(ShoppingCartOrderSuccessActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected Object doInBackground(Object... params) {
                OrderSuccess orderSuccess = (OrderSuccess) myIntent.getSerializableExtra("orderSuccess");
                String orderId = orderSuccess.getOrderId();
                String body = ShoppingCart.reqOnLinePayment(orderId, payment_plat_type);
                BDebug.e(Tag, body);
                String url = "";
                switch (payment_plat_type) {

                // 支付宝请求数据
                case PAYMENT_ONLINEPAYMENT_PLAT_ZHIFUBAO:
                    url = Constants.URL_ONLINE_ALIPAY;
                    break;
                // 银联请求数据
                case PAYMENT_ONLINEPAYMENT_PLAT_YILIAN:
                    url = Constants.URL_ONLINE_UNIONPAY;
                    break;
                default:
                    break;
                }
                String result = NetUtility.sendHttpRequestByPost(url, body);
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart
                            .setErrorMessage(getString(R.string.can_not_conntect_network_please_check_network_settings));
                    return null;
                }
                return ShoppingCart.paserResponseShoppingCart_Online(result, payment_plat_type);
            };

            protected void onPostExecute(Object obj) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                if (obj == null) {
                    CommonUtility.showMiddleToast(ShoppingCartOrderSuccessActivity.this, "",
                            ShoppingCart.getErrorMessage());
                    return;
                }
                switch (payment_plat_type) {
                // 支付宝
                case PAYMENT_ONLINEPAYMENT_PLAT_ZHIFUBAO:
                    ZhiFuBao zhifubao = (ZhiFuBao) obj;
                    // zhifubaoinfo = "partner=\"" + zhifubao.getPartner() + "\"&seller=\"" + zhifubao.getSeller()
                    // + "\"&out_trade_no=\"" + zhifubao.getOut_trade_no() + "\"&subject=\""
                    // + zhifubao.getSubject() + "\"&body=\"" + zhifubao.getBody() + "\"&total_fee=\""
                    // + zhifubao.getTotal_fee() + "\"&notify_url=\"" + zhifubao.getNotify_url() + "\"&sign=\""
                    // + zhifubao.getSign() + "\"&sign_type=\"" + zhifubao.getSign_type() + "\"";
                    //
                    // BDebug.e(Tag, zhifubaoinfo) ;

                    // 参数信息: 商户ID,账户ID,订单号,商品名称,商品描述,本次支付总金额,商家提供的URL,上述订单签名,签名类型
                    StringBuilder builder = new StringBuilder();
                    builder.append("partner=\"").append(zhifubao.getPartner()).append("\"&seller=\"")
                            .append(zhifubao.getSeller()).append("\"&out_trade_no=\"")
                            .append(zhifubao.getOut_trade_no()).append("\"&subject=\"").append(zhifubao.getSubject())
                            .append("\"&body=\"").append(zhifubao.getBody()).append("\"&total_fee=\"")
                            .append(zhifubao.getTotal_fee()).append("\"&notify_url=\"")
                            .append(zhifubao.getNotify_url());
                    if (!TextUtils.isEmpty(zhifubao.getExtern_token())) {// 值非空才能加入验证签名
                        builder.append("\"&extern_token=\"").append(zhifubao.getExtern_token());
                    }
                    builder.append("\"&sign=\"").append(zhifubao.getSign()).append("\"&sign_type=\"")
                            .append(zhifubao.getSign_type()).append("\"");
                    final String zhifubaoinfo = builder.toString();
                    BDebug.e(Tag, zhifubaoinfo);

                    // 这里传入使用支付宝快捷支付插件包名 或者 支付宝钱包快捷支付包名
                    // 如果是从支付宝钱包启动应用的包名为支付宝钱包
                    // AlipayUserInfo alipayUserInfo = AlipayUserInfo.getInstance();
                    final MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(
                            ShoppingCartOrderSuccessActivity.this);
                    final MobileSecurePayer msp = new MobileSecurePayer();
                    // if (alipayUserInfo.isAlipayLogin()) {
                    // 从支付宝钱包启动的统一使用支付宝钱包进行支付，只有支付宝钱包用户拥有短token
                    mspHelper.setUpdateAlipayWalletListener(new UpdateAlipayWalletListener() {

                        @Override
                        public void updateFinish(String newApkUrl) {
                            if (newApkUrl != null) {// 更新地址不为空的时候，调用支付宝快捷支付服务
                                boolean isMobile_spExist = mspHelper.detectMobile_sp("com.alipay.android.app");
                                if (!isMobile_spExist)
                                    return;
                                boolean bRet = msp.pay(zhifubaoinfo, handler, 0, ShoppingCartOrderSuccessActivity.this,
                                        true);
                                if (bRet) {
                                    // show the progress bar to indicate that we have started
                                    // paying.
                                    closeProgress();
                                    mProgress = BaseHelper.showProgress(ShoppingCartOrderSuccessActivity.this, null,
                                            getString(R.string.shopping_goods_order_payment_ing), false, true);
                                }
                            } else {// 更新地址为空的时候，支付宝钱包为最新版本，调用支付宝钱包进行支付
                                boolean bRet = msp.pay(zhifubaoinfo, handler, 0, ShoppingCartOrderSuccessActivity.this,
                                        false);
                                if (bRet) {
                                    // show the progress bar to indicate that we have started
                                    // paying.
                                    closeProgress();
                                    mProgress = BaseHelper.showProgress(ShoppingCartOrderSuccessActivity.this, null,
                                            getString(R.string.shopping_goods_order_payment_ing), false, true);
                                }
                            }
                        }
                    });
                    mspHelper.detectMobile_sp("com.eg.android.AlipayGphone");
                    // if (!isMobile_spExist)
                    // return;
                    // } else {
                    // // 非支付宝钱包用户没有短token，顾只能使用支付宝快捷支付服务
                    // // start the pay.
                    // boolean isMobile_spExist = mspHelper.detectMobile_sp("com.alipay.android.app");
                    // if (!isMobile_spExist)
                    // return;
                    // boolean bRet = msp.pay(zhifubaoinfo, handler, 0, ShoppingCartOrderSuccessActivity.this, true);
                    // if (bRet) {
                    // // show the progress bar to indicate that we have started
                    // // paying.
                    // closeProgress();
                    // mProgress = BaseHelper.showProgress(ShoppingCartOrderSuccessActivity.this, null,
                    // getString(R.string.shopping_goods_order_payment_ing), false, true);
                    // }
                    // }
                    break;
                // 银联
                case PAYMENT_ONLINEPAYMENT_PLAT_YILIAN:
                    YinLian yilian = (YinLian) obj;
                    // 跳转到SDK页面
                    // Intent intent = new Intent();
                    // intent.putExtra("tradNo", yilian.getTradeNo());
                    // // 第二个参数 payType 接口中未定义，与官方联系，此字段可以不填写
                    // intent.putExtra("payType", 9);
                    // intent.setClass(ShoppingCartOrderSuccessActivity.this, UmpayActivity.class);
                    // startActivityForResult(intent, PAYMENT_ONLINEPAYMENT_PLAT_YILIAN);

                    /*************************************************
                     * 
                     * 步骤2：通过银联工具类启动支付插件
                     * 
                     ************************************************/
                    // mMode参数解释：
                    // 0 - 启动银联正式环境
                    // 1 - 连接银联测试环境
                    int ret = UPPayAssistEx.startPay(ShoppingCartOrderSuccessActivity.this, null, null,
                            yilian.getTradeNo(), Constants.MMODE);
                    if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
                        // 需要重新安装控件
                        BDebug.e(Tag, " plugin not found or need upgrade!!!");

                        AlertDialog.Builder diagle = new AlertDialog.Builder(ShoppingCartOrderSuccessActivity.this);
                        diagle.setTitle(getString(R.string.confirm_install_hint));
                        diagle.setMessage(getString(R.string.confirm_install_uppay));

                        diagle.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent userComintent = new Intent();
                                userComintent.setAction("android.intent.action.VIEW");
                                Uri content_url = Uri
                                        .parse("http://mobile.unionpay.com/getclient?platform=android&type=securepayplugin");
                                userComintent.setData(content_url);
                                ShoppingCartOrderSuccessActivity.this.startActivity(userComintent);
                                dialog.dismiss();
                                // boolean b = UPPayAssistEx.installUPPayPlugin(MainActivity.this);
                            }
                        });

                        diagle.setPositiveButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        diagle.create().show();

                    }
                    BDebug.e(Tag, "" + ret);
                    break;
                default:
                    break;
                }
            }
        }.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeProgress();
    }

    private void closeProgress() {
        try {
            if (mProgress != null) {
                mProgress.dismiss();
                mProgress = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler() {
        public void dispatchMessage(android.os.Message msg) {
            super.dispatchMessage(msg);
            if (msg == null)
                return;
            String strRet = (String) msg.obj;
            BDebug.e(Tag, strRet);
            switch (msg.what) {
            case 0:
                if (TextUtils.isEmpty(strRet))
                    return;
                closeProgress();
                String memo = "memo=";
                int imemoStart = strRet.indexOf("memo=");
                imemoStart += memo.length();
                int imemoEnd = strRet.indexOf(";result=");

                try {
                    memo = strRet.substring(imemoStart, imemoEnd);
                    ResultChecker resultChecker = new ResultChecker(strRet);
                    int retVal = resultChecker.checkSign();
                    if (retVal == ResultChecker.RESULT_CHECK_SIGN_FAILED) {
                        BaseHelper.showDialog(ShoppingCartOrderSuccessActivity.this, "提示",
                                getResources().getString(R.string.check_sign_failed),
                                android.R.drawable.ic_dialog_alert);
                    } else if (resultChecker.isPayOk()) {
                        // 跳转首页
                        // Intent intent = new Intent(
                        // ShoppingCartActivityComplete.this,
                        // HomeListActivity.class);
                        // 跳转到支付成功页面
                        Intent intent = new Intent(ShoppingCartOrderSuccessActivity.this,
                                ShoppingCartOrderPaySuccessActivity.class);
                        intent.putExtra("isSelfStore", myIntent.getStringExtra("isSelfStore"));
                        intent.putExtra("orderSuccess", getIntent().getSerializableExtra("orderSuccess"));
                        intent.putExtra("onlinepaymethods", PAYMENT_ONLINEPAYMENT_PLAT_ZHIFUBAO);
                        startActivityForResult(intent, 0);
                    } else {
                        if (!"{}".equals(memo)) {
                            BaseHelper.showDialog(ShoppingCartOrderSuccessActivity.this, "提示",
                                    memo.substring(1, memo.length() - 1), R.drawable.infoicon);

                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    BaseHelper.showDialog(ShoppingCartOrderSuccessActivity.this, "提示",
                            ShoppingCartOrderSuccessActivity.this.getString(R.string.shopping_cart_pay_error),
                            R.drawable.infoicon);

                } catch (Exception e) {
                    e.printStackTrace();
                    if (!"{}".equals(memo)) {
                        // memo.substring(1, memo.length() - 1)
                        BaseHelper.showDialog(ShoppingCartOrderSuccessActivity.this, "提示",
                                ShoppingCartOrderSuccessActivity.this.getString(R.string.shopping_cart_pay_error),
                                R.drawable.infoicon);
                    }
                }
                break;

            default:
                break;
            }
        };
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (myIntent != null) {
                Intent intent = null;
                String action = myIntent.getAction();
                if ("OrderDetailsActivity".equals(action)) {
                    if (!isPaySuccess && isPayOnline) {
                        CommonUtility.showMiddleToast(this, "", "订单支付失败！");
                    }
                    intent = new Intent(this, OrderDetailsActivity.class);
                    setResult(5, intent);
                } else if ("ShoppingCartOrderActivity".equals(action)) {
                    if (!isPaySuccess && isPayOnline) {
                        CommonUtility.showMiddleToast(this, "", "订单支付失败！");
                    }
                    intent = new Intent(this, ShoppingCartOrderActivity.class);
                    setResult(5, intent);
                } else if ("GroupLimitOrderActivity".equals(action)) {
                    intent = new Intent(this, GroupLimitOrderActivity.class);
                    setResult(5, intent);
                } else if ("LimitConfirmActivity".equals(action)) {
                    intent = new Intent(this, LimitConfirmActivity.class);
                    setResult(5, intent);
                } else if ("LimitFlashConfirmActivity".equals(action)) {
                    intent = new Intent(this, LimitFlashConfirmActivity.class);
                    setResult(5, intent);
                }
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
        case 0: {
            if (myIntent != null) {
                Intent intent = null;
                String action = myIntent.getAction();
                if ("OrderDetailsActivity".equals(action)) {
                    intent = new Intent(this, OrderDetailsActivity.class);
                    setResult(7, intent);
                } else if ("ShoppingCartOrderActivity".equals(action)) {
                    intent = new Intent(this, ShoppingCartOrderActivity.class);
                    setResult(4, intent);
                } else if ("GroupLimitOrderActivity".equals(action)) {
                    intent = new Intent(this, GroupLimitOrderActivity.class);
                    setResult(5, intent);
                } else if ("LimitConfirmActivity".equals(action)) {
                    intent = new Intent(this, LimitConfirmActivity.class);
                    setResult(5, intent);
                }
                finish();
            }
        }
            break;
        case 6: {
            if (myIntent != null) {
                String action = myIntent.getAction();
                if ("OrderDetailsActivity".equals(action) || "ShoppingCartOrderActivity".equals(action)) {
                    Intent intent = new Intent(this, ShoppingCartOrderActivity.class);
                    setResult(6, intent);
                } else if ("GroupLimitOrderActivity".equals(action)) {
                    Intent intent = new Intent(this, GroupLimitOrderActivity.class);
                    setResult(6, intent);
                } else if ("LimitConfirmActivity".equals(action)) {
                    Intent intent = new Intent(this, LimitConfirmActivity.class);
                    setResult(6, intent);
                }
            }
            // orderSuccess = null;
            finish();
        }
            break;
        case 9: {
            if (myIntent != null) {
                String action = myIntent.getAction();
                if ("ShoppingCartOrderActivity".equals(action)) {
                    Intent intent = new Intent(this, ShoppingCartOrderActivity.class);
                    setResult(9, intent);
                } else if ("OrderDetailsActivity".equals(action)) {
                    Intent intent = new Intent(this, OrderDetailsActivity.class);
                    setResult(9, intent);
                }
            }
            finish();
        }
            break;
        // 银联支付页面返回结果 返回固定resultCode 8888
        // case 88888: {
        // if (requestCode == PAYMENT_ONLINEPAYMENT_PLAT_YILIAN) {
        // String msg = data.getStringExtra("resultMessage");
        // String code = data.getStringExtra("resultCode");
        //
        // if ("0000".equals(code)) {
        // Intent intent = new Intent(ShoppingCartOrderSuccessActivity.this,
        // ShoppingCartOrderPaySuccessActivity.class);
        // intent.putExtra("isSelfStore", myIntent.getStringExtra("isSelfStore"));
        // intent.putExtra("orderSuccess", getIntent().getSerializableExtra("orderSuccess"));
        // intent.putExtra("onlinepaymethods", PAYMENT_ONLINEPAYMENT_PLAT_YILIAN);
        // startActivityForResult(intent, 0);
        // } else if ("1001".equals(code)) {
        // CommonUtility.showMiddleToast(this, "", msg);
        // } else if ("1002".equals(code)) {
        // CommonUtility.showMiddleToast(this, "", msg);
        // }
        // }
        // }
        }
        
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        if(data == null){
            return;
        }
        String str = data.getStringExtra("pay_result");

        if ("success".equals(str)) {
            Intent intent = new Intent(ShoppingCartOrderSuccessActivity.this, ShoppingCartOrderPaySuccessActivity.class);
            intent.putExtra("isSelfStore", myIntent.getStringExtra("isSelfStore"));
            intent.putExtra("orderSuccess", getIntent().getSerializableExtra("orderSuccess"));
            intent.putExtra("onlinepaymethods", PAYMENT_ONLINEPAYMENT_PLAT_YILIAN);
            startActivityForResult(intent, 0);
        } else if ("fail".equals(str)) {
            CommonUtility.showMiddleToast(this, "", getString(R.string.shopping_cart_pay_error));
        } else if ("cancel".equals(str)) {
            CommonUtility.showMiddleToast(this, "", getString(R.string.shopping_cart_cancel_pay));
        }
    }
}
