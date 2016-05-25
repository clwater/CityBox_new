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
 * 解析图书查询
 */
public class BookAnalysis {

    public static Vector<Book> AnalysisBook(String result)  {
        Vector<Book> res = new Vector<Book>();

        JSONTokener jsonParser = new JSONTokener(result);
        JSONObject jsonObject = null;

        try {
            jsonObject = (JSONObject) jsonParser.nextValue();
            JSONArray book = jsonObject.getJSONArray("book");

            Log.d("bookbn" , "book" + book);

            for (int i = 0 ; i < book.length() ; i++){
                Book r = new Book();
                JSONObject temp = book.getJSONObject(i);


                Log.d("bookbn" , "temp" + temp);


                r.setName(temp.getString("title").toString());


                Log.d("bookbn" , "temp.getString(\"title\").toString():  " + temp.getString("title").toString());

                r.setAuthor(temp.getString("auther").toString());

                Log.d("bookbn" , "temp.getString(\"author\").toString(): " + temp.getString("auther").toString());

                r.setPress(temp.getString("press").toString());
                r.setDate(temp.getString("time").toString());
                r.setLocation(temp.getString("place").toString());
                r.setCall(temp.getString("search".toString()));



                //[{"id":"序号","no":"","title":"书名","auther":"作者","press":"出版社","time":"出版时间","search":"藏书编号","place":"藏书位置","state":"图书状态"}]

                res.add(r);
            }

        } catch (JSONException e) {
            Log.d("AnalysisBook" , "AnalysisBook error");
        }

        return  res;
    }

}
