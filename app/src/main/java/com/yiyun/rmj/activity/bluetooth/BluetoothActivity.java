package com.yiyun.rmj.activity.bluetooth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;


import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yiyun.rmj.R;
import com.yiyun.rmj.adapter.CommonRecyclerViewAdapter;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.BluetoothBean;
import com.yiyun.rmj.bean.BluetoothSettingBean;
import com.yiyun.rmj.bluetooth.NewBleBluetoothUtil;
import com.yiyun.rmj.dialog.RoundEditDialog;
import com.yiyun.rmj.utils.DisplayUtils;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.utils.PermissionUtil;
import com.yiyun.rmj.view.ElectricView;

import java.util.ArrayList;
import java.util.List;

import cn.jake.share.frdialog.dialog.FRDialog;

/**
 * File Name : BluetoothActivity
 * Created by : PanZX on 2020/05/27
 * Email : 644173944@qq.com
 * Github : https://github.com/Pulini
 * Remark：
 */
public class BluetoothActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout rl_elertic;
    private ElectricView tv_eric;
    private TextView tv_power;
    private TextView tv_boot_state;
    private Switch sw_boot_or_shutdown;
    private TextView txt_sbbh;
    private SwipeRecyclerView rv_list;
    private TextView tv_device_number;
    private RelativeLayout rll_update_bg;
    private LinearLayout ll_add_dialog;
    private TextView tv_to_setting;
    private TextView tv_update;
    private ImageView iv_update_flag;

    private Activity mActivity;

    private int deviceId;//设备ID
//    private int currentBootState;// 开机状态
//    private int currentElect; //电量
//    private int currentIntervalTime; //间隔时间
//    private int currentStrength; //喷雾强度
//    private int currentMode; //工作模式

    private int currentPowerStatus;//开机状态
    private int currentElectricQuantity;//剩余电量
    private int currentCleaningTime;//清洗时长
    private int currentIntervalTime;//普通间隔时间
    private int currentSprayIntensity;//普通喷雾强度
    private int currentPowerCleaning;//开机清洗使能
    private int currentWorkMode;//工作模式

    public static final int TYPE_ADD = 0;
    public static final int TYPE_MODIFY = 1;

    private NewBleBluetoothUtil bluetoothUtil;
    private BluetoothAdapter adapter;
    private List<BluetoothSettingBean> listData = new ArrayList<>();
    private BluetoothBean device;
    private boolean isGotoBluetoothControlDetailActivity = false;
    private FRDialog dialog;
    private TextView tv_small;
    private TextView tv_mid;
    private TextView tv_big;
    private int selectStrength = 0;  //0：小 1：中 2：大
    
    public void LOGE(String msg) {
        Log.e("Pan", msg);
    }

    Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (currentPowerStatus == NewBleBluetoothUtil.state_boot) {
                        tv_boot_state.setText("关机");
                        sw_boot_or_shutdown.setChecked(true);
                    } else {
                        tv_boot_state.setText("开机");
                        sw_boot_or_shutdown.setChecked(false);
                    }

                    int count = currentElectricQuantity / 10;
                    tv_eric.setCount(count);
                    tv_power.setText(currentElectricQuantity + "%");

                    LogUtils.LogE("1DeviceName=" + device.getDeviceName());

