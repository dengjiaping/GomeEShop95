package com.gome.ecmall.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 活动实体类【单独类】
 */
public class ActivityEntity implements JsonInterface {

    private String activityImgUrl;
    private String activityName;
    private String activityId;
    private String activityType;
    private String activityHtmlUrl;
    private String startDate;
    private String endDate;
    private String activityRule;
    private String relatedID;

    private boolean isLoadImage;

    public ActivityEntity() {
    }

    public ActivityEntity(String activityImgUrl, String activityName, String activityId, String activityType,
            String activityHtmlUrl, String startDate, String endDate) {
        super();
        this.activityImgUrl = activityImgUrl;
        this.activityName = activityName;
        this.activityId = activityId;
        this.activityType = activityType;
        this.activityHtmlUrl = activityHtmlUrl;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getRelatedID() {
        return relatedID;
    }

    public void setRelatedID(String relatedID) {
        this.relatedID = relatedID;
    }

    public String getActivityImgUrl() {
        return activityImgUrl;
    }

    public void setActivityImgUrl(String activityImgUrl) {
        this.activityImgUrl = activityImgUrl;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityHtmlUrl() {
        return activityHtmlUrl;
    }

    public void setActivityHtmlUrl(String activityHtmlUrl) {
        this.activityHtmlUrl = activityHtmlUrl;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setLoadImage(boolean flag) {
        isLoadImage = flag;
    }

    public boolean isLoadImage() {
        return isLoadImage;
    }

    @Override
    public String toString() {
        return "ActivityEntity [activityImgUrl=" + activityImgUrl + ", activityName=" + activityName + ", activityId="
                + activityId + ", activityType=" + activityType + ", activityHtmlUrl=" + activityHtmlUrl
                + ", startDate=" + startDate + ", endDate=" + endDate + "]";
    }

    public String getActivityRule() {
        return activityRule;
    }

    public void setActivityRule(String activityRule) {
        this.activityRule = activityRule;
    }

    /**
     * 创建首页广告请求参数
     * 
     * @param currentPage当前页
     * @param pageSize
     *            当前页条数
     * @return
     */
    public static String createRequestActivityRecommendListJson(int currentPage, int pageSize) {
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
     * 解析活动列表
     * @param json
     * @return
     */
    public static ArrayList<ActivityEntity> parseActivityRecommendList(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject content = result.getJsContent();
        JSONArray array = content.optJSONArray(JK_ACTIVITY_LIST);
        ArrayList<ActivityEntity> arrayList = new ArrayList<ActivityEntity>();
        try {

            if (array != null) {
                for (int i = 0, size = array.length(); i < size; i++) {
                    JSONObject item = array.getJSONObject(i);
                    ActivityEntity recommend = new ActivityEntity();
                    recommend.setActivityHtmlUrl(item.optString(JK_ACTIVITY_HTML_URL));
                    recommend.setActivityId(item.optString(JK_ACTIVITY_ID));
                    recommend.setActivityImgUrl(item.optString(JK_ACTIVITY_IMG_URL));// UrlMatcher.getFitPageAdUrl()
                    recommend.setActivityName(item.optString(JK_ACTIVITY_NAME));
                    recommend.setActivityType(item.optString(JK_ACTIVITY_TYPE));
                    recommend.setEndDate(item.optString(JK_END_DATE));
                    recommend.setStartDate(item.optString(JK_START_DATE));
                    recommend.setRelatedID(item.optString(JK_RELATED_ID));
                    arrayList.add(recommend);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
