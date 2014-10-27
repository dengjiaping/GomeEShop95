package com.gome.ecmall.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.gome.ecmall.bean.BarcodeScan.BarcodeHistory;
import com.gome.ecmall.util.BDebug;

public class BarcodeScanHistoryDao {

    public static final String TAG = "BarcodeScanHistoryDao";
    private DBOpenHelper helper;

    public BarcodeScanHistoryDao(Context context) {
        helper = new DBOpenHelper(context);
    }

    /**
     * 添加商品的浏览记录
     * 
     * @param product
     */
    public void addBarcodeHistory(BarcodeHistory barcodeHistory) {
        SQLiteDatabase db = null;
        boolean exist = false;
        try {
            db = helper.getWritableDatabase();
            // 添加扫描记录
            ContentValues cv = new ContentValues(5);
            cv.put(DBOpenHelper.FIELD_BARCODE, barcodeHistory.getBarcode());
            cv.put(DBOpenHelper.FIELD_NUMBER, barcodeHistory.getNumber());
            cv.put(DBOpenHelper.FIELD_DATE, barcodeHistory.getDate());
            cv.put(DBOpenHelper.FIELD_IMGURL, barcodeHistory.getImgurl());
            cv.put(DBOpenHelper.FIELD_TIME_STAMP, System.currentTimeMillis());
            db.insert(DBOpenHelper.TABLE_BARCODE_HISTORY, null, cv);

        } catch (SQLException e) {
            BDebug.e(TAG, "addBarcodeHistory() ERROR:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
            BDebug.d(TAG, "ExistRecord:" + exist);
        }
    }

    /**
     * 更新指定图片路径imgPath的商品记录
     * 
     * @param imgPath
     * @return
     */

    public void updateBarcodeHistory(String imgPath) {

        SQLiteDatabase db = null;
        boolean exist = false;
        try {
            db = helper.getWritableDatabase();
            String sql = "update " + DBOpenHelper.TABLE_BARCODE_HISTORY + " set " + DBOpenHelper.FIELD_NUMBER
                    + " = 0 where " + DBOpenHelper.FIELD_IMGURL + " = '" + imgPath + "'";
            BDebug.e(TAG, sql);
            db.execSQL(sql);
        } catch (SQLException e) {
            BDebug.e(TAG, "addBarcodeHistory() ERROR:" + e.getMessage());
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
    public boolean deleteBarcodeHistory(int barcodeId) {
        SQLiteDatabase db = null;
        boolean isSuc = false;
        int rows = 0;
        try {
            db = helper.getWritableDatabase();
            String sql = DBOpenHelper.FIELD_BARCODE_ID + "=?";
            rows = db.delete(DBOpenHelper.TABLE_BARCODE_HISTORY, sql, new String[] { Integer.toString(barcodeId) });
            if (rows > 0) {
                isSuc = true;
            }
        } catch (SQLException e) {
            BDebug.e(TAG, "deleteBarcodeHistory() ERROR:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
            BDebug.d(TAG, "deleteBarcodeHistory() result:" + isSuc + "  effected:" + rows);
        }
        return isSuc;
    }

    public void removeAllBarcodeHistory() {
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.delete(DBOpenHelper.TABLE_BARCODE_HISTORY, null, new String[] {});
        } catch (SQLException e) {
            BDebug.e(TAG, "removeAllBarcodeHistory() ERROR:" + e.getMessage());
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
    public ArrayList<BarcodeHistory> getAllBarcodeHistory() {
        SQLiteDatabase db = null;
        ArrayList<BarcodeHistory> barcodeHistoryList = new ArrayList<BarcodeHistory>();
        try {
            db = helper.getReadableDatabase();
            String sql = "SELECT * FROM " + DBOpenHelper.TABLE_BARCODE_HISTORY + " order by " + DBOpenHelper.FIELD_DATE
                    + " desc";
            Cursor cursor = db.rawQuery(sql, new String[] {});
            int barcodeIdIndex = cursor.getColumnIndex(DBOpenHelper.FIELD_BARCODE_ID);
            int barcodeIndex = cursor.getColumnIndex(DBOpenHelper.FIELD_BARCODE);
            int numberIndex = cursor.getColumnIndex(DBOpenHelper.FIELD_NUMBER);
            int dateIndex = cursor.getColumnIndex(DBOpenHelper.FIELD_DATE);
            int imgurlIndex = cursor.getColumnIndex(DBOpenHelper.FIELD_IMGURL);
            while (cursor.moveToNext()) {
                BarcodeHistory barcodeHistory = new BarcodeHistory();
                barcodeHistory.setBarcodeId(cursor.getInt(barcodeIdIndex));
                barcodeHistory.setBarcode(cursor.getString(barcodeIndex));
                barcodeHistory.setNumber(cursor.getString(numberIndex));
                barcodeHistory.setDate(cursor.getString(dateIndex));
                barcodeHistory.setImgurl(cursor.getString(imgurlIndex));
                barcodeHistoryList.add(barcodeHistory);
            }
        } catch (SQLException e) {
            BDebug.e(TAG, "getAllBarcodeHistory() ERROR:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return barcodeHistoryList;
    }

}
