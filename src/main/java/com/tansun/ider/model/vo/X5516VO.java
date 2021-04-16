package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class X5516VO implements Serializable{
	private static final long serialVersionUID = 5730330957874450191L;
	/** PCD编号 [8,0] Not NULL */
    private String objectCode;
	/** PCD编号 [8,0] Not NULL */
    private String pcdNo;
    /** 币种 [3,0] */
    private String currencyCode;
    /** 取值类型 D：数值 P：百分比 [1,0] Not NULL */
    private String pcdType;
    /** 取值 [10,0] */
    private BigDecimal pcdValue;
    /** 取值小数位 [10,0] */
    private Integer pcdPoint;
	public String getPcdNo() {
		return pcdNo;
	}
	public void setPcdNo(String pcdNo) {
		this.pcdNo = pcdNo;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getPcdType() {
		return pcdType;
	}
	public void setPcdType(String pcdType) {
		this.pcdType = pcdType;
	}
	public BigDecimal getPcdValue() {
		return pcdValue;
	}
	public void setPcdValue(BigDecimal pcdValue) {
		this.pcdValue = pcdValue;
	}
	public Integer getPcdPoint() {
		return pcdPoint;
	}
	public void setPcdPoint(Integer pcdPoint) {
		this.pcdPoint = pcdPoint;
	}
	public String getObjectCode() {
		return objectCode;
	}
	public void setObjectCode(String objectCode) {
		this.objectCode = objectCode;
	}
}
