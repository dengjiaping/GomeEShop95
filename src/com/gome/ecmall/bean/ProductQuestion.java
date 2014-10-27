package com.gome.ecmall.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gome.ecmall.util.CommonUtility;

/**
 * 商品资讯
 */
public class ProductQuestion implements JsonInterface {

    // 咨询分类
    // 全部
    public static final int QUESTION_CATEGORY_ALL = 0;
    // 商品购买咨询
    public static final int QUESTION_CATEGORY_PRODUCT = 1;
    // 配送咨询
    public static final int QUESTION_CATEGORY_DELIVERY = 2;
    // 支付问题咨询
    public static final int QUESTION_CATEGORY_PAYMENT = 3;
    // 发票问题咨询
    public static final int QUESTION_CATEGORY_INVOICE = 4;
    private static final String TAG = "ProductQuestion";

    public static String createRequestProductQuestionListJson(String goodsId, int currentPage, int pageSize,
            int categoryId, String keyword) {
        JSONObject json = new JSONObject();
        try {
            json.put(JK_GOODS_NO, goodsId);
            json.put(JK_CURRENT_PAGE, currentPage);
            json.put(JK_PAGE_SIZE, pageSize);
            json.put(JK_CATEGORY_ID, categoryId);
            if (keyword != null) {
                json.put(JK_KEYWORD, keyword);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    /** 创建我的咨询回复JSON */
    public static String createRequestAdvisoryReply(int currentPage, int pageSize, int categoryId, int questionDate,
            int returnStatus) {
        JSONObject json = new JSONObject();
        try {
            json.put(JK_CURRENT_PAGE, currentPage);
            json.put(JK_PAGE_SIZE, pageSize);
            json.put(JK_CATEGORY_ID, categoryId);
            json.put(JK_QUESTIION_DATE, questionDate);
            json.put(JK_RETURN_STATUS, returnStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public static String cerateProductQuestionPublishJson(String goodsId, int categoryId, String questionContent) {
        JSONObject json = new JSONObject();
        try {
            json.put(JK_GOODS_NO, goodsId);
            json.put(JK_CATEGORY_ID, categoryId);
            json.put(JK_QUESTION_CONTENT, questionContent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public static boolean parseProductQuestionPublishResult(String json) {
        if (json == null || json.length() == 0) {
            return false;
        }
        JsonResult jsonResult = new JsonResult(json);
        if (!jsonResult.isSuccess()) {
            return false;
        }
        JSONObject jsContent = jsonResult.getJsContent();
        String result = jsContent.optString(JK_IS_SUMBIT);
        if (JV_YES.endsWith(result)) {
            return true;
        } else {
            return false;
        }
    }

    public static ArrayList<ProductQuestion> parseProductQuestionList(String json) {
        if (json == null || json.length() == 0) {
            return null;
        }
        JsonResult jsonResult = new JsonResult(json);

        if (!jsonResult.isSuccess()) {
            return null;
        }
        JSONObject jsContent = jsonResult.getJsContent();
        JSONArray array = jsContent.optJSONArray(JK_QUES_ARRAY);
        if (array == null) {
            return null;
        }
        ArrayList<ProductQuestion> list = new ArrayList<ProductQuestion>();
        for (int i = 0, size = array.length(); i < size; i++) {
            JSONObject item = array.optJSONObject(i);
            ProductQuestion question = new ProductQuestion();
            question.setProfileId(item.optString(JK_APPRAISE_NAME));
            question.setQuestionTime(item.optString(JK_QUESTION_TIME));
            String content = item.optString(JK_QUESTION_CONTENT);
            question.setQuestionContent(CommonUtility.ToDBC(content));
            String returArray = item.optString(JK_RETURN_ARRAY);
            question.setReturnArray(CommonUtility.ToDBC(returArray));
            question.setCategory(item.optString(JK_CATEGORY));
            if (item.optString(JK_RETURN_STATUS).equals(JV_YES)) {
                question.setReturnStatus(true);
            } else {
                question.setReturnStatus(false);
            }
            list.add(question);
        }
        return list;
    }

    private String profileId;
    private String questionTime;
    private String questionContent;
    private String returnArray;
    private String category;
    private boolean returnStatus;

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getQuestionTime() {
        return questionTime;
    }

    public void setQuestionTime(String questionTime) {
        this.questionTime = questionTime;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public String getReturnArray() {
        return returnArray;
    }

    public void setReturnArray(String returnArray) {
        this.returnArray = returnArray;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(boolean returnStatus) {
        this.returnStatus = returnStatus;
    }

}
