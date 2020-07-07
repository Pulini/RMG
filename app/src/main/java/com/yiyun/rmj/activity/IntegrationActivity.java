package com.yiyun.rmj.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.fragment.IntegrationFragment;
import com.yiyun.rmj.utils.DisplayUtils;
import com.yiyun.rmj.utils.StatusBarUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

//我的积分界面
public class IntegrationActivity extends BaseActivity implements View.OnClickListener {

    ViewPager viewpage_list;
    IntegrationActivity.ViewPagerAdapter adapter;
    ArrayList<Fragment> fragments;
    TextView tv_integration_num;
    private String[] tabNames = new String[]{"全部","获取","使用"};
    private MagicIndicator indicator;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_title_add:
                //积分规则按钮
                Intent integrationDesIntent = new Intent(IntegrationActivity.this, IntegrationDesActivity.class);
                startActivity(integrationDesIntent);
                break;
        }
    }

    public void setTotalIntegral(String integral){
        tv_integration_num.setText(integral);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_integration;
    }

    public void initTitleView(){
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.my_integration));

        TextView tv_des = (TextView) findViewById(R.id.tv_title_add);
        tv_des.setVisibility(View.VISIBLE);
        tv_des.setText(getString(R.string.integration_des));
        tv_des.setOnClickListener(this);

        tv_integration_num = findViewById(R.id.tv_integration_num);
    }

    @Override
    protected void initView() {
        initTitleView();

        fragments = new ArrayList<>();
        for(int i=0; i<3; i++){
            IntegrationFragment integrationFragment = new IntegrationFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("type",i);
            integrationFragment.setArguments(bundle);
            fragments.add(integrationFragment);
        }

        viewpage_list = (ViewPager) findViewById(R.id.viewpage);
        adapter = new IntegrationActivity.ViewPagerAdapter(getSupportFragmentManager(),fragments);
        viewpage_list.setAdapter(adapter);

        indicator = findViewById(R.id.magic_indicator );
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter(){
            @Override
            public int getCount() {
                return tabNames == null ? 0 : tabNames.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);

                colorTransitionPagerTitleView.setNormalColor(getResources().getColor(R.color.textcolor_black));
                colorTransitionPagerTitleView.setPadding(DisplayUtils.dp2px(IntegrationActivity.this,15),0,DisplayUtils.dp2px(IntegrationActivity.this,15),0);
                colorTransitionPagerTitleView.setSelectedColor(getResources().getColor(R.color.textcolor_black));
                colorTransitionPagerTitleView.setText(tabNames[index]);

                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewpage_list.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_MATCH_EDGE);
                indicator.setColors(getResources().getColor(R.color.color_yellow));
                indicator.setLineHeight(DisplayUtils.dp2px(IntegrationActivity.this,2));
                return indicator;
            }
        });
        indicator.setNavigator(commonNavigator);

        viewpage_list.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                indicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                indicator.onPageScrollStateChanged(state);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                indicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        });
        viewpage_list.setCurrentItem(0);
    }

    @Override
    protected void initData() {

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

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this,true);
    }
}
