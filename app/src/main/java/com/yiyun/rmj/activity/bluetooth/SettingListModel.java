package com.yiyun.rmj.activity.bluetooth;

import java.io.Serializable;

/**
 * File Name : SettingListModel
 * Created by : PanZX on 2020/07/14
 * Email : 644173944@qq.com
 * Github : https://github.com/Pulini
 * Remark：
 */

public class SettingListModel implements Serializable {

    private int model = 0;
    private int shortTime = 0;
    private int shortStrength = 0;
    private int longTime = 0;
    private int longStrength = 0;
    private String modelName = "设置";
    private boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public int getShortTime() {
        return shortTime;
    }

    public void setShortTime(int shortTime) {
        this.shortTime = shortTime;
    }

    public int getShortStrength() {
        return shortStrength;
    }

    public void setShortStrength(int shortStrength) {
        this.shortStrength = shortStrength;
    }

    public int getLongTime() {
        return longTime;
    }

    public void setLongTime(int longTime) {
        this.longTime = longTime;
    }

    public int getLongStrength() {
        return longStrength;
    }

    public void setLongStrength(int longStrength) {
        this.longStrength = longStrength;
    }
}