package com.gome.ecmall.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.os.Build;

import com.gome.ecmall.bean.GlobalConfig;
import com.gome.ecmall.bean.JsonResult;

/**
 * http请求工具类,根据版本号进行http请求方式切换
 * 
 * @author qiudongchao
 * 
 */
public class HttpUtils {
	public static final String TAG = "HttpUtils";
	public static final String TAG_TIME = "time";

	/**
	 * 连接失败标识
	 */
	public static final String NO_CONN = "FAIL";

	/**
	 * 设置请求超时30秒
	 */
	private static final int REQUEST_TIMEOUT = 30 * 1000;

	/**
	 * 设置等待数据超时时间30秒钟
	 */
	private static final int SO_TIMEOUT = 30 * 1000;

	/**
	 * 全局配置
	 */
	public static GlobalConfig config = GlobalConfig.getInstance();

	/**
	 * 切换访问方式的分割-版本号  default:9
	 */
	private static final int VERSION = 9;
	
	private static long count = 0;

	/**
	 * 执行GET请求
	 * 
	 * @param url
	 *            请求URL
	 * @return
	 */
	public static String sendHttpRequestByGet(String url) {
		BDebug.w(TAG, "发送GET请求的URL =" + url);
		String result = NO_CONN;
		long begin = System.currentTimeMillis(); // TODO temp
		if (Build.VERSION.SDK_INT >= VERSION) {
			result = sendByHurlGet(url);
			BDebug.d(TAG_TIME, "url-get");
		} else {
			result = sendByClientGet(url);
			BDebug.d(TAG_TIME, "client-get");
		}
		long end = System.currentTimeMillis(); // TODO temp
		BDebug.d(TAG_TIME, url + (end - begin) + ":get"); // TODO temp
		BDebug.d(TAG_TIME, "COUNT:"+(count+=(end - begin)));
		BDebug.d(TAG, "获取GET请求的返回结果 Entity= " + result);
		return result;
	}

