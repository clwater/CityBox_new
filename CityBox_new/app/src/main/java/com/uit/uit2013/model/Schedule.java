package com.uit.uit2013.model;

import android.content.Context;

import com.uit.uit2013.utils.db.ScheduleDateCtrl;

import java.util.Vector;

/**
 * Created by soul on 2016/1/20.
 */
public class Schedule {
    public  static Vector<KeBiao> schedule = null;
    public static Vector<KeBiao>  getins(Context context){
        schedule = new Vector<KeBiao>();
        schedule = ScheduleDateCtrl.QuerySchedule(context);
        return  schedule;
    }
}

