package com.yiyun.rmj.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/*
import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.conversation.EServiceContact;
*/

/**
 * File Name : Ali
 * Created by : PanZX on 2020/07/09
 * Email : 644173944@qq.com
 * Github : https://github.com/Pulini
 * Remark：
 */
public class Ali {
   /*
    public static void YWLogin(final Context context, final boolean isNeedJumpToServer) {
        //******************云旺登录start**************************


        Log.d("云旺", "登录账号 == " + (SpUtils.getYWUserID(context) + "   登录密码 == " + SpUtils.getYWPwd(context)));

        //【1】获取IMKit对象
        mIMKit = YWAPI.getIMKitInstance(SpUtils.getYWUserID(context), MyStatic.YWAPPKEY); //UERID、 APPKEY
        //【2】创建登录对象
        YWLoginParam loginParam = YWLoginParam.createLoginParam(SpUtils.getYWUserID(context), SpUtils.getYWPwd(context));//用户名、密码
        //【3】获取登录服务
        final IYWLoginService loginService = mIMKit.getLoginService();

        //【4】开始登录
        loginService.login(loginParam, new IWxCallback() {

            @Override
            public void onSuccess(Object... arg0) {

                Log.d("云旺登录成功", "ok ");


                if (isNeedJumpToServer) {
                    //TODO
                    //（1）打开会话界面发送消息
                    //（2）跳转到客服聊天界面
                    EServiceContact contact = new EServiceContact(MyStatic.YWSERVERID, MyStatic.YWSERVERGROUPID);

                    //如果需要发给指定的客服帐号，不需要Server进行分流(默认Server会分流)，请调用EServiceContact对象
                    //的setNeedByPass方法，参数为false。
                    //contact.setNeedByPass(false);

                    Intent intent = getmIMKit(context).getChattingActivityIntent(contact);
                    context.startActivity(intent);

                }
            }

            @Override
            public void onProgress(int arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onError(int errCode, String description) {
                //如果登录失败，errCode为错误码,description是错误的具体描述信息

                Log.d("云旺登录失败", "错误码 == " + errCode + "    " + description);
            }
        });


        //***********************云旺登录end*****************************

    }

    *//**
     * 获取YWIMKit 对象
     *
     * @return YWIMKit 对象
     *//*
    public static YWIMKit getmIMKit(Context context) {
        YWIMKit mIMKit = YWAPI.getIMKitInstance(SpUtils.getYWUserID(context), MyStatic.YWAPPKEY); //UERID、 APPKEY
        return mIMKit;
    }

    *//**
     * 云旺账号退出
     *//*
    public static void YWLogout(Context context) {
        String userID = SpUtils.getYWUserID(context);

        if ("".equals(userID) || userID == null) {
            return;
        }

        //【1】获取IMKit对象
        mIMKit = YWAPI.getIMKitInstance(userID, MyStatic.YWAPPKEY); //UERID、 APPKEY
        //【2】获取登录服务
        final IYWLoginService loginService = mIMKit.getLoginService();

        //【3】退出登录
        loginService.logout(new IWxCallback() {

            @Override
            public void onSuccess(Object... arg0) {

                Log.d("云旺退出登录成功", "ok ");


                //TODO 如果本地存储了云旺信息，需要删除

            }

            @Override
            public void onProgress(int arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onError(int errCode, String description) {
                //如果登录失败，errCode为错误码,description是错误的具体描述信息

                Log.d("云旺退出登录失败", "错误码 == " + errCode + "    " + description);
            }
        });

    }

    *//**
     * 判断登录状态
     *
     * @return 云旺登录状态
     * disconnect账号在其他地方登录    fail登陆失败   idle  idle状态   logining登陆中  success登陆成功
     *//*
    public static int YWLoginJudge(Context context) {
        YWIMKit mIMKit = YWAPI.getIMKitInstance(SpUtils.getYWUserID(context), MyStatic.YWAPPKEY); //UERID、 APPKEY
        int value = mIMKit.getIMCore().getLoginState().getValue();

        return value;
    }

    */

    /**
     * 去除打开聊天界面出现的电池优化的dialog
     *//*
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
if(-1==sharedPreferences.getInt("IgnoreBatteryOpt",-1))

    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("IgnoreBatteryOpt", 1);
        editor.commit();
    }
    */
//    public static YWIMKit IMKitInstance() {
//        return YWAPI.getIMKitInstance("testpro1", "23015524");
//    }
//
//    public static void LoginIM(Context context) {
//        //开始登录
//        String userid = "testpro1";
//        String password = "taobao1234";
//        YWIMKit mIMKit = YWAPI.getIMKitInstance(userid, "23015524");
//        IYWLoginService loginService = mIMKit.getLoginService();
//        YWLoginParam loginParam = YWLoginParam.createLoginParam(userid, password);
//        loginService.login(loginParam, new IWxCallback() {
//
//            @Override
//            public void onSuccess(Object... arg0) {
//                //userid是客服帐号，第一个参数是客服帐号，第二个是组ID，如果没有，传0
//                EServiceContact contact = new EServiceContact("userid", 0);
//                //如果需要发给指定的客服帐号，不需要Server进行分流(默认Server会分流)，请调用EServiceContact对象
//                //的setNeedByPass方法，参数为false。
//                //contact.setNeedByPass(false);
//                Intent intent = mIMKit.getChattingActivityIntent(contact);
//                context.startActivity(intent);
//            }
//
//            @Override
//            public void onProgress(int arg0) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void onError(int errCode, String description) {
//                //如果登录失败，errCode为错误码,description是错误的具体描述信息
//            }
//        });
//    }
}
