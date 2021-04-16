package com.tansun.ider.model;

import java.util.Date;

public class AuthCardIssueExcptBean {

	/** 法人实体编号 */
	protected String corporation;
	 /** 客户号 [12,0] Not NULL */
    protected String customerNo;
    /** 业务项目 [9,0] */
    protected String businessProgram;
    /** 外部识别号 [32,0] */
    protected String externalIdentificationNo;
    /** 产品对象 [9,0] */
    protected String productObjectCode;
    /** 交易识别代码 [4,0] */
    protected String transIdentifiNo;
    /** 币种 [3,0] */
    protected String currencyCode;
    /** 授权回应 [1,0] Not NULL */
    protected String authResp;
    /** 说明 [50,0] Not NULL */
    protected String excptDesc;
    /** 无效标志 [1,0] */
    protected String invalidFlag;
    /** 创建时间 yyyy-MM-dd HH:mm:ss [23,0] */
    protected String gmtCreate;
    /** 时间戳 : oralce使用触发器更新， mysql使用自动更新 [19,0] Not NULL */
    protected Date timestamp;
    /** 版本号 [10,0] Not NULL */
    protected Integer version;
    /**  同步授权标志*/
    private String authDataSynFlag;
    /**  上管控码给ADD，下管控码给DEL*/
    private String modifyType;
    
	public String getAuthDataSynFlag() {
		return authDataSynFlag;
	}
	public void setAuthDataSynFlag(String authDataSynFlag) {
		this.authDataSynFlag = authDataSynFlag;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getBusinessProgram() {
		return businessProgram;
	}
	public void setBusinessProgram(String businessProgram) {
		this.businessProgram = businessProgram;
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
	public String getTransIdentifiNo() {
		return transIdentifiNo;
	}
	public void setTransIdentifiNo(String transIdentifiNo) {
		this.transIdentifiNo = transIdentifiNo;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getAuthResp() {
		return authResp;
	}
	public void setAuthResp(String authResp) {
		this.authResp = authResp;
	}
	public String getExcptDesc() {
		return excptDesc;
	}
	public void setExcptDesc(String excptDesc) {
		this.excptDesc = excptDesc;
	}
	public String getInvalidFlag() {
		return invalidFlag;
	}
	public void setInvalidFlag(String invalidFlag) {
		this.invalidFlag = invalidFlag;
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
	public String getCorporation() {
		return corporation;
	}
	public void setCorporation(String corporation) {
		this.corporation = corporation;
	}
	public String getModifyType() {
		return modifyType;
	}
	public void setModifyType(String modifyType) {
		this.modifyType = modifyType;
	}
	@Override
	public String toString() {
		return "AuthCardIssueExcptBean [corporation=" + corporation + ", customerNo=" + customerNo
				+ ", businessProgram=" + businessProgram + ", externalIdentificationNo=" + externalIdentificationNo
				+ ", productObjectCode=" + productObjectCode + ", transIdentifiNo=" + transIdentifiNo
				+ ", currencyCode=" + currencyCode + ", authResp=" + authResp + ", excptDesc=" + excptDesc
				+ ", invalidFlag=" + invalidFlag + ", gmtCreate=" + gmtCreate + ", timestamp=" + timestamp
				+ ", version=" + version + ", authDataSynFlag=" + authDataSynFlag + ", modifyType=" + modifyType + "]";
	}
}
