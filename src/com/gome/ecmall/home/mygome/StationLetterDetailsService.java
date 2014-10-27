package com.gome.ecmall.home.mygome;

import org.json.JSONException;
import org.json.JSONObject;

import com.gome.ecmall.bean.JsonInterface;
import com.gome.ecmall.bean.StationLetterDetails;

public class StationLetterDetailsService {

    public static String createJson(String messageId, String readStatus) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(JsonInterface.JK_MESSAGE_ID, messageId);
            obj.put(JsonInterface.JK_READ_STATUS, readStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    public static StationLetterDetails parseStationLetterDetails(String result) {
        if (result == null || result.length() == 0) {
            return null;
        }
        StationLetterDetails details = new StationLetterDetails();
        JSONObject obj;
        try {
            obj = new JSONObject(result);
            details.setMessageId(obj.optString(JsonInterface.JK_MESSAGE_ID));
            details.setMessageTitle(obj.optString(JsonInterface.JK_MESSAGE_TITLE));
            details.setMessageContent(obj.optString(JsonInterface.JK_MESSAGE_CONTENT));
            details.setMessageTime(obj.optString(JsonInterface.JK_MESSAGE_TIME));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return details;
    }
}
