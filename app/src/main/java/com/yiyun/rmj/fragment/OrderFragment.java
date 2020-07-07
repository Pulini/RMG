package com.yiyun.rmj.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.yiyun.rmj.activity.EvaluationActivity;
import com.yiyun.rmj.activity.MyOrderActivity;
import com.yiyun.rmj.activity.OrderDetailActivity;
import com.yiyun.rmj.activity.OrderProgressActivity;
import com.yiyun.rmj.activity.SettlementActivity;
import com.yiyun.rmj.adapter.CommonRecyclerViewAdapter;
import com.yiyun.rmj.adapter.CommonRecyclerViewHolder;
import com.yiyun.rmj.base.BaseFragment;
import com.yiyun.rmj.bean.OrderInfo;
import com.yiyun.rmj.bean.OrderProductInfo;
import com.yiyun.rmj.bean.apibase.BaseBean;
import com.yiyun.rmj.bean.apibean.BuyAgainBean;
import com.yiyun.rmj.bean.apibean.OrderListBean;
import com.yiyun.rmj.bean.apiparm.OrderDetailParm;
import com.yiyun.rmj.bean.apiparm.OrderListParm;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.LogUtils;

import java.util.ArrayList;

import cn.jake.share.frdialog.dialog.FRDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OrderFragment extends BaseFragment {

    public static int SHOW_TYPE_ALL = 0;  //全部订单
    public static int SHOW_TYPE_UNPAY = 1; //待付款订单
    public static int SHOW_TYPE_UNSHIP = 2; //待发货订单
    public static int SHOW_TYPE_UNRECEIPT = 3; //待收货订单
    public static int SHOW_TYPE_UNEVALUATION = 4; //待评价订单
    private int optionOrderPosition = -1;

    int type;
    CommonRecyclerViewAdapter adapter;
    RecyclerView rv_list;
    RelativeLayout rl_no_order;
    ArrayList<OrderInfo> listData;
    private int page = 1;//页码
    private FRDialog deleteDialog;
    private TextView tv_showmsg;
    private int optionDeleteOrCancel = 0; //1为删除订单，2为取消订单
    private OrderDetailParm parm = new OrderDetailParm();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order;
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

        rv_list = view.findViewById(R.id.rv_list);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(manager);

        rl_no_order = view.findViewById(R.id.rl_no_order);
        rl_no_order.setVisibility(View.VISIBLE);

        adapter = new CommonRecyclerViewAdapter<OrderInfo>(getContext(),listData) {
            @Override
            public void convert(CommonRecyclerViewHolder h, final OrderInfo entity, final int position) {
                final Button btn_left = h.getView(R.id.btn_order_left);
                final Button btn_right = h.getView(R.id.btn_order_right);

                btn_left.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        String btnTxt = btn_left.getText().toString();
                        if(btnTxt.equals("取消订单")){

                            optionDeleteOrCancel = 2;
                            tv_showmsg.setText("确定取消订单？");
                            parm.setOrderId(entity.getOrderId());
                            parm.setOrderNo(entity.getOrderNo());
                            deleteDialog.show();

                        }else if(btnTxt.equals("查看物流")){

                            Intent orderprogressIntent = new Intent(getActivity(), OrderProgressActivity.class);
                            orderprogressIntent.putExtra("orderId", entity.getOrderId());
                            startActivity(orderprogressIntent);

                        }else if(btnTxt.equals("删除订单")){

                            optionDeleteOrCancel = 1;
                            tv_showmsg.setText("确定删除订单？");
                            parm.setOrderId(entity.getOrderId());
                            parm.setOrderNo(entity.getOrderNo());
                            deleteDialog.show();

                        }
                    }
                });

                btn_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String btnTxt = btn_right.getText().toString();
                        if(btnTxt.equals("去付款")){

                            Intent intent = new Intent(getActivity(), SettlementActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("type",2);
                            bundle.putInt("orderId",entity.getOrderId());
                            intent.putExtras(bundle);
                            startActivityForResult(intent,0);

                        }else if(btnTxt.equals("再次购买")){

                            OrderDetailParm parm = new OrderDetailParm();
                            parm.setOrderId(entity.getOrderId());
                            String strDes = DESHelper.encrypt(gson.toJson(parm));
                            repurchase(strDes);

                        }else if(btnTxt.equals("确认收货")){

                            OrderDetailParm parm = new OrderDetailParm();
                            parm.setOrderNo(entity.getOrderNo());
                            String strDes = DESHelper.encrypt(gson.toJson(parm));
                            confirmReceipt(strDes);

                        }else if (btnTxt.equals("评价")){
                            Intent evaluationIntent = new Intent(getActivity(), EvaluationActivity.class);
                            evaluationIntent.putExtra("orderInfo", listData.get(position));
                            startActivityForResult(evaluationIntent,0);
                        }else if(btnTxt.equals("删除订单")){

                            optionDeleteOrCancel = 1;
                            tv_showmsg.setText("确定删除订单？");
                            parm.setOrderId(entity.getOrderId());
                            parm.setOrderNo(entity.getOrderNo());
                            deleteDialog.show();
                        }
                    }
                });

                TextView tv_order_num = h.getView(R.id.tv_order_num);
                tv_order_num.setText("订单编号：" + entity.getOrderNo());
                TextView tv_state = h.getView(R.id.tv_state);

                btn_right.setVisibility(View.VISIBLE);
                btn_left.setVisibility(View.VISIBLE);

                switch(entity.getOrderState()){
                    case 1:
                        tv_state.setText("待支付");

                        btn_left.setText("取消订单");
                        btn_right.setText("去付款");
                        break;
                    case 2:
                        tv_state.setText("待发货");
                        btn_left.setText("取消订单");
                        btn_right.setText("再次购买");
                        break;
                    case 3:
                        tv_state.setText("已发货");

                        btn_left.setText("查看物流");
                        btn_right.setText("确认收货");
                        break;
                    case 4:
                        tv_state.setText("待评价");

                        btn_left.setText("查看物流");
                        btn_right.setText("评价");
                        break;
                    case 5:
                        tv_state.setText("退货中");
                        break;
                    case 6:
                        tv_state.setText("已退货");
                        break;
                    case 7:
                        tv_state.setText("拒绝退货");
                        break;
                    case 8:
                        tv_state.setText("已完成");
                        btn_left.setText("查看物流");

                        btn_right.setVisibility(View.GONE);
                        break;
                    case 9:
                        tv_state.setText("已取消");
                        btn_left.setText("删除订单");
                        btn_right.setText("再次购买");
                        break;

                }

                TextView tv_count_price = h.getView(R.id.tv_count_price);
                tv_count_price.setText("" + entity.getOrderPrice());
                TextView tv_yf = h.getView(R.id.tv_yf);
                tv_yf.setText("（含运费 ￥"+ entity.getExpress_money() + "）");

                RecyclerView rv_product_detail = h.getView(R.id.rv_product_detail);
                LinearLayoutManager manager1 = new LinearLayoutManager(getActivity()){

                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                };
                rv_product_detail.setLayoutManager(manager1);

                rv_product_detail.setAdapter(new CommonRecyclerViewAdapter<OrderProductInfo>(getActivity(),entity.getOrderItem()) {
                    @Override
                    public void convert(CommonRecyclerViewHolder h, OrderProductInfo product, int pos) {

                        ImageView iv_product_image = h.getView(R.id.iv_product_image);
                        TextView tv_product_name = h.getView(R.id.tv_product_name);
                        TextView tv_price = h.getView(R.id.tv_price);
                        TextView tv_product_num = h.getView(R.id.tv_product_num);

                        tv_product_name.setText(product.getProduct_name());
                        tv_price.setText("" + product.getProduct_price());
                        tv_product_num.setText("x" + product.getOrder_item_num());

                        LinearLayout rl_product_detail = h.getView(R.id.rl_product_detail);
                        rl_product_detail.setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View view) {
                                Intent orderDetailIntent = new Intent(getActivity(), OrderDetailActivity.class);
                                orderDetailIntent.putExtra("type", type);
                                orderDetailIntent.putExtra("orderId",listData.get(position).getOrderId());
                                getActivity().startActivityForResult(orderDetailIntent,0);
                            }
                        });

                        Glide.with(getActivity()).load(product.getProduct_picture().get(0).getUrl()).apply(new RequestOptions()).into(iv_product_image);
                    }

                    @Override
                    public int getLayoutViewId(int viewType) {
                        return R.layout.item_order_product;
                    }

                });

            }

            @Override
            public int getLayoutViewId(int viewType) {
                return R.layout.item_order;
            }

        };
        adapter.setOnRecyclerViewItemClickListener(new CommonRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent orderDetailIntent = new Intent(getActivity(), OrderDetailActivity.class);
                orderDetailIntent.putExtra("type", type);
                orderDetailIntent.putExtra("orderId",listData.get(position).getOrderId());
                getActivity().startActivityForResult(orderDetailIntent,0);
            }
        });
        rv_list.setAdapter(adapter);

        Button btn_lookaround = view.findViewById(R.id.btn_lookaround);
        btn_lookaround.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ((MyOrderActivity)getActivity()).callToFinish();
