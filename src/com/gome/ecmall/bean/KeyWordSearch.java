package com.gome.ecmall.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 关键词搜索【业务类】
 */
public class KeyWordSearch implements JsonInterface {

    /**
     * 创建请求热门推荐关键字的json
     * 
     * @param currentPage
     * @param pageSize
     * @return
     */
    public static String createRequestHotKeyWordsJson(int currentPage, int pageSize) {
        JSONObject json = new JSONObject();
        try {
            json.put(JK_CURRENT_PAGE, currentPage);
            json.put(JK_PAGE_SIZE, pageSize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    /**
     * 解析热门推荐关键字数据
     * 
     * @param json
     * @return
     */
    public static ArrayList<String> parseHotKeyWords(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject content = result.getJsContent();
        ArrayList<String> arrayList = new ArrayList<String>();
        try {
            JSONArray array = content.getJSONArray(JK_HOT_WORDS_LIST);
            for (int i = 0, size = array.length(); i < size; i++) {
                JSONObject item = array.getJSONObject(i);
                String keyword = item.getString(JK_KEY_WORD);
                arrayList.add(keyword);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /**
     * 解析输入框默认推荐关键字
     * 
     * @param json
     * @return
     */
    public static String parseDefaultKeyWords(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        String keyWord = null;
        JSONObject content = result.getJsContent();
        try {
            JSONArray array = content.getJSONArray(JK_HOT_WORDS_LIST);
            if (array.length() > 0) {
                JSONObject item = array.getJSONObject(0);
                keyWord = item.getString(JK_KEY_WORD);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return keyWord;
    }

}
