package com.uit.uit2013.utils.analysis;

import android.util.Log;

import com.uit.uit2013.model.Book;
import com.uit.uit2013.model.Score;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Vector;

/**
 * Created by yszsyf on 16/2/26.
 */
public class BookAnalysis {

    public static Vector<Book> AnalysisBook(String result)  {
        Vector<Book> res = new Vector<Book>();

        JSONTokener jsonParser = new JSONTokener(result);
        JSONObject jsonObject = null;

        try {
            jsonObject = (JSONObject) jsonParser.nextValue();
            JSONArray grade = jsonObject.getJSONArray("books");

            for (int i = 0 ; i < grade.length() ; i++){
                Book r = new Book();
                JSONObject temp = grade.getJSONObject(i);
                r.setName(temp.getString("name").toString());
                r.setAuthor(temp.getString("author").toString());
                r.setPress(temp.getString("press").toString());
                r.setDate(temp.getString("date").toString());
                r.setLocation(temp.getString("location").toString());
                r.setCall(temp.getString("call".toString()));
                res.add(r);
            }

        } catch (JSONException e) {
            Log.d("AnalysisBook" , "AnalysisBook error");
        }

        return  res;
    }

}
