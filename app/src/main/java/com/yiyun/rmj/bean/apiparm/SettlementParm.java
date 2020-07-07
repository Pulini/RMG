package com.yiyun.rmj.bean.apiparm;

import com.yiyun.rmj.bean.apibase.BaseParm;

public class SettlementParm extends BaseParm {

    private String favoriteIdsStr;//购物车特殊Id,以逗号拼接
    private int type; // type:0:购物车购买 1：直接购买
    private int productId; //（type=1传）
    private int num; //商品数量（type=1时传）
    private int orderId; //type =2

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getFavoriteIdsStr() {
        return favoriteIdsStr;
    }

    public void setFavoriteIdsStr(String favoriteIdsStr) {
        this.favoriteIdsStr = favoriteIdsStr;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
