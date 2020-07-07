package com.yiyun.rmj.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.yiyun.rmj.R;
import com.yiyun.rmj.view.indicatorseekbar.SizeUtils;

public class CustomerServiceDialog extends AlertDialog implements View.OnClickListener {

    private ImageView tvCancel;
    private ICallBack callback;
    private Activity context;


    public CustomerServiceDialog(Context context, ICallBack callback) {
        super(context);
        this.context  = (Activity)context;
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_customer_service, null);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);
        setContentView(view);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.height = SizeUtils.dp2px( context,580);
        lp.width = SizeUtils.dp2px( context,320);
        window.setAttributes(lp);
        initView(view);
        initEvent();
//        window.setWindowAnimations(R.style.dialogScaleWindowAnim);
//        WindowManager.LayoutParams param = getWindow().getAttributes();
//        getWindow().setAttributes(param);
    }

    public void initView(View view)
    {
        //确定按钮和取消
        tvCancel=(ImageView)view.findViewById(R.id.iv_cancel);
        Button btn_after_sale = (Button) findViewById(R.id.btn_after_sale);
        btn_after_sale.setOnClickListener(this);

        Button btn_pre_sale = (Button) findViewById(R.id.btn_pre_sale);
        btn_pre_sale.setOnClickListener(this);

    }

    public void initEvent()
    {
        tvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.iv_cancel:
                dismiss();
//                callback.onCancel();
                break;
            case R.id.btn_after_sale:
                callback.onAfterSale();
                dismiss();
                break;
            case R.id.btn_pre_sale:
                callback.onPreSale();
                dismiss();
                break;
        }
    }

    public interface ICallBack{
        public void onAfterSale();
        public void onPreSale();
    }
}
