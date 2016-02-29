package com.uit.uit2013.ui.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
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

import com.easemob.EMCallBack;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.exceptions.EaseMobException;
import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.GrowEffect;
import com.twotoasters.jazzylistview.effects.HelixEffect;
import com.uit.uit2013.R;
import com.uit.uit2013.model.AllOrder;
import com.uit.uit2013.model.LocalOrder;
import com.uit.uit2013.ui.Dialog.OrderAcceptDialog;
import com.uit.uit2013.utils.PreferenceTool;
import com.uit.uit2013.utils.analysis.ComplexOrderAnalysis;
import com.uit.uit2013.utils.analysis.OrderAnalysis;
import com.uit.uit2013.utils.db.DKDateCtrl;
import com.uit.uit2013.utils.db.OrderDateCtrl;
import com.uit.uit2013.utils.db.ResDateCtrl;
import com.uit.uit2013.utils.network.AppleyOrderNetWork;
import com.uit.uit2013.utils.network.DangKouNetWork;
import com.uit.uit2013.utils.network.GetAllCanAcceptOrderNetWork;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by yszsyf on 16/2/16.
 * 订单接受
 */
public class LifeAcceptActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView accept_title , accept_back , accept_updata , accept_history;
    private AllOrder allorder;
    private Vector<AllOrder> vectior_allorder = new Vector<AllOrder>();
    private JazzyListView accept_listview;
    private  SimpleAdapter adapter;
    private  List<Map<String, Object>> data;
    public static ProgressDialog pr , apply_pr;
    private String com_name;
    private Activity activity;
    private Vector<String> unanalysicorder = new Vector<String>();          //所有为解析的数据 
    private int selectitem = 0;
    private boolean getindex = true , saveorder = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.life_accept);

        activity = this;
        OrderDateCtrl.createSQL(activity);

        imcreate();  //环信登陆
        createtitle();

        createlistview();       //设置listview

        getallneworder();       //得到所有的可接受的订单
    }

    private void createlistview() {
        accept_listview = (JazzyListView)findViewById(R.id.life_accept_listview);
        accept_listview.setTransitionEffect( new GrowEffect());

        data = getData();
        adapter = new SimpleAdapter(this,data,R.layout.item_accept,
                new String[]{"loaction","allprice" , "address" , "reward"},
                new int[]{ R.id.item_accept_location,R.id.item_accept_allprice , R.id.item_accept_address , R.id.item_accept_rewark} );


        accept_listview.setAdapter(adapter);
        accept_listview.setOnItemClickListener(this);
    }

    private List<Map<String,Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i=0 ; i<0 ; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("loaction", "位置:" + i);
            map.put("allprice" , "总价:" + i);
            map.put("address" , "地址:" + i);
            map.put("reward" , "打赏" + i);
            list.add(map);
        }
        return list;
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {

        OrderAcceptDialog dialog = new OrderAcceptDialog(this,vectior_allorder.get(arg2),new OrderAcceptDialog.OnCustomDialogListener() {
            @Override
            public void back(String name) {
                selectitem = arg2;
                Log.d("accept" , "back:" + name);
                if (!name.equals("canel") ){
                    com_name =name;
                    appelyorder(name);
                }

            }
        });
        dialog.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth() * 0.85); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }

    private void appelyorder(String ordernum) {
        apply_pr = ProgressDialog.show(LifeAcceptActivity.this, null, "正在抢单中,请保持网络畅通");
        saveorder = true ;
        CountingTask2 task2=new CountingTask2();
        task2.execute();
    }
    private class CountingTask2 extends AsyncTask<Void, Void, Void> {
        String resultweb_apply = "";
        protected Void doInBackground(Void... params) {


            try {
                PreferenceTool pt = new PreferenceTool(activity);
                resultweb_apply = AppleyOrderNetWork.AppleyOrder(com_name,PreferenceTool.getid());          //抢单
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("accept" , "resultweb_apply" + resultweb_apply);

            return null;
        }
        protected void onProgressUpdate(Void... progress){}
        protected void onPostExecute(Void result){
            if (resultweb_apply.equals("{\"status\":\"take success\"}")){
                //成功抢单后将数据保存到本地

                saveorder();
                saveorder = false;
                getindex = false;

                getallneworder();
                apply_pr.dismiss();

                Toast.makeText(activity , "抢单成功,请到历史记录中查看详细信息." , Toast.LENGTH_SHORT).show();

            }else if (resultweb_apply.equals("{\"status\":\"take failed\"}")){
                apply_pr.dismiss();
                Toast.makeText(activity , "抢单失败,你下手慢了,已经被别人抢先了" , Toast.LENGTH_SHORT).show();
            }else if(resultweb_apply.equals("{\"status\":\"internal error\"}")){
                apply_pr.dismiss();
                Toast.makeText(activity , "服务器异常,请稍后重试" , Toast.LENGTH_SHORT).show();
            }

        }
    }




    private void saveorder() {

        PreferenceTool pt = new PreferenceTool(activity);
        LocalOrder lo = new LocalOrder();
        lo.setOrdertype(LocalOrder.TYPE_SENG);
        lo.setOrdernum(vectior_allorder.get(selectitem).getOrdernum());
        lo.setOrdermealman(vectior_allorder.get(selectitem).getId());
        lo.setSendmealman(PreferenceTool.getid());
        lo.setSendmanphone("");
        lo.setOrderstart("");
        lo.setOrdersucces("");
        lo.setOrderend("");
        lo.setOrderstatu(LocalOrder.STATU_ACCEPT);

        String newsetOrdermenu = unanalysicorder.get(selectitem).replace('\"' , '@');           //数据库中不允许储存 " 将"替换为@存入

        lo.setOrdermenu(newsetOrdermenu);
        OrderDateCtrl.UpdateRes(activity , lo);



        //getallneworder();
    }

    private void createtitle() {
        accept_title = (TextView) findViewById(R.id.life_title);
        accept_title.setText("送餐");
        accept_back = (TextView) findViewById(R.id.life_back);
        accept_back.setOnClickListener(this);
        accept_updata = (TextView) findViewById(R.id.life_updata);
        accept_updata.setOnClickListener(this);
        accept_history = (TextView) findViewById(R.id.life_history);
        accept_history.setOnClickListener(this);

    }

    private void imcreate() {
        Activity appContext = this;
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        if (processAppName == null ||!processAppName.equalsIgnoreCase("com.uit.uit2013.ui.activity.LifeAcceptActivity")) {
            Log.e("accept", "enter the service process!");
        }
        EMChat.getInstance().init(this);
        EMChat.getInstance().setDebugMode(true);//在做打包混淆时，要关闭debug模式，避免消耗不必要的资源
        IntentFilter cmdIntentFilter = new IntentFilter(EMChatManager.getInstance().getCmdMessageBroadcastAction());
        appContext.registerReceiver(cmdMessageReceiver, cmdIntentFilter);
        EMChat.getInstance().setAppInited();
        loginim();

    }

    private BroadcastReceiver cmdMessageReceiver = new BroadcastReceiver() {
        //环信透出接受
        public void onReceive(Context context, Intent intent) {
            String msgId = intent.getStringExtra("msgid");
            EMMessage message = intent.getParcelableExtra("message");
            CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
            String aciton = cmdMsgBody.action;//获取自定义action
            //获取扩展属性

            String attr= null;
            try {
                attr = message.getStringAttribute("status");
                Log.d("accept" , attr);
                if (attr.equals("new")){
                    Log.d("accept" , aciton);
                    //新订单
                    getindex = false;
                    getneworder(aciton);

                }else if (attr.equals("taked")){
                    Log.d("accept" , aciton);
                    getindex = false;

                    if(saveorder == false) {
                        //防止抢单后没有保存完成事删除数据
                        getallneworder();
                    }


                    // removedongcanxinxi("" + aciton);
                    //removelist("" +aciton);
                }
            } catch (EaseMobException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    };



    private void getneworder(String aciton) throws JSONException {
        unanalysicorder.add(aciton);
        allorder = OrderAnalysis.orderanalysis(aciton);




        PreferenceTool pt = new PreferenceTool(this);
        if(!PreferenceTool.getid().equals(allorder.getId())){
            insertneworder(allorder);
            adapter.notifyDataSetChanged();
        }



    }

    private void insertneworder(AllOrder allorder) {
        vectior_allorder.add(allorder);
       // Log.d("history" , "vectior_allorder.get(0)" + vectior_allorder.get(0));
        Map<String, Object> map = new HashMap<String, Object>();
       //map.put("statu", "等你\n抢单");
        map.put("loaction", "位置:" + allorder.getOrdermeal().get(0).getSt());
        map.put("allprice" , "总价:" + allorder.getPrice());
        map.put("address" , "地址:" + allorder.getWhere());
        map.put("reward" , "打赏:" + allorder.getReward());

        data.add(map);



    }

    private void loginim() {
        PreferenceTool pt = new PreferenceTool(this);
        EMChatManager.getInstance().login(PreferenceTool.getid(),PreferenceTool.getid(),new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        EMGroupManager.getInstance().loadAllGroups();
                        EMChatManager.getInstance().loadAllConversations();
                        Log.e("accept", "登陆聊天服务器成功！");
                    }
                });
            }
            public void onProgress(int progress, String status) {}
            public void onError(int code, String message) {
                Log.e("accept", "登陆聊天服务器失败！");
            }
        });

    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {}
        }
        return processName;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.life_back:
                this.finish();
                break;
            case R.id.life_history:
                Intent history = new Intent();
                history.putExtra("type" , "" + LocalOrder.TYPE_SENG);
                history.setClass(this , MyOrderActivity.class);
                startActivity(history);
                break;
            case R.id.life_updata:
                getindex = true;
                getallneworder();
                break;
        }
    }

    private void getallneworder() {
        data.clear();
        vectior_allorder.clear();
        unanalysicorder.clear();
        adapter.notifyDataSetChanged();

        if (getindex){
            pr = ProgressDialog.show(LifeAcceptActivity.this, null, "获取可用订单中");
        }

        //DKDateCtrl.delete(this , dangkouid_s);
        CountingTask task=new CountingTask();
        task.execute();
    }
    private class CountingTask extends AsyncTask<Void, Void, Void> {
        String resultweb = "";
        protected Void doInBackground(Void... params) {


            try {
                resultweb = GetAllCanAcceptOrderNetWork.getAllCanAcceptOrder();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("accept" , "resultweb" + resultweb);

            return null;
        }
        protected void onProgressUpdate(Void... progress){}
        protected void onPostExecute(Void result){
            try {
                getdateforwebsuccess(resultweb);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getdateforwebsuccess(String result) throws JSONException {

        if (result.equals("{\"status\":\"null\"}") || result.equals("{\"status\":[null]}")){
            //pr.dismiss();
            if (getindex){
                pr.dismiss();
                getindex = true;
            }
            Toast.makeText(LifeAcceptActivity.this , "当前没有可获取的订单." , Toast.LENGTH_SHORT).show();
        }else if (result.equals("{\"status\":\"internal error\"}")){
            //pr.dismiss();
            if (getindex){
                pr.dismiss();
                getindex = true;
            }
            Toast.makeText(LifeAcceptActivity.this , "服务器出错了,请稍后重试." , Toast.LENGTH_SHORT).show();
        }else{
            Vector<String> getoldneworder = new Vector<String>();
            Log.d("accept" , "result: " +result);
            getoldneworder = ComplexOrderAnalysis.ComplexOrder(result);
            setComplexorder(getoldneworder);
            //pr.dismiss();
            if (getindex){
                pr.dismiss();
                getindex = true;
            }
        }


    }

    private void setComplexorder(Vector<String> getoldneworder) throws JSONException {
        for (int i = 0 ; i < getoldneworder.size() ; i++){
            AllOrder setComplexorder_allorder = new AllOrder();
            Log.d("accept" , "id: " + i  + "     " +getoldneworder.get(i).toString());
            if (!getoldneworder.get(i).toString().equals("null")) {

                setComplexorder_allorder = OrderAnalysis.orderanalysis(getoldneworder.get(i).toString());

                PreferenceTool pt = new PreferenceTool(this);
                if (!setComplexorder_allorder.getId().equals(PreferenceTool.getid())) {

                    unanalysicorder.add(getoldneworder.get(i).toString());


                    insertneworder(setComplexorder_allorder);
                }
            }
        }
        adapter.notifyDataSetChanged();

    }
}
