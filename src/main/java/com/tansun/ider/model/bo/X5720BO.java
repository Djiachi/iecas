package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.commun.BeanVO;

public class X5720BO extends BeanVO implements Serializable {
	/**
	 * @Field @serialVersionUID : TODO(这里用一句话描述这个类的作用)
	 */
	private static final long serialVersionUID = -6336360462690647613L;
	/** 活动与构件对应关系表 */
	CoreEventActivityRel coreEventActivityRel;
	/** 活动与构件对应关系表 */
	List<CoreActivityArtifactRel> activityArtifactList;
	 /** 事件编号 [14,0] Not NULL */
	private String eventNo;
    /** 活动编号 [8,0] Not NULL */
	private String activityNo;
	/* 创建一个全局流水号 */
	private String globalEventNo;
	/** 操作员Id */
	private String operatorId;
    /** MTI [4,0] */
    private String mti;
    /** 外部识别号 [32,0] */
    private String externalIdentificationNo;
    /** 处理码 [6,0] */
    private String processingCode;
    /** 交易金额 [18,0] */
    private BigDecimal transAmt;
    /** DE12 [12,0] */
    private String dateAndTime;
    /** DE14 [4,0] */
    private String dateExpiration;
    /** DE22 [12,0] */
    private String posEntryMode;
    /** 功能码 [3,0] */
    private String functionCode;
    /** 消息原因代码 [4,0] */
    private String messageReasonCode;
    /** MCC [4,0] */
    private String mcc;
    /** 原始金额 [12,0] */
    private String originalAmountReconciliation;
    /** 收单机构参考号 [23,0] */
    private String acquireReferenceNumbr;
    /** 受理机构识别码 [11,0] */
    private String acqId;
    /** DE33 [11,0] */
    private String forwdInstitIdCode;
    /** DE37 [12,0] */
    private String retrievalReferNum;
    /** 授权成功码 [6,0] */
    private String authApprovalCde;
    /** DE40 [3,0] */
    private String serviceCode;
    /** 受卡机终端标识码 [8,0] */
    private String terminateIdentNo;
    /** 商户代码 [15,0] */
    private String merchantCde;
    /** DE43 [99,0] */
    private String cardAcceptorName;
    /** 附加数据 [999,0] */
    private String additionalData;
    /** 终端类型 [3,0] */
    private String terminalType;
    /** 撤销标志 [1,0] */
    private String messageReversalIndicator;
    /** 电子商务安全等级指标 [3,0] */
    private String electronicCommerceSecurityLevelIndicator;
    /** 币种小数位 [1,0] Not NULL */
    private String currencyExponents;
    /** 交易币种 [3,0] */
    private String currencyCodesAmountsOriginal;
    /** 业务活动 [29,0] */
    private String businessActivity;
    /** 结算指标 [1,0] */
    private String settlementIndicator;
    /** 主卡id [6,0] */
    private String mastercardAssignedId;
    /** 检索文档码 [1,0] */
    private String retrievalDocumentCode;
    /** S1排除请求代码 [2,0] */
    private String exclusionRequestCode;
    /** 文件标识 [1,0] */
    private String documentationIndicator;
    /** 额外金额 [120,0] */
    private String amountsAdditional;
    /** DE63 [16,0] */
    private String transLifeCycleId;
    /** DE71 [8,0] */
    private String messageNumber;
    /** 数据记录 [999,0] */
    private String dataRecord;
    /** DE94 [11,0] */
    private String transOriginInstit;
    /** 发卡机构参考数据 [10,0] */
    private String cardIssuerReferenceData;
    /** 原交易全局流水号 [32,0] */
    private String oldGlobalSerialNumbr;
    /** 调单状态 N-新增C-确认O-已经发出 [1,0] */
    private String retrievalStatus;
    /** 拒付状态 N-新增C-确认O-已经发出 [1,0] */
    private String protestStatus;
    /** 客户号 [12,0] */
    private String customerNo;
	/** 当前日志标志   A/B [1,0] */
	private String currLogFlag;
	
