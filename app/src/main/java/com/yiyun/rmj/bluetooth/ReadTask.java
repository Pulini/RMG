package com.yiyun.rmj.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;

/**读取线程
 * Created by zqf on 2018/7/7.
 */

public class ReadTask extends AsyncTask<String, Integer, String> {
    private static final String TAG = ReadTask.class.getName();
    private IReadWriteCallBack callBack;
    private BluetoothSocket socket;

    public ReadTask(IReadWriteCallBack callBack, BluetoothSocket socket){
        this.callBack = callBack;
        this.socket = socket;
    }
    @Override
    protected String doInBackground(String... strings) {
        BufferedInputStream in = null;
        try {
            StringBuffer sb = new StringBuffer();
            in = new BufferedInputStream(socket.getInputStream());

            int length = 0;
            byte[] buf = new byte[1024];
            while ((length = in.read()) != -1) {
                sb.append(new String(buf,0,length));
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "读取失败";
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG,"开始读取数据");
        if (callBack != null) callBack.onStarted();
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG,"完成读取数据");
        if (callBack != null){
            if ("读取失败".equals(s)){
                callBack.onFinished(false, s);
            }else {
                callBack.onFinished(true, s);
            }
        }
    }
}
