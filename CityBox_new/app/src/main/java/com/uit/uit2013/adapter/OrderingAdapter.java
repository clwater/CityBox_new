package com.uit.uit2013.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.uit.uit2013.R;
import com.uit.uit2013.model.SimpleOrder;
import com.uit.uit2013.ui.activity.LifeDangKouActivity;

import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by yszsyf on 16/2/14.
 */
public class OrderingAdapter extends BaseAdapter {

    private List<Map<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;
    private int shuliang[];
    private Zujian dk = new Zujian();
    private Vector<TextView> vb = new Vector<TextView>();

    class Order{
        String name;
        String price;
        int number;
    }



    public OrderingAdapter(Context context, List<Map<String, Object>> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);

        shuliang = new int[data.size()];
        for (int i = 0; i < data.size(); i++) {
            shuliang[i] = 0;
        }

    }

    /**
     * 组件集合，对应list.xml中的控件
     *
     * @author Administrator
     */
    public final class Zujian {
        public TextView name;
        public TextView price;
        public TextView number;
        public Button sub;
        public Button add;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    /**
     * 获得某一位置的数据
     */
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    /**
     * 获得唯一标识
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final int selectID = position;
        if (convertView == null) {

            //获得组件，实例化组件
            convertView = layoutInflater.inflate(R.layout.item_dangkou, null);
            dk.name = (TextView) convertView.findViewById(R.id.itme_dk_name);
            dk.price = (TextView) convertView.findViewById(R.id.itme_dk_price);
            dk.sub = (Button) convertView.findViewById(R.id.item_dk_sub);
            dk.add = (Button) convertView.findViewById(R.id.item_dk_add);
            dk.number = (TextView) convertView.findViewById(R.id.item_dk_number);
            vb.add(dk.number);

            convertView.setTag(dk);
        } else {
            dk = (Zujian) convertView.getTag();
        }
        //绑定数据

        dk.name.setText((String) data.get(position).get("name"));
        dk.price.setText((String) data.get(position).get("price"));
        dk.number.setText("0");
        dk.sub.setText("-");
        dk.sub.setTag(position);
        dk.add.setTag(position);

        SimpleOrder order = new SimpleOrder();
        order.setName((String) data.get(position).get("name"));
        order.setPrice((String) data.get(position).get("price"));;
        order.setNumber(0);
        LifeDangKouActivity.order.add(order);

        final Zujian finalHolder = dk;
        dk.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(shuliang[position] < 1 ){
                    Toast.makeText(context , "你还没有选择这个菜品" , Toast.LENGTH_SHORT).show();
                }
                else {
                    shuliang[position]--;
                    vb.get(position).setText("" + shuliang[position]);
                    LifeDangKouActivity.order.get(position).setNumber(shuliang[position]);
                    LifeDangKouActivity.changeorde();
                    LifeDangKouActivity.select--;
                }
            }
        });
        dk.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shuliang[position]++;
                vb.get(position).setText("" + shuliang[position]);
                LifeDangKouActivity.order.get(position).setNumber(shuliang[position]);
                LifeDangKouActivity.changeorde();
                LifeDangKouActivity.select++;
            }
        });

        return convertView;
    }


}