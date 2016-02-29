package com.uit.uit2013.model;

/**
 * Created by yszsyf on 16/2/15.
 * 简略订单  用于选择
 */
public class SimpleOrder {
    String name;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    String price;
    int number;
}
