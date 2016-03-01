package com.uit.uit2013.ui.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.uit.uit2013.R;
import com.uit.uit2013.model.KeBiao;
import com.uit.uit2013.model.Schedule;
import com.uit.uit2013.utils.BetweenData;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * Created by yszsyf on 16/2/29.
 * 桌面小组件 =-=
 */
public class ScheduleWidgetProvider extends AppWidgetProvider{

    int[][] week =  new int[7][4];
    int[][] weekback = new int[7][4];
    int[] draw = new int[10];
    Vector<KeBiao> schedule;
    public  static Boolean pd = false;
    public  static int now_week = 1;

    //定义我们要发送的事件
    private final String broadCastString = "com.uit.uit2013.ScheduleWidget";
    private final String broadCastString_next = "com.uit.uit2013.ScheduleWidget_Next";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds)
    {
        super.onDeleted(context, appWidgetIds);
    }



    @Override
    public void onEnabled(Context context)
    {
        super.onEnabled(context);



        Context mcontext = null;
         Intent intent = null;

        //新建一个要发送的Intent
        mcontext = context;
        intent = new Intent();
        intent.setAction(broadCastString);

        //发送广播(由onReceive来接收)
        mcontext.sendBroadcast(intent);

    }

    private void createweek()  {
        draw[0] = R.drawable.weight_back_0;
        draw[1] = R.drawable.weight_back_1;
        draw[2] = R.drawable.weight_back_2;
        draw[3] = R.drawable.weight_back_3;
        draw[4] = R.drawable.weight_back_4;
        draw[5] = R.drawable.weight_back_5;
        draw[6] = R.drawable.weight_back_6;
        draw[7] = R.drawable.weight_back_7;
        draw[8] = R.drawable.weight_back_8;
        draw[9] = R.drawable.weight_back_9;

        Log.d("wedi" , "=-=2");
        week[0][0] = R.id.week_day00;
        week[0][1] = R.id.week_day01;
        week[0][2] = R.id.week_day02;
        week[0][3] = R.id.week_day03;

        week[1][0] = R.id.week_day10;
        week[1][1] = R.id.week_day11;
        week[1][2] = R.id.week_day12;
        week[1][3] = R.id.week_day13;


        week[2][0] = R.id.week_day20;
        week[2][1] = R.id.week_day21;
        week[2][2] = R.id.week_day22;
        week[2][3] = R.id.week_day23;

        week[3][0] = R.id.week_day30;
        week[3][1] = R.id.week_day31;
        week[3][2] = R.id.week_day32;
        week[3][3] = R.id.week_day33;

        week[4][0] = R.id.week_day40;
        week[4][1] = R.id.week_day41;
        week[4][2] = R.id.week_day42;
        week[4][3] = R.id.week_day43;

        week[5][0] = R.id.week_day50;
        week[5][1] = R.id.week_day51;
        week[5][2] = R.id.week_day52;
        week[5][3] = R.id.week_day53;

        week[6][0] = R.id.week_day60;
        week[6][1] = R.id.week_day61;
        week[6][2] = R.id.week_day62;
        week[6][3] = R.id.week_day63;


        weekback[0][0] = R.id.b_week_day00;
        weekback[0][1] = R.id.b_week_day01;
        weekback[0][2] = R.id.b_week_day02;
        weekback[0][3] = R.id.b_week_day03;

        weekback[1][0] = R.id.b_week_day10;
        weekback[1][1] = R.id.b_week_day11;
        weekback[1][2] = R.id.b_week_day12;
        weekback[1][3] = R.id.b_week_day13;


        weekback[2][0] = R.id.b_week_day20;
        weekback[2][1] = R.id.b_week_day21;
        weekback[2][2] = R.id.b_week_day22;
        weekback[2][3] = R.id.b_week_day23;

        weekback[3][0] = R.id.b_week_day30;
        weekback[3][1] = R.id.b_week_day31;
        weekback[3][2] = R.id.b_week_day32;
        weekback[3][3] = R.id.b_week_day33;

        weekback[4][0] = R.id.b_week_day40;
        weekback[4][1] = R.id.b_week_day41;
        weekback[4][2] = R.id.b_week_day42;
        weekback[4][3] = R.id.b_week_day43;

        weekback[5][0] = R.id.b_week_day50;
        weekback[5][1] = R.id.b_week_day51;
        weekback[5][2] = R.id.b_week_day52;
        weekback[5][3] = R.id.b_week_day53;

        weekback[6][0] = R.id.b_week_day60;
        weekback[6][1] = R.id.b_week_day61;
        weekback[6][2] = R.id.b_week_day62;
        weekback[6][3] = R.id.b_week_day63;




    }


    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }


    public void onReceive(Context context, Intent intent)
    {






        //Log.d("wedi" , "schedule"+ "" + schedule.get(0).getClass_name().toString());



        //当判断到是该事件发过来时， 我们就获取插件的界面， 然后将index自加后传入到textview中
        if(intent.getAction().equals(broadCastString))
        {


            createweek();

            schedule = Schedule.getins(context);


            Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
            int getData_year = c.get(Calendar.YEAR);
            int getData_mouth = c.get(Calendar.MONTH);
            int getData_day = c.get(Calendar.DATE);
            now_week = BetweenData.getWeekNumber(getData_year , getData_mouth + 1 ,getData_day);    //获取周数
            if (now_week < 0){
                Toast.makeText(context , "现在还没有开学,默认显示第一周的课表." , Toast.LENGTH_LONG).show();
                now_week = 1 ;
            }

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.schedule_widget_layout);
            rv.setTextViewText(R.id.weight_week , "第" + ( now_week + 1 ) + "周" );

            Intent next=new Intent(broadCastString_next);
            PendingIntent pendingnext= PendingIntent.getBroadcast(context, 0, next, 0);
            rv.setOnClickPendingIntent(R.id.week_next, pendingnext);

            rv.setImageViewResource(R.id.week_back , R.drawable.weight_back_un);
            rv.setImageViewResource(R.id.week_next , R.drawable.weight_next);


            rv.setTextViewText(R.id.week_day0 , "1");
            rv.setTextViewText(R.id.week_day1 , "2");
            rv.setTextViewText(R.id.week_day2 , "3");
            rv.setTextViewText(R.id.week_day3 , "4");
            rv.setTextViewText(R.id.week_day4 , "5");
            rv.setTextViewText(R.id.week_day5 , "6");
            rv.setTextViewText(R.id.week_day6 , "7");
            rv.setTextViewText(R.id.week_day7 , "8");


            if (schedule.size() > 0){
                pd = true;

                Log.d("wedi" , "pd_0: " + pd);
                rv.setTextViewText(week[0][0] , "1121");
                rv.setImageViewResource(weekback[0][0], R.drawable.testb);

                rv.setTextViewText(R.id.weight_week , "第" + now_week + "周" );

                for (int i = 0 ; i < 7 ; i++){
                    for (int j = 0 ; j < 4 ; j++){
                        rv.setTextViewText(week[i][j] ,"" );
                        rv.setImageViewResource(weekback[i][j] , R.drawable.touming);
                    }
                }


                for(int i = 0 ; i < 7 ; i++){
                    for (int j = 0 ; j < 4 ; j++){
                        for (int k = 0 ;k < schedule.size() ; k++){
                            KeBiao kb = schedule.get(k);
                            if ( (kb.getINDEX_T() == j ) && (kb.getINDEX_W() == i ) ){
                                Vector length = new Vector();
                                length = kb.getWeek();      //判断周数和位置是否正确
                                for (int m = 0 ; m <length.size() ; m++){
                                    if ( Integer.valueOf( length.get(m).toString() )== now_week){
                                        String classtext = "";
                                        classtext += kb.getClass_name() + "@"  + kb.getClassroom();
                                        rv.setTextViewText(week[i][j] ,classtext );

                                        int index = getcolor(kb.getColors());
                                        rv.setImageViewResource(weekback[i][j] , draw[index]);
                                    }
                                }
                            }
                        }
                    }
                }
            }else {
                pd = false;
                Log.d("wedi" , "pd_00: " + pd);
            }
        //将该界面显示到插件中
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context,ScheduleWidgetProvider.class);
            appWidgetManager.updateAppWidget(componentName, rv);

        }else  if(intent.getAction().equals(broadCastString_next)) {

            Log.d("wedi" , "pd_1: " + pd);
            createweek();

            schedule = Schedule.getins(context);

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.schedule_widget_layout);

            Intent back=new Intent(broadCastString);
            PendingIntent pendingback= PendingIntent.getBroadcast(context, 0, back, 0);
            rv.setOnClickPendingIntent(R.id.week_back, pendingback);

            rv.setImageViewResource(R.id.week_back , R.drawable.weight_back);
            rv.setImageViewResource(R.id.week_next , R.drawable.weight_next_un);


            for (int i = 0 ; i < 7 ; i++){
                for (int j = 0 ; j < 4 ; j++){
                    rv.setTextViewText(week[i][j] ,"" );
                    rv.setImageViewResource(weekback[i][j] , R.drawable.touming);
                }
            }

            rv.setTextViewText(R.id.week_day0 , "5");
            rv.setTextViewText(R.id.week_day1 , "6");
            rv.setTextViewText(R.id.week_day2 , "7");
            rv.setTextViewText(R.id.week_day3 , "8");
            rv.setTextViewText(R.id.week_day4 , "9");
            rv.setTextViewText(R.id.week_day5 , "10");
            rv.setTextViewText(R.id.week_day6 , "11");
            rv.setTextViewText(R.id.week_day7 , "12");


            if (pd == true){
                Log.d("wedi" , "aaaa");
                for(int i = 0 ; i < 7 ; i++){
                    for (int j = 0 ; j < 4 ; j++){
                        for (int k = 0 ;k < schedule.size() ; k++){
                            KeBiao kb = schedule.get(k);
                            //Log.d("wedi" , ":  " + );
                            Log.d("wedi" , "kb.getINDEX_T():  " +kb.getINDEX_T() );
                            Log.d("wedi" , "kb.getINDEX_W():  " +kb.getINDEX_W() );
                            if ( (kb.getINDEX_T() == ( j + 2 )) && (kb.getINDEX_W() == i ) ){
                                Vector length = new Vector();
                                length = kb.getWeek();      //判断周数和位置是否正确
                                for (int m = 0 ; m <length.size() ; m++){
                                    if ( Integer.valueOf( length.get(m).toString() )== now_week){
                                        String classtext = "";
                                        classtext += kb.getClass_name() + "@"  + kb.getClassroom();
                                        rv.setTextViewText(week[i][j] ,classtext );

                                        int index = getcolor(kb.getColors());
                                        rv.setImageViewResource(weekback[i][j] , draw[index]);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context,ScheduleWidgetProvider.class);
            appWidgetManager.updateAppWidget(componentName, rv);
        }


        super.onReceive(context, intent);
    }

    private int getcolor(String colors) {
        String[] s = { "#D7E1DC" , "#EBE5DE" ,"#EBD3CD" ,"#DCE0E5" ,"#E8D599" ,"#E7DADF" ,"#D1C9DB" ,"#CCE0E5" ,"#C9DDAA" ,"#EBCACA" };
        for (int i = 0 ; i < 10 ; i ++){
            if (colors.equals(s[i])){
                return i;
            }
        }

        return 100;

    }



}
