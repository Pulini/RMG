package com.yiyun.rmj.activity.bluetooth;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.hjq.toast.ToastUtils;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yiyun.rmj.view.indicatorseekbar.SizeUtils;

import com.yiyun.rmj.R;
import com.yiyun.rmj.adapter.CommonRecyclerViewAdapter;
import com.yiyun.rmj.adapter.CommonRecyclerViewHolder;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.BluetoothBean;
import com.yiyun.rmj.bean.BluetoothSettingBean;
import com.yiyun.rmj.bluetooth.NewBleBluetoothUtil;
import com.yiyun.rmj.dialog.RoundEditDialog;
import com.yiyun.rmj.utils.DisplayUtils;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.utils.PermissionUtil;
import com.yiyun.rmj.utils.SpfUtils;
import com.yiyun.rmj.utils.StatusBarUtil;
import com.yiyun.rmj.view.ElectricView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.jake.share.frdialog.dialog.FRDialog;

//保存设置界面
public class BluetoothMainActivity extends BaseActivity implements View.OnClickListener {

    ElectricView tv_eric;
    SwipeRecyclerView rv_list;
    ArrayList<BluetoothSettingBean> listData;
    NewBleBluetoothUtil bluetoothUtil;
    private int deviceId;
    private BluetoothBean device;
    MyCommRecAdapter adapter;
    String[] strStrenth = new String[]{"弱", "中", "强", "MAX"};
    String[] strStrenthNewMode = new String[]{"阅读模式", "电竞模式", "美容模式 ", "MAX"};
    public static final int resultCode_exit = 33;
    public static final int TYPE_ADD = 0;
    public static final int TYPE_MODIFY = 1;
    private TextView tv_device_number;
    private FRDialog dialog;
    RelativeLayout rll_update_bg;
    LinearLayout ll_add_dialog;
    TextView tv_small, tv_mid, tv_big;
    private int selectStrength = 0;  //0：小 1：中 2：大
    private TextView tv_power;
    private TextView tv_boot_state;
    private Switch sw_boot_or_shutdown;
    private boolean isNeedSendOrder = true;
    byte[] bytes; //0x53+【开机状态】+【剩余电量】+【清洗时长】+【标准间隔时间】+【标准喷雾强度】+【开机清洗使能】 +【工作模式】
    private int currentBootState;// 开机状态
    private int currentElect; //电量
    private int currentIntervalTime; //间隔时间
    private int currentStrength; //喷雾强度
    private int currentMode; //工作模式
    private int Index; //当前修改的Item下标
    private boolean isGotoBluetoothControlDetailActivity = false;

    boolean isFirst = true;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 11:
                    int Orders = msg.getData().getInt("Orders");
//                    rl_loading.setVisibility(View.VISIBLE);
//                    tv_msg.setText("正在发送指令...");
                    break;
                case 12:
//                    int index = msg.getData().getInt("index");
//                    int Orders1 = msg.getData().getInt("Orders");
//                    tv_msg.setText("正在发送指令：" + index + "/" + Orders1);
                    break;
                case 13:

//                    rl_loading.setVisibility(View.GONE);
//                    tv_msg.setText("正在读取设备信息...");
                    break;
                case 3:
                    //检查参数
//                    if(checkParmSetIsSuccess()){
//                        ToastUtils.show("参数修改成功");
//                    }else{
//                        ToastUtils.show("参数修改失败");
//                    }
                case 1:
//                    rl_loading.setVisibility(View.GONE);
                    initParm();
//                    currentBootState = bytes[1]; //开机状态
//                    currentElect = bytes[2];  //电量
//                    currentIntervalTime = bytes[4]; //间隔时间
//                    currentStrength = bytes[5]; //喷雾强度
//                    currentMode = bytes[7]; //工作模式

                    Log.e("readStatus", "currentBootState:" + currentBootState + "  currentElect : " + currentElect + "  currentIntervalTime : " + currentIntervalTime +
                            "  currentStrength : " + currentStrength + " currentMode ： " + currentMode);

                    Log.e("readStatus", " case 1: adapter.getItemCount() : " + adapter.getItemCount());
                    Log.e("readStatus", "case 1: listData.size : " + listData.size());
                    Log.e("readStatus", "case 1: listData : " + listData);

                    if (currentBootState == NewBleBluetoothUtil.state_boot) {
                        tv_boot_state.setText("关机");
                        sw_boot_or_shutdown.setChecked(true);
                    } else {
                        tv_boot_state.setText("开机");
                        sw_boot_or_shutdown.setChecked(false);
                    }

                    int count = currentElect / 10;
                    tv_eric.setCount(count);
                    tv_power.setText(currentElect + "%");

