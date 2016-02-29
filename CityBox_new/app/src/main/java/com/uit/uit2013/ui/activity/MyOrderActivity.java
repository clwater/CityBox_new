package com.uit.uit2013.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.GrowEffect;
import com.uit.uit2013.R;
import com.uit.uit2013.adapter.OrderHistoryAdapter;
import com.uit.uit2013.adapter.OrderingAdapter;
import com.uit.uit2013.model.AllOrder;
import com.uit.uit2013.model.LocalOrder;
import com.uit.uit2013.ui.Dialog.OrderAcceptDialog;
import com.uit.uit2013.ui.Dialog.OrderHistoryDialog;
import com.uit.uit2013.utils.PreferenceTool;
import com.uit.uit2013.utils.analysis.OrderAnalysis;
import com.uit.uit2013.utils.analysis.QueryOrderAnalysis;
import com.uit.uit2013.utils.db.DKDateCtrl;
import com.uit.uit2013.utils.db.OrderDateCtrl;
import com.uit.uit2013.utils.network.DangKouNetWork;
import com.uit.uit2013.utils.network.QueryOrderNewWork;
import com.uit.uit2013.utils.network.SuccessOrderNewWork;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by yszsyf on 16/2/17.
 * 历史订单信息
 */
public class MyOrderActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private String history_type;
    private TextView back , titile ;
    private Activity activity;
    private SimpleAdapter adapter;
    public static ProgressDialog pr;
    private List<Map<String, Object>> data;
    private Vector<LocalOrder> lo = new Vector<LocalOrder>();
    private Vector<AllOrder> v_allorder = new Vector<AllOrder>();
    private JazzyListView myorder_listview;
    private String phone , result;
    private Context context;
    private int index = 0;
    private  OrderHistoryAdapter myadapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.myorder);
        context = this.getBaseContext();
        activity = this;
        history_type = gethistorytype();        //判断是订餐 还是送餐订单


        createtitle();

      //  getlocalorder();

        try {
            oncreate();
        } catch (JSONException e) {}
    }

    private void oncreate() throws JSONException {
        lo.clear();
        v_allorder.clear();
        getlocalorder();

        myorder_listview = (JazzyListView)findViewById(R.id.myorder_listview);
        myorder_listview.setTransitionEffect( new GrowEffect());

        data = getData();

        for (int i = 0 ; i < lo.size() ; i++){
            Log.d("history_waht" , "=-= :" + lo.get(i).getOrderstatu());
        }
//        adapter = new SimpleAdapter(this,data,R.layout.item_accept,
//                new String[]{"statu","loaction","allprice" , "address" , "reward"},
//                new int[]{R.id.item_accept_statu , R.id.item_accept_location,R.id.item_accept_allprice , R.id.item_accept_address , R.id.item_accept_rewark} );


        myadapter = new OrderHistoryAdapter(getBaseContext() , data);

        myorder_listview.setAdapter(myadapter);
        myorder_listview.setOnItemClickListener(this);
    }

    private List<Map<String,Object>> getData() throws JSONException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i=0 ; i< lo.size() ; i++) {
           // Log.d("history", "lo.get(i).getOrdernum()" + lo.get(i).getOrdernum());
           // Log.d("history" , "lo.get(i).getOrdermenu()" + lo.get(i).getOrdermenu());
            AllOrder allorder_get = OrderAnalysis.orderanalysis(lo.get(i).getOrdermenu() );
            v_allorder.add(allorder_get);
            Map<String, Object> map = new HashMap<String, Object>();
           // Log.d("his_statu" ,"lo.get(i).getOrderstatu()" + lo.get(i).getOrderstatu() );
            map.put("statu", lo.get(i).getOrderstatu());
            map.put("loaction", "位置:" + allorder_get.getOrdermeal().get(0).getSt());
            map.put("allprice" , "总价:" + allorder_get.getPrice());
            map.put("address" , "地址:" + allorder_get.getWhere());
            map.put("reward" , "打赏:" + allorder_get.getReward());
            list.add(map);
        }
        return list;
    }

    private String gethistorytype() {
        Intent intent=getIntent();
        String get_type=intent.getStringExtra("type");
        if (get_type.equals(LocalOrder.TYPE_SENG)){
            return "send";
        }else {
            return  "order";
        }
    }

    private void getlocalorder() {
        PreferenceTool pt = new PreferenceTool(activity);

        if (history_type.equals(LocalOrder.TYPE_SENG)) {
            lo = OrderDateCtrl.QueryRes(activity, LocalOrder.TYPE_SENG, PreferenceTool.getid());
        }else if (history_type.equals(LocalOrder.TYPE_ORDER)){
            lo = OrderDateCtrl.QueryRes(activity, LocalOrder.TYPE_ORDER, PreferenceTool.getid());
        }
    }

    private void createtitle() {
        back = (TextView)findViewById(R.id.life_back);
        back.setOnClickListener(this);
        titile = (TextView)findViewById(R.id.life_title);
        titile.setText("我的订单");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.life_back:
                this.finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        boolean type;
        if (history_type.equals(LocalOrder.TYPE_SENG)){
            type = false;
        }else {
            type = true;
        }

         index = i ;


     //   Log.d("query get set ", "phone =-= :  " + lo.get(0).getSendmanphone());

        OrderHistoryDialog dialog = new OrderHistoryDialog(this , v_allorder.get(i), type , lo.get(i).getSendmanphone() , activity ,lo.get(i).getOrderstatu(),
                new OrderHistoryDialog.OnCustomDialogListener(){
                    @Override
                    public void back(String name) {
                        Log.d("query_what" , "return name" + name);

                        if (name.equals(LocalOrder.STATU_WAIT)){
                            //dailog_order_get.setText("更新状态");

                            updateOrder();
                        }else if (name.equals(LocalOrder.STATU_ACCEPT)){
                            successOrder();
                        }else if (name.equals(LocalOrder.STATU_SUCCESS)){
                            //dailog_order_get.setText("确定");
                        }else if (name.equals(LocalOrder.STATU_ERROR)){
                            //dailog_order_get.setText("确定");
                        }else if (name.equals(LocalOrder.STATU_QUESTION)){
                            //dailog_order_get.setText("确定");
                        }
                    }
                }
                );


        dialog.show();


        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth() * 0.85); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }

    private void successOrder() {
        pr = ProgressDialog.show(MyOrderActivity.this, null, "确认收到订单");
        // DKDateCtrl.delete(this , dangkouid_s);
        CountingTasksuccessorder success=new CountingTasksuccessorder();
        success.execute();
    }
    private class CountingTasksuccessorder extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... params) {

            try {
                result = SuccessOrderNewWork.SuccessOrder(v_allorder.get(index).getOrdernum() , lo.get(index).getSendmealman());
                //Log.d("query" , "return result task" + result);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onProgressUpdate(Void... progress){}
        protected void onPostExecute(Void result){
            try {
                successorderforwebsuccess();
            } catch (JSONException e) {}
        }
    }

    private void successorderforwebsuccess() throws JSONException {


        if (result.equals("{\"status\":\"ok\"}")){
            OrderDateCtrl.updatestausuccess(context , v_allorder.get(index).getOrdernum());

            oncreate();
//            getlocalorder();
//            data.clear();
//            data = getData();
//            myadapter.notifyDataSetChanged();
        }else {
            Toast.makeText(this , "订单确认失败" , Toast.LENGTH_SHORT).show();
        }

        pr.dismiss();

    }


    private void updateOrder() {
        pr = ProgressDialog.show(MyOrderActivity.this, null, "查询订单状态");
       // DKDateCtrl.delete(this , dangkouid_s);
        CountingTaskQuerorder query=new CountingTaskQuerorder();
        query.execute();
    }


    private class CountingTaskQuerorder extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... params) {

            try {
                result = QueryOrderNewWork.QueryOrder(v_allorder.get(index).getOrdernum());
                Log.d("query" , "return result task" + result);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onProgressUpdate(Void... progress){}
        protected void onPostExecute(Void result){
            try {
                getdateforwebsuccess();
            } catch (JSONException e) {}
        }
    }
    private  void getdateforwebsuccess() throws JSONException {



        LocalOrder t_l = QueryOrderAnalysis.QueryOrder(result);

        String st = null;

        if (t_l.getOrderstatu().equals("n")){
            st = LocalOrder.STATU_WAIT;
        }else if (t_l.getOrderstatu().equals("s")){
            st = LocalOrder.STATU_ACCEPT;
        }else if (t_l.getOrderstatu().equals("o")){
            st = LocalOrder.STATU_SUCCESS;
        }else if (t_l.getOrderstatu().equals("f")){
            st = LocalOrder.STATU_ERROR;
        }

        phone = t_l.getSendmanphone();

        OrderDateCtrl.update(context , v_allorder.get(index).getOrdernum() , st ,phone);

        Log.d("query" , "getdateforwebsuccess");

//        getlocalorder();
//        data.clear();
//        data = getData();
//        myadapter.notifyDataSetChanged();


        oncreate();
        pr.dismiss();

    }
}
