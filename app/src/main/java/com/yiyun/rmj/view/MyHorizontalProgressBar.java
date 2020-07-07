package com.yiyun.rmj.view;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyun.rmj.R;

public class MyHorizontalProgressBar extends View {

    private  int DEFAULT_START_COLOR = Color.parseColor("#34DAB5");
    private  int DEFAULT_END_COLOR = Color.parseColor("#27A5FE");
    private int DEFAULT_HUMB_COLOR = Color.parseColor("#FFFFFF");
    private final int COLOR_YELLOW = 0xFFFFFF33;//源的颜色,黄
    private final int COLOR_RED = 0xFFE32F4F;//源的颜色,红
    private final int COLOR_GREY = 0xFFDDD3DB;//目标的颜色,灰
    private int startProgress = 10;//起始进度数
    private int endProgress = 30;//终点进度数
    private RectF mRectFBg;//目标Rect
    private RectF mRectFSrc;//源Rect
    private Paint mPaint;//画笔
    private Paint mProgressPaint;
    private int RADIUS_BIG;//空心的外圆半径
    private int RADIUS_SMALL;//实心的内圆半径
    private float progress;//进度(百分比)
    private float downAndUpProgress;//按压后的动画进度
    private ProgressAnimation animation;//进度动画
    private DownAnimation downAnimation;//按 动画
    private UpAnimation upAnimation;//放 动画
    private ArgbEvaluator argbEvaluator;//计算颜色渐变值
    private boolean isAnimatinEnd = true;//进度动画是否结束
    private float lastX;//上一次触摸的横坐标
    private float lastY;//上一次触摸的纵坐标
    private TextView pointView;
    /**
     * 拖动条按钮
     */
    private Bitmap dragbutton;


    public MyHorizontalProgressBar(Context context) {
        super(context);
        init();
    }

    public int getProgress(float positionx)
    {
        if(mRectFBg == null)
        {
            return startProgress;
        }
        float increase = (mRectFBg.right - 2*RADIUS_BIG)/(endProgress - startProgress);
        return Math.round(positionx/increase) + startProgress;



    }

    public void setpointText(float currentPosx)
    {
        pointView.setText(getProgress(currentPosx)+"秒/次");
    }

    public void setPointTextView(TextView v)
    {
        pointView = v;
        setPointTextViewPosition(lastX);
    }

    public void setPointTextViewPosition(float x){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(pointView.getLayoutParams());
        lp.setMargins((int)x+getLeft()+RADIUS_BIG-pointView.getWidth()/2, lp.topMargin, lp.rightMargin, lp.bottomMargin);
        pointView.setLayoutParams(lp);
        setpointText(x);
    }

    public MyHorizontalProgressBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        downAnimation = new DownAnimation();
        downAnimation.setDuration(800);
        downAnimation.setInterpolator(new DecelerateInterpolator());
        upAnimation = new UpAnimation();
        upAnimation.setDuration(800);
        upAnimation.setInterpolator(new BounceInterpolator());
        argbEvaluator = new ArgbEvaluator();
        dragbutton = BitmapFactory.decodeResource(getResources(), R.mipmap.hprogressbutton);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();

        if (mRectFBg == null) {
            RADIUS_BIG = (int) (10 * getResources().getDisplayMetrics().density + 0.5f);//拿到外圆的半径
            RADIUS_SMALL = RADIUS_BIG >> 1;//内圆的半径为外圆的1/2
            //left=0,top=measuredHeight/4,right=measuredWidth - (RADIUS_BIG - RADIUS_SMALL),bottom=top=measuredHeight*3/4
            mRectFBg = new RectF(0, measuredHeight*4/11, measuredWidth - (RADIUS_BIG - RADIUS_SMALL), measuredHeight - (measuredHeight*4/11));
            mRectFSrc = new RectF(mRectFBg);
            mRectFSrc.right = 0;//源的right设置为0
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.FILL);//设置为实心
        mPaint.setStrokeWidth(1);//画笔的宽度,这个有个坑,strokeWidth [1,+∞],意思就是strokeWidth最小为+1,不管你设置的有多小
        mPaint.setColor(COLOR_GREY);
        canvas.drawRoundRect(mRectFBg, 20, 20, mPaint);//画一个目标灰色的圆角矩形