                    for (BluetoothSettingBean listDatum : listData) {
                        listDatum.setSelected(false);
//                        listDatum.save();
                    }
                    if (currentMode == 65 || currentMode == 66 || currentMode == 67) {
                        for (BluetoothSettingBean listDatum : listData) {
                            if (listDatum.getDeviceName().equals("智能模式")) {
                                LogUtils.LogE("模式" + listDatum.getStrength());
                                if (currentMode == 65) {
                                    listDatum.setStrength(1);
                                }
                                if (currentMode == 66) {
                                    listDatum.setStrength(2);
                                }
                                if (currentMode == 67) {
                                    listDatum.setStrength(3);
                                }
                                listDatum.setSelected(true);
                            }
                        }
                    } else {
                        isFirst = false;
                        if (currentIntervalTime > 0) {
                            for (BluetoothSettingBean listDatum : listData) {
                                if (listDatum.getStrength() == currentStrength && listDatum.getTime() == currentIntervalTime) {
                                    listDatum.setSelected(true);
//                                    listDatum.save();
                                    if (adapter != null) {
                                        adapter.setData(listData);
                                    }
                                    return;
                                }
                            }
                            BluetoothSettingBean bluetoothSettingBean = new BluetoothSettingBean();
                            bluetoothSettingBean.setDeviceName(device.getDeviceName());
                            bluetoothSettingBean.setDeviceId(device.getId());
                            bluetoothSettingBean.setStrength(currentStrength);
                            bluetoothSettingBean.setTime(currentIntervalTime);
                            bluetoothSettingBean.setSelected(true);
//                            bluetoothSettingBean.save();
                            listData.add(bluetoothSettingBean);
                        }
                    }
//                    for (BluetoothSettingBean listDatum : listData) {
//                        listDatum.save();
//                    }
                    if (adapter != null) {
                        adapter.setData(listData);
                    }

//                    if (listData == null || listData.isEmpty()) {
//                        LogUtils.LogE("------------------2-------------------");
//                        LogUtils.LogE("1DeviceName="+deivce.getDeviceName());
//                        BluetoothSettingBean bluetoothSettingBean = new BluetoothSettingBean();
//                        bluetoothSettingBean.setDeviceName(deivce.getDeviceName());
//                        bluetoothSettingBean.setDeviceId(deivce.getId());
//                        bluetoothSettingBean.setStrength(currentStrength);
//                        bluetoothSettingBean.setTime(currentIntervalTime);
//                        bluetoothSettingBean.setSelected(true);
//                        listData.add(bluetoothSettingBean);
//                        bluetoothSettingBean.save();
//                        if (adapter != null) {
//                            adapter.setData(listData);
//                        }
//                    } else {
//                        BluetoothSettingBean currentBluetoothSettingBean = null;
//                        int currentPosition = -1;
//                        for (int i = 0; i < listData.size(); i++) {
//                            BluetoothSettingBean listDatum = listData.get(i);
//                            if (listDatum != null && (listDatum.getStrength() == currentStrength) && (listDatum.getTime() == currentIntervalTime)) {
//                                currentBluetoothSettingBean = listDatum;
//                                currentPosition = i;
//                                currentBluetoothSettingBean.setStrength(currentStrength);
//                                currentBluetoothSettingBean.save();
//                                break;
//                            }
//                        }
//
//                        if (currentBluetoothSettingBean == null) {
//                            for (BluetoothSettingBean listDatum : listData) {
//                                listDatum.setSelected(false);
//                                listDatum.save();
//                            }
//                            LogUtils.LogE("------------------3-------------------");
//                            LogUtils.LogE("2DeviceName="+deivce.getDeviceName());
//                            BluetoothSettingBean bluetoothSettingBean = new BluetoothSettingBean();
//                            bluetoothSettingBean.setDeviceName(deivce.getDeviceName());
//                            bluetoothSettingBean.setDeviceId(deivce.getId());
//                            bluetoothSettingBean.setStrength(currentStrength);
//                            bluetoothSettingBean.setSelected((deivce.getId() == -2));
//                            bluetoothSettingBean.setTime(currentIntervalTime);
//                            bluetoothSettingBean.setSelected(true);
//                            listData.add(bluetoothSettingBean);
//                            bluetoothSettingBean.save();
//                            if (adapter != null) {
//                                adapter.setData(listData);
//                            }
//                        } else {
//                            boolean isMain = (currentMode == 41 || currentMode == 42 || currentMode == 42);
//                            if (isMain) {
//                                for (int i = 0; i < listData.size(); i++) {
//                                    BluetoothSettingBean listDatum = listData.get(i);
//                                    if (i != currentPosition) {
//                                        listDatum.setSelected(false);
//                                    } else {
//                                        listDatum.setSelected(true);
//                                    }
//                                    listDatum.save();
//                                }
//                            } else if (currentMode == 40) {
//                                int selectCount = 0;
//                                for (int i = 0; i < listData.size(); i++) {
//                                    BluetoothSettingBean listDatum = listData.get(i);
//                                    if (i != currentPosition) {
//                                        if (listDatum.isSelected() && selectCount == 0) {
//                                            selectCount++;
//                                        } else {
//                                            listDatum.setSelected(false);
//                                        }
//                                    } else {
//                                        listDatum.setSelected(false);
//                                    }
//                                    listDatum.save();
//                                }
//                            } else {
//                                for (int i = 0; i < listData.size(); i++) {
//                                    BluetoothSettingBean listDatum = listData.get(i);
//                                    if (i != currentPosition) {
//                                        listDatum.setSelected(false);
//                                    } else {
//                                        listDatum.setSelected(true);
//                                    }
//                                    listDatum.save();
//                                }
//                                if (currentBluetoothSettingBean.isSelected()) {
//                                    for (int i = 0; i < listData.size(); i++) {
//                                        if (i != currentPosition) {
//                                            BluetoothSettingBean listDatum = listData.get(i);
//                                            listDatum.setSelected(false);
//                                            listDatum.save();
//                                        }
//                                    }
//                                } else {
//                                    int selectCount = 0;
//                                    for (BluetoothSettingBean listDatum : listData) {
//                                        if (listDatum.isSelected() && selectCount == 0) {
//                                            selectCount++;
//                                        } else {
//                                            listDatum.setSelected(false);
//                                            listDatum.save();
//                                        }
//                                    }
//                                }
//                            }
//                            if (adapter != null) {
//                                adapter.setData(listData);
//                            }
//                        }
//                    }
                    break;
                case 2:
                    //刷新Item数据
//                    for (BluetoothSettingBean listDatum : listData) {
//                        listDatum.save();
//                    }
                    adapter.setData(listData);
                    adapter.notifyDataSetChanged();
//                    adapter.notifyItemChanged(msg.arg1, "payload");
                    break;
                case 4:
                    initParm();
                    int counts = currentElect / 10;
                    tv_eric.setCount(counts);
                    tv_power.setText(currentElect + "%");

