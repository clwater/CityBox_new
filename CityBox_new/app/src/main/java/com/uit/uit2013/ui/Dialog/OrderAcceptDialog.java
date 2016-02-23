package com.uit.uit2013.ui.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.uit.uit2013.R;
import com.uit.uit2013.model.AllOrder;
import com.uit.uit2013.model.Order;

import java.util.Vector;

/**
 * Created by yszsyf on 16/2/17.
 */
public class OrderAcceptDialog extends Dialog implements View.OnClickListener {



    //定义回调事件，用于dialog的点击事件
    public interface OnCustomDialogListener{
        public void back(String name);
    }

    private AllOrder allOrder;
    private OnCustomDialogListener customDialogListener;
    private TextView dailog_order_address , dailog_order_allprice , dailog_order_reward , dailog_order_where ,
                    dailog_order_order,dailog_order_remark ;
    private Button dailog_order_canel , dailog_order_get;
    private int width;

    public OrderAcceptDialog(Context context, AllOrder allorder , OnCustomDialogListener customDialogListener) {
        super(context);
        this.allOrder = allorder;
        this.customDialogListener = customDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_accept);



        //设置标题

        setTitle("");
        dailog_order_address = (TextView) findViewById(R.id.dailog_order_address);
        dailog_order_allprice = (TextView) findViewById(R.id.dailog_order_allprice);
        dailog_order_reward = (TextView) findViewById(R.id.dailog_order_reward);
        dailog_order_where = (TextView) findViewById(R.id.dailog_order_where);
        dailog_order_order = (TextView) findViewById(R.id.dailog_order_order);
        dailog_order_remark = (TextView) findViewById(R.id.dailog_order_remark);

        dailog_order_canel = (Button)findViewById(R.id.dailog_order_canel);
        dailog_order_get = (Button)findViewById(R.id.dailog_order_get);

        dailog_order_canel.setOnClickListener(this);
        dailog_order_get.setText("抢单");
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
                customDialogListener.back(allOrder.getOrdernum());
                dismiss();
                break;
        }

    }



}
