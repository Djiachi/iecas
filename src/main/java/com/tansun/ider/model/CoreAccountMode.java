package com.tansun.ider.model;

import java.math.BigDecimal;

public class CoreAccountMode {
    /** 账户代码 [23,0] Not NULL */
    private String accountId;
    /** 币种 [3,0] Not NULL */
    private String currencyCode;
    /** 业务项目 [9,0] */
    private String businessProgramNo;
    /** 产品对象代码 [15,0] */
    private String productObjectCode;
    /** 所属业务类型 [15,0] */
    private String businessTypeCode;
    // 交易识别
    private String transIdentifiNo;
    private String customerNo;
    // 账单本金金额
    private BigDecimal principalForBill;
    // 账单利息金额
    private BigDecimal interestForBill;
    // 账单费用金额
    private BigDecimal feeForBill;
    // 当期本金金额
    private BigDecimal principalForCurrent;
    // 当期利息金额
    private BigDecimal interestForCurrent;
    // 当期费用金额
    private BigDecimal feeForCurrent;
    // 总金额
    private BigDecimal totalBalance;

    @Override
    public String toString() {
        return "CoreAccountMode{" +
                "accountId='" + accountId + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", businessProgramNo='" + businessProgramNo + '\'' +
                ", productObjectCode='" + productObjectCode + '\'' +
                ", businessTypeCode='" + businessTypeCode + '\'' +
                ", transIdentifiNo='" + transIdentifiNo + '\'' +
                ", customerNo='" + customerNo + '\'' +
                ", principalForBill=" + principalForBill +
                ", interestForBill=" + interestForBill +
                ", feeForBill=" + feeForBill +
                ", principalForCurrent=" + principalForCurrent +
                ", interestForCurrent=" + interestForCurrent +
                ", feeForCurrent=" + feeForCurrent +
                ", totalBalance=" + totalBalance +
                '}';
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

    public String getBusinessProgramNo() {
        return businessProgramNo;
    }

    public void setBusinessProgramNo(String businessProgramNo) {
        this.businessProgramNo = businessProgramNo;
    }

    public String getProductObjectCode() {
        return productObjectCode;
    }

    public void setProductObjectCode(String productObjectCode) {
        this.productObjectCode = productObjectCode;
    }

    public String getBusinessTypeCode() {
        return businessTypeCode;
    }

    public void setBusinessTypeCode(String businessTypeCode) {
        this.businessTypeCode = businessTypeCode;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public BigDecimal getPrincipalForBill() {
        return principalForBill;
    }

    public void setPrincipalForBill(BigDecimal principalForBill) {
        this.principalForBill = principalForBill;
    }

    public BigDecimal getInterestForBill() {
        return interestForBill;
    }

    public void setInterestForBill(BigDecimal interestForBill) {
        this.interestForBill = interestForBill;
    }

    public BigDecimal getFeeForBill() {
        return feeForBill;
    }

    public void setFeeForBill(BigDecimal feeForBill) {
        this.feeForBill = feeForBill;
    }

    public BigDecimal getPrincipalForCurrent() {
        return principalForCurrent;
    }

    public void setPrincipalForCurrent(BigDecimal principalForCurrent) {
        this.principalForCurrent = principalForCurrent;
    }

    public BigDecimal getInterestForCurrent() {
        return interestForCurrent;
    }

    public void setInterestForCurrent(BigDecimal interestForCurrent) {
        this.interestForCurrent = interestForCurrent;
    }

    public BigDecimal getFeeForCurrent() {
        return feeForCurrent;
    }

    public void setFeeForCurrent(BigDecimal feeForCurrent) {
        this.feeForCurrent = feeForCurrent;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getTransIdentifiNo() {
        return transIdentifiNo;
    }

    public void setTransIdentifiNo(String transIdentifiNo) {
        this.transIdentifiNo = transIdentifiNo;
    }
}