	/**
	 * HttpURLConnection -- GET
	 * 
	 * @param url
	 * @return
	 */
	@SuppressLint("NewApi")
	private static String sendByHurlGet(String url) {
		String result = NO_CONN;
		HttpURLConnection connection = null;
		try {
			CookieManager manager = new CookieManager();
			manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
			CookieHandler.setDefault(manager);

			URL parsedUrl = new URL(url);
			connection = openAndConfigConnection(parsedUrl);
			connection.setUseCaches(true);
			// 设置HTTP请求方式为 GET
			connection.setRequestMethod("GET");

			// 打开连接
			connection.connect();

			// 获取HTTP头
			connection.getHeaderFields();

			// 获取 CookieStore
			CookieStore store = manager.getCookieStore();

			// 获取返回码 【200-成功】
			int responseCode = connection.getResponseCode();

			List<HttpCookie> cookies = store.getCookies();
			StringBuilder cookiesInfo = new StringBuilder();
			boolean needUpdate = false;
			for (HttpCookie cookie : cookies) {
				if (cookie.getName().equals(GlobalConfig.JSESSIONID)) {
					String value = cookie.getValue();
					if (!value.equals(config.getjSessionId())) {
						config.setjSessionId(value);
						needUpdate = true;
					}
				}
				cookiesInfo.append(cookie.getName() + "=" + cookie.getValue())
						.append(";");
			}
			if (needUpdate) {
				config.setCookieInfo(cookiesInfo.toString());
			}

			if (responseCode == HttpURLConnection.HTTP_OK) {
				result = convertStreamToString(connection.getInputStream());
				// 如果已登录则需要重新登录
				JsonResult jsonresult = new JsonResult(result);
				if ("Y".equalsIgnoreCase(jsonresult.getIsSessionExpired())) {
					if (GlobalConfig.isLogin) {
						GlobalConfig.isLogin = false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}
		return result;
	}

	/**
	 * DefaultHttpClient -- GET
	 * 
	 * @param url
	 * @return
	 */
	private static String sendByClientGet(String url) {
		String result = NO_CONN;
		try {
			HttpGet httpGet = new HttpGet(url);

			if (config.getCookieInfo() != null) {
				httpGet.setHeader("Cookie", config.getCookieInfo());
			}
			httpGet.setHeader("User-Agent", "android GomeShopApp "
					+ VersionUpdateUtils.USERUPDATEVERSONCODE + ";");

			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					REQUEST_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);

			DefaultHttpClient defaultHttpClient = new DefaultHttpClient(
					httpParams);
			HttpResponse httpResponse = defaultHttpClient.execute(httpGet);

			int responseCode = httpResponse.getStatusLine().getStatusCode();
			BDebug.d(TAG, "Send Get Request responseCode = " + responseCode);

			List<Cookie> cookies = defaultHttpClient.getCookieStore()
					.getCookies();
			StringBuffer cookiesInfo = new StringBuffer();
			boolean needUpdate = false;
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(GlobalConfig.JSESSIONID)) {
					String value = cookie.getValue();
					if (!value.equals(config.getjSessionId())) {
						config.setjSessionId(value);
						needUpdate = true;
					}
				}
				cookiesInfo.append(cookie.getName() + "=" + cookie.getValue())
						.append(";");
			}
			if (needUpdate) {
				config.setCookieInfo(cookiesInfo.toString());
			}
			if (responseCode == HttpURLConnection.HTTP_OK) {
				result = EntityUtils.toString(httpResponse.getEntity()).trim();
				// 如果已登录则需要重新登录
				JsonResult jsonresult = new JsonResult(result);
				if ("Y".equalsIgnoreCase(jsonresult.getIsSessionExpired())) {
					if (GlobalConfig.isLogin) {
						GlobalConfig.isLogin = false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// ***************************** POST **********************************

	/**
	 * 执行 POST 请求
	 * 
	 * @param url
	 *            请求 URL
	 * @param json
	 *            请求 JSON
	 * @return
	 */
	public static String sendHttpRequestByPost(String url, String json) {
		BDebug.w(TAG, "发送POST请求的URL =" + url);
		BDebug.w(TAG, "发送POST请求的JSON =" + json);
		String result = NO_CONN;

		long begin = System.currentTimeMillis();// TODO temp

		if (Build.VERSION.SDK_INT >= VERSION) {
			BDebug.d(TAG_TIME, "url-post");
			result = sendByHurlPost(url, json);
		} else {
			BDebug.d(TAG_TIME, "client-post");
			result = sendByClientPost(url, json);
		}

		long end = System.currentTimeMillis();// TODO temp

		BDebug.d(TAG_TIME, url + (end - begin) + ":post");// TODO temp
		BDebug.d(TAG_TIME, "COUNT:"+(count+=(end - begin)));
		BDebug.d(TAG, "获取GET请求的返回结果 Entity= " + result);
		return result;
	}

	/**
	 * HttpURLConnection -- POST
	 * 
	 * @param url
	 * @param json
	 * @return
	 */
	@SuppressLint("NewApi")
	private static String sendByHurlPost(String url, String json) {
		String result = NO_CONN;
		HttpURLConnection connection = null;
		UrlEncodedFormEntity p_entity;
		ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("body", json));
		try {
			p_entity = new UrlEncodedFormEntity(pairs, "utf-8");
			CookieManager manager = new CookieManager();
			manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
			CookieHandler.setDefault(manager);

			URL parsedUrl = new URL(url);
			connection = openAndConfigConnection(parsedUrl);
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.addRequestProperty("Content-type",
					"application/x-www-form-urlencoded;charset=utf-8");

			connection.connect();

			// 此处设置POST 请求参数
			OutputStream os = connection.getOutputStream();
			p_entity.writeTo(os);
			os.flush();

			connection.getHeaderFields();
			CookieStore store = manager.getCookieStore();

			int responseCode = connection.getResponseCode();

			List<HttpCookie> cookies = store.getCookies();
			StringBuffer cookiesInfo = new StringBuffer();
			boolean needUpdate = false;
			for (HttpCookie cookie : cookies) {
				if (cookie.getName().equals(GlobalConfig.JSESSIONID)) {
					String value = cookie.getValue();
					if (!value.equals(config.getjSessionId())) {
						config.setjSessionId(value);
						needUpdate = true;
					}
				}
				cookiesInfo.append(cookie.getName() + "=" + cookie.getValue())
						.append(";");
			}
			if (needUpdate) {
				config.setCookieInfo(cookiesInfo.toString());
			}

			if (responseCode == HttpURLConnection.HTTP_OK) {
				result = convertStreamToString(connection.getInputStream());
				// 如果已登录则需要重新登录
				JsonResult jsonresult = new JsonResult(result);
				if ("Y".equalsIgnoreCase(jsonresult.getIsSessionExpired())) {
					if (GlobalConfig.isLogin) {
						NetUtility
								.sendHttpRequestByGet(Constants.URL_PROFILE_USER_LOGINOUT);
						GlobalConfig.isLogin = false;
					}
				}
			}

		} catch (Exception e) {
		} finally {
			connection.disconnect();
		}
		return result;
	}

	/**
	 * DefaultHttpClient -- POST
	 * 
	 * @param url
	 * @param json
	 * @return
	 */
	private static String sendByClientPost(String url, String json) {
		String result = NO_CONN;
		HttpPost httpPost = new HttpPost(url);
		if (config.getCookieInfo() != null) {
			httpPost.setHeader("Cookie", config.getCookieInfo());
		}
		httpPost.setHeader("User-Agent", "android GomeShopApp "
				+ VersionUpdateUtils.USERUPDATEVERSONCODE + ";");
		// 传参服务端获取的方法为request.getParameter("name")
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("body", json));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					REQUEST_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			int responseCode = httpResponse.getStatusLine().getStatusCode();
			BDebug.d(TAG, "Send Post Request responseCode = " + responseCode
					+ "\n httpResponse=" + httpResponse);
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
				cookiesInfo.append(cookie.getName() + "=" + cookie.getValue())
						.append(";");
			}
			if (needUpdate) {
				// 如果已登录则需要重新登录
				// 更新cookie信息
				config.setCookieInfo(cookiesInfo.toString());
			}
			if (responseCode == HttpURLConnection.HTTP_OK) {
				result = EntityUtils.toString(httpResponse.getEntity()).trim();
				// 如果已登录则需要重新登录
				JsonResult jsonresult = new JsonResult(result);
				if ("Y".equalsIgnoreCase(jsonresult.getIsSessionExpired())) {
					if (GlobalConfig.isLogin) {
						NetUtility
								.sendHttpRequestByGet(Constants.URL_PROFILE_USER_LOGINOUT);
						GlobalConfig.isLogin = false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			BDebug.d(TAG, "Send Post Request Response Entity = :" + result);
		}
		return result;
	}

	// ***************************** COMMON **********************************

	/**
	 * InputStream --> String
	 * 
	 * @param is
	 * @return
	 */
	protected static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * 打开并配置 HttpURLConnection
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private static HttpURLConnection openAndConfigConnection(URL url)
			throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// 设置连接超时时间
		connection.setConnectTimeout(REQUEST_TIMEOUT);

		connection.setReadTimeout(SO_TIMEOUT);

		// 设置 Cookie
		if (config.getCookieInfo() != null) {
			connection.setRequestProperty("Cookie", config.getCookieInfo());
		}

		// 设置 User-Agent
		connection.setRequestProperty("User-Agent", "android GomeShopApp "
				+ VersionUpdateUtils.USERUPDATEVERSONCODE + ";");

		connection.setDoInput(true);

		return connection;
	}

}
