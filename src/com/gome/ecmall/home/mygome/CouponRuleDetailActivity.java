package com.gome.ecmall.home.mygome;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.gome.ecmall.bean.Helper;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class CouponRuleDetailActivity extends Activity implements OnClickListener {

    public static final String INTENT_KEY_TITLE = "title";
    public static final String INTENT_KEY_CONTENT = "content";
    public static final String TYPE = "type";
    private TextView tvTopTitle;
    private TextView tvContentTitle;
    private TextView tvContent;
    private Button btnBack;
    private WebView wvIntroduce;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helper_center_detail);
        type = getIntent().getStringExtra(TYPE);
        setupView();
        setData();
    }

    private void setupView() {
        tvTopTitle = (TextView) findViewById(R.id.common_title_tv_text);
        // tvTopTitle.setText(R.string.mygome_coupon_rule_title);
        tvContentTitle = (TextView) findViewById(R.id.more_usehelp_detail_tv_title);
        tvContentTitle.setVisibility(View.GONE);
        wvIntroduce = (WebView) findViewById(R.id.product_summary_introduce);
        wvIntroduce.getSettings().setDefaultTextEncodingName("UTF-8");
        // tvContentTitle.setText(R.string.mygome_coupon_rule_title);
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setText(R.string.back);
        btnBack.setOnClickListener(this);
    }

    private void setData() {
        if (!NetUtility.isNetworkAvailable(this)) {

            CommonUtility.showMiddleToast(this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, Helper>() {
            LoadingDialog progressDialog;

            protected void onPreExecute() {
                progressDialog = CommonUtility.showLoadingDialog(CouponRuleDetailActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                                cancel(true);
                            }
                        });
            };

            protected Helper doInBackground(Object... params) {
                String post = Helper.createRequestCouponJson(type);
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_SUPPLEMENT_HELPCENTER, post);
                return Helper.parseHelper(result);

            };

            protected void onPostExecute(Helper canUsedTicket) {
                progressDialog.dismiss();
                if (isCancelled()) {
                    return;
                }
                if (canUsedTicket == null) {
                    CommonUtility.showMiddleToast(CouponRuleDetailActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                tvTopTitle.setText(canUsedTicket.getTitle());
                if (!TextUtils.isEmpty(canUsedTicket.getContent())) {
                    wvIntroduce.loadDataWithBaseURL(null, canUsedTicket.getContent(), "text/html", "UTF-8", null);
                } else {
                    wvIntroduce.loadUrl(canUsedTicket.getHtmlURL());
                }
            };
        }.execute();
    }

    @Override
    public void onClick(View v) {
        if (btnBack == v) {
            finish();
        }
    }

}
