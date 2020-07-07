package com.yiyun.rmj.utils;

import android.content.Context;

import com.yiyun.rmj.base.MyApplication;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2018/6/6 0006.
 */

public class OkHttp3Helper {
    private static OkHttpClient mOkHttpClicent = null;

    private OkHttp3Helper(Context context) {
        mOkHttpClicent = getSingleInstance();
    }

    public static OkHttpClient getSingleInstance() {
        if (mOkHttpClicent == null) {
            synchronized (OkHttpClient.class) {
                if (mOkHttpClicent == null) {
                    mOkHttpClicent = new OkHttpClient.Builder()
                            //开启缓存
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30,TimeUnit.SECONDS)
                            .writeTimeout(30,TimeUnit.SECONDS)
                            .cache(getReponseCach())
                            //添加缓存拦截
                            .addInterceptor(new CacheInterceptor())
                            .addNetworkInterceptor(new CacheInterceptor())
                            .build();
                }
            }
        }
        return mOkHttpClicent;
    }

    //缓存路径
    public static Cache getReponseCach() {
        File externalCacheDir = MyApplication.getInstance().getExternalFilesDir("response");
        return new Cache(externalCacheDir, 50 * 1024 * 1024);
    }
}