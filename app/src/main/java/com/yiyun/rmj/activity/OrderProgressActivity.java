package com.yiyun.rmj.activity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hjq.toast.ToastUtils;
import com.yiyun.rmj.R;
import com.yiyun.rmj.adapter.CommonRecyclerViewAdapter;
import com.yiyun.rmj.adapter.CommonRecyclerViewHolder;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.ProgressInfo;
import com.yiyun.rmj.bean.apibean.OrderProgressBean;
import com.yiyun.rmj.bean.apiparm.OrderDetailParm;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.StatusBarUtil;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//订单进度页面
public class OrderProgressActivity extends BaseActivity implements View.OnClickListener {

    ArrayList<ProgressInfo> listData;
    private int orderId;
    private ImageView iv_product_image;
    private TextView tv_state;
    private TextView tv_express_delivery_name;
    private TextView tv_express_delivery_phone;
    private String[] orderState = new String[]{"在途中","已揽收","疑难","已签收" ,"退签","同城派送中","退回","转单"};
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
        return R.layout.activity_orderprogress;
    }

    public void initTitleView(){
        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.order_progress));
    }

    @Override
    protected void initView() {
        initTitleView();
        listData = new ArrayList<>();
        orderId = getIntent().getIntExtra("orderId",0);


        iv_product_image = findViewById(R.id.iv_product_image);
        tv_state = findViewById(R.id.tv_state);
        tv_express_delivery_name = findViewById(R.id.tv_express_delivery_name);
        tv_express_delivery_phone = findViewById(R.id.tv_express_delivery_phone);
        RecyclerView rv_list = findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommonRecyclerViewAdapter<ProgressInfo>(this,listData) {
            @Override
            public void convert(CommonRecyclerViewHolder h, ProgressInfo entity, int position) {
                Log.e("bcz","list_position:" + position);
                View v_progress_top_line = h.getView(R.id.v_progress_top_line);
                View v_progress_bottom_line = h.getView(R.id.v_progress_bottom_line);
                ImageView iv_current_pgress = h.getView(R.id.iv_current_pgress);
                ImageView iv_stale_pgress = h.getView(R.id.iv_stale_pgress);
                TextView tv_state = h.getView(R.id.tv_state);
                TextView tv_date = h.getView(R.id.tv_date);

                tv_state.setText(entity.getContext());
                tv_date.setText(entity.getFtime());

                if(position == 0){
                    Log.e("bcz","position == 0:" + position);
                    iv_current_pgress.setVisibility(View.VISIBLE);
                    iv_stale_pgress.setVisibility(View.GONE);
                    tv_state.setTextColor(getResources().getColor(R.color.lightblue));
                    v_progress_bottom_line.setVisibility(View.VISIBLE);
                    v_progress_top_line.setVisibility(View.INVISIBLE);

                }else if(position == listData.size() - 1){
                    Log.e("bcz","position == listData.size():" + position);
                    iv_current_pgress.setVisibility(View.GONE);
                    iv_stale_pgress.setVisibility(View.VISIBLE);
                    tv_state.setTextColor(getResources().getColor(R.color.color99));
                    v_progress_bottom_line.setVisibility(View.GONE);
                    v_progress_top_line.setVisibility(View.VISIBLE);

                }else{
                    iv_current_pgress.setVisibility(View.GONE);
                    iv_stale_pgress.setVisibility(View.VISIBLE);
                    tv_state.setTextColor(getResources().getColor(R.color.color99));
                    v_progress_bottom_line.setVisibility(View.VISIBLE);
                    v_progress_top_line.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public int getLayoutViewId(int viewType) {
                return R.layout.item_order_progress;
            }
        };

        rv_list.setAdapter(adapter);

    }

    @Override
    protected void initData() {
        OrderDetailParm orderDetailParm = new OrderDetailParm();
        orderDetailParm.setOrderId(orderId);
        String strDes = DESHelper.encrypt(gson.toJson(orderDetailParm));
        logisticsQuery(strDes);

    }


    /**
     * 物流信息查询
     * @param desparms
     */
    public void logisticsQuery(String desparms) {
        api.logisticsQuery(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<OrderProgressBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                    }

                    @Override
                    public void onNext(OrderProgressBean obj) {
                        if(obj.getState() == 1){
                            dragInfo(obj.getInfo().getData());
//                            listData.addAll(obj.getInfo().getData());
//                            adapter.notifyDataSetChanged();
                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }

                    }
                });

    }

    private void dragInfo(OrderProgressBean.ProgressData info){

        Glide.with(OrderProgressActivity.this).load(info.getProduct_picture()).into(iv_product_image);
        //        tv_express_delivery_phone.setText(info.getCourier_number());

        if(info.getLogistic() != null){
            if(info.getLogistic().getState() != null){
                tv_state.setText(orderState[Integer.parseInt(info.getLogistic().getState())]);
            }

            if(info.getLogistic().getNu() != null){
                tv_express_delivery_name.setText(info.getCourier_company() + ":" + info.getCourier_number());
            }

            if(info.getLogistic().getData() != null && info.getLogistic().getData().size() > 0){
                listData.addAll(info.getLogistic().getData());
                adapter.notifyDataSetChanged();
            }else{
                ToastUtils.show("暂无快递运输进度信息");
            }

        }else{
            ToastUtils.show("暂无快递运输进度信息");
        }
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this,true);
    }
}
