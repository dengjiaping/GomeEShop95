package com.gome.ecmall.home.login;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.UserProfile;
import com.gome.ecmall.custom.LoadingDialog;
import com.gome.ecmall.util.AppMeasurementUtils;
import com.gome.ecmall.util.BDebug;
import com.gome.ecmall.util.CommonUtility;
import com.gome.ecmall.util.Constants;
import com.gome.ecmall.util.NetUtility;
import com.gome.ecmall.util.PreferenceUtils;
import com.gome.eshopnew.R;

/**
 * @author qinxudong 第三方登录webview页面
 */
public class ThirdLoginActivity extends Activity {
    private WebView webView;
    private static final String THIRD_LOGIN_CODE = "quickLoginCode";
    private static final String THIRD_LOGIN_NAME = "quickLoginName";
    private static final String BASE_URL = "codeUrl";
    private static final String TAG = "ThirdLoginActivity";
    private String thirdCode;
    private String thirdName;
    private TextView progressTextView;
    private CookieManager cm;
    private Button backButton;
    private TextView titileTextView;
    private String uId;
    private String uname;
    private String bIGipServerpool_atgmobile;
    private String sessionId;
    private String language;
    private String loadurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.third_login);
        Intent intent = getIntent();
        thirdCode = intent.getStringExtra(THIRD_LOGIN_CODE);
        thirdName = intent.getStringExtra(THIRD_LOGIN_NAME);
        getUrl();
        init();
        initWebView();
    }

    private void init() {
        webView = (WebView) findViewById(R.id.third_login_webview);
        progressTextView = (TextView) findViewById(R.id.text_progress);
        backButton = (Button) findViewById(R.id.common_title_btn_back);
        backButton.setVisibility(View.VISIBLE);
        backButton.setText(R.string.back);
        backButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ThirdLoginActivity.this.finish();
            }
        });
        titileTextView = (TextView) findViewById(R.id.common_title_tv_text);
        titileTextView.setText("授权");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getUrl() {
        new AsyncTask<Void, Void, String>() {
            LoadingDialog dialog = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (!NetUtility.isNetworkAvailable(ThirdLoginActivity.this)) {
                    return;
                }

                dialog = CommonUtility.showLoadingDialog(ThirdLoginActivity.this, getString(R.string.loading), true,
                        new OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                dialog.cancel();
                            }
                        });
            }

            @Override
            protected String doInBackground(Void... params) {
                JSONObject object = new JSONObject();
                try {
                    if (thirdCode == null && thirdCode.length() == 0) {
                        return "error";
                    }
                    object.put(THIRD_LOGIN_CODE, thirdCode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String json = object.toString();
                String result = NetUtility.sendHttpRequestByPost(Constants.URL_THIRD_LOGIN_URL, json);
                try {
                    object = new JSONObject(result);
                    if (object.getString("isSuccess").equals("Y")) {
                        result = object.getString(BASE_URL);
                    } else {
                        result = "error";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    result = "exception";
                }

                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (result == null || result.equals("") || result.equals("error") || result.equals("exception")) {
                    CommonUtility.showMiddleToast(ThirdLoginActivity.this, null,
                            getString(R.string.data_load_fail_exception));
                    return;
                }

                loadurl = result;
                webView.requestFocus();
                webView.loadUrl(loadurl);
            }

        }.execute();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        if (thirdCode.equals("alipay")) {
            webView.setInitialScale(80);
        }
        if (thirdCode.equals("qihoo") || thirdCode.equals("netease")) {
            webView.getSettings().setDefaultZoom(ZoomDensity.FAR);
        }
        webView.setWebViewClient(new WebViewC());
        cm = CookieManager.getInstance();
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressTextView.setText(newProgress + "%");
                if (newProgress == 100) {
                    progressTextView.setVisibility(View.GONE);
                }
            }

        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    class WebViewC extends WebViewClient {

        /**
         * 由于腾讯授权页面采用https协议 执行此方法接受所有证书
         */
        @SuppressLint("NewApi")
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            // BDebug.e(TAG, "############################" + url);
            // if (url.startsWith("http://mobile.gome.com.cn")) {
            // url = url.replace("mobile.gome.com.cn", "10.57.4.13:8480");
            // view.loadUrl(url);
            // return;
            // }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // super.onReceivedError(view, errorCode, description, failingUrl);
            // if (errorCode == 404) {
            // failingUrl = failingUrl.replace("mobile.gome.com.cn", "10.57.4.13:8480");
            // view.loadUrl(failingUrl);
            // }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            String cookie = cm.getCookie(url);
            // BDebug.e("cookie", cookie + "@@@@@@@");
            if (cookie != null && cookie.contains("isSuccess=Y") && cookie.contains("BIGipServerpool_atgmobile")) {
                Map<String, String> cookieMap = getCookieMap(cookie);
                uId = cookieMap.get("uid");
                uname = cookieMap.get("uname");
                bIGipServerpool_atgmobile = cookieMap.get("BIGipServerpool_atgmobile");
                language = cookieMap.get("userPrefLanguage");
                try {
                    uname = URLDecoder.decode(uname, "utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sessionId = cookieMap.get("sessionId");
                // BDebug.e(TAG, sessionId + "********");
                if (sessionId == null || sessionId.length() == 0 || uId == null || uId.length() == 0) {
                    CommonUtility.showToast(ThirdLoginActivity.this, getString(R.string.data_load_fail_exception)
                            + sessionId);
                    cm.removeAllCookie();
                    cm.removeSessionCookie();
                    view.loadUrl(loadurl);
                    return;
                }

                GlobalConfig.getInstance().setjSessionId(sessionId);
                StringBuffer jessionId = new StringBuffer();
                jessionId.append("JSESSIONID=").append(sessionId).append(";").append("userPrefLanguage=")
                        .append(language).append(";").append("BIGipServerpool_atgmobile=")
                        .append(bIGipServerpool_atgmobile).append(";");
                GlobalConfig.getInstance().setCookieInfo(jessionId.toString());
                GlobalConfig.isLogin = true;
                loadUserInfo();

            } else if (cookie != null) {
                if (cookie.contains("isSuccess")
                        && (cookie.contains("isSuccess=N") || !cookie.contains("BIGipServerpool_atgmobile"))) {
                    CommonUtility.showToast(ThirdLoginActivity.this, "授权失败，请重试！");
                    cm.removeAllCookie();
                    cm.removeSessionCookie();
                    view.loadUrl(loadurl);
                }
            }
        }
    }

    private Map<String, String> getCookieMap(String cookie) {
        Map<String, String> cookieMap = new HashMap<String, String>();
        cookie = cookie.replace(" ", "");
        String[] cookies = cookie.split(";");
        if (cookies.length > 0) {
            for (String ss : cookies) {
                String[] cook = ss.split("=");
                if (cook.length == 2) {
                    cookieMap.put(cook[0], cook[1]);
                }
            }
        }
        return cookieMap;
    }

    /**
     * 加载用户信息
     */
    private void loadUserInfo() {
        new AsyncTask<Object, Void, UserProfile>() {
            LoadingDialog loadingDialog = null;

            protected void onPreExecute() {
                loadingDialog = LoadingDialog.show(ThirdLoginActivity.this, getString(R.string.loading), true,
                        new DialogInterface.OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                cancel(true);
                            }
                        });
            };

            protected UserProfile doInBackground(Object... params) {
                // 从服务器端获取用户信息
                String response = NetUtility.sendHttpRequestByGet(Constants.URL_PROFILE_USERINFO);
                BDebug.e("我的国美-用户信息", response);
                return UserProfile.parseUserProfile(response);
            };

            protected void onPostExecute(UserProfile result) {
                if (isCancelled()) {
                    return;
                }
                if (ThirdLoginActivity.this != null && !ThirdLoginActivity.this.isFinishing() && loadingDialog != null
                        && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                if (result == null) {
                    CommonUtility.showMiddleToast(ThirdLoginActivity.this, "",
                            getString(R.string.data_load_fail_exception));
                    webView.loadUrl(loadurl);
                    return;
                }
                AppMeasurementUtils appMeasurementUtils = new AppMeasurementUtils(ThirdLoginActivity.this);
                appMeasurementUtils.setUserName(result.getLoginName());
                appMeasurementUtils.setMobile(result.getMobile());
                appMeasurementUtils.setUserId(result.getProfileId());
                appMeasurementUtils.setLoginType(ThirdLoginActivity.this.getString(R.string.appMeas_login_third)
                        + result.getLoginName().split("-")[0]);
                appMeasurementUtils.getUrl(getString(R.string.appMeas_intalPage), "", "", "", "", "",
                        AppMeasurementUtils.EVENT_LOGIN_SUCCESS, "", "", "", "", "", "", "", "", "", null);
                LoginManager.setFirstLogin(ThirdLoginActivity.this);
                PreferenceUtils.getInstance(getApplicationContext());
//                String loginName = null;
//                try {
//                    loginName = DES.encryptDES(result.getLoginName(), Constants.LOGINDESKEY);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                PreferenceUtils.setStringValue(
                        new String[] { JsonInterface.JK_PROFILE_ID},
                        new String[] { result.getProfileId()});
                // 第三方登录 下次登录的时候 回显用户名
                LoginManager.setAutoLogin(ThirdLoginActivity.this, false);
                Intent intent = new Intent();
                intent.putExtra("uid", uId);
                BDebug.e(TAG, uId);
                setResult(RegisterActivity.THIRD_LOGGED_IN, intent);
                finish();

            };
        }.execute();
    }
}