                    break;
            }
        }
    };

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            Log.i("readStatus", "runnable()  242");
            //do something
            readStatus(1);

            //每隔30s循环执行run方法
            handler.postDelayed(this, 30000);
        }
    };
    private RelativeLayout rl_loading;
    private TextView tv_msg;

    @Override
    public void dealOrderCallback(byte[] orderInfo) {
        super.dealOrderCallback(orderInfo);
        int order = orderInfo[0];

        switch (order) {
            case NewBleBluetoothUtil.shutdown:
                currentBootState = 0x00;
                break;
            case NewBleBluetoothUtil.boot:
                currentBootState = 0x01;
                break;
            case NewBleBluetoothUtil.setstrenth:
                currentStrength = orderInfo[1];
                break;
            case NewBleBluetoothUtil.mode_mild:
                currentMode = 0x41;
                break;
            case NewBleBluetoothUtil.mode_middle:
                currentMode = 0x42;
                break;
            case NewBleBluetoothUtil.mode_long:
                currentMode = 0x43;
                break;
            case NewBleBluetoothUtil.mode_short:
                currentMode = 0x40;
                break;
            case NewBleBluetoothUtil.settimeinterval:
                currentIntervalTime = orderInfo[1];
                break;
        }


    }

    private void initParm() {
        //0x53+【开机状态】+【剩余电量】+【清洗时长】+【普通间隔时间】+【普通喷雾强度】+【开机清洗使能】 +【工作模式】
        LogUtils.LogE("开机状态=" + bytes[1]);
        LogUtils.LogE("剩余电量=" + bytes[2]);
        LogUtils.LogE("清洗时长=" + bytes[3]);
        LogUtils.LogE("普通间隔时间=" + bytes[4]);
        LogUtils.LogE("普通喷雾强度=" + bytes[5]);
        LogUtils.LogE("开机清洗使能=" + bytes[6]);
        LogUtils.LogE("工作模式=" + bytes[7]);
        currentBootState = bytes[1]; //开机状态
        currentElect = bytes[2];  //电量
        currentIntervalTime = bytes[4]; //间隔时间
        currentStrength = bytes[5]; //喷雾强度
        currentMode = bytes[7]; //工作模式
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                bluetoothUtil.disconnectDevice();
                finish();
                break;
            case R.id.iv_title_blue_add:
                //右上角添加按钮
//                rll_update_bg.setVisibility(View.VISIBLE);
//                startShowAnimation();

                rll_update_bg.setVisibility(View.GONE);
                Intent bluecontrolDetailIntents = new Intent(BluetoothMainActivity.this, BluetoothControlDetailActivity.class);
                bluecontrolDetailIntents.putExtra("type", BluetoothMainActivity.TYPE_ADD);
                isGotoBluetoothControlDetailActivity = true;
                startActivityForResult(bluecontrolDetailIntents, TYPE_ADD);
                overridePendingTransition(R.anim.activity_rightclick_in, R.anim.activity_rightclick_out);
                break;

            case R.id.rll_update_bg:
            case R.id.tv_update:
                startHideAnimation();
                break;

            case R.id.tv_to_setting:
                rll_update_bg.setVisibility(View.GONE);
                Intent bluecontrolDetailIntent = new Intent(BluetoothMainActivity.this, BluetoothControlDetailActivity.class);
                bluecontrolDetailIntent.putExtra("type", BluetoothMainActivity.TYPE_ADD);
                startActivityForResult(bluecontrolDetailIntent, TYPE_ADD);
                overridePendingTransition(R.anim.activity_rightclick_in, R.anim.activity_rightclick_out);
                break;
        }
    }

    /**
     *
     */
    private int getIntelligentModeId() {
        for (int i = 0; i < listData.size(); i++) {
            if (listData.get(i).getRemarks().equals("智能模式")) {
                return i;
            }
        }
        return 0;
    }

    private BluetoothSettingBean getNormalSelectItem() {
        for (BluetoothSettingBean bluetoothSettingBean : listData) {
            if (!"智能模式".equals(bluetoothSettingBean.getRemarks())) {
                if (bluetoothSettingBean.isSelected()) {
                    return bluetoothSettingBean;
                }
            }
        }
        return null;
    }


    // 启动动画
    public void startShowAnimation() {
        Animation animation = AnimationUtils.loadAnimation(BluetoothMainActivity.this, R.anim.bluetooth_main_show_dialog);
        ll_add_dialog.startAnimation(animation);
    }

    private boolean isAnimationRunning = false;

    // 启动动画
    public void startHideAnimation() {
        if (isAnimationRunning) {
            return;
        }
        isAnimationRunning = true;
        Animation animation = AnimationUtils.loadAnimation(BluetoothMainActivity.this, R.anim.bluetooth_main_hide_dialog);
        ll_add_dialog.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rll_update_bg.setVisibility(View.INVISIBLE);
                isAnimationRunning = false;
            }
        }, 200);

    }

    public void upBtnState(int select) {

        selectStrength = select;

        tv_small.setBackgroundResource(R.drawable.dialog_zn_btn_unselect_bg);
        tv_small.setTextColor(BluetoothMainActivity.this.getResources().getColor(R.color.color66));
        tv_mid.setBackgroundResource(R.drawable.dialog_zn_btn_unselect_bg);
        tv_mid.setTextColor(BluetoothMainActivity.this.getResources().getColor(R.color.color66));
        tv_big.setBackgroundResource(R.drawable.dialog_zn_btn_unselect_bg);
        tv_big.setTextColor(BluetoothMainActivity.this.getResources().getColor(R.color.color66));

        if (select == 1) {
            //选中第一个属性（小）
            tv_small.setBackgroundResource(R.drawable.dialog_zn_btn_select_bg);
            tv_small.setTextColor(BluetoothMainActivity.this.getResources().getColor(R.color.color_blue));

        } else if (select == 2) {
            //选中第二个属性（中）
            tv_mid.setBackgroundResource(R.drawable.dialog_zn_btn_select_bg);
            tv_mid.setTextColor(BluetoothMainActivity.this.getResources().getColor(R.color.color_blue));

        } else if (select == 3) {
            //选中第二个属性（大）
            tv_big.setBackgroundResource(R.drawable.dialog_zn_btn_select_bg);
            tv_big.setTextColor(BluetoothMainActivity.this.getResources().getColor(R.color.color_blue));
        }
    }

    public void initDialog() {

        dialog = new FRDialog.CommonBuilder(this).setContentView(R.layout.dialog_bluetooth_zn).create();

        tv_small = dialog.findViewById(R.id.tv_strength_small);
        tv_mid = dialog.findViewById(R.id.tv_strength_mid);
        tv_big = dialog.findViewById(R.id.tv_strength_big);

        ImageView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        TextView tv_sure = dialog.findViewById(R.id.tv_sure);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("bcz", "selectStrength:" + selectStrength);
                switch (selectStrength) {
                    case 1:

                        if (currentMode != NewBleBluetoothUtil.mode_mild) {
                            bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.mode_mild, 0);
                        }
//                        readStatusToQuee();
                        bluetoothUtil.sendOrder();

                        break;
                    case 2:

                        if (currentMode != NewBleBluetoothUtil.mode_middle) {
                            bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.mode_middle, 0);
                        }
