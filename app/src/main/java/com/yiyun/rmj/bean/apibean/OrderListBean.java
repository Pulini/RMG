package com.yiyun.rmj.bean.apibean;

import com.yiyun.rmj.bean.OrderInfo;

import java.util.ArrayList;

public class OrderListBean {
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
        private int isNull;
        private String message;
        private ArrayList<OrderInfo> data;

        public int getIsNull() {
            return isNull;
        }

        public void setIsNull(int isNull) {
            this.isNull = isNull;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ArrayList<OrderInfo> getData() {
            return data;
        }

        public void setData(ArrayList<OrderInfo> data) {
            this.data = data;
        }
    }

}
