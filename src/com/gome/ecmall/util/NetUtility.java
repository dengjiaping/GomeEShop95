package com.gome.ecmall.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.gome.ecmall.bean.GlobalConfig;

/**
 * 网络相关--工具类
 */
public class NetUtility {

    public static final String TAG = "NetUtility";
    public static final String NO_CONN = "FAIL";

    private static final int REQUEST_TIMEOUT = 30 * 1000;// 设置请求超时30秒
    private static final int SO_TIMEOUT = 30 * 1000; // 设置等待数据超时时间30秒钟
    public static GlobalConfig config = GlobalConfig.getInstance();
    private static Context mContext;

    public static boolean isNetworkAvailable(Context context) {
        mContext = context;
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    public static String sendHttpRequestByGet(String url) {
//        BDebug.w(TAG, "Send Get Request URL =" + url);
//        String result = NO_CONN;
//        try {
//            HttpGet httpGet = new HttpGet(url);
//            if (config.getCookieInfo() != null) {
//                httpGet.setHeader("Cookie", config.getCookieInfo());
//            }
//            httpGet.setHeader("User-Agent", "android GomeShopApp " + VersonUpdateUtils.USERUPDATEVERSONCODE + ";");
//            BasicHttpParams httpParams = new BasicHttpParams();
//            HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
//            HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
//            DefaultHttpClient defaultHttpClient = new DefaultHttpClient(httpParams);
//            HttpResponse httpResponse = defaultHttpClient.execute(httpGet);
//            int responseCode = httpResponse.getStatusLine().getStatusCode();
//            BDebug.d(TAG, "Send Get Request responseCode = " + responseCode);
//            List<Cookie> cookies = defaultHttpClient.getCookieStore().getCookies();
//            StringBuffer cookiesInfo = new StringBuffer();
//            boolean needUpdate = false;
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals(GobalConfig.JSESSIONID)) {
//                    String value = cookie.getValue();
//                    if (!value.equals(config.getjSessionId())) {
//                        config.setjSessionId(value);
//                        needUpdate = true;
//                    }
//                }
//                cookiesInfo.append(cookie.getName() + "=" + cookie.getValue()).append(";");
//            }
//            if (needUpdate) {
//                config.setCookieInfo(cookiesInfo.toString());
//            }
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                result = EntityUtils.toString(httpResponse.getEntity()).trim();
//                // 如果已登录则需要重新登录
//                JsonResult jsonresult = new JsonResult(result);
//                if ("Y".equalsIgnoreCase(jsonresult.getIsSessionExpired())) {
//                    if (GobalConfig.isLogin) {
//                        GobalConfig.isLogin = false;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        BDebug.d(TAG, "Send Get Request Response Entity= " + result);
//        return result;
    	return HttpUtils.sendHttpRequestByGet(url);
    }

    public static String sendHttpRequestByPost(final String url, final String json) {
//        BDebug.w(TAG, "Send Post Request URL = " + url);
//        BDebug.w(TAG, "Send Post Request JSON = " + json);
//        String result = NO_CONN;
//        HttpPost httpPost = new HttpPost(url);
//        if (config.getCookieInfo() != null) {
//            httpPost.setHeader("Cookie", config.getCookieInfo());
//        }
//        httpPost.setHeader("User-Agent", "android GomeShopApp " + VersonUpdateUtils.USERUPDATEVERSONCODE + ";");
//        // 传参服务端获取的方法为request.getParameter("name")
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("body", json));
//        try {
//            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
//            BasicHttpParams httpParams = new BasicHttpParams();
//            HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
//            HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
//            DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
//            HttpResponse httpResponse = httpClient.execute(httpPost);
//            int responseCode = httpResponse.getStatusLine().getStatusCode();
//            BDebug.d(TAG, "Send Post Request responseCode = " + responseCode + "\n httpResponse=" + httpResponse);
//            boolean needUpdate = false;
//            List<Cookie> cookies = httpClient.getCookieStore().getCookies();
//            StringBuffer cookiesInfo = new StringBuffer();
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals(GobalConfig.JSESSIONID)) {
//                    String value = cookie.getValue();
//                    if (!value.equals(config.getjSessionId())) {
//                        // sessionID失效，如果已登录则需重新登录
//                        // 设置新的jsessionId
//                        config.setjSessionId(value);
//                        needUpdate = true;
//                    }
//                }
//                cookiesInfo.append(cookie.getName() + "=" + cookie.getValue()).append(";");
//            }
//            if (needUpdate) {
//                // 如果已登录则需要重新登录
//                // 更新cookie信息
//                config.setCookieInfo(cookiesInfo.toString());
//            }
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                result = EntityUtils.toString(httpResponse.getEntity()).trim();
//                // 如果已登录则需要重新登录
//                JsonResult jsonresult = new JsonResult(result);
//                if ("Y".equalsIgnoreCase(jsonresult.getIsSessionExpired())) {
//                    if (GobalConfig.isLogin) {
//                        String loginresult = NetUtility.sendHttpRequestByGet(Constants.URL_PROFILE_USER_LOGINOUT);
//                        GobalConfig.isLogin = false;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            BDebug.d(TAG, "Send Post Request Response Entity = :" + result);
//        }
//        return result;
    	return HttpUtils.sendHttpRequestByPost(url, json);
    }

