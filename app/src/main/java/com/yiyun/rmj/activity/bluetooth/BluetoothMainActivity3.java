package com.yiyun.rmj.activity.bluetooth;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity2;
import com.yiyun.rmj.bean.AddDeviceParm;
import com.yiyun.rmj.bean.BluetoothBean;
import com.yiyun.rmj.bluetooth.BLEUtil;
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
public class BluetoothMainActivity3 extends BaseActivity2 {

    public static final int TYPE_ADD = 0;
    public static final int TYPE_MODIFY = 1;

    private ImageView iv_add;
    private ImageView iv_back;
    private TextView tv_eleQty;
    private ElectricView ev_electricityQuantity;
    private TextView tv_power;
    private TextView tv_bootState;
    private Switch sw_bootSwitch;
    private TextView tv_deviceName;
    private TextView tv_version;
    private TextView tv_cleanValue;
    private SeekBar sb_cleanValue;
    private Button bt_sure;
    private SwipeRecyclerView rv_deviceList;
    private Button bt_cleanLeft;
    private Button bt_cleanRight;


    private int deviceId = 0;
    private int index = 0;
    private BluetoothModelAdapter2 bma;
    private BluetoothBean device;
    private FRDialog dialog;
    private int settingModel = 0;
    private String DevcieVersion = "";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    refresh();
                    break;
                case 1:
                    cleanLeft = false;
                    bt_cleanLeft.setBackgroundResource(R.drawable.btn_cleanleft);
                    break;
                case 4:
                    cleanRight = false;
                    bt_cleanRight.setBackgroundResource(R.drawable.btn_cleanleft);
                    break;

                case 22:
                    Log.e("Pan", "设置版本号：" + DevcieVersion);
                    String version2 = DevcieVersion;
                    if (bm != null) {
                        version2 = DevcieVersion + "-" + bm.getAutoClean() + "-" + bm.getCleanTime();
                    }
                    tv_version.setText(version2);
//                    DevcieVersion = "";
                    break;

            }
        }
    };
    private boolean cleanLeft = false;
    private boolean cleanRight = false;
    private boolean isSending = false;
    private boolean isSetting = false;
    private BleDevice bleDevice;


    int cleanTime = 5;
    private BluetoothModel bm=new BluetoothModel();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_bluetooth_main2;
    }

    @Override
    protected void initView() {
        findView();
        setClick();
        cleanTime = SpfUtils.getSpfUtils(getApplicationContext()).getCleanTime();
        Log.e("Pan", "cleanTime=" + cleanTime);
        tv_cleanValue.setText("出液量: " + ((cleanTime - 6) / 3 + 2) * 10 + "%");
        sb_cleanValue.setProgress((cleanTime - 6) / 3);
        bt_sure.setBackgroundResource(R.drawable.shape_stroke_btn_bg_blue);


    }

    @Override
    protected void initData() {

        deviceId = getIntent().getIntExtra("deviceId", 0);
        bleDevice = getIntent().getParcelableExtra("bleDevice");
//        BLE_Notify();

        Log.e("Pan", "deviceId=" + deviceId);
        Log.e("Pan", "bleDevice=" + BleManager.getInstance().getConnectState(bleDevice));
        device = DataSupport.find(BluetoothBean.class, deviceId);
        device.setList(SpfUtils.getBluetoothSetList(deviceId));
        setData();
        tv_deviceName.setText(device.getDeviceName());

        rv_deviceList.setLayoutManager(new LinearLayoutManager(this));
        bma = new BluetoothModelAdapter2(this, device.getList(), new BluetoothModelAdapter2.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                if (device.getList().size() == position)
                    return;
                if (position == 0) {
                    showSettingDialog();
                } else {
                    index = position;
                    startActivityForResult(
                            new Intent(BluetoothMainActivity3.this, BluetoothControlDetailActivity3.class)
                                    .putExtra("type", TYPE_MODIFY)
                                    .putExtra("setting", device.getList().get(index)),
                            TYPE_MODIFY
                    );
                }

            }

            @Override
            public void SettingAutoModel(boolean isChecked) {
                if (isChecked) {
                    for (SettingListModel settingListModel : device.getList()) {
                        settingListModel.setSelected(false);
                    }
                    for (SettingListModel slm : device.getList()) {
                        slm.setSelected(false);
                    }
                    device.getList().get(0).setSelected(true);
                    switch (device.getList().get(0).getModel()) {
                        case BLEUtil.ble_Mode_Intelligent1:
                            sendOrder(new byte[]{BLEUtil.ble_Mode_Intelligent1}, BLEUtil.BLE_writeUUid_1);
                            break;
                        case BLEUtil.ble_Mode_Intelligent2:
                            sendOrder(new byte[]{BLEUtil.ble_Mode_Intelligent2}, BLEUtil.BLE_writeUUid_1);
                            break;
                        case BLEUtil.ble_Mode_Intelligent3:
                            sendOrder(new byte[]{BLEUtil.ble_Mode_Intelligent3}, BLEUtil.BLE_writeUUid_1);
                            break;
                    }
                    SpfUtils.saveBluetoothSetList(device.getList(), deviceId);
                    bma.notifyDataSetChanged();
                }

            }


            @Override
            public void SelectModel(int position, boolean b) {
                setModel(position, b);
            }

            @Override
            public void OnItemModifyClick(int position) {
                RoundEditDialog dialog = new RoundEditDialog(BluetoothMainActivity3.this, device.getList().get(position).getModelName(), new RoundEditDialog.IDialogBtnLiscener() {
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
                        if (bm.getModel() != BLEUtil.ble_Mode_Intelligent1 &&
                                bm.getModel() != BLEUtil.ble_Mode_Intelligent2 &&
                                bm.getModel() != BLEUtil.ble_Mode_Intelligent3
                        ) {
                            sendOrder(new byte[]{BLEUtil.ble_Mode_Intelligent1}, BLEUtil.BLE_writeUUid_1);
                        }
                        Log.e("Pan", "开机");
                    }
                    bma.notifyItemRemoved(adapterPosition);
                }
            }
        });

        rv_deviceList.setAdapter(bma);
        AddDevice();
