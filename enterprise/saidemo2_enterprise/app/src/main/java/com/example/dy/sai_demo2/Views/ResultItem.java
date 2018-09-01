package com.example.dy.sai_demo2.Views;

/**
 * Created by dy on 18-3-19.
 */

public class ResultItem {

    String title;
    String content;
    public ResultItem(String title, String content){
        this.title = title;
        this.content = content;
    }
    public String getTitle(){
        return title;
    }
    public String getContent(){
        return content;
    }

}
