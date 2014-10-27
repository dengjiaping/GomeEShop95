package com.gome.ecmall.home.limitbuy;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.MoreGomeStore.Store;
import com.gome.ecmall.bean.ShoppingCart;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_Recently_paymentDetail;
import com.gome.ecmall.bean.shippingInfo.ShippingMethod;
import com.gome.ecmall.custom.DisScrollListView;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.GlobalApplication;
import com.gome.ecmall.home.groupbuy.GroupLimitOrderActivity;
import com.gome.ecmall.shopping.ShoppingCartOrderActivity;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 配送方式
 * 
 * @author bo.yang
 * 
 */
public class LimitbuyShippingMethodActivity extends Activity implements OnClickListener {

    private static final String Tag = "ShoppingCartShippingMethodActivity:";
    // 支付方式标识
    private static final String PAYMENTHODETAIL_PAYMENT_ONLINEPAYMENT = "Gome Express";// 配送方式
    private static final String PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY = "cashOnDelivery";// 货到付款
    // 货到付款 (现金orPOS机)
    private static final String PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY_POS = "POS"; // pos机刷卡
    private static final String PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY_CASH = "CASH";// 现金
    // 送货时间
    private static final String PAYMETHODDETAIL_SHIPPINGTIME_WEEKDAY = "WEEKDAY"; // 只工作日送货(双休日、假日不用送)
    private static final String PAYMETHODDETAIL_SHIPPINGTIME_WEEKEND = "WEEKEND"; // 只双休日、假日送货(工作日不用送)
    private static final String PAYMETHODDETAIL_SHIPPINGTIME_ALL = "ALL"; // 工作日、双休日与假日均可送货
    private Button common_title_btn_back, common_title_btn_right;
    private TextView tvTitle;
    private RadioButton shopping_weekday_radiobutton, shopping_weekend_radiobutton, shopping_all_radiobutton,
            shopping_phone_confirm_yes_radiobutton, shopping_phone_confirm_no_radiobutton;
    private RelativeLayout shopping_weekday_relative, shopping_weekend_relative, shopping_all_relative,
            shopping_isneedconfirm_yes_relative, shopping_isneedconfirm_no_relative;
    private String paymentMethod = PAYMENTHODETAIL_PAYMENT_ONLINEPAYMENT;
    private String shippingTime = "ALL";
    private String telBefShipping = "0";
    private RadioButton lastShippingTimeRadioButton;
    private RadioButton lastIsneedConfirmRadioButton;
    private String shippingId = "";
    private String isGome = "N";
    private Context mContext;
    private ListView lvShipping;
    private RelativeLayout rlShipping;
    private List<ShippingMethod> smList;
    // 店铺自取
    private ArrayList<Store> mStoreList = null;
    private DisScrollListView mStoreListView;
    private LinearLayout mStoreLineLayout, mNormalLineLayout;
    private TextView mStoreTvTitle;
    private TextView mStoreTvContent;
    private boolean isStore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.shopping_cart_shippingmethod);
        initTitleButton();
        // 非普通购物车商品
        rlShipping.setVisibility(View.VISIBLE);
        goToPaymentData();
        GlobalApplication.screenManager.pushActivity(this);
    }

    private void initTitleButton() {
        common_title_btn_back = (Button) findViewById(R.id.common_title_btn_back);
        common_title_btn_back.setVisibility(View.VISIBLE);
        common_title_btn_back.setText(R.string.back);
        common_title_btn_back.setBackgroundResource(R.drawable.common_top_title_btn_bg_selector);
        common_title_btn_back.setOnClickListener(this);
        common_title_btn_right = (Button) findViewById(R.id.common_title_btn_right);
        common_title_btn_right.setVisibility(View.VISIBLE);
        common_title_btn_right.setText(R.string.groupbuy_next);
        common_title_btn_right.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.shopping_goods_distribution_method_null);
        shopping_weekday_relative = (RelativeLayout) findViewById(R.id.shopping_weekday_relative);
        shopping_weekday_relative.setOnClickListener(this);
        shopping_weekend_relative = (RelativeLayout) findViewById(R.id.shopping_weekend_relative);
        shopping_weekend_relative.setOnClickListener(this);
        shopping_all_relative = (RelativeLayout) findViewById(R.id.shopping_all_relative);
        shopping_all_relative.setOnClickListener(this);
        shopping_isneedconfirm_yes_relative = (RelativeLayout) findViewById(R.id.shopping_isneedconfirm_yes_relative);
        shopping_isneedconfirm_yes_relative.setOnClickListener(this);
        shopping_isneedconfirm_no_relative = (RelativeLayout) findViewById(R.id.shopping_isneedconfirm_no_relative);
        shopping_isneedconfirm_no_relative.setOnClickListener(this);
        shopping_weekday_radiobutton = (RadioButton) findViewById(R.id.shopping_weekday_radiobutton);
        shopping_weekday_radiobutton.setOnClickListener(this);
        lastShippingTimeRadioButton = shopping_weekday_radiobutton;
        shopping_weekend_radiobutton = (RadioButton) findViewById(R.id.shopping_weekend_radiobutton);
        shopping_weekend_radiobutton.setOnClickListener(this);
        shopping_all_radiobutton = (RadioButton) findViewById(R.id.shopping_all_radiobutton);
        shopping_all_radiobutton.setOnClickListener(this);
        shopping_phone_confirm_yes_radiobutton = (RadioButton) findViewById(R.id.shopping_phone_confirm_yes_radiobutton);
        shopping_phone_confirm_yes_radiobutton.setOnClickListener(this);
        lastIsneedConfirmRadioButton = shopping_phone_confirm_yes_radiobutton;
        shopping_phone_confirm_no_radiobutton = (RadioButton) findViewById(R.id.shopping_phone_confirm_no_radiobutton);
        shopping_phone_confirm_no_radiobutton.setOnClickListener(this);
        lvShipping = (ListView) findViewById(R.id.shopping_cart_shipping_lv);
        rlShipping = (RelativeLayout) findViewById(R.id.shopping_cart_shipping_rl);
        // 店铺自取
        mStoreListView = (DisScrollListView) findViewById(R.id.shopping_shipping_store_listview);
        mStoreLineLayout = (LinearLayout) findViewById(R.id.shopping_shipping_store_layout);
        mNormalLineLayout = (LinearLayout) findViewById(R.id.shopping_shipping_normal_layout);
        mStoreTvTitle = (TextView) findViewById(R.id.shopping__shipping_store_time_title);
        mStoreTvContent = (TextView) findViewById(R.id.shopping__shipping_store_time_content);
    }

    /**
     * 配送方式
     * 
     * @param paymentShippingType
     */
    private void goToPaymentData() {
        if (!NetUtility.isNetworkAvailable(this)) {
            CommonUtility.showMiddleToast(this, "",
                    this.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, ShoppingCart_Recently_paymentDetail>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(LimitbuyShippingMethodActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected ShoppingCart_Recently_paymentDetail doInBackground(Object... params) {
                String result = NetUtility.NO_CONN;
                result = NetUtility.sendHttpRequestByGet(Constants.URL_RUSHBUY_FLASHBUY_CONFIG_INFO);
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
                    return null;
                }
                return ShoppingCart.paserResponseShoppintCart_shippingMent(result);
            };

            protected void onPostExecute(ShoppingCart_Recently_paymentDetail paymentlist) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                if (paymentlist == null) {
                    CommonUtility.showMiddleToast(LimitbuyShippingMethodActivity.this, "",
                            ShoppingCart.getErrorMessage());
                    return;
                }

                initViewData(paymentlist);

            };
        }.execute();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.common_title_btn_right:
            if (!shopping_weekday_radiobutton.isChecked() && !shopping_weekend_radiobutton.isChecked()
                    && !shopping_all_radiobutton.isChecked()) {
                CommonUtility.showMiddleToast(LimitbuyShippingMethodActivity.this, "",
                        getString(R.string.shopping_goods_order_payment_cash_shippingtime));
                return;
            }
            if (!shopping_phone_confirm_yes_radiobutton.isChecked()
                    && !shopping_phone_confirm_no_radiobutton.isChecked()) {
                CommonUtility.showMiddleToast(LimitbuyShippingMethodActivity.this, "",
                        getString(R.string.shopping_goods_order_payment_cash_shippingconfirm));
                return;
            }
            savePayment();
            break;
        case R.id.shopping_weekday_relative:
        case R.id.shopping_weekday_radiobutton:
            shopping_weekday_radiobutton.setChecked(true);
            if (lastShippingTimeRadioButton != null && lastShippingTimeRadioButton != shopping_weekday_radiobutton) {
                lastShippingTimeRadioButton.setChecked(false);
            }
            lastShippingTimeRadioButton = shopping_weekday_radiobutton;
            break;
        case R.id.shopping_weekend_relative:
        case R.id.shopping_weekend_radiobutton:
            shopping_weekend_radiobutton.setChecked(true);
            if (lastShippingTimeRadioButton != null && lastShippingTimeRadioButton != shopping_weekend_radiobutton) {
                lastShippingTimeRadioButton.setChecked(false);
            }
            lastShippingTimeRadioButton = shopping_weekend_radiobutton;
            break;
        case R.id.shopping_all_relative:
        case R.id.shopping_all_radiobutton:
            shopping_all_radiobutton.setChecked(true);
            if (lastShippingTimeRadioButton != null && lastShippingTimeRadioButton != shopping_all_radiobutton) {
                lastShippingTimeRadioButton.setChecked(false);
            }
            lastShippingTimeRadioButton = shopping_all_radiobutton;
            break;
        case R.id.shopping_isneedconfirm_yes_relative:
        case R.id.shopping_phone_confirm_yes_radiobutton:
            shopping_phone_confirm_yes_radiobutton.setChecked(true);
            if (lastIsneedConfirmRadioButton != null
                    && lastIsneedConfirmRadioButton != shopping_phone_confirm_yes_radiobutton) {
                lastIsneedConfirmRadioButton.setChecked(false);
            }
            lastIsneedConfirmRadioButton = shopping_phone_confirm_yes_radiobutton;
            break;
        case R.id.shopping_isneedconfirm_no_relative:
        case R.id.shopping_phone_confirm_no_radiobutton:
            shopping_phone_confirm_no_radiobutton.setChecked(true);
            if (lastIsneedConfirmRadioButton != null
                    && lastIsneedConfirmRadioButton != shopping_phone_confirm_no_radiobutton) {
                lastIsneedConfirmRadioButton.setChecked(false);
            }
            lastIsneedConfirmRadioButton = shopping_phone_confirm_no_radiobutton;
            break;
        case R.id.common_title_btn_back:
            ShoppingCartOrderActivity.isBackKeyRefer = true;
            GroupLimitOrderActivity.isBackKeyRefer = true;
            finish();
            break;
        default:
            break;
        }
    }

    /**
     * 非普通购物车商品
     * 
     * @param paymentlist
     */
    private void initViewData(ShoppingCart_Recently_paymentDetail paymentlist) {
        shippingTime = paymentlist.getShippingTime();
        if (TextUtils.isEmpty(shippingTime)) {
            shippingTime = PAYMETHODDETAIL_SHIPPINGTIME_WEEKDAY;
        }
        paymentMethod = paymentlist.getShippingMethod();
        if (TextUtils.isEmpty(paymentMethod)) {
            paymentMethod = PAYMENTHODETAIL_PAYMENT_ONLINEPAYMENT;
        }
        if ("Y".equalsIgnoreCase(paymentlist.getIsNeedConfirm()) || TextUtils.isEmpty(paymentlist.getIsNeedConfirm())) {
            telBefShipping = "1";
            shopping_phone_confirm_yes_radiobutton.setChecked(true);
            lastIsneedConfirmRadioButton = shopping_phone_confirm_yes_radiobutton;
        } else if ("N".equalsIgnoreCase(paymentlist.getIsNeedConfirm())) {
            telBefShipping = "0";
            shopping_phone_confirm_no_radiobutton.setChecked(true);
            lastIsneedConfirmRadioButton = shopping_phone_confirm_no_radiobutton;
        }
        if (PAYMETHODDETAIL_SHIPPINGTIME_WEEKDAY.equals(shippingTime)) {
            shopping_weekday_radiobutton.setChecked(true);
            lastShippingTimeRadioButton = shopping_weekday_radiobutton;
        } else if (PAYMETHODDETAIL_SHIPPINGTIME_WEEKEND.equals(shippingTime)) {
            shopping_weekend_radiobutton.setChecked(true);
            lastShippingTimeRadioButton = shopping_weekend_radiobutton;
        } else if (PAYMETHODDETAIL_SHIPPINGTIME_ALL.equals(shippingTime)) {
            shopping_all_radiobutton.setChecked(true);
            lastShippingTimeRadioButton = shopping_all_radiobutton;
        }

    }

    private void savePayment() {
        if (!NetUtility.isNetworkAvailable(LimitbuyShippingMethodActivity.this)) {
            CommonUtility.showMiddleToast(LimitbuyShippingMethodActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, Object[]>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(LimitbuyShippingMethodActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected Object[] doInBackground(Object... params) {
                // BDebug.d(TAG, json);
                if (shopping_weekday_radiobutton.isChecked()) {
                    shippingTime = PAYMETHODDETAIL_SHIPPINGTIME_WEEKDAY;
                } else if (shopping_weekend_radiobutton.isChecked()) {
                    shippingTime = PAYMETHODDETAIL_SHIPPINGTIME_WEEKEND;
                } else if (shopping_all_radiobutton.isChecked()) {
                    shippingTime = PAYMETHODDETAIL_SHIPPINGTIME_ALL;
                }
                if (shopping_phone_confirm_yes_radiobutton.isChecked()) {
                    telBefShipping = "1";
                } else if (shopping_phone_confirm_no_radiobutton.isChecked()) {
                    telBefShipping = "0";
                }
                String body = ShoppingCart.saveFlashShippingInfo(paymentMethod, shippingTime, telBefShipping);
                String result = NetUtility.NO_CONN;
                result = NetUtility.sendHttpRequestByPost(Constants.URL_RUSHBUY_CHECKOUT_SAVE_SHIPPING, body);

                BDebug.e(Tag, result);
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
                if (objs == null || (Boolean) objs[0] == null) {
                    CommonUtility.showMiddleToast(LimitbuyShippingMethodActivity.this, "",
                            ShoppingCart.getErrorMessage());
                    return;
                }
                if ((Boolean) objs[0]) {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), LimitbuyInvoiceInfoActivity.class);
                    startActivity(intent);
                } else if (!(Boolean) objs[0] && objs.length == 2) {
                    CommonUtility.showAlertDialog(LimitbuyShippingMethodActivity.this, "", (String) objs[1],
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
