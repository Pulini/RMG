package com.yiyun.rmj.bean;


import java.util.ArrayList;

public class ProductInfo {
    private int product_id;
    private int num;
    private  double product_price;
    private String product_name;
    private ArrayList<PictureInfo> product_picture;

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(double product_price) {
        this.product_price = product_price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public ArrayList<PictureInfo> getProduct_picture() {
        return product_picture;
    }

    public void setProduct_picture(ArrayList<PictureInfo> product_picture) {
        this.product_picture = product_picture;
    }
}
