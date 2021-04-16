package com.tansun.ider.model.vo;

public class X5305VO {

	/** 激活方式 1：人工激活 2：无须激活 [1,0] */
	protected String activationMode;
	/** 操作员Id */
	private String operatorId;
	/** 产品描述 [50,0] */
	protected String productDesc;
	/** 产品形式代码 */
	private String productForm;
	/** 是否有效标识 */
	private String invalidFlag;
	/** 失效原因字段 */
	private String invalidReason;
	/** 产品持有人代码 [12,0] Not NULL */
	protected String productHolderNo;
	/** 媒介形式 R ： 实体 V：虚拟 [1,0] Not NULL */
	protected String mediaObjectForm;
	/** 媒介持有人代码 [12,0] Not NULL */
	protected String mediaHolderNo;
	/** 上一年费收取周期号 [10,0] */
	protected Integer lastAnnualFeeCycleNo;
	/** 发行卡BIN [10,0] Not NULL */
	protected Integer binNo;
	/** 卡类型 [2,0] */
	protected String cardTyp;
	/** 清算币种 [3,0] Not NULL */
	protected String settlementCurrency;
	/** 跨组织标识 Y表示支持跨组织 [1,0] */
	protected String crossOrganFlag;
	/** 制卡请求 0：无请求 1：新发卡制卡 2：到期续卡制卡 3：毁损补发制卡 4：挂失换卡制卡 [1,0] Not NULL */
	protected String productionCode;
	/** 制卡请求日期 yyyy-MM-dd [10,0] */
	protected String productionDate;
	/** 上一制卡请求 [1,0] */
	protected String previousProductionCode;
	/** 上一制卡请求日期 [10,0] */
	protected String previousProductionDate;
	/** 地址类型 [2,0] */
	protected String addressType;
	/** 新建日期 [10,0] */
	protected String createDate;
	/** 年费日期 [10,0] */
	protected String annualFeeDate;
	/** 免首年年费日期 [10,0] */
	protected String waiveFirstAnnualFeeDate;
	/** 媒介类型:M：磁条卡 C：芯片卡 V：虚拟卡 [1,0] */
	protected String mediaObjectType;
	/** 卡组织 : V：VISA M：Mastercard J:JCB C:CUP A:AMEX [1,0] Not NULL */
	protected String cardScheme;
	/** 卡板代码 */
	private String formatCode;
	/** 刻印名 */
	private String embosserName1;
	/** 媒介单元代码 [18,0] Not NULL */
	protected String mediaUnitCode;
	/** 媒介对象 [9,0] Not NULL */
	protected String mediaObjectCode;
	/** 产品对象代码 [9,0] Not NULL */
	protected String productObjectCode;
	/** 外部识别号 [19,0] Not NULL */
	protected String externalIdentificationNo;
	/** 主客户代码 [12,0] Not NULL */
	protected String mainCustomerNo;
	/** 副客户代码 [12,0] */
	protected String subCustomerNo;
	/** 所属机构 [9,0] Not NULL */
	protected String institutionId;
	/** 运营模式 [3,0] Not NULL */
	protected String operationMode;
	/** 主附标识 1：主卡 2：附属卡 [1,0] Not NULL */
	protected String mainSupplyIndicator;
	/** 媒介使用者姓名 [30,0] Not NULL */
	protected String mediaUserName;
	/** 进件人员编号 [8,0] */
	protected String applicationStaffNo;
	/** 分行号 : 分行号 [9,0] */
	protected String branchNumber;
	/** 申请书编号 [13,0] */
	protected String applicationNumber;
	/** 营销人员编号 [9,0] */
	protected String marketerCode;
	/** 有效期 MMYY [4,0] */
	protected String expirationDate;
	/** 上一有效期 MMYY [4,0] */
	protected String previousExpirationDate;
	/** 密码函领取标志 [1,0] */
	protected String pinDispatchMethod;
	/** 媒介领取标志 [1,0] */
	protected String mediaDispatchMethod;
	/** 状态 1：新发 2：活跃 3：非活跃 4：已转出 8：关闭 9：待删除 [1,0] */
	protected String statusCode;
	/** 激活状态标识 1：已激活 2：新发卡未激活 3：续卡未激活 4：转卡未激活 [1,0] */
	protected String activationFlag;
	/** 激活日期 [10,0] */
	protected String activationDate;
	/** 媒介对象描述 */
	private String mediaObjectDesc;
	/** 所属机构 */
	private String organName;
	
	public String getCardScheme() {
		return cardScheme;
	}

	public void setCardScheme(String cardScheme) {
		this.cardScheme = cardScheme;
	}

	public String getFormatCode() {
		return formatCode;
	}

	public void setFormatCode(String formatCode) {
		this.formatCode = formatCode;
	}

	public String getEmbosserName1() {
		return embosserName1;
	}

	public void setEmbosserName1(String embosserName1) {
		this.embosserName1 = embosserName1;
	}

	public String getMediaUnitCode() {
		return mediaUnitCode;
	}

