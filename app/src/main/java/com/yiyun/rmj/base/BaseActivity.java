package com.yiyun.rmj.base;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.yiyun.rmj.R;
import com.yiyun.rmj.activity.LoginActivity;
import com.yiyun.rmj.api.Api;
import com.yiyun.rmj.bluetooth.BaseBluetoothUtil;
import com.yiyun.rmj.bluetooth.ClassicalBluetoothUtil;
import com.yiyun.rmj.bluetooth.NewBleBluetoothUtil;
import com.yiyun.rmj.setting.UrlContact;
import com.yiyun.rmj.utils.AppManager;
import com.yiyun.rmj.utils.RetrofitHelper;
import com.yiyun.rmj.utils.SpfUtils;
import com.yiyun.rmj.utils.StatusBarUtil;

import cn.jake.share.frdialog.dialog.FRDialog;

/**
 * Created by Administrator on 2018/7/17 0017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    FRDialog unconnectedDialog;

    /**
     * activity堆栈管理
     */
    protected AppManager appManager = AppManager.getAppManager();
    private BaseBluetoothUtil mBluetoothUtil;
    private SVProgressHUD mSVProgressHUD;
    protected Api api;
    protected Gson gson;
    private FRDialog dialog;

    Handler handler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what){
                case 1:
                    unconnectedDialog.show();
                    break;
                case 2:
                    byte[] orders = (byte[])msg.obj;
                    if(msg.arg1 == BluetoothGatt.GATT_SUCCESS){
                        dealOrderCallback(orders);
                    }
                    showMessage(orders, msg.arg1);

                    break;
            }
        }
    };

    public void showMessage(byte[] orderInfo, int status){

        byte order = orderInfo[0];
        switch(order){
            case NewBleBluetoothUtil.shutdown:
                if(status == BluetoothGatt.GATT_SUCCESS){
                    ToastUtils.show("关机成功");
                }else{
                    ToastUtils.show("关机失败");
                }
                break;

            case NewBleBluetoothUtil.boot:

                if(status == BluetoothGatt.GATT_SUCCESS){
                    ToastUtils.show("开机成功");
                }else{
                    ToastUtils.show("开机失败");
                }

                break;
            case NewBleBluetoothUtil.forbidsetpoweronclear:
                if(status == BluetoothGatt.GATT_SUCCESS){
                    ToastUtils.show("禁止开机自动清洗设置成功");
                }else{
                    ToastUtils.show("禁止开机自动清洗设置失败");
                }
                break;

            case NewBleBluetoothUtil.setpoweronclear:
                if(status == BluetoothGatt.GATT_SUCCESS){
                    ToastUtils.show("开机自动清洗设置成功");
                }else{
                    ToastUtils.show("开机自动清洗设置失败");
                }
                break;

            case NewBleBluetoothUtil.setstrenth:
//                if(status == BluetoothGatt.GATT_SUCCESS){
//                    String strength = "阅读模式";
//                    if(orderInfo[1] == 1){
//                        strength = "阅读模式";
//                    }else if(orderInfo[1] == 2){
//                        strength = "电竞模式";
//                    }else if(orderInfo[1] == 3){
//                        strength = "美容模式";
//                    }else if(orderInfo[1] == 4){
//                        strength = "MAX";
//                    }
//                    ToastUtils.show("强度" + strength + "设置成功");
//                }else{
//                    ToastUtils.show("强度设置失败");
//                }
                break;
            case NewBleBluetoothUtil.mode_mild:
//                if(status == BluetoothGatt.GATT_SUCCESS){
//                    ToastUtils.show("智能模式强度小设置成功");
//                }else{
//                    ToastUtils.show("智能模式强度小设置失败");
//                }
                break;
            case NewBleBluetoothUtil.mode_middle:
//                if(status == BluetoothGatt.GATT_SUCCESS){
//                    ToastUtils.show("智能模式强度中设置成功");
//                }else{
//                    ToastUtils.show("智能模式强度中设置失败");
//                }
                break;
            case NewBleBluetoothUtil.mode_long:
//                if(status == BluetoothGatt.GATT_SUCCESS){
//                    ToastUtils.show("智能模式强度强设置成功");
//                }else{
//                    ToastUtils.show("智能模式强度强设置失败");
//                }
                break;

            case NewBleBluetoothUtil.setcleartime:
                if(status == BluetoothGatt.GATT_SUCCESS){
                    ToastUtils.show("设置清洗时间成功，当前清洗时间：" + orderInfo[1] + "秒");
                }else{
                    ToastUtils.show("设置清洗时间失败");
                }

            case NewBleBluetoothUtil.settimeinterval:
//                if(status == BluetoothGatt.GATT_SUCCESS){
//                    ToastUtils.show("设置间隔时间成功，当前间隔时间：" + orderInfo[1] + "秒");
//                }else{
//                    ToastUtils.show("设置间隔时间失败");
//                }
                break;

            case NewBleBluetoothUtil.clearleft:
                if(status == BluetoothGatt.GATT_SUCCESS){
                    ToastUtils.show("清洗左喷头指令发送成功");
                }else{
                    ToastUtils.show("清洗左喷头指令发送失败");
                }
                break;

            case NewBleBluetoothUtil.clearright:
                if(status == BluetoothGatt.GATT_SUCCESS){
                    ToastUtils.show("清洗右喷头指令发送成功");
                }else{
                    ToastUtils.show("清洗右喷头指令发送失败");
                }
                break;
        }
    }

    public void dealOrderCallback(byte[] order){

    }

    public void showUnconnectedDialog(){
        handler.sendEmptyMessage(1);
    }

    protected void startlogin() {
        if (System.currentTimeMillis() - UrlContact.exitTime > 1000) {
            UrlContact.exitTime = System.currentTimeMillis();
            dialog = new FRDialog.CommonBuilder(this).setContentView(R.layout.tip).create();

            dialog.setCancelable(false);

            TextView tv_login = dialog.findViewById(R.id.tv_login);
            TextView tv_showmsg = dialog.findViewById(R.id.tv_tip);

            SpfUtils.getSpfUtils(MyApplication.getInstance()).setToken("");
            tv_login.setText("去登陆");
            tv_showmsg.setText("您已离线 请先进行登录");
            dialog.setCancelable(true);

            dialog.show();
            tv_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                    intent.putExtra("relogin",true);
                    startActivity(intent);
                }
            });
        } else {
            SpfUtils.getSpfUtils(MyApplication.getInstance()).setToken("");
            return;
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appManager.addActivity(this);
        gson = new Gson();
        NewBleBluetoothUtil.getInstance().setCurrentContext(this);
        initUnconnectedDialog();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏

        setContentView(getLayoutId());
        setStatusBar();
        mSVProgressHUD = new SVProgressHUD(this);
        mSVProgressHUD.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(SVProgressHUD hud) {

            }
        });

        api  = RetrofitHelper.getSingleRetrofit().create(Api.class);
        initView();
        initData();
    }

    public void showOrderMessage(byte[] order, int status){
        Message message = new Message();
        message.what = 2;
        message.obj = order;
        message.arg1 = status;
        handler.sendMessage(message);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        NewBleBluetoothUtil.getInstance().setCurrentContext(this);
    }

    //获取蓝牙操作类对象
    public BaseBluetoothUtil getBluetoolthUtil()
    {
        if(mBluetoothUtil == null)
        {
//            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
//                //有BLE蓝牙功能模块
//                mBluetoothUtil = new BleBluetoothUtil();
//            }else
//            {
                mBluetoothUtil = new ClassicalBluetoothUtil();
//            }

        }
        return mBluetoothUtil;
    }

    protected void setStatusBar(){
        StatusBarUtil.setImmersiveStatusBar(this,false);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            //当isShouldHideInput(v, ev)为true时，表示的是点击输入框区域，则需要显示键盘，同时显示光标，反之，需要隐藏键盘、光标
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    //处理Editext的光标隐藏、显示逻辑
                    v.clearFocus();
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    protected void showProgressDialog(String msg) {

        mSVProgressHUD.showWithStatus(msg);

    }

    protected void dismissProgressDialog() {
        if (mSVProgressHUD.isShowing()) {
            mSVProgressHUD.dismiss();
        }
    }

    public void initUnconnectedDialog(){
        unconnectedDialog = new FRDialog.CommonBuilder(this).setContentView(R.layout.dialog_unconnected_device).create();
        unconnectedDialog.setCancelable(false);
//        unconnectedDialog.setCanceledOnTouchOutside(false);
        ImageView tv_cancel = unconnectedDialog.findViewById(R.id.iv_closedialog);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSometingAfterCloseUnconnectedDialog();
                unconnectedDialog.dismiss();
            }
        });
    }

    protected void doSometingAfterCloseUnconnectedDialog(){

    }

    protected void showConnectError() {
        ToastUtils.show("网络异常！");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        appManager.finishActivity(this);
    }

    protected void showConnectError(String msg) {
        ToastUtils.show(msg);
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

}
