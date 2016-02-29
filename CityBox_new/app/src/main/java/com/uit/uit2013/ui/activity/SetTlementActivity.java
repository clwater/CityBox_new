package com.uit.uit2013.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.uit.uit2013.R;
import com.uit.uit2013.model.LocalOrder;
import com.uit.uit2013.model.SimpleOrder;
import com.uit.uit2013.utils.LocalInformationTool;
import com.uit.uit2013.utils.PreferenceTool;
import com.uit.uit2013.utils.db.OrderDateCtrl;
import com.uit.uit2013.utils.db.OrderDateHelp;
import com.uit.uit2013.utils.network.RestaurantNetWork;
import com.uit.uit2013.utils.network.SetTlementNetWork;

import java.io.IOException;
import java.util.Vector;

/**
 * Created by yszsyf on 16/2/15.
 * 确认订单
 */
public class SetTlementActivity extends Activity implements View.OnClickListener {

    private TextView st_title , back ;
    private TextView settlement_address , settlement_phone , settlement_reward , settlement_remarks , settlement_dkname , settlement_all ;
    private TextView settlement_allprice , settlement_finish;
    private String dkname , address , phone , dangkoulocation;
    private LocalOrder lo = new LocalOrder();
    private int select;
    private LocalInformationTool lit;
    public static Vector<SimpleOrder> order = new Vector<SimpleOrder>();
    public static double allprice = 0 ;
    private String psostresult= "";
    private  String ordernum , ordermenu;

