package com.gome.ecmall.home.mygome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.util.CommonUtility;
import com.gome.eshopnew.R;

public class AccountSecurityActivity extends Activity implements OnClickListener {


    private TextView tvTitle;
    private Button btnBack;
    private RelativeLayout passwordRL;
    private RelativeLayout paymentRL;
    private TextView paymentState;
    private String virtualAccountStatus;
    private String mobileNum;
    private String virtualAccountStatusDesc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_security);
        setupView();
        setData();
    }


    private void setupView() {
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.mygome_payment_password);
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        passwordRL = (RelativeLayout) findViewById(R.id.login_modify_password_rl);
        paymentRL = (RelativeLayout) findViewById(R.id.set_payment_password_rl);
        paymentState = (TextView) findViewById(R.id.set_payment_password_tv);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setText(R.string.back);
        btnBack.setOnClickListener(this);
        passwordRL.setOnClickListener(this);
        paymentRL.setOnClickListener(this);
    }

    private void setData() {
        virtualAccountStatus = getIntent().getStringExtra("virtualAccountStatus");
        mobileNum = getIntent().getStringExtra("mobileNum");
        virtualAccountStatusDesc = getIntent().getStringExtra("virtualAccountStatusDesc");
        if ("1".equals(virtualAccountStatus)) {
            paymentState.setText(getResources().getString(R.string.mygome_modify_payment_password));
        } else {
            paymentState.setText(getResources().getString(R.string.mygome_set_payment_password));
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            finish();
        }else if(v == passwordRL){
            Intent intent = new Intent();
            intent.setClass(this, ModifyPasswordActivity.class);
            startActivity(intent);
            
        }else if(v == paymentRL){
            Intent intent = new Intent();
            Class<?> dispatchClass = null;
            if ("1".equals(virtualAccountStatus)) {
                intent.putExtra("isActivated", true);
                intent.putExtra("mobileNum", mobileNum);
                dispatchClass = ModifyPaymentPasswordActivity.class;
            } else if ("0".equals(virtualAccountStatus)) {
                dispatchClass = SetPaymentPasswordActivity.class;
            } else if ("-2".equals(virtualAccountStatus)) {
                intent.putExtra("isActivated", false);
                dispatchClass = ModifyPaymentPasswordActivity.class;
            } else if ("-1".equals(virtualAccountStatus)) {
                CommonUtility.showToast(AccountSecurityActivity.this, virtualAccountStatusDesc);
                return;
            }
            intent.setClass(this, dispatchClass);
            startActivityForResult(intent, 100);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            paymentState.setText(getResources().getString(R.string.mygome_set_payment_password));
        }
    }
}
