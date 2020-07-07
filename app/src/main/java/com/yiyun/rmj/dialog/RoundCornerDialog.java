package com.yiyun.rmj.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.yiyun.rmj.R;

public class RoundCornerDialog extends AlertDialog implements View.OnClickListener {

    private TextView tvDes;
    private TextView tvCancel;
    private TextView tvConfirm;
    private String msg;
    private IDialogCallback callback;
    private Activity context;


    public RoundCornerDialog(Context context,String msg, IDialogCallback callback) {
        super(context);
        this.msg = msg;
        this.callback = callback;
        this.context  = (Activity)context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_roundcorner, null);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);
        setContentView(view);
        initView(view);
        initEvent();
        window.setWindowAnimations(R.style.dialogScaleWindowAnim);
//        WindowManager.LayoutParams param = getWindow().getAttributes();
//        getWindow().setAttributes(param);
    }

    public void initView(View view)
    {
        //对话框描述信息
        tvDes=(TextView)view.findViewById(R.id.tv_des);
        //确定按钮和取消
        tvConfirm=(TextView)view.findViewById(R.id.tv_confirm);
        tvCancel=(TextView)view.findViewById(R.id.tv_cancel);
        if(TextUtils.isEmpty(msg)) {
            msg = "";
        }
        tvDes.setText(msg);
    }

    public void initEvent()
    {
        tvConfirm.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.tv_cancel:
                dismiss();
                callback.onCancel();
                break;
            case R.id.tv_confirm:
                dismiss();
                callback.onConfirm();
                break;
        }
    }
}
