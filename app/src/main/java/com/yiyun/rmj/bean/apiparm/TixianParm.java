package com.yiyun.rmj.bean.apiparm;

import com.yiyun.rmj.bean.apibase.BaseParm;

public class TixianParm extends BaseParm {

    private String money;
    private int alipayId;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getAlipayId() {
        return alipayId;
    }

    public void setAlipayId(int alipayId) {
        this.alipayId = alipayId;
    }
}
