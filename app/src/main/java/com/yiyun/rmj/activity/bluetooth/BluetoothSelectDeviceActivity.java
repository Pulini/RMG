package com.yiyun.rmj.activity.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;

import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yiyun.rmj.R;
import com.yiyun.rmj.adapter.CommonRecyclerViewAdapter;
import com.yiyun.rmj.adapter.CommonRecyclerViewHolder;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.BluetoothBean;
import com.yiyun.rmj.bluetooth.NewBleBluetoothUtil;
import com.yiyun.rmj.dialog.RoundEditDialog;
import com.yiyun.rmj.utils.DisplayUtils;
import com.yiyun.rmj.utils.DividerItemDecoration;
import com.yiyun.rmj.utils.PermissionUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

//选择设备界面
public class BluetoothSelectDeviceActivity extends BaseActivity implements View.OnClickListener {

    SwipeRecyclerView rv_list;
    ArrayList<BluetoothBean> listData;
    CommonRecyclerViewAdapter mAdapter;

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.iv_back_white:
                finish();
                break;

            case R.id.iv_add:
                Intent intent = new Intent(BluetoothSelectDeviceActivity.this, BluetoothAddDeviceActivity.class);
                startActivityForResult(intent,333);
                overridePendingTransition(R.anim.activity_rightclick_in,R.anim.activity_rightclick_out);
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bluetooth_select;
    }

    @Override
    protected void initView() {
        listData = new ArrayList<>();

        ImageView iv_back = (ImageView) findViewById(R.id.iv_back_white);
        iv_back.setOnClickListener(this);

        ImageView iv_add = (ImageView) findViewById(R.id.iv_add);
        iv_add.setVisibility(View.VISIBLE);
        iv_add.setOnClickListener(this);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.select_device));

        rv_list = (SwipeRecyclerView) findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        //设置item下划线
        rv_list.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        //设置侧滑按钮
        rv_list.setSwipeMenuCreator((leftMenu, rightMenu, position) -> {

                rightMenu.addMenuItem(
                        new SwipeMenuItem(this)
                                .setBackgroundColor(Color.parseColor("#BBBBBB"))
                                .setText("修改")
                                .setTextColor(Color.WHITE)
                                .setTextSize(14)
                                .setWidth(DisplayUtils.dp2px(this, 50))
                                .setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                );
                rightMenu.addMenuItem(
                        new SwipeMenuItem(this)
                                .setBackgroundColor(Color.parseColor("#FE0036"))
                                .setText("删除")
                                .setTextColor(Color.WHITE)
                                .setTextSize(14)
                                .setWidth(DisplayUtils.dp2px(this, 50))
                                .setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                );

        });

        rv_list.setOnItemMenuClickListener((menuBridge, adapterPosition) -> {
            menuBridge.closeMenu();
            if (menuBridge.getDirection() == SwipeRecyclerView.RIGHT_DIRECTION) {
                if (menuBridge.getPosition() == 0) {
                    RoundEditDialog dialog = new RoundEditDialog(BluetoothSelectDeviceActivity.this, "", new RoundEditDialog.IDialogBtnLiscener() {
                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onSure(String str) {

                            if(!str.equals(listData.get(adapterPosition).getDeviceName()))
                            {
                                BluetoothBean settingBean = listData.get(adapterPosition);
                                settingBean.setDeviceName(str);
                                settingBean.save();
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    dialog.show();
                    dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                    // 然后弹出输入法
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                }
                if(menuBridge.getPosition() == 1){
                    PermissionUtil.requestStoragePermission(BluetoothSelectDeviceActivity.this, new PermissionUtil.IRequestPermissionCallBack(){
                        @Override
                        public void permissionSuccess() {
                            listData.get(adapterPosition).delete();
                            listData.remove(adapterPosition);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

        //设置listview---item点击事件
        rv_list.setOnItemClickListener((itemView, position) -> {
            if(NewBleBluetoothUtil.getInstance().isBlueEnable()){

                Intent connectingIntent = new Intent(BluetoothSelectDeviceActivity.this, BluetoothConnecttingDeviceActivity.class);
                connectingIntent.putExtra("deviceId",listData.get(position).getId());
                startActivity(connectingIntent);
                overridePendingTransition(R.anim.activity_rightclick_in, R.anim.activity_rightclick_out);
                finish();

            }else{
                ToastUtils.show("请先打开蓝牙");
            }
        });


        mAdapter = new CommonRecyclerViewAdapter<BluetoothBean>(this, listData) {

            @Override
            public void convert(CommonRecyclerViewHolder h, BluetoothBean entity, int position) {
                TextView tv_name = h.getView(R.id.tv_device_bh);
                tv_name.setText(entity.getDeviceName());
            }

            @Override
            public int getLayoutViewId(int viewType) {
                return R.layout.item_select_device;
            }
        };
        rv_list.setAdapter(mAdapter);


    }


    @Override
    protected void initData() {
        PermissionUtil.requestStoragePermission(this, new PermissionUtil.IRequestPermissionCallBack() {
            @Override
            public void permissionSuccess() {
                ArrayList<BluetoothBean> tempData = (ArrayList<BluetoothBean>) DataSupport.findAll(BluetoothBean.class);
                listData.addAll(tempData);
                mAdapter.notifyDataSetChanged();
                if(NewBleBluetoothUtil.getInstance().isSupportBlue()){
                    if(!NewBleBluetoothUtil.getInstance().isBlueEnable()){
                        NewBleBluetoothUtil.getInstance().openBlueSync(BluetoothSelectDeviceActivity.this,NewBleBluetoothUtil.OPEN_BLUETOOTH_REQUEST_CODE);
                    }
                }else{
                    Toast.makeText(BluetoothSelectDeviceActivity.this,"该设备不支持蓝牙功能！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isExist(String deviceAddress){
        for(BluetoothBean bluetoothBean:listData){
            if(bluetoothBean.getAddress().equals(deviceAddress)){
                return true;
            }
        }
        return false;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == 333){
            PermissionUtil.requestStoragePermission(BluetoothSelectDeviceActivity.this, new PermissionUtil.IRequestPermissionCallBack() {
                @Override
                public void permissionSuccess() {
                    BluetoothDevice device = data.getParcelableExtra("device");
                    if(!isExist(device.getAddress())){
                        BluetoothBean bluetooth = new BluetoothBean();
                        bluetooth.setDeviceName(device.getName());
                        bluetooth.setAddress(device.getAddress());
                        bluetooth.setType(device.getType());
                        bluetooth.setBundState(device.getBondState());
                        bluetooth.save();
                        listData.add(bluetooth);
                        mAdapter.notifyDataSetChanged();
                        Log.e("testbcz","BluetoothSelectDeviceActivity-onActivityResult-device--" + new Gson().toJson(bluetooth));
                    }
                }
            });
        }
    }

}
