package com.tansun.ider.model.bo;

import java.io.Serializable;

import com.tansun.ider.framwork.commun.BeanVO;

public class X7010BO extends BeanVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	 /** 证件号码 */
	private String credentialNumber;
	/** 客户号 */
	private String customerNo;
	/** 手机号码 */
	private String phoneNumber;
	/** 外部识别号 */
	private String externalIdentificationNo;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCredentialNumber() {
		return credentialNumber;
	}
	public void setCredentialNumber(String credentialNumber) {
		this.credentialNumber = credentialNumber;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}
	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}
	
	@Override
	public String toString() {
		return "X7010BO [id=" + id + ", credentialNumber=" + credentialNumber + ", customerNo=" + customerNo
				+ ", phoneNumber=" + phoneNumber + ", externalIdentificationNo=" + externalIdentificationNo + "]";
	}
	
}
