package com.uit.uit2013.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.uit.uit2013.model.DangKou;
import com.uit.uit2013.model.Restaurant;

import java.util.Vector;

/**
 * Created by soul on 2016/1/22.
 * 课表数据库控制类
 */
public class DKDateCtrl {
    public static DKDateHelp dbHelper;
    public  static  SQLiteDatabase db;

    public static void createSQL(Context context){
        //创建数据库
        dbHelper = new DKDateHelp(context, "citybox.db", null, 1);
        dbHelper.getWritableDatabase();
        db = dbHelper.getWritableDatabase();

    }
    //插入数据到数据库
    public static void UpdateRes(Context context , String name , String price , String dangkouid  ){
        createSQL(context);
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("dangkouid", dangkouid);
        db.insert("dangkou", null, values);


    }
    

    //查询信息
    public static Vector<DangKou> QueryRes(Context context , String id){
        createSQL(context);
        Vector res = new Vector<DangKou>();

        Cursor cursor = db.query("dangkou", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do{

                String name = cursor.getString(cursor.getColumnIndex("name"));
                String price = cursor.getString(cursor.getColumnIndex("price"));
                String dangkouid = cursor.getString(cursor.getColumnIndex("dangkouid"));

                if (dangkouid.equals( id ) ) {
                    DangKou r = new DangKou();
                    r.setName(name);
                    r.setPrice(price);
                    r.setDangkouid(dangkouid);
                    res.add(r);
                }


            } while (cursor.moveToNext());
        }
        cursor.close();
        return res;
    }


    public  static  void delete(Context context , String id){
        createSQL(context);
        String sql="DELETE FROM dangkou where dangkouid = " + id ;
        db.execSQL(sql);
    }

    public static void clear(Context context){
        createSQL(context);
        String sql="DELETE FROM dangkou";
        db.execSQL(sql);
    }
}
