package com.yiyun.rmj.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.yiyun.rmj.R;

public class MyArcProgressBar extends View {
    /**
     * 进度条所占用的角度
     */
    private static final int ARC_FULL_DEGREE = 180;
    /**
     * 弧线的宽度
     */
    private int STROKE_WIDTH;
    /**
     * 组件的宽，高
     */
    private int sWidth,sHeight;

    /**
     * 进度条最大值和当前进度值
     */
    private float max, progress;

    /**
     * 是否允许拖动进度条
     */
    private boolean draggingEnabled = false;
    /**
     * 是否正在拖拽
     */
    private boolean isDragging = false;
    /**
     * 绘制弧线的矩形区域
     */
    private RectF circleRectF;
    /**
     * 绘制弧线的画笔
     */
    private Paint progressPaint;
    /**
     * 绘制文字的画笔
     */
    private Paint textPaint;
    /**
     * 绘制小字画笔
     */
    private Paint textSmallPaint;
    /**
     * 绘制当前进度值的画笔
     */
    private Paint thumbPaint;
    /**
     * 绘制进度条画笔
     */
    private Paint mProgressPaint;

    /**
     * 圆弧的半径
     */
    private int circleRadius;
    /**
     * 圆弧圆心位置
     */
    private int centerX, centerY;
    private int upBtCenterX,upBtCenterY,downBtCenterx,downBtCenterY;//控制按钮的坐标
    /**
     * 拖动条按钮
     */
    private Bitmap dragbutton;

    /**
     * 渐变进度条颜色值
     */
    private String[] progressColors = new String[]{"#02e7d2","#04e5d3","#01e6d1","#01e8d6","#06e5d0","#09e2d7","#00e6d6","#00e4cd","#08e1ce","#0ae5cf","#06e3da","#02e0d3","#01dfd4","#01dfd4","#03dbd0","#00dee0","#00d8d6","#02d7d3","#04d9d5","#00d5cf","#00ddd5","#00d6d8","#00cfd4","#03d2dc","#02ced9","#03ccd6","#00cadb","#00ccd9","#03c7e1","#01c4da","#00c2dd","#04bfe0","#01bede","#00bddd","#00bbdc","#00bbdb","#00b7df","#03b2df","#01b2db","#01b2db","#00b2d9","#00afe1","#0fabde","#00a9dd","#00a6e2","#00a0df","#00a4e2","#00a5e6","#00a2dc","#00a4e4","#00a1e8","#089bde","#03a0e3","#00a2e7","#049de1","#009ff0","#049deb","#039ce8","#009ce6","#019be3","#059be7"};

    private int buttonRadius;
    private float newProgress;

    public MyArcProgressBar(Context context) {
        super(context);
        init();
    }


    public MyArcProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public MyArcProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);  
        init();  
    }  
  
  
    private void init() {  
        dragbutton = BitmapFactory.decodeResource(getResources(), R.mipmap.dragbutton);
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);

  
  
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);

        thumbPaint = new Paint();
        thumbPaint.setAntiAlias(true);

        textSmallPaint = new Paint();
        textSmallPaint.setAntiAlias(true);

        mProgressPaint = new Paint();
        mProgressPaint.setStrokeWidth(5);
        mProgressPaint.setAntiAlias(true);

  
  
        //使用自定义字体  
