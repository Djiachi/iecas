package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.tansun.ider.framwork.commun.BeanVO;

public class X5703BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = -6294800859169407752L;

    /** 会计流水号 [64,0] Not NULL */
    private String acctSerialNbr;
    /** 序号 [2,0] Not NULL */
    private String seqNbr;
    /** 事件编号 [32,0] Not NULL */
    private String eventNo;
    /** 核算场景 [8,0] Not NULL */
    private String accountScene;
    /** 交易项目 [64,0] */
    private String projectId;
    /** 客户编码 [64,0] */
    private String customerCde;
    /** 账号 [64,0] */
    private String accountId;
    /** 会计科目代号 [16,0] */
    private String itemId;
    /** 借贷标识 借记：D 贷记：C [1,0] */
    private String debitCreditFlag;
    /** 币种 [3,0] Not NULL */
    private String currencyCde;
    /** 金额 [18,0] */
    private BigDecimal amount;
    /** 币种对应小数位 [8,0] */
    private Integer currencyPoint;
    /** 财务日期 [32,0] */
    private String financialDate;
    /** 摘要 [128,0] */
    private String summary;
    /** 操作时间 [32,0] */
    private String operationTime;
    /** 分录描述 [128,0] */
    private String acctDesc;
    /** 交易流水号 [64,0] */
    private String transSerialNbr;
    /** GMT_CREATE [23,0] */
    private String gmtCreate;
    /** TIMESTAMP [11,6] Not NULL */
    private Date timestamp;
    /** VERSION [10,0] Not NULL */
    private Integer version;

    private String startDate;
    private String endDate;

    public String getAcctSerialNbr() {
        return acctSerialNbr;
    }

    public void setAcctSerialNbr(String acctSerialNbr) {
        this.acctSerialNbr = acctSerialNbr;
    }

    public String getSeqNbr() {
        return seqNbr;
    }

    public void setSeqNbr(String seqNbr) {
        this.seqNbr = seqNbr;
    }

    public String getEventNo() {
        return eventNo;
    }

    public void setEventNo(String eventNo) {
        this.eventNo = eventNo;
    }

    public String getAccountScene() {
        return accountScene;
    }

    public void setAccountScene(String accountScene) {
        this.accountScene = accountScene;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCustomerCde() {
        return customerCde;
    }

    public void setCustomerCde(String customerCde) {
        this.customerCde = customerCde;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDebitCreditFlag() {
        return debitCreditFlag;
    }

    public void setDebitCreditFlag(String debitCreditFlag) {
        this.debitCreditFlag = debitCreditFlag;
    }

    public String getCurrencyCde() {
        return currencyCde;
    }

    public void setCurrencyCde(String currencyCde) {
        this.currencyCde = currencyCde;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getCurrencyPoint() {
        return currencyPoint;
    }

    public void setCurrencyPoint(Integer currencyPoint) {
        this.currencyPoint = currencyPoint;
    }

    public String getFinancialDate() {
        return financialDate;
    }

    public void setFinancialDate(String financialDate) {
        this.financialDate = financialDate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    public String getAcctDesc() {
        return acctDesc;
    }

    public void setAcctDesc(String acctDesc) {
        this.acctDesc = acctDesc;
    }

    public String getTransSerialNbr() {
        return transSerialNbr;
    }

    public void setTransSerialNbr(String transSerialNbr) {
        this.transSerialNbr = transSerialNbr;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "X5703BO [acctSerialNbr=" + acctSerialNbr + ", seqNbr=" + seqNbr + ", eventNo=" + eventNo
                + ", accountScene=" + accountScene + ", projectId=" + projectId + ", customerCde=" + customerCde
                + ", accountId=" + accountId + ", itemId=" + itemId + ", debitCreditFlag=" + debitCreditFlag
                + ", currencyCde=" + currencyCde + ", amount=" + amount + ", currencyPoint=" + currencyPoint
                + ", financialDate=" + financialDate + ", summary=" + summary + ", operationTime=" + operationTime
                + ", acctDesc=" + acctDesc + ", transSerialNbr=" + transSerialNbr + ", gmtCreate=" + gmtCreate
                + ", timestamp=" + timestamp + ", version=" + version + ", startDate=" + startDate + ", endDate="
                + endDate + "]";
    }

}
