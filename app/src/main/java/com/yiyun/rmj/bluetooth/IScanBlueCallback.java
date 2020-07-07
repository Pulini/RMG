package com.yiyun.rmj.bluetooth;

import android.bluetooth.BluetoothDevice;

public interface IScanBlueCallback {
    public void onScanStarted();
    public void onScanFinished();
    public void onScanning(BluetoothDevice blueDevice);
}
