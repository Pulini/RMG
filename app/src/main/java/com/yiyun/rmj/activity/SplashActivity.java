package com.yiyun.rmj.activity;

import android.content.Intent;
import android.os.Handler;

import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.utils.SpfUtils;
import com.yiyun.rmj.utils.StatusBarUtil;

public class SplashActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SpfUtils.getSpfUtils(SplashActivity.this).setIsNeedUpdate(true);
//                String token = SpfUtils.getSpfUtils(SplashActivity.this).getToken();
//                if(TextUtils.isEmpty(token)){
//                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                    SplashActivity.this.startActivity(intent);
//                    finish();
//                    overridePendingTransition(R.anim.activity_rightclick_in,R.anim.activity_rightclick_out);
//                }else{
//                    Intent homeIntent = new Intent(SplashActivity.this, HomeActivity.class);
//                    SplashActivity.this.startActivity(homeIntent);
//                    finish();
//                    overridePendingTransition(R.anim.activity_rightclick_in,R.anim.activity_rightclick_out);
//                }

                Intent homeIntent = new Intent(SplashActivity.this, NewHomeActivity.class);
                SplashActivity.this.startActivity(homeIntent);
                finish();
                overridePendingTransition(R.anim.activity_rightclick_in, R.anim.activity_rightclick_out);

            }
        }, 3000);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this, true);
    }
}
