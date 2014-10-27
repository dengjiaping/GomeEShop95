package com.gome.ecmall.home.mygome;

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
import com.gome.ecmall.home.login.Login;
import com.gome.ecmall.home.login.Register;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.DES;
import com.gome.ecmall.util.HttpsUtils;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class ModifyPaymentPassword2Activity extends Activity implements OnTouchListener, OnClickListener {// ,OnEditorActionListener

    private String oldPwd;// 密码
    private String pwd;// 密码
    private String cfPwd;// 确认密码
    private String enOldPwd;// 加密旧密码
    private String enPwd;// 加密密码
    private String enCfPwd;// 加密确认密码

    private Button backBtn;
    private Button closeBtn;
    private Button submitBtn;
    private TextView mTitle;

    private PasswordEditText oldPwdET;
    private PasswordEditText newPwdET;
    private PasswordEditText confirmET;

    private LoadingDialog mLoadingDialog;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_payment_password2);
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        initView();
        mLoadingDialog = null;
    }

    private void initView() {

        backBtn = (Button) findViewById(R.id.common_title_btn_back);
        backBtn.setText(R.string.back);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(this);

        closeBtn = (Button) findViewById(R.id.common_title_btn_right);
        closeBtn.setText(R.string.phone_recharge_close);
        closeBtn.setVisibility(View.VISIBLE);
        closeBtn.setOnClickListener(this);

        mTitle = (TextView) findViewById(R.id.common_title_tv_text);
        mTitle.setText(R.string.mygome_modify_payment_password);

        oldPwdET = (PasswordEditText) findViewById(R.id.old_password);
        oldPwdET.setOnTouchListener(this);
        newPwdET = (PasswordEditText) findViewById(R.id.new_password);
        newPwdET.setOnTouchListener(this);
        confirmET = (PasswordEditText) findViewById(R.id.confirm_password);
        confirmET.setOnTouchListener(this);

        submitBtn = (Button) findViewById(R.id.submit_button);
        submitBtn.setOnClickListener(this);

    }

    public boolean connected() {
        boolean conn = NetUtility.isNetworkAvailable(ModifyPaymentPassword2Activity.this);
        if (!conn) {
            CommonUtility.showMiddleToast(ModifyPaymentPassword2Activity.this, null,
                    getString(R.string.login_non_network));
        }
        return conn;
    }

    private String getOldPwd() {
        return oldPwdET.getString().trim();
    }

    private String getNewPwd() {
        return newPwdET.getString().trim();
    }

    private String getConfirmPwd() {
        return confirmET.getString().trim();
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

    public boolean isCheckCode(String strName) {
        String strPattern = "^[A-Za-z\\d\\u4E00-\\u9FA5]+$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strName);
        return m.matches();
    }

    public boolean checkAllUserInfo(String userName, String pwd, String cfPwd) {
        if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(pwd) && TextUtils.isEmpty(cfPwd)) {
            CommonUtility.showMiddleToast(ModifyPaymentPassword2Activity.this, null,
                    getString(R.string.login_input_user_info));
            return false;
        }
        return true;
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
            CommonUtility.showMiddleToast(ModifyPaymentPassword2Activity.this, null,
                    getString(R.string.login_input_password));
            return false;
        } else {
            int len = password.trim().length();
            if (len < 6 || len > 20) {
                CommonUtility.showMiddleToast(ModifyPaymentPassword2Activity.this, null,
                        getString(R.string.input_right_password));
                return false;
            } else if (!isPassword(password)) {
                CommonUtility.showMiddleToast(ModifyPaymentPassword2Activity.this, null,
                        getString(R.string.enter_letter_number_symbols_underscores));
                return false;
            } else if (isSameCharacter(password)) {
                toast(getString(R.string.password_cannot_be_same_character));
                return false;
            } else if (isNumber(password)) {
                toast(getString(R.string.password_cannot_be_numbers));
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
            CommonUtility.showMiddleToast(ModifyPaymentPassword2Activity.this, null,
                    getString(R.string.login_input_confirm_password));
            return false;
        } else {
            boolean isPassword = isPassword(password);
            if (!isPassword) {
                CommonUtility.showMiddleToast(ModifyPaymentPassword2Activity.this, null,
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
            CommonUtility.showMiddleToast(ModifyPaymentPassword2Activity.this, null,
                    getString(R.string.login_pwd_confirm_err));
        }
        return flag;
    }

    private boolean checkUser() {
        oldPwd = getOldPwd();
        pwd = getNewPwd();
        cfPwd = getConfirmPwd();
        return (connected() && checkPassowrd(oldPwd) && checkPassowrd(pwd) && checkConfirmPassowrd(cfPwd) && matchesPassword(
                pwd, cfPwd));
    }

    public void register() {
        if (!checkUser())
            return;
        try {
            enOldPwd = DES.encryptDES(oldPwd, Constants.LOGINDESKEY);
            enPwd = DES.encryptDES(pwd, Constants.LOGINDESKEY);
            enCfPwd = DES.encryptDES(cfPwd, Constants.LOGINDESKEY);
            // enKey = DES.encryptDES(Constants.PRIVATE_KEY,
            // Constants.LOGINDESKEY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new AsyncTask<String, Void, GetActivateCode>() {

            protected void onPreExecute() {

                mLoadingDialog = CommonUtility.showLoadingDialog(ModifyPaymentPassword2Activity.this,
                        getString(R.string.loading), true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected GetActivateCode doInBackground(String... params) {
                String json = Register.createRequestPaymentPassword(enOldPwd, enPwd, enCfPwd, token);
                String result = null;
                if (!GlobalApplication.isSupportedHttps) {
                    try {
                        HttpsUtils.initKey(ModifyPaymentPassword2Activity.this.getAssets());
                        String url = Constants.URL_VACCOUNT_SETORCHANGE_VIRTUALACCOUNTPWD
                                .replace("http://", "https://");
                        result = HttpsUtils.post(url, json);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    result = NetUtility.sendHttpRequestByPost(Constants.URL_VACCOUNT_SETORCHANGE_VIRTUALACCOUNTPWD,
                            json);
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
                    CommonUtility.showMiddleToast(ModifyPaymentPassword2Activity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }
                if (JsonInterface.JV_NO.equalsIgnoreCase(result.getIsSuccess())) {
                    CommonUtility.showMiddleToast(ModifyPaymentPassword2Activity.this, null, result.getFailReason());
                    return;
                }
                if (JsonInterface.JV_YES.equalsIgnoreCase(result.getIsSuccess())) {
                    CommonUtility.showMiddleToast(ModifyPaymentPassword2Activity.this, null,
                            getString(R.string.login_new_passoword_ok));
                    setResult(2);
                    ModifyPaymentPassword2Activity.this.finish();
                }

            };
        }.execute();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
        case R.id.old_password:
            oldPwdET.requestFocus();
            oldPwdET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            break;
        case R.id.new_password:
            newPwdET.requestFocus();
            newPwdET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            break;
        case R.id.confirm_password:
            confirmET.requestFocus();
            confirmET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            break;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        case R.id.common_title_btn_back:
            finish();
            break;
        case R.id.common_title_btn_right:
            setResult(2);
            finish();
            break;
        case R.id.submit_button:
            register();
            break;

        }

    }

    void toast(String message) {

        CommonUtility.showMiddleToast(ModifyPaymentPassword2Activity.this, null, message);
    }

}
