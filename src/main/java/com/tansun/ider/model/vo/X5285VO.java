package com.tansun.ider.model.vo;

import java.math.BigDecimal;

/**
 * 
* @类名称 		X5285VO 
* @类描述 		<pre>实时余额查询</pre>
* @作者 			chenyinliao@tansun.com.cn 
* @创建时间 		2018年5月30日 
* @版本 			v1.01 
* @修改记录 
* <pre>
* 版本 		修改人 					修改时间 				修改内容描述 
* ---------------------------------------------- 
* 1.00 		chenyinliao 			20180530 			新建 
* ---------------------------------------------- 
* </pre>
 */
public class X5285VO {
	
	/** 账户代码 */
	protected String accountId;
	/** 币种 */
	protected String currencyCode;
	/** 产品对象代码 */
	protected String productObjectCode;
	/** 所属业务类型代码 */
	protected String businessTypeCode;
	/** 客户号 */
	protected String customerNo;
	/** 当前余额 */
	protected BigDecimal balance;
	/** 当期本金余额 */
	protected BigDecimal currentPeriodPrincipal;
	/** 账单本金余额 */
	protected BigDecimal billPrincipalBalance;
	/** 当期利息余额 */
	protected BigDecimal currentPeriodInterest;
	/** 账单利息余额 */
	protected BigDecimal billInterestBalance;
	/** 当期费用余额 */
	protected BigDecimal currentPeriodExpense;
	/** 账单费用余额 */
	protected BigDecimal billExpenseBalance;
	
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

	public String getProductObjectCode() {
		return productObjectCode;
	}

	public void setProductObjectCode(String productObjectCode) {
		this.productObjectCode = productObjectCode;
	}

	public String getBusinessTypeCode() {
		return businessTypeCode;
	}

	public void setBusinessTypeCode(String businessTypeCode) {
		this.businessTypeCode = businessTypeCode;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getCurrentPeriodPrincipal() {
		return currentPeriodPrincipal;
	}

	public void setCurrentPeriodPrincipal(BigDecimal currentPeriodPrincipal) {
		this.currentPeriodPrincipal = currentPeriodPrincipal;
	}

	public BigDecimal getBillPrincipalBalance() {
		return billPrincipalBalance;
	}

	public void setBillPrincipalBalance(BigDecimal billPrincipalBalance) {
		this.billPrincipalBalance = billPrincipalBalance;
	}

	public BigDecimal getCurrentPeriodInterest() {
		return currentPeriodInterest;
	}

	public void setCurrentPeriodInterest(BigDecimal currentPeriodInterest) {
		this.currentPeriodInterest = currentPeriodInterest;
	}

	public BigDecimal getBillInterestBalance() {
		return billInterestBalance;
	}

	public void setBillInterestBalance(BigDecimal billInterestBalance) {
		this.billInterestBalance = billInterestBalance;
	}

	public BigDecimal getCurrentPeriodExpense() {
		return currentPeriodExpense;
	}

	public void setCurrentPeriodExpense(BigDecimal currentPeriodExpense) {
		this.currentPeriodExpense = currentPeriodExpense;
	}

	public BigDecimal getBillExpenseBalance() {
		return billExpenseBalance;
	}

	public void setBillExpenseBalance(BigDecimal billExpenseBalance) {
		this.billExpenseBalance = billExpenseBalance;
	}

	@Override
	public String toString() {
		return "X5285VO [accountId=" + accountId + ", currencyCode="
				+ currencyCode + ", productObjectCode=" + productObjectCode
				+ ", businessTypeCode=" + businessTypeCode + ", customerNo="
				+ customerNo + ", balance=" + balance
				+ ", currentPeriodPrincipal=" + currentPeriodPrincipal
				+ ", billPrincipalBalance=" + billPrincipalBalance
				+ ", currentPeriodInterest=" + currentPeriodInterest
				+ ", billInterestBalance=" + billInterestBalance
				+ ", currentPeriodExpense=" + currentPeriodExpense
				+ ", billExpenseBalance=" + billExpenseBalance + "]";
	}
    
}
