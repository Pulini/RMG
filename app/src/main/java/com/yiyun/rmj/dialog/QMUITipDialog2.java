package com.yiyun.rmj.dialog;

/**
 * Created by Administrator on 2018/7/24 0024.
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.LayoutRes;
import androidx.core.content.ContextCompat;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.yiyun.rmj.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 提供一个浮层展示在屏幕中间, 一般使用 {@link com.qmuiteam.qmui.widget.dialog.QMUITipDialog.Builder} 或 {@link com.qmuiteam.qmui.widget.dialog.QMUITipDialog.CustomBuilder} 生成。
 * <ul>
 * <li>{@link com.qmuiteam.qmui.widget.dialog.QMUITipDialog.Builder} 提供了一个图标和一行文字的样式, 其中图标有几种类型可选, 见 {@link com.qmuiteam.qmui.widget.dialog.QMUITipDialog.Builder.IconType}</li>
 * <li>{@link com.qmuiteam.qmui.widget.dialog.QMUITipDialog.CustomBuilder} 支持传入自定义的 layoutResId, 达到自定义 TipDialog 的效果。</li>
 * </ul>
 *
 * @author cginechen
 * @date 2016-10-14
 */

public class QMUITipDialog2 extends Dialog {

    private QMUITipDialog2 dialog;

    public QMUITipDialog2(Context context) {
        this(context, com.qmuiteam.qmui.R.style.QMUI_TipDialog);
    }

    public QMUITipDialog2(Context context, int themeResId) {
        super(context, themeResId);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialogWidth();
    }

    private void initDialogWidth() {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams wmLp = window.getAttributes();
            wmLp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            window.setAttributes(wmLp);
        }
    }

    /**
     * 生成默认的 {@link com.qmuiteam.qmui.widget.dialog.QMUITipDialog}
     * <p>
     * 提供了一个图标和一行文字的样式, 其中图标有几种类型可选。见 {@link com.qmuiteam.qmui.widget.dialog.QMUITipDialog.Builder.IconType}
     * </p>
     *
     * @see com.qmuiteam.qmui.widget.dialog.QMUITipDialog.CustomBuilder
     */
    public static class Builder {
        /**
         * 不显示任何icon
         */
        public static final int ICON_TYPE_NOTHING = 0;
        /**
         * 显示 Loading 图标
         */
        public static final int ICON_TYPE_LOADING = 1;
        /**
         * 显示成功图标
         */
        public static final int ICON_TYPE_SUCCESS = 2;
        /**
         * 显示失败图标
         */
        public static final int ICON_TYPE_FAIL = 3;
        /**
         * 显示信息图标
         */
        public static final int ICON_TYPE_INFO = 4;

        @IntDef({ICON_TYPE_NOTHING, ICON_TYPE_LOADING, ICON_TYPE_SUCCESS, ICON_TYPE_FAIL, ICON_TYPE_INFO})
        @Retention(RetentionPolicy.SOURCE)
        public @interface IconType {
        }

        private
        int mCurrentIconType = ICON_TYPE_NOTHING;

        private Context mContext;

        private CharSequence mTipWord;

        public Builder(Context context) {
            mContext = context;
        }

        /**
         * 设置 icon 显示的内容
         *
         * @see com.qmuiteam.qmui.widget.dialog.QMUITipDialog.Builder.IconType
         */
        public Builder setIconType(int iconType) {
            mCurrentIconType = iconType;
            return this;
        }

        /**
         * 设置显示的文案
         */
        public Builder setTipWord(CharSequence tipWord) {
            mTipWord = tipWord;
            return this;
        }

        /**
         * 创建 Dialog, 但没有弹出来, 如果要弹出来, 请调用返回值的 {@link Dialog#show()} 方法
         *
         * @return 创建的 Dialog
         */
        public QMUITipDialog2 create() {
           QMUITipDialog2 dialog = new QMUITipDialog2(mContext);
            dialog.setContentView(R.layout.updata_dialog);
            ViewGroup contentWrap = (ViewGroup) dialog.findViewById(R.id.contentWrap);

            switch (mCurrentIconType) {
                case ICON_TYPE_LOADING:
                    QMUILoadingView loadingView = new QMUILoadingView(mContext);
                    loadingView.setColor(Color.WHITE);
                    loadingView.setSize(QMUIDisplayHelper.dp2px(mContext, 32));
                    LinearLayout.LayoutParams loadingViewLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    loadingView.setLayoutParams(loadingViewLP);
                    contentWrap.addView(loadingView);
                    break;
                case ICON_TYPE_SUCCESS:
                case ICON_TYPE_FAIL:
                case ICON_TYPE_INFO:
                    ImageView imageView = new ImageView(mContext);
                    LinearLayout.LayoutParams imageViewLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    imageView.setLayoutParams(imageViewLP);

                    if (mCurrentIconType == ICON_TYPE_SUCCESS) {
                        imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_xuanzhong));
                    } else if (mCurrentIconType == ICON_TYPE_FAIL) {
                        imageView.setImageDrawable(mContext.getResources().getDrawable(com.qmuiteam.qmui.R.drawable.qmui_icon_notify_error));
                    } else {
                        imageView.setImageDrawable(mContext.getResources().getDrawable(com.qmuiteam.qmui.R.drawable.qmui_icon_notify_info));
                    }

                    contentWrap.addView(imageView);
                    break;
            }

            if (mTipWord != null && mTipWord.length() > 0) {
                TextView tipView = new TextView(mContext);
                LinearLayout.LayoutParams tipViewLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                if (mCurrentIconType != ICON_TYPE_NOTHING) {
                    tipViewLP.topMargin = QMUIDisplayHelper.dp2px(mContext, 12);
                }
                tipView.setLayoutParams(tipViewLP);

                tipView.setEllipsize(TextUtils.TruncateAt.END);
                tipView.setGravity(Gravity.CENTER);
                tipView.setMaxLines(2);
                tipView.setTextColor(ContextCompat.getColor(mContext, R.color.color222));
                tipView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tipView.setText(mTipWord);

                contentWrap.addView(tipView);
            }
            return dialog;
        }

    }

    /**
     * 传入自定义的布局并使用这个布局生成 TipDialog
     */
    public static class CustomBuilder {
        private Context mContext;
        private int mContentLayoutId;

        public CustomBuilder(Context context) {
            mContext = context;
        }

        public CustomBuilder setContent(@LayoutRes int layoutId) {
            mContentLayoutId = layoutId;
            return this;
        }

        /**
         * 创建 Dialog, 但没有弹出来, 如果要弹出来, 请调用返回值的 {@link Dialog#show()} 方法
         *
         * @return 创建的 Dialog
         */
        public QMUITipDialog2 create() {
            QMUITipDialog2 dialog = new QMUITipDialog2(mContext);
            dialog.setContentView(R.layout.updata_dialog);
            ViewGroup contentWrap = (ViewGroup) dialog.findViewById(com.qmuiteam.qmui.R.id.contentWrap);
            View customView = LayoutInflater.from(mContext).inflate(mContentLayoutId, contentWrap, true);
            return dialog;
        }
    }
}
