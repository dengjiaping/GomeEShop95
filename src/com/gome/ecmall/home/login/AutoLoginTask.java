package com.gome.ecmall.home.login;

import android.content.Context;
import android.os.AsyncTask;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.UserInfo;
import com.gome.ecmall.home.GlobalApplication;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.HttpsUtils;
import com.gome.ecmall.util.NetUtility;
import com.gome.eshopnew.R;

public class AutoLoginTask extends AsyncTask<Void, Void, UserInfo> {
    private Context mContext;
    // private LoadingDialog mDialog;
    private AutoLoginListener autoLoginListener;

    public AutoLoginListener getAutoLoginListener() {
        return autoLoginListener;
    }

    public void setAutoLoginListener(AutoLoginListener autoLoginListener) {
        this.autoLoginListener = autoLoginListener;
    }

    /**
     * @author qinxudong 自动登录监听接口
     */
    public interface AutoLoginListener {
        public void callBack(String state);
    }

    public AutoLoginTask(Context ctx) {
        mContext = ctx;
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
        String autoLoginJson = null;
        String result = null;

        autoLoginJson = LoginManager.createAutoLoginJson(mContext);
        if (!GlobalApplication.isSupportedHttps) {
            try {
                HttpsUtils.initKey(mContext.getAssets());
                String url = Constants.URL_PROFILE_USER_LOGIN.replace("http://", "https://");
                result = HttpsUtils.post(url, autoLoginJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            result = NetUtility.sendHttpRequestByPost(Constants.URL_PROFILE_USER_LOGIN, autoLoginJson);
        }
        if (result == null) {
            return null;
        }
        return Login.parseJson(result);
    }

    @Override
    protected void onPostExecute(UserInfo result) {
        if (result == null) {
            if (autoLoginListener != null)
                autoLoginListener.callBack(JsonInterface.JV_NO);
            return;
        }
        UserInfo userInfo = result;
        String isSuccess = userInfo.getIsSuccess();
        String fail = "";
        if (isSuccess.equalsIgnoreCase(JsonInterface.JV_YES)) {
            GlobalConfig.isLogin = true;
            if (autoLoginListener != null)
                autoLoginListener.callBack(JsonInterface.JV_YES);
        } else if (isSuccess.equalsIgnoreCase(JsonInterface.JV_NO)) {
            fail = userInfo.getFailReason();
            if (fail.equalsIgnoreCase(mContext.getString(R.string.login_user_is_logged))) {
                GlobalConfig.isLogin = true;
                if (autoLoginListener != null)
                    autoLoginListener.callBack(JsonInterface.JV_YES);
            } else {
                GlobalConfig.isLogin = false;
                if (autoLoginListener != null)
                    autoLoginListener.callBack(JsonInterface.JV_NO);
            }
        }

    }

}
