package com.gome.ecmall.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.gome.ecmall.bean.ActivityEntity;

public class ActivityRecommendDao {

    private DBOpenHelper dbHelper;

    public ActivityRecommendDao(Context context) {
        dbHelper = new DBOpenHelper(context);
    }

    /**
     * 添加活动推荐记录
     * 
     * @param recommend
     * @return
     */
    public boolean addActivityRecommend(ActivityEntity recommend) {
        SQLiteDatabase db = null;
        boolean inserted = false;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues(3);
            cv.put(DBOpenHelper.ACTIVITYHTMLURL, recommend.getActivityHtmlUrl());
            cv.put(DBOpenHelper.ACTIVITYID, recommend.getActivityId());
            cv.put(DBOpenHelper.ACTIVITYIMGURL, recommend.getActivityImgUrl());
            cv.put(DBOpenHelper.ACTIVITYNAME, recommend.getActivityName());
            cv.put(DBOpenHelper.ACTIVITYTYPE, recommend.getActivityType());
            cv.put(DBOpenHelper.STARTDATE, recommend.getStartDate());
            cv.put(DBOpenHelper.ENDDATE, recommend.getEndDate());
            if (db.insert(DBOpenHelper.TABLE_RECOMMEND_HISTORY, null, cv) > 0) {
                inserted = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return inserted;
    }

    public ArrayList<ActivityEntity> getAllActivityRecommends() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ArrayList<ActivityEntity> arrayList = new ArrayList<ActivityEntity>();
        try {
            db = dbHelper.getReadableDatabase();
            String sql = "SELECT * FROM " + DBOpenHelper.TABLE_RECOMMEND_HISTORY;
            cursor = db.rawQuery(sql, new String[] {});
            int activityhtmlurlindex = cursor.getColumnIndex(DBOpenHelper.ACTIVITYHTMLURL);
            int activityidindex = cursor.getColumnIndex(DBOpenHelper.ACTIVITYID);
            int activityimgurlindex = cursor.getColumnIndex(DBOpenHelper.ACTIVITYIMGURL);
            int activitynameindex = cursor.getColumnIndex(DBOpenHelper.ACTIVITYNAME);
            int activitytypeindex = cursor.getColumnIndex(DBOpenHelper.ACTIVITYTYPE);
            int startdateindex = cursor.getColumnIndex(DBOpenHelper.STARTDATE);
            int enddateindex = cursor.getColumnIndex(DBOpenHelper.ENDDATE);
            while (cursor.moveToNext()) {
                ActivityEntity recommend = new ActivityEntity();
                recommend.setActivityHtmlUrl(cursor.getString(activityhtmlurlindex));
                recommend.setActivityId(cursor.getString(activityidindex));
                recommend.setActivityImgUrl(cursor.getString(activityimgurlindex));
                recommend.setActivityName(cursor.getString(activitynameindex));
                recommend.setActivityType(cursor.getString(activitytypeindex));
                recommend.setStartDate(cursor.getString(startdateindex));
                recommend.setEndDate(cursor.getString(enddateindex));
                arrayList.add(recommend);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return arrayList;
    }

    /**
     * 移除所有的历史记录
     * 
     * @return
     */
    public int removeAllHistory() {
        SQLiteDatabase db = null;
        int effectedRows = 0;
        try {
            db = dbHelper.getWritableDatabase();
            effectedRows = db.delete(DBOpenHelper.TABLE_RECOMMEND_HISTORY, null, new String[] {});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return effectedRows;
    }

}
