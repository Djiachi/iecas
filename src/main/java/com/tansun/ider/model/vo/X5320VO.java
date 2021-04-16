package com.tansun.ider.model.vo;

public class X5320VO {
	/** 周期模式 */
	private String cycleModel;
	 /** 运营模式 [3,0] Not NULL */
    protected String operationMode;
	 /** 业务项目描述 [50,0] */
    protected String programDesc;
	 /** 客户代码 [12,0] Not NULL */
    protected String customerNo;
    /** 业务项目代码 [9,0] Not NULL */
    protected String businessProgramNo;
    /** 账单日 [10,0] */
    protected Integer billDay;
    /** 下一账单日期 [10,0] */
    protected String nextBillDate;
    /** 当前周期号 [10,0] */
    protected Integer currentCycleNumber;
    /** 约定扣款状态  0-未设置1-已设置 [1,0] */
    protected String directDebitStatus;
    /** 约定扣款方式  0-最小还款1-全额还款 [1,0] */
    protected String directDebitMode;
    /** 约定还款银行号 [7,0] */
    protected String directDebitBankNo;
    /** 约定还款账户号 [25,0] */
    protected String directDebitAccountNo;
    /** 购汇还款标志 Y:购汇还款;N:无购汇还款 [1,0] */
    protected String exchangePaymentFlag;
	public String getExchangePaymentFlag() {
		return exchangePaymentFlag;
	}
	public void setExchangePaymentFlag(String exchangePaymentFlag) {
		this.exchangePaymentFlag = exchangePaymentFlag;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getBusinessProgramNo() {
		return businessProgramNo;
	}
	public void setBusinessProgramNo(String businessProgramNo) {
		this.businessProgramNo = businessProgramNo;
	}
	public Integer getBillDay() {
		return billDay;
	}
	public void setBillDay(Integer billDay) {
		this.billDay = billDay;
	}
	public String getNextBillDate() {
		return nextBillDate;
	}
	public void setNextBillDate(String nextBillDate) {
		this.nextBillDate = nextBillDate;
	}
	public Integer getCurrentCycleNumber() {
		return currentCycleNumber;
	}
	public void setCurrentCycleNumber(Integer currentCycleNumber) {
		this.currentCycleNumber = currentCycleNumber;
	}
	public String getDirectDebitStatus() {
		return directDebitStatus;
	}
	public void setDirectDebitStatus(String directDebitStatus) {
		this.directDebitStatus = directDebitStatus;
	}
	public String getDirectDebitMode() {
		return directDebitMode;
	}
	public void setDirectDebitMode(String directDebitMode) {
		this.directDebitMode = directDebitMode;
	}
	public String getDirectDebitBankNo() {
		return directDebitBankNo;
	}
	public void setDirectDebitBankNo(String directDebitBankNo) {
		this.directDebitBankNo = directDebitBankNo;
	}
	public String getDirectDebitAccountNo() {
		return directDebitAccountNo;
	}
	public void setDirectDebitAccountNo(String directDebitAccountNo) {
		this.directDebitAccountNo = directDebitAccountNo;
	}
	public String getProgramDesc() {
		return programDesc;
	}
	public void setProgramDesc(String programDesc) {
		this.programDesc = programDesc;
	}
	public String getOperationMode() {
		return operationMode;
	}
	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}
	public String getCycleModel() {
		return cycleModel;
	}
	public void setCycleModel(String cycleModel) {
		this.cycleModel = cycleModel;
	}
    
    
}
