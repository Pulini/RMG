package com.yiyun.rmj.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.yiyun.rmj.R;
import com.yiyun.rmj.adapter.CommonRecyclerViewAdapter;
import com.yiyun.rmj.adapter.CommonRecyclerViewHolder;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.apibean.DistributionBean;
import com.yiyun.rmj.bean.apiparm.ListBaseParm;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.utils.StatusBarUtil;
import com.yiyun.rmj.view.CircleImageView;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//我的分销页面
public class DistributionActivity extends BaseActivity {

    ArrayList<DistributionBean.Distribution> distributionList;
    private int page = 1;
    private SmartRefreshLayout smart_refresh;
    private CommonRecyclerViewAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_distribution;
    }

    public void initTitleView(){
        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RelativeLayout rl_share = findViewById(R.id.rl_share);
        rl_share.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DistributionActivity.this, ShareRuleActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_rightclick_in,R.anim.activity_rightclick_out);
            }
        });

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.my_distribution));

        smart_refresh = findViewById(R.id.smart_refresh);

        smart_refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                initData();
                smart_refresh.finishLoadMore(2000);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                distributionList.clear();
                initData();
                smart_refresh.finishRefresh(2000);
            }
        });


    }

    @Override
    protected void initView() {
        initTitleView();
        distributionList = new ArrayList<>();
        RecyclerView rv_list = findViewById(R.id.rv_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(linearLayoutManager);

        adapter = new CommonRecyclerViewAdapter<DistributionBean.Distribution>(this,distributionList) {
            @Override
            public void convert(CommonRecyclerViewHolder h, DistributionBean.Distribution entity, int position) {

                TextView tv_person_name = h.getView(R.id.tv_person_name);
                TextView tv_level_1 = h.getView(R.id.tv_level_1);
                TextView tv_phone_number = h.getView(R.id.tv_phone_number);
                TextView tv_time = h.getView(R.id.tv_time);
                ImageView iv_invalid_icon = h.getView(R.id.iv_invalid_icon);
                CircleImageView iv_person_icon = h.getView(R.id.iv_person_icon);
                Glide.with(DistributionActivity.this).load(entity.getHead()).apply(new RequestOptions()).into(iv_person_icon);

                String type = entity.getType();

                if(type.equals("一级")){
                    tv_level_1.setBackgroundResource(R.mipmap.level_normal_bg);
                    tv_level_1.setTextColor(getResources().getColor(R.color.white));
                }else{
                    tv_level_1.setBackgroundResource(R.mipmap.level_high_bg);
                    tv_level_1.setTextColor(getResources().getColor(R.color.color_blue));
                }
                tv_level_1.setText(type);

                tv_time.setText(entity.getRegis_time());
                tv_person_name.setText(entity.getUsername());
                tv_phone_number.setText(entity.getPhone());

                if(entity.getIsOverdue().equals("已失效")){
                    iv_invalid_icon.setVisibility(View.VISIBLE);
                }else{
                    iv_invalid_icon.setVisibility(View.INVISIBLE);
                }
            }


            @Override
            public int getLayoutViewId(int viewType) {
                return R.layout.item_distribution;
            }

        };

        rv_list.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        super.onResume();
        ListBaseParm baseParm = new ListBaseParm();
        baseParm.setPage(page);
        String strDes = DESHelper.encrypt(gson.toJson(baseParm));
        getMyDistribution(strDes);
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtil.setImmersiveStatusBar(this,true);
    }

    /**
     * 获取分销列表
     * @param desparms
     */
    private void getMyDistribution(String desparms){


        api.getMyDistribution(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DistributionBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                        LogUtils.LogE("error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(DistributionBean obj) {
                        LogUtils.LogE(DistributionActivity.class.getName() + "--getMyDistribution:" + gson.toJson(obj));

                        if(obj.getState() == 1){
                            distributionList.addAll(obj.getInfo().getData());
                            adapter.notifyDataSetChanged();
                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }

                    }
                });

    }
}
