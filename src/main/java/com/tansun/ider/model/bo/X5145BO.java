package com.tansun.ider.model.bo;

import java.io.Serializable;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: 产品线账单日信息维护
 * @Author wt
 * @Date 2018年5月7日 下午2:53:57
 */
public class X5145BO extends BeanVO implements Serializable {

	private static final long serialVersionUID = 3829878085367887895L;

	/** 事件编号 [14,0] Not NULL */
    protected String eventNo;
    /** 活动编号 [8,0] Not NULL */
    protected String activityNo;
	/** 操作员Id */
	private String operatorId;
	/** 主键 [64,0] Not NULL */
	private String id;
	/** 客户代码 [12,0] Not NULL */
	private String customerNo;
	/** 产品线 [9,0] Not NULL */
	private String productLineCode;
	/** 账单日 [10,0] */
	private Integer billDay;
	/** 下一账单日期 [10,0] */
	private String nextBillDate;
	/** 当前周期号 [10,0] */
	private Integer currentCycleNumber;

	public X5145BO() {
		super();
	}

	public X5145BO(String id, String customerNo, String productLineCode, Integer billDay, String nextBillDate,
			Integer currentCycleNumber) {
		super();
		this.id = id;
		this.customerNo = customerNo;
		this.productLineCode = productLineCode;
		this.billDay = billDay;
		this.nextBillDate = nextBillDate;
		this.currentCycleNumber = currentCycleNumber;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getProductLineCode() {
		return productLineCode;
	}

	public void setProductLineCode(String productLineCode) {
		this.productLineCode = productLineCode;
	}

	public Integer getBillDay() {
		return billDay;
	}

	public void setBillDay(Integer billDay) {
		this.billDay = billDay;
	}

	public String getNextBillDate() {
		return nextBillDate;
	}

	public void setNextBillDate(String nextBillDate) {
		this.nextBillDate = nextBillDate;
	}

	public Integer getCurrentCycleNumber() {
		return currentCycleNumber;
	}

	public void setCurrentCycleNumber(Integer currentCycleNumber) {
		this.currentCycleNumber = currentCycleNumber;
	}
	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getEventNo() {
		return eventNo;
	}

	public void setEventNo(String eventNo) {
		this.eventNo = eventNo;
	}

	public String getActivityNo() {
		return activityNo;
	}

	public void setActivityNo(String activityNo) {
		this.activityNo = activityNo;
	}

	@Override
	public String toString() {
		return "X5145BO [id=" + id + ", customerNo=" + customerNo + ", productLineCode=" + productLineCode
				+ ", billDay=" + billDay + ", nextBillDate=" + nextBillDate + ", currentCycleNumber="
				+ currentCycleNumber + "]";
	}

}
