package com.yiyun.rmj.bean;

import java.io.Serializable;

public class OrderProgressBean implements Serializable {

    private String progressState;
    private String progressDate;

    public String getProgressState() {
        return progressState;
    }

    public void setProgressState(String progressState) {
        this.progressState = progressState;
    }

    public String getProgressDate() {
        return progressDate;
    }

    public void setProgressDate(String progressDate) {
        this.progressDate = progressDate;
    }
}
