package com.yiyun.rmj.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.yiyun.rmj.R;
import com.yiyun.rmj.dialog.QMUITipDialog2;
import com.yiyun.rmj.view.ElectricView;
import com.yiyun.rmj.view.MyArcProgressBar;
import com.yiyun.rmj.view.MyHorizontalProgressBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class ControlFragment extends Fragment {
    private int minNum = 10;

    private MyArcProgressBar myProgress01;
    private MyHorizontalProgressBar myHorProgress;
    private TextView myCursorView;
    private Button btn_updata;
    private Button btn_updata1;

    public ControlFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_main2, container, false);
        btn_updata1 = (Button) view.findViewById(R.id.btn_updata);
        myCursorView = (TextView)view.findViewById(R.id.tv_cursor);
        myHorProgress = (MyHorizontalProgressBar)view.findViewById(R.id.my_hpb);
        myHorProgress.setPointTextView(myCursorView);
        myHorProgress.setProgress(15);
//        IndicatorSeekBar seektime = (IndicatorSeekBar) view.findViewById(R.id.seektime);
//        seekBar.setProgress(50);
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                Log.e("Pan", "progress=" + progress);
//                myProgress01.setProgress(progress);
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });


        myProgress01 = (MyArcProgressBar) view.findViewById(R.id.prgrocess);
        myProgress01.setDraggingEnabled(true);
        myProgress01.setMax(100);
        myProgress01.setProgress(50);

        ElectricView tv_eric = (ElectricView) view.findViewById(R.id.tv_eric);
        tv_eric.setCount(10);
        TextView tv_power = (TextView) view.findViewById(R.id.tv_power);
        tv_power.setText(10 * 10 + "%");

//        seektime.setMin(10);
//        seektime.setMax(30);
        myHorProgress.setProgress(15);
//        seektime.setIndicatorTextFormat("${PROGRESS} 秒/次");

        btn_updata1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final QMUITipDialog2 qmuiTipDialog = new QMUITipDialog2.Builder(getContext())
                        .setIconType(QMUITipDialog2.Builder.ICON_TYPE_SUCCESS)
                        .setTipWord("修改成功")
                        .create();
                qmuiTipDialog.show();
                btn_updata1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        qmuiTipDialog.dismiss();
                    }
                }, 1000);

            }
        });
        return view;
    }

}
