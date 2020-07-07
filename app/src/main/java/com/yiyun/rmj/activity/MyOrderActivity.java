package com.yiyun.rmj.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.yiyun.rmj.R;
import com.yiyun.rmj.adapter.FraPagerAdapter;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.fragment.OrderFragment;
import com.yiyun.rmj.utils.StatusBarUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;


//查看全部--我的订单页面
public class MyOrderActivity extends BaseActivity implements View.OnClickListener {

    //0全部 ，1未支付，2已支付（待发货），3已发货（待收货），4待评价，5退货中，6已退货，7拒绝退货 ,8交易成功
    public static final int TYPE_START_SHOW_ALL = 0;  //显示全部
    public static final int TYPE_START_SHOW_UNPAY = 1;  //待付款
    public static final int TYPE_START_SHOW_UNSHIP = 2; //待发货
    public static final int TYPE_START_SHOW_UNRECEIPT = 3; //待收货
    public static final int TYPE_START_SHOW_UNEVALUATION = 4; //待评价
    public static final int TYPE_START_SHOW_RETURN = 5; //退货中
    public static final int TYPE_START_SHOW_RETURNED = 6; //已退货
    public static final int TYPE_START_SHOW_REFURE_RETURN = 7; //拒绝退货
    public static final int TYPE_START_SHOW_SUCCESS = 8; //交易成功

    public int startShowType;
    private int page = 1;//页码

    ArrayList<String> myOrderList;

    RelativeLayout rl_no_order;
    LinearLayout ll_have_order;

    String[] tabNames = new String[]{"全部", "待付款","待发货","待收货","待评价"};
    private MagicIndicator indicator;

    ViewPager viewpager;
    ArrayList<Fragment> fragments;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_order;
    }

    public void initTitleView(){
        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.my_order));
    }

    @Override
    protected void initView() {
        myOrderList = new ArrayList<>();
        myOrderList.add("1");
        fragments = new ArrayList<>();

        for(int i=0; i<5; i++){
            Bundle bundle = new Bundle();
            bundle.putInt("type",i);
            Fragment fragment = new OrderFragment();
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }

        startShowType = getIntent().getIntExtra("type", TYPE_START_SHOW_ALL);
        initTitleView();
        rl_no_order = findViewById(R.id.rl_no_order);
        ll_have_order = findViewById(R.id.ll_have_order);

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        indicator = findViewById(R.id.magic_indicator );
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter(){
            @Override
            public int getCount() {
                return tabNames == null ? 0 : tabNames.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);

                colorTransitionPagerTitleView.setNormalColor(getResources().getColor(R.color.textcolor_black));
                colorTransitionPagerTitleView.setSelectedColor(getResources().getColor(R.color.lightblue));
                colorTransitionPagerTitleView.setText(tabNames[index]);

                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewpager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setColors(getResources().getColor(R.color.lightblue));
                return indicator;
            }
        });
        indicator.setNavigator(commonNavigator);


        viewpager.setAdapter(new FraPagerAdapter(getSupportFragmentManager(),fragments));
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                indicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                indicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                indicator.onPageScrollStateChanged(state);
            }
        });

        viewpager.setCurrentItem(startShowType);
    }

    @Override
    protected void initData() {

    }

    public void callToFinish(){
        Log.e("bcz","start to callToFinish---- resultcode:" + 505);
        setResult(505);
        finish();
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this,true);
    }
}
