package com.yiyun.rmj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.yiyun.rmj.bean.apibean.CollectionBean;
import com.yiyun.rmj.bean.apiparm.ListBaseParm;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.utils.StatusBarUtil;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//我的收藏页
public class MyCollectionActivity extends BaseActivity implements View.OnClickListener {

    ArrayList<CollectionBean.Collection> collectionList;
    RecyclerView rv_collection_list;
    RelativeLayout rl_no_one_collection;
    private int page = 1;
    private CommonRecyclerViewAdapter adapter;

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
        return R.layout.activity_mycollection;
    }

    public void initTitleView(){
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.my_collection));

        ImageView iv_back  = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        collectionList = new ArrayList<>();

        initTitleView();

        rl_no_one_collection =  findViewById(R.id.rl_no_one_collection);
        rv_collection_list =  findViewById(R.id.rv_collection_list);

        rv_collection_list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommonRecyclerViewAdapter<CollectionBean.Collection>(this,collectionList) {
            @Override
            public void convert(CommonRecyclerViewHolder h, final CollectionBean.Collection entity, int position) {

                TextView tv_product_name = h.getView(R.id.tv_product_name);
                TextView tv_store = h.getView(R.id.tv_store);
                TextView tv_price = h.getView(R.id.tv_price);
                ImageView iv_product_image = h.getView(R.id.iv_product_image);

//                Glide.with(MyCollectionActivity.this).load

                tv_product_name.setText(entity.getProduct_name());
                tv_price.setText(entity.getProduct_price() + "");
                tv_store.setText("库存：" + entity.getInventory());
                Glide.with(MyCollectionActivity.this).load(entity.getProduct_picture().get(0).getUrl()).apply(new RequestOptions()).into(iv_product_image);

                Button btn_buy = h.getView(R.id.btn_buy);
                btn_buy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //到确认订单界面
                        Intent intent = new Intent(MyCollectionActivity.this, SettlementActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("type",1);
                        bundle.putInt("productId",entity.getProduct_id());
                        bundle.putInt("num",1);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public int getLayoutViewId(int viewType) {
                return R.layout.item_collection;
            }

        };

        rv_collection_list.setAdapter(adapter);

        final SmartRefreshLayout smart_refresh = findViewById(R.id.smart_refresh);

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
                collectionList.clear();
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
        getMyCollection(strDes);

    }

    public void refreshView(){
        if(collectionList.size() == 0){
            rv_collection_list.setVisibility(View.GONE);
            rl_no_one_collection.setVisibility(View.VISIBLE);
        }else{
            rv_collection_list.setVisibility(View.VISIBLE);
            rl_no_one_collection.setVisibility(View.GONE);
        }
    }

    private void getMyCollection(String desparms){


        api.getMyCollection(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CollectionBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                        LogUtils.LogE("error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(CollectionBean obj) {
                        LogUtils.LogE(ZfbAccountManagerActivity.class.getName() + "--defaultAliPayNum:" + gson.toJson(obj));
                        if(obj.getState() == 1){
                            if(obj.getInfo().getData() != null && obj.getInfo().getData().size() > 0){
                                collectionList.addAll(obj.getInfo().getData());
                                refreshView();
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
