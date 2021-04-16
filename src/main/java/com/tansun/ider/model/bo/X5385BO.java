package com.tansun.ider.model.bo;

import java.io.Serializable;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * 账单产品对象维度查询
 * @author huangyayun
 * @date 2018年8月13日
 */
public class X5385BO extends BeanVO implements Serializable {
    private static final long serialVersionUID = 5047821707257309928L;
    /** 币种 : 币种 [3,0] Not NULL */
    protected String currencyCode;
    /** 账单日期 : 账单日期[10,0] Not NULL */
    protected String billDate;
    /** 业务项目 : 业务项目 [9,0] Not NULL */
    protected String businessProgramNo;
    /** 产品对象代码 : 产品对象代码 [15,0] Not NULL */
    protected String productObjectCode;
    /** 所属业务类型 : 所属业务类型 [15,0] Not NULL */
    protected String businessTypeCode;
    /** 外部识别号 Not NULL */
    protected String externalIdentificationNo;
    /** 证件类型 Not NULL */
    protected String idType;
    /** 证件号码 Not NULL */
    protected String idNumber;

	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getBusinessProgramNo() {
		return businessProgramNo;
	}
	public void setBusinessProgramNo(String businessProgramNo) {
		this.businessProgramNo = businessProgramNo;
	}
	public String getProductObjectCode() {
		return productObjectCode;
	}
	public void setProductObjectCode(String productObjectCode) {
		this.productObjectCode = productObjectCode;
	}
	public String getBusinessTypeCode() {
		return businessTypeCode;
	}
	public void setBusinessTypeCode(String businessTypeCode) {
		this.businessTypeCode = businessTypeCode;
	}
	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}
	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	@Override
	public String toString() {
		return "X5385BO [currencyCode=" + currencyCode + ", BillDate=" + billDate
				+ ", businessProgramNo=" + businessProgramNo + ", productObjectCode=" + productObjectCode
				+ ", businessTypeCode=" + businessTypeCode + ", externalIdentificationNo=" + externalIdentificationNo
				+ ", idType=" + idType + ", idNumber=" + idNumber + "]";
	}
}
