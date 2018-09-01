package com.example.dy.sai_demo2.Views;

/**
 * Created by dy on 18-3-19.
 */

public class LooptechItem {
    String id;
    String user;
    String date;
    String title;
    String content;
    public LooptechItem(String user,String date,String title, String content){
        this.user = user;
        this.date = date;
        this.title = title;
        this.content = content;
    }
    public LooptechItem(String id,String user,String date,String title, String content){
        this.user = user;
        this.date = date;
        this.title = title;
        this.content = content;
        this.id = id;
    }
    public String getTitle(){
        return title;
    }
    public String getContent(){
        return content;
    }
    public String getUser(){
        return user;
    }
    public String getDate(){
        return date;
    }

    public String getId() {
        return id;
    }
}
