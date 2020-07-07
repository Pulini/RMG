package com.yiyun.rmj.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.utils.StatusBarUtil;

//关于我们
public class AboutUsActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_us;
    }

    public void initTitleView(){
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.about_us));

        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        TextView tv_version = findViewById(R.id.tv_version);
        tv_version.setText("V" + getVersionName());
    }

    @Override
    protected void initView() {
        initTitleView();
    }

    @Override
    protected void initData() {

    }

    public String getVersionName() {
        PackageManager manager = AboutUsActivity.this.getPackageManager();
        String versionName = "";
        try {
            PackageInfo info = manager.getPackageInfo(AboutUsActivity.this.getPackageName(), 0);
            versionName = "" + info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionName;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this,true);
    }
}
