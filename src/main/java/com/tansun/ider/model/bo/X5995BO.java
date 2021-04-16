package com.tansun.ider.model.bo;

import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.dao.issue.entity.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 
* @ClassName: X5810BO 
* @Description: 强制结息（产品注销时实时结息）
* @author by
* @date 2019年8月19日 下午5:55:21 
*
 */
public class X5995BO{
     
	/** 活动与构件对应关系表 */
	CoreEventActivityRel coreEventActivityRel;
	/** 活动与构件对应关系表 */
	List<CoreActivityArtifactRel> activityArtifactList;
	/** 证件类型 */
	private String idType;
	/** 证件号码 */
	private String idNumber;
	/** 外部识别号 [19,0] Not NULL */
	private String externalIdentificationNo;
	/** 客户号 */
	private String customerNo;
	/** 产品对象代码 */
	private String productObjectCode;
	/** 账户代码 [30,0] Not NULL */
	private String accountId;
	
	/** 当前日志标志   A/B [1,0] */
    protected String currLogFlag;
	/** 操作员Id */
	private String operatorId;
	 /** 事件编号 [14,0] Not NULL */
    protected String eventNo;
    /** 活动编号 [8,0] Not NULL */
    protected String activityNo;
	/** 全局事件编号 */
	private String globalEventNo;
	/** 赋予初始值 */
	private String ecommEventId; // 事件ID CRDPR40G000001
	
	/** 所属机构 */
	private String institutionId;
	/** 运行模式 */
	private String operationMode;
	/** 客户姓名 */
	private String customerName;
	
	/** 城市 */
	private String city;
	/** 个人公司标识 */
	private String customerType;
	/** 账单日 */
	private String billDay;
	/** 客户状态 */
	private String customerStatus;
	// 客户地址信息
	/** 地址信息 */
	private List<CoreCustomerAddr> coreCoreCustomerAddrs;
	// 客户备注信息
	/** 备注信息 */
	private String remarkInfo;
	/** 新增备注人员 */
	private String lastUpdateUserid;
	/** 序号 */
	private Integer serialNo;
	/** 新增备注日期 */
	private String lastUpdateDate;
	// 客户业务类型标签
	/** 币种 */
	private String currencyCode;
	/** 业务类型 */
	private String businessType;
	
	/** 定价区域 */
	private String pricingType;
	/** 标签号 */
	private String pricingTag;
	/** 标签生效日期 */
	private Date effectiveDate;
	/** 标签失效日期 */
	private Date tagExpirationDate;
	/** 余额类型 */
	private String balanceType;
	// 客户个人信息
	/** 分行号 */
	private String branchNumber;
	/** 生日 yyyy-MM-dd */
	private String dateOfBirth;
	/** 手机号码 */
	private String mobilePhone;
	/** 邮编 */
	private String zipCode;
	/** 家庭电话 */
	private String homePhone;
	/** 公司电话 */
	private String companyPhone;
	/** 联络人电话 */
	private String contactPhone;
	/** 联络人姓名 */
	private String contactName;
	/** 成为会员年份 */
	private Integer memberSince;
	/** 客户来源码 */
	private String customerSource;
	/** 行为得分 */
	private Integer behaviorScore;
	/** 客户等级 */
	private String customerLevel;
	/** 年收入 */
	private Integer annualIncome;
	/** 性别 */
	private String sexCode;
	/** 住宅性质 */
	private String residencyStatus;
	/** 婚姻状况 */
	private String maritalStatus;
	/** 工作行业代码 */
	private String occupationCode;
	/** 职务级别代码 */
	private String postRankCode;
	/** 职称代码 */
	private String titleCode;
	/** 工作年限 */
	private String periodOfOccupation;
	/** 兴趣爱好 */
	private String hobby;
	/** 担保人标识 */
	private String guarantorFlag;
	/** 营销员代码 */
	private String marketerCode;
	/** 持卡人的社会保障号类型 */
	private String socialSecurityId;
	/** 社保账号 */
	private String socialSecurityNumber;
	// 客户备注信息
	/** 备注信息 */
	private List<CoreCustomerRemarks> coreCustomerRemarkss;
	// 客户定价标签信息
	/** 定价标签信息 */
	private List<CoreCustomerBusinessType> coreCustomerBusinessTypes;
	// 产品单元基本信息
	/** 产品单元代码 [18,0] Not NULL */
	private String productUnitCode;
	/** 产品线代码 [9,0] */
	private String productLineCode;
	/** 入账币种1 [3,0] */
	private String postingCurrency1;
	/** 入账币种2 [3,0] */
	private String postingCurrency2;
	/** 入账币种3 [3,0] */
	private String postingCurrency3;
	/** 入账币种4 [3,0] */
	private String postingCurrency4;
	/** 入账币种5 [3,0] */
	private String postingCurrency5;
	/** 入账币种6 [3,0] */
	private String postingCurrency6;
	/** 入账币种7 [3,0] */
	private String postingCurrency7;
	/** 入账币种8 [3,0] */
	private String postingCurrency8;
	/** 入账币种9 [3,0] */
	private String postingCurrency9;
	/** 入账币种10 [3,0] */
	private String postingCurrency10;
	// 产品附加信息表
	/** 联名号 [20,0] */
	private String coBrandedNo;
	// 媒介单元基本信息表
	/** 媒介单元代码 [18,0] Not NULL */
	private String mediaUnitCode;
	/** 媒介对象 [9,0] Not NULL */
	private String mediaObjectCode;
	
