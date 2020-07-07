package com.yiyun.rmj.setting;

public class UrlContact {

    public static int LOGIN_TYPE_OWN = 1; //自己的渠道
    public static int LOGIN_TYPE_WEIXIN = 2; //微信渠道
    public static int LOGIN_TYPE_QQ = 3; //QQ登录
//    public static String BASEURL = "http://47.99.146.130:8082/";
    public static String BASEURL = "http://www.seeege.com/";
    public static String WXAPPID = "wxd05a2356c3312721";
    public static String getBaseUrl(){
        return BASEURL;
    }
    public static long exitTime;
}
