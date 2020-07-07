package com.yiyun.rmj.bean.apibean;

public class PersonCenterBean {
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
        private String message;
        private Data data;
        private int isNull;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public int getIsNull() {
            return isNull;
        }

        public void setIsNull(int isNull) {
            this.isNull = isNull;
        }
    }

    public class Data{
        private String head;
        private int toBeReceived;
        private double money;
        private int toBeEvaluated;
        private String name;
        private int unpaid;

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public int getToBeReceived() {
            return toBeReceived;
        }

        public void setToBeReceived(int toBeReceived) {
            this.toBeReceived = toBeReceived;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public int getToBeEvaluated() {
            return toBeEvaluated;
        }

        public void setToBeEvaluated(int toBeEvaluated) {
            this.toBeEvaluated = toBeEvaluated;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getUnpaid() {
            return unpaid;
        }

        public void setUnpaid(int unpaid) {
            this.unpaid = unpaid;
        }
    }
}
