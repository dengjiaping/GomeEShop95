package com.gome.ecmall.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class InstallNumDao {
    private DBOpenHelper dbOpenHelper;

    public InstallNumDao(Context context) {
        this.dbOpenHelper = new DBOpenHelper(context);
    }

    public void save(String uuid, String versonno) {
        // 如果要对数据进行更改，就调用此方法得到用于操作数据库的实例,该方法以读和写方式打开数据库
        SQLiteDatabase db = null;
        try {
            db = dbOpenHelper.getWritableDatabase();
            db.execSQL("insert into instalapp (uuid,versionno) values(?,?)", new Object[] { uuid, versonno });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null)
                db.close();
        }
    }

    public String findUUID(String versionNo) {
        SQLiteDatabase db = null;
        String uuid = "";
        try {
            db = dbOpenHelper.getWritableDatabase();
            String sql = "select * from instalapp where versionno = ? ";
            Cursor cursor = db.rawQuery(sql, new String[] { versionNo });
            while (cursor.moveToNext()) {
                uuid = cursor.getString(cursor.getColumnIndex("uuid"));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null)
                db.close();
        }
        return uuid;
    }

}
