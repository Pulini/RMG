package com.yiyun.rmj.bean.apibean;

import com.yiyun.rmj.bean.PictureInfo;

import java.util.ArrayList;

public class UpHeadImageBean {

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
        private ArrayList<PictureInfo> imgPath;
        private String message;

        public ArrayList<PictureInfo> getImgPath() {
            return imgPath;
        }

        public void setImgPath(ArrayList<PictureInfo> imgPath) {
            this.imgPath = imgPath;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
