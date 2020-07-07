package com.yiyun.rmj.bean.apibean;

import android.graphics.Bitmap;

import com.yiyun.rmj.bean.PictureInfo;

import java.util.ArrayList;

public class ProductBean {
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
        private ArrayList<Data> data;
        private String message;

        public ArrayList<Data> getData() {
            return data;
        }

        public void setData(ArrayList<Data> data) {
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class Data{
        public String VideoUrl;
        public Bitmap VideoImage;
        public boolean isReady=false;
        private String productModel;
        private int productId;
        private int isCollection;
        private String composition;
        private int primaryClassificationId;
        private String primaryClassificationName;
        private String productName;
        private String productPrice;
        private ArrayList<PictureInfo> productPicture;
        public String getProductModel() {
            return productModel;
        }

        public String getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(String productPrice) {
            this.productPrice = productPrice;
        }

        public void setProductModel(String productModel) {
            this.productModel = productModel;
        }

        public int getIsCollection() {
            return isCollection;
        }

        public void setIsCollection(int isCollection) {
            this.isCollection = isCollection;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public String getComposition() {
            return composition;
        }

        public void setComposition(String composition) {
            this.composition = composition;
        }

        public int getPrimaryClassificationId() {
            return primaryClassificationId;
        }

        public void setPrimaryClassificationId(int primaryClassificationId) {
            this.primaryClassificationId = primaryClassificationId;
        }

        public String getPrimaryClassificationName() {
            return primaryClassificationName;
        }

        public void setPrimaryClassificationName(String primaryClassificationName) {
            this.primaryClassificationName = primaryClassificationName;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public ArrayList<PictureInfo> getProductPicture() {
            return productPicture;
        }

        public void setProductPicture(ArrayList<PictureInfo> productPicture) {
            this.productPicture = productPicture;
        }
    }

}
