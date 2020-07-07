package com.yiyun.rmj.bean.apiparm;

import com.yiyun.rmj.bean.apibase.BaseParm;

public class IntergralParm extends BaseParm {

    private int type; //type:2：全部 0:获得  1：使用

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
