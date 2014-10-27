package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gome.ecmall.bean.AdvisoryReply;
import com.gome.ecmall.bean.JsonInterface;

/**
 * 【暂无调用】
 */
public class AdvisoryReplyService {

    public static String createJson(int currPage, int pageSize, int categoryId, int questionDate, int returnStatus) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(JsonInterface.JK_CURRENT_PAGE, currPage);
            obj.put(JsonInterface.JK_PAGE_SIZE, pageSize);
            obj.put(JsonInterface.JK_CATEGORY_ID, categoryId);
            obj.put(JsonInterface.JK_QUESTIION_DATE, questionDate);
            obj.put(JsonInterface.JK_RETURN_STATUS, returnStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    public static ArrayList<AdvisoryReply> parseAdvisoryReplyList(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        ArrayList<AdvisoryReply> list = null;
        JSONObject obj;
        try {
            obj = new JSONObject(json);
            JSONArray arr = obj.optJSONArray(JsonInterface.JK_QUES_ARRAY);
            if (arr != null && arr.length() > 0) {
                list = new ArrayList<AdvisoryReply>();
                int len = arr.length();
                for (int i = 0; i < len; i++) {
                    JSONObject replyObj = arr.optJSONObject(i);
                    AdvisoryReply reply = new AdvisoryReply();
                    reply.setProfileID(replyObj.optString(JsonInterface.JK_PROFILE_ID));
                    reply.setQuestionTime(replyObj.optString(JsonInterface.JK_QUESTION_TIME));
                    reply.setQuestionContent(replyObj.optString(JsonInterface.JK_QUESTION_CONTENT));
                    reply.setReturnArray(replyObj.optString(JsonInterface.JK_RETURN_ARRAY));

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }
}
