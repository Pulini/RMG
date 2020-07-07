package com.yiyun.rmj.bean.apibean;

import com.yiyun.rmj.bean.PictureInfo;

import java.util.ArrayList;

public class CollectionBean {
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
        private ArrayList<Collection> data;

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

        public ArrayList<Collection> getData() {
            return data;
        }

        public void setData(ArrayList<Collection> data) {
            this.data = data;
        }
    }

    public class Collection{

        private int product_id;
        private double product_price;
        private int inventory;
        private String product_name;
        private ArrayList<PictureInfo> product_picture;

        public ArrayList<PictureInfo> getProduct_picture() {
            return product_picture;
        }

        public void setProduct_picture(ArrayList<PictureInfo> product_picture) {
            this.product_picture = product_picture;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public double getProduct_price() {
            return product_price;
        }

        public void setProduct_price(double product_price) {
            this.product_price = product_price;
        }

        public int getInventory() {
            return inventory;
        }

        public void setInventory(int inventory) {
            this.inventory = inventory;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }
    }
}
