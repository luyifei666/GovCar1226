package com.clfsjkj.govcar.bean;

import java.io.Serializable;

public class TimeLineBean implements Serializable {
    //申请记录里的图片查看下面的时间轴
    private String title;
    private String time;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
