package com.gome.ecmall.bean;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 国美门店信息
 */
public class MoreGomeStore implements JsonInterface {

    private static final String JK_PARENTDIVISIONCODE = "parentDivisionCode";
    private static final String JK_COORDINATENAME = "coordinateName";
    private static final String JK_HOTDIVISIONLIST = "hotDivisionList";
    private static final String JK_STORELIST = "storeList";
    private static final String JK_DIVISIONLIST = "divisionList";
    private static final String JK_DIVISIONCODE = "divisionCode";
    private static final String JK_DIVISIONNAME = "divisionName";
    private static final String JK_STORENAME = "storeName";
    private static final String JK_STORETEL = "storeTel";
    private static final String JK_STOREADDRESS = "storeAddress";
    private static final String JK_STORELONGITUDE = "storeLongitude";
    private static final String JK_STORELATITUDE = "storeLatitude";
    private static final String JK_STOREDISTANCE = "storeDistance";
    private static final String JK_GPSLONGITUDE = "gpsLongitude";
    private static final String JK_GPSLATITUDE = "gpsLatitude";
    private static final String JK_SEARCHCITY = "keyword";

    /**
     * 城市列表请求数据
     * 
     * @param parentDivisionCode
     * @return
     */
    public static String createRequestGomeStoreCityListJson(String parentDivisionCode) {
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put(JK_PARENTDIVISIONCODE, parentDivisionCode);
            return requestJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String createRequestGomeStoreCityListJson(String parentDivisionCode, String coordinateName) {
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put(JK_PARENTDIVISIONCODE, parentDivisionCode);
            requestJson.put(JK_COORDINATENAME, coordinateName);
            return requestJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 门店城市列表
     * 
     * @param json
     * @return
     */
    public static MyMoreDivison parseGomeStoreDivison(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject content = result.getJsContent();
        MyMoreDivison myMoreDivision = new MyMoreDivison();
        try {
            JSONArray hotdivisionArray = content.optJSONArray(JK_HOTDIVISIONLIST);
            if (hotdivisionArray != null) {
                ArrayList<InventoryDivision> hotMoreDivisionList = new ArrayList<InventoryDivision>();
                for (int i = 0, length = hotdivisionArray.length(); i < length; i++) {
                    JSONObject hotJsonObj = hotdivisionArray.optJSONObject(i);
                    InventoryDivision moreDivision = new InventoryDivision(InventoryDivision.DIVISION_LEVEL_CITY);
                    moreDivision.setDivisionCode(hotJsonObj.optString(JK_DIVISIONCODE));
                    moreDivision.setDivisionName(hotJsonObj.optString(JK_DIVISIONNAME));
                    hotMoreDivisionList.add(moreDivision);
                }
                myMoreDivision.setHotArrayList(hotMoreDivisionList);
            }
            JSONArray divisionArray = content.optJSONArray(JK_DIVISIONLIST);
            if (divisionArray != null) {
                ArrayList<InventoryDivision> divisionList = new ArrayList<InventoryDivision>();
                for (int i = 0, length = divisionArray.length(); i < length; i++) {
                    JSONObject divJsonObj = divisionArray.optJSONObject(i);
                    InventoryDivision moreDivision = new InventoryDivision(InventoryDivision.DIVISION_LEVEL_PROVINCE);
                    moreDivision.setDivisionCode(divJsonObj.optString(JK_DIVISIONCODE));
                    moreDivision.setDivisionName(divJsonObj.optString(JK_DIVISIONNAME));
                    divisionList.add(moreDivision);
                }
                myMoreDivision.setDivisionList(divisionList);
            }
            return myMoreDivision;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 城市门店列表
     * 
     * @param json
     * @return
     */
    public static ArrayList<DivisionStore> parseDivisonGomeStore(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject content = result.getJsContent();
        try {
            JSONArray divisionArray = content.optJSONArray(JK_DIVISIONLIST);
            if (divisionArray != null) {
                ArrayList<DivisionStore> divisionList = new ArrayList<DivisionStore>();
                for (int i = 0, length = divisionArray.length(); i < length; i++) {
                    JSONObject divJsonObj = divisionArray.optJSONObject(i);
                    DivisionStore moreDivision = new DivisionStore();
                    moreDivision.setDivisionCode(divJsonObj.optString(JK_DIVISIONCODE));
                    moreDivision.setDivisionName(divJsonObj.optString(JK_DIVISIONNAME));
                    JSONArray storeArray = divJsonObj.optJSONArray(JK_STORELIST);
                    if (storeArray != null) {
                        for (int j = 0, storeLength = storeArray.length(); j < storeLength; j++) {
                            JSONObject storeJsonObj = storeArray.optJSONObject(j);
                            Store store = new Store();
                            store.setStoreName(storeJsonObj.optString(JK_STORENAME));
                            store.setStoreAddress(storeJsonObj.optString(JK_STOREADDRESS));
                            store.setStoreTel(storeJsonObj.optString(JK_STORETEL));
                            store.setStoreLongitude(storeJsonObj.optString(JK_STORELONGITUDE));
                            store.setStoreLatitude(storeJsonObj.optString(JK_STORELATITUDE));
                            moreDivision.addStore(store);
                        }
                    }
                    divisionList.add(moreDivision);
                }
                return divisionList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 附近门店列表
     * 
     * @param json
     * @return
     */
    public static ArrayList<Store> parseNearByGomeStore(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject content = result.getJsContent();
        try {
            JSONArray storeArray = content.optJSONArray(JK_STORELIST);
            if (storeArray != null) {
                ArrayList<Store> storeList = new ArrayList<Store>();
                for (int i = 0, length = storeArray.length(); i < length; i++) {
                    JSONObject storeJsonObj = storeArray.optJSONObject(i);
                    if (storeJsonObj != null) {
                        Store store = new Store();
                        store.setStoreName(storeJsonObj.optString(JK_STORENAME));
                        store.setStoreAddress(storeJsonObj.optString(JK_STOREADDRESS));
                        store.setStoreTel(storeJsonObj.optString(JK_STORETEL));
                        store.setStoreLongitude(storeJsonObj.optString(JK_STORELONGITUDE));
                        store.setStoreLatitude(storeJsonObj.optString(JK_STORELATITUDE));
                        store.setStoreDistance(storeJsonObj.optString(JK_STOREDISTANCE));
                        storeList.add(store);
                    }
                }
                return storeList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 附近城市列表请求数据
     * 
     * @param parentDivisionCode
     * @return
     */
    public static String createRequestNearByGomeStoreCityListJson(double gpsLongitude, double gpsLatitude,
            String coordinateName) {
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put(JK_GPSLONGITUDE, gpsLongitude);
            requestJson.put(JK_GPSLATITUDE, gpsLatitude);
            requestJson.put(JK_COORDINATENAME, coordinateName);
            return requestJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 城市搜索
     * 
     * @param keyword
     * @return
     */
    public static String createKeyWordInClude(String keyword) {
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put(JK_SEARCHCITY, keyword);
            return requestJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 区域分类【热门区域列表/区域列表】
     */
    public static class MyMoreDivison {

        private ArrayList<InventoryDivision> hotArrayList;
        private ArrayList<InventoryDivision> divisionList;

        public ArrayList<InventoryDivision> getHotArrayList() {
            return hotArrayList;
        }

        public void setHotArrayList(ArrayList<InventoryDivision> hotArrayList) {
            this.hotArrayList = hotArrayList;
        }

        public ArrayList<InventoryDivision> getDivisionList() {
            return divisionList;
        }

        public void setDivisionList(ArrayList<InventoryDivision> divisionList) {
            this.divisionList = divisionList;
        }

    }

    /**
     * 区域-门店列表【实体】
     */
    public static class DivisionStore {

        private String divisionCode;
        private String divisionName;
        private ArrayList<Store> storeList;

        public DivisionStore() {
            storeList = new ArrayList<Store>();
        }

        /**
         * 添加下一级(门店列表)
         * 
         * @param division
         */
        public void addStore(Store store) {
            storeList.add(store);
        }

        public void addStoreByList(ArrayList<Store> storeList) {
            if (storeList == null || storeList.size() == 0) {
                return;
            }
            for (Store store : storeList) {
                storeList.add(store);
            }
        }

        /**
         * 获取下一级门店列表
         * 
         * @return
         */
        public ArrayList<Store> getStoreList() {
            ArrayList<Store> myStoreList = new ArrayList<Store>(storeList.size());
            for (Store store : storeList) {
                myStoreList.add(store);
            }
            return myStoreList;
        }

        public String getDivisionCode() {
            return divisionCode;
        }

        public void setDivisionCode(String divisionCode) {
            this.divisionCode = divisionCode;
        }

        public String getDivisionName() {
            return divisionName;
        }

        public void setDivisionName(String divisionName) {
            this.divisionName = divisionName;
        }

    }

    /**
     * 门店信息【实体】【可继承】
     */
    public static class Store implements Serializable {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;
        private String storeName;
        private String storeTel;
        private String storeAddress;
        private String storeLongitude;
        private String storeLatitude;
        private String storeDistance;
        private boolean isCheck;
        private String storeId;

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean isCheck) {
            this.isCheck = isCheck;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getStoreTel() {
            return storeTel;
        }

        public void setStoreTel(String storeTel) {
            this.storeTel = storeTel;
        }

        public String getStoreAddress() {
            return storeAddress;
        }

        public void setStoreAddress(String storeAddress) {
            this.storeAddress = storeAddress;
        }

        public String getStoreLongitude() {
            return storeLongitude;
        }

        public void setStoreLongitude(String storeLongitude) {
            this.storeLongitude = storeLongitude;
        }

        public String getStoreLatitude() {
            return storeLatitude;
        }

        public void setStoreLatitude(String storeLatitude) {
            this.storeLatitude = storeLatitude;
        }

        public String getStoreDistance() {
            return storeDistance;
        }

        public void setStoreDistance(String storeDistance) {
            this.storeDistance = storeDistance;
        }

    }

}
