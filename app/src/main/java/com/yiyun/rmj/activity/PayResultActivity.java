package com.yiyun.rmj.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.utils.StatusBarUtil;

public class PayResultActivity extends BaseActivity implements View.OnClickListener{

    private int payResult; //0成功, else失败
    private int orderId;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_result;
    }

    public void initTitleView(){
        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        TextView textTitle = findViewById(R.id.tv_title);
        textTitle.setVisibility(View.VISIBLE);
        if(payResult == 0){
            textTitle.setText("支付成功");
        }else{
            textTitle.setText("支付失败");
        }

    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        payResult = intent.getIntExtra("payresult",0);
        orderId = intent.getIntExtra("orderId",0);
        initTitleView();

        ImageView iv_failed = findViewById(R.id.iv_failed);
        ImageView iv_success = findViewById(R.id.iv_success);
        TextView tv_payfailed = findViewById(R.id.tv_payfailed);
        TextView tv_paysuccess = findViewById(R.id.tv_paysuccess);

        Button btn_seeOrder = findViewById(R.id.btn_seeOrder);


        if(payResult == 0){
            tv_paysuccess.setVisibility(View.VISIBLE);
            iv_success.setVisibility(View.VISIBLE);
            tv_payfailed.setVisibility(View.INVISIBLE);
            iv_failed.setVisibility(View.INVISIBLE);
        }else{
            tv_paysuccess.setVisibility(View.INVISIBLE);
            iv_success.setVisibility(View.INVISIBLE);

            tv_payfailed.setVisibility(View.VISIBLE);
            iv_failed.setVisibility(View.VISIBLE);

        }

        btn_seeOrder.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_seeOrder:
                Intent orderDetailIntent = new Intent(PayResultActivity.this, OrderDetailActivity.class);
                orderDetailIntent.putExtra("orderId",orderId);
                startActivity(orderDetailIntent);
                finish();
                break;
            case R.id.iv_back:
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this,true);
    }
}
