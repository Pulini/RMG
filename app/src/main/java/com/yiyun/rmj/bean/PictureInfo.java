package com.yiyun.rmj.bean;

import java.io.Serializable;

public class PictureInfo implements Serializable {
    private int type;
    private String url;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
