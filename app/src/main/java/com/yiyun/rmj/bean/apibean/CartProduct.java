package com.yiyun.rmj.bean.apibean;

import java.util.ArrayList;

public class CartProduct{
    private int delete_state;
    private boolean checked = false;
    private int num;
    private int product_id;
    private String product_name;
    private int favorite_id;
    private double product_price;
    private ArrayList<ShoppingCartBean.ProductPicture> product_picture;

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getDelete_state() {
        return delete_state;
    }

    public void setDelete_state(int delete_state) {
        this.delete_state = delete_state;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getFavorite_id() {
        return favorite_id;
    }

    public void setFavorite_id(int favorite_id) {
        this.favorite_id = favorite_id;
    }

    public double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(double product_price) {
        this.product_price = product_price;
    }

    public ArrayList<ShoppingCartBean.ProductPicture> getProduct_picture() {
        return product_picture;
    }

    public void setProduct_picture(ArrayList<ShoppingCartBean.ProductPicture> product_picture) {
        this.product_picture = product_picture;
    }
}
