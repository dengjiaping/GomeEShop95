package com.gome.ecmall.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.res.AssetManager;

import com.gome.ecmall.bean.GlobalConfig;

/**
 * HTTPS--工具类
 */
public class HttpsUtils {

    public static AssetManager mAssetManager;
    public static DefaultHttpClient httpClient;
    private static final String TAG = "HttpsUtils";

    public static void initKey(AssetManager assetManager) throws Exception {
        mAssetManager = assetManager;
        String path = MobileMD5.encrypt(Constants.PATH, "utf-8");
        String passwd = DES.decryptDES(Constants.CLIENT_TRUST_PASSWORD, Constants.LOGINDESKEY);
        initHttpsURLConnection(passwd, path, path);
    }

    /**
     * 获取KeyStore
     * 
     * @param password
     *            密码
     * @param keyStorePath
     *            密钥库路径
     * @return 密钥库
     * @param manager
     *            assets管理器
     * @throws Exception
     */
    public static KeyStore getKeyStore(String password, String keyStorePath, AssetManager manager) throws Exception {
        // 实例化密钥库
        KeyStore ks = KeyStore.getInstance(Constants.CLIENT_TRUST_KEYSTORE);
        // 获得密钥库文件流
        InputStream in = manager.open(keyStorePath);
        // 加载密钥库
        ks.load(in, password.toCharArray());
        // 关闭文件流
        in.close();
        return ks;
    }

    /**
     * 获得SSLSocketFactory
     * 
     * @param password
     *            密码
     * @param keyStorePath
     *            密钥库路径
     * @param trustStorePaht
     *            信任库路径
     * @return SSLSocketFactory
     * @throws Exception
     */
    public static void sslContextInit(String password, String keyStorePath, String trustStorePath) throws Exception {
        // 实例化密钥库
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(Constants.CLIENT_TRUST_MANAGER);
        // 获得密钥库
        KeyStore keyStore = getKeyStore(password, keyStorePath, mAssetManager);
        // 初始化密钥工厂
        keyManagerFactory.init(keyStore, password.toCharArray());

        // 实例化信任库
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(Constants.CLIENT_TRUST_MANAGER);
        KeyStore trustKeyStore = getKeyStore(password, keyStorePath, mAssetManager);
        trustManagerFactory.init(trustKeyStore);

        SSLSocketFactory socketFactory = new SSLSocketFactory(trustKeyStore);
        socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        Scheme sch = new Scheme("https", socketFactory, 443);
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 30 * 1000);
        HttpConnectionParams.setSoTimeout(httpParams, 30 * 1000);
        httpClient = new DefaultHttpClient(httpParams);
        httpClient.getConnectionManager().getSchemeRegistry().register(sch);
    }

    /**
     * 初始化HttpsURLConnection
     * 
     * @param password
     *            密码
     * @param keyStorePath
     *            密钥库路径
     * @param trustStorePath
     *            信任库路径
     * @throws Exception
     */
    public static void initHttpsURLConnection(String password, String keyStorePath, String trustStorePath)
            throws Exception {
        SSLContext sslContext = null;
        // 实例化主机名验证接口
        HostnameVerifier hnv = new MyHostnameVerifier();
        sslContextInit(password, keyStorePath, trustStorePath);
        // if (sslContext != null) {
        // HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        // }
        // HttpsURLConnection.setDefaultHostnameVerifier(hnv);
    }

    /**
     * 发送网络请求
     * 
     * @param httpsUrl
     * @param json
     * @return
     */
    public static String post(String httpsUrl, String json) {
        BDebug.i(TAG, httpsUrl + "");
        BDebug.i(TAG, json + "");
        String result = null;
        HttpPost post = new HttpPost(httpsUrl);
        GlobalConfig config = GlobalConfig.getInstance();
        if (GlobalConfig.getInstance().getCookieInfo() != null) {
            post.setHeader("Cookie", config.getCookieInfo());
        }
        post.setHeader("User-Agent", "android GomeShopApp " + VersionUpdateUtils.USERUPDATEVERSONCODE + ";");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("body", json));
        try {
            post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse httpResponse = httpClient.execute(post);
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            boolean needUpdate = false;
            List<Cookie> cookies = httpClient.getCookieStore().getCookies();
            StringBuffer cookiesInfo = new StringBuffer();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(GlobalConfig.JSESSIONID)) {
                    String value = cookie.getValue();
                    if (!value.equals(config.getjSessionId())) {
                        // sessionID失效，如果已登录则需重新登录
                        // 设置新的jsessionId
                        config.setjSessionId(value);
                        needUpdate = true;
                    }
                }
                cookiesInfo.append(cookie.getName() + "=" + cookie.getValue()).append(";");
            }
            if (needUpdate) {
                // 如果已登录则需要重新登录
                // 更新cookie信息
                config.setCookieInfo(cookiesInfo.toString());
            }
            if (responseCode == HttpURLConnection.HTTP_OK) {
                result = EntityUtils.toString(httpResponse.getEntity()).trim();
                BDebug.i(TAG, result);
            }
            return result;
        } catch (Exception e) {
        }
        // StringBuffer str = new StringBuffer();
        // HttpsURLConnection urlConnection = null;
        // try {
        // urlConnection = (HttpsURLConnection) (new URL(httpsUrl)).openConnection();
        // urlConnection.setDoInput(true);
        // urlConnection.setDoOutput(true);
        // if (GobalConfig.getInstance().getCookieInfo() != null) {
        // urlConnection.setRequestProperty("Cookie", GobalConfig.getInstance().getCookieInfo());
        // }
        // urlConnection.setConnectTimeout(10 * 1000);
        // urlConnection.setRequestMethod("POST");
        // urlConnection.setUseCaches(false);
        // urlConnection.getOutputStream().write(json.getBytes());
        // urlConnection.getOutputStream().flush();
        // urlConnection.getOutputStream().close();
        // BufferedReader bReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        // String line;
        // while ((line = bReader.readLine()) != null) {
        // str.append(line);
        // }
        // String cookie = urlConnection.getHeaderField("Set-Cookie");// 获取cookie
        // BDebug.e("cookie", cookie + "@@@");
        // } catch (Exception e) {
        // // TODO: handle exception
        // e.printStackTrace() ;
        // }
        return result;
    }

}