        //绘制progress
        LinearGradient lg = new LinearGradient(0, 0, lastX, lastY, DEFAULT_START_COLOR, DEFAULT_END_COLOR, Shader.TileMode.CLAMP);
        mProgressPaint.setShader(lg);
        mRectFSrc.right += RADIUS_SMALL * 2;//此处+2倍的RADIUS_SMALL是为了让[图-1]mRectFSrc.right从的实线位置到虚线位置
        canvas.drawRoundRect(mRectFSrc, 20, 20, mProgressPaint);//画一个目标红色的圆角矩形
        mRectFSrc.right -= RADIUS_SMALL * 2;//然后再恢复回来
        //绘制humb
        canvas.drawBitmap(dragbutton,mRectFSrc.right,getHeight()/2-dragbutton.getHeight()/2,mPaint);

//        mPaint.setColor(DEFAULT_HUMB_COLOR);//重置为白色
//        canvas.drawCircle(mRectFSrc.right + 2*RADIUS_SMALL, getHeight() >> 1, RADIUS_BIG - RADIUS_BIG / 20, mPaint);//覆盖掉源的圆角矩形超出的部分
//       //绘制按钮中间白
////        int color = (int) argbEvaluator.evaluate(downAndUpProgress, COLOR_RED, Color.YELLOW);
//        mPaint.setColor(DEFAULT_HUMB_COLOR);
//        canvas.drawCircle(mRectFSrc.right + 2*RADIUS_SMALL, getHeight() >> 1, RADIUS_SMALL + RADIUS_SMALL * downAndUpProgress, mPaint);//内圆
//        //绘制按钮最外层阴影
//        mPaint.setColor(COLOR_GREY);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeWidth(RADIUS_BIG / 10);
//        canvas.drawCircle(mRectFSrc.right + 2*RADIUS_SMALL, getHeight() >> 1, RADIUS_BIG - RADIUS_BIG / 20, mPaint);//外圆

    }

    public void setProgressColor(String startColor, String endColor)
    {
        DEFAULT_START_COLOR = Color.parseColor(startColor);
        DEFAULT_END_COLOR = Color.parseColor(endColor);
    }

    public void setHumbColor(String str)
    {
        DEFAULT_HUMB_COLOR = Color.parseColor(str);
    }

    public void setProgress(float progress) {
        this.progress = (progress-startProgress)/(endProgress-startProgress);
        /*
         * 增加了一个动画效果
         */
        if (animation == null) {
            animation = new ProgressAnimation();
            animation.setDuration(3000);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    isAnimatinEnd = false;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isAnimatinEnd = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        startAnimation(animation);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        boolean onTouch = false;
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isInRound(x, y) && isAnimatinEnd) {
                    onTouch = true;
                    //startAnimation(downAnimation);
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - lastX;
                float dy = y - lastY;
                mRectFSrc.right += dx;
                if (mRectFSrc.right < 0) {
                    mRectFSrc.right = 0;
                    setPointTextViewPosition(0);
                } else if (mRectFSrc.right > (mRectFBg.right - dragbutton.getWidth())) {
                    mRectFSrc.right = (mRectFBg.right - dragbutton.getWidth());
                    setPointTextViewPosition(mRectFSrc.right);
                }else
                {
                    setPointTextViewPosition(mRectFSrc.right);
                }
                Log.e("test","----ACTION_MOVE--mRectFSrc.right:" + mRectFSrc.right);
                invalidate();
                onTouch = true;
                break;
            case MotionEvent.ACTION_UP:
                //startAnimation(upAnimation);
                break;
        }
        lastX = x;
        lastY = y;
        return onTouch;


        //return super.onTouchEvent(event);
    }

    private boolean isInRound(float x, float y) {
        return Math.sqrt(Math.pow(x - (mRectFSrc.right + RADIUS_SMALL), 2) + Math.pow(y - (getHeight() >> 1), 2)) < RADIUS_BIG;
    }

    private class ProgressAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            mRectFSrc.right = (mRectFBg.right - RADIUS_SMALL * 2) * interpolatedTime * progress;
            setPointTextViewPosition(mRectFSrc.right);
            invalidate();
        }
    }

    private class DownAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            downAndUpProgress = interpolatedTime;
            invalidate();
        }
    }

    private class UpAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            downAndUpProgress = 1 - interpolatedTime;
            invalidate();
        }
    }

}
