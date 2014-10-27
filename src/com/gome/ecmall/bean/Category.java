package com.gome.ecmall.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gome.ecmall.util.Constants;

/**
 * 商品分类【实体类】
 */
public class Category implements JsonInterface {

    public static final int CATEGORY_CLASS_FIRST = 1;
    public static final int CATEGORY_CLASS_SECOND = 2;
    public static final int CATEGORY_CLASS_THIRD = 3;
    public static final int CATEGORY_CLASS_SECOND_NONE = 5;
    public static final int CATEGORY_CLASS_THIRD_NONE = 6;

    /**
     * 创建分类请求json串
     * 
     * @param onlyFirstLevel
     *            只加载第一级分类(N = 默认加载所有分类 ; Y = 只加载第一级分类)
     * @param platform
     *            APP平台
     * @param screenWidth
     *            移动终端主屏分辨率(宽)
     * @param screenLength
     *            移动终端主屏分辨率(长)
     * @return
     */
    public static String buildAllCategoriesRequest(String onlyFirstLevel, String platform, int screenWidth,
            int screenLength) {
        try {
            JSONObject requestJson = new JSONObject();
            requestJson.put("onlyFirstLevel", onlyFirstLevel);
            requestJson.put("platform", platform);
            requestJson.put("screenWidth", screenWidth);
            requestJson.put("screenLength", screenLength);
            return requestJson.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Category> parseAllCategories(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        ArrayList<Category> totalList = new ArrayList<Category>();
        JSONObject content = result.getJsContent();
        try {
            JSONArray firstArray = content.getJSONArray(JK_FIRST_LEVEL_CATEGORIES);
            for (int f = 0, firstLength = firstArray.length(); f < firstLength; f++) {
                JSONObject first = firstArray.getJSONObject(f);
                Category firstCategory = new Category();
                firstCategory.setCategoryClass(CATEGORY_CLASS_FIRST);
                firstCategory.setTypeId(first.getString(JK_GOODS_TYPE_ID));
                firstCategory.setTypeName(first.getString(JK_GOODS_TYPE_NAME));
                firstCategory.setGoodsTypeLongName(first.optString(JK_GOODS_TYPE_LONG_NAME));
                String imageUrl = first.optString(JK_IMAGE_URL);
                if (imageUrl != null) {
                    // 服务器返回的是相对路径 需要加上绝对路径
                    imageUrl = Constants.SERVER_URL + "/" + imageUrl;
                }
                firstCategory.setImageUrl(imageUrl);
                JSONArray secondArray = first.optJSONArray(JK_GOODS_TYPE_LIST);
                if (secondArray != null) {
                    for (int s = 0, secondLength = secondArray.length(); s < secondLength; s++) {
                        JSONObject second = secondArray.getJSONObject(s);
                        Category secondCategory = new Category();
                        secondCategory.setCategoryClass(CATEGORY_CLASS_SECOND);
                        secondCategory.setTypeId(second.getString(JK_GOODS_TYPE_ID));
                        secondCategory.setTypeName(second.getString(JK_GOODS_TYPE_NAME));
                        JSONArray thirdArray = second.optJSONArray(JK_GOODS_TYPE_LIST);
                        if (thirdArray != null) {
                            for (int t = 0, thirdLength = thirdArray.length(); t < thirdLength; t++) {
                                JSONObject third = thirdArray.getJSONObject(t);
                                Category thirdCategory = new Category();
                                thirdCategory.setCategoryClass(CATEGORY_CLASS_THIRD);
                                thirdCategory.setTypeId(third.getString(JK_GOODS_TYPE_ID));
                                thirdCategory.setTypeName(third.getString(JK_GOODS_TYPE_NAME));
                                secondCategory.addChildCategory(thirdCategory);
                            }
                        }
                        firstCategory.addChildCategory(secondCategory);
                    }
                }
                totalList.add(firstCategory);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return totalList;
    }

    private String typeId;
    private String typeName;
    private String imageUrl;
    private int categoryClass;
    /** 是否可以展开，用于子列表的展开和关闭 */
    private boolean expand = false;
    private ArrayList<Category> childCategories;
    private String goodsTypeLongName;

    public String getGoodsTypeLongName() {
        return goodsTypeLongName;
    }

    public void setGoodsTypeLongName(String goodsTypeLongName) {
        this.goodsTypeLongName = goodsTypeLongName;
    }

    public Category() {
        childCategories = new ArrayList<Category>();
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void addChildCategory(Category category) {
        this.childCategories.add(category);
    }

    public ArrayList<Category> getChildCategories() {
        ArrayList<Category> arrayList = new ArrayList<Category>(childCategories);
        return arrayList;
    }

    /**
     * 收缩所有子分类
     */
    public void collapsedChildCategories() {
        for (int i = 0, size = childCategories.size(); i < size; i++) {
            Category category = childCategories.get(i);
            category.setExpand(false);
        }
    }

    public int getCategoryClass() {
        return categoryClass;
    }

    public void setCategoryClass(int categoryClass) {
        this.categoryClass = categoryClass;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Category)) {
            return false;
        }
        Category other = (Category) o;
        if (other.typeId.equals(typeId)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return typeId.hashCode();
    }

    /**
     * 将主分类集合转换为2个一组的集合
     * 
     * @param arrayList
     * @return
     */
    public static ArrayList<ArrayList<Category>> toCategories(ArrayList<Category> arrayList) {
        ArrayList<ArrayList<Category>> list = new ArrayList<ArrayList<Category>>();
        for (int i = 0,size = arrayList.size() ; i < size; i++) {
            ArrayList<Category> item = new ArrayList<Category>();
            item.add(arrayList.get(i++));
            if (i < arrayList.size()) {
                item.add(arrayList.get(i));
            }
            list.add(item);
        }
        return list;
    }

}
