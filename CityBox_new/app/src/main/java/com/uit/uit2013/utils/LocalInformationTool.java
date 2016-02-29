package com.uit.uit2013.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.uit.uit2013.model.User;

/**
 * Created by yszsyf on 16/2/16.
 * 本地数据 订单时的个人信息
 */
public class LocalInformationTool {
    public final static String ADDRESS = "ADDRESS";//其实是密码
    public final static String PHONE = "PHONE";


    private static SharedPreferences LOCAL_G = null;//读取信息
    private static SharedPreferences.Editor LOCAL_W = null; //保存信息

    public LocalInformationTool(Context context){
        LOCAL_G = context.getSharedPreferences("localinformation" , context.MODE_PRIVATE);
        LOCAL_W = context.getSharedPreferences("localinformation" , context.MODE_PRIVATE).edit();
    }

    public static void  setLocalInformationTool(String address , String phone ){
        LOCAL_W.putString("address" , address);
        LOCAL_W.putString("phone" , phone);
        LOCAL_W.putBoolean("statu" , true);
        LOCAL_W.commit();
    }

    public static String getAddress(){
        if (LOCAL_G.getBoolean("statu" , false)) {
            return LOCAL_G.getString("address", "");
        }else {
            return "";
        }
    }
    public static  String getPhone(){
        if (LOCAL_G.getBoolean("statu" , false)) {
            return LOCAL_G.getString("phone", "");
        }else {
            return "";
        }
    }

    public static  void clear(){
        LOCAL_W.clear().commit();
    }
}
