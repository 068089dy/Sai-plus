package com.example.dy.sai_demo2.Views;

public class MyJoinedItem {
    String date;
    String title;
    String content;
    String type;
    String id;
    public MyJoinedItem(String date, String title, String content){
        this.content = content;
        this.date = date;
        this.title = title;
    }

    public MyJoinedItem(String id,String type,String date, String title, String content){
        this.content = content;
        this.date = date;
        this.title = title;
        this.id = id;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
