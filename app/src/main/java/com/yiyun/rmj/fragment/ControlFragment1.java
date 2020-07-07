package com.yiyun.rmj.fragment;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yiyun.rmj.R;
import com.yiyun.rmj.dialog.QMUITipDialog2;
import com.yiyun.rmj.view.ArcProgress;
import com.yiyun.rmj.view.ElectricView;
import com.yiyun.rmj.view.MyHorizontalProgressBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class ControlFragment1 extends Fragment {
    private int minNum = 10;

    private ArcProgress myProgress01;
    private MyHorizontalProgressBar myHorProgress;
    private TextView myCursorView;
    private Button btn_updata;
    private Button btn_updata1;

    public ControlFragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_main2, container, false);
//        SeekBar seekBar = (SeekBar) view.findViewById(R.id.seek);
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

        myProgress01 = (ArcProgress) view.findViewById(R.id.prgrocess);
        myProgress01.setProgress(50);
        myProgress01.setOnCenterDraw(new ArcProgress.OnCenterDraw() {
            @Override
            public void draw(Canvas canvas, RectF rectF, float x, float y, float storkeWidth, int progress) {
                Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                textPaint.setStrokeWidth(36);
                textPaint.setTextSize(108);
                textPaint.setColor(getResources().getColor(R.color.black));
                String progressStr = String.valueOf(progress);
                canvas.drawText(progressStr, x -50, y-50, textPaint);
            }
        });
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
