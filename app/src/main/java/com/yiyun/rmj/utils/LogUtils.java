package com.yiyun.rmj.utils;

import android.util.Log;

public class LogUtils {
    private static final boolean printLog = true;
    private static final String TAG = "bcz";

    public static void LogE(String msg){
        if(printLog){
            Log.e(TAG, msg);
        }
    }

    public static void LogD(String msg){
        if(printLog){
            Log.d(TAG, msg);
        }
    }


}
