package com.gome.ecmall.shopping;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.ShoppingCart.OrderSuccess;
import com.gome.eshopnew.R;

/**
 * 订单在线支付成功
 * 
 * @author bo.yang
 * 
 */
public class ShoppingCartOrderPaySuccessActivity extends Activity implements OnClickListener {

    // 支付宝
    private static final int PAYMENT_ONLINEPAYMENT_PLAT_ZHIFUBAO = 1;
    // 银联
    private static final int PAYMENT_ONLINEPAYMENT_PLAT_YILIAN = 3;

    private Button common_title_btn_back, common_title_btn_right, shopping_cart_look_button, shopping_cart_go_button,
            shopping_cart_help_button;
    private TextView tvTitle, shopping_goods_order_success_ordernum_data, shopping_goods_order_success_orderprice_data,
            shopping_goods_order_success_paymethod_data, shopping_cart_store_mes;
    private Intent myIntent;
    private RelativeLayout shopping_phone_relative;
    private String isSelfStore = "N";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_cart_order_pay_success);
        initTitleButton();
        myIntent = getIntent();
        initDataView();
    }

    private void initTitleButton() {
        common_title_btn_back = (Button) findViewById(R.id.common_title_btn_back);
        common_title_btn_back.setVisibility(View.GONE);
        common_title_btn_right = (Button) findViewById(R.id.common_title_btn_right);
        common_title_btn_right.setVisibility(View.GONE);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.shopping_goods_order_pay_success_online);
        shopping_goods_order_success_ordernum_data = (TextView) findViewById(R.id.shopping_goods_order_success_ordernum_data);
        shopping_goods_order_success_orderprice_data = (TextView) findViewById(R.id.shopping_goods_order_success_orderprice_data);
        shopping_goods_order_success_paymethod_data = (TextView) findViewById(R.id.shopping_goods_order_success_paymethod_data);
        shopping_phone_relative = (RelativeLayout) findViewById(R.id.shopping_phone_relative);
        shopping_phone_relative.setOnClickListener(this);
        shopping_cart_look_button = (Button) findViewById(R.id.shopping_cart_look_button);
        shopping_cart_look_button.setOnClickListener(this);
        shopping_cart_go_button = (Button) findViewById(R.id.shopping_cart_go_button);
        shopping_cart_go_button.setOnClickListener(this);
        shopping_cart_help_button = (Button) findViewById(R.id.shopping_cart_go_help);
        shopping_cart_help_button.setOnClickListener(this);
        shopping_cart_store_mes = (TextView) findViewById(R.id.shopping_cart_store_mes);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.shopping_cart_look_button: {
            Intent intent = new Intent(this, ShoppingCartOrderSuccessActivity.class);
            intent.setAction(getIntent().getAction());
            setResult(0, intent);
            finish();
        }
            break;
        case R.id.shopping_cart_go_button: {
            Intent intent = new Intent(this, ShoppingCartOrderSuccessActivity.class);
            intent.setAction(getIntent().getAction());
            setResult(6, intent);
            finish();
        }
            break;
        case R.id.shopping_cart_go_help: {
            Intent intent = new Intent(this, ShoppingCartOrderSuccessActivity.class);
            intent.setAction(getIntent().getAction());
            setResult(9, intent);
            finish();
        }
            break;
        case R.id.common_title_btn_right:
            break;
        case R.id.shopping_phone_relative:
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:4008-199-777"));
            this.startActivity(intent);
            // CommonUtility.showConfirmDialog(ShoppingCartOrderPaySuccessActivity.this, "", "4008-199-777",
            // getString(R.string.shopping_goods_order_phone_call), leftListener,
            // getString(R.string.login_cancel), rightListener);
            break;
        default:
            break;
        }
    }

    private void initDataView() {
        if (myIntent != null) {
            isSelfStore = myIntent.getStringExtra("isSelfStore");// 是否为门店自提
            isSelfStore = isSelfStore == null ? "N" : isSelfStore;
            // 门店自提
            if ("Y".equalsIgnoreCase(isSelfStore)) {
                shopping_cart_help_button.setVisibility(View.VISIBLE);
                shopping_cart_store_mes.setText(Html.fromHtml(getString(R.string.shopping_cart_store_mess)));
            } else {
                shopping_cart_help_button.setVisibility(View.GONE);
            }
            OrderSuccess orderSuccess = (OrderSuccess) myIntent.getSerializableExtra("orderSuccess");
            if (orderSuccess != null) {
                shopping_goods_order_success_orderprice_data.setText("￥" + orderSuccess.getPayAmount());
                shopping_goods_order_success_ordernum_data.setText(orderSuccess.getOrderId());
            }
            int onlinepaymethods = myIntent.getIntExtra("onlinepaymethods", 0);
            switch (onlinepaymethods) {
            case PAYMENT_ONLINEPAYMENT_PLAT_ZHIFUBAO:
                shopping_goods_order_success_paymethod_data
                        .setText(getString(R.string.shopping_goods_order_payment_onlinepay) + "("
                                + getString(R.string.shopping_goods_order_success_payment_zhifubao) + ")");
                break;
            case PAYMENT_ONLINEPAYMENT_PLAT_YILIAN:
                shopping_goods_order_success_paymethod_data
                        .setText(getString(R.string.shopping_goods_order_payment_onlinepay) + "("
                                + getString(R.string.shopping_goods_order_success_payment_yinlian) + ")");
                break;
            }
        }
    }

    private android.content.DialogInterface.OnClickListener leftListener = new android.content.DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            dialog.dismiss();
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:4008199777"));// 直接拨打
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ShoppingCartOrderPaySuccessActivity.this.startActivity(intent);
        }

    };
    private android.content.DialogInterface.OnClickListener rightListener = new android.content.DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            dialog.cancel();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, ShoppingCartOrderSuccessActivity.class);
            intent.setAction(getIntent().getAction());
            setResult(0, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
