package com.gome.ecmall.home.login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.bangcle.safekeyboard.PasswordEditText;
import com.gome.ecmall.bean.GetActivateCode;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.UserInfo;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.GlobalApplication;
import com.gome.ecmall.home.ShoppingCartActivity;
import com.gome.ecmall.util.AppMeasurementUtils;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.DES;
import com.gome.ecmall.util.HttpsUtils;
import com.gome.ecmall.util.NetUtility;
import com.gome.ecmall.util.PreferenceUtils;
import com.gome.eshopnew.R;

public class RegisterActivity extends Activity implements OnTouchListener, OnClickListener {// ,OnEditorActionListener
    /** 未登录 */
    public static final int NOT_LOGGED_IN = 101;
    /** 已登录 */
    public static final int LOGGED_IN = 102;
    /** 未激活用户 */
    public static final int NONACTIVATED = 103;
    /** 已激活用户 */
    public static final int ACTIVATED = 104;
    public static final int THIRD_LOGGED_IN = 105;

    private String userName;
    // private String mobel;

    private String pwd;// 密码
    private String cfPwd;// 确认密码
    private String simCode;// 短信密码
    private String enUserName;// 加密用户名
    // private String enMobel;// 加密手机号
    private String enPwd;// 加密密码
    private String enCfPwd;// 加密确认密码
    private String enCode;// 加密短信密码
    // private String enKey;// 注册key

    private Button mCancelBtn;
    private Button mRegisterBtn;
    private Button nextButton;
    private Button getBtn;
    private TextView mTitle;
    
    //快速注册
    private TextView quickTab;
    private TextView normalTab;
    private LinearLayout quickLayout;
    private LinearLayout quickLayoutTwo;
    private TableLayout registerLayout;

    private EditText mUserNameEdit;
    // private EditText mUserMobelEdit;
    private PasswordEditText mUserPwd;
    private PasswordEditText mConfirmPwd;
    private CheckBox mAgreeCheck;

