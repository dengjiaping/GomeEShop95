package com.gome.ecmall.home.category;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gome.ecmall.bean.BelowPrice;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.home.login.RegisterActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 降价通知
 */
public class BelowPriceActivity extends AbsSubActivity implements OnClickListener {

    public static final String BelowPirce_GoodsNo = "goodsNo";
    public static final String BelowPirce_SKUID = "skuID";

    private Button common_title_btn_back, common_title_btn_right;
    private TextView tvTitle, exception_text;
    private String goodsNo, skuID, mobile, email;
    private EditText belowprice_edit_phone, belowprice_edit_email;

    private static String myEmail;
    private static String myMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_belowprice);
        initParams();
        initView();
    }
    
    /**
     * 初始化页面视图
     */
    private void initView() { 
        common_title_btn_back = (Button) findViewById(R.id.common_title_btn_back);
        common_title_btn_back.setText(R.string.back);
        common_title_btn_back.setVisibility(View.VISIBLE);
        common_title_btn_back.setOnClickListener(this);
        common_title_btn_right = (Button) findViewById(R.id.common_title_btn_right);
        common_title_btn_right.setVisibility(View.VISIBLE);
        common_title_btn_right.setText(R.string.submit);
        common_title_btn_right.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.cutprice);
        belowprice_edit_phone = (EditText) findViewById(R.id.belowprice_edit_phone);
        if (!TextUtils.isEmpty(myMobile)) {
            belowprice_edit_phone.setText(myMobile);
        }
        belowprice_edit_email = (EditText) findViewById(R.id.belowprice_edit_email);
        if (!TextUtils.isEmpty(myEmail)) {
            belowprice_edit_email.setText(myEmail);
        }
        exception_text = (TextView) findViewById(R.id.exception_text);
        exception_text.setText(Html.fromHtml("<font color=\"#666666\">"
                + getString(R.string.expation_text_first) + "</font>" + "<font color=\"#CC0000\">"
                + getString(R.string.expation_text_secode) + "</font>" + "<font color=\"#666666\">"
                + getString(R.string.expation_text_thrid) + "</font>"));
    }

	/**
	 * 初始化页面参数
	 */
	private void initParams() {
		Intent intent = getIntent();
        goodsNo = intent.getStringExtra(BelowPirce_GoodsNo);
        skuID = intent.getStringExtra(BelowPirce_SKUID);
	}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.common_title_btn_back: {
            goback();
        }
            break;
        case R.id.common_title_btn_right: {
            mobile = belowprice_edit_phone.getText().toString();
            email = belowprice_edit_email.getText().toString();
            if (TextUtils.isEmpty(mobile)) {
                CommonUtility.showMiddleToast(BelowPriceActivity.this, "", getString(R.string.arrnotice_please_input));
                return;
            }
            if (!TextUtils.isEmpty(mobile)) {
                if (!RegisterActivity.isMobile(mobile)) {
                    CommonUtility.showMiddleToast(BelowPriceActivity.this, "",
                            getString(R.string.shopping_goods_order_consinfo_phone_ren));
                    return;
                }

            }
            if (!TextUtils.isEmpty(email) && !RegisterActivity.isEmail(email)) {
                CommonUtility.showMiddleToast(BelowPriceActivity.this, "",
                        getString(R.string.shopping_goods_order_consinfo_email_ren));
                return;
            }
            sumberData();
        }
            break;
        }
    }

    /**
     * 数据提交
     */
    private void sumberData() {
        if (!NetUtility.isNetworkAvailable(BelowPriceActivity.this)) {
            CommonUtility.showMiddleToast(BelowPriceActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, String>() {
            LoadingDialog loadingDialog = null;

            @Override
            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(BelowPriceActivity.this, getString(R.string.loading),
                        true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected String doInBackground(Object... params) {
                String request = BelowPrice.createRequestBelowPriceJson(goodsNo, skuID, mobile, email);
                String response = NetUtility.sendHttpRequestByPost(Constants.URL_PROFILE_ADD_PRICE_NOTICE, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    return null;
                }
                return BelowPrice.parseBelowPriceRes(response);
            }

            @Override
            protected void onPostExecute(String result) {
                if (isCancelled()) {
                    return;
                }
                loadingDialog.dismiss();
                if (TextUtils.isEmpty(result)) {
                    CommonUtility.showMiddleToast(BelowPriceActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                if ("Y".equals(result)) {
                    CommonUtility.showToast(BelowPriceActivity.this, getString(R.string.arrnotice_success));
                    goback();
                } else if ("N".equals(result)) {
                    CommonUtility.showMiddleToast(BelowPriceActivity.this, "", BelowPrice.getErrorMessage());
                }
            }
        }.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        myEmail = belowprice_edit_email.getText().toString();
        myMobile = belowprice_edit_phone.getText().toString();

    }
   
}
