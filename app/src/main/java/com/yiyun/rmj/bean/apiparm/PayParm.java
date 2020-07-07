package com.yiyun.rmj.bean.apiparm;

import com.yiyun.rmj.bean.ProductSampleInfo;
import com.yiyun.rmj.bean.apibase.BaseParm;

import java.util.ArrayList;

public class PayParm extends BaseParm {

    private ArrayList<ProductSampleInfo> productJson;
    private int addressId;
    private int payType; //支付方式 0：支付宝 1：微信
    private String message;
    private int type;//0:购物车购买 1：直接购买
    private int deduction; //积分
    private int orderId; //订单id

    public ArrayList<ProductSampleInfo> getProductJson() {
        return productJson;
    }

    public void setProductJson(ArrayList<ProductSampleInfo> productJson) {
        this.productJson = productJson;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDeduction() {
        return deduction;
    }

    public void setDeduction(int deduction) {
        this.deduction = deduction;
    }
}
