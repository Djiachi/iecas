package com.tansun.ider.model.bo;

import java.io.Serializable;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.commun.BeanVO;

/**
 * 关联套卡查询
 * @author wangxi
 *
 */
public class X5271BO extends BeanVO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 685787126870544784L;
	/** 活动与构件对应关系表 */
    private CoreEventActivityRel coreEventActivityRel;
    /** 全局事件编号 */
    private String globalEventNo;
    /** 事件编号 [14,0] Not NULL */
    private String eventNo;
    /** 活动编号 [8,0] Not NULL */
    private String activityNo;
    /** 当前日志标志 A/B [1,0] */
    private String currLogFlag;
	/** 赋予初始值 */
	private String ecommEventId; // 事件ID CRDPR40G000001
    /** 外部识别号 */
    private String externalIdentificationNo;
    /** 客户代码 [12,0] */
    private String customerNo;
    /** 产品对象代码 [9,0] Not NULL */
    private String productObjectCode;
    /** 产品描述 [50,0] */
    private String productDesc;
    /** 套卡对方产品对象代码 [9,0] */
    private String productCodeSet;
	public CoreEventActivityRel getCoreEventActivityRel() {
		return coreEventActivityRel;
	}
	public void setCoreEventActivityRel(CoreEventActivityRel coreEventActivityRel) {
		this.coreEventActivityRel = coreEventActivityRel;
	}
	public String getGlobalEventNo() {
		return globalEventNo;
	}
	public void setGlobalEventNo(String globalEventNo) {
		this.globalEventNo = globalEventNo;
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
	public String getCurrLogFlag() {
		return currLogFlag;
	}
	public void setCurrLogFlag(String currLogFlag) {
		this.currLogFlag = currLogFlag;
	}
	public String getEcommEventId() {
		return ecommEventId;
	}
	public void setEcommEventId(String ecommEventId) {
		this.ecommEventId = ecommEventId;
	}
	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}
	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getProductObjectCode() {
		return productObjectCode;
	}
	public void setProductObjectCode(String productObjectCode) {
		this.productObjectCode = productObjectCode;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public String getProductCodeSet() {
		return productCodeSet;
	}
	public void setProductCodeSet(String productCodeSet) {
		this.productCodeSet = productCodeSet;
	}


}
