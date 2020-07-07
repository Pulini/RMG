package com.yiyun.rmj.bean.apibean;

import java.util.ArrayList;

public class IntegrationBean {
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

    public class Data{
        private int integral;
        ArrayList<Integration> list;

        public ArrayList<Integration> getList() {
            return list;
        }

        public void setList(ArrayList<Integration> list) {
            this.list = list;
        }

        public int getIntegral() {
            return integral;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
        }
    }

    public class Info{
        private int isNull;
        private String message;
        private  Data data;


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

    public class Integration{
        private String integralNumber; //+20
        private String saveType;//"2019-05-10 16:10:43"
        private String instructions; // "邀请好友"
        private String integralType; // "成功邀请好友并注册"

        public String getIntegralNumber() {
            return integralNumber;
        }

        public void setIntegralNumber(String integralNumber) {
            this.integralNumber = integralNumber;
        }

        public String getSaveType() {
            return saveType;
        }

        public void setSaveType(String saveType) {
            this.saveType = saveType;
        }

        public String getInstructions() {
            return instructions;
        }

        public void setInstructions(String instructions) {
            this.instructions = instructions;
        }

        public String getIntegralType() {
            return integralType;
        }

        public void setIntegralType(String integralType) {
            this.integralType = integralType;
        }
    }
}
