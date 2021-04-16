package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class X5535VO implements Serializable {
	
	private static final long serialVersionUID = 3160740559121268175L;
	/** 原结息金额 */
	private String oriInterestSettlementAmount;
	/** 余额对象代码*/
	private String balanceObjectCode;
	/** 余额单元代码 [18,0] Not NULL */
    protected String balanceUnitCode;
    /** 发生日期 yyyy-MM-dd [10,0] Not NULL */
    protected String occurrDate;
    /** 节点类别 WI-免息 BI-结息 [2,0] Not NULL */
    protected String nodeTyp;
    /** 节点状态 W0:有效 W1：无效 B0：正常结息 B1：0余额结息 B9：需重计利息 [2,0] Not NULL */
    protected String nodeStatus;
    /** 本结息周期利息累计金额 [18,0] Not NULL */
    protected BigDecimal currInterestAmount;
    /** 上一周期利息累计转入金额 [18,0] */
    protected BigDecimal lastInterestAmount;
    /** 结息周期号 [10,0] */
    protected Integer billingCycleNo;
    /** 结息开始日期 [10,0] */
    protected String billingStartDate;
    /** 结息结束日期 [10,0] */
    protected String billingEndDate;
    /** 本周起利息入账标志 Y：已入账 N：未入账 [1,0] */
    protected String currInterestPostFlag;
    /** 利息入账余额对象 [255,0] */
    protected String interestPostBalanceObj;
    /** 原利息金额 [18,0] */
    protected BigDecimal oriInterestAmount;
    /** 创建时间 yyyy-MM-dd HH:mm:ss.SSS [23,0] */
    protected String gmtCreate;
    /**  [19,0] Not NULL */
    protected Date timestamp;
    /** 版本号 [10,0] Not NULL */
    protected Integer version;
    /** period : 期次 [10,0] */
    protected Integer period;
    /** 余额对象描述 */
    private String balanceObjectDesc;
    /** 当前周期号 */
    private Integer cycleNumber;
	/** 动态计息利率 [8,7] */
	private BigDecimal dynamicInterestRate;
    
	public BigDecimal getDynamicInterestRate() {
		return dynamicInterestRate;
	}
	public void setDynamicInterestRate(BigDecimal dynamicInterestRate) {
		this.dynamicInterestRate = dynamicInterestRate;
	}
	public Integer getCycleNumber() {
		return cycleNumber;
	}
	public void setCycleNumber(Integer cycleNumber) {
		this.cycleNumber = cycleNumber;
	}
	public String getBalanceObjectDesc() {
		return balanceObjectDesc;
	}
	public void setBalanceObjectDesc(String balanceObjectDesc) {
		this.balanceObjectDesc = balanceObjectDesc;
	}
	public String getBalanceUnitCode() {
		return balanceUnitCode;
	}
	public void setBalanceUnitCode(String balanceUnitCode) {
		this.balanceUnitCode = balanceUnitCode;
	}
	public String getOccurrDate() {
		return occurrDate;
	}
	public void setOccurrDate(String occurrDate) {
		this.occurrDate = occurrDate;
	}
	public String getNodeTyp() {
		return nodeTyp;
	}
	public void setNodeTyp(String nodeTyp) {
		this.nodeTyp = nodeTyp;
	}
	public String getNodeStatus() {
		return nodeStatus;
	}
	public void setNodeStatus(String nodeStatus) {
		this.nodeStatus = nodeStatus;
	}
	public BigDecimal getCurrInterestAmount() {
		return currInterestAmount;
	}
	public void setCurrInterestAmount(BigDecimal currInterestAmount) {
		this.currInterestAmount = currInterestAmount;
	}
	public BigDecimal getLastInterestAmount() {
		return lastInterestAmount;
	}
	public void setLastInterestAmount(BigDecimal lastInterestAmount) {
		this.lastInterestAmount = lastInterestAmount;
	}
	public Integer getBillingCycleNo() {
		return billingCycleNo;
	}
	public void setBillingCycleNo(Integer billingCycleNo) {
		this.billingCycleNo = billingCycleNo;
	}
	public String getBillingStartDate() {
		return billingStartDate;
	}
	public void setBillingStartDate(String billingStartDate) {
		this.billingStartDate = billingStartDate;
	}
	public String getBillingEndDate() {
		return billingEndDate;
	}
	public void setBillingEndDate(String billingEndDate) {
		this.billingEndDate = billingEndDate;
	}
	public String getCurrInterestPostFlag() {
		return currInterestPostFlag;
	}
	public void setCurrInterestPostFlag(String currInterestPostFlag) {
		this.currInterestPostFlag = currInterestPostFlag;
	}
	public String getInterestPostBalanceObj() {
		return interestPostBalanceObj;
	}
	public void setInterestPostBalanceObj(String interestPostBalanceObj) {
		this.interestPostBalanceObj = interestPostBalanceObj;
	}
	public BigDecimal getOriInterestAmount() {
		return oriInterestAmount;
	}
	public void setOriInterestAmount(BigDecimal oriInterestAmount) {
		this.oriInterestAmount = oriInterestAmount;
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
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
	public String getBalanceObjectCode() {
		return balanceObjectCode;
	}
	public void setBalanceObjectCode(String balanceObjectCode) {
		this.balanceObjectCode = balanceObjectCode;
	}
	public String getOriInterestSettlementAmount() {
		return oriInterestSettlementAmount;
	}
	public void setOriInterestSettlementAmount(String oriInterestSettlementAmount) {
		this.oriInterestSettlementAmount = oriInterestSettlementAmount;
	}
	
}
