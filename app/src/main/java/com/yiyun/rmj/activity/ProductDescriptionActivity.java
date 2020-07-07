package com.yiyun.rmj.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.utils.StatusBarUtil;

//产品图文描述页面
public class ProductDescriptionActivity extends BaseActivity implements View.OnClickListener{


    /**
     * 分享的回调
     */
    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            Log.e("bcz","ProductDescriptionActivity-UMShareListener-onStart" );
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Log.e("bcz","ProductDescriptionActivity-UMShareListener-onResult:name==" + share_media.getName() + "==getauthstyle==" + share_media.getauthstyle(true) + "===getsharestyle==" + share_media.getsharestyle(true));

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Log.e("bcz","ProductDescriptionActivity-UMShareListener-onError:" + throwable.getMessage() );
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Log.e("bcz","ProductDescriptionActivity-UMShareListener-onCancel" );
        }
    };


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_title_share:
//                PermissionUtil.requestStoragePermission(this,new PermissionUtil.IRequestPermissionCallBack(){
//                    @Override
//                    public void permissionSuccess() {
//                        UMShareHelper.toShare(ProductDescriptionActivity.this,,shareListener);
//                    }
//                });

                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_description;
    }

    public void initTitle(){
        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        ImageView iv_share = findViewById(R.id.iv_title_share);
        iv_share.setVisibility(View.VISIBLE);
        iv_share.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        initTitle();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this,true);
    }

}
