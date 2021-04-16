package com.tansun.ider.model.vo;

import com.tansun.ider.dao.issue.entity.CoreAccount;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class X5115VO {


    /** 产品描述 [50,0] */
    protected String productDesc;
	private String businessDesc;
	 /** 账户代码 [18,0] Not NULL */
    protected String accountId;
    /** 币种 [3,0] Not NULL */
    protected String currencyCode;
    /** 业务项目 [9,0] */
    protected String businessProgramNo;
    /** 产品对象代码 [15,0] */
    protected String productObjectCode;
    /** 所属业务类型 [15,0] */
    protected String businessTypeCode;
    /** 客户代码，外键关联客户主表主键 [20,0] Not NULL */
    protected String customerNo;
    /** 所属机构号 [10,0] Not NULL */
    protected String organNo;
    /**  运营模式 [3,0] Not NULL */
    protected String operationMode;
    /** 账户组织形式 R：循环 [1,0] */
    protected String accountOrganForm;
    /** 账户性质  D：借记账户  C ： 贷记账户 [1,0] */
    protected String businessDebitCreditCode;
    /** 周期模式标志 Y：周期模式 N：非周期模式 [1,0] */
    protected String cycleModeFlag;
    /** 状态码 1：活跃账户 2：非活跃账户 3：冻结账户 8：关闭账户 9：待删除账户 [1,0] */
    protected String statusCode;
    /** 状态更新日期 [10,0] */
    protected String statusUpdateDate;
    /** 新建日期 [10,0] */
    protected String createDate;
    /** 关闭日期 [10,0] */
    protected String closedDate;
    /** 计息处理日 [10,0] */
    protected String interestProcessDate;
    /** 最后还款日 [10,0] */
    protected String paymentDueDate;
    /** 滞纳金产生日 [10,0] */
    protected String delinquencyDate;
    /** 核算状态码 [3,0] */
    protected String accountingStatusCode;
    /** 核算状态日期 [10,0] */
    protected String accountingStatusDate;
    /** 上一核算状态码 [3,0] */
    protected String prevAccountingStatusCode;
    /** 上一核算状态日期 [10,0] */
    protected String prevAccountingStatusDate;
    /**  约定扣款方式 1：账单余额  2：最低还款 [1,0] */
    protected String directDebitType;
    /** 逾期状态  [10,0] */
    protected Integer cycleDue;
    /** 上一逾期状态 [10,0] */
    protected Integer prevCycleDue;
    /**  ABS状态，资产担保证券或资产支撑证券（Asset-backed security） [2,0] */
    protected String absStatus;
    /**  当前周期号 [10,0] */
    protected Integer currentCycleNumber;
    /** 宽限日 [10,0] */
    protected String graceDate;
    /** 创建时间 yyyy-MM-dd HH:mm:ss [23,0] */
    protected String gmtCreate;
    /** 时间戳 : oralce使用触发器更新， mysql使用自动更新 [19,0] Not NULL */
    protected Date timestamp;
    /** 版本号 [10,0] Not NULL */
    protected Integer version;

    private String programDesc;
    private String nextBillDate;
    protected String id;
    private String currencyDesc;
    private String organName;
    private String authDataSynFlag;
    private BigDecimal totalBalance;
    /** 资产转变阶段:PACK-封包,TRSF-转让,REPO-回购 [4,0] */
    private String capitalStage;
	private String globalTransSerialNo;
	private String transIdentifiNo;
	private String fundNum;
	private Boolean haveChild;
	private List<CoreAccount> children;
	private String Flag;
	private String requestType;

	/** 前债权ID [25,0] */
	protected String prevFundNum;
	/** 子账户标识 [1,0] Not NULL */
	protected String subAccIdentify;
	/** 资产证券化计划代码 [6,0] */
	protected String absPlanId;
	/** 是否可以操作 */
	private String operation;
	/** abs资产属性 R-循环账户 T-交易账户 B-不良资产 [1,0] */
	protected String accountType;
	/** 资金法人名称 */
	protected String capitalOrganizationName;
	/** 法人名称 */
	protected String fundName;
	/** 交易识别描述 */
	protected String transIdentifiDesc;
	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public String getBusinessDesc() {
		return businessDesc;
	}

	public void setBusinessDesc(String businessDesc) {
		this.businessDesc = businessDesc;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getBusinessProgramNo() {
		return businessProgramNo;
	}

	public void setBusinessProgramNo(String businessProgramNo) {
		this.businessProgramNo = businessProgramNo;
	}

	public String getProductObjectCode() {
		return productObjectCode;
	}

	public void setProductObjectCode(String productObjectCode) {
		this.productObjectCode = productObjectCode;
	}

	public String getBusinessTypeCode() {
		return businessTypeCode;
	}

	public void setBusinessTypeCode(String businessTypeCode) {
		this.businessTypeCode = businessTypeCode;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getOrganNo() {
		return organNo;
	}

	public void setOrganNo(String organNo) {
		this.organNo = organNo;
	}

	public String getOperationMode() {
		return operationMode;
	}

	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}

	public String getAccountOrganForm() {
		return accountOrganForm;
	}

	public void setAccountOrganForm(String accountOrganForm) {
		this.accountOrganForm = accountOrganForm;
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

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusUpdateDate() {
		return statusUpdateDate;
	}

	public void setStatusUpdateDate(String statusUpdateDate) {
		this.statusUpdateDate = statusUpdateDate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
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

	public Integer getCycleDue() {
		return cycleDue;
	}

	public void setCycleDue(Integer cycleDue) {
		this.cycleDue = cycleDue;
	}

	public Integer getPrevCycleDue() {
		return prevCycleDue;
	}

	public void setPrevCycleDue(Integer prevCycleDue) {
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

	public String getGraceDate() {
		return graceDate;
	}

	public void setGraceDate(String graceDate) {
		this.graceDate = graceDate;
	}

	public String getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getProgramDesc() {
		return programDesc;
	}

	public void setProgramDesc(String programDesc) {
		this.programDesc = programDesc;
	}

	public String getNextBillDate() {
		return nextBillDate;
	}

	public void setNextBillDate(String nextBillDate) {
		this.nextBillDate = nextBillDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCurrencyDesc() {
		return currencyDesc;
	}

	public void setCurrencyDesc(String currencyDesc) {
		this.currencyDesc = currencyDesc;
	}

	public String getOrganName() {
		return organName;
	}

	public void setOrganName(String organName) {
		this.organName = organName;
	}

	public String getAuthDataSynFlag() {
		return authDataSynFlag;
	}

	public void setAuthDataSynFlag(String authDataSynFlag) {
		this.authDataSynFlag = authDataSynFlag;
	}

	public BigDecimal getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(BigDecimal totalBalance) {
		this.totalBalance = totalBalance;
	}

	public String getCapitalStage() {
		return capitalStage;
	}

	public void setCapitalStage(String capitalStage) {
		this.capitalStage = capitalStage;
	}

	public String getGlobalTransSerialNo() {
		return globalTransSerialNo;
	}

	public void setGlobalTransSerialNo(String globalTransSerialNo) {
		this.globalTransSerialNo = globalTransSerialNo;
	}

	public String getTransIdentifiNo() {
		return transIdentifiNo;
	}

	public void setTransIdentifiNo(String transIdentifiNo) {
		this.transIdentifiNo = transIdentifiNo;
	}

	public String getFundNum() {
		return fundNum;
	}

	public void setFundNum(String fundNum) {
		this.fundNum = fundNum;
	}

	public Boolean getHaveChild() {
		return haveChild;
	}

	public void setHaveChild(Boolean haveChild) {
		this.haveChild = haveChild;
	}

	public List<CoreAccount> getChildren() {
		return children;
	}

	public void setChildren(List<CoreAccount> children) {
		this.children = children;
	}

	public String getFlag() {
		return Flag;
	}

	public void setFlag(String flag) {
		Flag = flag;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getPrevFundNum() {
		return prevFundNum;
	}

	public void setPrevFundNum(String prevFundNum) {
		this.prevFundNum = prevFundNum;
	}

	public String getSubAccIdentify() {
		return subAccIdentify;
	}

	public void setSubAccIdentify(String subAccIdentify) {
		this.subAccIdentify = subAccIdentify;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getAbsPlanId() {
		return absPlanId;
	}

	public void setAbsPlanId(String absPlanId) {
		this.absPlanId = absPlanId;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

    public String getCapitalOrganizationName() {
        return capitalOrganizationName;
    }

    public void setCapitalOrganizationName(String capitalOrganizationName) {
        this.capitalOrganizationName = capitalOrganizationName;
    }

	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	public String getTransIdentifiDesc() {
		return transIdentifiDesc;
	}

	public void setTransIdentifiDesc(String transIdentifiDesc) {
		this.transIdentifiDesc = transIdentifiDesc;
	}

	@Override
	public String toString() {
		return "X5115VO{" +
				"productDesc='" + productDesc + '\'' +
				", businessDesc='" + businessDesc + '\'' +
				", accountId='" + accountId + '\'' +
				", currencyCode='" + currencyCode + '\'' +
				", businessProgramNo='" + businessProgramNo + '\'' +
				", productObjectCode='" + productObjectCode + '\'' +
				", businessTypeCode='" + businessTypeCode + '\'' +
				", customerNo='" + customerNo + '\'' +
				", organNo='" + organNo + '\'' +
				", operationMode='" + operationMode + '\'' +
				", accountOrganForm='" + accountOrganForm + '\'' +
				", businessDebitCreditCode='" + businessDebitCreditCode + '\'' +
				", cycleModeFlag='" + cycleModeFlag + '\'' +
				", statusCode='" + statusCode + '\'' +
				", statusUpdateDate='" + statusUpdateDate + '\'' +
				", createDate='" + createDate + '\'' +
				", closedDate='" + closedDate + '\'' +
				", interestProcessDate='" + interestProcessDate + '\'' +
				", paymentDueDate='" + paymentDueDate + '\'' +
				", delinquencyDate='" + delinquencyDate + '\'' +
				", accountingStatusCode='" + accountingStatusCode + '\'' +
				", accountingStatusDate='" + accountingStatusDate + '\'' +
				", prevAccountingStatusCode='" + prevAccountingStatusCode + '\'' +
				", prevAccountingStatusDate='" + prevAccountingStatusDate + '\'' +
				", directDebitType='" + directDebitType + '\'' +
				", cycleDue=" + cycleDue +
				", prevCycleDue=" + prevCycleDue +
				", absStatus='" + absStatus + '\'' +
				", currentCycleNumber=" + currentCycleNumber +
				", graceDate='" + graceDate + '\'' +
				", gmtCreate='" + gmtCreate + '\'' +
				", timestamp=" + timestamp +
				", version=" + version +
				", programDesc='" + programDesc + '\'' +
				", nextBillDate='" + nextBillDate + '\'' +
				", id='" + id + '\'' +
				", currencyDesc='" + currencyDesc + '\'' +
				", organName='" + organName + '\'' +
				", authDataSynFlag='" + authDataSynFlag + '\'' +
				", totalBalance=" + totalBalance +
				", capitalStage='" + capitalStage + '\'' +
				", globalTransSerialNo='" + globalTransSerialNo + '\'' +
				", transIdentifiNo='" + transIdentifiNo + '\'' +
				", fundNum='" + fundNum + '\'' +
				", haveChild=" + haveChild +
				", children=" + children +
				", Flag='" + Flag + '\'' +
				", requestType='" + requestType + '\'' +
				", prevFundNum='" + prevFundNum + '\'' +
				", subAccIdentify='" + subAccIdentify + '\'' +
				", absPlanId='" + absPlanId + '\'' +
				", operation='" + operation + '\'' +
				", accountType='" + accountType + '\'' +
				", capitalOrganizationName='" + capitalOrganizationName + '\'' +
				", fundName='" + fundName + '\'' +
				", transIdentifiDesc='" + transIdentifiDesc + '\'' +
				'}';
	}
}
