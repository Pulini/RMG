package com.yiyun.rmj.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.utils.LogUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class NewBleBluetoothUtil {

    public static final int OPEN_BLUETOOTH_REQUEST_CODE = 1001;
    private static NewBleBluetoothUtil instance = null;
    private static BluetoothAdapter mBluetoothAdapter;
    private BaseActivity currentContext;

    BluetoothGatt mBluetoothGatt;
    public boolean currentBlueConnected = false; //判断是否与蓝牙设备连接着
    public static final String serviceUUid = "0000fff0-0000-1000-8000-00805f9b34fb"; //蓝牙服务号
    public static final String character_Oen_Close_Number = "0000fff1-0000-1000-8000-00805f9b34fb"; //开关量命令特征号
    public static final String character_write_Number = "0000fff5-0000-1000-8000-00805f9b34fb"; //写特征号
    public static final String character_Read_Number = "0000fff6-0000-1000-8000-00805f9b34fb"; //读特征号
    public static final byte[] OX_ORDER = {
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f,
            0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, (byte) 0x1a, (byte) 0x1b, (byte) 0x1c, (byte) 0x1d, (byte) 0x1e, (byte) 0x1f,
            0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, (byte) 0x2a, (byte) 0x2b, (byte) 0x2c, (byte) 0x2d, (byte) 0x2e, (byte) 0x2f,
            0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, (byte) 0x3a, (byte) 0x3b, (byte) 0x3c, (byte) 0x3d, (byte) 0x3e, (byte) 0x3f
    };

    public static final byte boot = 0x11; //开机
    public static final byte shutdown = 0x10; //关机  10进制：16
    public static final byte clearleft = 0x21; //清洗左喷头  10进制：33
    public static final byte clearright = 0x20; //清洗右喷头 10进制：32
    public static final byte setpoweronclear = 0x31; //设置上电自动清洗  10进制：49
    public static final byte forbidsetpoweronclear = 0x30; //禁止上电自动清洗 10进制：48
    public static final byte settimeinterval = 0x54; //设置时间间隔  10进制：84
    public static final byte setstrenth = 0x57; //设置喷雾强度  10进制：87
    public static final byte setcleartime = 0x58; //设置清洗时长  10进制：88
    public static final byte readstate = 0x53;//读状态  10进制：83
    public static final byte mode_normal = 0x40;//普通模式  10进制：64
    public static final byte mode_mild = 0x41;//智能轻度模式  10进制：65
    public static final byte mode_middle = 0x42;//智能中度模式 10进制：66
    public static final byte mode_strength = 0x43;//智能强度模式 10进制：67
    public static final byte state_boot = 0x01; //开机状态
    public static final byte state_shutdown = 0x00; //关机状态
    public static final byte readStatuss = 0x71; //读取状态

    private static ArrayList<Order> orderQuee; //指令队列

    public BluetoothGattService gattService;
    public BluetoothGattCharacteristic readCharacter;
    public BluetoothGattCharacteristic writeCharacter;
    public BluetoothGattCharacteristic opencloseCharacter;
    private IReadInfoListener readInfoCallBack;
    private int orderSum=0;

    public static synchronized NewBleBluetoothUtil getInstance() {
        if (instance == null) {
            instance = new NewBleBluetoothUtil();
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            orderQuee = new ArrayList();
        }
        return instance;
    }

    OnBlutToothListener listener;
    boolean isSending = false;

    public interface OnBlutToothListener {
        void onStartSend(int Orders);
        void onSending(int index,int Orders);
        void onSendFinish();
    }

    public void setBlutToothListener(OnBlutToothListener listener) {
        this.listener = listener;
    }


    /**
     * 设置当前显示的Activity
     *
     * @param context 当前显示的Activity
     */
    public void setCurrentContext(BaseActivity context) {
        Log.e("bcz", "setCurrentContext");
        this.currentContext = context;
    }

    public boolean createBond(BluetoothDevice device) {

        try {
            Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
            Boolean returnValue = (Boolean) createBondMethod.invoke(device);
            return returnValue.booleanValue();
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean removeBond(BluetoothDevice device) {

        try {
            Method createBondMethod = BluetoothDevice.class.getMethod("removeBond");
            Boolean returnValue = (Boolean) createBondMethod.invoke(device);
            return returnValue.booleanValue();
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 添加指令到队列中
     *
     * @param order
     */
    public synchronized void addOrderToQuee(byte order, int param) {
        for (Order b : orderQuee) {
            if (b.getOrder() == order) {
                b.setIntdata(param);
                return;
            }
        }

        Order order1 = new Order();
        //设置指令
        order1.setOrder(order);
        //设置指令参数
        order1.setIntdata(param);
        orderQuee.add(order1);
    }

    public synchronized Order getOrderFromQuee() {
        if (orderQuee.size() == 0) {
            return null;
        } else {
            //获取第一个指令
            return orderQuee.get(0);
        }
    }

    public synchronized void removeOrderOnQuee(int order) {

        for (Order b : orderQuee) {
            if (b.getOrder() == order) {
                orderQuee.remove(b);
                return;
            }
        }

//        if(orderQuee.size() != 0){
//            orderQuee.remove(0);
//        }
    }

    public boolean bluetoothEnable() {
        // 打开蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            return mBluetoothAdapter.enable();
        } else {
            return true;
        }
    }

    public BluetoothDevice getBleDeviceByAddress(String address) {
        if (mBluetoothAdapter != null) {
            return mBluetoothAdapter.getRemoteDevice(address);
        }
        return null;
    }

    /**
     * 设备是否支持蓝牙  true为支持
     *
     * @return
     */
    public boolean isSupportBlue() {
        return mBluetoothAdapter != null;
    }

    /**
     * 蓝牙是否打开   true为打开
     *
     * @return
     */
    public boolean isBlueEnable() {
        return isSupportBlue() && mBluetoothAdapter.isEnabled();
    }

    /**
     * 自动打开蓝牙（同步）
     * 这个方法打开蓝牙会弹出提示
     * 需要在onActivityResult 方法中判断resultCode == RESULT_OK  true为成功
     */
    public void openBlueSync(Activity activity, int requestCode) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, requestCode);
    }

    private Handler mHandler;
    //15秒搜索时间
    private static final long SCAN_PERIOD = 15000;
    private IConnectingCallback connectingCallback;

    public void scanLeDevice(final boolean enable, final BluetoothAdapter.LeScanCallback callback) {
        if (enable) {//true
            mBluetoothAdapter.startLeScan(callback); //开始搜索
        } else {//false
            mBluetoothAdapter.stopLeScan(callback);//停止搜索
        }
    }


    /**
     * 连接设备
     *
     * @param context
     * @param address
     * @param connectingCallback
     */
    public void connectDevice(Context context, String address, IConnectingCallback connectingCallback) {
        this.connectingCallback = connectingCallback;
        BluetoothDevice bleDevice = getBleDeviceByAddress(address);

        if (bleDevice == null) {
            connectingCallback.connectFailed();
            connectingCallback = null;
        } else {
            mBluetoothGatt = bleDevice.connectGatt(context, false, new MyBluetoothGattCallback());
        }
    }

    /**
     * 断开设备连接
     */
    public void disconnectDevice() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
        }
    }

    /**
     * 关闭蓝牙连接通道
     */
    public void closeDevice() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
        }
    }


    public class MyBluetoothGattCallback extends BluetoothGattCallback {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                //连接成功
                currentBlueConnected = true;
                Log.e("bcz", "蓝牙连接成功");
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_CONNECTING) {
                Log.e("bcz", "蓝牙正在连接");
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                //连接断开
                currentBlueConnected = false;
                Log.e("bcz", "蓝牙连接断开");
                orderQuee.clear();
                if (connectingCallback != null) {
                    connectingCallback.connectFailed();
                    connectingCallback = null;
                } else {

                    if (currentContext != null) {
                        Log.e("bcz", "showUnconnectedDialog");
                        currentContext.showUnconnectedDialog();
                    } else {

                        ToastUtils.show("已与设备断开连接！");
                    }
                }

                //关闭蓝牙连接通道
                closeDevice();

            }
            // GATT Server disconnected
            else if (newState == BluetoothProfile.STATE_DISCONNECTING) {
                //正在连接
                Log.e("bcz", "蓝牙正在断开连接");
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.e("bcz", "onServicesDiscovered");
            //连接成功
            List<BluetoothGattService> services = gatt.getServices();
            for (BluetoothGattService service : services) {
                Log.e("bcz", "service.Uuid：" + service.getUuid().toString());
                if (service.getUuid().toString().equals(serviceUUid)) {
                    gattService = service;
                    initGattInfo(gattService);
                    connectingCallback.connectSuccess();
                    connectingCallback = null;
                    Log.e("bcz", "匹配到指定服务号，成功");
                    return;
                }
            }
            connectingCallback.connectFailed();
            connectingCallback = null;
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            byte[] values = characteristic.getValue();
            removeOrderOnQuee(values[0]);
            readInfoCallBack.callBack(values);
            sendOrder();
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            byte[] values = characteristic.getValue();

            if (status == BluetoothGatt.GATT_SUCCESS) {//写入成功
                Log.e("Pan", "写入成功" + values[0]);
                removeOrderOnQuee(values[0]);
                currentContext.showOrderMessage(values, status);
                if(orderQuee.size()>0){
                    if(listener!=null){
                        LogUtils.LogE("onSending-----------------");
                        listener.onSending(orderSum-orderQuee.size(),orderSum);
                    }
                    Log.e("Pan", "发送下一条协议");
                    sendOrder();
                }
                if(orderQuee.size()==0){
                    isSending=false;
                    if(listener!=null){
                        LogUtils.LogE("onSendFinish-----------------");
                        listener.onSendFinish();
                    }
                }

            } else if (status == BluetoothGatt.GATT_FAILURE) {
                sendOrder();
                Log.e("onCharacteristicWrite中", "写入失败");
            } else if (status == BluetoothGatt.GATT_WRITE_NOT_PERMITTED) {
                Log.e("onCharacteristicWrite中", "没权限");
            }


//            Log.e("bcz", "onCharacteristicWrite----------------begin---------------------");
//            for (byte value : values) {
//                Log.e("bcz", "onCharacteristicWrite--===value:" + value);
//            }
//            Log.e("bcz", "onCharacteristicWrite================end======================");


//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                sendOrder();
//            }
        }

        //数据返回的回调（此处接收BLE设备返回数据）
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            byte[] values = characteristic.getValue();
            Log.e("bcz", "onCharacteristicChanged----------------begin---------------------");
            for (byte value : values) {
                Log.e("bcz", "onCharacteristicChanged--===value:" + value);
            }
            Log.e("bcz", "onCharacteristicChanged================end======================");

        }
    }

    public void sendAllOrder() {

    }

    /**
     * 从队列中获取指令进行发送
     */
    public void sendOrder() {
        Log.e("bcz", "start to sendOrder");
        Order order = getOrderFromQuee();
        if (order == null) {
            return;
        }

        if(!isSending){
            isSending=true;
            if (listener != null) {
                orderSum=orderQuee.size();
                LogUtils.LogE("onStartSend-----------------");
                listener.onStartSend(orderSum);
            }
        }
        Log.e("bcz", "Order is :" + new Gson().toJson(order));

        switch (order.getOrder()) {
            case clearleft:
                //清洗左喷头
                sendClearLeft();
                break;
            case clearright:
                //清洗右喷头
                sendClearRight();
                break;
            case setpoweronclear:
                //设置上电自动清洗
                sendClearOnPower();
                break;
            case forbidsetpoweronclear:
                //禁止上电自动清洗
                sendForbidClearOnPower();
                break;
            case settimeinterval:
                //设置时间间隔
                sendTimeinterval(order.intdata);
                break;
            case setstrenth:
                //设置喷雾强度
                sendStrenth(order.intdata);
                break;
            case setcleartime:
                //设置清洗时长
                sendClearTime(order.intdata);
                break;
            case mode_normal:
                //设置普通模式
                setMode_normal();
                break;
            case mode_mild:
                setMode_mild();
                break;
            case mode_middle:
                setMode_middle();
                break;
            case mode_strength:
                setMode_strength();
                break;
            case readStatuss:
                mBluetoothGatt.readCharacteristic(readCharacter);
                break;
            case shutdown:
                sendSurtDown();
                break;
            case boot:
                sendBoot();
                break;

        }
    }

    private void initGattInfo(BluetoothGattService gattService) {
        List<BluetoothGattCharacteristic> characters = gattService.getCharacteristics();
        for (BluetoothGattCharacteristic character : characters) {
            Log.e("bcz", "character_uuid:" + character.getUuid().toString());
            if (character.getUuid().toString().equals(character_Oen_Close_Number)) {
                opencloseCharacter = character;
            } else if (character.getUuid().toString().equals(character_write_Number)) {
                writeCharacter = character;
            } else if (character.getUuid().toString().equals(character_Read_Number)) {
                readCharacter = character;
            }
        }
    }

    /**
     * 关机
     */
    public void sendSurtDown() {
        byte[] data = new byte[1];
        data[0] = shutdown;
        Log.e("bcz", "data:" + data[0] + "---dataLength:" + data.length);
        dataSend(data, opencloseCharacter);
    }

    /**
     * 开机
     */
    public void sendBoot() {
        byte[] data = new byte[1];
        data[0] = boot;
        dataSend(data, opencloseCharacter);
    }

    /**
     * 读取状态
     */
    public void readStatus(IReadInfoListener readCallBack) {
        readInfoCallBack = readCallBack;
        mBluetoothGatt.readCharacteristic(readCharacter);
    }

    public void readStatusToQuee(IReadInfoListener readCallBack) {
        readInfoCallBack = readCallBack;
        addOrderToQuee(readStatuss, 0);

    }

    public interface IReadInfoListener {
        void callBack(byte[] values);
    }

    /**
     * 清洗左喷头
     */
    public void sendClearLeft() {
        byte[] data = new byte[1];
        data[0] = clearleft;
        dataSend(data, opencloseCharacter);
    }

    /**
     * 清洗右喷头
     */
    public void sendClearRight() {
        byte[] data = new byte[1];
        data[0] = clearright;
        dataSend(data, opencloseCharacter);
    }

    /**
     * 设置上电自动清洗
     */
    public void sendClearOnPower() {
        byte[] data = new byte[1];
        data[0] = setpoweronclear;
        dataSend(data, opencloseCharacter);
    }

    /**
     * 禁用上电自动清洗
     */
    public void sendForbidClearOnPower() {
        byte[] data = new byte[1];
        data[0] = forbidsetpoweronclear;
        dataSend(data, opencloseCharacter);
    }


    /**
     * 设置普通模式
     */
    public void setMode_normal() {
        byte[] data = new byte[1];
        data[0] = mode_normal;
        dataSend(data, opencloseCharacter);
    }

    /**
     * 设置智能模式_弱
     */
    public void setMode_mild() {
        byte[] data = new byte[1];
        data[0] = mode_mild;
        dataSend(data, opencloseCharacter);
    }

    /**
     * 设置智能模式_中
     */
    public void setMode_middle() {
        byte[] data = new byte[1];
        data[0] = mode_middle;
        dataSend(data, opencloseCharacter);
    }

    /**
     * 设置智能模式_强
     */
    public void setMode_strength() {
        byte[] data = new byte[1];
        data[0] = mode_strength;
        dataSend(data, opencloseCharacter);
    }

    /**
     * 设置时间间隔
     *
     * @param time 时间间隔
     */
    public void sendTimeinterval(int time) {
        byte[] data = new byte[2];
        data[0] = settimeinterval;
        data[1] = OX_ORDER[time];
        dataSend(data, writeCharacter);
    }

    /**
     * 设置喷雾强度
     *
     * @param stength 强度
     */
    public void sendStrenth(int stength) {
        try {
            byte[] data = new byte[2];
            data[0] = setstrenth;
            data[1] = OX_ORDER[stength];
            dataSend(data, writeCharacter);
        } catch (ArrayIndexOutOfBoundsException e) {

        }
    }


    /**
     * 设置清洗时长
     *
     * @param time 强度
     */
    public void sendClearTime(int time) {
        byte[] data = new byte[2];
        data[0] = setcleartime;
        data[1] = OX_ORDER[time];
        dataSend(data, writeCharacter);
    }

    /**
     * 向蓝牙发送数据
     */
    private void dataSend(byte[] data, BluetoothGattCharacteristic characteristic) {
        characteristic.setValue(data);
        Log.e("bcz", "dataSend----------------start-------------------");
        for (byte dataha : data) {
            Log.e("bcz", "dataSend--data:" + dataha);
        }
        boolean status = mBluetoothGatt.writeCharacteristic(characteristic);
        Log.e("bcz", "dataSend--status:" + status);
        Log.e("bcz", "dataSend=================end=======================");
    }


    public interface IConnectingCallback {
        void connectSuccess();

        void connectFailed();
    }

    public class Order {
        private byte order;  //指令
        private int intdata; //参数

        public byte getOrder() {
            return order;
        }

        public void setOrder(byte order) {
            this.order = order;
        }

        public int getIntdata() {
            return intdata;
        }

        public void setIntdata(int intdata) {
            this.intdata = intdata;
        }
    }

}
