package com.yiyun.rmj.bean.apibean;

import com.yiyun.rmj.bean.AddressInfo;
import com.yiyun.rmj.bean.ProductInfo;

import java.util.ArrayList;

public class BeforePayBean {
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
        private Data data;

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

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }
    }

    public class Data{
        private ArrayList<ProductInfo> productItem;
        private double allPrice;
        private int expressMoney;
        private int integral;
        private int integralRatio;
        private int allNum;
        private AddressInfo defaultAddress;

        public ArrayList<ProductInfo> getProductItem() {
            return productItem;
        }

        public void setProductItem(ArrayList<ProductInfo> productItem) {
            this.productItem = productItem;
        }

        public double getAllPrice() {
            return allPrice;
        }

        public void setAllPrice(double allPrice) {
            this.allPrice = allPrice;
        }

        public int getExpressMoney() {
            return expressMoney;
        }

        public void setExpressMoney(int expressMoney) {
            this.expressMoney = expressMoney;
        }

        public int getIntegral() {
            return integral;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
        }

        public int getIntegralRatio() {
            return integralRatio;
        }

        public void setIntegralRatio(int integralRatio) {
            this.integralRatio = integralRatio;
        }

        public int getAllNum() {
            return allNum;
        }

        public void setAllNum(int allNum) {
            this.allNum = allNum;
        }

        public AddressInfo getDefaultAddress() {
            return defaultAddress;
        }

        public void setDefaultAddress(AddressInfo defaultAddress) {
            this.defaultAddress = defaultAddress;
        }
    }

}
