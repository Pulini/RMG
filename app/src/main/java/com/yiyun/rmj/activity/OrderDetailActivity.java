package com.yiyun.rmj.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hjq.toast.ToastUtils;
import com.yiyun.rmj.R;
import com.yiyun.rmj.adapter.CommonRecyclerViewAdapter;
import com.yiyun.rmj.adapter.CommonRecyclerViewHolder;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.OrderInfo;
import com.yiyun.rmj.bean.OrderProductInfo;
import com.yiyun.rmj.bean.apibase.BaseBean;
import com.yiyun.rmj.bean.apibean.BuyAgainBean;
import com.yiyun.rmj.bean.apibean.OrderDetailBean;
import com.yiyun.rmj.bean.apiparm.OrderDetailParm;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.utils.StatusBarUtil;

import java.util.ArrayList;

import cn.jake.share.frdialog.dialog.FRDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {

    public static final int SHOW_TYPE_UNSHIP = 0;//待发货
    public static final int SHOW_TYPE_UNPAY = 1;//待付款
    public static final int SHOW_TYPE_CANCEL = 2;//已取消
    public static final int SHOW_TYPE_COMPLETE = 3;//已完成
    public static final int SHOW_TYPE_UNEVALUATION = 4;//待评价
    public static final int SHOW_TYPE_UNRECEIPT = 5; //已发货
    private boolean needRefresh = false;

    private FRDialog deleteDialog;
    private TextView tv_showmsg;
    private int optionDeleteOrCancel = 0; //1为删除订单，2为取消订单
    private OrderDetailParm parm = new OrderDetailParm();

    public int type;
    private int orderId;
    CommonRecyclerViewAdapter adapter;
    ArrayList<OrderProductInfo> lists;
    private RelativeLayout rl_delivery_info;
    private OrderInfo orderInfo;

    ImageView iv_order_type_icon;
    TextView tv_order_type,tv_logistics_info,tv_logistics_date,tv_name, tv_phone, tv_address,
            tv_post_money, tv_collection_use_num,tv_leave_message,tv_productcount,tv_total_money,tv_order_num
            ,tv_order_date, tv_ship_date, tv_delivery_number,tv_copy;
    Button btn_left, btn_right;
    RelativeLayout ll_delivery_number;
    LinearLayout ll_ship_time, ll_pay_time;


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.iv_back:
                if(needRefresh){
                    setResult(RESULT_OK);
                }
                finish();
                break;
            case R.id.btn_left:
                String btnLeftTxt = btn_left.getText().toString();
                if(btnLeftTxt.equals("取消订单")){

                    optionDeleteOrCancel = 2;
                    tv_showmsg.setText("确定取消订单？");
                    parm.setOrderId(orderInfo.getOrderId());
                    parm.setOrderNo(orderInfo.getOrderNo());
                    deleteDialog.show();


                }else if(btnLeftTxt.equals("删除订单")){

                    optionDeleteOrCancel = 1;
                    tv_showmsg.setText("确定删除订单？");
                    parm.setOrderNo(orderInfo.getOrderNo());
                    parm.setOrderId(orderId);
                    deleteDialog.show();

                }else if(btnLeftTxt.equals("查看物流")){
                    Intent orderprogressIntent = new Intent(OrderDetailActivity.this, OrderProgressActivity.class);
                    orderprogressIntent.putExtra("orderId", orderId);
                    startActivity(orderprogressIntent);
                }
                break;
            case R.id.btn_right:
                String btnRightTxt = btn_left.getText().toString();
                if(btnRightTxt.equals("去付款")){

                    Intent intent = new Intent(OrderDetailActivity.this, SettlementActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("type",2);
                    bundle.putInt("orderId",orderId);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }else if(btnRightTxt.equals("再次购买")){

                    OrderDetailParm parm = new OrderDetailParm();
                    parm.setOrderId(orderId);
                    String strDes = DESHelper.encrypt(gson.toJson(parm));
                    repurchase(strDes);

                }else if(btnRightTxt.equals("评价")){

                    Intent evaluationIntent = new Intent(OrderDetailActivity.this, EvaluationActivity.class);
                    evaluationIntent.putExtra("orderInfo", orderInfo);
                    startActivity(evaluationIntent);

                }else if(btnRightTxt.equals("删除订单")){

                    optionDeleteOrCancel = 1;
                    tv_showmsg.setText("确定删除订单？");
                    parm.setOrderNo(orderInfo.getOrderNo());
                    parm.setOrderId(orderId);
                    deleteDialog.show();

                }else if(btnRightTxt.equals("确认收货")){
                    OrderDetailParm parm = new OrderDetailParm();
                    parm.setOrderNo(orderInfo.getOrderNo());
                    String strDes = DESHelper.encrypt(gson.toJson(parm));
                    confirmReceipt(strDes);
                }
                break;
            case R.id.tv_copy:
                copyToClipBoard(tv_delivery_number.getText().toString().trim());
                break;

            case R.id.rl_delivery_info:
                //查看物流信息
                Intent orderprogressIntent = new Intent(OrderDetailActivity.this, OrderProgressActivity.class);
                orderprogressIntent.putExtra("orderId", orderId);
                startActivity(orderprogressIntent);
                break;
        }
    }

    public void copyToClipBoard(String text){
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) OrderDetailActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", text);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        ToastUtils.show("已复制到粘贴板");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail;
    }


    public void initTitleView(){
        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.order_detail));

    }

    @Override
    protected void initView() {
        lists = new ArrayList<>();
        initTitleView();
        type = getIntent().getIntExtra("type",SHOW_TYPE_UNSHIP);
        orderId = getIntent().getIntExtra("orderId",0);

        iv_order_type_icon = findViewById(R.id.iv_order_type_icon);
        tv_order_type = findViewById(R.id.tv_order_type);
        rl_delivery_info = findViewById(R.id.rl_delivery_info);
        tv_logistics_info = findViewById(R.id.tv_logistics_info);
        tv_logistics_date = findViewById(R.id.tv_logistics_date);
        tv_name = findViewById(R.id.tv_name);
        tv_phone = findViewById(R.id.tv_phone);
        tv_address = findViewById(R.id.tv_address);
        tv_post_money = findViewById(R.id.tv_post_money);
        tv_collection_use_num = findViewById(R.id.tv_collection_use_num);
        tv_leave_message = findViewById(R.id.tv_leave_message);
        tv_productcount = findViewById(R.id.tv_productcount);
        tv_total_money = findViewById(R.id.tv_total_money);
        tv_order_num = findViewById(R.id.tv_order_num);
        tv_order_date = findViewById(R.id.tv_order_date);
        tv_ship_date = findViewById(R.id. tv_ship_date);
        tv_delivery_number = findViewById(R.id.tv_delivery_number);
        rl_delivery_info.setOnClickListener(this);

        ll_ship_time = findViewById(R.id.ll_ship_time);
        ll_delivery_number = findViewById(R.id.ll_delivery_number);
        ll_pay_time = findViewById(R.id.ll_pay_time);

        tv_copy = findViewById(R.id.tv_copy);
        tv_copy.setOnClickListener(this);

        btn_left = findViewById(R.id.btn_left);
        btn_right = findViewById(R.id.btn_right);
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);

        RecyclerView rv_product_list = findViewById(R.id.rv_product_list);
        LinearLayoutManager manager = new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        rv_product_list.setLayoutManager(manager);
        adapter = new CommonRecyclerViewAdapter<OrderProductInfo>(this,lists) {
            @Override
            public void convert(CommonRecyclerViewHolder h, OrderProductInfo entity, int position) {
                ImageView iv_product_image = h.getView(R.id.iv_product_image);
                TextView tv_product_name = h.getView(R.id.tv_product_name);
                TextView tv_price = h.getView(R.id.tv_price);
                TextView tv_product_num = h.getView(R.id.tv_product_num);

                tv_product_name.setText(entity.getProduct_name());
                tv_price.setText("" + entity.getProduct_price());
                tv_product_num.setText("x" + entity.getOrder_item_num());
                Glide.with(OrderDetailActivity.this).load(entity.getProduct_picture().get(0).getUrl()).apply(new RequestOptions()).into(iv_product_image);
            }

            @Override
            public int getLayoutViewId(int viewType) {
                return R.layout.item_order_productinfo;
            }
        };
        rv_product_list.setAdapter(adapter);

