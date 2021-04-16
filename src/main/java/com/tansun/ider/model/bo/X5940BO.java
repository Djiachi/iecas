package com.tansun.ider.model.bo;

import java.io.Serializable;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * 交易二次识别参数删除
 * 
 * @author lianhuan 2018年9月14日
 */
public class X5940BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;

    public String secondRecogId;

    public String getSecondRecogId() {
        return secondRecogId;
    }

    public void setSecondRecogId(String secondRecogId) {
        this.secondRecogId = secondRecogId;
    }

    @Override
    public String toString() {
        return "X5940BO [secondRecogId=" + secondRecogId + "]";
    }

}