//        textPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fangz.ttf"));  
    }  
    
    public float getProgress(){
    	return progress;
    }
  
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);  
  
        if (sWidth == 0 || sWidth == 0) {
            sWidth = MeasureSpec.getSize(widthMeasureSpec);
            sHeight = MeasureSpec.getSize(heightMeasureSpec);

            //计算圆弧半径和圆心点  
            circleRadius = Math.min(sWidth, sHeight)/2 ;
            circleRadius -= STROKE_WIDTH;
            
            STROKE_WIDTH = circleRadius / 10;  //圆弧描边的宽度

            centerX = sWidth/ 2;  
            centerY = centerX;  

            //圆弧所在矩形区域  
            circleRectF = new RectF();
            circleRectF.left = centerX - circleRadius;  
            circleRectF.top = centerY - circleRadius;  
            circleRectF.right = centerX + circleRadius;  
            circleRectF.bottom = centerY + circleRadius;

        }
    }  
  
  
    private Rect textBounds = new Rect();


    @SuppressLint("DrawAllocation")
	@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制progress
        for(int i=0;i<=180;i++)
        {
            if(i%3 == 0)
            {
                if(i<(getProgress()/max)*180)
                {
                    int color = Color.parseColor(progressColors[i/3]);
                    mProgressPaint.setColor(color);
                }else
                {

                    mProgressPaint.setColor(getResources().getColor(R.color.color99));
                }
                canvas.save();
                canvas.rotate(i,centerX,centerY);
                canvas.drawLine(centerX-circleRadius-dp2px(10), centerY, centerX-circleRadius+dp2px(10), centerY, mProgressPaint);
                canvas.restore();
            }
        }

        //绘制进度条上的按钮
        canvas.save();
        canvas.rotate((getProgress()/max)*180,centerX,centerY);
        canvas.drawBitmap(dragbutton,centerX-circleRadius-dragbutton.getWidth()/2,centerY-dragbutton.getHeight()/2,thumbPaint);
        canvas.restore();

        //进度值文字
        textPaint.setTextSize(circleRadius/3);
        textPaint.setColor(Color.BLACK);
        String text = (int) getProgress() + "";
        float textLen = textPaint.measureText(text);
        //计算文字高度  
        textPaint.getTextBounds("8", 0, 1, textBounds);  
        float h1 = textBounds.height();  
        //% 前面的数字水平居中，适当调整  
        canvas.drawText(text, centerX - textLen / 2, centerY - dp2px(50) + h1 / 2, textPaint);

        //画"数值越大喷雾越强"文字
        textPaint.setTextSize(circleRadius/7);
        String text2 = "数值越大喷雾越强";
        textPaint.setColor(getResources().getColor(R.color.color99));
        float textLen2 = textPaint.measureText(text2);
        //计算文字高度
        textPaint.getTextBounds(text2, 0, 1, textBounds);
        canvas.drawText(text2, centerX - textLen2 / 2 , centerY-dp2px(3), textPaint);

        //画"弱"文字
        textPaint.setTextSize(circleRadius/7);
        String text3 = "弱";
        textPaint.setColor(getResources().getColor(R.color.color99));
        float textLen3 = textPaint.measureText(text3);
        //计算文字高度
        textPaint.getTextBounds(text3, 0, 1, textBounds);
        float h3 = textBounds.height();
        canvas.drawText(text3, centerX - circleRadius - textLen3 / 2 , centerY + h3 + dp2px(7), textPaint);

        //画"弱"文字
        textPaint.setTextSize(circleRadius/7);
        String text4 = "强";
        textPaint.setColor(getResources().getColor(R.color.color99));
        float textLen4 = textPaint.measureText(text4);
        //计算文字高度
        textPaint.getTextBounds(text4, 0, 1, textBounds);
        float h4 = textBounds.height();
        canvas.drawText(text4, centerX + circleRadius - textLen4 / 2 , centerY + h4 + dp2px(7), textPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!draggingEnabled) {  
            return super.onTouchEvent(event);  
        }  

        //处理拖动事件  
        float currentX = event.getX();  
        float currentY = event.getY();  
  
  
        int action = event.getAction();  
        switch (action) {  
            case MotionEvent.ACTION_DOWN:
                //判断是否在进度条thumb位置  
                if (checkOnArc(currentX, currentY)) {  
                    newProgress = calDegreeByPosition(currentX, currentY) / ARC_FULL_DEGREE * max;  
                    setProgressSync(newProgress);  
                    isDragging = true;  
                } else if(checkOnButtonUp(currentX, currentY)){
							// TODO Auto-generated method stub
					setProgress(progress+10);
                	isDragging = false;
                }else if(checkOnButtonDwon(currentX, currentY)){
                	setProgress(progress-10);
                	isDragging = false;
                }
                	
                break;  
  
  
            case MotionEvent.ACTION_MOVE:
                if (isDragging) {  
                    //判断拖动时是否移出去了  
                    if (checkOnArc(currentX, currentY)) {  
                        setProgressSync(calDegreeByPosition(currentX, currentY) / ARC_FULL_DEGREE * max);  
                    } else {  
                        isDragging = false;  
                    }  
                }  
                break;  
  
                
            case MotionEvent.ACTION_UP:
                isDragging = false;  
                break;  
        }  
        return true;
    }  
  
  
    private float calDistance(float x1, float y1, float x2, float y2) {  
        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }  
  
    private boolean checkOnButtonUp(float currentX,float currentY){
    	float distance = calDistance(currentX, currentY, upBtCenterX, upBtCenterY);
    	return distance < 1.5*buttonRadius;
    }

    private boolean checkOnButtonDwon(float currentX,float currentY){
    	float distance = calDistance(currentX, currentY, downBtCenterx, downBtCenterY);
    	return distance < 1.5*buttonRadius;
    }
    /** 
     * 判断该点是否在弧线上（附近） 
     */  
    private boolean checkOnArc(float currentX, float currentY) {  
        float distance = calDistance(currentX, currentY, centerX, centerY);  
        float degree = calDegreeByPosition(currentX, currentY);  
        return distance > circleRadius - STROKE_WIDTH * 5 && distance < circleRadius + STROKE_WIDTH * 5  
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
  
  
    public void setMax(int max) {  
        this.max = max;  
        invalidate();  
    }  
  
  
    public void setProgress(float progress) {  
        final float validProgress = checkProgress(progress);  
        //动画切换进度值  
        new Thread(new Runnable() {
            @Override
            public void run() {  
                float oldProgress = MyArcProgressBar.this.progress;
                for (int i = 1; i <= 100; i++) {  
                    MyArcProgressBar.this.progress = oldProgress + (validProgress - oldProgress) * (1.0f * i / 100);
                    postInvalidate();  
                    SystemClock.sleep(20);
                }  
            }  
        }).start();  
    }  
  
    public void setProgressSync(float progress) {  
        this.progress = checkProgress(progress);  
        invalidate();  
    }  
  
  
    //保证progress的值位于[0,max]  
    private float checkProgress(float progress) {  
        if (progress < 0) {  
            return 0;  
        }  
        return progress > max ? max : progress;
    }  
  
  
    public void setDraggingEnabled(boolean draggingEnabled) {  
        this.draggingEnabled = draggingEnabled;  
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
}  