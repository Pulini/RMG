package com.yiyun.rmj.bean;

import java.util.ArrayList;

public class OrderProgressInfo {

    private String com;
    private String isCheck;
    private String condition;
    private String nu;
    private String state;
    private String message;
    private String status;
    private ArrayList<ProgressInfo> data;

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getNu() {
        return nu;
    }

    public void setNu(String nu) {
        this.nu = nu;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<ProgressInfo> getData() {
        return data;
    }

    public void setData(ArrayList<ProgressInfo> data) {
        this.data = data;
    }
}
