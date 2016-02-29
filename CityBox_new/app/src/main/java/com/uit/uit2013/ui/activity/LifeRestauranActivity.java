package com.uit.uit2013.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.HelixEffect;
import com.uit.uit2013.R;
import com.uit.uit2013.model.LocalOrder;
import com.uit.uit2013.model.Restaurant;
import com.uit.uit2013.utils.db.ResDateCtrl;
import com.uit.uit2013.utils.network.RestaurantNetWork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


/**
 * Created by yszsyf on 16/1/30.
 * 食堂信息
 */
public class LifeRestauranActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private TextView life_title , back , life_updata , life_history;
    public static ProgressDialog pr;
    String restaurant = null;
    private  List<Map<String, Object>> data;
    Vector<Restaurant> res = new Vector<Restaurant>();
    private JazzyListView life_res_listview;

    private SimpleAdapter adapter;

 //   public RequestQueue mQueue;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.life_restaurant);

        createtitle();
        createlist();

    }

    private void createlist() {
        life_res_listview = (JazzyListView)findViewById(R.id.life_res_listview);
        life_res_listview.setTransitionEffect( new HelixEffect());

       try {
           res = ResDateCtrl.QueryRes(this);        //读取本地信息
       }catch (Exception exceptione){}

        data = getData();
        adapter = new SimpleAdapter(this,data,R.layout.item_restaurant,
                new String[]{"title","body"},
                new int[]{R.id.itme_res_title,R.id.itme_res_body});

        life_res_listview.setAdapter(adapter);
        life_res_listview.setOnItemClickListener(this);
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i=0 ; i<res.size() ; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
             map.put("title", ""+res.get(i).getName().toString());
              map.put("body", "" + res.get(i).getLocation().toString() + res.get(i).getFloor().toString());
            list.add(map);
        }
        return list;
    }

    private void createtitle() {
        life_title = (TextView) findViewById(R.id.life_title);
        life_title.setText("选择档口");
        back = (TextView) findViewById(R.id.life_back);
        back.setOnClickListener(this);
        life_updata = (TextView) findViewById(R.id.life_updata);
        life_updata.setOnClickListener(this);
        life_history = (TextView) findViewById(R.id.life_history);
        life_history.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.life_back:
                this.finish();
                break;
            case R.id.life_history:
                Intent history = new Intent();
                history.putExtra("type" , "" + LocalOrder.TYPE_ORDER);
                history.setClass(this , MyOrderActivity.class);
                startActivity(history);
                break;
            case R.id.life_updata:
                updata();
                break;
        }
    }

    private void updata()  {
        pr = ProgressDialog.show(LifeRestauranActivity.this, null, "获取食堂数据中......");
        ResDateCtrl.delete(this);
        CountingTask task=new CountingTask();
        task.execute();
    }
    private class CountingTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            RestaurantNetWork.getRestaurant(getApplicationContext());
            return null;
        }
        protected void onProgressUpdate(Void... progress){}
        protected void onPostExecute(Void result){
           getdateforwebsuccess();
        }
    }
    private  void getdateforwebsuccess(){
        pr.dismiss();
        res = ResDateCtrl.QueryRes(this);
        createlist();
        //Toast.makeText(LifeRestauranActivity.this , "更新完成,请重新进入." ,Toast.LENGTH_SHORT).show();
        //this.finish();
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                            long arg3) {
        String danghouid = String.valueOf(arg2);
        Intent next = new Intent();
        next.putExtra("dangkouid" , ""+danghouid);
        next.putExtra("dangkouname" , "" + res.get(arg2).getName());
        next.putExtra("dangkoulocation", "" + res.get(arg2).getLocation());
        next.setClass(this,LifeDangKouActivity.class);
        startActivity(next);

    }



}
