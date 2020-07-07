package com.yiyun.rmj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.yiyun.rmj.bean.apibase.BaseBean;
import com.yiyun.rmj.bean.apibase.BaseParm;
import com.yiyun.rmj.bean.apibean.CartProduct;
import com.yiyun.rmj.bean.apibean.UpdateShopCartNumBean;
import com.yiyun.rmj.bean.apiparm.DelShoppingCartParm;
import com.yiyun.rmj.bean.apiparm.UpdateShoppingCartProductNumParm;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.utils.StatusBarUtil;
import com.yiyun.rmj.view.AmountView;

import java.util.ArrayList;
import java.util.List;

import cn.jake.share.frdialog.dialog.FRDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShoppingCartActivity extends BaseActivity implements View.OnClickListener{

    ArrayList<CartProduct> list;
    MyCommonRecyclerViewAdapter adapter;
    LinearLayout ll_count;//合计视图
    TextView tv_delete; //删除按钮
    TextView tv_count; //结算按钮
    TextView tv_complete; //完成按钮
    TextView tv_title_manager;// 管理按钮
    TextView tv_total_money;//合计金额
    CheckBox btn_select;
    private FRDialog deleteDialog;
    UpdateShoppingCartProductNumParm upParm;
    private int currentSelectPosition;
    private RelativeLayout rl_list_is_none;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.title_manager:
                tv_complete.setVisibility(View.VISIBLE);
                tv_title_manager.setVisibility(View.GONE);
                ll_count.setVisibility(View.GONE);
                tv_count.setVisibility(View.GONE);
                tv_delete.setVisibility(View.VISIBLE);
                break;

            case R.id.title_complete:
                tv_complete.setVisibility(View.GONE);
                tv_title_manager.setVisibility(View.VISIBLE);
                ll_count.setVisibility(View.VISIBLE);
                tv_count.setVisibility(View.VISIBLE);
                tv_delete.setVisibility(View.GONE);

                break;
            case R.id.tv_settlement:
                //结算
                String strFaverateId =  getSelectFaverateId();
                if(TextUtils.isEmpty(strFaverateId)){
                    ToastUtils.show("请选择要结算的选项");
                    return;
                }

                Intent intent = new Intent(ShoppingCartActivity.this, SettlementActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type",0);
                bundle.putString("favoriteIdsStr",strFaverateId);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.tv_delete_:
                deleteDialog.show();
                break;
            case R.id.btn_go_around:
                finish();
                break;
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_shopping_cart;
    }

    @Override
    protected void initView() {
        upParm = new UpdateShoppingCartProductNumParm();
        list = new ArrayList<>();
        initTitleView();
        ll_count = (LinearLayout) findViewById(R.id.ll_count);
        tv_count = (TextView) findViewById(R.id.tv_settlement);
        tv_count.setOnClickListener(this);
        tv_total_money = (TextView) findViewById(R.id.tv_total_money);
        tv_total_money.setText("0");
        tv_count.setText("结算(0)");
        TextView tv_delete_ = (TextView) findViewById(R.id.tv_delete_);
        tv_delete_.setOnClickListener(this);

        btn_select = (CheckBox) findViewById(R.id.btn_select);
        btn_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if(adapter.getSelectCount() < list.size()){
                        adapter.setListDataCheckState(true);
                    }
                }else{
                    if(adapter.getSelectCount() == list.size()){
                        //如果是本身操作，则当前item肯定是全选状态
                        //如果是item变化来调用不全选状态，则当前选中的个数肯定是list.size()-1
                        adapter.setListDataCheckState(false);
                    }
                }
            }
        });

        tv_delete = (TextView) findViewById(R.id.tv_delete_);
        RecyclerView rView = (RecyclerView) findViewById(R.id.rlv_cart);
        rView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyCommonRecyclerViewAdapter(ShoppingCartActivity.this,list);
        rView.setAdapter(adapter);

        deleteDialog =new FRDialog.CommonBuilder(this).setContentView(R.layout.dialog_delete).create();
        TextView dialog_tv_delete = deleteDialog.getView(R.id.tv_sure);
        TextView dialog_tv_cancel = deleteDialog.getView(R.id.tv_cancel);
        dialog_tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.dismiss();
            }
        });

        dialog_tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adapter.getSelectCount() > 0){
                    adapter.deleteSelectItem();
                }
                adapter.setListDataCheckState(false);
                initTotalBottomState(0,0);
                btn_select.setChecked(false);
                deleteDialog.dismiss();
            }
        });

        Button btn_go_around = findViewById(R.id.btn_go_around);
        btn_go_around.setOnClickListener(this);

        rl_list_is_none = findViewById(R.id.rl_list_is_none);
        rl_list_is_none.setVisibility(View.VISIBLE);
    }

    private void initTitleView(){
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        TextView iv_title = (TextView) findViewById(R.id.tv_title);
        iv_title.setVisibility(View.VISIBLE);
        iv_title.setText(getString(R.string.shopping_cart));

        tv_title_manager = (TextView) findViewById(R.id.title_manager);
        tv_title_manager.setVisibility(View.VISIBLE);
        tv_title_manager.setOnClickListener(this);

        tv_complete = (TextView) findViewById(R.id.title_complete);
        tv_complete.setVisibility(View.GONE);
        tv_complete.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        BaseParm baseParm = new BaseParm();
        String paramDes = DESHelper.encrypt(gson.toJson(baseParm));
        getShoppingCart(paramDes);
    }

    /**
     *
     */
    public void initTotalBottomState(int count, double money){
        Log.e("bcz","---initTotalBottomState---");
        String text = Double.toString(money);
        tv_total_money.setText(text);
        tv_count.setText("结算(" + count + ")");

        if(count == list.size()){
            if(!btn_select.isChecked()){
                btn_select.setChecked(true);
            }
        }else{
            if(btn_select.isChecked()){
                btn_select.setChecked(false);
            }
        }
    }

    public void getShoppingCart(String desparms) {
        api.getShoppingCart(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<com.yiyun.rmj.bean.apibean.ShoppingCartBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                    }

                    @Override
                    public void onNext(com.yiyun.rmj.bean.apibean.ShoppingCartBean obj) {
                        LogUtils.LogE("ShoppingCartActivity--getShoppingCart--onNext:" + gson.toJson(obj));
                        if(obj.getState() == 1){
                            list.addAll(obj.getInfo().getData());
                            if(list.size() == 0){
                                rl_list_is_none.setVisibility(View.VISIBLE);
                            }else{
                                rl_list_is_none.setVisibility(View.INVISIBLE);
                            }
                            adapter.notifyDataSetChanged();
                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }

                    }
                });

    }

    /**
     * 更改商品数量
     * @param desparms
     */
    public void updateShoppingCartProductNum(String desparms) {
        api.updateShoppingCartProductNum(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UpdateShopCartNumBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                    }

                    @Override
                    public void onNext(UpdateShopCartNumBean obj) {
                        LogUtils.LogE("ShoppingCartActivity--updateShoppingCartProductNum--onNext:" + gson.toJson(obj));
                        if(obj.getState() == 1){
                            list.get(currentSelectPosition).setNum(obj.getInfo().getData());
                            adapter.notifyDataSetChanged();
                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }

                    }
                });

    }


    public void delShoppingCart(String desparms) {
        api.delShoppingCart(desparms)
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
                        LogUtils.LogE("ShoppingCartActivity--updateShoppingCartProductNum--onNext:" + gson.toJson(obj));
                        if(obj.getState() == 1){
                            if(list.size() == 0){
                                rl_list_is_none.setVisibility(View.VISIBLE);
                            }else{
                                rl_list_is_none.setVisibility(View.GONE);
                            }
                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }

                    }
                });

    }

    public String getSelectFaverateId(){

        StringBuffer productFavrateId = new StringBuffer();
        for(int i=list.size()-1; i>=0;i--){
            if(list.get(i).isChecked()){
                if(productFavrateId.length() != 0){
                    productFavrateId.append(",");
                }
                productFavrateId.append(list.get(i).getFavorite_id());
            }
        }
        return productFavrateId.toString();
    }


    public class MyCommonRecyclerViewAdapter extends CommonRecyclerViewAdapter<CartProduct>{

        ShoppingCartActivity activity;

        /**
         * 获取当前选中的项数
         * @return
         */
        public int getSelectCount(){
            int selectCount = 0;
            for(CartProduct shoppingCartBean:data){
                if(shoppingCartBean.isChecked()){
                    selectCount++;
                }
            }
            return selectCount;
        }

        /**
         * 获取当前选中的总金额
         * @return
         */
        public double getSelectAmount() {
            double selectAmount = 0;
            for(CartProduct shoppingCartBean:data){
                if(shoppingCartBean.isChecked()){
                    selectAmount = selectAmount + shoppingCartBean.getNum()*shoppingCartBean.getProduct_price();
                }
            }
            return selectAmount;
        }

        /**
         * 构造函数
         *
         * @param context 上下文
         * @param data    显示的数据
         */
        public MyCommonRecyclerViewAdapter(ShoppingCartActivity context, List<CartProduct> data) {
            super(context, data);
            this.activity = context;
        }

        /**
         * 设置全选和全不选
         * @param checkState true:  全选   false:全不选
         */
        public void setListDataCheckState(boolean checkState){
            for (CartProduct shoppingCartBean:list){
                shoppingCartBean.setChecked(checkState);
            }
            notifyDataSetChanged();
        }

        public void deleteSelectItem(){
            StringBuffer productFavrateId = new StringBuffer();
            for(int i=data.size()-1; i>=0;i--){
                if(list.get(i).isChecked()){
                    if(productFavrateId.length() != 0){
                        productFavrateId.append(",");
                    }
                    productFavrateId.append(data.get(i).getFavorite_id());
                    list.remove(i);
                }
            }
            notifyDataSetChanged();
            DelShoppingCartParm parm = new DelShoppingCartParm();
            parm.setFavoriteIdsStr(productFavrateId.toString());
            String strDes = DESHelper.encrypt(gson.toJson(parm));
            delShoppingCart(strDes);
        }


        @Override
        public void convert(CommonRecyclerViewHolder h, final CartProduct entity, final int position) {
            final CheckBox checkBox = h.getView(R.id.cb_choice);
            checkBox.setChecked(entity.isChecked());
            TextView tv_product_name = h.getView(R.id.tv_product_name);
            tv_product_name.setText(entity.getProduct_name());
            TextView tv_price = h.getView(R.id.tv_price);
            tv_price.setText(entity.getProduct_price() + "");
            ImageView iv_cart_product_img = h.getView(R.id.iv_cart_product_img);
            Glide.with(ShoppingCartActivity.this).load(entity.getProduct_picture().get(0).getUrl()).apply(new RequestOptions()).into(iv_cart_product_img);

            AmountView amountView = h.getView(R.id.av_count);


            amountView.setAmount(entity.getNum());

            amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
                @Override
                public void onAmountChange(View view, int amount) {
                    if(entity.getNum() > amount){
                        entity.setNum(amount);
                        upParm.setType(1);
                        upParm.setProductId(entity.getProduct_id());
                        String strDes = DESHelper.encrypt(gson.toJson(upParm));
                        currentSelectPosition = position;
                        updateShoppingCartProductNum(strDes);


                    }else if(entity.getNum() < amount) {
                        entity.setNum(amount);
                        upParm.setType(0);
                        upParm.setProductId(entity.getProduct_id());
                        String strDes = DESHelper.encrypt(gson.toJson(upParm));
                        updateShoppingCartProductNum(strDes);
                    }

                    if (checkBox.isChecked()){
                        activity.initTotalBottomState(getSelectCount(), getSelectAmount());
                    }
                    entity.setNum(amount);
                }
            });
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    entity.setChecked(b);
                    activity.initTotalBottomState(getSelectCount(), getSelectAmount());
                }
            });
        }

        @Override
        public int getLayoutViewId(int viewType) {
            return R.layout.item_shopping_cart;
        }

    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this,true);
    }
}
