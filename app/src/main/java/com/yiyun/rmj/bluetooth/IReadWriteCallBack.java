package com.yiyun.rmj.bluetooth;

public interface IReadWriteCallBack {

    public void onStarted();
    public void onFinished(boolean readState, String msg);
}
