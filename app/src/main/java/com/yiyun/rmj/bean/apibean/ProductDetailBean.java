package com.yiyun.rmj.bean.apibean;

import java.util.ArrayList;

public class ProductDetailBean {
    private int state;
    private ProductDetailBean.Info info;


    public class Info{
        private Data data;
        private String message;
        private int isNull;

        public int getIsNull() {
            return isNull;
        }

        public void setIsNull(int isNull) {
            this.isNull = isNull;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }


    public class Data{
        private int productId;
        private String salesVolume; //销量
        private double expressMoney;
        private int isCollection;
        private int giveIntegral;
        private int inventory;
        private ArrayList<Classification> classification;
        private String productName;
        private String composition;
        private ArrayList<ProductPicture> productPicture;
        private int secondaryClassificationId;
        private int secondary_classification_id;
        private String productShare;
        private ArrayList<ProductPicture> graphic;
        private double productPrice;

        public String getSalesVolume() {
            return salesVolume;
        }

        public void setSalesVolume(String salesVolume) {
            this.salesVolume = salesVolume;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public String getProductShare() {
            return productShare;
        }

        public void setProductShare(String productShare) {
            this.productShare = productShare;
        }

        public int getSecondary_classification_id() {
            return secondary_classification_id;
        }

        public void setSecondary_classification_id(int secondary_classification_id) {
            this.secondary_classification_id = secondary_classification_id;
        }

        public ArrayList<Classification> getClassification() {
            return classification;
        }

        public void setClassification(ArrayList<Classification> classification) {
            this.classification = classification;
        }

        public String getComposition() {
            return composition;
        }

        public void setComposition(String composition) {
            this.composition = composition;
        }

        public double getExpressMoney() {
            return expressMoney;
        }

        public void setExpressMoney(double expressMoney) {
            this.expressMoney = expressMoney;
        }

        public int getIsCollection() {
            return isCollection;
        }

        public void setIsCollection(int isCollection) {
            this.isCollection = isCollection;
        }

        public ArrayList<ProductPicture> getProductPicture() {
            return productPicture;
        }

        public void setProductPicture(ArrayList<ProductPicture> productPicture) {
            this.productPicture = productPicture;
        }

        public int getSecondaryClassificationId() {
            return secondaryClassificationId;
        }

        public void setSecondaryClassificationId(int secondaryClassificationId) {
            this.secondaryClassificationId = secondaryClassificationId;
        }

        public int getGiveIntegral() {
            return giveIntegral;
        }

        public void setGiveIntegral(int giveIntegral) {
            this.giveIntegral = giveIntegral;
        }

        public int getInventory() {
            return inventory;
        }

        public void setInventory(int inventory) {
            this.inventory = inventory;
        }

        public ArrayList<ProductPicture> getGraphic() {
            return graphic;
        }

        public void setGraphic(ArrayList<ProductPicture> graphic) {
            this.graphic = graphic;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public double getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(double productPrice) {
            this.productPrice = productPrice;
        }
    }

    public class ProductPicture{
        private int type;
        private String url;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

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

    public class Classification{
        private String productModel;
        private int productId;
        private ProductPicture productPicture;
        private int inventory; //库存
        private double productPrice;

        public String getProductModel() {
            return productModel;
        }

        public void setProductModel(String productModel) {
            this.productModel = productModel;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public ProductPicture getProductPicture() {
            return productPicture;
        }

        public void setProductPicture(ProductPicture productPicture) {
            this.productPicture = productPicture;
        }

        public int getInventory() {
            return inventory;
        }

        public void setInventory(int inventory) {
            this.inventory = inventory;
        }

        public double getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(double productPrice) {
            this.productPrice = productPrice;
        }
    }
}
