package com.example.dy.sai_demo2.Views;

/**
 * Created by dy on 18-3-18.
 */

public class CompanyItem {
    String title;
    String content;
    String date;
    String id;
    public CompanyItem(String id,String title, String content, String date){
        this.title = title;
        this.content = content;
        this.date = date;
        this.id = id;
    }
    public String getTitle(){
        return title;
    }
    public String getContent(){
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }
}