//        switch(type){
//            case SHOW_TYPE_UNSHIP:
//                //待发货
//                iv_order_type_icon.setImageResource(R.mipmap.order_unship_icon);
//                tv_order_type.setText(getString(R.string.unship));
//                rl_delivery_info.setVisibility(View.GONE);
//                break;
//            case SHOW_TYPE_UNPAY:
//                //待付款
//                iv_order_type_icon.setImageResource(R.mipmap.order_unpay_icon);
//                tv_order_type.setText(getString(R.string.unpay));
//                rl_delivery_info.setVisibility(View.GONE);
//                break;
//            case SHOW_TYPE_CANCEL:
//                //已取消
//                iv_order_type_icon.setImageResource(R.mipmap.order_close_icon);
//                tv_order_type.setText(getString(R.string.deal_close));
//                rl_delivery_info.setVisibility(View.GONE);
//                break;
//            case SHOW_TYPE_COMPLETE:
//                //已完成
//                iv_order_type_icon.setImageResource(R.mipmap.order_deal_success_icon);
//                tv_order_type.setText(getString(R.string.deal_success));
//                rl_delivery_info.setVisibility(View.VISIBLE);
////                tv_logistics_info.setText(getString());
////                tv_logistics_date.setText(getString());
//                break;
//            case SHOW_TYPE_UNEVALUATION:
//                //待评价
//                iv_order_type_icon.setImageResource(R.mipmap.order_deal_success_icon);
//                tv_order_type.setText(getString(R.string.deal_success));
//                rl_delivery_info.setVisibility(View.VISIBLE);
////                tv_logistics_info.setText(getString());
////                tv_logistics_date.setText(getString());
//                break;
//            case SHOW_TYPE_UNRECEIPT:
//                //已发货
//                iv_order_type_icon.setImageResource(R.mipmap.order_unreceipt_icon);
//                tv_order_type.setText(getString(R.string.unreceipt));
//                rl_delivery_info.setVisibility(View.VISIBLE);
////                tv_logistics_info.setText(getString());
////                tv_logistics_date.setText(getString());
//                break;
//
//        }
        initDialog();

    }

    @Override
    protected void initData() {

        OrderDetailParm orderDetailParm = new OrderDetailParm();
        orderDetailParm.setOrderId(orderId);
        String strDes = DESHelper.encrypt(gson.toJson(orderDetailParm));
        LogUtils.LogE("strDes:" + strDes);
        orderDetail(strDes);

    }


    /**
     * 获取订单详情
     * @param desparms
     */
    public void orderDetail(String desparms) {
        api.orderDetail(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<OrderDetailBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                    }

                    @Override
                    public void onNext(OrderDetailBean obj) {
                        if(obj.getState() == 1){
                            refrashData(obj.getInfo().getData());
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
                            setResult(RESULT_OK);
                            finish();
                        }else if(obj.getState() == -1){
                            startlogin();
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
                            setResult(RESULT_OK);
                            finish();
                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{
                            showConnectError(obj.getInfo().getMessage());
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
                            Intent intent = new Intent(OrderDetailActivity.this, SettlementActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("type",2);
                            bundle.putInt("orderId",obj.getInfo().getData().getOrderId());
                            intent.putExtras(bundle);
                            startActivityForResult(intent,0);
                        }else if(obj.getState() == -1){
                            startlogin();
                        } else{
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
//                            listData.addAll(obj.getInfo().getData());
//                            adapter.notifyDataSetChanged();
                            setResult(RESULT_OK);
                            finish();
                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }
                    }
                });

    }


    public void refrashData(OrderInfo orderInfo){

        this.orderInfo = orderInfo;

        tv_name.setText(orderInfo.getConsigneeName());
        tv_phone.setText(orderInfo.getPhone());
        tv_address.setText(orderInfo.getAddress());

        lists.addAll(orderInfo.getOrderItem());
        adapter.notifyDataSetChanged();

        tv_post_money.setText(orderInfo.getExpress_money() + "");
        tv_collection_use_num.setText(orderInfo.getIntegral_deduction() + "");
        tv_leave_message.setText(orderInfo.getMessage());
        tv_productcount.setText("共计" + orderInfo.getOrderNum()+ "件商品");
        tv_total_money.setText("" + orderInfo.getOrderPrice());
        tv_order_num.setText(orderInfo.getOrderNo());
        tv_order_date.setText(orderInfo.getPayTime());

        String payTime = orderInfo.getPayTime();
        String deliverTime = orderInfo.getDeliverTime();
        String courierNumber = orderInfo.getCourierNumber();

        if(TextUtils.isEmpty(payTime)){
            ll_pay_time.setVisibility(View.GONE); //付款时间
        }else{
            ll_pay_time.setVisibility(View.VISIBLE); //付款时间
            tv_order_date.setText(payTime);
        }

        if(TextUtils.isEmpty(deliverTime)){
            ll_ship_time.setVisibility(View.GONE);  //发货时间
        }else{
            ll_ship_time.setVisibility(View.VISIBLE);  //发货时间
            tv_ship_date.setText(deliverTime);
        }

        if(TextUtils.isEmpty(deliverTime)){
            ll_delivery_number.setVisibility(View.GONE);  //快递单号
        }else{
            ll_delivery_number.setVisibility(View.VISIBLE);  //快递单号
            tv_delivery_number.setText(courierNumber);
        }

        int orderState = orderInfo.getOrderState();
        switch(orderState) {
            case 1:
                //待支付
                rl_delivery_info.setVisibility(View.GONE); //快递信息组件

                iv_order_type_icon.setImageResource(R.mipmap.order_unpay_icon);
                tv_order_type.setText(getString(R.string.unpay));

                btn_left.setText("取消订单");
                btn_right.setText("去付款");
                break;

            case 2:
                //待发货
                iv_order_type_icon.setImageResource(R.mipmap.order_unship_icon);
                tv_order_type.setText(getString(R.string.unship));
                rl_delivery_info.setVisibility(View.GONE);

                btn_left.setText("取消订单");
                btn_right.setVisibility(View.GONE);
                break;
            case 3:
                //已发货
                iv_order_type_icon.setImageResource(R.mipmap.order_unreceipt_icon);
                tv_order_type.setText(getString(R.string.unreceipt));
                rl_delivery_info.setVisibility(View.VISIBLE); //快递信息组件
                if(orderInfo.getLogistics().getContext() != null){
                    tv_logistics_info.setText(orderInfo.getLogistics().getContext());
                }else{
                    tv_logistics_info.setText("暂无物流信息");
                }
                if(orderInfo.getLogistics().getFtime() != null){
                    tv_logistics_date.setText(orderInfo.getLogistics().getFtime());
                }else{
                    tv_logistics_date.setText("");
                }

                btn_left.setText("查看物流");
                btn_right.setText("确认收货");
                break;
            case 4:
                //待评价
                iv_order_type_icon.setImageResource(R.mipmap.order_deal_success_icon);
                tv_order_type.setText(getString(R.string.deal_success));
                rl_delivery_info.setVisibility(View.VISIBLE); //快递信息组件
                tv_logistics_info.setText(orderInfo.getLogistics().getContext());
                tv_logistics_date.setText(orderInfo.getLogistics().getFtime());

                btn_left.setText("查看物流");
                btn_right.setText("删除订单");

                break;
            case 5:
                //退货中
//                tv_state.setText("退货中");
                break;
            case 6:
                //已退货
//                tv_state.setText("已退货");
                break;
            case 7:
                //拒绝退货
//                tv_state.setText("拒绝退货");
                break;
            case 8:
                //已完成

                iv_order_type_icon.setImageResource(R.mipmap.order_deal_success_icon);
                tv_order_type.setText(getString(R.string.deal_success));

                btn_left.setText("查看物流");
                btn_right.setVisibility(View.GONE);

                break;
            case 9:
                //已取消
                iv_order_type_icon.setImageResource(R.mipmap.order_close_icon);
                tv_order_type.setText(getString(R.string.deal_close));
                rl_delivery_info.setVisibility(View.GONE); //快递信息组件

                btn_left.setText("删除订单");
                btn_right.setText("再次购买");

                break;
        }

    }

    public void initDialog(){
        deleteDialog =new FRDialog.CommonBuilder(OrderDetailActivity.this).setContentView(R.layout.dialog_delete).create();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            needRefresh = true;
            initData();
        }
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this,true);
    }
}
