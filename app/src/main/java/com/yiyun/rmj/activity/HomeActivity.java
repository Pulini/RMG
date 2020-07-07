package com.yiyun.rmj.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.yiyun.rmj.R;
import com.yiyun.rmj.activity.bluetooth.BluetoothSelectDeviceActivity;
import com.yiyun.rmj.adapter.MyHomeAdapter;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.apibase.BaseParm;
import com.yiyun.rmj.bean.apibean.GetVersionBean;
import com.yiyun.rmj.bean.apibean.ProductBean;
import com.yiyun.rmj.bean.apibean.RotationBean;
import com.yiyun.rmj.bean.apiparm.GetVersionParm;
import com.yiyun.rmj.dialog.CustomerServiceDialog;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.utils.PackageUpdateUtil;
import com.yiyun.rmj.utils.PermissionUtil;
import com.yiyun.rmj.utils.SpfUtils;
import com.yiyun.rmj.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jake.share.frdialog.dialog.FRDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private SmartRefreshLayout smf;
    private int page = 1;
    private MyHomeAdapter adapter;
    private ArrayList<ProductBean.Data> list;
    private ArrayList<ProductBean.Data> videoList;
    private ArrayList<ProductBean.Data> productList;
    private List<RotationBean.SlidePicture> imgesUrlBanner;
    ImageView iv_control;//控制悬浮按钮
    RelativeLayout rl_menu_container;
    LinearLayout ll_portrait_menu;
    TextView tv_person_center;
    TextView tv_custom_service;
    private FRDialog updateDialog;
    private Gson gson;
    private RequestOptions myOptions;
    private TextView dialog_tv_version, dialog_tv_currentversion;

    ConvenientBanner mBanner;

    boolean isTouchUp = true;  //用户没有触碰屏幕
    boolean isRecyScrollStop = true; //RecyView滑动停止

    private boolean isMenuButtonOpen = false;

    private String downLoadPath = Environment.getExternalStorageDirectory() + "/";
    private String downloadFileName;
    private String downloadUrl;
    //    private ProgressBar downloadProgress;
    private TextView tv_progress;
    private RelativeLayout rl_progress;

    private Integer[] imageResource = {R.mipmap.glasses_icon, R.mipmap.consum_ables_icon, R.mipmap.glasses_case_icon};


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
//                    downloadProgress.setProgress(progress);
                    tv_progress.setText(progress + "%");
                    break;
                case 2:
                    updateDialog.dismiss();
                    //安装应用
                    PackageUpdateUtil.install(HomeActivity.this, downLoadPath + downloadFileName);
                    break;
                case 3:
//                    Bundle bundle1 = ;
                    int index = msg.getData().getInt("index");
                    MyHomeAdapter.MyViewHolder my = (MyHomeAdapter.MyViewHolder) recylist.findViewHolderForAdapterPosition(index + 1);
                    my.iv_videoBKG.setImageBitmap(videoList.get(index).VideoImage);
                    my.iv_videoBKG.setVisibility(View.VISIBLE);
                    my.iv_videoPreloadBKG.setVisibility(View.GONE);

//                                            mhanlder.post(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    adapter.notifyItemChanged(i);
////                                                    adapter.notifyDataSetChanged();
//                                                }
//                                            });

//                                mhanlder.sendEmptyMessage(3);
//                    }


//                    Bundle bundle1 = msg.getData();
//                    int index = bundle1.getInt("index");
//                    adapter.notifyItemChanged(index + 1);
//                    MyHomeAdapter.MyViewHolder my = (MyHomeAdapter.MyViewHolder) recylist.findViewHolderForAdapterPosition(index+1);
//                    my.iv_videoBKG.setImageBitmap(videoList.get(index).VideoImage);
//                    my.iv_videoBKG.setVisibility(View.VISIBLE);
//                    my.iv_videoPreloadBKG.setVisibility(View.GONE);

