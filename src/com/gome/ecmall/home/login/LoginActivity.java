package com.gome.ecmall.home.login;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bangcle.safekeyboard.PasswordEditText;
import com.gome.ecmall.bean.AuthenticCode;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.ThirdLogin;
import com.gome.ecmall.cache.ImageLoadTask;
import com.gome.ecmall.cache.ImageLoaderManager;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.GlobalApplication;
import com.gome.ecmall.home.login.HorizontalListView.OnScrollStopListner;
import com.gome.ecmall.util.AppMeasurementUtils;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.DES;
import com.gome.ecmall.util.MobileMD5;
import com.gome.ecmall.util.NetUtility;
import com.gome.ecmall.util.PreferenceUtils;
import com.gome.eshopnew.R;

/**
 * 登录类，包括普通登录，以及第三方登录
 * 
 * @author qinxudong
 */
public class LoginActivity extends Activity implements OnClickListener, OnTouchListener,
        DialogInterface.OnDismissListener, TextWatcher {// ,OnEditorActionListener

    private LoginTask mAuthTask;

    private TextView mTitle;
    private Button mBackBtn;
    private Button mLoginBtn;

    private RelativeLayout  mTabRow4;

    private TextView forget_password_text;

    private String mUsername;
    private EditText mUsernameEdit;
    private String mPassword;
    private PasswordEditText mPasswordEdit;
    private EditText loginCodeEdit;
    private String mCode;
    private ImageView delLoginCode;
    private ImageView imageLoginCode;
    private static CheckBox mAutoLogin;
    private HorizontalListView thirdListView;
    LoadingDialog progressDialog = null;
    private LoadingDialog loadingDialog;
    private boolean isClickFlag = false;
    private static final String THIRD_LOGIN_CODE = "quickLoginCode";
    private static final String THIRD_LOGIN_NAME = "quickLoginName";

    private static final String TAG = "LoginActivity";

    ImageLoaderManager loaderManager;
    private ColorDrawable transparentDrawable;
    SharedPreferences sp = null;
    private LayoutInflater inflater;
    private LinearLayout thirdLayout;
    private ImageView left_arrow_image;
    private ImageView right_arrow_image;
    private LinearLayout third_login_layout;
    private RelativeLayout third_loginRelativeLayout;
    private TableRow check_code_tableRow;
    int x2 = 0;
    int x1 = 0;
    boolean flag = true;
    private GlobalApplication application;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = PreferenceUtils.getInstance(getApplicationContext());
        setContentView(R.layout.login);
        application = (GlobalApplication) getApplication();
        initView();
        init();
        isClickFlag = false;
        loadingDialog = getLoadingDialog();

        if (loadingDialog != null) {
            if (!application.isAlwaysCaptcha) {// 每次登录都要输入验证码
                obtainVerification();
            }
            thirdLoginInit();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        AppMeasurementUtils appMeasurementUtils = new AppMeasurementUtils(LoginActivity.this);
        appMeasurementUtils.getUrl(getString(R.string.appMeas_myGomePage) + ":" + getString(R.string.appMeas_userLogn),
                getString(R.string.appMeas_myGomePage), getString(R.string.appMeas_myGomePage) + ":"
                        + getString(R.string.appMeas_userLogn), getString(R.string.appMeas_user_center), "", "", "",
                "", "", "", "", "", "", "", "", "", null);
    }

    /**
     * 初始化 用户名密码，若有值 给 EditText 赋值 
     */
    private void init() {
        if (PreferenceUtils.isAutoLogin()) {
            mAutoLogin.setChecked(true);
            String encryptUser = PreferenceUtils.getStringValue(GlobalConfig.USER_NAME, "");
            if (TextUtils.isEmpty(encryptUser))
                return;
            String encryptPwd = PreferenceUtils.getStringValue(GlobalConfig.PASSWORD, "");
            try {
                String user = DES.decryptDES(encryptUser, Constants.LOGINDESKEY);
                mUsernameEdit.setText(user);
                if (!TextUtils.isEmpty(encryptPwd)) {
                    String pwd = DES.decryptDES(encryptPwd, Constants.LOGINDESKEY);
                    mPasswordEdit.setText(pwd);
                }
            } catch (Exception e) {
                BDebug.e(TAG, e.getMessage());
            }
        } else {
            String encryptUser = PreferenceUtils.getStringValue(GlobalConfig.USER_NAME, "");
            if (TextUtils.isEmpty(encryptUser))
                return;
            try {
                String user = DES.decryptDES(encryptUser, Constants.LOGINDESKEY);
                mUsernameEdit.setText(user);
            } catch (Exception e) {
                BDebug.e(TAG, e.getMessage());
            }
            mAutoLogin.setChecked(false);
        }
    }

    /** 如果是首次登录，登录完成后设置首次登录为否 */
    public void setFirstLogin() {
        PreferenceUtils.setBooleanValue(GlobalConfig.IS_FIRST_LOGIN, false);
        SharedPreferences.Editor editor = sp.edit();
        if (PreferenceUtils.isFirstLogin()) {
            editor.putBoolean(GlobalConfig.IS_FIRST_LOGIN, false);
        }
        editor.commit();
    }

    /** 设置自动登录Preference */
    public void setAutoLogin(boolean isAutoLogin) {
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(GlobalConfig.IS_AUTO_LOGIN, isAutoLogin);
        edit.commit();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        final LoadingDialog dialog = CommonUtility.showLoadingDialog(this, getString(R.string.login_logining), true,
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (mAuthTask != null) {
                            mAuthTask.cancel(true);
                        }
                    }
                });

        return dialog;
    }

    private void handleLogin() {
        getUserName();
        getUserPwd();
        mCode = loginCodeEdit.getText().toString();
        if ((!NetUtility.isNetworkAvailable(this) || (TextUtils.isEmpty(mUsername)) || (TextUtils.isEmpty(mPassword)))) {
            CommonUtility.showMiddleToast(LoginActivity.this, null, getMessage().toString());
            return;
        }
        if (!application.isAlwaysCaptcha) {// 每次校验验证码
            if (mCode != null && mCode.length() != 4) {
                CommonUtility.showMiddleToast(LoginActivity.this, null, getString(R.string.input_check_code_login));
                return;
            }
        }

        if (mAuthTask != null) {
            mAuthTask.cancel(true);
            mAuthTask = null;
        }
        mAuthTask = new LoginTask(this, mUsername, mPassword, mCode, check_code_tableRow);
        mAuthTask.execute();
    }

    public CharSequence getMessage() {
        if (TextUtils.isEmpty(mUsername)) {
            return getText(R.string.login_non_user);
        }

        if (TextUtils.isEmpty(mPassword)) {
            return getText(R.string.login_non_password);
        }
        // if (TextUtils.isEmpty(mCode)) {
        // return getText(R.string.login_input_code);
        // }
        if (!NetUtility.isNetworkAvailable(this)) {
            return getText(R.string.login_non_network);
        }
        return null;
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.common_title_tv_text);
        mTitle.setText(R.string.login);
        mBackBtn = (Button) findViewById(R.id.common_title_btn_back);
        mBackBtn.setText(R.string.back);
        mBackBtn.setVisibility(View.VISIBLE);
        mBackBtn.setOnClickListener(this);
        mLoginBtn = (Button) findViewById(R.id.common_title_btn_right);
        mLoginBtn.setText(R.string.login);
        mLoginBtn.setBackgroundResource(R.drawable.common_title_btn_bg_selector);
        mLoginBtn.setVisibility(View.VISIBLE);
        mLoginBtn.setOnClickListener(this);
        mUsernameEdit = (EditText) findViewById(R.id.login_username_edit);
        // mUsernameEdit.setOnEditorActionListener(this);
        mPasswordEdit = (PasswordEditText) findViewById(R.id.login_password_edit);
        mPasswordEdit.setOnTouchListener(this);
        loginCodeEdit = (EditText) findViewById(R.id.login_code_edit);
        loginCodeEdit.addTextChangedListener(this);
        imageLoginCode = (ImageView) findViewById(R.id.image_login_code);
        imageLoginCode.setOnClickListener(this);
        delLoginCode = (ImageView) findViewById(R.id.login_code_del_imageView);
        delLoginCode.setOnClickListener(this);
        mTabRow4 = (RelativeLayout) findViewById(R.id.tableRow4);
        mTabRow4.setOnClickListener(this);
        check_code_tableRow = (TableRow) findViewById(R.id.tableRow5);
        if (application.isAlwaysCaptcha) {
            check_code_tableRow.setVisibility(View.GONE);
        } else {
            check_code_tableRow.setVisibility(View.VISIBLE);
        }

        forget_password_text = (TextView) findViewById(R.id.forget_password_text);
        forget_password_text.setOnClickListener(this);

        thirdListView = (HorizontalListView) findViewById(R.id.third_login_listview);
        thirdLayout = (LinearLayout) findViewById(R.id.third_layout);
        loaderManager = ImageLoaderManager.initImageLoaderManager(this);
        transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
        third_login_layout = (LinearLayout) findViewById(R.id.third_layout_title);
        third_loginRelativeLayout = (RelativeLayout) findViewById(R.id.third_layout_content);
        left_arrow_image = (ImageView) findViewById(R.id.left_arrow);
        left_arrow_image.setVisibility(View.INVISIBLE);
        right_arrow_image = (ImageView) findViewById(R.id.right_arrow);
        inflater = LayoutInflater.from(this);

        thirdListView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int action = event.getAction();
                switch (action) {
                case MotionEvent.ACTION_MOVE:
                    if (flag) {
                        x1 = (int) event.getX();

                        flag = false;
                    }
                    x2 = (int) event.getX();

                    if (thirdListView.getScrollX() != 0 && thirdListView.getState() != HorizontalListView.RIGHT_STOP) {
                        left_arrow_image.setVisibility(View.VISIBLE);
                        right_arrow_image.setVisibility(View.VISIBLE);
                    }
                    if ((x2 - x1) < 0 && thirdListView.getState() == HorizontalListView.RIGHT_STOP) {
                        right_arrow_image.setVisibility(View.INVISIBLE);
                    }
                    if (x2 > x1 && thirdListView.getState() == HorizontalListView.RIGHT_STOP) {
                        right_arrow_image.setVisibility(View.VISIBLE);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    thirdListView.startScrollerTask();
                    flag = true;
                    x1 = 0;
                    x2 = 0;
                    break;

                default:
                    break;
                }
                return false;

            }
        });

        thirdListView.setOnScrollStopListner(new OnScrollStopListner() {

            @Override
            public void onScrollToRightEdge() {
                right_arrow_image.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onScrollToMiddle() {
                left_arrow_image.setVisibility(View.VISIBLE);
                right_arrow_image.setVisibility(View.VISIBLE);
            }

            @Override
            public void onScrollToLeftEdge() {
                left_arrow_image.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onScrollStoped() {
                left_arrow_image.setVisibility(View.VISIBLE);
                right_arrow_image.setVisibility(View.VISIBLE);
            }
        });

        mAutoLogin = (CheckBox) findViewById(R.id.login_auto_login_check_box);
        mAutoLogin.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setAutoLogin(true);
                } else {
                    setAutoLogin(false);
                }
            }
        });
    }


    public String getEncryptUserName() {
        String encryptName = null;
        try {
            encryptName = DES.encryptDES(getUserName(), Constants.LOGINDESKEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptName;
    }

    public String getKey() {
        /**
         * 判断网络，当网络已连接，发送请求Key,响应结果结果为空时则返回空，
         */
        if (!NetUtility.isNetworkAvailable(getApplicationContext())) {
            return null;
        } else {
            String result = NetUtility.sendHttpRequestByGet(Constants.URL_SUPPLEMENT_ENCRYPT_KEY);
            if ((result == null) || (result.equals(""))) {
                return null;
            } else {
                JSONObject obj;
                try {
                    obj = new JSONObject(result);
                    String isSuccess = obj.getString(JsonInterface.JK_IS_SUCCESS);
                    if (isSuccess.equalsIgnoreCase("Y")) {
                        String key = obj.getString(JsonInterface.JK_KEY);
                        return key;
                    } else {
                        return null;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    public String getEncryptUserPwd() {
        String encryptPwd = null;
        try {
            encryptPwd = DES.encryptDES(getUserPwd(), Constants.LOGINDESKEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptPwd;
    }

    private String getUserName() {
        mUsername = mUsernameEdit.getText().toString().trim();
        return mUsername;
    }

    private String getUserPwd() {
        mPassword = mPasswordEdit.getString().trim();
        if (TextUtils.isEmpty(mPassword)) {
            mPassword = mPasswordEdit.getText().toString().trim();
        }
        return mPassword;
    }

    public String getUserSign(String user, String pwd, String key) {
        String sign = null;
        // String key = getKey();
        if (key == null || key.equals("")) {
            sign = null;
        } else {
            if (key.length() > 3) {
                key = key.substring(0, 3);
                String str = user + pwd + Constants.PRIVATE_KEY + key;
                sign = MobileMD5.encrypt(str, "utf-8");
            } else {
                sign = null;
            }
        }
        return sign;
    }

    public boolean checkUser() {
        String name = getUserName();
        String pwd = getUserPwd();
        if ((name == null) || (name.equals("")) || (pwd.equals("")) || (pwd == null)) {
            CommonUtility.showAlertDialog(LoginActivity.this, "", getString(R.string.login_non_username_password),
                    getString(R.string.login_confirm), null);
            return false;
        } else {
            return true;
        }
    }

    public void onCancelAuth() {
        if (mAuthTask != null) {
            mAuthTask.cancel(true);
        }
    }

    public boolean isConnected() {
        return NetUtility.isNetworkAvailable(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.image_login_code:
            isClickFlag = true;
            loadingDialog = getLoadingDialog();
            if (loadingDialog != null) {
                obtainVerification();
            }
            break;
//        case R.id.login_auto_login_check_box:
//            mAutoLogin.toggle();
//            break;
        case R.id.tableRow4:
            goRegister();
            break;
        case R.id.login_username_edit:
            mUsernameEdit.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            break;
        case R.id.login_password_edit:
            mPasswordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD
                    | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            break;

        case R.id.common_title_btn_back:
            // 隐藏输入用户名时候弹出的软件盘
            CommonUtility.hideSoftKeyboardNotClear(this, mUsernameEdit);
            // 隐藏输入验证码时候弹出的软键盘
            CommonUtility.hideSoftKeyboardNotClear(this, loginCodeEdit);
            setResult(404);
            finish();
            break;
        case R.id.common_title_btn_right:
            handleLogin();
            break;
        case R.id.login_code_del_imageView:// 删除验证码
            loginCodeEdit.setText("");
            break;
        case R.id.forget_password_text:// 忘记密码
            Intent intent = new Intent(this, RetrievePasswordActivity.class);
            startActivityForResult(intent, 100);
            break;
        default:
            break;
        }

    }

    private LoadingDialog getLoadingDialog() {
        LoadingDialog dialog = null;
        String message = getString(R.string.loading);
        if (!NetUtility.isNetworkAvailable(LoginActivity.this)) {
            return null;
        }

        dialog = CommonUtility.showLoadingDialog(LoginActivity.this, message, true, new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.cancel();
            }
        });
        return dialog;
    }

    private void dismissDialog(LoadingDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void thirdLoginInit() {
        third_login_layout.setVisibility(View.GONE);
        third_loginRelativeLayout.setVisibility(View.GONE);
        new AsyncTask<Void, Void, List<ThirdLogin>>() {

            @Override
            protected List<ThirdLogin> doInBackground(Void... params) {
                String result = NetUtility.sendHttpRequestByGet(Constants.URL_THIRD_LOGIN_LIST);
                if (result == null) {
                    return null;
                }

                List<ThirdLogin> thirdLogins = Login.parseJsonThirdLoginInfo(result);

                return thirdLogins;
            }

            @Override
            protected void onPostExecute(List<ThirdLogin> result) {
                super.onPostExecute(result);
                dismissDialog(loadingDialog);
                if (result.size() > 0) {
                    third_login_layout.setVisibility(View.VISIBLE);
                    third_loginRelativeLayout.setVisibility(View.VISIBLE);
                    for (final ThirdLogin third : result) {
                        View childView = inflater.inflate(R.layout.third_login_item, null);
                        ImageView imageView = (ImageView) childView.findViewById(R.id.third_login_img);
                        childView.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this, ThirdLoginActivity.class);
                                intent.putExtra(THIRD_LOGIN_CODE, third.getLoginCode());
                                intent.putExtra(THIRD_LOGIN_NAME, third.getLoginName());
                                startActivityForResult(intent, RegisterActivity.LOGGED_IN);
                            }
                        });
                        asyncLoadImage(third.getImgPath(), imageView, childView);
                        thirdLayout.addView(childView);
                    }

                }
            }

        }.execute();
    }

    public void asyncLoadImage(final String imgUrl, ImageView imageView, final View view) {
        imageView.setImageResource(R.drawable.third_login_load_icon);
        Bitmap bitmap = loaderManager.getCacheBitmap(imgUrl);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }
        imageView.setTag(imgUrl);
        loaderManager.asyncLoad(new ImageLoadTask(imgUrl) {

            private static final long serialVersionUID = -4002124060243995848L;

            @Override
            protected Bitmap doInBackground() {
                return Login.downloadNetworkBitmap(filePath);
            }

            @Override
            public void onImageLoaded(ImageLoadTask task, Bitmap bitmap) {
                View tagedView = view.findViewWithTag(task.filePath);

                if (tagedView != null) {
                    if (bitmap != null) {
                        BitmapDrawable destDrawable = new BitmapDrawable(inflater.getContext().getResources(), bitmap);
                        TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[] {
                                transparentDrawable, destDrawable });
                        ((ImageView) tagedView).setImageDrawable(transitionDrawable);
                        transitionDrawable.startTransition(300);
                    } else {
                        ((ImageView) tagedView).setImageResource(R.drawable.third_login_load_icon);
                    }
                }
            }

        });
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
                if (!isClickFlag) {

                    isClickFlag = true;
                }
                dismissDialog(loadingDialog);
                if (bm == null) {
                    CommonUtility.showMiddleToast(LoginActivity.this, null, getString(R.string.get_verification_error));
                    return;
                } else {
                    BitmapDrawable bd = new BitmapDrawable(LoginActivity.this.getResources(), bm);
                    imageLoginCode.setBackgroundDrawable(bd);
                }
            }
        }.execute();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
        case R.id.login_username_edit:
            mUsernameEdit.setInputType(InputType.TYPE_CLASS_TEXT);
            mUsernameEdit.onTouchEvent(event);
            return true;
        case R.id.login_password_edit:
            if (mUsernameEdit.requestFocus()) {
                mUsernameEdit.clearFocus();
            }
            if (loginCodeEdit.requestFocus()) {
                loginCodeEdit.clearFocus();
            }
            mPasswordEdit.requestFocus();
            mPasswordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
            mPasswordEdit.onTouchEvent(event);

            return true;
        default:
            break;
        }
        return false;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        onCancelAuth();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(loginCodeEdit.getText())) {
            delLoginCode.setVisibility(View.GONE);
        } else {
            delLoginCode.setVisibility(View.VISIBLE);
        }
    }

    /** 跳转注册 */
    public void goRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(intent, RegisterActivity.NOT_LOGGED_IN);
    }

    /** 已登录，去激活 */
    public void activate(String activate) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.putExtra(JsonInterface.JK_IS_ACTIVATED, activate);
        startActivityForResult(intent, RegisterActivity.LOGGED_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RegisterActivity.LOGGED_IN) {// 已登录
            dispatcClass();
        }

        if (resultCode == RegisterActivity.NOT_LOGGED_IN) {// 未登录

        }

        if (resultCode == RegisterActivity.NONACTIVATED) {
            dispatcClass();
        }

        if (resultCode == RegisterActivity.ACTIVATED) {
            dispatcClass();
        }

        if (resultCode == RegisterActivity.THIRD_LOGGED_IN) {
            // final String sessionId = data.getStringExtra("sessionId");
            // final String uid = data.getStringExtra("uid");
            // final String uname = data.getStringExtra("uname");
            // String userPrefLanguage = data.getStringExtra("userPrefLanguage");
            // String BIGipServerpool_atgmobile = data.getStringExtra("BIGipServerpool_atgmobile");
            // GobalConfig.getInstance().setjSessionId(sessionId);
            // StringBuffer jessionId = new StringBuffer();
            // jessionId.append("JSESSIONID=").append(sessionId).append(";").append("userPrefLanguage=")
            // .append(userPrefLanguage).append(";").append("BIGipServerpool_atgmobile=")
            // .append(BIGipServerpool_atgmobile).append(";");
            // GobalConfig.getInstance().setCookieInfo(jessionId.toString());
            // GobalConfig.isLogin = true;
            dispatcClass();

        }

    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        setResult(404);
        return super.onKeyDown(keyCode, event);
    }

    private void dispatcClass() {
        String targetClassName = getIntent().getStringExtra(GlobalConfig.CLASS_NAME);
        Intent intent = new Intent();
        intent.putExtra(GlobalConfig.CLASS_NAME, targetClassName);
        setResult(1, intent);
        finish();
    }

}
