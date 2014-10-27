package com.gome.ecmall.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 商品评价【复合类】
 */
public class ProductAppraise implements JsonInterface {
    public static final String JK_APPRAISE_ARRAY = "appraiseArray";
    public static final String JK_APPRAISE_ID = "appraiseId";
    public static final String JK_APPRAISE_NAME = "appraiseName";
    public static final String JK_APPRAISE_ADVANTAGE = "appraiseAdvantage";
    public static final String JK_APPRAISE_DISADVANTAGE = "appraiseDisadvantage";
    public static final String JK_APPRAISE_SUMMARY = "summary";
    public static final String JK_APPRAISE_TIME = "appraiseTime";
    public static final String JK_APPRAISE_TITLE = "title";
    public static final String JK_APPRAISE_GRADE = "appraiseGrade";

    /**
     * 创建请求评价列表的JSON
     * 
     * @param goodsNo
     *            商品编号
     * @param currentPage
     *            当前页
     * @param pageSize
     *            请求数量
     * @return
     */
    public static String createRequestAppraiseListJson(String goodsNo, int currentPage, int pageSize) {
        JSONObject json = new JSONObject();
        try {
            json.put(JK_GOODS_NO, goodsNo);
            json.put(JK_CURRENT_PAGE, currentPage);
            json.put(JK_PAGE_SIZE, pageSize);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public static ArrayList<Appraise> paserResponseAppraiseList(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        ArrayList<Appraise> list = null;
        try {
            JsonResult result = new JsonResult(json);
            if (result.isSuccess()) {
                JSONObject content = result.getJsContent();
                if (content != null) {
                    JSONArray array = content.optJSONArray(JK_APPRAISE_ARRAY);
                    if (array != null) {
                        list = new ArrayList<Appraise>();
                        for (int i = 0, size = array.length(); i < size; i++) {
                            JSONObject item = array.getJSONObject(i);
                            Appraise appraise = new Appraise();
                            appraise.setAppraiseId(item.optString(JK_APPRAISE_ID));
                            appraise.setAppraiseName(item.optString(JK_APPRAISE_NAME));
                            appraise.setAppraiseAdvantage(item.optString(JK_APPRAISE_ADVANTAGE));
                            appraise.setAppraiseDisadvantage(item.optString(JK_APPRAISE_DISADVANTAGE));
                            appraise.setSummary(item.optString(JK_APPRAISE_SUMMARY));
                            appraise.setAppraiseTime(item.optString(JK_APPRAISE_TIME));
                            appraise.setTitle(item.optString(JK_APPRAISE_TITLE));
                            String grade = item.optString(JK_APPRAISE_GRADE);
                            if (grade != null) {
                                appraise.setAppRaiseGrade(Float.parseFloat(grade));
                            }
                            list.add(appraise);
                        }// end for
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 商品评价【实体】
     */
    public static class Appraise {

        private String appraiseId;
        private String appraiseName;
        private String appraiseAdvantage;
        private String appraiseDisadvantage;
        private String summary;
        private String appraiseTime;
        private String title;
        private float appRaiseGrade;

        public String getAppraiseId() {
            return appraiseId;
        }

        public void setAppraiseId(String appraiseId) {
            this.appraiseId = appraiseId;
        }

        public String getAppraiseName() {
            return appraiseName;
        }

        public void setAppraiseName(String appraiseName) {
            this.appraiseName = appraiseName;
        }

        public String getAppraiseAdvantage() {
            return appraiseAdvantage;
        }

        public void setAppraiseAdvantage(String appraiseAdvantage) {
            this.appraiseAdvantage = appraiseAdvantage;
        }

        public String getAppraiseDisadvantage() {
            return appraiseDisadvantage;
        }

        public void setAppraiseDisadvantage(String appraiseDisadvantage) {
            this.appraiseDisadvantage = appraiseDisadvantage;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getAppraiseTime() {
            return appraiseTime;
        }

        public void setAppraiseTime(String appraiseTime) {
            this.appraiseTime = appraiseTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public float getAppRaiseGrade() {
            return appRaiseGrade;
        }

        public void setAppRaiseGrade(float appRaiseGrade) {
            this.appRaiseGrade = appRaiseGrade;
        }

    }

}
