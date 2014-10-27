package com.gome.ecmall.home.login;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TableRow;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.UserInfo;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.home.GlobalApplication;
import com.gome.ecmall.home.ShoppingCartActivity;
import com.gome.ecmall.util.AppMeasurementUtils;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.HttpsUtils;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class LoginTask extends AsyncTask<Void, Void, UserInfo> {

    private static final String TAG = "LoginTask";

    private Context mContext;
    private String mUserName;
    private String mPassword;
    private String mCode;
    private LoadingDialog mDialog;
    private TableRow check_code_tableRow;
    public LoginTask(Context ctx, String user, String pwd, String mCode, TableRow check_code_tabRow) {
        mContext = ctx;
        this.mUserName = user;
        this.mPassword = pwd;
        this.mCode = mCode;
        this.check_code_tableRow = check_code_tabRow;
    }

    @Override
    protected void onPreExecute() {
        if (!NetUtility.isNetworkAvailable(mContext)) {
            CommonUtility.showMiddleToast(mContext, null, mContext.getString(R.string.login_non_network));
            cancel(true);
            return;
        }

        mDialog = CommonUtility.showLoadingDialog(mContext, mContext.getString(R.string.login_logining), true,
                new OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        cancel(true);
                    }
                });
    }

    @Override
    protected UserInfo doInBackground(Void... params) {
        String loginJson = LoginManager.creatLoginJson(mUserName, mPassword, mCode);
        String result = null;
        if (!GlobalApplication.isSupportedHttps) {
            try {
                HttpsUtils.initKey(mContext.getAssets());
                String url = Constants.URL_PROFILE_USER_LOGIN.replace("http://", "https://");
                result = HttpsUtils.post(url, loginJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // "http://mobile.gome.com.cn/mobile/profile/userLogin.jsp"

            result = NetUtility.sendHttpRequestByPost(Constants.URL_PROFILE_USER_LOGIN, loginJson);
        }

        if (result == null)
            return null;
        return Login.parseJson(result);

        // UserInfo info = new UserInfo();
        // info.setIsSuccess("Y");
        // info.setIsActivated("N");
        // return info;

    }

    @Override
    protected void onPostExecute(UserInfo result) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        if (isCancelled()) {
            cancel(true);
        }
        if (result == null) {
            CommonUtility.showMiddleToast(mContext, null, mContext.getString(R.string.net_exception));
            return;
        }
        UserInfo userInfo = result;
        String isSuccess = userInfo.getIsSuccess();
        String fail = "";
        if (isSuccess.equalsIgnoreCase(JsonInterface.JV_YES)) {
            GlobalConfig.isLogin = true;
            GlobalConfig.isFirstShow = true;
            // applcation.isAlwaysCaptcha = true;
        } else if (isSuccess.equalsIgnoreCase(JsonInterface.JV_NO)) {
            fail = userInfo.getFailReason();
            if (fail.equalsIgnoreCase(mContext.getString(R.string.login_user_is_logged))) {
                GlobalConfig.isLogin = true;
                GlobalConfig.isFirstShow = true;
            } else {
                if (userInfo.isNeedCaptcha()) {
                    check_code_tableRow.setVisibility(View.VISIBLE);
                    // applcation.isAlwaysCaptcha = false;
                    ((LoginActivity) mContext).obtainVerification();
                }
                GlobalConfig.isLogin = false;
                GlobalConfig.isFirstShow = false;
            }
        }

        if (GlobalConfig.isLogin) {
            AppMeasurementUtils appMeasurementUtils = new AppMeasurementUtils(mContext);
            appMeasurementUtils.setUserName(userInfo.getLoginName());
            appMeasurementUtils.setMobile(userInfo.getMobile());
            appMeasurementUtils.setUserId(userInfo.getProfileID());
            appMeasurementUtils.setLoginType(mContext.getString(R.string.appMeas_login_normal));
            appMeasurementUtils.getUrl(
                    mContext.getString(R.string.appMeas_myGomePage) + ":"
                            + mContext.getString(R.string.appMeas_loginSuccess),
                    mContext.getString(R.string.appMeas_myGomePage), mContext.getString(R.string.appMeas_myGomePage)
                            + ":" + mContext.getString(R.string.appMeas_loginSuccess),
                    mContext.getString(R.string.appMeas_user_center), "", "", AppMeasurementUtils.EVENT_LOGIN_SUCCESS,
                    "", "", "", "", "", "", "", "", "", null);
            ShoppingCartActivity.getTotalShoppingNumber();
            LoginManager.setFirstLogin(mContext);
            LoginManager.saveUser(mContext, userInfo);
            LoginManager.saveUser(mContext, mUserName, mPassword);

            if (JsonInterface.JV_NO.equalsIgnoreCase(result.getIsActivated())) {// 未激活
                goActivate(result.getMobile());
                // goActivate(null);
            } else if (JsonInterface.JV_YES.equalsIgnoreCase(result.getIsActivated())) {
                dispatchClass();
            }
        } else {
            CommonUtility.showMiddleToast(mContext, null, fail);
        }

    }

    /** 返回上一模块 */
    private void dispatchClass() {
        String targetClassName = ((Activity) mContext).getIntent().getStringExtra(GlobalConfig.CLASS_NAME);
        Intent intent = new Intent();
        intent.putExtra(GlobalConfig.CLASS_NAME, targetClassName);
        ((Activity) mContext).setResult(1, intent);
        ((Activity) mContext).finish();
    }

    /** 跳转用户激活界面 */
    private void goActivate(String mobile) {
        Intent intent = new Intent(mContext, ActivateAccountActivity.class);
        intent.putExtra(JsonInterface.JK_MOBILE, mobile);
        ((LoginActivity) mContext).startActivityForResult(intent, 0);
    }

}
