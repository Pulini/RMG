package com.yiyun.rmj.bean.apibean;

import java.util.ArrayList;

public class ProductDetailEvaluationBean {
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
        private ArrayList<ProductEvaluation> data;

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

        public ArrayList<ProductEvaluation> getData() {
            return data;
        }

        public void setData(ArrayList<ProductEvaluation> data) {
            this.data = data;
        }
    }

    public class ProductEvaluation{

        private int evaluate_user_id;
        private int delete_state;
        private int evaluate_id;
        private String evaluate_content;
        private int product_id;
        private int browse_count;
        private int order_id;
        private String evaluate_time;
        private int evaluate_stars;
        private int likes;
        private String head;
        private String username;
        private ArrayList<String> evaluate_pictures;

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public ArrayList<String> getEvaluate_pictures() {
            return evaluate_pictures;
        }

        public void setEvaluate_pictures(ArrayList<String> evaluate_poctures) {
            this.evaluate_pictures = evaluate_poctures;
        }

        public int getEvaluate_user_id() {
            return evaluate_user_id;
        }

        public void setEvaluate_user_id(int evaluate_user_id) {
            this.evaluate_user_id = evaluate_user_id;
        }

        public int getDelete_state() {
            return delete_state;
        }

        public void setDelete_state(int delete_state) {
            this.delete_state = delete_state;
        }

        public int getEvaluate_id() {
            return evaluate_id;
        }

        public void setEvaluate_id(int evaluate_id) {
            this.evaluate_id = evaluate_id;
        }

        public String getEvaluate_content() {
            return evaluate_content;
        }

        public void setEvaluate_content(String evaluate_content) {
            this.evaluate_content = evaluate_content;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public int getBrowse_count() {
            return browse_count;
        }

        public void setBrowse_count(int browse_count) {
            this.browse_count = browse_count;
        }

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }

        public String getEvaluate_time() {
            return evaluate_time;
        }

        public void setEvaluate_time(String evaluate_time) {
            this.evaluate_time = evaluate_time;
        }

        public int getEvaluate_stars() {
            return evaluate_stars;
        }

        public void setEvaluate_stars(int evaluate_stars) {
            this.evaluate_stars = evaluate_stars;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
