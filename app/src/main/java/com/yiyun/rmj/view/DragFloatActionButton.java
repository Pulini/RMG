package com.yiyun.rmj.view;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Method;

public class DragFloatActionButton extends FloatingActionButton {


    private int screenWidth;
    private int screenHeight;
    private int screenWidthHalf;
    private int statusHeight;
    private int virtualHeight;

    private int lastX;
    private int lastY;
    private boolean isDrag;

    public DragFloatActionButton(Context context) {
        super(context);
    }

    public DragFloatActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragFloatActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        screenWidth = outMetrics.widthPixels;
        screenWidthHalf = screenWidth / 2;
        screenHeight = outMetrics.heightPixels;
        statusHeight = getStatusHeight(getContext());
        virtualHeight=getVirtualBarHeigh(getContext());
    }

    /**
       * 获得屏幕高度
       *
       * @param context
       * @return
       */
public static int getScreenWidth(Context context) {
WindowManager wm = (WindowManager) context
.getSystemService(Context.WINDOW_SERVICE);
DisplayMetrics outMetrics = new DisplayMetrics();
wm.getDefaultDisplay().getMetrics(outMetrics);
return outMetrics.widthPixels;
}
/**
   * 获得屏幕宽度
   *
   * @param context
   * @return
   */
public static int getScreenHeight(Context context) {
WindowManager wm = (WindowManager) context
.getSystemService(Context.WINDOW_SERVICE);
DisplayMetrics outMetrics = new DisplayMetrics();
wm.getDefaultDisplay().getMetrics(outMetrics);
return outMetrics.heightPixels;
}
/**
   * 获得状态栏的高度
   *
   * @param context
   * @return
   */
public static int getStatusHeight(Context context) {
int statusHeight = -1;
try {
Class<?> clazz = Class.forName("com.android.internal.R$dimen");
Object object = clazz.newInstance();
int height = Integer.parseInt(clazz.getField("status_bar_height")
        .get(object).toString());
statusHeight = context.getResources().getDimensionPixelSize(height);
} catch (Exception e) {
e.printStackTrace();
}
return statusHeight;
}
    /**
   * 获取虚拟功能键高度
   */
public static int getVirtualBarHeigh(Context context) {
int vh = 0;
WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
Display display = windowManager.getDefaultDisplay();
DisplayMetrics dm = new DisplayMetrics();
try {
@SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }
    public static int getVirtualBarHeigh(Activity activity) {
        int titleHeight = 0;
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusHeight = frame.top;
        titleHeight = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop() - statusHeight;
        return titleHeight;
    }

    @Override
public boolean onTouchEvent(MotionEvent event) {
int rawX = (int) event.getRawX();
int rawY = (int) event.getRawY();
switch (event.getAction() & MotionEvent.ACTION_MASK) {
case MotionEvent.ACTION_DOWN:
isDrag = false;
getParent().requestDisallowInterceptTouchEvent(true);
lastX = rawX;
lastY = rawY;
Log.e("down---->", "getX=" + getX() + "；screenWidthHalf=" + screenWidthHalf);
break;
case MotionEvent.ACTION_MOVE:
isDrag = true;
//计算手指移动了多少
int dx = rawX - lastX;
int dy = rawY - lastY;
//这里修复一些手机无法触发点击事件的问题
int distance= (int) Math.sqrt(dx*dx+dy*dy);
Log.e("distance---->",distance+"");
if(distance<3){//给个容错范围，不然有部分手机还是无法点击
isDrag=false;
break;
}
float x = getX() + dx;
float y = getY() + dy;
//检测是否到达边缘 左上右下
x = x < 0 ? 0 : x > screenWidth - getWidth() ? screenWidth - getWidth() : x;
// y = y < statusHeight ? statusHeight : (y + getHeight() >= screenHeight ? screenHeight - getHeight() : y);
if (y<0){
y=0;
}
if (y>screenHeight-statusHeight-getHeight()){
y=screenHeight-statusHeight-getHeight();
}
setX(x);
setY(y);
lastX = rawX;
lastY = rawY;
Log.e("move---->", "getX=" + getX() + "；screenWidthHalf=" + screenWidthHalf + " " + isDrag+" statusHeight="+statusHeight+ " virtualHeight"+virtualHeight+ " screenHeight"+ screenHeight+" getHeight="+getHeight()+" y"+y);
break;
case MotionEvent.ACTION_UP:
if (isDrag) {
//恢复按压效果
setPressed(false);
Log.e("ACTION_UP---->", "getX=" + getX() + "；screenWidthHalf=" + screenWidthHalf);
if (rawX >= screenWidthHalf) {
animate().setInterpolator(new DecelerateInterpolator())
.setDuration(500)
.xBy(screenWidth - getWidth() - getX())
.start();
} else {
ObjectAnimator oa = ObjectAnimator.ofFloat(this, "x", getX(), 0);
oa.setInterpolator(new DecelerateInterpolator());
oa.setDuration(500);
oa.start();
}
}
Log.e("up---->",isDrag+"");
break;
}
//如果是拖拽则消耗事件，否则正常传递即可。
return isDrag || super.onTouchEvent(event);
}
}
