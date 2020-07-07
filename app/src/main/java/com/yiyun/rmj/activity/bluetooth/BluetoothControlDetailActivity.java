package com.yiyun.rmj.activity.bluetooth;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.BluetoothSettingBean;
import com.yiyun.rmj.fragment.NewCleanFragment;
import com.yiyun.rmj.fragment.NewControlFragment;
import com.yiyun.rmj.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

//控制清洗界面
public class BluetoothControlDetailActivity extends BaseActivity implements View.OnClickListener {

    LinearLayout ll_tab_control, ll_tab_clean;
    TextView tv_control, tv_clean;
    View v_diver_control,v_diver_clean;

    ArrayList<Fragment> fragments;
    ViewPager viewpager;
    private int type;
    private BluetoothSettingBean settingBean;
    public boolean isFinishActivity = false;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                isFinishActivity = true;
                finish();
                overridePendingTransition(R.anim.activity_leftclick_in,R.anim.activity_leftclick_out);
                break;

            case R.id.ll_tab_control:
                viewpager.setCurrentItem(0);
                break;

            case R.id.ll_tab_clean:
                viewpager.setCurrentItem(1);
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bluetooth_control_detail;
    }

    @Override
    protected void initView() {
        type = getIntent().getIntExtra("type",BluetoothMainActivity.TYPE_ADD);
        if(type == BluetoothMainActivity.TYPE_ADD){
            settingBean = new BluetoothSettingBean();
        }else if(type == BluetoothMainActivity.TYPE_MODIFY){
            settingBean = (BluetoothSettingBean) getIntent().getSerializableExtra("setting");
//            settingBean.setDeviceName(tempSettingBean.getDeviceName());
//            settingBean.setTime(tempSettingBean.getTime());
//            settingBean.setStrength(tempSettingBean.getStrength());
//            settingBean.setDeviceId(tempSettingBean.getDeviceId());
//            settingBean.setId(tempSettingBean.getId());
//            settingBean.setRemarks(tempSettingBean.getRemarks());
        }

        fragments = new ArrayList<>();
        fragments.add(new NewControlFragment());
        fragments.add(new NewCleanFragment());

        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);

        ll_tab_control = (LinearLayout) findViewById(R.id.ll_tab_control);
        ll_tab_clean = (LinearLayout) findViewById(R.id.ll_tab_clean);
        tv_control = (TextView) findViewById(R.id.tv_control);
        tv_clean = (TextView) findViewById(R.id.tv_clean);
        v_diver_control = findViewById(R.id.v_diver_control);
        v_diver_clean = findViewById(R.id.v_diver_clean);

        ll_tab_control.setOnClickListener(this);
        ll_tab_clean.setOnClickListener(this);

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                refreshTabView();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void doSometingAfterCloseUnconnectedDialog() {
        super.doSometingAfterCloseUnconnectedDialog();
        setResult(BluetoothMainActivity.resultCode_exit);
        finish();
    }

    public void refreshTabView(){
        if(viewpager.getCurrentItem() == 0){
            //控制
            tv_control.setTextSize(18);
            tv_control.setTextColor(getResources().getColor(R.color.textcolor_black));
            tv_clean.setTextSize(15);
            tv_clean.setTextColor(getResources().getColor(R.color.color99));

            v_diver_control.setVisibility(View.VISIBLE);
            v_diver_clean.setVisibility(View.INVISIBLE);

        }else if(viewpager.getCurrentItem() == 1){
            //清洗
            tv_control.setTextSize(15);
            tv_control.setTextColor(getResources().getColor(R.color.color99));
            tv_clean.setTextSize(18);
            tv_clean.setTextColor(getResources().getColor(R.color.textcolor_black));
            v_diver_control.setVisibility(View.INVISIBLE);
            v_diver_clean.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        isFinishActivity = false;
    }

    @Override
    protected void initData() {

    }

    public void setIsFinishActivityFlag(boolean isFinishActivity){
        this.isFinishActivity = isFinishActivity;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!isFinishActivity){
            //退出蓝牙模块则断开蓝牙连接，不管是切后台还是干嘛
            Log.e("bcz","bluetoothConrolDetailActivity--主动断开连接");
//            NewBleBluetoothUtil.getInstance().disconnectDevice();
        }
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        List<Fragment> fragments;

        public ViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.fragments = list;
        }

        @Override
        public Fragment getItem(int num) {
            return fragments.get(num);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    public int getType(){
        return this.type;
    }

    public BluetoothSettingBean getSettingBean(){
        return this.settingBean;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this,true);
    }
}
