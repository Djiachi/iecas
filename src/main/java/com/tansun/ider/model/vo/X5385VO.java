package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.tansun.ider.dao.issue.entity.CoreCustomerDelqStatic;
import com.tansun.ider.framwork.commun.BeanVO;
import com.tansun.ider.framwork.commun.PageBean;

public class X5385VO  extends BeanVO implements Serializable{

	private static final long serialVersionUID = -2563282886664492663L;
	  /** 产品描述 [50,0] */
    protected String productDesc;
	 /** 币种 : 币种 [3,0] Not NULL */
    protected String currencyCode;
    /** 当前周期号 : 当前周期号 [10,0] Not NULL */
    protected Integer currentCycleNumber;
    /** 业务项目 : 业务项目 [9,0] Not NULL */
    protected String businessProgramNo;
    /** 产品对象代码 : 产品对象代码 [15,0] Not NULL */
    protected String productObjectCode;
    /** 客户代码 : 客户代码 [20,0] Not NULL */
    protected String customerNo;
    /** 账单日期 : 账单日期 [10,0] */
    protected String billDate;
    /** 宽限余额 : 宽限总余额 [18,0] */
    protected BigDecimal allGraceBalance;
    /** 账单日 : 账单日 [10,0] */
    protected Integer billDay;
    /** 还款到期日 : 还款到期日 [10,0] */
    protected String paymentDueDate;
    /** 账单周期天数 : 账单周期天数 [10,0] */
    protected Integer billCycleDays;
    /** 本期贷方发生额 : 本期贷方发生额 [18,0] */
    protected BigDecimal creditAmount;
    /** 本期贷方发生笔数 : 本期贷方发生笔数 [10,0] */
    protected Integer creditNum;
    /** 借方发生额 : 借方发生额 [18,0] */
    protected BigDecimal debitAmount;
    /** 借方笔数 : 借方笔数 [10,0] */
    protected Integer debitNum;
    /** 本期还款金额 : 本期还款金额 [18,0] */
    protected BigDecimal currentRepayAmount;
    /** 账单金额 : 账单金额 [18,0] */
    protected BigDecimal postingAmount;
    /** 本金金额 : 本金金额 [18,0] */
    protected BigDecimal principalAmount;
    /** 费用金额 : 费用金额 [18,0] */
    protected BigDecimal feeAmount;
    /** 利息金额 : 利息金额 [18,0] */
    protected BigDecimal interestAmount;
    /** 当期需还欠款 : 当期需还欠款 [18,0] */
    protected BigDecimal currentAccountBalance;
    /** 本期最低需还款额 : 本期最低需还款额 [18,0] */
    protected BigDecimal currCyclePaymentMin;
    /** 逾期金额 : 逾期金额 [18,0] */
    protected BigDecimal dueAmount;
    /** 逾期期数 : 逾期期数 [10,0] */
    protected Integer cycleDueNum;
    /** 逾期天数 : 逾期天数 [10,0] */
    protected Integer cycleDueDayNum;
    /** 期初余额 : 期初余额 [18,0] */
    protected BigDecimal beginBalance;
    /** 创建时间 : 创建时间 [23,0] */
    protected String gmtCreate;
    /** 时间戳 : 时间戳 [19,0] Not NULL */
    protected Date timestamp;
    /** 版本号 : 版本号 [10,0] Not NULL */
    protected Integer version;
    /** 最低还款额 */
    BigDecimal minRepayAmt;
    
