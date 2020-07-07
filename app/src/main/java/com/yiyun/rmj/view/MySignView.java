package com.yiyun.rmj.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MySignView extends View {

    private int width;   //控件可用总宽度
    private int height;  //控件可用总高度

    private int mounthBgWidth; //月份背景宽度
    private int mounthBgHeight; //月份背景高度
    private int mounthBgRadius ; //月份背景圆角半径
    private String mounthBgColor = "#FFFFFF"; //月份背景颜色
    private int mounthTextSize = 16; //月份字体大小
    private String mounthTextColor = "#943E1E"; //月份字体颜色

    private int mounthContentShowHeight;//月份内容显示高度
    private String mounthText = "2019-03-01";

    private int weekBgWidth; //周背景宽度
    private int weekBgHeight; //周背景高度
    private int weekBgRadius; //周背景圆角半径
    private String weekBgColor = "#FDDA89"; //周背景颜色
    private int weekTextSize = 16; //周字体大小
    private String weekTextColor = "#943E1E"; //周字体颜色

    private int weekContentShowHeight;//周内容显示高度

    private int dayBgWidth; //日背景宽度
    private int dayBgHeight; //日背景高度
    private int dayBgRadius; //日背景圆角半径
    private String dayBgColor = "#FFFFFF"; //日背景颜色
    private int dayTextSize = 16; //日字体大小
    private String dayTextColor = "#943E1E"; //日字体颜色
    private int dailyHeight;  //日期单元格的高度
    private int dailyWidth;   //日期单元格的宽度
    private String dayBgLineColor = "#EEEEEE"; //日九宫格颜色

    private int signRaidus;     // 签到背景圆半径
    private String signColor = "#FDD888";   //签到背景颜色

    private int dayBgMarginBottomHeight;//日期背景距离底部的高度
    private int rowNum;


    Paint mounthBgPaint;    //月份背景画笔
    RectF mounthBgRect;     //月份背景轮廓

    Paint weekBgPaint;      //周背景画笔
    RectF weekBgRect;       //周背景轮廓
    RectF weekBgRectFillingRoundRect;       //周填充圆角轮廓

    Paint mounthTextPaint;  //月文本画笔
    Paint weekTextPaint;    //周文本画笔
    Paint dayTextPaint;     //日文本画笔

    Paint dayBgPaint;       //日背景画笔
    RectF dayBgRect;        //日背景轮廓

    Paint signPaint;        //签到画笔
    Paint dayBgLinePaint;   //日九宫格线画笔

    String[] weekTextList = new String[]{"日","一","二","三","四","五","六"};
    int firstIndex;
    int dayOfMonth;
    Rect rect;
    HashMap<Integer,Boolean> signMap;




    public MySignView(Context context) {
        super(context);
        init();
    }

    public MySignView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MySignView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        signMap = new HashMap<>();

        mounthBgPaint = new Paint();
        mounthBgPaint.setColor(Color.parseColor(mounthBgColor));

        mounthTextPaint = new Paint();
        mounthTextPaint.setStrokeWidth(dp2px(2));
        mounthTextPaint.setTextSize(dp2px(mounthTextSize));
        mounthTextPaint.setColor(Color.parseColor(mounthTextColor));

        weekBgPaint = new Paint();
        weekBgPaint.setColor(Color.parseColor(weekBgColor));

        weekTextPaint = new Paint();
        weekTextPaint.setStrokeWidth(dp2px(2));
        weekTextPaint.setTextSize(dp2px(weekTextSize));
        weekTextPaint.setColor(Color.parseColor(dayTextColor));



        dayBgPaint = new Paint();
        dayBgPaint.setColor(Color.parseColor(dayBgColor));

        dayTextPaint = new Paint();
        dayTextPaint.setStrokeWidth(dp2px(2));
        dayTextPaint.setTextSize(dp2px(dayTextSize));
        dayTextPaint.setColor(Color.parseColor(dayTextColor));

        dayBgLinePaint = new Paint();
        dayBgLinePaint.setColor(Color.parseColor(dayBgLineColor));
        dayBgLinePaint.setStrokeWidth(dp2px(1));

        signPaint = new Paint();
        signPaint.setColor(Color.parseColor(signColor));

        mounthText = getDayStr(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.setTime(strMounth2Date(mounthText));
        firstIndex = calendar.get(Calendar.DAY_OF_WEEK)-1;
        dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for(int i = 1;i<= dayOfMonth;i++){
            signMap.put(i,false);
        }
        signMap.put(4,true);
        signMap.put(9,true);
        Log.e("bcz","firstIndex:" + firstIndex);
        Log.e("bcz","dayOfMonth:" + dayOfMonth);
        rect = new Rect(); //计算文字宽度用
        rowNum = (firstIndex + dayOfMonth)/7 + 1;
        Log.e("bcz","init");

    }

    /**
     * 画月份背景
     * @param canvas
     */
    public void drawMounthBg(Canvas canvas){
        //画出圆角矩形
        canvas.drawRoundRect(mounthBgRect, mounthBgRadius, mounthBgRadius, mounthBgPaint);
    }

    /**
     * 画月份
     * @param canvas
     */
    public void drawMounth(Canvas canvas){
        drawMounthBg(canvas);
        drawMounthText(canvas);
    }

    /**
     * 画周背景
     * @param canvas
     */
    public void drawWeekBg(Canvas canvas){
        canvas.drawRoundRect(weekBgRect, weekBgRadius, weekBgRadius, weekBgPaint);
        canvas.drawRect(weekBgRectFillingRoundRect,weekBgPaint );
    }

    public void drawWeekText(Canvas canvas){
        Rect rect = new Rect();
        weekTextPaint.getTextBounds(weekTextList[0],0,weekTextList[0].length(), rect);
        int textWidth = rect.width();
        int textHeight = rect.height();
        rect = null;

        for(int i = 0; i < weekTextList.length; i++){
            canvas.drawText(weekTextList[i],(dayBgRect.left + dailyWidth/2 - textWidth/2) + i*dailyWidth, dayBgRect.top - dailyWidth/2 + textHeight/2, weekTextPaint);
        }
    }

    public void getDayPaintStartPos(int day, MyPoint point){
        dayTextPaint.getTextBounds(day+"",0,(day+"").length(), rect);
        int textWidth = rect.width();
        int textHeight = rect.height();
        getDayPaintStartCenterPos(day,point);
        point.x = point.x - textWidth/2;
        point.y = point.y + textHeight/2;
    }

    public void getDayPaintStartCenterPos(int day, MyPoint point){
        int count = day + firstIndex -1;
        int row = count/7;
        int column = count%7;
        point.x = (int)(dayBgRect.left + column*dailyWidth + (dailyWidth/2));
        point.y = (int)(dayBgRect.top + row*dailyHeight + (dailyHeight/2));
    }



    public void drawMounthText(Canvas canvas){
        Rect rect = new Rect();
        weekTextPaint.getTextBounds(mounthText,0,mounthText.length(), rect);
        int textWidth = rect.width();
        int textHeight = rect.height();
        rect = null;
        canvas.drawText(mounthText,mounthBgWidth/2 - textWidth/2, mounthContentShowHeight/2 + textHeight/2, mounthTextPaint);
    }

    public void drawDayText(Canvas canvas){
        MyPoint point = new MyPoint();
        for(int i=1; i< dayOfMonth+1; i++){
            if(signMap.get(i))
            {
                getDayPaintStartCenterPos(i,point);
                canvas.drawCircle(point.x,point.y,signRaidus,signPaint);
            }

            getDayPaintStartPos(i,point);
            canvas.drawText(i+"",point.x, point.y, mounthTextPaint);
        }
    }

    public  boolean signToday(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return signDate(day);
    }

    public boolean signDate(int day){
        if(signMap.get(day) != null && signMap.get(day)){
            return false;
        }else{
            signMap.put(day,true);
            invalidate();
            return true;
        }
    }
    /**
     * 画周
     * @param canvas
     */
    public void drawWeek(Canvas canvas){

        drawWeekBg(canvas);
        drawWeekText(canvas);
    }



    public void drawDayBg(Canvas canvas){
        canvas.drawRoundRect(dayBgRect, dayBgRadius, dayBgRadius, dayBgPaint);
        //画九宫格横线
        for (int i = 1; i<rowNum; i++){
            canvas.drawLine(dayBgRect.left, dayBgRect.top + i*dailyHeight, dayBgRect.right, dayBgRect.top + i*dailyHeight,dayBgLinePaint);
        }
        //画九宫格纵线条
        for (int i = 1; i<7; i++){
            canvas.drawLine(dayBgRect.left + i*dailyWidth, dayBgRect.top, dayBgRect.left + i*dailyWidth, dayBgRect.bottom,dayBgLinePaint);
        }
    }

    public void drawDays(Canvas canvas){
        drawDayBg(canvas);
        drawDayText(canvas);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //画月份
        drawMounth(canvas);
        //画周
        drawWeek(canvas);
        //画日
        drawDays(canvas);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("bcz","onMeasure");
        width = getMeasuredWidth();
        height = width;
        mounthBgWidth = width;
        mounthBgHeight = height;
        weekBgWidth = mounthBgWidth;
        dayBgRadius = 20;
        weekBgRadius = 2*dayBgRadius;
        mounthBgRadius = 2*dayBgRadius;
        mounthBgRect = new RectF(0,0,mounthBgWidth,mounthBgHeight);

        mounthContentShowHeight = (int)mounthBgHeight/8;
        weekContentShowHeight = mounthContentShowHeight;

        weekBgRect = new RectF(0,mounthContentShowHeight,mounthBgWidth,mounthBgHeight);
        weekBgRectFillingRoundRect = new RectF(0,mounthContentShowHeight,mounthBgWidth,mounthContentShowHeight+weekBgRadius );

        dayBgMarginBottomHeight = (int)(weekContentShowHeight/2);
        dayBgHeight = mounthBgHeight - mounthContentShowHeight - weekContentShowHeight - dayBgMarginBottomHeight;
        dayBgWidth = weekBgWidth - dayBgMarginBottomHeight;

        dailyHeight = dayBgHeight/rowNum;
        dailyWidth = dayBgWidth/7;

        int minLength = dailyHeight > dailyWidth? dailyWidth:dailyHeight;
        signRaidus = minLength*2/5;

//        int dailyLength = dailyHeight > dailyWidth ? dailyWidth:dailyHeight;
//        dailyHeight = dailyLength;
//        dailyWidth = dailyLength;

        dayBgRect = new RectF();
        dayBgRect.left = (weekBgWidth - dailyWidth*7)/2;
        dayBgRect.top = mounthBgHeight - dailyHeight*rowNum - weekContentShowHeight/2;
        dayBgRect.right = dayBgRect.left + dailyWidth*7;
        dayBgRect.bottom = dayBgRect.top + dailyHeight*rowNum;
        setMeasuredDimension(width, height);

    }

    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    public class MyPoint {
        private int x;
        private int y;

        public void setPointX(int pointX){
            x = pointX;
        }

        public void setPointY(int pointY){
            y = pointY;
        }

        public int getPointX(){
            return x;
        }

        public int getPointY(){
            return y;
        }
    }

    /**获取月份标题*/
    private String getMonthStr(Date month){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        return df.format(month);
    }

    private Date strMounth2Date(String str){
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
            return df.parse(str);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**获取年月日标题*/
    private String getDayStr(Date month){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(month);
    }

    /**年月日标题转成日期对象*/
    private Date strDay2Date(String str){
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.parse(str);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
