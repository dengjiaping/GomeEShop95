package com.gome.ecmall.home.login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.GetActivateCode;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.PhoneActivation;
import com.gome.ecmall.bean.ShoppingCart.ShoppingGo;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

/**
 * 1.激活账号 校验手机号成功后获取手机激活码，校验激活码正确后请求激活会员账号 2.注册完成 激活账号成功，显示注册完成界面，点击"完成"按钮跳转上一模块
 * 
 * @author jinbin.lang
 * 
 */
public class ActivateAccountActivity extends Activity implements OnClickListener, TextWatcher {

    private boolean isActivated = false;

    private static final String TAG = "ActivateAccountActivity";

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case AUTH_USER:
                updateView((Integer) msg.obj);
            }
        };
    };

    private ShoppingGo shoppingGo;
    private String mobile;

    /** 激活成功 */
    public static int ACTIVATE_CODE_OK = 1;
    /** 放弃激活 */
    public static int ACTIVATE_CODE_CANCEL = 2;

    private static final int STATUS_COUNT = 2;

    /** 获取手机激活码完成 */

    /** 验证用户 */
    private static final int AUTH_USER = 0;
    /** 完成激活 */
    private static final int ACCOUNT_ACTIVATE = 1;

    private int CURRENT_ACTIVATE_STATUS = AUTH_USER;

    private boolean canGetCode = true;

    private TextView mTitleView;
    private Button mBtnRegister;
    private Button mBtnBack;

    /** 验证用户信息的Layout */
    private static LinearLayout[] mCurrStatusLayout = new LinearLayout[STATUS_COUNT];
    /**
     * 验证用户状态对应的Layout id
     */
    private static final int[] mCurrStatusLayoutIds = { R.id.activate_account_auth_user_layout,
            R.id.activate_account_resister_successful_layout };

    /** 验证用户状态时， 右边按钮提示文本 */
    private static int[] BUTTON_STATUS_AUTH = { R.string.submit, R.string.finish };

    /* ====================> 验证帐户信息 <======================= */
    private EditText mEditMobile;
    private ImageView mImageDelMobile;// 删除手机号
    private Button mBtnGetCheck;// 获取校验码
    private TextView mTextTimer;// 倒计时，秒
    private EditText mEditMobileCheck;// 校验码输入框
    private ImageView mImageDelCheck;// 删除检验码

    /* ====================> 完成验证 <======================= */
    private TextView mTextAuthSuccess;
    private TextView mTextRegUser;
    private TextView mTextRegMogile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            shoppingGo = (ShoppingGo) intent.getSerializableExtra(ShoppingGo.SHOPPING_GO);
            if (shoppingGo != null) {
                mobile = shoppingGo.getMobile();
            } else {
                mobile = intent.getStringExtra(JsonInterface.JK_MOBILE);
            }
        }
        setContentView(R.layout.activate_account);
        initView();
        toast(R.string.fill_phone_auth_info);
    }

    private void initView() {

        mTitleView = (TextView) findViewById(R.id.common_title_tv_text);
        mTitleView.setText(R.string.auth_account);
        mTitleView.setVisibility(View.VISIBLE);

        mBtnBack = (Button) findViewById(R.id.common_title_btn_back);
        mBtnBack.setText(R.string.cancel);
        mBtnBack.setOnClickListener(this);

        mBtnRegister = (Button) findViewById(R.id.common_title_btn_right);
        mBtnRegister.setVisibility(View.VISIBLE);
        mBtnRegister.setOnClickListener(this);

        mEditMobile = (EditText) findViewById(R.id.activate_account_mobile_editText);
        mEditMobile.setText(mobile);
        mEditMobile.addTextChangedListener(this);

        mImageDelMobile = (ImageView) findViewById(R.id.activate_account_del_mobile_imageView1);
        mImageDelMobile.setOnClickListener(this);
        mBtnGetCheck = (Button) findViewById(R.id.activate_account_get_mobile_auth_code_button);
        mBtnGetCheck.setOnClickListener(this);
        mTextTimer = (TextView) findViewById(R.id.activate_account_timer_textView);
        mEditMobileCheck = (EditText) findViewById(R.id.activate_account_mobile_check_code_editText);
        mEditMobileCheck.addTextChangedListener(new CheckCodeTextWatcher());
        mImageDelCheck = (ImageView) findViewById(R.id.activate_account_del_check_code_imageView);
        mImageDelCheck.setOnClickListener(this);

        mTextAuthSuccess = (TextView) findViewById(R.id.activate_account_success_auth_ok_textView);
        mTextRegUser = (TextView) findViewById(R.id.activate_account_success_account_textView);
        mTextRegMogile = (TextView) findViewById(R.id.activate_account_success_phone_number_textView);

        for (int i = 0; i < STATUS_COUNT; i++) {
            mCurrStatusLayout[i] = (LinearLayout) findViewById(mCurrStatusLayoutIds[i]);
        }

        setCurrentView(CURRENT_ACTIVATE_STATUS);

    }

    /**
     * 获取手机校验码
     */
    private AsyncTask<String, Integer, GetActivateCode> asyncTask = null;

    /** * 获取手机校验码 */
    private void getMobileCode() {
        if (!canGetCode) {
            toast(R.string.cannot_send_message);
            return;
        }
        if (checkPhone()) {
            mImageDelMobile.setVisibility(View.GONE);
            mBtnGetCheck.setText(R.string.gegain_check_code);
            mBtnGetCheck.setBackgroundResource(R.drawable.common_orange_btn_disable);
            mTextTimer.setVisibility(View.VISIBLE);
            updateTime();
            if (asyncTask != null) {
                asyncTask.cancel(true);
                asyncTask = null;
            }
            asyncTask = new MobileCodeTask();
            asyncTask.execute();
        }
    }

    /**
     * 激活账号
     */
    private void activeAccount() {
        if (!NetUtility.isNetworkAvailable(ActivateAccountActivity.this)) {
            toast(R.string.net_exception);
            return;
        }

        if (checkPhone() && checkMobileCode()) {
            new AsyncTask<Void, Void, PhoneActivation>() {
                LoadingDialog dialog;

                protected void onPreExecute() {
                    mImageDelMobile.setVisibility(View.GONE);
                    if (asyncTask != null) {
                        asyncTask.cancel(true);
                        asyncTask = null;
                    }
                    dialog = CommonUtility.showLoadingDialog(ActivateAccountActivity.this, getString(R.string.loading),
                            true, new OnCancelListener() {

                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    cancel(true);
                                }
                            });
                };

                @Override
                protected PhoneActivation doInBackground(Void... params) {

                    String request = Register.createActivateMobile(mEditMobile.getText().toString(), mEditMobileCheck
                            .getText().toString());
                    String response = NetUtility.sendHttpRequestByPost(
                            Constants.URL_PROFILE_VALIDATE_MOBILE_VERIFICTIONCODE, request);
                    if (response == null) {
                        return null;
                    }
                    return Register.parseActivateMobile(response);

                    // PhoneActivation pa = new PhoneActivation();
                    // pa.setIsSuccess("Y");
                    // pa.setLoginName("test");
                    // pa.setMobile("13333333333");
                    // return pa;

                }

                protected void onPostExecute(PhoneActivation result) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    if (result == null) {
                        toast(R.string.fail_to_activate_account);
                        isActivated = false;
                        return;
                    }

                    if (JsonInterface.JV_YES.equalsIgnoreCase(result.getIsSuccess())) {
                        CURRENT_ACTIVATE_STATUS = ACCOUNT_ACTIVATE;
                        setCurrentView(CURRENT_ACTIVATE_STATUS);
                        setAuthSuccess();
                        mTextRegUser.append("\t\t" + result.getLoginName());
                        mTextRegMogile.append("\t\t" + result.getMobile());
                        isActivated = true;
                    } else if (JsonInterface.JV_NO.equalsIgnoreCase(result.getIsSuccess())) {
                        toast(result.getFailReason());
                        isActivated = false;
                        canGetCode = true;
                        mTextTimer.setVisibility(View.GONE);
                        mBtnGetCheck.setBackgroundResource(R.drawable.common_orange_btn);
                        return;
                    }
                };
            }.execute();
        }
    }

    /**
     * 完成激活
     */
    private void completeActivate() {
        setResult(RegisterActivity.ACTIVATED, getIntent());
        finish();
    }

    /** 返回 */
    private void back() {
        if (isActivated) {
            setResult(RegisterActivity.ACTIVATED, getIntent());
            finish();
        } else {
            CommonUtility.showConfirmDialog(ActivateAccountActivity.this, null,
                    getString(R.string.cannot_complete_shipping_information), getString(R.string.confirm),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            setResult(RegisterActivity.NONACTIVATED, getIntent());
                            finish();
                        }
                    }, getString(R.string.cancel), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
        }
    }

    /**
     * 根据当前进度显示对应的View
     * 
     * @param currId
     */
    void setCurrentView(int currId) {
        int len = mCurrStatusLayout.length;
        for (int i = 0; i < len; i++) {
            mCurrStatusLayout[i].setVisibility(View.GONE);
            if (currId == i) {
                mCurrStatusLayout[i].setVisibility(View.VISIBLE);
                mBtnRegister.setText(BUTTON_STATUS_AUTH[i]);
            }
            if (currId == ACCOUNT_ACTIVATE) {
                mBtnBack.setVisibility(View.INVISIBLE);
            } else {
                mBtnBack.setVisibility(View.VISIBLE);
            }
        }
    }

    /** 激活成功提示信息 */
    private void setAuthSuccess() {
        if (CURRENT_ACTIVATE_STATUS == ACCOUNT_ACTIVATE) {
            mTextAuthSuccess.setText(R.string.complete_verified);
            mTitleView.setText(R.string.validation_successful);
        }
    }

    /** 校验手机号码 */
    private boolean checkPhone() {
        String mobile = mEditMobile.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            toast(R.string.enter_your_phone_number);
            return false;
        }

        String exp = "^(1[3584]\\d{9})$";
        Pattern p = Pattern.compile(exp);
        Matcher m = p.matcher(mobile);
        boolean isPhoneNum = m.matches();
        if (!isPhoneNum) {
            toast(R.string.phone_number_errors);
        }
        return isPhoneNum;
    }

    /**
     * 获取手机校验码Task
     * 
     * @author lang
     * 
     */
    class MobileCodeTask extends AsyncTask<String, Integer, GetActivateCode> {

        @Override
        protected void onPreExecute() {
            if (isCancelled()) {
                cancel(true);
            }
        }

        @Override
        protected GetActivateCode doInBackground(String... params) {

            String request = Login.createJsonMobile(mEditMobile.getText().toString(), "0");
            String response = NetUtility.sendHttpRequestByPost(Constants.URL_PROFILE_GENERATE_MOBILE_VERIFICATIONCODE,
                    request);

            if (null == response || JsonInterface.JV_FAIL.equalsIgnoreCase(response)) {
                toast(R.string.data_load_fail_exception);
                return null;
            }

            return Login.parseJsonMobile(response);

        }

        @Override
        protected void onCancelled() {
            cancel(true);
        }

        @Override
        protected void onPostExecute(GetActivateCode result) {
            if (result == null) {
                toast(R.string.data_load_fail_exception);
                stopTimer();
                return;
            }

            if (JsonInterface.JV_NO.equalsIgnoreCase(result.getIsSuccess())) {
                toast(result.getFailReason());
                stopTimer();
                return;
            }
            asyncTask = null;
        }

    }

    /**
     * 使View获得或失去焦点
     */
    private void bindViewStatus(View v, boolean flag) {
        v.setClickable(flag);
        v.setFocusable(flag);
        v.setFocusableInTouchMode(flag);
    }

    /**
     * 校验手机激活码
     * 
     * @return
     */
    boolean checkMobileCode() {
        String code = mEditMobileCheck.getText().toString();
        if (TextUtils.isEmpty(code)) {
            toast(R.string.input_check_code);
            return false;
        }
        if (code != null && code.length() != 4) {
            toast(R.string.input_check_code);
            return false;
        } else {
            return true;
        }
    }

    void updateView(int time) {
        mTextTimer.setText(time + getString(R.string.regain_parity_check_code_later));
        if (time >= 45 && time < 60) {
            canGetCode = false;
            bindViewStatus(mEditMobile, false);
        } else if (time < 45) {
            bindViewStatus(mEditMobile, true);
            if (time == 0) {
                canGetCode = true;
                mTextTimer.setVisibility(View.GONE);
                mBtnGetCheck.setBackgroundResource(R.drawable.common_orange_btn);
            }
        }
    }

    class TimerRunnable implements Runnable {
        @Override
        public void run() {
            try {
                int time = 60;
                while (time > 0) {
                    time--;
                    mHandler.obtainMessage(AUTH_USER, time).sendToTarget();
                    Thread.sleep(1000);
                }
                // mHandler.obtainMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private Thread timerThread = null;

    private void updateTime() {
        if (timerThread != null) {
            timerThread.interrupt();
            timerThread = null;
        }
        timerThread = new Thread(new TimerRunnable());
        timerThread.start();
    }

    private static final int STOP_TIMER = 0;

    private void stopTimer() {
        if (timerThread != null) {
            timerThread.interrupt();
            timerThread = null;
        }
        updateView(STOP_TIMER);
    }

    @Override
    public void onClick(View v) {
        if (mBtnRegister == v) {
            switch (CURRENT_ACTIVATE_STATUS) {

            case AUTH_USER:
                activeAccount();// 注册第二步，执行注册操作
                break;
            case ACCOUNT_ACTIVATE:
                completeActivate();// 注册完成，跳转
                break;
            default:
                break;
            }
        }

        if (mBtnBack == v) {
            back();
        }

        if (mBtnGetCheck == v) {
            getMobileCode();
        }

        if (mImageDelMobile == v) {
            mEditMobile.setText("");
        }

        if (v == mImageDelCheck) {
            mEditMobileCheck.setText("");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(mEditMobile.getText())) {
            mImageDelMobile.setVisibility(View.GONE);
        } else {
            mImageDelMobile.setVisibility(View.VISIBLE);
        }

        // canGetCode = true;
        // mBtnGetCheck.setBackgroundResource(R.drawable.common_orange_btn);
        stopTimer();
    }

    class CheckCodeTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String code = mEditMobileCheck.getText().toString();
            if (TextUtils.isEmpty(code)) {
                mImageDelCheck.setVisibility(View.GONE);
            } else {
                mImageDelCheck.setVisibility(View.VISIBLE);
            }
            if (code != null && code.length() > 6) {
                mEditMobileCheck.setText(code.substring(0, 6));
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    }

    private void toast(int resId) {
        CommonUtility.showMiddleToast(ActivateAccountActivity.this, null, getString(resId));
    }

    private void toast(String msg) {
        CommonUtility.showMiddleToast(ActivateAccountActivity.this, null, msg);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
        }
        return super.onKeyDown(keyCode, event);
    }
}
