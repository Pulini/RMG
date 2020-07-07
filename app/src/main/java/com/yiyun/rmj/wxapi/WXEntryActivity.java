package com.yiyun.rmj.wxapi;

import android.util.Log;

import com.google.gson.Gson;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

public class WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler {

    @Override
    public void onReq(BaseReq baseReq) {
        Log.e("bcz","baseReq:" + new Gson().toJson(baseReq));
        if(baseReq.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){
            Log.d("bcz","baseReq:" + new Gson().toJson(baseReq));
        }
    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.e("bcz","baseResp:" + new Gson().toJson(baseResp));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



}
