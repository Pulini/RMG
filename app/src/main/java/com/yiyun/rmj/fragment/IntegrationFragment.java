package com.yiyun.rmj.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yiyun.rmj.R;
import com.yiyun.rmj.activity.IntegrationActivity;
import com.yiyun.rmj.adapter.CommonRecyclerViewAdapter;
import com.yiyun.rmj.adapter.CommonRecyclerViewHolder;
import com.yiyun.rmj.base.BaseFragment;
import com.yiyun.rmj.bean.apibean.IntegrationBean;
import com.yiyun.rmj.bean.apiparm.IntergralParm;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.DividerItemDecoration;
import com.yiyun.rmj.utils.LogUtils;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class IntegrationFragment extends BaseFragment{
    public static int SHOW_TYPE_ALL = 0;  //全部订单
    public static int SHOW_TYPE_GET = 1; //获取
    public static int SHOW_TYPE_OUT = 2; //使用

    int type;
    RecyclerView rv_list;
    ArrayList<IntegrationBean.Integration> listData;
    CommonRecyclerViewAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_integration;
    }

    @Override
    protected void initView(View view) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt("type");
        }else{
            type = SHOW_TYPE_ALL;
        }
        listData = new ArrayList<>();


        rv_list = (RecyclerView) view.findViewById(R.id.rv_list);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(manager);
        rv_list.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        adapter = new CommonRecyclerViewAdapter<IntegrationBean.Integration>(getContext(),listData) {
            @Override
            public void convert(CommonRecyclerViewHolder h, IntegrationBean.Integration entity, int position) {

                TextView tv_origin = h.getView(R.id.tv_origin);
                TextView tv_integration_num = h.getView(R.id.tv_integration_num);
                TextView tv_integrtation_des = h.getView(R.id.tv_integrtation_des);
                TextView tv_date = h.getView(R.id.tv_date);

                tv_origin.setText(entity.getInstructions());
                tv_integration_num.setText(entity.getIntegralNumber());
                tv_integrtation_des.setText(entity.getIntegralType());
                tv_date.setText(entity.getSaveType());

            }

            @Override
            public int getLayoutViewId(int viewType) {
                return R.layout.item_integration;
            }

        };

        rv_list.setAdapter(adapter);

    }

    @Override
    protected void initData() {
        IntergralParm parm = new IntergralParm();
        parm.setType(getUpType());
        String strDes = DESHelper.encrypt(gson.toJson(parm));
        getMyIntegral(strDes);
    }

    public int getUpType(){
        if(type == SHOW_TYPE_ALL){
            return 2;
        }else if(type == SHOW_TYPE_GET){
            return 0;
        }else if(type == SHOW_TYPE_OUT){
            return 1;
        }
        return 2;
    }

    private void getMyIntegral(String desparms){

        api.getMyIntegral(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<IntegrationBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                        LogUtils.LogE("error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(IntegrationBean obj) {
                        LogUtils.LogE(IntegrationFragment.class.getName() + "--defaultAliPayNum:" + gson.toJson(obj));
                        if(obj.getState() == 1){
                            listData.clear();
                            listData.addAll(obj.getInfo().getData().getList());
                            adapter.notifyDataSetChanged();
                            IntegrationActivity activity = (IntegrationActivity)getActivity();
                            activity.setTotalIntegral("" + obj.getInfo().getData().getIntegral());
                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }

                    }
                });

    }
}
