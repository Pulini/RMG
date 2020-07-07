package com.yiyun.rmj.bean.apiparm;

import com.yiyun.rmj.bean.apibase.BaseParm;

public class ZfbParm extends BaseParm {

    private String name; //姓名
    private String alipayNum; //支付宝账号
    private int type; //0：新增  1:修改
    private int alipayId; //支付宝id

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlipayNum() {
        return alipayNum;
    }

    public void setAlipayNum(String alipayNum) {
        this.alipayNum = alipayNum;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAlipayId() {
        return alipayId;
    }

    public void setAlipayId(int alipayId) {
        this.alipayId = alipayId;
    }
}
