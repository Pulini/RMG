package com.yiyun.rmj.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.yiyun.rmj.R;
import com.yiyun.rmj.view.indicatorseekbar.SizeUtils;

//签到成功弹窗
public class SignSuccessDialog extends AlertDialog implements View.OnClickListener {

    private ImageView iv_cancel;
    private Activity context;


    public SignSuccessDialog(Context context) {
        super(context);
        this.context  = (Activity)context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_sign_success, null);
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
        iv_cancel=(ImageView)view.findViewById(R.id.iv_close);
    }

    public void initEvent()
    {
        iv_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.iv_close:
                dismiss();
                break;

        }
    }

}
