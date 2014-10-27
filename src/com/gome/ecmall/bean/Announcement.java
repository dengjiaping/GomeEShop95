package com.gome.ecmall.bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 商城公告实体类【复合类】
 */
public class Announcement implements JsonInterface {

    /**
     * {\"currentPage\":1,\"pageSize\":10} 创建请求公告列表的JSON
     * 
     * @param currentPage
     * @param pageSize
     * @return
     */
    public static String createRequestAnnListJson(int currentPage, int pageSize) {
        JSONObject outer = new JSONObject();
        try {
            outer.put(JK_CURRENT_PAGE, currentPage);
            outer.put(JK_PAGE_SIZE, pageSize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return outer.toString();
    }

    /**
     * 创建请求详细公告的JSON
     * 
     * @param newsId
     *            公告ID
     * @return
     */
    public static String createRequestAnnDetailJson(String newsId) {
        JSONObject json = new JSONObject();
        try {
            json.put(JK_NEWS_ID, newsId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    /**
     * 解析公告列表的JSON
     * @param json
     * @return
     */
    public static ArrayList<ReplyAnnInfo> parseReplayAnnInfos(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        ArrayList<ReplyAnnInfo> list = null;
        try {
            JsonResult result = new JsonResult(json);
            if (result.isSuccess()) {
                JSONObject content = result.getJsContent();
                JSONArray array = content.optJSONArray(JsonInterface.JK_REPLIES);
                if (array != null) {
                    list = new ArrayList<ReplyAnnInfo>();
                    for (int i = 0, size = array.length(); i < size; i++) {
                        JSONObject item = array.getJSONObject(i);
                        ReplyAnnInfo info = new ReplyAnnInfo();
                        info.setNewsId(item.optString(JK_NEWS_ID));
                        info.setAnnSummary(item.optString(JK_ANN_SUMMARY));
                        info.setAnnTime(item.getString(JK_ANN_TIME));
                        list.add(info);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            list.clear();
        }
        return list;

    }

    /**
     * 解析公告列表的JSON
     * @param json
     * @return
     */
    public static AnnounceDetail parseReplyAnnounceDetail(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject content = result.getJsContent();
        AnnounceDetail detail = new AnnounceDetail();
        try {
            detail.setAnnSummary(content.getString(Announcement.JK_ANN_SUMMARY));
            detail.setAnnContent(content.getString(Announcement.JK_ANN_CONTENT));
            detail.setAnnTime(content.getString(JK_ANN_TIME));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return detail;

    }

    /**
     *公告详情-实体
     */
    public static class AnnounceDetail {
        private String annSummary;
        private String annTime;
        private String annContent;

        public String getAnnSummary() {
            return annSummary;
        }

        public void setAnnSummary(String annSummary) {
            this.annSummary = annSummary;
        }

        public String getAnnTime() {
            return annTime;
        }

        public void setAnnTime(String annTime) {
            this.annTime = annTime;
        }

        public String getAnnContent() {
            return annContent;
        }

        public void setAnnContent(String annContent) {
            this.annContent = annContent;
        }

    }

    /**
     *公告列表内单独公告-实体
     */
    public static class ReplyAnnInfo implements Parcelable {
        private String newsId;
        private String annSummary;
        private String annTime;

        public ReplyAnnInfo() {

        }

        public ReplyAnnInfo(Parcel source) {
            setNewsId(source.readString());
            setAnnTime(source.readString());
            setAnnSummary(source.readString());
        }

        public String getNewsId() {
            return newsId;
        }

        public void setNewsId(String newsId) {
            this.newsId = newsId;
        }

        public String getAnnSummary() {
            return annSummary;
        }

        public void setAnnSummary(String annSummary) {
            this.annSummary = annSummary;
        }

        public void setAnnTime(String annTime) {
            this.annTime = annTime;
        }

        public String getAnnTime() {
            return annTime;
        }

        @Override
        public int describeContents() {

            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(newsId);
            dest.writeString(annTime);
            dest.writeString(annSummary);
        }

        public static final Parcelable.Creator<ReplyAnnInfo> CREATOR = new Parcelable.Creator<ReplyAnnInfo>() {

            @Override
            public ReplyAnnInfo createFromParcel(Parcel source) {
                return new ReplyAnnInfo(source);
            }

            @Override
            public ReplyAnnInfo[] newArray(int size) {
                return new ReplyAnnInfo[size];
            }
        };

    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static String getFormatDate(long time) {
        return sdf.format(new Date(time));
    }
}
