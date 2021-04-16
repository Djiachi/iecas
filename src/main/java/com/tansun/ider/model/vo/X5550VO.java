package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.tansun.ider.framwork.commun.BeanVO;

public class X5550VO extends BeanVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7129121001158567632L;
    /** 账户代码 [23,0] Not NULL */
    protected String accountId;
    /** 币种 [3,0] Not NULL */
    protected String currencyCode;
    /** 当前余额 [18,0] */
    protected BigDecimal currBalance;
    /** 存款利率 [8,7] */
    protected BigDecimal depositInterestRate;
    /** 上一结息金额 [18,0] */
    protected BigDecimal interestBillingAmt;
    /** 冻结余额 [18,0] */
    protected BigDecimal frozenBalance;
    /** 上一结息日期 [10,0] */
    protected String prevInterestBillingDate;
    /** 当前累积积数 [18,0] */
    protected BigDecimal currAccumultBalance;
    /** 未入账利息 [18,0] */
    protected BigDecimal unPostingInterest;
    /** 上一累积日期 [10,0] */
    protected String prevAccumultDate;
    /** 利率生效日期 [8,0] */
    protected String interestRateEffectiveDate;
    /** 币种描述 [5,0] */
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
	public BigDecimal getCurrBalance() {
		return currBalance;
	}
	public void setCurrBalance(BigDecimal currBalance) {
		this.currBalance = currBalance;
	}
	public BigDecimal getDepositInterestRate() {
		return depositInterestRate;
	}
	public void setDepositInterestRate(BigDecimal depositInterestRate) {
		this.depositInterestRate = depositInterestRate;
	}
	public BigDecimal getInterestBillingAmt() {
		return interestBillingAmt;
	}
	public void setInterestBillingAmt(BigDecimal interestBillingAmt) {
		this.interestBillingAmt = interestBillingAmt;
	}
	public BigDecimal getFrozenBalance() {
		return frozenBalance;
	}
	public void setFrozenBalance(BigDecimal frozenBalance) {
		this.frozenBalance = frozenBalance;
	}
	public String getPrevInterestBillingDate() {
		return prevInterestBillingDate;
	}
	public void setPrevInterestBillingDate(String prevInterestBillingDate) {
		this.prevInterestBillingDate = prevInterestBillingDate;
	}
	public BigDecimal getCurrAccumultBalance() {
		return currAccumultBalance;
	}
	public void setCurrAccumultBalance(BigDecimal currAccumultBalance) {
		this.currAccumultBalance = currAccumultBalance;
	}
	public BigDecimal getUnPostingInterest() {
		return unPostingInterest;
	}
	public void setUnPostingInterest(BigDecimal unPostingInterest) {
		this.unPostingInterest = unPostingInterest;
	}
	public String getPrevAccumultDate() {
		return prevAccumultDate;
	}
	public void setPrevAccumultDate(String prevAccumultDate) {
		this.prevAccumultDate = prevAccumultDate;
	}
	public String getInterestRateEffectiveDate() {
		return interestRateEffectiveDate;
	}
	public void setInterestRateEffectiveDate(String interestRateEffectiveDate) {
		this.interestRateEffectiveDate = interestRateEffectiveDate;
	}
	public String getCurrencyDesc() {
		return currencyDesc;
	}
	public void setCurrencyDesc(String currencyDesc) {
		this.currencyDesc = currencyDesc;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
}