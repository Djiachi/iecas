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
public class X5760BO extends BeanVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4049091273302572998L;
	/** 活动与构件对应关系表 */
	CoreEventActivityRel coreEventActivityRel;
	/** 活动与构件对应关系表 */
	List<CoreActivityArtifactRel> activityArtifactList;
    /** 活动编号 [8,0] Not NULL */
    private String activityNo;
	/* 创建一个全局流水号 */
	private String globalEventNo;
    /** 客户号 [12,0] */
    private String customerNo;
    /** 交易代码 [2,0] */
    private String transactionCode;
    /** 帐号 [32,0] */
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
    /** 拒付状态 N-新增C-确认O-已经发出 [1,0] */
    private String protestStatus;
    /** 特殊退单标识 [1,0] */
    private String specialChargebackIndicator;
    /** 文档标识 [1,0] */
    private String documentationIndicator;
    /** 原因码 [2,0] */
    private String reasonCode;
    /** 退单参考号 [6,0] */
    private String chargebackReferenceNumber;
    /** 消息文本 [50,0] */
    private String messageText;
    /** 请求的付款服务 [1,0] */
    private String requestedPaymentService;
    /** 授权特征标识 [1,0] */
    private String authorizationCharacteristicsIndicator;
    /** 国际费用标识 [1,0] */
    private String internationalFeeIndicator;
    /** 费用计划指标 [3,0] */
    private String feeProgramIndicator;
    /** 邮件电话电子商务和支付指标 [1,0] */
    private String mailPhoneElectronicCommerceAndPaymentIndicator;
    /** 预付卡标识 [1,0] */
    private String prepaidCardIndicator;
    /** 授权金额 [18,0] */
    private BigDecimal authorizedAmount;
    /** 授权货币代码 [3,0] */
    private String authorizationCurrencyCode;
    /** 授权响应代码 [2,0] */
    private String authorizationResponseCode;
    /** 多笔清算序列号 [2,0] */
    private String multipleClearingSequenceNumber;
    /** 多笔清算序列数 [2,0] */
    private String multipleClearingSequenceCount;
    /** 动态货币兑换DCC指标 [1,0] */
    private String dynamicCurrencyConversionIndicator;
    /** 终端标识 [8,0] */
    private String terminateIdentNo;
    /** 事件编号 */
	private String eventNo;
	/** 当前日志标志   A/B [1,0] */
	private String currLogFlag;
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
	private String paramLogEventId;
	private String paramLogActivityId;
	private String paramLogLevel;
	private String operatorId;
	
    /** 交易代码限定符 [1,0] */
    protected String transCodeQualifer;
    /** 授权成功码 [6,0] */
    protected String authApprovalCde;
    /** 服务点输入方式码 [2,0] */
    protected String servicePointInputModeCode;
    /** 终端读取能力取值 [2,0] */
    protected String terminalReadabilityValue;
    /** 商户代码 [15,0] */
    protected String merchantCde;
	
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
	public String getProtestStatus() {
		return protestStatus;
	}
	public void setProtestStatus(String protestStatus) {
		this.protestStatus = protestStatus;
	}
	public String getSpecialChargebackIndicator() {
		return specialChargebackIndicator;
	}
	public void setSpecialChargebackIndicator(String specialChargebackIndicator) {
		this.specialChargebackIndicator = specialChargebackIndicator;
	}
	public String getDocumentationIndicator() {
		return documentationIndicator;
	}
	public void setDocumentationIndicator(String documentationIndicator) {
		this.documentationIndicator = documentationIndicator;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getChargebackReferenceNumber() {
		return chargebackReferenceNumber;
	}
	public void setChargebackReferenceNumber(String chargebackReferenceNumber) {
		this.chargebackReferenceNumber = chargebackReferenceNumber;
	}
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public String getRequestedPaymentService() {
		return requestedPaymentService;
	}
	public void setRequestedPaymentService(String requestedPaymentService) {
		this.requestedPaymentService = requestedPaymentService;
	}
	public String getAuthorizationCharacteristicsIndicator() {
		return authorizationCharacteristicsIndicator;
	}
	public void setAuthorizationCharacteristicsIndicator(String authorizationCharacteristicsIndicator) {
		this.authorizationCharacteristicsIndicator = authorizationCharacteristicsIndicator;
	}
	public String getInternationalFeeIndicator() {
		return internationalFeeIndicator;
	}
	public void setInternationalFeeIndicator(String internationalFeeIndicator) {
		this.internationalFeeIndicator = internationalFeeIndicator;
	}
	public String getFeeProgramIndicator() {
		return feeProgramIndicator;
	}
	public void setFeeProgramIndicator(String feeProgramIndicator) {
		this.feeProgramIndicator = feeProgramIndicator;
	}
	public String getMailPhoneElectronicCommerceAndPaymentIndicator() {
		return mailPhoneElectronicCommerceAndPaymentIndicator;
	}
	public void setMailPhoneElectronicCommerceAndPaymentIndicator(String mailPhoneElectronicCommerceAndPaymentIndicator) {
		this.mailPhoneElectronicCommerceAndPaymentIndicator = mailPhoneElectronicCommerceAndPaymentIndicator;
	}
	public String getPrepaidCardIndicator() {
		return prepaidCardIndicator;
	}
	public void setPrepaidCardIndicator(String prepaidCardIndicator) {
		this.prepaidCardIndicator = prepaidCardIndicator;
	}
	public BigDecimal getAuthorizedAmount() {
		return authorizedAmount;
	}
	public void setAuthorizedAmount(BigDecimal authorizedAmount) {
		this.authorizedAmount = authorizedAmount;
	}
	public String getAuthorizationCurrencyCode() {
		return authorizationCurrencyCode;
	}
	public void setAuthorizationCurrencyCode(String authorizationCurrencyCode) {
		this.authorizationCurrencyCode = authorizationCurrencyCode;
	}
	public String getAuthorizationResponseCode() {
		return authorizationResponseCode;
	}
	public void setAuthorizationResponseCode(String authorizationResponseCode) {
		this.authorizationResponseCode = authorizationResponseCode;
	}
	public String getMultipleClearingSequenceNumber() {
		return multipleClearingSequenceNumber;
	}
	public void setMultipleClearingSequenceNumber(String multipleClearingSequenceNumber) {
		this.multipleClearingSequenceNumber = multipleClearingSequenceNumber;
	}
	public String getMultipleClearingSequenceCount() {
		return multipleClearingSequenceCount;
	}
	public void setMultipleClearingSequenceCount(String multipleClearingSequenceCount) {
		this.multipleClearingSequenceCount = multipleClearingSequenceCount;
	}
	public String getDynamicCurrencyConversionIndicator() {
		return dynamicCurrencyConversionIndicator;
	}
	public void setDynamicCurrencyConversionIndicator(String dynamicCurrencyConversionIndicator) {
		this.dynamicCurrencyConversionIndicator = dynamicCurrencyConversionIndicator;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getTerminateIdentNo() {
		return terminateIdentNo;
	}
	public void setTerminateIdentNo(String terminateIdentNo) {
		this.terminateIdentNo = terminateIdentNo;
	}
	public String getTransCodeQualifer() {
		return transCodeQualifer;
	}
	public void setTransCodeQualifer(String transCodeQualifer) {
		this.transCodeQualifer = transCodeQualifer;
	}
	public String getAuthApprovalCde() {
		return authApprovalCde;
	}
	public void setAuthApprovalCde(String authApprovalCde) {
		this.authApprovalCde = authApprovalCde;
	}
	public String getServicePointInputModeCode() {
		return servicePointInputModeCode;
	}
	public void setServicePointInputModeCode(String servicePointInputModeCode) {
		this.servicePointInputModeCode = servicePointInputModeCode;
	}
	public String getTerminalReadabilityValue() {
		return terminalReadabilityValue;
	}
	public void setTerminalReadabilityValue(String terminalReadabilityValue) {
		this.terminalReadabilityValue = terminalReadabilityValue;
	}
	public String getMerchantCde() {
		return merchantCde;
	}
	public void setMerchantCde(String merchantCde) {
		this.merchantCde = merchantCde;
	}
}