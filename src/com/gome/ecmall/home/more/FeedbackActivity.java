package com.gome.ecmall.home.more;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gome.ecmall.bean.FeedBack;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.framework.AbsSubActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.MobileDeviceUtil;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class FeedbackActivity extends AbsSubActivity implements OnClickListener, OnFocusChangeListener {
    private Button btnBack;
    private TextView tvTitle;
    private Button btnSubmit;
    private EditText etFeed;
    private EditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_feedback);
        setupView();
    }

    private void setupView() {
        tvTitle = (TextView) findViewById(R.id.common_title_tv_text);
        tvTitle.setText(R.string.more_gomestore_feedback);
        btnBack = (Button) findViewById(R.id.common_title_btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setText(R.string.back);
        btnBack.setOnClickListener(this);
        btnSubmit = (Button) findViewById(R.id.common_title_btn_right);
        btnSubmit.setVisibility(View.VISIBLE);
        btnSubmit.setText(R.string.submit);
        btnSubmit.setOnClickListener(this);
        etFeed = (EditText) findViewById(R.id.more_feedback_et_feed);
        etEmail = (EditText) findViewById(R.id.more_feedback_et_email);
        etFeed.setOnFocusChangeListener(this);
        etEmail.setOnFocusChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            goback();
        } else if (v == btnSubmit) {// 提交意见
            String feed = etFeed.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            if (submitCheck(feed, email)) {
                CommonUtility.hideSoftKeyboardNotClear(this, etFeed);
                CommonUtility.hideSoftKeyboardNotClear(this, etEmail);
                submitQuestion(feed, email);
            }
        }
    }

    private boolean submitCheck(String feed, String email) {

        if (TextUtils.isEmpty(feed)) {
            CommonUtility.showToast(this, getString(R.string.more_gomestore_feedback_your_feed_null));
            return false;
        } else if (!(5 <= feed.length() && feed.length() <= 200)) {
            CommonUtility.showToast(this, getString(R.string.more_gomestore_feedback_your_feed_error_formt));
            return false;
        }
        if (!TextUtils.isEmpty(email)) {
            if (!isEmail(email)) {
                CommonUtility.showToast(this, getString(R.string.more_gomestore_feedback_your_email_error_formt));
                return false;
            }
        }
        return true;
    }

    private void submitQuestion(final String feed, final String email) {
        if (!NetUtility.isNetworkAvailable(FeedbackActivity.this)) {
            CommonUtility.showMiddleToast(FeedbackActivity.this, "",
                    getString(R.string.can_not_conntect_network_please_check_network_settings));
            return;
        }
        new AsyncTask<Object, Void, Boolean>() {

            private LoadingDialog loadingDialog;

            protected void onPreExecute() {
                loadingDialog = CommonUtility.showLoadingDialog(FeedbackActivity.this, getString(R.string.loading),
                        true, new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            };

            protected Boolean doInBackground(Object... params) {
                String updateVersionCode = MobileDeviceUtil.getInstance(getApplicationContext()).getVersonCode();// 升级码
                String uuid = MobileDeviceUtil.getInstance(getApplicationContext()).getUUID();// uuid
                String phoneModel = MobileDeviceUtil.getInstance(getApplicationContext()).getMobileModel();// 手机型号
                String systemVersionCode = MobileDeviceUtil.getInstance(getApplicationContext()).getSystemVersion();// 操作系统版本号
                String phoneScreen = MobileDeviceUtil.getInstance(getApplicationContext()).getScreenWidth() + "*"
                        + MobileDeviceUtil.getInstance(getApplicationContext()).getScreenHeight();
                ;// 手机分辨率
                String userName = GlobalConfig.userName;
                String phonePlatform = "android";
                String request = FeedBack.cerateFeedbackJson(updateVersionCode, systemVersionCode, phoneModel, uuid,
                        phonePlatform, phoneScreen, userName, feed, email);
                String response = NetUtility.sendHttpRequestByPost(Constants.MORE_FEED, request);
                if (NetUtility.NO_CONN.equals(response)) {
                    FeedBack.errorMessage = getString(R.string.more_gomestore_feedback_submit_neterror_plaease_retry);
                    return false;
                }
                return FeedBack.parseFeedbackResult(response);
            };

            protected void onPostExecute(Boolean result) {
                loadingDialog.dismiss();
                CommonUtility.showMiddleToast(FeedbackActivity.this, "",
                        result ? getString(R.string.more_gomestore_feedback_submit_success)
                                : getString(R.string.more_gomestore_feedback_submit_fail) + FeedBack.errorMessage);
                FeedBack.errorMessage = null;
                if (result) {
                    // 成功，然后清空内容，留在本页
                    etFeed.setText(null);
                    etEmail.setText(null);
                } else {
                    // 失败，保留文字
                    etFeed.setText(feed);
                    etEmail.setText(email);
                }
            };

        }.execute();
    }

    // 检查邮箱
    public static boolean isEmail(String strEmail) {
        String strPattern = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (v == etFeed) {
                etFeed.setHint(null);
            } else if (v == etEmail) {
                etEmail.setHint(null);
            }

        }
    }
}
