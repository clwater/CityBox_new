package com.uit.uit2013.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.uit.uit2013.model.User;

/**
 * Created by soul on 2016/1/17.
 */

//用户工具类  用于储存登陆信息
public class PreferenceTool {
    public final static String USER_PW = "USER_RANDOM_UUID";//其实是密码
    public final static String USER_ID = "USER_ID";
    public final static String USER_NAME = "USER_NAME";
    public final static String IS_LOGIN = "IS_LOGIN";

    private static SharedPreferences  USERI_G = null;//读取信息
    private static SharedPreferences.Editor USERI_W = null; //保存信息

    public PreferenceTool(Context context){
        USERI_G = context.getSharedPreferences("login" , context.MODE_PRIVATE);
        USERI_W = context.getSharedPreferences("login" , context.MODE_PRIVATE).edit();
    }

    public static void  setPreferenceTool(String id , String pw ,String username , boolean loginstatu){
        USERI_W.putString("id" , id);
        USERI_W.putString("pw" , pw);
        USERI_W.putString("username" , username);
        USERI_W.putBoolean("loginstatu" , loginstatu);
        USERI_W.commit();
    }
    public static void setPreferenceTool(User user){
        USERI_W.putString("id" , user.getId());
        USERI_W.putString("pw" , user.getPw());
        USERI_W.putString("username" , user.getName());
        USERI_W.putBoolean("loginstatu" , true);
        USERI_W.commit();

    }
    public static String getid(){
        return USERI_G.getString("id" , "");
    }
    public static  String getpw(){
        return USERI_G.getString("pw" , "");
    }
    public static String getusername(){
        return USERI_G.getString("username" , "");
    }
    public static  boolean getloginstatu(){
        return USERI_G.getBoolean("loginstatu", false);
    }

    public static  void clear(){
        USERI_W.clear().commit();
    }

}
