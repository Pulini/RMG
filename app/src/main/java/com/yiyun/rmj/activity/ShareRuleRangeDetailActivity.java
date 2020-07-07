package com.yiyun.rmj.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.utils.StatusBarUtil;

public class ShareRuleRangeDetailActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_share_rule_range_detail;
    }

    @Override
    protected void initView() {
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText("战绩详情");

        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                overridePendingTransition(R.anim.activity_leftclick_in,R.anim.activity_leftclick_out);
            }
        });

        TextView tv_add_friend_num = findViewById(R.id.tv_add_friend_num);
        TextView tv_record_ranking = findViewById(R.id.tv_record_ranking);
        TextView tv_reword_collection = findViewById(R.id.tv_reword_collection);

        Intent intent = getIntent();
        int rewardCount = intent.getIntExtra("rewardCount",0);
        String rowNum = intent.getStringExtra("rowNum");
        int invite = intent.getIntExtra("invite",0);

        tv_add_friend_num.setText("" + rewardCount);
        tv_record_ranking.setText("" + rowNum);
        tv_reword_collection.setText("" + invite);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this,true);
    }
}
