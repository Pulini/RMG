package com.yiyun.rmj.bean;

public class InvitationRecord {

    private int c_invited_user_id;
    private String phone;
    private String rownum;
    private String head;
    private int count;
    private int invite;
    private String username;

    public int getC_invited_user_id() {
        return c_invited_user_id;
    }

    public void setC_invited_user_id(int c_invited_user_id) {
        this.c_invited_user_id = c_invited_user_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRownum() {
        return rownum;
    }

    public void setRownum(String rownum) {
        this.rownum = rownum;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getInvite() {
        return invite;
    }

    public void setInvite(int invite) {
        this.invite = invite;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
