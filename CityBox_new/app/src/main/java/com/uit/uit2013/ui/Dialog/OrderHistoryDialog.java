package com.uit.uit2013.ui.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.uit.uit2013.R;
import com.uit.uit2013.model.AllOrder;
import com.uit.uit2013.model.LocalOrder;
import com.uit.uit2013.model.Order;
import com.uit.uit2013.ui.activity.MyOrderActivity;

import java.util.Vector;

/**
 * Created by yszsyf on 16/2/17.
 */
public class OrderHistoryDialog extends Dialog implements View.OnClickListener {




    //定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener{
        public void back(String name);
    }

    private AllOrder allOrder;
    private OnCustomDialogListener customDialogListener;
    private TextView dailog_order_address , dailog_order_allprice , dailog_order_reward , dailog_order_where ,
                    dailog_order_order,dailog_order_remark  , dailog_order_orderphone ,dailog_order_sendphone;
    private Button dailog_order_canel , dailog_order_get;
    private boolean type = true; //true 为订餐
    private String phone , statu;
    private Activity activity;


    public OrderHistoryDialog(Context context, AllOrder allorder , boolean type  , String phone , Activity activity , String statu , OnCustomDialogListener customDialogListener ) {
        super(context);
        this.allOrder = allorder;
        this.customDialogListener = customDialogListener;
        this.type = type;
        this.phone = phone;
        this.activity = activity;
        this.statu = statu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_order_information);
        //设置标题

        setTitle("");
        dailog_order_address = (TextView) findViewById(R.id.dailog_order_address);
        dailog_order_allprice = (TextView) findViewById(R.id.dailog_order_allprice);
        dailog_order_reward = (TextView) findViewById(R.id.dailog_order_reward);
        dailog_order_where = (TextView) findViewById(R.id.dailog_order_where);
        dailog_order_order = (TextView) findViewById(R.id.dailog_order_order);
        dailog_order_remark = (TextView) findViewById(R.id.dailog_order_remark);
        dailog_order_orderphone = (TextView) findViewById(R.id.dailog_order_orderphone);
        dailog_order_orderphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+dailog_order_orderphone.getText().toString()));
                activity.startActivity(intent);

            }
        });
        dailog_order_sendphone = (TextView) findViewById(R.id.dailog_order_sendphone);
        dailog_order_sendphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+dailog_order_sendphone.getText().toString()));
                activity.startActivity(intent);

            }
        });
        dailog_order_canel = (Button)findViewById(R.id.dailog_order_canel);
        dailog_order_get = (Button)findViewById(R.id.dailog_order_get);

        dailog_order_canel.setOnClickListener(this);
        dailog_order_get.setOnClickListener(this);

        setText();
    }

    private void setText() {
        dailog_order_address.setText(allOrder.getWhere());
        dailog_order_allprice.setText(allOrder.getPrice() + "元");
        dailog_order_reward.setText(allOrder.getReward() + "元");
        dailog_order_where.setText(allOrder.getOrdermeal().get(0).getSt() + ":" + allOrder.getOrdermeal().get(0).getDk());
        dailog_order_order.setText(getallorder());
        dailog_order_remark.setText(allOrder.getRemark());
        dailog_order_orderphone.setText(allOrder.getPhone());
        dailog_order_sendphone.setText(phone);

        if (statu.equals(LocalOrder.STATU_WAIT)){
            Log.d("history_what2" ,"statu  "+ statu + "  type" + type);
            dailog_order_get.setText("更新状态");
        }else if (statu.equals(LocalOrder.STATU_ACCEPT) && (type == true)){
            Log.d("history_what2" ,"statu   "+ statu+ "  type" + type);
            dailog_order_get.setText("确认收到");
        }else if (statu.equals(LocalOrder.STATU_ACCEPT) && (type == false)){
            Log.d("history_what2" ,"statu1   "+ statu+ "  type" + type);
            statu = LocalOrder.STATU_WAIT;
            Log.d("history_what2" ,"statu2   "+ statu+ "  type" + type);
            dailog_order_get.setText("更新状态");
        }else {
            dailog_order_get.setText("确定");
        }

    }

    private String getallorder() {
        String a_order = "";
        Vector<Order> o = allOrder.getOrdermeal();
        for (int i = 0 ; i <o.size() ; i++){
            a_order += o.get(i).getCm();
            for (int j = 0; j + o.get(i).getCm().length() < 10; j++) {
                a_order += "    ";
            }
            String temp_number = String.valueOf(o.get(i).getSl());
            a_order += " x" + temp_number;
            for (int j = 0; j + temp_number.length() < 3; j++) {
                a_order += "  ";
            }
            a_order += "" + o.get(i).getDj() + "元\n";

        }

        return a_order;
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dailog_order_canel:
                customDialogListener.back("canel");
                dismiss();
                break;
            case R.id.dailog_order_get:

                customDialogListener.back(statu);
                dismiss();
                break;
        }

    }



}
