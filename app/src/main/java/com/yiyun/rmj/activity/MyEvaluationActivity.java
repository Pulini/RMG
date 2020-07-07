package com.yiyun.rmj.activity;

import android.view.View;
import android.widget.ImageView;
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
import com.yiyun.rmj.bean.apibean.EvaluationBean;
import com.yiyun.rmj.bean.apiparm.ListBaseParm;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.utils.StatusBarUtil;
import com.yiyun.rmj.view.CircleImageView;
import com.yiyun.rmj.view.RatingBar;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//我的评价页面
public class MyEvaluationActivity extends BaseActivity implements View.OnClickListener {

    RecyclerView rv_evaluationlist;
    ArrayList<EvaluationBean.Evaluation> listData;
    private int page = 1;
    private CommonRecyclerViewAdapter adapter;
    RequestOptions myOptions;
    SmartRefreshLayout smart_refresh;

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
        return R.layout.activity_my_evaluation;
    }

    public void initTitleView(){
        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.my_evaluation));
    }

    @Override
    protected void initView() {
        initTitleView();
        listData = new ArrayList<>();
        myOptions = new RequestOptions();
        rv_evaluationlist = findViewById(R.id.rv_evaluationlist);
        rv_evaluationlist.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommonRecyclerViewAdapter<EvaluationBean.Evaluation>(this,listData) {

            @Override
            public void convert(CommonRecyclerViewHolder h, EvaluationBean.Evaluation entity, final int position) {
                final ImageView iv_like = h.getView(R.id.iv_like);
                final ImageView iv_un_like = h.getView(R.id.iv_un_like);
                iv_un_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        iv_like.setVisibility(View.VISIBLE);
                        iv_un_like.setVisibility(View.GONE);
                    }
                });

                CircleImageView iv_buyer_icon = h.getView(R.id.iv_buyer_icon);
                TextView tv_buyer_name = h.getView(R.id.tv_buyer_name);
                TextView tv_date = h.getView(R.id.tv_date);
                RatingBar rb_star = h.getView(R.id.rb_star);
                TextView tv_evaluation_content = h.getView(R.id.tv_evaluation_content);
                TextView tv_ll_num = h.getView(R.id.tv_ll_num);
                TextView tv_like_num = h.getView(R.id.tv_like_num);

                Glide.with(MyEvaluationActivity.this).load(entity.getHead()).apply(myOptions).into(iv_buyer_icon);
                tv_buyer_name.setText(entity.getUsername());
                tv_date.setText(entity.getEvaluateTime());
                tv_ll_num.setText("浏览" + entity.getBrowseCount());
                tv_like_num.setText(entity.getLikes() + "");
                rb_star.setStar(entity.getEvaluateStars());
                tv_evaluation_content.setText(entity.getEvaluateContent());
                RecyclerView rv_img = h.getView(R.id.rv_img);
                LinearLayoutManager managers = new LinearLayoutManager(MyEvaluationActivity.this);
                managers.setOrientation(LinearLayoutManager.HORIZONTAL);
                rv_img.setLayoutManager(managers);
                ArrayList<String> pictureList = new ArrayList();
                ArrayList tempList = listData.get(position).getEvaluate_pictures();
                if(tempList != null){
                    pictureList.addAll(tempList);
                }
                rv_img.setAdapter(new CommonRecyclerViewAdapter<String>(MyEvaluationActivity.this,pictureList) {
                    @Override
                    public void convert(CommonRecyclerViewHolder h, String entity, int position) {
                        ImageView iv_picture = h.getView(R.id.iv_picture);
                        Glide.with(MyEvaluationActivity.this).load(entity).apply(new RequestOptions()).into(iv_picture);
                    }

                    @Override
                    public int getLayoutViewId(int viewType) {
                        return R.layout.item_picture_show;
                    }
                });


            }

            @Override
            public int getLayoutViewId(int viewType) {
                return R.layout.item_product_evaluation;
            }

        };
        rv_evaluationlist.setAdapter(adapter);

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
                listData.clear();
                initData();
                smart_refresh.finishRefresh(2000);
            }
        });

    }

    @Override
    protected void initData() {

        ListBaseParm baseParm = new ListBaseParm();
        baseParm.setPage(page);
        String strDes = DESHelper.encrypt(gson.toJson(baseParm));
        getMyEvaluate(strDes);

    }

    private void getMyEvaluate(String desparms){


        api.getMyEvaluate(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EvaluationBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                        LogUtils.LogE("error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(EvaluationBean obj) {
                        LogUtils.LogE(MyEvaluationActivity.class.getName() + "--getMyEvaluate:" + gson.toJson(obj));
                        if(obj.getState() == 1){

                            if(obj.getInfo().getData() != null && obj.getInfo().getData().size() > 0){
                                listData.addAll(obj.getInfo().getData());
                                adapter.notifyDataSetChanged();
                            }
                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }

                    }
                });

    }



    @Override
    protected void setStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this,true);
    }
}
