package com.gome.ecmall.phonerecharge;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.PhoneRecharge;
import com.gome.ecmall.bean.PhoneRecharge.RechargeOrder;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.login.LoginManager;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.DES;
import com.gome.ecmall.util.NetUtility;
import com.gome.ecmall.util.PreferenceUtils;
import com.gome.eshopnew.R;

public class PhoneRechargeOrderConfirmActivity extends Activity implements OnClickListener {
    private Button common_title_btn_back;
    private Button common_title_btn_right;
    private TextView common_title_tv_text;
    private TextView tv_phone_recharge_order_confirm_num;
    private TextView tv_phone_recharge_order_confirm_location;
    private TextView tv_phone_recharge_order_confirm_money;
    private TextView tv_phone_recharge_order_confirm_pay_money;
    private Button bt_phone_recharge_order_confirm;
    private String num;// 手机号
    private String selectedMoney;// 用户选择的金额
    private String numLocation;// 号码归属地
    private String payMoney;// 用户需要支付金额
    private String skuId;// 用户需要支付金额

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_recharge_order_confirm);
        num = getIntent().getStringExtra(PhoneRecharge.NUM);
        selectedMoney = getIntent().getStringExtra(PhoneRecharge.SELECTEDMONEY);
        numLocation = getIntent().getStringExtra(PhoneRecharge.NUMLOCATION);
        payMoney = getIntent().getStringExtra(PhoneRecharge.PAYMONEY);
        skuId = getIntent().getStringExtra(PhoneRecharge.SKUID);
        initializeView();// 初始化控件
        setData();// 设置数据
    }

    /**
     * 设置数据
     */
    private void setData() {
        tv_phone_recharge_order_confirm_num.setText(num);
        tv_phone_recharge_order_confirm_location.setText(numLocation);
        tv_phone_recharge_order_confirm_money.setText("￥" + selectedMoney);
        tv_phone_recharge_order_confirm_pay_money.setText("￥" + payMoney);
    }

    /**
     * 初始化控件
     */
    private void initializeView() {
        common_title_btn_back = (Button) findViewById(R.id.common_title_btn_back);
        common_title_btn_right = (Button) findViewById(R.id.common_title_btn_right);
        common_title_tv_text = (TextView) findViewById(R.id.common_title_tv_text);
        tv_phone_recharge_order_confirm_num = (TextView) findViewById(R.id.tv_phone_recharge_order_confirm_num);
        tv_phone_recharge_order_confirm_location = (TextView) findViewById(R.id.tv_phone_recharge_order_confirm_location);
        tv_phone_recharge_order_confirm_money = (TextView) findViewById(R.id.tv_phone_recharge_order_confirm_money);
        tv_phone_recharge_order_confirm_pay_money = (TextView) findViewById(R.id.tv_phone_recharge_order_confirm_pay_money);
        bt_phone_recharge_order_confirm = (Button) findViewById(R.id.bt_phone_recharge_order_confirm);
        common_title_btn_back.setVisibility(View.VISIBLE);
        common_title_btn_back.setText(getString(R.string.back));
        common_title_btn_back.setOnClickListener(this);
        common_title_btn_right.setVisibility(View.VISIBLE);
        common_title_btn_right.setText(getString(R.string.cancel));
        common_title_btn_right.setOnClickListener(this);
        common_title_tv_text.setVisibility(View.VISIBLE);
        common_title_tv_text.setText(getString(R.string.confirm_phone_recharge_order));
        bt_phone_recharge_order_confirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.common_title_btn_back:
            // 返回按钮
            finish();
            break;
        case R.id.common_title_btn_right:
            // 取消按钮
            Intent intent = new Intent();
            setResult(2, intent);
            finish();
            break;
        case R.id.bt_phone_recharge_order_confirm:
            // 确认按钮
            AsynConfirm();
            break;
        default:
            break;
        }

    }

    /**
     * 异步验证订单是否=能够提交
     */
    public void AsynConfirm() {
        if (!NetUtility.isNetworkAvailable(PhoneRechargeOrderConfirmActivity.this)) {
            CommonUtility.showMiddleToast(PhoneRechargeOrderConfirmActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, RechargeOrder>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(PhoneRechargeOrderConfirmActivity.this, "", true,
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected RechargeOrder doInBackground(Object... params) {
                String profileID = PreferenceUtils.getStringValue(JsonInterface.JK_PROFILE_ID, "");
                String loginName = PreferenceUtils.getStringValue(JsonInterface.JK_USER_NAME, "");
                try {
                    loginName = DES.decryptDES(loginName, Constants.LOGINDESKEY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String sign = LoginManager.getSigns(profileID, loginName, num, skuId,
                        Constants.PHONE_RECHARGE_SECRET_KEY);
                String request = PhoneRecharge.createConfirmOrderJson(profileID, loginName, num, skuId, sign);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_SUBMIT_ORDER, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    PhoneRecharge.failReason = "";
                    // Toast.makeText(PhoneRechargeOrderConfirmActivity.this,
                    // getString(R.string.data_load_fail_exception), 0).show();
                    return null;
                }
                return PhoneRecharge.parseConfirmOrder(response);
            }

            @Override
            protected void onPostExecute(RechargeOrder result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                if (result == null) {
                    if (!TextUtils.isEmpty(PhoneRecharge.failReason)) {
                        Toast.makeText(PhoneRechargeOrderConfirmActivity.this, PhoneRecharge.failReason, 0).show();
                    } else {
                        Toast.makeText(PhoneRechargeOrderConfirmActivity.this,
                                getString(R.string.data_load_fail_exception), 0).show();
                    }
                    return;
                }
                enterOrderSuccess(result);

            }
        }.execute();
    }

    /**
     * 跳入订单提交成功页面
     */
    private void enterOrderSuccess(RechargeOrder result) {
        Intent intent = new Intent();
        intent.setClass(PhoneRechargeOrderConfirmActivity.this, PhoneRechargeOrderSubmitSuccessActivity.class);
        intent.putExtra(PhoneRecharge.NUM, num);
        intent.putExtra(PhoneRecharge.ORDERNUM, result.getOrderId());
        intent.putExtra(PhoneRecharge.PAYMONEY, result.getOrderPrice());
        intent.putExtra(PhoneRecharge.FROMPAGE, "PhoneRechargeOrderConfirmActivity");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 2) {
            String targetClassName = data.getStringExtra(GlobalConfig.CLASS_NAME);
            Intent intent = new Intent();
            if (!TextUtils.isEmpty(targetClassName)
                    && "PhoneRechargeOrderSubmitSuccessActivity".equals(targetClassName)) {
                intent.putExtra(GlobalConfig.CLASS_NAME, targetClassName);
            }
            setResult(2, intent);
            finish();
        }
    }
}
