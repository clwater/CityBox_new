package com.uit.uit2013.utils.analysis;

import android.util.Log;

import com.uit.uit2013.model.User;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by soul on 2016/1/17.
 * 登录信息解析工具类
 */
public class LoginAnalysis {
    public boolean getstatu(String result) throws JSONException {
        JSONTokener jsonParser = new JSONTokener(result);
        JSONObject person = (JSONObject) jsonParser.nextValue();
        String status = person.getString("status");
        if (status.equals("ok")) {
            return true;
        }else {
            return false;
        }
    }

    public User getUser(String request ,String id , String pw) throws JSONException {
        User user = new User();
        JSONTokener jsonParser = new JSONTokener(request);
        JSONObject person = (JSONObject) jsonParser.nextValue();
        String man = person.getString("man");
        user.setName(man);
        user.setId(id);
        user.setPw(pw);

        return user;
    }

}
