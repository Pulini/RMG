package com.yiyun.rmj.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.huantansheng.easyphotos.setting.Setting;
import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.base.MyApplication;
import com.yiyun.rmj.bean.apibase.BaseBean;
import com.yiyun.rmj.bean.apibean.UpHeadImageBean;
import com.yiyun.rmj.bean.apiparm.UpheadInfoParm;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.GlideEngine;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.utils.PermissionUtil;
import com.yiyun.rmj.utils.SpfUtils;
import com.yiyun.rmj.utils.StatusBarUtil;
import com.yiyun.rmj.view.CircleImageView;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//个人信息页面
public class PersonInfomationActivity extends BaseActivity implements View.OnClickListener {

    protected RequestOptions myOptions;
    private CircleImageView civ_mine_head;
    private EditText et_person_name;
    String headUrl = "";


    public void initTitleview() {
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.person_information));

        TextView tv_complete = (TextView) findViewById(R.id.title_complete);
        tv_complete.setVisibility(View.VISIBLE);
        tv_complete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.title_complete:
                //完成
                String name = et_person_name.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    ToastUtils.show("请输入您的昵称");
                    return;
                }

                UpheadInfoParm upheadInfoParm = new UpheadInfoParm();
                if (TextUtils.isEmpty(headUrl)) {
                    headUrl = SpfUtils.getSpfUtils(this).getHeadImageUrl();
                }
                upheadInfoParm.setHead(headUrl);
                upheadInfoParm.setName(name);
                String strDes = DESHelper.encrypt(gson.toJson(upheadInfoParm));
                updatePersonalMes(strDes);
                break;
            case R.id.civ_mine_head:
                //个人头像
                choiceHeadImg();
                break;
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_person_information;
    }

    @Override
    protected void initView() {
        initTitleview();
        civ_mine_head = findViewById(R.id.civ_mine_head);
        civ_mine_head.setOnClickListener(this);
        et_person_name = findViewById(R.id.et_person_name);
        myOptions = new RequestOptions()
                .error(R.mipmap.person_default_icon);
        loadimg();
        et_person_name.setText(SpfUtils.getSpfUtils(this).getUserName());

    }

    @Override
    protected void initData() {

    }

    public void choiceHeadImg() {
        PermissionUtil.requestStorageCameraPermission(this, new PermissionUtil.IRequestPermissionCallBack() {
            @Override
            public void permissionSuccess() {
                Log.e("bcz", "PersonInfomationActivity---start simagepicker");
//                SImagePicker
//                        .from(PersonInfomationActivity.this)
//                        .rowCount(4)
//                        .showCamera(true)
//                        .maxCount(1)
//                        .pickMode(SImagePicker.MODE_IMAGE)
//                        .forResult(103);

                EasyPhotos.createAlbum(PersonInfomationActivity.this, true, GlideEngine.getInstance()).setFileProviderAuthority("com.yiyun.rmj.fileprovider")
                        .setCount(1)
                        .setCameraLocation(Setting.LIST_FIRST)
                        .setPuzzleMenu(false)
                        .setCleanMenu(false)
                        .start(new SelectCallback() {
                            @Override
                            public void onResult(ArrayList<Photo> photos, boolean isOriginal) {
                                Log.e("Pan", "uri=" + photos.get(0).uri);
                                starCropPhoto(photos.get(0).uri);
                            }
                        });
            }
        });
    }

    public void starCropPhoto(Uri uri) {

        if (uri == null) {
            ToastUtils.show("null");
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipHeaderActivity.class);
        intent.setData(uri);
        intent.putExtra("side_length", 200);//裁剪图片宽高
        startActivityForResult(intent, 102);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("syqwwww", requestCode + "*/" + resultCode);
        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == 103) {
//                ArrayList<String> pathList =
//                        data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);
//                String imagePath = pathList.get(0);
//                Log.e("Pan", imagePath);
//                starCropPhoto(Uri.fromFile(new File(imagePath)));
//            }
            if (requestCode == 102) {
                setPicToView(data);
                loadimg();
            }
        }

    }

    private void loadimg() {
        String imgUrl = SpfUtils.getSpfUtils(MyApplication.getInstance()).getHeadImageUrl();
        if (!TextUtils.isEmpty(imgUrl)) {
            Glide.with(this).load(imgUrl).apply(myOptions).into(civ_mine_head);
        }

    }

    public void setPicToView(Intent picdata) {
        Uri uri = picdata.getData();
        if (uri == null) {
            return;
        }
        String str = uri.toString();
        SpfUtils.getSpfUtils(this).setHeadImageUrl(str);
        Log.e("Pan", str);
        int x = str.indexOf("d");
        //Log.e("syqfile", x + "");
        str = str.substring(x - 1);
        Log.e("syqfile", str + "");
        File file = new File(str);
        showProgressDialog("正在上传头像");
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);

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
                        dismissProgressDialog();
                        Log.e("bcz", e.getMessage());
                        showConnectError();
                    }

                    @Override
                    public void onNext(UpHeadImageBean obj) {
                        LogUtils.LogE("PersonInfoMationActivity-setPicToView-onNext:" + new Gson().toJson(obj));
                        dismissProgressDialog();
                        if (obj.getState() == 1) {
                            headUrl = obj.getInfo().getImgPath().get(0).getUrl();
                            loadimg();
//                            saveImg();

                        } else {
                            showConnectError();
                        }
                    }
                });
    }

    /**
     * 更改个人信息
     *
     * @param desparms
     */
    public void updatePersonalMes(String desparms) {
        api.updatePersonalMes(desparms)
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
                        if (obj.getState() == 1) {
                            showConnectError(obj.getInfo().getMessage());
                            SpfUtils.getSpfUtils(MyApplication.getInstance()).setHeadImageUrl(headUrl);
                            SpfUtils.getSpfUtils(MyApplication.getInstance()).setUserName(et_person_name.getText().toString());
                            finish();

                        } else if (obj.getState() == -1) {
                            startlogin();
                        } else {
                            showConnectError(obj.getInfo().getMessage());
                        }
                    }
                });

    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this, true);
    }
}
