package com.yiyun.rmj.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class BluetoothSettingBean extends DataSupport implements Serializable {

    private int id;// 设置Id
    private String deviceName; //设备名
    private int deviceid; //设置ID
    private String remarks; //备注
    private int strength; //强度  0:弱  1:中 2：强 3：MAX
    private int time; //间隔时间
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getDeviceId() {
        return deviceid;
    }

    public void setDeviceId(int deviceId) {
        this.deviceid = deviceId;
    }

    @Override
    public String toString() {
        return "BluetoothSettingBean{" +
                "id=" + id +
                ", deviceName='" + deviceName + '\'' +
                ", deviceid=" + deviceid +
                ", remarks='" + remarks + '\'' +
                ", strength=" + strength +
                ", time=" + time +
                ", isSelected=" + isSelected +
                '}';
    }
}
