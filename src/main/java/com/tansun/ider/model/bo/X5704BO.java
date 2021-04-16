package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.Date;

import com.tansun.ider.framwork.commun.BeanVO;

public class X5704BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = -6294800859169407752L;

    /** 核算场景代码 [8,0] Not NULL */
    private String accountingScene;
    /** 个人/公司标识 P：个人 C：公司 [1,0] */
    private String personalCompanyType;
    /** 交易状态（NN:正常；NR：拒绝；RN：拒绝重入） [2,0] */
    private String transStatus;
    private String transStatusDesc;
    /** 核算状态（0：正常；1：非应计；2：核销） [1,0] */
    private String acctStatus;
    private String acctStatusDesc;
    /** 资产属性（0：正常；1：证券化） [1,0] */
    private String assetAttributes;
    /** 还款类型（0：正常还款；1：购汇还款；2：约定还款） [1,0] */
    private String paymentType;
    private String paymentTypeDesc;
    /** 0：入账币种清算；1：非入账币种清算 [1,0] */
    private String clearingSign;
    /** GMT_CREATE [23,0] */
    private String gmtCreate;
    /** TIMESTAMP [11,6] Not NULL */
    private Date timestamp;
    /** VERSION [10,0] Not NULL */
    private Integer version;

    public String getAccountingScene() {
        return accountingScene;
    }

    public void setAccountingScene(String accountingScene) {
        this.accountingScene = accountingScene;
    }

    public String getPersonalCompanyType() {
        return personalCompanyType;
    }

    public void setPersonalCompanyType(String personalCompanyType) {
        this.personalCompanyType = personalCompanyType;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }

    public String getAcctStatus() {
        return acctStatus;
    }

    public void setAcctStatus(String acctStatus) {
        this.acctStatus = acctStatus;
    }

    public String getAssetAttributes() {
        return assetAttributes;
    }

    public void setAssetAttributes(String assetAttributes) {
        this.assetAttributes = assetAttributes;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getClearingSign() {
        return clearingSign;
    }

    public void setClearingSign(String clearingSign) {
        this.clearingSign = clearingSign;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getTransStatusDesc() {
        return transStatusDesc;
    }

    public void setTransStatusDesc(String transStatusDesc) {
        this.transStatusDesc = transStatusDesc;
    }

    public String getAcctStatusDesc() {
        return acctStatusDesc;
    }

    public void setAcctStatusDesc(String acctStatusDesc) {
        this.acctStatusDesc = acctStatusDesc;
    }

    public String getPaymentTypeDesc() {
        return paymentTypeDesc;
    }

    public void setPaymentTypeDesc(String paymentTypeDesc) {
        this.paymentTypeDesc = paymentTypeDesc;
    }

    @Override
    public String toString() {
        return "X5704BO [accountingScene=" + accountingScene + ", personalCompanyType=" + personalCompanyType
                + ", transStatus=" + transStatus + ", transStatusDesc=" + transStatusDesc + ", acctStatus=" + acctStatus
                + ", acctStatusDesc=" + acctStatusDesc + ", assetAttributes=" + assetAttributes + ", paymentType="
                + paymentType + ", paymentTypeDesc=" + paymentTypeDesc + ", clearingSign=" + clearingSign
                + ", gmtCreate=" + gmtCreate + ", timestamp=" + timestamp + ", version=" + version + "]";
    }

}
