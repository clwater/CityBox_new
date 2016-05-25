package com.uit.uit2013.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.GrowEffect;
import com.uit.uit2013.R;
import com.uit.uit2013.model.Book;
import com.uit.uit2013.utils.PreferenceTool;
import com.uit.uit2013.utils.ScoreTool;
import com.uit.uit2013.utils.analysis.BookAnalysis;
import com.uit.uit2013.utils.analysis.ScoreAnalysis;
import com.uit.uit2013.utils.network.BookNetWork;
import com.uit.uit2013.utils.network.QueryScoreNetWork;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


/**
 * Created by yszsyf on 16/2/26.
 * 图书查询
 */
public class BookActivity  extends Activity implements View.OnClickListener {
    private Activity activity;
    private TextView titletv , updatatv;
    private EditText book_input;
    private TextView book_update;
    private JazzyListView book_listview;
    private String bookname;
    public static ProgressDialog pr;
    private Vector<Book> book_all = new Vector<Book>();
    private List<Map<String, Object>> data;
    private SimpleAdapter adapter_list;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.study_book);
        activity = this;

        createtitle();   //标题
        create();          //内容
    }

    private void create() {
        book_input = (EditText) findViewById(R.id.book_input);
        book_update = (TextView)findViewById(R.id.book_update);
        book_update.setOnClickListener(this);
        book_listview  = (JazzyListView) findViewById(R.id.book_listview);      //jazzylist为第三方库
        book_listview.setTransitionEffect( new GrowEffect());                   //滑动效果

    }

    private void createtitle() {
        titletv = (TextView)findViewById(R.id.titleTv);
        titletv.setText("图书查询");
        updatatv = (TextView)findViewById(R.id.updatatv);
        updatatv.setText("");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.book_update:
                querybook();
                break;
        }
    }

    private void querybook() {
        bookname = book_input.getText().toString().trim();

        pr = ProgressDialog.show(activity, null, "正在查找相关图书信息");
        BookTask task=new BookTask();       //异步
        task.execute();
    }

    private class BookTask extends AsyncTask<Void, Void, Void> {
        String resultweb = "";
        protected Void doInBackground(Void... params) {

                resultweb = BookNetWork.Book(bookname);         //得到查询结果

            return null;
        }
        protected void onProgressUpdate(Void... progress){}
        protected void onPostExecute(Void result){
            getsuccess(resultweb);

        }
    }

    private void getsuccess(String resultweb) {
        pr.dismiss();

        Log.d("bookbn" , "resultweb  " + resultweb );
        if (resultweb.equals("[]")){
            Toast.makeText(activity , "没有找到相关图书信息.",Toast.LENGTH_SHORT).show();
            resultweb = "{\"books\":[]}";
        }

        book_all = BookAnalysis.AnalysisBook(resultweb);            //解析得到的数据
        setbook();

        Log.d("book" , "result: " + resultweb);
        Log.d("book" , "result.size: " + resultweb.length());
    }

    private void setbook() {
        //不知道为什么  adapter_list.notifyDataSetChanged();  方法不好用 =-=  只能用这样的方法

        data = getData();
        adapter_list = new SimpleAdapter(this, data, R.layout.item_book,
                new String[]{"name", "author", "press", "date", "location" , "call"},
                new int[]{R.id.item_book_name, R.id.item_book_author, R.id.item_book_press, R.id.item_book_date, R.id.item_book_location , R.id.item_book_call});

        book_listview.setAdapter(adapter_list);
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i=0 ; i<book_all.size() ; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            Book book = book_all.get(i);
            map.put("name", book.getName());
            map.put("author", book.getAuthor());
            map.put("press", book.getPress());
            map.put("date", book.getDate());
            map.put("location", book.getLocation());
            map.put("call" , book.getCall());
            list.add(map);
        }
        return list;
    }
}
