package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.tansun.ider.framwork.commun.BeanVO;
import com.tansun.ider.service.business.EventCommArea;

/**账单日查询返回
 * 
 * @author sunyaoyao 2019.03.12
 *
 */
public class X5625VO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5123786561346277457L;

    /** 外部识别号*/
	private String ecommEntryId;
	/*** 客户证件号码*/
	private String idNumber;
	/*** 证件类型*/
	private String idType;
	/** 产品对象代码 */
    private String ecommProdObjId;
    /** 还款日**/
 	private Integer repayDay;
 	/** 所属业务类型*/
    private String ecommBusineseType;
    /** 业务项目 */
    private String ecommBusinessProgramCode;
    /** 客户号码 */
    private String ecommCustId;
    /**客户姓名*/
    private String ecommCustName;
    
    
    //首次还款日
    private String firstRepayDate;
    //利率依据方式
    private String loanRateMode;
    //计息方式，按天或者按期
    private String interestMode;
    //首期计息规则
    private String firstInterestMode;
    //末期计息规则
    private String endInterestMode;
    //年利率
    private BigDecimal yearRate;
    //罚息利率
    private BigDecimal ecommPenaltyInterestRate;
    //还款日类型
    private String repaymentDateType;
    //是否更新客户账单表
    private String updateCustBillDay;
    /** 还款周期 **/
    private Integer repayPrincipal;
    /** 只适用于EACHTIMEREPAY还本周期单位 **/
 	private String prcpRepayPrincipalUnit;
 	//计算还款日
    private Integer payoutDay;
    /**客户姓名*/
    private String productDesc;
    
	public String getFirstRepayDate() {
		return firstRepayDate;
	}
	public void setFirstRepayDate(String firstRepayDate) {
		this.firstRepayDate = firstRepayDate;
	}
	public String getLoanRateMode() {
		return loanRateMode;
	}
	public void setLoanRateMode(String loanRateMode) {
		this.loanRateMode = loanRateMode;
	}
	public String getInterestMode() {
		return interestMode;
	}
	public void setInterestMode(String interestMode) {
		this.interestMode = interestMode;
	}
	public String getFirstInterestMode() {
		return firstInterestMode;
	}
	public void setFirstInterestMode(String firstInterestMode) {
		this.firstInterestMode = firstInterestMode;
	}
	public String getEndInterestMode() {
		return endInterestMode;
	}
	public void setEndInterestMode(String endInterestMode) {
		this.endInterestMode = endInterestMode;
	}
	public BigDecimal getYearRate() {
		return yearRate;
	}
	public void setYearRate(BigDecimal yearRate) {
		this.yearRate = yearRate;
	}
	public BigDecimal getEcommPenaltyInterestRate() {
		return ecommPenaltyInterestRate;
	}
	public void setEcommPenaltyInterestRate(BigDecimal ecommPenaltyInterestRate) {
		this.ecommPenaltyInterestRate = ecommPenaltyInterestRate;
	}
	public String getRepaymentDateType() {
		return repaymentDateType;
	}
	public void setRepaymentDateType(String repaymentDateType) {
		this.repaymentDateType = repaymentDateType;
	}
	public String getUpdateCustBillDay() {
		return updateCustBillDay;
	}
	public void setUpdateCustBillDay(String updateCustBillDay) {
		this.updateCustBillDay = updateCustBillDay;
	}
	public Integer getRepayPrincipal() {
		return repayPrincipal;
	}
	public void setRepayPrincipal(Integer repayPrincipal) {
		this.repayPrincipal = repayPrincipal;
	}
	public String getPrcpRepayPrincipalUnit() {
		return prcpRepayPrincipalUnit;
	}
	public void setPrcpRepayPrincipalUnit(String prcpRepayPrincipalUnit) {
		this.prcpRepayPrincipalUnit = prcpRepayPrincipalUnit;
	}
	public Integer getPayoutDay() {
		return payoutDay;
	}
	public void setPayoutDay(Integer payoutDay) {
		this.payoutDay = payoutDay;
	}
	public String getEcommCustName() {
		return ecommCustName;
	}
	public void setEcommCustName(String ecommCustName) {
		this.ecommCustName = ecommCustName;
	}
	public String getEcommEntryId() {
		return ecommEntryId;
	}
	public void setEcommEntryId(String ecommEntryId) {
		this.ecommEntryId = ecommEntryId;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getEcommProdObjId() {
		return ecommProdObjId;
	}
	public void setEcommProdObjId(String ecommProdObjId) {
		this.ecommProdObjId = ecommProdObjId;
	}
	public Integer getRepayDay() {
		return repayDay;
	}
	public void setRepayDay(Integer repayDay) {
		this.repayDay = repayDay;
	}
	public String getEcommBusineseType() {
		return ecommBusineseType;
	}
	public void setEcommBusineseType(String ecommBusineseType) {
		this.ecommBusineseType = ecommBusineseType;
	}
	public String getEcommBusinessProgramCode() {
		return ecommBusinessProgramCode;
	}
	public void setEcommBusinessProgramCode(String ecommBusinessProgramCode) {
		this.ecommBusinessProgramCode = ecommBusinessProgramCode;
	}
	public String getEcommCustId() {
		return ecommCustId;
	}
	public void setEcommCustId(String ecommCustId) {
		this.ecommCustId = ecommCustId;
	}
	@Override
	public String toString() {
		return "X5625VO [ ecommEntryId=" + ecommEntryId + ", idNumber=" + idNumber
				+ ", idType=" + idType + ", ecommProdObjId=" + ecommProdObjId + ", repayDay=" + repayDay
				+ ", ecommBusineseType=" + ecommBusineseType + ", ecommBusinessProgramCode=" + ecommBusinessProgramCode
				+ ", ecommCustId=" + ecommCustId + ", ecommCustName=" + ecommCustName + ", firstRepayDate="
				+ firstRepayDate + ", loanRateMode=" + loanRateMode + ", interestMode=" + interestMode
				+ ", firstInterestMode=" + firstInterestMode + ", endInterestMode=" + endInterestMode + ", yearRate="
				+ yearRate + ", ecommPenaltyInterestRate=" + ecommPenaltyInterestRate + ", repaymentDateType="
				+ repaymentDateType + ", updateCustBillDay=" + updateCustBillDay + ", repayPrincipal=" + repayPrincipal
				+ ", prcpRepayPrincipalUnit=" + prcpRepayPrincipalUnit + ", payoutDay=" + payoutDay + "]";
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	
}
