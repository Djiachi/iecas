package com.tansun.ider.model;

import java.util.List;

public class InstallAccountBean {

	private String businessTypeCode;
	private String customerNo;
	private String loanType;
	private String externalIdentificationNo;
	private String businessProgramNo;
	private String productObjectCode;
	private Integer indexNo;
	private Integer pageSize;
	//增加的查询条件
	private String loanStartDate;
	private String loanEndDate;
	private String oldGlobalSerialNumbr;
	private List<String> externalIdentificationNos;
	private String globalTransSerialNo;
	
	public String getLoanStartDate() {
		return loanStartDate;
	}
	public void setLoanStartDate(String loanStartDate) {
		this.loanStartDate = loanStartDate;
	}
	public String getLoanEndDate() {
		return loanEndDate;
	}
	public void setLoanEndDate(String loanEndDate) {
		this.loanEndDate = loanEndDate;
	}
	public String getOldGlobalSerialNumbr() {
		return oldGlobalSerialNumbr;
	}
	public void setOldGlobalSerialNumbr(String oldGlobalSerialNumbr) {
		this.oldGlobalSerialNumbr = oldGlobalSerialNumbr;
	}
	public List<String> getExternalIdentificationNos() {
		return externalIdentificationNos;
	}
	public void setExternalIdentificationNos(List<String> externalIdentificationNos) {
		this.externalIdentificationNos = externalIdentificationNos;
	}
	public Integer getIndexNo() {
		return indexNo;
	}
	public void setIndexNo(Integer indexNo) {
		this.indexNo = indexNo;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String getBusinessTypeCode() {
		return businessTypeCode;
	}
	public void setBusinessTypeCode(String businessTypeCode) {
		this.businessTypeCode = businessTypeCode;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getLoanType() {
		return loanType;
	}
	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}
	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}
	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
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
	@Override
	public String toString() {
		return "InstallAccountBean [businessTypeCode=" + businessTypeCode + ", customerNo=" + customerNo + ", loanType="
				+ loanType + ", externalIdentificationNo=" + externalIdentificationNo + ", businessProgramNo="
				+ businessProgramNo + ", productObjectCode=" + productObjectCode + "]";
	}
	public String getGlobalTransSerialNo() {
		return globalTransSerialNo;
	}
	public void setGlobalTransSerialNo(String globalTransSerialNo) {
		this.globalTransSerialNo = globalTransSerialNo;
	}

}
