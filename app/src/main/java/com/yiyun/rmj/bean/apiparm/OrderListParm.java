package com.yiyun.rmj.bean.apiparm;

import com.yiyun.rmj.bean.apibase.BaseParm;

public class OrderListParm extends BaseParm {
    private int orderState;//订单状态(0全部 ，1未支付，2已支付（待发货），3已发货（待收货），4待评价，5退货中，6已退货，7拒绝退货 ,8交易成功
    private int page; //页码
    private int limit = 10; //展示数量

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
