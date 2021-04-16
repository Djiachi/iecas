package com.tansun.ider.model;

import java.math.BigDecimal;
import java.util.Date;

public class CoreBalanceBean {

	 /** 余额单元代码 [18,0] Not NULL */
    protected String balanceUnitCode;
    /** 余额类型，P-本金 I-利息 F-费用 [1,0] */
    protected String balanceType;
    /** 建立周期号 [10,0] */
    protected Integer cycleNumber;
    /** 期次 [10,0] */
    protected Integer period;
    /** 账户代码 [23,0] Not NULL */
    protected String accountId;
    /** 币种 [3,0] Not NULL */
    protected String currencyCode;
    /** 余额 [18,0] */
    protected BigDecimal balance;
    /** 计息年利率 [8,7] */
    protected BigDecimal annualInterestRate;
    /** 累计利息 [19,0] */
    protected BigDecimal accumulatedInterest;
    /** 当期最低还款 [18,0] */
    protected BigDecimal currentMinPayment;
    /** 余额对象 [9,0] */
    protected String balanceObjectCode;
    /** 下一结息日期 yyyy-MM-dd [10,0] */
    protected String nextInterestBillingDate;
    /** 上一结息处理日 yyyy-MM-dd [10,0] */
    protected String prevInterestBillingDate;
    /** 资产属性 [2,0] */
    protected String assetProperties;
    /** 余额建立日期 yyyy-MM-dd [10,0] */
    protected String createDate;
    /** 余额结束日期 yyyy-MM-dd [10,0] */
    protected String endDate;
    /** 上一计息年利率 [8,7] */
    protected BigDecimal lastInterestRate;
    /** 上一年利率失效日期 [10,0] */
    protected String lastInterestRateExpirDate;
    /** 首次结息周期号 [10,0] */
    protected Integer firstBillingCycle;
    /** 当期计算比例 [8,7] */
    protected BigDecimal currCalculationPercentage;
    /** 创建时间 yyyy-MM-dd HH:mm:ss [23,0] */
    protected String gmtCreate;
    /** 时间戳 : oralce使用触发器更新， mysql使用自动更新 [19,0] Not NULL */
    protected Date timestamp;
    /** 版本号 [10,0] Not NULL */
    protected Integer version;
    /** 余额 [18,0] */
    protected BigDecimal balanceTotal;
    // 利息金额
    private BigDecimal interest;
    // 余额对象代码描述
    private String objectDesc;
    //币种描述
    private String currencyDesc;
    
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public BigDecimal getBalanceTotal() {
		return balanceTotal;
	}
	public void setBalanceTotal(BigDecimal balanceTotal) {
		this.balanceTotal = balanceTotal;
	}
	public String getBalanceUnitCode() {
		return balanceUnitCode;
	}
	public void setBalanceUnitCode(String balanceUnitCode) {
		this.balanceUnitCode = balanceUnitCode;
	}
	public String getBalanceType() {
		return balanceType;
	}
	public void setBalanceType(String balanceType) {
		this.balanceType = balanceType;
	}
	public Integer getCycleNumber() {
		return cycleNumber;
	}
	public void setCycleNumber(Integer cycleNumber) {
		this.cycleNumber = cycleNumber;
	}
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
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
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getAnnualInterestRate() {
		return annualInterestRate;
	}
	public void setAnnualInterestRate(BigDecimal annualInterestRate) {
		this.annualInterestRate = annualInterestRate;
	}
	public BigDecimal getAccumulatedInterest() {
		return accumulatedInterest;
	}
	public void setAccumulatedInterest(BigDecimal accumulatedInterest) {
		this.accumulatedInterest = accumulatedInterest;
	}
	public BigDecimal getCurrentMinPayment() {
		return currentMinPayment;
	}
	public void setCurrentMinPayment(BigDecimal currentMinPayment) {
		this.currentMinPayment = currentMinPayment;
	}
	public String getBalanceObjectCode() {
		return balanceObjectCode;
	}
	public void setBalanceObjectCode(String balanceObjectCode) {
		this.balanceObjectCode = balanceObjectCode;
	}
	public String getNextInterestBillingDate() {
		return nextInterestBillingDate;
	}
	public void setNextInterestBillingDate(String nextInterestBillingDate) {
		this.nextInterestBillingDate = nextInterestBillingDate;
	}
	public String getPrevInterestBillingDate() {
		return prevInterestBillingDate;
	}
	public void setPrevInterestBillingDate(String prevInterestBillingDate) {
		this.prevInterestBillingDate = prevInterestBillingDate;
	}
	public String getAssetProperties() {
		return assetProperties;
	}
	public void setAssetProperties(String assetProperties) {
		this.assetProperties = assetProperties;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public BigDecimal getLastInterestRate() {
		return lastInterestRate;
	}
	public void setLastInterestRate(BigDecimal lastInterestRate) {
		this.lastInterestRate = lastInterestRate;
	}
	public String getLastInterestRateExpirDate() {
		return lastInterestRateExpirDate;
	}
	public void setLastInterestRateExpirDate(String lastInterestRateExpirDate) {
		this.lastInterestRateExpirDate = lastInterestRateExpirDate;
	}
	public Integer getFirstBillingCycle() {
		return firstBillingCycle;
	}
	public void setFirstBillingCycle(Integer firstBillingCycle) {
		this.firstBillingCycle = firstBillingCycle;
	}
	public BigDecimal getCurrCalculationPercentage() {
		return currCalculationPercentage;
	}
	public void setCurrCalculationPercentage(BigDecimal currCalculationPercentage) {
		this.currCalculationPercentage = currCalculationPercentage;
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
	public String getObjectDesc() {
		return objectDesc;
	}
	public void setObjectDesc(String objectDesc) {
		this.objectDesc = objectDesc;
	}
	public String getCurrencyDesc() {
		return currencyDesc;
	}
	public void setCurrencyDesc(String currencyDesc) {
		this.currencyDesc = currencyDesc;
	}
    
}
