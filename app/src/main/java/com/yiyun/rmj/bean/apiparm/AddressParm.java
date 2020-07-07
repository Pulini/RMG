package com.yiyun.rmj.bean.apiparm;

import com.yiyun.rmj.bean.apibase.BaseParm;

public class AddressParm extends BaseParm {

    //token:token , name:收件人姓名 , phone:收件人手机号 , province:省 , city:城市 , adress:详细地址 , type:0：新增 1：修改 , addressId:地址id type=1时传
    private String name;//收件人姓名
    private String phone;// 收件人手机号
    private String province; //省
    private String city;  // 城市
    private String area; //区
    private String address; // 详细地址
    private int type; //0：新增  1：修改
    private int addressId; //地址id



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String adress) {
        this.address = adress;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}
