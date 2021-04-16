package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.commun.BeanVO;
/**
 * VISA调单申请建立
 * 
 * @ClassName X5705BO
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author yanyingzhao
 * @Date 2019年1月253日 下午5:36:09
 * @version 1.0.0
 */
public class X5705BO extends BeanVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -322108030635184878L;
	/** 活动与构件对应关系表 */
	CoreEventActivityRel coreEventActivityRel;
	/** 活动与构件对应关系表 */
	List<CoreActivityArtifactRel> activityArtifactList;
    /** 活动编号 [8,0] Not NULL */
    protected String activityNo;
	/* 创建一个全局流水号 */
	private String globalEventNo;
    /** 客户号 [12,0] */
	private String customerNo;
    /** 交易代码 [2,0] */
	private String transactionCode;
    /** 帐号 [19,0] */
	private String externalIdentificationNo;
    /** 收单机构参考号 [23,0] */
	private String acquireReferenceNumbr;
    /** 收单业务编号 [8,0] */
	private String acquirerBusinessId;
    /** 购买日期 [10,0] */
	private String gmtDate;
    /** 交易金额 [18,0] */
	private BigDecimal transAmt;
    /** 交易货币代码 [3,0] */
	private String transCurrCode;
    /** 商户名称 [25,0] */
	private String merchantName;
    /** 商城 [30,0] */
	private String city;
    /** 商人国家代码 [30,0] */
	private String countryCode;
    /** 商家类别代码 [15,0] */
	private String mcc;
    /** 美国商户邮政编码 [10,0] */
	private String merchantZip;
    /** 商户国家╱省代码 [6,0] */
	private String stateCode;
    /** 发卡机构控制号 [9,0] */
	private String issuerControlNumber;
    /** 请求原因代码 [2,0] */
	private String requestReasonCode;
    /** 结算标志 [1,0] */
	private String settlementFlag;
    /** 国家偿还费 [18,0] */
	private BigDecimal nationalReimburseFee;
    /** 账户种类 [1,0] */
	private String accountSelection;
    /** 调单请求ID [12,0] */
	private String retrievalRequestId;
    /** 中央处理日期 yydd [10,0] */
	private String settlementDate;
    /** 偿还属性 [1,0] */
	private String reimburseAttribute;
    /** 传真号码 [16,0] */
	private String faxNumber;
    /** 请求完成方式 0-人工 1-自动 [1,0] */
	private String requestedFulfllmentMethod;
    /** 已建立的完成方式 0-人工 1-自动 [1,0] */
	private String establishedFulfllmentMethod;
    /** 发卡机构清算币种 [3,0] */
	private String clearCurrCode;
    /** 发卡机构清算金额 [18,0] */
	private BigDecimal clearAmount;
    /** 交易ID [15,0] */
	private String transIdentifier;
    /** 原交易全局流水号 [32,0] */
	private String oldGlobalSerialNumbr;
    /** 调单状态 N-新增C-确认O-已经发出 [1,0] */
	private String retrievalStatus;
	/** 证件号码 */
	private String idNumber;
	/** 证件类型 */
	private String idType;
	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	/** 事件编号 [14,0] Not NULL */
	private String eventNo;
	/** 当前日志标志   A/B [1,0] */
	private String currLogFlag;
	
	private String paramLogEventId;
	private String paramLogActivityId;
	private String paramLogLevel;
	private String operatorId;
    
	@Override
	public String toString() {
		return "X5705BO [globalEventNo=" + globalEventNo +", customerNo=" + customerNo + ", transactionCode=" + transactionCode + "]";
	}

	public String getGlobalEventNo() {
		return globalEventNo;
	}

	public void setGlobalEventNo(String globalEventNo) {
		this.globalEventNo = globalEventNo;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}

	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}

	public String getAcquireReferenceNumbr() {
		return acquireReferenceNumbr;
	}

	public void setAcquireReferenceNumbr(String acquireReferenceNumbr) {
		this.acquireReferenceNumbr = acquireReferenceNumbr;
	}

	public String getAcquirerBusinessId() {
		return acquirerBusinessId;
	}

	public void setAcquirerBusinessId(String acquirerBusinessId) {
		this.acquirerBusinessId = acquirerBusinessId;
	}

	public String getGmtDate() {
		return gmtDate;
	}

	public void setGmtDate(String gmtDate) {
		this.gmtDate = gmtDate;
	}

	public BigDecimal getTransAmt() {
		return transAmt;
	}

	public void setTransAmt(BigDecimal transAmt) {
		this.transAmt = transAmt;
	}

	public String getTransCurrCode() {
		return transCurrCode;
	}

	public void setTransCurrCode(String transCurrCode) {
		this.transCurrCode = transCurrCode;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getMerchantZip() {
		return merchantZip;
	}

	public void setMerchantZip(String merchantZip) {
		this.merchantZip = merchantZip;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getIssuerControlNumber() {
		return issuerControlNumber;
	}

	public void setIssuerControlNumber(String issuerControlNumber) {
		this.issuerControlNumber = issuerControlNumber;
	}

	public String getRequestReasonCode() {
		return requestReasonCode;
	}

	public void setRequestReasonCode(String requestReasonCode) {
		this.requestReasonCode = requestReasonCode;
	}

	public String getSettlementFlag() {
		return settlementFlag;
	}

	public void setSettlementFlag(String settlementFlag) {
		this.settlementFlag = settlementFlag;
	}

	public BigDecimal getNationalReimburseFee() {
		return nationalReimburseFee;
	}

	public void setNationalReimburseFee(BigDecimal nationalReimburseFee) {
		this.nationalReimburseFee = nationalReimburseFee;
	}

	public String getAccountSelection() {
		return accountSelection;
	}

	public void setAccountSelection(String accountSelection) {
		this.accountSelection = accountSelection;
	}

	public String getRetrievalRequestId() {
		return retrievalRequestId;
	}

	public void setRetrievalRequestId(String retrievalRequestId) {
		this.retrievalRequestId = retrievalRequestId;
	}

	public String getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(String settlementDate) {
		this.settlementDate = settlementDate;
	}

	public String getReimburseAttribute() {
		return reimburseAttribute;
	}

	public void setReimburseAttribute(String reimburseAttribute) {
		this.reimburseAttribute = reimburseAttribute;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getRequestedFulfllmentMethod() {
		return requestedFulfllmentMethod;
	}

	public void setRequestedFulfllmentMethod(String requestedFulfllmentMethod) {
		this.requestedFulfllmentMethod = requestedFulfllmentMethod;
	}

	public String getEstablishedFulfllmentMethod() {
		return establishedFulfllmentMethod;
	}

	public void setEstablishedFulfllmentMethod(String establishedFulfllmentMethod) {
		this.establishedFulfllmentMethod = establishedFulfllmentMethod;
	}

	public String getClearCurrCode() {
		return clearCurrCode;
	}

	public void setClearCurrCode(String clearCurrCode) {
		this.clearCurrCode = clearCurrCode;
	}

	public BigDecimal getClearAmount() {
		return clearAmount;
	}

	public void setClearAmount(BigDecimal clearAmount) {
		this.clearAmount = clearAmount;
	}

	public String getTransIdentifier() {
		return transIdentifier;
	}

	public void setTransIdentifier(String transIdentifier) {
		this.transIdentifier = transIdentifier;
	}

	public String getOldGlobalSerialNumbr() {
		return oldGlobalSerialNumbr;
	}

	public void setOldGlobalSerialNumbr(String oldGlobalSerialNumbr) {
		this.oldGlobalSerialNumbr = oldGlobalSerialNumbr;
	}

	public String getRetrievalStatus() {
		return retrievalStatus;
	}

	public void setRetrievalStatus(String retrievalStatus) {
		this.retrievalStatus = retrievalStatus;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getParamLogEventId() {
		return paramLogEventId;
	}

	public void setParamLogEventId(String paramLogEventId) {
		this.paramLogEventId = paramLogEventId;
	}

	public String getParamLogActivityId() {
		return paramLogActivityId;
	}

	public void setParamLogActivityId(String paramLogActivityId) {
		this.paramLogActivityId = paramLogActivityId;
	}

	public String getParamLogLevel() {
		return paramLogLevel;
	}

	public void setParamLogLevel(String paramLogLevel) {
		this.paramLogLevel = paramLogLevel;
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
	
}