package com.yiyun.rmj.bean;

public class MoneyDetailInfo {

    private int money_detail_id;
    private String money;
    private String save_time;
    private String remark;
    private String detail;

    public int getMoney_detail_id() {
        return money_detail_id;
    }

    public void setMoney_detail_id(int money_detail_id) {
        this.money_detail_id = money_detail_id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getSave_time() {
        return save_time;
    }

    public void setSave_time(String save_time) {
        this.save_time = save_time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
