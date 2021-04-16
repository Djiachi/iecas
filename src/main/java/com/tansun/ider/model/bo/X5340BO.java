package com.tansun.ider.model.bo;

import java.io.Serializable;

import com.tansun.ider.framwork.commun.BeanVO;

public class X5340BO extends BeanVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6345700809884117443L;
	/** 证件号码 */
	private String idNumber;
	/** 证件类型*/
	private String idType;
	@Override
	public String toString() {
		return "X5340BO [idNumber=" + idNumber + ", idType=" + idType + ", externalIdentificationNo="
				+ externalIdentificationNo + ", oldGlobalSerialNumbr=" + oldGlobalSerialNumbr + ", customerNo="
				+ customerNo + "]";
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	/** 外部识别号 [19,0] Not NULL */
	private String externalIdentificationNo;
	
	 /** 原交易全局流水号 : 原交易全局流水号 [36,0] */
	private String oldGlobalSerialNumbr;
	 /** 客户号 [36,0] */
	private String customerNo;
	
	private String currencyCode;
	

	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}

	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getOldGlobalSerialNumbr() {
		return oldGlobalSerialNumbr;
	}

	public void setOldGlobalSerialNumbr(String oldGlobalSerialNumbr) {
		this.oldGlobalSerialNumbr = oldGlobalSerialNumbr;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

}