	private String paramLogEventId;
	private String paramLogActivityId;
	private String paramLogLevel;
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
	public String getGlobalEventNo() {
		return globalEventNo;
	}
	public void setGlobalEventNo(String globalEventNo) {
		this.globalEventNo = globalEventNo;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getMti() {
		return mti;
	}
	public void setMti(String mti) {
		this.mti = mti;
	}
	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}
	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}
	public String getProcessingCode() {
		return processingCode;
	}
	public void setProcessingCode(String processingCode) {
		this.processingCode = processingCode;
	}
	public BigDecimal getTransAmt() {
		return transAmt;
	}
	public void setTransAmt(BigDecimal transAmt) {
		this.transAmt = transAmt;
	}
	public String getDateAndTime() {
		return dateAndTime;
	}
	public void setDateAndTime(String dateAndTime) {
		this.dateAndTime = dateAndTime;
	}
	public String getDateExpiration() {
		return dateExpiration;
	}
	public void setDateExpiration(String dateExpiration) {
		this.dateExpiration = dateExpiration;
	}
	public String getPosEntryMode() {
		return posEntryMode;
	}
	public void setPosEntryMode(String posEntryMode) {
		this.posEntryMode = posEntryMode;
	}
	public String getFunctionCode() {
		return functionCode;
	}
	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}
	public String getMessageReasonCode() {
		return messageReasonCode;
	}
	public void setMessageReasonCode(String messageReasonCode) {
		this.messageReasonCode = messageReasonCode;
	}
	public String getMcc() {
		return mcc;
	}
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}
	public String getOriginalAmountReconciliation() {
		return originalAmountReconciliation;
	}
	public void setOriginalAmountReconciliation(String originalAmountReconciliation) {
		this.originalAmountReconciliation = originalAmountReconciliation;
	}
	public String getAcquireReferenceNumbr() {
		return acquireReferenceNumbr;
	}
	public void setAcquireReferenceNumbr(String acquireReferenceNumbr) {
		this.acquireReferenceNumbr = acquireReferenceNumbr;
	}
	public String getAcqId() {
		return acqId;
	}
	public void setAcqId(String acqId) {
		this.acqId = acqId;
	}
	public String getForwdInstitIdCode() {
		return forwdInstitIdCode;
	}
	public void setForwdInstitIdCode(String forwdInstitIdCode) {
		this.forwdInstitIdCode = forwdInstitIdCode;
	}
	public String getRetrievalReferNum() {
		return retrievalReferNum;
	}
	public void setRetrievalReferNum(String retrievalReferNum) {
		this.retrievalReferNum = retrievalReferNum;
	}
	public String getAuthApprovalCde() {
		return authApprovalCde;
	}
	public void setAuthApprovalCde(String authApprovalCde) {
		this.authApprovalCde = authApprovalCde;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getTerminateIdentNo() {
		return terminateIdentNo;
	}
	public void setTerminateIdentNo(String terminateIdentNo) {
		this.terminateIdentNo = terminateIdentNo;
	}
	public String getMerchantCde() {
		return merchantCde;
	}
	public void setMerchantCde(String merchantCde) {
		this.merchantCde = merchantCde;
	}
	public String getCardAcceptorName() {
		return cardAcceptorName;
	}
	public void setCardAcceptorName(String cardAcceptorName) {
		this.cardAcceptorName = cardAcceptorName;
	}
	public String getAdditionalData() {
		return additionalData;
	}
	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}
	public String getTerminalType() {
		return terminalType;
	}
	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}
	public String getMessageReversalIndicator() {
		return messageReversalIndicator;
	}
	public void setMessageReversalIndicator(String messageReversalIndicator) {
		this.messageReversalIndicator = messageReversalIndicator;
	}
	public String getElectronicCommerceSecurityLevelIndicator() {
		return electronicCommerceSecurityLevelIndicator;
	}
	public void setElectronicCommerceSecurityLevelIndicator(String electronicCommerceSecurityLevelIndicator) {
		this.electronicCommerceSecurityLevelIndicator = electronicCommerceSecurityLevelIndicator;
	}
	public String getCurrencyExponents() {
		return currencyExponents;
	}
	public void setCurrencyExponents(String currencyExponents) {
		this.currencyExponents = currencyExponents;
	}
	public String getCurrencyCodesAmountsOriginal() {
		return currencyCodesAmountsOriginal;
	}
	public void setCurrencyCodesAmountsOriginal(String currencyCodesAmountsOriginal) {
		this.currencyCodesAmountsOriginal = currencyCodesAmountsOriginal;
	}
	public String getBusinessActivity() {
		return businessActivity;
	}
	public void setBusinessActivity(String businessActivity) {
		this.businessActivity = businessActivity;
	}
	public String getSettlementIndicator() {
		return settlementIndicator;
	}
	public void setSettlementIndicator(String settlementIndicator) {
		this.settlementIndicator = settlementIndicator;
	}
	public String getMastercardAssignedId() {
		return mastercardAssignedId;
	}
	public void setMastercardAssignedId(String mastercardAssignedId) {
		this.mastercardAssignedId = mastercardAssignedId;
	}
	public String getRetrievalDocumentCode() {
		return retrievalDocumentCode;
	}
	public void setRetrievalDocumentCode(String retrievalDocumentCode) {
		this.retrievalDocumentCode = retrievalDocumentCode;
	}
	public String getExclusionRequestCode() {
		return exclusionRequestCode;
	}
	public void setExclusionRequestCode(String exclusionRequestCode) {
		this.exclusionRequestCode = exclusionRequestCode;
	}
	public String getDocumentationIndicator() {
		return documentationIndicator;
	}
	public void setDocumentationIndicator(String documentationIndicator) {
		this.documentationIndicator = documentationIndicator;
	}
	public String getAmountsAdditional() {
		return amountsAdditional;
	}
	public void setAmountsAdditional(String amountsAdditional) {
		this.amountsAdditional = amountsAdditional;
	}
	public String getTransLifeCycleId() {
		return transLifeCycleId;
	}
	public void setTransLifeCycleId(String transLifeCycleId) {
		this.transLifeCycleId = transLifeCycleId;
	}
	public String getMessageNumber() {
		return messageNumber;
	}
	public void setMessageNumber(String messageNumber) {
		this.messageNumber = messageNumber;
	}
	public String getDataRecord() {
		return dataRecord;
	}
	public void setDataRecord(String dataRecord) {
		this.dataRecord = dataRecord;
	}
	public String getTransOriginInstit() {
		return transOriginInstit;
	}
	public void setTransOriginInstit(String transOriginInstit) {
		this.transOriginInstit = transOriginInstit;
	}
	public String getCardIssuerReferenceData() {
		return cardIssuerReferenceData;
	}
	public void setCardIssuerReferenceData(String cardIssuerReferenceData) {
		this.cardIssuerReferenceData = cardIssuerReferenceData;
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
	public String getProtestStatus() {
		return protestStatus;
	}
	public void setProtestStatus(String protestStatus) {
		this.protestStatus = protestStatus;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getCurrLogFlag() {
		return currLogFlag;
	}
	public void setCurrLogFlag(String currLogFlag) {
		this.currLogFlag = currLogFlag;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
