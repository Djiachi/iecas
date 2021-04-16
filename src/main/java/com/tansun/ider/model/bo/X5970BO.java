package com.tansun.ider.model.bo;

import java.io.Serializable;

import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.commun.BeanVO;

public class X5970BO extends BeanVO implements Serializable{
    
    private static final long serialVersionUID = 3101293273540612590L;
    private String balanceUnitCode;
    private String globalTransSerialNo;
    private CoreEventActivityRel coreEventActivityRel;
    
    public String getBalanceUnitCode() {
        return balanceUnitCode;
    }
    public void setBalanceUnitCode(String balanceUnitCode) {
        this.balanceUnitCode = balanceUnitCode;
    }
    
    public String getGlobalTransSerialNo() {
        return globalTransSerialNo;
    }
    public void setGlobalTransSerialNo(String globalTransSerialNo) {
        this.globalTransSerialNo = globalTransSerialNo;
    }
    public CoreEventActivityRel getCoreEventActivityRel() {
        return coreEventActivityRel;
    }
    public void setCoreEventActivityRel(CoreEventActivityRel coreEventActivityRel) {
        this.coreEventActivityRel = coreEventActivityRel;
    }
}
