package com.yiyun.rmj.bean.apiparm;

import com.yiyun.rmj.bean.apibase.BaseParm;

public class ProductDetailParm extends BaseParm {
    private int productId; //产品ID

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
