package com.gome.ecmall.shopping;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.MoreGomeStore.Store;
import com.gome.ecmall.bean.ShoppingCart;
import com.gome.ecmall.bean.ShoppingCart.PaymentDetail_paymentMethods;
import com.gome.ecmall.bean.ShoppingCart.ShoppintCart_payMentList;
import com.gome.ecmall.bean.shippingInfo;
import com.gome.ecmall.bean.shippingInfo.ShippingMethod;
import com.gome.ecmall.custom.DisScrollListView;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.groupbuy.GroupLimitOrderActivity;
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
public class ShoppingCartShippingMethodActivity extends Activity implements OnClickListener {

    private static final String Tag = "ShoppingCartShippingMethodActivity:";
    // 支付方式标识
    private static final String PAYMENTHODETAIL_PAYMENT_ONLINEPAYMENT = "onlinePayment";// 在线支付
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
    private String subPaymentMethod = PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY_CASH;
    private String shippingTime = "ALL";
    private String telBefShipping = "0";
    private RadioButton lastShippingTimeRadioButton;
    private RadioButton lastIsneedConfirmRadioButton;
    private String shippingId = "";
    private String isGome = "N";
    private Context mContext;
    private ListView lvShipping;
    private ShipingMethodAdapter mAdapter;
    private RelativeLayout rlShipping;
    private List<ShippingMethod> smList;
    // 店铺自取
    private ShipingStoreAdapter mStoreAdapter = null;
    private ArrayList<Store> mStoreList = null;
    private DisScrollListView mStoreListView;
    private LinearLayout mStoreLineLayout, mNormalLineLayout;
    private boolean isStore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.shopping_cart_shippingmethod);
        initTitleButton();
        // 普通购物车商品
        if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.GoodOrderType) {
            lvShipping.setVisibility(View.VISIBLE);
            shippingInfo paymentlist = (shippingInfo) getIntent().getSerializableExtra("paymentlist");
            shippingId = getIntent().getStringExtra("shippingId");
            isGome = getIntent().getStringExtra("isGome");
            initViewNormalData(paymentlist);
        } else { // 非普通购物车商品
            if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.GroupType
                    || GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.LimitType) {
                // 团购，抢购走普通的
                lvShipping.setVisibility(View.VISIBLE);
                shippingInfo paymentlist = (shippingInfo) getIntent().getSerializableExtra("paymentlist");
                shippingId = getIntent().getStringExtra("shippingId");
                isGome = getIntent().getStringExtra("isGome");
                initViewNormalData(paymentlist);
            } else {
                rlShipping.setVisibility(View.VISIBLE);
                ShoppintCart_payMentList paymentlist = (ShoppintCart_payMentList) getIntent().getSerializableExtra(
                        "paymentlist");
                if (paymentlist != null) {
                    initViewData(paymentlist);
                }
            }

        }
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
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.common_title_btn_right:
            if (!shopping_weekday_radiobutton.isChecked() && !shopping_weekend_radiobutton.isChecked()
                    && !shopping_all_radiobutton.isChecked()) {
                CommonUtility.showMiddleToast(ShoppingCartShippingMethodActivity.this, "",
                        getString(R.string.shopping_goods_order_payment_cash_shippingtime));
                return;
            }
            if (!shopping_phone_confirm_yes_radiobutton.isChecked()
                    && !shopping_phone_confirm_no_radiobutton.isChecked()) {
                CommonUtility.showMiddleToast(ShoppingCartShippingMethodActivity.this, "",
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
     * 普通购物车商品
     * 
     * @param paymentlist
     */
    private void initViewNormalData(shippingInfo paymentlist) {
        boolean isDefStore = false;
        // 生命配送方式列表
        smList = new ArrayList<shippingInfo.ShippingMethod>();
        if (paymentlist != null && paymentlist.getShippingMethodArray() != null
                && paymentlist.getShippingMethodArray().size() > 0) {
            smList.clear();
            smList.addAll(paymentlist.getShippingMethodArray());
        }

        if (paymentlist != null && smList.size() > 0) {
            if (!TextUtils.isEmpty(paymentlist.getShippingMethod())) {

                for (ShippingMethod sm : smList) {
                    if (sm.shippingMethod.equals(paymentlist.getShippingMethod())) {
                        sm.isCheck = true;
                        // 国美门店自提时初始化自提点显示
                        if ("Y".equalsIgnoreCase(isGome) && sm.shippingMethod.contains("Picking")) {
                            isDefStore = true;
                        } else {
                            isDefStore = false;
                        }
                    }
                }
            } else {
                // 默认第一个选中
                smList.get(0).isCheck = true;
            }
        }
        // 门店自提数据绑定
        if ("Y".equalsIgnoreCase(isGome)) {
            if (smList != null && smList.size() > 0) {
                for (ShippingMethod shippingMethod : smList) {
                    // Gome Picking Up
                    if (shippingMethod.shippingMethod.contains("Picking")) {
                        if (shippingMethod.getGomeStoreList() != null && shippingMethod.getGomeStoreList().size() > 0) {
                            if (mStoreList == null) {
                                mStoreList = new ArrayList<Store>();
                            }
                            mStoreList.clear();
                            mStoreList.addAll(shippingMethod.getGomeStoreList());
                        }
                    }
                }
                // 门店列表设置默认值
                if (mStoreList != null && mStoreList.size() > 0) {
                    Store defStore = paymentlist.getGomeStoreInfo();
                    if (defStore != null) {
                        boolean flag = true;
                        for (int i = 0 , size = mStoreList.size(); i < size; i++) {
                            if (mStoreList.get(i).getStoreId().equalsIgnoreCase(defStore.getStoreId())) {
                                flag = false;
                                mStoreList.get(i).setCheck(true);
                                break;
                            }
                        }
                        if (flag) {
                            mStoreList.get(0).setCheck(true);
                        }
                    } else {
                        mStoreList.get(0).setCheck(true);
                    }
                    // 数据绑定
                    if (isDefStore) {
                        showStoreView();
                    } else {
                        hideStoreView();
                    }
                }
            }
        }
        mAdapter = new ShipingMethodAdapter(smList);
        lvShipping.setAdapter(mAdapter);
        lvShipping.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0 , size = smList.size(); i < size; i++) {
                    if (i == position) {
                        smList.get(i).isCheck = true;
                        if ("Y".equalsIgnoreCase(isGome)) {
                            if (smList.get(i).shippingMethod.contains("Picking")) {
                                // 显示
                                showStoreView();
                            } else {
                                hideStoreView();
                            }
                        }
                    } else {
                        smList.get(i).isCheck = false;
                    }
                }
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        if (paymentlist == null) {
            return;
        }
        shippingTime = paymentlist.getShippingTime();
        if (TextUtils.isEmpty(shippingTime)) {
            shippingTime = PAYMETHODDETAIL_SHIPPINGTIME_WEEKDAY;
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

    /**
     * 非普通购物车商品
     * 
     * @param paymentlist
     */
    private void initViewData(ShoppintCart_payMentList paymentlist) {
        shippingTime = paymentlist.getShippingTime();
        if (TextUtils.isEmpty(shippingTime)) {
            shippingTime = PAYMETHODDETAIL_SHIPPINGTIME_WEEKDAY;
        }
        if ("true".equalsIgnoreCase(paymentlist.getTelBefShipping())
                || TextUtils.isEmpty(paymentlist.getTelBefShipping())) {
            telBefShipping = "1";
            shopping_phone_confirm_yes_radiobutton.setChecked(true);
            lastIsneedConfirmRadioButton = shopping_phone_confirm_yes_radiobutton;
        } else if ("false".equalsIgnoreCase(paymentlist.getTelBefShipping())) {
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
        List<PaymentDetail_paymentMethods> curretnpaymentMethodslist = paymentlist.getCurrentPaymentMethodsList();
        // List<PaymentDetail_paymentMethods> selepaymentMethodslist =
        // paymentlist.getPaymentMethodsSelectList();
        if (curretnpaymentMethodslist != null) {
            for (int i = 0, size = curretnpaymentMethodslist.size(); i < size; i++) {
                PaymentDetail_paymentMethods currentpaymentMethod = curretnpaymentMethodslist.get(i);
                if (currentpaymentMethod == null)
                    break;
                if (PAYMENTHODETAIL_PAYMENT_ONLINEPAYMENT.equals(currentpaymentMethod.getPaymentMethod())) {
                    paymentMethod = PAYMENTHODETAIL_PAYMENT_ONLINEPAYMENT;
                    subPaymentMethod = "";
                    return;
                } else if (PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY.equals(currentpaymentMethod.getPaymentMethod())) {
                    String posCash = currentpaymentMethod.getPosOrCash();
                    paymentMethod = PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY;
                    subPaymentMethod = posCash;
                    return;
                }
            }
        } else {
            // 默认在线支付
            paymentMethod = PAYMENTHODETAIL_PAYMENT_ONLINEPAYMENT;
            subPaymentMethod = "";
        }

    }

    private void savePayment() {
        if (!NetUtility.isNetworkAvailable(ShoppingCartShippingMethodActivity.this)) {
            CommonUtility.showMiddleToast(ShoppingCartShippingMethodActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, Object[]>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(ShoppingCartShippingMethodActivity.this,
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
                // start
                String shippingMethod = "Gome Express";
                for (ShippingMethod sm : smList) {
                    if (sm.isCheck) {
                        shippingMethod = sm.shippingMethod;
                    }
                }
                String storeId = "";
                if (isStore && mStoreList != null && mStoreList.size() > 0) {
                    for (Store store : mStoreList) {
                        if (store.isCheck()) {
                            storeId = store.getStoreId();
                        }
                    }
                }
                String mybody = ShoppingCart.saveOrModifyShippinginfo(shippingId, shippingMethod, shippingTime,
                        telBefShipping, storeId);
                // end
                String result = NetUtility.NO_CONN;
                if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.GroupType) {
                    result = NetUtility.sendHttpRequestByPost(Constants.URL_SAVE_GROUP_SHIPPING_TYPE, mybody);
                } else if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.LimitType) {
                    result = NetUtility.sendHttpRequestByPost(Constants.URL_SAVE_LIMIT_SHIPPING_TYPE, mybody);
                } else if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.GoodOrderType) {
                    result = NetUtility.sendHttpRequestByPost(Constants.URL_CHECKOUT_SAVE_DELIVERY, mybody);
                }
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
                    CommonUtility.showMiddleToast(ShoppingCartShippingMethodActivity.this, "",
                            ShoppingCart.getErrorMessage());
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
                    CommonUtility.showAlertDialog(ShoppingCartShippingMethodActivity.this, "", (String) objs[1],
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

    /**
     * 配送方式列表
     * 
     * @author qiudongchao
     * 
     */
    private class ShipingMethodAdapter extends BaseAdapter {
        List<ShippingMethod> mList;

        public ShipingMethodAdapter(List<ShippingMethod> list) {
            mList = list;
        }

        @Override
        public int getCount() {
            return mList != null ? mList.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mHolder = null;
            if (convertView == null) {
                mHolder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.shopping_cart_shippingmethod_item, null);
                mHolder.shopping_gomeexmp_radiobutton = (RadioButton) convertView
                        .findViewById(R.id.shopping_gomeexmp_radiobutton);
                mHolder.shopping_goods_order_payment_cash_gomeexmp = (TextView) convertView
                        .findViewById(R.id.shopping_goods_order_payment_cash_gomeexmp);
                mHolder.shopping_goods_order_payment_cash_price = (TextView) convertView
                        .findViewById(R.id.shopping_goods_order_payment_cash_price);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            ShippingMethod sm = mList.get(position);
            mHolder.shopping_gomeexmp_radiobutton.setChecked(sm.isCheck);
            mHolder.shopping_goods_order_payment_cash_gomeexmp.setText(sm.shippingMethodName);
            if (!TextUtils.isEmpty(sm.shippingFreight)) {
                converFei(mHolder.shopping_goods_order_payment_cash_price, sm.shippingFreight);
            }

            if (getCount() > 0) {
                if (getCount() == 1) {
                    convertView.setBackgroundResource(R.drawable.more_item_single_bg_selector);
                }
                if (getCount() > 1) {
                    if (position < getCount() - 1 && position > 0) {
                        convertView.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
                    } else if (position == getCount() - 1) {
                        convertView.setBackgroundResource(R.drawable.more_item_last_bg_selector);
                    } else if (position == 0) {
                        convertView.setBackgroundResource(R.drawable.more_item_first_bg_selector);
                    }
                }
            }
            return convertView;
        }

        /**
         * 免运费数据转换
         * 
         * @param fei
         * @return
         */
        public void converFei(TextView tv, String fei) {
            String message = "";
            if (fei.indexOf("0.0") == 0) {
                message = "(免运费)";
                tv.setTextColor(Color.GRAY);
            } else {
                message = "(" + getString(R.string.yuan_sign) + fei + ")";
                tv.setTextColor(Color.RED);
            }
            tv.setText(message);
        }

        private class ViewHolder {
            RadioButton shopping_gomeexmp_radiobutton;
            TextView shopping_goods_order_payment_cash_gomeexmp, shopping_goods_order_payment_cash_price;
        }
    }

    /**
     * 显示店铺自提视图
     */
    private void showStoreView() {
        isStore = true;
        mStoreLineLayout.setVisibility(View.VISIBLE);
        mNormalLineLayout.setVisibility(View.GONE);
        if (mStoreList != null) {
            if (mStoreAdapter == null) {
                mStoreAdapter = new ShipingStoreAdapter(mStoreList);
                mStoreListView.setAdapter(mStoreAdapter);
                mStoreListView.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        for (int i = 0 , size = mStoreList.size(); i < size; i++) {
                            if (i == position) {
                                mStoreList.get(i).setCheck(true);
                            } else {
                                mStoreList.get(i).setCheck(false);
                            }
                        }
                        if (mStoreAdapter != null) {
                            mStoreAdapter.notifyDataSetChanged();
                        }
                    }
                });
            } else {
                mStoreAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 隐藏店铺自提视图
     */
    private void hideStoreView() {
        isStore = false;
        mStoreLineLayout.setVisibility(View.GONE);
        mNormalLineLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 配送店铺适配器
     * 
     * @author qiudongchao
     * 
     */
    private class ShipingStoreAdapter extends BaseAdapter {
        ArrayList<Store> mList;

        public ShipingStoreAdapter(ArrayList<Store> list) {
            mList = list;
        }

        @Override
        public int getCount() {
            return mList != null ? mList.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mHolder = null;
            if (convertView == null) {
                mHolder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.shopping_cart_shippingstore_item, null);
                mHolder.shopping_store_radiobutton = (RadioButton) convertView
                        .findViewById(R.id.shopping_store_radiobutton);
                mHolder.shopping_store_name = (TextView) convertView.findViewById(R.id.shopping_store_name);
                mHolder.shopping_store_phone = (TextView) convertView.findViewById(R.id.shopping_store_phone);
                mHolder.shopping_store_address = (TextView) convertView.findViewById(R.id.shopping_store_address);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            Store sm = mList.get(position);

            mHolder.shopping_store_radiobutton.setChecked(sm.isCheck());
            String storeName = sm.getStoreName();
            if (!TextUtils.isEmpty(storeName)) {
                mHolder.shopping_store_name.setText(storeName);
            }
            String storeTel = sm.getStoreTel();
            if (!TextUtils.isEmpty(storeTel)) {
                mHolder.shopping_store_phone.setText(storeTel);
            }
            String storeAddr = sm.getStoreAddress();
            if (!TextUtils.isEmpty(storeAddr)) {
                mHolder.shopping_store_address.setText(storeAddr);
            }
            // 设置背景
            if (getCount() > 0) {
                if (getCount() == 1) {
                    convertView.setBackgroundResource(R.drawable.more_item_single_bg_selector);
                }
                if (getCount() > 1) {
                    if (position < getCount() - 1 && position > 0) {
                        convertView.setBackgroundResource(R.drawable.more_item_middle_bg_selector);
                    } else if (position == getCount() - 1) {
                        convertView.setBackgroundResource(R.drawable.more_item_last_bg_selector);
                    } else if (position == 0) {
                        convertView.setBackgroundResource(R.drawable.more_item_first_bg_selector);
                    }
                }
            }
            return convertView;
        }

        private class ViewHolder {
            RadioButton shopping_store_radiobutton;
            TextView shopping_store_name, shopping_store_phone, shopping_store_address;
        }
    }
}
