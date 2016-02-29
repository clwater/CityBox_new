package com.uit.uit2013.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.GrowEffect;
import com.uit.uit2013.R;
import com.uit.uit2013.model.Score;
import com.uit.uit2013.utils.PreferenceTool;
import com.uit.uit2013.utils.ScoreTool;
import com.uit.uit2013.utils.analysis.ScoreAnalysis;
import com.uit.uit2013.utils.network.QueryScoreNetWork;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by yszsyf on 16/2/23.
 * 成绩查询
 */
public class ScoreActivity  extends Activity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private TextView titletv , updatatv;
    private Spinner choosetream;
    private TextView score_update;
    private ArrayAdapter<String> adapter_adapter;
    private static  String[] choosedate ;
   // private Vector<String> choosedate = new Vector<String >();
    private String choosetream_s;
    private JazzyListView score_listview;
    private Activity activity;
    private  List<Map<String, Object>> data;
    private String result;
    private Vector<Score> score_all = new Vector<Score>();
    private SimpleAdapter adapter_list;
    private int seletc_item = 0 ;
    public static ProgressDialog pr;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.study_score);
        activity = this;

        createtitle();
        createspinner();
        createlist();
        create();
    }

    private void createlist() {
        score_listview = (JazzyListView)findViewById(R.id.score_listview);
        score_listview.setTransitionEffect( new GrowEffect());

        PreferenceTool pt = new PreferenceTool(activity);
        ScoreTool st = new ScoreTool(activity);
        String trem = st.getTrem(PreferenceTool.getid() , "" + seletc_item , choosetream_s);
        Log.d("score" , "trem: " + trem);
        if (!trem.equals("null")) {
            score_all = ScoreAnalysis.AnalysisScore(trem);
            data = getData();
            adapter_list = new SimpleAdapter(this, data, R.layout.item_score,
                    new String[]{"name", "k", "p", "q", "x"},
                    new int[]{R.id.item_score_name, R.id.item_score_k, R.id.item_score_p, R.id.item_score_q, R.id.item_score_x});
            score_listview.setAdapter(adapter_list);
        }else {
            data = getData2();
            adapter_list = new SimpleAdapter(this, data, R.layout.item_score,
                    new String[]{"name", "k", "p", "q", "x"},
                    new int[]{R.id.item_score_name, R.id.item_score_k, R.id.item_score_p, R.id.item_score_q, R.id.item_score_x});
            score_listview.setAdapter(adapter_list);
        }
    }

    private void changetrem(){
        PreferenceTool pt = new PreferenceTool(activity);
        ScoreTool st = new ScoreTool(activity);
        String trem = st.getTrem(PreferenceTool.getid() , "" + seletc_item , choosetream_s);
        Log.d("score" , "trem: " + trem);
        if (!trem.equals("null")) {
            Log.d("score" , "trem != null");
            score_all = ScoreAnalysis.AnalysisScore(trem);
            data = getData();
            adapter_list.notifyDataSetChanged();
        }else {
            Log.d("score" , "trem == null");
            data.clear();
            adapter_list.notifyDataSetChanged();
        }
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i=0 ; i<score_all.size() ; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", ""+score_all.get(i).getName().toString());
            map.put("k", "课程成绩: "+score_all.get(i).getKscore().toString());
            map.put("p", "平时成绩: "+score_all.get(i).getPscore().toString());
            map.put("q", "期末成绩: "+score_all.get(i).getQscore().toString());
            map.put("x", "学分: "+score_all.get(i).getXuefen().toString());
            list.add(map);
        }
        return list;
    }

    private List<Map<String, Object>> getData2() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        return list;
    }

    private void create() {
        score_update = (TextView) findViewById(R.id.score_update);
        score_update.setOnClickListener(this);
    }


    public void onClick(View v) {
        switch (v.getId()){
            case R.id.score_update:
                try {
                    queryscore();
                } catch (IOException e) {}
                break;
        }
    }

    private void queryscore() throws IOException {
        pr = ProgressDialog.show(activity, null, "获取成绩中");
        GetScoreTask task=new GetScoreTask();
        task.execute();

    }


    private class GetScoreTask extends AsyncTask<Void, Void, Void> {
        String resultweb = "";
        protected Void doInBackground(Void... params) {
            try {
                PreferenceTool pt = new PreferenceTool(activity);
                result = QueryScoreNetWork.QueryScore(PreferenceTool.getid() , PreferenceTool.getpw() , choosetream_s );
                Log.d("score" , "result" + result);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onProgressUpdate(Void... progress){}
        protected void onPostExecute(Void result){
            Scorerestultanalysis();
        }
    }

    private void Scorerestultanalysis() {
        if(result.equals("{\"status\":\"School network connection failure\"}")){
            Toast.makeText(activity , "校网或网络问题,请重试,如果多次尝试都未有结果,请检查账号和密码正确." , Toast.LENGTH_LONG).show();
        }else if(result.equals("{\"status\":\"internal error\"}")){
            Toast.makeText(activity , "服务器异常" , Toast.LENGTH_LONG).show();
        }else {
            PreferenceTool pt = new PreferenceTool(activity);
            ScoreTool.setPreferenceTool(PreferenceTool.getid() ,""+ seletc_item, result);
            score_all = ScoreAnalysis.AnalysisScore(result);
            createlist();
        }
        pr.dismiss();
    }

    private void createspinner() {
        setchoosedate();

        choosetream = (Spinner) findViewById(R.id.score_spinner);
        adapter_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,choosedate);
        adapter_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choosetream.setAdapter(adapter_adapter);
        choosetream.setOnItemSelectedListener(this);
        choosetream.setVisibility(View.VISIBLE);

        choosetream_s = choosedate[0];
    }

    private void setchoosedate() {
        PreferenceTool pt = new PreferenceTool(this);
        String id = PreferenceTool.getid();
        String year = id.substring(0 , 4);

        String lastyear = year;
        String newyear = year;


        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        int getData_year = c.get(Calendar.YEAR);
        int getData_mouth = c.get(Calendar.MONTH);

        int s_year = Integer.valueOf(year);
        int between   ;
        if (getData_year  == s_year){
            between = 1 ;
        }else {
            between = getData_year - s_year ;
            between = between * 2;
            if (getData_mouth < 8){
                between = between -1 ;
            }
        }
        between++;
        choosedate = new String[between];

        Log.d("score" , "between" + between);



        int p;

        for (int i = 0 ; i < between ; ){
            lastyear = String.valueOf( (Integer.valueOf(lastyear) + 1 ));
            for (int j = 1 ; j < 3 ; j++ , i++){
                choosedate[i] = newyear + "-" + lastyear + "学年第" + j + "学期";
            }
            newyear = lastyear;
        }


    }

    private void createtitle() {
        titletv = (TextView)findViewById(R.id.titleTv);
        titletv.setText("成绩查询");
        updatatv = (TextView)findViewById(R.id.updatatv);
        updatatv.setText("");
    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        choosetream_s = choosedate[position];
        seletc_item = position;
        Log.d("score" , "choosetream_s: " + choosetream_s);
        createlist();
    }

    public void onNothingSelected(AdapterView<?> parent) {}



}
