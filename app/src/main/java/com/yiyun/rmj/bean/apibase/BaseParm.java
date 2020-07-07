package com.yiyun.rmj.bean.apibase;

import com.yiyun.rmj.base.MyApplication;
import com.yiyun.rmj.utils.SpfUtils;

public class BaseParm {
    private String token = SpfUtils.getSpfUtils(MyApplication.getInstance()).getToken();
}
