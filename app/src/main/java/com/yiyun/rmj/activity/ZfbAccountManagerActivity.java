package com.yiyun.rmj.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yiyun.rmj.R;
import com.yiyun.rmj.adapter.CommonRecyclerViewAdapter;
import com.yiyun.rmj.adapter.CommonRecyclerViewHolder;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.apibase.BaseBean;
import com.yiyun.rmj.bean.apibase.BaseParm;
import com.yiyun.rmj.bean.apibean.ZfbAccountBean;
import com.yiyun.rmj.bean.apiparm.AlipayParm;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.utils.SpfUtils;
import com.yiyun.rmj.utils.StatusBarUtil;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//选择支付宝页面
public class ZfbAccountManagerActivity extends BaseActivity implements View.OnClickListener {

    RecyclerView rv_zfblist;
    ArrayList<ZfbAccountBean.Account> lists;
    CommonRecyclerViewAdapter adapter;
    private int defaultItemPosition = -1; //默认地址的item
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_add_zfb:
                //添加支付宝
                Intent addZfbIntent = new Intent(ZfbAccountManagerActivity.this, EditAddZfbAccountActivity.class);
                addZfbIntent.putExtra("type",EditAddZfbAccountActivity.TYPE_ADD);
                startActivity(addZfbIntent);
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_zfb_manager;
    }

    public void initTitleView(){
        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.select_zfb_account));
    }

    @Override
    protected void initView() {
        initTitleView();

        Button btn_add_zfb = findViewById(R.id.btn_add_zfb);
        btn_add_zfb.setOnClickListener(this);

        lists = new ArrayList<>();

        rv_zfblist = findViewById(R.id.rv_zfblist);
        rv_zfblist.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommonRecyclerViewAdapter<ZfbAccountBean.Account>(this,lists) {
            @Override
            public void convert(CommonRecyclerViewHolder h, final ZfbAccountBean.Account entity, final int position) {

                TextView tv_name = h.getView(R.id.tv_name);
                TextView tv_account = h.getView(R.id.tv_account);
                CheckBox cb_choice = h.getView(R.id.cb_choice);
                TextView tv_edit = h.getView(R.id.tv_edit);
                TextView tv_delete = h.getView(R.id.tv_delete);

                tv_name.setText(entity.getName());
                tv_account.setText(entity.getAlipayNum());
                if(entity.getIsDefault() == 1){
                    cb_choice.setChecked(true);
                    defaultItemPosition = position;
                }else{
                    cb_choice.setChecked(false);
                }

                cb_choice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if(b){
                            lists.get(position).setIsDefault(1);
                            AlipayParm alipayParm = new AlipayParm();
                            alipayParm.setAlipayId(entity.getAlipayId());
                            String parmDes = DESHelper.encrypt(gson.toJson(alipayParm));
                            defaultAliPayNum(parmDes);
                        }
                    }
                });

                tv_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //修改支付宝
                        Intent addZfbIntent = new Intent(ZfbAccountManagerActivity.this, EditAddZfbAccountActivity.class);
                        addZfbIntent.putExtra("type",EditAddZfbAccountActivity.TYPE_EDIT);
                        addZfbIntent.putExtra("account",entity.getAlipayNum());
                        addZfbIntent.putExtra("name",entity.getName());
                        addZfbIntent.putExtra("alipayid",entity.getAlipayId());
                        startActivity(addZfbIntent);
                    }
                });

                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlipayParm alipayParm = new AlipayParm();
                        alipayParm.setAlipayId(entity.getAlipayId());
                        String parmDes = DESHelper.encrypt(gson.toJson(alipayParm));
                        delAliPayNum(parmDes);
                    }
                });


            }

            @Override
            public int getLayoutViewId(int viewType) {
                return R.layout.item_zfb_account;
            }

        };

        adapter.setOnRecyclerViewItemClickListener(new CommonRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                ZfbAccountBean.Account account = lists.get(position);
                Intent intent = new Intent();
                intent.putExtra("alipayId",account.getAlipayId());
                intent.putExtra("name",account.getName());
                intent.putExtra("alipayNum",account.getAlipayNum());
                intent.putExtra("isDefault",account.getIsDefault());
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        rv_zfblist.setAdapter(adapter);
    }

    @Override
    protected void initData() {

    }

    public void updateSavedDefaultAddressId(){
        for(int i=0; i < lists.size(); i++){
            ZfbAccountBean.Account zfbAccount = lists.get(i);
            if(zfbAccount.getIsDefault() == 1){
                LogUtils.LogE("start to set alipayId");
                SpfUtils.getSpfUtils(ZfbAccountManagerActivity.this).setDefaultAlipayId(zfbAccount.getAlipayId());
                return;
            }
        }
        LogUtils.LogE("not alipayId");
        SpfUtils.getSpfUtils(ZfbAccountManagerActivity.this).setDefaultAlipayId(-1);
    }

    @Override
    public void onResume() {
        super.onResume();
        String parm = gson.toJson(new BaseParm());
        LogUtils.LogE("parm:" + parm);
        String parmDes = DESHelper.encrypt(parm);
        aliPayNumList(parmDes);
    }

    /**
     * 设置为默认支付宝
     * @param desparms
     */
    private void defaultAliPayNum(String desparms){


        api.defaultAliPayNum(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                        LogUtils.LogE("error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean obj) {
                        LogUtils.LogE(ZfbAccountManagerActivity.class.getName() + "--defaultAliPayNum:" + gson.toJson(obj));
                        if(obj.getState() == 1){
//                            String parm = gson.toJson(new BaseParm());
//                            String parmDes = DESHelper.encrypt(parm);
//                            aliPayNumList(parmDes);
                            if(defaultItemPosition != -1){
                                lists.get(defaultItemPosition).setIsDefault(0);
                                adapter.notifyDataSetChanged();
                            }
                            updateSavedDefaultAddressId();

                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }

                    }
                });

    }

    /**
     * 删除支付宝账号
     * @param desparms
     */
    private void delAliPayNum(String desparms){


        api.delAliPayNum(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                        LogUtils.LogE("error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean obj) {
                        LogUtils.LogE(ZfbAccountManagerActivity.class.getName() + "--delAliPayNum:" + gson.toJson(obj));
                        showConnectError(obj.getInfo().getMessage());
                        if(obj.getState() == 1){
                            String parm = gson.toJson(new BaseParm());
                            String parmDes = DESHelper.encrypt(parm);
                            aliPayNumList(parmDes);
                        }else if(obj.getState() == -1){
                            startlogin();
                        }

                    }
                });

    }

    private void aliPayNumList(String desparms){


        api.AliPayNumList(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ZfbAccountBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                        LogUtils.LogE("error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(ZfbAccountBean obj) {
                        LogUtils.LogE(ZfbAccountManagerActivity.class.getName() + "--aliPayNumList:" + gson.toJson(obj));

                        if(obj.getState() == 1){
                            lists.clear();
                            lists.addAll(obj.getInfo().getData());
                            adapter.notifyDataSetChanged();
                            updateSavedDefaultAddressId();
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
