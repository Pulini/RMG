package com.yiyun.rmj.fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.hjq.toast.ToastUtils;
import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseFragment;
import com.yiyun.rmj.bluetooth.NewBleBluetoothUtil;
import com.yiyun.rmj.view.WheelView;

import java.util.ArrayList;

public class NewCleanFragment extends BaseFragment {

    private ArrayList<String> list;
    private WheelView wheelView;
    private Switch switch_auto_wash;
    int lastindex = 0;
    private NewBleBluetoothUtil bluetoothUtil;
    private static String timeUnit = "\t秒";
    private static int currentCleanTime;
    private static int currentBootState;
    private static int currentAutowashTag = 0;
    private boolean needSendOrder = true;
    private byte[] mValues;
    private Button mBtnLeft;
    private Button mBtnRight;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 3:
//                    if(checkStatus()){
//                        ToastUtils.show("发送成功");
//                    }
                    initParm();
                    break;
                case 1:
                    if (mValues != null) {

                        initParm();
                        wheelView.setSelectItem(lastindex);
                        wheelView.refresh();

                        currentAutowashTag = mValues[6];
                        if (currentAutowashTag == 0) {
                            switch_auto_wash.setChecked(false);
                        } else {
                            needSendOrder = false;
                            switch_auto_wash.setChecked(true);
                        }
                    }
                    break;
            }
        }
    };

    private void initParm() {
        currentCleanTime = mValues[3];
        currentBootState = mValues[1];
        int index = list.indexOf(currentCleanTime + timeUnit);
        lastindex = index;
    }

    private boolean checkStatus() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_clean_new;
    }

    private void readStatus() {
        //读取设备状态值
        NewBleBluetoothUtil.getInstance().readStatus(new NewBleBluetoothUtil.IReadInfoListener() {
            @Override
            public void callBack(byte[] values) {
                //0x53+【开机状态】+【剩余电量】+【清洗时长】+【普通间隔时间】+【普通喷雾强度】+【开机清洗使能】 +【工作模式】。
                for (byte value : values) {
                    Log.e("bcz", "readStatus---value:" + value);
                }
                mValues = values;

                if (values.length != 0) {
                    mHandler.sendEmptyMessage(1);
                }

            }
        });
    }

    @Override
    protected void initView(View view) {

        bluetoothUtil = NewBleBluetoothUtil.getInstance();
        wheelView = view.findViewById(R.id.wheel);
        list = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            list.add(i + timeUnit);
        }
        wheelView.lists(list).select(lastindex).showCount(5)
                .listener(new WheelView.OnWheelViewItemSelectListener() {
                    @Override
                    public void onItemSelect(int index) {
                        lastindex = index;
                        if (currentCleanTime != getLastTime(lastindex)) {
                            //清洗时间参数与设备上的一致则不发送
                            bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.setcleartime, getLastTime(lastindex));
                            currentCleanTime = getLastTime(lastindex);
//                            readStatusToQuee();
                            NewBleBluetoothUtil.getInstance().sendOrder();
                        }
                    }
                });
        mBtnLeft = view.findViewById(R.id.btn_clean_left);
        mBtnRight = view.findViewById(R.id.btn_clean_right);

        mBtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentBootState == NewBleBluetoothUtil.state_boot) {
                    //发送清洗左喷头的命令
                    bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.clearleft, 0);
                    bluetoothUtil.sendOrder();
                    if (mBtnLeft != null) {
                        mBtnRight.setSelected(false);
                        mBtnLeft.setSelected(true);
                    }
                    if (mHandler != null) {
                        mHandler.removeCallbacks(mTimeoutRunnable);
                        mHandler.postDelayed(mTimeoutRunnable, currentCleanTime * 1000);
                    }
                } else {
                    ToastUtils.show("当前处于关机状态无法发送该指令");
                    if (mBtnRight != null) {
                        mBtnLeft.setSelected(false);
                        mBtnRight.setSelected(false);
                    }
                }
            }
        });

        mBtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentBootState == NewBleBluetoothUtil.state_boot) {
                    //发送清洗右喷头的命令
                    bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.clearright, 0);
                    bluetoothUtil.sendOrder();
                    if (mBtnRight != null) {
                        mBtnLeft.setSelected(false);
                        mBtnRight.setSelected(true);
                    }
                    if (mHandler != null) {
                        mHandler.removeCallbacks(mTimeoutRunnable);
                        mHandler.postDelayed(mTimeoutRunnable, currentCleanTime * 1000);
                    }
                } else {
                    ToastUtils.show("当前处于关机状态无法发送该指令");
                    if (mBtnRight != null) {
                        mBtnLeft.setSelected(false);
                        mBtnRight.setSelected(false);
                    }
                }
            }
        });

        switch_auto_wash = view.findViewById(R.id.swith_auto_wash);
        switch_auto_wash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (needSendOrder) {
                        Log.e("bcz", "start to send setpoweronclear");
                        NewBleBluetoothUtil.getInstance().addOrderToQuee(NewBleBluetoothUtil.setpoweronclear, 0);
//                        readStatusToQuee();
                        NewBleBluetoothUtil.getInstance().sendOrder();
                    }

                    needSendOrder = true;

                } else {
                    if (needSendOrder) {
                        Log.e("bcz", "start to send forbidsetpoweronclear");
                        NewBleBluetoothUtil.getInstance().addOrderToQuee(NewBleBluetoothUtil.forbidsetpoweronclear, 0);
//                        readStatusToQuee();
                        NewBleBluetoothUtil.getInstance().sendOrder();
                    }

                    needSendOrder = true;

                }
            }
        });

        readStatus();
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
                    mValues = values;
                    mHandler.sendEmptyMessage(3);
                }

            }
        });
    }

    private int getLastTime(int index) {
        String str = list.get(index);
        String str2 = str.substring(0, str.length() - 1).trim();
        return Integer.parseInt(str2);
    }

    @Override
    protected void initData() {

    }

    private Runnable mTimeoutRunnable = new Runnable() {
        @Override
        public void run() {
            if (mBtnRight != null) {
                mBtnLeft.setSelected(false);
                mBtnRight.setSelected(false);
            }
        }
    };
}
