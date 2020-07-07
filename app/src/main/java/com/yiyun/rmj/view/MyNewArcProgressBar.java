package com.yiyun.rmj.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.yiyun.rmj.R;

public class MyNewArcProgressBar extends View {

    private String[] stepText = new String[]{"弱", "中" , "强", "MAX"};
    private String textDes = "数值越大喷雾越强";
    private Context context;
    private Paint backgroundPaint;
    private Paint progressPaint;
    private Paint centerTextPaint;
    private Paint centerTextDesPaint;
    private Paint bottomTextDesPaint;
    private Paint roundTextPaint;
    private RectF rectF;
    private int centerX;
    private int centerY;
    private int centerStrenthSize;
    private int centerDesSize;
    private int calibrationTextSize;
    private int bottomTextSize;
    private int width;
    private int height;
    private int circleRadius;
    private static final float ARC_FULL_DEGREE = 270;
    private int currentStep = 0;
    private float progressStep = 0;
    private Rect textBounds = new Rect();

    private int strokeWidth = dp2px(15);


    public MyNewArcProgressBar(Context context) {
        super(context);
        this.context = context;
        init(context, null);
    }

    public MyNewArcProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }

    public MyNewArcProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context, attrs);
    }

    public void init(Context context,AttributeSet attrs){
        if(attrs != null){
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.MyNewArcProgressBar);
            centerStrenthSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.MyNewArcProgressBar_centerStrenthSize, 80);
            centerDesSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.MyNewArcProgressBar_centerDesSize, 26);
            calibrationTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.MyNewArcProgressBar_calibrationTextSize, 26);
            bottomTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.MyNewArcProgressBar_bottomTextSize, 26);

        }else{

        }
        Log.e("test", "init");
        backgroundPaint = new Paint();
        backgroundPaint.setColor(getResources().getColor(R.color.color_fa));
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(strokeWidth);
        backgroundPaint.setStrokeCap(Paint.Cap.ROUND);
        backgroundPaint.setAntiAlias(true);

        progressPaint = new Paint();
        progressPaint.setColor(getResources().getColor(R.color.lightblue));
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(strokeWidth);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setAntiAlias(true);

        centerTextPaint = new Paint();
        centerTextPaint.setTextSize(centerStrenthSize);
        centerTextPaint.setColor(getResources().getColor(R.color.lightblue));
        centerTextPaint.setAntiAlias(true);
        centerTextPaint.setStrokeWidth(5);

        centerTextDesPaint = new Paint();
        centerTextDesPaint.setTextSize(centerDesSize);
        centerTextDesPaint.setColor(getResources().getColor(R.color.lightblue));
        centerTextDesPaint.setAntiAlias(true);
        centerTextDesPaint.setStrokeWidth(2);

        bottomTextDesPaint = new Paint();
        bottomTextDesPaint.setTextSize(bottomTextSize);
        bottomTextDesPaint.setColor(getResources().getColor(R.color.lightblue));
        bottomTextDesPaint.setAntiAlias(true);
        bottomTextDesPaint.setStrokeWidth(2);

        roundTextPaint = new Paint();
        roundTextPaint.setTextSize(calibrationTextSize);
        roundTextPaint.setColor(getResources().getColor(R.color.textcolor_black));
        roundTextPaint.setAntiAlias(true);
        roundTextPaint.setStrokeWidth(2);


    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(width == 0){
            width = getMeasuredWidth();
            height = width;
            circleRadius = width/2 - strokeWidth/2;
            rectF = new RectF(strokeWidth/2,strokeWidth/2,width-strokeWidth/2,height-strokeWidth/2);
            centerY = height/2;
            centerX = width/2;
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画Arc背景
        canvas.drawArc(rectF,90+(360-ARC_FULL_DEGREE)/2,ARC_FULL_DEGREE,false,backgroundPaint);
        //画Arc进度条
        canvas.drawArc(rectF,90+(360-ARC_FULL_DEGREE)/2 + progressStep,ARC_FULL_DEGREE/4,false,progressPaint);

        //画中间的MAX
        centerTextPaint.getTextBounds(stepText[currentStep], 0, stepText[currentStep].length(), textBounds);
        canvas.drawText(stepText[currentStep],centerX - textBounds.width()/2,centerY,centerTextPaint);

        //画中间的描述文字
        centerTextDesPaint.getTextBounds(textDes, 0, textDes.length(), textBounds);
        canvas.drawText(textDes,centerX- textBounds.width()/2,centerY + 2*textBounds.height(),centerTextDesPaint);

        //画底部描述文字
        centerTextDesPaint.getTextBounds(stepText[0], 0, stepText[0].length(), textBounds);
        canvas.drawText(stepText[0],width/6 - textBounds.width()/2 ,width-width/20,bottomTextDesPaint);
        centerTextDesPaint.getTextBounds(stepText[3], 0, stepText[3].length(), textBounds);
        canvas.drawText(stepText[3],width-width/6 - textBounds.width()/2,width-width/20,bottomTextDesPaint);

        //画刻度文字

        roundTextPaint.getTextBounds(stepText[0], 0, stepText[0].length(), textBounds);
        canvas.drawText(stepText[0],width/7 - textBounds.width()/2 ,7*width/12,roundTextPaint);

        canvas.drawText(stepText[1],5*width/16 - textBounds.width()/2 ,5*width/24,roundTextPaint);

        canvas.drawText(stepText[2],width-5*width/16 - textBounds.width()/2 ,5*width/24,roundTextPaint);

        centerTextDesPaint.getTextBounds(stepText[3], 0, stepText[3].length(), textBounds);
        canvas.drawText(stepText[3],width-width/7 - textBounds.width()/2,7*width/12,roundTextPaint);
    }

    public void setProgressStep(final float digree){
        final float currentDigree = currentStep*(ARC_FULL_DEGREE/4);
        currentStep = (int)(digree/(ARC_FULL_DEGREE/4));
        final float tagDigree = currentStep*(ARC_FULL_DEGREE/4);
        if(currentDigree != tagDigree) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 1; i <= 100; i++) {
                        progressStep =progressStep + (tagDigree - currentDigree)/100;
                        postInvalidate();
                        SystemClock.sleep(5);
                    }
                }
            }).start();
        }
    }

    public void setCurrentStep(int step){
        setProgressStep((step-currentStep)*ARC_FULL_DEGREE/4);
    }

    public int getCurrentStep(){
        return currentStep;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float currentX = event.getX();
        float currentY = event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:

            case MotionEvent.ACTION_MOVE:
                if(checkOnArc(currentX,currentY)){
                    float tagetDegree = calDegreeByPosition(currentX,currentY);
                    if(tagetDegree >= ARC_FULL_DEGREE){
                        tagetDegree = ARC_FULL_DEGREE - ARC_FULL_DEGREE/4;
                    }
                    setProgressStep(tagetDegree);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);

    }

    private float calDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    /**
     * 判断该点是否在弧线上（附近）
     */
    private boolean checkOnArc(float currentX, float currentY) {
        float distance = calDistance(currentX, currentY, centerX, centerY);
        float degree = calDegreeByPosition(currentX, currentY);
        return distance > circleRadius - strokeWidth * 5 && distance < circleRadius + strokeWidth * 5
                && (degree >= -8 && degree <= ARC_FULL_DEGREE + 8);
    }


    /**
     * 根据当前位置，计算出进度条已经转过的角度。
     */
    private float calDegreeByPosition(float currentX, float currentY) {
        float a1 = (float) (Math.atan(1.0f * (centerX - currentX) / (currentY - centerY)) / Math.PI * 180);
        if (currentY < centerY) {
            a1 += 180;
        } else if (currentY > centerY && currentX > centerX) {
            a1 += 360;
        }
        return a1 - (360 - ARC_FULL_DEGREE) / 2;
    }
}