    public static ProgressDialog pr;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.settlement);


        lit = new LocalInformationTool(this);

        dkname = getdkname();
        dangkoulocation = getdangkoulocation();
        getoeder();
        createtitle();
        crete();
        
        getlocalainformation();
        setlocalainformation();

        setallorder();



    }





    private LocalOrder getlo() {
        PreferenceTool pt = new PreferenceTool(this);

        LocalOrder l =new LocalOrder();
        l.setOrdertype(LocalOrder.TYPE_ORDER);
        l.setOrdernum(ordernum);
        l.setOrdermealman(PreferenceTool.getid());
        l.setSendmealman("");
        l.setSendmanphone("");
        String om = ordermenu.replace('\"' , '@');
        l.setOrdermenu(om);
        l.setOrderstart("");
        l.setOrdersucces("");
        l.setOrderend("");
        l.setOrderstatu(LocalOrder.STATU_WAIT);

        Log.d("his_statu_life" , "lo.getOrderstatu()_order" + l.getOrderstatu());
        OrderDateCtrl.UpdateRes(this , l);
        return l;
    }

    private String getdangkoulocation() {
        Intent intent = getIntent();
        String location;
        location  = intent.getStringExtra("dangkoulocation");
        return location;
    }

    private void setallorder() {
        String setallorder_all = "";
        for (int i = 0 ; i <order.size() ; i++){
            if (order.get(i).getNumber() > 0) {
                setallorder_all += order.get(i).getName();
                for (int j = 0; j + order.get(i).getName().length() < 12; j++) {
                    setallorder_all += "    ";
                }
                setallorder_all += "x";

                String temp_number = String.valueOf(order.get(i).getNumber());
                setallorder_all += " " + temp_number;
                for (int j = 0; j + temp_number.length() < 4; j++) {
                    setallorder_all += "  ";
                }
                setallorder_all += "" + order.get(i).getPrice() + "\n";
            }
        }

        settlement_all.setText(setallorder_all);
    }

    private void setlocalainformation() {
        settlement_address.setText(address);
        settlement_phone.setText(phone);
    }

    private void getlocalainformation() {
        address = LocalInformationTool.getAddress();
        phone = LocalInformationTool.getPhone();
    }

    private void getoeder() {
        order = LifeDangKouActivity.order;
        allprice = LifeDangKouActivity.all + 1 ;
        select = LifeDangKouActivity.select;
    }

    private void crete() {
        settlement_address = (TextView) findViewById(R.id.settlement_address);
        settlement_address.setOnClickListener(this);
        settlement_phone = (TextView) findViewById(R.id.settlement_phone);
        settlement_phone.setOnClickListener(this);
        settlement_reward= (TextView) findViewById(R.id.settlement_reward);//大赏
        settlement_reward.setOnClickListener(this);
        settlement_remarks= (TextView) findViewById(R.id.settlement_remarks);//备注
        settlement_remarks.setOnClickListener(this);
        settlement_dkname= (TextView) findViewById(R.id.settlement_dkname);
        settlement_dkname.setText(dkname);
        settlement_all = (TextView) findViewById(R.id.settlement_all);
        settlement_allprice= (TextView) findViewById(R.id.settlement_allprice);
        settlement_allprice.setText("" + allprice);
        settlement_finish= (TextView) findViewById(R.id.settlement_finish);
        settlement_finish.setOnClickListener(this);


    }

    private void createtitle() {
        st_title = (TextView) findViewById(R.id.life_title);
        st_title.setText("确认订单");
        back = (TextView) findViewById(R.id.life_back);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.life_back:
                this.finish();
                break;
            case R.id.settlement_address:
                changedate(1);
                break;
            case R.id.settlement_phone:
                changedate(2);
                break;
            case R.id.settlement_reward:
                changedate(3);
                break;
            case R.id.settlement_remarks:
                changedate(4);
                break;
            case R.id.settlement_finish:
                settlement_finish();
                break;
        }
    }

    private void settlement_finish() {

        address = settlement_address.getText().toString();
        phone = settlement_phone.getText().toString();

        if (address.length() < 4 || phone.length()<4){
            Toast.makeText(this , "请输入正确的地址和联系方式" , Toast.LENGTH_SHORT).show();
        }else {
            ordernum = getordernum();
            ordermenu = getordermenu(ordernum);

            pr = ProgressDialog.show(SetTlementActivity.this, null, "提交订单中,请稍后");
            CountingTask task=new CountingTask();
            task.execute();
        }
    }
    private class CountingTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            PreferenceTool pt = new PreferenceTool(SetTlementActivity.this);

            try {

                psostresult = SetTlementNetWork.SetTlement(ordernum , ordermenu , PreferenceTool.getid());
                //psostresult = SetTlementNetWork.SetTlement(ordernum , ordermenu , "201300001");
            } catch (IOException e) {}
            return null;
        }
        protected void onProgressUpdate(Void... progress){}
        protected void onPostExecute(Void result){
            if (psostresult.equals("{\"status\":\"ok\"}")){
                success();
            }else {
                error();
            }
        }
    }

    private void error() {
        pr.dismiss();
        Toast.makeText(this , "订单提交失败,请重新提交" , Toast.LENGTH_SHORT).show();
    }

    private void success() {
        lo = getlo();




        pr.dismiss();
        Toast.makeText(this , "订单提交成功,请耐心等候" , Toast.LENGTH_SHORT).show();
        this.finish();
        LifeDangKouActivity.lifedangkouactivity.finish();
    }

    private String getordermenu(String ordernum) {
        //将数据转换为json格式
        String getordermenu_menu = "";
        getordermenu_menu +="{" +
                "\"ordernum\":\""+ ordernum +"\",\n" +
                "\"where\":\"" + address + "\",\n" +
                "\"phone\":\"" + phone + "\",\n" +
                "\"ordermeal\":[";
        int spd = 0 ;
        for (int i = 0 ; i < order.size() ; i++ ){
            SimpleOrder so = order.get(i);
            if (so.getNumber() > 0){
                getordermenu_menu += "{\"st\":\"" + dangkoulocation + "\",\"dk\":\""+ dkname +"\",\"sl\":\""+ "" + so.getNumber() +"\",\"dj\":\"" + so.getPrice().substring(0 , so.getPrice().length()-1)+ "\",\"cm\":\"" + so.getName()+ "\"}";
                spd ++;
                if ( spd != select){
                    getordermenu_menu +=",";
                }
            }

        }

        getordermenu_menu +=
                "],\n" +
                "\"price\":\"" + allprice + "\",\n" +
                "\"reward\":\"" + settlement_reward.getText().toString() + "\",\n" +
                "\"remark\":\"" + settlement_remarks.getText().toString() +"\"\n" +
                "}";

       Log.d("=-=" ,"getordermenu_menu: " + getordermenu_menu );


        return  getordermenu_menu;
    }

    private String getordernum() {
        String getordernum_ordernum;

        PreferenceTool pt = new PreferenceTool(this);
        getordernum_ordernum = PreferenceTool.getid();

        getordernum_ordernum += System.currentTimeMillis();

        return getordernum_ordernum;
    }

    private void changedate(int index) {
        Intent change_intent = new Intent();
        switch (index){
            case 1:
                change_intent.putExtra("name" , "地址");
                change_intent.putExtra("text" , settlement_address.getText().toString());
                change_intent.putExtra("remark" , "请详细填写你的位置,方便他人查看");
                break;
            case 2:
                change_intent.putExtra("name" , "联系方式");
                change_intent.putExtra("text" , settlement_phone.getText().toString());
                change_intent.putExtra("remark" , "请保证你联系方式正确,防止无法送达");
                break;
            case 3:
                change_intent.putExtra("name" , "打赏");
                change_intent.putExtra("text" , settlement_reward.getText().toString());
                change_intent.putExtra("remark" , "提高你的大赏金额可以让别人关注你的订单");
                break;
            case 4:
                change_intent.putExtra("name" , "备注");
                change_intent.putExtra("text" , settlement_remarks.getText().toString());
                change_intent.putExtra("remark" , "适当的填写一定的备注可以帮助大家");
                break;
        }
        change_intent.setClass(this , EnterDataActivity.class);

        startActivityForResult(change_intent, 1);
        //startActivity(change_intent);
    }

    public String getdkname() {
        Intent intent = getIntent();
        String name;
        name  = intent.getStringExtra("dangkouname");
        return name;

    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode==1)
        {
            String returnname = data.getStringExtra("returnname");
            String returntext = data.getStringExtra("returntext");

            if (returnname.equals("地址")){
                settlement_address.setText(returntext);
            }else  if (returnname.equals("联系方式")){
                settlement_phone.setText(returntext);
            }else  if (returnname.equals("打赏")){
                settlement_reward.setText(returntext);
                int return_reward = Integer.valueOf(returntext);
                allprice += return_reward;
                settlement_allprice.setText("" + allprice);
            }else if (returnname.equals("备注")){
                settlement_remarks.setText(returntext);
            }
            LocalInformationTool.setLocalInformationTool(settlement_address.getText().toString() , settlement_phone.getText().toString());

        }


    }


}
