package com.yiyun.rmj.activity.bluetooth;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;

public class BluetoothConnectFailedActivity extends BaseActivity implements View.OnClickListener {

    public int deviceId;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back_white:
                Intent selectIntent = new Intent(BluetoothConnectFailedActivity.this, BluetoothSelectDeviceActivity.class);
                selectIntent.putExtra("deviceId",deviceId);
                startActivity(selectIntent);
                overridePendingTransition(R.anim.activity_leftclick_in, R.anim.activity_leftclick_out);
                finish();
                break;
            case R.id.btn_reconnecting:
                //重新连接按钮
                Intent connectingIntent = new Intent(BluetoothConnectFailedActivity.this, BluetoothConnecttingDeviceActivity.class);
                connectingIntent.putExtra("deviceId",deviceId);
                startActivity(connectingIntent);
                overridePendingTransition(R.anim.activity_rightclick_in, R.anim.activity_rightclick_out);
                finish();


                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bluetooth_connectingfailed;
    }

    public void initTitleView(){
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back_white);
        iv_back.setOnClickListener(this);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.connecting_failed));
    }

    @Override
    protected void initView() {
        initTitleView();
        deviceId = getIntent().getIntExtra("deviceId",0);
        Button btn_reconnecting = (Button) findViewById(R.id.btn_reconnecting);
        btn_reconnecting.setOnClickListener(this);

    }

    @Override
    protected void initData() {

    }
}
