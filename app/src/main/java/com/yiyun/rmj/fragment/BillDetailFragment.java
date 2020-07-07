package com.yiyun.rmj.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yiyun.rmj.R;
import com.yiyun.rmj.adapter.CommonRecyclerViewAdapter;
import com.yiyun.rmj.adapter.CommonRecyclerViewHolder;
import com.yiyun.rmj.base.BaseFragment;
import com.yiyun.rmj.bean.MoneyDetailInfo;
import com.yiyun.rmj.bean.apibean.MoneyDetailBean;
import com.yiyun.rmj.bean.apiparm.MoneyDetailParm;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.LogUtils;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BillDetailFragment extends BaseFragment {

    public static int SHOW_TYPE_ALL = 0;  //全部
    public static int SHOW_TYPE_GET = 1; //收入
    public static int SHOW_TYPE_OUT = 2; //支出

    ArrayList<MoneyDetailInfo> listData;
    RecyclerView rv_list;
    private int type;
    CommonRecyclerViewAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bill;
    }

    @Override
    protected void initView(View view) {
        listData = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt("type");
        }else{
            type = SHOW_TYPE_ALL;
        }

        rv_list = view.findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CommonRecyclerViewAdapter<MoneyDetailInfo>(getActivity(),listData) {

            @Override
            public void convert(CommonRecyclerViewHolder h, MoneyDetailInfo entity, int position) {

                TextView bill_type = h.getView(R.id.bill_type);
                TextView bill_num = h.getView(R.id.bill_num);
                TextView bill_des = h.getView(R.id.bill_des);
                TextView bill_date = h.getView(R.id.bill_date);
                bill_type.setText(entity.getDetail());
                bill_num.setText(entity.getMoney());
                bill_des.setText(entity.getRemark());
                bill_date.setText(entity.getSave_time());
            }

            @Override
            public int getLayoutViewId(int viewType) {
                return R.layout.item_bill;
            }

        };
        rv_list.setAdapter(adapter);

    }

    @Override
    protected void initData() {

        MoneyDetailParm moneyDetailParm = new MoneyDetailParm();
        moneyDetailParm.setType(getUpType());
        String strDes = DESHelper.encrypt(gson.toJson(moneyDetailParm));
        LogUtils.LogE("Uptype:" + strDes);
        moneyDetail(strDes);

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

    private void moneyDetail(String desparms){

        api.moneyDetail(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MoneyDetailBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                        LogUtils.LogE("error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(MoneyDetailBean obj) {
                        LogUtils.LogE(BillDetailFragment.class.getName() + "--moneyDetail:" + gson.toJson(obj));
                        if(obj.getState() == 1){
                            listData.clear();
                            listData.addAll(obj.getInfo().getData());
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
