package com.tansun.ider.model.vo;

import java.util.Date;

/**
 * @Desc:
 * @Author wt
 * @Date 2018年5月3日 上午10:43:23
 */
public class X5125VO_ {

	/** 媒介单元代码 [18,0] Not NULL */
	private String mediaUnitCode;
	/** 媒介对象 [9,0] Not NULL */
	private String mediaObject;
	/** 产品对象代码 [9,0] Not NULL */
	private String productObjectCode;
	/** 外部识别号 [19,0] Not NULL */
	private String externalIdentificationNo;
	/** 主客户代码 [12,0] Not NULL */
	private String mainCustomerCode;
	/** 副客户代码 [12,0] */
	private String subCustomerCode;
	/** 所属机构 [10,0] Not NULL */
	private String affiliatedInstitutions;
	/** 运营模式 [3,0] Not NULL */
	private String operationMode;
	/** 主附标识 [1,0] Not NULL */
	private String mainAttachment;
	/** 媒介使用者姓名 [30,0] Not NULL */
	private String mediaUserName;
	/** 进件人员编号 [8,0] */
	private String numberStaffMembers;
	/** 分行号 [9,0] */
	private String branchNumber;
	/** 申请书编号 [13,0] */
	private String applicationNumber;
	/** 营销人员编号 [8,0] */
	private String salesmanNumber;
	/** 有效期 [10,0] */
	private String termValidity;
	/** 上一有效期 [10,0] */
	private String lastEffectivePeriod;
	/** 密码函领取标志 [1,0] */
	private String cryptographicSign;
	/** 媒介领取标志 [1,0] */
	private String mediaMark;
	/** 状态 [1,0] */
	private String state;
	/** 制卡信息一 [26,0] */
	protected String cardMakingInformation;
	/** 制卡信息二 [26,0] */
	protected String cardMakingInformationTwo;
	/** 制卡信息三 [26,0] */
	protected String cardMakingInformationThree;
	/** 制卡信息四 [26,0] */
	protected String cardMakingInformationFour;
	/** 制卡请求 [1,0] Not NULL */
	protected String requestCardMaking;
	/** 制卡请求日期 [8,0] */
	protected String dateRequestCardMaking;
	/** 上一制卡请求 [1,0] */
	protected String firstCardRequest;
	/** 上一制卡请求日期 [8,0] */
	protected String requestPreviousCard;
	/** 卡版代码 [6,0] */
	protected String blockCode;
	/** 标签号 [9,0] */
	protected String labelNumber;
	/** 生效日期 [19,0] */
	protected Date effectiveDate;
	/** 失效日期 [19,0] */
	protected Date expirationDate;
	/** 转入介质代码 [18,0] */
	protected String transferIntoMediaCode;
	/** 转出介质代码 [18,0] */
	protected String transferOutMediaCode;
	/** 转入日期 [19,0] */
	protected Date transferIntoDate;
	/** 转出日期 [19,0] */
	protected Date transferOutDate;
	/** 阶段类型 [1,0] Not NULL */
	protected String stageType;
	/** 转出媒介代码 [18,0] */
	protected String transferMediaCode;
	/** 媒介对象代码 [9,0] Not NULL */
	protected String mediaObjectCode;
	/** 媒介对象描述 [9,0] Not NULL */
	protected String mediaObjectMsg;
	/**  激活日期 [9,0] Not NULL */
    protected String activeDate;
    /** 激活状态  [9,0] Not NULL */
    protected String activeFlag;
    /** 证件号  [9,0] Not NULL */
    protected String credentialNumber;
    /** 卡组织 */
	private String cardAssociations;
	/** 卡版代号 */
	private String cardCode;
	/** 实时制卡标识 */
	protected String makeCardSigns;
    /** 新建日期 */
	protected String createDate;
	/** 刻印名*/
	protected String info1;
	
	
	public String getInfo1() {
		return info1;
	}

