package com.gome.ecmall.home.mygome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gome.ecmall.bean.ReturnProduct.ReturnAddress;
import com.gome.ecmall.util.CommonUtility;
import com.gome.eshopnew.R;

/**
 * 地址编辑页
 * 
 * @author qiudongchao
 */
public class MyReturnAddressActivity extends Activity implements OnClickListener {
    // 页面控件
    private TextView tvTitle;
    private Button btnBack, btnRight;
    // 收货地址
    private EditText etCons;
    private EditText etPhone;
    private Button btnProvince;
    private Button btnCity;
    private Button btnDiscont;
    private EditText etDetailAddress;
    private EditText etZope;
    private Button btnsubmit;

    private ReturnAddress mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mygome_return_address);
        mAddress = (ReturnAddress) getIntent().getSerializableExtra("address");
        initView(mAddress);
    }

    private void initView(ReturnAddress mAddress2) {
        // 标题
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnRight = (Button) findViewById(R.id.common_title_btn_right);
        btnBack.setOnClickListener(this);
        btnRight.setVisibility(View.INVISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.VISIBLE);
        tvTitle.setText("上门取货");
        btnBack.setText(R.string.back);

        etCons = (EditText) findViewById(R.id.shopping_goods_cons_name_data);
        etPhone = (EditText) findViewById(R.id.shopping_goods_cons_phone_data);
        btnProvince = (Button) findViewById(R.id.shopping_province_data);
        btnCity = (Button) findViewById(R.id.shopping_city_data);
        btnDiscont = (Button) findViewById(R.id.shopping_discont_data);
        etDetailAddress = (EditText) findViewById(R.id.shopping_goods_cons_detailaddress_data);
        etZope = (EditText) findViewById(R.id.shopping_goods_cons_zope_data);
        btnsubmit = (Button) findViewById(R.id.btnsubmit);
        btnsubmit.setOnClickListener(this);
        if (mAddress2 != null) {
            etCons.setText(mAddress2.getUser());
            etDetailAddress.setText(mAddress2.getAddressDetail());
            etPhone.setText(mAddress2.getPhoneNumber());
            etZope.setText(mAddress2.getZipCode());
        }
    }

    @Override
    public void onClick(View v) {
        if (btnBack == v) {
            this.finish();
        } else if (btnsubmit == v) {
            checkAndSubmit();
        }
    }

    private void checkAndSubmit() {
        if (TextUtils.isEmpty(etCons.getText())) {
            CommonUtility.showMiddleToast(this, "", "联系人不能为空！");
            return;
        } else if (TextUtils.isEmpty(etPhone.getText())) {
            CommonUtility.showMiddleToast(this, "", "电话不能为空！");
            return;
        } else if (TextUtils.isEmpty(etDetailAddress.getText())) {
            CommonUtility.showMiddleToast(this, "", "地址不能为空！");
            return;
        }
        // else if (TextUtils.isEmpty(etZope.getText())) {
        // CommonUtility.showMiddleToast(this, "", "邮编不能为空！");
        // return;
        // }
        submitData();
    }

    private void submitData() {
        Intent intent = new Intent(this, MyReturnDetailActivity.class);
        mAddress.setUser(etCons.getText().toString());
        mAddress.setPhoneNumber(etPhone.getText().toString());
        mAddress.setAddressDetail(etDetailAddress.getText().toString());
        mAddress.setZipCode(etZope.getText().toString());
        intent.putExtra("address", mAddress);
        setResult(1, intent);// 返回上级
        finish();
    }
}
