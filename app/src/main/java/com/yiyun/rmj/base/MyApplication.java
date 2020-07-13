package com.yiyun.rmj.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

//import com.alibaba.mobileim.YWAPI;
//import com.alibaba.mobileim.YWIMKit;
//import com.alibaba.wxlib.util.SysUtil;
import com.hjq.toast.ToastUtils;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.yiyun.rmj.R;


import org.litepal.LitePal;

//import static com.alibaba.tcms.client.ClientRegInfo.APP_KEY;

public class MyApplication extends Application {

    private static MyApplication myApplication;
    public static int wxpayResltCode = 999;
    public static String IM_KEY = "30456578";


    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setPrimaryColorsId( android.R.color.transparent,R.color.colorPrimary);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });

    }
    private static SharedPreferences mSharedPreferences;
    public static SharedPreferences getSP() {
        return mSharedPreferences;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        mSharedPreferences = getSharedPreferences("BlueTooth", MODE_PRIVATE);
        // 初始化LitePal数据库
        LitePal.initialize(this);
        ToastUtils.init(this);

//        SImagePicker.init(new PickerConfig.Builder().setAppContext(this)
//                .setImageLoader(new FrescoImageLoader())
//                .build());

        UMConfigure.init(this,"5cbe8432570df3dc3600102c","wzsjkj",UMConfigure.DEVICE_TYPE_PHONE,"");
        UMConfigure.setLogEnabled(true);
        PlatformConfig.setWeixin("wxd05a2356c3312721", "c4a3b38e018d7e008f6103537fe1fff3");
        PlatformConfig.setQQZone("101573070", "a6c9826c42a370f88895485c244f88c7");

        //集成bugly日志上报  热更新
        Beta.autoInit = true;
        Beta.autoCheckUpgrade = true;
        Bugly.init(getApplicationContext(), "7c0d355d82", false);


//        //必须首先执行这部分代码, 如果在":TCMSSevice"进程中，无需进行云旺（OpenIM）和app业务的初始化，以节省内存;
//        SysUtil.setApplication(this);
//        if(SysUtil.isTCMSServiceProcess(this)){
//            return;
//        }
//        //第一个参数是Application Context
//        //这里的APP_KEY即应用创建时申请的APP_KEY，同时初始化必须是在主进程中
//        if(SysUtil.isMainProcess()){
//            YWAPI.init(this, "23015524");
//        }



    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
//        MultiDex.install(base);

        // 安装tinker
//        Beta.installTinker();
    }

    public static MyApplication getInstance() {
        return myApplication;
    }
}
