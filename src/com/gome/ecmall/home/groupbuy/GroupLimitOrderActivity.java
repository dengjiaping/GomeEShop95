package com.gome.ecmall.home.groupbuy;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.ShopInfo;
import com.gome.ecmall.bean.ShoppingCart;
import com.gome.ecmall.bean.ShoppingCart.Goods;
import com.gome.ecmall.bean.ShoppingCart.OrderSuccess;
import com.gome.ecmall.bean.ShoppingCart.PaymentDetail_paymentMethods;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_ConsInfo_address;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_Recently;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_Recently_address;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_Recently_nvoiceDetail;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_Recently_paymentDetail;
import com.gome.ecmall.bean.ShoppingCart.ShoppintCart_payMentList;
import com.gome.ecmall.bean.ShoppingCart.WanceInfo;
import com.gome.ecmall.bean.shippingInfo;
import com.gome.ecmall.custom.DisScrollListView;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.custom.SlipButton;
import com.gome.ecmall.custom.SlipButton.OnSwitchListener;
import com.gome.ecmall.home.limitbuy.LimitConfirmActivity;
import com.gome.ecmall.home.limitbuy.NewLimitbuyActivity;
import com.gome.ecmall.shopping.ShoppingCart1Adapter;
import com.gome.ecmall.shopping.ShoppingCartConsInfoAddActivity;
import com.gome.ecmall.shopping.ShoppingCartInvoiceInfoActivity;
import com.gome.ecmall.shopping.ShoppingCartOrderConsInfoActivity;
import com.gome.ecmall.shopping.ShoppingCartOrderSuccessActivity;
import com.gome.ecmall.shopping.ShoppingCartPaymentActivity;
import com.gome.ecmall.shopping.ShoppingCartShippingMethodActivity;
import com.gome.ecmall.shopping.ShoppingCartWanceInfoActivity;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.DES;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 填写订单（团购，抢购）
 * 
 * @author bo.yang createData 2012_07_16
 * 
 */
public class GroupLimitOrderActivity extends Activity implements OnClickListener {

    private static final String Tag = "ShoppingCartOrderActivity:";
    public static final int GroupType = 0;
    public static final int LimitType = 1;
    public static final int GoodOrderType = 2;
    public static final String GroupLimitOrderActivitySKUID = "skuID";
    public static final String GroupLimitOrderActivityGoodsNo = "goodsNo";
    public static final String GroupLimitOrderActivitySkuNo = "skuNo";
    public static final String GroupLimitOrderActivityRushbuyitemId = "rushBuyItemId";
    public static final String GroupLimitOrderActivityGoodName = "goodsName";
    public static final String GroupLimitOrderActivityMobileNum = "mobileNum";
    public static final String GroupLimitOrderActivityBuyCount = "buyCount";
    public static final String GroupLimitOrderActivityUnitPrice = "unitPrice";
    public static boolean isBackKeyRefer = false; // 判断是否是返回键
    // 支付方式标识
    private static final String PAYMENTHODETAIL_PAYMENT_ONLINEPAYMENT = "onlinePayment";// 在线支付
    private static final String PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY = "cashOnDelivery";// 货到付款
    // 是否需要电话确认
    private static final String PAYMETHODDETAIL_ISNEEDCONFIRM_NEED = "Y";
    private static final String PAYMETHODDETAIL_ISNEEDCONFIRM_NOT_NEED = "N";
    // 送货时间
    private static final String PAYMETHODDETAIL_SHIPPINGTIME_WEEKDAY = "WEEKDAY"; // 只工作日送货(双休日、假日不用送)
    private static final String PAYMETHODDETAIL_SHIPPINGTIME_WEEKEND = "WEEKEND"; // 只双休日、假日送货(工作日不用送)
    private static final String PAYMETHODDETAIL_SHIPPINGTIME_ALL = "ALL"; // 工作日、双休日与假日均可送货
    // 货到付款 (现金orPOS机)
    private static final String PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY_POS = "POS"; // pos机刷卡
    private static final String PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY_CASH = "CASH";// 现金
    private Context context;
    // private OrderSuccess myOrderSuccess;
    private WanceInfo orderwanceInfo;// 节能补贴数据，未从网站获取
    private String isForegoAllowance; // 是否放弃
    private String hasAllowance; // 是否节能补贴商品
    private Button common_title_btn_back, common_title_btn_right, sumberorderbutton;
    private TextView tvTitle, shopping_goods_cons_name_data, shopping_goods_cons_phone_data,
            shopping_goods_cons_address_data, shopping_goods_pay_method_data, shopping_goods_distribution_method_data,
            shopping_goods_distribution_time_data, shopping_goods_distribution_phone_confirm_data,
            shopping_goods_invoice_type_data, shopping_goods_invoice_title_data,
            shopping_goods_invoice_title_name_data, shopping_goods_invoice_content_data,
            shopping_goods_use_balance_data, shopping_goods_order_goods_price_data, shopping_goods_order_freight_data,
            shopping_goods_order_goods_balance_data, shopping_goods_order_goods_amount_due_data,
            shopping_goods_order_regtel_data, shopping_goods_order_bankname_data,
            shopping_goods_order_bankaccount_data, mark_del_text, energy_info_data, shopping_cart_energy_buytype_data,
            shopping_cart_energy_name_data, shopping_cart_energy_idcardnumber_data, shopping_cart_energy_bank_data,
            shopping_cart_energy_bank_number_data, shopping_cart_energy_name, shopping_cart_energy_idcardnumber;
    private RelativeLayout pay_method_relative, cons_info_relativelayout, distribution_relative,
            invoice_relativelayout, mark_del_relative, energy_info_relativelayout, energy_info_relative;
    private EditText shopping_cart_edit_ordermark;
    private SlipButton useBlanceSlipbutton;
    private LayoutInflater inflater;
    private LinearLayout cons_info_relative, shopping_invoice_relative, invoice_last_relative,
            ll_groupbuy_shipping_type;
    private ShoppingCart1Adapter shoppingAdapter1;
    private DisScrollListView disScrollListView1;
    private ShoppingCart_Recently_nvoiceDetail invoiceDetail; // 保存当前选择的发票;
    // private String usedTicketCount; // 可使用优惠券张数
    // private String totalAmount = "0.00";// 应付总金额
    // private String couponAmount = "0.00";// 优惠券

