package com.uit.uit2013.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by soul on 2016/1/22.
 * 课表数据库操作类
 */




public class OrderDateHelp extends SQLiteOpenHelper{
    public static final String CREATE_ORDER  = "create table my_order ("
            + "ordertype text ,"
            + "ordernum text , "
            + "ordermealman text , "
            + "sendmealman text ,"
            + "sendmanphone text ,"
            + "ordermenu text ,"
            + "orderstart text ,"
            + "ordersucces text ,"
            + "orderend text ,"
            + "orderstatu  text "
            +")";
    //建表信息
    private Context mContext;


    public OrderDateHelp(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL("DROP TABLE IF EXISTS my_order");
        db.execSQL(CREATE_ORDER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists my_order");
        onCreate(sqLiteDatabase);
    }
}
