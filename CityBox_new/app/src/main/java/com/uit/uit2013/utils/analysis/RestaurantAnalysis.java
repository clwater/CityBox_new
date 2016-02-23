package com.uit.uit2013.utils.analysis;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.uit.uit2013.model.Restaurant;
import com.uit.uit2013.ui.activity.LifeRestauranActivity;
import com.uit.uit2013.utils.db.ResDateCtrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Vector;

/**
 * Created by yszsyf on 16/1/31.
 */
public class RestaurantAnalysis {
    public static Vector<Restaurant> AnalysisRes(String result , Context context)  {
        Vector<Restaurant> res = new Vector<Restaurant>();
      //  ResDateCtrl.delete(context);
       // ResDateCtrl.createSQL(context);

        JSONTokener jsonParser = new JSONTokener(result);
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) jsonParser.nextValue();
            JSONArray yy = jsonObject.getJSONArray("yy");
          //  Log.d("bn" ,  "yy"+yy);
            JSONTokener data2 = new JSONTokener(String.valueOf(yy.get(0)));
            JSONObject child2 = (JSONObject) data2.nextValue();
            JSONArray st2 = child2.getJSONArray("data");
         //   Log.d("bn" ,  "st2"+st2);
            JSONTokener data3 = new JSONTokener(String.valueOf(yy.get(1)));
            JSONObject child3 = (JSONObject) data3.nextValue();
            JSONArray st3 = child3.getJSONArray("data");
          //  Log.d("bn" ,  "st3"+st3);

            int dangkou_num = st2.length() + st3.length() ;


           // Log.d("bn" , "dangkou_num:" + dangkou_num + "   st2: "  +st2.length()  +"  st3:  " +  st3.length() );

            for (int i = 0 ; i < st2.length() ; i++){
                Restaurant r = new Restaurant();
                JSONObject temp = st2.getJSONObject(i);
                Log.e("bn", "temp" + temp);
                r.setId(temp.getString("0").toString());
                r.setName(temp.getString("3").toString());
                r.setLocation(temp.getString("1").toString());
                r.setFloor(temp.getString("2").toString());
                r.setTelephone(temp.getString("4").toString());
                ResDateCtrl.UpdateRes(context ,r.getId() ,r.getName() ,r.getLocation() ,r.getFloor() ,r.getTelephone());
                res.add(r);
            }
            for (int i = st2.length() ; i < dangkou_num ; i ++){
                Restaurant r = new Restaurant();
                JSONObject temp = st3.getJSONObject(i - st2.length());
                Log.e("bn", "temp333" + temp);
                r.setId(temp.getString("0").toString());
                r.setName(temp.getString("3").toString());
                r.setLocation(temp.getString("1").toString());
                r.setFloor(temp.getString("2").toString());
                r.setTelephone(temp.getString("4").toString());
                ResDateCtrl.UpdateRes(context ,r.getId() ,r.getName() ,r.getLocation() ,r.getFloor() ,r.getTelephone());
                res.add(r);
            }

        } catch (JSONException e) {
            Log.d("AnalysisRes" , "AnalysisRes error");
        }

        //Log.d("bn" , "=-==-=" + res.get(3).getName());
        return  res;
    }
}
