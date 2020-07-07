package com.yiyun.rmj.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hjq.toast.ToastUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.BluetoothBean;
import com.yiyun.rmj.bean.BluetoothSettingBean;
import com.yiyun.rmj.bean.apibean.GetVersionBean;
import com.yiyun.rmj.bean.apiparm.GetVersionParm;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.utils.PackageUpdateUtil;
import com.yiyun.rmj.utils.PermissionUtil;
import com.yiyun.rmj.utils.SpfUtils;
import com.yiyun.rmj.utils.StatusBarUtil;

import org.litepal.crud.DataSupport;

import cn.jake.share.frdialog.dialog.FRDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SettingActivity extends BaseActivity implements View.OnClickListener{

    private String downLoadPath = Environment.getExternalStorageDirectory() + "/";
    private String downloadFileName;
    private String downloadUrl;
    private FRDialog updateDialog;
    private ProgressBar downloadProgress;
    private TextView tv_progress;
    private RelativeLayout rl_progress;
    private int versionCode;
    private ImageView tv_update_flag;
    private TextView dialog_tv_version, dialog_tv_currentversion;

    Handler mhanlder = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch(msg.what){
                case 1:
                    //更新进度
                    rl_progress.setVisibility(View.VISIBLE);
                    Bundle bundle = msg.getData();
                    int progress = bundle.getInt("progress");
//                    downloadProgress.setProgress(progress);
                    tv_progress.setText(progress + "%");
                    break;
                case 2:
                    updateDialog.dismiss();
                    //安装应用
                    PackageUpdateUtil.install(SettingActivity.this,downLoadPath + downloadFileName);
                    break;
            }

        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.iv_back:
                finish();
                break;

            case R.id.rl_account_safe:
                //账户安全
                Intent safeAccountIntent = new Intent(SettingActivity.this, SafeAccountActivity.class);
                startActivity(safeAccountIntent);
                break;
                case R.id.rl_about_us:
                //关于我们
                Intent aboultUsIntent = new Intent(SettingActivity.this, AboutUsActivity.class);
                startActivity(aboultUsIntent);
                break;
            case R.id.rl_update:
                //系统升级
                if(PackageUpdateUtil.getInstance().getVersionCode(SettingActivity.this) < versionCode){
                    //版本不是最新，需要更新版本
                    updateDialog.show();
                }else{
                    ToastUtils.show("已经是最新版本了");
                }
                break;

            case R.id.rl_clean_cache:
                //清除缓存
                DataSupport.deleteAll(BluetoothBean.class);
                DataSupport.deleteAll(BluetoothSettingBean.class);
                ToastUtils.show("清除成功");
                break;

            case R.id.btn_login_out:
                //安全退出
                PermissionUtil.requestStoragePermission(SettingActivity.this, new PermissionUtil.IRequestPermissionCallBack() {
                    @Override
                    public void permissionSuccess() {
                        SpfUtils.getSpfUtils(SettingActivity.this).delete();
                        Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.activity_leftclick_in,R.anim.activity_leftclick_out);
                    }
                });

                break;

        }
    }

    public void initUpdateDialog(){

        updateDialog = new FRDialog.CommonBuilder(this).setContentView(R.layout.dialog_update).create();
        updateDialog.setCancelable(false);
        dialog_tv_version = updateDialog.findViewById(R.id.tv_version);
        dialog_tv_currentversion = updateDialog.findViewById(R.id.tv_currentversion);
        TextView tv_cancel = updateDialog.findViewById(R.id.tv_cancel);
        downloadProgress = updateDialog.findViewById(R.id.pb_downlaodprogress);
        tv_progress = updateDialog.findViewById(R.id.tv_progress);
        tv_progress.setText(0 + "%");
        rl_progress = updateDialog.findViewById(R.id.rl_progress);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDialog.dismiss();
            }
        });

        TextView tv_sure = updateDialog.findViewById(R.id.tv_sure);
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionUtil.requestStoragePermission(SettingActivity.this ,new PermissionUtil.IRequestPermissionCallBack() {
                    @Override
                    public void permissionSuccess() {
                        downloadFileName = System.currentTimeMillis() + ".apk";
                        PackageUpdateUtil.getInstance().downloadAPK(downLoadPath, downloadFileName , downloadUrl, new PackageUpdateUtil.IDownLoadCallback() {
                            @Override
                            public void downloadStart() {
                                ToastUtils.show("开始下载");
                                Bundle bundle = new Bundle();
                                bundle.putInt("progress",0);
                                Message message = new Message();
                                message.what = 1;
                                message.setData(bundle);
                                mhanlder.sendMessage(message);
                            }

                            @Override
                            public void downloading(int progress) {
                                Bundle bundle = new Bundle();
                                bundle.putInt("progress",progress);
                                Message message = new Message();
                                message.what = 1;
                                message.setData(bundle);
                                mhanlder.sendMessage(message);
                            }

                            @Override
                            public void downloadComplete() {
                                Message message = new Message();
                                message.what = 2;
                                mhanlder.sendMessage(message);
                            }
                        });
                    }
                });
            }
        });

    }

    /**
     * 获取版本号
     * @param desparms
     */
    public void getVersion(String desparms){
        api.getVersion(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetVersionBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                    }

                    @Override
                    public void onNext(GetVersionBean obj) {
                        LogUtils.LogE("getVersion--onNext:" + gson.toJson(obj));
                        if(obj.getState() == 1){
                            String versionStr = obj.getInfo().getData().getVersion();
                            if(versionStr!=null){
                                String[] strArr = versionStr.split("\\.");
                                versionCode = Integer.parseInt(strArr[strArr.length-1]);
                                downloadUrl = obj.getInfo().getData().getDownloadLink();
                                if(PackageUpdateUtil.getInstance().getVersionCode(SettingActivity.this) < versionCode){
                                    //版本不是最新，需要更新版本
                                    tv_update_flag.setVisibility(View.VISIBLE);
                                    dialog_tv_version.setText("发现新版本(" + versionStr + ")");
                                    dialog_tv_currentversion.setText("当前版本:"  + PackageUpdateUtil.getInstance().getVersion(SettingActivity.this));

                                }else{
                                    tv_update_flag.setVisibility(View.GONE);
                                }
                            }
                        }else{
                        }

                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    public void initTitleView(){

        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.setting));


    }

    @Override
    protected void initView() {
        initTitleView();

        RelativeLayout rl_account_safe = findViewById(R.id.rl_account_safe);
        rl_account_safe.setOnClickListener(this);

        RelativeLayout rl_about_us = findViewById(R.id.rl_about_us);
        rl_about_us.setOnClickListener(this);

        RelativeLayout rl_clean_cache = findViewById(R.id.rl_clean_cache);
        rl_clean_cache.setOnClickListener(this);

        RelativeLayout rl_update = findViewById(R.id.rl_update);
        rl_update.setOnClickListener(this);

        Button btn_login_out = findViewById(R.id.btn_login_out);
        btn_login_out.setOnClickListener(this);

        TextView tv_version_show = findViewById(R.id.tv_version_show);
        tv_version_show.setText("当前版本:" + PackageUpdateUtil.getInstance().getVersion(SettingActivity.this));

        tv_update_flag = findViewById(R.id.tv_update_flag);

        initUpdateDialog();

        GetVersionParm getVersionParm = new GetVersionParm();
        getVersionParm.setDevice("android");
        String getVersionParms = DESHelper.encrypt(gson.toJson(getVersionParm));
        getVersion(getVersionParms);


    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this,true);
    }

    public void requestpession() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            RxPermissions rx = new RxPermissions(this);
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
                                DataSupport.deleteAll(BluetoothBean.class);
                                DataSupport.deleteAll(BluetoothSettingBean.class);

                            } else {
                                Toast.makeText(SettingActivity.this,"打开权限才能使用这个功能哦!~",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

        }else{

        }
    }

}
