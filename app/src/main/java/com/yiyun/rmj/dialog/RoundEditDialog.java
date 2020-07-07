package com.yiyun.rmj.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyun.rmj.R;
import com.yiyun.rmj.utils.InputMethodUtils;

public class RoundEditDialog extends AlertDialog implements View.OnClickListener {

    private EditText tvDes;
    private ImageView tvCancel;
    private TextView tvConfirm;
    private String msg;
    private IDialogBtnLiscener callback;
    private Activity context;


    public RoundEditDialog(Context context, String msg, IDialogBtnLiscener callback) {
        super(context,R.style.input_dialog);
        this.msg = msg;
        this.callback = callback;
        this.context  = (Activity)context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_round_edit, null);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);
        setContentView(view);
        initView(view);
        initEvent();

//        WindowManager.LayoutParams param = getWindow().getAttributes();
//        getWindow().setAttributes(param);
    }

    public void initView(View view)
    {
        //对话框描述信息
        tvDes=(EditText) view.findViewById(R.id.tv_des);
        //确定按钮和取消
        tvConfirm=(TextView)view.findViewById(R.id.tv_confirm);
        tvCancel=(ImageView)view.findViewById(R.id.tv_cancel);
        if(TextUtils.isEmpty(msg)) {
            msg = "";
        }
        tvDes.setText(msg);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvDes.requestFocus();
                tvDes.setSelection(msg.length());
                InputMethodUtils.showOrHide(getContext());
            }
        },300);
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
                callback.onSure(tvDes.getText().toString());
                break;
        }
    }

    public interface IDialogBtnLiscener{
        public void onCancel();
        public void onSure(String str);
    }
}
