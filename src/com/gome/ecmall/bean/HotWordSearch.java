package com.gome.ecmall.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;

/**
 * 热词搜索【复合类】
 */
public class HotWordSearch implements JsonInterface {

    // 当前第几页
    public static final String JK_HOTWORDSEARCH_CURRENTPAGE = "currentPage";
    // 每页总数
    public static final String JK_HOTWORDSEARCH_PAGESIZE = "pageSize";
    public static final String JK_HOTWORDSEARCH_KEYWORDLIST = "keywordsList";
    public static final String JK_HOTWORDSEARCH_KEYWORD = "keyword";
    public static final String JK_HOTWORDSEARCH_TOTALCOUNT = "totalCount";
    public static final String JK_HOTWORDSEARCH_COUNT = "count";
    public static final String JK_HOTWORDSEARCH_GOODSTYPEID = "goodsTypeId";

    /**
     * 获取热词_请求数据
     * 
     * @param delAddressId
     * @return
     */
    public static String getReqHotWordSearch(int currentPage, int pageSize) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put(JK_HOTWORDSEARCH_CURRENTPAGE, currentPage);
            requestJson.put(JK_HOTWORDSEARCH_PAGESIZE, pageSize);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取热词_结果
     * 
     * @param json
     * @return
     */
    public static List<HotWord> getResHotWord(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            JsonResult result = new JsonResult(json);
            if (result.isSuccess()) {
                JSONObject content = result.getJsContent();
                if (content != null) {
                    JSONArray jsonArray = content.optJSONArray(JK_HOTWORDSEARCH_KEYWORDLIST);
                    if (jsonArray != null) {
                        List<HotWord> list = new ArrayList<HotWord>();
                        for (int i = 0, length = jsonArray.length(); i < length; i++) {
                            HotWord hotword = new HotWord();
                            JSONObject jsonobj = jsonArray.optJSONObject(i);
                            hotword.setKeyword(jsonobj.optString(JK_HOTWORDSEARCH_KEYWORD));
                            hotword.setTotalCount(jsonobj.optString(JK_HOTWORDSEARCH_TOTALCOUNT));
                            list.add(hotword);
                        }
                        return list;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 热词【实体】
     */
    public static class HotWord implements Serializable {
        private static final long serialVersionUID = 1L;
        private String keyword;
        private String totalCount;

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(String totalCount) {
            this.totalCount = totalCount;
        }
    }

    /**
     * 搜索智能提示
     * 
     * @param keyWord
     * @param goodsTypeId
     * @return
     */
    public static String createKeyWordInClude(String keyWord, String goodsTypeId) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put(JK_HOTWORDSEARCH_KEYWORD, keyWord);
            requestJson.put(JK_HOTWORDSEARCH_GOODSTYPEID, goodsTypeId);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 搜索智能提示数据返回
     * 
     * @param json
     * @return
     */
    public static ArrayList<String> parseAllKeyWordList(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            JsonResult result = new JsonResult(json);
            if (result.isSuccess()) {
                JSONObject content = result.getJsContent();
                if (content != null) {
                    JSONArray jsonArray = content.optJSONArray(JK_HOTWORDSEARCH_KEYWORDLIST);
                    if (jsonArray != null) {
                        ArrayList<String> list = new ArrayList<String>();
                        for (int i = 0, length = jsonArray.length(); i < length; i++) {
                            JSONObject jsonobj = jsonArray.optJSONObject(i);
                            String keywordStr = jsonobj.optString(JK_HOTWORDSEARCH_KEYWORD);
                            String countStr = jsonobj.optString(JK_HOTWORDSEARCH_COUNT);
                            if (!TextUtils.isEmpty(keywordStr)) {
                                list.add(keywordStr + "," + countStr);
                            }
                        }
                        return list;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}