package com.yiyun.rmj.bean;

public class AddressInfo {

    private String area;
    private String address;
    private String province;
    private String city;
    private String phone;
    private String consignee_name;
    private int receiv_address_id;
    private int if_default_address;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getConsignee_name() {
        return consignee_name;
    }

    public void setConsignee_name(String consignee_name) {
        this.consignee_name = consignee_name;
    }

    public int getReceiv_address_id() {
        return receiv_address_id;
    }

    public void setReceiv_address_id(int receiv_address_id) {
        this.receiv_address_id = receiv_address_id;
    }

    public int getIf_default_address() {
        return if_default_address;
    }

    public void setIf_default_address(int if_default_address) {
        this.if_default_address = if_default_address;
    }
}
