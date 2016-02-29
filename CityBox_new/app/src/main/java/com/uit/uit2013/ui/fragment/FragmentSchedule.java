package com.uit.uit2013.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.uit.uit2013.R;
import com.uit.uit2013.model.KeBiao;
import com.uit.uit2013.model.Schedule;
import com.uit.uit2013.model.User;
import com.uit.uit2013.ui.activity.LoginActivity;
import com.uit.uit2013.utils.BetweenData;
import com.uit.uit2013.utils.PreferenceTool;
import com.uit.uit2013.utils.analysis.ScheduleAnalysis;
import com.uit.uit2013.utils.db.ScheduleDateCtrl;

import org.json.JSONException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by soul on 2016/1/19.
 * 课表 由于自己写的=-=课表显示  有点多
 */
public class FragmentSchedule extends Fragment implements View.OnClickListener {
    private TextView tv,updatatv;
    private View view;
    private Activity activity;
    private int screenWidth ,screenHeight;
    private TextView[] bian;
    private Spinner choose;
    private String[] s_choose;
    private LinearLayout[] day;
    private TextView[][] day_t;
    private TextView[][] day_top;
    private LinearLayout top;
    private ArrayAdapter<String> choose_adapter;
    private TextView top_dq , return_this_day;
    public static ProgressDialog pr;
    public RequestQueue mQueue;
    private String request ;
    private int choose_week = 0 , now_week = 0;
    Context context;
    Vector<KeBiao>  schedule;
    public static Boolean loginpd = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_schedule, container, false);
        activity = getActivity();
        context = activity.getApplicationContext();

        genScreen();        //获取屏幕大小
        oncreate();         //实例化组建
        getData();          //获取日期信息

        PreferenceTool pt = new PreferenceTool(activity);

        getSchedule();
            //获取课表


        return view;
    }

    private void getData(){
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        int getData_year = c.get(Calendar.YEAR);
        int getData_mouth = c.get(Calendar.MONTH);
        int getData_day = c.get(Calendar.DATE);
        now_week = BetweenData.getWeekNumber(getData_year , getData_mouth + 1 ,getData_day);    //获取周数

        Log.d("datedate" , "now_week : " + now_week);
        if (now_week < 0){
            if (loginpd == true) {
                Toast.makeText(activity, "现在还没有开学,默认显示第一周的课表.", Toast.LENGTH_SHORT).show();
            }
            ScheduleDateCtrl.delete(activity);
            now_week = 0 ;
        }

        Log.d("-=" , "now_week:" + now_week);
        choose_week = now_week ;
        changeDayWidth(BetweenData.getDayOfWeekNumber(getData_year , getData_mouth + 1 ,getData_day) - 1 ); //获取周几
        int sweek = now_week + 1 ;
        String top_dq_s = "第" + sweek +"周" ;
        top_dq.setText(top_dq_s);
        choose.setSelection(choose_week,true);

    }

    private void getSchedule() {
        schedule = Schedule.getins(context);
        drawSchedule();//绘制课表

    }

    private void drawSchedule() {
        cleardrawSchedule();

        for (int i = 0 ; i < 7 ; i++){
            for (int j = 0 ; j < 6 ; j++){
                for (int k = 0 ; k < schedule.size() ; k++){
                    KeBiao kb = schedule.get(k);
                    if ( (kb.getINDEX_T() == j ) && (kb.getINDEX_W() == i ) ){
                        Vector length = new Vector();
                        length = kb.getWeek();      //判断周数和位置是否正确
                        for (int m = 0 ; m <length.size() ; m++){
                            if ( Integer.valueOf( length.get(m).toString() )== choose_week){
                                String classtext = "";
                                classtext += kb.getClass_name() + "@"  + kb.getClassroom();
                                day_t[i][j].setText(classtext);
                                day_t[i][j].setBackgroundColor(Color.parseColor(kb.getColors()));
                            }
                        }
                    }
                 }
            }
        }
    }

    private void cleardrawSchedule() {
        //清空课表
        for (int i = 0 ; i < 7 ; i++){
            for (int j = 0 ; j < 6 ; j++){
                day_t[i][j].setText("");
                day_t[i][j].setBackgroundColor((Color.argb(120, 255, 255, 255)));
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv = (TextView) getView().findViewById(R.id.titleTv);
        updatatv = (TextView)getView().findViewById(R.id.updatatv);
        updatatv.setText("更新数据");
        updatatv.setOnClickListener(this);
        tv.setText("课表");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.updatatv:
                updaataSchedule();
                break;
            case R.id.day0:                                 //0-6是课表的变换 点击的时候选中的变宽
                changeDayWidth(0);
                break;
            case R.id.day1:
                changeDayWidth(1);
                break;
            case R.id.day2:
                changeDayWidth(2);
                break;
            case R.id.day3:
                changeDayWidth(3);
                break;
            case R.id.day4:
                changeDayWidth(4);
                break;
            case R.id.day5:
                changeDayWidth(5);
                break;
            case R.id.day6:
                changeDayWidth(6);
                break;
            case R.id.return_this_day:
                choose_week = now_week;
                drawSchedule();
                choose.setSelection(choose_week,true);
                break;
        }
    }

    private void updaataSchedule() {//更新课表
        pr = ProgressDialog.show(activity, null, "更新数据中......");
        mQueue = Volley.newRequestQueue(activity);
        CountingTask task=new CountingTask();
        task.execute();
    }




    private class CountingTask extends AsyncTask<Void, Void, Void> {
        boolean pd = false;
        User user = User.getDefault();
        protected Void doInBackground(Void... params) {
            StringRequest postRequest = new StringRequest(Request.Method.POST ,  "http://120.27.53.146:5000/api/schedule",
                    new Response.Listener<String>() {
                        public void onResponse(String response) {
                            request = response;
                            ScheduleAnalysis sa = new ScheduleAnalysis();
                            try {
                                pd = sa.getstatu(response);
                                if (pd){
                                    ScheduleAnalysis.AnalysisSchedule(response ,context);
                                    getSchedule();
                                    pr.dismiss();
                                }else{
                                    pr.dismiss();
                                    Toast.makeText(activity , "账号或密码错误,请重试." ,Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {}


                        }
                    }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {}
            }) {
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", user.getId());
                    params.put("password", user.getPw());
                    params.put("action", "update");
                    return params;
                }
            };
            mQueue.add(postRequest);
            return null;
        }

        protected void onProgressUpdate(Void... progress){}
        protected void onPostExecute(Void result){}

    }


    private void genScreen() {
        //获取屏幕大小
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screenWidth  = display.getWidth();
        screenHeight  = display.getHeight();
        screenWidth = screenWidth / 10 * 13 ;
    }


    private void oncreate() {
        //实例化
        bian = new TextView[12];
        bian[0] = (TextView) view.findViewById(R.id.bian_0);
        bian[1] = (TextView) view.findViewById(R.id.bian_1);
        bian[2] = (TextView) view.findViewById(R.id.bian_2);
        bian[3] = (TextView) view.findViewById(R.id.bian_3);
        bian[4] = (TextView) view.findViewById(R.id.bian_4);
        bian[5] = (TextView) view.findViewById(R.id.bian_5);
        bian[6] = (TextView) view.findViewById(R.id.bian_6);
        bian[7] = (TextView) view.findViewById(R.id.bian_7);
        bian[8] = (TextView) view.findViewById(R.id.bian_8);
        bian[9] = (TextView) view.findViewById(R.id.bian_9);
        bian[10] = (TextView) view.findViewById(R.id.bian_10);
        bian[11] = (TextView) view.findViewById(R.id.bian_11);

        for (int i = 0 ; i < 12 ; i++){
            bian[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, screenHeight / 12 ));
        }

        //对应活动中的各个组件
        choose = (Spinner) view.findViewById(R.id.choose);
        s_choose  = new String[30];

        day = new LinearLayout[7];
        day[0] = (LinearLayout) view.findViewById(R.id.day0);
        day[1] = (LinearLayout) view.findViewById(R.id.day1);
        day[2] = (LinearLayout) view.findViewById(R.id.day2);
        day[3] = (LinearLayout) view.findViewById(R.id.day3);
        day[4] = (LinearLayout) view.findViewById(R.id.day4);
        day[5] = (LinearLayout) view.findViewById(R.id.day5);
        day[6] = (LinearLayout) view.findViewById(R.id.day6);

        for (int i = 0 ; i < 7 ; i++){
            day[i].setLayoutParams(new LinearLayout.LayoutParams( screenWidth / 8 ,  LinearLayout.LayoutParams.MATCH_PARENT));
        }

        day_t = new TextView[7][6];
        day_top = new TextView[7][2];

        day_top[0][0] = (TextView) view.findViewById(R.id.day0_top1);
        day_top[0][1] = (TextView) view.findViewById(R.id.day0_top2);
        day_top[1][0] = (TextView) view.findViewById(R.id.day1_top1);
        day_top[1][1] = (TextView) view.findViewById(R.id.day1_top2);
        day_top[2][0] = (TextView) view.findViewById(R.id.day2_top1);
        day_top[2][1] = (TextView) view.findViewById(R.id.day2_top2);
        day_top[3][0] = (TextView) view.findViewById(R.id.day3_top1);
        day_top[3][1] = (TextView) view.findViewById(R.id.day3_top2);
        day_top[4][0] = (TextView) view.findViewById(R.id.day4_top1);
        day_top[4][1] = (TextView) view.findViewById(R.id.day4_top2);
        day_top[5][0] = (TextView) view.findViewById(R.id.day5_top1);
        day_top[5][1] = (TextView) view.findViewById(R.id.day5_top2);
        day_top[6][0] = (TextView) view.findViewById(R.id.day6_top1);
        day_top[6][1] = (TextView) view.findViewById(R.id.day6_top2);



        day_t[0][0] = (TextView) view.findViewById(R.id.day0_0);
        day_t[0][1] = (TextView) view.findViewById(R.id.day0_1);
        day_t[0][2] = (TextView) view.findViewById(R.id.day0_2);
        day_t[0][3] = (TextView) view.findViewById(R.id.day0_3);
        day_t[0][4] = (TextView) view.findViewById(R.id.day0_4);
        day_t[0][5] = (TextView) view.findViewById(R.id.day0_5);

        day_t[1][0] = (TextView) view.findViewById(R.id.day1_0);
        day_t[1][1] = (TextView) view.findViewById(R.id.day1_1);
        day_t[1][2] = (TextView) view.findViewById(R.id.day1_2);
        day_t[1][3] = (TextView) view.findViewById(R.id.day1_3);
        day_t[1][4] = (TextView) view.findViewById(R.id.day1_4);
        day_t[1][5] = (TextView) view.findViewById(R.id.day1_5);

        day_t[2][0] = (TextView) view.findViewById(R.id.day2_0);
        day_t[2][1] = (TextView) view.findViewById(R.id.day2_1);
        day_t[2][2] = (TextView) view.findViewById(R.id.day2_2);
        day_t[2][3] = (TextView) view.findViewById(R.id.day2_3);
        day_t[2][4] = (TextView) view.findViewById(R.id.day2_4);
        day_t[2][5] = (TextView) view.findViewById(R.id.day2_5);

        day_t[3][0] = (TextView) view.findViewById(R.id.day3_0);
        day_t[3][1] = (TextView) view.findViewById(R.id.day3_1);
        day_t[3][2] = (TextView) view.findViewById(R.id.day3_2);
        day_t[3][3] = (TextView) view.findViewById(R.id.day3_3);
        day_t[3][4] = (TextView) view.findViewById(R.id.day3_4);
        day_t[3][5] = (TextView) view.findViewById(R.id.day3_5);

        day_t[4][0] = (TextView) view.findViewById(R.id.day4_0);
        day_t[4][1] = (TextView) view.findViewById(R.id.day4_1);
        day_t[4][2] = (TextView) view.findViewById(R.id.day4_2);
        day_t[4][3] = (TextView) view.findViewById(R.id.day4_3);
        day_t[4][4] = (TextView) view.findViewById(R.id.day4_4);
        day_t[4][5] = (TextView) view.findViewById(R.id.day4_5);

        day_t[5][0] = (TextView) view.findViewById(R.id.day5_0);
        day_t[5][1] = (TextView) view.findViewById(R.id.day5_1);
        day_t[5][2] = (TextView) view.findViewById(R.id.day5_2);
        day_t[5][3] = (TextView) view.findViewById(R.id.day5_3);
        day_t[5][4] = (TextView) view.findViewById(R.id.day5_4);
        day_t[5][5] = (TextView) view.findViewById(R.id.day5_5);

        day_t[6][0] = (TextView) view.findViewById(R.id.day6_0);
        day_t[6][1] = (TextView) view.findViewById(R.id.day6_1);
        day_t[6][2] = (TextView) view.findViewById(R.id.day6_2);
        day_t[6][3] = (TextView) view.findViewById(R.id.day6_3);
        day_t[6][4] = (TextView) view.findViewById(R.id.day6_4);
        day_t[6][5] = (TextView) view.findViewById(R.id.day6_5);

        for (int i = 0 ; i < 7 ; i++){
            for (int j = 0 ; j < 6 ; j++) {
                day_t[i][j].setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 8, screenHeight / 6 ));
            }
        }

        top = (LinearLayout)view.findViewById(R.id.top);
        top.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, screenHeight / 12 ));

        int i = 0;

        for( i = 0 ; i< 7 ; i++){
            day[i].setOnClickListener((View.OnClickListener) this);
        }

        for( i = 0 ; i < 30 ; i++){
            s_choose[i] = "第" + ( i+1) + "周";
        }

        choose_adapter = new ArrayAdapter<String>(activity,R.layout.drop_down_item_top,  s_choose);
        choose_adapter.setDropDownViewResource(R.layout.drop_down_item);
        choose.setAdapter(choose_adapter);
        choose.setOnItemSelectedListener(new SpinnerSelectedListener());
        choose.setVisibility(View.VISIBLE);

        top_dq = ( TextView ) view.findViewById(R.id.top_dq);
        return_this_day = (TextView) view.findViewById(R.id.return_this_day);
        return_this_day.setOnClickListener(this);
    }

        /*
        * 下拉菜单监听
        * */
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            choose_week = (int) arg3 + 1;
            drawSchedule();
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }




    private void changeDayWidth(int _day) {
        //组建宽度变化
        for(int  i = 0 ; i < 7 ; i++){
            if( i !=  _day) {
                day[i].setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 8, LinearLayout.LayoutParams.MATCH_PARENT ));

                day_top[i][0].setTextColor(Color.parseColor("#000000"));
                day_top[i][1].setBackgroundColor(Color.parseColor("#EFEAF0"));
                for ( int j = 0 ; j < 6 ; j++ ){
                    day_t[i][j].setTextSize(12);
                    day_t[i][j].setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 8 , screenHeight / 6));
                }
            }else{
                day[i].setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 16 * 3, LinearLayout.LayoutParams.MATCH_PARENT ));
                day_top[i][0].setTextColor(Color.parseColor("#34CED9"));
                day_top[i][1].setBackgroundColor(Color.parseColor("#34CED9"));
                for (int j = 0 ; j < 6 ; j++ ){
                    day_t[i][j].setTextSize(14);
                    day_t[i][j].setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 16 * 3, screenHeight / 6));
                }
            }
        }
    }

}