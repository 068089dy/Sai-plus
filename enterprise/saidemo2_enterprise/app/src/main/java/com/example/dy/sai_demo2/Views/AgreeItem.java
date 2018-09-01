package com.example.dy.sai_demo2.Views;

public class AgreeItem {
    public String title;
    public String date;
    public String id;
    public String is_join;
    public AgreeItem(String id, String title, String date, String is_join){
        this.date = date;
        this.id = id;
        this.title = title;
        this.is_join = is_join;
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIs_join() {
        return is_join;
    }
}