	/** 主客户代码 [12,0] Not NULL */
	private String mainCustomerNo;
	/** 副客户代码 [12,0] */
	private String subCustomerNo;
	/** 主附标识 1：主卡 2：附属卡 [1,0] Not NULL */
	private String mainSupplyIndicator;
	/** 媒介使用者姓名 [30,0] Not NULL */
	private String mediaUserName;
	/** 进件人员编号 [8,0] */
	private String applicationStaffNo;
	/** 申请书编号 [13,0] */
	private String applicationNumber;
	/** 有效期 MMYY [4,0] */
	private String expirationDate;
	/** 上一有效期 MMYY [4,0] */
	private String previousExpirationDate;
	/** 密码函领取标志 [1,0] */
	private String pinDispatchMethod;
	/** 媒介领取标志 [1,0] */
	private String mediaDispatchMethod;
	/** 状态 1：新发 2：活跃 3：非活跃 4：已转出 8：关闭 9：待删除 [1,0] */
	private String statusCode;
	/** 激活状态标识 1：已激活 2：新发卡未激活 3：续卡未激活 4：转卡未激活 [1,0] */
	private String activationFlag;
	/** 激活日期 [10,0] */
	private String activationDate;
	/** 新建日期 [10,0] */
	private String createDate;
	// 媒介制卡信息
	/** 制卡信息一 [26,0] */
	private String embosserName1;
	/** 制卡信息二 [26,0] */
	private String embosserName2;
	/** 制卡信息三 [26,0] */
	private String embosserName3;
	/** 制卡信息四 [26,0] */
	private String embosserName4;
	/** 制卡请求 0：无请求 1：新发卡制卡 2：到期续卡制卡 3：毁损补发制卡 4：挂失换卡制卡 [1,0] Not NULL */
	private String productionCode;
	/** 制卡请求日期 yyyy-MM-dd [10,0] */
	private String productionDate;
	/** 上一制卡请求 [1,0] */
	private String previousProductionCode;
	/** 上一制卡请求日期 [8,0] */
	private String previousProductionDate;
	/** 卡版的样式代码 [2,0] */
	private String formatCode;
	/** 运营核算币种 [3,0] Not NULL */
	private String accountCurrency;
	/** 模式名称 [50,0] Not NULL */
	private String modeName;
	/** 上一处理日期 [10,0] */
	private String lastProcessDate;
	/** 当前处理日期 [10,0] */
	private String currProcessDate;
	/** 下一处理日期 [10,0] */
	private String nextProcessDate;
	/** 营业日期 [10,0] Not NULL */
	private String operationDate;
	/** 额度树编号 [3,0] */
	private String creditTreeId;
	/** 溢缴款业务类型 [9,0] */
	private String excessPymtBusinessType;
	// 媒介标签信息表
	/** 定价标签信息 */
	private List<CoreMediaLabelInfo> coreMediaLabelInfos;
	// 媒介绑定信息表
	/** 绑定识别号 [9,0] */
	private String bindId;
	/** 绑定日期 yyyy-MM-dd */
	private String bindDate;
	/** 解绑日期 yyyy-MM-dd */
	private String unbindDate;
	/** 产品线账单日表中 */
	private String nextBillDate;
	// 是否转卡
	private String isTransferCard;
	// 非金融日志记录 主键
	private String nonLogId;
	// Id 主键
	private String id;
	
