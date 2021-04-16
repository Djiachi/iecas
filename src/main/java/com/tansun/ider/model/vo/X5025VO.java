package com.tansun.ider.model.vo;

/**
 * 转卡媒介查询
 * 
 * @author wt
 *
 */
public class X5025VO {

	/** 客户号 [12,0] Not NULL */
	private String customerNo;
	/** 分行号 [9,0] */
	private String branchNumber;
	/** 生日 yyyy-MM-dd [10,0] */
	private String dateOfBirth;
	/** 手机号码 [18,0] */
	private String mobilePhone;
	/** 邮编 [9,0] */
	private String zipCode;
	/** 家庭电话 [18,0] */
	private String homePhone;
	/** 公司电话 [18,0] */
	private String companyPhone;
	/** 联络人电话 [18,0] */
	private String contactPhone;
	/** 联络人姓名 [30,0] */
	private String contactName;
	/** 成为会员年份 [10,0] */
	private Integer memberSince;
	/** 客户来源码 [4,0] */
	private String customerSource;
	/** 行为得分 [10,0] */
	private Integer behaviorScore;
	/** 客户等级 [2,0] */
	private String customerLevel;
	/** 年收入 [10,0] */
	private Integer annualIncome;
	/** 性别 [1,0] */
	private String sexCode;
	/** 住宅性质 [1,0] */
	private String residencyStatus;
	/** 婚姻状况 [1,0] */
	private String maritalStatus;
	/** 工作行业代码 [4,0] */
	private String occupationCode;
	/** 职务级别代码 [2,0] */
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
	/** 同步授权标志 */
	private String authDataSynFlag;
	/** 所属机构 [10,0] */
	protected String institutionId;
	/** 运行模式 [3,0] Not NULL */
	protected String operationMode;
	/** 客户姓名 [30,0] */
	protected String customerName;
	/** 证件类型 [1,0] */
	protected String idType;
	/** 证件号码 [30,0] */
	protected String idNumber;
	/** 个人公司标识 [1,0] */
	protected String customerType;
	/** 账单日 [2,0] */
	protected String billDay;
	/** 客户状态 [1,0] */
	protected String customerStatus;
	/** 新建客户日期 [10,0] */
	protected String createDate;
	/** 序号 [10,0] */
	protected Integer serialNo;
	/** 标识是何场景下使用的地址 [10,0] */
	protected Integer type;
	/** 联系地址 [90,0] */
	protected String contactAddress;
	/** 联系邮编 [9,0] */
	protected String contactPostCode;
	/** 指定电话 [18,0] */
	protected String contactMobilePhone;
	/** 所在城市 [30,0] */
	protected String city;

	public X5025VO() {
		super();
	}

	public X5025VO(String customerNo, String branchNumber, String dateOfBirth, String mobilePhone, String zipCode,
			String homePhone, String companyPhone, String contactPhone, String contactName, Integer memberSince,
			String customerSource, Integer behaviorScore, String customerLevel, Integer annualIncome, String sexCode,
			String residencyStatus, String maritalStatus, String occupationCode, String postRankCode, String titleCode,
			String periodOfOccupation, String hobby, String guarantorFlag, String marketerCode, String socialSecurityId,
			String socialSecurityNumber, String authDataSynFlag, String institutionId, String operationMode,
			String customerName, String idType, String idNumber, String customerType, String billDay,
			String customerStatus, String createDate, Integer serialNo, Integer type, String contactAddress,
			String contactPostCode, String contactMobilePhone, String city) {
		super();
		this.customerNo = customerNo;
		this.branchNumber = branchNumber;
		this.dateOfBirth = dateOfBirth;
		this.mobilePhone = mobilePhone;
		this.zipCode = zipCode;
		this.homePhone = homePhone;
		this.companyPhone = companyPhone;
		this.contactPhone = contactPhone;
		this.contactName = contactName;
		this.memberSince = memberSince;
		this.customerSource = customerSource;
		this.behaviorScore = behaviorScore;
		this.customerLevel = customerLevel;
		this.annualIncome = annualIncome;
		this.sexCode = sexCode;
		this.residencyStatus = residencyStatus;
		this.maritalStatus = maritalStatus;
		this.occupationCode = occupationCode;
		this.postRankCode = postRankCode;
		this.titleCode = titleCode;
		this.periodOfOccupation = periodOfOccupation;
		this.hobby = hobby;
		this.guarantorFlag = guarantorFlag;
		this.marketerCode = marketerCode;
		this.socialSecurityId = socialSecurityId;
		this.socialSecurityNumber = socialSecurityNumber;
		this.authDataSynFlag = authDataSynFlag;
		this.institutionId = institutionId;
		this.operationMode = operationMode;
		this.customerName = customerName;
		this.idType = idType;
		this.idNumber = idNumber;
		this.customerType = customerType;
		this.billDay = billDay;
		this.customerStatus = customerStatus;
		this.createDate = createDate;
		this.serialNo = serialNo;
		this.type = type;
		this.contactAddress = contactAddress;
		this.contactPostCode = contactPostCode;
		this.contactMobilePhone = contactMobilePhone;
		this.city = city;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
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

	public String getAuthDataSynFlag() {
		return authDataSynFlag;
	}

	public void setAuthDataSynFlag(String authDataSynFlag) {
		this.authDataSynFlag = authDataSynFlag;
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

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Integer getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	public String getContactPostCode() {
		return contactPostCode;
	}

	public void setContactPostCode(String contactPostCode) {
		this.contactPostCode = contactPostCode;
	}

	public String getContactMobilePhone() {
		return contactMobilePhone;
	}

	public void setContactMobilePhone(String contactMobilePhone) {
		this.contactMobilePhone = contactMobilePhone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "X5025VO [customerNo=" + customerNo + ", branchNumber=" + branchNumber + ", dateOfBirth=" + dateOfBirth
				+ ", mobilePhone=" + mobilePhone + ", zipCode=" + zipCode + ", homePhone=" + homePhone
				+ ", companyPhone=" + companyPhone + ", contactPhone=" + contactPhone + ", contactName=" + contactName
				+ ", memberSince=" + memberSince + ", customerSource=" + customerSource + ", behaviorScore="
				+ behaviorScore + ", customerLevel=" + customerLevel + ", annualIncome=" + annualIncome + ", sexCode="
				+ sexCode + ", residencyStatus=" + residencyStatus + ", maritalStatus=" + maritalStatus
				+ ", occupationCode=" + occupationCode + ", postRankCode=" + postRankCode + ", titleCode=" + titleCode
				+ ", periodOfOccupation=" + periodOfOccupation + ", hobby=" + hobby + ", guarantorFlag=" + guarantorFlag
				+ ", marketerCode=" + marketerCode + ", socialSecurityId=" + socialSecurityId
				+ ", socialSecurityNumber=" + socialSecurityNumber + ", authDataSynFlag=" + authDataSynFlag
				+ ", institutionId=" + institutionId + ", operationMode=" + operationMode + ", customerName="
				+ customerName + ", idType=" + idType + ", idNumber=" + idNumber + ", customerType=" + customerType
				+ ", billDay=" + billDay + ", customerStatus=" + customerStatus + ", createDate=" + createDate
				+ ", serialNo=" + serialNo + ", type=" + type + ", contactAddress=" + contactAddress
				+ ", contactPostCode=" + contactPostCode + ", contactMobilePhone=" + contactMobilePhone + ", city="
				+ city + "]";
	}

}
