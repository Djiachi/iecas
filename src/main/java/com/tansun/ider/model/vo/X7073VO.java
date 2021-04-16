package com.tansun.ider.model.vo;

import java.math.BigDecimal;

/**
 * @Desc:分期计划详细信息查询
 * @Author huangyayun
 * @Date 2018年5月29日
 */
public class X7073VO {

	private String id;
	private String stageControlNum;
	private String stageControlName;
	private String appType;
	private String feeCollectType;
	private String projectType;
	private String effectFlag;
	private String effectStartDate;
	private String effectEndDate;
	private BigDecimal minAmt;
	private BigDecimal maxAmt;
	private BigDecimal feeRate;
	private Integer period;
	private BigDecimal feeRateType;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStageControlNum() {
		return stageControlNum;
	}
	public void setStageControlNum(String stageControlNum) {
		this.stageControlNum = stageControlNum;
	}
	public String getStageControlName() {
		return stageControlName;
	}
	public void setStageControlName(String stageControlName) {
		this.stageControlName = stageControlName;
	}
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	public String getFeeCollectType() {
		return feeCollectType;
	}
	public void setFeeCollectType(String feeCollectType) {
		this.feeCollectType = feeCollectType;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public String getEffectFlag() {
		return effectFlag;
	}
	public void setEffectFlag(String effectFlag) {
		this.effectFlag = effectFlag;
	}
	public String getEffectStartDate() {
		return effectStartDate;
	}
	public void setEffectStartDate(String effectStartDate) {
		this.effectStartDate = effectStartDate;
	}
	public String getEffectEndDate() {
		return effectEndDate;
	}
	public void setEffectEndDate(String effectEndDate) {
		this.effectEndDate = effectEndDate;
	}
	public BigDecimal getMinAmt() {
		return minAmt;
	}
	public void setMinAmt(BigDecimal minAmt) {
		this.minAmt = minAmt;
	}
	public BigDecimal getMaxAmt() {
		return maxAmt;
	}
	public void setMaxAmt(BigDecimal maxAmt) {
		this.maxAmt = maxAmt;
	}
	public BigDecimal getFeeRate() {
		return feeRate;
	}
	public void setFeeRate(BigDecimal feeRate) {
		this.feeRate = feeRate;
	}
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
	public BigDecimal getFeeRateType() {
		return feeRateType;
	}
	public void setFeeRateType(BigDecimal feeRateType) {
		this.feeRateType = feeRateType;
	}
	  
	

}