    private String programDesc;
    
    
	PageBean<CoreCustomerDelqStatic> page = new PageBean<>();
	public PageBean<CoreCustomerDelqStatic> getPage() {
		return page;
	}
	public void setPage(PageBean<CoreCustomerDelqStatic> page) {
		this.page = page;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public Integer getCurrentCycleNumber() {
		return currentCycleNumber;
	}
	public void setCurrentCycleNumber(Integer currentCycleNumber) {
		this.currentCycleNumber = currentCycleNumber;
	}
	public String getBusinessProgramNo() {
		return businessProgramNo;
	}
	public void setBusinessProgramNo(String businessProgramNo) {
		this.businessProgramNo = businessProgramNo;
	}
	public String getProductObjectCode() {
		return productObjectCode;
	}
	public void setProductObjectCode(String productObjectCode) {
		this.productObjectCode = productObjectCode;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public BigDecimal getAllGraceBalance() {
		return allGraceBalance;
	}
	public void setAllGraceBalance(BigDecimal allGraceBalance) {
		this.allGraceBalance = allGraceBalance;
	}
	public Integer getBillDay() {
		return billDay;
	}
	public void setBillDay(Integer billDay) {
		this.billDay = billDay;
	}
	public String getPaymentDueDate() {
		return paymentDueDate;
	}
	public void setPaymentDueDate(String paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}
	public Integer getBillCycleDays() {
		return billCycleDays;
	}
	public void setBillCycleDays(Integer billCycleDays) {
		this.billCycleDays = billCycleDays;
	}
	public BigDecimal getCreditAmount() {
		return creditAmount;
	}
	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}
	public Integer getCreditNum() {
		return creditNum;
	}
	public void setCreditNum(Integer creditNum) {
		this.creditNum = creditNum;
	}
	public BigDecimal getDebitAmount() {
		return debitAmount;
	}
	public void setDebitAmount(BigDecimal debitAmount) {
		this.debitAmount = debitAmount;
	}
	public Integer getDebitNum() {
		return debitNum;
	}
	public void setDebitNum(Integer debitNum) {
		this.debitNum = debitNum;
	}
	public BigDecimal getCurrentRepayAmount() {
		return currentRepayAmount;
	}
	public void setCurrentRepayAmount(BigDecimal currentRepayAmount) {
		this.currentRepayAmount = currentRepayAmount;
	}
	public BigDecimal getPostingAmount() {
		return postingAmount;
	}
	public void setPostingAmount(BigDecimal postingAmount) {
		this.postingAmount = postingAmount;
	}
	public BigDecimal getPrincipalAmount() {
		return principalAmount;
	}
	public void setPrincipalAmount(BigDecimal principalAmount) {
		this.principalAmount = principalAmount;
	}
	public BigDecimal getFeeAmount() {
		return feeAmount;
	}
	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}
	public BigDecimal getInterestAmount() {
		return interestAmount;
	}
	public void setInterestAmount(BigDecimal interestAmount) {
		this.interestAmount = interestAmount;
	}
	public BigDecimal getCurrentAccountBalance() {
		return currentAccountBalance;
	}
	public void setCurrentAccountBalance(BigDecimal currentAccountBalance) {
		this.currentAccountBalance = currentAccountBalance;
	}
	public BigDecimal getCurrCyclePaymentMin() {
		return currCyclePaymentMin;
	}
	public void setCurrCyclePaymentMin(BigDecimal currCyclePaymentMin) {
		this.currCyclePaymentMin = currCyclePaymentMin;
	}
	public BigDecimal getDueAmount() {
		return dueAmount;
	}
	public void setDueAmount(BigDecimal dueAmount) {
		this.dueAmount = dueAmount;
	}
	public Integer getCycleDueNum() {
		return cycleDueNum;
	}
	public void setCycleDueNum(Integer cycleDueNum) {
		this.cycleDueNum = cycleDueNum;
	}
	public Integer getCycleDueDayNum() {
		return cycleDueDayNum;
	}
	public void setCycleDueDayNum(Integer cycleDueDayNum) {
		this.cycleDueDayNum = cycleDueDayNum;
	}
	public BigDecimal getBeginBalance() {
		return beginBalance;
	}
	public void setBeginBalance(BigDecimal beginBalance) {
		this.beginBalance = beginBalance;
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
	public BigDecimal getMinRepayAmt() {
		return minRepayAmt;
	}
	public void setMinRepayAmt(BigDecimal minRepayAmt) {
		this.minRepayAmt = minRepayAmt;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public String getProgramDesc() {
		return programDesc;
	}
	public void setProgramDesc(String programDesc) {
		this.programDesc = programDesc;
	}
	
}
