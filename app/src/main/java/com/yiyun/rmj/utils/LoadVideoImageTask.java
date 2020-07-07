package com.yiyun.rmj.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;

import java.util.HashMap;

/**
 * File Name : LoadVideoImageTask
 * Created by : PanZX on  2020/6/3 21:26
 * Email : 644173944@qq.com
 * Github : https://github.com/Pulini
 * Remark：
 */
public class LoadVideoImageTask extends AsyncTask<String, Integer, Bitmap> {
    public interface OnLoadVideoImageListener {
        void onLoadImage(Bitmap file);
    }

    private OnLoadVideoImageListener listener;

    public LoadVideoImageTask(OnLoadVideoImageListener listener) {
        LogUtils.LogE("线程准备");
        this.listener = listener;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        LogUtils.LogE("开始执行："+params[0]);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(params[0], new HashMap<String, String>());
        return mmr.getFrameAtTime();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        LogUtils.LogE("执行完成");
        if (listener != null) {
            listener.onLoadImage(bitmap);
        }
    }
}

