package com.yiyun.rmj.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.core.content.FileProvider;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PackageUpdateUtil {

    private boolean mIsCancel = false;
    private static PackageUpdateUtil mPackageUtil;


    public static PackageUpdateUtil getInstance(){
        if(mPackageUtil == null){
            mPackageUtil = new PackageUpdateUtil();
        }
        return mPackageUtil;
    }

    public int getVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        int versionName = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionName;
    }

    public String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        String versionName = "";
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionName;
    }

    public String getVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        String version = "";
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName + "." + info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    /**
     * 取消下载
     */
    public void cancelDownloadApk(){
        mIsCancel = true;
    }

    /*
     * 开启新线程下载apk文件
     */
    public void downloadAPK(final String savePath, final String apkName, final String url, final IDownLoadCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                        callback.downloadStart();
                        File dir = new File(savePath);
                        if (!dir.exists()){
                            dir.mkdir();
                        }
                        // 下载文件
                        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        int length = conn.getContentLength();
                        String fileDir =savePath + apkName;
                        File apkFile = new File(fileDir);
                        if(apkFile.exists()){
                            apkFile.delete();
                            apkFile.createNewFile();
                        }
                        FileOutputStream fos = new FileOutputStream(apkFile);

                        int count = 0;
                        byte[] buffer = new byte[1024];
                        int mProgress = 0;
                        while (!mIsCancel){
                            int numread = is.read(buffer);
                            count += numread;
                            // 计算进度条的当前位置
                            mProgress = (int) (((float)count/length) * 100);
                            callback.downloading(mProgress);

                            // 下载完成
                            if (numread < 0){
                                callback.downloadComplete();
                                break;
                            }
                            fos.write(buffer, 0, numread);
                        }
                        fos.close();
                        is.close();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
/*
    public static boolean install(Context con, String filePath) {
        try {
            if(TextUtils.isEmpty(filePath))
            {
                return false;
            }

            File file = new File(filePath);
            if(!file.exists()){
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//增加读写权限
            }
            intent.setDataAndType(getPathUri(con, filePath), "application/vnd.android.package-archive");
            con.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
//            Toast.makeText(con, "安装失败，请重新下载", Toast.LENGTH_LONG).show();
            return false;
        } catch (Error error) {
            error.printStackTrace();
//            Toast.makeText(con, "安装失败，请重新下载", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    */

    //安装apk
    public static boolean install(Context act, String filePath) {
        if(TextUtils.isEmpty(filePath)) {
            return false;
        }

        File file = new File(filePath);
        if(!file.exists()){
            return false;
        }
        if (Build.VERSION.SDK_INT >= 24) {//判读版本是否在7.0以上
            Uri apkUri = FileProvider.getUriForFile(act, "com.yiyun.rmj.fileprovider", file);
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
            act.startActivity(install);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            act.startActivity(intent);
        }
        return true;
    }

    public static Uri getPathUri(Context context, String filePath) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String packageName = context.getPackageName();
            uri = FileProvider.getUriForFile(context, packageName + ".provider", new File(filePath));
        } else {
            uri = Uri.fromFile(new File(filePath));
        }
        return uri;
    }

    public interface IDownLoadCallback{
        public void downloadStart();
        public void downloading(int progress);
        public void downloadComplete();
    }


}
