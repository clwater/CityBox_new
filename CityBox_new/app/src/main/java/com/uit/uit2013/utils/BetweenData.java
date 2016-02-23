package com.uit.uit2013.utils;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by yszsyf on 16/1/27.
 * 日期相关工具类
 */
public class BetweenData {
    public static int BaseYear = 2015;
    public static int BaseMouth = 9;
    public static int BaseDay = 7;
    //开学日期

    public static  long getBetweenDate(int year , int mouth , int day){
        //获取当前距开学多久
        long bd = 0 ;
        Calendar BaseCa = Calendar.getInstance();
        BaseCa.set(BaseYear,BaseMouth,BaseDay);
        long BaseN = BaseCa.getTimeInMillis();
        Calendar NowCa = Calendar.getInstance();
        NowCa.set(year,mouth,day);
        long NowN = NowCa.getTimeInMillis();
        bd = Math.abs( (BaseN - NowN) / 24 / 3600000 );
        return (int)bd;
    }

    public static int  getWeekNumber(int year , int mouth , int day){
        //当前第几周
        int week_number = 0 ;
        int bd = (int ) getBetweenDate(year , mouth , day);
        week_number  = bd / 7 ;

        return week_number;
    }

    public static int  getDayOfWeekNumber(int year , int mouth , int day){
        //当前周几
        int dayofweek_number = 0 ;
        int db = (int) getBetweenDate(year , mouth , day);
        dayofweek_number = db % 7;
        return dayofweek_number;
    }

}
