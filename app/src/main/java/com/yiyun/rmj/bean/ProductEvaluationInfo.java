package com.yiyun.rmj.bean;

import java.util.ArrayList;

public class ProductEvaluationInfo {

    private int productId;
    private int stars;
    private String contet;
    private String pictures;
    private ArrayList<String> picturesArray;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getContent() {
        return contet;
    }

    public void setContent(String content) {
        this.contet = content;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public ArrayList<String> getPicturesArray() {
        return picturesArray;
    }

    public void setPicturesArray(ArrayList<String> picturesArray) {
        this.picturesArray = picturesArray;
    }

    public void picturesArrayToStr(){
        StringBuffer strBuffer = new StringBuffer();
        for(int i=0; i< picturesArray.size();i++){
            if(i != 0){
                strBuffer.append(",");
            }
            strBuffer.append(picturesArray.get(i));
        }
        pictures = strBuffer.toString();
    }

}
