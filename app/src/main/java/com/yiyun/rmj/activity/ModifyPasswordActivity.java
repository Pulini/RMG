package com.yiyun.rmj.activity;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjq.toast.ToastUtils;
import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.apibase.BaseBean;
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

//修改密码
public class ModifyPasswordActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title_next;
    private TextView tv_complete;
    private RelativeLayout rl_check_code;
    private LinearLayout rl_modify_password;
    private EditText et_one_password;
    private EditText et_two_password;
    private EditText et_input_code;
    private TextView tv_get_code;
    private int currentStep = 1;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_get_code:
                //获取验证码
                startTimeCount();
                GetCodeParm getCodeParm = new GetCodeParm();
                getCodeParm.setType(3);
                getCodeParm.setPhone(SpfUtils.getSpfUtils(this).getAccount());
                String parmDes = DESHelper.encrypt(gson.toJson(getCodeParm));
                getCode(parmDes);
                break;

            case R.id.tv_title_next:
                //下一步
                checkCode();
                break;


            case R.id.title_complete:
                //完成
                checkPassword();
                break;
        }

    }

    public void startTimeCount(){
        tv_get_code.setClickable(false);
        CountDownTimer downTimer  = new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long l) {
                tv_get_code.setText("" + (int)(l/1000) + "秒");
            }

            @Override
            public void onFinish() {
                tv_get_code.setText("重发");
                tv_get_code.setClickable(true);
            }
        };
        downTimer.start();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_password;
    }

    public void initTitleView(){
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.modify_password));

        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        tv_title_next = (TextView) findViewById(R.id.tv_title_next);
        tv_title_next.setVisibility(View.VISIBLE);
        tv_title_next.setOnClickListener(this);

        tv_complete = (TextView) findViewById(R.id.title_complete);
        tv_complete.setVisibility(View.GONE);
        tv_complete.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        initTitleView();
        tv_get_code = (TextView) findViewById(R.id.tv_get_code);
        tv_get_code.setOnClickListener(this);

        rl_check_code = (RelativeLayout) findViewById(R.id.rl_check_code);
        rl_modify_password = (LinearLayout) findViewById(R.id.rl_modify_password);

        et_one_password = (EditText) findViewById(R.id.et_one_password);
        et_two_password = (EditText) findViewById(R.id.et_two_password);

        et_input_code = findViewById(R.id.et_input_code);

        TextView tv_phone = findViewById(R.id.tv_phone_number);
        tv_phone.setText(SpfUtils.getSpfUtils(this).getAccount());

        gotoCheckCode();

    }

    @Override
    protected void initData() {

    }

    public void checkPassword(){
        if(checkInput()){
            RegistParm parm = new RegistParm();
            parm.setPassword(et_one_password.getText().toString().trim());
            parm.setPasswordAgain(et_two_password.getText().toString());
            String parmStr = gson.toJson(parm);
            LogUtils.LogE("ModifyPasswordActivity--CheckPassword:" + parmStr);
            String parmDes = DESHelper.encrypt(parmStr);
            updatePassword(parmDes);
        }
    }

    public boolean checkInput(){
        if(TextUtils.isEmpty(et_one_password.getText().toString().trim())){
            ToastUtils.show("密码不能为空");
            return false;
        }

        if(TextUtils.isEmpty(et_two_password.getText().toString().trim())){
            ToastUtils.show("验证密码不能为空");
            return false;
        }
        return true;
    }

    public void checkCode(){
        if(TextUtils.isEmpty(et_input_code.getText().toString().trim())){
            ToastUtils.show("验证码不能为空");
            return;
        }
        RegistParm registParm = new RegistParm();
        registParm.setPhone(SpfUtils.getSpfUtils(this).getAccount());
        registParm.setCode(et_input_code.getText().toString().trim());
        String parmDes = DESHelper.encrypt(gson.toJson(registParm));
        updatePasswordCodeContrast(parmDes);

    }

    //显示修改密码页面
    public void gotoPasswordModify() {
        tv_title_next.setVisibility(View.GONE);
        tv_complete.setVisibility(View.VISIBLE);
        rl_modify_password.setVisibility(View.VISIBLE);
        rl_check_code.setVisibility(View.GONE);
    }

    //显示获取验证码页面
    public void gotoCheckCode() {
        tv_title_next.setVisibility(View.VISIBLE);
        tv_complete.setVisibility(View.GONE);
        rl_modify_password.setVisibility(View.GONE);
        rl_check_code.setVisibility(View.VISIBLE);
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
    public void updatePasswordCodeContrast(String desparms) {
        showProgressDialog("正在校验");
        api.updatePasswordCodeContrast(desparms)
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
                            gotoPasswordModify();
                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }

                    }
                });

    }



    public void updatePassword(String desparms) {
        showProgressDialog("正在提交");
        api.updatePassword(desparms)
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
