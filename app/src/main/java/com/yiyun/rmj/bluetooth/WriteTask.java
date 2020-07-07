package com.yiyun.rmj.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;

/**写入线程
 * Created by zqf on 2018/7/7.
 */

public class WriteTask extends AsyncTask<String, Integer, String> {
    private static final String TAG = WriteTask.class.getName();
    private IReadWriteCallBack callBack;
    private BluetoothSocket socket;

    public WriteTask(IReadWriteCallBack callBack, BluetoothSocket socket){
        this.callBack = callBack;
        this.socket = socket;
    }
    @Override
    protected String doInBackground(String... strings) {
        String string = strings[0];
        OutputStream outputStream = null;
        try{
            outputStream = socket.getOutputStream();

            outputStream.write(string.getBytes());
        } catch (IOException e) {
            Log.e("error", "ON RESUME: Exception during write.", e);
            return "发送失败";
        }finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "发送成功";


    }

    @Override
    protected void onPreExecute() {
        if (callBack != null) callBack.onStarted();
    }

    @Override
    protected void onPostExecute(String s) {
        if (callBack != null){
            if ("发送成功".equals(s)){
                callBack.onFinished(true, s);
            }else {
                callBack.onFinished(false, s);
            }

        }
    }
}
