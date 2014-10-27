package com.gome.ecmall.phonerecharge;

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
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.PhoneRecharge;
import com.gome.ecmall.bean.ShoppingCart;
import com.gome.ecmall.bean.ShoppingCart.YinLian;
import com.gome.ecmall.bean.ShoppingCart.ZhiFuBao;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.groupbuy.VirtualGroupOrderPaySuccessActivity;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.ecmall.util.PreferenceUtils;
import com.gome.ecmall.zhifubao.BaseHelper;
import com.gome.ecmall.zhifubao.MobileSecurePayHelper;
import com.gome.ecmall.zhifubao.MobileSecurePayHelper.UpdateAlipayWalletListener;
import com.gome.ecmall.zhifubao.MobileSecurePayer;
import com.gome.ecmall.zhifubao.ResultChecker;
import com.gome.eshopnew.R;
import com.unionpay.UPPayAssistEx;

public class PhoneRechargeOrderSubmitSuccessActivity extends Activity implements OnClickListener {
    private Button common_title_btn_back;
    private Button common_title_btn_right;
    private TextView common_title_tv_text;
    private TextView tv_phone_recharge_order_submit_success_ordernum;
    private LinearLayout phone_recharge_order_product_name_ll;// 虚拟团购商品名称区域 手机充值不显示
    private TextView phone_recharge_order_product_name;// 虚拟团购商品名称
    private TextView tv_phone_recharge_order_submit_success_pay_money;
    private Button bt_phone_recharge_order_submit_success_go_pay;
    private RadioGroup rg_phone_recharge_order_submit_success_pay_type;
    private String num;
    private String orderNum;
    private String payMoney;
    private String fromPage;
    /**
     * 虚拟团购有商品名称
     */
    private String goodName;
    private int payType;
    // 支付宝
    private static final int PAYMENT_ONLINEPAYMENT_PLAT_ZHIFUBAO = 1;
    // 银联
    private static final int PAYMENT_ONLINEPAYMENT_PLAT_YILIAN = 3;

    // 支付宝
    private ProgressDialog mProgress = null;

