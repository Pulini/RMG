package com.yiyun.rmj.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;

public class ShareActivity extends BaseActivity implements View.OnClickListener {
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.iv_back_white:
                finish();
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_share;
    }

    public void initTitleView(){
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back_white);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setTextColor(this.getResources().getColor(R.color.white));
        tv_title.setText(getString(R.string.share));

    }

    @Override
    protected void initView() {
        initTitleView();

    }

    @Override
    protected void initData() {

    }
}
