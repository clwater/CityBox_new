package com.uit.uit2013.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.uit.uit2013.R;
import com.uit.uit2013.model.LocalOrder;
import com.uit.uit2013.model.SimpleOrder;
import com.uit.uit2013.ui.activity.LifeDangKouActivity;

import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by yszsyf on 16/2/18.
 */
public class OrderHistoryAdapter extends BaseAdapter {
    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private Zujian dk =  new Zujian();

    class Order{
        String name;
        String price;
        int number;
    }



    public OrderHistoryAdapter(Context context, List<Map<String, Object>> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    /**
     * 组件集合，对应list.xml中的控件
     *
     * @author Administrator
     */
    public final class Zujian {
        public TextView item_order_statu , item_order_location , item_order_allprice , item_order_address , item_order_rewark ;

    }


    public int getCount() {
        return data.size();
    }
    public Object getItem(int position) {
        return data.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(final int position, View convertView, ViewGroup parent) {

        final int selectID = position;
        if (convertView == null) {

            //获得组件，实例化组件
            convertView = layoutInflater.inflate(R.layout.item_order, null);
            dk.item_order_statu = (TextView) convertView.findViewById(R.id.item_order_statu);
            dk.item_order_location = (TextView) convertView.findViewById(R.id.item_order_location);
            dk.item_order_allprice = (TextView) convertView.findViewById(R.id.item_order_allprice);
            dk.item_order_address = (TextView) convertView.findViewById(R.id.item_order_address);
            dk.item_order_rewark = (TextView) convertView.findViewById(R.id.item_order_rewark);
            convertView.setTag(dk);
        } else {
            dk = (Zujian) convertView.getTag();
        }
        //绑定数据

        dk.item_order_statu.setText((String) data.get(position).get("statu"));
        dk.item_order_location.setText((String) data.get(position).get("loaction"));
        dk.item_order_allprice.setText((String) data.get(position).get("allprice"));
        dk.item_order_address.setText((String) data.get(position).get("address"));
        dk.item_order_rewark.setText((String) data.get(position).get("reward"));

        String  statu = (String) data.get(position).get("statu");

        if (statu.equals(LocalOrder.STATU_WAIT)){
            convertView.setBackgroundColor(Color.parseColor("#F0ED84"));
            dk.item_order_statu.setText("等待\n抢单");
        }else if (statu.equals(LocalOrder.STATU_ACCEPT)){
            convertView.setBackgroundColor(Color.parseColor("#7BAEF0"));
            dk.item_order_statu.setText("正在\n派送");
        }else if (statu.equals(LocalOrder.STATU_SUCCESS)){
            convertView.setBackgroundColor(Color.parseColor("#70F0A8"));
            dk.item_order_statu.setText("订单\n成功");
        }else if (statu.equals(LocalOrder.STATU_ERROR)){
            convertView.setBackgroundColor(Color.parseColor("#EBECF0"));
            dk.item_order_statu.setText("订单\n失效");
        }else if (statu.equals(LocalOrder.STATU_QUESTION)){
            convertView.setBackgroundColor(Color.parseColor("#F06964"));
            dk.item_order_statu.setText("订单\n出错");
        }

        return convertView;
    }


}
