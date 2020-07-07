package com.yiyun.rmj.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Created by Administrator on 2018/7/18 0018.
 */

public class CircleArc extends View {
    public CircleArc(Context context) {
        super(context);
    }

    public CircleArc(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleArc(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        for (int i = 0; i < 180; i++) {

        }
        mPaint.setTextSize(24);
        Rect rect = new Rect();
        Log.e("Pan", rect.centerX() + "**" + rect.centerY());
        canvas.drawText("数值越大喷雾越强", rect.centerX(), rect.centerY(), mPaint);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int _WidthMode=MeasureSpec.getMode(widthMeasureSpec);
        int _HeightMode = MeasureSpec.getMode(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(_HeightMode, MeasureSpec.EXACTLY));


    }
}
