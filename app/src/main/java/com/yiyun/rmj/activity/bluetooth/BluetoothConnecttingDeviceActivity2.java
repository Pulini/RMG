package com.yiyun.rmj.activity.bluetooth;

import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity2;
import com.yiyun.rmj.bean.BluetoothBean;

import org.litepal.crud.DataSupport;


//连接设备界面
public class BluetoothConnecttingDeviceActivity2 extends BaseActivity2 {

    private int deviceId;
    private BluetoothBean device;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_connect;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void initView() {
        findViewById(R.id.iv_back_white).setOnClickListener(v -> onBackPressed());
        ((TextView) findViewById(R.id.tv_title)).setText(getString(R.string.connect_device));

        deviceId = getIntent().getIntExtra("deviceId", 0);
        device = DataSupport.find(BluetoothBean.class, deviceId);

        BleManager.getInstance().connect(device.getAddress(), new BleGattCallback() {
            @Override
            public void onStartConnect() {
                runOnMainThread(() ->
                                Log.e("Pan", "onStartConnect")
//                        callback.onStartConnect()
                );
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                runOnMainThread(() -> {
                            Log.e("Pan", "onConnectFail");
                            startActivity(
                                    new Intent(BluetoothConnecttingDeviceActivity2.this, BluetoothConnectFailedActivity.class)
                                    .putExtra("deviceId", deviceId)
                            );
                            overridePendingTransition(R.anim.activity_leftclick_in, R.anim.activity_leftclick_out);
                            finish();
                        }
//                        callback.onFailedConnect()
                );
            }

            @Override
            public void onConnectSuccess(final BleDevice bleDevice, BluetoothGatt gatt, int status) {
                runOnMainThread(() -> {
                            Log.e("Pan", "onConnectSuccess:" + bleDevice.getName());

                            startActivity(
                                    new Intent(BluetoothConnecttingDeviceActivity2.this, BluetoothMainActivity3.class)
                                            .putExtra("deviceId", deviceId)
                                    .putExtra("bleDevice",bleDevice)
                            );
                            finish();
                            overridePendingTransition(R.anim.activity_rightclick_in, R.anim.activity_rightclick_out);
                        }
//                        callback.onSuccessConnect(bleDevice)
                );
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                runOnMainThread(() ->
                                Log.e("Pan", "onDisConnected:" + isActiveDisConnected + "   Device:" + bleDevice.getName())
//                        callback.onDisConnect(isActiveDisConnected, bleDevice)
                );
            }
        });

    }

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    private void runOnMainThread(Runnable runnable) {
        if (isMainThread()) {
            runnable.run();
        } else {
            if (mHandler != null) {
                mHandler.post(runnable);
            }
        }
    }

    @Override
    protected void initData() {

    }


}
