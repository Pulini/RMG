package com.yiyun.rmj.bean;

import com.yiyun.rmj.bean.apibase.BaseParm;

import java.util.ArrayList;

public class AddEvaluationParm extends BaseParm {

    private int orderId;
    private ArrayList<ProductEvaluationInfo> evaluation;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public ArrayList<ProductEvaluationInfo> getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(ArrayList<ProductEvaluationInfo> evaluation) {
        this.evaluation = evaluation;
    }
}
