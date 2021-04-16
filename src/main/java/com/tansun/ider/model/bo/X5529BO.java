package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: X5529账户余额对象
 * @Author lianhuan
 * @Date 2018年4月25日下午3:04:31
 */
public class X5529BO extends BeanVO implements Serializable {

	private static final long serialVersionUID = 5035068883811420241L;
	/** 账户代码 : 账户代码 [32,0] Not NULL */
    private String accountId;
    /** 币种 : 币种 [3,0] Not NULL */
    private String currencyCode;
    /** 余额对象代码 : 余额对象代码 [9,0] Not NULL */
    private String balanceObjectCode;
    /** 余额 : 余额 [18,0] */
    private BigDecimal balance;
    /** 累计利息 : 累计利息 [18,0] */
    private BigDecimal accumulatedInterest;
    /** 当期最低还款 : 当期最低还款 [18,0] */
    private BigDecimal currentMinRepayment;
    /** 生效利率 : 生效利率 [8,7] */
    private BigDecimal interestRate;
    /** 更新标识 : 1：没有PENDING状态的修改 2：当天未修改 3：下个交易日生效 4：下个账单日生效 5：下个入账日生效 [1,0] */
    private String updateFlag;
    /** 创建时间 yyyy-MM-dd HH:mm:ss [23,0] */
    private String gmtCreate;
    /** 时间戳 : oralce使用触发器更新， mysql使用自动更新 [19,0] Not NULL */
    private Date timestamp;
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
	public BigDecimal getCurrentMinRepayment() {
		return currentMinRepayment;
	}
	public void setCurrentMinRepayment(BigDecimal currentMinRepayment) {
		this.currentMinRepayment = currentMinRepayment;
	}
	public BigDecimal getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
	public String getUpdateFlag() {
		return updateFlag;
	}
	public void setUpdateFlag(String updateFlag) {
		this.updateFlag = updateFlag;
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
	@Override
	public String toString() {
		return "X55289O [accountId=" + accountId + ", currencyCode=" + currencyCode + ", balanceObjectCode="
				+ balanceObjectCode + ", balance=" + balance + ", accumulatedInterest=" + accumulatedInterest
				+ ", currentMinRepayment=" + currentMinRepayment + ", interestRate=" + interestRate + ", updateFlag="
				+ updateFlag + ", gmtCreate=" + gmtCreate + ", timestamp=" + timestamp + "]";
	}
    

}
