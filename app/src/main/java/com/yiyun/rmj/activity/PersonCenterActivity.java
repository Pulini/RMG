package com.yiyun.rmj.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.apibase.BaseParm;
import com.yiyun.rmj.bean.apibean.PersonCenterBean;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.SpfUtils;
import com.yiyun.rmj.view.CircleImageView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;



//个人中心页面
public class PersonCenterActivity extends BaseActivity implements View.OnClickListener{

    CircleImageView civ_mine_head;
    TextView tv_name;
    TextView tv_money;
    private double money;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_person_center;
    }

    public void initTitleView(){
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back_white);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        ImageView iv_setting = (ImageView) findViewById(R.id.iv_setting);
        iv_setting.setVisibility(View.VISIBLE);
        iv_setting.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        initTitleView();

        LinearLayout ll_person_name_icon = (LinearLayout) findViewById(R.id.ll_person_name_icon);
        ll_person_name_icon.setOnClickListener(this);

        TextView tv_see_order_all = (TextView) findViewById(R.id.tv_see_order_all);
        tv_see_order_all.setOnClickListener(this);

        LinearLayout ll_un_pay = (LinearLayout) findViewById(R.id.ll_un_pay);
        ll_un_pay.setOnClickListener(this);

        LinearLayout ll_unship = (LinearLayout) findViewById(R.id.ll_unship);
        ll_unship.setOnClickListener(this);

        LinearLayout ll_un_receipt = (LinearLayout) findViewById(R.id.ll_un_receipt);
        ll_un_receipt.setOnClickListener(this);

        LinearLayout ll_un_evaluation = (LinearLayout) findViewById(R.id.ll_un_evaluation);
        ll_un_evaluation.setOnClickListener(this);

        RelativeLayout rl_wallet = (RelativeLayout) findViewById(R.id.rl_wallet);
        rl_wallet.setOnClickListener(this);

        RelativeLayout rl_distribution = (RelativeLayout) findViewById(R.id.rl_distribution);
        rl_distribution.setOnClickListener(this);

        RelativeLayout rl_address = (RelativeLayout) findViewById(R.id.rl_address);
        rl_address.setOnClickListener(this);

        RelativeLayout rl_integration = (RelativeLayout) findViewById(R.id.rl_integration);
        rl_integration.setOnClickListener(this);

        RelativeLayout rl_collection = (RelativeLayout) findViewById(R.id.rl_collection);
        rl_collection.setOnClickListener(this);

        RelativeLayout rl_evaluation = (RelativeLayout) findViewById(R.id.rl_evaluation);
        rl_evaluation.setOnClickListener(this);

        tv_money = findViewById(R.id.tv_money);

        civ_mine_head = findViewById(R.id.civ_mine_head);
        tv_name = findViewById(R.id.tv_name);

    }

    @Override
    protected void initData() {
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.iv_back_white:
                //后退
//                Intent homeIntent = new Intent(PersonCenterActivity.this, HomeActivity.class);
//                startActivity(homeIntent);
                finish();
                break;
            case R.id.iv_setting:
                //设置页面
                Intent settingIntent = new Intent(PersonCenterActivity.this,SettingActivity.class);
                startActivity(settingIntent);
                break;

            case R.id.ll_person_name_icon:
                //个人信息页面
                Intent personInfoIntent = new Intent(PersonCenterActivity.this,PersonInfomationActivity.class);
                startActivity(personInfoIntent);
                break;

            case R.id.tv_see_order_all:
                //查看全部
                Intent seeAllIntent = new Intent(PersonCenterActivity.this,MyOrderActivity.class);
                seeAllIntent.putExtra("type",MyOrderActivity.TYPE_START_SHOW_ALL);
                startActivityForResult(seeAllIntent,0);
                break;

            case R.id.ll_un_pay:
                //待付款
                Intent unpayIntent = new Intent(PersonCenterActivity.this,MyOrderActivity.class);
                unpayIntent.putExtra("type",MyOrderActivity.TYPE_START_SHOW_UNPAY);
                startActivityForResult(unpayIntent,0);
                break;

            case R.id.ll_unship:
                //待发货
                Intent unshipIntent = new Intent(PersonCenterActivity.this,MyOrderActivity.class);
                unshipIntent.putExtra("type",MyOrderActivity.TYPE_START_SHOW_UNSHIP);
                startActivityForResult(unshipIntent,0);

                break;

            case R.id.ll_un_receipt:
                //待收货
                Intent unreceiptIntent = new Intent(PersonCenterActivity.this,MyOrderActivity.class);
                unreceiptIntent.putExtra("type",MyOrderActivity.TYPE_START_SHOW_UNRECEIPT);
                startActivityForResult(unreceiptIntent,0);
                break;

            case R.id.ll_un_evaluation:
                //待评价
                Intent unevaluationIntent = new Intent(PersonCenterActivity.this,MyOrderActivity.class);
                unevaluationIntent.putExtra("type",MyOrderActivity.TYPE_START_SHOW_UNEVALUATION);
                startActivityForResult(unevaluationIntent,0);
                break;

            case R.id.rl_wallet:
                //我的钱包
                Intent myWalletIntent = new Intent(PersonCenterActivity.this, MyWalletActivity.class);
                myWalletIntent.putExtra("money",money);
                startActivity(myWalletIntent);
                break;

            case R.id.rl_distribution:
                //我的分销
                Intent distributionIntent = new Intent(PersonCenterActivity.this, DistributionActivity.class);
                startActivity(distributionIntent);
                break;

            case R.id.rl_address:
                //我的地址
                Intent addessIntent = new Intent(PersonCenterActivity.this, SelectOrShowAddressActivity.class);
                addessIntent.putExtra("type",SelectOrShowAddressActivity.TYPE_JUST_SHOW_ADREESS);
                startActivity(addessIntent);
                break;
            case R.id.rl_integration:
                //我的积分
                Intent orderprogressIntent = new Intent(PersonCenterActivity.this, IntegrationActivity.class);
                startActivity(orderprogressIntent);
                break;

            case R.id.rl_collection:
                //我的收藏
                Intent collectionIntent = new Intent(PersonCenterActivity.this, MyCollectionActivity.class);
                startActivity(collectionIntent);
                break;

            case R.id.rl_evaluation:
                //我的评价
                Intent myEvaluationIntent = new Intent(PersonCenterActivity.this, MyEvaluationActivity.class);
                startActivity(myEvaluationIntent);
                break;

        }
    }

    public void refreshInfo(){
        String head = SpfUtils.getSpfUtils(this).getHeadImageUrl();
        String name = SpfUtils.getSpfUtils(this).getUserName();
        if(!TextUtils.isEmpty(head)){
            Glide.with(PersonCenterActivity.this).load(head).apply(new RequestOptions()).into(civ_mine_head);
        }

        //名字
        if(!TextUtils.isEmpty(name)){
            tv_name.setText(name);
        }else{
            tv_name.setText("lucy");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        BaseParm baseParm = new BaseParm();
        String strDes = DESHelper.encrypt(gson.toJson(baseParm));
        home(strDes);
        refreshInfo();
    }

    /**
     * 获取个人中心信息
     * @param desparms
     */
    public void home(String desparms) {
        api.home(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PersonCenterBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                    }

                    @Override
                    public void onNext(PersonCenterBean obj) {

                        if(obj.getState() == 1){
                            SpfUtils.getSpfUtils(PersonCenterActivity.this).setUserName(obj.getInfo().getData().getName());
                            SpfUtils.getSpfUtils(PersonCenterActivity.this).setHeadImageUrl(obj.getInfo().getData().getHead());
//                            money = obj.getInfo().getData().getMoney();
//                            tv_money.setText("" + money);
                            refreshInfo();
                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }
                    }
                });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("bcz","personcenterActivity---onActivityResult--resultcode:" + resultCode);
        if(resultCode == 505){
            finish();
        }
    }
}
