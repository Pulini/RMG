package com.yiyun.rmj.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyun.rmj.R;

/**
 * Created by Administrator on 2018/7/23 0023.
 */

public class UpdataDialog extends Dialog {
    private Context mContext;
    private CharSequence mTipWord;
    private Activity activity;

    public UpdataDialog(@NonNull Context context) {
        super(context);
        mContext = context;
        activity = (Activity) context;
        setContentView(R.layout.updata_dialog);
        initviw();
    }

    public UpdataDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);

    }

    protected UpdataDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);

    }

    public void setTipWord(CharSequence tipWord) {
        mTipWord = tipWord;
    }

    public void initviw() {

        ViewGroup contentWrap = (ViewGroup)activity.findViewById(R.id.contentWrap);
        ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams imageViewLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(imageViewLP);
        imageView.setImageResource(R.mipmap.icon_xuanzhong);
        TextView tipView = new TextView(mContext);
        LinearLayout.LayoutParams tipViewLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        tipView.setLayoutParams(tipViewLP);

        tipView.setEllipsize(TextUtils.TruncateAt.END);
        tipView.setGravity(Gravity.CENTER);
        tipView.setMaxLines(2);
        tipView.setTextColor(ContextCompat.getColor(mContext, R.color.qmui_config_color_white));
        tipView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tipView.setText(mTipWord);
        contentWrap.addView(imageView);
        contentWrap.addView(tipView);

    }
}
