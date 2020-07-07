package com.yiyun.rmj.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.huantansheng.easyphotos.setting.Setting;
import com.yiyun.rmj.R;
import com.yiyun.rmj.adapter.CommonRecyclerViewAdapter;
import com.yiyun.rmj.adapter.CommonRecyclerViewHolder;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.AddEvaluationParm;
import com.yiyun.rmj.bean.OrderInfo;
import com.yiyun.rmj.bean.OrderProductInfo;
import com.yiyun.rmj.bean.ProductEvaluationInfo;
import com.yiyun.rmj.bean.apibase.BaseBean;
import com.yiyun.rmj.bean.apibean.UpHeadImageBean;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.GlideEngine;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.utils.PermissionUtil;
import com.yiyun.rmj.utils.StatusBarUtil;
import com.yiyun.rmj.view.RatingBar;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//评价页面
public class EvaluationActivity extends BaseActivity implements View.OnClickListener {

    ArrayList<OrderProductInfo> productData;
    private final int up_photo_max_num = 5;
    private OrderInfo orderInfo;
    RecyclerView revy_list;
    AddEvaluationParm evaluationParm;
    ArrayList<CommonRecyclerViewAdapter> pictureAdapters;
    private int currentSelectPosition = 0;


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.btn_submit:
                //提交评价
                if (checkInput()) {
                    formatPictureStr();
                    String strDes = DESHelper.encrypt(gson.toJson(evaluationParm));
                    addEvaluation(strDes);
                }
                break;
        }

    }

    public boolean checkInput() {
        for (ProductEvaluationInfo info : evaluationParm.getEvaluation()) {

            if (info.getStars() < 1) {
                ToastUtils.show("商品评分至少要点亮一颗星哦");
                return false;
            }

            if (TextUtils.isEmpty(info.getContent())) {
                ToastUtils.show("产品评语不能为空哦");
                return false;
            }

        }
        return true;
    }

    public void formatPictureStr() {
        for (ProductEvaluationInfo info : evaluationParm.getEvaluation()) {
            info.picturesArrayToStr();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_evaluation;
    }

    public void initTitleView() {
        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.evaluatation));

    }

    public void takePhoto() {
        EasyPhotos.createAlbum(this, true, GlideEngine.getInstance()).setFileProviderAuthority("com.yiyun.rmj.fileprovider")
                .setCameraLocation(Setting.LIST_FIRST)
                .setCount(up_photo_max_num - evaluationParm.getEvaluation().get(currentSelectPosition).getPicturesArray().size())
                .setPuzzleMenu(false)
                .setCleanMenu(false)
                .start(new SelectCallback() {
                    @Override
                    public void onResult(ArrayList<Photo> photos, boolean isOriginal) {
                        for (Photo str : photos) {
                            LogUtils.LogE("picture_Url:" + str);
                            uploadfile(new File(str.uri.getPath()));
                        }
                    }
                });

//        PermissionUtil.requestStorageCameraPermission(this, new PermissionUtil.IRequestPermissionCallBack() {
//            @Override
//            public void permissionSuccess() {
//                Log.e("bcz","PersonInfomationActivity---start simagepicker");
//                SImagePicker
//                        .from(EvaluationActivity.this)
//                        .rowCount(4)
//                        .showCamera(true)
//                        .maxCount(up_photo_max_num - evaluationParm.getEvaluation().get(currentSelectPosition).getPicturesArray().size())
//                        .pickMode(SImagePicker.MODE_IMAGE)
//                        .forResult(103);
//            }
//        });
    }

    private void initEvaluationParm() {
        evaluationParm = new AddEvaluationParm();
        evaluationParm.setOrderId(orderInfo.getOrderId());
        ArrayList<OrderProductInfo> orderProducts = orderInfo.getOrderItem();
        ArrayList<ProductEvaluationInfo> productInfos = new ArrayList<>();
        for (OrderProductInfo orderProductInfo : orderProducts) {
            ProductEvaluationInfo productEvaluationInfo = new ProductEvaluationInfo();
            productEvaluationInfo.setProductId(orderProductInfo.getProduct_id());
            ArrayList<String> pictureList = new ArrayList<>();
            productEvaluationInfo.setPicturesArray(pictureList);
            productEvaluationInfo.setStars(1);
            productInfos.add(productEvaluationInfo);
        }
        evaluationParm.setEvaluation(productInfos);
    }

    @Override
    protected void initView() {
        initTitleView();
        orderInfo = (OrderInfo) getIntent().getSerializableExtra("orderInfo");
        productData = orderInfo.getOrderItem();
        initEvaluationParm();
        pictureAdapters = new ArrayList<>();
        Button btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

        revy_list = findViewById(R.id.rv_list);
        revy_list.setLayoutManager(new LinearLayoutManager(this));

        revy_list.setAdapter(new CommonRecyclerViewAdapter<OrderProductInfo>(this, productData) {
            @Override
            public void convert(CommonRecyclerViewHolder h, OrderProductInfo entity, final int position) {

                //产品主图加载
                ImageView iv_product_image = h.getView(R.id.iv_product_image);
                Glide.with(EvaluationActivity.this).load(entity.getProduct_picture().get(0).getUrl()).into(iv_product_image);
                //评价星星添加监听
                RatingBar rb_star = h.getView(R.id.rb_star);
                rb_star.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
                    @Override
                    public void onRatingChange(float ratingCount) {
                        if (ratingCount < 1) {
                            ToastUtils.show("评价星星数必须要大于1颗星哦");
                            evaluationParm.getEvaluation().get(position).setStars(0);
                        }
                        evaluationParm.getEvaluation().get(position).setStars((int) ratingCount);
                    }
                });

                //评语监听
                EditText et_evaluation = h.getView(R.id.et_evaluation);
                et_evaluation.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        evaluationParm.getEvaluation().get(position).setContent(editable.toString());
                    }
                });

                //评论图监听
                ImageView tv_btn_take_photo = h.getView(R.id.tv_btn_take_photo);
                tv_btn_take_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentSelectPosition = position;
                        takePhoto();
                    }
                });

                //评论图列表
                RecyclerView rv_image_contanner = h.getView(R.id.rv_image_contanner);
                LinearLayoutManager managers = new LinearLayoutManager(EvaluationActivity.this);
                managers.setOrientation(LinearLayoutManager.HORIZONTAL);
                rv_image_contanner.setLayoutManager(managers);
                //        pictureAdapters
                CommonRecyclerViewAdapter mPictureAdapter = new CommonRecyclerViewAdapter<String>(EvaluationActivity.this, evaluationParm.getEvaluation().get(position).getPicturesArray()) {
                    @Override
                    public void convert(CommonRecyclerViewHolder h, String entity, final int picturePosition) {
                        ImageView imageView = h.getView(R.id.iv_photo);
                        Glide.with(EvaluationActivity.this).load(entity).into(imageView);
                        ImageView iv_delete = h.getView(R.id.iv_delete);
                        iv_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                remove(position);
                                evaluationParm.getEvaluation().get(position).getPicturesArray().remove(picturePosition);
                                notifyDataSetChanged();
                            }
                        });
                    }

                    @Override
                    public int getLayoutViewId(int viewType) {
                        return R.layout.item_evaluation_photo;
                    }

                };
                rv_image_contanner.setAdapter(mPictureAdapter);
                pictureAdapters.add(mPictureAdapter);

            }

            @Override
            public int getLayoutViewId(int viewType) {
                return R.layout.item_add_evaluation;
            }
        });

    }

    @Override
    protected void initData() {

    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        //super.onActivityResult(requestCode, resultCode, data);
//        Log.e("syqwwww", requestCode + "*/" + resultCode);
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == 103) {
//                ArrayList<String> pathList =
//                        data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);
////                evaluationParm.getEvaluation().get(currentSelectPosition).getPicturesArray().addAll(pathList);
////                pictureAdapters.get(currentSelectPosition).notifyDataSetChanged();
//                for (String str :pathList){
//                    LogUtils.LogE("picture_Url:" + str);
//                    uploadfile(new File(str));
//                }
//            }
//        }
//
//    }

    /**
     * 创建订单前的操作
     *
     * @param desparms
     */
    public void addEvaluation(String desparms) {
        api.addEvaluation(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                    }

                    @Override
                    public void onNext(BaseBean obj) {
                        LogUtils.LogE("beforeCreatOrder:" + gson.toJson(obj));
                        if (obj.getState() == 1) {
                            setResult(RESULT_OK);
                            finish();
                        } else if (obj.getState() == -1) {
                            startlogin();
                        } else {
                            showConnectError(obj.getInfo().getMessage());
                        }
                    }
                });

    }

    public void uploadfile(File file) {

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);

        api.uploadfile(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UpHeadImageBean>() {
                    @Override
                    public void onCompleted() {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("bcz", e.getMessage());
                        showConnectError();
                    }

                    @Override
                    public void onNext(UpHeadImageBean obj) {
                        LogUtils.LogE("PersonInfoMationActivity-setPicToView-onNext:" + new Gson().toJson(obj));
                        if (obj.getState() == 1) {
                            String url = obj.getInfo().getImgPath().get(0).getUrl();
                            LogUtils.LogE("bcz_onnextUrl:" + url);
                            evaluationParm.getEvaluation().get(currentSelectPosition).getPicturesArray().add(url);
                            pictureAdapters.get(currentSelectPosition).notifyDataSetChanged();
//                            loadimg();
//                            saveImg();

                        } else {
                            showConnectError();
                        }

                    }
                });


    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this, true);
    }
}
