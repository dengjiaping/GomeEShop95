package com.gome.ecmall.home.login;

import android.content.Context;
import android.os.AsyncTask;

import com.gome.ecmall.bean.AlipayUserInfo;
import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.UserInfo;
import com.gome.ecmall.home.GlobalApplication;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.HttpsUtils;
import com.gome.ecmall.util.NetUtility;
import com.gome.ecmall.util.PreferenceUtils;
import com.gome.eshopnew.R;

public class AlipayAutoLoginTask extends AsyncTask<Void, Void, UserInfo> {
    private Context mContext;
    private AlipayUserInfo alipayUserInfo;
    private OnFinish onFinish;

    public AlipayAutoLoginTask(Context ctx) {
        this.mContext = ctx;
        alipayUserInfo = AlipayUserInfo.getInstance();
    }

    @Override
    protected void onPreExecute() {
        if (!NetUtility.isNetworkAvailable(mContext)) {
            CommonUtility.showMiddleToast(mContext, null, mContext.getString(R.string.login_non_network));
            return;
        }
    }

    @Override
    protected UserInfo doInBackground(Void... params) {
        String result = null;

        String autoLoginJson = LoginManager.createAlipayAutoLoginJson(alipayUserInfo.getAlipayUserId(),
                alipayUserInfo.getAlipayAuthCode());
        if (!GlobalApplication.isSupportedHttps) {
            try {
                HttpsUtils.initKey(mContext.getAssets());
                String url = Constants.URL_ALIPAY_WALLET_QUICK_LOGIN.replace("http://", "https://");
                result = HttpsUtils.post(url, autoLoginJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            result = NetUtility.sendHttpRequestByPost(Constants.URL_ALIPAY_WALLET_QUICK_LOGIN, autoLoginJson);
        }

        if (result == null) {
            return null;
        }
        
        return Login.parseJson(result);
    }

    @Override
    protected void onPostExecute(UserInfo result) {
        super.onPostExecute(result);
        if (onFinish != null)
            onFinish.callBack(result);
        if (result == null)
            return;
        UserInfo userInfo = result;
        String isSuccess = userInfo.getIsSuccess();
        String fail = "";
        if (isSuccess.equalsIgnoreCase(JsonInterface.JV_YES)) {
            GlobalConfig.isLogin = true;
        } else if (isSuccess.equalsIgnoreCase(JsonInterface.JV_NO)) {
            fail = userInfo.getFailReason();
            if (fail.equalsIgnoreCase(mContext.getString(R.string.login_user_is_logged))) {
                GlobalConfig.isLogin = true;
            } else {
                GlobalConfig.isLogin = false;
                if (alipayUserInfo.isAlipayLogin()) {
                    // 支付宝钱包用户自动登录失败时，关闭支付宝钱包登录开关，
                    // 允许用户自己登录，以及选择支付方式，并且提示用户支付宝账户登录失败
                    alipayUserInfo.setAlipayLogin(false);
                }
            }
        }
        PreferenceUtils.getInstance(mContext.getApplicationContext());
        if (!PreferenceUtils.isAutoLogin() && GlobalConfig.isLogin) {// 非自动登录账户，保存用户信息
            PreferenceUtils.setStringValue(new String[] { JsonInterface.JK_PROFILE_ID },
                    new String[] { userInfo.getProfileID() });
            LoginManager.setAutoLogin(mContext, false);
        }

        BDebug.e("GlobalConfig", GlobalConfig.isLogin + "*****");

    }

    public OnFinish getOnFinish() {
        return onFinish;
    }

    public void setOnFinish(OnFinish onFinish) {
        this.onFinish = onFinish;
    }

    /**
     * 支付宝钱包用户登录回调
     * 
     * @author qinxudong
     * 
     */
    public interface OnFinish {
        public void callBack(UserInfo result);
    }
}
