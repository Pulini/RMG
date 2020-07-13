package com.yiyun.rmj.activity.bluetooth;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.BluetoothBean;
import com.yiyun.rmj.bean.BluetoothSettingBean;
import com.yiyun.rmj.bluetooth.NewBleBluetoothUtil;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.utils.SpfUtils;
import com.yiyun.rmj.view.ElectricView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

/**
 * File Name : BluetoothMainActivity2
 * Created by : PanZX on 2020/07/13
 * Email : 644173944@qq.com
 * Github : https://github.com/Pulini
 * Remark：
 */
public class BluetoothMainActivity2 extends BaseActivity {

    public static final int TYPE_ADD = 0;
    public static final int TYPE_MODIFY = 1;

    NewBleBluetoothUtil bluetoothUtil;
    private ImageView iv_add;
    private ImageView iv_back;
    private ElectricView ev_electricityQuantity;
    private TextView tv_power;
    private TextView tv_bootState;
    private Switch sw_bootSwitch;
    private TextView tv_deviceName;
    private SwipeMenuRecyclerView rv_deviceList;
    private Button bt_cleanLeft;
    private Button bt_cleanRight;


    private int deviceId = 0;
    private ArrayList<BluetoothSettingBean> listData = new ArrayList<>();
    private BluetoothBean device;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_bluetooth_main2;
    }

    @Override
    protected void initView() {
        findView();
        setClick();
        bluetoothUtil = NewBleBluetoothUtil.getInstance();

    }

    @Override
    protected void initData() {
        deviceId = getIntent().getIntExtra("deviceId", 0);
        Log.e("Pan", "deviceId=" + deviceId);
        device = SpfUtils.getBluetoothSetList(deviceId);
        if (device == null) {
            LogUtils.LogE("2次读取");
            device = DataSupport.find(BluetoothBean.class, deviceId);
        }
        Log.e("Pan", "device=" + new Gson().toJson(device));
        tv_deviceName.setText(device.getDeviceName());
        readStatus();

    }

    public void findView() {
        iv_add = findViewById(R.id.iv_title_blue_add);
        iv_back = findViewById(R.id.iv_back);
        iv_add.setVisibility(View.VISIBLE);
        iv_back.setVisibility(View.VISIBLE);

        ev_electricityQuantity = findViewById(R.id.ev_electricityQuantity);
        tv_power = findViewById(R.id.tv_power);
        tv_bootState = findViewById(R.id.tv_bootState);
        sw_bootSwitch = findViewById(R.id.sw_bootSwitch);
        tv_deviceName = findViewById(R.id.tv_deviceName);
        rv_deviceList = findViewById(R.id.rv_deviceList);
        bt_cleanLeft = findViewById(R.id.bt_cleanLeft);
        bt_cleanRight = findViewById(R.id.bt_cleanRight);

    }

    public void setClick() {
        iv_back.setOnClickListener(view -> {
            bluetoothUtil.disconnectDevice();
            finish();
        });

        iv_add.setOnClickListener(view ->
            startActivityForResult(
                    new Intent(this, BluetoothControlDetailActivity2.class).putExtra("type", TYPE_ADD),
                    TYPE_ADD
            )
        );

        sw_bootSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            tv_bootState.setText(b ? "关机" : "开机");
            bluetoothUtil.addOrderToQuee(b ? NewBleBluetoothUtil.boot : NewBleBluetoothUtil.shutdown, 0);
            bluetoothUtil.sendOrder();
        });

        bt_cleanLeft.setOnClickListener(view -> {
            bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.clearleft, 0);
            bluetoothUtil.sendOrder();
        });

        bt_cleanRight.setOnClickListener(view -> {
            bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.clearright, 0);
            bluetoothUtil.sendOrder();
        });


    }

    private void readStatus() {
        bluetoothUtil.readStatus(values -> {
            //0x53+【开机状态】+【剩余电量】+【清洗时长】+【短喷间隔时间】+【短喷喷雾强度】+【开机清洗使能】 +【工作模式】+【长喷间隔时间】+【长喷喷雾强度】。
            runOnUiThread(() -> {
                if (values.length != 0) {
                    BluetoothModel bm = new BluetoothModel(values);
                    ev_electricityQuantity.setCount(bm.getElectricityQuantity() / 10);
                    tv_power.setText(bm.getElectricityQuantity() + "%");
                    Log.e("Pan", bm.getState() == NewBleBluetoothUtil.state_boot ? "开机" : "关机");
                    tv_bootState.setText(bm.getState() == NewBleBluetoothUtil.state_boot ? "关机" : "开机");
                    sw_bootSwitch.setChecked(bm.getState() == NewBleBluetoothUtil.state_boot);
                }
            });
        });

    }

}
