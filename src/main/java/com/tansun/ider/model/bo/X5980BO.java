package com.tansun.ider.model.bo;

import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.framwork.commun.BeanVO;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: PanQi
 * @Date: 2020/04/03
 * @updater:
 * @description: 资产选择+封包
 */
public class X5980BO extends BeanVO implements Serializable {

    /**
     * 全局事件编号
     */
    private String globalEventNo;
    private String credentialNumber;
    private String externalIdentificationNo;
    private String idNumber;
    private String idType;
    /**
     * 事件编号 [14,0] Not NULL
     */
    protected String eventNo;
    /**
     * 活动编号 [8,0] Not NULL
     */
    protected String activityNo;
    /**
     * 客户代码，外键关联客户主表主键 [20,0] Not NULL
     */
    protected String customerNo;
    /**
     * 所属机构号 [10,0] Not NULL
     */
    protected String organNo;
    /**
     * 运营模式 [3,0] Not NULL
     */
    protected String operationMode;
    /**
     * ABS计划号 [6,0] Not NULL
     */
    protected String planId;
    /** 转让方式 */
    protected String type;
    /** 账户类型 R-循环账户 T-交易账户 B-不良资产 [1,0] */
    private String accountType;
    private String globalSerialNumbr;
    protected List<CoreAccount> accountList;

    public String getGlobalEventNo() {
        return globalEventNo;
    }

    public void setGlobalEventNo(String globalEventNo) {
        this.globalEventNo = globalEventNo;
    }

    public String getCredentialNumber() {
        return credentialNumber;
    }

    public void setCredentialNumber(String credentialNumber) {
        this.credentialNumber = credentialNumber;
    }

    public String getExternalIdentificationNo() {
        return externalIdentificationNo;
    }

    public void setExternalIdentificationNo(String externalIdentificationNo) {
        this.externalIdentificationNo = externalIdentificationNo;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getEventNo() {
        return eventNo;
    }

    public void setEventNo(String eventNo) {
        this.eventNo = eventNo;
    }

    public String getActivityNo() {
        return activityNo;
    }

    public void setActivityNo(String activityNo) {
        this.activityNo = activityNo;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getOrganNo() {
        return organNo;
    }

    public void setOrganNo(String organNo) {
        this.organNo = organNo;
    }

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getGlobalSerialNumbr() {
        return globalSerialNumbr;
    }

    public void setGlobalSerialNumbr(String globalSerialNumbr) {
        this.globalSerialNumbr = globalSerialNumbr;
    }

    public List<CoreAccount> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<CoreAccount> accountList) {
        this.accountList = accountList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        return "X5980BO{" +
                "globalEventNo='" + globalEventNo + '\'' +
                ", credentialNumber='" + credentialNumber + '\'' +
                ", externalIdentificationNo='" + externalIdentificationNo + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", idType='" + idType + '\'' +
                ", eventNo='" + eventNo + '\'' +
                ", activityNo='" + activityNo + '\'' +
                ", customerNo='" + customerNo + '\'' +
                ", organNo='" + organNo + '\'' +
                ", operationMode='" + operationMode + '\'' +
                ", planId='" + planId + '\'' +
                ", type='" + type + '\'' +
                ", accountType='" + accountType + '\'' +
                ", globalSerialNumbr='" + globalSerialNumbr + '\'' +
                ", accountList=" + accountList +
                '}';
    }
}
