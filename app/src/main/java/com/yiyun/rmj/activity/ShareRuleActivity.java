package com.yiyun.rmj.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yiyun.rmj.R;
import com.yiyun.rmj.adapter.CommonRecyclerViewAdapter;
import com.yiyun.rmj.adapter.CommonRecyclerViewHolder;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.InvitationRecord;
import com.yiyun.rmj.bean.apibase.BaseParm;
import com.yiyun.rmj.bean.apibean.RecordListBean;
import com.yiyun.rmj.bean.apibean.ShareBean;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.utils.PermissionUtil;
import com.yiyun.rmj.utils.UMShareHelper;
import com.yiyun.rmj.view.CircleImageView;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//分享规则页面
public class ShareRuleActivity extends BaseActivity implements View.OnClickListener{

    RecyclerView rv_frend_ranking;
    ArrayList<InvitationRecord> friends;

    TextView tv_add_friend_num, tv_record_ranking, tv_reword_collection;
    private int rewardCount;//邀请总数
    private String rowNum;  //排名
    private int invite;  // 获得总积分
    private CommonRecyclerViewAdapter adapter;

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
        switch (view.getId()){
            case R.id.iv_back_white:
                //后退
                finish();
                overridePendingTransition(R.anim.activity_leftclick_in,R.anim.activity_leftclick_out);
                break;
            case R.id.tv_invite_friends:
                //添加好友
            case R.id.tv_add_friend:
                //添加好友
                BaseParm baseParm = new BaseParm();
                String strDes = DESHelper.encrypt(gson.toJson(baseParm));
                share(strDes);
                break;
            case R.id.tv_see_the_detail:
                //查看详情
                Intent intentRangeDetail = new Intent(ShareRuleActivity.this, ShareRuleRangeDetailActivity.class);
                intentRangeDetail.putExtra("rewardCount",rewardCount);
                intentRangeDetail.putExtra("rowNum",rowNum);
                intentRangeDetail.putExtra("invite",invite);
                startActivity(intentRangeDetail);
//                overridePendingTransition(R.anim.activity_rightclick_in,R.anim.activity_rightclick_out);
                break;
            case R.id.tv_hdgz:
                //活动规则
                Intent intentDetail = new Intent(ShareRuleActivity.this, ShareRuleDetailActivity.class);
                startActivity(intentDetail);
//                overridePendingTransition(R.anim.activity_rightclick_in, R.anim.activity_rightclick_out);
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_share_rule;
    }

    @Override
    protected void initView() {

        friends = new ArrayList<>();


        //后退按钮
        ImageView iv_back_white = findViewById(R.id.iv_back_white);
        iv_back_white.setOnClickListener(this);

        TextView tv_hdgz = findViewById(R.id.tv_hdgz);
        tv_hdgz.setOnClickListener(this);

        TextView tv_add_friend = findViewById(R.id.tv_add_friend);
        tv_add_friend.setOnClickListener(this);

        TextView tv_see_the_detail = findViewById(R.id.tv_see_the_detail);
        tv_see_the_detail.setOnClickListener(this);

        TextView tv_invite_friends = findViewById(R.id.tv_invite_friends);
        tv_invite_friends.setOnClickListener(this);

        tv_add_friend_num = findViewById(R.id.tv_add_friend_num);
        tv_record_ranking = findViewById(R.id.tv_record_ranking);
        tv_reword_collection = findViewById(R.id.tv_reword_collection);

        rv_frend_ranking = findViewById(R.id.rv_frend_ranking);
        rv_frend_ranking.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        adapter = new CommonRecyclerViewAdapter<InvitationRecord>(this,friends) {
            @Override
            public void convert(CommonRecyclerViewHolder h, InvitationRecord entity, int position) {

                CircleImageView iv_icon = h.getView(R.id.iv_icon);
                TextView tv_name = h.getView(R.id.tv_name);
                TextView tv_phone = h.getView(R.id.tv_phone);
                TextView tv_collection = h.getView(R.id.tv_collection);

                TextView tv_range_num = h.getView(R.id.tv_range_num);
                ImageView iv_range_num1 = h.getView(R.id.iv_range_num1);
                ImageView iv_range_num2 = h.getView(R.id.iv_range_num2);
                ImageView iv_range_num3 = h.getView(R.id.iv_range_num3);
                tv_range_num.setVisibility(View.INVISIBLE);
                iv_range_num1.setVisibility(View.INVISIBLE);
                iv_range_num2.setVisibility(View.INVISIBLE);
                iv_range_num3.setVisibility(View.INVISIBLE);

                if(position == 0){
                    iv_range_num1.setVisibility(View.VISIBLE);
                }else if(position == 1){
                    iv_range_num2.setVisibility(View.VISIBLE);
                }else if(position == 2){
                    iv_range_num3.setVisibility(View.VISIBLE);
                }else{
                    tv_range_num.setVisibility(View.VISIBLE);
                    tv_range_num.setText("" + (position+1));
                }

                String head = entity.getHead();
                if(!TextUtils.isEmpty(head)){
                    Glide.with(ShareRuleActivity.this).load(head).apply(new RequestOptions()).into(iv_icon);
                }
                tv_name.setText(entity.getUsername());
                tv_phone.setText(entity.getPhone());
                tv_collection.setText("" + entity.getInvite());


            }

            @Override
            public int getLayoutViewId(int viewType) {
                return R.layout.item_share_rule_invite_friends;
            }

        };
        rv_frend_ranking.setAdapter(adapter);


    }

    @Override
    protected void initData() {
        BaseParm parm = new BaseParm();
        String strDes = DESHelper.encrypt(gson.toJson(parm));
        getMyInvitationRecord(strDes);
    }

    /**
     * 获取邀请战绩
     * @param desparms
     */
    private void getMyInvitationRecord(String desparms){

        api.getMyInvitationRecord(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RecordListBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                        LogUtils.LogE("error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(RecordListBean obj) {
                        LogUtils.LogE(ShareRuleActivity.class.getName() + "--getMyInvitationRecord:" + gson.toJson(obj));

                        if(obj.getState() == 1){
                            rewardCount = obj.getInfo().getData().getMyInvitationRecord().getCount();
                            rowNum = obj.getInfo().getData().getMyInvitationRecord().getRownum();
                            invite = obj.getInfo().getData().getMyInvitationRecord().getInvite();

                            tv_add_friend_num.setText("" + rewardCount);
                            tv_record_ranking.setText("" + rowNum);
                            tv_reword_collection.setText("" + invite);

                            friends.clear();
                            friends.addAll(obj.getInfo().getData().getInvitationRecordList());
                            adapter.notifyDataSetChanged();
                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }

                    }
                });
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
                            PermissionUtil.requestStoragePermission(ShareRuleActivity.this,new PermissionUtil.IRequestPermissionCallBack(){
                                @Override
                                public void permissionSuccess() {
                                UMShareHelper.toShare(ShareRuleActivity.this, obj.getInfo().getData().getTitle(), obj.getInfo().getData().getLink(), obj.getInfo().getData().getContent(),shareListener);
                                }
                            });
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }
                    }
                });
    }


}