//        refresh();
//        readVersion();
    }


    private void setModel(int position, boolean isChecked) {
        Log.e("Pan", "position=" + position);
        Log.e("Pan", "isChecked=" + isChecked);
        for (int i = device.getList().size() - 1; i >= 0; i--) {
            if (device.getList().get(i).getModel() == BLEUtil.ble_Mode_Intelligent1 ||
                    device.getList().get(i).getModel() == BLEUtil.ble_Mode_Intelligent2 ||
                    device.getList().get(i).getModel() == BLEUtil.ble_Mode_Intelligent3
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
                if (device.getList().get(i).getModel() == BLEUtil.ble_Mode_Short) {
                    hasShort = i;
                }
                if (device.getList().get(i).getModel() == BLEUtil.ble_Mode_Long) {
                    hasLong = i;
                }
            }
        }

        Log.e("Pan", "hasShort=" + hasShort);
        Log.e("Pan", "hasLong=" + hasLong);
        Log.e("Pan", "Model=" + device.getList().get(position).getModel());
        if (hasShort != -1 && hasLong != -1) {
            sendOrder(new byte[]{
                    BLEUtil.ble_LongOrder,
                    BLEUtil.OX_ORDER[cleanTime],
                    BLEUtil.OX_ORDER[bm.getAutoClean()],
                    BLEUtil.OX_ORDER[device.getList().get(hasShort).getShortTime()],
                    BLEUtil.OX_ORDER[device.getList().get(hasShort).getShortStrength()],
                    BLEUtil.OX_ORDER[device.getList().get(hasLong).getLongTime()],
                    BLEUtil.OX_ORDER[device.getList().get(hasLong).getLongStrength()],
                    BLEUtil.OX_ORDER[BLEUtil.ble_Mode_Short_Long],
            }, BLEUtil.BLE_writeUUid_2);
            Log.e("Pan", "设置长短喷");
        } else {
            if (hasShort != -1) {
                sendOrder(new byte[]{
                        BLEUtil.ble_LongOrder,
                        BLEUtil.OX_ORDER[cleanTime],
                        BLEUtil.OX_ORDER[bm.getAutoClean()],
                        BLEUtil.OX_ORDER[device.getList().get(hasShort).getShortTime()],
                        BLEUtil.OX_ORDER[device.getList().get(hasShort).getShortStrength()],
                        BLEUtil.OX_ORDER[bm.getLongTime()],
                        BLEUtil.OX_ORDER[bm.getLongStrength()],
                        BLEUtil.OX_ORDER[BLEUtil.ble_Mode_Short],
                }, BLEUtil.BLE_writeUUid_2);
                Log.e("Pan", "设置短喷");
            }
            if (hasLong != -1) {
                sendOrder(new byte[]{
                        BLEUtil.ble_LongOrder,
                        BLEUtil.OX_ORDER[cleanTime],
                        BLEUtil.OX_ORDER[bm.getAutoClean()],
                        BLEUtil.OX_ORDER[bm.getShortTime()],
                        BLEUtil.OX_ORDER[bm.getShortStrength()],
                        BLEUtil.OX_ORDER[device.getList().get(hasLong).getLongTime()],
                        BLEUtil.OX_ORDER[device.getList().get(hasLong).getLongStrength()],
                        BLEUtil.OX_ORDER[BLEUtil.ble_Mode_Long],
                }, BLEUtil.BLE_writeUUid_2);
                Log.e("Pan", "设置长喷");
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

    public void findView() {
        iv_add = findViewById(R.id.iv_title_blue_add);
        iv_back = findViewById(R.id.iv_back);
        iv_add.setVisibility(View.VISIBLE);
        iv_back.setVisibility(View.VISIBLE);

        tv_eleQty = findViewById(R.id.tv_eleQty);
        ev_electricityQuantity = findViewById(R.id.ev_electricityQuantity);
        tv_power = findViewById(R.id.tv_power);
        tv_bootState = findViewById(R.id.tv_bootState);
        sw_bootSwitch = findViewById(R.id.sw_bootSwitch);
        tv_deviceName = findViewById(R.id.tv_deviceName);
        tv_version = findViewById(R.id.tv_version);
        tv_cleanValue = findViewById(R.id.tv_cleanValue);
        sb_cleanValue = findViewById(R.id.sb_cleanValue);
        bt_sure = findViewById(R.id.bt_sure);

        rv_deviceList = findViewById(R.id.rv_deviceList);
        bt_cleanLeft = findViewById(R.id.bt_cleanLeft);
        bt_cleanRight = findViewById(R.id.bt_cleanRight);

    }

    public void setClick() {
        iv_back.setOnClickListener(view -> {
            BleManager.getInstance().disconnect(bleDevice);
            finish();
        });

        iv_add.setOnClickListener(view ->
                startActivityForResult(
                        new Intent(this, BluetoothControlDetailActivity3.class).putExtra("type", TYPE_ADD),
                        TYPE_ADD
                )
        );


        sb_cleanValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                bt_sure.setBackgroundResource(R.drawable.shape_stroke_btn_bg_purple);
                cleanTime = i * 3 + 6;
                tv_cleanValue.setText("出液量: " + ((cleanTime - 6) / 3 + 2) * 10 + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        bt_sure.setOnClickListener(view -> {
            isSetting = true;
            bt_sure.setBackgroundResource(R.drawable.shape_stroke_btn_bg_blue);
            SpfUtils.getSpfUtils(getApplicationContext()).setCleanTime(cleanTime);
            sendOrder(new byte[]{BLEUtil.ble_SprayQuantity, BLEUtil.OX_ORDER[cleanTime]}, BLEUtil.BLE_writeUUid_2);
            bm.setCleanTime(cleanTime);
            ToastUtils.show("设置出液量：" + ((cleanTime - 6) / 3 + 2) * 10 + "%");
            runOnUiThread(() -> {
                if (!DevcieVersion.isEmpty()) {
                    String version1 = DevcieVersion + "-" + bm.getAutoClean() + "-" + bm.getCleanTime();
                    tv_version.setText(version1);
                }
            });
//            handler.sendEmptyMessage(2);

        });
        bt_cleanLeft.setOnClickListener(view -> {
            if (!cleanLeft) {
                cleanLeft = true;
                sendOrder(new byte[]{BLEUtil.ble_CleanLift}, BLEUtil.BLE_writeUUid_1);
                bt_cleanLeft.setBackgroundResource(R.drawable.btn_cleanright);
                if (handler.hasMessages(1)) {
                    handler.removeMessages(1);
                }
                handler.sendEmptyMessageDelayed(1, 1000 * 5);
            }
        });

        bt_cleanRight.setOnClickListener(view -> {
            if (!cleanRight) {
                cleanRight = true;
                sendOrder(new byte[]{BLEUtil.ble_CleanRight}, BLEUtil.BLE_writeUUid_1);
                bt_cleanRight.setBackgroundResource(R.drawable.btn_cleanright);
                if (handler.hasMessages(4)) {
                    handler.removeMessages(4);
                }
                handler.sendEmptyMessageDelayed(4, 1000 * 5);
            }
        });


    }

    private CompoundButton.OnCheckedChangeListener checkedListener = (compoundButton, b) -> {
        tv_bootState.setText(b ? "关机" : "开机");
        sendOrder(new byte[]{b ? BLEUtil.ble_PowerON : BLEUtil.ble_PowerOff}, BLEUtil.BLE_writeUUid_1);
    };

    private void refresh() {
        Log.e("Pan", "刷新数据");
        if (isFinishing()) {
            Log.e("Pan", "Activity以关闭");
            return;
        }
        BleManager.getInstance().read(
                bleDevice,
                BLEUtil.BLE_serviceUUid,
                BLEUtil.BLE_character_Read_Number,
                new BleReadCallback() {
                    @Override
                    public void onReadSuccess(byte[] values) {
                        Log.e("Pan", "蓝牙数据读取完成");
                        if (isSending) {
                            Log.e("Pan", "蓝牙正在发送");
                            return;
                        }
                        //0x53+【开机状态】+【剩余电量】+【清洗时长】+【短喷间隔时间】+【短喷喷雾强度】+【开机清洗使能】 +【工作模式】+【长喷间隔时间】+【长喷喷雾强度】。
                        bm = new BluetoothModel(values);
                        if (!DevcieVersion.isEmpty()) {
                            String version1 = DevcieVersion + "-" + bm.getAutoClean() + "-" + bm.getCleanTime();
                            tv_version.setText(version1);
                        }

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
                        tv_bootState.setText(bm.getState() == BLEUtil.state_boot ? "关机" : "开机");
                        sw_bootSwitch.setOnCheckedChangeListener(null);
                        sw_bootSwitch.setChecked(bm.getState() == BLEUtil.state_boot);
                        sw_bootSwitch.setOnCheckedChangeListener(checkedListener);
                        //检查当前蓝牙所开启的模式在设备列表中是否存在

                        int isAlreadyAuto = -1;
                        int isAlreadyShort = -1;
                        int isAlreadyLong = -1;

                        if (bm.getModel() == BLEUtil.ble_Mode_Short_Long) {
                            Log.e("Pan", "长短模式");
                            for (int i = 0; i < device.getList().size(); i++) {
                                device.getList().get(i).setSelected(false);
                                if (device.getList().get(i).getModel() == BLEUtil.ble_Mode_Short &&
                                        device.getList().get(i).getShortTime() == bm.getShortTime() &&
                                        device.getList().get(i).getShortStrength() == bm.getShortStrength()
                                ) {
                                    isAlreadyShort = i;
                                }
                                if (device.getList().get(i).getModel() == BLEUtil.ble_Mode_Long &&
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
                                        (bm.getModel() == BLEUtil.ble_Mode_Intelligent1 ||
                                                bm.getModel() == BLEUtil.ble_Mode_Intelligent2 ||
                                                bm.getModel() == BLEUtil.ble_Mode_Intelligent3
                                        ) &&
                                                (device.getList().get(i).getModel() == BLEUtil.ble_Mode_Intelligent1 ||
                                                        device.getList().get(i).getModel() == BLEUtil.ble_Mode_Intelligent2 ||
                                                        device.getList().get(i).getModel() == BLEUtil.ble_Mode_Intelligent3
                                                )
                                ) {
                                    device.getList().get(i).setModelName("智能模式");
                                    device.getList().get(i).setModel(bm.getModel());
                                    isAlreadyAuto = i;
                                    continue;
                                }
                                if (device.getList().get(i).getModel() == bm.getModel()) {
                                    if (bm.getModel() == BLEUtil.ble_Mode_Short) {
                                        //（短）模式
                                        if (device.getList().get(i).getShortTime() == bm.getShortTime() &&
                                                device.getList().get(i).getShortStrength() == bm.getShortStrength()) {
                                            isAlreadyShort = i;
                                        }
                                    }
                                    if (bm.getModel() == BLEUtil.ble_Mode_Long) {
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
                            if (settingListModel.getModel() == BLEUtil.ble_Mode_Intelligent1 ||
                                    settingListModel.getModel() == BLEUtil.ble_Mode_Intelligent2 ||
                                    settingListModel.getModel() == BLEUtil.ble_Mode_Intelligent3) {
                                hasAuto = true;
                                break;
                            }
                        }
                        if (!hasAuto) {
                            SettingListModel bsm = new SettingListModel();
                            bsm.setModelName("智能模式");
                            bsm.setModel(BLEUtil.ble_Mode_Intelligent1);
                            List<SettingListModel> list = new ArrayList<>();
                            list.add(bsm);
                            list.addAll(device.getList());
                            device.getList().clear();
                            device.getList().addAll(list);
                            Log.e("Pan", "添加智能模式=" + new Gson().toJson(device));
                        }

                        if (isAlreadyShort < 0 && (bm.getModel() == BLEUtil.ble_Mode_Short || bm.getModel() == BLEUtil.ble_Mode_Short_Long)) {
                            SettingListModel bsm = new SettingListModel();
                            bsm.setModel(BLEUtil.ble_Mode_Short);
                            bsm.setShortTime(bm.getShortTime());
                            bsm.setShortStrength(bm.getShortStrength());
                            bsm.setSelected(true);
                            device.getList().add(bsm);
                            Log.e("Pan", "添加普通模式=" + new Gson().toJson(device));
                        }
                        if (isAlreadyLong < 0 && (bm.getModel() == BLEUtil.ble_Mode_Long || bm.getModel() == BLEUtil.ble_Mode_Short_Long)) {
                            SettingListModel bsm = new SettingListModel();
                            bsm.setModel(BLEUtil.ble_Mode_Long);
                            bsm.setLongTime(bm.getLongTime());
                            bsm.setLongStrength(bm.getLongStrength());
                            bsm.setSelected(true);
                            device.getList().add(bsm);
                            Log.e("Pan", "添加普通模式=" + new Gson().toJson(device));
                        }
                        setData();

                        formatData();

                        Log.e("Pan", "device=" + new Gson().toJson(device));
                        bma.notifyDataSetChanged();
                        SpfUtils.saveBluetoothSetList(device.getList(), deviceId);

//                            bluetoothUtil.removeAllOrder();
                        if (bm.getAutoClean() == BLEUtil.ble_AutoCleanOff) {
                            sendOrder(new byte[]{BLEUtil.ble_AutoCleanOn}, BLEUtil.BLE_writeUUid_1);
                        }

                        //0 1  2  3  4  5
                        //0 20 40 60 80 100
                        //5 10 15 20 25 30
                        if (isSetting) {
                            if (cleanTime == bm.getCleanTime()) {
                                isSetting = false;
                                tv_cleanValue.setText("出液量: " + ((cleanTime - 6) / 3 + 2) * 10 + "%");
                                sb_cleanValue.setProgress((cleanTime - 6) / 3);
                            } else {
                                Log.e("Pan", "设置中.....");
                                sendOrder(new byte[]{BLEUtil.ble_SprayQuantity, BLEUtil.OX_ORDER[cleanTime]}, BLEUtil.BLE_writeUUid_2);
                            }
                        } else {
                            cleanTime = bm.getCleanTime();
                            tv_cleanValue.setText("出液量: " + ((cleanTime - 6) / 3 + 2) * 10 + "%");
                            sb_cleanValue.setProgress((cleanTime - 6) / 3);
                        }

                        Log.e("Pan", "启动计时刷新");
                        if (handler.hasMessages(0)) {
                            handler.removeMessages(0);
                        }
                        handler.sendEmptyMessageDelayed(0, 1000 * 5);
                    }

                    @Override
                    public void onReadFailure(BleException exception) {
                        Log.e("Pan", "刷新蓝牙数据失败，倒计时5秒重新获取");
                        if (handler.hasMessages(0)) {
                            handler.removeMessages(0);
                        }
                        handler.sendEmptyMessageDelayed(0, 1000 * 5);
                    }
                }
        );
    }

    private void readStatus() {
        Log.e("Pan", "读取蓝牙状态");
        if (isFinishing()) {
            Log.e("Pan", "Activity以关闭");
            return;
        }
        Log.e("Pan", "开始读取蓝牙");
        BleManager.getInstance().read(
                bleDevice,
                BLEUtil.BLE_serviceUUid,
                BLEUtil.BLE_character_Read_Number,
                new BleReadCallback() {
                    @Override
                    public void onReadSuccess(byte[] values) {
                        Log.e("Pan", "蓝牙数据读取成功");
                        if (isSending) {
                            Log.e("Pan", "蓝牙正在发送");
                            return;
                        }
                        //0x53+【开机状态】+【剩余电量】+【清洗时长】+【短喷间隔时间】+【短喷喷雾强度】+【开机清洗使能】 +【工作模式】+【长喷间隔时间】+【长喷喷雾强度】。
                        bm = new BluetoothModel(values);
                        if (!DevcieVersion.isEmpty()) {
                            String version1 = DevcieVersion + "-" + bm.getAutoClean() + "-" + bm.getCleanTime();
                            tv_version.setText(version1);
                        }

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
                        tv_bootState.setText(bm.getState() == BLEUtil.state_boot ? "关机" : "开机");
                        sw_bootSwitch.setOnCheckedChangeListener(null);
                        sw_bootSwitch.setChecked(bm.getState() == BLEUtil.state_boot);
                        sw_bootSwitch.setOnCheckedChangeListener(checkedListener);
                        //检查当前蓝牙所开启的模式在设备列表中是否存在

                        int isAlreadyAuto = -1;
                        int isAlreadyShort = -1;
                        int isAlreadyLong = -1;

                        if (bm.getModel() == BLEUtil.ble_Mode_Short_Long) {
                            Log.e("Pan", "长短模式");
                            for (int i = 0; i < device.getList().size(); i++) {
                                device.getList().get(i).setSelected(false);
                                if (device.getList().get(i).getModel() == BLEUtil.ble_Mode_Short &&
                                        device.getList().get(i).getShortTime() == bm.getShortTime() &&
                                        device.getList().get(i).getShortStrength() == bm.getShortStrength()
                                ) {
                                    isAlreadyShort = i;
                                }
                                if (device.getList().get(i).getModel() == BLEUtil.ble_Mode_Long &&
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
                                        (bm.getModel() == BLEUtil.ble_Mode_Intelligent1 ||
                                                bm.getModel() == BLEUtil.ble_Mode_Intelligent2 ||
                                                bm.getModel() == BLEUtil.ble_Mode_Intelligent3
                                        ) &&
                                                (device.getList().get(i).getModel() == BLEUtil.ble_Mode_Intelligent1 ||
                                                        device.getList().get(i).getModel() == BLEUtil.ble_Mode_Intelligent2 ||
                                                        device.getList().get(i).getModel() == BLEUtil.ble_Mode_Intelligent3
                                                )
                                ) {
                                    device.getList().get(i).setModelName("智能模式");
                                    device.getList().get(i).setModel(bm.getModel());
                                    isAlreadyAuto = i;
                                    continue;
                                }
                                if (device.getList().get(i).getModel() == bm.getModel()) {
                                    if (bm.getModel() == BLEUtil.ble_Mode_Short) {
                                        //（短）模式
                                        if (device.getList().get(i).getShortTime() == bm.getShortTime() &&
                                                device.getList().get(i).getShortStrength() == bm.getShortStrength()) {
                                            isAlreadyShort = i;
                                        }
                                    }
                                    if (bm.getModel() == BLEUtil.ble_Mode_Long) {
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
                            if (settingListModel.getModel() == BLEUtil.ble_Mode_Intelligent1 ||
                                    settingListModel.getModel() == BLEUtil.ble_Mode_Intelligent2 ||
                                    settingListModel.getModel() == BLEUtil.ble_Mode_Intelligent3) {
                                hasAuto = true;
                                break;
                            }
                        }
                        if (!hasAuto) {
                            SettingListModel bsm = new SettingListModel();
                            bsm.setModelName("智能模式");
                            bsm.setModel(BLEUtil.ble_Mode_Intelligent1);
                            List<SettingListModel> list = new ArrayList<>();
                            list.add(bsm);
                            list.addAll(device.getList());
                            device.getList().clear();
                            device.getList().addAll(list);
                            Log.e("Pan", "添加智能模式=" + new Gson().toJson(device));
                        }

                        if (isAlreadyShort < 0 && (bm.getModel() == BLEUtil.ble_Mode_Short || bm.getModel() == BLEUtil.ble_Mode_Short_Long)) {
                            SettingListModel bsm = new SettingListModel();
                            bsm.setModel(BLEUtil.ble_Mode_Short);
                            bsm.setShortTime(bm.getShortTime());
                            bsm.setShortStrength(bm.getShortStrength());
                            bsm.setSelected(true);
                            device.getList().add(bsm);
                            Log.e("Pan", "添加普通模式=" + new Gson().toJson(device));
                        }
                        if (isAlreadyLong < 0 && (bm.getModel() == BLEUtil.ble_Mode_Long || bm.getModel() == BLEUtil.ble_Mode_Short_Long)) {
                            SettingListModel bsm = new SettingListModel();
                            bsm.setModel(BLEUtil.ble_Mode_Long);
                            bsm.setLongTime(bm.getLongTime());
                            bsm.setLongStrength(bm.getLongStrength());
                            bsm.setSelected(true);
                            device.getList().add(bsm);
                            Log.e("Pan", "添加普通模式=" + new Gson().toJson(device));
                        }
                        setData();

                        formatData();

                        Log.e("Pan", "device=" + new Gson().toJson(device));
                        bma.notifyDataSetChanged();
                        SpfUtils.saveBluetoothSetList(device.getList(), deviceId);

//                            bluetoothUtil.removeAllOrder();
                        if (bm.getAutoClean() == BLEUtil.ble_AutoCleanOff) {
                            sendOrder(new byte[]{BLEUtil.ble_AutoCleanOn}, BLEUtil.BLE_writeUUid_1);
                        }

                        //0 1  2  3  4  5
                        //0 20 40 60 80 100
                        //5 10 15 20 25 30
                        if (isSetting) {
                            if (cleanTime == bm.getCleanTime()) {
                                isSetting = false;
                                tv_cleanValue.setText("出液量: " + ((cleanTime - 6) / 3 + 2) * 10 + "%");
                                sb_cleanValue.setProgress((cleanTime - 6) / 3);
                            } else {
                                Log.e("Pan", "设置中.....");
                                sendOrder(new byte[]{BLEUtil.ble_SprayQuantity, BLEUtil.OX_ORDER[cleanTime]}, BLEUtil.BLE_writeUUid_2);
                            }
                        } else {
                            cleanTime = bm.getCleanTime();
                            tv_cleanValue.setText("出液量: " + ((cleanTime - 6) / 3 + 2) * 10 + "%");
                            sb_cleanValue.setProgress((cleanTime - 6) / 3);
                        }
//                        Log.e("Pan", "重新倒计时读取蓝牙");
                        handler.sendEmptyMessageDelayed(0, 1000 * 30);
                    }

                    @Override
                    public void onReadFailure(BleException exception) {
                        Log.e("Pan", "蓝牙读取失败，重新读取");
                        handler.sendEmptyMessageDelayed(0, 1000 * 30);
                    }
                }
        );
    }

    private void readVersion() {
        Log.e("Pan", "读取版本号");
        if (isFinishing()) {
            Log.e("Pan", "Activity以关闭");
            return;
        }
        BleManager.getInstance().read(
                bleDevice,
                BLEUtil.BLE_serviceUUid,
                BLEUtil.BLE_character_Read_Version,
                new BleReadCallback() {
                    @Override
                    public void onReadSuccess(byte[] data) {
                        Log.e("Pan", "读取版本号成功");
                        DevcieVersion = "版本号:" + getVersion(data);
                        handler.sendEmptyMessage(22);
                    }

                    @Override
                    public void onReadFailure(BleException exception) {
                        Log.e("Pan", "读取版本号失败，重新读取");
                        readVersion();
                    }
                });

    }

    private String getVersion(byte[] values) {
        String bt = "";
        for (byte aByte : values) {
            bt += aByte & 0xFF;
        }
        String bts = "";
        for (int i = 0; i < bt.length(); i++) {
            if (i == bt.length() - 1) {
                bts += bt.charAt(i);
            } else {
                bts += bt.charAt(i) + ".";
            }
        }
        Log.e("Pan", "bt=" + bt);
        Log.e("Pan", "bts=" + bts);
        return bts;
    }

    private void setData() {
        for (SettingListModel slm : device.getList()) {
            if (slm.getModel() == BLEUtil.ble_Mode_Long) {
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
            if (slm.getModel() == BLEUtil.ble_Mode_Short) {
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
//                device.getList().get(0).setSelected(false);
//                bma.notifyItemChanged(0);
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
            settingModel = BLEUtil.ble_Mode_Intelligent1;
        });

        tv_model2.setOnClickListener(view -> {
            tv_model1.setBackgroundResource(R.drawable.dialog_zn_btn_unselect_bg);
            tv_model1.setTextColor(getResources().getColor(R.color.color66));
            tv_model2.setBackgroundResource(R.drawable.dialog_zn_btn_select_bg);
            tv_model2.setTextColor(getResources().getColor(R.color.color_blue));
            tv_model3.setBackgroundResource(R.drawable.dialog_zn_btn_unselect_bg);
            tv_model3.setTextColor(getResources().getColor(R.color.color66));
            settingModel = BLEUtil.ble_Mode_Intelligent2;
        });

        tv_model3.setOnClickListener(view -> {
            tv_model1.setBackgroundResource(R.drawable.dialog_zn_btn_unselect_bg);
            tv_model1.setTextColor(getResources().getColor(R.color.color66));
            tv_model2.setBackgroundResource(R.drawable.dialog_zn_btn_unselect_bg);
            tv_model2.setTextColor(getResources().getColor(R.color.color66));
            tv_model3.setBackgroundResource(R.drawable.dialog_zn_btn_select_bg);
            tv_model3.setTextColor(getResources().getColor(R.color.color_blue));
            settingModel = BLEUtil.ble_Mode_Intelligent3;
        });

        switch (device.getList().get(0).getModel()) {
            case BLEUtil.ble_Mode_Intelligent1:
                tv_model1.performClick();
                break;
            case BLEUtil.ble_Mode_Intelligent2:
                tv_model2.performClick();
                break;
            case BLEUtil.ble_Mode_Intelligent3:
                tv_model3.performClick();
                break;
        }

        dialog.findViewById(R.id.tv_sure).setOnClickListener(view -> {

//            bluetoothUtil.removeAllOrder();
            Log.e("Pan", "settingModel=" + settingModel);
            Log.e("Pan", "getModel=" + device.getList().get(0).getModel());
            Log.e("Pan", "bm=" + bm.getModel());
            Log.e("Pan", "isSelected=" + device.getList().get(0).isSelected());
//            switch (settingModel) {
//                case BLEUtil.ble_Mode_Intelligent1:
//                    if (bm.getModel() != BLEUtil.ble_Mode_Intelligent1) {
//                        sendOrder(new byte[]{BLEUtil.ble_Mode_Intelligent1}, BLEUtil.BLE_writeUUid_1);
//                    }
//                    break;
//                case BLEUtil.ble_Mode_Intelligent2:
//                    if (bm.getModel() != BLEUtil.ble_Mode_Intelligent2) {
//                        sendOrder(new byte[]{BLEUtil.ble_Mode_Intelligent2}, BLEUtil.BLE_writeUUid_1);
//                    }
//                    break;
//                case BLEUtil.ble_Mode_Intelligent3:
//                    if (bm.getModel() != BLEUtil.ble_Mode_Intelligent3) {
//                        sendOrder(new byte[]{BLEUtil.ble_Mode_Intelligent3}, BLEUtil.BLE_writeUUid_1);
//                    }
//                    break;
//            }
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
        Log.e("Pan", "读取状态");
        refresh();
        readVersion();
        super.onResume();
    }

    @Override
    public void onPause() {
        if (handler.hasMessages(0)) {
            handler.removeMessages(0);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //移除30秒读状态的定时器
        if (handler.hasMessages(0)) {
            handler.removeMessages(0);
        }
        BleManager.getInstance().disconnect(bleDevice);
        BleManager.getInstance().destroy();
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
                    Log.e("Pan", "添加");
                    device.getList().add(bsm);
                    break;
                case TYPE_MODIFY:
                    Log.e("Pan", "修改");
                    device.getList().set(index, bsm);
                    if (device.getList().get(index).isSelected()) {
                        setModel(index, true);
                    }
                    break;
            }
            formatData();

            SpfUtils.saveBluetoothSetList(device.getList(), deviceId);
            bma.notifyDataSetChanged();
        }
    }

    public void formatData() {

        Log.e("Pan", "device.getList()=" + device.getList().size());
        SettingListModel dataNull = new SettingListModel();
        dataNull.setModel(BLEUtil.modeNull);

        SettingListModel smart = new SettingListModel();

        List<SettingListModel> shortList = new ArrayList<>();

        List<SettingListModel> longList = new ArrayList<>();

        for (SettingListModel slm : device.getList()) {
            if (slm.getModel() == BLEUtil.ble_Mode_Short) {
                boolean has = false;
                for (SettingListModel slm2 : shortList) {
                    if (slm2.getShortTime() == slm.getShortTime() && slm2.getShortStrength() == slm.getShortStrength()) {
                        has = true;
                        break;
                    }
                }
                if (!has) {
                    shortList.add(slm);
                }
            }
            if (slm.getModel() == BLEUtil.ble_Mode_Long) {
                boolean has = false;
                for (SettingListModel slm2 : longList) {
                    if (slm2.getLongTime() == slm.getLongTime() && slm2.getLongStrength() == slm.getLongStrength()) {
                        has = true;
                        break;
                    }
                }
                if (!has) {
                    longList.add(slm);
                }
            }
            if (slm.getModel() == BLEUtil.ble_Mode_Intelligent1 ||
                    slm.getModel() == BLEUtil.ble_Mode_Intelligent2 ||
                    slm.getModel() == BLEUtil.ble_Mode_Intelligent3) {
                smart = slm;
            }
        }
        device.getList().clear();
        device.getList().add(smart);

        if (shortList.size() > 0 || longList.size() > 0) {
            device.getList().add(dataNull);
        }

        if (shortList.size() > 0) {
            device.getList().addAll(shortList);
            device.getList().add(dataNull);
        }

        if (longList.size() > 0) {
            device.getList().addAll(longList);
            device.getList().add(dataNull);
        }


        Log.e("Pan", "shortList=" + new Gson().toJson(shortList));
        Log.e("Pan", "longList=" + new Gson().toJson(longList));
        Log.e("Pan", "smart=" + new Gson().toJson(smart));
    }


    List<byte[]> Orders = new ArrayList<>();

    private void write() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (byte[] order : Orders) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendOrder(order, "");
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                readStatus();
            }
        }).start();

    }

    private void sendOrder(byte[] data, String write_id) {
        Log.e("Pan", "发送指令：" + gson.toJson(data));
        new send(data,write_id).start();
    }

     class send extends Thread{
        byte[] data;
        String write_id;
        public send(byte[] data, String write_id) {
            this.data=data;
            this.write_id=write_id;
        }

        @Override
        public void run() {
            if(data==null){
                return;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            BleManager.getInstance().write(
                    bleDevice,
                    BLEUtil.BLE_serviceUUid,
                    write_id,
                    data,
                    new BleWriteCallback() {
                        @Override
                        public void onWriteSuccess(int current, int total, byte[] justWrite) {
                            Log.e("Pan", "指令发送成功----"+gson.toJson(justWrite));
                            data=null;
                        }

                        @Override
                        public void onWriteFailure(BleException exception) {
                            Log.e("Pan", "指令发送失败：" + exception);
                            run();
                        }
                    }
            );
        }

    }



}