//                    adapter.notifyDataSetChanged();
                    break;
            }

        }
    };

    RecyclerView recylist;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    public void initTitleView() {
        ImageView iv_shopping_cart = findViewById(R.id.iv_shopping_cart);
        iv_shopping_cart.setVisibility(View.VISIBLE);
        iv_shopping_cart.setOnClickListener(this);

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.home));
    }

    @Override
    protected void initView() {
        initTitleView();
        gson = new Gson();

        list = new ArrayList<>();
        View header = LayoutInflater.from(this).inflate(R.layout.item_product_head, null, false);
        mBanner = header.findViewById(R.id.banner_1);
        //设置高度是屏幕1/4
        mBanner.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, getWindowManager().getDefaultDisplay().getHeight() / 4));

        imgesUrlBanner = new ArrayList<>();
        videoList = new ArrayList<>();
        productList = new ArrayList<>();

        myOptions = new RequestOptions()
                .error(R.mipmap.ic_launcher);

        ImageView iv_menu_btn = findViewById(R.id.iv_menu_btn);
        iv_menu_btn.setOnClickListener(this);

        tv_custom_service = findViewById(R.id.tv_custom_service);
        tv_custom_service.setOnClickListener(this);

        rl_menu_container = findViewById(R.id.rl_menu);
        ll_portrait_menu = findViewById(R.id.ll_portrait_menu);

        tv_person_center = findViewById(R.id.tv_person_center);
        tv_person_center.setOnClickListener(this);

        iv_control = findViewById(R.id.iv_control);
        iv_control.setOnClickListener(this);

        ll_portrait_menu.setVisibility(View.INVISIBLE);
        tv_person_center.setVisibility(View.INVISIBLE);
        tv_custom_service.setClickable(false);
        tv_person_center.setClickable(false);

        recylist = findViewById(R.id.recycle_product);
        recylist.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isTouchUp = false;
                        startAnimationToHide();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        isTouchUp = true;
                        startAnimationToShow();
                        break;
                }
                return false;
            }
        });
        recylist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        isRecyScrollStop = false;
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        isRecyScrollStop = true;
                        startAnimationToShow();
                        break;
                }
            }
        });
        //设置线性布局
        recylist.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyHomeAdapter(this, list);
        initBanner();
        adapter.setmHeaderView(mBanner);

        recylist.setAdapter(adapter);

        smf = findViewById(R.id.smf);

        smf.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                page = 1;
                initData();
                smf.finishRefresh(2000);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isTouchUp = true;
                        startAnimationToShow();
                    }
                }, 2000);

            }
        });


        initUpdateDialog();
