package com.uit.uit2013.utils.analysis;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.uit.uit2013.utils.db.ScheduleDateCtrl;
import com.uit.uit2013.utils.db.ScheduleDateHelp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by soul on 2016/1/20.
 * 课表信息解析工具类
 */
public class ScheduleAnalysis {


    static String[] cs = new String[10];        //课表颜色 =-=  可以增加或者修改  记得下方使用的时候做取余操作
    static int index = 0;
    public boolean  getstatu(String rquest) throws JSONException {
        JSONTokener jsonParser = new JSONTokener(rquest);
        JSONObject person = (JSONObject) jsonParser.nextValue();
        String status = person.getString("status");
        if (status.equals("ok")) {
            return true;
        }else {
            return false;
        }
    }

    public static void AnalysisSchedule(String rquest, Context context) throws JSONException {

        ScheduleDateCtrl.createSQL(context);

        for (int i = 0 ; i < 10 ; i++){
            cs[i%10] = new String();
        }

        String sql;
        JSONTokener jsonParser = new JSONTokener(rquest);
        JSONObject person = (JSONObject) jsonParser.nextValue();
        String status = person.getString("status");


            JSONObject schedule = person.getJSONObject("schedule");
            JSONObject[] Week = new JSONObject[7];

            Week[0] = schedule.getJSONObject("Mon");
            Week[1] = schedule.getJSONObject("Tue");
            Week[2] = schedule.getJSONObject("Wed");
            Week[3] = schedule.getJSONObject("Thu");
            Week[4] = schedule.getJSONObject("Fri");
            Week[5] = schedule.getJSONObject("Sat");
            Week[6] = schedule.getJSONObject("Sat");
            JSONArray[][] lesson = new JSONArray[7][6];

            for (int i = 0; i < 7; i++) {
                lesson[i][0] = Week[i].getJSONArray("1-2");
                lesson[i][1] = Week[i].getJSONArray("3-4");
                lesson[i][2] = Week[i].getJSONArray("5-6");
                lesson[i][3] = Week[i].getJSONArray("7-8");
                lesson[i][4] = Week[i].getJSONArray("9-10");
                lesson[i][5] = Week[i].getJSONArray("11-12");
            }

        int allclass = 0 ;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                int length = lesson[i][j].length();

                for (int k = 0; k < length; k++) {
                    String temp_kebiao = String.valueOf(lesson[i][j].get(k));

                    if (temp_kebiao.length() != 4) {
                        JSONTokener temp = new JSONTokener(temp_kebiao);
                        JSONObject child = (JSONObject) temp.nextValue();
                        String class_name = child.getString("class_name");
                        String classrom = child.getString("classrom");
                        JSONArray j_weeks = child.getJSONArray("weeks");
                        String week = "";
                        int[] weeks = new int[j_weeks.length()];
                        for (int l = 0; l < j_weeks.length(); l++) {
                            week +=  Integer.valueOf(String.valueOf(j_weeks.get(l))) + ",";
                        }
                        String color = setColor(class_name);
                        ScheduleDateCtrl.UpdateSchedule(context , class_name ,classrom ,week , color ,i , j);
                        allclass++;
                    }
                }
            }

        }


    }

    public static String setColor(String class_name) {
        String[] s = inscolor();
        String c = null;
        boolean pd = true;
        int in = 0 ;
        for (int i = 0 ; i < index ; i++){
          if (class_name.equals(cs[i])){
              in = i ;
              pd = false;
              i = index;
          }
        }

        if (pd){
            cs[index % 10] = class_name;
            c = s[ index % 10];
            index++;
        }else{
            c = s[ in ];
        }

        return c;
    }

    public static String[] inscolor(){//课表颜色信息
        String[] s = { "#67A9BF" , "#8CCBB8" ,"#D5EAE1" ,"#A5D6D3" ,"#47BBE0" ,"#67A9BF" ,"#9DBDE3" ,"#CBE8FA" ,"#7DC8DB" ,"#43A6DD" };
        return s;
    }
}
