package com.yiyun.rmj.bean;

/**
 * Created by Administrator on 2018/7/17 0017.
 */

public class RecetitionBean {
    public RecetitionBean(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isOnclick() {
        return isOnclick;
    }

    public void setOnclick(boolean onclick) {
        isOnclick = onclick;
    }

    private String text;
    private  boolean isOnclick=false;

}