    private String skuID;
    private String goodsNo;
    private String rushBuyItemId;
    private double useBalance;// 账户余额
    private String orderModifying;
    private EditText editText;
    private StringBuffer sbString;
    private ArrayList<Goods> limitGoodsList;

    private String shippingId;// 配送id
    private String isGome;// 是否是gome商品
    private shippingInfo shippingInfos;// 配送信息

    private boolean isCanClickShipping; // 是否能点击配送方式

    private TextView tvShopName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        orderModifying = "";
        setContentView(R.layout.grouplimit_shopping_cart_order);
        inflater = LayoutInflater.from(context);
        initTitleButton();
        setData(null);
    };

    @Override
    protected void onDestroy() {
        shippingInfos = null;
        if (limitGoodsList != null) {
            limitGoodsList.clear();
            limitGoodsList = null;
        }
        invoiceDetail = null;
        orderwanceInfo = null;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.energy_info_relativelayout: {
            Intent intent = new Intent();
            intent.putExtra(ShoppingCart.JK_SHOPPINGCART_ALLOWANCEINFO, this.orderwanceInfo);
            intent.setClass(getApplicationContext(), ShoppingCartWanceInfoActivity.class);
            startActivityForResult(intent, 2);
        }
            break;
        case R.id.cons_info_relativelayout: {
            goToConsInfoData();
        }
            break;
        case R.id.pay_method_relative: {
            goToPaymentData(0);
        }
            break;
        case R.id.distribution_relative: {
            if (isCanClickShipping) {
                goToPaymentData(1);
            } else {
                Toast.makeText(GroupLimitOrderActivity.this, "请先填写收获地址", 1).show();
            }

        }
            break;
        case R.id.invoice_relativelayout:
            if ("N".equals(hasAllowance) || "Y".equals(isForegoAllowance)) {
                Intent intent = new Intent();
                intent.putExtra(ShoppingCart.JK_SHOPPINGCART_RECENTLY_NVOICEDETAIL, this.invoiceDetail);
                intent.putExtra("shippingId", shippingId);
                intent.putExtra("isGome", isGome);
                intent.setClass(getApplicationContext(), ShoppingCartInvoiceInfoActivity.class);
                startActivityForResult(intent, 2);
            } else {
                CommonUtility.showMiddleToast(context, "", "节能补贴商品不可修改发票信息");
            }
            break;
        case R.id.common_title_btn_right:
            sumberOrderData();
            break;
        case R.id.sumberorderbutton:
            sumberOrderData();
            break;
        case R.id.common_title_btn_back:
            finish();
            break;
        default:
            break;
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
        common_title_btn_right.setText(R.string.submit_order);
        common_title_btn_right.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.shopping_goods_order_goods_order_fill);
        shopping_goods_cons_name_data = (TextView) findViewById(R.id.shopping_goods_cons_name_data);
        shopping_goods_cons_phone_data = (TextView) findViewById(R.id.shopping_goods_cons_phone_data);
        shopping_goods_cons_address_data = (TextView) findViewById(R.id.shopping_goods_cons_address_data);
        shopping_goods_pay_method_data = (TextView) findViewById(R.id.shopping_goods_pay_method_data);
        shopping_goods_distribution_method_data = (TextView) findViewById(R.id.shopping_goods_distribution_method_data);
        shopping_goods_distribution_time_data = (TextView) findViewById(R.id.shopping_goods_distribution_time_data);
        shopping_goods_distribution_phone_confirm_data = (TextView) findViewById(R.id.shopping_goods_distribution_phone_confirm_data);
        shopping_goods_invoice_type_data = (TextView) findViewById(R.id.shopping_goods_invoice_type_data);
        shopping_goods_invoice_title_data = (TextView) findViewById(R.id.shopping_goods_invoice_title_data);
        shopping_goods_invoice_title_name_data = (TextView) findViewById(R.id.shopping_goods_invoice_title_name_data);
        shopping_goods_invoice_content_data = (TextView) findViewById(R.id.shopping_goods_invoice_content_data);
        shopping_goods_use_balance_data = (TextView) findViewById(R.id.shopping_goods_use_balance_data);
        shopping_goods_order_goods_price_data = (TextView) findViewById(R.id.shopping_goods_order_goods_price_data);
        shopping_goods_order_freight_data = (TextView) findViewById(R.id.shopping_goods_order_freight_data);
        shopping_goods_order_goods_balance_data = (TextView) findViewById(R.id.shopping_goods_order_goods_balance_data);
        shopping_goods_order_goods_amount_due_data = (TextView) findViewById(R.id.shopping_goods_order_goods_amount_due_data);
        // shopping_goods_invoice_title = (TextView) findViewById(R.id.shopping_goods_invoice_title);
        // shopping_goods_invoice_title_name = (TextView) findViewById(R.id.shopping_goods_invoice_title_name);
        // shopping_goods_invoice_content = (TextView) findViewById(R.id.shopping_goods_invoice_content);
        cons_info_relative = (LinearLayout) findViewById(R.id.cons_info_relative);
        shopping_invoice_relative = (LinearLayout) findViewById(R.id.shopping_invoice_relative);
        invoice_last_relative = (LinearLayout) findViewById(R.id.invoice_last_relative);
        ll_groupbuy_shipping_type = (LinearLayout) findViewById(R.id.ll_groupbuy_shipping_type);
        shopping_goods_order_regtel_data = (TextView) findViewById(R.id.shopping_goods_order_regtel_data);
        shopping_goods_order_bankname_data = (TextView) findViewById(R.id.shopping_goods_order_bankname_data);
        shopping_goods_order_bankaccount_data = (TextView) findViewById(R.id.shopping_goods_order_bankaccount_data);
        disScrollListView1 = (DisScrollListView) findViewById(R.id.shopping_cart_section_lv_first);
        cons_info_relativelayout = (RelativeLayout) findViewById(R.id.cons_info_relativelayout);
        cons_info_relativelayout.setOnClickListener(this);
        pay_method_relative = (RelativeLayout) findViewById(R.id.pay_method_relative);
        pay_method_relative.setOnClickListener(this);
        distribution_relative = (RelativeLayout) findViewById(R.id.distribution_relative);
        distribution_relative.setOnClickListener(this);
        invoice_relativelayout = (RelativeLayout) findViewById(R.id.invoice_relativelayout);
        invoice_relativelayout.setOnClickListener(this);
        // 节能补贴
        energy_info_relativelayout = (RelativeLayout) findViewById(R.id.energy_info_relativelayout);
        energy_info_relativelayout.setOnClickListener(this);
        energy_info_relative = (RelativeLayout) findViewById(R.id.energy_info_relative);
        energy_info_data = (TextView) findViewById(R.id.energy_info_data);
        shopping_cart_energy_buytype_data = (TextView) findViewById(R.id.shopping_cart_energy_buytype_data);
        shopping_cart_energy_name = (TextView) findViewById(R.id.shopping_cart_energy_name);
        shopping_cart_energy_name_data = (TextView) findViewById(R.id.shopping_cart_energy_name_data);
        shopping_cart_energy_idcardnumber_data = (TextView) findViewById(R.id.shopping_cart_energy_idcardnumber_data);
        shopping_cart_energy_bank_data = (TextView) findViewById(R.id.shopping_cart_energy_bank_data);
        shopping_cart_energy_bank_number_data = (TextView) findViewById(R.id.shopping_cart_energy_bank_number_data);
        shopping_cart_energy_idcardnumber = (TextView) findViewById(R.id.shopping_cart_energy_idcardnumber);
        tvShopName = (TextView) findViewById(R.id.shopping_goods_order_goods_list);
        //
        sumberorderbutton = (Button) findViewById(R.id.sumberorderbutton);
        sumberorderbutton.setOnClickListener(this);
        useBlanceSlipbutton = (SlipButton) findViewById(R.id.slipbutton_use_balance);
        useBlanceSlipbutton.setImageResource(R.drawable.slip_on_off_bg, R.drawable.slip_on_off_bg, R.drawable.slip);
        if (0.00 == useBalance) {
            useBlanceSlipbutton.setVisibility(View.GONE);
        }
        useBlanceSlipbutton.setSwitchState(false);
        useBlanceSlipbutton.setOnSwitchListener(new OnSwitchListener() {
            @Override
            public void onSwitched(boolean isSwitchOn) {

                if (isSwitchOn) {
                    editText = CommonUtility.showEditDialog(GroupLimitOrderActivity.this, "", editLeftListener,
                            editRightListener, false);
                }
            }
        });
        mark_del_relative = (RelativeLayout) findViewById(R.id.mark_del_relative);
        mark_del_relative.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                mark_del_relative.setVisibility(View.GONE);
            }

        });
        mark_del_text = (TextView) findViewById(R.id.mark_del_text);
        shopping_cart_edit_ordermark = (EditText) findViewById(R.id.shopping_cart_edit_ordermark);
        shopping_cart_edit_ordermark.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                mark_del_relative.setVisibility(View.VISIBLE);
            }

        });
        shopping_cart_edit_ordermark.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    mark_del_relative.setVisibility(View.VISIBLE);
                }
            }

        });
        shopping_cart_edit_ordermark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String afterText = s.toString();
                if (!TextUtils.isEmpty(afterText.trim())) {
                    int length = 15 - afterText.length();
                    mark_del_text.setText(Integer.toString(length));
                } else {
                    mark_del_text.setText("15");
                    mark_del_relative.setVisibility(View.GONE);
                }
            }
        });
        skuID = getIntent().getStringExtra(GroupLimitOrderActivitySKUID);
        goodsNo = getIntent().getStringExtra(GroupLimitOrderActivityGoodsNo);
        rushBuyItemId = getIntent().getStringExtra(GroupLimitOrderActivityRushbuyitemId);
    }

    private void setData(final String payPassword) {
        if (!NetUtility.isNetworkAvailable(context)) {
            CommonUtility.showMiddleToast(context, "",
                    context.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, ShoppingCart_Recently>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(GroupLimitOrderActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected ShoppingCart_Recently doInBackground(Object... params) {
                // BDebug.d(TAG, json);
                String result = NetUtility.NO_CONN;
                if (TextUtils.isEmpty(payPassword)) {
                    if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.GroupType) {
                        String request = ShoppingCart.createRequestGroupOrderListJson(skuID, goodsNo, rushBuyItemId,
                                orderModifying);
                        // result =
                        // "{\"isSuccess\": \"Y\",\"address\": {\"name\": \"张莉\",\"consignee\": \"张莉\",\"address\": \"到六里桥\",\"zipCode\": \"100070\",\"mobile\": \"13811574485\",\"provinceName\": \"北京\",\"cityName\": \"北京市\",\"districtName\": \"石景山区(五环里)\"},\"total\": 3997,\"payAmount\": 3997,\"paymentDetail\": {\"shippingMethod\": \"\",\"isNeedConfirm\": true},\"invoiceDetail\": {\"invoice\": {\"context\": \"明细\",\"head\": \"个人\",\"headType\": \"0\",\"headTypeDesc\": \"个人\",\"contextType\": \"0\",\"invoiceType\": \"0\",\"invoiceTypeDesc\": \"普通发票\"},\"isApplyForVAT\": false,\"isActiveMobile\": false,\"isSupportVAT\": true},\"goodsList\": [{\"skuID\": \"1000039537\",\"goodsNo\": \"9100016292\",\"skuNo\": \"10000001654\",\"skuName\": \"联合康森（Lahecs）RF-618电子冰箱除味器\",\"commerceItemID\": \"1545174214\",\"rushBuyItemId\": \"19700005\",\"skuThumbImgUrl\": \"http://img1.gome.com.cn/adfsdf_60.jpg\",\"skuOriginalPrice\": \"3998.00\",\"skuGrouponBuyPrice\": \"3500.00\",\"goodsCount\": \"1\",\"attributes\": [{\"name\": \"颜色\",\"value\": \"白色\"},{\"name\": \"尺寸\",\"value\": \"2M*1M\"}]}],\"couponAmount\": 0,\"freight\": 0,\"goodsCount\": 2,\"jsessionId\": \"4H57P2jWtrc17cxJyZJsdvmQ06nsQZSPrKQ5FLxSw1hd8srdLWLB!1014839381!1341563894676\"}";//NetUtility.sendHttpRequestByPost(Constants.URL_CHECKOUT_RECENTLY_ADDRESS,
                        // request);
                        result = NetUtility.sendHttpRequestByPost(Constants.URL_GROUPON_PURCHASE, request);
                        BDebug.e(Tag, result);
                    } else if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.LimitType) {
                        String request = ShoppingCart.createRequestLimitOrderListJson(skuID, goodsNo, rushBuyItemId,
                                orderModifying);
                        BDebug.e(Tag, request);
                        // result =
                        // "{\"isSuccess\": \"Y\",\"address\": {\"name\": \"张莉\",\"consignee\": \"张莉\",\"address\": \"到六里桥\",\"zipCode\": \"100070\",\"mobile\": \"13811574485\",\"provinceName\": \"北京\",\"cityName\": \"北京市\",\"districtName\": \"石景山区(五环里)\"},\"total\": 3997,\"payAmount\": 3997,\"paymentDetail\": {\"shippingMethod\": \"\",\"isNeedConfirm\": true},\"invoiceDetail\": {\"invoice\": {\"context\": \"明细\",\"head\": \"个人\",\"headType\": \"0\",\"headTypeDesc\": \"个人\",\"contextType\": \"0\",\"invoiceType\": \"0\",\"invoiceTypeDesc\": \"普通发票\"},\"isApplyForVAT\": false,\"isActiveMobile\": false,\"isSupportVAT\": true},\"goodsList\": [{\"skuID\": \"1000039537\",\"goodsNo\": \"9100016292\",\"skuNo\": \"10000001654\",\"skuName\": \"联合康森（Lahecs）RF-618电子冰箱除味器\",\"commerceItemID\": \"1545174214\",\"rushBuyItemId\": \"19700005\",\"skuThumbImgUrl\": \"http://img1.gome.com.cn/adfsdf_60.jpg\",\"skuOriginalPrice\": \"3998.00\",\"skuGrouponBuyPrice\": \"3500.00\",\"goodsCount\": \"1\",\"attributes\": [{\"name\": \"颜色\",\"value\": \"白色\"},{\"name\": \"尺寸\",\"value\": \"2M*1M\"}]}],\"couponAmount\": 0,\"freight\": 0,\"goodsCount\": 2,\"jsessionId\": \"4H57P2jWtrc17cxJyZJsdvmQ06nsQZSPrKQ5FLxSw1hd8srdLWLB!1014839381!1341563894676\"}";//NetUtility.sendHttpRequestByPost(Constants.URL_CHECKOUT_RECENTLY_ADDRESS,
                        // request);
                        result = NetUtility.sendHttpRequestByPost(Constants.URL_RUSHBUY_CART_RUSHBUY_PURCHASE, request);
                        BDebug.e(Tag, result);
                    }
                } else {
                    String request = ShoppingCart.createRequestUseBalanceJson(payPassword, useBalance);
                    result = NetUtility.sendHttpRequestByPost(Constants.URL_CHECKOUT_RECENTLY_ADDRESS, request);
                    BDebug.e(Tag, result);
                }
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
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
                    CommonUtility.showMiddleToast(GroupLimitOrderActivity.this, "", ShoppingCart.getErrorMessage());
                    return;
                }

                initViewData(shoppingCart_recently);
            };
        }.execute();
    }

    private void initViewData(ShoppingCart_Recently shoppingCart_recently) {
        shippingInfos = shoppingCart_recently.getShippingInfo();
        shippingId = shoppingCart_recently.getShippingId();
        isGome = shoppingCart_recently.getIsGome();
        // 节能补贴信息
        hasAllowance = shoppingCart_recently.getHasAllowance();
        if ("Y".equalsIgnoreCase(shoppingCart_recently.getHasAllowance())) {
            if ("Y".equals(shoppingCart_recently.getIsForegoAllowance())) {
                energy_info_relative.setVisibility(View.GONE);
                energy_info_data.setText("已放弃");
                energy_info_data.setVisibility(View.VISIBLE);
            } else if ("N".equals(shoppingCart_recently.getIsForegoAllowance())) {
                energy_info_relative.setVisibility(View.VISIBLE);
                energy_info_data.setVisibility(View.GONE);
            }
            initRecently_energy(shoppingCart_recently.getWanceInfo());
            isForegoAllowance = shoppingCart_recently.getIsForegoAllowance();
            energy_info_relativelayout.setVisibility(View.VISIBLE);
        } else if ("N".equalsIgnoreCase(shoppingCart_recently.getHasAllowance())) {
            energy_info_relativelayout.setVisibility(View.GONE);
        }
        // 收货人信息
        ShoppingCart_Recently_address rec_address = shoppingCart_recently.getRec_address();
        initRecently_address(rec_address);
        // 支付及配送方式
        ShoppingCart_Recently_paymentDetail paymentDetail = shoppingCart_recently.getPaymentDetail();
        shippingInfo shippingInfos = shoppingCart_recently.getShippingInfo();
        initRecently_paymentDetail(paymentDetail, shippingInfos);
        // 发票信息
        ShoppingCart_Recently_nvoiceDetail invoiceDetail = shoppingCart_recently.getNvoiceDetail();
        initRecently_invoiceDetail(invoiceDetail);
        // 使用余额
        if (TextUtils.isEmpty(shoppingCart_recently.getBalance())) {
            shopping_goods_use_balance_data.setText("￥0.00");
        } else {
            shopping_goods_use_balance_data.setText(shoppingCart_recently.getBalance());
            useBalance = Double.parseDouble(shoppingCart_recently.getBalance());
        }
        ArrayList<Goods> goodsList = shoppingCart_recently.getGoodsList();
        // 设置店铺名称
        setShopNames(shoppingCart_recently.getGoodsCount(), shoppingCart_recently.getShopInfo());
        // 商品清单
        initGoodsListData(goodsList);
        // 商品总价
        shopping_goods_order_goods_price_data.setText("￥"
                + (TextUtils.isEmpty(shoppingCart_recently.getTotal()) ? "0.00" : shoppingCart_recently.getTotal()));
        // 运费
        shopping_goods_order_freight_data
                .setText("￥"
                        + (TextUtils.isEmpty(shoppingCart_recently.getFreight()) ? "0.00" : shoppingCart_recently
                                .getFreight()));
        // 余额
        shopping_goods_order_goods_balance_data.setText("￥"
                + (TextUtils.isEmpty(shoppingCart_recently.getPayBalance()) ? "0.00" : shoppingCart_recently
                        .getPayBalance()));
        // 应付金额
        shopping_goods_order_goods_amount_due_data.setText("￥"
                + (TextUtils.isEmpty(shoppingCart_recently.getPayAmount()) ? "0.00" : shoppingCart_recently
                        .getPayAmount()));
        // totalAmount = shoppingCart_recently.getPayAmount();
        // couponAmount = shoppingCart_recently.getCouponAmount();
    }

    // 设置店铺名称
    private void setShopNames(String goodsCount, ShopInfo shopInfo) {
        if ("Y".equalsIgnoreCase(isGome)) {
            tvShopName.setText("国美在线  " + goodsCount);
        } else {
            if (shopInfo != null) {
                String shopInfoName = shopInfo.getBbcShopName();
                if (TextUtils.isEmpty(shopInfoName)) {
                    tvShopName.setText("商品清单  " + goodsCount);
                } else {
                    tvShopName.setText(shopInfoName + " " + goodsCount);
                }

            } else {
                tvShopName.setText("商品清单  " + goodsCount);
            }
        }
        if (!TextUtils.isEmpty(goodsCount)) {
            tvShopName.append("件");
        }
    }

    private void initRecently_address(ShoppingCart_Recently_address rec_address) {
        if (rec_address == null
                || (TextUtils.isEmpty(rec_address.getConsignee()) && TextUtils.isEmpty(rec_address.getMobile())
                        && TextUtils.isEmpty(rec_address.getPhone()) && TextUtils.isEmpty(rec_address.getAddress()))) {
            cons_info_relative.setVisibility(View.GONE);
        } else {
            isCanClickShipping = true;
            cons_info_relative.setVisibility(View.VISIBLE);
            shopping_goods_cons_name_data.setText(rec_address.getConsignee());
            String phone = rec_address.getPhone();
            String mobile = rec_address.getMobile();
            String showPhoneMoble = "";
            if (!TextUtils.isEmpty(phone))
                showPhoneMoble = phone;
            else
                showPhoneMoble = mobile;
            shopping_goods_cons_phone_data.setText(showPhoneMoble);
            shopping_goods_cons_address_data.setText(rec_address.getProvinceName().concat(rec_address.getCityName())
                    .concat(rec_address.getDistrictName()).concat(rec_address.getAddress()));
        }
    }

    private void initRecently_energy(WanceInfo wanceInfo) {

        if (wanceInfo == null
                || (TextUtils.isEmpty(wanceInfo.getHeadType()) && TextUtils.isEmpty(wanceInfo.getHead())
                        && TextUtils.isEmpty(wanceInfo.getPayerID()) && TextUtils.isEmpty(wanceInfo.getAccount()) && TextUtils
                            .isEmpty(wanceInfo.getBank()))) {
            energy_info_relative.setVisibility(View.GONE);
        } else {
            orderwanceInfo = wanceInfo;
            if ("0".equals(wanceInfo.getHeadType())) {
                shopping_cart_energy_buytype_data
                        .setText(getString(R.string.shopping_goods_order_general_invoice_person));
                shopping_cart_energy_name.setText(R.string.shopping_cart_energy_name);
                shopping_cart_energy_idcardnumber.setText(R.string.shopping_cart_energy_idcardnumber);
            } else if ("1".equals(wanceInfo.getHeadType())) {
                shopping_cart_energy_buytype_data.setText(getString(R.string.shopping_cart_energy_company));
                shopping_cart_energy_name.setText(R.string.shopping_cart_energy_mycompany);
                shopping_cart_energy_idcardnumber.setText(R.string.shopping_cart_energy_compancardnumber_null);
            }
            shopping_cart_energy_name_data.setText(wanceInfo.getHead());
            shopping_cart_energy_idcardnumber_data.setText(wanceInfo.getPayerID());
            shopping_cart_energy_bank_data.setText(wanceInfo.getBank());
            shopping_cart_energy_bank_number_data.setText(wanceInfo.getAccount());
        }
    }

    private void initRecently_paymentDetail(ShoppingCart_Recently_paymentDetail paymentDetail,
            shippingInfo shippingInfos) {
        if (paymentDetail != null) {
            String shippingMethod = null;
            String isNeedConfirm = null;
            String shippingTime = null;
            if (shippingInfos != null) {
                shippingMethod = shippingInfos.getShippingMethodName();
                isNeedConfirm = shippingInfos.getIsNeedConfirm();
                shippingTime = shippingInfos.getShippingTime();
            }
            if (TextUtils.isEmpty(shippingMethod) && TextUtils.isEmpty(shippingTime)) {
                ll_groupbuy_shipping_type.setVisibility(View.GONE);
            } else {
                ll_groupbuy_shipping_type.setVisibility(View.VISIBLE);
            }
            shopping_goods_distribution_method_data.setText(shippingMethod);
            // String isNeedConfirm = paymentDetail.getIsNeedConfirm();
            if (PAYMETHODDETAIL_ISNEEDCONFIRM_NEED.equalsIgnoreCase(isNeedConfirm)) {
                shopping_goods_distribution_phone_confirm_data.setText(R.string.shopping_goods_order_isneedconfirm_yes);
            } else if (PAYMETHODDETAIL_ISNEEDCONFIRM_NOT_NEED.equalsIgnoreCase(isNeedConfirm)) {
                shopping_goods_distribution_phone_confirm_data.setText(R.string.shopping_goods_order_isneedconfirm_no);
            }
            // String shippingTime = paymentDetail.getShippingTime();
            if (PAYMETHODDETAIL_SHIPPINGTIME_WEEKDAY.equalsIgnoreCase(shippingTime)) {
                shopping_goods_distribution_time_data.setText(R.string.shopping_goods_order_shippingtime_weekday);
            } else if (PAYMETHODDETAIL_SHIPPINGTIME_WEEKEND.equalsIgnoreCase(shippingTime)) {
                shopping_goods_distribution_time_data.setText(R.string.shopping_goods_order_shippingtime_weekend);
            } else if (PAYMETHODDETAIL_SHIPPINGTIME_ALL.equalsIgnoreCase(shippingTime)) {
                shopping_goods_distribution_time_data.setText(R.string.shopping_goods_order_shippingtime_all);
            }
            List<PaymentDetail_paymentMethods> paymentMethodsList = paymentDetail.getPaymentMethods();
            if (paymentMethodsList != null) {
                StringBuffer sb = new StringBuffer();
                for (int i = 0, size = paymentMethodsList.size(); i < size; i++) {
                    PaymentDetail_paymentMethods paymentMethods = paymentMethodsList.get(i);
                    if (paymentMethods != null) {
                        sb.append(paymentMethods.getPaymentMethodDesc());
                        if (PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY.equals(paymentMethods.getPaymentMethod())) {
                            if (PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY_POS.equals(paymentMethods.getPosOrCash()))
                                sb.append("(POS机刷卡)");
                            else if (PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY_CASH.equals(paymentMethods.getPosOrCash())) {
                                sb.append("(现金)");
                            }
                        }
                        if (i < size - 1) {
                            sb.append("，");
                        }
                    }

                }
                shopping_goods_pay_method_data.setText(sb.toString());
            }
        }
    }

    private void initRecently_invoiceDetail(ShoppingCart_Recently_nvoiceDetail invoiceDetail) {
        if (invoiceDetail == null) {
            shopping_invoice_relative.setVisibility(View.GONE);
        } else if ("0".equals(invoiceDetail.getInvoiceType()) && TextUtils.isEmpty(invoiceDetail.getHeadType())
                && TextUtils.isEmpty(invoiceDetail.getHead())) {
            shopping_invoice_relative.setVisibility(View.GONE);
        } else if ("1".equals(invoiceDetail.getInvoiceType()) && TextUtils.isEmpty(invoiceDetail.getCompanyName())) {
            shopping_invoice_relative.setVisibility(View.GONE);
        } else {
            shopping_invoice_relative.setVisibility(View.VISIBLE);
            shopping_goods_invoice_type_data.setText(invoiceDetail.getInvoiceTypeDesc());
            if ("0".equals(invoiceDetail.getInvoiceType())) {
                invoice_last_relative.setVisibility(View.GONE);
                // shopping_goods_invoice_title.setText(R.string.shopping_goods_invoice_title);
                // shopping_goods_invoice_title_name.setText(R.string.shopping_goods_invoice_title_name);
                // .setText(R.string.shopping_goods_invoice_content);
                shopping_goods_invoice_title_data.setText(invoiceDetail.getHeadTypeDesc());
                shopping_goods_invoice_title_name_data.setText(invoiceDetail.getHead());
                shopping_goods_invoice_content_data.setText(invoiceDetail.getContext());
            } else if ("1".equals(invoiceDetail.getInvoiceType())) {
                invoice_last_relative.setVisibility(View.VISIBLE);
                // shopping_goods_invoice_title.setText(R.string.shopping_goods_order_companyname);
                // shopping_goods_invoice_title_name.setText(R.string.shopping_goods_order_taxpayerno);
                // shopping_goods_invoice_content.setText(R.string.shopping_goods_order_regaddress);
                shopping_goods_invoice_title_data.setText(invoiceDetail.getCompanyName());
                shopping_goods_invoice_title_name_data.setText(invoiceDetail.getTaxpayerNo());
                shopping_goods_invoice_content_data.setText(invoiceDetail.getRegAddress());
                shopping_goods_order_regtel_data.setText(invoiceDetail.getRegTel());
                shopping_goods_order_bankname_data.setText(invoiceDetail.getBankName());
                shopping_goods_order_bankaccount_data.setText(invoiceDetail.getBankAccount());
            } else {
                shopping_invoice_relative.setVisibility(View.GONE);
            }
            this.invoiceDetail = invoiceDetail;
        }
    }

    // 加载商品清单
    private void initGoodsListData(ArrayList<Goods> shppingCartGoodsList_goods) {
        if (shppingCartGoodsList_goods != null) {
            sbString = new StringBuffer();
            if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.LimitType) {
                for (Goods goods : shppingCartGoodsList_goods) {
                    sbString.append(";");
                    sbString.append(goods.getSkuID());
                    sbString.append(";");
                    sbString.append(goods.getGoodsCount());
                    sbString.append(";");
                    sbString.append(goods.getSkuRushBuyPrice());
                    sbString.append(",");
                }
            } else if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.GroupType) {
                for (Goods goods : shppingCartGoodsList_goods) {
                    sbString.append(";");
                    sbString.append(goods.getSkuID());
                    sbString.append(";");
                    sbString.append(goods.getGoodsCount());
                    sbString.append(";");
                    sbString.append(goods.getSkuGroupBuyPrice());
                    sbString.append(",");
                }
            }
            limitGoodsList = shppingCartGoodsList_goods;
            shoppingAdapter1 = new ShoppingCart1Adapter(context, shppingCartGoodsList_goods, disScrollListView1);
            shoppingAdapter1.setShoppingCartGroupGoodsCount(shppingCartGoodsList_goods.size());
            disScrollListView1.setAdapter(shoppingAdapter1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
        // 收货人信息返回处理
        case 0:
        case 1:
        case 2:
        case 3:
        case 7: {
            if (!isBackKeyRefer) {
                orderModifying = "Y";
                setData(null);
            }
            isBackKeyRefer = false;
        }
            break;
        case 4: {
            // Intent intent = new Intent(this,ShoppingCartActivity.class);
            // setResult(4, intent);
            finish();
        }
            break;
        case 5:
            finish();
            break;
        case 6: {
            if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.GroupType) {
                Intent intent = new Intent(this, NewGroupBuyDetailActivity.class);
                setResult(5, intent);
            } else if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.LimitType) {
                Intent intent = new Intent(this, NewLimitbuyActivity.class);
                setResult(5, intent);
            }
            finish();
        }
            break;
        case 11:
            if (!isBackKeyRefer) {
                isCanClickShipping = true;
                orderModifying = "Y";
                setData(null);
            }
            isBackKeyRefer = false;
            break;
        default:
            break;
        }
    }

    /**
     * 支付及配送方式: 0 支付方式 1 配送方式
     * 
     * @param paymentShippingType
     */
    private void goToPaymentData(final int paymentShippingType) {
        if ((GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.GroupType && 1 == paymentShippingType)
                || (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.LimitType && 1 == paymentShippingType)) {
            // 团购，抢购并且是去配送方式,不用请求网络，带着配送列表过去
            Intent intent = new Intent();
            intent.putExtra("paymentlist", shippingInfos);
            intent.putExtra("shippingId", shippingId);
            intent.putExtra("isGome", isGome);
            intent.setClass(getApplicationContext(), ShoppingCartShippingMethodActivity.class);
            startActivityForResult(intent, 1);

        } else {
            if (!NetUtility.isNetworkAvailable(context)) {
                CommonUtility.showMiddleToast(context, "",
                        context.getString(R.string.can_not_conntect_network_please_check_network_settings));
                return;
            }
            new AsyncTask<Object, Void, ShoppintCart_payMentList>() {
                LoadingDialog progressDialog;

                protected void onPreExecute() {
                    progressDialog = CommonUtility.showLoadingDialog(GroupLimitOrderActivity.this,
                            getString(R.string.loading), true, new OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {

                                    cancel(true);
                                }
                            });
                };

                protected ShoppintCart_payMentList doInBackground(Object... params) {
                    String result = NetUtility.NO_CONN;
                    if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.GroupType) {
                        result = NetUtility.sendHttpRequestByGet(Constants.URL_GROUPON_QUERYMOBILEPAYMENTMETHODS);
                    } else if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.LimitType) {
                        result = NetUtility
                                .sendHttpRequestByGet(Constants.URL_RUSHBUY_CHECKOUT_QUERY_MOBILE_PAYMENT_METHODS);
                    }
                    if (NetUtility.NO_CONN.equals(result)) {
                        ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
                        return null;
                    }
                    return ShoppingCart.paserResponseShoppintCart_payMent(result);
                };

                protected void onPostExecute(ShoppintCart_payMentList paymentlist) {
                    progressDialog.dismiss();
                    if (isCancelled()) {
                        return;
                    }
                    if (paymentlist == null) {
                        CommonUtility.showMiddleToast(GroupLimitOrderActivity.this, "", ShoppingCart.getErrorMessage());
                        return;
                    }
                    if (0 == paymentShippingType) {
                        Intent intent = new Intent();
                        intent.putExtra("paymentlist", paymentlist);
                        intent.setClass(getApplicationContext(), ShoppingCartPaymentActivity.class);
                        startActivityForResult(intent, 1);
                    } else if (1 == paymentShippingType) {
                        Intent intent = new Intent();
                        intent.putExtra("paymentlist", paymentlist);
                        intent.setClass(getApplicationContext(), ShoppingCartShippingMethodActivity.class);
                        startActivityForResult(intent, 1);
                    }
                };
            }.execute();
        }

    }

    /**
     * 获取收货人信息
     */
    private void goToConsInfoData() {
        if (!NetUtility.isNetworkAvailable(context)) {
            CommonUtility.showMiddleToast(context, "",
                    context.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, ShoppingCart_ConsInfo_address>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(GroupLimitOrderActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected ShoppingCart_ConsInfo_address doInBackground(Object... params) {
                String result = NetUtility.NO_CONN;
                if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.GroupType) {
                    result = NetUtility.sendHttpRequestByGet(Constants.URL_GROUPON_ADDRESSLIST);
                } else if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.LimitType) {
                    result = NetUtility.sendHttpRequestByGet(Constants.URL_RUSHBUY_CHECKOUT_ADDRESS_LIST);
                }
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
                    CommonUtility.showMiddleToast(GroupLimitOrderActivity.this, "", ShoppingCart.getErrorMessage());
                    return;
                }
                if (shoppingCart_consInfo_address.getAddressList() == null
                        || shoppingCart_consInfo_address.getAddressList().size() == 0) {
                    Intent intent = new Intent();
                    intent.setAction("GroupLimitOrderActivity");
                    intent.putExtra("shoppingCart_consInfo_address", shoppingCart_consInfo_address.getCurrentAddress());
                    intent.setClass(getApplicationContext(), ShoppingCartConsInfoAddActivity.class);
                    startActivityForResult(intent, 0);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("shoppingCart_consInfo_address", shoppingCart_consInfo_address);
                    intent.setClass(getApplicationContext(), ShoppingCartOrderConsInfoActivity.class);
                    startActivityForResult(intent, 0);
                }
            };
        }.execute();
    }

    private void sumberOrderData() {
        if (TextUtils.isEmpty(shopping_goods_cons_name_data.getText())
                || TextUtils.isEmpty(shopping_goods_cons_phone_data.getText())
                || TextUtils.isEmpty(shopping_goods_cons_address_data.getText())) {
            CommonUtility.showMiddleToast(context, "", context.getString(R.string.shopping_cart_order_consinfo));
            return;
        } else if (TextUtils.isEmpty(shopping_goods_distribution_method_data.getText())
                || TextUtils.isEmpty(shopping_goods_distribution_time_data.getText())
                || TextUtils.isEmpty(shopping_goods_distribution_phone_confirm_data.getText())) {
            CommonUtility.showMiddleToast(context, "", context.getString(R.string.shopping_cart_order_shippment));
            return;
        } else if (TextUtils.isEmpty(shopping_goods_invoice_title_data.getText())
                || TextUtils.isEmpty(shopping_goods_invoice_title_name_data.getText())
                || TextUtils.isEmpty(shopping_goods_invoice_content_data.getText())) {
            CommonUtility.showMiddleToast(context, "", context.getString(R.string.shopping_cart_order_invoice));
            return;
        }
        if (!NetUtility.isNetworkAvailable(context)) {
            CommonUtility.showMiddleToast(context, "",
                    context.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.LimitType) {
            Intent intent = new Intent();
            intent.putExtra("limitgoods", limitGoodsList.get(0));
            String orderMark = shopping_cart_edit_ordermark.getText().toString();
            intent.putExtra("orderMark", orderMark);
            if (!TextUtils.isEmpty(sbString))
                intent.putExtra("shoppingCartOctree", sbString.toString());
            intent.setClass(getApplicationContext(), LimitConfirmActivity.class);
            startActivityForResult(intent, 0);
        } else if (GlobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.GroupType) {
            new AsyncTask<Object, Void, OrderSuccess>() {
                LoadingDialog progressDialog;

                protected void onPreExecute() {
                    progressDialog = CommonUtility.showLoadingDialog(GroupLimitOrderActivity.this,
                            getString(R.string.loading), true, new OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {

                                    cancel(true);
                                }
                            });
                };

                protected OrderSuccess doInBackground(Object... params) {
                    String orderMark = shopping_cart_edit_ordermark.getText().toString();
                    if (TextUtils.isEmpty(orderMark)) {
                        orderMark = "";
                    }
                    String body = ShoppingCart.reqGroupLimtSubmitOrder(orderMark, null);
                    // if (GobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.GroupType) {
                    String result = NetUtility.sendHttpRequestByPost(Constants.URL_GROUPON_SUBMITORDER, body);
                    // } else if (GobalConfig.getInstance().getGroupLimitType() == GroupLimitOrderActivity.LimitType) {
                    // result = NetUtility.sendHttpRequestByPost(Constants.URL_RUSHBUY_CHECKOUT_SUBMIT_ORDER, body);
                    // }
                    // result =
                    // "{\"isSuccess\": \"Y\",\"payAmount\": \"3500.00\",\"orderId\": \"3044984188\",\"paymentMethods\": [{\"payModeID\": \"cashOnDelivery\"}],\"message\": \"\"}";//NetUtility.sendHttpRequestByPost(Constants.URL_CHECKOUT__SUBMIT,body);
                    // BDebug.e(Tag, result);
                    if (NetUtility.NO_CONN.equals(result)) {
                        ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
                        return null;
                    }
                    return ShoppingCart.paserResponseGroupLimit_OrderSubmit(result);
                };

                protected void onPostExecute(OrderSuccess orderSuccess) {
                    progressDialog.dismiss();
                    if (isCancelled()) {
                        return;
                    }
                    if (orderSuccess == null) {
                        CommonUtility.showMiddleToast(GroupLimitOrderActivity.this, "", ShoppingCart.getErrorMessage());
                        return;
                    }
                    // myOrderSuccess = orderSuccess;
                    if (!TextUtils.isEmpty(orderSuccess.getOrderId())) {
                        Intent intent = new Intent();
                        intent.putExtra("orderSuccess", orderSuccess);
                        intent.setAction("GroupLimitOrderActivity");
                        ShoppingCartOrderSuccessActivity.orderSuccess = null;
                        if (!TextUtils.isEmpty(sbString))
                            intent.putExtra("shoppingCartOctree", sbString.toString());
                        intent.setClass(getApplicationContext(), ShoppingCartOrderSuccessActivity.class);
                        startActivityForResult(intent, 4);
                    }
                };
            }.execute();
        }
    }

    private android.content.DialogInterface.OnClickListener editLeftListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            if (editText != null && !TextUtils.isEmpty(editText.getText().toString())) {
                dialog.dismiss();
                String payPassword = editText.getText().toString();
                try {
                    setData(DES.encryptDES(payPassword, Constants.LOGINDESKEY));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (TextUtils.isEmpty(editText.getText().toString())) {
                CommonUtility.showMiddleToast(GroupLimitOrderActivity.this, "",
                        getString(R.string.shopping_cart_used_paypassword));
            }
        }

    };

    private android.content.DialogInterface.OnClickListener editRightListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            dialog.cancel();
        }

    };

}
