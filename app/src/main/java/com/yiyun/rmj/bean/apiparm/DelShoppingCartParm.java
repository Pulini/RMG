package com.yiyun.rmj.bean.apiparm;

import com.yiyun.rmj.bean.apibase.BaseParm;

public class DelShoppingCartParm extends BaseParm {
    private String favoriteIdsStr;//多个以逗号相隔

    public String getFavoriteIdsStr() {
        return favoriteIdsStr;
    }

    public void setFavoriteIdsStr(String favoriteIdsStr) {
        this.favoriteIdsStr = favoriteIdsStr;
    }
}
