package com.yiyun.rmj.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;

public class LabelActivity extends BaseActivity {


    private EditText et_label;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_label;
    }

    @Override
    protected void initView() {
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("标签");
        ImageView back = (ImageView) findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getIntent();
                String s = et_label.getText().toString();
                Log.e("syq", s);
                intent.putExtra("label", s);
                setResult(102, intent);
                finish();
            }
        });
        et_label = (EditText) findViewById(R.id.et_label);
        et_label.setFocusable(true);
        et_label.setFocusable(true);
        et_label.setFocusableInTouchMode(true);
        et_label.requestFocus();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }

    @Override
    protected void initData() {

    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, R.anim.activity_close);
    }
}
