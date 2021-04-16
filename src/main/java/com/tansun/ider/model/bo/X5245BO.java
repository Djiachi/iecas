package com.tansun.ider.model.bo;


/**
 * @Desc:
 * @Author wt
 * @Date 2018年5月28日 下午8:38:06
 */
public class X5245BO {

	/** 创建一个全局流水号 */
	private String globalEventNo;
	/** 赋予初始值 */
	private String ecommEventId; // 事件ID CRDPR40G000001
	/** 客户号 */
	private String credentialNumber;
	private String phoneNumber;
	/** 外部识别号 */
	private String externalIdentificationNo;
	
	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}

	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}

	public String getGlobalEventNo() {
		return globalEventNo;
	}

	public void setGlobalEventNo(String globalEventNo) {
		this.globalEventNo = globalEventNo;
	}

	public String getEcommEventId() {
		return ecommEventId;
	}

	public void setEcommEventId(String ecommEventId) {
		this.ecommEventId = ecommEventId;
	}

	public String getCredentialNumber() {
		return credentialNumber;
	}

	public void setCredentialNumber(String credentialNumber) {
		this.credentialNumber = credentialNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "X5245BO [globalEventNo=" + globalEventNo + ", ecommEventId=" + ecommEventId + ", credentialNumber="
				+ credentialNumber + ", phoneNumber=" + phoneNumber + ", externalIdentificationNo="
				+ externalIdentificationNo + "]";
	}

}
