package com.gome.ecmall.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.gome.ecmall.util.BDebug;

public class PushHistoryDao {

    public static final String TAG = "PushHistoryDao";
    private DBOpenHelper helper;

    public PushHistoryDao(Context context) {
        helper = new DBOpenHelper(context);
    }

    /**
     * 添加推送记录
     * 
     * @param product
     */
    public void addPushHistory(String pushMessageId) {
        SQLiteDatabase db = null;
        boolean exist = false;
        try {
            // 添加记录是首先查看是否已存在
            db = helper.getWritableDatabase();
            String sql = "SELECT * FROM " + DBOpenHelper.TABLE_PUSH_HISTORY + " where " + DBOpenHelper.PUSH_MESSAGE_ID
                    + "=?";
            Cursor cursor = db.rawQuery(sql, new String[] { pushMessageId });
            if (cursor.moveToNext()) {
                exist = true;
            }
            cursor.close();
            if (!exist) {// 不存在则添加记录
                ContentValues cv = new ContentValues(2);
                cv.put(DBOpenHelper.PUSH_MESSAGE_ID, pushMessageId);
                cv.put(DBOpenHelper.PUSH_ARRIVED_TIME, System.currentTimeMillis());
                db.insert(DBOpenHelper.TABLE_PUSH_HISTORY, null, cv);
            }
        } catch (SQLException e) {
            BDebug.e(TAG, "addPushHistory() ERROR:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
            BDebug.d(TAG, "ExistRecord:" + exist);
        }
    }

    /**
     * 获得所有推送记录
     * 
     * @return
     */
    public ArrayList<String> getPartPushHistory() {
        SQLiteDatabase db = null;
        ArrayList<String> pushMessages = new ArrayList<String>();
        try {
            db = helper.getReadableDatabase();
            String sql = "SELECT * FROM " + DBOpenHelper.TABLE_PUSH_HISTORY;
            Cursor cursor = db.rawQuery(sql, new String[] {});
            int int_push_message_id = cursor.getColumnIndex(DBOpenHelper.PUSH_MESSAGE_ID);
            while (cursor.moveToNext()) {
                String push_messge_id = cursor.getString(int_push_message_id);
                pushMessages.add(push_messge_id);
            }
        } catch (SQLException e) {
            BDebug.e(TAG, "getAllProductHistory() ERROR:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return pushMessages;
    }

}
