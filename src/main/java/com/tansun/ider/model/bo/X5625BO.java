package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;

import com.tansun.ider.framwork.commun.BeanVO;
import com.tansun.ider.model.PrcpPlan;
import com.tansun.ider.service.business.EventCommArea;
/**账单日查询上送
 * 
 * @author sunyaoyao 2019.03.12
 *
 */
public class X5625BO extends BeanVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4625326427076646209L;

	 /** 公共区域信息*/
    private EventCommArea eventCommArea;
    /** 还款周期单位 **/
 	private String repayPrincipalUnit;
 	/** 只适用于EACHTIMEREPAY还本周期**/
 	private Integer prcpRepayPrincipal;
 	/** 只适用于EACHTIMEREPAY还本周期单位 **/
 	private String prcpRepayPrincipalUnit;
 	/** 自定义还本计划EACHTIMEREPAY，ONETIMEPP，只适应于按天计息 **/
 	private List<PrcpPlan> prcpPlans = new ArrayList<PrcpPlan>();
   
    public List<PrcpPlan> getPrcpPlans() {
		return prcpPlans;
	}
	public void setPrcpPlans(List<PrcpPlan> prcpPlans) {
		this.prcpPlans = prcpPlans;
	}
	public String getRepayPrincipalUnit() {
		return repayPrincipalUnit;
	}
	public void setRepayPrincipalUnit(String repayPrincipalUnit) {
		this.repayPrincipalUnit = repayPrincipalUnit;
	}
	public Integer getPrcpRepayPrincipal() {
		return prcpRepayPrincipal;
	}
	public void setPrcpRepayPrincipal(Integer prcpRepayPrincipal) {
		this.prcpRepayPrincipal = prcpRepayPrincipal;
	}
	public String getPrcpRepayPrincipalUnit() {
		return prcpRepayPrincipalUnit;
	}
	public void setPrcpRepayPrincipalUnit(String prcpRepayPrincipalUnit) {
		this.prcpRepayPrincipalUnit = prcpRepayPrincipalUnit;
	}
	/** 放款日期**/
 	private String payLoanDate;
 	/** 试算标志 */
    private String tryFlag;
    /** 放款利率**/
 	private BigDecimal loanRate;
 	/** 利率类型，0:年利率，1：月利率 2：日利率*/
	private String rateInd;
	/** 罚息**/
 	private BigDecimal ecommPenaltyInterestRate;
 	/** 罚息上浮比例**/
 	private BigDecimal penaltyUp;
 	/** 还款日类型**/
 	private String repaymentDateType;
 	/** 还款周期 **/
 	private Integer repayPrincipal;
 	/** 业务项目 */
    private String ecommBusinessProgramCode;
    /** 还款日**/
 	private Integer repayDay;
 	/**首次还款日*/
    private String firstRepayDate;
    /** 还款方式**/
    @NotBlank(message ="还款方式不能为空")
 	private String repayMode;
    /**贷款到期日*/
    private String loanEndDate;
    /** 月供期限   **/
 	@Min(0)
 	private Integer monthSupplyPeriod;
	public Integer getMonthSupplyPeriod() {
		return monthSupplyPeriod;
	}
	public void setMonthSupplyPeriod(Integer monthSupplyPeriod) {
		this.monthSupplyPeriod = monthSupplyPeriod;
	}
	public String getLoanEndDate() {
		return loanEndDate;
	}
	public void setLoanEndDate(String loanEndDate) {
		this.loanEndDate = loanEndDate;
	}
	public String getRepayMode() {
		return repayMode;
	}
	public void setRepayMode(String repayMode) {
		this.repayMode = repayMode;
	}
	public String getFirstRepayDate() {
		return firstRepayDate;
	}
	public void setFirstRepayDate(String firstRepayDate) {
		this.firstRepayDate = firstRepayDate;
	}
	public Integer getRepayDay() {
		return repayDay;
	}
	public void setRepayDay(Integer repayDay) {
		this.repayDay = repayDay;
	}
	public String getPayLoanDate() {
		return payLoanDate;
	}
	/**金额*/
    @DecimalMin(value="0",message ="放款金额应大于0")
    private BigDecimal ecommTransAmount;
	public void setPayLoanDate(String payLoanDate) {
		this.payLoanDate = payLoanDate;
	}
	public BigDecimal getEcommTransAmount() {
		return ecommTransAmount;
	}
	public void setEcommTransAmount(BigDecimal ecommTransAmount) {
		this.ecommTransAmount = ecommTransAmount;
	}
	public String getTryFlag() {
		return tryFlag;
	}
	public void setTryFlag(String tryFlag) {
		this.tryFlag = tryFlag;
	}
	public BigDecimal getLoanRate() {
		return loanRate;
	}
	public void setLoanRate(BigDecimal loanRate) {
		this.loanRate = loanRate;
	}
	public String getRateInd() {
		return rateInd;
	}
	public void setRateInd(String rateInd) {
		this.rateInd = rateInd;
	}
	public BigDecimal getEcommPenaltyInterestRate() {
		return ecommPenaltyInterestRate;
	}
	public void setEcommPenaltyInterestRate(BigDecimal ecommPenaltyInterestRate) {
		this.ecommPenaltyInterestRate = ecommPenaltyInterestRate;
	}
	public BigDecimal getPenaltyUp() {
		return penaltyUp;
	}
	public void setPenaltyUp(BigDecimal penaltyUp) {
		this.penaltyUp = penaltyUp;
	}
	public String getRepaymentDateType() {
		return repaymentDateType;
	}
	public void setRepaymentDateType(String repaymentDateType) {
		this.repaymentDateType = repaymentDateType;
	}
	public Integer getRepayPrincipal() {
		return repayPrincipal;
	}
	public void setRepayPrincipal(Integer repayPrincipal) {
		this.repayPrincipal = repayPrincipal;
	}
	public String getEcommBusinessProgramCode() {
		return ecommBusinessProgramCode;
	}
	public void setEcommBusinessProgramCode(String ecommBusinessProgramCode) {
		this.ecommBusinessProgramCode = ecommBusinessProgramCode;
	}
	public EventCommArea getEventCommArea() {
		return eventCommArea;
	}
	public void setEventCommArea(EventCommArea eventCommArea) {
		this.eventCommArea = eventCommArea;
	}
	/**外部识别号*/
	private String ecommEntryId;
//	/**
//	 * 客户证件号码
//	 */
//	private String id_number;
//	/**
//	 * 证件类型
//	 */
//	private String id_type;
//	/**
//	 * 产品对象代码
//	 */
//	private String product_object_code;
//	
	public String getEcommEntryId() {
		return ecommEntryId;
	}
	public void setEcommEntryId(String ecommEntryId) {
		this.ecommEntryId = ecommEntryId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
