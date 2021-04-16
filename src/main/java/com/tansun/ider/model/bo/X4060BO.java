package com.tansun.ider.model.bo;

import java.io.Serializable;

import com.tansun.ider.service.business.EventCommArea;
/**
 * 
 * @Description:TODO()   
 * @author: sunyaoyao
 * @date:   2019年5月22日 下午2:52:20   
 *
 */
public class X4060BO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -578950858819760393L;
	/** 外部识别号 [19,0] Not NULL */
	private String externalIdentificationNo;
	/**公共区*/
	private EventCommArea eventCommArea;
	/**操作人*/
	private String operatorId;
	/**事件号*/
	private String eventNo;
	
	public String getEventNo() {
		return eventNo;
	}
	public void setEventNo(String eventNo) {
		this.eventNo = eventNo;
	}
	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}
	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}
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


}
