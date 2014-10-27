package com.gome.ecmall.bean;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 查询库存地区列表和库存地存货信息
 * 
 * @author zhneyu.fang
 * @date 2012-8-2
 */
public class InventoryDivision implements JsonInterface, Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * 创建查询库存地区列表
     * 
     * @param divisionLevel
     *            区域级别
     * @param divisionCode
     *            区域代号
     * @return
     */
    public static String createRequestInventoryDivisionJson(int divisionLevel, String divisionCode) {
        JSONObject json = new JSONObject();
        try {
            json.put(JK_DIVISION_LEVEL, divisionLevel);
            if (divisionLevel != DIVISION_LEVEL_PROVINCE) {
                json.put(JK_PARENT_DIVISION_CODE, divisionCode);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    /**
     * 解析库存地区列表
     * 
     * @param json
     * @param divisonLevel
     * @return
     */
    public static ArrayList<InventoryDivision> parseInventoryDivisions(String json, int divisonLevel) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject jsContent = result.getJsContent();
        JSONArray array = jsContent.optJSONArray(JK_DIVISION_LIST);
        if (array == null) {
            return null;
        }
        ArrayList<InventoryDivision> arrayList = new ArrayList<InventoryDivision>();
        try {
            for (int i = 0, length = array.length(); i < length; i++) {
                JSONObject item = array.getJSONObject(i);
                InventoryDivision division = new InventoryDivision(divisonLevel);
                division.setDivisionName(item.getString(JK_DIVISION_NAME));
                division.setDivisionCode(item.getString(JK_DIVISION_CODE));
                arrayList.add(division);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /**
     * 创建请求城市库存信息的Json
     * 
     * @param cityId
     *            城市ID
     * @param skuId
     *            商品SKU ID
     * @param itemFlag
     *            商品标识(Y:商品;N:赠品)
     * @return
     */
    public static String createRequestInventoryInfoJson(String cityId, String skuId, String itemFlag, int level,
            String skuNo) {
        JSONObject json = new JSONObject();
        try {
            json.put(JK_CITY_ID, cityId);
            JSONArray array = new JSONArray();
            JSONObject goods = new JSONObject();
            goods.put(JK_SKU_ID, skuId);
            goods.put(JK_ITEM_FLAG, itemFlag);
            goods.put(JK_GOODS_NO, skuNo);
            array.put(goods);
            json.put(JK_GOODS_LIST, array);
            json.put(JK_LEVEL, level);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    /**
     * 解析库存地存货信息
     * 
     * @param json
     * @return
     */
    public static StockState parseCityInventoryInfo(String json) {
        JsonResult result = new JsonResult(json);
        if (!result.isSuccess()) {
            return null;
        }
        JSONObject content = result.getJsContent();
        StockState stockState = new StockState();
        try {
            stockState.setCityId(content.getString(JK_CITY_ID));
            JSONArray array = content.getJSONArray(JK_STOCK_LIST);
            JSONObject item = array.getJSONObject(0);
            stockState.setSkuId(item.getString(JK_SKU_ID));
            stockState.setRegionalPrice(item.optString(JK_REGIONAL_PRICE));
            stockState.setStockStateDesc(item.optString(JK_STOCK_STATEDESC));
            String state = item.getString(JK_STOCK_STATE);
            JSONArray productPromArray = item.optJSONArray(JK_PROMOTION_LIST);
            if (productPromArray != null) {
                ArrayList<Promotionable> skuPromotions = new ArrayList<Promotionable>();
                for (int i = 0, length = productPromArray.length(); i < length; i++) {
                    JSONObject promItem = productPromArray.getJSONObject(i);
                    Promotion productProm = new Promotion();
                    // Product级别的促销信息
                    productProm.setType(Integer.parseInt(promItem.getString(JK_PROMOTION_TYPE)));
                    productProm.setDesc(promItem.getString(JK_PROMOTION_DESC));
                    skuPromotions.add(productProm);
                }
                stockState.setSkuPromotions(skuPromotions);
            }

            if (StockState.STATE_AVAILABLE.equals(state)) {
                stockState.setStock(true);
            } else {
                stockState.setStock(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return stockState;
    }

    // 省级
    public static final int DIVISION_LEVEL_PROVINCE = 1;
    // 市级
    public static final int DIVISION_LEVEL_CITY = 2;
    // 县区级
    public static final int DIVISION_LEVEL_COUNTRY = 3;

    public static final String INVENTORY_ITEM_FLAG_PRODUCT = "Y";
    public static final String INVENTORY_ITEM_FLAG_GIFT = "N";
    private int divisionLevel;
    private InventoryDivision parentDivision;
    private String divisionCode;
    private String divisionName;
    private boolean isExpland;
    private ArrayList<InventoryDivision> nextDivsions;

    public InventoryDivision(int divisionLevel) {
        this.divisionLevel = divisionLevel;
        nextDivsions = new ArrayList<InventoryDivision>();
    }

    /**
     * 添加下一级区域,上一级区域只能添加下一级区域
     * 
     * @param division
     */
    public void addNextDivision(InventoryDivision division) {
        if (division.divisionLevel - divisionLevel == 1) {
            nextDivsions.add(division);
            division.setParentDivision(this);
        }
    }

    public void addNextDivisions(ArrayList<InventoryDivision> divisions) {
        if (divisions == null || divisions.size() == 0) {
            return;
        }
        for (InventoryDivision division : divisions) {
            if (division.getDivisionLevel() - this.divisionLevel == 1) {
                nextDivsions.add(division);
                division.setParentDivision(this);
            }
        }
    }

    /**
     * 获取下一级区域列表
     * 
     * @return
     */
    public ArrayList<InventoryDivision> getNextDivisions() {
        ArrayList<InventoryDivision> divisions = new ArrayList<InventoryDivision>(nextDivsions.size());
        for (InventoryDivision division : nextDivsions) {
            divisions.add(division);
        }
        return divisions;
    }

    public int getDivisionLevel() {
        return divisionLevel;
    }

    public InventoryDivision getParentDivision() {
        return parentDivision;
    }

    public void setParentDivision(InventoryDivision parentDivision) {
        this.parentDivision = parentDivision;
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

    public boolean isExpland() {
        return isExpland;
    }

    public void setExpland(boolean isExpland) {
        this.isExpland = isExpland;
    }

    /**
     * 库存状态/相关
     */
    public static class StockState {

        public static final String STATE_AVAILABLE = "Y";
        public static final String STATE_UNAVAILABLE = "N";
        private String cityId;
        private boolean hasStock;
        private String skuId;
        // 区域售价
        private String regionalPrice;
        private ArrayList<Promotionable> skuPromotions;

        /**
         * 无货 ; 现货 ; 预售
         */
        private String stockStateDesc;

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public boolean hasStock() {
            return hasStock;
        }

        public void setStock(boolean hasStock) {
            this.hasStock = hasStock;
        }

        public String getSkuId() {
            return skuId;
        }

        public void setSkuId(String skuId) {
            this.skuId = skuId;
        }

        public String getRegionalPrice() {
            return regionalPrice;
        }

        public void setRegionalPrice(String regionalPrice) {
            this.regionalPrice = regionalPrice;
        }

        public ArrayList<Promotionable> getSkuPromotions() {
            return skuPromotions;
        }

        public void setSkuPromotions(ArrayList<Promotionable> skuPromotions) {
            this.skuPromotions = skuPromotions;
        }

        public String getStockStateDesc() {
            return stockStateDesc;
        }

        public void setStockStateDesc(String stockStateDesc) {
            this.stockStateDesc = stockStateDesc;
        }

    }
}
