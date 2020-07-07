package com.yiyun.rmj.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kyesun.ly.CircleTimePicker.callback.OnCirclePickerTimeChangedListener;
import com.kyesun.ly.CircleTimePicker.widget.CirclePicker;
import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;

public class TimeFrameActivity extends BaseActivity implements View.OnClickListener {


    private TextView tv_open_time;
    private TextView tv_end_time;
    private TextView tv_save;
    private TextView tv_reception;
    private TextView tv_label;
    private ImageView iv_back;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_time_frame;
    }

    @Override
    protected void initView() {
        tv_open_time = (TextView) findViewById(R.id.tv_open_time);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_end_time = (TextView) findViewById(R.id.tv_end_time);
        tv_reception = (TextView) findViewById(R.id.tv_reception);
        tv_label = (TextView) findViewById(R.id.tv_label);
        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_save.setVisibility(View.VISIBLE);
        tv_save.setOnClickListener(this);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RelativeLayout rll_reception = (RelativeLayout) findViewById(R.id.rll_reception);
        CirclePicker circle_pciker = (CirclePicker) findViewById(R.id.circle_pciker);
        RelativeLayout rll_label = (RelativeLayout) findViewById(R.id.rll_label);
        rll_reception.setOnClickListener(this);
        rll_label.setOnClickListener(this);
        circle_pciker.setOnTimerChangeListener(new OnCirclePickerTimeChangedListener() {
            @Override
            public void startTimeChanged(float startDegree, float endDegree) {
                Log.e("syq1", "startDegree=" + startDegree + "endDegree=" + endDegree);

                float startCount = (startDegree / (720 / 2)) * (24 * 60);
                int startHour = (int) Math.floor(startCount / (60));
                int startMinute = (int) Math.floor(startCount % (60));
                tv_open_time.setText(((startHour < 10) ? ("0" + startHour) : (startHour + "")) + ":" + ((startMinute < 10) ? ("0" + startMinute) : (startMinute + "")));
                Log.e("syq1", "startDegree=" + startDegree + "endDegree=" + endDegree);
                Log.e("syq2", "startCount=" + startCount + "startHour=" + startHour + "startMinute=" + startMinute);

            }

            @Override
            public void endTimeChanged(float startDegree, float endDegree) {
                double endCount = (endDegree / (720 / 2)) * (24 * 60);
                int endHour = (int) Math.floor(endCount / (60));
                int endMinute = (int) Math.floor(endCount % (60));
                tv_end_time.setText(((endHour < 10) ? ("0" + endHour) : (endHour + "")) + ":" + ((endMinute < 10) ? ("0" + endMinute) : (endMinute + "")));
            }

            @Override
            public void onTimeInitail(float startDegree, float endDegree) {
                double startCount = (startDegree / (720 / 2)) * (24 * 60);
                int startHour = (int) Math.floor(startCount / 60);
                int startMinute = (int) Math.floor(startCount % 60);
                tv_open_time.setText(((startHour < 10) ? ("0" + startHour) : (startHour + "")) + ":" + ((startMinute < 10) ? ("0" + startMinute) : (startMinute + "")));


                double endCount = (endDegree / (720 / 2)) * (24 * 60);
                int endHour = (int) Math.floor(endCount / 60);
                int endMinute = (int) Math.floor(endCount % 60);
                tv_end_time.setText(((endHour < 10) ? ("0" + endHour) : (endHour + "")) + ":" + ((endMinute < 10) ? ("0" + endMinute) : (endMinute + "")));
            }

            @Override
            public void onAllTimeChanaged(float startDegree, float endDegree) {
                double startCount = (startDegree / (720 / 2)) * (24 * 60);
                int startHour = (int) Math.floor(startCount / 60);
                int startMinute = (int) Math.floor(startCount % 60);
                tv_open_time.setText(((startHour < 10) ? ("0" + startHour) : (startHour + "")) + ":" + ((startMinute < 10) ? ("0" + startMinute) : (startMinute + "")));


                double endCount = (endDegree / (720 / 2)) * (24 * 60);
//                int endMinute = (int) (endCount % 60);
//                int endHour = (int) (endCount / 60);
                int endHour = (int) Math.floor(endCount / 60);
                int endMinute = (int) Math.floor(endCount % 60);
                tv_end_time.setText(((endHour < 10) ? ("0" + endHour) : (endHour + "")) + ":" + ((endMinute < 10) ? ("0" + endMinute) : (endMinute + "")));
            }
        });


    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rll_reception:
                Intent intent = new Intent();
                intent.setClassName(this, "com.yiyun.rmj.activity.RepetitionActivity");
                startActivityForResult(intent, 99);
                this.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);

                break;
            case R.id.rll_label:
                Intent intent1 = new Intent();
                intent1.setClassName(this, "com.yiyun.rmj.activity.LabelActivity");
                startActivityForResult(intent1, 88);
                this.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                break;
            case R.id.tv_save:
                Intent intenttime = getIntent();
                String reception = tv_reception.getText().toString();
                if (tv_reception.equals("")) {
                    reception = "从不";
                }

                intenttime.putExtra("reception", reception);
//                intenttime.putExtra("label", tv_label.getText().toString());
                intenttime.putExtra("starttime", tv_open_time.getText().toString());
                intenttime.putExtra("endtime", tv_end_time.getText().toString());

                setResult(66, intenttime);
                finish();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 101) {
            String str = data.getStringExtra("reception");
            tv_reception.setText(str);
        }
        if (resultCode == 102) {
            String str = data.getStringExtra("label");
            tv_label.setText(str);
        }
    }
}
