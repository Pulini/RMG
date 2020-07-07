package com.yiyun.rmj.fragment;


import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.yiyun.rmj.R;
import com.yiyun.rmj.view.WheelView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CleanFragment extends Fragment {


    private ArrayList<String> list;
    private int time = 30;
    private int lasttime = 30;
    private WheelView wheelView;
    private Button btn_clean;
    private TextView tv_time;
    Handler handler = new Handler();
    private Runnable runnable;
    private int lastindex = 10;

    public CleanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_clean, container, false);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        wheelView = (WheelView) view.findViewById(R.id.wheel);
        list = new ArrayList<>();
        for (int i = 20; i < 61; i++) {
            list.add(i + "");
        }
        btn_clean = (Button) view.findViewById(R.id.btn_clean);

        wheelView.lists(list).select(10).showCount(5).fontSize(35)
                .listener(new WheelView.OnWheelViewItemSelectListener() {
                    @Override
                    public void onItemSelect(int index) {
                        time = Integer.parseInt(list.get(index));
                        Log.e("Pan", "index=" + index);
                        lastindex = index;
                        lasttime = time;
                    }
                });

        btn_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resources res = getResources();
                if (btn_clean.getText().toString().equals("取消")) {
                    tv_time.setVisibility(View.GONE);
                    wheelView.setVisibility(View.VISIBLE);
                    btn_clean.setText("开始清洗");
                    Drawable drawable = res.getDrawable(R.mipmap.button_ksqx);
                    btn_clean.setBackground(drawable);
                    handler.removeCallbacks(runnable);

                } else {

                    tv_time.setText(time + "");
                    tv_time.setVisibility(View.VISIBLE);
                    wheelView.setVisibility(View.INVISIBLE);
                    btn_clean.setText("取消");
                    time = lasttime;

                    tv_time.setText(time+"");
                    Drawable drawable = res.getDrawable(R.drawable.button_quxiao);
                    btn_clean.setBackground(drawable);
//                    tv_time.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            tv_time.setText(time-- + "");
//                        }
//                    }, 1000);
                    handler.postDelayed(runnable, 1000);

                    //跳转到倒计时页面
                }

            }
        });

        // TODO Auto-generated method stub
//要做的事情，这里再次调用此Runnable对象，以实现每两秒实现一次的定时器操作
        runnable = new Runnable() {
            @Override
            public void run() {

                if (time == 0) {
                    tv_time.setText(0 + "");
                    handler.removeCallbacks(runnable);
                    time = lasttime;
                } else {
                    tv_time.setText(time-- + "");
                    handler.postDelayed(this, 1000);
                }
            }
        };


        return view;
    }

}
