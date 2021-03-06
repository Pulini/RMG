package com.yiyun.rmj.activity.bluetooth;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bluetooth.NewBleBluetoothUtil;
import com.yiyun.rmj.view.MyNewArcProgressBar;
import com.yiyun.rmj.view.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * File Name : BluetoothControlDetailActivity2
 * Created by : PanZX on 2020/07/13
 * Email : 644173944@qq.com
 * Github : https://github.com/Pulini
 * Remark：
 */
public class BluetoothControlDetailActivity2 extends BaseActivity {

    private TabLayout tl_title;
    private ViewPager vp_blueTooth;
    private Button bt_save;

    private View v1;
    private MyNewArcProgressBar pb_short;
    private WheelView wv_short;
    private TextView tv_shortSwitch;
    private Switch sw_shortSwitch;
    private List<Integer> shortList = new ArrayList<>();


    private View v2;
    private MyNewArcProgressBar pb_long;
    private WheelView wv_long;
    private TextView tv_longSwitch;
    private Switch sw_longSwitch;
    private List<Integer> longList = new ArrayList<>();


    private SettingListModel bsm = new SettingListModel();
    private int type = 0;
    private int shortTime = 5;
    private int longTime = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bluetooth_control_detail2;
    }

    @Override
    protected void initView() {
        findViewById(R.id.iv_back).setOnClickListener(view -> finish());
        tl_title = findViewById(R.id.tl_title);
        vp_blueTooth = findViewById(R.id.vp_blueTooth);
        bt_save = findViewById(R.id.bt_save);

        v1 = LayoutInflater.from(this).inflate(R.layout.view_bluetooth_setting_short, null);
        pb_short = v1.findViewById(R.id.pb_short);
        wv_short = v1.findViewById(R.id.wv_short);
        tv_shortSwitch = v1.findViewById(R.id.tv_shortSwitch);
        sw_shortSwitch = v1.findViewById(R.id.sw_shortSwitch);

        v2 = LayoutInflater.from(this).inflate(R.layout.view_bluetooth_setting_long, null);
        pb_long = v2.findViewById(R.id.pb_long);
        wv_long = v2.findViewById(R.id.wv_long);
        tv_longSwitch = v2.findViewById(R.id.tv_longSwitch);
        sw_longSwitch = v2.findViewById(R.id.sw_longSwitch);

        vp_blueTooth.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tl_title));

        List<String> shortListName = new ArrayList<>();
        for (int i = 5; i < 65; i += 5) {
            shortList.add(i);
            shortListName.add(i + "秒");
        }

        List<String> longListName = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            longList.add(i);
            longListName.add(i + "分");
        }

        wv_short.lists(shortListName).select(0).showCount(5).listener(index -> {
            shortTime = shortList.get(index);
        });

        wv_long.lists(longListName).select(0).showCount(5).listener(index -> {
            longTime = longList.get(index);
        });

        sw_shortSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            tv_shortSwitch.setText(b ? "短喷关闭" : "短喷开启");
            bt_save.setBackgroundResource(b || sw_longSwitch.isChecked() ? R.drawable.shape_conner_blue_bg : R.drawable.shape_conner_gray_bg);
        });

        sw_longSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            tv_longSwitch.setText(b ? "长喷关闭" : "长喷开启");
            bt_save.setBackgroundResource(b || sw_shortSwitch.isChecked() ? R.drawable.shape_conner_blue_bg : R.drawable.shape_conner_gray_bg);
        });

        bt_save.setOnClickListener(view -> {
            if (sw_shortSwitch.isChecked() || sw_longSwitch.isChecked()) {
                setModel();
            } else {
                ToastUtils.show("请先开启喷射模式开关");
            }
        });

        vp_blueTooth.addView(v1);
        vp_blueTooth.addView(v2);
        vp_blueTooth.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return vp_blueTooth.getChildCount();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                return vp_blueTooth.getChildAt(position);
            }
        });

        tl_title.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                vp_blueTooth.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void setModel() {
        Log.e("Pan", "CurrentStep=" + pb_short.getCurrentStep());
        Log.e("Pan", "SelectItem=" + wv_short.getSelectItem());
        if (sw_shortSwitch.isChecked() && sw_longSwitch.isChecked()) {
            bsm.setModel(NewBleBluetoothUtil.mode_short_long);
        } else {
            if (sw_shortSwitch.isChecked()) {
                bsm.setModel(NewBleBluetoothUtil.mode_short);
                if(type == BluetoothMainActivity.TYPE_ADD){
                    bsm.setModelName("短喷设置");
                }
            }
            if (sw_longSwitch.isChecked()) {
                bsm.setModel(NewBleBluetoothUtil.mode_long);
                if(type == BluetoothMainActivity.TYPE_ADD){
                    bsm.setModelName("长喷设置");
                }
            }
        }
        bsm.setShortTime(shortTime);
        bsm.setShortStrength(pb_short.getCurrentStep() + 1);
        bsm.setLongTime(longTime);
        bsm.setLongStrength((pb_long.getCurrentStep() + 1) * 2);
        setResult(RESULT_OK, new Intent().putExtra("Model", bsm));
        finish();
    }

    @Override
    protected void initData() {
        type = getIntent().getIntExtra("type", BluetoothMainActivity2.TYPE_ADD);
        if (type != BluetoothMainActivity.TYPE_ADD) {
            bsm = (SettingListModel) getIntent().getSerializableExtra("setting");
            Log.e("Pan", new Gson().toJson(bsm));
            if (bsm.getModel() == NewBleBluetoothUtil.mode_short_long) {
                sw_shortSwitch.setChecked(true);
                sw_longSwitch.setChecked(true);
            } else {
                sw_shortSwitch.setChecked(bsm.getModel() == NewBleBluetoothUtil.mode_short);
                sw_longSwitch.setChecked(bsm.getModel() == NewBleBluetoothUtil.mode_long);
            }
            wv_short.select(shortList.indexOf(bsm.getShortTime()));
            wv_long.select(longList.indexOf(bsm.getLongTime()));
            pb_short.setCurrentStep(bsm.getShortStrength()-1);
            pb_long.setCurrentStep(bsm.getLongStrength() / 2 - 1);
            Log.e("Pan", "shortList=" + shortList.indexOf(bsm.getShortTime()));
            Log.e("Pan", "longList=" + longList.indexOf(bsm.getLongTime()));
            Log.e("Pan", "pb_short=" + bsm.getShortStrength());
            Log.e("Pan", "pb_long=" + bsm.getLongStrength());
        }
        bt_save.setBackgroundResource(sw_longSwitch.isChecked() || sw_shortSwitch.isChecked() ? R.drawable.shape_conner_blue_bg : R.drawable.shape_conner_gray_bg);
    }
}
