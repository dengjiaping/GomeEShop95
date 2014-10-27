package com.gome.ecmall.home.mygome;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gome.ecmall.bean.GetActivateCode;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.GlobalApplication;
import com.gome.ecmall.home.login.ActivateAccountActivity;
import com.gome.ecmall.home.login.Login;
import com.gome.ecmall.home.login.Register;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.HttpsUtils;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class ModifyPaymentPasswordActivity extends Activity implements OnClickListener {// ,OnEditorActionListener
    private String mobileNum;
    private String checkCode;
    private boolean isActivated;

    private Button backBtn;
    private Button closeBtn;
    private Button getBtn;
    private Button nextBtn;
    private Button valBtn;
    private TextView mTitle;

    private EditText mobileNumET;
    private EditText checkCodeET;

    private RelativeLayout notRelayout;
    private LinearLayout checkLinlayout;
    private LoadingDialog mLoadingDialog;

    private boolean canGetCode = true;
    private TextView check_phone_describe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_payment_password);
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

        mobileNumET = (EditText) findViewById(R.id.modify_payment_password_mobile);

        checkCodeET = (EditText) findViewById(R.id.modify_payment_check_code);

        getBtn = (Button) findViewById(R.id.get_button);
        getBtn.setOnClickListener(this);
        nextBtn = (Button) findViewById(R.id.next_button);
        nextBtn.setOnClickListener(this);
        valBtn = (Button) findViewById(R.id.validation_button);
        valBtn.setOnClickListener(this);

        notRelayout = (RelativeLayout) findViewById(R.id.not_check_rl);
        checkLinlayout = (LinearLayout) findViewById(R.id.register_table_layout);
        check_phone_describe = (TextView) findViewById(R.id.check_phone_describe);
        check_phone_describe.setVisibility(View.GONE);

        mobileNum = getIntent().getStringExtra("mobileNum");
        isActivated = getIntent().getBooleanExtra("isActivated", false);
        if (isActivated) {
            mobileNumET.setText(mobileNum.subSequence(0, 3)+"****"+mobileNum.substring(mobileNum.length()-4, mobileNum.length()));
            checkLinlayout.setVisibility(View.VISIBLE);
            notRelayout.setVisibility(View.GONE);
        } else {
            checkLinlayout.setVisibility(View.GONE);
            notRelayout.setVisibility(View.VISIBLE);
            mTitle.setText(R.string.mygome_set_payment_password);
        }

    }

    public boolean connected() {
        boolean conn = NetUtility.isNetworkAvailable(ModifyPaymentPasswordActivity.this);
        if (!conn) {
            CommonUtility.showMiddleToast(ModifyPaymentPasswordActivity.this, null,
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
        if (!NetUtility.isNetworkAvailable(ModifyPaymentPasswordActivity.this)) {
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
                dialog = CommonUtility.showLoadingDialog(ModifyPaymentPasswordActivity.this,
                        getString(R.string.loading), true, new OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            };

            @Override
            protected GetActivateCode doInBackground(Void... params) {
                String request = Register.createActivateMobile(mobileNum, checkCodeET.getText().toString());
                String response = null;
                if (!GlobalApplication.isSupportedHttps) {
                    try {
                        HttpsUtils.initKey(ModifyPaymentPasswordActivity.this.getAssets());
                        String url = Constants.URL_VACCOUNT_VALIDATEVERIFYCODE.replace("http://", "https://");
                        response = HttpsUtils.post(url, request);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    response = NetUtility.sendHttpRequestByPost(Constants.URL_VACCOUNT_VALIDATEVERIFYCODE, request);
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
                    CommonUtility.showMiddleToast(ModifyPaymentPasswordActivity.this, null,
                            getString(R.string.net_exception));
                    return;
                }

                if (!TextUtils.isEmpty(result.getFailReason())) {
                    toast(result.getFailReason());
                    return;
                }
                if (JsonInterface.JV_YES.equalsIgnoreCase(result.getIsSuccess())) {
                    Intent intent = new Intent(ModifyPaymentPasswordActivity.this, ModifyPaymentPassword2Activity.class);
                    startActivityForResult(intent, 100);
                }
            };
        }.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            setResult(2);
            this.finish();
        } else if (resultCode == 103) {
            checkLinlayout.setVisibility(View.GONE);
            notRelayout.setVisibility(View.VISIBLE);
        } else if (resultCode == 104) {
            Intent intent = new Intent();
            intent.setClass(ModifyPaymentPasswordActivity.this, SetPaymentPasswordActivity.class);
            startActivity(intent);
            finish();
        }
        stopTimer();
        checkCodeET.setText("");
    }

    void toast(String message) {

        CommonUtility.showMiddleToast(ModifyPaymentPasswordActivity.this, null, message);
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

            String request = Login.createJsonMobile(mobileNum, "1");
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
