package com.yiyun.rmj.bean.apibean;

import com.yiyun.rmj.bean.InvitationRecord;

import java.util.ArrayList;

public class RecordListBean {
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
        private Data data;
        private int isNull;
        private String message;

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public int getIsNull() {
            return isNull;
        }

        public void setIsNull(int isNull) {
            this.isNull = isNull;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public class Data{
        private ArrayList<InvitationRecord> invitationRecordList;
        private InvitationRecord myInvitationRecord;

        public ArrayList<InvitationRecord> getInvitationRecordList() {
            return invitationRecordList;
        }

        public void setInvitationRecordList(ArrayList<InvitationRecord> invitationRecordList) {
            this.invitationRecordList = invitationRecordList;
        }

        public InvitationRecord getMyInvitationRecord() {
            return myInvitationRecord;
        }

        public void setMyInvitationRecord(InvitationRecord myInvitationRecord) {
            this.myInvitationRecord = myInvitationRecord;
        }
    }


}
