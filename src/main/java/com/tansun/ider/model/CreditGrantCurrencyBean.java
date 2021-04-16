package com.tansun.ider.model;

import java.math.BigDecimal;

public class CreditGrantCurrencyBean {

    /** 同步授权标志 */
    private String authDataSynFlag;
	private String authDataSynMap;
    /** 运营模式  */
    private String operationMode;
    /** 客户号 [12,0] Not NULL */
    private String customerNo;
    /** 外部识别号*/
    private String externalIdentificationNo;
    /** 产品对象*/
    private String productObjectCode;
    /** 额度树ID*/
    private String creditTreeId;
    /** 授信节点 [3,0] Not NULL */
    private String creditNodeNo;
    /** 授信币种 [3,0] Not NULL */
    private String currencyCode;
    /** 授信类型   P： 永久额度   T：临时额度*/
    private String creditType;
    /** 额度生效日期 [10,0] */
    private String limitEffectvDate;
    /** 额度失效日期 [10,0] */
    private String limitExpireDate;
    /** 授信额度 */
    private BigDecimal creditLimit;
    /**操作员ID*/
    private String operatorId;
    /** 法人corparation*/
    private String corporation;
    /** 适配批量建卡 -- 附卡不授信*/
    private String applicationCardType;
    
    
	public String getOperationMode() {
		return operationMode;
	}
	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}
	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}
	public String getProductObjectCode() {
		return productObjectCode;
	}
	public void setProductObjectCode(String productObjectCode) {
		this.productObjectCode = productObjectCode;
	}
	public String getCreditTreeId() {
		return creditTreeId;
	}
	public void setCreditTreeId(String creditTreeId) {
		this.creditTreeId = creditTreeId;
	}
	public String getCreditNodeNo() {
		return creditNodeNo;
	}
	public void setCreditNodeNo(String creditNodeNo) {
		this.creditNodeNo = creditNodeNo;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getCreditType() {
		return creditType;
	}
	public void setCreditType(String creditType) {
		this.creditType = creditType;
	}
	public String getLimitEffectvDate() {
		return limitEffectvDate;
	}
	public void setLimitEffectvDate(String limitEffectvDate) {
		this.limitEffectvDate = limitEffectvDate;
	}
	public String getLimitExpireDate() {
		return limitExpireDate;
	}
	public void setLimitExpireDate(String limitExpireDate) {
		this.limitExpireDate = limitExpireDate;
	}
	public BigDecimal getCreditLimit() {
		return creditLimit;
	}
	public void setCreditLimit(BigDecimal creditLimit) {
		this.creditLimit = creditLimit;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getCorporation() {
		return corporation;
	}
	public void setCorporation(String corporation) {
		this.corporation = corporation;
	}
	public String getApplicationCardType() {
		return applicationCardType;
	}
	public void setApplicationCardType(String applicationCardType) {
		this.applicationCardType = applicationCardType;
	}
	public String getAuthDataSynMap() {
		return authDataSynMap;
	}
	public void setAuthDataSynMap(String authDataSynMap) {
		this.authDataSynMap = authDataSynMap;
	}
	public String getAuthDataSynFlag() {
		return authDataSynFlag;
	}
	public void setAuthDataSynFlag(String authDataSynFlag) {
		this.authDataSynFlag = authDataSynFlag;
	}
    
}
