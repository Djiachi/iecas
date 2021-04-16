package com.tansun.ider.model.vo;

/**
 * @Desc:产品信息查询
 * @Author wt
 * @Date 2018年5月2日 上午10:55:34
 */
public class X5120VO {

	
	  /** 产品类型 CRD: 信用卡产品 RLN:消费信贷产品 [3,0] */
    protected String productType;
	/** 运营模式 [3,0] Not NULL */
    protected String operationMode;
    /** 产品对象代码 [9,0] Not NULL */
    protected String productObjectCode;
    /** 产品描述 [50,0] */
    protected String productDesc;
    /** 产品线代码 [9,0] Not NULL */
    protected String productLineCode;
    /** 发行卡BIN [10,0] */
    protected Integer binNo;
    /** 还款优先级，数值越小优先级越高 [10,0] */
    protected Integer paymentPriority;
    /** 产品单元代码 [18,0] Not NULL */
    protected String productUnitCode;
    /** 联名号 [20,0] */
    protected String coBrandedNo;
    /** 客户号 */
    private String customerNo;
    /** 状态 1：正常,8：关闭 [1,0] */
    protected String statusCode;
    //申请日期
    private String createDate;
    //注销日期
    private String productCancelDate;
    
    protected String id;
    
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getOperationMode() {
		return operationMode;
	}
	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}
	public String getProductObjectCode() {
		return productObjectCode;
	}
	public void setProductObjectCode(String productObjectCode) {
		this.productObjectCode = productObjectCode;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public String getProductLineCode() {
		return productLineCode;
	}
	public void setProductLineCode(String productLineCode) {
		this.productLineCode = productLineCode;
	}
	public Integer getBinNo() {
		return binNo;
	}
	public void setBinNo(Integer binNo) {
		this.binNo = binNo;
	}
	public Integer getPaymentPriority() {
		return paymentPriority;
	}
	public void setPaymentPriority(Integer paymentPriority) {
		this.paymentPriority = paymentPriority;
	}
	public String getProductUnitCode() {
		return productUnitCode;
	}
	public void setProductUnitCode(String productUnitCode) {
		this.productUnitCode = productUnitCode;
	}
	public String getCoBrandedNo() {
		return coBrandedNo;
	}
	public void setCoBrandedNo(String coBrandedNo) {
		this.coBrandedNo = coBrandedNo;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	public X5120VO(String operationMode, String productObjectCode, String productDesc, String productLineCode,
			Integer binNo, Integer paymentPriority, String productUnitCode, String coBrandedNo) {
		super();
		this.operationMode = operationMode;
		this.productObjectCode = productObjectCode;
		this.productDesc = productDesc;
		this.productLineCode = productLineCode;
		this.binNo = binNo;
		this.paymentPriority = paymentPriority;
		this.productUnitCode = productUnitCode;
		this.coBrandedNo = coBrandedNo;
	}
	@Override
	public String toString() {
		return "X5120VO [operationMode=" + operationMode + ", productObjectCode=" + productObjectCode + ", productDesc="
				+ productDesc + ", productLineCode=" + productLineCode + ", binNo=" + binNo + ", paymentPriority="
				+ paymentPriority + ", productUnitCode=" + productUnitCode + ", coBrandedNo=" + coBrandedNo + "]";
	}
	public X5120VO() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getProductCancelDate() {
		return productCancelDate;
	}
	public void setProductCancelDate(String productCancelDate) {
		this.productCancelDate = productCancelDate;
	}
    
}
