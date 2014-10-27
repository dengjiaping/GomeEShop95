package com.gome.ecmall.home.groupbuy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gome.ecmall.bean.GetActivateCode;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.GlobalApplication;
import com.gome.ecmall.home.login.ActivateAccountActivity;
import com.gome.ecmall.home.login.Login;
import com.gome.ecmall.home.login.Register;
import com.gome.ecmall.home.login.RegisterActivity;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.HttpsUtils;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;
import com.gome.eshopnew.R.string;

public class SetGroupBuyMobileActivity extends Activity implements OnClickListener {// ,OnEditorActionListener
    private String mobileNum;
    private String checkCode;

    private Button backBtn;
    private Button getBtn;
    private Button nextBtn;
    private TextView mTitle;

    private EditText mobileNumET;
    private EditText checkCodeET;

    private GlobalApplication applcation;
    private LoadingDialog mLoadingDialog;

    private boolean canGetCode = true;
    private TextView check_phone_describe;
    /**
     * 设置团购绑定手机号
     */
    public final static String OPERATE_TYPE_SET = "1";
    /**
     * 修改团购绑定手机号
     */
    public final static String OPERATE_TYPE_MOD = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_group_buy_mobile);
        initView();
        applcation = (GlobalApplication) getApplication();
        mLoadingDialog = null;
    }

    private void initView() {

        backBtn = (Button) findViewById(R.id.common_title_btn_back);
        backBtn.setText(R.string.back);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(this);

        mTitle = (TextView) findViewById(R.id.common_title_tv_text);
        mTitle.setText(R.string.new_groupbuy_buy_set_mobile);

        mobileNumET = (EditText) findViewById(R.id.modify_payment_password_mobile);

        checkCodeET = (EditText) findViewById(R.id.modify_payment_check_code);

        getBtn = (Button) findViewById(R.id.get_button);
        getBtn.setOnClickListener(this);
        nextBtn = (Button) findViewById(R.id.next_button);
        nextBtn.setOnClickListener(this);

        check_phone_describe = (TextView) findViewById(R.id.check_phone_describe);
        check_phone_describe.setVisibility(View.GONE);

    }

    public boolean connected() {
        boolean conn = NetUtility.isNetworkAvailable(SetGroupBuyMobileActivity.this);
        if (!conn) {
            CommonUtility.showMiddleToast(SetGroupBuyMobileActivity.this, null,
                    getString(R.string.login_non_network));
        }
        return conn;
    }

    public boolean isCheckCode(String strName) {
        String strPattern = "^[A-Za-z\\d\\u4E00-\\u9FA5]+$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strName);
        return m.matches();
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
        case R.id.next_button:
            checkCode = checkCodeET.getText().toString().trim();
            mobileNum = mobileNumET.getText().toString().trim();
            if ("".equals(mobileNum)) {
                toast(getString(R.string.login_input_mobile));
                return;
            } else if (!RegisterActivity.isMobile(mobileNum)) {
                toast(getString(R.string.login_input_right_mobile));
                return;
            }
            if ("".equals(checkCodeET.getText().toString().trim())) {
                toast(getString(R.string.input_check_code_find));
                return;
            } else if (!isCheckCode(checkCodeET.getText().toString())) {
                toast(getString(R.string.input_right_check_code_find));
                return;
            }
            activeAccount();
            break;
        case R.id.get_button:
            mobileNum = mobileNumET.getText().toString().trim();
            if ("".equals(mobileNum)) {
                toast(getString(R.string.login_input_mobile));
                return;
            } else if (!RegisterActivity.isMobile(mobileNum)) {
                toast(getString(R.string.login_input_right_mobile));
                return;
            }
            getMobileCode();
            break;
        case R.id.common_title_btn_back:
            finish();
            break;
        case R.id.common_title_btn_right:
            finish();
            break;
        case R.id.validation_button:
            Intent intent = new Intent(this, ActivateAccountActivity.class);
            startActivityForResult(intent, 200);
            break;
        }

    }

    /**
     * 激活账号
     */
    private void activeAccount() {
        if (!NetUtility.isNetworkAvailable(SetGroupBuyMobileActivity.this)) {
            toast(getString(R.string.net_exception));
            return;
        }

        new AsyncTask<Void, Void, GetActivateCode>() {
            LoadingDialog dialog;

            protected void onPreExecute() {
                if (asyncTask != null) {
                    asyncTask.cancel(true);
                    asyncTask = null;
                }
                dialog = CommonUtility.showLoadingDialog(SetGroupBuyMobileActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            };

            @Override
            protected GetActivateCode doInBackground(Void... params) {
                String request = Register.createActivateGroupMobile(OPERATE_TYPE_SET, mobileNum,checkCodeET.getText().toString(),"");
                String response = null;
                if (!applcation.isSupportedHttps) {
                    try {
                        HttpsUtils.initKey(SetGroupBuyMobileActivity.this.getAssets());
                        String url = Constants.URL_GROUPON_VIRTUAL_SET_MOD_MOBILE.replace("http://", "https://");
                        response = HttpsUtils.post(url, request);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    response = NetUtility.sendHttpRequestByPost(Constants.URL_GROUPON_VIRTUAL_SET_MOD_MOBILE, request);
                }
                if (response == null) {
                    return null;
                }
                return Login.parseJsonMobile(response);

            }

            protected void onPostExecute(GetActivateCode result) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (result == null) {
                    CommonUtility.showMiddleToast(SetGroupBuyMobileActivity.this, null,
                            getString(R.string.net_exception));
                    return;
                }

                if (!TextUtils.isEmpty(result.getFailReason())) {
                    toast(result.getFailReason());
                    return;
                }
                if (JsonInterface.JV_YES.equalsIgnoreCase(result.getIsSuccess())) {
                    Intent intent = new Intent();
                    intent.putExtra("mobileNum", mobileNum);
                    setResult(1,intent);//通知填写订单页修改手机号
                    finish();
                }
            };
        }.execute();
    }

    void toast(String message) {

        CommonUtility.showMiddleToast(SetGroupBuyMobileActivity.this, null, message);
    }

    /**
     * 获取手机校验码
     */
    private AsyncTask<String, Integer, GetActivateCode> asyncTask = null;

    /** * 获取手机校验码 */
    private void getMobileCode() {
        if (!canGetCode) {
            // toast(getString(R.string.cannot_send_message));
            return;
        }

        updateTime();
        if (asyncTask != null) {
            asyncTask.cancel(true);
            asyncTask = null;
        }
        asyncTask = new MobileCodeTask();
        asyncTask.execute();

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

            String request = Login.createJsonMobile(mobileNum, "3");
            String response = NetUtility.sendHttpRequestByPost(Constants.URL_PROFILE_GENERATE_MOBILE_VERIFICATIONCODE,
                    request);

            return Login.parseJsonMobile(response);

        }

        @Override
        protected void onCancelled() {
            cancel(true);
        }

        @Override
        protected void onPostExecute(GetActivateCode result) {
            if (result == null) {
                toast(getString(R.string.data_load_fail_exception));
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

    private void stopTimer() {
        if (timerThread != null) {
            timerThread.interrupt();
            timerThread = null;
        }
        updateView(STOP_TIMER);
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
                updateView((Integer) msg.obj);
            }
        };
    };

    void updateView(int time) {
        if (time > 0) {
            canGetCode = false;
            getBtn.setText(time + getString(R.string.regain_parity_check_code_later));
            getBtn.setBackgroundResource(R.drawable.common_orange_btn_disable);
            check_phone_describe.setVisibility(View.VISIBLE);
        } else if (time == 0) {
            canGetCode = true;
            getBtn.setText(getString(R.string.gegain_check_code));
            getBtn.setBackgroundResource(R.drawable.common_orange_btn);
            check_phone_describe.setVisibility(View.GONE);
        }
    }
}
