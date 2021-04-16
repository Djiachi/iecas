package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: 交易限制
 * @Author kxw
 * @Date 2019年4月25日下午3:03:34
 */
public class X7006BO extends BeanVO implements Serializable {

	private static final long serialVersionUID = 7934690795147016582L;
	private String id;
	private String operationMode;
	private String differentCode;
	private String transLimitCode;
	private String levelFlag;
	private String currencyCode;
	private BigDecimal limitSingleTrans;
	private BigDecimal limitDayTrans;
	private BigDecimal limitCycleTrans;
	private BigDecimal limitMonthTrans;
	private BigDecimal limitHalfYearTrans;
	private BigDecimal limitYearTrans;
	private String gmtCreate;
	private Date timestamp;
	private Integer version;
	private BigDecimal limitFullLifeCycle;

	public String getCurrencyCode() {
		return currencyCode;
	}

	public String getDifferentCode() {
		return differentCode;
	}

	public String getGmtCreate() {
		return gmtCreate;
	}

	public String getId() {
		return id;
	}

	public String getLevelFlag() {
		return levelFlag;
	}

	public BigDecimal getLimitCycleTrans() {
		return limitCycleTrans;
	}

	public BigDecimal getLimitDayTrans() {
		return limitDayTrans;
	}

	public BigDecimal getLimitFullLifeCycle() {
		return limitFullLifeCycle;
	}

	public BigDecimal getLimitHalfYearTrans() {
		return limitHalfYearTrans;
	}

	public BigDecimal getLimitMonthTrans() {
		return limitMonthTrans;
	}

	public BigDecimal getLimitSingleTrans() {
		return limitSingleTrans;
	}

	public BigDecimal getLimitYearTrans() {
		return limitYearTrans;
	}

	public String getOperationMode() {
		return operationMode;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getTransLimitCode() {
		return transLimitCode;
	}

	public Integer getVersion() {
		return version;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public void setDifferentCode(String differentCode) {
		this.differentCode = differentCode;
	}

	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLevelFlag(String levelFlag) {
		this.levelFlag = levelFlag;
	}

	public void setLimitCycleTrans(BigDecimal limitCycleTrans) {
		this.limitCycleTrans = limitCycleTrans;
	}

	public void setLimitDayTrans(BigDecimal limitDayTrans) {
		this.limitDayTrans = limitDayTrans;
	}

	public void setLimitFullLifeCycle(BigDecimal limitFullLifeCycle) {
		this.limitFullLifeCycle = limitFullLifeCycle;
	}

	public void setLimitHalfYearTrans(BigDecimal limitHalfYearTrans) {
		this.limitHalfYearTrans = limitHalfYearTrans;
	}

	public void setLimitMonthTrans(BigDecimal limitMonthTrans) {
		this.limitMonthTrans = limitMonthTrans;
	}

	public void setLimitSingleTrans(BigDecimal limitSingleTrans) {
		this.limitSingleTrans = limitSingleTrans;
	}

	public void setLimitYearTrans(BigDecimal limitYearTrans) {
		this.limitYearTrans = limitYearTrans;
	}

	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public void setTransLimitCode(String transLimitCode) {
		this.transLimitCode = transLimitCode;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "X7006BO [id=" + id + ", operationMode=" + operationMode + ", differentCode=" + differentCode
				+ ", transLimitCode=" + transLimitCode + ", levelFlag=" + levelFlag + ", currencyCode=" + currencyCode
				+ ", limitSingleTrans=" + limitSingleTrans + ", limitDayTrans=" + limitDayTrans + ", limitCycleTrans="
				+ limitCycleTrans + ", limitMonthTrans=" + limitMonthTrans + ", limitHalfYearTrans="
				+ limitHalfYearTrans + ", limitYearTrans=" + limitYearTrans + ", gmtCreate=" + gmtCreate
				+ ", timestamp=" + timestamp + ", version=" + version + ", limitFullLifeCycle=" + limitFullLifeCycle
				+ "]";
	}
}
