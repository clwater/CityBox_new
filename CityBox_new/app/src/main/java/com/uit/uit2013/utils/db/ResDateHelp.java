package com.uit.uit2013.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by soul on 2016/1/22.
 * 课表数据库操作类
 */




public class ResDateHelp extends SQLiteOpenHelper{
    public static final String CREATE_RES = "create table res ("
            + "id text , "
            + "name text ,"
            + "loaction text ,"
            + "floor text ,"
            + "phone text"
            +")";
    //建表信息
    private Context mContext;


    public ResDateHelp(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL("DROP TABLE IF EXISTS res");
        db.execSQL(CREATE_RES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists res");
        onCreate(sqLiteDatabase);
    }
}
