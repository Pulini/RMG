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
    public static final String character_Read_Version = "0000fff2-0000-1000-8000-00805f9b34fb"; //读特版本

    public static final byte[] OX_ORDER = {
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, (byte) 0x0a, (byte) 0x0b, (byte) 0x0c, (byte) 0x0d, (byte) 0x0e, (byte) 0x0f,
            0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, (byte) 0x1a, (byte) 0x1b, (byte) 0x1c, (byte) 0x1d, (byte) 0x1e, (byte) 0x1f,
            0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, (byte) 0x2a, (byte) 0x2b, (byte) 0x2c, (byte) 0x2d, (byte) 0x2e, (byte) 0x2f,
            0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, (byte) 0x3a, (byte) 0x3b, (byte) 0x3c, (byte) 0x3d, (byte) 0x3e, (byte) 0x3f,
            0x40, 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, (byte) 0x4a, (byte) 0x4b, (byte) 0x4c, (byte) 0x4d, (byte) 0x4e, (byte) 0x4f,
            0x50, 0x51, 0x52, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58, 0x59, (byte) 0x5a, (byte) 0x5b, (byte) 0x5c, (byte) 0x5d, (byte) 0x5e, (byte) 0x5f,
            0x60, 0x61, 0x62, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, (byte) 0x3a, (byte) 0x6b, (byte) 0x6c, (byte) 0x6d, (byte) 0x6e, (byte) 0x6f,
            0x70, 0x71, 0x72, 0x73, 0x74, 0x75, 0x76, 0x77, 0x78, 0x79, (byte) 0x7a, (byte) 0x7b, (byte) 0x7c, (byte) 0x7d, (byte) 0x7e, (byte) 0x7f,
    };


    public static final byte modeNull = -99; //开机
    public static final byte boot = 0x11; //开机
    public static final byte shutdown = 0x10; //关机  10进制：16
    public static final byte clearleft = 0x21; //清洗左喷头  10进制：33
    public static final byte clearright = 0x20; //清洗右喷头 10进制：32
    public static final byte cleanLong = 0x22; //清洗右喷头 10进制：34
    public static final byte setpoweronclear = 0x31; //设置上电自动清洗  10进制：49
    public static final byte forbidsetpoweronclear = 0x30; //禁止上电自动清洗 10进制：48
    public static final byte shortTime = 0x54; //设置短喷时间  10进制：84
    public static final byte shortStrength = 0x57; //设置短喷强度  10进制：87
    public static final byte longTime = 0x55; //设置长喷时间  10进制：87
    public static final byte longStrength = 0x56; //设置长喷强度  10进制：87
    public static final byte setcleartime = 0x58; //设置清洗时长  10进制：88
    public static final byte longOrder = 0x59; //设置长指令  10进制：89
    public static final byte readstate = 0x53;//读状态  10进制：83
    public static final byte mode_short = 0x40;//短喷  10进制：64
    public static final byte mode_auto_mild = 0x41;//智能轻度模式  10进制：65
    public static final byte mode_auto_middle = 0x42;//智能中度模式 10进制：66
    public static final byte mode_auto_strength = 0x43;//智能强度模式 10进制：67
    public static final byte mode_long = 0x44;//长喷 10进制：68
    public static final byte mode_short_long = 0x45;//长喷加短喷 10进制：69
    public static final byte state_boot = 0x01; //开机状态
    public static final byte state_shutdown = 0x00; //关机状态
    public static final byte readStatuss = 0x71; //读取状态

    private static ArrayList<Order> orderQuee; //指令队列

    public BluetoothGattService gattService;
    public BluetoothGattCharacteristic readCharacter;
    public BluetoothGattCharacteristic writeCharacter;
    public BluetoothGattCharacteristic opencloseCharacter;
    public BluetoothGattCharacteristic readVersion;
    private IReadInfoListener readInfoCallBack;
    private OnVersionListener readVersionBack;

    public static synchronized NewBleBluetoothUtil getInstance() {
        if (instance == null) {
            instance = new NewBleBluetoothUtil();
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            orderQuee = new ArrayList();
        }
        return instance;
    }

    OnBlutToothListener listener;

    public interface OnBlutToothListener {
        void onSending(byte order);

        void onSendFinish(byte value);
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
    public synchronized void addOrderToQuee(byte order, int... param) {
        for (Order b : orderQuee) {
            if (b.getOrder() == order) {
                List<Integer> list = new ArrayList<>();
                for (int i : param) {
                    list.add(i);
                }
                b.setIntdata(list);
                return;
            }
        }

        Order order1 = new Order();
        //设置指令
        order1.setOrder(order);
        //设置指令参数
        List<Integer> list = new ArrayList<>();
        for (int i : param) {
            list.add(i);
        }
        order1.setIntdata(list);
        orderQuee.add(order1);
    }

    public synchronized Order getOrderFromQuee() {
        Log.e("Pan", "orderQuee=" + orderQuee.size());
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

    public synchronized void removeAllOrder() {
        orderQuee.clear();
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

    public MyBluetoothGattCallback BluetoothListener = new MyBluetoothGattCallback();

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
            mBluetoothGatt = bleDevice.connectGatt(context, false, BluetoothListener);
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
            Log.e("Pan", "onConnectionStateChange");
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
            Log.e("Pan", "onServicesDiscovered");
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
            Log.e("Pan", "Read=" + new Gson().toJson(values));
            removeOrderOnQuee(values[0]);
            if (characteristic == readVersion) {
                readVersionBack.callBack(values);
            }

            if (characteristic == readCharacter) {
                readInfoCallBack.callBack(values);
            }
            sendOrder();
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.e("Pan", "onCharacteristicWrite");
//            if(status == BluetoothGatt.GATT_SUCCESS){
//                Log.e("Pan","写入成功");
//            }else if (status == BluetoothGatt.GATT_FAILURE) {
//                Log.e("onCharacteristicWrite中", "写入失败");
//            } else if (status == BluetoothGatt.GATT_WRITE_NOT_PERMITTED) {
//                Log.e("onCharacteristicWrite中", "没权限");
//            }


            byte[] values = characteristic.getValue();
            if (status == BluetoothGatt.GATT_SUCCESS) {//写入成功
                Log.e("Pan", "写入成功" + values[0]);
                removeOrderOnQuee(values[0]);
                currentContext.showOrderMessage(values, status);
                if (orderQuee.size() > 0) {
                    Log.e("Pan", "发送下一条协议");
                    sendOrder();
                }
                if (orderQuee.size() == 0) {
                    if (listener != null) {
                        LogUtils.LogE("onSendFinish-----------------");
                        listener.onSendFinish(values[0]);
                    }
                }
            } else if (status == BluetoothGatt.GATT_FAILURE) {
                Log.e("Pan", "写入失败" + values[0]);
                sendOrder();
            } else if (status == BluetoothGatt.GATT_WRITE_NOT_PERMITTED) {
                Log.e("Pan", "没权限");
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
            Log.e("Pan", "onCharacteristicChanged");
        }
    }


   /* public void sendOrders() {
        new Thread(() -> {
            for (Order order : orderQuee) {
                send(order);
                SystemClock.sleep(300);
            }
            orderQuee.clear();
            if (listener != null) {
                LogUtils.LogE("onSendFinish-----------------");
                listener.onSendFinish();
            }
        }).start();

    }

    private void send(Order order) {
        byte[] data;
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
            case shortTime:
                //设置短喷时间
                sendShortTime(order.getIntdata().get(0));
                break;
            case shortStrength:
                //设置短喷强度
                sendShortStrenth(order.getIntdata().get(0));
                break;
            case longTime:
                //设置短喷强度
                sendLongTime(order.getIntdata().get(0));
                break;
            case longStrength:
                //设置短喷强度
                sendLongStrenth(order.getIntdata().get(0));
                break;
            case setcleartime:
                //设置清洗时长
                sendClearTime(order.getIntdata().get(0));
                break;
            case mode_short:
                setMode_Short();
                break;
            case mode_long:
                setMode_Long();
                break;
            case mode_short_long:
                setMode_ShortLong();
                break;
            case mode_auto_mild:
                setMode_mild();
                break;
            case mode_auto_middle:
                setMode_middle();
                break;
            case mode_auto_strength:
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
            case longOrder:
                sendLongOrder(order.getIntdata());
                break;

        }
    }
*/

    /**
     * 从队列中获取指令进行发送
     */
    public void sendOrder() {
        Log.e("bcz", "start to sendOrder");
        Order order = getOrderFromQuee();
        if (order == null) {
            return;
        }
        if (listener != null) {
            LogUtils.LogE("onStartSend-----------------");
            listener.onSending(order.getOrder());
        }
        Log.e("bcz", "Order is :" + new Gson().toJson(order.getOrder()));

        switch (order.getOrder()) {
            case clearleft:
                //清洗左喷头
                sendClearLeft();
                break;
            case clearright:
                //清洗右喷头
                sendClearRight();
                break;
            case cleanLong:
                //长喷
                sendCleanLong();
                break;
            case setpoweronclear:
                //设置上电自动清洗
                sendClearOnPower();
                break;
            case forbidsetpoweronclear:
                //禁止上电自动清洗
                sendForbidClearOnPower();
                break;
            case shortTime:
                //设置短喷时间
                sendShortTime(order.getIntdata().get(0));
                break;
            case shortStrength:
                //设置短喷强度
                sendShortStrenth(order.getIntdata().get(0));
                break;
            case longTime:
                //设置短喷强度
                sendLongTime(order.getIntdata().get(0));
                break;
            case longStrength:
                //设置短喷强度
                sendLongStrenth(order.getIntdata().get(0));
                break;
            case setcleartime:
                //设置清洗时长
                sendClearTime(order.getIntdata().get(0));
                break;
            case mode_short:
                setMode_Short();
                break;
            case mode_long:
                setMode_Long();
                break;
            case mode_short_long:
                setMode_ShortLong();
                break;
            case mode_auto_mild:
                setMode_mild();
                break;
            case mode_auto_middle:
                setMode_middle();
                break;
            case mode_auto_strength:
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
            case longOrder:
                sendLongOrder(order.getIntdata());
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
            } else if (character.getUuid().toString().equals(character_Read_Version)) {
                readVersion = character;
            }
        }
    }

    public byte getModel(int model) {
        if (model == mode_short) {
            return mode_short;
        } else if (model == mode_auto_mild) {
            return mode_auto_mild;
        } else if (model == mode_auto_middle) {
            return mode_auto_middle;
        } else if (model == mode_auto_strength) {
            return mode_auto_strength;
        } else if (model == mode_long) {
            return mode_long;
        } else if (model == mode_short_long) {
            return mode_short_long;
        } else {
            return mode_short;
        }
    }

    public void sendLongOrder(List<Integer> intdata) {
        byte[] data = new byte[8];
        data[0] = longOrder;
        data[1] = OX_ORDER[intdata.get(0)];
        data[2] = OX_ORDER[intdata.get(1)];
        data[3] = OX_ORDER[intdata.get(2)];
        data[4] = OX_ORDER[intdata.get(3)];
        data[5] = OX_ORDER[intdata.get(4)];
        data[6] = OX_ORDER[intdata.get(5)];
        data[7] = OX_ORDER[intdata.get(6)];
//        data[7] =getModel(intdata.get(6));


        dataSend(data, writeCharacter);
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
//        mBluetoothGatt.setCharacteristicNotification(readCharacter,true);
        Log.e("Pan", "开始读取");
        mBluetoothGatt.readCharacteristic(readCharacter);
    }

    /**
     * 读取版本号
     */
    public void readVersion(OnVersionListener readCallBack) {
        readVersionBack = readCallBack;
        mBluetoothGatt.readCharacteristic(readVersion);
    }

    public void readStatusToQuee(IReadInfoListener readCallBack) {
        readInfoCallBack = readCallBack;
        addOrderToQuee(readStatuss, 0);

    }

    public interface IReadInfoListener {
        void callBack(byte[] values);
    }

    public interface OnVersionListener {
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
     * 长喷
     */
    public void sendCleanLong() {
        byte[] data = new byte[1];
        data[0] = cleanLong;
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
     * 设置短喷
     */
    public void setMode_Short() {
        byte[] data = new byte[1];
        data[0] = mode_short;
        dataSend(data, opencloseCharacter);
    }

    /**
     * 设置短喷
     */
    public void setMode_Long() {
        byte[] data = new byte[1];
        data[0] = mode_long;
        dataSend(data, opencloseCharacter);
    }

    /**
     * 设置短喷+长喷
     */
    public void setMode_ShortLong() {
        byte[] data = new byte[1];
        data[0] = mode_short_long;
        dataSend(data, opencloseCharacter);
    }

    /**
     * 设置智能模式_弱
     */
    public void setMode_mild() {
        byte[] data = new byte[1];
        data[0] = mode_auto_mild;
        dataSend(data, opencloseCharacter);
    }

    /**
     * 设置智能模式_中
     */
    public void setMode_middle() {
        byte[] data = new byte[1];
        data[0] = mode_auto_middle;
        dataSend(data, opencloseCharacter);
    }

    /**
     * 设置智能模式_强
     */
    public void setMode_strength() {
        byte[] data = new byte[1];
        data[0] = mode_auto_strength;
        dataSend(data, opencloseCharacter);
    }

    /**
     * 设置时间间隔
     *
     * @param time 时间间隔
     */
    public void sendShortTime(int time) {
        byte[] data = new byte[2];
        data[0] = shortTime;
        data[1] = OX_ORDER[time];
        dataSend(data, writeCharacter);
    }

    /**
     * 设置喷雾强度
     *
     * @param stength 强度
     */
    public void sendShortStrenth(int stength) {
        try {
            byte[] data = new byte[2];
            data[0] = shortStrength;
            data[1] = OX_ORDER[stength];
            dataSend(data, writeCharacter);
        } catch (ArrayIndexOutOfBoundsException e) {

        }
    }

    /**
     * 设置时间间隔
     *
     * @param time 时间间隔
     */
    public void sendLongTime(int time) {
        byte[] data = new byte[2];
        data[0] = longTime;
        data[1] = OX_ORDER[time];
        dataSend(data, writeCharacter);
    }

    /**
     * 设置喷雾强度
     *
     * @param stength 强度
     */
    public void sendLongStrenth(int stength) {
        try {
            byte[] data = new byte[2];
            data[0] = longStrength;
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
//        characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
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
        private List<Integer> intdata; //参数

        public byte getOrder() {
            return order;
        }

        public void setOrder(byte order) {
            this.order = order;
        }

        public List<Integer> getIntdata() {
            return intdata;
        }

        public void setIntdata(List<Integer> intdata) {
            this.intdata = intdata;
        }
    }

}
