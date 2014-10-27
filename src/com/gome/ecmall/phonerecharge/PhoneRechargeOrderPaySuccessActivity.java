package com.gome.ecmall.phonerecharge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.PhoneRecharge;
import com.gome.ecmall.util.PreferenceUtils;
import com.gome.eshopnew.R;

public class PhoneRechargeOrderPaySuccessActivity extends Activity implements OnClickListener {
    private Button common_title_btn_right;
    private TextView common_title_tv_text;
    private TextView tv_phone_recharge_order_pay_success_ordernum;
    private TextView tv_phone_recharge_order_pay_success_already_pay_money;
    private TextView tv_phone_recharge_order_pay_success_pay_type;
    private Button bt_phone_recharge_order_pay_success_continue_recharge;
    private Button bt_phone_recharge_order_pay_success_myorder;
    private Button bt_phone_recharge_order_pay_often_question;
    private String num;
    private String orderNum;
    private String payMoney;
    private int payType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        num = getIntent().getStringExtra(PhoneRecharge.NUM);
        orderNum = getIntent().getStringExtra(PhoneRecharge.ORDERNUM);
        payMoney = getIntent().getStringExtra(PhoneRecharge.PAYMONEY);
        payType = getIntent().getIntExtra(PhoneRecharge.PAYTYPE, 0);
        setContentView(R.layout.phone_recharge_order_pay_success);
        initializeView();// 初始化控件
        setData();
        // 把充值成功手机号存起来
        if (!TextUtils.isEmpty(num)) {
            PreferenceUtils.getInstance(getApplicationContext());
            PreferenceUtils.setStringValue("phone_recharge_hostory_num", num);
        }

    }

    /**
     * 设置数据
     */
    private void setData() {
        tv_phone_recharge_order_pay_success_ordernum.setText(orderNum);
        tv_phone_recharge_order_pay_success_already_pay_money.setText("￥" + payMoney);
        String result = "";
        if (1 == payType) {
            result = getString(R.string.shopping_goods_order_success_payment_zhifubao);
        } else if (3 == payType) {
            result = getString(R.string.phone_recharge_pay_type_yinlian_ufu);
        }
        tv_phone_recharge_order_pay_success_pay_type.setText(result);
    }

    /**
     * 初始化控件
     */
    private void initializeView() {
        common_title_btn_right = (Button) this.findViewById(R.id.common_title_btn_right);
        common_title_tv_text = (TextView) this.findViewById(R.id.common_title_tv_text);
        tv_phone_recharge_order_pay_success_ordernum = (TextView) this
                .findViewById(R.id.tv_phone_recharge_order_pay_success_ordernum);
        tv_phone_recharge_order_pay_success_already_pay_money = (TextView) this
                .findViewById(R.id.tv_phone_recharge_order_pay_success_already_pay_money);
        tv_phone_recharge_order_pay_success_pay_type = (TextView) this
                .findViewById(R.id.tv_phone_recharge_order_pay_success_pay_type);
        bt_phone_recharge_order_pay_success_continue_recharge = (Button) this
                .findViewById(R.id.bt_phone_recharge_order_pay_success_continue_recharge);
        bt_phone_recharge_order_pay_success_myorder = (Button) this
                .findViewById(R.id.bt_phone_recharge_order_pay_success_myorder);
        bt_phone_recharge_order_pay_often_question = (Button) this
                .findViewById(R.id.bt_phone_recharge_order_pay_often_question);
        common_title_btn_right.setVisibility(View.VISIBLE);
        common_title_btn_right.setText(getString(R.string.phone_recharge_close));
        common_title_btn_right.setOnClickListener(this);
        common_title_tv_text.setVisibility(View.VISIBLE);
        common_title_tv_text.setText(getString(R.string.shopping_goods_order_pay_success_online));
        bt_phone_recharge_order_pay_success_continue_recharge.setOnClickListener(this);
        bt_phone_recharge_order_pay_success_myorder.setOnClickListener(this);
        bt_phone_recharge_order_pay_often_question.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.bt_phone_recharge_order_pay_success_continue_recharge:
            // 继续充值,重新进入充值页面
            Intent intentNew = new Intent();
            intentNew.setClass(PhoneRechargeOrderPaySuccessActivity.this, PhoneRechargeActivity.class);
            startActivityForResult(intentNew, 0);

            break;
        case R.id.bt_phone_recharge_order_pay_success_myorder:
            // 我的订单
            if (!TextUtils.isEmpty(orderNum)) {
                Intent intentDetail = new Intent(this, PhoneRechargeOrderDetailActivity.class);
                intentDetail.putExtra(JsonInterface.JK_ORDER_ID_LOW, orderNum);
                startActivityForResult(intentDetail, 0);
            }
            break;
        case R.id.bt_phone_recharge_order_pay_often_question:
            // 常见问题
            Intent intentHelp = new Intent(this, PhoneRechargeHelpActivity.class);
            startActivity(intentHelp);
            break;
        case R.id.common_title_btn_right:
            // 取消
            Intent intent = new Intent();
            setResult(2, intent);
            finish();
            break;

        default:
            break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 关闭
            Intent intent = new Intent();
            setResult(2, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
        case 2:
            Intent intent = new Intent();
            setResult(2, intent);
            finish();
            break;

        default:
            break;
        }
    }
}
