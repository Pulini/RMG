package com.yiyun.rmj.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Method;

public class ClassicalBluetoothUtil extends BaseBluetoothUtil {
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mBluetoothSocket;

    public static final String TAG = "bcz";

    public void init()
    {
        //获取蓝牙Adapter对象
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * 设备是否支持蓝牙  true为支持
     * @return
     */
    public boolean isSupportBlue(){
        return mBluetoothAdapter != null;
    }

    /**
     * 蓝牙是否打开   true为打开
     * @return
     */
    public boolean isBlueEnable(){
        return isSupportBlue() && mBluetoothAdapter.isEnabled();
    }

    /**
     * 自动打开蓝牙（异步：蓝牙不会立刻就处于开启状态）
     * 这个方法打开蓝牙不会弹出提示
     */
    public void openBlueAsyn(){
        if (isSupportBlue()) {
            mBluetoothAdapter.enable();
        }
    }

    /**
     * 自动打开蓝牙（同步）
     * 这个方法打开蓝牙会弹出提示
     * 需要在onActivityResult 方法中判断resultCode == RESULT_OK  true为成功
     */
    public void openBlueSync(Activity activity, int requestCode){
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 扫描的方法 返回true 扫描成功
     * 通过接收广播获取扫描到的设备
     * @return
     */
    public boolean scanBlue(){
        if (!isBlueEnable()){
            Log.e("bcz", "Bluetooth not enable!");
            return false;
        }

        //当前是否在扫描，如果是就取消当前的扫描，重新扫描
        if (mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
        }

        //此方法是个异步操作，一般搜索12秒
        return mBluetoothAdapter.startDiscovery();
    }

    /**
     * 取消扫描蓝牙
     * @return  true 为取消成功
     */
    public boolean cancelScanBule(){
        if (isSupportBlue()){
            return mBluetoothAdapter.cancelDiscovery();
        }
        return true;
    }

    /**
     * 取消配对（取消配对成功与失败通过广播返回 也就是配对失败）
     * @param device
     */
    public void cancelPinBule(BluetoothDevice device){
        if (device == null){
            Log.d(TAG, "cancel bond device null");
            return;
        }
        if (!isBlueEnable()){
            Log.e(TAG, "Bluetooth not enable!");
            return;
        }
        //判断设备是否配对，没有配对就不用取消了
        if (device.getBondState() != BluetoothDevice.BOND_NONE) {
            Log.d(TAG, "attemp to cancel bond:" + device.getName());
            try {
                Method removeBondMethod = device.getClass().getMethod("removeBond");
                Boolean returnValue = (Boolean) removeBondMethod.invoke(device);
                returnValue.booleanValue();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e(TAG, "attemp to cancel bond fail!");
            }
        }
    }

    /**
     * 配对（配对成功与失败通过广播返回）
     * @param device
     */
    public void pin(BluetoothDevice device){
        if (device == null){
            Log.e(TAG, "bond device null");
            return;
        }
        if (!isBlueEnable()){
            Log.e(TAG, "Bluetooth not enable!");
            return;
        }
        //配对之前把扫描关闭
        if (mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
        }
        //判断设备是否配对，没有配对在配，配对了就不需要配了
        if (device.getBondState() == BluetoothDevice.BOND_NONE) {
            Log.d(TAG, "attemp to bond:" + device.getName());
            try {
                Method createBondMethod = device.getClass().getMethod("createBond");
                Boolean returnValue = (Boolean) createBondMethod.invoke(device);
                returnValue.booleanValue();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e(TAG, "attemp to bond fail!");
            }
        }
    }


    /**
     * 连接 （在配对之后调用）
     * @param device
     */
    public void connect(BluetoothDevice device, IConnectBlueCallBack callBack){
        if (device == null){
            Log.d(TAG, "bond device null");
            return;
        }
        if (!isBlueEnable()){
            Log.e(TAG, "Bluetooth not enable!");
            return;
        }
        //连接之前把扫描关闭
        if (mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
        }
        new ConnectBlueTask(callBack).execute(device);
    }

    public void setBluetoothSocket(BluetoothSocket socket)
    {
        mBluetoothSocket = socket;
    }

    /**
     * 蓝牙是否连接
     * @return
     */
    public boolean isConnectBlue(){
        return mBluetoothSocket != null && mBluetoothSocket.isConnected();
    }

    /**
     * 断开连接
     * @return
     */
    public boolean cancelConnect(){
        if (mBluetoothSocket != null && mBluetoothSocket.isConnected()){
            try {
                mBluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        mBluetoothSocket = null;
        return true;
    }

    /**
     * 输入mac地址进行自动配对
     * 前提是系统保存了该地址的对象
     * @param address
     * @param callBack
     */
    public void connectMAC(String address, IConnectBlueCallBack callBack) {
        if (!isBlueEnable()){
            return ;
        }
        BluetoothDevice btDev = mBluetoothAdapter.getRemoteDevice(address);
        connect(btDev, callBack);
    }


}