//                        readStatusToQuee();
                        bluetoothUtil.sendOrder();

                        break;
                    case 3:
                        if (currentMode != NewBleBluetoothUtil.mode_middle) {
                            bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.mode_long, 0);
                        }
//                        readStatusToQuee();
                        bluetoothUtil.sendOrder();

                        break;
                }
                BluetoothSettingBean bluetoothSettingBean = listData.get(getIntelligentModeId());
                bluetoothSettingBean.setStrength(selectStrength);
                bluetoothSettingBean.setSelected(true);
//                bluetoothSettingBean.save();
                adapter.notifyDataSetChanged();
                //强度小
                dialog.dismiss();
            }
        });

        tv_small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upBtnState(1);
            }
        });

        tv_mid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upBtnState(2);
            }
        });

        tv_big.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upBtnState(3);
            }
        });

        upBtnState(listData.get(getIntelligentModeId()).getStrength());
        dialog.setCanceledOnTouchOutside(false);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bluetooth_main;
    }

    public void initTitleView() {
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText("");

        ImageView iv_add = (ImageView) findViewById(R.id.iv_title_blue_add);
        iv_add.setVisibility(View.VISIBLE);
        iv_add.setOnClickListener(this);

    }

    @Override
    protected void initView() {

        initTitleView();
        bluetoothUtil = NewBleBluetoothUtil.getInstance();
        bluetoothUtil.setBlutToothListener(new NewBleBluetoothUtil.OnBlutToothListener() {
            @Override
            public void onStartSend(int Orders) {
                Bundle bundle = new Bundle();
                bundle.putInt("Orders", Orders);
                Message message = new Message();
                message.what = 11;
                message.setData(bundle);
                handler.sendMessage(message);
            }

            @Override
            public void onSending( int index,  int Orders) {
                Bundle bundle = new Bundle();
                bundle.putInt("index", index);
                bundle.putInt("Orders", Orders);
                Message message = new Message();
                message.what = 12;
                message.setData(bundle);
                handler.sendMessage(message);
            }

            @Override
            public void onSendFinish() {
//                ToastUtils.show("指令发送成功！");
                handler.sendEmptyMessage(13);

            }
        });
        listData = new ArrayList<>();

        deviceId = getIntent().getIntExtra("deviceId", 0);
        tv_device_number = findViewById(R.id.tv_device_number);

        rl_loading = findViewById(R.id.rl_loading);
        tv_msg = findViewById(R.id.tv_msg);
        rl_loading.setVisibility(View.GONE);
        rl_loading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.LogE("正在操作");
            }
        });

        tv_boot_state = findViewById(R.id.tv_boot_state);
        tv_boot_state.setText("开机");

        sw_boot_or_shutdown = findViewById(R.id.sw_boot_or_shutdown);
        sw_boot_or_shutdown.setChecked(false);
        sw_boot_or_shutdown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //处于开机状态
                    tv_boot_state.setText("关机");
                    if (currentBootState == NewBleBluetoothUtil.state_shutdown) {
                        bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.boot, 0);
