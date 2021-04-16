package com.tansun.ider.model.bo;

import java.util.Date;

/**
 * @Desc:客户个人信息维护
 * @Author wt
 * @Date 2018年5月7日 下午3:55:09
 */
public class X5150BO {

	/** 事件编号 [14,0] Not NULL */
    protected String eventNo;
    /** 活动编号 [8,0] Not NULL */
    protected String activityNo;
	/** 操作员Id */
	private String operatorId;
	/* 外部识别号 */
	private String globalEventNo;
	/* id */
	private String id;
	/* 客户号 */
	private String customerNo;
	/* 分行号 */
	private String branchNumber;
	/* 生日 */
	private Date birthday;
	/* 手机号码 */
	private String phoneNumber;
	/* 邮编 */
	private String zipCode;
	/* 家庭电话 */
	private String homePhone;
	/* 公司电话 */
	private String companyPhone;
	/* 联络人电话 */
	private String contactPhone;
	/* 联络人姓名 */
	private String contactName;
	/* 成为会员年份 */
	private String memberOfTheYear;
	/* 客户来源码 */
	private String customerSource;
	/* 行为得分 */
	private Integer behaviorScore;
	/* 客户等级 */
	private String customerLevel;
	/* 年收入 */
	private Integer annualIncome;
	/* 性别 */
	private String gender;
	/* 住宅性质 */
	private String propertyResidence;
	/* 婚姻状况 */
	private String maritalStatus;
	/* 工作行业代码 */
	private String workIndustryCode;
	/* 职务级别代码 */
	private String jobLevelCode;
	/* 职称代码 */
	private String titleCode;
	/* 工作年限 */
	private String workingLife;
	/* 兴趣爱好 */
	private String hobby;
	/* 担保人标识 */
	private String guarantorLogo;
	/* 营销员代码 */
	private String marketerCode;
	/* 持卡人的社会保障号类型 */
	private String typeSocialSecurityNumber;
	/* 社保账号 */
	private String socialSecurityAccount;

	public String getGlobalEventNo() {
		return globalEventNo;
	}

	public void setGlobalEventNo(String globalEventNo) {
		this.globalEventNo = globalEventNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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

	public String getMemberOfTheYear() {
		return memberOfTheYear;
	}

	public void setMemberOfTheYear(String memberOfTheYear) {
		this.memberOfTheYear = memberOfTheYear;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPropertyResidence() {
		return propertyResidence;
	}

	public void setPropertyResidence(String propertyResidence) {
		this.propertyResidence = propertyResidence;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getWorkIndustryCode() {
		return workIndustryCode;
	}

	public void setWorkIndustryCode(String workIndustryCode) {
		this.workIndustryCode = workIndustryCode;
	}

	public String getJobLevelCode() {
		return jobLevelCode;
	}

	public void setJobLevelCode(String jobLevelCode) {
		this.jobLevelCode = jobLevelCode;
	}

	public String getTitleCode() {
		return titleCode;
	}

	public void setTitleCode(String titleCode) {
		this.titleCode = titleCode;
	}

	public String getWorkingLife() {
		return workingLife;
	}

	public void setWorkingLife(String workingLife) {
		this.workingLife = workingLife;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getGuarantorLogo() {
		return guarantorLogo;
	}

	public void setGuarantorLogo(String guarantorLogo) {
		this.guarantorLogo = guarantorLogo;
	}

	public String getMarketerCode() {
		return marketerCode;
	}

	public void setMarketerCode(String marketerCode) {
		this.marketerCode = marketerCode;
	}

	public String getTypeSocialSecurityNumber() {
		return typeSocialSecurityNumber;
	}

	public void setTypeSocialSecurityNumber(String typeSocialSecurityNumber) {
		this.typeSocialSecurityNumber = typeSocialSecurityNumber;
	}

	public String getSocialSecurityAccount() {
		return socialSecurityAccount;
	}

	public void setSocialSecurityAccount(String socialSecurityAccount) {
		this.socialSecurityAccount = socialSecurityAccount;
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

	@Override
	public String toString() {
		return "X5150BO [globalEventNo=" + globalEventNo + ", id=" + id + ", customerNo=" + customerNo
				+ ", branchNumber=" + branchNumber + ", birthday=" + birthday + ", phoneNumber=" + phoneNumber
				+ ", zipCode=" + zipCode + ", homePhone=" + homePhone + ", companyPhone=" + companyPhone
				+ ", contactPhone=" + contactPhone + ", contactName=" + contactName + ", memberOfTheYear="
				+ memberOfTheYear + ", customerSource=" + customerSource + ", behaviorScore=" + behaviorScore
				+ ", customerLevel=" + customerLevel + ", annualIncome=" + annualIncome + ", gender=" + gender
				+ ", propertyResidence=" + propertyResidence + ", maritalStatus=" + maritalStatus
				+ ", workIndustryCode=" + workIndustryCode + ", jobLevelCode=" + jobLevelCode + ", titleCode="
				+ titleCode + ", workingLife=" + workingLife + ", hobby=" + hobby + ", guarantorLogo=" + guarantorLogo
				+ ", marketerCode=" + marketerCode + ", typeSocialSecurityNumber=" + typeSocialSecurityNumber
				+ ", socialSecurityAccount=" + socialSecurityAccount + "]";
	}

}
