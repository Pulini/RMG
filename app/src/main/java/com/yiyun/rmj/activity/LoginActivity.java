package com.yiyun.rmj.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.helpdesk.callback.Callback;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.apibase.BaseBean;
import com.yiyun.rmj.bean.apiparm.LoginParm;
import com.yiyun.rmj.setting.UrlContact;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.utils.SpfUtils;
import com.yiyun.rmj.utils.StatusBarUtil;

import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity implements View.OnClickListener{


    private EditText et_username;
    private EditText et_password;
    private Gson gson;
    private int currentLoginType = 0;//0账号密码  1qq  2vx
    private boolean isRelogin;

    UMAuthListener authListener = new UMAuthListener() {

        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            SpfUtils spf = SpfUtils.getSpfUtils(LoginActivity.this);

            //设置登录类型
            spf.setLoginType(UrlContact.LOGIN_TYPE_WEIXIN);
            //获取用户id
            String uid = data.get("uid");
            spf.setOpenId(uid);

            //获取头像url
            spf.setHeadImageUrl(data.get("iconurl"));
            //设置用户名
            spf.setUserName(data.get("name"));
            //设置token
            String access_token = data.get("access_token");
            spf.setWeichatAccessToken(access_token);

            String str = gson.toJson(data);

            LoginParm loginParm = new LoginParm();
            loginParm.setType(currentLoginType);
            loginParm.setOpenid(uid);
            String parmStr = gson.toJson(loginParm);
            String parmEnpcept = DESHelper.encrypt(parmStr);
            login(parmEnpcept);
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {

            Toast.makeText(LoginActivity.this, "失败：" + t.getMessage(),Toast.LENGTH_LONG).show();
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(LoginActivity.this, "取消了", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }




    @Override
    protected void initView() {
        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        isRelogin = getIntent().getBooleanExtra("relogin",false);

        //注册按钮
        TextView tv_register = findViewById(R.id.tv_register_now);
        tv_register.setOnClickListener(this);

        //忘记密码按钮
        TextView tv_fogetps = findViewById(R.id.tv_foget_password);
        tv_fogetps.setOnClickListener(this);

        //用户名。密码输入框
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);

        //微信按钮
        ImageView iv_weixin = findViewById(R.id.iv_weixin);
        iv_weixin.setOnClickListener(this);

        //QQ按钮
        ImageView iv_qq = findViewById(R.id.iv_qq);
        iv_qq.setOnClickListener(this);

        TextView tv_welcome_txt = findViewById(R.id.tv_welcome_txt);
        tv_welcome_txt.setText(getString(R.string.welcome_word, getString(R.string.app_name)));

        gson = new Gson();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_login:

                //测试
//                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                LoginActivity.this.startActivity(intent);
//                finish();

                currentLoginType = 0;
                if(checkLoginInput()){
                    LoginParm loginParm = new LoginParm();
                    loginParm.setPhone(et_username.getText().toString().trim());
                    loginParm.setPassword(et_password.getText().toString().trim());
                    loginParm.setType(currentLoginType);
                    String parmStr = gson.toJson(loginParm);
                    String parmEnpcept = DESHelper.encrypt(parmStr);
                    login(parmEnpcept);
                }
                break;

            case R.id.tv_register_now:
                Intent intentRegister = new Intent();
                intentRegister.setClassName(LoginActivity.this, "com.yiyun.rmj.activity.RegisterOrForgetPassActivity");
                intentRegister.putExtra("type",RegisterOrForgetPassActivity.TYPE_REGISTER);
                intentRegister.putExtra("relogin",isRelogin);
                LoginActivity.this.startActivity(intentRegister);
               // LoginActivity.this.overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
                break;

            case R.id.tv_foget_password:
                Intent intentForget = new Intent();
                intentForget.setClassName(LoginActivity.this, "com.yiyun.rmj.activity.RegisterOrForgetPassActivity");
                intentForget.putExtra("type",RegisterOrForgetPassActivity.TYPE_FORGET);
                intentForget.putExtra("relogin",isRelogin);
                LoginActivity.this.startActivity(intentForget);
                //LoginActivity.this.overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
                break;

            case R.id.iv_weixin:
                currentLoginType = 2;
                Toast.makeText(this,"点击微信",Toast.LENGTH_SHORT).show();
                UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, authListener);
                break;

            case R.id.iv_qq:
                currentLoginType = 1;
                Toast.makeText(this,"点击QQ",Toast.LENGTH_SHORT).show();
                UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, authListener);
            break;
        }
    }

    public boolean checkLoginInput(){
        if(TextUtils.isEmpty(et_username.getText().toString().trim())){
            ToastUtils.show("账号不能为空");
            return false;
        }

        if(TextUtils.isEmpty(et_password.getText().toString().trim())){
            ToastUtils.show("密码不能为空");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    public void login(String desparms) {
        showProgressDialog("正在登录");
        api.login(desparms)
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
                        LogUtils.LogE("login-onNext:" + gson.toJson(obj));
                        if(obj.getState() == 1){
                            if(isRelogin){
                                SpfUtils.getSpfUtils(LoginActivity.this).setToken(obj.getInfo().getData().getToken());
                                SpfUtils.getSpfUtils(LoginActivity.this).setAccount(et_username.getText().toString().trim());
                                ChatClient.getInstance().register( et_username.getText().toString().trim(), et_password.getText().toString().trim(), new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        Log.e("Pan","环信注册成功");
                                        SpfUtils.getSpfUtils(LoginActivity.this).setHXName(et_username.getText().toString().trim());
                                        SpfUtils.getSpfUtils(LoginActivity.this).setHXPwd(et_password.getText().toString().trim());
                                        finish();
                                    }

                                    @Override
                                    public void onError(int code, String error) {
                                        Log.e("Pan","环信注册失败:code="+code+"   error="+error);
                                        SpfUtils.getSpfUtils(LoginActivity.this).setHXName(et_username.getText().toString().trim());
                                        SpfUtils.getSpfUtils(LoginActivity.this).setHXPwd(et_password.getText().toString().trim());
                                        finish();
                                    }

                                    @Override
                                    public void onProgress(int progress, String status) {

                                    }
                                });
                            }else{
                                SpfUtils.getSpfUtils(LoginActivity.this).setToken(obj.getInfo().getData().getToken());
                                SpfUtils.getSpfUtils(LoginActivity.this).setAccount(et_username.getText().toString().trim());

                                ChatClient.getInstance().register( et_username.getText().toString().trim(), et_password.getText().toString().trim(), new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        Log.e("Pan","环信注册成功");
                                        SpfUtils.getSpfUtils(LoginActivity.this).setHXName(et_username.getText().toString().trim());
                                        SpfUtils.getSpfUtils(LoginActivity.this).setHXPwd(et_password.getText().toString().trim());
                                        Intent intent = new Intent(LoginActivity.this, NewHomeActivity.class);
                                        LoginActivity.this.startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onError(int code, String error) {
                                        Log.e("Pan","环信注册失败:code="+code+"   error="+error);
                                        SpfUtils.getSpfUtils(LoginActivity.this).setHXName(et_username.getText().toString().trim());
                                        SpfUtils.getSpfUtils(LoginActivity.this).setHXPwd(et_password.getText().toString().trim());
                                        Intent intent = new Intent(LoginActivity.this, NewHomeActivity.class);
                                        LoginActivity.this.startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onProgress(int progress, String status) {

                                    }
                                });
                            }
                        }else if(obj.getState() == 3){
                            ToastUtils.show(obj.getInfo().getMessage());
                            Intent intent = new Intent(LoginActivity.this, RegisterOrForgetPassActivity.class);
                            intent.putExtra("type",RegisterOrForgetPassActivity.TYPE_BINDPHONE);
                            intent.putExtra("loginType", currentLoginType);
                            intent.putExtra("relogin",isRelogin);
                            startActivity(intent);
                            finish();

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
