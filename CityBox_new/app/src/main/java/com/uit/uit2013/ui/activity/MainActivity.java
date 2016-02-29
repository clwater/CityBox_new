package com.uit.uit2013.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.uit.uit2013.R;
import com.uit.uit2013.ui.fragment.FragmentSchedule;
import com.uit.uit2013.utils.PreferenceTool;
import com.uit.uit2013.utils.db.OrderDateHelp;
import com.uit.uit2013.utils.db.ResDateHelp;
import com.uit.uit2013.utils.db.ScheduleDateCtrl;
import com.uit.uit2013.utils.db.ScheduleDateHelp;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;


public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private Fragment[] mFragments;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private TextView[] rb;
    //设置Fragment想换
    final int RIGHT = 0;
    final int LEFT = 1;
    private GestureDetector gestureDetector;
    private int CHANGE = 0 ;
    private boolean app_statu = false;
    //设置滑动判断相关
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Activity activity = this;

        //UmengUpdateAgent.update(this);
        UmengUpdateAgent.update(this);//友盟更新

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        app_statu = markapp();//判断是否第一次打开应用

        PreferenceTool pt = new PreferenceTool(this);

          boolean login_statu = pt.getloginstatu();        //判断是否登陆
           if(!login_statu){
                this.finish();
                startActivity(new Intent( this , LoginActivity.class));
               FragmentSchedule.loginpd = false;
         }else {
               FragmentSchedule.loginpd = true;
           }

        Context mcontext = null;
        Intent intent = null;
        mcontext = this;
        intent = new Intent();
        intent.setAction("com.uit.uit2013.ScheduleWidget");
        //发送广播(由onReceive来接收)
        mcontext.sendBroadcast(intent);

        gestureDetector = new GestureDetector(MainActivity.this,onGestureListener);//滑动监听


        if ( app_statu) {
            createtable();//用于创建相关数据库 =-=   在其他地方创建不好用  不好用 不好用.....
        }

        setcreate();
    }

    private boolean markapp() {
        boolean appopenstatu = false;


        SharedPreferences appinfo = this.getSharedPreferences("appinfo", MODE_PRIVATE);
        appopenstatu = appinfo.getBoolean("app" , false);

        if ( appopenstatu == false ) {
            SharedPreferences.Editor editor = appinfo.edit();
            editor.putBoolean("app", true);
            editor.commit();
            return true;
        }else {
            return false;
        }

    }

    private void createtable() {
        ResDateHelp dbHelper;
        OrderDateHelp orderDateHelp;
        SQLiteDatabase db;
        dbHelper = new ResDateHelp(this , "citybox.db", null, 1);
        orderDateHelp =  new OrderDateHelp(this , "citybox.db", null, 1);

        orderDateHelp.getWritableDatabase();
        dbHelper.getWritableDatabase();
        db = dbHelper.getWritableDatabase();
        String res = "create table res ( id text , name text , loaction text , floor text , phone text ) ";
        db.execSQL(res);

        String dangkou = "create table dangkou ("
                + "name text , "
                + "price text ,"
                + "dangkouid text "
                +")";
        db.execSQL(dangkou);

        db = orderDateHelp.getWritableDatabase();
        String order = "create table my_order ("
                + "ordertype text ,"
                + "ordernum text ,"
                + "ordermealman text ,"
                + "sendmealman text ,"
                + "sendmanphone text ,"
                + "ordermenu text ,"
                + "orderstart text ,"
                + "ordersucces text ,"
                + "orderend text ,"
                + "orderstatu  text "
                +")";
        db.execSQL(order);



//        String schedule = "create table schedule ("
//                + "class_name text , "
//                + "classroom text ,"
//                + "weeks text ,"
//                + "colors text ,"
//                + "INDEX_W text ,"
//                + "INDEX_T text "
//                +")";
//
//        db.execSQL(schedule);

        //ScheduleDateCtrl.createSQL(this);


    }


    private void setcreate() {//初始化相关 fragment
        mFragments = new Fragment[4];
        fragmentManager = getSupportFragmentManager();
        mFragments[0] = fragmentManager.findFragmentById(R.id.fragement_schedule);
        mFragments[1] = fragmentManager.findFragmentById(R.id.fragement_life);
        mFragments[2] = fragmentManager.findFragmentById(R.id.fragement_study);
        mFragments[3] = fragmentManager.findFragmentById(R.id.fragement_setting);

        fragmentTransaction = fragmentManager.beginTransaction()
                .hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]).hide(mFragments[3]);
        fragmentTransaction.show(mFragments[0]).commit();
        setFragmentIndicator();
    }
    private void setFragmentIndicator() {
        rb = new TextView[4];

        rb[0] = (TextView) findViewById(R.id.rbOne);
        rb[1] = (TextView) findViewById(R.id.rbTwo);
        rb[2] = (TextView) findViewById(R.id.rbThree);
        rb[3] = (TextView) findViewById(R.id.rbFour);

        rb[0].setOnClickListener(this);
        rb[1].setOnClickListener(this);
        rb[2].setOnClickListener(this);
        rb[3].setOnClickListener(this);

        clearf();

        rb[0].setBackgroundColor(Color.parseColor("#CEF4FF"));
        rb[0].setTextColor(Color.parseColor("#000000"));
    }
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rbOne:
                CHANGE = 0 ;
                changef(CHANGE);
                break;
            case R.id.rbTwo:
                CHANGE = 1 ;
                changef(CHANGE);
                break;
            case R.id.rbThree:
                CHANGE = 2 ;
                changef(CHANGE);
                break;
            case R.id.rbFour:
                CHANGE = 3 ;
                changef(CHANGE);
                break;
            default:
                break;
        }
    }

    public void clearf(){
        fragmentTransaction = fragmentManager.beginTransaction()
                .hide(mFragments[0]).hide(mFragments[1])
                .hide(mFragments[2]).hide(mFragments[3]);
        rb[0].setBackgroundColor(Color.parseColor("#96D7E9"));
        rb[1].setBackgroundColor(Color.parseColor("#96D7E9"));
        rb[2].setBackgroundColor(Color.parseColor("#96D7E9"));
        rb[3].setBackgroundColor(Color.parseColor("#96D7E9"));

        rb[0].setTextColor(Color.parseColor("#7B8693"));
        rb[1].setTextColor(Color.parseColor("#7B8693"));
        rb[2].setTextColor(Color.parseColor("#7B8693"));
        rb[3].setTextColor(Color.parseColor("#7B8693"));
    }
    public void changef(int i){//更改显示的 fragment
        clearf();
        fragmentTransaction.show(mFragments[i]).commit();
        rb[i].setBackgroundColor(Color.parseColor("#CEF4FF"));
        rb[i].setTextColor(Color.parseColor("#000000"));
    }

    private GestureDetector.OnGestureListener onGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    float x = e2.getX() - e1.getX();
                    float y = e2.getY() - e1.getY();
                    if (x > 0) {
                        doResult(RIGHT);
                    } else if (x < 0) {
                        doResult(LEFT);
                    }
                    return true;
                }
            };


    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public void doResult(int action) {
        switch (action) {
            case RIGHT:
                CHANGE = ( CHANGE + 4 - 1 ) % 4 ;
                changef(CHANGE);
                break;
            case LEFT:
                CHANGE = ( CHANGE + 4 + 1 ) % 4 ;
                changef(CHANGE);
                break;
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
