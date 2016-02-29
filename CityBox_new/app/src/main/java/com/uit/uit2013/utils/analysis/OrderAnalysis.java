package com.uit.uit2013.utils.analysis;

import com.uit.uit2013.model.AllOrder;
import com.uit.uit2013.model.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Vector;

/**
 * Created by yszsyf on 16/2/16.
 * 订单解析
 */
public class OrderAnalysis {
    public static AllOrder orderanalysis(String result) throws JSONException {
        AllOrder allOrder = new AllOrder();

        String ordernum , where , phone , reward , remark ,price;
        Vector<Order> ordermeal = new Vector<Order>();


        JSONTokener jsonParser = new JSONTokener(result);
        JSONObject person = (JSONObject) jsonParser.nextValue();

        ordernum = person.getString("ordernum");
        allOrder.setOrdernum(ordernum);
        where = person.getString("where");
        allOrder.setWhere(where);
        phone = person.getString("phone");
        allOrder.setPhone(phone);
        reward = person.getString("reward");
        allOrder.setReward(reward);
        remark = person.getString("remark");
        allOrder.setRemark(remark);
        price = person.getString("price");
        allOrder.setPrice(price);

        allOrder.setId(allOrder.getOrdernum().substring(0,9));


        JSONArray order = person.getJSONArray("ordermeal");


        for (int i = 0; i < order.length(); i++) {
            Order o  = new Order();

            JSONObject temp = order.getJSONObject(i);
            String c = order.get(i).toString();
            JSONTokener t = new JSONTokener(c);

            o.setCm(temp.getString("cm"));
            o.setDj(temp.getString("dj"));
            o.setDk(temp.getString("dk"));
            o.setSl(temp.getString("sl"));
            o.setSt(temp.getString("st"));

            ordermeal.add(o);

        }

        allOrder.setOrdermeal(ordermeal);



        return allOrder;

    }
}
