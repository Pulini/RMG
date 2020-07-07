package com.yiyun.rmj.bean.apibean;

import java.util.ArrayList;

public class RotationBean {
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
        private ArrayList<SlidePicture> data;
        private String message;

        public ArrayList<SlidePicture> getData() {
            return data;
        }

        public void setData(ArrayList<SlidePicture> data) {
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public class SlidePicture{
        private int productId;
        private String slideshowPicture;
        private int type;
        private String title;
        private String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public String getSlideshowPicture() {
            return slideshowPicture;
        }

        public void setSlideshowPicture(String slideshowPicture) {
            this.slideshowPicture = slideshowPicture;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