	/** 所属业务类型 [15,0] */
	private String businessTypeCode;
	/** 所属机构号 [10,0] Not NULL */
	private String organNo;
	/** 账户组织形式 R：循环类账户 T：交易 [1,0] */
	private String accountOrganizeTyp;
	/** 账户性质 D：借记账户 C ： 贷记账户 [1,0] */
	private String businessDebitCreditCode;
	/** 周期模式标志 Y：周期模式 N：非周期模式 [1,0] */
	private String cycleModeFlag;
	/** 状态更新日期 [10,0] */
	private String statusUpdateDate;
	/** 关闭日期 [10,0] */
	private String closedDate;
	/** 计息处理日 [10,0] */
	private String interestProcessDate;
	/** 最后还款日 [10,0] */
	private String paymentDueDate;
	/** 滞纳金产生日 [10,0] */
	private String delinquencyDate;
	/** 核算状态码 [3,0] */
	private String accountingStatusCode;
	/** 核算状态日期 [10,0] */
	private String accountingStatusDate;
	/** 上一核算状态码 [3,0] */
	private String prevAccountingStatusCode;
	/** 上一核算状态日期 [10,0] */
	private String prevAccountingStatusDate;
	/** 约定扣款方式 1：账单余额 2：最低还款 [1,0] */
	private String directDebitType;
	/** 逾期状态 [1,0] */
	private String cycleDue;
	/** 上一逾期状态 [1,0] */
	private String prevCycleDue;
	/** ABS状态，资产担保证券或资产支撑证券（Asset-backed security） [2,0] */
	private String absStatus;
	/** 当前周期号 [10,0] */
	private Integer currentCycleNumber;
	/** 最低还款总额 [18,0] */
	private BigDecimal totalAmtDue;
	/** 升序，降序标签 */
	private String flag;
	/** 版本号 [10,0] Not NULL */
	protected Integer version;
	/** 套卡对方产品对象代码 [9,0] 只上POC*/
    protected String productCodeSet;
    private List<CoreAccount> coreAccountList;
    private String  corporation;
	public X5995BO() {
		super();
	}
	public String getCorporation() {
        return corporation;
    }

    public void setCorporation(String corporation) {
        this.corporation = corporation;
    }

    public CoreEventActivityRel getCoreEventActivityRel() {
		return coreEventActivityRel;
	}

	public void setCoreEventActivityRel(CoreEventActivityRel coreEventActivityRel) {
		this.coreEventActivityRel = coreEventActivityRel;
	}

	public List<CoreActivityArtifactRel> getActivityArtifactList() {
		return activityArtifactList;
	}

	public void setActivityArtifactList(List<CoreActivityArtifactRel> activityArtifactList) {
		this.activityArtifactList = activityArtifactList;
	}

	public String getGlobalEventNo() {
		return globalEventNo;
	}

	public void setGlobalEventNo(String globalEventNo) {
		this.globalEventNo = globalEventNo;
	}

	public String getEcommEventId() {
		return ecommEventId;
	}

