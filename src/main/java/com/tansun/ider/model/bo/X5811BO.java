package com.tansun.ider.model.bo;

import java.io.Serializable;
import com.tansun.ider.service.business.EventCommArea;

public class X5811BO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3646797172525414696L;
	private EventCommArea eventCommArea;
	private String operatorId;
	/** 客户号 */
	private String customerNo;
	/** 外部识别号 [19,0] Not NULL */
	private String externalIdentificationNo;
	/** 产品对象代码 */
	private String productObjectCode;
	/** 事件编号 [14,0] Not NULL */
	private String eventNo;
    /** 活动编号 [8,0] Not NULL */
	private String activityNo;
	
	public EventCommArea getEventCommArea() {
		return eventCommArea;
	}
	public void setEventCommArea(EventCommArea eventCommArea) {
		this.eventCommArea = eventCommArea;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}
	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}
	public String getProductObjectCode() {
		return productObjectCode;
	}
	public void setProductObjectCode(String productObjectCode) {
		this.productObjectCode = productObjectCode;
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
		return "X5811BO [eventCommArea=" + eventCommArea + ", operatorId=" + operatorId + "]";
	}
}