package com.tansun.ider.model.bo;

import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.commun.BeanVO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: PanQi
 * @Date: 2020/04/03
 * @updater:
 * @description: 资产选择账户查询
 */
public class X5975BO extends BeanVO implements Serializable {

	/** 全局事件编号 */
	private String globalEventNo;
	private String credentialNumber;
	private String externalIdentificationNo;
	private String idNumber;
	private String idType;

	/** 事件编号 [14,0] Not NULL */
	protected String eventNo;
	/** 活动编号 [8,0] Not NULL */
	protected String activityNo;
	/** 业务项目 [9,0] */
	protected String businessProgramNo;
	/** 产品对象代码 [15,0] */
	protected String productObjectCode;
	/** 所属业务类型 [15,0] */
	protected String businessTypeCode;
	/** 客户代码，外键关联客户主表主键 [20,0] Not NULL */
	protected String customerNo;
	/** 所属机构号 [10,0] Not NULL */
	protected String organNo;
	/**  运营模式 [3,0] Not NULL */
	protected String operationMode;
	/** 创建时间 yyyy-MM-dd HH:mm:ss [23,0] */
	protected String gmtCreate;
	/** 时间戳 : oralce使用触发器更新， mysql使用自动更新 [19,0] Not NULL */
	protected Date timestamp;
	/** 版本号 [10,0] Not NULL */
	protected Integer version;
	/** 转让方式 */
	protected String type;

	private String flag;
	private String payFlag;
	private String accFlag;
	private String accountId;
	private String accountOrganForm;
	private String currencyCode;
	private String pageFlag;
	private String globalTransSerialNo;
	/** 活动与构件对应关系表 */
	CoreEventActivityRel coreEventActivityRel;
	/** 活动与构件对应关系表 */
	List<CoreActivityArtifactRel> activityArtifactList;
	/** 账户类型 R-循环账户 T-交易账户 B-不良资产 [1,0] */
	private String accountType;

	public String getGlobalEventNo() {
		return globalEventNo;
	}

	public void setGlobalEventNo(String globalEventNo) {
		this.globalEventNo = globalEventNo;
	}

	public String getCredentialNumber() {
		return credentialNumber;
	}

	public void setCredentialNumber(String credentialNumber) {
		this.credentialNumber = credentialNumber;
	}

	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}

	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
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

	public String getBusinessProgramNo() {
		return businessProgramNo;
	}

	public void setBusinessProgramNo(String businessProgramNo) {
		this.businessProgramNo = businessProgramNo;
	}

	public String getProductObjectCode() {
		return productObjectCode;
	}

	public void setProductObjectCode(String productObjectCode) {
		this.productObjectCode = productObjectCode;
	}

	public String getBusinessTypeCode() {
		return businessTypeCode;
	}

	public void setBusinessTypeCode(String businessTypeCode) {
		this.businessTypeCode = businessTypeCode;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getOrganNo() {
		return organNo;
	}

	public void setOrganNo(String organNo) {
		this.organNo = organNo;
	}

	public String getOperationMode() {
		return operationMode;
	}

	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getPayFlag() {
		return payFlag;
	}

	public void setPayFlag(String payFlag) {
		this.payFlag = payFlag;
	}

	public String getAccFlag() {
		return accFlag;
	}

	public void setAccFlag(String accFlag) {
		this.accFlag = accFlag;
	}

	public CoreEventActivityRel getCoreEventActivityRel() {
		return coreEventActivityRel;
	}

	public void setCoreEventActivityRel(CoreEventActivityRel coreEventActivityRel) {
		this.coreEventActivityRel = coreEventActivityRel;
	}

	public List<CoreActivityArtifactRel> getActivityArtifactList() {
		return activityArtifactList;
	}

	public void setActivityArtifactList(List<CoreActivityArtifactRel> activityArtifactList) {
		this.activityArtifactList = activityArtifactList;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountOrganForm() {
		return accountOrganForm;
	}

	public void setAccountOrganForm(String accountOrganForm) {
		this.accountOrganForm = accountOrganForm;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getPageFlag() {
		return pageFlag;
	}

	public void setPageFlag(String pageFlag) {
		this.pageFlag = pageFlag;
	}

	public String getGlobalTransSerialNo() {
		return globalTransSerialNo;
	}

	public void setGlobalTransSerialNo(String globalTransSerialNo) {
		this.globalTransSerialNo = globalTransSerialNo;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	@Override
	public String toString() {
		return "X5975BO{" +
				"globalEventNo='" + globalEventNo + '\'' +
				", credentialNumber='" + credentialNumber + '\'' +
				", externalIdentificationNo='" + externalIdentificationNo + '\'' +
				", idNumber='" + idNumber + '\'' +
				", idType='" + idType + '\'' +
				", eventNo='" + eventNo + '\'' +
				", activityNo='" + activityNo + '\'' +
				", businessProgramNo='" + businessProgramNo + '\'' +
				", productObjectCode='" + productObjectCode + '\'' +
				", businessTypeCode='" + businessTypeCode + '\'' +
				", customerNo='" + customerNo + '\'' +
				", organNo='" + organNo + '\'' +
				", operationMode='" + operationMode + '\'' +
				", gmtCreate='" + gmtCreate + '\'' +
				", timestamp=" + timestamp +
				", version=" + version +
				", type='" + type + '\'' +
				", flag='" + flag + '\'' +
				", payFlag='" + payFlag + '\'' +
				", accFlag='" + accFlag + '\'' +
				", accountId='" + accountId + '\'' +
				", accountOrganForm='" + accountOrganForm + '\'' +
				", currencyCode='" + currencyCode + '\'' +
				", pageFlag='" + pageFlag + '\'' +
				", globalTransSerialNo='" + globalTransSerialNo + '\'' +
				", coreEventActivityRel=" + coreEventActivityRel +
				", activityArtifactList=" + activityArtifactList +
				", accountType='" + accountType + '\'' +
				'}';
	}
}
