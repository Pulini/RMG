package com.yiyun.rmj.bean.apibean;

import java.util.ArrayList;

public class EvaluationBean {

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
        private int isNull;
        private String message;
        private ArrayList<Evaluation> data;

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

        public ArrayList<Evaluation> getData() {
            return data;
        }

        public void setData(ArrayList<Evaluation> data) {
            this.data = data;
        }
    }

    public class Evaluation{
        private String head;
        private String evaluateTime;
        private int orderId;
        private String evaluateContent; //评价内容
        private int browseCount; //浏览次数
        private String username;
        private int evaluateId;
        private ArrayList<String> evaluatePictures; //评价图片组
        private ArrayList<String> evaluate_pictures;
        private int likes; //点赞次数
        private int evaluateStars; //评价星星


        public ArrayList<String> getEvaluate_pictures() {
            return evaluate_pictures;
        }

        public void setEvaluate_pictures(ArrayList<String> evaluate_pictures) {
            this.evaluate_pictures = evaluate_pictures;
        }

        public ArrayList<String> getEvaluatePictures() {
            return evaluatePictures;
        }

        public void setEvaluatePictures(ArrayList<String> evaluatePictures) {
            this.evaluatePictures = evaluatePictures;
        }

        public int getEvaluateStars() {
            return evaluateStars;
        }

        public void setEvaluateStars(int evaluateStars) {
            this.evaluateStars = evaluateStars;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getEvaluateTime() {
            return evaluateTime;
        }

        public void setEvaluateTime(String evaluateTime) {
            this.evaluateTime = evaluateTime;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public String getEvaluateContent() {
            return evaluateContent;
        }

        public void setEvaluateContent(String evaluateContent) {
            this.evaluateContent = evaluateContent;
        }

        public int getBrowseCount() {
            return browseCount;
        }

        public void setBrowseCount(int browseCount) {
            this.browseCount = browseCount;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getEvaluateId() {
            return evaluateId;
        }

        public void setEvaluateId(int evaluateId) {
            this.evaluateId = evaluateId;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }
    }

}
