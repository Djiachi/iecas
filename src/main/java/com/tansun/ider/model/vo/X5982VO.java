package com.tansun.ider.model.vo;

import java.math.BigDecimal;
import java.util.Date;

public class X5982VO {



    /** 产品描述 [50,0] */
    protected String productDesc;
    /** 所属业务描述 [50,0] */
    private String businessDesc;
    /** 业务项目描述 [50,0] */
    private String programDesc;
    /** 币种 */
    private String currencyDesc;
    /** 机构名称 */
    private String organName;

    private String authDataSynFlag;

    /** 余额 */
    private BigDecimal totalBalance;

    protected String id;
    /** 是否可以操作 */
    private String operation;



    /** 账户代码 [23,0] Not NULL */
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
    /** 逾期状态  [10,0] */
    protected Integer cycleDue;
    /** 上一逾期状态 [10,0] */
    protected Integer prevCycleDue;
    /** 资产属性，资产担保证券或资产支撑证券（Asset-backed security） [2,0] */
    protected String absStatus;
    /** 资产转变方式:ABS-证券化 [3,0] */
    protected String capitalType;
    /** 资产转变阶段:PACK-封包,TRSF-转让,REPO-回购 [4,0] */
    protected String capitalStage;
    /** 资产转出机构代码 [25,0] */
    protected String capitalOrganizationCode;
    /** 资产转出机构名称 [120,0] */
    protected String capitalOrganizationName;
    /** 宽限日 [10,0] */
    protected String graceDate;
    /** 当前余额 [18,0] */
    protected BigDecimal newBalance;
    /** 创建时间 yyyy-MM-dd HH:mm:ss [23,0] */
    protected String gmtCreate;
    /** 时间戳 : oralce使用触发器更新， mysql使用自动更新 [19,0] Not NULL */
    protected Date timestamp;
    /** 版本号 [10,0] Not NULL */
    protected Integer version;

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

    public String getProgramDesc() {
        return programDesc;
    }

    public void setProgramDesc(String programDesc) {
        this.programDesc = programDesc;
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

    public String getCapitalType() {
        return capitalType;
    }

    public void setCapitalType(String capitalType) {
        this.capitalType = capitalType;
    }

    public String getCapitalStage() {
        return capitalStage;
    }

    public void setCapitalStage(String capitalStage) {
        this.capitalStage = capitalStage;
    }

    public String getCapitalOrganizationCode() {
        return capitalOrganizationCode;
    }

    public void setCapitalOrganizationCode(String capitalOrganizationCode) {
        this.capitalOrganizationCode = capitalOrganizationCode;
    }

    public String getCapitalOrganizationName() {
        return capitalOrganizationName;
    }

    public void setCapitalOrganizationName(String capitalOrganizationName) {
        this.capitalOrganizationName = capitalOrganizationName;
    }

    public String getGraceDate() {
        return graceDate;
    }

    public void setGraceDate(String graceDate) {
        this.graceDate = graceDate;
    }

    public BigDecimal getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(BigDecimal newBalance) {
        this.newBalance = newBalance;
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

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "X5982VO{" +
                "productDesc='" + productDesc + '\'' +
                ", businessDesc='" + businessDesc + '\'' +
                ", programDesc='" + programDesc + '\'' +
                ", currencyDesc='" + currencyDesc + '\'' +
                ", organName='" + organName + '\'' +
                ", authDataSynFlag='" + authDataSynFlag + '\'' +
                ", totalBalance=" + totalBalance +
                ", id='" + id + '\'' +
                ", operation='" + operation + '\'' +
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
                ", cycleDue=" + cycleDue +
                ", prevCycleDue=" + prevCycleDue +
                ", absStatus='" + absStatus + '\'' +
                ", capitalType='" + capitalType + '\'' +
                ", capitalStage='" + capitalStage + '\'' +
                ", capitalOrganizationCode='" + capitalOrganizationCode + '\'' +
                ", capitalOrganizationName='" + capitalOrganizationName + '\'' +
                ", graceDate='" + graceDate + '\'' +
                ", newBalance=" + newBalance +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", timestamp=" + timestamp +
                ", version=" + version +
                '}';
    }
}
