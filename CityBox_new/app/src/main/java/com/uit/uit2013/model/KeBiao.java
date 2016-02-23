package com.uit.uit2013.model;

import android.util.Log;

import java.util.Vector;

/**
 * Created by soul on 2016/1/22.
 */
public class  KeBiao{
    private String class_name;
    private String classroom;
    private String weeks;

    private Vector week;
    private String colors;
    private int Num;
    private int INDEX_W = 0 , INDEX_T = 0 ;

    public int getINDEX_T() {
        return INDEX_T;
    }
    public void setINDEX_T(int INDEX_T) {
        this.INDEX_T = INDEX_T;
    }
    public int getINDEX_W() {
        return INDEX_W;
    }
    public void setINDEX_W(int INDEX_W) {
        this.INDEX_W = INDEX_W;
    }
    public String getClass_name() {
        return class_name;
    }
    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }
    public String getClassroom() {
        return classroom;
    }
    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
    public String getWeeks() {
        return weeks;
    }
    public void setWeeks(String weeks) {
        setWeek(weeks);
        this.weeks = weeks;
    }
    public String getColors() {
        return colors;
    }
    public void setColors(String colors) {
        this.colors = colors;
    }

    public Vector getWeek() {
        return week;
    }

    public void setWeek(String weeks) {
        String[] s = new String[20];
        s = weeks.split(",");

        Vector wk = new Vector();
        for (int i = 0 ; i < s.length ; i++){
            wk.add( Integer.valueOf(s[i]) );

        }

        this.week = wk;
    }


}
