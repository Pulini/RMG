package com.yiyun.rmj.activity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.apibase.BaseParm;
import com.yiyun.rmj.bean.apibean.ShareBean;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.PermissionUtil;
import com.yiyun.rmj.utils.StatusBarUtil;
import com.yiyun.rmj.utils.UMShareHelper;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//分享活动规则页面
public class ShareRuleDetailActivity extends BaseActivity {

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
    protected int getLayoutId() {
        return R.layout.activity_share_rule_detail;
    }

    public void initTitleView(){
        TextView title = findViewById(R.id.tv_title);
        title.setVisibility(View.VISIBLE);
        title.setText("活动规则");

        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.activity_leftclick_in,R.anim.activity_leftclick_out);
            }
        });
    }

    @Override
    protected void initView() {
        initTitleView();

        TextView tv_add_friend = findViewById(R.id.tv_add_friend);
        tv_add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BaseParm baseParm = new BaseParm();
                String strDes = DESHelper.encrypt(gson.toJson(baseParm));
                share(strDes);

            }
        });


    }

    @Override
    protected void initData() {

    }

    /**
     * 分享
     * @param desparms
     */
    public void share(String desparms) {
        api.share(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ShareBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                    }

                    @Override
                    public void onNext(final ShareBean obj) {

                        if(obj.getState() == 1){
                            PermissionUtil.requestStoragePermission(ShareRuleDetailActivity.this,new PermissionUtil.IRequestPermissionCallBack(){
                                @Override
                                public void permissionSuccess() {
                                    UMShareHelper.toShare(ShareRuleDetailActivity.this, obj.getInfo().getData().getTitle(), obj.getInfo().getData().getLink(), obj.getInfo().getData().getContent(),shareListener);
                                }
                            });
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }
                    }
                });
    }


    @Override
    protected void setStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this,true);
    }
}
