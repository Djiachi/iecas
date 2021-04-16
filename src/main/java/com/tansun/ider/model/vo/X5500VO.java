package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Desc: X5500查询账户余额对象
 * @Author lianhuan
 * @Date 2018年4月25日下午3:07:29
 */
public class X5500VO implements Serializable {
    private static final long serialVersionUID = 6845242395753577120L;
    /** 账户代码 [23,0] Not NULL */
    private String accountId;
    /** 币种 [3,0] Not NULL */
    private String currencyCode;
    /** 余额对象代码 [9,0] Not NULL */
    private String balanceObjectCode;
    /** 余额 [18,0] */
    private BigDecimal balance;
    /** 累计利息 [18,0] */
    private BigDecimal accumulatedInterest;
    /** 当期最低还款 [18,0] */
    private BigDecimal currentDue;
    /** 生效利率 [8,7] */
    private BigDecimal interestRate;
    /** 更新标识 : 1：没有PENDING状态的修改 2：当天未修改 3：下个交易日生效 4：下个账单日生效 5：下个入账日生效 [1,0] */
    private String rateChangeFlag;
    /** pending生效利率 [8,7] */
    private BigDecimal pendingInterestRate;
    /** 溢缴款利率变动标志 1：变动 0：不变动 [1,0] */
    private String overpayRateChangeFlag;
    /** 分期标识 0：不可分期 1：可分期 [1,0] */
    private String installmentFlag;
    /** 余额对象描述 */
    private String balanceObjectDesc;
    /** 币种描述 */
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

    public String getBalanceObjectCode() {
        return balanceObjectCode;
    }

    public void setBalanceObjectCode(String balanceObjectCode) {
        this.balanceObjectCode = balanceObjectCode;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getAccumulatedInterest() {
        return accumulatedInterest;
    }

    public void setAccumulatedInterest(BigDecimal accumulatedInterest) {
        this.accumulatedInterest = accumulatedInterest;
    }

    public BigDecimal getCurrentDue() {
        return currentDue;
    }

    public void setCurrentDue(BigDecimal currentDue) {
        this.currentDue = currentDue;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public String getRateChangeFlag() {
        return rateChangeFlag;
    }

    public void setRateChangeFlag(String rateChangeFlag) {
        this.rateChangeFlag = rateChangeFlag;
    }

    public BigDecimal getPendingInterestRate() {
        return pendingInterestRate;
    }

    public void setPendingInterestRate(BigDecimal pendingInterestRate) {
        this.pendingInterestRate = pendingInterestRate;
    }

    public String getOverpayRateChangeFlag() {
        return overpayRateChangeFlag;
    }

    public void setOverpayRateChangeFlag(String overpayRateChangeFlag) {
        this.overpayRateChangeFlag = overpayRateChangeFlag;
    }

    public String getInstallmentFlag() {
        return installmentFlag;
    }

    public void setInstallmentFlag(String installmentFlag) {
        this.installmentFlag = installmentFlag;
    }

    public String getBalanceObjectDesc() {
        return balanceObjectDesc;
    }

    public void setBalanceObjectDesc(String balanceObjectDesc) {
        this.balanceObjectDesc = balanceObjectDesc;
    }

    @Override
    public String toString() {
        return "X5500VO [accountId=" + accountId + ", currencyCode=" + currencyCode + ", balanceObjectCode="
                + balanceObjectCode + ", balance=" + balance + ", accumulatedInterest=" + accumulatedInterest
                + ", currentDue=" + currentDue + ", interestRate=" + interestRate + ", rateChangeFlag=" + rateChangeFlag
                + ", pendingInterestRate=" + pendingInterestRate + ", overpayRateChangeFlag=" + overpayRateChangeFlag
                + ", installmentFlag=" + installmentFlag + ", balanceObjectDesc=" + balanceObjectDesc + "]";
    }

	public String getCurrencyDesc() {
		return currencyDesc;
	}

	public void setCurrencyDesc(String currencyDesc) {
		this.currencyDesc = currencyDesc;
	}

}
