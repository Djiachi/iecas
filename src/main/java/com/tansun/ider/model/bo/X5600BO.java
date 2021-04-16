package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.tansun.ider.framwork.commun.BeanVO;
import com.tansun.ider.service.business.EventCommArea;

/**
 * 分期金额统计
 * 
 * @author huangyayun 2018年10月19日
 */
public class X5600BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;
	/** 客户号 */
    private String customerNo;
    /** 业务项目 */
    private String businessProgramNo;
    
    private Integer firstBillingCycle;
    /** 所属业务类型 [15,0] */
    private String businessTypeCode;
    /** 账户代码 */
    private String accountId;
    /** 余额对象代码 */
    private String balanceObjectCode;
    /** 入账币种 */
    private String postingCurrencyCode;
    /** 入账日期 */
    private String occurrDate;
    /** 入账金额 */
    private BigDecimal postingAmount;
    /** 全局流水号 */
    private String ecommOrigGlobalSerialNumbr;
    /** 当前周期号 */
    private Integer currentCycleNumber; 
    
    
    
    
    
	public Integer getCurrentCycleNumber() {
		return currentCycleNumber;
	}
	public void setCurrentCycleNumber(Integer currentCycleNumber) {
		this.currentCycleNumber = currentCycleNumber;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public Integer getFirstBillingCycle() {
		return firstBillingCycle;
	}
	public void setFirstBillingCycle(Integer firstBillingCycle) {
		this.firstBillingCycle = firstBillingCycle;
	}
	public String getBusinessProgramNo() {
		return businessProgramNo;
	}
	public void setBusinessProgramNo(String businessProgramNo) {
		this.businessProgramNo = businessProgramNo;
	}
	public String getBusinessTypeCode() {
		return businessTypeCode;
	}
	public void setBusinessTypeCode(String businessTypeCode) {
		this.businessTypeCode = businessTypeCode;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getBalanceObjectCode() {
		return balanceObjectCode;
	}
	public void setBalanceObjectCode(String balanceObjectCode) {
		this.balanceObjectCode = balanceObjectCode;
	}
	public String getPostingCurrencyCode() {
		return postingCurrencyCode;
	}
	public void setPostingCurrencyCode(String postingCurrencyCode) {
		this.postingCurrencyCode = postingCurrencyCode;
	}
	public String getOccurrDate() {
		return occurrDate;
	}
	public void setOccurrDate(String occurrDate) {
		this.occurrDate = occurrDate;
	}
	public BigDecimal getPostingAmount() {
		return postingAmount;
	}
	public void setPostingAmount(BigDecimal postingAmount) {
		this.postingAmount = postingAmount;
	}
	public String getEcommOrigGlobalSerialNumbr() {
		return ecommOrigGlobalSerialNumbr;
	}
	public void setEcommOrigGlobalSerialNumbr(String ecommOrigGlobalSerialNumbr) {
		this.ecommOrigGlobalSerialNumbr = ecommOrigGlobalSerialNumbr;
	}

}
