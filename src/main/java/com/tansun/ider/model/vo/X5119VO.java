package com.tansun.ider.model.vo;

import java.math.BigDecimal;
import java.util.Date;

public class X5119VO {
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
    /** 客户代码，外键关联客户主表主键 [20,0] Not NULL */
    private String customerNo;
    /** 交易识别代码 [4,0] */
    private String transIdentifiNo;
    private BigDecimal billPrincipalBalCard;
    /** 账单本金金额auth [18,0] */
    private BigDecimal billPrincipalBalAuth;
    /** 账单费用金额card [18,0] */
    private BigDecimal billCostBalCard;
    /** 账单费用金额auth [18,0] */
    private BigDecimal billCostBalAuth;
    /** 账单利息金额card [18,0] */
    private BigDecimal billInterestBalCard;
    /** 账单利息金额auth [18,0] */
    private BigDecimal billInterestBalAuth;
    /** 当期本金金额card [18,0] */
    private BigDecimal currPrincipalBalCard;
    /** 当期本金金额auth [18,0] */
    private BigDecimal currPrincipalBalAuth;
    /** 当期费用金额card [18,0] */
    private BigDecimal currCostBalCard;
    /** 当期费用金额auth [18,0] */
    private BigDecimal currCostBalAuth;
    /** 当期利息金额card [18,0] */
    private BigDecimal currInterestBalCard;
    /** 当期利息金额auth [18,0] */
    private BigDecimal currInterestBalAuth;
    /** 创建时间 yyyy-MM-dd HH:mm:ss [23,0] */
    private String gmtCreate;
    /** 时间戳 [19,0] Not NULL */
    private Date timeStamp;
    /** 版本号 [10,0] */
    private Integer version;
    /** 产品描述 [50,0] */
    private String productDesc;
    private String businessDesc;
    private String programDesc;

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

    public String getTransIdentifiNo() {
        return transIdentifiNo;
    }

    public void setTransIdentifiNo(String transIdentifiNo) {
        this.transIdentifiNo = transIdentifiNo;
    }

    public BigDecimal getBillPrincipalBalCard() {
        return billPrincipalBalCard;
    }

    public void setBillPrincipalBalCard(BigDecimal billPrincipalBalCard) {
        this.billPrincipalBalCard = billPrincipalBalCard;
    }

    public BigDecimal getBillPrincipalBalAuth() {
        return billPrincipalBalAuth;
    }

    public void setBillPrincipalBalAuth(BigDecimal billPrincipalBalAuth) {
        this.billPrincipalBalAuth = billPrincipalBalAuth;
    }

    public BigDecimal getBillCostBalCard() {
        return billCostBalCard;
    }

    public void setBillCostBalCard(BigDecimal billCostBalCard) {
        this.billCostBalCard = billCostBalCard;
    }

    public BigDecimal getBillCostBalAuth() {
        return billCostBalAuth;
    }

    public void setBillCostBalAuth(BigDecimal billCostBalAuth) {
        this.billCostBalAuth = billCostBalAuth;
    }

    public BigDecimal getBillInterestBalCard() {
        return billInterestBalCard;
    }

    public void setBillInterestBalCard(BigDecimal billInterestBalCard) {
        this.billInterestBalCard = billInterestBalCard;
    }

    public BigDecimal getBillInterestBalAuth() {
        return billInterestBalAuth;
    }

    public void setBillInterestBalAuth(BigDecimal billInterestBalAuth) {
        this.billInterestBalAuth = billInterestBalAuth;
    }

    public BigDecimal getCurrPrincipalBalCard() {
        return currPrincipalBalCard;
    }

    public void setCurrPrincipalBalCard(BigDecimal currPrincipalBalCard) {
        this.currPrincipalBalCard = currPrincipalBalCard;
    }

    public BigDecimal getCurrPrincipalBalAuth() {
        return currPrincipalBalAuth;
    }

    public void setCurrPrincipalBalAuth(BigDecimal currPrincipalBalAuth) {
        this.currPrincipalBalAuth = currPrincipalBalAuth;
    }

    public BigDecimal getCurrCostBalCard() {
        return currCostBalCard;
    }

    public void setCurrCostBalCard(BigDecimal currCostBalCard) {
        this.currCostBalCard = currCostBalCard;
    }

    public BigDecimal getCurrCostBalAuth() {
        return currCostBalAuth;
    }

    public void setCurrCostBalAuth(BigDecimal currCostBalAuth) {
        this.currCostBalAuth = currCostBalAuth;
    }

    public BigDecimal getCurrInterestBalCard() {
        return currInterestBalCard;
    }

    public void setCurrInterestBalCard(BigDecimal currInterestBalCard) {
        this.currInterestBalCard = currInterestBalCard;
    }

    public BigDecimal getCurrInterestBalAuth() {
        return currInterestBalAuth;
    }

    public void setCurrInterestBalAuth(BigDecimal currInterestBalAuth) {
        this.currInterestBalAuth = currInterestBalAuth;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getBusinessDesc() {
        return businessDesc;
    }

    public void setBusinessDesc(String businessDesc) {
        this.businessDesc = businessDesc;
    }

    public String getProgramDesc() {
        return programDesc;
    }

    public void setProgramDesc(String programDesc) {
        this.programDesc = programDesc;
    }
}
