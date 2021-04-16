package com.tansun.ider.model.vo;

public class X5000VO {

	/* 媒介单元代码 */
	private String returnCode;
	/* 客户号 */
	private String customerNo;

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	@Override
	public String toString() {
		return "X5000VO [returnCode=" + returnCode + ", customerNo=" + customerNo + "]";
	}

}