//                        readStatusToQuee();
                        bluetoothUtil.sendOrder();
                    }


                } else {
                    //处于关机状态
                    tv_boot_state.setText("开机");

                    if (currentBootState == NewBleBluetoothUtil.state_boot) {
                        bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.shutdown, 0);
//                        readStatusToQuee();
                        bluetoothUtil.sendOrder();
                    }
                }
            }
        });

        rv_list = findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyCommRecAdapter(this, listData);

        rv_list.setSwipeMenuCreator((leftMenu, rightMenu, position) -> {
            if (0 != position) {
                rightMenu.addMenuItem(
                        new SwipeMenuItem(this)
                                .setBackgroundColor(Color.parseColor("#FE0036"))
                                .setText("删除")
                                .setTextColor(Color.WHITE)
                                .setTextSize(14)
                                .setWidth(DisplayUtils.dp2px(this, 50))
                                .setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                );
            }
        });

        rv_list.setOnItemMenuClickListener((menuBridge, adapterPosition) -> {
            menuBridge.closeMenu();
            if (menuBridge.getDirection() == SwipeRecyclerView.RIGHT_DIRECTION) {
                if (menuBridge.getPosition() == 0) {
                    PermissionUtil.requestStoragePermission(BluetoothMainActivity.this, new PermissionUtil.IRequestPermissionCallBack() {
                        @Override
                        public void permissionSuccess() {
                            listData.get(adapterPosition).delete();
                            listData.remove(adapterPosition);
                            if (listData.size() == 1) {
                                bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.settimeinterval, 5);
                                bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.setstrenth, 64);
                                bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.boot, 0);
                                bluetoothUtil.sendOrder();
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

//        adapter.setHasStableIds(true);
        rv_list.setAdapter(adapter);

        adapter.setOnRecyclerViewItemClickListener(new CommonRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                if (position == 0) {
                    return;
                }

                isGotoBluetoothControlDetailActivity = true;
                Intent bluecontrolDetailIntent = new Intent(BluetoothMainActivity.this, BluetoothControlDetailActivity.class);
                bluecontrolDetailIntent.putExtra("type", BluetoothMainActivity.TYPE_MODIFY);
                BluetoothSettingBean setting = listData.get(position);
                bluecontrolDetailIntent.putExtra("setting", setting);
                startActivityForResult(bluecontrolDetailIntent, TYPE_MODIFY);
                overridePendingTransition(R.anim.activity_rightclick_in, R.anim.activity_rightclick_out);
                Index = position;
            }
        });

        tv_eric = findViewById(R.id.tv_eric);
        tv_power = findViewById(R.id.tv_power);

        rll_update_bg = findViewById(R.id.rll_update_bg);
        rll_update_bg.setOnClickListener(this);

        TextView tv_to_setting = findViewById(R.id.tv_to_setting);
        tv_to_setting.setOnClickListener(this);

        TextView tv_update = findViewById(R.id.tv_update);
        tv_update.setOnClickListener(this);

        ll_add_dialog = findViewById(R.id.ll_add_dialog);
//
//        device = DataSupport.find(BluetoothBean.class, deviceId);

    }

    @Override
    protected void initData() {
       /*
        PermissionUtil.requestStoragePermission(BluetoothMainActivity.this, new PermissionUtil.IRequestPermissionCallBack() {
            @Override
            public void permissionSuccess() {
                LogUtils.LogE("deviceId=" + deviceId);
//                Log.e("bcz", "initData--deviceId:" + deviceId);

                device = SpfUtils.getBluetoothSetList(deviceId);

                if (device == null) {
                    LogUtils.LogE("2次读取");
                    device = DataSupport.find(BluetoothBean.class, deviceId);
                }
                LogUtils.LogE("device=" + device.getId());
                LogUtils.LogE("读取数据：" + device.getList().size());

                tv_device_number.setText(device.getDeviceName());
                ArrayList<BluetoothSettingBean> tempData = (ArrayList<BluetoothSettingBean>) device.getList();
//                Log.e("readStatus", "initData()  tempData : " + tempData.size());
                boolean hasOne = false;
                for (BluetoothSettingBean tempDatum : tempData) {
                    LogUtils.LogE("===========" + tempDatum.getDeviceName() + "============\n" + tempDatum.toString());
                    if (tempDatum.getDeviceName().equals("智能模式")) {
                        listData.add(tempDatum);
                        hasOne = true;
                    }
                }

                if (!hasOne) {
                    LogUtils.LogE("------------------1-------------------");
                    BluetoothSettingBean bluetoothSettingBean = new BluetoothSettingBean();
                    bluetoothSettingBean.setDeviceName("智能模式");
                    bluetoothSettingBean.setRemarks("智能模式");
                    bluetoothSettingBean.setDeviceId(device.getId());
                    bluetoothSettingBean.setStrength(1);
                    listData.add(bluetoothSettingBean);
//                    bluetoothSettingBean.save();
                }
                for (BluetoothSettingBean tempDatum : tempData) {
                    if (tempDatum.getTime() > 0) {
//                        tempDatum.save();
                        listData.add(tempDatum);
                    }
                }
                adapter.setData(listData);
                Log.e("readStatus", "initData()  adapter.getItemCount() : " + adapter.getItemCount());
                readStatus(1);
                //主线程中调用：
                handler.postDelayed(runnable, 5000);//延时100毫秒
                initDialog();
            }
        });
        */
    }

    public void readStatus(final int dealMessage) {
//        rl_loading.setVisibility(View.VISIBLE);
        Log.e("readStatus", "readStatus()  dealMessage : " + dealMessage);
        bluetoothUtil.readStatus(new NewBleBluetoothUtil.IReadInfoListener() {
            @Override
            public void callBack(byte[] values) {

                //0x53+【开机状态】+【剩余电量】+【清洗时长】+【普通间隔时间】+【普通喷雾强度】+【开机清洗使能】 +【工作模式】。
//                for (int i = 0; i < values.length; i++) {
//                    Log.i("readStatus", "readStatus--- i:" + i + "   values[i] : " + values[i]);
//                }

                Log.e("readStatus", "callBack(): " + currentStrength);

                if (values.length != 0) {
                    bytes = values;
                    handler.sendEmptyMessage(dealMessage);
                }

            }
        });
    }

    public void readStatusToQuee() {
        bluetoothUtil.readStatusToQuee(new NewBleBluetoothUtil.IReadInfoListener() {
            @Override
            public void callBack(byte[] values) {
                //0x53+【开机状态】+【剩余电量】+【清洗时长】+【普通间隔时间】+【普通喷雾强度】+【开机清洗使能】 +【工作模式】。
                for (byte value : values) {
                    Log.e("bcz", "readStatus---value:" + value);
                }

                if (values.length != 0) {
                    bytes = values;
                    handler.sendEmptyMessage(4);
                }

            }
        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == BluetoothMainActivity.TYPE_ADD) {
                PermissionUtil.requestStoragePermission(BluetoothMainActivity.this, new PermissionUtil.IRequestPermissionCallBack() {
                    @Override
                    public void permissionSuccess() {
                        BluetoothSettingBean settingBean_add = (BluetoothSettingBean) data.getSerializableExtra("setting");
                        settingBean_add.setDeviceName(device.getDeviceName());
                        settingBean_add.setDeviceId(device.getId());
//                        deivce.getList().add(settingBean_add);
//                        SpfUtils.saveBluetoothSetList(deivce);
//                        settingBean_add.save();
                        listData.add(settingBean_add);
                        adapter.notifyDataSetChanged();
                    }
                });
            } else if (requestCode == BluetoothMainActivity.TYPE_MODIFY) {
                PermissionUtil.requestStoragePermission(BluetoothMainActivity.this, new PermissionUtil.IRequestPermissionCallBack() {
                    @Override
                    public void permissionSuccess() {
                        BluetoothSettingBean settingBean_nodify = (BluetoothSettingBean) data.getSerializableExtra("setting");
                        if (settingBean_nodify.isSelected()) {

                            if (currentStrength != settingBean_nodify.getStrength()) {
                                bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.setstrenth, settingBean_nodify.getStrength());
                                bluetoothUtil.sendOrder();
                            }

                            if (currentIntervalTime != settingBean_nodify.getTime()) {
                                bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.settimeinterval, settingBean_nodify.getTime());
                                bluetoothUtil.sendOrder();
                            }

//                            readStatusToQuee();
//                            bluetoothUtil.sendOrder();
                        }
//                        settingBean_nodify.update(settingBean_nodify.getId());
//                        upAlldata();
                        listData.set(Index, settingBean_nodify);
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        } else if (resultCode == resultCode_exit) {
            doSometingAfterCloseUnconnectedDialog();
        }

    }

//    public void upAlldata() {
//        listData.clear();
//        listData.addAll(device.getList());
//        adapter.notifyDataSetChanged();
//
//    }

    public class MyCommRecAdapter extends CommonRecyclerViewAdapter<BluetoothSettingBean> {

        ArrayList<BluetoothSettingBean> list;
        Context context;

        /**
         * 构造函数
         *
         * @param context 上下文
         * @param data    显示的数据
         */
        public MyCommRecAdapter(Context context, List data) {
            super(context, data);
            this.context = context;
            this.list = (ArrayList<BluetoothSettingBean>) data;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        private int getCurrentSelectPosition() {
            for (int i = 0; i < list.size(); i++) {
                BluetoothSettingBean beann = list.get(i);
                if (beann.isSelected()) {
                    return i;
                }
            }
            return -2;
        }

        public void setData(List data) {
            this.list = (ArrayList<BluetoothSettingBean>) data;
            notifyDataSetChanged();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void convert(final CommonRecyclerViewHolder h, final BluetoothSettingBean entity, final int position) {
            final TextView tv_remarks = h.getView(R.id.tv_remarks);
            TextView tv_modify = h.getView(R.id.tv_modify);
            TextView tv_strength = h.getView(R.id.tv_strength);
            TextView tv_time = h.getView(R.id.tv_time);
            TextView txt_time = h.getView(R.id.txt_time);
            TextView txt_qd = h.getView(R.id.txt_qd);
            final ImageView iv_leftgraytag = h.getView(R.id.iv_leftgraytag);
            final ImageView iv_leftbluetag = h.getView(R.id.iv_leftbluetag);
            final ImageView iv_leftgraytag_intelligent = h.getView(R.id.iv_leftgraytag_intelligent);
            final ImageView iv_leftbluetag_intelligent = h.getView(R.id.iv_leftbluetag_intelligent);

            if (("智能模式").equals(entity.getRemarks())) {
                tv_modify.setVisibility(View.GONE);
                tv_time.setVisibility(View.GONE);
                iv_leftgraytag.setVisibility(View.GONE);
                iv_leftbluetag.setVisibility(View.GONE);
                iv_leftgraytag_intelligent.setVisibility(View.VISIBLE);
                txt_time.setVisibility(View.GONE);
                tv_strength.setText(strStrenthNewMode[entity.getStrength() - 1]);
            } else {
                tv_modify.setVisibility(View.VISIBLE);
                tv_time.setVisibility(View.VISIBLE);
                iv_leftgraytag_intelligent.setVisibility(View.GONE);
                iv_leftbluetag_intelligent.setVisibility(View.GONE);
                txt_time.setVisibility(View.VISIBLE);
                tv_strength.setText(strStrenth[entity.getStrength() - 1]);
            }


            //修改按钮点击弹窗
            tv_modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    RoundEditDialog dialog = new RoundEditDialog(BluetoothMainActivity.this, tv_remarks.getText().toString(), new RoundEditDialog.IDialogBtnLiscener() {
                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onSure(String str) {

                            if (!str.equals(tv_remarks.getText().toString())) {
                                BluetoothSettingBean settingBean = listData.get(position);
                                settingBean.setRemarks(str);
//                                settingBean.save();
//                                SpfUtils.saveBluetoothSetList(deivce);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                    if (isNeedSendOrder) {
                        LogUtils.LogE("1111111111111111111");
                        dialog.show();
                    } else {
                        isNeedSendOrder = true;
                    }
                    dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                    // 然后弹出输入法
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                }
            });
            tv_time.setText(entity.getTime() + "秒");
            String remark = entity.getRemarks();
            if (remark == null || remark.isEmpty()) {
                entity.setRemarks("设置");
//                entity.save();
            }
            tv_remarks.setText(entity.getRemarks());

            final Switch sw_choice = h.getView(R.id.sw_choice);
            Log.e("bcz", "entity:" + gson.toJson(entity));


            //设置左侧显示状态
            if (entity.isSelected()) {
                if (("智能模式").equals(entity.getRemarks())) {
                    iv_leftgraytag.setVisibility(View.GONE);
                    iv_leftbluetag.setVisibility(View.GONE);
                    iv_leftgraytag_intelligent.setVisibility(View.GONE);
                    iv_leftbluetag_intelligent.setVisibility(View.VISIBLE);
                    txt_qd.setVisibility(View.GONE);
                } else {
                    iv_leftgraytag_intelligent.setVisibility(View.GONE);
                    iv_leftbluetag_intelligent.setVisibility(View.GONE);
                    iv_leftgraytag.setVisibility(View.GONE);
                    iv_leftbluetag.setVisibility(View.VISIBLE);
                    txt_qd.setVisibility(View.VISIBLE);
                }
            } else {

                if (("智能模式").equals(entity.getRemarks())) {
                    iv_leftgraytag.setVisibility(View.GONE);
                    iv_leftbluetag.setVisibility(View.GONE);
                    iv_leftgraytag_intelligent.setVisibility(View.VISIBLE);
                    iv_leftbluetag_intelligent.setVisibility(View.GONE);
                    txt_qd.setVisibility(View.GONE);
                } else {
                    iv_leftgraytag_intelligent.setVisibility(View.GONE);
                    iv_leftbluetag_intelligent.setVisibility(View.GONE);
                    iv_leftgraytag.setVisibility(View.VISIBLE);
                    iv_leftbluetag.setVisibility(View.GONE);
                    txt_qd.setVisibility(View.VISIBLE);
                }

            }

            sw_choice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int currentSelectPosition = getCurrentSelectPosition();
                    if (b) {
                        if (currentSelectPosition != -2 && currentSelectPosition != position) {
                            listData.get(currentSelectPosition).setSelected(false);
//                            listData.get(currentSelectPosition).save();
                            if (currentSelectPosition == 0) {
                                bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.mode_short, 0);
                            }
                        }
                        for (int i = 0; i < listData.size(); i++) {
                            BluetoothSettingBean bluetoothSettingBean = listData.get(i);
                            if (i == position) {
                                bluetoothSettingBean.setSelected(true);
//                                bluetoothSettingBean.save();
                                bluetoothSettingBean.setDeviceId(-2);
                            } else {
                                bluetoothSettingBean.setSelected(false);
//                                bluetoothSettingBean.save();
                                bluetoothSettingBean.setDeviceId(-1);
                            }
                        }
                        if (("智能模式").equals(entity.getRemarks())) {
                            iv_leftgraytag.setVisibility(View.GONE);
                            iv_leftbluetag.setVisibility(View.GONE);
                            iv_leftgraytag_intelligent.setVisibility(View.GONE);
                            iv_leftbluetag_intelligent.setVisibility(View.VISIBLE);
                            LogUtils.LogE("2222222222222222222");
                            if (isFirst) {
                                isFirst = false;
                            } else {
                                dialog.show();
                            }
                        } else {
                            iv_leftgraytag_intelligent.setVisibility(View.GONE);
                            iv_leftbluetag_intelligent.setVisibility(View.GONE);
                            iv_leftgraytag.setVisibility(View.GONE);
                            iv_leftbluetag.setVisibility(View.VISIBLE);
                            //发送操作指令
//                            startExcute(position);
                        }
                        startExcute(position);
                        Message message = new Message();
                        message.what = 2;
                        message.arg1 = currentSelectPosition;
                        handler.sendMessage(message);
                    } else {
                        if (getCurrentSelectPosition() == position) {

                            if (("智能模式").equals(entity.getRemarks())) {
                                iv_leftgraytag.setVisibility(View.GONE);
                                iv_leftbluetag.setVisibility(View.GONE);
                                iv_leftgraytag_intelligent.setVisibility(View.VISIBLE);
                                iv_leftbluetag_intelligent.setVisibility(View.GONE);
                                //设置普通模式
                                bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.mode_short, 0);
//                                readStatusToQuee();
                                bluetoothUtil.sendOrder();
                            } else {
                                iv_leftgraytag_intelligent.setVisibility(View.GONE);
                                iv_leftbluetag_intelligent.setVisibility(View.GONE);
                                iv_leftgraytag.setVisibility(View.VISIBLE);
                                iv_leftbluetag.setVisibility(View.GONE);
                            }
                            entity.setSelected(false);
//                            entity.save();
                        }
                    }
                    boolean isClose = true;
                    for (BluetoothSettingBean listDatum : listData) {
                        if (listDatum.isSelected()) {
                            isClose = false;
                        }
                    }
                    if (isClose) {
                        bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.settimeinterval, 5);
                        bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.setstrenth, 64);
                        bluetoothUtil.sendOrder();
//                        bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.boot, 0);
                    }
//                    for (BluetoothSettingBean listDatum : listData) {
//                        listDatum.save();
//
//                    }

                }

            });
            sw_choice.setChecked(entity.isSelected());

            h.itemView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(context, 78)));

        }

        @Override
        public int getLayoutViewId(int viewType) {
            return R.layout.item_bluetooth_setting;
        }

    }

    @Override
    protected void onDestroy() {
       /* if (device != null) {
            LogUtils.LogE("listData：" + listData.size());
            for (BluetoothSettingBean listDatum : listData) {
                listDatum.setSelected(false);
            }
            device.getList().clear();
            device.getList().addAll(listData);
            LogUtils.LogE("保存数据：" + device.getList().size());
            SpfUtils.saveBluetoothSetList(device);
            LogUtils.LogE("device：" + device.getId());
        }*/
        //移除30秒读状态的定时器
        handler.removeCallbacks(runnable);
        super.onDestroy();
        Log.e("bczcircle", "onDestroy");
        //断开蓝牙连接
//        bluetoothUtil.disconnectDevice();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("bczcircle", "onStop--" + isGotoBluetoothControlDetailActivity);
        if (!isGotoBluetoothControlDetailActivity) {
            //退出蓝牙模块则断开蓝牙连接，不管是切后台还是干嘛
            Log.e("bcz", "bluetoothMainActivity--主动断开连接");
//            bluetoothUtil.disconnectDevice();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("bczcircle", "onStart");
        isGotoBluetoothControlDetailActivity = false;
    }

    @Override
    protected void doSometingAfterCloseUnconnectedDialog() {
        Intent intent = new Intent(BluetoothMainActivity.this, BluetoothSelectDeviceActivity.class);
        startActivity(intent);
        finish();
    }



    /**
     * 启动眼镜护眼功能
     */
    private void startExcute(int position) {
        if (!bluetoothUtil.currentBlueConnected) {
            //蓝牙设备已断开连接
            ToastUtils.show("蓝牙已断开，请重新连接");
            finish();
        } else {
            //启动普通模式的强度指令
            BluetoothSettingBean settingBean = listData.get(position);
            if (settingBean.getTime() != currentIntervalTime) {
                bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.settimeinterval, settingBean.getTime());
                currentIntervalTime = settingBean.getTime();
                bluetoothUtil.sendOrder();
            }

            if (settingBean.getStrength() != currentStrength) {
                bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.setstrenth, settingBean.getStrength());
                currentStrength = settingBean.getStrength();
                bluetoothUtil.sendOrder();
            }

//            readStatusToQuee();
            //开始发送命令
//            bluetoothUtil.sendOrder();
        }

    }
}
