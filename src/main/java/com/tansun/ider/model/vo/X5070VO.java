package com.tansun.ider.model.vo;

public class X5070VO {

	private String idType;
	private String idNumber;
	/** 是否有效标识 */
	private String invalidFlag;
	/** 失效原因字段 */
	private String invalidReason;
	/** 媒介外部识别号 */
	private String externalIdentificationNo;
	/** 产品对象号 */
	private String productObjectCode;
	/** 主附标识 1：主卡 2：附属卡 [1,0] Not NULL */
	private String mainSupplyIndicator;
	/** 状态 1：新发 2：活跃 3：非活跃 4：已转出 8：关闭 9：待删除 [1,0] */
	private String statusCode;
	/** 媒介类型是否可转卡 */
	private String transferCard;
    /** 媒介单元代码 [18,0] Not NULL */
    protected String mediaUnitCode;
    protected String mediaObjectCode;
    /** 媒介使用者姓名 [30,0] Not NULL */
    protected String mediaUserName;
    /** 激活状态标识 1：已激活 2：新发卡未激活 3：续卡未激活 4：转卡未激活 [1,0] */
    protected String activationFlag;
    /** 产品描述 [50,0] Not NULL */
    private String ProductDesc;
    /** 媒介对象描述 [50,0] Not NULL */
    private String mediaObjectDesc;
    
	public X5070VO() {
		super();
	}

	public X5070VO(String externalIdentificationNo, String mediaObjectCode, String productObjectCode,
			String mainSupplyIndicator, String statusCode, String transferCard) {
		super();
		this.externalIdentificationNo = externalIdentificationNo;
		this.mediaObjectCode = mediaObjectCode;
		this.productObjectCode = productObjectCode;
		this.mainSupplyIndicator = mainSupplyIndicator;
		this.statusCode = statusCode;
		this.transferCard = transferCard;
	}
	
	public String getInvalidFlag() {
		return invalidFlag;
	}

	public void setInvalidFlag(String invalidFlag) {
		this.invalidFlag = invalidFlag;
	}

	public String getInvalidReason() {
		return invalidReason;
	}

	public void setInvalidReason(String invalidReason) {
		this.invalidReason = invalidReason;
	}

	public String getMediaUnitCode() {
		return mediaUnitCode;
	}

	public void setMediaUnitCode(String mediaUnitCode) {
		this.mediaUnitCode = mediaUnitCode;
	}

	public String getMediaUserName() {
		return mediaUserName;
	}

	public void setMediaUserName(String mediaUserName) {
		this.mediaUserName = mediaUserName;
	}

	public String getActivationFlag() {
		return activationFlag;
	}

	public void setActivationFlag(String activationFlag) {
		this.activationFlag = activationFlag;
	}

	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}

	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}

	public String getMediaObjectCode() {
		return mediaObjectCode;
	}

	public void setMediaObjectCode(String mediaObjectCode) {
		this.mediaObjectCode = mediaObjectCode;
	}

	public String getProductObjectCode() {
		return productObjectCode;
	}

	public void setProductObjectCode(String productObjectCode) {
		this.productObjectCode = productObjectCode;
	}

	public String getMainSupplyIndicator() {
		return mainSupplyIndicator;
	}

	public void setMainSupplyIndicator(String mainSupplyIndicator) {
		this.mainSupplyIndicator = mainSupplyIndicator;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getTransferCard() {
		return transferCard;
	}

	public void setTransferCard(String transferCard) {
		this.transferCard = transferCard;
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
		return "X5070VO [externalIdentificationNo=" + externalIdentificationNo + ", mediaObjectCode=" + mediaObjectCode
				+ ", productObjectCode=" + productObjectCode + ", mainSupplyIndicator=" + mainSupplyIndicator
				+ ", statusCode=" + statusCode + ", transferCard=" + transferCard + "]";
	}

	public String getProductDesc() {
		return ProductDesc;
	}

	public void setProductDesc(String productDesc) {
		ProductDesc = productDesc;
	}

	public String getMediaObjectDesc() {
		return mediaObjectDesc;
	}

	public void setMediaObjectDesc(String mediaObjectDesc) {
		this.mediaObjectDesc = mediaObjectDesc;
	}

}
