package com.gome.ecmall.home.groupbuy;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.OrderCancel;
import com.gome.ecmall.bean.PhoneRecharge;
import com.gome.ecmall.bean.VirtualGroupOrderDetail;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.mygome.CouponRuleDetailActivity;
import com.gome.ecmall.home.mygome.OrderDetailsService;
import com.gome.ecmall.phonerecharge.PhoneRechargeOrderSubmitSuccessActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;
/**
 * 新版团购虚拟订单详情
 * @author liuyang-ds
 *
 */
public class VirtualGroupOrderDetailActivity extends Activity implements OnClickListener {
    private Button bt_back;
    private TextView tv_title;
    private Button bt_right;
    private ScrollView sl_detail_main;
    private TextView tv_order_num_data;
    private TextView tv_order_state_data;
    private Button bt_detail_change;
    private TextView tv_buy_time_data;
    private TextView tv_buy_price_data;
    private TextView tv_goods_num_data;
    private TextView tv_goods_name_data;
    private TextView tv_goods_count_data;
    private TextView tv_goods_price_data;
    private TextView tv_goods_total_price_data;
    private TextView tv_pay_type_data;
    private TextView tv_goods_pay_price_data;
    //private TextView tv_account_pay_data;
    private TextView tv_order_pay_data;
    private RelativeLayout rl_good_list;
    private String orderID;
    /**
     * 跳转需要传值，做全局变量
     */
    private VirtualGroupOrderDetail result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.virtual_group_order_detail);
        orderID = getIntent().getStringExtra(JsonInterface.JK_ORDER_ID);
        initializeViews();// 初始化控件
        setData();// 设置数据
    }

    /**
     * 设置数据
     */
    private void setData() {
        if (!NetUtility.isNetworkAvailable(VirtualGroupOrderDetailActivity.this)) {
            CommonUtility.showMiddleToast(VirtualGroupOrderDetailActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            sl_detail_main.setVisibility(View.GONE);
            return;
        }
        new AsyncTask<Object, Void, VirtualGroupOrderDetail>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(VirtualGroupOrderDetailActivity.this,
                        getString(R.string.loading), true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected VirtualGroupOrderDetail doInBackground(Object... params) {
                String request = VirtualGroupOrderDetail.createOrderDetailJson(orderID);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_NEW_GROUPBUY_VRITUAL_ORDER_DETAILS,
                        request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return VirtualGroupOrderDetail.parseOrderDetail(response);
            }

            @Override
            protected void onPostExecute(VirtualGroupOrderDetail result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                if (result == null) {
                    CommonUtility.showMiddleToast(VirtualGroupOrderDetailActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                sl_detail_main.setVisibility(View.VISIBLE);
                // 给控件赋值
                VirtualGroupOrderDetailActivity.this.result = result;
                setViewsData(result);

            }

        }.execute();
    }
    /**
     * 给控件赋值
     */
    protected void setViewsData(VirtualGroupOrderDetail result) {
        tv_order_state_data.setText(transform(result.getOrderStatue()));
        tv_order_num_data.setText(result.getOrderID());
        tv_buy_time_data.setText(result.getSumitTime());
        tv_buy_price_data.setText("￥"+result.getOrderPrice());
        tv_goods_num_data.setText(result.getGoodsNo());
        tv_goods_name_data.setText(result.getGoodName());
        tv_goods_count_data.setText(result.getGoodsCount());
        tv_goods_price_data.setText("￥"+result.getGoodPrice());
        tv_goods_total_price_data.setText("￥"+result.getTotalPrice());
        tv_pay_type_data.setText(result.getPaymentMethodDesc());
        tv_goods_pay_price_data.setText("￥"+result.getGoodTotalPrice());
        //tv_account_pay_data.setText("-￥"+result.getBalance());
        tv_order_pay_data.setText("￥"+result.getTotalPrice());
    }
/**
 * 把订单的英文状态转化成中文
 * @param orderStatue
 * @return
 */
    private String transform(String orderStatue) {
        if(TextUtils.isEmpty(orderStatue)){
            return null;
        }else{
            if("INITIAL".equals(orderStatue)){
                bt_right.setVisibility(View.VISIBLE);
                bt_detail_change.setVisibility(View.VISIBLE);
                bt_right.setText("取消订单");
                bt_detail_change.setText("立即支付");
                bt_right.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //取消订单
                        CommonUtility.showConfirmDialog(VirtualGroupOrderDetailActivity.this, "",
                                getString(R.string.yes_or_no_cancel_order), getString(R.string.confirm), leftListener,
                                getString(R.string.cancel), rightListener);
                    }
                });
                bt_detail_change.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      //进入支付页面
                        Intent intent = new Intent();
                        intent.setClass(VirtualGroupOrderDetailActivity.this, PhoneRechargeOrderSubmitSuccessActivity.class);
                        intent.putExtra(PhoneRecharge.NUM, result.getGoodsCount());
                        intent.putExtra(PhoneRecharge.ORDERNUM, result.getOrderID());
                        intent.putExtra(PhoneRecharge.PAYMONEY, result.getOrderPrice());
                        intent.putExtra(PhoneRecharge.GOODNAME, result.getGoodName());
                        intent.putExtra(PhoneRecharge.FROMPAGE, "VirtualGroupOrderDetailActivity");
                        VirtualGroupOrderDetailActivity.this.startActivityForResult(intent,100);
                    }
                });
                return "未支付";
            }else if("SETTLED".equals(orderStatue)){
                bt_right.setVisibility(View.VISIBLE);
                bt_detail_change.setVisibility(View.VISIBLE);
                bt_right.setText("使用帮助");
                bt_detail_change.setText("再次购买");
                bt_right.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //团购卷使用流程
                        Intent intent = new Intent(VirtualGroupOrderDetailActivity.this,CouponRuleDetailActivity.class);
                        intent.putExtra(CouponRuleDetailActivity.TYPE, "2");
                        startActivity(intent);
                    }
                });
                bt_detail_change.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //再次购买
                        Intent intent = new Intent(VirtualGroupOrderDetailActivity.this,NewGroupBuyDetailActivity.class);
                        intent.putExtra(NewGroupBuyDetailActivity.SALEPROMOITEM, result.getSalePromoItem());
                        startActivity(intent);
                        
                        
                    }
                });
                return "已支付";
            }else if("REMOVED".equals(orderStatue)){
                bt_right.setVisibility(View.INVISIBLE);
                bt_detail_change.setVisibility(View.GONE);
                return "已取消";
            }
        }
        return null;
    }

    /**
     * 初始化控件
     */
    private void initializeViews() {
        bt_back = (Button) this.findViewById(R.id.common_title_btn_back);
        tv_title = (TextView) this.findViewById(R.id.common_title_tv_text);
        bt_right = (Button) this.findViewById(R.id.common_title_btn_right);
        sl_detail_main = (ScrollView) this.findViewById(R.id.sl_detail_main);
        tv_order_num_data = (TextView) this.findViewById(R.id.tv_order_num_data);
        tv_order_state_data = (TextView) this.findViewById(R.id.tv_order_state_data);
        bt_detail_change = (Button) this.findViewById(R.id.bt_detail_change);
        tv_buy_time_data = (TextView) this.findViewById(R.id.tv_buy_time_data);
        tv_buy_price_data = (TextView) this.findViewById(R.id.tv_buy_price_data);
        tv_goods_num_data = (TextView) this.findViewById(R.id.tv_goods_num_data);
        tv_goods_name_data = (TextView) this.findViewById(R.id.tv_goods_name_data);
        tv_goods_count_data = (TextView) this.findViewById(R.id.tv_goods_count_data);
        tv_goods_price_data = (TextView) this.findViewById(R.id.tv_goods_price_data);
        tv_goods_total_price_data = (TextView) this.findViewById(R.id.tv_goods_total_price_data);
        tv_pay_type_data = (TextView) this.findViewById(R.id.tv_pay_type_data);
        tv_goods_pay_price_data = (TextView) this.findViewById(R.id.tv_goods_pay_price_data);
        //tv_account_pay_data = (TextView) this.findViewById(R.id.tv_account_pay_data);
        tv_order_pay_data = (TextView) this.findViewById(R.id.tv_order_pay_data);
        rl_good_list = (RelativeLayout) this.findViewById(R.id.rl_good_list);
        bt_back.setText("返回");
        bt_back.setVisibility(View.VISIBLE);
        bt_back.setOnClickListener(this);
        tv_title.setText("订单详情");
        tv_title.setVisibility(View.VISIBLE);
        rl_good_list.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.common_title_btn_back:
            finish();
            break;
        case R.id.rl_good_list:
            Intent intent = new Intent(VirtualGroupOrderDetailActivity.this,NewGroupBuyDetailActivity.class);
            intent.putExtra(NewGroupBuyDetailActivity.SALEPROMOITEM, result.getSalePromoItem());
            startActivity(intent);
            break;
        default:
            break;
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
        case  4:// 继续购物跳转到团购列表
            setResult(4);
            finish();
            break;
        default:
            break;
        }
    }
    private android.content.DialogInterface.OnClickListener leftListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            cancelOrder(result.getOrderID());
        }
    };
    private android.content.DialogInterface.OnClickListener rightListener = new android.content.DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    };
    private void cancelOrder(final String orderId) {
        new CancelOrderTask(VirtualGroupOrderDetailActivity.this, orderId).execute();
    }

    private class CancelOrderTask extends AsyncTask<String, Void, OrderCancel> {

        private Context context;
        private LoadingDialog dialog;
        private String orderId;

        public CancelOrderTask(Context ctx, String orderId) {
            context = ctx;
            this.orderId = orderId;
        }

        @Override
        protected void onPreExecute() {
            if (!NetUtility.isNetworkAvailable(context)) {
                CommonUtility.showMiddleToast(context, null,
                        context.getString(R.string.data_load_fail_please_check_network_settings));
                return;
            }
            dialog = CommonUtility.showLoadingDialog(context, context.getString(R.string.loading), true,
                    new OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            cancel(true);
                        }
                    });
        }

        @Override
        protected OrderCancel doInBackground(String... params) {
            String json = OrderDetailsService.createCancelOrderJson(orderId);
            String result = NetUtility.sendHttpRequestByPost(Constants.URL_ORDER_CANCEL_ORDER, json);
            return OrderDetailsService.parseJsonCancelOrder(result);
        }

        @Override
        protected void onPostExecute(OrderCancel result) {

            if (context != null && !((Activity) context).isFinishing() && dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (result == null || result.equals("")) {
                CommonUtility.showMiddleToast(context, null, context.getString(R.string.data_load_fail_exception));
                return;
            }

            if (result.getFlag().equalsIgnoreCase("Y")) {
                bt_right.setVisibility(View.INVISIBLE);
                bt_detail_change.setVisibility(View.GONE);
                CommonUtility.showMiddleToast(context, null, context.getString(R.string.cancel_order_ok));
            } else if (result.getFlag().equalsIgnoreCase("N")) {
                String err = result.getErrorMessage();
                String fail = result.getFailReason();
                if (fail != null) {
                    CommonUtility.showMiddleToast(context, null, fail);
                } else {
                    if (err != null && !err.equals("")) {
                        CommonUtility.showMiddleToast(context, null, err);
                    }
                }
            } else {
                CommonUtility.showMiddleToast(context, null, context.getString(R.string.cancel_order_err));
            }

        }
    }
}
