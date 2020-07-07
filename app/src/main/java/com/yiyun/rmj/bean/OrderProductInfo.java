package com.yiyun.rmj.bean;


import java.io.Serializable;
import java.util.ArrayList;

public class OrderProductInfo implements Serializable {
    private double order_item_price;
    private ArrayList<PictureInfo> product_picture;
    private int order_item_num;
    private double product_price;
    private String product_name;
    private int product_id;

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getOrder_item_num() {
        return order_item_num;
    }

    public void setOrder_item_num(int order_item_num) {
        this.order_item_num = order_item_num;
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

    public double getOrder_item_price() {
        return order_item_price;
    }

    public void setOrder_item_price(double order_item_price) {
        this.order_item_price = order_item_price;
    }

    public ArrayList<PictureInfo> getProduct_picture() {
        return product_picture;
    }

    public void setProduct_picture(ArrayList<PictureInfo> product_picture) {
        this.product_picture = product_picture;
    }
}
