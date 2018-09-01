package com.example.dy.sai_demo2.Views;

/**
 * Created by dy on 18-3-13.
 */

public class LoopItem {

    String title;
    String id;
    String create_time;
    String content;
    String is_join;
    String image;
    public LoopItem(String id, String create_time, String title, String content, String image, String is_join){
        this.title = title;
        this.id = id;
        this.create_time = create_time;
        this.content = content;
        this.image = image;
        this.is_join = is_join;
    }
    public LoopItem(String title, String content){
        this.title = title;
        this.content = content;
    }
    public String getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public String getContent(){
        return content;
    }
    public String getCreate_time(){
        return create_time;
    }

}
