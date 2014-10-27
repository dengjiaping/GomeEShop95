package com.gome.ecmall.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

/**
 * 百度地图-工具类
 */
public class BaiduMapUtils {

    public static final String googleToBaiduUrl = "http://api.map.baidu.com/ag/coord/convert?from=2&to=4&x=#x#&y=#y#";

    private static String reverseGeocode(String url) {
        try {
            HttpGet httpGet = new HttpGet(url);
            BasicHttpParams httpParams = new BasicHttpParams();
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient(httpParams);
            HttpResponse httpResponse = defaultHttpClient.execute(httpGet);
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            if (responseCode == HttpStatus.SC_OK) {
                String charSet = EntityUtils.getContentCharSet(httpResponse.getEntity());
                if (null == charSet) {
                    charSet = "UTF-8";
                }
                String str = new String(EntityUtils.toByteArray(httpResponse.getEntity()), charSet);
                defaultHttpClient.getConnectionManager().shutdown();
                return str;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * google to baidu 坐标
     * @param x
     * @param y
     * @return
     */
    public static Map<String, Object> googleToBaidu(Double x, Double y) {
        Map<String, Object> map = new HashMap<String, Object>();
        String relt = reverseGeocode(getGoogleToBaiduUrl(String.valueOf(x), String.valueOf(y)));
        if (!TextUtils.isEmpty(relt)) {
            JSONObject jsonObj;
            try {
                jsonObj = new JSONObject(relt);
                String xjson = jsonObj.optString("x");
                String yjson = jsonObj.optString("y");
                map.put("x", Double.parseDouble(new String(Base64.decode(xjson))));
                map.put("y", Double.parseDouble(new String(Base64.decode(yjson))));
                return map;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String getGoogleToBaiduUrl(String x, String y) {
        return googleToBaiduUrl.replace("#x#", x).replace("#y#", y);
    }

}
