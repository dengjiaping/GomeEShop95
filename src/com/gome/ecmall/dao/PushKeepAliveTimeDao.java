package com.gome.ecmall.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.gome.ecmall.util.BDebug;
/**
 * 操作心跳时间的数据库，因用xml操作有事成功有事不成功，故改为数据库
 * @author liuyang-ds
 *
 */
public class PushKeepAliveTimeDao {

    public static final String TAG = "PushKeepAliveTimeDao";
    private DBOpenHelper helper;

    public PushKeepAliveTimeDao(Context context) {
        helper = new DBOpenHelper(context);
    }

    /**
     * 添加心跳时间
     * 
     * @param product
     */
    public void addKeepAlvieTime(String alvieTime,String isSendNetState ) {
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            ContentValues cv = new ContentValues(2);
            cv.put(DBOpenHelper.ALIVE_TIME, alvieTime);
            cv.put(DBOpenHelper.IS_SEND_NET_STATE, isSendNetState);
            db.insert(DBOpenHelper.TABLE_KEEP_ALIVE_TIME, null, cv);
        } catch (SQLException e) {
            BDebug.e(TAG, "addKeepAlvieTime() ERROR:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

   
/**
 * 删除心跳时间
 */
    public void removeKeepAlvieTime() {
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.delete(DBOpenHelper.TABLE_KEEP_ALIVE_TIME, null, new String[] {});
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
     * 获得心跳时间
     * 
     * @return
     */
    public String getkeepAliveTime() {
        SQLiteDatabase db = null;
        String keep_alive = "";
        try {
            db = helper.getReadableDatabase();
            String sql = "SELECT * FROM " + DBOpenHelper.TABLE_KEEP_ALIVE_TIME;
            Cursor cursor = db.rawQuery(sql, new String[] {});
            int aliveTimeIndex = cursor.getColumnIndex(DBOpenHelper.ALIVE_TIME);
            while (cursor.moveToNext()) {
                keep_alive = cursor.getString(aliveTimeIndex);
            }
            cursor.close();
        } catch (SQLException e) {
            BDebug.e(TAG, "getkeepAliveTime() ERROR:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return keep_alive;
    }
    /**
     * 获得是否发送网络状态
     * 
     * @return
     */
    public String getIsSendNetState() {
        SQLiteDatabase db = null;
        String isSendNetState = "";
        try {
            db = helper.getReadableDatabase();
            String sql = "SELECT * FROM " + DBOpenHelper.TABLE_KEEP_ALIVE_TIME;
            Cursor cursor = db.rawQuery(sql, new String[] {});
            int isSendNetStateIndex = cursor.getColumnIndex(DBOpenHelper.IS_SEND_NET_STATE);
            while (cursor.moveToNext()) {
                isSendNetState = cursor.getString(isSendNetStateIndex);
            }
            cursor.close();
        } catch (SQLException e) {
            BDebug.e(TAG, "getkeepAliveTime() ERROR:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return isSendNetState;
    }

}
