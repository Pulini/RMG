package com.yiyun.rmj.bean.apibean;

import com.yiyun.rmj.bean.MoneyDetailInfo;

import java.util.ArrayList;

public class MoneyDetailBean {

    private int state;
    private Info info;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public class Info{
        private ArrayList<MoneyDetailInfo> data;
        private String message;
        private int isNull;

        public ArrayList<MoneyDetailInfo> getData() {
            return data;
        }

        public void setData(ArrayList<MoneyDetailInfo> data) {
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getIsNull() {
            return isNull;
        }

        public void setIsNull(int isNull) {
            this.isNull = isNull;
        }
    }
}
