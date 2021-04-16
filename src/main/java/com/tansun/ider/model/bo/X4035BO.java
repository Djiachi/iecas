package com.tansun.ider.model.bo;

import java.math.BigDecimal;

/**
 * <p> Title: X4030BO </p>
 * <p> Description: 公务卡额度调整</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年4月25日
 */
public class X4035BO {

    // 预算编号
	private String limitExpireDate;
    private String operationMode;
    private String externalIdentificationNo;
    private String customerNo;
    private String creditType;
    private String currencyCode;
    private String creditNodeNo;
    private BigDecimal creditLimit;
    private String operatorId;
    private String limitEffectvDate;
    /** 调额类型  1 - 永额调升  2 - 永额调降   3 - 临额调升  4 - 临额取消  5-临额调降 */
    private String adjustType;
    private String corporation;
    
    public String getCorporation() {
		return corporation;
	}
	public void setCorporation(String corporation) {
		this.corporation = corporation;
	}
	public String getAdjustType() {
		return adjustType;
	}
	public void setAdjustType(String adjustType) {
		this.adjustType = adjustType;
	}
	/** 预算单位编号 [30,0] Not NULL */
    private String budgetOrgCode;
    
	public String getLimitExpireDate() {
		return limitExpireDate;
	}
	public void setLimitExpireDate(String limitExpireDate) {
		this.limitExpireDate = limitExpireDate;
	}
	public String getOperationMode() {
		return operationMode;
	}
	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}
	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}
	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getCreditType() {
		return creditType;
	}
	public void setCreditType(String creditType) {
		this.creditType = creditType;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getCreditNodeNo() {
		return creditNodeNo;
	}
	public void setCreditNodeNo(String creditNodeNo) {
		this.creditNodeNo = creditNodeNo;
	}
	public BigDecimal getCreditLimit() {
		return creditLimit;
	}
	public void setCreditLimit(BigDecimal creditLimit) {
		this.creditLimit = creditLimit;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getLimitEffectvDate() {
		return limitEffectvDate;
	}
	public void setLimitEffectvDate(String limitEffectvDate) {
		this.limitEffectvDate = limitEffectvDate;
	}
	public String getBudgetOrgCode() {
		return budgetOrgCode;
	}
	public void setBudgetOrgCode(String budgetOrgCode) {
		this.budgetOrgCode = budgetOrgCode;
	}

}
