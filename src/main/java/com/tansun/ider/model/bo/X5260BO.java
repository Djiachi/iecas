package com.tansun.ider.model.bo;

import java.math.BigDecimal;

public class X5260BO {

    private String accountId;
    private String currencyCode;
    private BigDecimal interestRate;
    
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
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    @Override
    public String toString() {
        return "X5260BO [accountId=" + accountId + ", currencyCode=" + currencyCode + ", interestRate=" + interestRate
                + "]";
    }
}