//        updateDialog.show();
    }


    private void initBanner() {

        mBanner.setPages(new CBViewHolderCreator<NetWorkImageHolderView>() {
            @Override
            public NetWorkImageHolderView createHolder() {
                return new NetWorkImageHolderView();
            }
        }, imgesUrlBanner)
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .setPageIndicator(new int[]{R.mipmap.home_lunbo_button01, R.mipmap.home_lunbo_button02})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setScrollDuration(1500);

        mBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
                intent.putExtra("productId", imgesUrlBanner.get(position).getProductId());
                intent.putExtra("showFlag", 1); //商品详情
                startActivity(intent);
//                Intent intent = new Intent(HomeActivity.this, MyWebActivity.class);
//                intent.putExtra("url", imgesUrlBanner.get(position).getUrl());
//                intent.putExtra("title", imgesUrlBanner.get(position).getTitle());
//                startActivity(intent);
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
                PermissionUtil.requestStoragePermission(HomeActivity.this, new PermissionUtil.IRequestPermissionCallBack() {
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


    public void startAnimationToShow() {
        //滑动停止开始展示按钮
        if (isTouchUp && isRecyScrollStop) {
            Animation hyperspaceJumpAnimation =
                    AnimationUtils.loadAnimation(this, R.anim.view_show_from_left);
            hyperspaceJumpAnimation.setFillAfter(true);
            iv_control.startAnimation(hyperspaceJumpAnimation);

            Animation hyperspaceJumpAnimation1 =
                    AnimationUtils.loadAnimation(this, R.anim.view_show_from_right);
            hyperspaceJumpAnimation1.setFillAfter(true);
            rl_menu_container.startAnimation(hyperspaceJumpAnimation1);
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
        rl_menu_container.startAnimation(hyperspaceJumpAnimation1);
    }

    @Override
    protected void initData() {
        BaseParm parm = new BaseParm();
        String parmStr = gson.toJson(parm);
        String parmDes = DESHelper.encrypt(parmStr);
        getRotation(parmDes);
        getCommodityList(parmDes);

        //每次打开App的时候检测
        if (SpfUtils.getSpfUtils(HomeActivity.this).isNeedUpdate()) {
            SpfUtils.getSpfUtils(HomeActivity.this).setIsNeedUpdate(false);
            GetVersionParm getVersionParm = new GetVersionParm();
            getVersionParm.setDevice("android");
            String getVersionParms = DESHelper.encrypt(gson.toJson(getVersionParm));
            getVersion(getVersionParms);
        }

    }

    /**
     * 获取版本号
     *
     * @param desparms
     */
    public void getVersion(String desparms) {
        api.getVersion(desparms)
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
                                if (PackageUpdateUtil.getInstance().getVersionCode(HomeActivity.this) < versionCode) {
                                    //版本不是最新，需要更新版本
                                    dialog_tv_version.setText("发现新版本(" + versionStr + ")");
                                    dialog_tv_currentversion.setText("当前版本:" + PackageUpdateUtil.getInstance().getVersion(HomeActivity.this));
                                    updateDialog.show();
                                }
                            }
                        } else {
                        }

                    }
                });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_control:
                //前往蓝牙模块
                Intent selectDeviceIntent = new Intent(HomeActivity.this, BluetoothSelectDeviceActivity.class);
                startActivity(selectDeviceIntent);

                break;
            case R.id.tv_custom_service:
                CustomerServiceDialog dialog = new CustomerServiceDialog(this, new CustomerServiceDialog.ICallBack() {

                    @Override
                    public void onAfterSale() {
                        Intent afterSaleIntent = new Intent(HomeActivity.this, CustomServiceActivity.class);
                        afterSaleIntent.putExtra("type", CustomServiceActivity.TYPE_AFTER_SERVICE);
                        startActivity(afterSaleIntent);
                    }

                    @Override
                    public void onPreSale() {
                        Intent preSaleIntent = new Intent(HomeActivity.this, CustomServiceActivity.class);
                        preSaleIntent.putExtra("type", CustomServiceActivity.TYPE_PRE_SERVICE);
                        startActivity(preSaleIntent);
                    }
                });
                dialog.show();
                break;
            case R.id.iv_shopping_cart:
                Intent intentCart = new Intent(HomeActivity.this, ShoppingCartActivity.class);
                startActivity(intentCart);
                break;
            case R.id.iv_menu_btn:
                changeMenuButtonState();
                break;
            case R.id.tv_person_center:
                //个人中心
                Intent personcenterIntent = new Intent(HomeActivity.this, PersonCenterActivity.class);
                startActivity(personcenterIntent);
                break;
        }
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

    @Override
    public void onResume() {
        super.onResume();
        mBanner.startTurning(5000);
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

    /**
     * 获取轮播图
     */
    public void getRotation(String desparms) {
        api.getRotation(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RotationBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                        LogUtils.LogE("error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(RotationBean obj) {
                        LogUtils.LogE("HomeActivity--getRotation:" + obj.getState());
                        if (obj.getState() == 1) {
                            ArrayList<RotationBean.SlidePicture> datas = obj.getInfo().getData();
                            if (datas != null && datas.size() > 0) {
                                imgesUrlBanner.clear();
                                videoList.clear();
                                ProductBean.Data videoData;
                                for (RotationBean.SlidePicture data : datas) {
                                    if (data.getSlideshowPicture().endsWith(".mp4") ||
                                            data.getSlideshowPicture().endsWith(".avi") ||
                                            data.getSlideshowPicture().endsWith(".3gp")) {
                                        videoData = new ProductBean.Data();
                                        videoData.VideoUrl = data.getSlideshowPicture();
                                        videoList.add(videoData);
                                    } else {
                                        imgesUrlBanner.add(data);
                                    }
                                }
                                LogUtils.LogE("1videoList=" + videoList.size());
                                list.clear();
                                if (videoList.size() > 0) {
                                    list.addAll(videoList);
                                }
                                list.addAll(productList);
                                adapter.notifyDataSetChanged();
                                mBanner.notifyDataSetChanged();
                                if (videoList.size() > 0) {
                                    LogUtils.LogE("截取首帧");
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            smf.setEnableRefresh(false);
                                            for (int i = 0; i < videoList.size(); i++) {
                                                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                                                mmr.setDataSource(videoList.get(i).VideoUrl, new HashMap<String, String>());
                                                videoList.get(i).VideoImage=mmr.getFrameAtTime();

                                                Bundle bundle = new Bundle();
                                                bundle.putInt("index",i);
                                                Message message = new Message();
                                                message.what = 3;
                                                message.setData(bundle);
                                                mhanlder.sendMessage(message);

//                                                mhanlder.sendEmptyMessage(3);
                                            }
                                            smf.setEnableRefresh(true);
                                        }
                                    }).start();
                                }


                            }
//                            adapter.notifyHeadDataChanged();
                        } else if (obj.getState() == -1) {
                            startlogin();
                        } else {
                            showConnectError(obj.getInfo().getMessage());
                        }

                    }
                });
    }


    /**
     * 获取产品列表
     */
    public void getCommodityList(String desparms) {
        api.getCommodityList(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ProductBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                    }

                    @Override
                    public void onNext(ProductBean obj) {
                        LogUtils.LogE("getCommodityList--onNext:" + gson.toJson(obj));
                        if (obj.getState() == 1) {
                            productList.clear();
                            productList.addAll(obj.getInfo().getData());
                            list.clear();
                            list.addAll(videoList);
                            list.addAll(productList);
                            adapter.notifyDataSetChanged();
                            LogUtils.LogE("2videoList=" + videoList.size());
                        } else if (obj.getState() == -1) {
                            startlogin();
                        } else {
                            showConnectError(obj.getInfo().getMessage());
                        }

                    }
                });

    }

    public class NetWorkImageHolderView implements Holder<RotationBean.SlidePicture> {

        private ImageView imageView;
//        private VideoView video;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.rv_header_img, null, false);
            imageView = view.findViewById(R.id.iv_head);
//            video = view.findViewById(R.id.vv_player);
//            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    mp.setVolume(0f, 0f);
//                    mp.start();
//                    mp.setLooping(true);
//                }
//            });
//            imageView.setScaleType(ImageView.ScaleType.CENTER);
            return view;
        }

        @Override
        public void UpdateUI(Context context, int position, RotationBean.SlidePicture data) {
            //Glide.with(context).load(data.getImgUrl()).into(imageView);
            Log.d("imgUrl", "UpdateUI: " + data.getSlideshowPicture());
//            if (data.getSlideshowPicture().endsWith(".mp4") ||
//                    data.getSlideshowPicture().endsWith(".avi") ||
//                    data.getSlideshowPicture().endsWith(".3gp")) {
//                video.setVisibility(View.VISIBLE);
//                imageView.setVisibility(View.GONE);
//                video.setVideoPath(data.getSlideshowPicture());
//                video.requestFocus();
//                video.start();
//            } else {
//                video.setVisibility(View.GONE);
//                imageView.setVisibility(View.VISIBLE);
            Glide.with(context).load(data.getSlideshowPicture()).into(imageView);
//            }
        }
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this, true);
    }
}
