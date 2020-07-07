package com.yiyun.rmj.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.dialog.SignSuccessDialog;
import com.yiyun.rmj.view.MySignView;

//签到界面
public class SignActivity extends BaseActivity implements View.OnClickListener {


    MySignView msv_sign_calender;
    public boolean todaySigned = false;
    Button btn_sign;


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back_white:
                finish();
                break;

            case R.id.tv_title_next:
                //签到说明按钮
                Intent descriptionIntent = new Intent(SignActivity.this,SignDescriptionActivity.class);
                startActivity(descriptionIntent);
                break;

            case R.id.btn_sign:
                if(msv_sign_calender.signToday()){
                    showSignDialog();
                    todaySigned = true;
                    initSignButtonState();
                }else{
                    Toast.makeText(this,"今日已签到",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    public void showSignDialog(){
        SignSuccessDialog dialog = new SignSuccessDialog(this);
        dialog.show();
    }

    public void initSignButtonState(){
        if(todaySigned){
            btn_sign.setText(getString(R.string.signed));
        }else{
            btn_sign.setText(getString(R.string.sign));
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign;
    }

    public void initTitleView(){
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back_white);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setTextColor(getResources().getColor(R.color.white));
        tv_title.setText(getString(R.string.daily_sign));

        TextView tv_description = (TextView) findViewById(R.id.tv_title_next);
        tv_description.setTextColor(getResources().getColor(R.color.white));
        tv_description.setVisibility(View.VISIBLE);
        tv_description.setText(getString(R.string.sign_description));
        tv_description.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        initTitleView();

        btn_sign = (Button) findViewById(R.id.btn_sign);
        btn_sign.setOnClickListener(this);
        initSignButtonState();

        msv_sign_calender = (MySignView) findViewById(R.id.msv_sign_calender);
    }

    @Override
    protected void initData() {

    }
}
