package com.yiyun.rmj.activity.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.yiyun.rmj.R;
import com.yiyun.rmj.adapter.CommonRecyclerViewAdapter;
import com.yiyun.rmj.adapter.CommonRecyclerViewHolder;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.base.BaseActivity2;
import com.yiyun.rmj.bluetooth.BLEUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


//添加蓝牙设备页面
public class BluetoothAddDeviceActivity2 extends BaseActivity2 {


    ArrayList<BluetoothDevice> deviceList = new ArrayList<>();   //设备列表
    RecyclerView rv_list; //设备列表控件
    CommonRecyclerViewAdapter<BluetoothDevice> adapter;   //设备列表控件适配器
    RelativeLayout relativeLayout;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_device;
    }

    @Override
    protected void initView() {
        findViewById(R.id.iv_back_white).setOnClickListener(view -> onBackPressed());
        ((TextView) findViewById(R.id.tv_title)).setText(getString(R.string.add_device));
        relativeLayout = (RelativeLayout) findViewById(R.id.rl_progress);
        relativeLayout.setVisibility(View.GONE);
        rv_list = (RecyclerView) findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new LinearLayoutManager(this));


        adapter = new CommonRecyclerViewAdapter<BluetoothDevice>(this, deviceList) {
            @Override
            public void convert(CommonRecyclerViewHolder h, BluetoothDevice entity, int position) {
                TextView tv = h.getView(R.id.tv_device_bh);
                tv.setText(entity.getName());
            }

            @Override
            public int getLayoutViewId(int viewType) {
                return R.layout.item_select_device;
            }
        };
        //设置item监听事件
        adapter.setOnRecyclerViewItemClickListener((v, position) -> {
//                bluetoothUtil.scanLeDevice(false,scanCallback);
            Intent intent = getIntent();
            intent.putExtra("device", deviceList.get(position));
            setResult(RESULT_OK, intent);
            finish();
        });
        rv_list.setAdapter(adapter);

        BleManager.getInstance().initScanRule(new BleScanRuleConfig.Builder()
                .setServiceUuids(new UUID[]{UUID.fromString(BLEUtil.BLE_serviceUUid)})
                .setScanTimeOut(10000)
                .build());

        findViewById(R.id.btn_start_scan).setOnClickListener(view -> {
            deviceList.clear();
            BleManager.getInstance().scan(new BleScanCallback() {
                @Override
                public void onScanStarted(boolean success) {
                    relativeLayout.setVisibility(View.VISIBLE);
                    Log.e("Pan","onScanStarted:"+success);
                }

                @Override
                public void onLeScan(BleDevice bleDevice) {
                    Log.e("Pan","onLeScan:"+bleDevice.getName());
                }

                @Override
                public void onScanning(BleDevice bleDevice) {
                    deviceList.add(bleDevice.getDevice());
                    adapter.notifyDataSetChanged();
                    Log.e("Pan","onScanning:"+bleDevice.getName());
                }

                @Override
                public void onScanFinished(List<BleDevice> scanResultList) {
                    relativeLayout.setVisibility(View.GONE);
                    Log.e("Pan","onScanFinished");
                    for (BleDevice bleDevice : scanResultList) {
                        Log.e("Pan","bleDevice=:"+bleDevice.getName());
                    }
                }
            });
        });
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        BleManager.getInstance().cancelScan();
        overridePendingTransition(R.anim.activity_leftclick_in, R.anim.activity_leftclick_out);
    }
}
