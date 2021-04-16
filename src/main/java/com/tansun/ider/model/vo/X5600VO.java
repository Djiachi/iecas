package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.tansun.ider.framwork.commun.BeanVO;

public class X5600VO  extends BeanVO implements Serializable{

	private static final long serialVersionUID = -2563282886664492663L;
	private  BigDecimal billAmt;
	private  String customerNo;
	private  String currencyCode;
	private String currencyDesc;
	private  BigDecimal installmentAmount;
	private  String balanceObjectCode;
	
	
	
	public BigDecimal getBillAmt() {
		return billAmt;
	}
	public void setBillAmt(BigDecimal billAmt) {
		this.billAmt = billAmt;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getCurrencyDesc() {
		return currencyDesc;
	}
	public void setCurrencyDesc(String currencyDesc) {
		this.currencyDesc = currencyDesc;
	}
	public BigDecimal getInstallmentAmount() {
		return installmentAmount;
	}
	public void setInstallmentAmount(BigDecimal installmentAmount) {
		this.installmentAmount = installmentAmount;
	}
	public String getBalanceObjectCode() {
		return balanceObjectCode;
	}
	public void setBalanceObjectCode(String balanceObjectCode) {
		this.balanceObjectCode = balanceObjectCode;
	}
    
}