//                getActivity().finish();
//                Log.e("bcz","OrderFragment---onClick---setResult--resultcode:" + 505);
            }
        });


        final SmartRefreshLayout smart_refresh = view.findViewById(R.id.smartrf);
        smart_refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                initData();
                smart_refresh.finishLoadMore(1000);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                listData.clear();
                initData();
                smart_refresh.finishRefresh(1000);
            }
        });


        initDialog();

    }

    @Override
    protected void initData() {
        OrderListParm orderListParm = new OrderListParm();
        orderListParm.setPage(page);
        orderListParm.setOrderState(type);
        String parmDes = DESHelper.encrypt(gson.toJson(orderListParm));
        LogUtils.LogE(parmDes);
        myOrderList(parmDes);
    }

    public void myOrderList(String desparms) {
        api.myOrderList(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<OrderListBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                    }

                    @Override
                    public void onNext(OrderListBean obj) {
                        if(obj.getState() == 1){
                            if(obj.getInfo().getData() != null && obj.getInfo().getData().size() > 0){
                                listData.addAll(obj.getInfo().getData());
                                adapter.notifyDataSetChanged();
                                if(listData.size() == 0){
                                    rl_no_order.setVisibility(View.VISIBLE);
                                }else{
                                    rl_no_order.setVisibility(View.GONE);
                                }
                            }
                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }

                    }
                });

    }

    public void delOrder(String desparms) {
        api.delOrder(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                    }

                    @Override
                    public void onNext(BaseBean obj) {
                        if(obj.getState() == 1){
                            showConnectError(obj.getInfo().getMessage());
                            listData.clear();
                            page = 1;
                            initData();
                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }
                    }
                });
    }

    /**
     * 取消订单
     * @param desparms
     */
    public void cancel(String desparms) {
        api.cancel(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                    }

                    @Override
                    public void onNext(BaseBean obj) {
                        if(obj.getState() == 1){
                            showConnectError(obj.getInfo().getMessage());
                            listData.clear();
                            page = 1;
                            initData();
                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }

                    }
                });
    }

    /**
     * 确认收货
     * @param desparms
     */
    public void confirmReceipt(String desparms) {
        api.confirmReceipt(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                    }

                    @Override
                    public void onNext(BaseBean obj) {
                        showConnectError(obj.getInfo().getMessage());
                        if(obj.getState() == 1){
                            listData.clear();
                            page = 1;
                            initData();
                        }else{

                        }

                    }
                });
    }



    /**
     * 再次购买
     * @param desparms
     */
    public void repurchase(String desparms) {
        api.repurchase(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BuyAgainBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                    }

                    @Override
                    public void onNext(BuyAgainBean obj) {

                        if(obj.getState() == 1){
                            Intent intent = new Intent(getActivity(), SettlementActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("type",2);
                            bundle.putInt("orderId",obj.getInfo().getData().getOrderId());
                            intent.putExtras(bundle);
                            startActivityForResult(intent,0);
                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }

                    }
                });

    }

    public void initDialog(){
        deleteDialog =new FRDialog.CommonBuilder(getActivity()).setContentView(R.layout.dialog_delete).create();
        TextView dialog_tv_delete = deleteDialog.getView(R.id.tv_sure);
        TextView dialog_tv_cancel = deleteDialog.getView(R.id.tv_cancel);
        tv_showmsg = deleteDialog.getView(R.id.tv_showmsg);
        dialog_tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.dismiss();
            }
        });

        dialog_tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(optionDeleteOrCancel == 1){
                    String strDes = DESHelper.encrypt(gson.toJson(parm));
                    delOrder(strDes);
                }else if(optionDeleteOrCancel == 2){
                    String strDes = DESHelper.encrypt(gson.toJson(parm));
                    cancel(strDes);
                }

                deleteDialog.dismiss();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            page = 1;
            listData.clear();
            initData();
        }

    }
}
