package com.yiyun.rmj.bean.apibean;

import com.yiyun.rmj.bean.OrderProgressInfo;

public class OrderProgressBean {

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
        private ProgressData data;
        private int isNull;
        private String message;

        public ProgressData getData() {
            return data;
        }

        public void setData(ProgressData data) {
            this.data = data;
        }

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
    }

    public class ProgressData{
        private String courier_number;
        private String courier_company;
        private OrderProgressInfo logistics;
        private String product_picture;

        public String getCourier_number() {
            return courier_number;
        }

        public void setCourier_number(String courier_number) {
            this.courier_number = courier_number;
        }

        public String getCourier_company() {
            return courier_company;
        }

        public void setCourier_company(String courier_company) {
            this.courier_company = courier_company;
        }

        public OrderProgressInfo getLogistic() {
            return logistics;
        }

        public void setLogistic(OrderProgressInfo logistic) {
            this.logistics = logistic;
        }

        public String getProduct_picture() {
            return product_picture;
        }

        public void setProduct_picture(String product_picture) {
            this.product_picture = product_picture;
        }
    }
}
