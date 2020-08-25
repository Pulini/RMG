package com.yiyun.rmj.activity.bluetooth;

import com.yiyun.rmj.utils.LogUtils;

public class BluetoothModel {
    // 开机状态
    private int State = 0x00;
    // 剩余电量
    private int ElectricityQuantity = 0x00;
    // 清洗时长
    private int CleanTime = 0x00;
    // 短喷间隔时间
    private int ShortTime = 0x00;
    // 短喷喷雾强度
    private int ShortStrength = 0x00;
    // 开机清洗使能
    private int AutoClean = 0x00;
    // 工作模式
    private int Model = 0x00;
    // 长喷间隔时间
    private int LongTime = 0x00;
    // 长喷喷雾强度
    private int LongStrength = 0x00;

    public BluetoothModel(byte[] data) {
        try {
            State = data[1];
            ElectricityQuantity = data[2];
            CleanTime = data[3];
            ShortTime = data[4];
            ShortStrength = data[5];
            AutoClean = data[6];
            Model = data[7];
            LongTime = data[8];
            LongStrength = data[9];
            if (LongTime< 1) {
                LongTime=1;
            }
            if (LongTime > 10) {
                LongTime=10;
            }
            if (LongStrength< 2) {
                LongStrength=2;
            }
            if (LongStrength> 8) {
                LongStrength=8;
            }
            if (LongStrength % 2 != 0) {
                LongStrength=LongStrength / 2 * 2;
            }
        }catch (Exception e){
            LogUtils.LogE("设备状态数据异常" + data.toString());
        }
        LogUtils.LogE("开机状态=" + State);
        LogUtils.LogE("剩余电量=" + ElectricityQuantity);
        LogUtils.LogE("清洗时长=" + CleanTime);
        LogUtils.LogE("短喷间隔时间=" + ShortTime);
        LogUtils.LogE("短喷喷雾强度=" + ShortStrength);
        LogUtils.LogE("开机清洗使能=" + AutoClean);
        LogUtils.LogE("工作模式=" + Model);
        LogUtils.LogE("长喷间隔时间=" + LongTime);
        LogUtils.LogE("长喷喷雾强度=" + LongStrength);

    }

    public BluetoothModel() { }

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
