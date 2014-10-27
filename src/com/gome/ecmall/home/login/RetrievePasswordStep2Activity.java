package com.gome.ecmall.home.login;

import java.util.HashMap;
import java.util.Map;
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
import android.widget.TextView;

import com.gome.ecmall.bean.FindPassWordEntity;
import com.gome.ecmall.bean.FindPassWordEntity.FindPasswordStep1;
import com.gome.ecmall.bean.GetActivateCode;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.GlobalApplication;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.HttpsUtils;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class RetrievePasswordStep2Activity extends Activity implements OnClickListener {

    private static final String TAG = "RetrievePasswordStep2Activity";

    private String userName;

    private String phoneNum;

    /** 获取手机激活码的状态值：0 初始状态（获取短信校验码）；1 短信校验码倒计时状态；2 再次获取校验码； */
    private int get_phone_check_code_statu = 0;

    private int seconds = 120;

    /** 标题头部分 */
    Button backBtn;
    Button shutBtn;
    TextView titleText;

    /** 验证手机操作 */

    private LinearLayout checked_phone_layout;
    private TextView check_phone_user_name;
    private TextView check_phone_num;
    private TextView check_phone_describe;
    private EditText input_check_phone_code;
    private Button get_check_code_btn;
    private Button next_step_check_phone_btn;

    private boolean canGetCode = true;

    private String selectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checked_phone);
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        phoneNum = intent.getStringExtra("phoneNum");
        selectId = intent.getStringExtra("selectId");
        initView();
        bindData();
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

        /** 验证手机操作视图初始化 */

        checked_phone_layout = (LinearLayout) findViewById(R.id.checked_phone);
        checked_phone_layout.setVisibility(View.VISIBLE);
        // checked_phone_layout.requestFocus();

        check_phone_user_name = (TextView) findViewById(R.id.checked_phone_user_name);

        check_phone_num = (TextView) findViewById(R.id.checked_phone_num);

        input_check_phone_code = (EditText) findViewById(R.id.check_phone_check_code);

        get_check_code_btn = (Button) findViewById(R.id.get_check_phone_code_btn);
        get_check_code_btn.setOnClickListener(this);

        next_step_check_phone_btn = (Button) findViewById(R.id.next_step_check_phone_btn);
        next_step_check_phone_btn.setOnClickListener(this);
        check_phone_describe = (TextView) findViewById(R.id.check_phone_describe);
        check_phone_describe.setVisibility(View.GONE);
    }

    void bindData() {
        check_phone_user_name.setText(userName);
        check_phone_num.setText(phoneNum.subSequence(0, 3)+"****"+phoneNum.substring(phoneNum.length()-4, phoneNum.length()));
    }

    public boolean isCheckCode(String strName) {
        String strPattern = "^[A-Za-z\\d\\u4E00-\\u9FA5]+$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strName);
        return m.matches();
    }

    @Override
    public void onClick(View v) {
        if (v == next_step_check_phone_btn) {
            if ("".equals(input_check_phone_code.getText().toString().trim())) {
                CommonUtility.showMiddleToast(RetrievePasswordStep2Activity.this, null,
                        getString(R.string.input_check_code_find));
                return;
            } else if (!isCheckCode(input_check_phone_code.getText().toString())) {
                CommonUtility.showMiddleToast(RetrievePasswordStep2Activity.this, null,
                        getString(R.string.input_right_check_code_find));
                return;
            }
            getPhoneCode(input_check_phone_code.getText().toString());
        } else if (v == get_check_code_btn) {
            getMobileCode();
        } else if (v == backBtn) {
            setResult(3);
            this.finish();
        } else if (v == shutBtn) {
            setResult(2);
            this.finish();
        }
    }

    void getPhoneCode(final String verifyCode) {
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
                map.put("mobile", phoneNum);
                map.put("loginName", userName);
                map.put("verifyCode", verifyCode);
                String requestJson = FindPassWordEntity.createRequestJson(map);
                String result = null;
                if (!GlobalApplication.isSupportedHttps) {
                    try {
                        HttpsUtils.initKey(RetrievePasswordStep2Activity.this.getAssets());
                        String url = Constants.URL_PWDRESET_VALIDATEVERIFYCODE.replace("http://", "https://");
                        result = HttpsUtils.post(url, requestJson);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    result = NetUtility.sendHttpRequestByPost(Constants.URL_PWDRESET_VALIDATEVERIFYCODE, requestJson);
                }

                return FindPassWordEntity.parseStep1Json(result);
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
                    CommonUtility.showMiddleToast(RetrievePasswordStep2Activity.this, null,
                            getString(R.string.net_exception));
                    return;
                }
                if (!TextUtils.isEmpty(result.getToken())) {
                    Intent intent = new Intent(RetrievePasswordStep2Activity.this, RetrievePasswordStep3Activity.class);
                    intent.putExtra("token", result.getToken());
                    intent.putExtra("selectId", selectId);
                    intent.putExtra("loginName", userName);
                    startActivityForResult(intent, 100);
                } else {
                    if (!TextUtils.isEmpty(result.getFailReason()))
                        CommonUtility.showMiddleToast(RetrievePasswordStep2Activity.this, null, result.getFailReason());
                }

            }
        }.execute();
    }

    private LoadingDialog getLoadingDialog() {
        LoadingDialog dialog = null;
        String message = getString(R.string.loading);
        if (!NetUtility.isNetworkAvailable(this)) {
            return null;
        }

        dialog = CommonUtility.showLoadingDialog(this, message, true, new OnCancelListener() {

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
        stopTimer();
        input_check_phone_code.setText("");
    }

    /**
     * 获取手机校验码
     */
    private AsyncTask<String, Integer, GetActivateCode> asyncTask = null;

    /** * 获取手机校验码 */
    private void getMobileCode() {
        if (!canGetCode) {
            // CommonUtility.showMiddleToast(RetrievePasswordStep2Activity.this, null,
            // getString(R.string.cannot_send_message));
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

            String request = Login.createJsonMobile(phoneNum, "2");
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
                CommonUtility.showMiddleToast(RetrievePasswordStep2Activity.this, null,
                        getString(R.string.data_load_fail_exception));
                stopTimer();
                return;
            }

            if (JsonInterface.JV_NO.equalsIgnoreCase(result.getIsSuccess())) {
                CommonUtility.showMiddleToast(RetrievePasswordStep2Activity.this, null, result.getFailReason());
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
            get_check_code_btn.setText(time + getString(R.string.regain_parity_check_code_later));
            get_check_code_btn.setBackgroundResource(R.drawable.common_orange_btn_disable);
            check_phone_describe.setVisibility(View.VISIBLE);
        } else if (time == 0) {
            canGetCode = true;
            get_check_code_btn.setText(getString(R.string.gegain_check_code));
            get_check_code_btn.setBackgroundResource(R.drawable.common_orange_btn);
            check_phone_describe.setVisibility(View.GONE);
        }
    }
}
