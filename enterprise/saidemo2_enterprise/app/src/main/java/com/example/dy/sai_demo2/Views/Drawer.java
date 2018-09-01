package com.example.dy.sai_demo2.Views;

import android.media.Image;
import android.provider.ContactsContract;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by dy on 17-12-5.
 */

public class Drawer {
    private String Name;
    int ImageId;
    int MsgNum;
    public Drawer(String Name,int ImageId){
        this.Name = Name;
        this.ImageId = ImageId;
        //this.MsgNum = MsgNum;
    }
    public void setMsgNum(int num){
        this.MsgNum = num;
    }
    public int getMsgNum(){
        return MsgNum;
    }
    public int getImageId(){
        return ImageId;
    }
    public String getName(){
        return Name;
    }

}
