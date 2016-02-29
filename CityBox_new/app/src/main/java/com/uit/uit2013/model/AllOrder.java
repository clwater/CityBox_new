package com.uit.uit2013.model;

import java.util.List;
import java.util.Vector;

/**
 * Created by yszsyf on 16/2/16.
 * 订单
 */
public class AllOrder {
    String ordernum;
    String where;
    String phone;
    String reward;
    String remark;
    String price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;
    Vector<Order> ordermeal = new Vector<Order>();

    public String getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Vector<Order> getOrdermeal() {
        return ordermeal;
    }

    public void setOrdermeal(Vector<Order> ordermeal) {
        this.ordermeal = ordermeal;
    }
}



