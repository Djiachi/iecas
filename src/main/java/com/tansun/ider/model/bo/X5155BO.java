/**
 * 
 */
package com.tansun.ider.model.bo;

import java.io.Serializable;

/**
 * @Desc:账户基本信息维护
 * @Author wt
 * @Date 2018年5月8日 上午9:48:24
 */
public class X5155BO implements Serializable {

    private static final long serialVersionUID = 730788112226097058L;
	/** 事件编号 [14,0] Not NULL */
    protected String eventNo;
    /** 活动编号 [8,0] Not NULL */
    protected String activityNo;
	/** 操作员Id */
	private String operatorId;
    /* id */
    private String id;
    /* 账户代码 */
    private String accountId;
    /* 币种 */
    private String currencyCode;
    /* 状态吗 */
    private String statusCode;
    /* 账单频率 */
    private Integer billFrequency;
    /* 核算状态码 */
    private String checkStatusCode;
    /* 约定扣款方式 */
    private String deductWay;
    /* ABS状态 */
    private String absStatus;
    /* 约定扣款银行号 */
    private String agreeDeductBankNo;
    /* 约定扣款账号 */
    private String agreeDeductAccountNo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getBillFrequency() {
        return billFrequency;
    }

    public void setBillFrequency(Integer billFrequency) {
        this.billFrequency = billFrequency;
    }

    public String getCheckStatusCode() {
        return checkStatusCode;
    }

    public void setCheckStatusCode(String checkStatusCode) {
        this.checkStatusCode = checkStatusCode;
    }

    public String getDeductWay() {
        return deductWay;
    }

    public void setDeductWay(String deductWay) {
        this.deductWay = deductWay;
    }

    public String getAbsStatus() {
        return absStatus;
    }

    public void setAbsStatus(String absStatus) {
        this.absStatus = absStatus;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getAgreeDeductBankNo() {
        return agreeDeductBankNo;
    }

    public void setAgreeDeductBankNo(String agreeDeductBankNo) {
        this.agreeDeductBankNo = agreeDeductBankNo;
    }

    public String getAgreeDeductAccountNo() {
        return agreeDeductAccountNo;
    }

    public void setAgreeDeductAccountNo(String agreeDeductAccountNo) {
        this.agreeDeductAccountNo = agreeDeductAccountNo;
    }
	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
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

    @Override
    public String toString() {
        return "X5155BO [id=" + id + ", accountId=" + accountId + ", currencyCode=" + currencyCode + ", statusCode="
                + statusCode + ", billFrequency=" + billFrequency + ", checkStatusCode=" + checkStatusCode
                + ", deductWay=" + deductWay + ", absStatus=" + absStatus + ", agreeDeductBankNo=" + agreeDeductBankNo
                + ", agreeDeductAccountNo=" + agreeDeductAccountNo + "]";
    }

}
