package com.tansun.ider.model.bo;

import java.io.Serializable;

/**
 * @Desc: 产品附加信息维护
 * @Author wt
 * @Date 2018年5月8日 上午10:33:35
 */
public class X5160BO implements Serializable {

	private static final long serialVersionUID = -414767865800879907L;

	/** 事件编号 [14,0] Not NULL */
    protected String eventNo;
    /** 活动编号 [8,0] Not NULL */
    protected String activityNo;
	/** 操作员Id */
	private String operatorId;
	/* id */
	private String id;
	/* 产品单元代码 */
	private String productUnitCode;
	/* 联名号 */
	private String coBrandedNo;
    /** 客户号 */
    private String customerNo;
    /** 产品对象代码 */
    private String productObjectCode;
    
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductUnitCode() {
		return productUnitCode;
	}

	public void setProductUnitCode(String productUnitCode) {
		this.productUnitCode = productUnitCode;
	}

	public String getCoBrandedNo() {
		return coBrandedNo;
	}

	public void setCoBrandedNo(String coBrandedNo) {
		this.coBrandedNo = coBrandedNo;
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

	
	

}