    // 银联支付控件状态
    private static final int PLUGIN_NOT_INSTALLED = -1;
    private static final int PLUGIN_NEED_UPGRADE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_recharge_order_submit_success);
        num = getIntent().getStringExtra(PhoneRecharge.NUM);
        orderNum = getIntent().getStringExtra(PhoneRecharge.ORDERNUM);
        payMoney = getIntent().getStringExtra(PhoneRecharge.PAYMONEY);
        fromPage = getIntent().getStringExtra(PhoneRecharge.FROMPAGE);
        goodName = getIntent().getStringExtra(PhoneRecharge.GOODNAME);
        initializeView();// 初始化控件
        setData();

    }

    /**
     * 设置数据
     */
    private void setData() {
        tv_phone_recharge_order_submit_success_ordernum.setText(orderNum);
        tv_phone_recharge_order_submit_success_pay_money.setText("￥" + payMoney);
        if (!TextUtils.isEmpty(goodName)) {
            phone_recharge_order_product_name.setText(goodName);
        }
    }

    /**
     * 初始化控件
     */
    private void initializeView() {
        common_title_btn_back = (Button) this.findViewById(R.id.common_title_btn_back);
        common_title_btn_right = (Button) this.findViewById(R.id.common_title_btn_right);
        common_title_tv_text = (TextView) this.findViewById(R.id.common_title_tv_text);
        tv_phone_recharge_order_submit_success_ordernum = (TextView) this
                .findViewById(R.id.tv_phone_recharge_order_submit_success_ordernum);
        phone_recharge_order_product_name_ll = (LinearLayout) this
                .findViewById(R.id.phone_recharge_order_product_name_ll);
        if (TextUtils.isEmpty(goodName)) {
            phone_recharge_order_product_name_ll.setVisibility(View.GONE);
        } else {
            phone_recharge_order_product_name_ll.setVisibility(View.VISIBLE);
        }
        phone_recharge_order_product_name = (TextView) this.findViewById(R.id.phone_recharge_order_product_name);
        tv_phone_recharge_order_submit_success_pay_money = (TextView) this
                .findViewById(R.id.tv_phone_recharge_order_submit_success_pay_money);
        bt_phone_recharge_order_submit_success_go_pay = (Button) this
                .findViewById(R.id.bt_phone_recharge_order_submit_success_go_pay);
        rg_phone_recharge_order_submit_success_pay_type = (RadioGroup) this
                .findViewById(R.id.rg_phone_recharge_order_submit_success_pay_type);
        common_title_btn_back.setVisibility(View.VISIBLE);
        common_title_btn_back.setText(getString(R.string.back));
        common_title_btn_back.setOnClickListener(this);
        common_title_btn_right.setVisibility(View.VISIBLE);
        common_title_btn_right.setText(getString(R.string.cancel));
        common_title_btn_right.setOnClickListener(this);
        common_title_tv_text.setVisibility(View.VISIBLE);
        common_title_tv_text.setText(getString(R.string.phone_recharge_order_submit_success));
        bt_phone_recharge_order_submit_success_go_pay.setOnClickListener(this);
        rg_phone_recharge_order_submit_success_pay_type.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                case R.id.rb_phone_recharge_order_submit_success_online_zhifubao:
                    payType = PAYMENT_ONLINEPAYMENT_PLAT_ZHIFUBAO;
                    break;
                case R.id.rb_phone_recharge_order_submit_success_online_yinlian:
                    payType = PAYMENT_ONLINEPAYMENT_PLAT_YILIAN;
                    break;
                default:
                    break;
                }

            }
        });
        payType = PAYMENT_ONLINEPAYMENT_PLAT_ZHIFUBAO;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.common_title_btn_back:
            if (TextUtils.isEmpty(fromPage)) {
                finish();
            } else {
                if ("PhoneRechargeOrderConfirmActivity".equals(fromPage)
                        || "VirtualGroupSubmitOrderActivity".equals(fromPage)) {
                    // 返回充值信息填写页面 或者虚拟团购详情页
                    Intent intent1 = new Intent();
                    intent1.putExtra(GlobalConfig.CLASS_NAME, "PhoneRechargeOrderSubmitSuccessActivity");
                    setResult(2, intent1);
                    finish();
                    // Intent intentPhoneRecharge = new
                    // Intent(PhoneRechargeOrderSubmitSuccessActivity.this,PhoneRechargeActivity.class);
                    // startActivity(intentPhoneRecharge);
                } else {
                    // 从订单列表过来的
                    finish();
                }
            }

            break;
        case R.id.common_title_btn_right:
            // 取消返回首页
            if (!"VirtualGroupOrderDetailActivity".equals(fromPage)) {
                Intent intent = new Intent();
                setResult(2, intent);
            }
            finish();
            break;
        case R.id.bt_phone_recharge_order_submit_success_go_pay:
            // 去支付
            // onlinePayment(payType);
            if ("VirtualGroupOrderDetailActivity".equals(fromPage)
                    || "VirtualGroupSubmitOrderActivity".equals(fromPage)) {
                Intent intent1 = new Intent(PhoneRechargeOrderSubmitSuccessActivity.this,
                        VirtualGroupOrderPaySuccessActivity.class);
                intent1.putExtra(PhoneRecharge.NUM, num);
                intent1.putExtra(PhoneRecharge.ORDERNUM, orderNum);
                intent1.putExtra(PhoneRecharge.PAYMONEY, payMoney);
                intent1.putExtra(PhoneRecharge.PAYTYPE, PAYMENT_ONLINEPAYMENT_PLAT_ZHIFUBAO);
                intent1.putExtra(PhoneRecharge.FROMPAGE, fromPage);
                startActivityForResult(intent1, 0);
            } else {
                Intent intent1 = new Intent(PhoneRechargeOrderSubmitSuccessActivity.this,
                        PhoneRechargeOrderPaySuccessActivity.class);
                intent1.putExtra(PhoneRecharge.NUM, num);
                intent1.putExtra(PhoneRecharge.ORDERNUM, orderNum);
                intent1.putExtra(PhoneRecharge.PAYMONEY, payMoney);
                intent1.putExtra(PhoneRecharge.PAYTYPE, PAYMENT_ONLINEPAYMENT_PLAT_ZHIFUBAO);
                startActivityForResult(intent1, 0);
            }
            break;
        default:
            break;
        }

    }

    /**
     * 获取调去支付方式的参数信息
     * 
     * @param payOrderMessage
     * @param payment_plat_type
     */
    private void onlinePayment(final int payment_plat_type) {
        if (!NetUtility.isNetworkAvailable(PhoneRechargeOrderSubmitSuccessActivity.this)) {
            CommonUtility.showMiddleToast(PhoneRechargeOrderSubmitSuccessActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, Object>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(PhoneRechargeOrderSubmitSuccessActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected Object doInBackground(Object... params) {
                String orderId = orderNum;
                PreferenceUtils.getInstance(getApplicationContext());
                String profileID = PreferenceUtils.getStringValue(JsonInterface.JK_PROFILE_ID, "");
                String body = PhoneRecharge.reqOnLinePayOrderMessage(orderId, profileID, payment_plat_type);
                String url = "";
                switch (payment_plat_type) {
                // 支付宝请求数据
                case PAYMENT_ONLINEPAYMENT_PLAT_ZHIFUBAO:
                    if ("VirtualGroupOrderDetailActivity".equals(fromPage)
                            || "VirtualGroupSubmitOrderActivity".equals(fromPage)) {
                        url = Constants.URL_ONLINE_ALIPAY;
                    } else {
                        url = Constants.PHONE_RECHARGE_URL_ONLINE_ALIPAY;
                    }
                    break;
                // 银联请求数据
                case PAYMENT_ONLINEPAYMENT_PLAT_YILIAN:
                    if ("VirtualGroupOrderDetailActivity".equals(fromPage)
                            || "VirtualGroupSubmitOrderActivity".equals(fromPage)) {
                        url = Constants.URL_ONLINE_UNIONPAY;
                    } else {
                        url = Constants.PHONE_RECHARGE_URL_ONLINE_UNIONPAY;
                    }
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
                    CommonUtility.showMiddleToast(PhoneRechargeOrderSubmitSuccessActivity.this, "",
                            ShoppingCart.getErrorMessage());
                    return;
                }
                switch (payment_plat_type) {
                case PAYMENT_ONLINEPAYMENT_PLAT_ZHIFUBAO:
                    ZhiFuBao zhifubao = (ZhiFuBao) obj;
                    // 参数信息: 商户ID,账户ID,订单号,商品名称,商品描述,本次支付总金额,商家提供的URL,上述订单签名,签名类型
                    // String zhifubaoinfo = "partner=\"" + zhifubao.getPartner() + "\"&seller=\"" +
                    // zhifubao.getSeller()
                    // + "\"&out_trade_no=\"" + zhifubao.getOut_trade_no() + "\"&subject=\""
                    // + zhifubao.getSubject() + "\"&body=\"" + zhifubao.getBody() + "\"&total_fee=\""
                    // + zhifubao.getTotal_fee() + "\"&notify_url=\"" + zhifubao.getNotify_url() + "\"&sign=\""
                    // + zhifubao.getSign() + "\"&sign_type=\"" + zhifubao.getSign_type() + "\"";
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

                    // 这里传入使用支付宝快捷支付插件包名 或者 支付宝钱包快捷支付包名
                    // 如果是从支付宝钱包启动应用的包名为支付宝钱包
                    // AlipayUserInfo alipayUserInfo = AlipayUserInfo.getInstance();
                    final MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(
                            PhoneRechargeOrderSubmitSuccessActivity.this);
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
                                boolean bRet = msp.pay(zhifubaoinfo, handler, 0,
                                        PhoneRechargeOrderSubmitSuccessActivity.this, true);
                                if (bRet) {
                                    // show the progress bar to indicate that we have started
                                    // paying.
                                    closeProgress();
                                    mProgress = BaseHelper.showProgress(PhoneRechargeOrderSubmitSuccessActivity.this,
                                            null, getString(R.string.shopping_goods_order_payment_ing), false, true);
                                }
                            } else {// 更新地址为空的时候，支付宝钱包为最新版本，调用支付宝钱包进行支付
                                boolean bRet = msp.pay(zhifubaoinfo, handler, 0,
                                        PhoneRechargeOrderSubmitSuccessActivity.this, false);
                                if (bRet) {
                                    // show the progress bar to indicate that we have started
                                    // paying.
                                    closeProgress();
                                    mProgress = BaseHelper.showProgress(PhoneRechargeOrderSubmitSuccessActivity.this,
                                            null, getString(R.string.shopping_goods_order_payment_ing), false, true);
                                }
                            }
                        }
                    });
                    mspHelper.detectMobile_sp("com.eg.android.AlipayGphone");
                    // if (!isMobile_spExist)
                    // return;
                    // } else {
                    // 非支付宝钱包用户没有短token，顾只能使用支付宝快捷支付服务
                    // start the pay.
                    // boolean isMobile_spExist = mspHelper.detectMobile_sp("com.alipay.android.app");
                    // if (!isMobile_spExist)
                    // return;
                    // boolean bRet = msp.pay(zhifubaoinfo, handler, 0, PhoneRechargeOrderSubmitSuccessActivity.this,
                    // true);
                    // if (bRet) {
                    // // show the progress bar to indicate that we have started
                    // // paying.
                    // closeProgress();
                    // mProgress = BaseHelper.showProgress(PhoneRechargeOrderSubmitSuccessActivity.this, null,
                    // getString(R.string.shopping_goods_order_payment_ing), false, true);
                    // }
                    // }
                    break;
                // 银联
                case PAYMENT_ONLINEPAYMENT_PLAT_YILIAN:
                    YinLian yilian = (YinLian) obj;
                    /*************************************************
                     * 
                     * 步骤2：通过银联工具类启动支付插件
                     * 
                     ************************************************/
                    // mMode参数解释：
                    // 0 - 启动银联正式环境
                    // 1 - 连接银联测试环境
                    int ret = UPPayAssistEx.startPay(PhoneRechargeOrderSubmitSuccessActivity.this, null, null,
                            yilian.getTradeNo(), Constants.MMODE);
                    if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
                        // 需要重新安装控件
                        BDebug.e("uppay", " plugin not found or need upgrade!!!");

                        AlertDialog.Builder diagle = new AlertDialog.Builder(
                                PhoneRechargeOrderSubmitSuccessActivity.this);
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
                                PhoneRechargeOrderSubmitSuccessActivity.this.startActivity(userComintent);
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
                    BDebug.e("uppay", "" + ret);
                    break;
                default:
                    break;
                }
            }
        }.execute();
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
            switch (msg.what) {
            case 0:
                if (TextUtils.isEmpty(strRet))
                    return;
                closeProgress();
                String memo = "memo=";
                int imemoStart = strRet.indexOf("memo=");
                imemoStart += memo.length();
                int imemoEnd = strRet.indexOf(";result=");
                memo = strRet.substring(imemoStart, imemoEnd);
                try {

                    // Intent intent = new Intent(PhoneRechargeOrderSubmitSuccessActivity.this,
                    // PhoneRechargeOrderPaySuccessActivity.class);
                    // intent.putExtra("num", num);
                    // intent.putExtra("payMoney", payMoney);
                    // intent.putExtra("orderNum", orderNum);
                    // intent.putExtra("payType", PAYMENT_ONLINEPAYMENT_PLAT_YILIAN);
                    // startActivityForResult(intent, 0);

                    ResultChecker resultChecker = new ResultChecker(strRet);
                    int retVal = resultChecker.checkSign();
                    if (retVal == ResultChecker.RESULT_CHECK_SIGN_FAILED) {
                        BaseHelper.showDialog(PhoneRechargeOrderSubmitSuccessActivity.this, "提示", getResources()
                                .getString(R.string.check_sign_failed), android.R.drawable.ic_dialog_alert);
                    } else if (resultChecker.isPayOk()) { // 跳转到支付成功页面
                        if ("VirtualGroupOrderDetailActivity".equals(fromPage)
                                || "VirtualGroupSubmitOrderActivity".equals(fromPage)) {
                            Intent intent1 = new Intent(PhoneRechargeOrderSubmitSuccessActivity.this,
                                    VirtualGroupOrderPaySuccessActivity.class);
                            intent1.putExtra(PhoneRecharge.NUM, num);
                            intent1.putExtra(PhoneRecharge.ORDERNUM, orderNum);
                            intent1.putExtra(PhoneRecharge.PAYMONEY, payMoney);
                            intent1.putExtra(PhoneRecharge.PAYTYPE, PAYMENT_ONLINEPAYMENT_PLAT_ZHIFUBAO);
                            startActivityForResult(intent1, 0);
                        } else {
                            Intent intent1 = new Intent(PhoneRechargeOrderSubmitSuccessActivity.this,
                                    PhoneRechargeOrderPaySuccessActivity.class);
                            intent1.putExtra(PhoneRecharge.NUM, num);
                            intent1.putExtra(PhoneRecharge.ORDERNUM, orderNum);
                            intent1.putExtra(PhoneRecharge.PAYMONEY, payMoney);
                            intent1.putExtra(PhoneRecharge.PAYTYPE, PAYMENT_ONLINEPAYMENT_PLAT_ZHIFUBAO);
                            startActivityForResult(intent1, 0);
                        }
                    } else {
                        if (!"{}".equals(memo)) {
                            BaseHelper.showDialog(PhoneRechargeOrderSubmitSuccessActivity.this, "提示",
                                    memo.substring(1, memo.length() - 1), R.drawable.infoicon);

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (!"{}".equals(memo)) {
                        BaseHelper.showDialog(PhoneRechargeOrderSubmitSuccessActivity.this, "提示",
                                memo.substring(1, memo.length() - 1), R.drawable.infoicon);
                    }
                }
                break;

            default:
                break;
            }
        };
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
        // 银联支付页面返回结果 返回固定resultCode 8888
        // case 88888:
        // if (requestCode == PAYMENT_ONLINEPAYMENT_PLAT_YILIAN) {
        // /*
        // * Intent intent = new Intent(PhoneRechargeOrderSubmitSuccessActivity.this,
        // * PhoneRechargeOrderPaySuccessActivity.class); intent.putExtra(PhoneRecharge.NUM, num);
        // * intent.putExtra(PhoneRecharge.ORDERNUM, orderNum); intent.putExtra(PhoneRecharge.PAYMONEY, payMoney);
        // * intent.putExtra(PhoneRecharge.PAYTYPE, PAYMENT_ONLINEPAYMENT_PLAT_YILIAN);
        // * startActivityForResult(intent, 0);
        // */
        //
        // String msg = data.getStringExtra("resultMessage");
        // String code = data.getStringExtra("resultCode");
        //
        // if ("0000".equals(code)) {
        // if("VirtualGroupOrderDetailActivity".equals(fromPage)||"VirtualGroupSubmitOrderActivity".equals(fromPage)){
        // Intent intent1 = new Intent(PhoneRechargeOrderSubmitSuccessActivity.this,
        // VirtualGroupOrderPaySuccessActivity.class);
        // intent1.putExtra(PhoneRecharge.NUM, num);
        // intent1.putExtra(PhoneRecharge.ORDERNUM, orderNum);
        // intent1.putExtra(PhoneRecharge.PAYMONEY, payMoney);
        // intent1.putExtra(PhoneRecharge.PAYTYPE, PAYMENT_ONLINEPAYMENT_PLAT_YILIAN);
        // intent1.putExtra(PhoneRecharge.FROMPAGE, fromPage);
        // startActivityForResult(intent1, 0);
        // }else{
        // Intent intent1 = new Intent(PhoneRechargeOrderSubmitSuccessActivity.this,
        // PhoneRechargeOrderPaySuccessActivity.class);
        // intent1.putExtra(PhoneRecharge.NUM, num);
        // intent1.putExtra(PhoneRecharge.ORDERNUM, orderNum);
        // intent1.putExtra(PhoneRecharge.PAYMONEY, payMoney);
        // intent1.putExtra(PhoneRecharge.PAYTYPE, PAYMENT_ONLINEPAYMENT_PLAT_YILIAN);
        // startActivityForResult(intent1, 0);
        // }
        // } else if ("1001".equals(code)) {
        // CommonUtility.showMiddleToast(this, "", msg);
        // } else if ("1002".equals(code)) {
        // CommonUtility.showMiddleToast(this, "", msg);
        // }
        //
        // }
        // break;
        case 2:
            if (!"VirtualGroupOrderDetailActivity".equals(fromPage)) {
                Intent intent = new Intent();
                setResult(2, intent);
            }
            finish();
            break;
        case 3:
            setResult(3);// 继续购物跳转到团购列表
            finish();
            break;
        case 4:
            setResult(4);
            finish();// 到团购券，将此模块界面都关闭
            break;
        }
        /*
         * Intent intent = new Intent(PhoneRechargeOrderSubmitSuccessActivity.this,
         * PhoneRechargeOrderPaySuccessActivity.class); intent.putExtra(PhoneRecharge.NUM, num);
         * intent.putExtra(PhoneRecharge.ORDERNUM, orderNum); intent.putExtra(PhoneRecharge.PAYMONEY, payMoney);
         * intent.putExtra(PhoneRecharge.PAYTYPE, PAYMENT_ONLINEPAYMENT_PLAT_YILIAN); startActivityForResult(intent, 0);
         */

        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        if(data == null){
            return;
        }
        String str = data.getStringExtra("pay_result");

        if ("success".equals(str)) {
            if ("VirtualGroupOrderDetailActivity".equals(fromPage)
                    || "VirtualGroupSubmitOrderActivity".equals(fromPage)) {
                Intent intent1 = new Intent(PhoneRechargeOrderSubmitSuccessActivity.this,
                        VirtualGroupOrderPaySuccessActivity.class);
                intent1.putExtra(PhoneRecharge.NUM, num);
                intent1.putExtra(PhoneRecharge.ORDERNUM, orderNum);
                intent1.putExtra(PhoneRecharge.PAYMONEY, payMoney);
                intent1.putExtra(PhoneRecharge.PAYTYPE, PAYMENT_ONLINEPAYMENT_PLAT_YILIAN);
                intent1.putExtra(PhoneRecharge.FROMPAGE, fromPage);
                startActivityForResult(intent1, 0);
            } else {
                Intent intent1 = new Intent(PhoneRechargeOrderSubmitSuccessActivity.this,
                        PhoneRechargeOrderPaySuccessActivity.class);
                intent1.putExtra(PhoneRecharge.NUM, num);
                intent1.putExtra(PhoneRecharge.ORDERNUM, orderNum);
                intent1.putExtra(PhoneRecharge.PAYMONEY, payMoney);
                intent1.putExtra(PhoneRecharge.PAYTYPE, PAYMENT_ONLINEPAYMENT_PLAT_YILIAN);
                startActivityForResult(intent1, 0);
            }
        } else if ("fail".equals(str)) {
            CommonUtility.showMiddleToast(this, "", getString(R.string.shopping_cart_pay_error));
        } else if ("cancel".equals(str)) {
            CommonUtility.showMiddleToast(this, "", getString(R.string.shopping_cart_cancel_pay));
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (TextUtils.isEmpty(fromPage)) {
                finish();
            } else {
                if ("PhoneRechargeOrderConfirmActivity".equals(fromPage)
                        || "VirtualGroupSubmitOrderActivity".equals(fromPage)) {
                    // 返回充值信息填写页面
                    Intent intent1 = new Intent();
                    intent1.putExtra(GlobalConfig.CLASS_NAME, "PhoneRechargeOrderSubmitSuccessActivity");
                    setResult(2, intent1);
                    finish();
                    // Intent intentPhoneRecharge = new
                    // Intent(PhoneRechargeOrderSubmitSuccessActivity.this,PhoneRechargeActivity.class);
                    // startActivity(intentPhoneRecharge);
                } else {
                    // 从订单列表过来的
                    finish();
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
