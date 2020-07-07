package com.yiyun.rmj.bean.apibean;

import java.util.ArrayList;

public class ZfbAccountBean{

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
        private ArrayList<Account> data;

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

        public ArrayList<Account> getData() {
            return data;
        }

        public void setData(ArrayList<Account> data) {
            this.data = data;
        }
    }

    public class Account{
        private int isDefault;
        private String alipayNum;
        private String name;
        private int alipayId;

        public int getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(int isDefault) {
            this.isDefault = isDefault;
        }

        public String getAlipayNum() {
            return alipayNum;
        }

        public void setAlipayNum(String alipayNum) {
            this.alipayNum = alipayNum;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAlipayId() {
            return alipayId;
        }

        public void setAlipayId(int alipayId) {
            this.alipayId = alipayId;
        }
    }
}
