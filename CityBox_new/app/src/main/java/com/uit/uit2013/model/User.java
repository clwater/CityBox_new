package com.uit.uit2013.model;

import com.uit.uit2013.utils.PreferenceTool;

/**
 * Created by soul on 2016/1/16.
 * 用户
 */
public class User {

    String name;
    String id;
    String pw;

    public static User getDefault() {
        User user = new User();
        user.setId(PreferenceTool.getid());
        user.setName(PreferenceTool.getusername());
        user.setPw_forP(PreferenceTool.getpw());
        return  user;
    }

    private void setPw_forP(String getpw) {
        this.pw = getpw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public static String des(String str)
    {
        char[] pBuf = str.toCharArray();
        int iLen = pBuf.length;

        int i;
        for(i=0;i<iLen;i++)
        {
            pBuf[i]^=iLen-i;
        }

        return String.valueOf(pBuf);

    }

}
