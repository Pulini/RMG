package com.yiyun.rmj.wxapi;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.weixin.view.WXCallbackActivity;
import com.yiyun.rmj.base.MyApplication;
import com.yiyun.rmj.setting.UrlContact;


public class WXPayEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler {

    private IWXAPI iwxapi;

    @Override
    protected void onResume() {
        super.onResume();
        iwxapi = WXAPIFactory.createWXAPI(this, UrlContact.WXAPPID);
        iwxapi.handleIntent(getIntent(),this);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.e("bcz-WXPayEntryActivity","baseReq:" + new Gson().toJson(baseReq));
        if(baseReq.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){
            Log.d("bcz-WXPayEntryActivity","baseReq:" + new Gson().toJson(baseReq));
        }
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.e("bcz-WXPayEntryActivity","baseResp:" + new Gson().toJson(baseResp));
        if(baseResp.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){

            if(baseResp.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){

                if(baseResp.errCode == -2){
                    ToastUtils.show("取消支付");
                }
                MyApplication.wxpayResltCode = baseResp.errCode;
                finish();
            }
        }
    }

}
