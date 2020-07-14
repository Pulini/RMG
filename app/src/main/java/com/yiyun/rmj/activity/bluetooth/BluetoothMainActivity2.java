package com.yiyun.rmj.activity.bluetooth;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;

import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.BluetoothBean;
import com.yiyun.rmj.bluetooth.NewBleBluetoothUtil;
import com.yiyun.rmj.dialog.RoundEditDialog;
import com.yiyun.rmj.utils.DisplayUtils;
import com.yiyun.rmj.utils.SpfUtils;
import com.yiyun.rmj.view.ElectricView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.jake.share.frdialog.dialog.FRDialog;

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
    private SwipeRecyclerView rv_deviceList;
    private Button bt_cleanLeft;
    private Button bt_cleanRight;


    private int deviceId = 0;
    private int index = 0;
    private BluetoothModelAdapter bma;
    private BluetoothBean device;
    private FRDialog dialog;
    private int settingModel = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    readStatus();
                    break;
            }
        }
    };

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
        device = DataSupport.find(BluetoothBean.class, deviceId);
        device.setList(SpfUtils.getBluetoothSetList(deviceId));
        tv_deviceName.setText(device.getDeviceName());

        rv_deviceList.setLayoutManager(new LinearLayoutManager(this));
        bma = new BluetoothModelAdapter(this, device.getList(), new BluetoothModelAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                if(device.getList().size()==position)
                    return;
                index = position;
                startActivityForResult(
                        new Intent(BluetoothMainActivity2.this, BluetoothControlDetailActivity2.class)
                                .putExtra("type", TYPE_MODIFY)
                                .putExtra("setting", device.getList().get(position)),
                        TYPE_MODIFY
                );
            }

            @Override
            public void SettingAutoModel() {
                showSettingDialog();
            }

            @Override
            public void OnItemModifyClick(int position) {
                new RoundEditDialog(BluetoothMainActivity2.this, device.getList().get(position).getModelName(), new RoundEditDialog.IDialogBtnLiscener() {
                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onSure(String str) {
                        if (!str.isEmpty()) {
                            device.getList().get(position).setModelName(str);
                            SpfUtils.saveBluetoothSetList(device.getList(), deviceId);
                            bma.notifyItemChanged(position);
                        }
                    }
                }).show();
            }
        });
        rv_deviceList.setSwipeMenuCreator((leftMenu, rightMenu, position) -> {
            if (0 != position) {
                rightMenu.addMenuItem(
                        new SwipeMenuItem(this)
                                .setBackgroundColor(Color.parseColor("#DD4F42"))
                                .setText("删除")
                                .setTextColor(Color.WHITE)
                                .setTextSize(15)
                                .setWidth(DisplayUtils.dp2px(this, 60))
                                .setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                );
            }
        });
        // 设置菜单Item点击监听。
        rv_deviceList.setOnItemMenuClickListener((menuBridge, adapterPosition) -> {
            menuBridge.closeMenu();
            if (menuBridge.getDirection() == SwipeRecyclerView.RIGHT_DIRECTION) {
                if (menuBridge.getPosition() == 0) {
                    device.getList().remove(adapterPosition);
                    SpfUtils.saveBluetoothSetList(device.getList(), deviceId);
//                if (device.getList().size() == 1) {
//                    bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.settimeinterval, 5);
//                    bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.setstrenth, 64);
//                    bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.boot, 0);
//                    bluetoothUtil.sendOrder();
//                }
                    bma.notifyItemRemoved(adapterPosition);
                }
            }
        });

        rv_deviceList.setAdapter(bma);
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
                            tv_bootState.setText(bm.getState() == NewBleBluetoothUtil.state_boot ? "关机" : "开机");
                            sw_bootSwitch.setChecked(bm.getState() == NewBleBluetoothUtil.state_boot);
                            //检查当前蓝牙所开启的模式在设备列表中是否存在
                            boolean isAlready = false;
                            for (SettingListModel data : device.getList()) {
                                //初始为未选中状态
                                data.setSelected(false);
                                //模式相同进行内容匹配
                                if (data.getModel() == bm.getModel()) {
                                    switch (data.getModel()) {
                                        case NewBleBluetoothUtil.mode_mild:
                                            isAlready = true;
                                            data.setSelected(true);
                                            data.setModelName("阅读模式");
                                            break;
                                        case NewBleBluetoothUtil.mode_middle:
                                            isAlready = true;
                                            data.setSelected(true);
                                            data.setModelName("电竞模式");
                                            break;
                                        case NewBleBluetoothUtil.mode_strength:
                                            isAlready = true;
                                            data.setSelected(true);
                                            data.setModelName("美容模式");
                                            break;
                                        case NewBleBluetoothUtil.mode_short_long:
                                            //（短+长）模式
                                            if (data.getShortTime() == bm.getShortTime() &&
                                                    data.getShortStrength() == bm.getShortStrength() &&
                                                    data.getLongTime() == bm.getLongTime() &&
                                                    data.getLongStrength() == bm.getLongStrength()) {
                                                isAlready = true;
                                                data.setSelected(true);
                                            }
                                            break;
                                        case NewBleBluetoothUtil.mode_short:
                                            //（短）模式
                                            if (data.getShortTime() == bm.getShortTime() &&
                                                    data.getShortStrength() == bm.getShortStrength()) {
                                                isAlready = true;
                                                data.setSelected(true);
                                            }
                                            break;
                                        case NewBleBluetoothUtil.mode_long:
                                            //（长）模式
                                            if (data.getLongTime() == bm.getLongTime() &&
                                                    data.getLongStrength() == bm.getLongStrength()) {
                                                isAlready = true;
                                                data.setSelected(true);
                                            }
                                            break;
                                    }
                                }
                            }

                            if (!isAlready) {
                                SettingListModel bsm = new SettingListModel();
                                bsm.setModel(bm.getModel());
                                bsm.setShortTime(bm.getShortTime());
                                bsm.setShortStrength(bm.getShortStrength());
                                bsm.setLongTime(bm.getLongTime());
                                bsm.setLongStrength(bm.getLongStrength());
                                bsm.setSelected(true);
                                if (bm.getModel() == NewBleBluetoothUtil.mode_mild ||
                                        bm.getModel() == NewBleBluetoothUtil.mode_middle ||
                                        bm.getModel() == NewBleBluetoothUtil.mode_strength) {
                                    bsm.setModelName("智能模式");
                                    List<SettingListModel> list = new ArrayList<>();
                                    list.add(bsm);
                                    list.addAll(device.getList());
                                    device.getList().clear();
                                    device.getList().addAll(list);
                                    Log.e("Pan", "添加智能模式=" + new Gson().toJson(device));
                                } else {
                                    device.getList().add(bsm);
                                    Log.e("Pan", "添加普通模式=" + new Gson().toJson(device));
                                }

                            }

                            boolean hasAuto = false;
                            for (SettingListModel data : device.getList()) {
                                //检查列表中是否有智能模式
                                if (data.getModel() == NewBleBluetoothUtil.mode_mild ||
                                        data.getModel() == NewBleBluetoothUtil.mode_middle ||
                                        data.getModel() == NewBleBluetoothUtil.mode_strength) {
                                    hasAuto = true;
                                    break;
                                }
                            }
                            if (!hasAuto) {
                                SettingListModel bsm = new SettingListModel();
                                bsm.setModelName("智能模式");
                                bsm.setModel(NewBleBluetoothUtil.mode_mild);
                                List<SettingListModel> list = new ArrayList<>();
                                list.add(bsm);
                                list.addAll(device.getList());
                                device.getList().clear();
                                device.getList().addAll(list);
                                Log.e("Pan", "添加智能模式=" + new Gson().toJson(device));
                            }
                            Log.e("Pan", "device=" + new Gson().toJson(device));
                            bma.notifyDataSetChanged();
                            SpfUtils.saveBluetoothSetList(device.getList(), deviceId);

                        }
                    });
                    handler.sendEmptyMessageDelayed(0, 1000 * 30);
                }
        );

    }

    public void showSettingDialog() {
        if (dialog == null) {
            dialog = new FRDialog
                    .CommonBuilder(this)
                    .setContentView(R.layout.dialog_bluetooth_auto_model_setting)
                    .setCancelableOutside(false)
                    .create();
            dialog.findViewById(R.id.iv_cancel).setOnClickListener(view -> {
                dialog.dismiss();
                device.getList().get(0).setSelected(false);
                bma.notifyItemChanged(0);
            });
        }
        dialog.show();

        TextView tv_model1 = dialog.findViewById(R.id.tv_model1);
        TextView tv_model2 = dialog.findViewById(R.id.tv_model2);
        TextView tv_model3 = dialog.findViewById(R.id.tv_model3);

        tv_model1.setOnClickListener(view -> {
            tv_model1.setBackgroundResource(R.drawable.dialog_zn_btn_select_bg);
            tv_model1.setTextColor(getResources().getColor(R.color.color_blue));
            tv_model2.setBackgroundResource(R.drawable.dialog_zn_btn_unselect_bg);
            tv_model2.setTextColor(getResources().getColor(R.color.color66));
            tv_model3.setBackgroundResource(R.drawable.dialog_zn_btn_unselect_bg);
            tv_model3.setTextColor(getResources().getColor(R.color.color66));
            settingModel = NewBleBluetoothUtil.mode_mild;
        });

        tv_model2.setOnClickListener(view -> {
            tv_model1.setBackgroundResource(R.drawable.dialog_zn_btn_unselect_bg);
            tv_model1.setTextColor(getResources().getColor(R.color.color66));
            tv_model2.setBackgroundResource(R.drawable.dialog_zn_btn_select_bg);
            tv_model2.setTextColor(getResources().getColor(R.color.color_blue));
            tv_model3.setBackgroundResource(R.drawable.dialog_zn_btn_unselect_bg);
            tv_model3.setTextColor(getResources().getColor(R.color.color66));
            settingModel = NewBleBluetoothUtil.mode_middle;
        });

        tv_model3.setOnClickListener(view -> {
            tv_model1.setBackgroundResource(R.drawable.dialog_zn_btn_unselect_bg);
            tv_model1.setTextColor(getResources().getColor(R.color.color66));
            tv_model2.setBackgroundResource(R.drawable.dialog_zn_btn_unselect_bg);
            tv_model2.setTextColor(getResources().getColor(R.color.color66));
            tv_model3.setBackgroundResource(R.drawable.dialog_zn_btn_select_bg);
            tv_model3.setTextColor(getResources().getColor(R.color.color_blue));
            settingModel = NewBleBluetoothUtil.mode_strength;
        });

        switch (device.getList().get(0).getModel()) {
            case NewBleBluetoothUtil.mode_mild:
                tv_model1.performClick();
                break;
            case NewBleBluetoothUtil.mode_middle:
                tv_model2.performClick();
                break;
            case NewBleBluetoothUtil.mode_strength:
                tv_model3.performClick();
                break;
        }

        dialog.findViewById(R.id.tv_sure).setOnClickListener(view -> {
            switch (settingModel) {
                case NewBleBluetoothUtil.mode_mild:
                    if (device.getList().get(0).getModel() != NewBleBluetoothUtil.mode_mild) {
                        bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.mode_mild, 0);
                    }
                    break;
                case NewBleBluetoothUtil.mode_middle:
                    if (device.getList().get(0).getModel() != NewBleBluetoothUtil.mode_middle) {
                        bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.mode_middle, 0);
                    }
                    break;
                case NewBleBluetoothUtil.mode_strength:
                    if (device.getList().get(0).getModel() != NewBleBluetoothUtil.mode_strength) {
                        bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.mode_long, 0);
                    }
                    break;
            }
            bluetoothUtil.sendOrder();
            device.getList().get(0).setModel(settingModel);
            SpfUtils.saveBluetoothSetList(device.getList(), deviceId);
            bma.notifyDataSetChanged();
            dialog.dismiss();
        });

    }

    @Override
    public void onBackPressed() {
        if (dialog != null && dialog.isShowing()) {
            device.getList().get(0).setSelected(false);
            bma.notifyItemChanged(0);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            SettingListModel bsm = (SettingListModel) data.getExtras().getSerializable("Model");
            switch (requestCode) {
                case TYPE_ADD:
                    device.getList().add(bsm);
                    break;
                case TYPE_MODIFY:
                    device.getList().set(index, bsm);
                    break;
            }
            SpfUtils.saveBluetoothSetList(device.getList(), deviceId);
            bma.notifyDataSetChanged();
        }
    }
}
