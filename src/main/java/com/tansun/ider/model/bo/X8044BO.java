package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.Date;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: 运营模式
 * @Author kangxuw
 * @Date 2018年5月24日下午3:03:34
 */
public class X8044BO extends BeanVO implements Serializable {

	private static final long serialVersionUID = 8972173625014349163L;
	private String id;
	private String operationMode;
	private String modeName;
	private String accountCurrency;
	private Date operationDate;

	private String creditTreeId;
	private String excessContriServTyp;
	private Date gmtCreate;
	private Date gmtModified;
	private Integer version;

	public String getAccountCurrency() {
		return accountCurrency;
	}

	public String getCreditTreeId() {
		return creditTreeId;
	}

	public String getExcessContriServTyp() {
		return excessContriServTyp;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public String getId() {
		return id;
	}

	public String getModeName() {
		return modeName;
	}

	public Date getOperationDate() {
		return operationDate;
	}

	public String getOperationMode() {
		return operationMode;
	}

	public Integer getVersion() {
		return version;
	}

	public void setAccountCurrency(String accountCurrency) {
		this.accountCurrency = accountCurrency;
	}

	public void setCreditTreeId(String creditTreeId) {
		this.creditTreeId = creditTreeId;
	}

	public void setExcessContriServTyp(String excessContriServTyp) {
		this.excessContriServTyp = excessContriServTyp;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setModeName(String modeName) {
		this.modeName = modeName;
	}

	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "X8044BO [id=" + id + ", operationMode=" + operationMode + ", modeName=" + modeName
				+ ", accountCurrency=" + accountCurrency + ", operationDate=" + operationDate + ", creditTreeId="
				+ creditTreeId + ", excessContriServTyp=" + excessContriServTyp + ", gmtCreate=" + gmtCreate
				+ ", gmtModified=" + gmtModified + ", version=" + version + "]";
	}
}
