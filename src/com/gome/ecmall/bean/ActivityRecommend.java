package com.gome.ecmall.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 【暂无调用】
 */
public class ActivityRecommend implements JsonInterface {

    public static ArrayList<ActivityRecommend> parseActivityRecommendList(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject content = result.getJsContent();
        JSONArray array = content.optJSONArray(JK_ACTIVITY_LIST);
        ArrayList<ActivityRecommend> arrayList = new ArrayList<ActivityRecommend>();
        try {

            if (array != null) {
                for (int i = 0, size = array.length(); i < size; i++) {
                    JSONObject item = array.getJSONObject(i);
                    ActivityRecommend recommend = new ActivityRecommend();
                    recommend.setType(item.getString(JK_TYPE));
                    recommend.setTypeId(item.getString(JK_TYPE_ID));
                    recommend.setTitleName(item.getString(JK_TITLE_NAME));
                    recommend.setPicUrl(item.getString(JK_PIC_URL));
                    arrayList.add(recommend);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    private String type;
    private String typeId;
    private String titleName;
    private String picUrl;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
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

}
