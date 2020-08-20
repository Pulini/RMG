package com.yiyun.rmj.bluetooth;

/**
 * File Name : BLEUtil
 * Created by : PanZX on  2020/08/20 16:55
 * Email : 644173944@qq.com
 * Github : https://github.com/Pulini
 * Remark：
 */
public class BLEUtil {
    public static final String BLE_serviceUUid = "0000fff0-0000-1000-8000-00805f9b34fb"; //蓝牙服务号
    public static final String BLE_character_Oen_Close_Number = "0000fff1-0000-1000-8000-00805f9b34fb"; //开关量命令特征号
    public static final String BLE_character_write_Number = "0000fff5-0000-1000-8000-00805f9b34fb"; //写特征号
    public static final String BLE_character_Read_Number = "0000fff6-0000-1000-8000-00805f9b34fb"; //读特征号
    public static final String BLE_character_Read_Version = "0000fff2-0000-1000-8000-00805f9b34fb"; //读特版本

    public static final byte modeNull = -99; //开机
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
    public static final byte longOrder = 0x59; //设置清洗指令  10进制：89
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
}
