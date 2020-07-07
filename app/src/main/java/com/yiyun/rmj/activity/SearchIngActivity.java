package com.yiyun.rmj.activity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bluetooth.BaseBluetoothUtil;
import com.yiyun.rmj.bluetooth.IScanBlueCallback;
import com.yiyun.rmj.receiver.ScanBlueReceiver;

import java.util.ArrayList;

public class SearchIngActivity extends BaseActivity implements IScanBlueCallback {

    private Runnable runnable;
    Handler handler = new Handler();
    ScanBlueReceiver scanBlueReceiver;
    ArrayList<BluetoothDevice> list_conactdevice;//蓝牙列表数组
    BaseBluetoothUtil blueUtil;
    private boolean isPushBackBtn = false;//是否按了返回按钮

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_ing;
    }

    @Override
    protected void initView() {
        TextView tv_tile = findViewById(R.id.tv_title);
        ImageView back =  findViewById(R.id.iv_back);
        back.setImageResource(R.mipmap.quxiao);
        back.setScaleType(ImageView.ScaleType.CENTER);
        tv_tile.setText("正在搜索");

        ProgressBar progress = (ProgressBar) findViewById(R.id.pb_Circle);

//        dialog = new QMUITipDialog2.Builder(this)
//                .setIconType(QMUITipDialog2.Builder.ICON_TYPE_SUCCESS)
//                .setTipWord("修改成功")
//                .create();
//        dialog.show();
//        CustomProgressDialog dialog = new CustomProgressDialog(this);
//        dialog.show();


//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent();
//                intent.setClassName(SearchIngActivity.this, "com.yiyun.rmj.activity.ConnectableDevicesActivity");
//                startActivity(intent);
//                SearchIngActivity.this.overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
//                finish();
//            }
//        };


//        handler.postDelayed(runnable, 5000);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPushBackBtn = true;
                blueUtil.cancelScanBule();
                Intent intent = new Intent();
                intent.setClassName(SearchIngActivity.this, "com.yiyun.rmj.activity.SearchFailActivity");
                startActivity(intent);
//                SearchIngActivity.this.overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
                finish();
//                handler.removeCallbacks(runnable);


            }
        });

    }

    public void registReceiver()
    {

        IntentFilter filter1 = new IntentFilter(android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        IntentFilter filter2 = new IntentFilter(android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(scanBlueReceiver,filter1);
        registerReceiver(scanBlueReceiver,filter2);
        registerReceiver(scanBlueReceiver,filter3);

    }

    public void unregistReceiver()
    {
        unregisterReceiver(scanBlueReceiver);
    }

    @Override
    protected void initData() {

        list_conactdevice = new ArrayList<>();
        scanBlueReceiver = new ScanBlueReceiver(this);
        registReceiver();

        blueUtil = getBluetoolthUtil();
        blueUtil.scanBlue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregistReceiver();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onScanStarted() {

    }

    @Override
    public void onScanFinished() {
        if(isPushBackBtn)
        return;
        if (list_conactdevice.size() != 0)
        {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("devices",list_conactdevice);
            intent.setClassName(SearchIngActivity.this, "com.yiyun.rmj.activity.ConnectableDevicesActivity");
            startActivity(intent);
//            SearchIngActivity.this.overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
            finish();
        }else
        {
            Intent intent = new Intent();
            intent.setClassName(SearchIngActivity.this, "com.yiyun.rmj.activity.SearchFailActivity");
            startActivity(intent);
//            SearchIngActivity.this.overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
            finish();
        }

    }

    @Override
    public void onScanning(BluetoothDevice blueDevice) {
        list_conactdevice.add(blueDevice);
    }
}