    /**
     * 从网络中获取 图片字节码
     * @param url 图片Url
     * @return
     */
    public static byte[] downloadImageFromNetwork(String url) {
        InputStream is = null;
        byte[] data = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
            HttpResponse httpResponse = new DefaultHttpClient(httpParams).execute(httpGet);
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                is = httpResponse.getEntity().getContent();
                data = transStreamToBytes(is, 4096);
                BDebug.i(TAG, "downloadImageFromNetwork()  rspCode:" + responseCode + "  URL:" + url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 从网络中获取 图片字节码
     * @param url 图片url
     * @return 
     */
    public static Bitmap downloadNetworkBitmap(String url) {
        byte[] data = downloadImageFromNetwork(url);
        if (data == null || data.length == 0) {
            return null;
        }
        // Drawable drawable = new
        // BitmapDrawable(BitmapFactory.decodeByteArray(data, 0, data.length));
        //
        // return getRoundCornerDrawable(drawable, 10);

        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    /**
     * 将图片转化为圆角图片
     * 
     * @param drawable
     * @param roundPX
     * @return
     */
    private static Bitmap getRoundCornerDrawable(Drawable drawable, float roundPX /* 圆角的半径 */) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(w, h, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap retBmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas can = new Canvas(retBmp);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);

        paint.setColor(color);
        paint.setAntiAlias(true);
        can.drawARGB(0, 0, 0, 0);
        can.drawRoundRect(rectF, roundPX, roundPX, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        can.drawBitmap(bitmap, rect, rect, paint);
        // return new BitmapDrawable(retBmp);
        return retBmp;
    }

    /**
     * 将输入流转化为字节数组
     * @param is
     * @param buffSize
     * @return
     */
    public static byte[] transStreamToBytes(InputStream is, int buffSize) {
        if (is == null) {
            return null;
        }
        if (buffSize <= 0) {
            throw new IllegalArgumentException("buffSize can not less than zero.....");
        }
        byte[] data = null;
        byte[] buffer = new byte[buffSize];
        int actualSize = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            while ((actualSize = is.read(buffer)) != -1) {
                baos.write(buffer, 0, actualSize);
            }
            data = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**
     * 判断图片是否能被加载到本地
     * @param imgUrl 图片url
     * @param localPath 本地路径
     * @return
     */
    public static boolean downloadImageToLocalFile(String imgUrl, String localPath) {
        InputStream is = null;
        boolean isLoaded = false;
        FileOutputStream fos = null;
        try {
            HttpGet httpGet = new HttpGet(imgUrl);
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
            HttpResponse httpResponse = new DefaultHttpClient(httpParams).execute(httpGet);
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                is = httpResponse.getEntity().getContent();
                File file = new File(localPath);
                if (file.exists()) {
                    file.delete();
                }
                fos = new FileOutputStream(localPath);
                byte[] buffer = new byte[4096];
                int actualSize = -1;
                while ((actualSize = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, actualSize);
                }
                isLoaded = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isLoaded;
    }

    /**
     * 保存bitmap到本地
     * 
     * @param bitmap
     * @param localPath
     * @return
     */
    public static boolean bitmapToLocalFile(Bitmap bitmap, String localPath) {
        BDebug.e("bitmap", "" + bitmap);
        BDebug.e("localPath", localPath);
        boolean isLoaded = false;
        FileOutputStream fos = null;
        File f = new File(localPath);
        try {
            f.createNewFile();
            fos = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            isLoaded = true;
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isLoaded;
    }

    /**
     * 读取本地图片
     * 
     * @param localPath
     * @return
     */
    public static Bitmap getBitMap(String localPath) {

        BDebug.e("localPath", localPath);
        Bitmap bitmap = null;
        try {
            File file = new File(localPath);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(localPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    // MODIFY zhouxm 自动登录取消，本方法暂时不使用 注释掉
    // private static boolean loginTimeoutAutonLogin() {
    // boolean isLoginSuccess = false;
    // try {
    // String nameStr = PreferenceUtils.getStringValue(GobalConfig.USER_NAME,
    // "");
    // String pwdStr = PreferenceUtils.getStringValue(GobalConfig.PASSWORD, "");
    // String userName = DES.decryptDES(nameStr, Constants.LOGINDESKEY);
    // String pwd = DES.decryptDES(pwdStr, Constants.LOGINDESKEY);
    // String encryptUser = DES.encryptDES(userName, Constants.LOGINDESKEY);
    // String encryptPwd = DES.encryptDES(pwd, Constants.LOGINDESKEY);
    // String sign = getSign(userName, pwd, getKey());
    // String loginJson = Login.createRequestLogin(encryptUser, encryptPwd,
    // sign);
    // String logonresult =
    // NetUtility.sendHttpRequestByPost(Constants.URL_PROFILE_USER_LOGIN,
    // loginJson);
    // UserInfo userInfo = Login.parseJson(logonresult);
    // if ("Y".equalsIgnoreCase(userInfo.getIsSuccess())) {
    // isLoginSuccess = true;
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return isLoginSuccess;
    // }

    // private static String getKey() {
    // String result =
    // NetUtility.sendHttpRequestByGet(Constants.URL_SUPPLEMENT_ENCRYPT_KEY);
    // if (!TextUtils.isEmpty(result)) {
    // try {
    // JSONObject obj = new JSONObject(result);
    // String isSuccess = obj.getString(JsonInterface.JK_IS_SUCCESS);
    // if (isSuccess.equalsIgnoreCase("Y")) {
    // String key = obj.getString(JsonInterface.JK_KEY);
    // return key;
    // }
    // } catch (JSONException e) {
    // e.printStackTrace();
    // }
    // }
    // return result;
    // }

    /**
     * 获取sign 
     * @param user
     * @param pwd
     * @param key
     * @return
     */
    @Deprecated
    public static String getSign(String user, String pwd, String key) {
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
}
