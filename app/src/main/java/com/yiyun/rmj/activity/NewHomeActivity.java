package com.yiyun.rmj.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjq.toast.ToastUtils;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.helpdesk.callback.Callback;
import com.hyphenate.helpdesk.easeui.util.IntentBuilder;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.GSYVideoHelper;
import com.yiyun.rmj.R;
import com.yiyun.rmj.activity.bluetooth.BluetoothSelectDeviceActivity;
import com.yiyun.rmj.activity.bluetooth.BluetoothSelectDeviceActivity2;
import com.yiyun.rmj.adapter.HomeAdapter;
import com.yiyun.rmj.adapter.HomeVideo2Adapter;
import com.yiyun.rmj.adapter.MyHomeAdapter;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.VideoModel;
import com.yiyun.rmj.bean.apibase.BaseParm;
import com.yiyun.rmj.bean.apibean.GetVersionBean;
import com.yiyun.rmj.bean.apibean.ProductBean;
import com.yiyun.rmj.bean.apibean.RotationBean;
import com.yiyun.rmj.bean.apiparm.AddressParm;
import com.yiyun.rmj.bean.apiparm.GetVersionParm;
import com.yiyun.rmj.dialog.CustomerServiceDialog;
import com.yiyun.rmj.hx.ChatActivity;
import com.yiyun.rmj.utils.Ali;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.utils.PackageUpdateUtil;
import com.yiyun.rmj.utils.PermissionUtil;
import com.yiyun.rmj.utils.SpfUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import cn.jake.share.frdialog.dialog.FRDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * File Name : HomeActivity2
 * Created by : PanZX on  2020/6/2 20:38
 * Email : 644173944@qq.com
 * Github : https://github.com/Pulini
 * Remark：
 */
//@RequiresApi(api = Build.VERSION_CODES.M)
public class NewHomeActivity extends BaseActivity {

    private SmartRefreshLayout smf;
    private RecyclerView rv_home;

    private ImageView iv_control;
    private RelativeLayout rl_menu;
    private ImageView iv_menu_btn;
    private LinearLayout ll_portrait_menu;
    private TextView tv_custom_service;
    private TextView tv_person_center;

