package com.tansun.ider.model.vo;

import java.util.List;

import com.tansun.ider.dao.issue.entity.CoreCustomerAddr;
import com.tansun.ider.dao.issue.entity.CoreCustomerRemarks;
import com.tansun.ider.dao.issue.entity.CoreCustomerUnifyInfo;

/**
 * @version:1.0
 * @Description: 客户资料查询
 * @author: admin
 */
public class X5105VO {

	private String corporationEntityNo;
    /** 还款选项 1：统一还款还款 2：单独还款 [1,0] */
    protected String paymentMark;
	/** 客户号 [12,0] */
	private String customerNo;
	/** 所属机构 [10,0] */
	private String institutionId;
	/** 运行模式 [3,0] Not NULL */
	private String operationMode;
	/** 客户姓名 [30,0] */
	private String customerName;
	/** 证件类型 [1,0] */
	private String idType;
	/** 证件号码 [30,0] */
	private String idNumber;
	/** 港澳台居住证件号码*/
	private String idNumberHmt;
	

	public String getIdNumberHmt() {
		return idNumberHmt;
	}

	public void setIdNumberHmt(String idNumberHmt) {
		this.idNumberHmt = idNumberHmt;
	}

	/** 城市 [30,0] */
	private String city;
	/** 个人公司标识 [1,0] */
	private String customerType;
	/** 账单日 [2,0] */
	private String billDay;
	/** 客户状态 [1,0] */
	private String customerStatus;
	/** 新建客户日期 [10,0] */
	private String createDate;
	/** 客户地址 */
	private List<CoreCustomerAddr> coreCustomerAddrs;
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
	/** 职称代码 [2,0] */
	private String titleCode;
	/** 工作年限 [2,0] */
	private String periodOfOccupation;
	/** 兴趣爱好 [30,0] */
	private String hobby;
	/** 担保人标识 [1,0] */
	private String guarantorFlag;
	/** 营销员代码 [9,0] */
	private String marketerCode;
	/** 持卡人的社会保障号类型 [1,0] */
	private String socialSecurityId;
	/** 社保账号 [30,0] */
	private String socialSecurityNumber;
	/** 备注信息 [50,0] */
	private String remarkInfo;
	/** 新增备注人员 [30,0] */
	private String lastUpdateUserid;
	/** 序号 [10,0] */
	private Integer serialNo;
	/** 新增备注日期 [10,0] */
	private String lastUpdateDate;
	/** 客户业务类型标签 */
	private List<CoreCustomerUnifyInfo> coreCustomerUnifyInfos;
	/** 本次账单日期 yyyy-MM-dd [10,0] */
	private String statementDate;
	/** 本次最后还款日期 yyyy-MM-dd [10,0] */
	private String paymentDueDate;
	/** 本次宽限日期 yyyy-MM-dd [10,0] */
	private String graceDate;
	/** 本次迟缴费收取日期 yyyy-MM-dd [10,0] */
	private String delinquencyDate;
	/** 产品线 [9,0] Not NULL */
	private String productLineCode;
	/** 周期号 [10,0] Not NULL */
	private Integer cycleNumber;
	/** 客户备注信息 */
	private List<CoreCustomerRemarks> listcoreCustomerRemarks;
    /** 追账完成日期 [10,0] */
    protected String afterHoursCompleteDate;
    /** 客户核算状态 [10,0] */
    protected String accountingStatusCode;
    /** 系统单元编号 [3,0] */
    protected String systemUnitNo;
    
	public X5105VO() {
		super();
	}
	
	public String getCorporationEntityNo() {
		return corporationEntityNo;
	}

	public void setCorporationEntityNo(String corporationEntityNo) {
		this.corporationEntityNo = corporationEntityNo;
	}

	public String getAfterHoursCompleteDate() {
		return afterHoursCompleteDate;
	}

	public void setAfterHoursCompleteDate(String afterHoursCompleteDate) {
		this.afterHoursCompleteDate = afterHoursCompleteDate;
	}

	public String getAccountingStatusCode() {
		return accountingStatusCode;
	}

	public void setAccountingStatusCode(String accountingStatusCode) {
		this.accountingStatusCode = accountingStatusCode;
	}

	public String getSystemUnitNo() {
		return systemUnitNo;
	}

	public void setSystemUnitNo(String systemUnitNo) {
		this.systemUnitNo = systemUnitNo;
	}

	public List<CoreCustomerUnifyInfo> getCoreCustomerUnifyInfos() {
		return coreCustomerUnifyInfos;
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


	public String getCreateDate() {
		return createDate;
	}


	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}


	public List<CoreCustomerAddr> getCoreCustomerAddrs() {
		return coreCustomerAddrs;
	}


	public void setCoreCustomerAddrs(List<CoreCustomerAddr> coreCustomerAddrs) {
		this.coreCustomerAddrs = coreCustomerAddrs;
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


	public void setCoreCustomerUnifyInfos(List<CoreCustomerUnifyInfo> coreCustomerUnifyInfos) {
		this.coreCustomerUnifyInfos = coreCustomerUnifyInfos;
	}


	public String getStatementDate() {
		return statementDate;
	}


	public void setStatementDate(String statementDate) {
		this.statementDate = statementDate;
	}


	public String getPaymentDueDate() {
		return paymentDueDate;
	}


	public void setPaymentDueDate(String paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}


	public String getGraceDate() {
		return graceDate;
	}


	public void setGraceDate(String graceDate) {
		this.graceDate = graceDate;
	}


	public String getDelinquencyDate() {
		return delinquencyDate;
	}


	public void setDelinquencyDate(String delinquencyDate) {
		this.delinquencyDate = delinquencyDate;
	}


	public String getProductLineCode() {
		return productLineCode;
	}


	public void setProductLineCode(String productLineCode) {
		this.productLineCode = productLineCode;
	}


	public Integer getCycleNumber() {
		return cycleNumber;
	}


	public void setCycleNumber(Integer cycleNumber) {
		this.cycleNumber = cycleNumber;
	}

	public List<CoreCustomerRemarks> getListcoreCustomerRemarks() {
		return listcoreCustomerRemarks;
	}

	public void setListcoreCustomerRemarks(List<CoreCustomerRemarks> listcoreCustomerRemarks) {
		this.listcoreCustomerRemarks = listcoreCustomerRemarks;
	}

	public String getPaymentMark() {
		return paymentMark;
	}

	public void setPaymentMark(String paymentMark) {
		this.paymentMark = paymentMark;
	}

}
