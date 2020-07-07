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
import com.yiyun.rmj.fragment.BillDetailFragment;
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


//我的钱包页面
public class MyWalletActivity extends BaseActivity implements View.OnClickListener {

    ViewPager viewpage_list;
    ViewPagerAdapter adapter;
    ArrayList<Fragment> fragments;
    private double money;
    private String[] tabNames = new String[]{"全部","收入","支出"};
    private MagicIndicator indicator;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_withdraw:
                //提现
                Intent withdrawIntent = new Intent(MyWalletActivity.this, WithdrawActivity.class);
                withdrawIntent.putExtra("money",money);
                startActivityForResult(withdrawIntent,0);
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_wallet;
    }

    public void initTitleView(){
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.my_wallet));
    }

    @Override
    protected void initView() {
        initTitleView();
        money = getIntent().getDoubleExtra("money",0);

        fragments = new ArrayList<>();

        for(int i=0; i<3; i++){
            BillDetailFragment billDetailFragment = new BillDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("type",i);
            billDetailFragment.setArguments(bundle);
            fragments.add(billDetailFragment);
        }

        TextView tv_withdraw = (TextView) findViewById(R.id.tv_withdraw);
        tv_withdraw.setOnClickListener(this);

        TextView tv_money = findViewById(R.id.tv_money);
        tv_money.setText("" + money);

        viewpage_list = findViewById(R.id.viewpage);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments);
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
                colorTransitionPagerTitleView.setPadding(DisplayUtils.dp2px(MyWalletActivity.this,15),0,DisplayUtils.dp2px(MyWalletActivity.this,15),0);
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
                indicator.setColors(getResources().getColor(R.color.lightblue));
                indicator.setLineHeight(DisplayUtils.dp2px(MyWalletActivity.this,2));
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
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                indicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                indicator.onPageScrollStateChanged(state);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            finish();
        }
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this,true);
    }
}
