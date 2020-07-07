package com.yiyun.rmj.utils;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;

import com.yiyun.rmj.base.MyApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * File Name : VideoUtils
 * Created by : PanZX on  2020/5/26 00:04
 * Email : 644173944@qq.com
 * Github : https://github.com/Pulini
 * Remark：
 */
public class VideoUtils {
    private volatile static VideoUtils instance;

    /**
     * 单例（双重校验锁模式）
     */
    public static VideoUtils getInstance() {
        if (instance == null) {
            synchronized (VideoUtils.class) {
                if (instance == null) {
                    instance = new VideoUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     * @param kind      参照MediaStore.Images(Video).Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind); //調用ThumbnailUtils類的靜態方法createVideoThumbnail獲取視頻的截圖；
        if (bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);//調用ThumbnailUtils類的靜態方法extractThumbnail將原圖片（即上方截取的圖片）轉化為指定大小；
        }
        return bitmap;
    }

    public String saveBitmap(Bitmap bitmap, String bitName) {
        File file = new File(MyApplication.getInstance().getExternalFilesDir("response") + bitName + ".png");
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }
}
