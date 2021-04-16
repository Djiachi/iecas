package com.tansun.ider.model;

import java.math.BigDecimal;

public class CoreAuthBalanceModel {
    private String accountId;
    /** 币种 [3,0] Not NULL */
    private String currencyCode;
    /** 产品对象 [9,0] */
    private String productObjectCode;
    /** 业务项目代码 [9,0] */
    private String businessProjectCode;
    /** 所属业务类型代码 [9,0] */
    private String businessTypeCode;
    /** 交易识别代码 [4,0] */
    private String transIdentifiNo;
    /** 客户号 [12,0] */
    private String customerNo;
    /** 当期本金金额 [18,0] */
    private BigDecimal currPrincipalBalance;
    /** 账单本金金额 [18,0] */
    private BigDecimal billPrincipalBalance;
    /** 当期利息金额 [18,0] */
    private BigDecimal currInterestBalance;
    /** 账单利息金额 [18,0] */
    private BigDecimal billInterestBalance;
    /** 当期费用金额 [18,0] */
    private BigDecimal currCostBalance;
    /** 账单费用金额 [18,0] */
    private BigDecimal billCostBalance;

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

    public String getProductObjectCode() {
        return productObjectCode;
    }

    public void setProductObjectCode(String productObjectCode) {
        this.productObjectCode = productObjectCode;
    }

    public String getBusinessProjectCode() {
        return businessProjectCode;
    }

    public void setBusinessProjectCode(String businessProjectCode) {
        this.businessProjectCode = businessProjectCode;
    }

    public String getBusinessTypeCode() {
        return businessTypeCode;
    }

    public void setBusinessTypeCode(String businessTypeCode) {
        this.businessTypeCode = businessTypeCode;
    }

    public String getTransIdentifiNo() {
        return transIdentifiNo;
    }

    public void setTransIdentifiNo(String transIdentifiNo) {
        this.transIdentifiNo = transIdentifiNo;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public BigDecimal getBillCostBalance() {
        return billCostBalance;
    }

    public void setBillCostBalance(BigDecimal billCostBalance) {
        this.billCostBalance = billCostBalance;
    }

    public BigDecimal getCurrPrincipalBalance() {
        return currPrincipalBalance;
    }

    public void setCurrPrincipalBalance(BigDecimal currPrincipalBalance) {
        this.currPrincipalBalance = currPrincipalBalance;
    }

    public BigDecimal getBillPrincipalBalance() {
        return billPrincipalBalance;
    }

    public void setBillPrincipalBalance(BigDecimal billPrincipalBalance) {
        this.billPrincipalBalance = billPrincipalBalance;
    }

    public BigDecimal getCurrInterestBalance() {
        return currInterestBalance;
    }

    public void setCurrInterestBalance(BigDecimal currInterestBalance) {
        this.currInterestBalance = currInterestBalance;
    }

    public BigDecimal getBillInterestBalance() {
        return billInterestBalance;
    }

    public void setBillInterestBalance(BigDecimal billInterestBalance) {
        this.billInterestBalance = billInterestBalance;
    }

    public BigDecimal getCurrCostBalance() {
        return currCostBalance;
    }

    public void setCurrCostBalance(BigDecimal currCostBalance) {
        this.currCostBalance = currCostBalance;
    }
}
