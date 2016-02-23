package com.uit.uit2013.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.uit.uit2013.model.KeBiao;
import com.uit.uit2013.model.Restaurant;

import java.util.Vector;

/**
 * Created by soul on 2016/1/22.
 * 课表数据库控制类
 */
public class ResDateCtrl {
    public static ResDateHelp dbHelper;
    public  static  SQLiteDatabase db;

    public static void createSQL(Context context){
        //创建数据库
        dbHelper = new ResDateHelp(context, "citybox.db", null, 1);
        dbHelper.getWritableDatabase();
        db = dbHelper.getWritableDatabase();
        //Log.d("yanzheng" , "success2");
        //String a = "create table res ( id text , name text , loaction text , floor text , phone text ) ";
       // db.execSQL(a);

    }
    //插入数据到数据库
    public static void UpdateRes(Context context , String id , String name , String location , String floor , String phone  ){
        createSQL(context);
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        values.put("loaction", location);
        values.put("floor", floor);
        values.put("phone", phone);
        db.insert("res", null, values);


    }
    

    //查询课表信息
    public static Vector<Restaurant> QueryRes(Context context){
        createSQL(context);
        Vector res = new Vector<Restaurant>();
        Cursor cursor = db.query("res", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String loaction = cursor.getString(cursor.getColumnIndex("loaction"));
                String floor = cursor.getString(cursor.getColumnIndex("floor"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));

                Restaurant r = new Restaurant();
                r.setId(id);
                r.setName(name);
                r.setLocation(loaction);
                r.setFloor(floor);
                r.setTelephone(phone);

                res.add(r);


            } while (cursor.moveToNext());
        }
        cursor.close();
        return res;
    }


    public  static  void delete(Context context){
        createSQL(context);
        String sql="DELETE FROM res";
        db.execSQL(sql);
    }
}
