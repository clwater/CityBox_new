package com.uit.uit2013.utils.analysis;

import android.util.Log;

import com.uit.uit2013.model.LocalOrder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Vector;

/**
 * Created by yszsyf on 16/2/19.
 */
public class QueryOrderAnalysis  {
    public static LocalOrder QueryOrder(String result) throws JSONException {
        LocalOrder lo = new LocalOrder();

        JSONTokener jsonParser = new JSONTokener(result);
        JSONObject person = (JSONObject) jsonParser.nextValue();

        lo.setOrderstatu(  person.getString("orderstate") );
        lo.setSendmanphone( person.getString("sendmanphone"));


        Log.d("query_analysis" , "lo.getSendmanphone()" + lo.getSendmanphone() );

        return lo;
    }
}
