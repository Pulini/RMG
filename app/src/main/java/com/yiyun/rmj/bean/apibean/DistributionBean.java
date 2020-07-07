package com.yiyun.rmj.bean.apibean;

import java.util.ArrayList;

public class DistributionBean {
    private int state;
    private Info info;

    public class Info{
        private int isNull;
        private String message;
        private ArrayList<Distribution> data;

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

        public ArrayList<Distribution> getData() {
            return data;
        }

        public void setData(ArrayList<Distribution> data) {
            this.data = data;
        }
    }

    public class Distribution{
        private String phone;
        private String regis_time;
        private String type;
        private int userId;
        private String username;
        private String head;
        private String isOverdue;

        public String getIsOverdue() {
            return isOverdue;
        }

        public void setIsOverdue(String isOverdue) {
            this.isOverdue = isOverdue;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRegis_time() {
            return regis_time;
        }

        public void setRegis_time(String regis_time) {
            this.regis_time = regis_time;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

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
}
