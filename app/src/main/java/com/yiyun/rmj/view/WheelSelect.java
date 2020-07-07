package com.yiyun.rmj.view;

/**
 * Created by Administrator on 2018/7/17 0017.
 */

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by ccwxf on 2016/4/1.
 */
public class WheelSelect {
    //黑框背景颜色
    public static final int COLOR_BACKGROUND = Color.parseColor("#77777777");
    //黑框的Y坐标起点、宽度、高度
    private int startY;
    private int width;
    private int height;
    //四点坐标
    private Rect rect = new Rect();
    //需要选择文本的颜色、大小、补白
    private String selectText;
    private int fontColor;
    private int fontSize;
    private int padding;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public WheelSelect(int startY, int width, int height, String selectText, int fontColor, int fontSize, int padding) {
        this.startY = startY;
        this.width = width;
        this.height = height;
        this.selectText = selectText;
        this.fontColor = fontColor;
        this.fontSize = fontSize;
        this.padding = padding;
        rect.left = 0;
        rect.top = startY;
        rect.right = width;
        rect.bottom = startY + height;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public void onDraw(Canvas mCanvas) {
        //绘制背景
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(COLOR_BACKGROUND);
        // mCanvas.drawRect(rect, mPaint);
        //绘制提醒文字
        if (selectText != null) {
            //设置钢笔属性
            mPaint.setTextSize(fontSize);
            mPaint.setColor(fontColor);
            //得到字体的宽度
            int textWidth = (int) mPaint.measureText(selectText);
            //drawText的绘制起点是左下角,y轴起点为baseLine
            Paint.FontMetrics metrics = mPaint.getFontMetrics();
            int baseLine = (int) (rect.centerY() + (metrics.bottom - metrics.top) / 2 - metrics.bottom);
            //在靠右边绘制文本
            Log.e("syq", rect.centerX() + "*");
            //mPaint.setColor("");
            int color = Color.parseColor("#029DF9");
            mPaint.setColor(color);
            mCanvas.drawLine(50, rect.centerY() - textWidth, rect.right - 50, rect.centerY() - textWidth, mPaint);
            mCanvas.drawLine(50, rect.centerY() + textWidth, rect.right - 50, rect.centerY() + textWidth, mPaint);
//            mPaint.setColor(fontColor);
//            mCanvas.drawText(selectText, rect.centerX() + padding + textWidth, baseLine, mPaint);
        }
    }
}