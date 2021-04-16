package com.tansun.ider.model.bo;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * 账单产品对象+业务类型维度查询
 * @author huangyayun
 * @date 2018年8月13日
 */
public class X5395BO extends BeanVO implements Serializable {
    private static final long serialVersionUID = 5047821707257309928L;
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    /** 币种 : 币种 [3,0] Not NULL */
    protected String currencyCode;
    /** 账单日期 : 账单日期[10,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    protected String billDate;
    /** 业务项目 : 业务项目 [9,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    protected String businessProgramNo;
    /** 产品对象代码 : 产品对象代码 [15,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    protected String productObjectCode;
    /** 所属业务类型 : 所属业务类型 [15,0] Not NULL */
    protected String businessTypeCode;
    /** 客户号: 客户号 [15,0] Not NULL */
    @NotBlank(message = "验证字符串非null，且长度必须大于0 ")
    protected String customerNo;
    /** 当前周期号 : 当前周期号 [10,0] Not NULL */
    protected Integer currentCycleNumber;

	public Integer getCurrentCycleNumber() {
		return currentCycleNumber;
	}
	public void setCurrentCycleNumber(Integer currentCycleNumber) {
		this.currentCycleNumber = currentCycleNumber;
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
	@Override
	public String toString() {
		return "X5395BO [currencyCode=" + currencyCode + ", BillDate=" + billDate
				+ ", businessProgramNo=" + businessProgramNo + ", productObjectCode=" + productObjectCode
				+ ", businessTypeCode=" + businessTypeCode +  ", customerNo=" + customerNo + ", currentCycleNumber=" + currentCycleNumber +"]";
	}
}
