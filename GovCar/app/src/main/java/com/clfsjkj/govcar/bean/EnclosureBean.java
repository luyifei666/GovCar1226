package com.clfsjkj.govcar.bean;

import java.io.Serializable;

public class EnclosureBean implements Serializable {
    //申请记录里的图片查看 Enclosure（附件）
    private String picName;
    private String picUrl;

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
