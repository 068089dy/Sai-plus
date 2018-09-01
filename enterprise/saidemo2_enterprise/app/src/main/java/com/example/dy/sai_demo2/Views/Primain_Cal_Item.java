package com.example.dy.sai_demo2.Views;

public class Primain_Cal_Item {
    String id;
    String title;
    String content;
    String date;
    public Primain_Cal_Item(String id,String title, String content, String date){
        this.id = id;
        this.content = content;
        this.title = title;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }
}

