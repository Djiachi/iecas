package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.tansun.ider.framwork.commun.BeanVO;
import com.tansun.ider.service.business.EventCommArea;

/**
 * 	附加表信息查询	
 * 
 * @author yanyingzhao 2019年1月24日
 */
public class X5735BO extends BeanVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7934471632846481672L;
    /** 全局流水号 [32,0] Not NULL */
	private String globalSerialNumber;
    /** 外部识别号 [32,0] Not NULL */
	private String externalIdentificationNo;
    /** DE22 [12,0] */
	private String posEntryMode;
    /** DE33 [11,0] */
	private String forwdInstitIdCode;
    /** DE37 [12,0] */
	private String retrievalReferNum;
    /** DE48.P0165 [30,0] */
	private String settlementIndicator;
    /** DE63 [16,0] */
	private String transLifeCycleId;
    /** DE71 [8,0] */
	private String messageNumber;
    /** DE94 [11,0] */
	private String transOriginInstit;
    /** 收购人的业务 编号 [8,0] */
	private String acquirerBusinessId;
    /** 国家偿还费 [18,0] */
	private BigDecimal nationalReimburseFee;
    /** 账户选择 [1,0] */
	private String accountSelection;
    /** 偿还属性 [1,0] */
	private String reimburseAttribute;
    /** 事务标识 [15,0] */
	private String transIdentifier;
	public String getGlobalSerialNumber() {
		return globalSerialNumber;
	}
	public void setGlobalSerialNumber(String globalSerialNumber) {
		this.globalSerialNumber = globalSerialNumber;
	}
	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}
	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}
	public String getPosEntryMode() {
		return posEntryMode;
	}
	public void setPosEntryMode(String posEntryMode) {
		this.posEntryMode = posEntryMode;
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
	public String getSettlementIndicator() {
		return settlementIndicator;
	}
	public void setSettlementIndicator(String settlementIndicator) {
		this.settlementIndicator = settlementIndicator;
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
	public String getTransOriginInstit() {
		return transOriginInstit;
	}
	public void setTransOriginInstit(String transOriginInstit) {
		this.transOriginInstit = transOriginInstit;
	}
	public String getAcquirerBusinessId() {
		return acquirerBusinessId;
	}
	public void setAcquirerBusinessId(String acquirerBusinessId) {
		this.acquirerBusinessId = acquirerBusinessId;
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
	public String getReimburseAttribute() {
		return reimburseAttribute;
	}
	public void setReimburseAttribute(String reimburseAttribute) {
		this.reimburseAttribute = reimburseAttribute;
	}
	public String getTransIdentifier() {
		return transIdentifier;
	}
	public void setTransIdentifier(String transIdentifier) {
		this.transIdentifier = transIdentifier;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}

