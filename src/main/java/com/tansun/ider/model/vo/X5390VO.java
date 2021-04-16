package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.tansun.ider.dao.issue.entity.CoreCustomerDelqStatic;
import com.tansun.ider.framwork.commun.BeanVO;
import com.tansun.ider.framwork.commun.PageBean;

public class X5390VO extends BeanVO implements Serializable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = -7572783161182303108L;
	/** 币种 : 币种 [3,0] Not NULL */
    protected String currencyCode;
    /** 当前周期号 : 当前周期号 [10,0] Not NULL */
    protected Integer currentCycleNumber;
    /** 业务项目 : 业务项目 [9,0] Not NULL */
    protected String businessProgramNo;
    /** 产品对象代码 : 产品对象代码 [15,0] Not NULL */
    protected String productObjectCode;
    /** 所属业务类型 : 所属业务类型 [15,0] Not NULL */
    protected String businessTypeCode;
    /** 账户组织形式 : 账户组织形式 [1,0] */
    protected String accountOrganForm;
    /** 客户代码 : 客户代码 [20,0] Not NULL */
    protected String customerNo;
    /** 账单日期 : 账单日期 [10,0] */
    protected String billDate;
    /** 宽限余额 : 宽限余额 [18,0] */
    protected BigDecimal graceBalance;
    /** 贷方发生额 : 贷方发生额 [18,0] */
    protected BigDecimal creditAmount;
    /** 贷方发生笔数 : 贷方发生笔数 [10,0] */
    protected Integer creditNum;
    /** 借方发生额 : 借方发生额 [18,0] */
    protected BigDecimal debitAmount;
    /** 借方笔数 : 借方笔数 [10,0] */
    protected Integer debitNum;
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
    /** 状态码 : 状态码 [10,0] */
    protected String statusCode;
    /** 期初余额 : 期初余额 [18,0] */
    protected BigDecimal beginBalance;
    /** 本期还款金额 : 本期还款金额 [18,0] */
    protected BigDecimal currentRepayAmount;
    /** 创建时间 : 创建时间 [23,0] */
    protected String gmtCreate;
    /** 时间戳 : 时间戳 [19,0] Not NULL */
    protected Date timestamp;
    /** 版本号 : 版本号 [10,0] Not NULL */
    protected Integer version;
    /** 最低还款额 */
    BigDecimal minRepayAmt;
    protected String accountId;
	protected String programDesc;
    protected String businessDesc;
    private String productDesc;
    private String currencyDesc;
    
	PageBean<CoreCustomerDelqStatic> page = new PageBean<>();
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
	public String getBusinessTypeCode() {
		return businessTypeCode;
	}
	public void setBusinessTypeCode(String businessTypeCode) {
		this.businessTypeCode = businessTypeCode;
	}
	public String getAccountOrganForm() {
		return accountOrganForm;
	}
	public void setAccountOrganForm(String accountOrganForm) {
		this.accountOrganForm = accountOrganForm;
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
	public BigDecimal getGraceBalance() {
		return graceBalance;
	}
	public void setGraceBalance(BigDecimal graceBalance) {
		this.graceBalance = graceBalance;
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
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public BigDecimal getBeginBalance() {
		return beginBalance;
	}
	public void setBeginBalance(BigDecimal beginBalance) {
		this.beginBalance = beginBalance;
	}
	public BigDecimal getCurrentRepayAmount() {
		return currentRepayAmount;
	}
	public void setCurrentRepayAmount(BigDecimal currentRepayAmount) {
		this.currentRepayAmount = currentRepayAmount;
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
	public PageBean<CoreCustomerDelqStatic> getPage() {
		return page;
	}
	public void setPage(PageBean<CoreCustomerDelqStatic> page) {
		this.page = page;
	}
	public String getProgramDesc() {
		return programDesc;
	}
	public void setProgramDesc(String programDesc) {
		this.programDesc = programDesc;
	}
	public String getBusinessDesc() {
		return businessDesc;
	}
	public void setBusinessDesc(String businessDesc) {
		this.businessDesc = businessDesc;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public String getCurrencyDesc() {
		return currencyDesc;
	}
	public void setCurrencyDesc(String currencyDesc) {
		this.currencyDesc = currencyDesc;
	}
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
}
