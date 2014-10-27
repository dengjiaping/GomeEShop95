package com.gome.ecmall.home.login;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.gome.ecmall.bean.AuthenticCode;
import com.gome.ecmall.bean.FindPassWordEntity;
import com.gome.ecmall.bean.FindPassWordEntity.FindPasswordStep1;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.GlobalApplication;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.HttpsUtils;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * @author qinxudong 找回密码页面
 */
public class RetrievePasswordActivity extends Activity implements OnClickListener {

    private static final String TAG = "RetrievePasswordActivity";

    /** 选中的账号 0=国美 ; 1=库巴 ; */
    private String selectId = "0";

    private String loginName;
    private String captcha;

    /** 标题头部分 */
    Button backBtn;
    Button shutBtn;
    TextView titleText;

    /** 填写用户名操作 */

    LinearLayout write_account_layout;
    RadioGroup radioGroup;
    RadioButton gomeRadioButton;
    RadioButton coo8RadioButton;
    EditText input_account_name;
    EditText input_account_code;
    ImageView image_clear_text;
    ImageView image_account_code;
    Button write_account_next_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_account);
        initView();
        obtainVerification();
    }

    void initView() {

        titleText = (TextView) findViewById(R.id.common_title_tv_text);
        titleText.setText(R.string.find_password);
        titleText.setVisibility(View.VISIBLE);

        backBtn = (Button) findViewById(R.id.common_title_btn_back);
        backBtn.setOnClickListener(this);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setText(R.string.back);

        shutBtn = (Button) findViewById(R.id.common_title_btn_right);
        shutBtn.setOnClickListener(this);
        shutBtn.setVisibility(View.VISIBLE);
        shutBtn.setText(R.string.phone_recharge_close);

        /** 填写用户名操作视图初始化 */

        write_account_layout = (LinearLayout) findViewById(R.id.write_account);
        write_account_layout.setVisibility(View.VISIBLE);
        // write_account_layout.requestFocus() ;

        radioGroup = (RadioGroup) findViewById(R.id.write_account_radio_group);
        gomeRadioButton = (RadioButton) findViewById(R.id.gome_radio_btn);
        coo8RadioButton = (RadioButton) findViewById(R.id.coo8_radio_btn);
        input_account_name = (EditText) findViewById(R.id.find_password_account);
        input_account_code = (EditText) findViewById(R.id.write_account_code);
        image_clear_text = (ImageView) findViewById(R.id.clear_text);
        image_account_code = (ImageView) findViewById(R.id.image_account_code);
        write_account_next_btn = (Button) findViewById(R.id.write_account_next_btn);
        write_account_next_btn.setOnClickListener(RetrievePasswordActivity.this);
        input_account_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                image_clear_text.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                loginName = input_account_name.getText().toString().trim();

            }
        });

        input_account_code.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                captcha = input_account_code.getText().toString().trim();

            }
        });

        image_clear_text.setOnClickListener(this);

        image_account_code.setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.gome_radio_btn) {
                    selectId = "0";
                } else if (checkedId == R.id.coo8_radio_btn) {
                    selectId = "1";
                }
                radioGroup.check(checkedId);
            }
        });

    }

    @Override
    protected void onResume() {
        image_account_code.setBackgroundResource(R.drawable.get_code);
        obtainVerification();
        input_account_code.setText("");
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v == image_clear_text) {
            input_account_name.setText("");
            image_clear_text.setVisibility(View.GONE);
        } else if (v == write_account_next_btn) {
            if (checkUser()) {
                getData();
            }
        } else if (v == image_account_code) {
            image_account_code.setBackgroundResource(R.drawable.get_code);
            obtainVerification();
        } else if (v == backBtn) {
            this.finish();
        } else if (v == shutBtn) {
            this.finish();
        }
    }

    /**
     * 获取校验码
     */
    public void obtainVerification() {
        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... params) {

                String result = NetUtility.sendHttpRequestByGet(Constants.URL_PROFILE_CHECKCODE);
                if (result == null) {
                    return null;
                }

                AuthenticCode ac = Login.parseAuthenticCode(result);
                if (ac == null) {
                    return null;
                }
                String imgUrl = Constants.SERVER_URL + ac.getPhotoUrl();
                return Login.downloadNetworkBitmap(imgUrl);
            }

            protected void onPostExecute(Bitmap bm) {

                if (bm == null) {
                    CommonUtility.showMiddleToast(RetrievePasswordActivity.this, null,
                            getString(R.string.get_verification_error));
                    return;
                } else {
                    BitmapDrawable bd = new BitmapDrawable(RetrievePasswordActivity.this.getResources(), bm);
                    image_account_code.setBackgroundDrawable(bd);
                }
            }
        }.execute();
    }

    private LoadingDialog getLoadingDialog() {
        LoadingDialog dialog = null;
        String message = getString(R.string.loading);
        if (!NetUtility.isNetworkAvailable(RetrievePasswordActivity.this)) {
            return null;
        }

        dialog = CommonUtility.showLoadingDialog(RetrievePasswordActivity.this, message, true, new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.cancel();
            }
        });
        return dialog;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            setResult(2);
            this.finish();
        }

    }

    public static boolean isString(String str) {
        String strPattern = "^[a-zA-Z0-9-_]{6,20}$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public boolean isNumber(String strName) {
        String strPattern = "^[0-9]+$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strName);
        return m.matches();
    }

    public boolean isCheckCode(String strName) {
        String strPattern = "^[A-Za-z\\d\\u4E00-\\u9FA5]+$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strName);
        return m.matches();
    }

    public boolean checkUserName(String userName) {
        if (TextUtils.isEmpty(userName)) {
            CommonUtility.showMiddleToast(RetrievePasswordActivity.this, null,
                    getString(R.string.login_input_user_name));
            return false;
        } else {
            /*
             * int userLen = userName.length(); if (userLen < 6 || userLen > 20) {
             * CommonUtility.showMiddleToast(RetrievePasswordActivity.this, null,
             * getString(R.string.login_username_is_form_six_to_twelve)); return false; } boolean isUserName =
             * isString(userName); boolean isNumber = isNumber(userName); if (!isUserName) {
             * CommonUtility.showMiddleToast(RetrievePasswordActivity.this, null,
             * getString(R.string.login_input_right_user_name_more)); return false; } else if (isNumber) {
             * CommonUtility.showMiddleToast(RetrievePasswordActivity.this, null,
             * getString(R.string.login_input_right_user_name_number)); return false; } return (isUserName &&
             * !isNumber);
             */
            return true;
        }
    }

    private boolean checkCaptcha(String captcha) {
        if (TextUtils.isEmpty(captcha)) {
            CommonUtility.showMiddleToast(RetrievePasswordActivity.this, null, getString(R.string.login_input_code));
            return false;
        } else {
            return true;
        }
    }

    public boolean connected() {
        boolean conn = NetUtility.isNetworkAvailable(RetrievePasswordActivity.this);
        if (!conn) {
            CommonUtility.showMiddleToast(RetrievePasswordActivity.this, null, getString(R.string.login_non_network));
        }
        return conn;
    }

    public boolean checkUser() {
        captcha = input_account_code.getText().toString().trim();
        loginName = input_account_name.getText().toString().trim();
        return (connected() && checkUserName(loginName) && checkCaptcha(captcha));
    }

    void getData() {
        new AsyncTask<Void, Void, FindPasswordStep1>() {

            LoadingDialog dialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = getLoadingDialog();
            }

            @Override
            protected FindPasswordStep1 doInBackground(Void... params) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("userType", selectId);
                map.put("loginName", loginName);
                map.put("captcha", captcha);
                String requestJson = FindPassWordEntity.createRequestJson(map);
                String response = null;
                if (!GlobalApplication.isSupportedHttps) {
                    try {
                        HttpsUtils.initKey(RetrievePasswordActivity.this.getAssets());
                        String url = Constants.URL_PWDRESET_CHECKUSERNAME.replace("http://", "https://");
                        response = HttpsUtils.post(url, requestJson);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    response = NetUtility.sendHttpRequestByPost(Constants.URL_PWDRESET_CHECKUSERNAME, requestJson);
                }
                return FindPassWordEntity.parseStep1Json(response);
            }

            @Override
            protected void onPostExecute(FindPasswordStep1 result) {
                super.onPostExecute(result);
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (isCancelled()) {
                    cancel(true);
                }
                if (result == null) {
                    CommonUtility.showMiddleToast(RetrievePasswordActivity.this, null,
                            getString(R.string.net_exception));
                    return;
                }
                if (!TextUtils.isEmpty(result.getPhoneNum()) && !TextUtils.isEmpty(result.getUserName())) {
                    Intent intent = new Intent(RetrievePasswordActivity.this, RetrievePasswordStep2Activity.class);
                    intent.putExtra("userName", result.getUserName());
                    intent.putExtra("phoneNum", result.getPhoneNum());
                    intent.putExtra("selectId", selectId);
                    startActivityForResult(intent, 100);
                } else {
                    image_account_code.setBackgroundResource(R.drawable.get_code);
                    obtainVerification();
                    if (!TextUtils.isEmpty(result.getFailReason()))
                        CommonUtility.showMiddleToast(RetrievePasswordActivity.this, null, result.getFailReason());
                }

            }

        }.execute();
    }

}
