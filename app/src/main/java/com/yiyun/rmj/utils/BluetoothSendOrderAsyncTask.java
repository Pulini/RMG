package com.yiyun.rmj.utils;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import com.yiyun.rmj.bluetooth.NewBleBluetoothUtil;

import java.util.ArrayList;
import java.util.List;

public class BluetoothSendOrderAsyncTask extends AsyncTask<ArrayList<NewBleBluetoothUtil.Order>, String, String> {
    private BluetoothGatt mBluetoothGatt;
    private OnOrderSendListener listener;

    public  interface OnOrderSendListener {
        void End();
    }

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
    public static final byte shortTime = 0x54; //设置短喷时间  10进制：84
    public static final byte shortStrength = 0x57; //设置短喷强度  10进制：87
    public static final byte longTime = 0x55; //设置长喷时间  10进制：87
    public static final byte longStrength = 0x56; //设置长喷强度  10进制：87
    public static final byte setcleartime = 0x58; //设置清洗时长  10进制：88
    public static final byte readstate = 0x53;//读状态  10进制：83
    public static final byte mode_short = 0x40;//短喷  10进制：64
    public static final byte mode_mild = 0x41;//智能轻度模式  10进制：65
    public static final byte mode_middle = 0x42;//智能中度模式 10进制：66
    public static final byte mode_strength = 0x43;//智能强度模式 10进制：67
    public static final byte mode_long = 0x44;//长喷 10进制：68
    public static final byte mode_short_long = 0x45;//长喷加短喷 10进制：69
    public static final byte state_boot = 0x01; //开机状态
    public static final byte state_shutdown = 0x00; //关机状态
    public static final byte readStatuss = 0x71; //读取状态

    public static final String serviceUUid = "0000fff0-0000-1000-8000-00805f9b34fb"; //蓝牙服务号
    public static final String character_Oen_Close_Number = "0000fff1-0000-1000-8000-00805f9b34fb"; //开关量命令特征号
    public static final String character_write_Number = "0000fff5-0000-1000-8000-00805f9b34fb"; //写特征号
    public static final String character_Read_Number = "0000fff6-0000-1000-8000-00805f9b34fb"; //读特征号
    public static final String character_Read_Version = "0000fff2-0000-1000-8000-00805f9b34fb"; //读特版本

    public BluetoothGattCharacteristic readCharacter;
    public BluetoothGattCharacteristic writeCharacter;
    public BluetoothGattCharacteristic opencloseCharacter;
    public BluetoothGattCharacteristic readVersion;

    public BluetoothSendOrderAsyncTask(BluetoothGatt bluetoothGatt, BluetoothGattService gattService, OnOrderSendListener listener) {
        this.listener = listener;
        this.mBluetoothGatt = bluetoothGatt;
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

    private void send(NewBleBluetoothUtil.Order order) {
        byte[] data;
        switch (order.getOrder()) {
            case clearleft:
                //清洗左喷头
                data = new byte[1];
                data[0] = clearleft;
                dataSend(data, opencloseCharacter);
                break;
            case clearright:
                //清洗右喷头
                data = new byte[1];
                data[0] = clearright;
                dataSend(data, opencloseCharacter);
                break;
            case setpoweronclear:
                //设置上电自动清洗
                data = new byte[1];
                data[0] = setpoweronclear;
                dataSend(data, opencloseCharacter);
                break;
            case forbidsetpoweronclear:
                //禁止上电自动清洗
                data = new byte[1];
                data[0] = forbidsetpoweronclear;
                dataSend(data, opencloseCharacter);
                break;
            case shortTime:
                //设置短喷时间
                data = new byte[2];
                data[0] = shortTime;
                data[1] = OX_ORDER[order.getIntdata()];
                dataSend(data, writeCharacter);
                break;
            case shortStrength:
                //设置短喷强度
                data = new byte[2];
                data[0] = shortStrength;
                data[1] = OX_ORDER[order.getIntdata()];
                dataSend(data, writeCharacter);
                break;
            case longTime:
                //设置短喷强度
                data = new byte[2];
                data[0] = longTime;
                data[1] = OX_ORDER[order.getIntdata()];
                dataSend(data, writeCharacter);
                break;
            case longStrength:
                //设置短喷强度
                data = new byte[2];
                data[0] = longStrength;
                data[1] = OX_ORDER[order.getIntdata()];
                dataSend(data, writeCharacter);
                break;
            case setcleartime:
                //设置清洗时长
                data = new byte[2];
                data[0] = setcleartime;
                data[1] = OX_ORDER[order.getIntdata()];
                dataSend(data, writeCharacter);
                break;
            case mode_short:
                //设置普通模式
                data = new byte[1];
                data[0] = mode_short;
                dataSend(data, opencloseCharacter);
                break;
            case mode_long:
                data = new byte[1];
                data[0] = mode_long;
                dataSend(data, opencloseCharacter);
                break;
            case mode_short_long:
                data = new byte[1];
                data[0] = mode_short_long;
                dataSend(data, opencloseCharacter);
                break;
            case mode_mild:
                data = new byte[1];
                data[0] = mode_mild;
                dataSend(data, opencloseCharacter);
                break;
            case mode_middle:
                data = new byte[1];
                data[0] = mode_middle;
                dataSend(data, opencloseCharacter);
                break;
            case shutdown:
                data = new byte[1];
                data[0] = shutdown;
                dataSend(data, opencloseCharacter);
                break;
            case boot:
                data = new byte[1];
                data[0] = boot;
                dataSend(data, opencloseCharacter);
                break;

            case readStatuss:
                mBluetoothGatt.readCharacteristic(readCharacter);
                break;

        }
    }

    private void dataSend(byte[] data, BluetoothGattCharacteristic characteristic) {
        characteristic.setValue(data);
        Log.e("Pan", "dataSend----------------start-------------------");
        for (byte dataha : data) {
            Log.e("Pan", "dataSend--data:" + dataha);
        }
        mBluetoothGatt.writeCharacteristic(characteristic);
        Log.e("Pan", "dataSend=================end=======================");
    }

    @Override
    protected String doInBackground(ArrayList<NewBleBluetoothUtil.Order>... arrayLists) {
        List<NewBleBluetoothUtil.Order> list = arrayLists[0];
        for(NewBleBluetoothUtil.Order order:list){
            send(order);
            SystemClock.sleep(300);
        }
        return "下发完成";
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(String s) {
        listener.End();
    }

}
