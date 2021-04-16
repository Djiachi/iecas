package com.tansun.ider.model.vo;

import java.io.Serializable;

import com.tansun.ider.framwork.commun.BeanVO;

public class X5125VO extends BeanVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7652799073403469747L;
	/** 客户代码 */
	private String customerNo;
	/** 客户姓名 */
	private String customerName;
	/** 证件号码 */
	private String idNumber;
	/** 外部识别号 */
	private String externalIdentificationNo;
	/** 媒介对象代码 */
	private String mediaObjectCode;
	/** 主附标识 1：主卡 2：附属卡 [1,0] Not NULL */
	private String mainSupplyIndicator;
	/** 卡片状态 1：新发 2：活跃 3：非活跃 4：已转出 8：关闭 9：待删除 [1,0] */
	private String statusCode;
	/** 激活日期 [10,0] */
	private String activationDate;
	
	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

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

	public String getMainSupplyIndicator() {
		return mainSupplyIndicator;
	}

	public void setMainSupplyIndicator(String mainSupplyIndicator) {
		this.mainSupplyIndicator = mainSupplyIndicator;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(String activationDate) {
		this.activationDate = activationDate;
	}

	@Override
	public String toString() {
		return "X5125VO [customerNo=" + customerNo + ", customerName=" + customerName + ", idNumber=" + idNumber
				+ ", externalIdentificationNo=" + externalIdentificationNo + ", mediaObjectCode=" + mediaObjectCode
				+ ", mainSupplyIndicator=" + mainSupplyIndicator + ", statusCode=" + statusCode + ", activationDate="
				+ activationDate + "]";
	}

}
