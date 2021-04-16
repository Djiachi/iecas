package com.tansun.ider.model.bo;

import com.tansun.ider.dao.issue.entity.CoreAccount;
import com.tansun.ider.framwork.commun.BeanVO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Desc:账户基本信息查询 查询账户基本信息
 * @Author wt
 * @Date 2018年4月28日 下午2:43:42
 */
public class X5982BO extends BeanVO implements Serializable {

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

	/** 账户代码 [23,0] Not NULL */
	protected String accountId;
	/** 币种 [3,0] Not NULL */
	protected String currencyCode;
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
	/** 资产转变方式:ABS-证券化 [3,0] */
	protected String capitalType;
	/** 资产转变阶段:PACK-封包,TRSF-转让,REPO-回购 [4,0] */
	protected String capitalStage;
	/** 资产转出机构代码 [25,0] */
	protected String capitalOrganizationCode;
	/** 资产转出机构名称 [120,0] */
	protected String capitalOrganizationName;
	/** 宽限日 [10,0] */
	protected String graceDate;
	/** 当前余额 [18,0] */
	protected BigDecimal newBalance;
	/** 创建时间 yyyy-MM-dd HH:mm:ss [23,0] */
	protected String gmtCreate;
	/** 时间戳 : oralce使用触发器更新， mysql使用自动更新 [19,0] Not NULL */
	protected Date timestamp;
	/** 版本号 [10,0] Not NULL */
	protected Integer version;
	/** 转让方式 */
	protected String type;

	private String globalSerialNumbr;
	protected List<CoreAccount> accountList;

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

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
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

	public String getCapitalType() {
		return capitalType;
	}

	public void setCapitalType(String capitalType) {
		this.capitalType = capitalType;
	}

	public String getCapitalStage() {
		return capitalStage;
	}

	public void setCapitalStage(String capitalStage) {
		this.capitalStage = capitalStage;
	}

	public String getCapitalOrganizationCode() {
		return capitalOrganizationCode;
	}

	public void setCapitalOrganizationCode(String capitalOrganizationCode) {
		this.capitalOrganizationCode = capitalOrganizationCode;
	}

	public String getCapitalOrganizationName() {
		return capitalOrganizationName;
	}

	public void setCapitalOrganizationName(String capitalOrganizationName) {
		this.capitalOrganizationName = capitalOrganizationName;
	}

	public String getGraceDate() {
		return graceDate;
	}

	public void setGraceDate(String graceDate) {
		this.graceDate = graceDate;
	}

	public BigDecimal getNewBalance() {
		return newBalance;
	}

	public void setNewBalance(BigDecimal newBalance) {
		this.newBalance = newBalance;
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

	public String getGlobalSerialNumbr() {
		return globalSerialNumbr;
	}

	public void setGlobalSerialNumbr(String globalSerialNumbr) {
		this.globalSerialNumbr = globalSerialNumbr;
	}

	public List<CoreAccount> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<CoreAccount> accountList) {
		this.accountList = accountList;
	}

	@Override
	public String toString() {
		return "X5982BO{" +
				"globalEventNo='" + globalEventNo + '\'' +
				", credentialNumber='" + credentialNumber + '\'' +
				", externalIdentificationNo='" + externalIdentificationNo + '\'' +
				", idNumber='" + idNumber + '\'' +
				", idType='" + idType + '\'' +
				", eventNo='" + eventNo + '\'' +
				", activityNo='" + activityNo + '\'' +
				", accountId='" + accountId + '\'' +
				", currencyCode='" + currencyCode + '\'' +
				", businessProgramNo='" + businessProgramNo + '\'' +
				", productObjectCode='" + productObjectCode + '\'' +
				", businessTypeCode='" + businessTypeCode + '\'' +
				", customerNo='" + customerNo + '\'' +
				", organNo='" + organNo + '\'' +
				", operationMode='" + operationMode + '\'' +
				", capitalType='" + capitalType + '\'' +
				", capitalStage='" + capitalStage + '\'' +
				", capitalOrganizationCode='" + capitalOrganizationCode + '\'' +
				", capitalOrganizationName='" + capitalOrganizationName + '\'' +
				", graceDate='" + graceDate + '\'' +
				", newBalance=" + newBalance +
				", gmtCreate='" + gmtCreate + '\'' +
				", timestamp=" + timestamp +
				", version=" + version +
				", type='" + type + '\'' +
				", globalSerialNumbr='" + globalSerialNumbr + '\'' +
				", accountList=" + accountList +
				'}';
	}
}