    private TextView mAgreeText;
    private InputMethodManager mInputMethodManager;
    private LoadingDialog mLoadingDialog;
    private SharedPreferences sp;
    private EditText mobileET;
    private Button quickRegisterBtn;
    private TextView checkCodeET;
    private CheckBox mAgreeCheckNormal;
    private TextView mAgreeTextNormal;
    private RelativeLayout quickRalLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_user_register);
        sp = PreferenceUtils.getInstance(getApplicationContext());
        initView();
        mLoadingDialog = null;
    }

    @Override
    protected void onResume() {

        super.onResume();
        AppMeasurementUtils appMeasurementUtils = new AppMeasurementUtils(RegisterActivity.this);
        appMeasurementUtils.getUrl(getString(R.string.appMeas_myGomePage) + ":"
                + getString(R.string.appMeas_start_registar), getString(R.string.appMeas_myGomePage),
                getString(R.string.appMeas_myGomePage) + ":" + getString(R.string.appMeas_start_registar),
                getString(R.string.appMeas_user_center), "", "", "", "", "", "", "", "", "", "", "", "", null);
    }

    private void initView() {
        mCancelBtn = (Button) findViewById(R.id.common_title_btn_back);
        mCancelBtn.setText(R.string.back);
        mCancelBtn.setVisibility(View.VISIBLE);
        mCancelBtn.setOnClickListener(this);

        quickRegisterBtn = (Button) findViewById(R.id.quick_register_button);
        mRegisterBtn = (Button) findViewById(R.id.normal_register_button);
        nextButton = (Button) findViewById(R.id.next_button);
        getBtn = (Button) findViewById(R.id.get_button);
        mRegisterBtn.setText(R.string.login_register);
        mRegisterBtn.setVisibility(View.INVISIBLE);
        mRegisterBtn.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        quickRegisterBtn.setOnClickListener(this);
        getBtn.setOnClickListener(this);

        mTitle = (TextView) findViewById(R.id.common_title_tv_text);
        
        mTitle.setText(R.string.login_new_register);

        //快速注册
        quickTab = (TextView) findViewById(R.id.quick_register_tab);
        normalTab = (TextView) findViewById(R.id.normal_register_tab);
        quickTab.setSelected(true);
        quickTab.setOnClickListener(this);
        normalTab.setOnClickListener(this);
        quickRalLayout = (RelativeLayout) findViewById(R.id.quick_register_ral_layout);
        quickLayout = (LinearLayout) findViewById(R.id.quick_register_table_layout);
        quickLayoutTwo = (LinearLayout) findViewById(R.id.quick_register_table_layout_two);
        registerLayout = (TableLayout) findViewById(R.id.register_table_layout);
        
        checkCodeET = (EditText) findViewById(R.id.check_code);
        mobileET = (EditText) findViewById(R.id.mobile);
        mUserNameEdit = (EditText) findViewById(R.id.register_user_name);
        mUserNameEdit.setOnTouchListener(this);
        // mUserNameEdit.setOnEditorActionListener(this);
        // mUserMobelEdit = (EditText) findViewById(R.id.register_user_mobel);

        mUserPwd = (PasswordEditText) findViewById(R.id.login_register_password);
        mUserPwd.setOnTouchListener(this);

        mConfirmPwd = (PasswordEditText) findViewById(R.id.login_confirm_password);
        mConfirmPwd.setOnTouchListener(this);

        mAgreeText = (TextView) findViewById(R.id.login_agree_text);
        mAgreeText.setOnClickListener(this);

        mAgreeTextNormal = (TextView) findViewById(R.id.login_agree_text_normal);
        mAgreeTextNormal.setOnClickListener(this);
        

        mAgreeCheck = (CheckBox) findViewById(R.id.register_agree_check);
        mAgreeCheckNormal = (CheckBox) findViewById(R.id.register_agree_check_normal);

        mAgreeCheckNormal.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean agreeCheck = mAgreeCheckNormal.isChecked();
                if (!agreeCheck) {
                    mRegisterBtn.setEnabled(false);
                    //CommonUtility.showMiddleToast(RegisterActivity.this, null, getString(R.string.login_agree_check));
                }else{
                    mRegisterBtn.setEnabled(true);
                }
            }
        });
        mAgreeCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkAgree();
            }
        });

        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public boolean connected() {
        boolean conn = NetUtility.isNetworkAvailable(RegisterActivity.this);
        if (!conn) {
            CommonUtility.showMiddleToast(RegisterActivity.this, null, getString(R.string.login_non_network));
        }
        return conn;
    }

    public String getUserName() {
        return mUserNameEdit.getText().toString().trim();
    }

    // public String getMobel() {
    // return mUserMobelEdit.getText().toString().trim();
    // }

    private String getPwd() {
        return mUserPwd.getString().trim();
    }

    private String getConfirmPwd() {
        return mConfirmPwd.getString().trim();
    }

    public boolean isPassword(String strPass) {
        String strPattern = "^[a-zA-Z0-9_]{6,20}$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strPass);
        return m.matches();
    }

    public static boolean isEmail(String str) {
        // String strPattern =
        // "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*([a-zA-Z0-9]{2,4})+$";
        String strPattern = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean isMobile(String str) {
        // String strPattern =
        // "^(0(10|2\\d|[3-9]\\d\\d)[- ]{0,3}\\d{7,8}|0?1[3584]\\d{9})$";
        String strPattern = "^(0?1[3584]\\d{9})$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(str);
        return m.matches();
    }
    public boolean isCheckCode(String strName) {
        String strPattern = "^[A-Za-z\\d\\u4E00-\\u9FA5]+$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strName);
        return m.matches();
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

    public static boolean isCDcard(String cardNum) {
        String strPattern = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(cardNum);
        return m.matches();

    }

    public boolean isUserName(String str) {
        return isString(str);
    }

    public boolean checkAllUserInfo(String userName, String pwd, String cfPwd) {
        if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(pwd) && TextUtils.isEmpty(cfPwd)) {
            CommonUtility.showMiddleToast(RegisterActivity.this, null, getString(R.string.login_input_user_info));
            return false;
        }
        return true;
    }

    public boolean checkUserName(String userName) {
        if (TextUtils.isEmpty(userName)) {
            CommonUtility.showMiddleToast(RegisterActivity.this, null, getString(R.string.login_input_user_name));
            return false;
        } else {
            int userLen = userName.length();
            if (userLen < 6 || userLen > 20) {
                toast(R.string.login_username_is_form_six_to_twelve);
                return false;
            }
            boolean isUserName = isUserName(userName);
            boolean isNumber = isNumber(userName);
            if (!isUserName) {
                CommonUtility.showMiddleToast(RegisterActivity.this, null,
                        getString(R.string.login_input_right_user_name_more));
                return false;
            } else if (isNumber) {
                CommonUtility.showMiddleToast(RegisterActivity.this, null,
                        getString(R.string.login_input_right_user_name_number));
                return false;
            }
            return (isUserName && !isNumber);
        }
    }

    public boolean checkMobel(String mobel) {
        if ((mobel == null) || (mobel.equals(""))) {
            CommonUtility.showMiddleToast(RegisterActivity.this, null, getString(R.string.phone_can_not_null));
            return false;
        } else {
            int len = mobel.trim().length();
            if (len != 11) {
                CommonUtility
                        .showMiddleToast(RegisterActivity.this, null, getString(R.string.login_input_right_mobile));
                return false;
            } else {

                if (isMobile(mobel)) {
                    return true;
                } else {
                    CommonUtility.showMiddleToast(RegisterActivity.this, null,
                            getString(R.string.login_input_right_mobile));
                    return false;
                }
            }
        }
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
            CommonUtility.showMiddleToast(RegisterActivity.this, null, getString(R.string.login_input_password));
            return false;
        } else {
            int len = password.trim().length();
            if (len < 6 || len > 20) {
                CommonUtility.showMiddleToast(RegisterActivity.this, null, getString(R.string.input_right_password));
                return false;
            } else if (!isPassword(password)) {
                CommonUtility.showMiddleToast(RegisterActivity.this, null,
                        getString(R.string.enter_letter_number_symbols_underscores));
                return false;
            } else if (isSameCharacter(password)) {
                toast(R.string.password_cannot_be_same_character);
                return false;
            } else if (isNumber(password)) {
                toast(R.string.password_cannot_be_numbers);
                return false;
            } else if (getUserName() != null && getUserName().equals(getPwd())) {
                toast(R.string.password_cannot_be_same_account_name);
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
            CommonUtility
                    .showMiddleToast(RegisterActivity.this, null, getString(R.string.login_input_confirm_password));
            return false;
        } else {
            boolean isPassword = isPassword(password);
            if (!isPassword) {
                CommonUtility.showMiddleToast(RegisterActivity.this, null, getString(R.string.login_pwd_confirm_err));
            }
            return isPassword;
        }
    }

    private boolean checkAgree() {
        boolean agreeCheck = mAgreeCheck.isChecked();
        if (!agreeCheck) {
            nextButton.setEnabled(false);
            //CommonUtility.showMiddleToast(RegisterActivity.this, null, getString(R.string.login_agree_check));
        }else{
            nextButton.setEnabled(true);
        }
        return agreeCheck;
    }

    private boolean matchesPassword(String pwd1, String pwd2) {
        if (pwd1 == null || pwd1.equals("") || pwd2.equals("") || pwd2 == null)
            return false;
        boolean flag = pwd1.equals(pwd2);
        if (!flag) {
            CommonUtility.showMiddleToast(RegisterActivity.this, null, getString(R.string.login_pwd_confirm_err));
        }
        return flag;
    }

    private boolean checkUser() {
        userName = getUserName();
        // mobel = getMobel();
        pwd = getPwd();
        cfPwd = getConfirmPwd();

        return (connected() && checkAllUserInfo(userName, pwd, cfPwd) && checkUserName(userName) && checkPassowrd(pwd)
                && checkConfirmPassowrd(cfPwd) && matchesPassword(pwd, cfPwd) && checkAgree());
    }

    public void register() {
        if (!checkUser())
            return;
        try {
            enUserName = DES.encryptDES(userName, Constants.LOGINDESKEY);
            // enMobel = DES.encryptDES(mobel, Constants.LOGINDESKEY);
            enPwd = DES.encryptDES(pwd, Constants.LOGINDESKEY);
            enCfPwd = DES.encryptDES(cfPwd, Constants.LOGINDESKEY);
            // enKey = DES.encryptDES(Constants.PRIVATE_KEY,
            // Constants.LOGINDESKEY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new AsyncTask<String, Void, UserInfo>() {

            protected void onPreExecute() {

                mLoadingDialog = CommonUtility.showLoadingDialog(RegisterActivity.this, getString(R.string.loading),
                        true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            }

            @Override
            protected UserInfo doInBackground(String... params) {
                String json = Register.createRequestRegister(enUserName, "", enPwd, enCfPwd, "", "");
                String result = null;
                if (!GlobalApplication.isSupportedHttps) {
                    try {
                        HttpsUtils.initKey(RegisterActivity.this.getAssets());
                        String url = Constants.URL_PROFILE_USER_REGESTER.replace("http://", "https://");
                        result = HttpsUtils.post(url, json);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    result = NetUtility.sendHttpRequestByPost(Constants.URL_PROFILE_USER_REGESTER, json);
                }
                if (result == null || JsonInterface.JV_FAIL.equals(result)) {
                    // CommonUtility.showMiddleToast(RegisterActivity.this, null, getString(R.string.net_exception));
                    return null;
                }
                // if (JsonInterface.JV_FAIL.equals(result)) {
                // CommonUtility.showMiddleToast(RegisterActivity.this, null, getString(R.string.net_exception));
                // return null;
                // }
                return Register.parseJsonUserInfo(result);

                // UserInfo info = new UserInfo();
                // info.setIsSuccess("Y");
                // info.setIsActivated("N");
                // return info;
            }

            protected void onPostExecute(UserInfo info) {
                mLoadingDialog.dismiss();

                if (info == null) {
                    CommonUtility.showMiddleToast(RegisterActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }

                String isSuccess = info.getIsSuccess();
                if (JsonInterface.JV_NO.equalsIgnoreCase(isSuccess)) {
                    String failResult = info.getFailReason();
                    if (failResult == null || failResult.equals("")) {
                        return;
                    }
                    CommonUtility.showMiddleToast(RegisterActivity.this, null, failResult);
                    GlobalConfig.isRegister = false;
                    return;
                } else if (JsonInterface.JV_YES.equalsIgnoreCase(isSuccess)) {
                    CommonUtility.showMiddleToast(RegisterActivity.this, null, getString(R.string.register_ok));
                    GlobalConfig.isRegister = true;
                    GlobalConfig.isLogin = true;
                }

                if (GlobalConfig.isLogin) {
                    AppMeasurementUtils appMeasurementUtils = new AppMeasurementUtils(RegisterActivity.this);
                    appMeasurementUtils.setUserName(info.getLoginName());
                    appMeasurementUtils.setMobile(info.getMobile());
                    appMeasurementUtils.getUrl(getString(R.string.appMeas_myGomePage) + ":"
                            + getString(R.string.appMeas_registar), getString(R.string.appMeas_myGomePage),
                            getString(R.string.appMeas_myGomePage) + ":" + getString(R.string.appMeas_registar),
                            getString(R.string.appMeas_user_center), "", "", AppMeasurementUtils.EVENT_REG_SUCCESS, "",
                            "", "", "", "", "", "", "", "", null);
                    ShoppingCartActivity.getTotalShoppingNumber();
                    setAutoLogin(true);
                    LoginManager.setFirstLogin(RegisterActivity.this);
                    LoginManager.saveUser(RegisterActivity.this, info);
                    LoginManager.saveUser(RegisterActivity.this, userName, pwd);

                    if (JsonInterface.JV_NO.equalsIgnoreCase(info.getIsActivated())) {// 未激活
                        // goActivate(info.getMobile());
                        goActivate(null);
                    } else if (JsonInterface.JV_YES.equalsIgnoreCase(info.getIsActivated())) {
                        dispatchClass();
                    }
                }
            };
        }.execute();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
        case R.id.register_user_name:

            mInputMethodManager.hideSoftInputFromInputMethod(mUserNameEdit.getWindowToken(), 0);
            break;
        case R.id.login_register_password:
            if (mUserNameEdit.requestFocus()) {
                mUserNameEdit.clearFocus();
            }
            if (mConfirmPwd.requestFocus()) {
                mConfirmPwd.clearFocus();
            }
            mUserPwd.requestFocus();
            mUserPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            break;
        case R.id.login_confirm_password:
            if (mUserNameEdit.requestFocus()) {
                mUserNameEdit.clearFocus();
            }
            if (mConfirmPwd.requestFocus()) {
                mUserPwd.clearFocus();
            }
            mConfirmPwd.requestFocus();
            mConfirmPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            break;
        case R.id.common_title_btn_back:
            // Intent intent = new Intent(RegisterActivity.this,
            // LoginActivity.class);
            // setResult(1, intent);
            // finish();
            back();
        case R.id.common_title_btn_right:
            register();
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
        case R.id.login_agree_text:
            startActivity(new Intent(getApplicationContext(), LoginAgreeActivity.class));
            break;
        case R.id.login_agree_text_normal:
            startActivity(new Intent(getApplicationContext(), LoginAgreeActivity.class));
            break;
        case R.id.common_title_btn_back:
            back();
            break;
        case R.id.normal_register_button:

            if (checkUser())
                register();
            break;
        case R.id.register_user_name:
            mInputMethodManager.hideSoftInputFromInputMethod(mUserNameEdit.getWindowToken(), 0);
            break;
        case R.id.quick_register_tab:
            quickRalLayout.setVisibility(View.VISIBLE);
            registerLayout.setVisibility(View.GONE);
            mTitle.setText(R.string.login_new_register);
            mRegisterBtn.setVisibility(View.INVISIBLE);
            quickTab.setSelected(true);
            normalTab.setSelected(false);
            break;
        case R.id.normal_register_tab:
            quickRalLayout.setVisibility(View.GONE);
            registerLayout.setVisibility(View.VISIBLE);
            mTitle.setText(R.string.login_quick_register);
            mRegisterBtn.setVisibility(View.VISIBLE);
            quickTab.setSelected(false);
            normalTab.setSelected(true);
            break;
        case R.id.next_button:
            mobileNum = mobileET.getText().toString().trim();
            if ("".equals(mobileNum)) {
                toast(getString(R.string.login_input_mobile));
                return;
            } else if (!isMobile(mobileNum)) {
                toast(getString(R.string.login_input_right_mobile));
                return;
            }
            getMobileCode(false);//是否立即倒计时
            break;
        case R.id.get_button:
            getMobileCode(true);//是否立即倒计时
            break;
        case R.id.quick_register_button:
            if ("".equals(checkCodeET.getText().toString().trim())) {
                toast(getString(R.string.regedit_password_msg));
                return;
            }
            activeAccount();
            break;
        }

    }
    /**
     * 激活账号
     */
    private void activeAccount() {
        if (!NetUtility.isNetworkAvailable(RegisterActivity.this)) {
            toast(getString(R.string.net_exception));
            return;
        }
        simCode = checkCodeET.getText().toString();
        try {
            enCode = DES.encryptDES(simCode, Constants.LOGINDESKEY);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        new AsyncTask<Void, Void, UserInfo>() {
            LoadingDialog dialog;
            protected void onPreExecute() {
                if (asyncTask != null) {
                    asyncTask.cancel(true);
                    asyncTask = null;
                }
                dialog = CommonUtility.showLoadingDialog(RegisterActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            };

            @Override
            protected UserInfo doInBackground(Void... params) {
                String request = Register.createFasterRegister(mobileNum,enCode);
                String response = null;
                if (!GlobalApplication.isSupportedHttps) {
                    try {
                        HttpsUtils.initKey(RegisterActivity.this.getAssets());
                        String url = Constants.URL_VACCOUNT_FAST_REGISTER.replace("http://", "https://");
                        response = HttpsUtils.post(url, request);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    response = NetUtility.sendHttpRequestByPost(Constants.URL_VACCOUNT_FAST_REGISTER, request);
                }
                if (response == null) {
                    return null;
                }
                return Register.parseJsonUserInfo(response);

            }

            protected void onPostExecute(UserInfo info) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (info == null) {
                    CommonUtility.showMiddleToast(RegisterActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    return;
                }

                String isSuccess = info.getIsSuccess();
                if (JsonInterface.JV_NO.equalsIgnoreCase(isSuccess)) {
                    String failResult = info.getFailReason();
                    if("E001".equals(info.getFailCode())){
                        stopTimer(false);
                    }
                    if (failResult == null || failResult.equals("")) {
                        return;
                    }
                    CommonUtility.showMiddleToast(RegisterActivity.this, null, failResult);
                    GlobalConfig.isRegister = false;
                    return;
                } else if (JsonInterface.JV_YES.equalsIgnoreCase(isSuccess)) {
                    CommonUtility.showMiddleToast(RegisterActivity.this, null, getString(R.string.quick_register_ok));
                    GlobalConfig.isRegister = true;
                    GlobalConfig.isLogin = true;
                }

                if (GlobalConfig.isLogin) {
                    AppMeasurementUtils appMeasurementUtils = new AppMeasurementUtils(RegisterActivity.this);
                    appMeasurementUtils.setUserName(info.getLoginName());
                    appMeasurementUtils.setMobile(info.getMobile());
                    appMeasurementUtils.getUrl(getString(R.string.appMeas_myGomePage) + ":"
                            + getString(R.string.appMeas_registar), getString(R.string.appMeas_myGomePage),
                            getString(R.string.appMeas_myGomePage) + ":" + getString(R.string.appMeas_registar),
                            getString(R.string.appMeas_user_center), "", "", AppMeasurementUtils.EVENT_REG_SUCCESS, "",
                            "", "", "", "", "", "", "", "", null);
                    ShoppingCartActivity.getTotalShoppingNumber();
                    setAutoLogin(true);
                    LoginManager.setFirstLogin(RegisterActivity.this);
                    LoginManager.saveUser(RegisterActivity.this, info);
                    LoginManager.saveUser(RegisterActivity.this, mobileNum, simCode);

                    if (JsonInterface.JV_NO.equalsIgnoreCase(info.getIsActivated())) {// 未激活
                        // goActivate(info.getMobile());
                        goActivate(null);
                    } else if (JsonInterface.JV_YES.equalsIgnoreCase(info.getIsActivated())) {
                        dispatchClass();
                    }
                }
            };
        }.execute();
    }
    /**
     * 获取手机校验码
     */
    private AsyncTask<String, Integer, GetActivateCode> asyncTask = null;
    private boolean canGetCode = true;
    public String mobileNum;

    /** * 获取手机校验码 
     * @param isPromptly 是否立即倒计时
     * */
    private void getMobileCode(boolean isPromptly) {
        if (!canGetCode) {
            // toast(getString(R.string.cannot_send_message));
            return;
        }
        if(isPromptly){
            updateTime();
        }
        if (asyncTask != null) {
            asyncTask.cancel(true);
            asyncTask = null;
        }
        asyncTask = new MobileCodeTask(isPromptly);
        asyncTask.execute();

    }

    /**
     * 获取手机校验码Task
     * 
     * @author lang
     * 
     */
    class MobileCodeTask extends AsyncTask<String, Integer, GetActivateCode> {
        
        private boolean isPromptly;

        public MobileCodeTask(boolean isPromptly) {
            this.isPromptly = isPromptly;
        }

        LoadingDialog dialog;
        protected void onPreExecute() {
            dialog = CommonUtility.showLoadingDialog(RegisterActivity.this,
                    getString(R.string.loading), true, new OnCancelListener() {

                        @Override
                        public void onCancel(DialogInterface dialog) {
                            cancel(true);
                        }
                    });
        };

        @Override
        protected GetActivateCode doInBackground(String... params) {

            String request = Login.createJsonMobile(mobileNum, null);
            String response = NetUtility.sendHttpRequestByPost(Constants.URL_VACCOUNT_GET_LOGIN_PASSWORD,
                    request);

            return Login.parseJsonMobile(response);

        }


        @Override
        protected void onPostExecute(GetActivateCode result) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }

            if (result == null) {
                toast(getString(R.string.data_load_fail_exception));
                stopTimer(true);
                return;
            }

            if (JsonInterface.JV_NO.equalsIgnoreCase(result.getIsSuccess())) {
                toast(result.getFailReason());
                if("E001".equals(result.getFailCode())||"E002".equals(result.getFailCode())){
                    stopTimer(false);
                }else{
                    stopTimer(true);
                }
                return;
            }
            if(!isPromptly){
                updateTime();//没有立即倒计时，在此处返回成功时再倒计时
            }
            quickLayout.setVisibility(View.GONE);
            quickLayoutTwo.setVisibility(View.VISIBLE);
            asyncTask = null;
        }

    }

    /**
     * 按钮是否可以被点击
     * @param isClickAble
     */
    private void stopTimer(boolean isClickAble) {
        if (timerThread != null) {
            timerThread.interrupt();
            timerThread = null;
        }
        updateView(STOP_TIMER,isClickAble);
    }

    private Thread timerThread = null;
    public int STOP_TIMER = 0;

    private void updateTime() {
        if (timerThread != null) {
            timerThread.interrupt();
            timerThread = null;
        }
        timerThread = new Thread(new TimerRunnable());
        timerThread.start();
    }

    class TimerRunnable implements Runnable {
        @Override
        public void run() {
            try {
                int time = 120;
                while (time > 0) {
                    time--;
                    mHandler.obtainMessage(0, time).sendToTarget();
                    Thread.sleep(1000);
                }
                // mHandler.obtainMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case 0:
                updateView((Integer) msg.obj,true);
            }
        };
    };
    /**
     * 按钮是否可点击
     * @param time
     * @param isClickAble
     */
    void updateView(int time,boolean isClickAble) {
        if(isClickAble){
            
            if (time == 0) {
                canGetCode = true;
                getBtn.setText(getString(R.string.getagain_check_password));
                getBtn.setBackgroundResource(R.drawable.common_orange_btn);
            }else if (time <= 9) {
                canGetCode = false;
                getBtn.setText("   "+time+ " " + getString(R.string.regain_parity_check_code_later));
                getBtn.setBackgroundResource(R.drawable.common_orange_btn_disable);
            } else if (time <= 99) {
                canGetCode = false;
                getBtn.setText(" "+time+" " + getString(R.string.regain_parity_check_code_later));
                getBtn.setBackgroundResource(R.drawable.common_orange_btn_disable);
            } else if (time <=120) {
                canGetCode = false;
                getBtn.setText(time + getString(R.string.regain_parity_check_code_later));
                getBtn.setBackgroundResource(R.drawable.common_orange_btn_disable);
            } 
        }else{
            getBtn.setText(getString(R.string.getagain_check_password));
            getBtn.setBackgroundResource(R.drawable.common_orange_btn_disable);
            getBtn.setEnabled(false);
        }
    }
    void toast(String message) {

        CommonUtility.showMiddleToast(RegisterActivity.this, null, message);
    }
    private void back() {
        if (!GlobalConfig.isLogin) {// 未登录或未注册成功
            setResult(NOT_LOGGED_IN);
        } else {
            setResult(LOGGED_IN);
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ACTIVATED) {
            dispatchClass();
        }

        if (resultCode == NONACTIVATED) {
            dispatchClass();
        }
    }

    /** 返回上一模块 */
    private void dispatchClass() {
        String targetClassName = getIntent().getStringExtra(GlobalConfig.CLASS_NAME);
        Intent intent = new Intent();
        intent.putExtra(GlobalConfig.CLASS_NAME, targetClassName);
        setResult(LOGGED_IN, getIntent());
        finish();
    }

    /** 跳转用户激活界面 */
    private void goActivate(String mobile) {

        Intent intent = new Intent(RegisterActivity.this, ActivateAccountActivity.class);
        intent.putExtra(JsonInterface.JK_MOBILE, mobile);
        startActivityForResult(intent, NONACTIVATED);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (GlobalConfig.isLogin) {// 注册成功
                dispatchClass();
            } else {
                back();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    void toast(int resId) {

        CommonUtility.showMiddleToast(RegisterActivity.this, null, getString(resId));
    }
    /** 设置自动登录Preference */
    public void setAutoLogin(boolean isAutoLogin) {
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(GlobalConfig.IS_AUTO_LOGIN, isAutoLogin);
        edit.commit();
    }
}
