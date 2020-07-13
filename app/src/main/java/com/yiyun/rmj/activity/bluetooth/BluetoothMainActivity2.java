package com.yiyun.rmj.activity.bluetooth;

import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bluetooth.NewBleBluetoothUtil;
import com.yiyun.rmj.view.ElectricView;

/**
 * File Name : BluetoothMainActivity2
 * Created by : PanZX on 2020/07/13
 * Email : 644173944@qq.com
 * Github : https://github.com/Pulini
 * Remark：
 */
public class BluetoothMainActivity2 extends BaseActivity {


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

        iv_add.setOnClickListener(view -> {

        });

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


    @Override
    protected void initData() {
        deviceId = getIntent().getIntExtra("deviceId", 0);
    }


}
