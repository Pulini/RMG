package com.yiyun.rmj.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;

public class BleBluetoothUtil extends BaseBluetoothUtil {

    BluetoothAdapter mBluetoothAdapter;
    @Override
    public void init() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public boolean isSupportBlue() {
        return mBluetoothAdapter != null;
    }

    @Override
    public boolean isBlueEnable() {
        return isSupportBlue() && mBluetoothAdapter.isEnabled();
    }

    @Override
    public boolean scanBlue() {
//        mBluetoothAdapter.startLeScan(mLeScanCallback); //开始搜索
        return false;
    }

    @Override
    public void openBlueAsyn() {
        if (isSupportBlue()) {
            mBluetoothAdapter.enable();
        }
    }

    @Override
    public void openBlueSync(Activity activity, int requestCode) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public boolean cancelScanBule() {
//        mBluetoothAdapter.stopLeScan(mLeScanCallback);
        return false;
    }

    @Override
    public void pin(BluetoothDevice device) {

    }

    @Override
    public void cancelPinBule(BluetoothDevice device) {
    }

    @Override
    public void connect(BluetoothDevice device, IConnectBlueCallBack callBack) {

    }

    @Override
    public void setBluetoothSocket(BluetoothSocket socket) {

    }

    @Override
    public boolean isConnectBlue() {
        return false;
    }

    @Override
    public boolean cancelConnect() {
        return false;
    }

    @Override
    public void connectMAC(String address, IConnectBlueCallBack callBack) {

    }
}
