package com.tansun.ider.model;

public class AdjustmentLimitBean {

	// 法人	        
	private String corporation;
	//运营模式	
	private String operationMode;
	// 客户号	        
	private String customerNo;
	// 外部识别号	
	private String externalIdentificationNo;
	// 产品对象	
	private String productObjectCode;
	// 授信节点	
	private String creditNodeNo;
	// 授信币种	
	private String currencyCode;
	// 调额类型	
	private String adjustType;
	// 业务节点调额
	private String adjusClass;
	// 授信额度	
	private String creditLimit;
	// 额度生效日期	
	private String limitEffectvDate;
	// 额度失效日期	
	private String limitExpireDate;
	// 操作员	       
	private String operatorId;
    /** 同步授权标志 */
    private String authDataSynFlag;
    
	public String getCorporation() {
		return corporation;
	}
	public void setCorporation(String corporation) {
		this.corporation = corporation;
	}
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
	public String getAdjustType() {
		return adjustType;
	}
	public void setAdjustType(String adjustType) {
		this.adjustType = adjustType;
	}
	public String getCreditLimit() {
		return creditLimit;
	}
	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
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
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getAuthDataSynFlag() {
		return authDataSynFlag;
	}
	public void setAuthDataSynFlag(String authDataSynFlag) {
		this.authDataSynFlag = authDataSynFlag;
	}
	public String getAdjusClass() {
		return adjusClass;
	}
	public void setAdjusClass(String adjusClass) {
		this.adjusClass = adjusClass;
	}
	public AdjustmentLimitBean() {
		super();
	}
	public AdjustmentLimitBean(String corporation, String operationMode, String customerNo,
			String externalIdentificationNo, String productObjectCode, String creditNodeNo, String currencyCode,
			String adjustType, String creditLimit, String limitEffectvDate, String limitExpireDate, String operatorId,
			String authDataSynFlag) {
		super();
		this.corporation = corporation;
		this.operationMode = operationMode;
		this.customerNo = customerNo;
		this.externalIdentificationNo = externalIdentificationNo;
		this.productObjectCode = productObjectCode;
		this.creditNodeNo = creditNodeNo;
		this.currencyCode = currencyCode;
		this.adjustType = adjustType;
		this.creditLimit = creditLimit;
		this.limitEffectvDate = limitEffectvDate;
		this.limitExpireDate = limitExpireDate;
		this.operatorId = operatorId;
		this.authDataSynFlag = authDataSynFlag;
	}
}
