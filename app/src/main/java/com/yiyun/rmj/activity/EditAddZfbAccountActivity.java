package com.yiyun.rmj.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjq.toast.ToastUtils;
import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.apibase.BaseBean;
import com.yiyun.rmj.bean.apiparm.ZfbParm;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.utils.StatusBarUtil;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//添加或修改支付宝账号页面
public class EditAddZfbAccountActivity extends BaseActivity implements View.OnClickListener {


    public static final int TYPE_ADD = 0;
    public static final int TYPE_EDIT = 1;
    EditText et_input_name;
    EditText et_input_zfbaccount;
    private int type;
    ZfbParm zfbParm;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                    finish();
                break;

            case R.id.btn_save:
                if(checkInput()){

                    zfbParm.setType(type);
                    zfbParm.setAlipayNum(et_input_zfbaccount.getText().toString().trim());
                    zfbParm.setName(et_input_name.getText().toString().trim());
                    String parmDes = DESHelper.encrypt(gson.toJson(zfbParm));
                    addAliPayNum(parmDes);

                }

                //保存
            break;
        }
    }

    private boolean checkInput(){

        if(TextUtils.isEmpty(et_input_name.getText().toString().trim())){
            ToastUtils.show("姓名不能为空");
            return false;
        }

        if(TextUtils.isEmpty(et_input_zfbaccount.getText().toString().trim())){
            ToastUtils.show("支付宝不能为空");
            return false;
        }

        return true;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_add_zfbaccount;
    }

    public void initTitleView(){
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        if(type == TYPE_ADD){
            tv_title.setText(getString(R.string.select_zfb_add));
        }else if(type == TYPE_EDIT){
            tv_title.setText(getString(R.string.select_zfb_edit));
        }
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        initTitleView();
        et_input_name = (EditText) findViewById(R.id.et_input_name);
        et_input_zfbaccount = (EditText) findViewById(R.id.et_input_zfbaccount);
        Button btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        zfbParm = new ZfbParm();

        if( type == TYPE_ADD){
        }else if( type == TYPE_EDIT){

            zfbParm.setAlipayNum(intent.getStringExtra("account"));
            zfbParm.setName(intent.getStringExtra("name"));
            zfbParm.setAlipayId(intent.getIntExtra("alipayid",0));

            et_input_name.setText(zfbParm.getName());
            et_input_zfbaccount.setText(zfbParm.getAlipayNum());
        }
    }



    @Override
    protected void initData() {

    }

    private void addAliPayNum(String desparms){

        api.addAliPayNum(desparms)
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
                        LogUtils.LogE(EditAddZfbAccountActivity.class.getName() + "--addAliPayNum:" + gson.toJson(obj));
                        showConnectError(obj.getInfo().getMessage());
                        if(obj.getState() == 1){
                            finish();
                        }else if(obj.getState() == -1){
                            startlogin();
                        }

                    }
                });

    }



    @Override
    protected void setStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this,true);
    }
}
