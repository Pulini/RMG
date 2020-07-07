package com.yiyun.rmj.activity.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.toast.ToastUtils;
import com.yiyun.rmj.R;
import com.yiyun.rmj.adapter.CommonRecyclerViewAdapter;
import com.yiyun.rmj.adapter.CommonRecyclerViewHolder;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bluetooth.NewBleBluetoothUtil;
import com.yiyun.rmj.utils.PermissionUtil;

import java.util.ArrayList;

//添加蓝牙设备页面
public class BluetoothAddDeviceActivity extends BaseActivity implements View.OnClickListener {

    NewBleBluetoothUtil bluetoothUtil;    //蓝牙操作类
    BluetoothAdapter.LeScanCallback scanCallback;  //扫描蓝牙的回调函数
    public final int REQUEST_CODE_OPEN_GPS = 1;     //打开GPS请求码
    public final int REQUEST_CODE_PERMISSION_LOCATION = 2;//打开位置信息请求码
    ArrayList<BluetoothDevice> deviceList;   //设备列表
    RecyclerView rv_list; //设备列表控件
    CommonRecyclerViewAdapter<BluetoothDevice> adapter;   //设备列表控件适配器
    RelativeLayout relativeLayout;
    private boolean isScan = false; //蓝牙是否正在扫描

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back_white:
                finish();
                BluetoothAddDeviceActivity.this.overridePendingTransition(R.anim.activity_leftclick_in,R.anim.activity_leftclick_out);
                break;
            case R.id.btn_start_scan:
                deviceList.clear();
                if(!isScan){
                    isScan = true;
                    PermissionUtil.requestGpsPermission(BluetoothAddDeviceActivity.this, new PermissionUtil.IRequestPermissionCallBack() {
                        @Override
                        public void permissionSuccess() {
                            if(bluetoothUtil.isSupportBlue()){
                                if(bluetoothUtil.isBlueEnable()){
                                    relativeLayout.setVisibility(View.VISIBLE);
                                    NewBleBluetoothUtil.getInstance().scanLeDevice(true,scanCallback);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            NewBleBluetoothUtil.getInstance().scanLeDevice(false,scanCallback);
                                            relativeLayout.setVisibility(View.GONE);
                                            isScan = false;
                                            if(deviceList.size() == 0){
                                                ToastUtils.show("未搜索到设备，请重新搜索");
                                            }
                                        }
                                    },15000);
                                }else{
                                    bluetoothUtil.openBlueSync(BluetoothAddDeviceActivity.this,NewBleBluetoothUtil.OPEN_BLUETOOTH_REQUEST_CODE);
                                }
                            }else{
                                Toast.makeText(BluetoothAddDeviceActivity.this,"该设备不支持蓝牙功能！",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_device;
    }

    public void initTitleView(){
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back_white);
        iv_back.setOnClickListener(this);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.add_device));

    }

    @Override
    protected void initView() {
        initTitleView();
        bluetoothUtil = NewBleBluetoothUtil.getInstance();
        deviceList = new ArrayList<>();
        Button btn_start_scan = (Button) findViewById(R.id.btn_start_scan);
        btn_start_scan.setOnClickListener(this);
        relativeLayout = (RelativeLayout) findViewById(R.id.rl_progress);
        relativeLayout.setVisibility(View.GONE);
        rv_list = (RecyclerView) findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommonRecyclerViewAdapter<BluetoothDevice>(this,deviceList) {
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
        adapter.setOnRecyclerViewItemClickListener(new CommonRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                bluetoothUtil.scanLeDevice(false,scanCallback);
                Intent intent = getIntent();
                intent.putExtra("device",deviceList.get(position));
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        rv_list.setAdapter(adapter);

        //saomiao
        scanCallback = new BluetoothAdapter.LeScanCallback() {

            @Override
            public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {

                if(bluetoothDevice.getType() == BluetoothDevice.TRANSPORT_LE){
                    boolean isSameDevice = false;
                    for(BluetoothDevice device:deviceList){
                        if(bluetoothDevice.getAddress().equals(device.getAddress()))
                        {
                            isSameDevice = true;
                            break;
                        }
                    }
                    if(!isSameDevice){
                        deviceList.add(bluetoothDevice);
                        adapter.notifyDataSetChanged();
                    }
                }
                Log.e("bcz","blue-Device---name:" + bluetoothDevice.getName() + "---address:" + bluetoothDevice.getAddress() + "---type:" + bluetoothDevice.getType());
            }


        };
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NewBleBluetoothUtil.OPEN_BLUETOOTH_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NewBleBluetoothUtil.getInstance().scanLeDevice(true,scanCallback);
                    }
                },3000);

            }else{
                Toast.makeText(this,"请先打开蓝牙权限！",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
