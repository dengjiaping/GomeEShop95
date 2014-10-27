package com.gome.ecmall.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.gome.ecmall.util.BDebug;

public class GroupBuySearchHistoryDao {

    private DBOpenHelper helper;
    public static final String TAG = "GroupBuySearchHistoryDao";

    public GroupBuySearchHistoryDao(Context context) {
        helper = new DBOpenHelper(context);
    }

    public void addSearchHistory(String keyWords) {
        if (keyWords == null || keyWords.length() == 0) {
            return;
        }
        SQLiteDatabase db = null;
        try {
            boolean exist = false;
            db = helper.getWritableDatabase();
            String sql = "SELECT * FROM " + DBOpenHelper.TABLE_GROUPBUY_SEARCH_HISTORY + " where "
                    + DBOpenHelper.FILED_KEY_WORDS + "=?";
            Cursor cursor = db.rawQuery(sql, new String[] { keyWords });
            if (cursor.moveToNext()) {
                exist = true;
                //如果存在则更新时间
                ContentValues updatedValues = new ContentValues();
                updatedValues.put(DBOpenHelper.FIELD_TIME_STAMP, System.currentTimeMillis());
                String whereClause = DBOpenHelper.FILED_KEY_WORDS + "='" + keyWords+"'";
                db.update(DBOpenHelper.TABLE_GROUPBUY_SEARCH_HISTORY, updatedValues, whereClause, null);
            }
            cursor.close();
            if (!exist) {
                ContentValues cv = new ContentValues(2);
                cv.put(DBOpenHelper.FILED_KEY_WORDS, keyWords);
                cv.put(DBOpenHelper.FIELD_TIME_STAMP, System.currentTimeMillis());
                db.insert(DBOpenHelper.TABLE_GROUPBUY_SEARCH_HISTORY, null, cv);
            }
        } catch (SQLException e) {
            BDebug.e(TAG, "addSearchHistory: ERROR:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public ArrayList<String> getSearchHistoryList(int size) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ArrayList<String> list = new ArrayList<String>();
        try {
            String sql = "SELECT * FROM " + DBOpenHelper.TABLE_GROUPBUY_SEARCH_HISTORY + " order by "
                    + DBOpenHelper.FIELD_TIME_STAMP + " desc limit 0," + size;
            db = helper.getReadableDatabase();
            cursor = db.rawQuery(sql, new String[] {});
            int keywordsIndex = cursor.getColumnIndex(DBOpenHelper.FILED_KEY_WORDS);
            while (cursor.moveToNext()) {
                list.add(cursor.getString(keywordsIndex));
            }
        } catch (SQLException e) {
            BDebug.e(TAG, "getSearchHistoryList() ERROR:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return list;
    }

    public int removeAllHistory() {
        SQLiteDatabase db = null;
        int affectedRows = 0;
        try {
            db = helper.getWritableDatabase();
            affectedRows = db.delete(DBOpenHelper.TABLE_GROUPBUY_SEARCH_HISTORY, null, new String[] {});
        } catch (SQLException e) {
            BDebug.e(TAG, "removeAllHistory():" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return affectedRows;
    }

    public boolean removeHistoryBykeyWords(String keyWords) {
        if (TextUtils.isEmpty(keyWords))
            return false;
        boolean isdel = false;
        SQLiteDatabase db = null;
        int affectedRows = 0;
        try {
            db = helper.getWritableDatabase();
            affectedRows = db.delete(DBOpenHelper.TABLE_GROUPBUY_SEARCH_HISTORY, DBOpenHelper.FILED_KEY_WORDS + " = ? ",
                    new String[] { keyWords });
            if (affectedRows > 0) {
                isdel = true;
            }
        } catch (SQLException e) {
            BDebug.e(TAG, "deleteSearchHistory: ERROR:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return isdel;
    }
}
