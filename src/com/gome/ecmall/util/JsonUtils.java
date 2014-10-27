package com.gome.ecmall.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * JSON -- 工具类
 */
public class JsonUtils {
    private static final boolean DEBUG = false;

    /**
     * 构建Json
     * 
     * @param keys
     * @param values
     * @return
     */
    public static JSONObject createJsonObject(String[] keys, Object[] values) {
        if (keys == null || values == null || keys.length < 1 || values.length < 1)
            return null;
        int length = Math.min(keys.length, values.length);
        JSONObject obj = new JSONObject();

        try {
            for (int i = 0; i < length; i++) {
                obj.put(keys[i], values[i]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (DEBUG)
            Log.d("====createJsonObject(String[],Object[])====", obj.toString());
        return obj;
    }

    /**
     * 将json 加入jsonArray
     * 
     * @param obj
     * @return
     */
    public static JSONArray createJsonArray(JSONObject obj) {
        if (obj == null)
            return null;
        JSONArray jsonArray = null;
        if (jsonArray == null)
            jsonArray = new JSONArray();
        jsonArray.put(obj);
        return jsonArray;
    }

    /**
     * 未使用
     * 
     * @param obj
     * @return
     */
    @Deprecated
    public static JSONArray createJsonArray(ArrayList<JSONObject> obj) {
        if (obj == null || obj.size() < 1)
            return null;
        JSONArray jsonArray = new JSONArray();
        int length = obj.size();
        try {
            for (int i = 0; i < length; i++) {
                jsonArray.put(obj.get(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (DEBUG)
            Log.d("====createJsonArray(ArrayList<JSONObject>)====", jsonArray.toString());
        return jsonArray;
    }

    public static JSONObject createJsonObject(String key, JSONArray array) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(key, array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (DEBUG)
            Log.d("==createJsonObject(String , JSONArray)======", obj.toString());
        return obj;
    }

    /**
     * 构建访问请求json
     * @param map
     * @return
     */
    public static String createRequestJson(HashMap<String, String> map) {
        String json = null;
        if (map != null) {
            JSONObject obj = new JSONObject();
            for (String key : map.keySet()) {
                try {
                    obj.put(key, map.get(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            json = obj.toString();
        }
        return json;
    }

}
