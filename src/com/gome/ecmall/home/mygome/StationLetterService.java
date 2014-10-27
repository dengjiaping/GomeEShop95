package com.gome.ecmall.home.mygome;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.StationLetter;

public class StationLetterService {

    private static final String TAG = "StationLetterService";

    public static String createRequest(int currentPage, int pageSize) {

        JSONObject obj = new JSONObject();
        try {
            if (currentPage > 0 && pageSize > 0) {
                obj.put(JsonInterface.JK_CURRENT_PAGE, currentPage);
                obj.put(JsonInterface.JK_PAGE_SIZE, pageSize);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    public static StationLetter parseJsonStationLetter(JSONObject obj) {
        if (obj == null) {
            return null;
        }
        StationLetter sl = new StationLetter();
        sl.setMessageArray(obj.optString(JsonInterface.JK_MESSAGE_ARRAY));
        sl.setMessageContent(obj.optString(JsonInterface.JK_MESSAGE_CONTENT));
        sl.setMessageId(obj.optString(JsonInterface.JK_MESSAGE_ID));
        sl.setMessageTime(obj.optString(JsonInterface.JK_MESSAGE_TIME));
        sl.setMessageTitle(obj.optString(JsonInterface.JK_MESSAGE_TITLE));
        sl.setReadStatus(obj.optString(JsonInterface.JK_READ_STATUS));
        return sl;
    }

    public static ArrayList<StationLetter> parseJsonStationLetterList(String json) {
        if (json == null) {
            return null;
        }
        JSONObject obj;
        ArrayList<StationLetter> list = null;
        try {
            obj = new JSONObject(json);
            JSONArray arr = obj.optJSONArray(JsonInterface.JK_MESSAGE_ARRAY);
            if (arr != null) {
                list = new ArrayList<StationLetter>();
                for (int i = 0, len = arr.length(); i < len; i++) {
                    list.add(parseJsonStationLetter(arr.optJSONObject(i)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }
}
