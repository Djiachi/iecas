package com.tansun.ider.model.bo;

import java.io.Serializable;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc:查询余额单元
 * @Author lianhuan
 * @Date 2018年4月28日 下午2:43:42
 */
public class X5505BO extends BeanVO implements Serializable {
    private static final long serialVersionUID = -4256912206267593510L;
    private String accountId;
    private String currencyCode;
    private String operationMode;

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

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    @Override
    public String toString() {
        return "X5505BO [accountId=" + accountId + ", currencyCode=" + currencyCode + ", operationMode=" + operationMode
                + "]";
    }

}
