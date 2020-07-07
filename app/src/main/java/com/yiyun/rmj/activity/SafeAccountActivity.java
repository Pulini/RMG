package com.yiyun.rmj.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.utils.SpfUtils;
import com.yiyun.rmj.utils.StatusBarUtil;

//账户与安全
public class SafeAccountActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;

            case R.id.rl_settingpassword:
                //设置修改密码
                Intent modifyIntent = new Intent(SafeAccountActivity.this, ModifyPasswordActivity.class);
                startActivity(modifyIntent);
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_safe_account;
    }

    public void initTitleView(){
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.safe_account));

    }

    @Override
    protected void initView() {
        initTitleView();

        RelativeLayout rl_settingpassword = (RelativeLayout) findViewById(R.id.rl_settingpassword);
        rl_settingpassword.setOnClickListener(this);

        TextView tv_phone = findViewById(R.id.tv_phone);
        String account = SpfUtils.getSpfUtils(this).getAccount();

        if(account.length() > 7){
            String strStart = account.substring(0,3);
            String strEnd = account.substring(account.length() -4);
            account = strStart + "****" + strEnd;
        }

        tv_phone.setText(account);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this,true);
    }
}
