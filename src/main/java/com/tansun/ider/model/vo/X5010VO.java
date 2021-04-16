package com.tansun.ider.model.vo;

public class X5010VO {

	private String returnCode;
	private String productUnitCode;
	private String productObjectCode;
	
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getProductUnitCode() {
		return productUnitCode;
	}
	public void setProductUnitCode(String productUnitCode) {
		this.productUnitCode = productUnitCode;
	}
	public String getProductObjectCode() {
		return productObjectCode;
	}
	public void setProductObjectCode(String productObjectCode) {
		this.productObjectCode = productObjectCode;
	}
	
	@Override
	public String toString() {
		return "X5010VO [returnCode=" + returnCode + ", productUnitCode=" + productUnitCode + ", productObjectCode="
				+ productObjectCode + "]";
	}
	
}
