package com.gome.ecmall.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "gome.db";
    public static final int VERSION = 19;
    public static final String TABLE_PRODUCT_HISTORY = "product_history";
    public static final String TABLE_SEARCH_HISTORY = "search_history";
    public static final String TABLE_RECOMMEND_HISTORY = "recommend_history";
    public static final String TABLE_BARCODE_HISTORY = "barcode_history";
    public static final String TABLE_PUSH_HISTORY = "push_history";
    public static final String TABLE_KEEP_ALIVE_TIME = "keep_alive_time";
    public static final String TABLE_GROUPBUY_SEARCH_HISTORY = "groupbuy_search_history";
    public static final String FIELD_GOODS_NO = "goods_no";
    public static final String FIELD_GOODS_NAME = "goods_name";
    public static final String FIELD_PRODUCT_URL = "product_url";
    public static final String FIELD_PRODUCT_PRICE = "product_price";
    public static final String FILED_KEY_WORDS = "keywords";
    public static final String FIELD_TIME_STAMP = "timestamp";

    // 首页活动专题
    // public static final String ACTIVITYLIST = "activityList";
    public static final String ACTIVITYIMGURL = "activityImgUrl";
    public static final String ACTIVITYNAME = "activityName";
    public static final String ACTIVITYID = "activityId";
    public static final String ACTIVITYTYPE = "activityType";
    public static final String ACTIVITYHTMLURL = "activityHtmlUrl";
    public static final String STARTDATE = "startDate";
    public static final String ENDDATE = "endDate";

    public static final String TABLE_INSTALAPP = "instalapp";
    public static final String FIELD_UUID = "uuid";
    public static final String FIELD_VERSIONNO = "versionno";
    public static final String FIELD_BARCODE_ID = "barcodeid";
    public static final String FIELD_BARCODE = "barcode";
    public static final String FIELD_NUMBER = "number";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_IMGURL = "imgurl";
    // 消息推送
    // public static final String PUSH_ID = "push_id";
    public static final String PUSH_MESSAGE_ID = "push_message_id";
    public static final String PUSH_ARRIVED_TIME = "push_arrived_time";
    public static final String ALIVE_TIME = "alive_time";
    public static final String IS_SEND_NET_STATE = "is_send_net_state";
    

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    public DBOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCT_HISTORY
                + "( _id INTEGER PRIMARY KEY AUTOINCREMENT," + FIELD_GOODS_NO + ", " + FIELD_GOODS_NAME + ", "
                + FIELD_PRODUCT_URL + ", " + FIELD_PRODUCT_PRICE + ", " + FIELD_TIME_STAMP + ")";
        String sql3 = "CREATE TABLE IF NOT EXISTS " + TABLE_SEARCH_HISTORY + "( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FILED_KEY_WORDS + ", " + FIELD_TIME_STAMP + ")";
        String sql4 = "CREATE TABLE IF NOT EXISTS " + TABLE_RECOMMEND_HISTORY
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," + ACTIVITYIMGURL + ", " + ACTIVITYNAME + ", " + ACTIVITYID
                + ", " + ACTIVITYTYPE + ", " + ACTIVITYHTMLURL + ", " + STARTDATE + ", " + ENDDATE + ")";
        String sql5 = "CREATE TABLE IF NOT EXISTS " + TABLE_INSTALAPP + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FIELD_UUID + "," + FIELD_VERSIONNO + ")";
        String sql6 = "CREATE TABLE IF NOT EXISTS " + TABLE_BARCODE_HISTORY + "(" + FIELD_BARCODE_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + FIELD_BARCODE + "," + FIELD_NUMBER + "," + FIELD_DATE + ","
                + FIELD_TIME_STAMP + "," + FIELD_IMGURL + ")";
        String sql7 = "CREATE TABLE IF NOT EXISTS " + TABLE_PUSH_HISTORY + "(_id"
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + PUSH_MESSAGE_ID + "," + PUSH_ARRIVED_TIME + ")";
        String sql8 = "CREATE TABLE IF NOT EXISTS " + TABLE_KEEP_ALIVE_TIME + "(_id" 
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + ALIVE_TIME +","+IS_SEND_NET_STATE+")";
        String sql9 = "CREATE TABLE IF NOT EXISTS " + TABLE_GROUPBUY_SEARCH_HISTORY + "( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FILED_KEY_WORDS + ", " + FIELD_TIME_STAMP + ")";
        db.execSQL(sql1);
        db.execSQL(sql3);
        db.execSQL(sql4);
        db.execSQL(sql5);
        db.execSQL(sql6);
        db.execSQL(sql7);
        db.execSQL(sql8);
        db.execSQL(sql9);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql2 = "DROP TABLE IF EXISTS " + TABLE_SEARCH_HISTORY;
        String sql3 = "DROP TABLE IF EXISTS " + TABLE_RECOMMEND_HISTORY;
        String sql4 = "DROP TABLE IF EXISTS " + TABLE_PRODUCT_HISTORY;
        String sql5 = "DROP TABLE IF EXISTS " + TABLE_INSTALAPP;
        String sql6 = "DROP TABLE IF EXISTS " + TABLE_BARCODE_HISTORY;
        String sql7 = "DROP TABLE IF EXISTS " + TABLE_PUSH_HISTORY;
        String sql8 = "DROP TABLE IF EXISTS " + TABLE_KEEP_ALIVE_TIME;
        String sql9 = "DROP TABLE IF EXISTS " + TABLE_GROUPBUY_SEARCH_HISTORY;
        db.execSQL(sql2);
        db.execSQL(sql3);
        db.execSQL(sql4);
        db.execSQL(sql5);
        db.execSQL(sql6);
        db.execSQL(sql7);
        db.execSQL(sql8);
        db.execSQL(sql9);
        onCreate(db);
    }

}
