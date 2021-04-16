package com.tansun.ider.model.bo;

import java.io.Serializable;
/**
 * 报销标识
 * @author sunyaoyao
 *
 */
public class X4002BO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1365867373107695393L;
	/** 客户号 */
	private String customerNo;
	 /** 客户号/账户号/余额单元号/余额对象号 账户定位活动赋值账户号，余额定位时赋值余额单元号 [23,0] Not NULL */
	private String entityKey;
    /** 币种 [3,0] Not NULL */
	private String currencyCode;
    /** 全局流水号 [36,0] Not NULL */
	private String globalSerialNumbr;
    /** 发生时间 HH:mm:ss  [12,0] Not NULL */
	private String occurrTime;
    /** 日志层级 [1,0] */
	private String logLevel;
    /** 余额类型 [1,0] */
	private String balanceType;
	/**报销状态 [1,0]*/
	private String reimbursementStatus;
    /** 借贷标志 [1,0] */
    protected String loanSign;
    
	public String getLoanSign() {
		return loanSign;
	}
	public void setLoanSign(String loanSign) {
		this.loanSign = loanSign;
	}
	public String getReimbursementStatus() {
		return reimbursementStatus;
	}
	public void setReimbursementStatus(String reimbursementStatus) {
		this.reimbursementStatus = reimbursementStatus;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getEntityKey() {
		return entityKey;
	}
	public void setEntityKey(String entityKey) {
		this.entityKey = entityKey;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getGlobalSerialNumbr() {
		return globalSerialNumbr;
	}
	public void setGlobalSerialNumbr(String globalSerialNumbr) {
		this.globalSerialNumbr = globalSerialNumbr;
	}
	public String getOccurrTime() {
		return occurrTime;
	}
	public void setOccurrTime(String occurrTime) {
		this.occurrTime = occurrTime;
	}
	public String getLogLevel() {
		return logLevel;
	}
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}
	public String getBalanceType() {
		return balanceType;
	}
	public void setBalanceType(String balanceType) {
		this.balanceType = balanceType;
	}
	

}
