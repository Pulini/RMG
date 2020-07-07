package com.yiyun.rmj.bean.apiparm;

import com.yiyun.rmj.bean.apibase.BaseParm;

public class GetCodeParm extends BaseParm {
    private String phone; //手机号码
    private int type; //1：注册 2： 忘记密码 3：修改密码

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
