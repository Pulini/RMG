package com.yiyun.rmj.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;

public class SearchFailActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_fail;
    }

    @Override
    protected void initView() {
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        ImageView back = (ImageView) findViewById(R.id.iv_back);
        back.setImageResource(R.mipmap.quxiao);
        back.setScaleType(ImageView.ScaleType.CENTER);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName(SearchFailActivity.this, "com.yiyun.rmj.activity.UnConactActivity");
                SearchFailActivity.this.startActivity(intent);
//                SearchFailActivity.this.overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
                finish();

            }
        });
        tv_title.setText("搜索失败");

        Button btn_search_again = (Button) findViewById(R.id.btn_search_again);
        btn_search_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName(SearchFailActivity.this, "com.yiyun.rmj.activity.SearchIngActivity");
                SearchFailActivity.this.startActivity(intent);
//                SearchFailActivity.this.overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
                finish();
            }
        });
    }

    @Override
    protected void initData() {

    }
}
