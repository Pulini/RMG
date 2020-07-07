package com.yiyun.rmj.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public interface IConnectBlueCallBack {
    public void onStartConnect();
    public void onConnectSuccess(BluetoothDevice bluetoothDevice, BluetoothSocket bluetoothSocket);
    public void onConnectFail(BluetoothDevice bluetoothDevice, String resultMassage);
}
