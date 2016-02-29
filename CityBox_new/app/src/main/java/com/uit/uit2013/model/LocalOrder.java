package com.uit.uit2013.model;

/**
 * Created by yszsyf on 16/2/17.
 * 本地订单  用于和数据库进行交互
 */
public class LocalOrder {
    public static String TYPE_ORDER = "order" ;
    public static String TYPE_SENG = "send";
    public static String STATU_WAIT = "wait";
    public static String STATU_ACCEPT = "accept";
    public static String STATU_SUCCESS = "success";
    public static String STATU_ERROR = "error";
    public static String STATU_QUESTION = "question";


    private String ordertype;
    private String ordernum;
    private String ordermealman;
    private String sendmealman;
    private String sendmanphone;
    private String ordermenu;
    private String orderstart;
    private String ordersucces;
    private String orderend;
    private String orderstatu;

    public String getOrdermealman() {
        return ordermealman;
    }

    public void setOrdermealman(String ordermealman) {
        this.ordermealman = ordermealman;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public String getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    public String getSendmealman() {
        return sendmealman;
    }

    public void setSendmealman(String sendmealman) {
        this.sendmealman = sendmealman;
    }

    public String getSendmanphone() {
        return sendmanphone;
    }

    public void setSendmanphone(String sendmanphone) {
        this.sendmanphone = sendmanphone;
    }

    public String getOrdermenu() {
        return ordermenu;
    }

    public void setOrdermenu(String ordermenu) {
        this.ordermenu = ordermenu;
    }

    public String getOrderstart() {
        return orderstart;
    }

    public void setOrderstart(String orderstart) {
        this.orderstart = orderstart;
    }

    public String getOrdersucces() {
        return ordersucces;
    }

    public void setOrdersucces(String ordersucces) {
        this.ordersucces = ordersucces;
    }

    public String getOrderend() {
        return orderend;
    }

    public void setOrderend(String orderend) {
        this.orderend = orderend;
    }

    public String getOrderstatu() {
        return orderstatu;
    }

    public void setOrderstatu(String orderstatu) {
        this.orderstatu = orderstatu;
    }


}

