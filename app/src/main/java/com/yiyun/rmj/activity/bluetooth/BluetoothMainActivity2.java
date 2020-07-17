package com.yiyun.rmj.activity.bluetooth;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
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
import com.yiyun.rmj.bean.AddDeviceParm;
import com.yiyun.rmj.bean.BluetoothBean;
import com.yiyun.rmj.bluetooth.NewBleBluetoothUtil;
import com.yiyun.rmj.dialog.RoundEditDialog;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.DisplayUtils;
import com.yiyun.rmj.utils.SpfUtils;
import com.yiyun.rmj.view.ElectricView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.jake.share.frdialog.dialog.FRDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    private int cleanType = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    readStatus();
                    break;
                case 1:
                    if (cleanType == 1) {
                        bt_cleanLeft.setBackgroundResource(R.drawable.btn_cleanleft);
                        cleanType = 0;
                    }
                    if (cleanType == 2) {
                        bt_cleanRight.setBackgroundResource(R.drawable.btn_cleanleft);
                        cleanType = 0;
                    }
                    break;
                case 2:
                    bluetoothUtil.readVersion(values -> {
                        for (byte value : values) {
                            Log.e("Pan", "value=" + value);
                        }
                    });
                    break;
            }
        }
    };
    private BluetoothModel bm;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bluetooth_main2;
    }

    @Override
    protected void initView() {
        findView();
        setClick();
        bluetoothUtil = NewBleBluetoothUtil.getInstance();
        handler.sendEmptyMessageDelayed(2, 3000);
        bluetoothUtil.setBlutToothListener(new NewBleBluetoothUtil.OnBlutToothListener() {
            @Override
            public void onStartSend(int Orders) {

            }

            @Override
            public void onSending(int index, int Orders) {

            }

            @Override
            public void onSendFinish() {
                Log.e("Pan", "指令发送完成=" + cleanType);

                if (handler.hasMessages(0)) {
                    handler.removeMessages(0);
                }

                handler.sendEmptyMessageDelayed(1, 1000 * 5);
                handler.sendEmptyMessageDelayed(0, 1000 * 10);

//                readStatus();
            }
        });

    }

    @Override
    protected void initData() {

        deviceId = getIntent().getIntExtra("deviceId", 0);
        Log.e("Pan", "deviceId=" + deviceId);
        device = DataSupport.find(BluetoothBean.class, deviceId);
        device.setList(SpfUtils.getBluetoothSetList(deviceId));
        setData();
        tv_deviceName.setText(device.getDeviceName());

        rv_deviceList.setLayoutManager(new LinearLayoutManager(this));
        bma = new BluetoothModelAdapter(this, device.getList(), new BluetoothModelAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                if (device.getList().size() == position)
                    return;
                index = position;
                startActivityForResult(
                        new Intent(BluetoothMainActivity2.this, BluetoothControlDetailActivity3.class)
                                .putExtra("type", TYPE_MODIFY)
                                .putExtra("setting", device.getList().get(index)),
                        TYPE_MODIFY
                );
            }

            @Override
            public void SettingAutoModel(boolean isChecked) {
                if (isChecked) {
                    showSettingDialog();
                } else {
                    device.getList().get(0).setSelected(false);
                    if (isAllClose()) {

                        bluetoothUtil.removeAllOrder();
                        bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.shutdown, 0);
                        if (bluetoothUtil.getOrderFromQuee() != null) {
                            handler.removeMessages(0);
                            bluetoothUtil.sendOrder();
                            sw_bootSwitch.setOnCheckedChangeListener(null);
                            sw_bootSwitch.setChecked(false);
                            sw_bootSwitch.setOnCheckedChangeListener(checkedListener);
                        }
                    }
                    SpfUtils.saveBluetoothSetList(device.getList(), deviceId);
                }

            }


            @Override
            public void SelectModel(int position, boolean b) {
                setModel(position, b);
            }

            @Override
            public void OnItemModifyClick(int position) {
                RoundEditDialog dialog = new RoundEditDialog(BluetoothMainActivity2.this, device.getList().get(position).getModelName(), new RoundEditDialog.IDialogBtnLiscener() {
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
                });
                dialog.show();
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                // 然后弹出输入法
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

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
                    if (device.getList().size() == 1) {

                        bluetoothUtil.removeAllOrder();
                        if (bm.getModel() != NewBleBluetoothUtil.mode_auto_mild &&
                                bm.getModel() != NewBleBluetoothUtil.mode_auto_middle &&
                                bm.getModel() != NewBleBluetoothUtil.mode_auto_strength
                        ) {
                            bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.mode_auto_mild, 0);
                            handler.removeMessages(0);
                            bluetoothUtil.sendOrder();
                        }

                        Log.e("Pan", "开机");
                    }
                    bma.notifyItemRemoved(adapterPosition);
                }
            }
        });

        rv_deviceList.setAdapter(bma);
