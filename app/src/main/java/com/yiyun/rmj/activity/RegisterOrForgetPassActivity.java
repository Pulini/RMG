package com.yiyun.rmj.activity;

import android.content.Intent;
import android.os.CountDownTimer;
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
import com.yiyun.rmj.bean.apibean.BondRegistBean;
import com.yiyun.rmj.bean.apibean.GetCodeBean;
import com.yiyun.rmj.bean.apiparm.GetCodeParm;
import com.yiyun.rmj.bean.apiparm.RegistParm;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.utils.SpfUtils;
import com.yiyun.rmj.utils.StatusBarUtil;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterOrForgetPassActivity extends BaseActivity implements View.OnClickListener{

    public static final int TYPE_REGISTER = 1;  //注册
    public static final int TYPE_FORGET = 2;   //忘记密码
    public static final int TYPE_CHANGEPASSWORD = 3; //修改密码
    public static final int TYPE_BINDPHONE = 4;//绑定手机
    private int type;
    private int userType;
    private int currentLoginType;


    private Button btn_getcode;
    private EditText et_phone; //手机号输入框
    private EditText et_check_code; //验证码输入框
    private EditText et_password; //密码输入框
    private EditText et_confirm_password; //再次输入密码输入框
    private boolean isRelogin;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_forget;
    }


    private void initTitleView(){
        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

    }


    @Override
    protected void initView() {

        initTitleView();
        Intent intent = getIntent();
        type = intent.getIntExtra("type",TYPE_REGISTER);
        currentLoginType = intent.getIntExtra("loginType", 0);
        isRelogin = intent.getBooleanExtra("relogin",false);

        Button btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);

        btn_getcode = findViewById(R.id.btn_getcode);
        btn_getcode.setOnClickListener(this);

          et_phone = findViewById(R.id.et_phone);
          et_check_code = findViewById(R.id.et_check_code);
          et_password = findViewById(R.id.et_password);
          et_confirm_password = findViewById(R.id.et_confirm_password);

        ImageView iv_weixin = findViewById(R.id.iv_weixin);
        ImageView iv_qq = findViewById(R.id.iv_qq);

        iv_weixin.setOnClickListener(this);
        iv_qq.setOnClickListener(this);

        TextView tv_action_type = findViewById(R.id.tv_action_type);
        if (type == TYPE_FORGET) {
            tv_action_type.setText(getString(R.string.forget));
            btn_register.setText(getString(R.string.sure));
        }else if(type == TYPE_BINDPHONE){
            tv_action_type.setText("绑定手机");
            btn_register.setText("绑定");
        }else{
            tv_action_type.setText("注册");
            btn_register.setText("注册");
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                Intent intentBack = new Intent();
                intentBack.setClassName(RegisterOrForgetPassActivity.this, "com.yiyun.rmj.activity.LoginActivity");
                intentBack.putExtra("relogin", isRelogin);
                RegisterOrForgetPassActivity.this.startActivity(intentBack);

                finish();
                break;
            case R.id.btn_register:

                if(checkRegister()){

                    RegistParm param = new RegistParm();
                    param.setCode(et_check_code.getText().toString().trim());  // 验证码
                    param.setPassword(et_password.getText().toString().trim()); //密码
                    param.setPasswordAgain(et_confirm_password.getText().toString().trim()); //确认密码
                    param.setType(currentLoginType);
                    param.setOpenid(SpfUtils.getSpfUtils(RegisterOrForgetPassActivity.this).getOpenId());
                    param.setPhone(et_phone.getText().toString().trim()); //手机号
                    String gsonStr = gson.toJson(param);
                    LogUtils.LogE("Register--register-parm:" + gsonStr);
                    String enpcypt = DESHelper.encrypt(gsonStr);
                    LogUtils.LogE("Register--register-enpcyptparm:" + gsonStr);
                    if(type == TYPE_REGISTER || type == TYPE_BINDPHONE){
                        rigister(enpcypt);
                    }else if(type == TYPE_FORGET){
                        forgotPassword(enpcypt);
                    }
                }
                break;

            case R.id.btn_getcode:
                String phone = et_phone.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    ToastUtils.show("手机号不能为空");
                    return;
                }
                startTimeCount();

                GetCodeParm getCodeParm = new GetCodeParm();
                getCodeParm.setPhone(phone);
                getCodeParm.setType(type);
                String parm = gson.toJson(getCodeParm);
                LogUtils.LogE("Register--getCode:" + parm);
                String enpcypt = DESHelper.encrypt(parm);
                getCode(enpcypt);

                break;
            case R.id.iv_weixin:
