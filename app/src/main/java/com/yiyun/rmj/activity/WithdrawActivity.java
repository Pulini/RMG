package com.yiyun.rmj.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjq.toast.ToastUtils;
import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.apibean.OneZfbAccountBean;
import com.yiyun.rmj.bean.apibean.ShareBean;
import com.yiyun.rmj.bean.apibean.ZfbAccountBean;
import com.yiyun.rmj.bean.apiparm.AlipayParm;
import com.yiyun.rmj.bean.apiparm.TixianParm;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.utils.SpfUtils;
import com.yiyun.rmj.utils.StatusBarUtil;

import cn.jake.share.frdialog.dialog.FRDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//提现页面
public class WithdrawActivity extends BaseActivity implements View.OnClickListener {

    TextView tv_zfb;
    ZfbAccountBean.Account account;
    private int accountId = 0;
    private double money;
    private EditText et_tx_money;
    private RelativeLayout rl_zfb;
    private FRDialog addMoneyDialog;

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.iv_back:
                finish();
                break;
//            case R.id.tv_title_add:
//                break;
            case R.id.rl_zfb:
                //选择支付宝
                Intent selectZfbIntent = new Intent(WithdrawActivity.this, ZfbAccountManagerActivity.class);
                startActivityForResult(selectZfbIntent,101);
                break;

            case R.id.btn_withdraw:
                //提现
                if(checkInput()){

                    TixianParm parm = new TixianParm();
                    parm.setAlipayId(accountId);
                    parm.setMoney(et_tx_money.getText().toString());
                    String strDes = DESHelper.encrypt(gson.toJson(parm));
                    withdraw(strDes);
                }

                break;

            case R.id.tv_all:
                //提现全部
                et_tx_money.setText("" + money);
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdraw;
    }

    public void initTitleView(){

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.withdraw));


        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

//        TextView tv_add = (TextView) findViewById(R.id.tv_title_add);
//        tv_add.setVisibility(View.VISIBLE);
//        tv_add.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        money = getIntent().getDoubleExtra("money",0);
        initTitleView();
        tv_zfb = findViewById(R.id.tv_zfb);
        TextView tv_txje = findViewById(R.id.tv_txje);
        tv_txje.setText("可提现金额" + money + "元");

        et_tx_money = findViewById(R.id.et_tx_money);

        TextView tv_all = findViewById(R.id.tv_all);
        tv_all.setOnClickListener(this);

        rl_zfb = findViewById(R.id.rl_zfb);
        rl_zfb.setOnClickListener(this);

        Button btn_withdraw = (Button) findViewById(R.id.btn_withdraw);
        btn_withdraw.setOnClickListener(this);

        et_tx_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String temp = editable.toString();
                int posDot = temp.indexOf(".");
                if (posDot > 0){
                    //保留小数点后两位
                    if (temp.length() - posDot - 1 > 2) {
                        editable.delete(posDot + 3, posDot + 4);
                    }
                }

                //输入的金额超出最大还款金额则设置为最大还款金额
                if(!TextUtils.isEmpty(temp)){
                    if(temp.equals(".")){
                        temp = "0.";
                        et_tx_money.setText(temp);
                        et_tx_money.setSelection(temp.length());
                    }
                    double tempDouble = Double.parseDouble(temp);
                    if(tempDouble > money){
                        et_tx_money.setText("" + money);
                        et_tx_money.setSelection(("" + money).length());
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        int alipayId = SpfUtils.getSpfUtils(this).getDefaultAlipayId();
        LogUtils.LogE("alipayId:" + alipayId);
        if(alipayId != -1){
            AlipayParm parm = new AlipayParm();
            parm.setAlipayId(alipayId);
            String parmDes = DESHelper.encrypt(gson.toJson(parm));
            getAliPayNum(parmDes);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101 && resultCode == RESULT_OK){

            accountId = data.getIntExtra("alipayId",-1);
            tv_zfb.setText(data.getStringExtra("alipayNum"));
        }
    }

    private void getAliPayNum(String desparms){

        api.getAliPayNum(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<OneZfbAccountBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                        LogUtils.LogE("error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(OneZfbAccountBean obj) {

                        LogUtils.LogE(ZfbAccountManagerActivity.class.getName() + "--defaultAliPayNum:" + gson.toJson(obj));
                        if(obj.getState() == 1){
                            accountId = obj.getInfo().getData().getAlipayId();
                            tv_zfb.setText(obj.getInfo().getData().getAlipayNum());
                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }

                    }
                });

    }

    /**
     * 提现
     * @param desparms
     */
    public void withdraw(String desparms) {
        showProgressDialog("正在提现中");
        api.withdraw(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ShareBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        showConnectError();
                    }

                    @Override
                    public void onNext(final ShareBean obj) {

                        dismissProgressDialog();
                        showConnectError(obj.getInfo().getMessage());
                        if(obj.getState() == 1){
                            setResult(RESULT_OK);
                            finish();
                        }else if(obj.getState() == -1){

                        }
                    }
                });
    }

    public boolean checkInput(){

        if(TextUtils.isEmpty(et_tx_money.getText().toString().trim())){
            ToastUtils.show("请输入提现金额");
            return false;
        }

        if(accountId == 0){
            ToastUtils.show("请选择支付宝账号");
            return false;
        }
        return true;

    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this,true);
    }
}
