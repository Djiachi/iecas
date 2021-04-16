package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.Date;

import com.tansun.ider.framwork.commun.BeanVO;

public class X8003BO extends BeanVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String doWhat;
	private String changeType;
	
	/** 创建一个流水号*/
	private String id;
	/** 客户号*/
	private String customerNo;
	/** 额度树*/
	private String creditTreeId;
	/** 节点编号*/
	private String creditNodeNo;
	
	private String currencyCode;
	
	private String productObjectCode;
	
	private String currEffectvLimit;
	
	private double toleranceRatio;
	
	private double toleranceAmount;
	
	private double permLimit;
	
	private double tempLimit;
	
	private double availLimit;
	
	private Date permLimitEffectvDate;
	
	private Date tempLimitEffectvDate;
	
	private double outstandingLimit;
	
	private Date gmtCreate;
	
	private Date gmtModified;
	
	private Integer version;
	

	public String getDoWhat() {
		return doWhat;
	}

	public void setDoWhat(String doWhat) {
		this.doWhat = doWhat;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
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

	public String getProductObjectCode() {
		return productObjectCode;
	}

	public void setProductObjectCode(String productObjectCode) {
		this.productObjectCode = productObjectCode;
	}

	public String getCurrEffectvLimit() {
		return currEffectvLimit;
	}

	public void setCurrEffectvLimit(String currEffectvLimit) {
		this.currEffectvLimit = currEffectvLimit;
	}

	public double getToleranceRatio() {
		return toleranceRatio;
	}

	public void setToleranceRatio(double toleranceRatio) {
		this.toleranceRatio = toleranceRatio;
	}

	public double getToleranceAmount() {
		return toleranceAmount;
	}

	public void setToleranceAmount(double toleranceAmount) {
		this.toleranceAmount = toleranceAmount;
	}

	public double getPermLimit() {
		return permLimit;
	}

	public void setPermLimit(double permLimit) {
		this.permLimit = permLimit;
	}

	public double getTempLimit() {
		return tempLimit;
	}

	public void setTempLimit(double tempLimit) {
		this.tempLimit = tempLimit;
	}

	public double getAvailLimit() {
		return availLimit;
	}

	public void setAvailLimit(double availLimit) {
		this.availLimit = availLimit;
	}

	public Date getPermLimitEffectvDate() {
		return permLimitEffectvDate;
	}

	public void setPermLimitEffectvDate(Date permLimitEffectvDate) {
		this.permLimitEffectvDate = permLimitEffectvDate;
	}

	public Date getTempLimitEffectvDate() {
		return tempLimitEffectvDate;
	}

	public void setTempLimitEffectvDate(Date tempLimitEffectvDate) {
		this.tempLimitEffectvDate = tempLimitEffectvDate;
	}

	public double getOutstandingLimit() {
		return outstandingLimit;
	}

	public void setOutstandingLimit(double outstandingLimit) {
		this.outstandingLimit = outstandingLimit;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "X8003BO [changeType=" + changeType + ", id=" + id + ", customerNo=" + customerNo + ", creditTreeId="
				+ creditTreeId + ", creditNodeNo=" + creditNodeNo + ", currencyCode=" + currencyCode
				+ ", productObjectCode=" + productObjectCode + ", currEffectvLimit=" + currEffectvLimit
				+ ", toleranceRatio=" + toleranceRatio + ", toleranceAmount=" + toleranceAmount + ", permLimit="
				+ permLimit + ", tempLimit=" + tempLimit + ", availLimit=" + availLimit + ", permLimitEffectvDate="
				+ permLimitEffectvDate + ", tempLimitExpireDate=" + tempLimitEffectvDate + ", outstandingLimit="
				+ outstandingLimit + ", gmtCreate=" + gmtCreate + ", gmtModified=" + gmtModified + ", version="
				+ version + "]";
	}
}
