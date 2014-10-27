package com.gome.ecmall.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.gome.ecmall.bean.Product;
import com.gome.ecmall.util.BDebug;

public class ProductHistoryDao {

    public static final String TAG = "ProductHistoryDao";
    private DBOpenHelper helper;

    public ProductHistoryDao(Context context) {
        helper = new DBOpenHelper(context);
    }

    /**
     * 添加商品的浏览记录
     * 
     * @param product
     */
    public void addProductHistory(Product product) {
        SQLiteDatabase db = null;
        boolean exist = false;
        try {
            // 添加记录是首先查看是否已存在
            db = helper.getWritableDatabase();
            String sql = "SELECT * FROM " + DBOpenHelper.TABLE_PRODUCT_HISTORY + " where "
                    + DBOpenHelper.FIELD_GOODS_NO + "=?";
            Cursor cursor = db.rawQuery(sql, new String[] { product.getGoodsNo() });
            if (cursor.moveToNext()) {
                exist = true;
            }
            cursor.close();
            if (!exist) {// 不存在则添加记录
                ContentValues cv = new ContentValues(4);
                cv.put(DBOpenHelper.FIELD_GOODS_NO, product.getGoodsNo());
                cv.put(DBOpenHelper.FIELD_GOODS_NAME, product.getGoodsName());
                cv.put(DBOpenHelper.FIELD_PRODUCT_URL, product.getImgListUrl());
                cv.put(DBOpenHelper.FIELD_PRODUCT_PRICE, product.getDisplayPrice());
                cv.put(DBOpenHelper.FIELD_TIME_STAMP, System.currentTimeMillis());
                db.insert(DBOpenHelper.TABLE_PRODUCT_HISTORY, null, cv);
            }
        } catch (SQLException e) {
            BDebug.e(TAG, "addProductHistory() ERROR:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
            BDebug.d(TAG, "ExistRecord:" + exist);
        }
    }

    /**
     * 删除指定goodsNo的商品记录
     * 
     * @param goodsNo
     * @return
     */
    public boolean deleteProductHistory(String goodsNo) {
        SQLiteDatabase db = null;
        boolean isSuc = false;
        int rows = 0;
        try {
            db = helper.getWritableDatabase();
            String sql = DBOpenHelper.FIELD_GOODS_NO + "=?";
            rows = db.delete(DBOpenHelper.TABLE_PRODUCT_HISTORY, sql, new String[] { goodsNo });
            if (rows > 0) {
                isSuc = true;
            }
        } catch (SQLException e) {
            BDebug.e(TAG, "deleteProductHistory() ERROR:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
            BDebug.d(TAG, "deleteProductHistory() result:" + isSuc + "  effected:" + rows);
        }
        return isSuc;
    }

    public void removeAllProductHistory() {
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.delete(DBOpenHelper.TABLE_PRODUCT_HISTORY, null, new String[] {});
        } catch (SQLException e) {
            BDebug.e(TAG, "deleteProductHistory() ERROR:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 获得商品的所有历史记录
     * 
     * @return
     */
    public ArrayList<Product> getAllProductHistory(int size) {
        SQLiteDatabase db = null;
        ArrayList<Product> products = new ArrayList<Product>();
        try {
            db = helper.getReadableDatabase();
            String sql = "SELECT * FROM " + DBOpenHelper.TABLE_PRODUCT_HISTORY + " order by "
                    + DBOpenHelper.FIELD_TIME_STAMP + " desc limit 0," + size;
            Cursor cursor = db.rawQuery(sql, new String[] {});
            int goodsNoIndex = cursor.getColumnIndex(DBOpenHelper.FIELD_GOODS_NO);
            int goodsNameIndex = cursor.getColumnIndex(DBOpenHelper.FIELD_GOODS_NAME);
            int goodsUrlIndex = cursor.getColumnIndex(DBOpenHelper.FIELD_PRODUCT_URL);
            int goodsPriceIndex = cursor.getColumnIndex(DBOpenHelper.FIELD_PRODUCT_PRICE);
            while (cursor.moveToNext()) {
                Product product = new Product();
                product.setGoodsNo(cursor.getString(goodsNoIndex));
                product.setGoodsName(cursor.getString(goodsNameIndex));
                product.setDisplayPrice(cursor.getString(goodsPriceIndex));
                BDebug.e(TAG, product.getDisplayPrice());
                product.setImgListUrl(cursor.getString(goodsUrlIndex));
                products.add(product);
            }
        } catch (SQLException e) {
            BDebug.e(TAG, "getAllProductHistory() ERROR:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return products;
    }

    /**
     * 获得商品的所有历史记录
     * 
     * @return
     */
    public ArrayList<Product> getPartProductHistory() {
        SQLiteDatabase db = null;
        ArrayList<Product> products = new ArrayList<Product>();
        try {
            db = helper.getReadableDatabase();
            String sql = "SELECT * FROM " + DBOpenHelper.TABLE_PRODUCT_HISTORY;
            Cursor cursor = db.rawQuery(sql, new String[] {});
            int goodsNoIndex = cursor.getColumnIndex(DBOpenHelper.FIELD_GOODS_NO);
            int goodsNameIndex = cursor.getColumnIndex(DBOpenHelper.FIELD_GOODS_NAME);
            int goodsUrlIndex = cursor.getColumnIndex(DBOpenHelper.FIELD_PRODUCT_URL);
            int goodsPriceIndex = cursor.getColumnIndex(DBOpenHelper.FIELD_PRODUCT_PRICE);
            while (cursor.moveToNext()) {
                Product product = new Product();
                product.setGoodsNo(cursor.getString(goodsNoIndex));
                product.setGoodsName(cursor.getString(goodsNameIndex));
                product.setDisplayPrice(cursor.getString(goodsPriceIndex));
                product.setImgListUrl(cursor.getString(goodsUrlIndex));
                products.add(product);
            }
        } catch (SQLException e) {
            BDebug.e(TAG, "getAllProductHistory() ERROR:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return products;
    }

}
