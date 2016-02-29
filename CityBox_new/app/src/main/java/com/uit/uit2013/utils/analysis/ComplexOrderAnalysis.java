package com.uit.uit2013.utils.analysis;

import com.uit.uit2013.model.AllOrder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Vector;

/**
 * Created by yszsyf on 16/2/17.
 * 解析多个订单同时返回的时候
 */
public class ComplexOrderAnalysis {
    public static Vector<String>  ComplexOrder(String result) throws JSONException {
        Vector<String> all = new Vector<String>();

        JSONTokener jsonParser = new JSONTokener(result);
        JSONObject person = (JSONObject) jsonParser.nextValue();
        JSONArray order = person.getJSONArray("status");

        for (int i = 0 ; i < order.length() ; i++){
            String a = order.get(i).toString();
            all.add(a);
        }

        return all;
    }
}
