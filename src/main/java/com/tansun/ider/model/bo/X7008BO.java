package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: 交易累计查询
 * @Author kxw
 * @Date 2019年4月25日下午3:03:34
 */
public class X7008BO extends BeanVO implements Serializable {

	private static final long serialVersionUID = 9004944181388459625L;

	private String id;
	private String accumultTyp;
	private String accumultIndentitCde;
	private String transLimitCde;
	private String currencyCode;
	private BigDecimal accumultDayTrans;
	private BigDecimal accumultCycleTrans;
	private BigDecimal accumultMonthTrans;
	private BigDecimal accumultHalfYearTrans;
	private BigDecimal accumultYearTrans;
	private String gmtCreate;
	private Date timestamp;
	private Integer version;
	private BigDecimal accumultLifeCycleTrans;

	public BigDecimal getAccumultCycleTrans() {
		return accumultCycleTrans;
	}

	public BigDecimal getAccumultDayTrans() {
		return accumultDayTrans;
	}

	public BigDecimal getAccumultHalfYearTrans() {
		return accumultHalfYearTrans;
	}

	public String getAccumultIndentitCde() {
		return accumultIndentitCde;
	}

	public BigDecimal getAccumultLifeCycleTrans() {
		return accumultLifeCycleTrans;
	}

	public BigDecimal getAccumultMonthTrans() {
		return accumultMonthTrans;
	}

	public String getAccumultTyp() {
		return accumultTyp;
	}

	public BigDecimal getAccumultYearTrans() {
		return accumultYearTrans;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public String getGmtCreate() {
		return gmtCreate;
	}

	public String getId() {
		return id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getTransLimitCde() {
		return transLimitCde;
	}

	public Integer getVersion() {
		return version;
	}

	public void setAccumultCycleTrans(BigDecimal accumultCycleTrans) {
		this.accumultCycleTrans = accumultCycleTrans;
	}

	public void setAccumultDayTrans(BigDecimal accumultDayTrans) {
		this.accumultDayTrans = accumultDayTrans;
	}

	public void setAccumultHalfYearTrans(BigDecimal accumultHalfYearTrans) {
		this.accumultHalfYearTrans = accumultHalfYearTrans;
	}

	public void setAccumultIndentitCde(String accumultIndentitCde) {
		this.accumultIndentitCde = accumultIndentitCde;
	}

	public void setAccumultLifeCycleTrans(BigDecimal accumultLifeCycleTrans) {
		this.accumultLifeCycleTrans = accumultLifeCycleTrans;
	}

	public void setAccumultMonthTrans(BigDecimal accumultMonthTrans) {
		this.accumultMonthTrans = accumultMonthTrans;
	}

	public void setAccumultTyp(String accumultTyp) {
		this.accumultTyp = accumultTyp;
	}

	public void setAccumultYearTrans(BigDecimal accumultYearTrans) {
		this.accumultYearTrans = accumultYearTrans;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public void setTransLimitCde(String transLimitCde) {
		this.transLimitCde = transLimitCde;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "X7008BO [id=" + id + ", accumultTyp=" + accumultTyp + ", accumultIndentitCde=" + accumultIndentitCde
				+ ", transLimitCde=" + transLimitCde + ", currencyCode=" + currencyCode + ", accumultDayTrans="
				+ accumultDayTrans + ", accumultCycleTrans=" + accumultCycleTrans + ", accumultMonthTrans="
				+ accumultMonthTrans + ", accumultHalfYearTrans=" + accumultHalfYearTrans + ", accumultYearTrans="
				+ accumultYearTrans + ", gmtCreate=" + gmtCreate + ", timestamp=" + timestamp + ", version=" + version
				+ ", accumultLifeCycleTrans=" + accumultLifeCycleTrans + "]";
	}
}
