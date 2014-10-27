package com.gome.ecmall.home.suitebuy;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.JsonResult;
import com.gome.ecmall.bean.SuiteBuyFilter;

public class SuiteBuyFilterService {

    public static ArrayList<SuiteBuyFilter> parseJson(String json) {
        JsonResult jsonResult = new JsonResult(json);
        JSONObject obj = jsonResult.getJsContent();
        ArrayList<SuiteBuyFilter> list = null;
        if (obj != null) {
            JSONArray arr = obj.optJSONArray(JsonInterface.JK_SUITE_FILTER_LIST);
            int len = arr.length();
            if (arr != null && len > 0) {
                list = new ArrayList<SuiteBuyFilter>();
                for (int i = 0; i < len; i++) {
                    JSONObject sbfObj = arr.optJSONObject(i);
                    SuiteBuyFilter sbf = new SuiteBuyFilter();
                    sbf.setSelectIndex(sbfObj.optString(JsonInterface.JK_SELECT_INDEX));
                    sbf.setSelectIndexName(sbfObj.optString(JsonInterface.JK_SELECT_INDEX_NAME));
                    list.add(sbf);
                }
            }
        }
        return list;
    }
}