	public void setEcommEventId(String ecommEventId) {
		this.ecommEventId = ecommEventId;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
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

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getBillDay() {
		return billDay;
	}

	public void setBillDay(String billDay) {
		this.billDay = billDay;
	}

	public String getCustomerStatus() {
		return customerStatus;
	}

	public void setCustomerStatus(String customerStatus) {
		this.customerStatus = customerStatus;
	}

	public List<CoreCustomerAddr> getCoreCoreCustomerAddrs() {
		return coreCoreCustomerAddrs;
	}

	public void setCoreCoreCustomerAddrs(List<CoreCustomerAddr> coreCoreCustomerAddrs) {
		this.coreCoreCustomerAddrs = coreCoreCustomerAddrs;
	}

	public String getRemarkInfo() {
		return remarkInfo;
	}

	public void setRemarkInfo(String remarkInfo) {
		this.remarkInfo = remarkInfo;
	}

	public String getLastUpdateUserid() {
		return lastUpdateUserid;
	}

	public void setLastUpdateUserid(String lastUpdateUserid) {
		this.lastUpdateUserid = lastUpdateUserid;
	}

	public Integer getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}

	public String getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getProductObjectCode() {
		return productObjectCode;
	}

	public void setProductObjectCode(String productObjectCode) {
		this.productObjectCode = productObjectCode;
	}

	public String getPricingType() {
		return pricingType;
	}

	public void setPricingType(String pricingType) {
		this.pricingType = pricingType;
	}

	public String getPricingTag() {
		return pricingTag;
	}

