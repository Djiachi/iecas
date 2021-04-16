package com.tansun.ider.model.bo;

import java.io.Serializable;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.commun.BeanVO;

/**
 * 统一利率
 * @author qianyp
 */

public class X5268BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 3582082822560763048L;
    
    private CoreEventActivityRel coreEventActivityRel;
    private String accountId;
    private String currencyCode;
    
    public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    public String getCurrencyCode() {
        return currencyCode;
    }
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    public CoreEventActivityRel getCoreEventActivityRel() {
        return coreEventActivityRel;
    }
    public void setCoreEventActivityRel(CoreEventActivityRel coreEventActivityRel) {
        this.coreEventActivityRel = coreEventActivityRel;
    }
    @Override
    public String toString() {
        return "X5268BO [coreEventActivityRel=" + coreEventActivityRel + ", accountId=" + accountId + ", currencyCode="
                + currencyCode + "]";
    }
}
