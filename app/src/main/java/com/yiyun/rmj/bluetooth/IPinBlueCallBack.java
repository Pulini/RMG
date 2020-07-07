package com.yiyun.rmj.bluetooth;

import android.bluetooth.BluetoothDevice;

public interface IPinBlueCallBack {

    public void onBondRequest();
    public void onBondFail(BluetoothDevice device);
    public void onBonding(BluetoothDevice device);
    public void onBondSuccess(BluetoothDevice device);
}