	public void setMediaUnitCode(String mediaUnitCode) {
		this.mediaUnitCode = mediaUnitCode;
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

	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}

	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}

	public String getMainCustomerNo() {
		return mainCustomerNo;
	}

	public void setMainCustomerNo(String mainCustomerNo) {
		this.mainCustomerNo = mainCustomerNo;
	}

	public String getSubCustomerNo() {
		return subCustomerNo;
	}

	public void setSubCustomerNo(String subCustomerNo) {
		this.subCustomerNo = subCustomerNo;
	}

	public String getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(String institutionId) {
		this.institutionId = institutionId;
	}

	public String getOperationMode() {
		return operationMode;
	}

	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}

	public String getMainSupplyIndicator() {
		return mainSupplyIndicator;
	}

	public void setMainSupplyIndicator(String mainSupplyIndicator) {
		this.mainSupplyIndicator = mainSupplyIndicator;
	}

	public String getMediaUserName() {
		return mediaUserName;
	}

	public void setMediaUserName(String mediaUserName) {
		this.mediaUserName = mediaUserName;
	}

	public String getApplicationStaffNo() {
		return applicationStaffNo;
	}

	public void setApplicationStaffNo(String applicationStaffNo) {
		this.applicationStaffNo = applicationStaffNo;
	}

	public String getBranchNumber() {
		return branchNumber;
	}

	public void setBranchNumber(String branchNumber) {
		this.branchNumber = branchNumber;
	}

	public String getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	public String getMarketerCode() {
		return marketerCode;
	}

	public void setMarketerCode(String marketerCode) {
		this.marketerCode = marketerCode;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getPreviousExpirationDate() {
		return previousExpirationDate;
	}

	public void setPreviousExpirationDate(String previousExpirationDate) {
		this.previousExpirationDate = previousExpirationDate;
	}

	public String getPinDispatchMethod() {
		return pinDispatchMethod;
	}

	public void setPinDispatchMethod(String pinDispatchMethod) {
		this.pinDispatchMethod = pinDispatchMethod;
	}

	public String getMediaDispatchMethod() {
		return mediaDispatchMethod;
	}

	public void setMediaDispatchMethod(String mediaDispatchMethod) {
		this.mediaDispatchMethod = mediaDispatchMethod;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getActivationFlag() {
		return activationFlag;
	}

	public void setActivationFlag(String activationFlag) {
		this.activationFlag = activationFlag;
	}

	public String getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(String activationDate) {
		this.activationDate = activationDate;
	}

	public String getMediaObjectDesc() {
		return mediaObjectDesc;
	}

	public void setMediaObjectDesc(String mediaObjectDesc) {
		this.mediaObjectDesc = mediaObjectDesc;
	}

	public String getMediaObjectType() {
		return mediaObjectType;
	}

	public void setMediaObjectType(String mediaObjectType) {
		this.mediaObjectType = mediaObjectType;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getAnnualFeeDate() {
		return annualFeeDate;
	}

	public void setAnnualFeeDate(String annualFeeDate) {
		this.annualFeeDate = annualFeeDate;
	}

	public String getWaiveFirstAnnualFeeDate() {
		return waiveFirstAnnualFeeDate;
	}

	public void setWaiveFirstAnnualFeeDate(String waiveFirstAnnualFeeDate) {
		this.waiveFirstAnnualFeeDate = waiveFirstAnnualFeeDate;
	}

	public String getProductionCode() {
		return productionCode;
	}

	public void setProductionCode(String productionCode) {
		this.productionCode = productionCode;
	}

	public String getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}

	public String getPreviousProductionCode() {
		return previousProductionCode;
	}

	public void setPreviousProductionCode(String previousProductionCode) {
		this.previousProductionCode = previousProductionCode;
	}

	public String getPreviousProductionDate() {
		return previousProductionDate;
	}

	public void setPreviousProductionDate(String previousProductionDate) {
		this.previousProductionDate = previousProductionDate;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public Integer getBinNo() {
		return binNo;
	}

	public void setBinNo(Integer binNo) {
		this.binNo = binNo;
	}

	public String getCardTyp() {
		return cardTyp;
	}

	public void setCardTyp(String cardTyp) {
		this.cardTyp = cardTyp;
	}

	public String getSettlementCurrency() {
		return settlementCurrency;
	}

	public void setSettlementCurrency(String settlementCurrency) {
		this.settlementCurrency = settlementCurrency;
	}

	public String getCrossOrganFlag() {
		return crossOrganFlag;
	}

	public void setCrossOrganFlag(String crossOrganFlag) {
		this.crossOrganFlag = crossOrganFlag;
	}

	public String getProductHolderNo() {
		return productHolderNo;
	}

	public void setProductHolderNo(String productHolderNo) {
		this.productHolderNo = productHolderNo;
	}

	public String getMediaObjectForm() {
		return mediaObjectForm;
	}

	public void setMediaObjectForm(String mediaObjectForm) {
		this.mediaObjectForm = mediaObjectForm;
	}

	public String getMediaHolderNo() {
		return mediaHolderNo;
	}

	public void setMediaHolderNo(String mediaHolderNo) {
		this.mediaHolderNo = mediaHolderNo;
	}

	public Integer getLastAnnualFeeCycleNo() {
		return lastAnnualFeeCycleNo;
	}

	public void setLastAnnualFeeCycleNo(Integer lastAnnualFeeCycleNo) {
		this.lastAnnualFeeCycleNo = lastAnnualFeeCycleNo;
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

	public String getProductForm() {
		return productForm;
	}

	public void setProductForm(String productForm) {
		this.productForm = productForm;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getActivationMode() {
		return activationMode;
	}

	public void setActivationMode(String activationMode) {
		this.activationMode = activationMode;
	}

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }
}
