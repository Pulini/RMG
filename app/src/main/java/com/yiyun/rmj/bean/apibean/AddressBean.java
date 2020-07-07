package com.yiyun.rmj.bean.apibean;

import java.util.ArrayList;

public class AddressBean {
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
        private String message;
        private ArrayList<AddressDetail> data;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ArrayList<AddressDetail> getData() {
            return data;
        }

        public void setData(ArrayList<AddressDetail> data) {
            this.data = data;
        }
    }

    public class AddressDetail{
        private String address;
        private String province;
        private String consignee_name;
        private String addressStr;
        private String city;
        private String area;
        private String phone;
        private int receiv_address_id;
        private int if_default_address;  //0不是默认， 1是默认

        public String getConsignee_name() {
            return consignee_name;
        }

        public void setConsignee_name(String consignee_name) {
            this.consignee_name = consignee_name;
        }

        public String getAddressStr() {
            return addressStr;
        }

        public void setAddressStr(String addressStr) {
            this.addressStr = addressStr;
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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
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
}
