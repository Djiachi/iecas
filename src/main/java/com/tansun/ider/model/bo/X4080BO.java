package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * <p> Title: X4080BO </p>
 * <p> Description: 会计接口文件查询</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author yanyingzhao
 * @since 2019年6月20日
 */
public class X4080BO extends BeanVO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2189563547821728773L;
	/** 全局流水号 [36,0] Not NULL */
	private String globalSerialNumber;
	/** 记账规则代码 [5,0] Not NULL */
	private String accountingRuleCode;
	/** 子表序号 [10,0] Not NULL */
	private String subTableSequence;
	/** 入账日期 yyyy-MM-dd [10,0] Not NULL */
	private String postingDate;
	/** 账号 [23,0] */
	private String accountId;
	/** 核算类型编号 [7,0] */
	private String accountingType;
	/** 产品对象代码 [15,0] */
	private String productObjectCode;
	/** 业务类型代码 [9,0] */
	private String businessTypeCode;
	/** 事件编号 [14,0] */
	private String eventNo;
	/** 外部识别号 [32,0] */
	private String externalIdentificationNo;
	/** 交易机构 [10,0] */
	private String transOrganization;
	/** 账户机构 [10,0] */
	private String accountOrganization;
	/** 交易摘要 [50,0] */
	private String transSummary;
	/** 渠道代码 [8,0] */
	private String channelCde;
	/** 核算状态码 [3,0] */
	private String accountingStatusCode;
	/** 记账标识 I：内部帐 L：科目 [1,0] */
	private String accountingFlag;
	/**  原子动作编号 [4,0] */
	private String atomicActionNumber;
	/**  金额类型编号 [4,0] */
	private String amountTypeNumber;
	/** 内部账号 [23,0] */
	private String internalAccountNo;
	/** 借贷方向 D：借方 C：贷方 [1,0] */
	private String drcrFlag;
	/** 记账币种 [3,0] */
	private String postingCurrencyCode;
	/** 记账币种小数位 [10,0] */
	private Integer postingCurrencyPoint;
	/** 记账金额 [18,0] */
	private BigDecimal actualPostingAmount;
	/** 处理状态 [2,0] */
	private String dealStatus;
	/** 证件类型[1,0] */
	private String idType;
	/** 证件号码[30,0] */
	private String idNumber;
	private String accountingRuleDesc;
	/** 原子动作编号描述[50,0] */
	private String atomicActionNumberDesc;
	/** 金额类型编号描述[50,0] */
	private String amountTypeNumberDesc;
	/** 币种描述 [50,0] */
	protected String currencyDesc;
	/** 访问类别 */
	private String modifyType;


	public String getGlobalSerialNumber() {
		return globalSerialNumber;
	}
	public void setGlobalSerialNumber(String globalSerialNumber) {
		this.globalSerialNumber = globalSerialNumber;
	}
	public String getAccountingRuleCode() {
		return accountingRuleCode;
	}
	public void setAccountingRuleCode(String accountingRuleCode) {
		this.accountingRuleCode = accountingRuleCode;
	}
	public String getSubTableSequence() {
		return subTableSequence;
	}
	public void setSubTableSequence(String subTableSequence) {
		this.subTableSequence = subTableSequence;
	}
	public String getPostingDate() {
		return postingDate;
	}
	public void setPostingDate(String postingDate) {
		this.postingDate = postingDate;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getAccountingType() {
		return accountingType;
	}
	public void setAccountingType(String accountingType) {
		this.accountingType = accountingType;
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
	public String getTransOrganization() {
		return transOrganization;
	}
	public void setTransOrganization(String transOrganization) {
		this.transOrganization = transOrganization;
	}
	public String getAccountOrganization() {
		return accountOrganization;
	}
	public void setAccountOrganization(String accountOrganization) {
		this.accountOrganization = accountOrganization;
	}
	public String getTransSummary() {
		return transSummary;
	}
	public void setTransSummary(String transSummary) {
		this.transSummary = transSummary;
	}
	public String getChannelCde() {
		return channelCde;
	}
	public void setChannelCde(String channelCde) {
		this.channelCde = channelCde;
	}
	public String getAccountingStatusCode() {
		return accountingStatusCode;
	}
	public void setAccountingStatusCode(String accountingStatusCode) {
		this.accountingStatusCode = accountingStatusCode;
	}
	public String getAccountingFlag() {
		return accountingFlag;
	}
	public void setAccountingFlag(String accountingFlag) {
		this.accountingFlag = accountingFlag;
	}
	public String getAtomicActionNumber() {
		return atomicActionNumber;
	}
	public void setAtomicActionNumber(String atomicActionNumber) {
		this.atomicActionNumber = atomicActionNumber;
	}
	public String getAmountTypeNumber() {
		return amountTypeNumber;
	}
	public void setAmountTypeNumber(String amountTypeNumber) {
		this.amountTypeNumber = amountTypeNumber;
	}
	public String getInternalAccountNo() {
		return internalAccountNo;
	}
	public void setInternalAccountNo(String internalAccountNo) {
		this.internalAccountNo = internalAccountNo;
	}
	public String getDrcrFlag() {
		return drcrFlag;
	}
	public void setDrcrFlag(String drcrFlag) {
		this.drcrFlag = drcrFlag;
	}
	public String getPostingCurrencyCode() {
		return postingCurrencyCode;
	}
	public void setPostingCurrencyCode(String postingCurrencyCode) {
		this.postingCurrencyCode = postingCurrencyCode;
	}
	public Integer getPostingCurrencyPoint() {
		return postingCurrencyPoint;
	}
	public void setPostingCurrencyPoint(Integer postingCurrencyPoint) {
		this.postingCurrencyPoint = postingCurrencyPoint;
	}
	public BigDecimal getActualPostingAmount() {
		return actualPostingAmount;
	}
	public void setActualPostingAmount(BigDecimal actualPostingAmount) {
		this.actualPostingAmount = actualPostingAmount;
	}
	public String getDealStatus() {
		return dealStatus;
	}
	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	public String getAccountingRuleDesc() {
		return accountingRuleDesc;
	}
	public void setAccountingRuleDesc(String accountingRuleDesc) {
		this.accountingRuleDesc = accountingRuleDesc;
	}
	public String getAtomicActionNumberDesc() {
		return atomicActionNumberDesc;
	}
	public void setAtomicActionNumberDesc(String atomicActionNumberDesc) {
		this.atomicActionNumberDesc = atomicActionNumberDesc;
	}
	public String getAmountTypeNumberDesc() {
		return amountTypeNumberDesc;
	}
	public void setAmountTypeNumberDesc(String amountTypeNumberDesc) {
		this.amountTypeNumberDesc = amountTypeNumberDesc;
	}
	public String getCurrencyDesc() {
		return currencyDesc;
	}
	public void setCurrencyDesc(String currencyDesc) {
		this.currencyDesc = currencyDesc;
	}

	public String getModifyType() {
		return modifyType;
	}

	public void setModifyType(String modifyType) {
		this.modifyType = modifyType;
	}
}