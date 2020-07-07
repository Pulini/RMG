package com.yiyun.rmj.activity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.yiyun.rmj.R;
import com.yiyun.rmj.adapter.FraPagerAdapter;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bluetooth.BaseBluetoothUtil;
import com.yiyun.rmj.bluetooth.IConnectBlueCallBack;
import com.yiyun.rmj.fragment.CleanFragment;
import com.yiyun.rmj.fragment.ControlFragment;
import com.yiyun.rmj.fragment.TimingFragment;

import java.util.ArrayList;

//控制页面
public class ControlActivity extends BaseActivity {

    private TextView tv_control;
    private TextView tv_timing;
    private TextView tv_clean;
    private ImageView iv_disable;
    public static TextView tv_edit;
    private Drawable img_off;
    private int color22;
    private int color99;
    private ViewPager viewpager;
    BaseBluetoothUtil bluetoothUtil;
    BluetoothDevice bindDevice;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_control;
    }

    @Override
    protected void initView() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        ArrayList<Fragment> fragments = new ArrayList<>();
        tv_control = (TextView) findViewById(R.id.tv_control);
        tv_timing = (TextView) findViewById(R.id.tv_timing);
        tv_clean = (TextView) findViewById(R.id.tv_clean);
        iv_disable = (ImageView) findViewById(R.id.iv_disable);
        tv_edit = (TextView) findViewById(R.id.tv_edit);
        Resources res = getResources();
        img_off = res.getDrawable(R.drawable.xiahuaxian);
        img_off.setBounds(0, 0, img_off.getMinimumWidth(), img_off.getMinimumHeight());
        color22 = res.getColor(R.color.color222);
        color99 = res.getColor(R.color.color99);

        tv_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_control.setTextSize(18);
                tv_clean.setTextSize(14);
                tv_timing.setTextSize(14);

                tv_control.setTextColor(color22);
                tv_clean.setTextColor(color99);
                tv_timing.setTextColor(color99);
                tv_clean.setCompoundDrawables(null, null, null, null);
                tv_control.setCompoundDrawables(null, null, null, img_off);
                tv_timing.setCompoundDrawables(null, null, null, null);
                viewpager.setCurrentItem(0);
            }
        });
        tv_timing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_edit.setVisibility(View.VISIBLE);
                iv_disable.setVisibility(View.GONE);
//                    tv_timing.setTextSize(res.getDimension(R.dimen.textsize18));
//                    tv_control.setTextSize(res.getDimension(R.dimen.textsize14));
//                    tv_clean.setTextSize(res.getDimension(R.dimen.textsize14));
//
                tv_timing.setTextSize(18);
                tv_control.setTextSize(14);
                tv_clean.setTextSize(14);
                tv_control.setTextColor(color99);
                tv_clean.setTextColor(color99);
                tv_timing.setTextColor(color22);
                tv_control.setCompoundDrawables(null, null, null, null);
                tv_timing.setCompoundDrawables(null, null, null, img_off);
                tv_clean.setCompoundDrawables(null, null, null, null);
                viewpager.setCurrentItem(1);

            }
        });
        tv_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_edit.setVisibility(View.GONE);
                iv_disable.setVisibility(View.GONE);
