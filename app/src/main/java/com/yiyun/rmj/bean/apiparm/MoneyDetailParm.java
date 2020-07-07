package com.yiyun.rmj.bean.apiparm;

import com.yiyun.rmj.bean.apibase.BaseParm;

public class MoneyDetailParm extends BaseParm {

    private int type;//type :类型 0：收入 1:全部 2

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