//                Toast.makeText(this,"点击微信",Toast.LENGTH_SHORT).show();
//                UMShareAPI.get(RegisterOrForgetPassActivity.this).getPlatformInfo(RegisterOrForgetPassActivity.this, SHARE_MEDIA.WEIXIN, authListener);
                break;

            case R.id.iv_qq:
//                Toast.makeText(this,"点击QQ",Toast.LENGTH_SHORT).show();
//                UMShareAPI.get(RegisterOrForgetPassActivity.this).getPlatformInfo(RegisterOrForgetPassActivity.this, SHARE_MEDIA.QQ, authListener);
                break;

        }
    }

    public void startTimeCount(){
        btn_getcode.setClickable(false);
        CountDownTimer downTimer  = new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long l) {
                btn_getcode.setText("" + (int)(l/1000) + "秒");
            }

            @Override
            public void onFinish() {
                btn_getcode.setText("重发");
                btn_getcode.setClickable(true);
            }
        };
        downTimer.start();
    }

    public boolean checkRegister(){

        if(TextUtils.isEmpty(et_phone.getText().toString().trim())){
            ToastUtils.show("手机号不能为空");
            return false;
        }

        if(TextUtils.isEmpty(et_check_code.getText().toString().trim())){
            ToastUtils.show("验证码不能为空");
            return false;
        }

        if(userType != 1){
            //绑定手机不需要进行密码校验
            if(TextUtils.isEmpty(et_password.getText().toString().trim())){
                ToastUtils.show("密码不能为空");
                return false;
            }

            if(TextUtils.isEmpty(et_confirm_password.getText().toString().trim())){
                ToastUtils.show("确认密码不能为空");
                return false;
            }

            if(!et_confirm_password.getText().toString().trim().equals(et_password.getText().toString().trim())){
                ToastUtils.show("两次输入的密码不一致，请重新输入");
                return false;
            }
        }

        return true;
    }


    public void rigister(String desparms) {
        showProgressDialog("正在注册");
        api.register(desparms)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<BondRegistBean>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    dismissProgressDialog();
                    showConnectError();
                }

                @Override
                public void onNext(BondRegistBean obj) {
                    dismissProgressDialog();
                    showConnectError(obj.getInfo().getMessage());
                    if(obj.getState() == 1){
                        if(type == TYPE_BINDPHONE){
                            SpfUtils.getSpfUtils(RegisterOrForgetPassActivity.this).setToken(obj.getInfo().getData().getToken());
                            SpfUtils.getSpfUtils(RegisterOrForgetPassActivity.this).setAccount(obj.getInfo().getData().getPhone());
                            if(isRelogin){
                                finish();
                            }else{
                                Intent intent = new Intent(RegisterOrForgetPassActivity.this, NewHomeActivity.class);
                                RegisterOrForgetPassActivity.this.startActivity(intent);
                                finish();
                            }
                        }else{
                            ToastUtils.show(obj.getInfo().getMessage());
                        }
                    }
                }
            });

    }

    public void forgotPassword(String desparms) {
        showProgressDialog("正在提交");
        api.forgotPassword(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        showConnectError();
                    }

                    @Override
                    public void onNext(BaseBean obj) {
                        dismissProgressDialog();
                        if(obj.getState() == 1){
                            if(isRelogin){
                                appManager.finishActivity(LoginActivity.class);
                                finish();
                            }else{
                                Intent intent = new Intent(RegisterOrForgetPassActivity.this, NewHomeActivity.class);
                                startActivity(intent);
                                appManager.finishActivity(LoginActivity.class);
                                finish();
                            }

                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }

                    }
                });

    }


    /**
     * 获取验证码
     * @param desparms
     */
    public void getCode(String desparms) {
        api.getCode(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetCodeBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                    }

                    @Override
                    public void onNext(GetCodeBean obj) {
                        if(obj.getState() == 1){
                            if(obj.getInfo().getData() != null && obj.getInfo().getData().getUserType() == 1){
                                userType = obj.getInfo().getData().getUserType();
                                et_password.setVisibility(View.GONE);
                                et_confirm_password.setVisibility(View.GONE);
                            }else{
                                et_password.setVisibility(View.VISIBLE);
                                et_confirm_password.setVisibility(View.VISIBLE);
                            }
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
