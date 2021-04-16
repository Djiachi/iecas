package com.tansun.ider.model.bo;

import java.io.Serializable;

import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.service.business.EventCommArea;

public class X5805BO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6734236769934122602L;
	private EventCommArea eventCommArea;
	private String operatorId;
	/** 客户号 */
	private String customerNo;
	/** 外部识别号 [19,0] Not NULL */
	private String externalIdentificationNo;
	//事件编号
	private String eventNo;
	/** 活动与构件对应关系表 */
	CoreEventActivityRel coreEventActivityRel;
	
	public String getEventNo() {
		return eventNo;
	}

	public void setEventNo(String eventNo) {
		this.eventNo = eventNo;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public EventCommArea getEventCommArea() {
		return eventCommArea;
	}

	public void setEventCommArea(EventCommArea eventCommArea) {
		this.eventCommArea = eventCommArea;
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
	
	public CoreEventActivityRel getCoreEventActivityRel() {
		return coreEventActivityRel;
	}

	public void setCoreEventActivityRel(CoreEventActivityRel coreEventActivityRel) {
		this.coreEventActivityRel = coreEventActivityRel;
	}

	@Override
	public String toString() {
		return "X5805BO [eventCommArea=" + eventCommArea + ", operatorId=" + operatorId + "]";
	}
	
}