//                    tv_clean.setTextSize(res.getDimension(R.dimen.textsize18));
//                    tv_control.setTextSize(res.getDimension(R.dimen.textsize14));
//                    tv_timing.setTextSize(res.getDimension(R.dimen.textsize14));
//
                tv_clean.setTextSize(18);
                tv_control.setTextSize(14);
                tv_timing.setTextSize(14);
                tv_control.setTextColor(color99);
                tv_clean.setTextColor(color22);
                tv_timing.setTextColor(color99);

                tv_control.setCompoundDrawables(null, null, null, null);
                tv_clean.setCompoundDrawables(null, null, null, img_off);
                tv_timing.setCompoundDrawables(null, null, null, null);
                viewpager.setCurrentItem(2);

            }
        });


        fragments.add(new ControlFragment());
        fragments.add(new TimingFragment());
        fragments.add(new CleanFragment());
        FraPagerAdapter adapter = new FraPagerAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(adapter);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                Resources res = getResources();
                Drawable img_off = res.getDrawable(R.drawable.xiahuaxian);
                img_off.setBounds(0, 0, img_off.getMinimumWidth(), img_off.getMinimumHeight());
                int color22 = res.getColor(R.color.color222);
                int color99 = res.getColor(R.color.color99);

                Log.e("Pan", position + "");
                if (position == 0) {
                    tv_edit.setVisibility(View.GONE);
                    iv_disable.setVisibility(View.VISIBLE);
//                    tv_control.setTextSize(res.getDimension(R.dimen.textsize18));
//                    tv_clean.setTextSize(res.getDimension(R.dimen.textsize14));
//                    tv_timing.setTextSize(res.getDimension(R.dimen.textsize14));
//
                    tv_control.setTextSize(18);
                    tv_clean.setTextSize(14);
                    tv_timing.setTextSize(14);

                    tv_control.setTextColor(color22);
                    tv_clean.setTextColor(color99);
                    tv_timing.setTextColor(color99);
                    tv_clean.setCompoundDrawables(null, null, null, null);
                    tv_control.setCompoundDrawables(null, null, null, img_off);
                    tv_timing.setCompoundDrawables(null, null, null, null);

                } else if (position == 1) {
                    tv_edit.setVisibility(View.VISIBLE);
                    iv_disable.setVisibility(View.GONE);
//                    tv_timing.setTextSize(res.getDimension(R.dimen.textsize18));
//                    tv_control.setTextSize(res.getDimension(R.dimen.textsize14));
//                    tv_clean.setTextSize(res.getDimension(R.dimen.textsize14));
//
                    tv_timing.setTextSize(18);
                    tv_control.setTextSize(14);
                    tv_clean.setTextSize(14);
                    tv_control.setTextColor(color99);
                    tv_clean.setTextColor(color99);
                    tv_timing.setTextColor(color22);
                    tv_control.setCompoundDrawables(null, null, null, null);
                    tv_timing.setCompoundDrawables(null, null, null, img_off);
                    tv_clean.setCompoundDrawables(null, null, null, null);
                } else {
                    tv_edit.setVisibility(View.GONE);
                    iv_disable.setVisibility(View.GONE);
//                    tv_clean.setTextSize(res.getDimension(R.dimen.textsize18));
//                    tv_control.setTextSize(res.getDimension(R.dimen.textsize14));
//                    tv_timing.setTextSize(res.getDimension(R.dimen.textsize14));
//
                    tv_clean.setTextSize(18);
                    tv_control.setTextSize(14);
                    tv_timing.setTextSize(14);
                    tv_control.setTextColor(color99);
                    tv_clean.setTextColor(color22);
                    tv_timing.setTextColor(color99);

                    tv_control.setCompoundDrawables(null, null, null, null);
                    tv_clean.setCompoundDrawables(null, null, null, img_off);
                    tv_timing.setCompoundDrawables(null, null, null, null);

                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        iv_disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName(ControlActivity.this, "com.yiyun.rmj.activity.UnConactActivity");
                startActivity(intent);
//                ControlActivity.this.overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
                finish();
            }
        });

    }

    @Override
    protected void initData() {
        bluetoothUtil = getBluetoolthUtil();
        Intent intent = getIntent();
        bindDevice = intent.getParcelableExtra("bindDevicce");
        if(bindDevice != null)
        {
            Log.e("bcz","Uuid" + bindDevice.getUuids()[0].getUuid().toString());
            Log.e("bcz","getName" + bindDevice.getName());
            Log.e("bcz","getBondState" + bindDevice.getBondState());
            Log.e("bcz","getAddress" + bindDevice.getAddress());
            Log.e("bcz","getType" + bindDevice.getType());
            bluetoothUtil.connect(bindDevice,new IConnectBlueCallBack(){
                @Override
                public void onStartConnect() {

                }

                @Override
                public void onConnectSuccess(BluetoothDevice bluetoothDevice, BluetoothSocket bluetoothSocket) {
                    Log.e("bcz","onConnectSuccess:" + bluetoothSocket.isConnected());
                    getBluetoolthUtil().setBluetoothSocket(bluetoothSocket);
                }

                @Override
                public void onConnectFail(BluetoothDevice bluetoothDevice, String resultMassage) {
                    Log.e("bcz","onConnectFail:" + resultMassage);

                }
            });
        }


    }
}
