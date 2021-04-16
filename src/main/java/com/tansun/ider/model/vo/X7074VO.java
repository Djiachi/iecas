package com.tansun.ider.model.vo;

import java.math.BigDecimal;

/**
 * @Desc:订单建立数据推送
 * @Author huangyayun
 * @Date 2018年5月29日
 */
public class X7074VO {

	// 分期交易日期  
	String ecommInstallmentTransDate;
    // 分期付款金额 
	BigDecimal ecommInstallmentAmount;
    // 分期付款期数 
	int ecommInstallmentPeriod;
    // 分期付款费率 
	BigDecimal ecommInstallmentFeeRatio;
    // 分期付款费用  
	BigDecimal ecommInstallmentFee;
    // 首次入帐金额 
	BigDecimal ecommFirstPostingAmount;
    // 下次入账金额 
	BigDecimal ecommNextPostingAmount;
    // 分期业务类型 
	String ecommInstallmentBusinessType;
	 // 币种
	String ecommPostingCurr;
	 // 客户号
	String ecommCustId;
	
	public String getEcommCustId() {
		return ecommCustId;
	}
	public void setEcommCustId(String ecommCustId) {
		this.ecommCustId = ecommCustId;
	}
	public String getEcommPostingCurr() {
		return ecommPostingCurr;
	}
	public void setEcommPostingCurr(String ecommPostingCurr) {
		this.ecommPostingCurr = ecommPostingCurr;
	}
	// 外部识别号
	String ecommEntryId;
	public String getEcommInstallmentTransDate() {
		return ecommInstallmentTransDate;
	}
	public void setEcommInstallmentTransDate(String ecommInstallmentTransDate) {
		this.ecommInstallmentTransDate = ecommInstallmentTransDate;
	}
	public BigDecimal getEcommInstallmentAmount() {
		return ecommInstallmentAmount;
	}
	public void setEcommInstallmentAmount(BigDecimal ecommInstallmentAmount) {
		this.ecommInstallmentAmount = ecommInstallmentAmount;
	}
	public int getEcommInstallmentPeriod() {
		return ecommInstallmentPeriod;
	}
	public void setEcommInstallmentPeriod(int ecommInstallmentPeriod) {
		this.ecommInstallmentPeriod = ecommInstallmentPeriod;
	}
	public BigDecimal getEcommInstallmentFeeRatio() {
		return ecommInstallmentFeeRatio;
	}
	public void setEcommInstallmentFeeRatio(BigDecimal ecommInstallmentFeeRatio) {
		this.ecommInstallmentFeeRatio = ecommInstallmentFeeRatio;
	}
	public BigDecimal getEcommInstallmentFee() {
		return ecommInstallmentFee;
	}
	public void setEcommInstallmentFee(BigDecimal ecommInstallmentFee) {
		this.ecommInstallmentFee = ecommInstallmentFee;
	}
	public BigDecimal getEcommFirstPostingAmount() {
		return ecommFirstPostingAmount;
	}
	public void setEcommFirstPostingAmount(BigDecimal ecommFirstPostingAmount) {
		this.ecommFirstPostingAmount = ecommFirstPostingAmount;
	}
	public BigDecimal getEcommNextPostingAmount() {
		return ecommNextPostingAmount;
	}
	public void setEcommNextPostingAmount(BigDecimal ecommNextPostingAmount) {
		this.ecommNextPostingAmount = ecommNextPostingAmount;
	}
	public String getEcommInstallmentBusinessType() {
		return ecommInstallmentBusinessType;
	}
	public void setEcommInstallmentBusinessType(String ecommInstallmentBusinessType) {
		this.ecommInstallmentBusinessType = ecommInstallmentBusinessType;
	}
	public String getEcommEntryId() {
		return ecommEntryId;
	}
	public void setEcommEntryId(String ecommEntryId) {
		this.ecommEntryId = ecommEntryId;
	}
	@Override
	public String toString() {
		return "X7074VO [ecommInstallmentTransDate=" + ecommInstallmentTransDate + ", ecommInstallmentAmount="
				+ ecommInstallmentAmount + ", ecommInstallmentPeriod=" + ecommInstallmentPeriod
				+ ", ecommInstallmentFeeRatio=" + ecommInstallmentFeeRatio + ", ecommInstallmentFee="
				+ ecommInstallmentFee + ", ecommFirstPostingAmount=" + ecommFirstPostingAmount
				+ ", ecommNextPostingAmount=" + ecommNextPostingAmount + ", ecommInstallmentBusinessType="
				+ ecommInstallmentBusinessType + ", ecommEntryId=" + ecommEntryId + ", ecommPostingCurr=" + ecommPostingCurr+ ", ecommCustId=" + ecommCustId + "]";
	}
	 

}
