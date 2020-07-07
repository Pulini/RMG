package com.yiyun.rmj.bean.apibean;

public class CustomServiceBean {

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
        private CustomerInfo data;

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

        public CustomerInfo getData() {
            return data;
        }

        public void setData(CustomerInfo data) {
            this.data = data;
        }
    }


    public class CustomerInfo{

        private String vx_afterSale="";
        private String qq_preSale="";
        private String qq_afterSale="";
        private String vx_preSale="";

        public String getVx_afterSale() {
            return vx_afterSale;
        }

        public void setVx_afterSale(String vx_afterSale) {
            this.vx_afterSale = vx_afterSale;
        }

        public String getQq_preSale() {
            return qq_preSale;
        }

        public void setQq_preSale(String qq_preSale) {
            this.qq_preSale = qq_preSale;
        }

        public String getQq_afterSale() {
            return qq_afterSale;
        }

        public void setQq_afterSale(String qq_afterSale) {
            this.qq_afterSale = qq_afterSale;
        }

        public String getVx_preSale() {
            return vx_preSale;
        }

        public void setVx_preSale(String vx_preSale) {
            this.vx_preSale = vx_preSale;
        }
    }

}


