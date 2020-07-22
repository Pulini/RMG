package com.yiyun.rmj.utils;

import android.Manifest;
import android.app.Activity;
import android.widget.Toast;

import com.tbruyelle.rxpermissions.RxPermissions;

import rx.Subscriber;

public class PermissionUtil {

    public static void requestStoragePermission(final Activity activity, final IRequestPermissionCallBack callback) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            RxPermissions rx = new RxPermissions(activity);
            rx.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            if (aBoolean) {
                                callback.permissionSuccess();
                            } else {
                                Toast.makeText(activity,"打开权限才能使用这个功能哦!~",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

        }else{
            callback.permissionSuccess();
        }
    }

    public static void requestGpsPermission(final Activity activity, final IRequestPermissionCallBack callback) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            RxPermissions rx = new RxPermissions(activity);
            rx.request(Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            if (aBoolean) {
                                callback.permissionSuccess();

                            } else {
                                Toast.makeText(activity,"打开权限才能使用这个功能哦!~",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

        }else{
            callback.permissionSuccess();
        }
    }


    public static void requestStorageCameraPermission(final Activity activity, final IRequestPermissionCallBack callback) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            RxPermissions rx = new RxPermissions(activity);
            rx.request(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            if (aBoolean) {
                                callback.permissionSuccess();
                            } else {
                                Toast.makeText(activity,"打开权限才能使用这个功能哦!~",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

        }else{
            callback.permissionSuccess();
        }
    }

    public static void requestHXPermission(final Activity activity, final IRequestPermissionCallBack callback) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            RxPermissions rx = new RxPermissions(activity);
            rx.request(Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            if (aBoolean) {
                                callback.permissionSuccess();
                            } else {
                                Toast.makeText(activity,"打开权限才能使用这个功能哦!~",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

        }else{
            callback.permissionSuccess();
        }
    }

    public interface IRequestPermissionCallBack{
        public void permissionSuccess();
    }


}