	public void setInfo1(String info1) {
		this.info1 = info1;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCardAssociations() {
		return cardAssociations;
	}

	public void setCardAssociations(String cardAssociations) {
		this.cardAssociations = cardAssociations;
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public String getMakeCardSigns() {
		return makeCardSigns;
	}

	public void setMakeCardSigns(String makeCardSigns) {
		this.makeCardSigns = makeCardSigns;
	}

	public String getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(String activeDate) {
        this.activeDate = activeDate;
    }

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }

    public String getMediaUnitCode() {
		return mediaUnitCode;
	}

	public void setMediaUnitCode(String mediaUnitCode) {
		this.mediaUnitCode = mediaUnitCode;
	}

	public String getMediaObject() {
		return mediaObject;
	}

	public void setMediaObject(String mediaObject) {
		this.mediaObject = mediaObject;
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

	public String getMainCustomerCode() {
		return mainCustomerCode;
	}

	public void setMainCustomerCode(String mainCustomerCode) {
		this.mainCustomerCode = mainCustomerCode;
	}

	public String getSubCustomerCode() {
		return subCustomerCode;
	}

	public void setSubCustomerCode(String subCustomerCode) {
		this.subCustomerCode = subCustomerCode;
	}

	public String getAffiliatedInstitutions() {
		return affiliatedInstitutions;
	}

	public void setAffiliatedInstitutions(String affiliatedInstitutions) {
		this.affiliatedInstitutions = affiliatedInstitutions;
	}

	public String getOperationMode() {
		return operationMode;
	}

	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}

	public String getMainAttachment() {
		return mainAttachment;
	}

	public void setMainAttachment(String mainAttachment) {
		this.mainAttachment = mainAttachment;
	}

	public String getMediaUserName() {
		return mediaUserName;
	}

	public void setMediaUserName(String mediaUserName) {
		this.mediaUserName = mediaUserName;
	}

	public String getNumberStaffMembers() {
		return numberStaffMembers;
	}

	public void setNumberStaffMembers(String numberStaffMembers) {
		this.numberStaffMembers = numberStaffMembers;
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

	public String getSalesmanNumber() {
		return salesmanNumber;
	}

	public void setSalesmanNumber(String salesmanNumber) {
		this.salesmanNumber = salesmanNumber;
	}

	public String getTermValidity() {
        return termValidity;
    }

    public void setTermValidity(String termValidity) {
        this.termValidity = termValidity;
    }

	public String getLastEffectivePeriod() {
        return lastEffectivePeriod;
    }

    public void setLastEffectivePeriod(String lastEffectivePeriod) {
        this.lastEffectivePeriod = lastEffectivePeriod;
    }

    public String getCryptographicSign() {
		return cryptographicSign;
	}

	public void setCryptographicSign(String cryptographicSign) {
		this.cryptographicSign = cryptographicSign;
	}

	public String getMediaMark() {
		return mediaMark;
	}

	public void setMediaMark(String mediaMark) {
		this.mediaMark = mediaMark;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCardMakingInformation() {
		return cardMakingInformation;
	}

	public void setCardMakingInformation(String cardMakingInformation) {
		this.cardMakingInformation = cardMakingInformation;
	}

	public String getCardMakingInformationTwo() {
		return cardMakingInformationTwo;
	}

	public void setCardMakingInformationTwo(String cardMakingInformationTwo) {
		this.cardMakingInformationTwo = cardMakingInformationTwo;
	}

	public String getCardMakingInformationThree() {
		return cardMakingInformationThree;
	}

	public void setCardMakingInformationThree(String cardMakingInformationThree) {
		this.cardMakingInformationThree = cardMakingInformationThree;
	}

	public String getCardMakingInformationFour() {
		return cardMakingInformationFour;
	}

	public void setCardMakingInformationFour(String cardMakingInformationFour) {
		this.cardMakingInformationFour = cardMakingInformationFour;
	}

	public String getRequestCardMaking() {
		return requestCardMaking;
	}

	public void setRequestCardMaking(String requestCardMaking) {
		this.requestCardMaking = requestCardMaking;
	}

	public String getDateRequestCardMaking() {
		return dateRequestCardMaking;
	}

	public void setDateRequestCardMaking(String dateRequestCardMaking) {
		this.dateRequestCardMaking = dateRequestCardMaking;
	}

	public String getFirstCardRequest() {
		return firstCardRequest;
	}

	public void setFirstCardRequest(String firstCardRequest) {
		this.firstCardRequest = firstCardRequest;
	}

	public String getRequestPreviousCard() {
		return requestPreviousCard;
	}

	public void setRequestPreviousCard(String requestPreviousCard) {
		this.requestPreviousCard = requestPreviousCard;
	}

	public String getBlockCode() {
		return blockCode;
	}

	public void setBlockCode(String blockCode) {
		this.blockCode = blockCode;
	}

	public String getLabelNumber() {
		return labelNumber;
	}

	public void setLabelNumber(String labelNumber) {
		this.labelNumber = labelNumber;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getTransferIntoMediaCode() {
		return transferIntoMediaCode;
	}

	public void setTransferIntoMediaCode(String transferIntoMediaCode) {
		this.transferIntoMediaCode = transferIntoMediaCode;
	}

	public String getTransferOutMediaCode() {
		return transferOutMediaCode;
	}

	public void setTransferOutMediaCode(String transferOutMediaCode) {
		this.transferOutMediaCode = transferOutMediaCode;
	}

	public Date getTransferIntoDate() {
		return transferIntoDate;
	}

	public void setTransferIntoDate(Date transferIntoDate) {
		this.transferIntoDate = transferIntoDate;
	}

	public Date getTransferOutDate() {
		return transferOutDate;
	}

	public void setTransferOutDate(Date transferOutDate) {
		this.transferOutDate = transferOutDate;
	}

	public String getStageType() {
		return stageType;
	}

	public void setStageType(String stageType) {
		this.stageType = stageType;
	}

	public String getTransferMediaCode() {
		return transferMediaCode;
	}

	public void setTransferMediaCode(String transferMediaCode) {
		this.transferMediaCode = transferMediaCode;
	}

	public String getMediaObjectCode() {
		return mediaObjectCode;
	}

	public void setMediaObjectCode(String mediaObjectCode) {
		this.mediaObjectCode = mediaObjectCode;
	}

	public String getMediaObjectMsg() {
		return mediaObjectMsg;
	}

	public void setMediaObjectMsg(String mediaObjectMsg) {
		this.mediaObjectMsg = mediaObjectMsg;
	}

	public String getCredentialNumber() {
		return credentialNumber;
	}

	public void setCredentialNumber(String credentialNumber) {
		this.credentialNumber = credentialNumber;
	}

	@Override
	public String toString() {
		return "X5125VO [mediaUnitCode=" + mediaUnitCode + ", mediaObject=" + mediaObject + ", productObjectCode="
				+ productObjectCode + ", externalIdentificationNo=" + externalIdentificationNo + ", mainCustomerCode="
				+ mainCustomerCode + ", subCustomerCode=" + subCustomerCode + ", affiliatedInstitutions="
				+ affiliatedInstitutions + ", operationMode=" + operationMode + ", mainAttachment=" + mainAttachment
				+ ", mediaUserName=" + mediaUserName + ", numberStaffMembers=" + numberStaffMembers + ", branchNumber="
				+ branchNumber + ", applicationNumber=" + applicationNumber + ", salesmanNumber=" + salesmanNumber
				+ ", termValidity=" + termValidity + ", lastEffectivePeriod=" + lastEffectivePeriod
				+ ", cryptographicSign=" + cryptographicSign + ", mediaMark=" + mediaMark + ", state=" + state
				+ ", cardMakingInformation=" + cardMakingInformation + ", cardMakingInformationTwo="
				+ cardMakingInformationTwo + ", cardMakingInformationThree=" + cardMakingInformationThree
				+ ", cardMakingInformationFour=" + cardMakingInformationFour + ", requestCardMaking="
				+ requestCardMaking + ", dateRequestCardMaking=" + dateRequestCardMaking + ", firstCardRequest="
				+ firstCardRequest + ", requestPreviousCard=" + requestPreviousCard + ", blockCode=" + blockCode
				+ ", labelNumber=" + labelNumber + ", effectiveDate=" + effectiveDate + ", expirationDate="
				+ expirationDate + ", transferIntoMediaCode=" + transferIntoMediaCode + ", transferOutMediaCode="
				+ transferOutMediaCode + ", transferIntoDate=" + transferIntoDate + ", transferOutDate="
				+ transferOutDate + ", stageType=" + stageType + ", transferMediaCode=" + transferMediaCode
				+ ", mediaObjectCode=" + mediaObjectCode + ", mediaObjectMsg=" + mediaObjectMsg + ", activeDate="
				+ activeDate + ", activeFlag=" + activeFlag + ", credentialNumber=" + credentialNumber
				+ ", cardAssociations=" + cardAssociations + ", cardCode=" + cardCode + ", makeCardSigns="
				+ makeCardSigns + "]";
	}

}
