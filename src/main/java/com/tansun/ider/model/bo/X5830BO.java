package com.tansun.ider.model.bo;

import java.io.Serializable;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.commun.BeanVO;

/**
 * 产品升降级
 * @author wangxi
 *
 */
public class X5830BO extends BeanVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2401540999030420902L;
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
    /** 产品类型 CRD: 信用卡产品 RLN:消费信贷产品 [3,0] */
    private String productType;
	/** 运营模式 [3,0] Not NULL */
    private String operationMode;
    private String productLineCode;
    /** 发行卡BIN [10,0] */
    private Integer binNo;
    /** 还款优先级，数值越小优先级越高 [10,0] */
    private Integer paymentPriority;
    /** 产品单元代码 [18,0] Not NULL */
    private String productUnitCode;
    /** 联名号 [20,0] */
    private String coBrandedNo;
    /** 状态 1：正常,8：关闭 [1,0] */
    private String statusCode;
    //申请日期
    private String createDate;
    //注销日期
    private String productCancelDate;
    /** 产品对象代码 [9,0] Not NULL 产品升降级所选新的产品对象*/
    private String productObjectCodeNew;
    protected String id;
    /** 证件类型 */
    private String idType;
    /** 证件号码 */
    private String idNumber;

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

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getOperationMode() {
		return operationMode;
	}

	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}

	public String getProductLineCode() {
		return productLineCode;
	}

	public void setProductLineCode(String productLineCode) {
		this.productLineCode = productLineCode;
	}

	public Integer getBinNo() {
		return binNo;
	}

	public void setBinNo(Integer binNo) {
		this.binNo = binNo;
	}

	public Integer getPaymentPriority() {
		return paymentPriority;
	}

	public void setPaymentPriority(Integer paymentPriority) {
		this.paymentPriority = paymentPriority;
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

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getProductCancelDate() {
		return productCancelDate;
	}

	public void setProductCancelDate(String productCancelDate) {
		this.productCancelDate = productCancelDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductObjectCodeNew() {
		return productObjectCodeNew;
	}

	public void setProductObjectCodeNew(String productObjectCodeNew) {
		this.productObjectCodeNew = productObjectCodeNew;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

  
}
