package com.tansun.ider.model.bo;

/**
 * @Desc:客户备注信息维护
 * @Author wt
 * @Date 2018年5月7日 下午2:26:03
 */
public class X5140BO {
	/** 事件编号 [14,0] Not NULL */
    protected String eventNo;
    /** 活动编号 [8,0] Not NULL */
    protected String activityNo;
	/** 操作员Id */
	private String operatorId;
	/* 外部识别号 */
	private String globalEventNo;
	/* id */
	private String id;
	/* 客户号 */
	private String customerNo;
	/* 备注信息 */
	private String remarkInfo;
	
	public X5140BO() {
		super();
	}

	public X5140BO(String globalEventNo, String id, String customerNo, String remarkInfo) {
		super();
		this.globalEventNo = globalEventNo;
		this.id = id;
		this.customerNo = customerNo;
		this.remarkInfo = remarkInfo;
	}

	public String getGlobalEventNo() {
		return globalEventNo;
	}

	public void setGlobalEventNo(String globalEventNo) {
		this.globalEventNo = globalEventNo;
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

	public String getRemarkInfo() {
		return remarkInfo;
	}

	public void setRemarkInfo(String remarkInfo) {
		this.remarkInfo = remarkInfo;
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
		return "X5140BO [globalEventNo=" + globalEventNo + ", id=" + id + ", customerNo=" + customerNo + ", remarkInfo="
				+ remarkInfo + "]";
	}
	
}
