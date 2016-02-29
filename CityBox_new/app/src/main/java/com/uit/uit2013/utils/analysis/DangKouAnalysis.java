package com.uit.uit2013.utils.analysis;

import android.content.Context;
import android.util.Log;

import com.uit.uit2013.model.DangKou;
import com.uit.uit2013.model.Restaurant;
import com.uit.uit2013.utils.db.DKDateCtrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Vector;

/**
 * Created by yszsyf on 16/2/13.
 * 档口信息解析
 */
public class DangKouAnalysis {
    public static Vector<DangKou> AnalysisRes(String result , String id , Context context) throws JSONException {
        Vector<DangKou> res = new Vector<DangKou>();

        JSONTokener jsonParser = new JSONTokener(result);
        JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
        JSONArray yy = jsonObject.getJSONArray("data");

        int maxcaidan = yy.length();

        for (int i = 0; i < maxcaidan; i++) {
            DangKou d = new DangKou();
            JSONObject temp = yy.getJSONObject(i);

            d.setName(temp.getString("name"));
            d.setPrice(temp.getString("price"));
            d.setDangkouid(id);


            DKDateCtrl.UpdateRes(context , d.getName() , d.getPrice() , d.getDangkouid());
            res.add(d);
        }

        return  res;
    }
}
