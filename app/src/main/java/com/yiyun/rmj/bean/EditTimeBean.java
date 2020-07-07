package com.yiyun.rmj.bean;

/**
 * Created by Administrator on 2018/7/20 0020.
 */

public class EditTimeBean {
    private boolean iseditor = false;
    private String time;
    private String shiduan;
    private String chongfu;
    private String startTime;
    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    private boolean isopen;

    public boolean iseditor() {
        return iseditor;
    }

    public void setIseditor(boolean iseditor) {
        this.iseditor = iseditor;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getShiduan() {
        return shiduan;
    }

    public void setShiduan(String shiduan) {
        this.shiduan = shiduan;
    }

    public String getChongfu() {
        return chongfu;
    }

    public void setChongfu(String chongfu) {
        this.chongfu = chongfu;
    }

    public boolean isopen() {
        return isopen;
    }

    public void setIsopen(boolean isopen) {
        this.isopen = isopen;
    }
}
