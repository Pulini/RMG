package com.yiyun.rmj.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;

public class ShareRegisterActivity extends BaseActivity implements View.OnClickListener{

    private Button btn_getcode;

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.iv_back_white:
                finish();
                break;
            case R.id.btn_share_registnow:
                Intent shareIntent = new Intent(ShareRegisterActivity.this, ShareActivity.class);
                startActivity(shareIntent);
                break;
            case R.id.btn_getcode:
                startTimeCount();
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_share_register;
    }

    public void initTitleView(){
        ImageView iv_back = findViewById(R.id.iv_back_white);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);



    }

    @Override
    protected void initView() {
        initTitleView();

        Button btn_share_registnow = findViewById(R.id.btn_share_registnow);
        btn_share_registnow.setOnClickListener(this);

        btn_getcode = findViewById(R.id.btn_getcode);
        btn_getcode.setOnClickListener(this);
    }

    @Override
    protected void initData() {

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
                btn_getcode.setText("重新发送");
                btn_getcode.setClickable(true);
            }
        };
        downTimer.start();
    }
}
