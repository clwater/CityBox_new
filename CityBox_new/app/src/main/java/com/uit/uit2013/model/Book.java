package com.uit.uit2013.model;

/**
 * Created by yszsyf on 16/2/26.
 * 图书
 */
public class Book {
    //String name , author , press , date , location , call;
    String name , author , press , date , location , call;
   // String id , no , title , auther , press , time , search , place , state;

    //[{"id":"序号","no":"","title":"书名","auther":"作者","press":"出版社","time":"出版时间","search":"藏书编号","place":"藏书位置","state":"图书状态"}]

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCall() {
        return call;
    }

    public void setCall(String call) {
        this.call = call;
    }
}

