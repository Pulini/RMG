package com.yiyun.rmj.activity.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hjq.toast.ToastUtils;
import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.BluetoothBean;
import com.yiyun.rmj.bluetooth.IPinBlueCallBack;
import com.yiyun.rmj.bluetooth.NewBleBluetoothUtil;
import com.yiyun.rmj.receiver.PinBlueReceiver;
import com.yiyun.rmj.utils.LogUtils;

import org.litepal.crud.DataSupport;


//连接设备界面
public class BluetoothConnecttingDeviceActivity extends BaseActivity implements View.OnClickListener {

    private boolean isActivityOnline = false;
    private int deviceId;
    private BluetoothBean device;
    PinBlueReceiver receiver;
    Handler myHandler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what){
                case 1:
                    Log.e("bcz","startActivity---failedConnect");
                    //连接失败
                    Intent connectFailedIntent = new Intent(BluetoothConnecttingDeviceActivity.this, BluetoothConnectFailedActivity.class);
                    connectFailedIntent.putExtra("deviceId",deviceId);
                    startActivity(connectFailedIntent);
                    overridePendingTransition(R.anim.activity_leftclick_in, R.anim.activity_leftclick_out);
                    finish();
                    break;
                case 2:

                    BluetoothDevice bleDevice =  NewBleBluetoothUtil.getInstance().getBleDeviceByAddress(device.getAddress());
                    Log.e("bcz","bleDevice:state::" + bleDevice.getBondState());
//                    if(bleDevice.getBondState() != 12){
//                        Toast.makeText(BluetoothConnecttingDeviceActivity.this,"请先完成设备的配对，若没有弹出配对框或上方通知栏中未收到配对请求，则请到手机的设置->蓝牙中进行搜索配对",Toast.LENGTH_LONG).show();
//                        NewBleBluetoothUtil.getInstance().createBond(bleDevice);
//                    }else{
                        //fortest
//                        NewBleBluetoothUtil.getInstance().removeBond(bleDevice);
                        //连接成功
                        Intent intent = new Intent(BluetoothConnecttingDeviceActivity.this, BluetoothMainActivity2.class);
//                        Intent intent = new Intent(BluetoothConnecttingDeviceActivity.this, BluetoothActivity.class);
                        intent.putExtra("deviceId",deviceId);
                        startActivity(intent);
                        finish();
                        BluetoothConnecttingDeviceActivity.this.overridePendingTransition(R.anim.activity_rightclick_in, R.anim.activity_rightclick_out);
//                    }
                    Log.e("bcz","startActivity---successConnect");
                    break;
                case 3:
                    //连接成功
                    Intent intent2 = new Intent(BluetoothConnecttingDeviceActivity.this, BluetoothMainActivity2.class);
//                    Intent intent2 = new Intent(BluetoothConnecttingDeviceActivity.this, BluetoothActivity.class);
                    intent2.putExtra("deviceId",deviceId);
                    startActivity(intent2);
                    finish();
                    BluetoothConnecttingDeviceActivity.this.overridePendingTransition(R.anim.activity_rightclick_in, R.anim.activity_rightclick_out);
                    break;
            }

        }
    };


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.iv_back_white:
                myHandler.sendEmptyMessage(1);
                //fortest
//                myHandler.sendEmptyMessage(2);
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_connect;
    }

    public void initTitleView(){
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back_white);
        iv_back.setOnClickListener(this);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.connect_device));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void initView() {
        isActivityOnline = true;
        initTitleView();
        deviceId = getIntent().getIntExtra("deviceId",0);
        device = DataSupport.find(BluetoothBean.class,deviceId);

        receiver = new PinBlueReceiver(new IPinBlueCallBack(){
            @Override
            public void onBondRequest() {

            }

            @Override
            public void onBondFail(BluetoothDevice device) {
                ToastUtils.show("配对失败");
                myHandler.sendEmptyMessage(1);
            }

            @Override
            public void onBonding(BluetoothDevice device) {

            }

            @Override
            public void onBondSuccess(BluetoothDevice device) {
                ToastUtils.show("配对成功");
                myHandler.sendEmptyMessage(3);
            }
        });

        IntentFilter counterActionFilter = new IntentFilter();
        counterActionFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(receiver,counterActionFilter);


        NewBleBluetoothUtil.getInstance().connectDevice(this, device.getAddress(), new NewBleBluetoothUtil.IConnectingCallback() {
            @Override
            public void connectSuccess() {
                LogUtils.LogE("BluetoothConnectting--connectSuccess");
                if(isActivityOnline){
                    myHandler.sendEmptyMessage(2);
                }
            }

            @Override
            public void connectFailed() {
                LogUtils.LogE("BluetoothConnectting--connectFailed");
                if(isActivityOnline){
                    myHandler.sendEmptyMessage(1);
                }
            }
        });


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(BluetoothConnecttingDeviceActivity.this, BluetoothMainActivity.class);
//                intent.putExtra("deviceId",deviceId);
//                startActivity(intent);
//                finish();
//            }
//        },3000);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActivityOnline = false;
        if(receiver != null){
            unregisterReceiver(receiver);
        }
    }
}
