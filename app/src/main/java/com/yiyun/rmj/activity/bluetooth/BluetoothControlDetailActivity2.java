package com.yiyun.rmj.activity.bluetooth;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.BluetoothSettingBean;
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
    private View v2;
    private MyNewArcProgressBar pb_short;
    private WheelView wv_short;
    private MyNewArcProgressBar pb_long;
    private WheelView wv_long;
    private List<String> shortList = new ArrayList<>();
    private List<String> longList = new ArrayList<>();
    private BluetoothSettingBean settingBean = new BluetoothSettingBean();

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

        v2 = LayoutInflater.from(this).inflate(R.layout.view_bluetooth_setting_long, null);
        pb_long = v2.findViewById(R.id.pb_long);
        wv_long = v2.findViewById(R.id.wv_long);

        vp_blueTooth.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tl_title));

        for (int i = 5; i < 65; i += 5) {
            shortList.add(i + "秒");
        }
        for (int i = 1; i < 11; i++) {
            longList.add(i + "分");
        }
        wv_short.lists(shortList).select(0).showCount(5)
                .listener(index -> {

                });

        wv_long.lists(longList).select(0).showCount(5)
                .listener(index -> {

                });

        bt_save.setOnClickListener(view -> {

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

    @Override
    protected void initData() {
        if (getIntent().getIntExtra("type", BluetoothMainActivity.TYPE_ADD) == BluetoothMainActivity.TYPE_MODIFY) {
            settingBean = (BluetoothSettingBean) getIntent().getSerializableExtra("setting");
        }
        wv_short.select(shortList.indexOf(settingBean.getTime() +"秒"));
        wv_long.select(longList.indexOf(settingBean.getTime() +"分"));

    }
}
