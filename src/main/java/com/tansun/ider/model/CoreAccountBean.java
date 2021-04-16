package com.tansun.ider.model;

import java.math.BigDecimal;

public class CoreAccountBean {

	private String accountId;
	private String currencyCode;
	private String interestStartDate;
	private BigDecimal balanceTotal;
	private String currencyDesc;
	
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
	public String getInterestStartDate() {
		return interestStartDate;
	}
	public void setInterestStartDate(String interestStartDate) {
		this.interestStartDate = interestStartDate;
	}
	public BigDecimal getBalanceTotal() {
		return balanceTotal;
	}
	public void setBalanceTotal(BigDecimal balanceTotal) {
		this.balanceTotal = balanceTotal;
	}
	@Override
	public String toString() {
		return "CoreAccountBean [accountId=" + accountId + ", currencyCode=" + currencyCode + ", interestStartDate="
				+ interestStartDate + ", balanceTotal=" + balanceTotal + "]";
	}
	public String getCurrencyDesc() {
		return currencyDesc;
	}
	public void setCurrencyDesc(String currencyDesc) {
		this.currencyDesc = currencyDesc;
	}
	
}
