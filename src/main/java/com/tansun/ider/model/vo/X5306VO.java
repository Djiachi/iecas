package com.tansun.ider.model.vo;

public class X5306VO {
	
	/** 操作员Id */
	private String operatorId;
	/** 跨组织标识 Y表示支持跨组织 [1,0] */
	protected String crossOrganFlag;
	/** 副客户代码 [12,0] */
	protected String subCustomerNo;
	/** 上一有效期 MMYY [4,0] */
	protected String previousExpirationDate;
	/** 状态 1：新发 2：活跃 3：非活跃 4：已转出 8：关闭 9：待删除 [1,0] */
	protected String statusCode;
	
	//------------------媒介单元基本信息------------------
	/** 媒介单元代码 [18,0] Not NULL */
    protected String mediaUnitCode;
    /** 媒介对象 [9,0] Not NULL */
    protected String mediaObjectCode;
    /** 产品对象代码 [9,0] Not NULL */
    protected String productObjectCode;
    /** 外部识别号 [32,0] Not NULL */
    protected String externalIdentificationNo;
    /** 主客户代码 [12,0] Not NULL */
    protected String mainCustomerNo;
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
    /** 密码函领取标志 [1,0] */
    protected String pinDispatchMethod;
    /** 媒介领取标志 [1,0] */
    protected String mediaDispatchMethod;
    /** 激活状态标识 1：已激活 2：新发卡未激活 3：毁卡补发/续卡未激活 4：转卡未激活  5： 无需激活 6: 提前续卡为激活 [1,0] */
    protected String activationFlag;
    /** 激活日期 [10,0] */
    protected String activationDate;
    /** 激活方式 1：人工激活 2：无须激活 [1,0] */
    protected String activationMode;
    /** 新建日期 [10,0] */
    protected String createDate;
    /** 产品形式代码 [18,0] */
    protected String productForm;
    /** 是否有效标识 Y：有效 N : 无效 [1,0] */
    protected String invalidFlag;
    /** 失效原因字段 TRF：转卡 EXP：到期 BRK：毁损 CLS：关闭  RNA:续卡未激活 [3,0] */
    protected String invalidReason;
    /** 上一活动日期 [10,0] */
    protected String prevActivityDate;
    /** 创建时间 yyyy-MM-dd HH:mm:ss [23,0] */
    protected String gmtCreate;
    
	//------------------媒介制卡信息------------------
    /** 凸字信息 [26,0] */
    protected String embosserName1;
    /** 制卡请求 0：无请求 1：新发卡制卡 2：到期续卡制卡 3：毁损补发制卡 4：挂失换卡制卡 5：提前续卡制卡 [1,0] Not NULL */
    protected String productionCode;
    /** 制卡请求日期 yyyy-MM-dd [10,0] */
    protected String productionDate;
    /** 上一制卡请求 [1,0] */
    protected String previousProductionCode;
    /** 上一制卡请求日期 [10,0] */
    protected String previousProductionDate;
    /** 卡版的样式代码 [4,0] */
    protected String formatCode;
    /** 地址类型 [2,0] */
    protected String addressType;
    
    //------------------产品形式表------------------
    /** 产品持有人代码 [12,0] Not NULL */
    protected String productHolderNo;
    /** 媒介形式 R ： 实体  V：虚拟 [1,0] Not NULL */
    protected String mediaObjectForm;
    /** 媒介持有人代码 [12,0] Not NULL */
    protected String mediaHolderNo;
    /** 年费日期 [10,0] */
    protected String annualFeeDate;
    /** 首年免年费日期 [10,0] */
    protected String waiveFirstAnnualFeeDate;
    /** 上一年费收取周期号 [10,0] */
    protected Integer lastAnnualFeeCycleNo;
    
	//------------------媒介对象表------------------
    /** 媒介对象描述 [9,0] Not NULL */
    protected String mediaObjectDesc;
    /** 媒介类型:M：磁条卡 C：芯片卡  V：虚拟卡 [1,0] */
    protected String mediaObjectType;
    
    //------------------法人实体表------------------
    /** 法人实体编号 [10,0] Not NULL */
    protected String corporationEntityNo;
    /** 法人实体名称 [50,0] */
    protected String corporationEntityName;
    /** 系统单元编号 [3,0] */
    protected String systemUnitNo;
    
    //------------------产品对象表------------------
    /** 产品描述 [50,0] */
    protected String productDesc;
    /** 发行卡BIN [10,0] */
    protected Integer binNo;
    /** 还款优先级，数值越小优先级越高 [10,0] */
    protected Integer paymentPriority;
    /** 特殊号码段号 [10,0] */
    protected Integer segmentNumber;
    /** 产品最大期限 [10,0] */
    protected Integer productTimeLimit;
    
	//------------------发行卡BIN表------------------
    /** 卡组织 : V：VISA M：Mastercard J:JCB C:CUP A:AMEX [1,0] Not NULL */
    protected String cardScheme;
    /** 卡类型 [2,0] */
    protected String cardTyp;
    /** 清算币种 [3,0] Not NULL */
    protected String settlementCurrency;
    /**  [19,0] Not NULL */
    protected Long nextSerialNo;	
    
	//----------------------------------------------------------------------
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

}
