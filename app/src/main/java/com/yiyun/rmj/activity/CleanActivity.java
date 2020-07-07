package com.yiyun.rmj.activity;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.view.WheelView;

import java.util.ArrayList;

public class CleanActivity extends BaseActivity {

    private ArrayList<String> list;
    private int time = 30;
    private TextView tv_time;
    private WheelView wheelView;
    private Button btn_clean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_clean;
    }

    @Override
    protected void initView() {
        wheelView = (WheelView) findViewById(R.id.wheel);
        tv_time = (TextView) findViewById(R.id.tv_time);
        list = new ArrayList<>();
        for (int i = 20; i < 61; i++) {
            list.add(i + "");
        }
        btn_clean = (Button) findViewById(R.id.btn_clean);

        wheelView.lists(list).select(30).showCount(5).fontSize(35)
                .listener(new WheelView.OnWheelViewItemSelectListener() {
                    @Override
                    public void onItemSelect(int index) {
                        time = Integer.parseInt(list.get(index));
                    }
                });
        btn_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_time.setText(time + "");
                tv_time.setVisibility(View.VISIBLE);
                wheelView.setVisibility(View.GONE);
                btn_clean.setText("取消");
                int color = Color.parseColor("#a9a9a9 ");
                btn_clean.setBackgroundColor(color);

                //跳转到倒计时页面


            }
        });
    }

    @Override
    protected void initData() {

    }
}
