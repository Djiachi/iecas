package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Desc: X5505查询余额单元
 * @Author lianhuan
 * @Date 2018年4月25日下午3:07:29
 */
public class X5505VO implements Serializable {
    private static final long serialVersionUID = 5186654812037894406L;
    /** 余额单元代码 [18,0] Not NULL */
    private String balanceUnitCode;
    /** 余额类型，P-本金 I-利息 F-费用 [1,0] */
    private String balanceType;
    /** 建立周期号 [10,0] */
    private Integer cycleNumber;
    /** 期次 [10,0] */
    private Integer period;
    /** 账户代码 [23,0] Not NULL */
    private String accountId;
    /** 币种 [3,0] Not NULL */
    private String currencyCode;
    /** 余额 [18,0] */
    private BigDecimal balance;
    /** 计息年利率 [8,7] */
    private BigDecimal annualInterestRate;
    /** 累计利息 [19,0] */
    private BigDecimal accumulatedInterest;
    /** 当期最低还款 [18,0] */
    private BigDecimal currentMinPayment;
    /** 余额对象 [9,0] */
    private String balanceObjectCode;
    /** 下一结息日期 yyyy-MM-dd [10,0] */
    private String nextInterestBillingDate;
    /** 上一结息处理日 yyyy-MM-dd [10,0] */
    private String prevInterestBillingDate;
    /** 资产属性 [2,0] */
    private String assetProperties;
    /** 余额建立日期 yyyy-MM-dd [10,0] */
    private String createDate;
    /** 余额结束日期 yyyy-MM-dd [10,0] */
    private String endDate;
    /** 上一计息年利率 [8,7] */
    private BigDecimal lastInterestRate;
    /** 上一年利率失效日期 [10,0] */
    private String lastInterestRateExpirDate;
    /** 首次结息周期号 [10,0] */
    private Integer firstBillingCycle;
    /** 当期计算比例 [8,7] */
    private BigDecimal currCalculationPercentage;
    /** 余额对象描述 */
    private String balanceObjectDesc;
    /** 币种描述 */
    private String currencyDesc;
    /** 核算状态码 [3,0] */
    protected String accountingStatusCode;
    /** 核算状态描述 */
    protected String accountingStatusCodeDesc;

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
    public String getBalanceObjectDesc() {
        return balanceObjectDesc;
    }
    public void setBalanceObjectDesc(String balanceObjectDesc) {
        this.balanceObjectDesc = balanceObjectDesc;
    }

    public String getAccountingStatusCode() {
        return accountingStatusCode;
    }

    public void setAccountingStatusCode(String accountingStatusCode) {
        this.accountingStatusCode = accountingStatusCode;
    }
    public String getCurrencyDesc() {
        return currencyDesc;
    }
    public void setCurrencyDesc(String currencyDesc) {
        this.currencyDesc = currencyDesc;
    }

    public String getAccountingStatusCodeDesc() {
        return accountingStatusCodeDesc;
    }

    public void setAccountingStatusCodeDesc(String accountingStatusCodeDesc) {
        this.accountingStatusCodeDesc = accountingStatusCodeDesc;
    }

    @Override
    public String toString() {
        return "X5505VO{" +
                "balanceUnitCode='" + balanceUnitCode + '\'' +
                ", balanceType='" + balanceType + '\'' +
                ", cycleNumber=" + cycleNumber +
                ", period=" + period +
                ", accountId='" + accountId + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", balance=" + balance +
                ", annualInterestRate=" + annualInterestRate +
                ", accumulatedInterest=" + accumulatedInterest +
                ", currentMinPayment=" + currentMinPayment +
                ", balanceObjectCode='" + balanceObjectCode + '\'' +
                ", nextInterestBillingDate='" + nextInterestBillingDate + '\'' +
                ", prevInterestBillingDate='" + prevInterestBillingDate + '\'' +
                ", assetProperties='" + assetProperties + '\'' +
                ", createDate='" + createDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", lastInterestRate=" + lastInterestRate +
                ", lastInterestRateExpirDate='" + lastInterestRateExpirDate + '\'' +
                ", firstBillingCycle=" + firstBillingCycle +
                ", currCalculationPercentage=" + currCalculationPercentage +
                ", balanceObjectDesc='" + balanceObjectDesc + '\'' +
                ", currencyDesc='" + currencyDesc + '\'' +
                ", accountingStatusCode='" + accountingStatusCode + '\'' +
                ", accountingStatusCodeDesc='" + accountingStatusCodeDesc + '\'' +
                '}';
    }
}
