package com.yiyun.rmj.bean.apiparm;

public class LoginParm {
    private String phone;  //手机号码
    private String password; //密码
    private String openid;
    private int type; //0账号密码  1qq  2vx

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
