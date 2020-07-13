package com.yiyun.rmj.activity.bluetooth;

import com.yiyun.rmj.utils.LogUtils;

public class BluetoothModel {
    // 开机状态
    int State = 0x00;
    // 剩余电量
    int ElectricityQuantity = 0x00;
    // 清洗时长
    int CleanTime = 0x00;
    // 短喷间隔时间
    int ShortTime = 0x00;
    // 短喷喷雾强度
    int ShortStrength = 0x00;
    // 开机清洗使能
    int AutoClean = 0x00;
    // 工作模式
    int Model = 0x00;
    // 长喷间隔时间
    int LongTime = 0x00;
    // 长喷喷雾强度
    int LongStrength = 0x00;

    public BluetoothModel(byte[] data) {
        State = data[1];
        ElectricityQuantity = data[2];
        CleanTime = data[3];
        ShortTime = data[4];
        ShortStrength = data[5];
        AutoClean = data[6];
        Model = data[7];
        LongTime = data[8];
        LongStrength = data[9];

        LogUtils.LogE("开机状态=" +State);
        LogUtils.LogE("剩余电量=" +ElectricityQuantity);
        LogUtils.LogE("清洗时长=" +CleanTime);
        LogUtils.LogE("短喷间隔时间=" + ShortTime);
        LogUtils.LogE("短喷喷雾强度=" + ShortStrength);
        LogUtils.LogE("开机清洗使能=" + AutoClean);
        LogUtils.LogE("工作模式=" +Model);
        LogUtils.LogE("长喷间隔时间=" + LongTime);
        LogUtils.LogE("长喷喷雾强度=" + LongStrength);

    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    public int getElectricityQuantity() {
        return ElectricityQuantity;
    }

    public void setElectricityQuantity(int electricityQuantity) {
        ElectricityQuantity = electricityQuantity;
    }

    public int getCleanTime() {
        return CleanTime;
    }

    public void setCleanTime(int cleanTime) {
        CleanTime = cleanTime;
    }

    public int getShortTime() {
        return ShortTime;
    }

    public void setShortTime(int shortTime) {
        ShortTime = shortTime;
    }

    public int getShortStrength() {
        return ShortStrength;
    }

    public void setShortStrength(int shortStrength) {
        ShortStrength = shortStrength;
    }

    public int getAutoClean() {
        return AutoClean;
    }

    public void setAutoClean(int autoClean) {
        AutoClean = autoClean;
    }

    public int getModel() {
        return Model;
    }

    public void setModel(int model) {
        Model = model;
    }

    public int getLongTime() {
        return LongTime;
    }

    public void setLongTime(int longTime) {
        LongTime = longTime;
    }

    public int getLongStrength() {
        return LongStrength;
    }

    public void setLongStrength(int longStrength) {
        LongStrength = longStrength;
    }
}
