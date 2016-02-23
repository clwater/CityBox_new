package com.uit.uit2013.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by soul on 2016/1/22.
 * 课表数据库操作类
 */
public class ScheduleDateHelp extends SQLiteOpenHelper{
    public static final String CREATE_SCHEDULE = "create table schedule ("
            + "class_name text , "
            + "classroom text ,"
            + "weeks text ,"
            + "colors text ,"
            + "INDEX_W text ,"
            + "INDEX_T text "
            +")";
    //建表信息
    private Context mContext;


    public ScheduleDateHelp(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SCHEDULE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists schedule");
        onCreate(sqLiteDatabase);
    }
}
