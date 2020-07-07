package com.yiyun.rmj.base;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.yiyun.rmj.R;
import com.yiyun.rmj.activity.LoginActivity;
import com.yiyun.rmj.api.Api;
import com.yiyun.rmj.setting.UrlContact;
import com.yiyun.rmj.utils.RetrofitHelper;
import com.yiyun.rmj.utils.SpfUtils;

import cn.jake.share.frdialog.dialog.FRDialog;


public abstract class BaseFragment extends Fragment {

    private SVProgressHUD mSVProgressHUD;
    protected Api api;
    protected Gson gson;
    private FRDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(getLayoutId(), container, false);
    }

    protected void startlogin() {
        if (System.currentTimeMillis() - UrlContact.exitTime > 1000) {
            UrlContact.exitTime = System.currentTimeMillis();
            dialog = new FRDialog.CommonBuilder(getActivity()).setContentView(R.layout.tip).create();

            dialog.setCancelable(false);

            TextView tv_login = dialog.findViewById(R.id.tv_login);
            TextView tv_showmsg = dialog.findViewById(R.id.tv_tip);

            SpfUtils.getSpfUtils(MyApplication.getInstance()).setToken("");
            tv_login.setText("去登陆");
            tv_showmsg.setText("您已离线 请先进行登录");
            dialog.setCancelable(true);

            dialog.show();
            tv_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("relogin",true);
                    startActivity(intent);
                }
            });
        } else {
            SpfUtils.getSpfUtils(MyApplication.getInstance()).setToken("");
            return;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gson = new Gson();
        api = RetrofitHelper.getSingleRetrofit().create(Api.class);
        mSVProgressHUD = new SVProgressHUD(getActivity());
        mSVProgressHUD.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(SVProgressHUD hud) {

            }
        });

        initView(view);
        initData();
    }

    protected void showProgressDialog(String msg) {

        mSVProgressHUD.showWithStatus(msg);

    }

    protected void dismissProgressDialog() {
        if (mSVProgressHUD.isShowing()) {
            mSVProgressHUD.dismiss();

        }
    }

    protected void showConnectError() {
        ToastUtils.show("网络异常！");
    }

    protected void showConnectError(String msg) {
        ToastUtils.show(msg);
    }

    protected abstract int getLayoutId();

    protected abstract void initView(View view);

    protected abstract void initData();

}
