package com.yiyun.rmj.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderInfo implements Serializable {

    private String deliverTime;
    private String orderNo;
    private int orderId;
    private double orderPrice;
    private int userId;
    private int orderState;
    private String state;
    private double express_money;
    private ArrayList<OrderProductInfo> orderItem;
    private String payTime;
    private String address;
    private int orderNum;
    private String message;
    private String courierNumber;
    private String consigneeName;
    private String phone;
    private String courierCompany;
    private ProgressInfo logistics;
    private int receivAddressId;
    private double integral_deduction;

    public String getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(String deliverTime) {
        this.deliverTime = deliverTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCourierCompany() {
        return courierCompany;
    }

    public void setCourierCompany(String courierCompany) {
        this.courierCompany = courierCompany;
    }

    public ProgressInfo getLogistics() {
        return logistics;
    }

    public void setLogistics(ProgressInfo logistics) {
        this.logistics = logistics;
    }

    public double getIntegral_deduction() {
        return integral_deduction;
    }

    public void setIntegral_deduction(double integral_deduction) {
        this.integral_deduction = integral_deduction;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCourierNumber() {
        return courierNumber;
    }

    public void setCourierNumber(String courierNumber) {
        this.courierNumber = courierNumber;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getReceivAddressId() {
        return receivAddressId;
    }

    public void setReceivAddressId(int receivAddressId) {
        this.receivAddressId = receivAddressId;
    }

    public double getExpress_money() {
        return express_money;
    }

    public void setExpress_money(double express_money) {
        this.express_money = express_money;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public ArrayList<OrderProductInfo> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(ArrayList<OrderProductInfo> orderItem) {
        this.orderItem = orderItem;
    }
}
