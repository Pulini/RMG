package com.yiyun.rmj.bean.apibean;

import java.util.ArrayList;

public class ShoppingCartBean {
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
        ArrayList<CartProduct> data;

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

        public ArrayList<CartProduct> getData() {
            return data;
        }

        public void setData(ArrayList<CartProduct> data) {
            this.data = data;
        }
    }

    public class ProductPicture{

        private int type;
        private String url;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
