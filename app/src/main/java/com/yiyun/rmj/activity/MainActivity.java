package com.yiyun.rmj.activity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.view.WheelView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private TextView tv_text;
    private TextView tv_time;
    private int time = 30;
    private List<String> lists;
    private WheelView wheelView;
    private Button btn_clean;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_clean;
    }

    @Override
    protected void initView() {
        wheelView = (WheelView) findViewById(R.id.wheel);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_text = (TextView) findViewById(R.id.tv_text);
        btn_clean = (Button) findViewById(R.id.btn_clean);
        btn_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_time.setVisibility(View.VISIBLE);
                tv_time.setText(time + "");
                tv_text.setText("剩余时间");
                wheelView.setVisibility(View.INVISIBLE);
              //  btn_clean.setBackgroundResource(R.mipmap.button_quxiao);
                btn_clean.setText("取消");
            }
        });

        lists = new ArrayList<>();
        for (int i = 1; i < 61; i++) {
            lists.add(i + "");
        }

        wheelView.lists(lists).fontSize(35).showCount(5).select(0);

        wheelView.lists(lists).select(30).fontSize(30).showCount(5).listener(new WheelView.OnWheelViewItemSelectListener() {
            @Override
            public void onItemSelect(int index) {
                Log.e("syq", "index=" + index + "");
                time = Integer.parseInt(lists.get(index));
            }
        });
    }

    @Override
    protected void initData() {

    }
}
