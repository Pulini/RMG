package com.yiyun.rmj.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Created by Administrator on 2018/7/19 0019.
 */

public class ElectricView extends View {



    private String[] colorStr = {"#54FFB2","#49f9ae","#41f0ab","#37eaa8","#30e4a5","#24dda1","#1ed59d","#14d09d","#0ac996","#02c293"};
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        this.invalidate();
    }

    private int count;
    private int totalCount = 10;
    private int perWidth = dp2px(3);
    private int perHeight = dp2px(16);
    private int perStepWidth = dp2px(5);

    public ElectricView(Context context) {
        super(context);
    }

    public ElectricView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ElectricView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);//充满
        p.setColor(Color.GREEN);
        p.setAntiAlias(true);// 设置画笔的锯齿效果
        int startx;
        int endx;
        for (int i = 1; i < totalCount + 1; i++) {


            if (i <= getCount()) {
                if (getCount() <= 2) {

                    int color = Color.parseColor("#f00f21");
                    p.setColor(color);
                } else {
                    int color = Color.parseColor(colorStr[i-1]);
                    p.setColor(color);
                }
            } else {
                int color = Color.parseColor("#d8d8d8");
                p.setColor(color);
            }

            RectF oval4 = new RectF(perWidth*(i- 1) + perStepWidth*(i - 1), 0, perWidth*i + perStepWidth*(i - 1),perHeight);// 设置个新的长方形
//            RectF oval4 = new RectF(dp2px(3 * i + 5 * (i - 1)), dp2px(0), dp2px(3 * (i + 1) + 5 * (i - 1)), dp2px(16));// 设置个新的长方形
            canvas.drawRoundRect(oval4, 20, 15, p);
        }


//      RectF oval4 = new RectF(dp2px(3*1+5*0), dp2px(20), dp2px(3*2+5*0), dp2px(36));// 设置个新的长方形
//        canvas.drawRoundRect(oval4, 20, 15, p);
//        RectF oval5 = new RectF(dp2px(3*2+5*1), dp2px(20), dp2px(3*3+5*1), dp2px(36));// 设置个新的长方形
//        canvas.drawRoundRect(oval5, 20, 15, p);
//        RectF oval6 = new RectF(dp2px(3*3+5*2), dp2px(20), dp2px(3*4+5*2), dp2px(36));// 设置个新的长方形
//        canvas.drawRoundRect(oval6, 20, 15, p);
//        RectF oval7 = new RectF(dp2px(3*4+5*3), dp2px(20), dp2px(3*5+5*3), dp2px(36));// 设置个新的长方形
//        canvas.drawRoundRect(oval7, 20, 15, p);


    }

    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(perWidth*totalCount + perStepWidth*(totalCount - 1), perHeight);
    }
}
