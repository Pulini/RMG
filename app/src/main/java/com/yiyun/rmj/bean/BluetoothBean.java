package com.yiyun.rmj.bean;

import com.yiyun.rmj.activity.bluetooth.SettingListModel;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class BluetoothBean extends DataSupport {

    private int id;
    private int type;           //蓝牙类型
    private String deviceName; //设备名
    private String address;  //Mac地址
    private int bundState;  //绑定状态
    private List<SettingListModel> list = new ArrayList();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<SettingListModel> getList() {
        return list;
    }

    public void setList(List<SettingListModel> list) {
        this.list = list;
    }
//    public List<BluetoothSettingBean> getList() {
//        //子表中会生成一个关联父表的id供父表查询，且字表中id生成符合规则："父表类名小写_id"
//        //若父表为Person类(父表中会自动生成一个id自增列)，子表为User类,则字表中会自动生成字段person_id对应父表中id，以供查询
//        List<BluetoothSettingBean> settingList= DataSupport.where("deviceid=?",String.valueOf(id)).order("id asc").find(BluetoothSettingBean.class);
//        if(settingList == null){
//            settingList=new ArrayList<>();
//        }
//        Log.e("bcz","getList:listSize:" + settingList.size());
//        return settingList;
//    }
//
//    public void setList(List<BluetoothSettingBean> list) {
//        //批量存储userList
//        if(list != null && list.size() != 0){
//            DataSupport.saveAll(list);
//        }
//        this.list = list;
//    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getBundState() {
        return bundState;
    }

    public void setBundState(int bundState) {
        this.bundState = bundState;
    }
}
