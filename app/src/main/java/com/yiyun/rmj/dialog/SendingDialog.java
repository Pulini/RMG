package com.yiyun.rmj.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yiyun.rmj.R;
import com.yiyun.rmj.view.indicatorseekbar.SizeUtils;


public class SendingDialog extends Dialog {
    private TextView tv_msg;


    public SendingDialog(Context context) {
        //一开始就设置为透明背景
        super(context, R.style.LoadingDialog);
        init(context);
    }


    public void init(final Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        //得到加载的view
        View v = inflater.inflate(R.layout.dialog_sending, null);
        //加载布局
        tv_msg = v.findViewById(R.id.tv_msg);
        Window window = getWindow();
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        setContentView(v);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.height = SizeUtils.dp2px( context,150);
        lp.width = SizeUtils.dp2px( context,150);
        window.setAttributes(lp);
    }

    // 设置加载信息
    public void setMessage(String msg) {
        tv_msg.setText(msg);
    }

}
