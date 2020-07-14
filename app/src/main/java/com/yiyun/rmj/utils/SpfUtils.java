package com.yiyun.rmj.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yiyun.rmj.activity.bluetooth.SettingListModel;
import com.yiyun.rmj.base.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 80999 on 2018/11/26.
 */

public class SpfUtils {
    private static SpfUtils spfUtils = null;
    private SharedPreferences spf;
    private SharedPreferences.Editor editor;
    private Context mContext;

    public SpfUtils(Context context) {
        mContext = context;
        spf = context.getSharedPreferences("load", Context.MODE_PRIVATE);
        editor = spf.edit();
    }

    public static SpfUtils getSpfUtils(Context context) {
        if (spfUtils == null) {
            synchronized (SpfUtils.class) {
                if (spfUtils == null) {
                    spfUtils = new SpfUtils(context);
                }
            }
        }
        return spfUtils;
    }

    public boolean isNeedUpdate() {
        return spf.getBoolean("isNeedUpdate", true);
    }

    public void setIsNeedUpdate(boolean isNeed) {
        editor.putBoolean("isNeedUpdate", isNeed);
        editor.commit();
    }

    //登录方式
    public void setLoginType(int type) {
        editor.putInt("login_type", type);
        editor.commit();
    }

    public int getLoginType() {
        return spf.getInt("login_type", -1);
    }

    //微信登录token
    public void setWeichatAccessToken(String weichatAcctssToken) {
        editor.putString("wei_chat_access_token", weichatAcctssToken);
        editor.commit();
    }

    public void setToken(String token) {
        editor.putString("token", token);
        editor.commit();
    }

    public String getToken() {
        return spf.getString("token", "");
    }

    public String getWeichatAccessToken() {
        return spf.getString("wei_chat_access_token", "");
    }

    //微信uid
    public String getOpenId() {
        return spf.getString("openid", "");
    }

    public void setOpenId(String openId) {
        editor.putString("openid", openId);
        editor.commit();
    }

    public void setAccount(String account) {
        editor.putString("account", account);
        editor.commit();
    }

    public void setDefaultAlipayId(int alipayId) {
        editor.putInt("alipayId", alipayId);
        editor.commit();
    }

    public int getDefaultAlipayId() {
        return spf.getInt("alipayId", -1);
    }

    public void setDefaultAddressId(int addressId) {
        editor.putInt("addressId", addressId);
        editor.commit();
    }

    public int getDefaultAddressId() {
        return spf.getInt("addressId", -1);
    }


    public String getAccount() {
        return spf.getString("account", "");
    }

    //用户名
    public void setUserName(String userName) {
        editor.putString("username", userName);
        editor.commit();
    }

    public String getUserName() {
        return spf.getString("username", "");
    }

    //头像Url
    public void setHeadImageUrl(String imageUrl) {
        editor.putString("head_img_url", imageUrl);
        editor.commit();
    }

    //头像Url
    public String getHeadImageUrl() {
        return spf.getString("head_img_url", "");
    }

    //引导页
    public void setguide(boolean guide) {
        editor.putBoolean("guide", guide);
        editor.commit();
    }

    public boolean getguide() {
        return spf.getBoolean("guide", false);
    }

    public void delete() {
        editor.putString("login_type", "");
        editor.putString("wei_chat_access_token", "");
        editor.putString("openid", "");
        editor.putString("username", "");
        editor.putString("head_img_url", "");
        editor.putString("token", "");
        editor.putString("account", "");
        editor.putInt("alipayId", -1);
        editor.putInt("addressId", -1);


        editor.commit();
    }

    public static List<SettingListModel> getBluetoothSetList(int id) {
        String json = MyApplication.getSP().getString("BluetoothSetList:" + id, "");
        Log.e("Pan","json="+json);
        List<SettingListModel> list = new Gson().fromJson(json, new TypeToken<List<SettingListModel>>() {
                }.getType());
        return list==null?new ArrayList<>():list;
    }

    public static void saveBluetoothSetList(List<SettingListModel> data,int id) {
        SharedPreferences.Editor edit = MyApplication.getSP().edit();
        edit.putString("BluetoothSetList:" + id, new Gson().toJson(data));
        edit.commit();
    }

}