//                    BluetoothSettingBean bluetoothSettingBean = new BluetoothSettingBean();
//                    bluetoothSettingBean.setDeviceName(device.getDeviceName());
//                    bluetoothSettingBean.setDeviceId(device.getId());
//                    bluetoothSettingBean.setStrength(currentSprayIntensity);
//                    bluetoothSettingBean.setTime(currentIntervalTime);
//                    bluetoothSettingBean.setSelected(true);
//                    listData.add(bluetoothSettingBean);
//                    bluetoothSettingBean.save();
//                    if (adapter != null) {
//                        adapter.setData(listData);
//                    }
                    break;
                case 2:
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bluetooth_main;
    }


    @Override
    protected void initView() {
        mActivity = this;
        deviceId = getIntent().getIntExtra("deviceId", 0);
        LOGE("deviceId=" + deviceId);
        initTitleView();
        findView();
        bluetoothUtil = NewBleBluetoothUtil.getInstance();
        tv_boot_state.setText("开机");
        sw_boot_or_shutdown.setChecked(false);
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BluetoothAdapter(this, listData);
        setListener();
        rv_list.setAdapter(adapter);
    }

    @Override
    protected void initData() {
       /*
        PermissionUtil.requestStoragePermission(mActivity, new PermissionUtil.IRequestPermissionCallBack() {
            @Override
            public void permissionSuccess() {
                device = DataSupport.find(BluetoothBean.class, deviceId);
                LOGE("DeviceName=" + device.getDeviceName());

                tv_device_number.setText(device.getDeviceName());

                ArrayList<BluetoothSettingBean> tempData = (ArrayList<BluetoothSettingBean>) device.getList();

                LOGE("tempData=" + tempData.size());
//阅读模式：【长喷5秒—-短喷4次（强，间隔30秒）—长喷5秒—短喷6次（强，间隔30秒）】（3次循环总计15分钟）—【长喷5秒-短喷5分钟—长喷3秒—短喷5分钟（强，间隔30秒）】（循环45分钟）一个小时为周期/
// 
//电竞模式：长喷7秒—-短喷4次（强，间隔30秒）—长喷7秒—短喷6次（MaX，间隔30秒）（3次循环总计15分钟）—长喷7秒-短喷5分钟—长喷7秒—短喷5分钟（MAX间隔30秒）（循环45分钟）一个小时为周期
// 
//美容模式：长喷7秒—-短喷4次（强，间隔30秒）—长喷7秒—短喷6次（MAX，间隔30秒）（5次循环总计25分钟）—长喷7秒-短喷5分钟—长喷7秒—短喷5分钟（MAX，间隔30秒）（循环35分钟）一个小时为周期
                boolean has = false;
                for (BluetoothSettingBean tempDatum : tempData) {
                    LogUtils.LogE("-----3-----蓝牙设置：\n" + tempDatum.toString());
                    if (tempDatum.getDeviceName().equals("智能模式")) {
                        has = true;
                    }
                }
                if (!has) {
                    BluetoothSettingBean bluetoothSettingBean = new BluetoothSettingBean();
                    bluetoothSettingBean.setDeviceName("智能模式");
                    bluetoothSettingBean.setRemarks("智能模式");
                    bluetoothSettingBean.setDeviceId(device.getId());
                    bluetoothSettingBean.setStrength(1);
                    listData.add(bluetoothSettingBean);
                    bluetoothSettingBean.save();
                }
                listData.addAll(tempData);
                adapter.setData(listData);
                LOGE("设置列表=" + adapter.getItemCount());
                readStatus();
//                readStatus(1);
//                //主线程中调用：
//                handler.postDelayed(runnable, 5000);//延时100毫秒
//                initDialog();
            }
        });

        */
    }

    public void readStatus() {
        bluetoothUtil.readStatus(new NewBleBluetoothUtil.IReadInfoListener() {
            @Override
            public void callBack(byte[] values) {
                //0x53+【开机状态】+【剩余电量】+【清洗时长】+【普通间隔时间】+【普通喷雾强度】+【开机清洗使能】 +【工作模式】。
                if (values.length != 0) {
                    LogUtils.LogE("开机状态=" + values[1]);
                    LogUtils.LogE("剩余电量=" + values[2]);
                    LogUtils.LogE("清洗时长=" + values[3]);
                    LogUtils.LogE("普通间隔时间=" + values[4]);
                    LogUtils.LogE("普通喷雾强度=" + values[5]);
                    LogUtils.LogE("开机清洗使能=" + values[6]);
                    LogUtils.LogE("工作模式=" + values[7]);
                    currentPowerStatus = values[1];
                    currentElectricQuantity = values[2];
                    currentCleaningTime = values[3];
                    currentIntervalTime = values[4];
                    currentSprayIntensity = values[5];
                    currentPowerCleaning = values[6];
                    currentWorkMode = values[7];
                    handler.sendEmptyMessage(1);
                }
            }
        });
    }

    public void initTitleView() {
        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText("");
        ImageView iv_add = findViewById(R.id.iv_title_blue_add);
        iv_add.setVisibility(View.VISIBLE);
        iv_add.setOnClickListener(this);
    }

    public void findView() {
        rl_elertic = findViewById(R.id.rl_elertic);
        tv_eric = findViewById(R.id.tv_eric);
        tv_power = findViewById(R.id.tv_power);
        tv_boot_state = findViewById(R.id.tv_boot_state);
        sw_boot_or_shutdown = findViewById(R.id.sw_boot_or_shutdown);
        txt_sbbh = findViewById(R.id.txt_sbbh);
        rv_list = findViewById(R.id.rv_list);
        tv_device_number = findViewById(R.id.tv_device_number);
        rll_update_bg = findViewById(R.id.rll_update_bg);
        ll_add_dialog = findViewById(R.id.ll_add_dialog);
        tv_to_setting = findViewById(R.id.tv_to_setting);
        tv_update = findViewById(R.id.tv_update);
        iv_update_flag = findViewById(R.id.iv_update_flag);
    }

    public void setListener() {
        sw_boot_or_shutdown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //处于开机状态
                    tv_boot_state.setText("关机");
                    if (currentPowerStatus == NewBleBluetoothUtil.state_shutdown) {
                        bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.boot, 0);
                        bluetoothUtil.sendOrder();
                    }
                } else {
                    //处于关机状态
                    tv_boot_state.setText("开机");
                    if (currentPowerStatus == NewBleBluetoothUtil.state_boot) {
                        bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.shutdown, 0);
                        bluetoothUtil.sendOrder();
                    }
                }
            }
        });
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
                    PermissionUtil.requestStoragePermission(mActivity, new PermissionUtil.IRequestPermissionCallBack() {
                        @Override
                        public void permissionSuccess() {
                            listData.get(adapterPosition).delete();
                            listData.remove(adapterPosition);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });


        adapter.setOnRecyclerViewItemClickListener(new CommonRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (position == 0) return;
                isGotoBluetoothControlDetailActivity = true;
                Intent bluecontrolDetailIntent = new Intent(mActivity, BluetoothControlDetailActivity.class);
                bluecontrolDetailIntent.putExtra("type", TYPE_MODIFY);
                BluetoothSettingBean setting = listData.get(position);
                bluecontrolDetailIntent.putExtra("setting", setting);
                startActivityForResult(bluecontrolDetailIntent, TYPE_MODIFY);
                overridePendingTransition(R.anim.activity_rightclick_in, R.anim.activity_rightclick_out);
            }
        });
        adapter.setListener(new BluetoothAdapter.BluetoothAdapterClickListener() {

            @Override
            public void itemModify(final BluetoothSettingBean data) {
                RoundEditDialog dialog = new RoundEditDialog(mActivity, data.getRemarks(), new RoundEditDialog.IDialogBtnLiscener() {
                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onSure(String str) {
                        if (!str.equals(data.getRemarks())) {
                            data.setRemarks(str);
                            data.save();
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                dialog.show();
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            }

            @Override
            public void itemSelect(BluetoothSettingBean data1, boolean isChecked, BluetoothSettingBean data2) {
                if(data1.getDeviceName().equals("智能模式")&&isChecked){
                    showDialog(data1, true,data2);
                }else{
                    data1.setSelected(isChecked);
                    if (data2 != null&&data1.getDeviceId()!=data2.getDeviceId()) {
                        data2.setSelected(!isChecked);
                    }
                    handler.sendEmptyMessage(2);
                }
            }
        });

        rll_update_bg.setOnClickListener(this);
        tv_to_setting.setOnClickListener(this);
        tv_update.setOnClickListener(this);
    }

    public void showDialog(final BluetoothSettingBean data1, final boolean isChecked, final BluetoothSettingBean data2) {
        dialog = new FRDialog.CommonBuilder(this).setContentView(R.layout.dialog_bluetooth_zn).create();
        tv_small = dialog.findViewById(R.id.tv_strength_small);
        tv_mid = dialog.findViewById(R.id.tv_strength_mid);
        tv_big = dialog.findViewById(R.id.tv_strength_big);

        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (selectStrength) {
                    case 1:
                        if (currentWorkMode != NewBleBluetoothUtil.mode_auto_mild) {
                            bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.mode_auto_mild, 0);
                        }
                        bluetoothUtil.sendOrder();
                        break;
                    case 2:
                        if (currentWorkMode != NewBleBluetoothUtil.mode_auto_middle) {
                            bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.mode_auto_middle, 0);
                        }
                        bluetoothUtil.sendOrder();
                        break;
                    case 3:
                        if (currentWorkMode != NewBleBluetoothUtil.mode_auto_middle) {
                            bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.mode_long, 0);
                        }
                        bluetoothUtil.sendOrder();
                        break;
                }
                BluetoothSettingBean bluetoothSettingBean = listData.get(getIntelligentModeId());
                bluetoothSettingBean.setStrength(selectStrength);
                bluetoothSettingBean.setSelected(true);
                bluetoothSettingBean.save();

                //强度小
                dialog.dismiss();
                data1.setSelected(isChecked);
                if (data2 != null) {
                    data2.setSelected(!isChecked);
                }
                handler.sendEmptyMessage(2);
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
        dialog.show();
    }
    public void upBtnState(int select) {
        selectStrength = select;
        tv_small.setBackgroundResource(R.drawable.dialog_zn_btn_unselect_bg);
        tv_small.setTextColor(mActivity.getResources().getColor(R.color.color66));
        tv_mid.setBackgroundResource(R.drawable.dialog_zn_btn_unselect_bg);
        tv_mid.setTextColor(mActivity.getResources().getColor(R.color.color66));
        tv_big.setBackgroundResource(R.drawable.dialog_zn_btn_unselect_bg);
        tv_big.setTextColor(mActivity.getResources().getColor(R.color.color66));
        if (select == 1) {
            //选中第一个属性（小）
            tv_small.setBackgroundResource(R.drawable.dialog_zn_btn_select_bg);
            tv_small.setTextColor(mActivity.getResources().getColor(R.color.color_blue));

        } else if (select == 2) {
            //选中第二个属性（中）
            tv_mid.setBackgroundResource(R.drawable.dialog_zn_btn_select_bg);
            tv_mid.setTextColor(mActivity.getResources().getColor(R.color.color_blue));

        } else if (select == 3) {
            //选中第二个属性（大）
            tv_big.setBackgroundResource(R.drawable.dialog_zn_btn_select_bg);
            tv_big.setTextColor(mActivity.getResources().getColor(R.color.color_blue));
        }
    }
    private int getIntelligentModeId() {
        for (int i = 0; i < listData.size(); i++) {
            if (listData.get(i).getRemarks().equals("智能模式")) {
                return i;
            }
        }
        return 0;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:

                break;
            case R.id.iv_title_blue_add:

                break;
            case R.id.rll_update_bg:

                break;
            case R.id.tv_to_setting:

                break;

            case R.id.tv_update:

                break;
        }
    }
}
