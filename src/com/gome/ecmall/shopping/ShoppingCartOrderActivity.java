package com.gome.ecmall.shopping;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bangcle.safekeyboard.PasswordEditText;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.ShopingCartInfo;
import com.gome.ecmall.bean.ShoppingCart;
import com.gome.ecmall.bean.ShoppingCart.CouponTicket_CanUsed;
import com.gome.ecmall.bean.ShoppingCart.Goods;
import com.gome.ecmall.bean.ShoppingCart.OrderProm;
import com.gome.ecmall.bean.ShoppingCart.OrderSuccess;
import com.gome.ecmall.bean.ShoppingCart.OutOfStockGoods;
import com.gome.ecmall.bean.ShoppingCart.PaymentDetail_paymentMethods;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCartGoods;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_ConsInfo_address;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_Recently;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_Recently_address;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_Recently_nvoiceDetail;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_Recently_paymentDetail;
import com.gome.ecmall.bean.ShoppingCart.ShoppingCart_Recently_usedTicketDetail;
import com.gome.ecmall.bean.ShoppingCart.ShoppintCart_payMentList;
import com.gome.ecmall.bean.ShoppingCart.UsedTicketDetail_usedBlueTickets;
import com.gome.ecmall.bean.ShoppingCart.UsedTicketDetail_usedRedTickets;
import com.gome.ecmall.bean.ShoppingCart.WanceInfo;
import com.gome.ecmall.bean.ShoppingCart.outOfStock4Gift;
import com.gome.ecmall.bean.shippingInfo;
import com.gome.ecmall.custom.DisScrollListView;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.custom.SlipButton;
import com.gome.ecmall.custom.SlipButton.OnSwitchListener;
import com.gome.ecmall.home.ShoppingCartActivity;
import com.gome.ecmall.home.groupbuy.GroupLimitOrderActivity;
import com.gome.ecmall.home.mygome.ModifyPaymentPasswordActivity;
import com.gome.ecmall.home.mygome.SetPaymentPasswordActivity;
import com.gome.ecmall.util.AppMeasurementUtils;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.DES;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 填写订单
 * 
 * @author bo.yang createData 2012_07_16
 * 
 */
public class ShoppingCartOrderActivity extends Activity implements OnClickListener {

    private static final String Tag = "ShoppingCartOrderActivity:";
    public static boolean isBackKeyRefer = false; // 判断是否是返回键

    // 支付方式标识
    private static final String PAYMENTHODETAIL_PAYMENT_ONLINEPAYMENT = "onlinePayment";// 在线支付
    private static final String PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY = "cashOnDelivery";// 货到付款
    // 货到付款 (现金orPOS机)
    private static final String PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY_POS = "POS"; // pos机刷卡
    private static final String PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY_CASH = "CASH";// 现金

