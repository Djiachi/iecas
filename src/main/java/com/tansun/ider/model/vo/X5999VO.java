package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.tansun.ider.framwork.commun.BeanVO;

public class X5999VO  extends BeanVO implements Serializable{
    
    private static final long serialVersionUID = 6981900035275154412L;
    /** 客户号 [12,0] Not NULL */
    protected String customerNo;
    /** 全局流水号 [36,0] Not NULL */
    protected String globalSerialNumbr;
    /** 账户代码 [23,0] Not NULL */
    protected String accountId;
    /** 币种 [3,0] Not NULL */
    protected String currencyCode;
    /** 余额单元代码 [18,0] Not NULL */
    protected String balanceUnitCode;
    /** 余额类型，P-本金 I-利息 F-费用 [1,0] Not NULL */
    protected String balanceType;
    /** 余额对象代码 [9,0] Not NULL */
    protected String balanceObjectCode;
    /** 周期号 [10,0] Not NULL */
    protected Integer cycleNumber;
    /** 分配顺序 [10,0] Not NULL */
    protected Integer distributionOrder;
    /** 分配金额 [18,0] Not NULL */
    protected BigDecimal distributionAmount;
    protected String balanceObjectDesc;

    public X5999VO() {
      
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getGlobalSerialNumbr() {
        return globalSerialNumbr;
    }

    public void setGlobalSerialNumbr(String globalSerialNumbr) {
        this.globalSerialNumbr = globalSerialNumbr;
    }

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

    public String getBalanceUnitCode() {
        return balanceUnitCode;
    }

    public void setBalanceUnitCode(String balanceUnitCode) {
        this.balanceUnitCode = balanceUnitCode;
    }

    public String getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }

    public String getBalanceObjectCode() {
        return balanceObjectCode;
    }

    public void setBalanceObjectCode(String balanceObjectCode) {
        this.balanceObjectCode = balanceObjectCode;
    }

    public Integer getCycleNumber() {
        return cycleNumber;
    }

    public void setCycleNumber(Integer cycleNumber) {
        this.cycleNumber = cycleNumber;
    }

    public Integer getDistributionOrder() {
        return distributionOrder;
    }

    public void setDistributionOrder(Integer distributionOrder) {
        this.distributionOrder = distributionOrder;
    }

    public BigDecimal getDistributionAmount() {
        return distributionAmount;
    }

    public void setDistributionAmount(BigDecimal distributionAmount) {
        this.distributionAmount = distributionAmount;
    }

    public String getBalanceObjectDesc() {
        return balanceObjectDesc;
    }

    public void setBalanceObjectDesc(String balanceObjectDesc) {
        this.balanceObjectDesc = balanceObjectDesc;
    }

    @Override
    public String toString() {
        return "X5999VO [customerNo=" + customerNo + ", globalSerialNumbr=" + globalSerialNumbr + ", accountId="
                + accountId + ", currencyCode=" + currencyCode + ", balanceUnitCode=" + balanceUnitCode
                + ", balanceType=" + balanceType + ", balanceObjectCode=" + balanceObjectCode + ", cycleNumber="
                + cycleNumber + ", distributionOrder=" + distributionOrder + ", distributionAmount="
                + distributionAmount + ", balanceObjectDesc=" + balanceObjectDesc + "]";
    }

}