    private List<RotationBean.SlidePicture> images = new ArrayList<>();
    private List<VideoModel> videos = new ArrayList<>();
    private List<ProductBean.Data> products = new ArrayList<>();
    private String downloadUrl;
    private String downloadFileName;
    private String downLoadPath = Environment.getExternalStorageDirectory() + "/";
    private FRDialog updateDialog;
    private TextView dialog_tv_version, dialog_tv_currentversion, tv_progress;
    private RelativeLayout rl_progress;
    String parmDes;
    boolean isgetRotationFinish = true;
    boolean isgetCommodityListFinish = true;
    private HomeAdapter ha;
    private boolean isMenuButtonOpen = false;
    boolean isRecyScrollStop = true; //RecyView滑动停止
    CustomerServiceDialog dialog;
    Handler mhanlder = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 1:
                    //更新进度
                    rl_progress.setVisibility(View.VISIBLE);
                    Bundle bundle = msg.getData();
                    int progress = bundle.getInt("progress");
                    tv_progress.setText(progress + "%");
                    break;
                case 2:
                    updateDialog.dismiss();
                    //安装应用
                    PackageUpdateUtil.install(NewHomeActivity.this, downLoadPath + downloadFileName);
                    break;
                case 3:
//                    int index = msg.getData().getInt("index");
//                    HomeVideoAdapter.HomeVideoViewHolder my = (HomeVideoAdapter.HomeVideoViewHolder) rv_video.findViewHolderForAdapterPosition(index);
//                    my.iv_videoBKG.setImageBitmap(videos.get(index).VideoImage);
//                    my.iv_videoBKG.setVisibility(View.VISIBLE);
//                    my.iv_videoPreloadBKG.setVisibility(View.GONE);
//                    int index = msg.getData().getInt("index");
//                    HomeAdapter.VideoHolder holder = (HomeAdapter.VideoHolder) rv_home.findViewHolderForAdapterPosition(2);
//                    HomeVideoAdapter.HomeVideoViewHolder item = (HomeVideoAdapter.HomeVideoViewHolder) holder.video.findViewHolderForAdapterPosition(index);
//                    item.iv_videoBKG.setImageBitmap(videos.get(index).VideoImage);
//                    item.iv_videoBKG.setVisibility(View.VISIBLE);
//                    item.iv_videoPreloadBKG.setVisibility(View.GONE);
                    break;
                case 4:
                    if (rv_home.getScrollState() != 0) {
                        startAnimationToHide();
                    } else {
                        startAnimationToShow();
                    }
                    mhanlder.sendEmptyMessageDelayed(4, 1000);
                    break;
            }
        }
    };
    private Context context;
    private FRDialog needPermissionDialog;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_home2;
    }

    @Override
    protected void initView() {
        context = this;
        setTitle();
        findView();
        setView();
        initUpdateDialog();
    }

    public void line() {

        if (ChatClient.getInstance().isLoggedInBefore()) {

            Intent intent = new IntentBuilder(context)
                    .setTargetClass(ChatActivity.class)
                    .setServiceIMNumber("kefuchannelimid_264622") //获取地址：kefu.easemob.com，“管理员模式 > 渠道管理 > 手机APP”页面的关联的“IM服务号”
                    .setTitleName("客服中心")
                    .setShowUserNick(true)
                    .build();
            startActivity(intent);


            Log.e("Pan", "已经登录");
//            Intent intent = new IntentBuilder(context)
//                    .setServiceIMNumber("kefuchannelimid_264622") //获取地址：kefu.easemob.com，“管理员模式 > 渠道管理 > 手机APP”页面的关联的“IM服务号”
//                    .setTitleName("客服中心")
//                    .setShowUserNick(true)
//                    .build();
//            startActivity(intent);
            //已经登录，可以直接进入会话界面
        } else {
            Log.e("Pan", "未登录");
            ChatClient.getInstance().login(SpfUtils.getSpfUtils(context).getHXName(), SpfUtils.getSpfUtils(context).getHXPwd(), new Callback() {
                @Override
                public void onSuccess() {
                    Log.e("Pan", "登录成功");
                    Intent intent = new IntentBuilder(context)
                            .setTargetClass(ChatActivity.class)
                            .setServiceIMNumber("kefuchannelimid_264622") //获取地址：kefu.easemob.com，“管理员模式 > 渠道管理 > 手机APP”页面的关联的“IM服务号”
                            .setTitleName("客服中心")
                            .setShowUserNick(true)
                            .build();
                    startActivity(intent);
                }

                @Override
                public void onError(int code, String error) {
                    Log.e("Pan", code + "登录失败=" + error);
                }

                @Override
                public void onProgress(int progress, String status) {

                }
            });
        }

    }

    public void stopVideo() {
//        for (int i = 0; i < rv_home.getChildCount(); i++) {
//            if(rv_home.findViewHolderForAdapterPosition(i) instanceof HomeAdapter.VideoHolder){
//                HomeAdapter.VideoHolder videoHolder = (HomeAdapter.VideoHolder) rv_home.findViewHolderForAdapterPosition(i);
//                Log.e("Pan", "video  getChildCount="+videoHolder.video.getChildCount() );
//                for (int j = 0; j < videoHolder.video.getChildCount(); j++) {
//                    HomeVideo2Adapter.HomeVideoViewHolder hvvh = (HomeVideo2Adapter.HomeVideoViewHolder) videoHolder.video.findViewHolderForAdapterPosition(j);
//                    if (hvvh.gsyvp_player.isInPlayingState()) {
//                        hvvh.gsyvp_player.onVideoPause();
//                    }
//                }
//            }
//        }
    }

    public void findView() {
        smf = findViewById(R.id.smf);
        rv_home = findViewById(R.id.rv_home);
        iv_control = findViewById(R.id.iv_control);
        rl_menu = findViewById(R.id.rl_menu);
        iv_menu_btn = findViewById(R.id.iv_menu_btn);
        ll_portrait_menu = findViewById(R.id.ll_portrait_menu);
        tv_custom_service = findViewById(R.id.tv_custom_service);
        tv_person_center = findViewById(R.id.tv_person_center);
    }

    public void setView() {
        ha = new HomeAdapter(this, getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight());
        rv_home.setLayoutManager(new LinearLayoutManager(this));
        rv_home.setAdapter(ha);
//        rv_home.addItemDecoration(new SpacesItemDecoration(QMUIDisplayHelper.dp2px(this, 5)));


        smf.setEnableLoadMore(false);
        smf.setOnRefreshListener(refreshlayout -> getCommodityList());

        iv_control.setOnClickListener(v -> {
//                Ali.LoginIM(getApplicationContext());
            String token = SpfUtils.getSpfUtils(getApplicationContext()).getToken();
            if (token.isEmpty()) {
                startlogin();
            } else {
                startActivity(new Intent(context, BluetoothSelectDeviceActivity.class));
            }
        });
        tv_person_center.setOnClickListener(v -> startActivity(new Intent(context, PersonCenterActivity.class)));
        tv_custom_service.setOnClickListener(v -> {
//                dialog.show();
            String token = SpfUtils.getSpfUtils(getApplicationContext()).getToken();
            if (token.isEmpty()) {
                startlogin();
            } else {
                line();
            }
        });
        dialog = new CustomerServiceDialog(context, new CustomerServiceDialog.ICallBack() {
            @Override
            public void onAfterSale() {
                startActivity(new Intent(context, CustomServiceActivity.class).putExtra("type", CustomServiceActivity.TYPE_AFTER_SERVICE));
            }

            @Override
            public void onPreSale() {
                startActivity(new Intent(context, CustomServiceActivity.class).putExtra("type", CustomServiceActivity.TYPE_PRE_SERVICE));
            }
        });


        iv_menu_btn.setOnClickListener(v -> changeMenuButtonState());

        rv_home.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        isRecyScrollStop = false;
                        startAnimationToHide();
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        isRecyScrollStop = true;
                        startAnimationToShow();
                        break;
                }
            }
        });

    }

    public void goDetail(int id) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("productId", id);
        intent.putExtra("showFlag", 1); //了解更多
        startActivity(intent);
    }

    public void setTitle() {
        ImageView iv_shopping_cart = findViewById(R.id.iv_shopping_cart);
        iv_shopping_cart.setVisibility(View.VISIBLE);
        iv_shopping_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ShoppingCartActivity.class));
            }
        });
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.home));
    }

    @Override
    protected void initData() {
        BaseParm parm = new BaseParm();
        String parmStr = gson.toJson(parm);
        parmDes = DESHelper.encrypt(parmStr);
        getCommodityList();

        Log.e("Pan", "isNeedUpdate=" + SpfUtils.getSpfUtils(NewHomeActivity.this).isNeedUpdate());
        //每次打开App的时候检测
        if (SpfUtils.getSpfUtils(NewHomeActivity.this).isNeedUpdate()) {
            SpfUtils.getSpfUtils(NewHomeActivity.this).setIsNeedUpdate(false);
            GetVersionParm getVersionParm = new GetVersionParm();
            getVersionParm.setDevice("android");
            String getVersionParms = DESHelper.encrypt(gson.toJson(getVersionParm));
            Log.e("Pan", "----------------检查更新------------------");
            getVersion(getVersionParms);
        }
    }

    private void getCommodityList() {
        isgetCommodityListFinish = false;
        api.getCommodityList(parmDes)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ProductBean>() {
                    @Override
                    public void onCompleted() {
                        isgetCommodityListFinish = true;
                        if (isgetRotationFinish) {
                            smf.finishRefresh();
                        }
                        getRotation();
                    }

                    @Override
                    public void onError(Throwable e) {
                        isgetCommodityListFinish = true;
                        if (isgetRotationFinish) {
                            smf.finishRefresh();
                        }
                        showConnectError();
                        getRotation();
                    }

                    @Override
                    public void onNext(ProductBean obj) {
                        getRotation();
                        isgetCommodityListFinish = true;
                        if (isgetRotationFinish) {
                            smf.finishRefresh();
                        }
                        LogUtils.LogE("getCommodityList--onNext:" + gson.toJson(obj));
                        if (obj.getState() == 1) {
                            products.clear();
                            products.addAll(obj.getInfo().getData());
                            ha.setProductData(products, new HomeAdapter.OnProductClickListener() {
                                @Override
                                public void ProductItemClick(int id) {
                                    goDetail(id);
                                }
                            });
                            LogUtils.LogE("products=" + products.size());
                        } else if (obj.getState() == -1) {
                            startlogin();
                        } else {
                            showConnectError(obj.getInfo().getMessage());
                        }

                    }
                });
    }

    private void getVersion(String desParams) {
        api.getVersion(desParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetVersionBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("bczonerror", e.getMessage());
                        showConnectError();
                    }

                    @Override
                    public void onNext(GetVersionBean obj) {
                        LogUtils.LogE("getVersion--onNext:" + gson.toJson(obj));
                        if (obj.getState() == 1) {
                            String versionStr = obj.getInfo().getData().getVersion();
                            if (versionStr != null) {
                                String[] strArr = versionStr.split("\\.");
                                int versionCode = Integer.parseInt(strArr[strArr.length - 1]);
                                downloadUrl = obj.getInfo().getData().getDownloadLink();
                                if (PackageUpdateUtil.getInstance().getVersionCode(context) < versionCode) {
                                    //版本不是最新，需要更新版本
                                    dialog_tv_version.setText("发现新版本(" + versionStr + ")");
                                    dialog_tv_currentversion.setText("当前版本:" + PackageUpdateUtil.getInstance().getVersion(context));
                                    updateDialog.show();
                                }
                            }
                        } else {
                        }

                    }
                });
    }

    public void initUpdateDialog() {

        updateDialog = new FRDialog.CommonBuilder(this).setContentView(R.layout.dialog_update).create();
        updateDialog.setCancelable(false);
        dialog_tv_version = updateDialog.findViewById(R.id.tv_version);
        dialog_tv_currentversion = updateDialog.findViewById(R.id.tv_currentversion);
        TextView tv_cancel = updateDialog.findViewById(R.id.tv_cancel);
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
                PermissionUtil.requestStoragePermission(NewHomeActivity.this, new PermissionUtil.IRequestPermissionCallBack() {
                    @Override
                    public void permissionSuccess() {
                        downloadFileName = System.currentTimeMillis() + ".apk";
                        PackageUpdateUtil.getInstance().downloadAPK(downLoadPath, downloadFileName, downloadUrl, new PackageUpdateUtil.IDownLoadCallback() {
                            @Override
                            public void downloadStart() {
                                ToastUtils.show("开始下载");
                                Bundle bundle = new Bundle();
                                bundle.putInt("progress", 0);
                                Message message = new Message();
                                message.what = 1;
                                message.setData(bundle);
                                mhanlder.sendMessage(message);
                            }

                            @Override
                            public void downloading(int progress) {
                                Bundle bundle = new Bundle();
                                bundle.putInt("progress", progress);
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

    private void getRotation() {
        isgetRotationFinish = false;

        api.getRotation(parmDes)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RotationBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        isgetRotationFinish = true;
                        showConnectError();
                        LogUtils.LogE("error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(RotationBean obj) {
                        isgetRotationFinish = true;
                        if (isgetCommodityListFinish) {
                            smf.finishRefresh();
                        }
                        LogUtils.LogE("HomeActivity--getRotation:" + obj.getState());
                        if (obj.getState() == 1) {
                            ArrayList<RotationBean.SlidePicture> datas = obj.getInfo().getData();
                            if (datas != null && datas.size() > 0) {
                                images.clear();
                                videos.clear();
                                VideoModel videoData;
                                for (RotationBean.SlidePicture data : datas) {
                                    if (data.getSlideshowPicture().endsWith(".mp4") ||
                                            data.getSlideshowPicture().endsWith(".avi") ||
                                            data.getSlideshowPicture().endsWith(".3gp")) {
                                        videoData = new VideoModel();
                                        videoData.VideoUrl = data.getSlideshowPicture();
                                        videos.add(videoData);
                                    } else {
                                        images.add(data);
                                    }
                                }
                                LogUtils.LogE("images=" + images.size());
                                LogUtils.LogE("videos=" + videos.size());
                                ha.setBannerAndVideoData(videos, images, new HomeAdapter.OnImageClickListener() {
                                    @Override
                                    public void ImageItemClick(int id) {
                                        goDetail(id);
                                    }
                                });
                            }
                        } else if (obj.getState() == -1) {
                            startlogin();
                        } else {
                            showConnectError(obj.getInfo().getMessage());
                        }

                    }
                });
    }


    public void startAnimationToShow() {
        //滑动停止开始展示按钮
        if (isRecyScrollStop) {
            Animation hyperspaceJumpAnimation =
                    AnimationUtils.loadAnimation(this, R.anim.view_show_from_left);
            hyperspaceJumpAnimation.setFillAfter(true);
            iv_control.startAnimation(hyperspaceJumpAnimation);

            Animation hyperspaceJumpAnimation1 =
                    AnimationUtils.loadAnimation(this, R.anim.view_show_from_right);
            hyperspaceJumpAnimation1.setFillAfter(true);
            rl_menu.startAnimation(hyperspaceJumpAnimation1);
        }
    }

    public void startAnimationToHide() {
        if (isMenuButtonOpen) {
            startCloseMenuAnimation();
        }

        Animation hyperspaceJumpAnimation =
                AnimationUtils.loadAnimation(this, R.anim.view_hide_to_left);
        hyperspaceJumpAnimation.setFillAfter(true);
        iv_control.startAnimation(hyperspaceJumpAnimation);

        Animation hyperspaceJumpAnimation1 =
                AnimationUtils.loadAnimation(this, R.anim.view_hide_to_right);
        hyperspaceJumpAnimation1.setFillAfter(true);
        rl_menu.startAnimation(hyperspaceJumpAnimation1);
    }

    public void startCloseMenuAnimation() {
        isMenuButtonOpen = false;
        Animation menuHideAnimation =
                AnimationUtils.loadAnimation(this, R.anim.view_portrait_menu_hide);
        menuHideAnimation.setFillAfter(true);
        ll_portrait_menu.startAnimation(menuHideAnimation);

        Animation menuHideAnimation1 =
                AnimationUtils.loadAnimation(this, R.anim.view_landscape_menu_hide);
        menuHideAnimation1.setFillAfter(true);
        tv_person_center.startAnimation(menuHideAnimation1);

        tv_custom_service.setClickable(false);
        tv_person_center.setClickable(false);
    }


    public void startOpenMenuAnimation() {
        isMenuButtonOpen = true;
        Animation hyperspaceJumpAnimation =
                AnimationUtils.loadAnimation(this, R.anim.view_portrait_menu_show);
        hyperspaceJumpAnimation.setFillAfter(true);
        ll_portrait_menu.startAnimation(hyperspaceJumpAnimation);

        Animation hyperspaceJumpAnimation1 =
                AnimationUtils.loadAnimation(this, R.anim.view_landscape_menu_show);
        hyperspaceJumpAnimation1.setFillAfter(true);
        tv_person_center.startAnimation(hyperspaceJumpAnimation1);

        tv_custom_service.setClickable(true);
        tv_person_center.setClickable(true);
        ll_portrait_menu.setVisibility(View.VISIBLE);
        tv_person_center.setVisibility(View.VISIBLE);


    }

    public void changeMenuButtonState() {
        if (isMenuButtonOpen) {
            startCloseMenuAnimation();
        } else {
            startOpenMenuAnimation();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 10086) {
            line();
        }
    }

    @Override
    public void onBackPressed() {
        if (GSYVideoManager.isFullState(this)) {
            GSYVideoManager.backFromWindowFull(context);
        }
//        else {
//            System.exit(0);
//        }
    }


    @Override
    public void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }
}
