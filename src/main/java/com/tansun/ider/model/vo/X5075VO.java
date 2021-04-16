package com.tansun.ider.model.vo;

public class X5075VO {

	/** 原媒介外部识别号 : 原媒介外部识别号 [9,0] Not NULL */
	protected String externalIdentificationNo;
	/** 原媒介对象代码: 原媒介对象代码 [9,0] Not NULL */
	protected String mediaObjectCode;
	/** 原媒介产品对象代码 [9,0] Not NULL */
	protected String productObjectCode;
	/** 主客户代码 : 主客户代码 [12,0] Not NULL */
	protected String mainCustomerCode;
	/** 所所属机构 : 所所属机构 [10,0] Not NULL */
	protected String affiliatedInstitutions;
	/** 所属运营模式 : 所属运营模式 [3,0] Not NULL */
	protected String operationMode;
	/** 原媒介使用者姓名 : 原媒介使用者姓名 [30,0] Not NULL */
	protected String mediaUserName;
	/** 原媒介有效期 [10,0] */
	protected String termValidity;

	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}

	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}

	public String getMediaObjectCode() {
		return mediaObjectCode;
	}

	public void setMediaObjectCode(String mediaObjectCode) {
		this.mediaObjectCode = mediaObjectCode;
	}

	public String getProductObjectCode() {
		return productObjectCode;
	}

	public void setProductObjectCode(String productObjectCode) {
		this.productObjectCode = productObjectCode;
	}

	public String getMainCustomerCode() {
		return mainCustomerCode;
	}

	public void setMainCustomerCode(String mainCustomerCode) {
		this.mainCustomerCode = mainCustomerCode;
	}

	public String getAffiliatedInstitutions() {
		return affiliatedInstitutions;
	}

	public void setAffiliatedInstitutions(String affiliatedInstitutions) {
		this.affiliatedInstitutions = affiliatedInstitutions;
	}

	public String getOperationMode() {
		return operationMode;
	}

	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}

	public String getMediaUserName() {
		return mediaUserName;
	}

	public void setMediaUserName(String mediaUserName) {
		this.mediaUserName = mediaUserName;
	}

	public String getTermValidity() {
		return termValidity;
	}

	public void setTermValidity(String termValidity) {
		this.termValidity = termValidity;
	}

}
