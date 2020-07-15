package com.yiyun.rmj.activity.bluetooth;

import com.yiyun.rmj.bluetooth.NewBleBluetoothUtil;

import java.io.Serializable;

/**
 * File Name : SettingListModel
 * Created by : PanZX on 2020/07/14
 * Email : 644173944@qq.com
 * Github : https://github.com/Pulini
 * Remark：
 */

public class SettingListModel implements Serializable {

    private int model = NewBleBluetoothUtil.mode_short_long;
    private int shortTime = 1;
    private int shortStrength = 1;
    private int longTime = 1;
    private int longStrength = 1;
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