//        readStatus();
        AddDevice();
    }

    private void setModel(int position, boolean isChecked) {
        Log.e("Pan", "position=" + position);
        Log.e("Pan", "isChecked=" + isChecked);
        for (int i = device.getList().size() - 1; i >= 0; i--) {
            if (device.getList().get(i).getModel() == NewBleBluetoothUtil.mode_auto_mild ||
                    device.getList().get(i).getModel() == NewBleBluetoothUtil.mode_auto_middle ||
                    device.getList().get(i).getModel() == NewBleBluetoothUtil.mode_auto_strength
            ) {
                device.getList().get(i).setSelected(false);
            }

            if (device.getList().get(i).getModel() == device.getList().get(position).getModel() && i != position) {
                device.getList().get(i).setSelected(false);
            }
        }
        device.getList().get(position).setSelected(isChecked);

        int hasShort = -1;
        int hasLong = -1;
        for (int i = 0; i < device.getList().size(); i++) {
            if (device.getList().get(i).isSelected()) {
                if (device.getList().get(i).getModel() == NewBleBluetoothUtil.mode_short) {
                    hasShort = i;
                }
                if (device.getList().get(i).getModel() == NewBleBluetoothUtil.mode_long) {
                    hasLong = i;
                }
            }
        }

        Log.e("Pan", "hasShort=" + hasShort);
        Log.e("Pan", "hasLong=" + hasLong);
        Log.e("Pan", "Model=" + device.getList().get(position).getModel());
        if (hasShort!=-1 && hasLong!=-1) {
            bluetoothUtil.removeAllOrder();
         /*
         Log.e("Pan", "getModel=" + bm.getModel());
            if (bm.getModel() != NewBleBluetoothUtil.mode_short_long) {
                bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.mode_short_long, 0);
            }
            if (device.getList().get(position).getModel() == NewBleBluetoothUtil.mode_short) {
                if (bm.getShortTime() != device.getList().get(position).getShortTime()) {
                    bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.shortTime, device.getList().get(position).getShortTime());
                }
                if (bm.getShortStrength() != device.getList().get(position).getShortStrength()) {
                    bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.shortStrength, device.getList().get(position).getShortStrength());
                }
            }

            if (device.getList().get(position).getModel() == NewBleBluetoothUtil.mode_long) {
                if (bm.getLongTime() != device.getList().get(position).getLongTime()) {
                    bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.longTime, device.getList().get(position).getLongTime());
                }
                if (bm.getLongStrength() != device.getList().get(position).getLongStrength()) {
                    Log.e("Pan", "getLongStrength=" + device.getList().get(position).getLongStrength());
                    bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.longStrength, device.getList().get(position).getLongStrength());
                }
            }
*/
            if (bm.getCleanTime()!=5||
                    bm.getAutoClean()!=NewBleBluetoothUtil.setpoweronclear||
                    bm.getShortTime() != device.getList().get(hasShort).getShortTime() ||
                    bm.getShortStrength() != device.getList().get(hasShort).getShortStrength() ||
                    bm.getLongTime() != device.getList().get(hasLong).getLongTime() ||
                    bm.getLongStrength() != device.getList().get(hasLong).getLongStrength() ||
                    bm.getModel() != NewBleBluetoothUtil.mode_short_long
            ) {
                bluetoothUtil.addOrderToQuee(
                        NewBleBluetoothUtil.longOrder,
                        5,
                        bm.getAutoClean(),
                        device.getList().get(hasShort).getShortTime(),
                        device.getList().get(hasShort).getShortStrength(),
                        device.getList().get(hasLong).getLongTime(),
                        device.getList().get(hasLong).getLongStrength(),
                        NewBleBluetoothUtil.mode_short_long
                );
            }



            if (bluetoothUtil.getOrderFromQuee() != null) {
                handler.removeMessages(0);
                bluetoothUtil.sendOrder();
            }
            Log.e("Pan", "设置长短喷");
        } else {
            if (hasShort!=-1) {

                bluetoothUtil.removeAllOrder();
            /*
                if (bm.getModel() != NewBleBluetoothUtil.mode_short) {
                    bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.mode_short, 0);
                }

                if (bm.getShortTime() != device.getList().get(position).getShortTime()) {
                    bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.shortTime, device.getList().get(position).getShortTime());
                }

                if (bm.getShortStrength() != device.getList().get(position).getShortStrength()) {
                    bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.shortStrength, device.getList().get(position).getShortStrength());
                }
*/
                if (bm.getShortTime() != device.getList().get(hasShort).getShortTime() ||
                        bm.getShortStrength() != device.getList().get(hasShort).getShortStrength() ||
                        bm.getModel() != NewBleBluetoothUtil.mode_short
                ) {
                    bluetoothUtil.addOrderToQuee(
                            NewBleBluetoothUtil.longOrder,
                            5,
                            bm.getAutoClean(),
                            device.getList().get(hasShort).getShortTime(),
                            device.getList().get(hasShort).getShortStrength(),
                            bm.getLongTime(),
                            bm.getLongStrength(),
                            NewBleBluetoothUtil.mode_short
                    );
                }
                if (bluetoothUtil.getOrderFromQuee() != null) {
                    handler.removeMessages(0);
                    bluetoothUtil.sendOrder();
                }
                Log.e("Pan", "设置短喷");
            }
            if (hasLong!=-1) {

                bluetoothUtil.removeAllOrder();
/*

                if (bm.getModel() != NewBleBluetoothUtil.mode_long) {
                    bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.mode_long, 0);
                }
                if (bm.getLongTime() != device.getList().get(position).getLongTime()) {
                    bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.longTime, device.getList().get(position).getLongTime());
                }

                if (bm.getLongStrength() != device.getList().get(position).getLongStrength()) {
                    bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.longStrength, device.getList().get(position).getLongStrength());
                }
*/
                if (bm.getLongTime() != device.getList().get(hasLong).getLongTime() ||
                        bm.getLongStrength() != device.getList().get(hasLong).getLongStrength() ||
                        bm.getModel() != NewBleBluetoothUtil.mode_long
                ) {
                    bluetoothUtil.addOrderToQuee(
                            NewBleBluetoothUtil.longOrder,
                            5,
                            bm.getAutoClean(),
                            bm.getShortTime(),
                            bm.getShortStrength(),
                            device.getList().get(hasLong).getLongTime(),
                            device.getList().get(hasLong).getLongStrength(),
                            NewBleBluetoothUtil.mode_long
                    );
                }
                if (bluetoothUtil.getOrderFromQuee() != null) {
                    handler.removeMessages(0);
                    bluetoothUtil.sendOrder();
                }
                Log.e("Pan", "设置长喷");
            }
        }

        if (isAllClose()) {

            bluetoothUtil.removeAllOrder();
            if (bm.getState() != NewBleBluetoothUtil.state_shutdown) {
                bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.shutdown, 0);
            }
            if (bluetoothUtil.getOrderFromQuee() != null) {
                handler.removeMessages(0);
                bluetoothUtil.sendOrder();
                Log.e("Pan", "关机");
            }
        }
        bma.notifyDataSetChanged();
        SpfUtils.saveBluetoothSetList(device.getList(), deviceId);
    }

    private void AddDevice() {
        AddDeviceParm addDeviceParm = new AddDeviceParm();
        addDeviceParm.setToken(SpfUtils.getSpfUtils(getApplicationContext()).getToken());
        addDeviceParm.setDeviceId(deviceId);

        api.addDeviceRecord(DESHelper.encrypt(gson.toJson(addDeviceParm)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e("Pan", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Pan", "onError=" + e.getMessage());
                    }

                    @Override
                    public void onNext(String add) {
                        Log.e("Pan", "onNext=" + add);
                    }
                });
    }

    public boolean isAllClose() {
        boolean isClose = true;
        for (SettingListModel settingListModel : device.getList()) {
            if (settingListModel.isSelected()) {
                isClose = false;
                break;
            }
        }
        return isClose;
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
                        new Intent(this, BluetoothControlDetailActivity3.class).putExtra("type", TYPE_ADD),
                        TYPE_ADD
                )
        );


        bt_cleanLeft.setOnClickListener(view -> {
            bluetoothUtil.removeAllOrder();
            bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.clearleft, 0);
            if (bluetoothUtil.getOrderFromQuee() != null) {
                handler.removeMessages(0);
                bluetoothUtil.sendOrder();
            }
            bt_cleanLeft.setBackgroundResource(R.drawable.btn_cleanright);
            cleanType = 1;
        });

        bt_cleanRight.setOnClickListener(view -> {
            bluetoothUtil.removeAllOrder();
            bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.clearright, 0);
            if (bluetoothUtil.getOrderFromQuee() != null) {
                handler.removeMessages(0);
                bluetoothUtil.sendOrder();
            }
            bt_cleanRight.setBackgroundResource(R.drawable.btn_cleanright);
            cleanType = 2;
        });


    }

    private CompoundButton.OnCheckedChangeListener checkedListener = (compoundButton, b) -> {
        tv_bootState.setText(b ? "关机" : "开机");
        bluetoothUtil.removeAllOrder();
        bluetoothUtil.addOrderToQuee(b ? NewBleBluetoothUtil.boot : NewBleBluetoothUtil.shutdown, 0);
        if (bluetoothUtil.getOrderFromQuee() != null) {
            handler.removeMessages(0);
            bluetoothUtil.sendOrder();
        }
    };

    private void readStatus() {
        bluetoothUtil.readStatus(values -> {

                    if (bluetoothUtil.getOrderFromQuee() != null) {
                        handler.removeMessages(0);
                        handler.sendEmptyMessageDelayed(0, 1000 * 10);
                        return;
                    }
                    //0x53+【开机状态】+【剩余电量】+【清洗时长】+【短喷间隔时间】+【短喷喷雾强度】+【开机清洗使能】 +【工作模式】+【长喷间隔时间】+【长喷喷雾强度】。
                    runOnUiThread(() -> {
                        if (values.length > 0 && values.length != 2) {
                            bm = new BluetoothModel(values);
                            if (bm.getLongTime() < 1) {
                                bm.setLongTime(1);
                            }
                            if (bm.getLongTime() > 10) {
                                bm.setLongTime(10);
                            }
                            if (bm.getLongStrength() < 2) {
                                bm.setLongStrength(2);
                            }
                            if (bm.getLongStrength() > 8) {
                                bm.setLongStrength(8);
                            }
                            if (bm.getLongStrength() % 2 != 0) {
                                bm.setLongStrength(bm.getLongStrength() / 2 * 2);
                            }

                            ev_electricityQuantity.setCount(bm.getElectricityQuantity() / 10);
                            tv_power.setText(bm.getElectricityQuantity() + "%");
                            tv_bootState.setText(bm.getState() == NewBleBluetoothUtil.state_boot ? "关机" : "开机");
                            sw_bootSwitch.setOnCheckedChangeListener(null);
                            sw_bootSwitch.setChecked(bm.getState() == NewBleBluetoothUtil.state_boot);
                            sw_bootSwitch.setOnCheckedChangeListener(checkedListener);
                            //检查当前蓝牙所开启的模式在设备列表中是否存在

                            int isAlreadyAuto = -1;
                            int isAlreadyShort = -1;
                            int isAlreadyLong = -1;

                            if (bm.getModel() == NewBleBluetoothUtil.mode_short_long) {
                                Log.e("Pan", "长短模式");
                                for (int i = 0; i < device.getList().size(); i++) {
                                    device.getList().get(i).setSelected(false);
                                    if (device.getList().get(i).getModel() == NewBleBluetoothUtil.mode_short &&
                                            device.getList().get(i).getShortTime() == bm.getShortTime() &&
                                            device.getList().get(i).getShortStrength() == bm.getShortStrength()
                                    ) {
                                        isAlreadyShort = i;
                                    }
                                    if (device.getList().get(i).getModel() == NewBleBluetoothUtil.mode_long &&
                                            device.getList().get(i).getLongTime() == bm.getLongTime() &&
                                            device.getList().get(i).getLongStrength() == bm.getLongStrength()
                                    ) {
                                        isAlreadyLong = i;
                                    }
                                }
                            } else {
                                Log.e("Pan", "非长短模式");
                                for (int i = 0; i < device.getList().size(); i++) {
                                    device.getList().get(i).setSelected(false);
                                    if (
                                            (bm.getModel() == NewBleBluetoothUtil.mode_auto_mild ||
                                                    bm.getModel() == NewBleBluetoothUtil.mode_auto_middle ||
                                                    bm.getModel() == NewBleBluetoothUtil.mode_auto_strength
                                            ) &&
                                                    (device.getList().get(i).getModel() == NewBleBluetoothUtil.mode_auto_mild ||
                                                            device.getList().get(i).getModel() == NewBleBluetoothUtil.mode_auto_middle ||
                                                            device.getList().get(i).getModel() == NewBleBluetoothUtil.mode_auto_strength
                                                    )
                                    ) {
                                        device.getList().get(i).setModelName("智能模式");
                                        device.getList().get(i).setModel(bm.getModel());
                                        isAlreadyAuto = i;
                                        continue;
                                    }
                                    if (device.getList().get(i).getModel() == bm.getModel()) {
                                        if (bm.getModel() == NewBleBluetoothUtil.mode_short) {
                                            //（短）模式
                                            if (device.getList().get(i).getShortTime() == bm.getShortTime() &&
                                                    device.getList().get(i).getShortStrength() == bm.getShortStrength()) {
                                                isAlreadyShort = i;
                                            }
                                        }
                                        if (bm.getModel() == NewBleBluetoothUtil.mode_long) {
                                            if (device.getList().get(i).getLongTime() == bm.getLongTime() &&
                                                    device.getList().get(i).getLongStrength() == bm.getLongStrength()) {
                                                isAlreadyLong = i;
                                            }
                                        }
                                    }
                                }

                            }


                            Log.e("Pan", "isAlreadyAuto=" + isAlreadyAuto);
                            Log.e("Pan", "isAlreadyShort=" + isAlreadyShort);
                            Log.e("Pan", "isAlreadyLong=" + isAlreadyLong);
                            if (isAlreadyAuto >= 0) {
                                device.getList().get(isAlreadyAuto).setSelected(true);
                            }
                            if (isAlreadyShort >= 0) {
                                device.getList().get(isAlreadyShort).setSelected(true);
                            }
                            if (isAlreadyLong >= 0) {
                                device.getList().get(isAlreadyLong).setSelected(true);
                            }

                            boolean hasAuto = false;
                            for (SettingListModel settingListModel : device.getList()) {
                                if (settingListModel.getModel() == NewBleBluetoothUtil.mode_auto_mild ||
                                        settingListModel.getModel() == NewBleBluetoothUtil.mode_auto_middle ||
                                        settingListModel.getModel() == NewBleBluetoothUtil.mode_auto_strength) {
                                    hasAuto = true;
                                    break;
                                }
                            }
                            if (!hasAuto) {
                                SettingListModel bsm = new SettingListModel();
                                bsm.setModelName("智能模式");
                                bsm.setModel(NewBleBluetoothUtil.mode_auto_mild);
                                List<SettingListModel> list = new ArrayList<>();
                                list.add(bsm);
                                list.addAll(device.getList());
                                device.getList().clear();
                                device.getList().addAll(list);
                                Log.e("Pan", "添加智能模式=" + new Gson().toJson(device));
                            }

                            if (isAlreadyShort < 0 && (bm.getModel() == NewBleBluetoothUtil.mode_short || bm.getModel() == NewBleBluetoothUtil.mode_short_long)) {
                                SettingListModel bsm = new SettingListModel();
                                bsm.setModel(NewBleBluetoothUtil.mode_short);
                                bsm.setShortTime(bm.getShortTime());
                                bsm.setShortStrength(bm.getShortStrength());
                                bsm.setSelected(true);
                                device.getList().add(bsm);
                                Log.e("Pan", "添加普通模式=" + new Gson().toJson(device));
                            }
                            if (isAlreadyLong < 0 && (bm.getModel() == NewBleBluetoothUtil.mode_long || bm.getModel() == NewBleBluetoothUtil.mode_short_long)) {
                                SettingListModel bsm = new SettingListModel();
                                bsm.setModel(NewBleBluetoothUtil.mode_long);
                                bsm.setLongTime(bm.getLongTime());
                                bsm.setLongStrength(bm.getLongStrength());
                                bsm.setSelected(true);
                                device.getList().add(bsm);
                                Log.e("Pan", "添加普通模式=" + new Gson().toJson(device));
                            }
                            setData();

                            Log.e("Pan", "device=" + new Gson().toJson(device));
                            bma.notifyDataSetChanged();
                            SpfUtils.saveBluetoothSetList(device.getList(), deviceId);
                            if (bm.getCleanTime() != 5) {
                                bluetoothUtil.removeAllOrder();
                                bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.setcleartime, 5);
                                bluetoothUtil.sendOrder();
                            }
                        }
                    });
                    handler.sendEmptyMessageDelayed(0, 1000 * 30);
                }
        );

    }

    private void setData() {
        for (SettingListModel slm : device.getList()) {
            if (slm.getModel() == NewBleBluetoothUtil.mode_long) {
                if (slm.getLongTime() < 1) {
                    slm.setLongTime(1);
                }
                if (slm.getLongTime() > 10) {
                    slm.setLongTime(10);
                }
                if (slm.getLongStrength() < 2) {
                    slm.setLongStrength(2);
                }
                if (slm.getLongStrength() > 8) {
                    slm.setLongStrength(8);
                }
                if (slm.getLongStrength() % 2 != 0) {
                    slm.setLongStrength(slm.getLongStrength() / 2 * 2);
                }
            }
            if (slm.getModel() == NewBleBluetoothUtil.mode_short) {
                if (slm.getShortTime() < 5) {
                    slm.setShortTime(5);
                }
                if (slm.getShortTime() > 60) {
                    slm.setShortTime(60);
                }
                if (slm.getShortStrength() < 1) {
                    slm.setShortStrength(1);
                }
                if (slm.getShortStrength() > 4) {
                    slm.setShortStrength(4);
                }
                if (slm.getLongStrength() % 5 != 0) {
                    slm.setLongStrength(slm.getLongStrength() / 5 * 5);
                }
            }

        }
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
            settingModel = NewBleBluetoothUtil.mode_auto_mild;
        });

        tv_model2.setOnClickListener(view -> {
            tv_model1.setBackgroundResource(R.drawable.dialog_zn_btn_unselect_bg);
            tv_model1.setTextColor(getResources().getColor(R.color.color66));
            tv_model2.setBackgroundResource(R.drawable.dialog_zn_btn_select_bg);
            tv_model2.setTextColor(getResources().getColor(R.color.color_blue));
            tv_model3.setBackgroundResource(R.drawable.dialog_zn_btn_unselect_bg);
            tv_model3.setTextColor(getResources().getColor(R.color.color66));
            settingModel = NewBleBluetoothUtil.mode_auto_middle;
        });

        tv_model3.setOnClickListener(view -> {
            tv_model1.setBackgroundResource(R.drawable.dialog_zn_btn_unselect_bg);
            tv_model1.setTextColor(getResources().getColor(R.color.color66));
            tv_model2.setBackgroundResource(R.drawable.dialog_zn_btn_unselect_bg);
            tv_model2.setTextColor(getResources().getColor(R.color.color66));
            tv_model3.setBackgroundResource(R.drawable.dialog_zn_btn_select_bg);
            tv_model3.setTextColor(getResources().getColor(R.color.color_blue));
            settingModel = NewBleBluetoothUtil.mode_auto_strength;
        });

        switch (device.getList().get(0).getModel()) {
            case NewBleBluetoothUtil.mode_auto_mild:
                tv_model1.performClick();
                break;
            case NewBleBluetoothUtil.mode_auto_middle:
                tv_model2.performClick();
                break;
            case NewBleBluetoothUtil.mode_auto_strength:
                tv_model3.performClick();
                break;
        }

        dialog.findViewById(R.id.tv_sure).setOnClickListener(view -> {

            bluetoothUtil.removeAllOrder();
            Log.e("Pan", "settingModel=" + settingModel);
            Log.e("Pan", "getModel=" + device.getList().get(0).getModel());
            switch (settingModel) {
                case NewBleBluetoothUtil.mode_auto_mild:
                    if (device.getList().get(0).getModel() != NewBleBluetoothUtil.mode_auto_mild) {
                        if (bm.getModel() != NewBleBluetoothUtil.mode_auto_mild) {
                            bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.mode_auto_mild, 0);
                        }
                    }
                    break;
                case NewBleBluetoothUtil.mode_auto_middle:
                    if (device.getList().get(0).getModel() != NewBleBluetoothUtil.mode_auto_middle) {
                        if (bm.getModel() != NewBleBluetoothUtil.mode_auto_middle) {
                            bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.mode_auto_middle, 0);
                        }
                    }
                    break;
                case NewBleBluetoothUtil.mode_auto_strength:
                    if (device.getList().get(0).getModel() != NewBleBluetoothUtil.mode_auto_strength) {
                        if (bm.getModel() != NewBleBluetoothUtil.mode_auto_strength) {
                            bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.mode_auto_strength, 0);
                        }
                    }
                    break;
            }

            if (bluetoothUtil.getOrderFromQuee() != null) {
                handler.removeMessages(0);
                bluetoothUtil.sendOrder();
            }
            device.getList().get(0).setModel(settingModel);
            for (SettingListModel slm : device.getList()) {
                slm.setSelected(false);
            }
            device.getList().get(0).setSelected(true);
            SpfUtils.saveBluetoothSetList(device.getList(), deviceId);
            bma.notifyDataSetChanged();
            dialog.dismiss();
        });

    }

    @Override
    public void onResume() {
        if (handler.hasMessages(0)) {
            handler.removeMessages(0);
        }
        handler.sendEmptyMessage(0);
        super.onResume();
    }

    @Override
    public void onPause() {
        handler.removeMessages(0);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //移除30秒读状态的定时器
        handler.removeMessages(0);
//        bluetoothUtil.disconnectDevice();
        super.onDestroy();
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
                    if (device.getList().get(index).isSelected()) {
                        setModel(index, true);
                    }
                    break;
            }
            SpfUtils.saveBluetoothSetList(device.getList(), deviceId);
            bma.notifyDataSetChanged();
        }
    }
}