	public void setPricingTag(String pricingTag) {
		this.pricingTag = pricingTag;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getTagExpirationDate() {
		return tagExpirationDate;
	}

	public void setTagExpirationDate(Date tagExpirationDate) {
		this.tagExpirationDate = tagExpirationDate;
	}

	public String getBalanceType() {
		return balanceType;
	}

	public void setBalanceType(String balanceType) {
		this.balanceType = balanceType;
	}

	public String getBranchNumber() {
		return branchNumber;
	}

	public void setBranchNumber(String branchNumber) {
		this.branchNumber = branchNumber;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getCompanyPhone() {
		return companyPhone;
	}

	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Integer getMemberSince() {
		return memberSince;
	}

	public void setMemberSince(Integer memberSince) {
		this.memberSince = memberSince;
	}

	public String getCustomerSource() {
		return customerSource;
	}

	public void setCustomerSource(String customerSource) {
		this.customerSource = customerSource;
	}

	public Integer getBehaviorScore() {
		return behaviorScore;
	}

	public void setBehaviorScore(Integer behaviorScore) {
		this.behaviorScore = behaviorScore;
	}

	public String getCustomerLevel() {
		return customerLevel;
	}

	public void setCustomerLevel(String customerLevel) {
		this.customerLevel = customerLevel;
	}

	public Integer getAnnualIncome() {
		return annualIncome;
	}

	public void setAnnualIncome(Integer annualIncome) {
		this.annualIncome = annualIncome;
	}

	public String getSexCode() {
		return sexCode;
	}

	public void setSexCode(String sexCode) {
		this.sexCode = sexCode;
	}

	public String getResidencyStatus() {
		return residencyStatus;
	}

	public void setResidencyStatus(String residencyStatus) {
		this.residencyStatus = residencyStatus;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getOccupationCode() {
		return occupationCode;
	}

	public void setOccupationCode(String occupationCode) {
		this.occupationCode = occupationCode;
	}

	public String getPostRankCode() {
		return postRankCode;
	}

	public void setPostRankCode(String postRankCode) {
		this.postRankCode = postRankCode;
	}

	public String getTitleCode() {
		return titleCode;
	}

	public void setTitleCode(String titleCode) {
		this.titleCode = titleCode;
	}

	public String getPeriodOfOccupation() {
		return periodOfOccupation;
	}

	public void setPeriodOfOccupation(String periodOfOccupation) {
		this.periodOfOccupation = periodOfOccupation;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getGuarantorFlag() {
		return guarantorFlag;
	}

	public void setGuarantorFlag(String guarantorFlag) {
		this.guarantorFlag = guarantorFlag;
	}

	public String getMarketerCode() {
		return marketerCode;
	}

	public void setMarketerCode(String marketerCode) {
		this.marketerCode = marketerCode;
	}

	public String getSocialSecurityId() {
		return socialSecurityId;
	}

	public void setSocialSecurityId(String socialSecurityId) {
		this.socialSecurityId = socialSecurityId;
	}

	public String getSocialSecurityNumber() {
		return socialSecurityNumber;
	}

	public void setSocialSecurityNumber(String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}

	public List<CoreCustomerRemarks> getCoreCustomerRemarkss() {
		return coreCustomerRemarkss;
	}

	public void setCoreCustomerRemarkss(List<CoreCustomerRemarks> coreCustomerRemarkss) {
		this.coreCustomerRemarkss = coreCustomerRemarkss;
	}

	public List<CoreCustomerBusinessType> getCoreCustomerBusinessTypes() {
		return coreCustomerBusinessTypes;
	}

	public void setCoreCustomerBusinessTypes(List<CoreCustomerBusinessType> coreCustomerBusinessTypes) {
		this.coreCustomerBusinessTypes = coreCustomerBusinessTypes;
	}

	public String getProductUnitCode() {
		return productUnitCode;
	}

	public void setProductUnitCode(String productUnitCode) {
		this.productUnitCode = productUnitCode;
	}

	public String getProductLineCode() {
		return productLineCode;
	}

	public void setProductLineCode(String productLineCode) {
		this.productLineCode = productLineCode;
	}

	public String getPostingCurrency1() {
		return postingCurrency1;
	}

	public void setPostingCurrency1(String postingCurrency1) {
		this.postingCurrency1 = postingCurrency1;
	}

	public String getPostingCurrency2() {
		return postingCurrency2;
	}

	public void setPostingCurrency2(String postingCurrency2) {
		this.postingCurrency2 = postingCurrency2;
	}

	public String getPostingCurrency3() {
		return postingCurrency3;
	}

	public void setPostingCurrency3(String postingCurrency3) {
		this.postingCurrency3 = postingCurrency3;
	}

	public String getPostingCurrency4() {
		return postingCurrency4;
	}

	public void setPostingCurrency4(String postingCurrency4) {
		this.postingCurrency4 = postingCurrency4;
	}

	public String getPostingCurrency5() {
		return postingCurrency5;
	}

	public void setPostingCurrency5(String postingCurrency5) {
		this.postingCurrency5 = postingCurrency5;
	}

	public String getPostingCurrency6() {
		return postingCurrency6;
	}

	public void setPostingCurrency6(String postingCurrency6) {
		this.postingCurrency6 = postingCurrency6;
	}

	public String getPostingCurrency7() {
		return postingCurrency7;
	}

	public void setPostingCurrency7(String postingCurrency7) {
		this.postingCurrency7 = postingCurrency7;
	}

	public String getPostingCurrency8() {
		return postingCurrency8;
	}

	public void setPostingCurrency8(String postingCurrency8) {
		this.postingCurrency8 = postingCurrency8;
	}

	public String getPostingCurrency9() {
		return postingCurrency9;
	}

	public void setPostingCurrency9(String postingCurrency9) {
		this.postingCurrency9 = postingCurrency9;
	}

	public String getPostingCurrency10() {
		return postingCurrency10;
	}

	public void setPostingCurrency10(String postingCurrency10) {
		this.postingCurrency10 = postingCurrency10;
	}

	public String getCoBrandedNo() {
		return coBrandedNo;
	}

	public void setCoBrandedNo(String coBrandedNo) {
		this.coBrandedNo = coBrandedNo;
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

	public String getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
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

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getEmbosserName1() {
		return embosserName1;
	}

	public void setEmbosserName1(String embosserName1) {
		this.embosserName1 = embosserName1;
	}

	public String getEmbosserName2() {
		return embosserName2;
	}

	public void setEmbosserName2(String embosserName2) {
		this.embosserName2 = embosserName2;
	}

	public String getEmbosserName3() {
		return embosserName3;
	}

	public void setEmbosserName3(String embosserName3) {
		this.embosserName3 = embosserName3;
	}

	public String getEmbosserName4() {
		return embosserName4;
	}

	public void setEmbosserName4(String embosserName4) {
		this.embosserName4 = embosserName4;
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

	public String getFormatCode() {
		return formatCode;
	}

	public void setFormatCode(String formatCode) {
		this.formatCode = formatCode;
	}

	public String getAccountCurrency() {
		return accountCurrency;
	}

	public void setAccountCurrency(String accountCurrency) {
		this.accountCurrency = accountCurrency;
	}

	public String getModeName() {
		return modeName;
	}

	public void setModeName(String modeName) {
		this.modeName = modeName;
	}

	public String getLastProcessDate() {
		return lastProcessDate;
	}

	public void setLastProcessDate(String lastProcessDate) {
		this.lastProcessDate = lastProcessDate;
	}

	public String getCurrProcessDate() {
		return currProcessDate;
	}

	public void setCurrProcessDate(String currProcessDate) {
		this.currProcessDate = currProcessDate;
	}

	public String getNextProcessDate() {
		return nextProcessDate;
	}

	public void setNextProcessDate(String nextProcessDate) {
		this.nextProcessDate = nextProcessDate;
	}

	public String getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(String operationDate) {
		this.operationDate = operationDate;
	}

	public String getCreditTreeId() {
		return creditTreeId;
	}

	public void setCreditTreeId(String creditTreeId) {
		this.creditTreeId = creditTreeId;
	}

	public String getExcessPymtBusinessType() {
		return excessPymtBusinessType;
	}

	public void setExcessPymtBusinessType(String excessPymtBusinessType) {
		this.excessPymtBusinessType = excessPymtBusinessType;
	}

	public List<CoreMediaLabelInfo> getCoreMediaLabelInfos() {
		return coreMediaLabelInfos;
	}

	public void setCoreMediaLabelInfos(List<CoreMediaLabelInfo> coreMediaLabelInfos) {
		this.coreMediaLabelInfos = coreMediaLabelInfos;
	}

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}

	public String getBindDate() {
		return bindDate;
	}

	public void setBindDate(String bindDate) {
		this.bindDate = bindDate;
	}

	public String getUnbindDate() {
		return unbindDate;
	}

	public void setUnbindDate(String unbindDate) {
		this.unbindDate = unbindDate;
	}

	public String getNextBillDate() {
		return nextBillDate;
	}

	public void setNextBillDate(String nextBillDate) {
		this.nextBillDate = nextBillDate;
	}

	public String getIsTransferCard() {
		return isTransferCard;
	}

	public void setIsTransferCard(String isTransferCard) {
		this.isTransferCard = isTransferCard;
	}

	public String getNonLogId() {
		return nonLogId;
	}

	public void setNonLogId(String nonLogId) {
		this.nonLogId = nonLogId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getBusinessTypeCode() {
		return businessTypeCode;
	}

	public void setBusinessTypeCode(String businessTypeCode) {
		this.businessTypeCode = businessTypeCode;
	}

	public String getOrganNo() {
		return organNo;
	}

	public void setOrganNo(String organNo) {
		this.organNo = organNo;
	}

	public String getAccountOrganizeTyp() {
		return accountOrganizeTyp;
	}

	public void setAccountOrganizeTyp(String accountOrganizeTyp) {
		this.accountOrganizeTyp = accountOrganizeTyp;
	}

	public String getBusinessDebitCreditCode() {
		return businessDebitCreditCode;
	}

	public void setBusinessDebitCreditCode(String businessDebitCreditCode) {
		this.businessDebitCreditCode = businessDebitCreditCode;
	}

	public String getCycleModeFlag() {
		return cycleModeFlag;
	}

	public void setCycleModeFlag(String cycleModeFlag) {
		this.cycleModeFlag = cycleModeFlag;
	}

	public String getStatusUpdateDate() {
		return statusUpdateDate;
	}

	public void setStatusUpdateDate(String statusUpdateDate) {
		this.statusUpdateDate = statusUpdateDate;
	}

	public String getClosedDate() {
		return closedDate;
	}

	public void setClosedDate(String closedDate) {
		this.closedDate = closedDate;
	}

	public String getInterestProcessDate() {
		return interestProcessDate;
	}

	public void setInterestProcessDate(String interestProcessDate) {
		this.interestProcessDate = interestProcessDate;
	}

	public String getPaymentDueDate() {
		return paymentDueDate;
	}

	public void setPaymentDueDate(String paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}

	public String getDelinquencyDate() {
		return delinquencyDate;
	}

	public void setDelinquencyDate(String delinquencyDate) {
		this.delinquencyDate = delinquencyDate;
	}

	public String getAccountingStatusCode() {
		return accountingStatusCode;
	}

	public void setAccountingStatusCode(String accountingStatusCode) {
		this.accountingStatusCode = accountingStatusCode;
	}

	public String getAccountingStatusDate() {
		return accountingStatusDate;
	}

	public void setAccountingStatusDate(String accountingStatusDate) {
		this.accountingStatusDate = accountingStatusDate;
	}

	public String getPrevAccountingStatusCode() {
		return prevAccountingStatusCode;
	}

	public void setPrevAccountingStatusCode(String prevAccountingStatusCode) {
		this.prevAccountingStatusCode = prevAccountingStatusCode;
	}

	public String getPrevAccountingStatusDate() {
		return prevAccountingStatusDate;
	}

	public void setPrevAccountingStatusDate(String prevAccountingStatusDate) {
		this.prevAccountingStatusDate = prevAccountingStatusDate;
	}

	public String getDirectDebitType() {
		return directDebitType;
	}

	public void setDirectDebitType(String directDebitType) {
		this.directDebitType = directDebitType;
	}

	public String getCycleDue() {
		return cycleDue;
	}

	public void setCycleDue(String cycleDue) {
		this.cycleDue = cycleDue;
	}

	public String getPrevCycleDue() {
		return prevCycleDue;
	}

	public void setPrevCycleDue(String prevCycleDue) {
		this.prevCycleDue = prevCycleDue;
	}

	public String getAbsStatus() {
		return absStatus;
	}

	public void setAbsStatus(String absStatus) {
		this.absStatus = absStatus;
	}

	public Integer getCurrentCycleNumber() {
		return currentCycleNumber;
	}

	public void setCurrentCycleNumber(Integer currentCycleNumber) {
		this.currentCycleNumber = currentCycleNumber;
	}

	public BigDecimal getTotalAmtDue() {
		return totalAmtDue;
	}

	public void setTotalAmtDue(BigDecimal totalAmtDue) {
		this.totalAmtDue = totalAmtDue;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getCurrLogFlag() {
		return currLogFlag;
	}

	public void setCurrLogFlag(String currLogFlag) {
		this.currLogFlag = currLogFlag;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getEventNo() {
		return eventNo;
	}

	public void setEventNo(String eventNo) {
		this.eventNo = eventNo;
	}

	public String getActivityNo() {
		return activityNo;
	}

	public void setActivityNo(String activityNo) {
		this.activityNo = activityNo;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getProductCodeSet() {
		return productCodeSet;
	}

	public void setProductCodeSet(String productCodeSet) {
		this.productCodeSet = productCodeSet;
	}

	public List<CoreAccount> getCoreAccountList() {
		return coreAccountList;
	}

	public void setCoreAccountList(List<CoreAccount> coreAccountList) {
		this.coreAccountList = coreAccountList;
	}

}
