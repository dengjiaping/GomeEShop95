package com.gome.ecmall.home.login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

import com.bangcle.safekeyboard.PasswordEditText;
import com.gome.ecmall.bean.GetActivateCode;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.GlobalApplication;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.DES;
import com.gome.ecmall.util.HttpsUtils;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class RetrievePasswordStep3Activity extends Activity implements OnTouchListener, OnClickListener {

    private static final String TAG = "RetrievePasswordStep3Activity";

    /** 标题头部分 */
    Button backBtn;
    Button shutBtn;
    TextView titleText;

    private PasswordEditText newPwdET;
    private PasswordEditText confirmET;
    private Button submitBtn;
    private LoadingDialog mLoadingDialog;

    private String pwd;// 密码
    private String cfPwd;// 确认密码
    private String enPwd;// 加密密码
    private String enCfPwd;// 加密确认密码
    private String token;

    private String selectId;

    private String loginName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_new_password);
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        selectId = intent.getStringExtra("selectId");
        loginName = intent.getStringExtra("loginName");
        initView();
        mLoadingDialog = null;
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
        newPwdET = (PasswordEditText) findViewById(R.id.set_new_password);
        newPwdET.setOnTouchListener(this);
        confirmET = (PasswordEditText) findViewById(R.id.set_agin_new_password);
        confirmET.setOnTouchListener(this);
        submitBtn = (Button) findViewById(R.id.submit_button);
        submitBtn.setOnClickListener(this);

    }

    private String getNewPwd() {
        return newPwdET.getString().trim();
    }

    private String getConfirmPwd() {
        return confirmET.getString().trim();
    }

    @Override
    public void onClick(View v) {
        if (v == backBtn) {
            setResult(3);
            this.finish();
        } else if (v == shutBtn) {
            setResult(2);
            this.finish();
        } else if (v == submitBtn) {
            register();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
        case R.id.set_new_password:
            newPwdET.requestFocus();
            newPwdET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            break;
        case R.id.set_agin_new_password:
            confirmET.requestFocus();
            confirmET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            break;
        }
        return false;
    }

    public boolean isPassword(String strPass) {
        String strPattern = "^[a-zA-Z0-9_]{6,20}$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strPass);
        return m.matches();
    }

    public boolean isNumber(String strName) {
        String strPattern = "^[0-9]+$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strName);
        return m.matches();
    }

    /**
     * 判断是否为相同字符，字符数量限在6-20
     * 
     * @return true if same,
     */
    private boolean isSameCharacter(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("([0-9a-zA-Z_])\\1{5,19}");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public boolean checkPassowrd(String password) {
        if (TextUtils.isEmpty(password)) {
            CommonUtility.showMiddleToast(RetrievePasswordStep3Activity.this, null,
                    getString(R.string.login_input_password));
            return false;
        } else {
            int len = password.trim().length();
            if (len < 6 || len > 20) {
                CommonUtility.showMiddleToast(RetrievePasswordStep3Activity.this, null,
                        getString(R.string.input_right_password));
                return false;
            } else if (!isPassword(password)) {
                CommonUtility.showMiddleToast(RetrievePasswordStep3Activity.this, null,
                        getString(R.string.enter_letter_number_symbols_underscores));
                return false;
            } else if (isSameCharacter(password)) {
                CommonUtility.showMiddleToast(RetrievePasswordStep3Activity.this, null,
                        getString(R.string.password_cannot_be_same_character));
                return false;
            } else if (isNumber(password)) {
                CommonUtility.showMiddleToast(RetrievePasswordStep3Activity.this, null,
                        getString(R.string.password_cannot_be_numbers));
                return false;
            } else {
                return true;
            }

            // return
            // (isPassword(password)&&!isSameCharacter(password)&&!isNumber(password)&&(!getUserName().equals(getPwd())));
        }
    }

    public boolean checkConfirmPassowrd(String password) {
        if ((password == null) || (password.equals(""))) {
            CommonUtility.showMiddleToast(RetrievePasswordStep3Activity.this, null,
                    getString(R.string.login_input_confirm_password));
            return false;
        } else {
            boolean isPassword = isPassword(password);
            if (!isPassword) {
                CommonUtility.showMiddleToast(RetrievePasswordStep3Activity.this, null,
                        getString(R.string.login_pwd_confirm_err));
            }
            return isPassword;
        }
    }

    private boolean matchesPassword(String pwd1, String pwd2) {
        if (pwd1 == null || pwd1.equals("") || pwd2.equals("") || pwd2 == null)
            return false;
        boolean flag = pwd1.equals(pwd2);
        if (!flag) {
            CommonUtility.showMiddleToast(RetrievePasswordStep3Activity.this, null,
                    getString(R.string.login_pwd_confirm_err));
        }
        return flag;
    }

    public boolean connected() {
        boolean conn = NetUtility.isNetworkAvailable(RetrievePasswordStep3Activity.this);
        if (!conn) {
            CommonUtility.showMiddleToast(RetrievePasswordStep3Activity.this, null,
                    getString(R.string.login_non_network));
        }
        return conn;
    }

    private boolean checkUser() {
        pwd = getNewPwd();
        cfPwd = getConfirmPwd();
        return (connected() && checkPassowrd(pwd) && checkConfirmPassowrd(cfPwd) && matchesPassword(pwd, cfPwd));
    }

    public void register() {
        if (!checkUser())
            return;
        try {
            enPwd = DES.encryptDES(pwd, Constants.LOGINDESKEY);
            enCfPwd = DES.encryptDES(cfPwd, Constants.LOGINDESKEY);
            // enKey = DES.encryptDES(Constants.PRIVATE_KEY,
            // Constants.LOGINDESKEY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new AsyncTask<String, Void, GetActivateCode>() {

            protected void onPreExecute() {

                mLoadingDialog = CommonUtility.showLoadingDialog(RetrievePasswordStep3Activity.this,
                        getString(R.string.loading), true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected GetActivateCode doInBackground(String... params) {
                String key = LoginManager.getKey();
                String sign = LoginManager.getSign(loginName, pwd, key);
                String json = Register.createRequestNewPassword(selectId, loginName, enPwd, enCfPwd, sign, token);
                String result = null;
                if (!GlobalApplication.isSupportedHttps) {
                    try {
                        HttpsUtils.initKey(RetrievePasswordStep3Activity.this.getAssets());
                        String url = Constants.URL_FINDBACK_PASSWORD.replace("http://", "https://");
                        result = HttpsUtils.post(url, json);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    result = NetUtility.sendHttpRequestByPost(Constants.URL_FINDBACK_PASSWORD, json);
                }
                if (result == null || JsonInterface.JV_FAIL.equals(result)) {
                    // CommonUtility.showMiddleToast(RegisterActivity.this, null, getString(R.string.net_exception));
                    return null;
                }
                return Login.parseJsonMobile(result);

            }

            protected void onPostExecute(GetActivateCode result) {
                mLoadingDialog.dismiss();

                if (result == null) {
                    CommonUtility.showMiddleToast(RetrievePasswordStep3Activity.this, null,
                            getString(R.string.data_load_fail_exception));
                    return;
                }

                if (JsonInterface.JV_NO.equalsIgnoreCase(result.getIsSuccess())) {
                    CommonUtility.showMiddleToast(RetrievePasswordStep3Activity.this, null, result.getFailReason());
                    return;
                }
                if (JsonInterface.JV_YES.equalsIgnoreCase(result.getIsSuccess())) {
                    CommonUtility.showMiddleToast(RetrievePasswordStep3Activity.this, null,
                            getString(R.string.login_new_passoword_ok));
                    setResult(2);
                    RetrievePasswordStep3Activity.this.finish();
                }

            };
        }.execute();
    }

}
