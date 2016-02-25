package com.uit.uit2013.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.uit.uit2013.model.User;

/**
 * Created by yszsyf on 16/2/25.
 */
public class ScoreTool {
    

    private static SharedPreferences SCORE_G = null;//读取信息
    private static SharedPreferences.Editor SCORE_W = null; //保存信息

    public ScoreTool(Context context){
        SCORE_G = context.getSharedPreferences("score" , context.MODE_PRIVATE);
        SCORE_W = context.getSharedPreferences("score" , context.MODE_PRIVATE).edit();
    }

    public static void  setPreferenceTool(String id ,String position, String trem){
        String t = "trem" + id + position;
        SCORE_W.putString(t , trem);
        SCORE_W.commit();
    }
    public static String getTrem(String id ,String position, String trem){
        String t = "trem" + id + position;
        return SCORE_G.getString(t , "null");
    }


    public static  void clear(){
        SCORE_W.clear().commit();
    }


}
