package com.yiyun.rmj.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjq.toast.ToastUtils;
import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.apibase.BaseParm;
import com.yiyun.rmj.bean.apibean.CustomServiceBean;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.LogUtils;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//售前售后客服页面
public class CustomServiceActivity extends BaseActivity implements View.OnClickListener {

    public static final int TYPE_PRE_SERVICE = 1;//售前服务
    public static final int TYPE_AFTER_SERVICE = 2;//售后服务
    public int type;
    private CustomServiceBean.CustomerInfo customerInfo;

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.iv_back_white:
                finish();
                break;

            case R.id.tv_service_weixin:
                String toClipboardWXTxt = "";
                if(type == TYPE_PRE_SERVICE){
                    toClipboardWXTxt = customerInfo.getVx_preSale();
                }else{
                    toClipboardWXTxt = customerInfo.getVx_afterSale();
                }
                copyToClipBoard(toClipboardWXTxt);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String url="weixin://";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    }
                },1000);


                break;

            case R.id.tv_service_qq:
                String toClipboardQQTxt = "";
                if(type == TYPE_PRE_SERVICE){
                    toClipboardQQTxt = customerInfo.getQq_preSale();
                }else{
                    toClipboardQQTxt = customerInfo.getQq_preSale();
                }
                copyToClipBoard(toClipboardQQTxt);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq");
                            startActivity(intent);

                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.show("请检查是否安装QQ");
                        }
                    }
                },1000);


                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_custom_service;
    }

    public void initTitleView(){
        ImageView iv_back = findViewById(R.id.iv_back_white);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setTextColor(this.getResources().getColor(R.color.white));
        if(type == TYPE_AFTER_SERVICE){
            tv_title.setText(getString(R.string.after_service));
        }else if(type == TYPE_PRE_SERVICE){
            tv_title.setText(getString(R.string.pre_service));
        }
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type",TYPE_PRE_SERVICE);
        initTitleView();

        TextView tv_service_weixin = findViewById(R.id.tv_service_weixin);
        tv_service_weixin.setOnClickListener(this);

        TextView tv_service_qq = findViewById(R.id.tv_service_qq);
        tv_service_qq.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        BaseParm parm = new BaseParm();
        String strDes = DESHelper.encrypt(gson.toJson(parm));
        customerService(strDes);
    }

    /**
     * 获取客服信息
     */
    public void customerService(String desparms) {
        api.customerService(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CustomServiceBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                        LogUtils.LogE("error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(CustomServiceBean obj) {
                        LogUtils.LogE("HomeActivity--getRotation:" + obj.getState());
                        if(obj.getState() == 1){
                            customerInfo = obj.getInfo().getData();
                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }

                    }
                });

    }

    public void copyToClipBoard(String text){
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) CustomServiceActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", text);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        ToastUtils.show("已复制到粘贴板");
    }
}

