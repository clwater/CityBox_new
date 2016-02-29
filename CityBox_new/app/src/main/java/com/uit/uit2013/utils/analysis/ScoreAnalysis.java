package com.uit.uit2013.utils.analysis;

import android.content.Context;
import android.util.Log;

import com.uit.uit2013.model.Restaurant;
import com.uit.uit2013.model.Score;
import com.uit.uit2013.utils.db.ResDateCtrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Vector;

/**
 * Created by yszsyf on 16/2/25.
 * 成绩解析
 */
public class ScoreAnalysis {
    public static Vector<Score> AnalysisScore(String result)  {
        Vector<Score> res = new Vector<Score>();

        JSONTokener jsonParser = new JSONTokener(result);
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) jsonParser.nextValue();
            JSONArray grade = jsonObject.getJSONArray("grade");

            for (int i = 0 ; i < grade.length() ; i++){
                Score r = new Score();
                JSONObject temp = grade.getJSONObject(i);
                r.setName(temp.getString("课程名称").toString());
                r.setKscore(temp.getString("课程成绩").toString());
                r.setPscore(temp.getString("平时成绩").toString());
                r.setQscore(temp.getString("期末成绩").toString());
                r.setXuefen(temp.getString("学分").toString());
                res.add(r);
            }

        } catch (JSONException e) {
            Log.d("AnalysisRes" , "AnalysisRes error");
        }

        return  res;
    }
}
