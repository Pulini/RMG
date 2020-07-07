package com.yiyun.rmj.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public abstract class BaseBluetoothUtil {

     public BaseBluetoothUtil()
     {
          init();
     }
     public abstract void init();
     public abstract boolean isSupportBlue();//是否支持蓝牙功能
     public abstract boolean isBlueEnable();//蓝牙是否打开
     public abstract boolean scanBlue(); //扫描蓝牙
     public abstract void openBlueAsyn();//自动打开蓝牙 不弹提示
     public abstract void openBlueSync(Activity activity, int requestCode);//自动打开蓝牙 弹提示
     public abstract boolean cancelScanBule();//取消扫描蓝牙
     public abstract void pin(BluetoothDevice device);//配对蓝牙
     public abstract void cancelPinBule(BluetoothDevice device); //取消配对
     public abstract void connect(BluetoothDevice device, IConnectBlueCallBack callBack);//与蓝牙设备连接
     public abstract void setBluetoothSocket(BluetoothSocket socket);//设置socket
     public abstract boolean isConnectBlue();//判断是否已连接上蓝牙
     public abstract boolean cancelConnect();//断开连接
     public abstract void connectMAC(String address, IConnectBlueCallBack callBack);//输入mac地址进行自动配对

}
