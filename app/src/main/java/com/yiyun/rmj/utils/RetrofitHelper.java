package com.yiyun.rmj.utils;


import com.yiyun.rmj.setting.UrlContact;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by Administrator on 2017/6/9.
 */

public class RetrofitHelper {
    private static Retrofit retrofit = null;

    public RetrofitHelper() {
        retrofit = getSingleRetrofit();
    }

    public static Retrofit getSingleRetrofit() {
        if (retrofit == null) {
            synchronized (RetrofitHelper.class) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(UrlContact.getBaseUrl())
                            .client(OkHttp3Helper.getSingleInstance())
                            .addConverterFactory(MGsonConverterFactory.create(true))
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

}
