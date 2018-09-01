package com.example.dy.sai_demo2.Views;

/**
 * Created by dy on 17-12-2.
 */

public class Trace {
    /** 时间 */
    private String acceptTime;
    /** 描述 */
    private String acceptStation;
    private String id;
    public Trace() {
    }

    public Trace(String acceptTime, String acceptStation) {
        this.acceptTime = acceptTime;
        this.acceptStation = acceptStation;
    }
    public Trace(String id, String acceptTime, String acceptStation) {
        this.acceptTime = acceptTime;
        this.acceptStation = acceptStation;
        this.id = id;
    }
    public String getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getAcceptStation() {
        return acceptStation;
    }

    public String getId() {
        return id;
    }

    public void setAcceptStation(String acceptStation) {
        this.acceptStation = acceptStation;
    }
}