    private Context context;
    private OrderSuccess myOrderSuccess = null;
    private Button common_title_btn_back, common_title_btn_right, sumberorderbutton, btnExpendAll;
    private TextView tvTitle, shopping_goods_cons_name_data, shopping_goods_cons_phone_data,
            shopping_goods_cons_address_data, shopping_goods_pay_method_data, shopping_goods_use_balance_data,
            shopping_goods_coupon_data_number, shopping_goods_order_goods_price_data,
            shopping_goods_order_freight_data, shopping_goods_order_coupon_data,
            shopping_goods_order_goods_balance_data, shopping_goods_order_goods_amount_due_data, bluetickets,
            redtickets, bluetickets_data, redtickets_data, shopping_goods_coupon_data,
            shopping_goods_coupon_data_right, mark_del_text, shopping_goods_order_goods_discount_data,
            energy_info_data, shopping_cart_energy_buytype_data, shopping_cart_energy_name_data,
            shopping_cart_energy_idcardnumber_data, shopping_cart_energy_bank_data,
            shopping_cart_energy_bank_number_data, shopping_cart_energy_name, shopping_cart_energy_idcardnumber,
            use_balance_no;
    private RelativeLayout energy_info_relativelayout, energy_info_relative, cons_info_relative, tickets_relative,
            cons_info_relativelayout, pay_method_relative, coupon_relative, mark_del_relative, prom_linear;
    private LinearLayout shopping_cart_section_prom;
    private SlipButton useBlanceSlipbutton;
    private EditText shopping_cart_edit_ordermark;
    private ImageView common_right_img, coupon_used_img, prom_linear_iv, img_shopping_iv_arrow;
    private LayoutInflater inflater;
    private ProgressBar progressBar;
    private DisScrollListView disScrollListView1;
    private boolean isShow;
    private String allowGiftOutOfStock = "N";
    private ShoppingCart_Recently_nvoiceDetail invoiceDetail; // 保存当前选择的发票;
    private String usedTicketCount; // 可使用优惠券张数
    private String totalAmount = "0.00";// 应付总金额
    private String couponAmount = "0.00";// 优惠券
    private double useBalance;// 账户余额
    private PasswordEditText editText;
    private WanceInfo orderwanceInfo;// 节能补贴数据，未从网站获取
    private String isForegoAllowance; // 是否放弃
    private String hasAllowance; // 是否节能补贴商品
    private String shoppingCartOctree;
    // 全部展开
    private boolean isAllExpend = true;
    private ShoppingCartOrderAdapter sca;
    private boolean isShowPromContent = false;
    private boolean isContainBBC = false;
    private boolean isNormalPay = true;
    public String isSelfStore = "N";
    // 购物车获取商品列表
    public ShoppingCartGoods shoppingCartGoodsList = null;
    private ShoppingCart_Recently mShoppingCart_recently = null;
    private ShoppingCart_Recently shoppingCart_recently;
    private ImageView shopping_goods_cons_info_line;
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        GlobalConfig.getInstance().setGroupLimitType(GroupLimitOrderActivity.GoodOrderType);
        // 数据统计使用
        shoppingCartOctree = getIntent().getStringExtra("shoppingCartOctree");
        // 购物车传过来数据
        shoppingCartGoodsList = (ShoppingCartGoods) getIntent().getSerializableExtra("shoppingCartGoods");
        // 设置布局文件
        setContentView(R.layout.shopping_cart_order);
        inflater = LayoutInflater.from(context);
        // 数据统计
        AppMeasurementUtils appMeasurementUtils = new AppMeasurementUtils(ShoppingCartOrderActivity.this);
        appMeasurementUtils.getUrl(getString(R.string.appMeas_shoppingCartProcess) + ":"
                + getString(R.string.appMeas_shoppingOrder), getString(R.string.appMeas_shoppingCartProcess) + ":"
                + getString(R.string.appMeas_shoppingOrder), getString(R.string.appMeas_shoppingCartProcess) + ":"
                + getString(R.string.appMeas_shoppingOrder), getString(R.string.appMeas_shoppingCartProcess) + ":"
                + getString(R.string.appMeas_shoppingOrder), "", "", AppMeasurementUtils.EVENT_SHOPPINGORDER,
                shoppingCartOctree, "", "", "", "", "", "", "", "", null);
        // 初始化页面控件
        initTitleButton();
        // 获取最近数据，初始化页面数据
        setData(null);
    };

    /**
     * 初始化商品列表
     */
    private void initRecentlyShopList(ShoppingCartGoods shoppingCartGoods, ShoppingCart_Recently shoppingCartR) {
        if (shoppingCartGoods == null || shoppingCartR == null) {
            return;
        }
        /*
         * // 初始化店铺商品列表-----------begin ArrayList<ShopingCartInfo> shopCartInfoList = shoppingCartGoods
         * .getShopCartInfoList(); // 获取店铺相关信息 ArrayList<ShopingCartInfo> shopCartInfoRecentList = shoppingCartR
         * .getShopCartInfoList(); // 整合服务器端返回数据，配送信息及发票信息 for (int i = 0; i < shopCartInfoRecentList.size(); i++) {
         * ShopingCartInfo shopingCartRecentInfo = shopCartInfoRecentList .get(i); for (int j = 0; j <
         * shopCartInfoList.size(); j++) { ShopingCartInfo shopingCartInfo = shopCartInfoList.get(j); if
         * (shopingCartInfo.getShippingId().equalsIgnoreCase( shopingCartRecentInfo.getShippingId())) {
         * shopingCartInfo.setShippingInfoOrder(shopingCartRecentInfo .getShippingInfoOrder());
         * shopingCartInfo.setInvoiceInfoOrder(shopingCartRecentInfo .getInvoiceInfoOrder()); } } }
         */
        // if (sca == null){
        sca = new ShoppingCartOrderAdapter(this, shoppingCartGoods, shoppingCartR, this);
        disScrollListView1.setAdapter(sca);
        /*
         * }else{ sca.Integrate(); sca.notifyDataSetChanged(); }
         */
        if (shoppingCartGoods != null && shoppingCartGoods.getShopCartInfoList() != null
                && shoppingCartGoods.getShopCartInfoList().size() <= 1) {
            btnExpendAll.setVisibility(View.INVISIBLE);
        } else {
            btnExpendAll.setVisibility(View.VISIBLE);
        }
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
            goToPaymentData(1);
        }
            break;
        case R.id.invoice_relativelayout: {
            if ("N".equals(hasAllowance) || "Y".equals(isForegoAllowance)) {
                Intent intent = new Intent();
                intent.putExtra(ShoppingCart.JK_SHOPPINGCART_RECENTLY_NVOICEDETAIL, this.invoiceDetail);
                intent.setClass(getApplicationContext(), ShoppingCartInvoiceInfoActivity.class);
                startActivityForResult(intent, 2);
            } else {
                CommonUtility.showMiddleToast(context, "", "节能补贴商品不可修改发票信息");
            }
        }
            break;
        case R.id.coupon_relative: {
            if (!TextUtils.isEmpty(usedTicketCount) && !"0".equals(usedTicketCount)) {
                goToCouponData();
            }
        }
            break;
        case R.id.common_title_btn_right:
        case R.id.sumberorderbutton: {
            // OrderSuccess orderSuccess = new OrderSuccess();
            // orderSuccess.setOrderId("8012530008");
            // orderSuccess.setPayAmount("4.00");
            // List<OrderPayment> orderpaymentList = new
            // ArrayList<OrderPayment>();
            // OrderPayment orderPayment = new OrderPayment();
            // orderPayment.setPayModelID(PAYMENTHODETAIL_PAYMENT_ONLINEPAYMENT);
            // orderPayment.setSubPayModelID("CASH");
            // orderpaymentList.add(orderPayment);
            // orderSuccess.setOrderpaymentList(orderpaymentList);
            // Intent intent = new Intent();
            // intent.putExtra("orderSuccess",orderSuccess);
            // intent.setAction(getClass().getName());
            // intent.setClass(getApplicationContext(),
            // ShoppingCartOrderSuccessActivity.class);
            // startActivityForResult(intent,0);
            sumberOrderData();
        }
            break;
        case R.id.common_title_btn_back:
            finish();
            break;
        case R.id.shopping_goods_order_expend_all:
            for (ShopingCartInfo shopingCartInfo : sca.mList) {
                shopingCartInfo.setExpend(isAllExpend);
            }
            isAllExpend = isAllExpend ? false : true;
            if (!isAllExpend) {
                sca.UpdateShoppingCart();
            } else {
                sca.notifyDataSetChanged();
            }
            btnExpendAll.setText(isAllExpend ? "全部展开" : "全部收起");
            break;
        case R.id.prom_linear:
            isShowPromContent = isShowPromContent ? false : true;
            if (isShowPromContent) {
                shopping_cart_section_prom.setVisibility(View.VISIBLE);
                prom_linear_iv.setBackgroundResource(R.drawable.category_arrow_up);
                prom_linear.setBackgroundResource(R.drawable.good_shop_name_top_background);
            } else {
                shopping_cart_section_prom.setVisibility(View.GONE);
                prom_linear_iv.setBackgroundResource(R.drawable.category_arrow_down);
                prom_linear.setBackgroundResource(R.drawable.more_item_single_prom_bg);
            }
            break;
        default:
            break;
        }
    }

    /**
     * 初始化页面控件
     */
    private void initTitleButton() {
        // 页面标题栏控件初始化--begin
        common_title_btn_back = (Button) findViewById(R.id.common_title_btn_back);
        common_title_btn_back.setVisibility(View.VISIBLE);
        common_title_btn_back.setText(R.string.back);
        common_title_btn_back.setBackgroundResource(R.drawable.common_top_title_btn_bg_selector);
        common_title_btn_back.setOnClickListener(this);
        common_title_btn_right = (Button) findViewById(R.id.common_title_btn_right);
        common_title_btn_right.setVisibility(View.VISIBLE);
        common_title_btn_right.setText(R.string.shopping_goods_order_goods_order_submit);
        common_title_btn_right.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.shopping_goods_order_goods_order_fill);
        // 页面标题栏控件初始化--end
        // 全部展开
        btnExpendAll = (Button) findViewById(R.id.shopping_goods_order_expend_all);
        btnExpendAll.setOnClickListener(this);
        shopping_goods_cons_info_line = (ImageView)findViewById(R.id.shopping_goods_cons_info_line);
        shopping_goods_cons_name_data = (TextView) findViewById(R.id.shopping_goods_cons_name_data);
        shopping_goods_cons_phone_data = (TextView) findViewById(R.id.shopping_goods_cons_phone_data);
        shopping_goods_cons_address_data = (TextView) findViewById(R.id.shopping_goods_cons_address_data);
        shopping_goods_pay_method_data = (TextView) findViewById(R.id.shopping_goods_pay_method_data);
        shopping_goods_use_balance_data = (TextView) findViewById(R.id.shopping_goods_use_balance_data);
        shopping_goods_coupon_data_number = (TextView) findViewById(R.id.shopping_goods_coupon_data_number);
        shopping_cart_energy_idcardnumber = (TextView) findViewById(R.id.shopping_cart_energy_idcardnumber);
        shopping_goods_order_goods_price_data = (TextView) findViewById(R.id.shopping_goods_order_goods_price_data);
        shopping_goods_order_goods_discount_data = (TextView) findViewById(R.id.shopping_goods_order_goods_discount_data);
        shopping_goods_order_freight_data = (TextView) findViewById(R.id.shopping_goods_order_freight_data);
        shopping_goods_order_coupon_data = (TextView) findViewById(R.id.shopping_goods_order_coupon_data);
        shopping_goods_order_goods_balance_data = (TextView) findViewById(R.id.shopping_goods_order_goods_balance_data);
        shopping_goods_order_goods_amount_due_data = (TextView) findViewById(R.id.shopping_goods_order_goods_amount_due_data);
        cons_info_relative = (RelativeLayout) findViewById(R.id.cons_info_relative);
        tickets_relative = (RelativeLayout) findViewById(R.id.tickets_relative);
        bluetickets = (TextView) findViewById(R.id.bluetickets);
        bluetickets_data = (TextView) findViewById(R.id.bluetickets_data);
        redtickets = (TextView) findViewById(R.id.redtickets);
        redtickets_data = (TextView) findViewById(R.id.redtickets_data);
        shopping_goods_coupon_data = (TextView) findViewById(R.id.shopping_goods_coupon_data);
        shopping_cart_section_prom = (LinearLayout) findViewById(R.id.shopping_cart_section_prom);
        prom_linear_iv = (ImageView) findViewById(R.id.prom_linear_iv);
        common_right_img = (ImageView) findViewById(R.id.common_right_img);
        disScrollListView1 = (DisScrollListView) findViewById(R.id.shopping_cart_section_lv_first);
        prom_linear = (RelativeLayout) findViewById(R.id.prom_linear);
        shopping_goods_coupon_data_right = (TextView) findViewById(R.id.shopping_goods_coupon_data_right);
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
        cons_info_relativelayout = (RelativeLayout) findViewById(R.id.cons_info_relativelayout);
        cons_info_relativelayout.setOnClickListener(this);
        pay_method_relative = (RelativeLayout) findViewById(R.id.pay_method_relative);
        img_shopping_iv_arrow = (ImageView) findViewById(R.id.img_shopping_iv_arrow);
        coupon_relative = (RelativeLayout) findViewById(R.id.coupon_relative);
        coupon_relative.setOnClickListener(this);
        coupon_used_img = (ImageView) findViewById(R.id.coupon_used_img);
        sumberorderbutton = (Button) findViewById(R.id.sumberorderbutton);
        sumberorderbutton.setOnClickListener(this);
        use_balance_no = (TextView) findViewById(R.id.use_balance_no);
        useBlanceSlipbutton = (SlipButton) findViewById(R.id.slipbutton_use_balance);
        useBlanceSlipbutton.setImageResource(R.drawable.slip_on_off_bg, R.drawable.slip_on_off_bg, R.drawable.slip);
        useBlanceSlipbutton.setSwitchState(false);
        useBlanceSlipbutton.setOnSwitchListener(new OnSwitchListener() {
            @Override
            public void onSwitched(boolean isSwitchOn) {

                if (isSwitchOn) {
                    if (shoppingCart_recently != null) {
                        if ("0".equals(shoppingCart_recently.getVirtualAccountStatus())) {
                            createRedTicketSelectDialog();
                            return;
                        } else if ("-2".equals(shoppingCart_recently.getVirtualAccountStatus())) {
                            createRedTicketSelectDialog();
                            return;
                        } else if ("-1".equals(shoppingCart_recently.getVirtualAccountStatus())) {
                            CommonUtility.showToast(ShoppingCartOrderActivity.this,
                                    shoppingCart_recently.getVirtualAccountStatusDesc());
                            return;
                        }
                    }
                    // 余额支付--非正常支付流程
                    isNormalPay = false;
                    // editText = CommonUtility.showEditDialogWithCancle(
                    // ShoppingCartOrderActivity.this, "",
                    // editLeftListener, editRightListener, false,cancleListener);
                    setData("Y");
                } else {
                    // 取消余额支付--正常支付流程
                    isNormalPay = true;
                    cancelVirtualAccountPay();
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
    }

    protected void createRedTicketSelectDialog() {
        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.shopping_cart_payment_not_select))
                .setMessage(getResources().getString(R.string.shopping_cart_payment_not_select_msg))
                .setPositiveButton(getResources().getString(R.string.Ensure), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        if ("0".equals(shoppingCart_recently.getVirtualAccountStatus())) {
                            intent.setClass(ShoppingCartOrderActivity.this, SetPaymentPasswordActivity.class);
                            startActivity(intent);
                        } else if ("-2".equals(shoppingCart_recently.getVirtualAccountStatus())) {
                            intent.putExtra("isActivated", false);
                            intent.setClass(ShoppingCartOrderActivity.this, ModifyPaymentPasswordActivity.class);
                            startActivity(intent);
                        }
                        dialog.dismiss();
                    }
                }).setNegativeButton(getResources().getString(R.string.Cancel), null)

                .create().show();
    }

    /**
     * 异步页面数据
     * 
     * @param payPassword
     *            支付密码-余额支付
     */
    private void setData(final String payPassword) {
        // 判断网络
        if (!NetUtility.isNetworkAvailable(context)) {
            CommonUtility.showMiddleToast(context, "",
                    context.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        // 异步获取最近数据及其商品配送-发票清单
        new AsyncTask<Object, Void, ShoppingCart_Recently>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(ShoppingCartOrderActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected ShoppingCart_Recently doInBackground(Object... params) {
                String result = NetUtility.NO_CONN;
                // 未使用余额支付
                if (TextUtils.isEmpty(payPassword)) {
                    result = NetUtility.sendHttpRequestByGet(Constants.URL_CHECKOUT_RECENTLY_ADDRESS);
                } else {
                    // 余额支付
                    String request = ShoppingCart.createRequestUseBalanceJson("", useBalance);
                    result = NetUtility.sendHttpRequestByPost(Constants.URL_USEBALANCE, request);
                }
                if (NetUtility.NO_CONN.equals(result) || TextUtils.isEmpty(result)) {
                    ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
                    return null;
                }
                return ShoppingCart.paserResponseShoppingCart_Recently(result);
            };

            protected void onPostExecute(ShoppingCart_Recently shoppingCart_recently) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                // 如果最近数据为空 TODO 需要对密码是否输入正确做判断
                if (shoppingCart_recently == null) {

                    CommonUtility.showMiddleToast(ShoppingCartOrderActivity.this, "", ShoppingCart.getErrorMessage());
                    useBlanceSlipbutton.setSwitchState(false);
                    // if(!isNormalPay){
                    // editText = CommonUtility.showEditDialogWithCancle(
                    // ShoppingCartOrderActivity.this, "",
                    // editLeftListener, editRightListener, false,cancleListener);
                    // }
                    return;
                }
                // 初始化页面视图数据
                initViewData(shoppingCart_recently);
            };
        }.execute();
    }

    /**
     * 初始化页面数据
     * 
     * @param shoppingCart_recently
     */
    private void initViewData(ShoppingCart_Recently shoppingCart_recently) {
        this.shoppingCart_recently = shoppingCart_recently;
        // 初始化购物车数据列表
        initRecentlyShopList(shoppingCartGoodsList, shoppingCart_recently);

        // 节能补贴信息
        initRecentlyAllowance(shoppingCart_recently);

        // 收货人信息
        initRecentlyAddress(shoppingCart_recently);

        // 支付方式信息
        initRecentlyPaymentDetail(shoppingCart_recently);

        // 优惠券-红券-蓝券
        initRecentlyUsedTicketDetail(shoppingCart_recently);

        // 使用余额
        initRecentlyBalance(shoppingCart_recently);

        // 优惠券
        initRecentlyTicket(shoppingCart_recently);

        // 订单优惠信息
        initRecentlyShoppingCartGoodsProm(shoppingCart_recently);

        // 支付结算信息
        initRecentlyPayDetail(shoppingCart_recently);

        // 我知道了
        initRecentlyIKnow(shoppingCart_recently);
    }

    /**
     * 我知道了
     * 
     * @param shoppingCart_recently
     */
    private void initRecentlyIKnow(ShoppingCart_Recently shoppingCart_recently) {
        // TODO Auto-generated method stub
        String successMessage = shoppingCart_recently.getSuccessMessage();
        if (!TextUtils.isEmpty(successMessage)) {
            CommonUtility.showAlertDialog(this, "", successMessage, "我知道了", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.cancel();
                }
            });
        }
    }

    /**
     * 结算信息
     * 
     * @param shoppingCart_recently
     */
    private void initRecentlyPayDetail(ShoppingCart_Recently shoppingCart_recently) {
        // 商品总价
        // shopping_goods_order_goods_price_data.setText("￥"
        // + (TextUtils.isEmpty(shoppingCart_recently.getTotal()) ? "0.00"
        // : shoppingCart_recently.getTotal()));
        shopping_goods_order_goods_price_data.setText("￥"
                + (TextUtils.isEmpty(shoppingCart_recently.getOrderAmount()) ? "0.00" : shoppingCart_recently
                        .getOrderAmount()));
        // 订单优惠
        shopping_goods_order_goods_discount_data.setText("￥"
                + (TextUtils.isEmpty(shoppingCart_recently.getDiscountAmount()) ? "0.00" : shoppingCart_recently
                        .getDiscountAmount()));
        // 运费
        shopping_goods_order_freight_data
                .setText("￥"
                        + (TextUtils.isEmpty(shoppingCart_recently.getFreight()) ? "0.00" : shoppingCart_recently
                                .getFreight()));
        // 优惠券
        shopping_goods_order_coupon_data.setText("￥"
                + (TextUtils.isEmpty(shoppingCart_recently.getCouponAmount()) ? "0.00" : shoppingCart_recently
                        .getCouponAmount()));
        // 余额
        shopping_goods_order_goods_balance_data.setText("￥"
                + (TextUtils.isEmpty(shoppingCart_recently.getPayBalance()) ? "0.00" : shoppingCart_recently
                        .getPayBalance()));
        // 应付金额
        shopping_goods_order_goods_amount_due_data.setText("￥"
                + (TextUtils.isEmpty(shoppingCart_recently.getPayAmount()) ? "0.00" : shoppingCart_recently
                        .getPayAmount()));
        totalAmount = shoppingCart_recently.getPayAmount();
        couponAmount = shoppingCart_recently.getCouponAmount();
    }

    /**
     * 红蓝劵使用情况
     * 
     * @param shoppingCart_recently
     */
    private void initRecentlyTicket(ShoppingCart_Recently shoppingCart_recently) {
        usedTicketCount = shoppingCart_recently.getUsedTicketCount();
        if (TextUtils.isEmpty(usedTicketCount) || "0".equals(usedTicketCount.trim())) {
            shopping_goods_coupon_data.setText(R.string.shopping_goods_coupon_no);
            shopping_goods_coupon_data_number.setVisibility(View.GONE);
            shopping_goods_coupon_data_right.setVisibility(View.GONE);
            coupon_used_img.setVisibility(View.GONE);
        } else {
            shopping_goods_coupon_data.setText(R.string.shopping_goods_coupon_left);
            shopping_goods_coupon_data_number.setVisibility(View.VISIBLE);
            shopping_goods_coupon_data_right.setVisibility(View.VISIBLE);
            shopping_goods_coupon_data_number.setText(usedTicketCount + "张");
            coupon_used_img.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 余额支付
     * 
     * @param shoppingCart_recently
     */
    private void initRecentlyBalance(ShoppingCart_Recently shoppingCart_recently) {
        // useBlanceSlipbutton.updateSwitchState(false);
        if (TextUtils.isEmpty(shoppingCart_recently.getBalance())) {
            useBalance = 0.0;
        } else {
            try {
                useBalance = Double.parseDouble(shoppingCart_recently.getBalance());
            } catch (Exception e) {
                e.printStackTrace();
                useBalance = 0.0;
            }
        }
        shopping_goods_use_balance_data.setText(NumberFormat.getCurrencyInstance().format(useBalance));
        if ("N".equals(shoppingCart_recently.getIsBalanceAvailable())) {
            useBlanceSlipbutton.setVisibility(View.GONE);
            use_balance_no.setText(shoppingCart_recently.getUnAvailableReason());
            use_balance_no.setVisibility(View.VISIBLE);
        } else if ("Y".equals(shoppingCart_recently.getIsBalanceAvailable())) {
            use_balance_no.setVisibility(View.GONE);
            if (0 == useBalance) {
                useBlanceSlipbutton.setEnabled(false);
            } else {
                useBlanceSlipbutton.setEnabled(true);
            }
            useBlanceSlipbutton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化节能补贴数据
     * 
     * @param shoppingCart_recently
     */
    private void initRecentlyAllowance(ShoppingCart_Recently shoppingCart_recently) {
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
    }

    /**
     * 初始化最近的节能补贴
     * 
     * @param wanceInfo
     */
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

    /**
     * 初始化最近的收货地址
     * 
     * @param rec_address
     */
    private void initRecentlyAddress(ShoppingCart_Recently shoppingCart_recently) {

        mShoppingCart_recently = shoppingCart_recently;

        ShoppingCart_Recently_address rec_address = shoppingCart_recently.getRec_address();
        if (rec_address == null

                || (TextUtils.isEmpty(rec_address.getConsignee()) && TextUtils.isEmpty(rec_address.getMobile())
                        && TextUtils.isEmpty(rec_address.getPhone()) && TextUtils.isEmpty(rec_address.getAddress()))) {
            cons_info_relative.setVisibility(View.GONE);
            shopping_goods_cons_info_line.setVisibility(View.GONE);
        } else {
            cons_info_relative.setVisibility(View.VISIBLE);
            shopping_goods_cons_info_line.setVisibility(View.VISIBLE);
            shopping_goods_cons_name_data.setText(rec_address.getConsignee());
            String phone = rec_address.getPhone();
            String mobile = rec_address.getMobile();
            String showPhoneMoble = "";
            if (!TextUtils.isEmpty(mobile))
                showPhoneMoble = mobile;
            else
                showPhoneMoble = phone;
            shopping_goods_cons_phone_data.setText(showPhoneMoble);
            shopping_goods_cons_address_data.setText(CommonUtility.ToDBC(rec_address.getProvinceName()
                    + rec_address.getCityName() + rec_address.getDistrictName() + rec_address.getAddress()));
        }
    }

    /**
     * 初始化最近的支付方式
     * 
     * @param paymentDetail
     */
    private void initRecentlyPaymentDetail(ShoppingCart_Recently shoppingCart_recently) {
        // 支付方式--如果店铺列表大于1，则包含店铺；1为国美
        int ShopCartInfoListSize = shoppingCart_recently.getShopCartInfoList().size();
        if (ShopCartInfoListSize > 0) {
            isContainBBC = ShopCartInfoListSize > 1 ? true : ShopCartInfoListSize == 1
                    && shoppingCart_recently.getShopCartInfoList().get(0).getIsGome().equalsIgnoreCase("Y") ? false
                    : true;
        }
        ShoppingCart_Recently_paymentDetail paymentDetail = shoppingCart_recently.getPaymentDetail();

        // 国美商品支持货到付款，店铺商品只支持在线支付
        if (!isContainBBC) {// 国美商品
            if (paymentDetail != null) {
                List<PaymentDetail_paymentMethods> paymentMethodsList = paymentDetail.getPaymentMethods();
                if (paymentMethodsList != null) {
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0, size = paymentMethodsList.size(); i < size; i++) {
                        PaymentDetail_paymentMethods paymentMethods = paymentMethodsList.get(i);
                        if (paymentMethods != null) {
                            sb.append(paymentMethods.getPaymentMethodDesc());
                            if (PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY.equals(paymentMethods.getPaymentMethod())) {
                                if (PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY_POS.equals(paymentMethods.getPosOrCash()))
                                    sb.append("(POS)");
                                else if (PAYMENTHODETAIL_PAYMENT_CASHONDELIVERY_CASH.equals(paymentMethods
                                        .getPosOrCash())) {
                                    sb.append("(现金)");
                                }
                            }
                            if (i < size - 1) {
                                sb.append("+");
                            }
                        }

                    }
                    shopping_goods_pay_method_data.setText(sb.toString());
                    pay_method_relative.setOnClickListener(this);
                    img_shopping_iv_arrow.setVisibility(View.VISIBLE);
                } else {
                    pay_method_relative.setOnClickListener(this);
                    img_shopping_iv_arrow.setVisibility(View.VISIBLE);
                }
            } else {
                pay_method_relative.setOnClickListener(this);
                img_shopping_iv_arrow.setVisibility(View.VISIBLE);
            }
        } else {
            // shopping_goods_pay_method_data.setText(paymentDetail.get);
            img_shopping_iv_arrow.setVisibility(View.GONE);
            StringBuilder sbStr = new StringBuilder();
            if (paymentDetail != null) {
                List<PaymentDetail_paymentMethods> paymentMethodsList = paymentDetail.getPaymentMethods();
                if (paymentMethodsList != null && paymentMethodsList.size() > 0) {
                    for (int j = 0 , size = paymentMethodsList.size(); j < size; j++) {
                        PaymentDetail_paymentMethods paymentMethods = paymentMethodsList.get(j);
                        if (sbStr.length() == 0)
                            sbStr.append(paymentMethods.getPaymentMethodDesc());
                        else
                            sbStr.append("+" + paymentMethods.getPaymentMethodDesc());
                    }
                }
            }
            shopping_goods_pay_method_data.setText(sbStr.toString());
        }

    }

    /**
     * 初始化最近的红蓝券使用情况
     * 
     * @param usedTicketDetail
     */
    private void initRecentlyUsedTicketDetail(ShoppingCart_Recently shoppingCart_recently) {

        ShoppingCart_Recently_usedTicketDetail usedTicketDetail = shoppingCart_recently.getUsedTicketDetail();
        if (usedTicketDetail != null) {
            List<UsedTicketDetail_usedRedTickets> usedRedTicketsList = usedTicketDetail.getUsedRedTicketsList();
            List<UsedTicketDetail_usedBlueTickets> usedBlueTicketsList = usedTicketDetail.getUsedBlueTickets();
            if ((usedRedTicketsList == null && usedBlueTicketsList == null)
                    || ((usedRedTicketsList != null && usedRedTicketsList.size() == 0) && (usedBlueTicketsList != null && usedBlueTicketsList
                            .size() == 0))) {
                tickets_relative.setVisibility(View.GONE);
            } else {
                tickets_relative.setVisibility(View.VISIBLE);
                if (usedBlueTicketsList != null && usedBlueTicketsList.size() != 0) {
                    StringBuffer strBuffer = new StringBuffer();
                    for (int i = 0, size = usedBlueTicketsList.size(); i < size; i++) {
                        UsedTicketDetail_usedBlueTickets usedBlueTickets = usedBlueTicketsList.get(i);
                        strBuffer.append("￥".concat(usedBlueTickets.getAmount()).concat("*")
                                .concat(usedBlueTickets.getQuantity()).concat("张"));
                        if (i < size - 1) {
                            strBuffer.append("\n");
                        }
                    }
                    bluetickets_data.setText(strBuffer.toString());
                    bluetickets.setVisibility(View.VISIBLE);
                    bluetickets_data.setVisibility(View.VISIBLE);
                } else {
                    bluetickets.setVisibility(View.GONE);
                    bluetickets_data.setVisibility(View.GONE);
                }
                if (usedRedTicketsList != null && usedRedTicketsList.size() != 0) {
                    StringBuffer strBuffer = new StringBuffer();
                    for (int i = 0, size = usedRedTicketsList.size(); i < size; i++) {
                        UsedTicketDetail_usedRedTickets usedRedTickets = usedRedTicketsList.get(i);
                        strBuffer.append("￥".concat(usedRedTickets.getAmount()).concat("*")
                                .concat(usedRedTickets.getQuantity()).concat("张"));
                        if (i < size - 1) {
                            strBuffer.append("\n");
                        }
                    }
                    redtickets_data.setText(strBuffer.toString());
                    redtickets.setVisibility(View.VISIBLE);
                    redtickets_data.setVisibility(View.VISIBLE);
                } else {
                    redtickets.setVisibility(View.GONE);
                    redtickets_data.setVisibility(View.GONE);
                }
            }
        } else {
            tickets_relative.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化购物车商品促销信息
     * 
     * @param shoppingCartOrderpromList
     * @param shppingCartGoodsList_zen_goods
     */
    private void initRecentlyShoppingCartGoodsProm(ShoppingCart_Recently shoppingCart_recently) {
        /*
         * List<OrderProm> shoppingCartOrderpromList = (List<OrderProm>) getIntent()
         * .getSerializableExtra("shoppingCartOrderpromList");
         */
        List<OrderProm> shoppingCartOrderpromList = shoppingCartGoodsList.getOrderpromList();
        @SuppressWarnings("unchecked")
        List<Goods> shppingCartGoodsList_zen_goods = (List<Goods>) getIntent().getSerializableExtra(
                "shppingCartGoodsList_zen_goods");
        if ((shoppingCartOrderpromList == null || shoppingCartOrderpromList.size() == 0)
                && (shppingCartGoodsList_zen_goods == null || shppingCartGoodsList_zen_goods.size() == 0)) {
            prom_linear.setVisibility(View.GONE);
            shopping_cart_section_prom.setVisibility(View.GONE);
            return;
        } else {
            shopping_cart_section_prom.setVisibility(View.GONE);
            // 默认向下箭头
            prom_linear_iv.setBackgroundResource(R.drawable.category_arrow_down);
            // 添加绑定事件用来收起与隐藏
            prom_linear.setOnClickListener(this);
        }
        if (shopping_cart_section_prom != null && shopping_cart_section_prom.getChildCount() != 0) {
            shopping_cart_section_prom.removeAllViews();
        }
        if (shppingCartGoodsList_zen_goods != null) {
            for (int i = 0, zen_goodsSize = shppingCartGoodsList_zen_goods.size(); i < zen_goodsSize; i++) {
                Goods zen_goods = shppingCartGoodsList_zen_goods.get(i);
                View goods_zengView = inflater.inflate(R.layout.shopping_cart1_section_item_item2, null);
                /*
                 * TextView shopping_cart_type = (TextView) goods_zengView .findViewById(R.id.shopping_cart_type);
                 */
                TextView shopping_cart_type_data = (TextView) goods_zengView.findViewById(R.id.shopping_cart_type_data);
                // 设置促销类型
                /*
                 * shopping_cart_type.setText(CommonUtility.getPromTypeDesc(this, zen_goods.getGoodsType()));
                 * shopping_cart_type_data.setText(zen_goods.getSkuName() + "*" + zen_goods.getGoodsCount());
                 */
                shopping_cart_type_data.setText(Html.fromHtml("<font color=\""
                        + CommonUtility.getPromTypeColor(context, zen_goods.getGoodsType()) + "\"" + ">"
                        + CommonUtility.getPromTypeDesc(context, zen_goods.getGoodsType()) + "</font>"
                        + zen_goods.getSkuName() + "*" + zen_goods.getGoodsCount()));
                shopping_cart_section_prom.addView(goods_zengView);
            }
        }
        if (shoppingCartOrderpromList != null) {
            for (int i = 0, orderpromSize = shoppingCartOrderpromList.size(); i < orderpromSize; i++) {
                OrderProm orderProm = shoppingCartOrderpromList.get(i);
                View goods_promView = inflater.inflate(R.layout.shopping_cart1_section_item_item2, null);
                /*
                 * TextView shopping_cart_type = (TextView) goods_promView .findViewById(R.id.shopping_cart_type);
                 */
                TextView shopping_cart_type_data = (TextView) goods_promView.findViewById(R.id.shopping_cart_type_data);

                shopping_cart_type_data.setText(Html.fromHtml("<font color=\""
                        + CommonUtility.getPromTypeColor(context, orderProm.getPromType()) + "\"" + ">"
                        + CommonUtility.getPromTypeDesc(context, orderProm.getPromType()) + "</font>"
                        + orderProm.getPromDesc()));

                /*
                 * switch (Integer.parseInt(orderProm.getPromType())) { case 1: shopping_cart_type.setText("[直降]");
                 * shopping_cart_type_data.setText(orderProm.getPromDesc()); break; case 2:
                 * shopping_cart_type.setText("[折扣]"); shopping_cart_type_data.setText(orderProm.getPromDesc()); break;
                 * default: shopping_cart_type.setText("[促销]");
                 * shopping_cart_type_data.setText(orderProm.getPromDesc()); break; }
                 */
                shopping_cart_section_prom.addView(goods_promView);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
        case 0:
            setExpendFalse();
        case 1:
        case 2:
        case 3:
            setExpendFalse();
        case 7: {
            if (!isBackKeyRefer)
                setData(null);
            isBackKeyRefer = false;
        }
            break;
        case 4: {
            Intent intent = new Intent(this, ShoppingCartActivity.class);
            setResult(4, intent);
            finish();
        }
            break;
        case 5:
            finish();
            break;
        case 6: {
            Intent intent = new Intent(this, ShoppingCartActivity.class);
            setResult(5, intent);
            finish();
        }
        case 8: {
            if (myOrderSuccess == null) {
                myOrderSuccess = new OrderSuccess();
                myOrderSuccess.setIsOutOfShipping(data.getStringExtra("isOutOfShipping"));
                myOrderSuccess.setOutOfShippingList((ArrayList<ShoppingCart.OutOfStockGoods>) data
                        .getSerializableExtra("outOfShippingList"));
            }
            Intent intent = new Intent();
            intent.putExtra("orderSuccess", myOrderSuccess);
            intent.setClass(getApplicationContext(), ShoppingCartActivity.class);
            setResult(0, intent);
            finish();
        }
            break;
        case 9: {
            Intent intent = new Intent(this, ShoppingCartActivity.class);
            setResult(9, intent);
            finish();
        }
            break;
        default:
            break;
        }
    }

    /**
     * 异步获取可使用的优惠券
     */
    private void goToCouponData() {
        if (!NetUtility.isNetworkAvailable(context)) {

            CommonUtility.showMiddleToast(context, "",
                    context.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, CouponTicket_CanUsed>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(ShoppingCartOrderActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected CouponTicket_CanUsed doInBackground(Object... params) {
                String result = NetUtility.sendHttpRequestByGet(Constants.URL_ORDER_COUPON);
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
                    return null;
                }
                return ShoppingCart.paserResponseShoppingCart_CanUsedTicket(result);
            };

            protected void onPostExecute(CouponTicket_CanUsed canUsedTicket) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                if (canUsedTicket == null) {
                    CommonUtility.showMiddleToast(ShoppingCartOrderActivity.this, "", ShoppingCart.getErrorMessage());
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("totalAmount", totalAmount);
                intent.putExtra("couponAmount", couponAmount);
                intent.putExtra("canUsedTicket", canUsedTicket);
                intent.setClass(getApplicationContext(), ShoppingCartCouponActivity.class);
                startActivityForResult(intent, 3);
            };
        }.execute();
    }

    /**
     * 异步支付及配送方式: 0 支付方式 1 配送方式
     * 
     * @param paymentShippingType
     */
    private void goToPaymentData(final int paymentShippingType) {
        if (!NetUtility.isNetworkAvailable(context)) {
            CommonUtility.showMiddleToast(context, "",
                    context.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, ShoppintCart_payMentList>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(ShoppingCartOrderActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected ShoppintCart_payMentList doInBackground(Object... params) {
                String result = NetUtility.sendHttpRequestByGet(Constants.URL_CHECKOUT_QUERY_PAYMENT_SUPPORT);
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
                    CommonUtility.showMiddleToast(ShoppingCartOrderActivity.this, "", ShoppingCart.getErrorMessage());
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

    /**
     * 异步获取收货人信息
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
                progressDialog = CommonUtility.showLoadingDialog(ShoppingCartOrderActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected ShoppingCart_ConsInfo_address doInBackground(Object... params) {
                String result = NetUtility.sendHttpRequestByGet(Constants.URL_CHECKOUT_ADDRESS_LIST);
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
                    CommonUtility.showMiddleToast(ShoppingCartOrderActivity.this, "", ShoppingCart.getErrorMessage());
                    return;
                }
                if (shoppingCart_consInfo_address.getAddressList() == null
                        || shoppingCart_consInfo_address.getAddressList().size() == 0) {
                    Intent intent = new Intent();
                    intent.setAction("ShoppingCartOrderActivity");
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

    /**
     * 提交订单数据
     */
    private void sumberOrderData() {

        // 配送方式校验
        boolean distributionOK = false;
        boolean distributionUnArrive = false;
        if (shoppingCartGoodsList != null && shoppingCartGoodsList.getShopCartInfoList() != null
                && shoppingCartGoodsList.getShopCartInfoList().size() > 0) {
            for (ShopingCartInfo sci : shoppingCartGoodsList.getShopCartInfoList()) {
                shippingInfo si = sci.getShippingInfoOrder();
                if (si != null) {
                    // 是否有到达不了的
                    if (si.getShippingMethodArray() == null) {
                        distributionUnArrive = true;
                    }
                    // 配送方式信息是否完整
                    distributionOK = TextUtils.isEmpty(si.getIsNeedConfirm())
                            || TextUtils.isEmpty(si.getShippingTime()) || TextUtils.isEmpty(si.getShippingMethod())
                            || TextUtils.isEmpty(si.getShippingMethodName()) ? false : true;
                }
                if (!distributionOK || distributionUnArrive) {
                    break;
                }
            }
        }
        /*
         * // 校验配送是否可以到达 if (distributionUnArrive) { CommonUtility.showMiddleToast(context, "",
         * "配送方式不能到达您的收货地址，请重新编辑收货地址！"); return; }
         */

        // 发票校验
        boolean InvoiceOK = false;
        if (shoppingCartGoodsList != null && shoppingCartGoodsList.getShopCartInfoList() != null
                && shoppingCartGoodsList.getShopCartInfoList().size() > 0) {
            for (ShopingCartInfo sci : shoppingCartGoodsList.getShopCartInfoList()) {
                ShoppingCart_Recently_nvoiceDetail si = sci.getInvoiceInfoOrder();
                if (si != null)
                    InvoiceOK = TextUtils.isEmpty(si.getContext()) || TextUtils.isEmpty(si.getHead())
                            || TextUtils.isEmpty(si.getHeadTypeDesc()) ? false : true;
                if (!InvoiceOK) {
                    break;
                }
            }
        }
        // 未放弃--节能补贴
        if (orderwanceInfo != null && "Y".equals(hasAllowance) && "N".equals(isForegoAllowance)) {
            if (TextUtils.isEmpty(shopping_cart_energy_buytype_data.getText())
                    || TextUtils.isEmpty(shopping_cart_energy_name_data.getText())
                    || TextUtils.isEmpty(shopping_cart_energy_idcardnumber_data.getText())
                    || TextUtils.isEmpty(shopping_cart_energy_bank_data.getText())
                    || TextUtils.isEmpty(shopping_cart_energy_bank_number_data.getText())) {
                CommonUtility.showMiddleToast(context, "", context.getString(R.string.shopping_cart_order_wance));
                return;
            }
        }
        // 联系人信息
        if (TextUtils.isEmpty(shopping_goods_cons_name_data.getText())
                || TextUtils.isEmpty(shopping_goods_cons_phone_data.getText())
                || TextUtils.isEmpty(shopping_goods_cons_address_data.getText())) {
            CommonUtility.showMiddleToast(context, "", context.getString(R.string.shopping_cart_order_consinfo));
            return;
        } else if (TextUtils.isEmpty(shopping_goods_pay_method_data.getText())) {
            CommonUtility.showMiddleToast(context, "", context.getString(R.string.shopping_cart_order_payment));
            return;
        }
        // 配送方式验证 TODO
        else if (!distributionOK) {
            CommonUtility.showMiddleToast(context, "", context.getString(R.string.shopping_cart_order_shippment));
            return;
        }

        // 发票 验证 TODO
        else if (!InvoiceOK) {
            CommonUtility.showMiddleToast(context, "", context.getString(R.string.shopping_cart_order_invoice));
            return;
        }

        if (!NetUtility.isNetworkAvailable(context)) {
            CommonUtility.showMiddleToast(context, "",
                    context.getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        // 需要支付密码
        if ("Y".equalsIgnoreCase(mShoppingCart_recently.getIsNeedPayPassword())) {
            editText = CommonUtility.showEditDialogWithCancle(ShoppingCartOrderActivity.this, "", editLeftListener,
                    editRightListener, false, cancleListener);
        } else {// 不需要支付密码
            submitOrder("");
        }
    }

    /**
     * 提交订单
     */
    private void submitOrder(final String password) {
        new AsyncTask<Object, Void, OrderSuccess>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(ShoppingCartOrderActivity.this,
                        getString(R.string.loading), false, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(false);
                            }
                        });
            };

            protected OrderSuccess doInBackground(Object... params) {
                String orderMark = shopping_cart_edit_ordermark.getText().toString();
                if (TextUtils.isEmpty(orderMark)) {
                    orderMark = "";
                }
                String body = ShoppingCart.reqSubmitOrder(orderMark, allowGiftOutOfStock, password);
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_CHECKOUT__SUBMIT, body);
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
                    return null;
                }
                return ShoppingCart.paserResponseShoppingCart_OrderSubmit(result);
            };

            protected void onPostExecute(OrderSuccess orderSuccess) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                // 返回订单信息为空，提示购物车错误
                if (orderSuccess == null) {
                    CommonUtility.showMiddleToast(ShoppingCartOrderActivity.this, "", ShoppingCart.getErrorMessage());
                    return;
                }

                myOrderSuccess = orderSuccess;

                // 门店自提检测
                if ("Y".equals(orderSuccess.getIsOutOfPickingup())) {
                    List<OutOfStockGoods> outOfPickingup = myOrderSuccess.getOutOfPickingupList();
                    if (outOfPickingup != null && outOfPickingup.size() > 0) {
                        StringBuffer sb = new StringBuffer();
                        for (int i = 0, size = outOfPickingup.size(); i < size; i++) {
                            OutOfStockGoods outOfShipping = outOfPickingup.get(i);
                            sb.append(outOfShipping.getSkuName());

                            sb.append("\n");
                        }
                        CommonUtility.showConfirmDialog(ShoppingCartOrderActivity.this,
                                "购物车中有商品无法自提：建议您使用 国美快递 或 删除商品", sb.toString(), "修改配送方式",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ShopingCartInfo gome = null;
                                        if (mShoppingCart_recently != null) {
                                            for (ShopingCartInfo sCartInfo : mShoppingCart_recently
                                                    .getShopCartInfoList()) {
                                                if (sCartInfo.getIsGome().equalsIgnoreCase("Y")) {
                                                    gome = sCartInfo;
                                                }
                                            }
                                        }
                                        if (gome != null) {
                                            Intent intent = new Intent();
                                            intent.putExtra("paymentlist", gome.getShippingInfoOrder());
                                            intent.putExtra("shippingId",
                                                    gome.getShippingId() == null ? "" : gome.getShippingId());
                                            intent.putExtra("isGome", gome.getIsGome() == null ? "" : gome.getIsGome());
                                            intent.setClass(getApplicationContext(),
                                                    ShoppingCartShippingMethodActivity.class);
                                            ShoppingCartOrderActivity.this.startActivityForResult(intent, 1);
                                        }
                                    }
                                }, "修改购物车", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intent = new Intent();
                                        intent.putExtra("orderSuccess", myOrderSuccess);
                                        intent.setClass(getApplicationContext(), ShoppingCartActivity.class);
                                        setResult(0, intent);
                                        finish();
                                    }
                                });
                    } else {
                        CommonUtility.showAlertDialog(ShoppingCartOrderActivity.this, "", "您选择的商品无法提供自提，请用国美快递！",
                                "修改配送方式", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ShopingCartInfo gome = null;
                                        if (mShoppingCart_recently != null) {
                                            for (ShopingCartInfo sCartInfo : mShoppingCart_recently
                                                    .getShopCartInfoList()) {
                                                if (sCartInfo.getIsGome().equalsIgnoreCase("Y")) {
                                                    gome = sCartInfo;
                                                }
                                            }
                                        }
                                        if (gome != null) {
                                            Intent intent = new Intent();
                                            intent.putExtra("paymentlist", gome.getShippingInfoOrder());
                                            intent.putExtra("shippingId",
                                                    gome.getShippingId() == null ? "" : gome.getShippingId());
                                            intent.putExtra("isGome", gome.getIsGome() == null ? "" : gome.getIsGome());
                                            intent.setClass(getApplicationContext(),
                                                    ShoppingCartShippingMethodActivity.class);
                                            ShoppingCartOrderActivity.this.startActivityForResult(intent, 1);
                                        }
                                    }
                                });
                    }
                }

                // 配送方式检测
                if ("Y".equals(orderSuccess.getIsOutOfShipping())) {
                    List<OutOfStockGoods> outOfShippingList = myOrderSuccess.getOutOfShippingList();
                    if (outOfShippingList != null && outOfShippingList.size() > 0) {
                        StringBuffer sb = new StringBuffer();
                        for (int i = 0, size = outOfShippingList.size(); i < size; i++) {
                            OutOfStockGoods outOfShipping = outOfShippingList.get(i);
                            sb.append(outOfShipping.getSkuName());

                            sb.append("\n");
                        }
                        // 配送方式失败--修改购物车
                        CommonUtility.showConfirmDialog(context,
                                getString(R.string.shopping_goods_order_so_sorry_shipping),
                                getString(R.string.shopping_goods_order_so_sorry_shipping_su) + "\n" + sb.toString(),
                                getString(R.string.shopping_goods_order_modify_shopping),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 返回购物车
                                        dialog.dismiss();
                                        Intent intent = new Intent();
                                        intent.putExtra("orderSuccess", myOrderSuccess);
                                        intent.setClass(getApplicationContext(), ShoppingCartActivity.class);
                                        setResult(0, intent);
                                        finish();
                                    }
                                }, getString(R.string.shopping_goods_order_modify_address),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 修改地址
                                        dialog.dismiss();
                                        /*
                                         * Intent intent = new Intent();
                                         * intent.putExtra("shoppingCart_consInfo_address",mShoppingCart_recently.get);
                                         * intent.setClass(getApplicationContext(),
                                         * ShoppingCartOrderConsInfoActivity.class); startActivityForResult(intent, 8);
                                         */
                                        goToConsInfoData();
                                    }
                                });
                        return;
                    }
                }

                // 商品缺货检测
                if ("Y".equals(orderSuccess.getIsOutOfStock())) {
                    // 获取商品缺货列表
                    List<OutOfStockGoods> outOfStockGoodsList = myOrderSuccess.getOutOfStockGoods();
                    // 商品缺货列表不为空 TODO 此处代码存在存在逻辑问题
                    if (outOfStockGoodsList != null && outOfStockGoodsList.size() != 0) {
                        boolean isOutOfStock = false;
                        List<OutOfStockGoods> outOfStockGoodsStockList = new ArrayList<OutOfStockGoods>();
                        for (int i = 0, size = outOfStockGoodsList.size(); i < size; i++) {
                            OutOfStockGoods outOfStockGoods = outOfStockGoodsList.get(i);
                            String stockNum = outOfStockGoods.getStockNum();
                            if (TextUtils.isEmpty(stockNum) || "0".equals(stockNum)) {
                                isOutOfStock = false;
                            } else {
                                outOfStockGoodsStockList.add(outOfStockGoods);
                                isOutOfStock = true;
                            }
                        }
                        if (isOutOfStock) {
                            StringBuffer sb = new StringBuffer();
                            for (int i = 0, size = outOfStockGoodsStockList.size(); i < size; i++) {
                                OutOfStockGoods outOfStockGoods = outOfStockGoodsStockList.get(i);
                                sb.append(outOfStockGoods.getSkuName());
                                sb.append(Html.fromHtml("<font color=\"red\">(" + outOfStockGoods.getStockNum()
                                        + ")</font>"));
                                sb.append("\n");
                            }
                            // 赠品缺货--修改购物车
                            CommonUtility.showAlertDialog(context, getString(R.string.shopping_goods_order_so_sorry),
                                    sb.toString(), getString(R.string.shopping_goods_order_modify_shopping),
                                    btnListener);
                        } else {// 缺货提示--商品库存数与收货地址有关
                            CommonUtility.showAlertDialog(context,
                                    getString(R.string.shopping_goods_order_outstock_goods),
                                    getString(R.string.shopping_goods_order_outstock_inve_address),
                                    getString(R.string.shopping_goods_order_modify_shopping), btnListener);
                        }
                    } else {
                        // 缺货提示--缺货类表为空的情况下
                        CommonUtility.showAlertDialog(context, "",
                                getString(R.string.shopping_goods_order_outstock_goods),
                                getString(R.string.shopping_goods_order_modify_shopping), btnListener);
                    }
                    return;
                } else {
                    // TODO 此处代码存在可能存在逻辑问题
                    if (!"Y".equals(orderSuccess.getIsOutOfStock()) && !orderSuccess.getIsSuccess()
                            && !"Y".equals(orderSuccess.getIsoutofStock4Gift())) {
                        // 购物车 数据错误
                        CommonUtility.showAlertDialog(context, "", ShoppingCart.getErrorMessage(),
                                getString(R.string.confirm), colseListener);
                        return;
                    } else if (!orderSuccess.getIsSuccess() && !"Y".equals(orderSuccess.getIsoutofStock4Gift())) {
                        // 购物车 数据错误
                        CommonUtility.showMiddleToast(context, "", ShoppingCart.getErrorMessage());
                        return;
                    }
                }

                // 赠品缺货检测
                List<outOfStock4Gift> outOfStock4GiftList = orderSuccess.getOutOfStock4Gift();
                if (outOfStock4GiftList != null && outOfStock4GiftList.size() != 0
                        && "Y".equals(orderSuccess.getIsoutofStock4Gift())) {
                    // 赠品取货--单可以提交订单
                    CommonUtility.showConfirmDialog(context, "", getString(R.string.shopping_goods_order_so_sorry),
                            getString(R.string.shopping_goods_order_cancel_submit), colseListener,
                            getString(R.string.shopping_goods_order_goods_order_submit), rightListener);
                    ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
                    return;
                }

                // 提交订单
                if (!TextUtils.isEmpty(orderSuccess.getOrderId())) {
                    Intent intent = new Intent();
                    intent.putExtra("orderSuccess", orderSuccess);
                    intent.setAction("ShoppingCartOrderActivity");
                    intent.putExtra("shoppingCartOctree", shoppingCartOctree);
                    // 门店自提标识位
                    intent.putExtra("isSelfStore", isSelfStore);
                    ShoppingCartOrderSuccessActivity.orderSuccess = null;
                    intent.setClass(getApplicationContext(), ShoppingCartOrderSuccessActivity.class);
                    startActivityForResult(intent, 4);
                }
            };
        }.execute();
    }

    /**
     * 取消虚拟账户支付
     */
    private void cancelVirtualAccountPay() {
        if (!NetUtility.isNetworkAvailable(context)) {
            CommonUtility.showMiddleToast(context, "",
                    context.getString(R.string.can_not_conntect_network_please_check_network_settings));
            useBlanceSlipbutton.updateSwitchState(true);
            return;
        }
        new AsyncTask<Object, Void, ShoppingCart_Recently>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(ShoppingCartOrderActivity.this,
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
                result = NetUtility.sendHttpRequestByGet(Constants.URL_CANCELUSEBALANCE);
                BDebug.i("虚拟账户支付", result);
                if (NetUtility.NO_CONN.equals(result)) {
                    ShoppingCart.setErrorMessage(getString(R.string.data_load_fail_exception));
                    useBlanceSlipbutton.updateSwitchState(true);
                    return null;
                }
                return ShoppingCart.paserResponseShoppingCart_Recently(result);
            };

            protected void onPostExecute(ShoppingCart_Recently shoppingCart_recently) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    useBlanceSlipbutton.updateSwitchState(true);
                    return;
                }
                if (shoppingCart_recently == null) {
                    CommonUtility.showMiddleToast(ShoppingCartOrderActivity.this, "", ShoppingCart.getErrorMessage());
                    useBlanceSlipbutton.updateSwitchState(true);
                    return;
                }
                initViewData(shoppingCart_recently);
            };
        }.execute();
    }

    private android.content.DialogInterface.OnClickListener btnListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            dialog.dismiss();
            Intent intent = new Intent();
            // /*************模拟数据****start******/
            // OutOfStockGoods outOfStockGoods1 = new OutOfStockGoods();
            // outOfStockGoods1.setSkuID("sku1400035");
            // OutOfStockGoods outOfStockGoods2 = new OutOfStockGoods();
            // outOfStockGoods2.setSkuID("sku1400033");
            // List<OutOfStockGoods> outOfStockGoodsList = new
            // ArrayList<OutOfStockGoods>();
            // // outOfStockGoodsList.add(outOfStockGoods1);
            // outOfStockGoodsList.add(outOfStockGoods2);
            // myOrderSuccess = new OrderSuccess();
            // myOrderSuccess.setOutOfStockGoods(outOfStockGoodsList);
            // /*************模拟数据****end******/
            intent.putExtra("orderSuccess", myOrderSuccess);
            intent.setClass(getApplicationContext(), ShoppingCartActivity.class);
            setResult(0, intent);
            finish();
        }

    };

    private android.content.DialogInterface.OnClickListener leftListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            dialog.dismiss();
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), ShoppingCartActivity.class);
            setResult(0, intent);
            finish();
        }

    };
    private android.content.DialogInterface.OnClickListener rightListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            dialog.dismiss();
            allowGiftOutOfStock = "Y";
            sumberOrderData();
        }

    };

    private android.content.DialogInterface.OnClickListener colseListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            dialog.dismiss();
        }

    };

    private android.content.DialogInterface.OnClickListener editLeftListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            if (editText != null && !TextUtils.isEmpty(editText.getString().trim())) {
                dialog.dismiss();
                String payPassword = editText.getString().trim();
                try {
                    submitOrder(DES.encryptDES(payPassword, Constants.LOGINDESKEY));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (TextUtils.isEmpty(editText.getString().trim())) {
                // 当输入密码为空，开启余额支付开关关闭
                // useBlanceSlipbutton.updateSwitchState(false);
                CommonUtility.showMiddleToast(ShoppingCartOrderActivity.this, "",
                        getString(R.string.shopping_cart_used_paypassword));
            }

        }

    };

    private OnCancelListener cancleListener = new OnCancelListener() {

        @Override
        public void onCancel(DialogInterface dialog) {
            // 密码对话框弹出后当取消该对话框，余额支付开关关闭
            // useBlanceSlipbutton.updateSwitchState(false);
        }
    };

    private android.content.DialogInterface.OnClickListener editRightListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            dialog.cancel();
            // useBlanceSlipbutton.updateSwitchState(false);
        }

    };

    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ShoppingCartActivity.isBackKeyRefer = true;
        }
        return super.onKeyDown(keyCode, event);
    };

    /**
     * 收回商品选项卡
     */
    public void setExpendFalse() {
        if (shoppingCartGoodsList != null && shoppingCartGoodsList.getShopCartInfoList() != null
                && shoppingCartGoodsList.getShopCartInfoList().size() > 0) {
            for (ShopingCartInfo sci : shoppingCartGoodsList.getShopCartInfoList()) {
                sci.setExpend(false);
            }
        }
        isAllExpend = true;
        btnExpendAll.setText(isAllExpend ? "全部展开" : "全部收起");
    }

}
