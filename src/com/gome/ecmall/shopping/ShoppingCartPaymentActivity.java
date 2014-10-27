package com.gome.ecmall.shopping;

import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.ShoppingCart;
import com.gome.ecmall.bean.ShoppingCart.PaymentDetail_paymentMethods;
import com.gome.ecmall.bean.ShoppingCart.ShoppintCart_payMentList;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.groupbuy.GroupLimitOrderActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 支付方式
 * 
 * @author bo.yang
 * 
 */
public class ShoppingCartPaymentActivity extends Activity implements OnClickListener {

    private static final String Tag = "ShoppingCartPaymentActivity:";
    private static final String PAYMETHODDETAIL_SHIPPINGTIME_WEEKDAY = "WEEKDAY"; // 只工作日送货(双休日、假日不用送)
    // 支付方式标识
    private static final String PAYMENTHODETAIL_PAYMENT_ONLINEPAYMENT = "onlinePayment";// 在线支付
    private static final String PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY = "cashOnDelivery";// 货到付款
    // 货到付款 (现金orPOS机)
    private static final String PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY_POS = "POS"; // pos机刷卡
    private static final String PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY_CASH = "CASH";// 现金
    private Button common_title_btn_back, common_title_btn_right;
    private TextView tvTitle;
    private ImageView pos_img, cash_img;
    private RadioButton shopping_online_radiobutton, shopping_cash_radiobutton;
    private RelativeLayout pos_relaitve, cash_relaitve, payment_relative_online, payment_relative_cash;
    private String paymentMethod = PAYMENTHODETAIL_PAYMENT_ONLINEPAYMENT;
    private String subPaymentMethod = PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY_CASH;
    private String shippingTime = "ALL";
    private String telBefShipping = "0";
    private String isSupportCash;// 该订单是否支持现金支付
    private boolean isSupportCashOnDelivery; // 订单是否支持货到付款

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart_payment);
        initTitleButton();
        ShoppintCart_payMentList paymentlist = (ShoppintCart_payMentList) getIntent().getSerializableExtra(
                "paymentlist");
        initViewData(paymentlist);
    }

    private void initTitleButton() {
        common_title_btn_back = (Button) findViewById(R.id.common_title_btn_back);
        common_title_btn_back.setVisibility(View.VISIBLE);
        common_title_btn_back.setText(R.string.back);
        common_title_btn_back.setBackgroundResource(R.drawable.common_top_title_btn_bg_selector);
        common_title_btn_back.setOnClickListener(this);
        common_title_btn_right = (Button) findViewById(R.id.common_title_btn_right);
        common_title_btn_right.setVisibility(View.VISIBLE);
        common_title_btn_right.setText(R.string.mygome_edit_completed);
        common_title_btn_right.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.shopping_goods_pay_method_null);
        pos_relaitve = (RelativeLayout) findViewById(R.id.pos_relaitve);
        pos_relaitve.setOnClickListener(this);
        cash_relaitve = (RelativeLayout) findViewById(R.id.cash_relaitve);
        cash_relaitve.setOnClickListener(this);
        shopping_online_radiobutton = (RadioButton) findViewById(R.id.shopping_online_radiobutton);
        shopping_online_radiobutton.setOnClickListener(this);
        shopping_cash_radiobutton = (RadioButton) findViewById(R.id.shopping_cash_radiobutton);
        shopping_cash_radiobutton.setOnClickListener(this);
        pos_img = (ImageView) findViewById(R.id.pos_img);
        cash_img = (ImageView) findViewById(R.id.cash_img);
        payment_relative_online = (RelativeLayout) findViewById(R.id.payment_relative_online);
        payment_relative_online.setOnClickListener(this);
        payment_relative_cash = (RelativeLayout) findViewById(R.id.payment_relative_cash);
        payment_relative_cash.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.common_title_btn_right:
            if (shopping_cash_radiobutton.isChecked() && !pos_img.isShown() && !cash_img.isShown()) {
                CommonUtility.showMiddleToast(ShoppingCartPaymentActivity.this, "",
                        getString(R.string.shopping_goods_order_payment_cash_nullsubpayment));
                return;
            }
            if (!isSupportCashOnDelivery && shopping_cash_radiobutton.isChecked()) {
                CommonUtility.showMiddleToast(ShoppingCartPaymentActivity.this, "",
                        getString(R.string.shopping_goods_order_no_supportcashondelivery));
                return;
            }
            if (cash_img.isShown() && "N".equalsIgnoreCase(isSupportCash)) {
                CommonUtility.showMiddleToast(ShoppingCartPaymentActivity.this, "",
                        getString(R.string.shopping_goods_order_conpon_no_issupportcash));
                return;
            }
            savePayment();
            break;
        case R.id.common_title_btn_back:
            ShoppingCartOrderActivity.isBackKeyRefer = true;
            GroupLimitOrderActivity.isBackKeyRefer = true;
            finish();
            break;
        case R.id.pos_relaitve:
            shopping_online_radiobutton.setChecked(false);
            shopping_cash_radiobutton.setChecked(true);
            pos_img.setVisibility(View.VISIBLE);
            cash_img.setVisibility(View.GONE);
            break;
        case R.id.cash_relaitve:
            shopping_online_radiobutton.setChecked(false);
            shopping_cash_radiobutton.setChecked(true);
            pos_img.setVisibility(View.GONE);
            cash_img.setVisibility(View.VISIBLE);
            break;
        case R.id.payment_relative_online:
        case R.id.shopping_online_radiobutton:
            shopping_online_radiobutton.setChecked(true);
            shopping_cash_radiobutton.setChecked(false);
            pos_img.setVisibility(View.GONE);
            cash_img.setVisibility(View.GONE);
            break;
        case R.id.payment_relative_cash:
        case R.id.shopping_cash_radiobutton:
            shopping_online_radiobutton.setChecked(false);
            shopping_cash_radiobutton.setChecked(true);
            break;
        default:
            break;
        }
    }

    private void initViewData(ShoppintCart_payMentList paymentlist) {
        shippingTime = paymentlist.getShippingTime();
        if (TextUtils.isEmpty(shippingTime)) {
            shippingTime = PAYMETHODDETAIL_SHIPPINGTIME_WEEKDAY;
        }
        if ("true".equalsIgnoreCase(paymentlist.getTelBefShipping())) {
            telBefShipping = "1";
        } else if ("false".equalsIgnoreCase(paymentlist.getTelBefShipping())) {
            telBefShipping = "0";
        }
        List<PaymentDetail_paymentMethods> curretnpaymentMethodslist = paymentlist.getCurrentPaymentMethodsList();
        List<PaymentDetail_paymentMethods> selepaymentMethodslist = paymentlist.getPaymentMethodsSelectList();
        if (curretnpaymentMethodslist != null) {
            for (int i = 0, size = curretnpaymentMethodslist.size(); i < size; i++) {
                PaymentDetail_paymentMethods currentpaymentMethod = curretnpaymentMethodslist.get(i);
                if (currentpaymentMethod == null)
                    break;
                if (PAYMENTHODETAIL_PAYMENT_ONLINEPAYMENT.equals(currentpaymentMethod.getPaymentMethod())) {
                    shopping_online_radiobutton.setChecked(true);
                    shopping_cash_radiobutton.setChecked(false);
                } else if (PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY.equals(currentpaymentMethod.getPaymentMethod())) {
                    String posCash = currentpaymentMethod.getPosOrCash();
                    shopping_online_radiobutton.setChecked(false);
                    shopping_cash_radiobutton.setChecked(true);
                    if (PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY_POS.equals(posCash)) {
                        pos_img.setVisibility(View.VISIBLE);
                        cash_img.setVisibility(View.GONE);
                    } else if (PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY_CASH.equals(posCash)) {
                        pos_img.setVisibility(View.GONE);
                        cash_img.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {
            // 默认在线支付
            shopping_online_radiobutton.setChecked(true);
            shopping_cash_radiobutton.setChecked(false);
        }
        if (selepaymentMethodslist != null) {
            for (int i = 0, size = selepaymentMethodslist.size(); i < size; i++) {
                PaymentDetail_paymentMethods selectpaymentMethod = selepaymentMethodslist.get(i);
                if (selectpaymentMethod == null)
                    break;
                if (PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY.equals(selectpaymentMethod.getPaymentMethod())) {
                    isSupportCash = selectpaymentMethod.getIsSupportCash();
                    isSupportCashOnDelivery = true;
                    return;
                }
            }
        }
        if (!isSupportCashOnDelivery) {
            payment_relative_cash.setVisibility(View.GONE);
            pos_relaitve.setVisibility(View.GONE);
            cash_relaitve.setVisibility(View.GONE);
        }
    }

    private void savePayment() {
        if (!NetUtility.isNetworkAvailable(ShoppingCartPaymentActivity.this)) {
            CommonUtility.showMiddleToast(ShoppingCartPaymentActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, Object[]>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(ShoppingCartPaymentActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected Object[] doInBackground(Object... params) {
                // BDebug.d(TAG, json);
                if (shopping_online_radiobutton.isChecked()) {
                    paymentMethod = PAYMENTHODETAIL_PAYMENT_ONLINEPAYMENT;
                    subPaymentMethod = "";
                } else if (shopping_cash_radiobutton.isChecked()) {
                    paymentMethod = PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY;
                    if (pos_img.isShown()) {
                        subPaymentMethod = PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY_POS;
                    } else if (cash_img.isShown()) {
                        subPaymentMethod = PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY_CASH;
                    }

                }
                // start
                String myBody = ShoppingCart.saveOrModifyNormalPayMentInfo(paymentMethod, subPaymentMethod);
                // end
                String result = NetUtility.NO_CONN;
                if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.GroupType) {
                    result = NetUtility.sendHttpRequestByPost(Constants.URL_GROUPON_SAVEPAYANDELIVERY, myBody);
                } else if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.LimitType) {
                    result = NetUtility.sendHttpRequestByPost(Constants.URL_RUSHBUY_CHECKOUT_SAVE_PAY_AND_DELIVERY,
                            myBody);
                } else if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.GoodOrderType) {
                    // 普通购物车商品
                    result = NetUtility.sendHttpRequestByPost(Constants.URL_CHECKOUT_SAVE_PAY_DELIVERY, myBody);
                }
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
                    return null;
                }
                return ShoppingCart.paserResponseShoppingCart_Address_del(result);
            };

            protected void onPostExecute(Object[] objs) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                if (objs == null) {
                    CommonUtility.showMiddleToast(ShoppingCartPaymentActivity.this, "", ShoppingCart.getErrorMessage());
                    return;
                }
                if ((Boolean) objs[0] == null) {
                    CommonUtility.showToast(ShoppingCartPaymentActivity.this,
                            getString(R.string.can_not_conntect_network_please_check_network_settings));
                    return;
                }
                if ((Boolean) objs[0]) {
                    if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.GroupType
                            || GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.LimitType) {
                        Intent intent = new Intent(getApplicationContext(), GroupLimitOrderActivity.class);
                        setResult(1, intent);
                        finish();
                    } else if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.GoodOrderType) {
                        Intent intent = new Intent(getApplicationContext(), ShoppingCartOrderActivity.class);
                        setResult(1, intent);
                        finish();
                    }
                } else if (!(Boolean) objs[0] && objs.length == 2) {
                    CommonUtility.showAlertDialog(ShoppingCartPaymentActivity.this, "", (String) objs[1],
                            getString(R.string.confirm), rightListener);
                }
            };
        }.execute();
    }

    private android.content.DialogInterface.OnClickListener rightListener = new android.content.DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            dialog.cancel();
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ShoppingCartOrderActivity.isBackKeyRefer = true;
            GroupLimitOrderActivity.isBackKeyRefer = true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
