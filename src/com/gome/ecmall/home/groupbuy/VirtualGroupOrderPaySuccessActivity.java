package com.gome.ecmall.home.groupbuy;

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
import com.gome.ecmall.phonerecharge.PhoneRechargeActivity;
import com.gome.ecmall.phonerecharge.PhoneRechargeHelpActivity;
import com.gome.ecmall.phonerecharge.PhoneRechargeOrderDetailActivity;
import com.gome.ecmall.util.PreferenceUtils;
import com.gome.eshopnew.R;

public class VirtualGroupOrderPaySuccessActivity extends Activity implements OnClickListener {
    private Button common_title_btn_right;
    private TextView common_title_tv_text;
    private TextView tv_phone_recharge_order_pay_success_ordernum;
    private TextView tv_phone_recharge_order_pay_success_already_pay_money;
    private TextView tv_phone_recharge_order_pay_success_pay_type;
    private Button bt_phone_recharge_order_pay_success_continue_recharge;
    private Button bt_phone_recharge_order_pay_success_myorder;
    private String orderNum;
    private String payMoney;
    private int payType;
    private String fromPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderNum = getIntent().getStringExtra(PhoneRecharge.ORDERNUM);
        payMoney = getIntent().getStringExtra(PhoneRecharge.PAYMONEY);
        payType = getIntent().getIntExtra(PhoneRecharge.PAYTYPE, 0);
        fromPage = getIntent().getStringExtra(PhoneRecharge.FROMPAGE);
        setContentView(R.layout.virtual_group_order_pay_success);
        initializeView();// 初始化控件
        setData();
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
        common_title_btn_right.setVisibility(View.VISIBLE);
        common_title_btn_right.setText(getString(R.string.phone_recharge_close));
        common_title_btn_right.setOnClickListener(this);
        common_title_tv_text.setVisibility(View.VISIBLE);
        common_title_tv_text.setText(getString(R.string.shopping_goods_order_pay_success_online));
        bt_phone_recharge_order_pay_success_continue_recharge.setOnClickListener(this);
        bt_phone_recharge_order_pay_success_myorder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.bt_phone_recharge_order_pay_success_continue_recharge:
            // 继续购物跳转到团购列表
            if("VirtualGroupOrderDetailActivity".equals(fromPage)){
                Intent intentDetail = new Intent(this, NewGroupBuyActivity.class);
                startActivity(intentDetail);
                setResult(2);
            }else{
                setResult(3);
            }
            finish();

            break;
        case R.id.bt_phone_recharge_order_pay_success_myorder:
            // 团购券
            if (!TextUtils.isEmpty(orderNum)) {
                Intent intentDetail = new Intent(this, VirtualGroupTicketsActivity.class);
                startActivity(intentDetail);
                setResult(2);//到团购券，将此付款模块界面都关闭
                finish();
            }
            break;
        case R.id.common_title_btn_right:
            // 取消
            Intent intent = new Intent();
            setResult(4, intent);
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
            setResult(4, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
