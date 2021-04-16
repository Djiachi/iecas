package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.tansun.ider.framwork.commun.BeanVO;

public class X5340VO extends BeanVO implements Serializable{

	private static final long serialVersionUID = 1L;
	 /** 账户代码 : 账户代码 [18,0] Not NULL */
    private String accountId;
    /** 币种 : 币种 [3,0] Not NULL */
    private String currencyCode;
    /** 原交易全局流水号 : 原交易全局流水号 [36,0] */
    private String oldGlobalSerialNumbr;
    /** 外部识别号 : 外部识别号 [19,0] */
    private String externalIdentificationNo;
    /** 争议金额 : 争议金额 [18,0] */
    private BigDecimal disputeAmount;
    /** 账户余额 : 账户余额 [18,0] */
    private BigDecimal blanceAmount;
    /** 溢缴款冻结金额 : 溢缴款冻结金额 [18,0] */
    private BigDecimal overpayFreezeAmount;
    /** 溢缴款冻结状态 : 溢缴款冻结状态F-已冻结U-已解冻N-无冻结 [1,0] */
    private String overpayFreezeStatus;
    /** 释放日期 : 释放日期 [10,0] */
    private String freeDate;
    /** 原交易结息截止日 : 原交易结息截止日 [10,0] */
    private String interestExpirationDate;
    /** 原交易已结记利息金额 : 原交易已结记利息金额 [18,0] */
    private BigDecimal interestAmt;
    /** 额度占用标识 : 额度占用标识0：不占用；1：占用 [1,0] */
    private String amtOccFlag;
    /** 创建时间 : 创建时间 [23,0] */
    private String gmtCreate;
    /** 时间戳 : 时间戳 [19,0] Not NULL */
    private Date timestamp;
    /** 版本 [10,0] Not NULL */
    private Integer version;
    /** 业务项目 [9,0] */
    private String businessProgramNo;
    /** 产品对象代码 [15,0] */
    private String productObjectCode;
    /** 所属业务类型 [15,0] */
    private String businessTypeCode;
    /** 客户代码，外键关联客户主表主键 [20,0] Not NULL */
    private String customerNo;
    /** 所属机构号 [10,0] Not NULL */
    private String organNo;
    /**  运营模式 [3,0] Not NULL */
    private String operationMode;
    /** 账户组织形式 R：循环 [1,0] */
    private String accountOrganForm;
    /** 账户性质  D：借记账户  C ： 贷记账户 [1,0] */
    private String businessDebitCreditCode;
    /** 周期模式标志 Y：周期模式 N：非周期模式 [1,0] */
    private String cycleModeFlag;
    /** 状态码 1：活跃账户 2：非活跃账户 3：冻结账户 8：关闭账户 9：待删除账户 [1,0] */
    private String statusCode;
    /** 状态更新日期 [10,0] */
    private String statusUpdateDate;
    /** 新建日期 [10,0] */
    private String createDate;
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
    /**  约定扣款方式 1：账单余额  2：最低还款 [1,0] */
    private String directDebitType;
    /** 逾期状态  [10,0] */
    private Integer cycleDue;
    /** 上一逾期状态 [10,0] */
    private Integer prevCycleDue;
    /**  ABS状态，资产担保证券或资产支撑证券（Asset-backed security） [2,0] */
    private String absStatus;
    /**  当前周期号 [10,0] */
    private Integer currentCycleNumber;
    /** 宽限日 [10,0] */
    private String graceDate;
    /** 取值 <== 业务项目 [9,0] */
    private String programDesc;
    
    private String organName;
    private String businessDesc;
    private String productDesc;
    private String currencyDesc;
    
    /** 取值 <== 账户代码 : 账户代码 [18,0], Not NULL */
    public String getAccountId() {
        return accountId;
    }

    /** 赋值 ==> 账户代码 : 账户代码 [18,0], Not NULL */
    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
    }

    /** 取值 <== 币种 : 币种 [3,0], Not NULL */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /** 赋值 ==> 币种 : 币种 [3,0], Not NULL */
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode == null ? null : currencyCode.trim();
    }

    /** 取值 <== 原交易全局流水号 : 原交易全局流水号 [36,0] */
    public String getOldGlobalSerialNumbr() {
        return oldGlobalSerialNumbr;
    }

    /** 赋值 ==> 原交易全局流水号 : 原交易全局流水号 [36,0] */
    public void setOldGlobalSerialNumbr(String oldGlobalSerialNumbr) {
        this.oldGlobalSerialNumbr = oldGlobalSerialNumbr == null ? null : oldGlobalSerialNumbr.trim();
    }

    /** 取值 <== 外部识别号 : 外部识别号 [19,0] */
    public String getExternalIdentificationNo() {
        return externalIdentificationNo;
    }

    /** 赋值 ==> 外部识别号 : 外部识别号 [19,0] */
    public void setExternalIdentificationNo(String externalIdentificationNo) {
        this.externalIdentificationNo = externalIdentificationNo == null ? null : externalIdentificationNo.trim();
    }

    /** 取值 <== 争议金额 : 争议金额 [18,0] */
    public BigDecimal getDisputeAmount() {
        return disputeAmount;
    }

    /** 赋值 ==> 争议金额 : 争议金额 [18,0] */
    public void setDisputeAmount(BigDecimal disputeAmount) {
        this.disputeAmount = disputeAmount;
    }

    /** 取值 <== 账户余额 : 账户余额 [18,0] */
    public BigDecimal getBlanceAmount() {
        return blanceAmount;
    }

    /** 赋值 ==> 账户余额 : 账户余额 [18,0] */
    public void setBlanceAmount(BigDecimal blanceAmount) {
        this.blanceAmount = blanceAmount;
    }

    /** 取值 <== 溢缴款冻结金额 : 溢缴款冻结金额 [18,0] */
    public BigDecimal getOverpayFreezeAmount() {
        return overpayFreezeAmount;
    }

    /** 赋值 ==> 溢缴款冻结金额 : 溢缴款冻结金额 [18,0] */
    public void setOverpayFreezeAmount(BigDecimal overpayFreezeAmount) {
        this.overpayFreezeAmount = overpayFreezeAmount;
    }

    /** 取值 <== 溢缴款冻结状态 : 溢缴款冻结状态F-已冻结U-已解冻N-无冻结 [1,0] */
    public String getOverpayFreezeStatus() {
        return overpayFreezeStatus;
    }

    /** 赋值 ==> 溢缴款冻结状态 : 溢缴款冻结状态F-已冻结U-已解冻N-无冻结 [1,0] */
    public void setOverpayFreezeStatus(String overpayFreezeStatus) {
        this.overpayFreezeStatus = overpayFreezeStatus == null ? null : overpayFreezeStatus.trim();
    }

    /** 取值 <== 释放日期 : 释放日期 [10,0] */
    public String getFreeDate() {
        return freeDate;
    }

    /** 赋值 ==> 释放日期 : 释放日期 [10,0] */
    public void setFreeDate(String freeDate) {
        this.freeDate = freeDate == null ? null : freeDate.trim();
    }

    /** 取值 <== 原交易结息截止日 : 原交易结息截止日 [10,0] */
    public String getInterestExpirationDate() {
        return interestExpirationDate;
    }

    /** 赋值 ==> 原交易结息截止日 : 原交易结息截止日 [10,0] */
    public void setInterestExpirationDate(String interestExpirationDate) {
        this.interestExpirationDate = interestExpirationDate == null ? null : interestExpirationDate.trim();
    }

    /** 取值 <== 原交易已结记利息金额 : 原交易已结记利息金额 [18,0] */
    public BigDecimal getInterestAmt() {
        return interestAmt;
    }

    /** 赋值 ==> 原交易已结记利息金额 : 原交易已结记利息金额 [18,0] */
    public void setInterestAmt(BigDecimal interestAmt) {
        this.interestAmt = interestAmt;
    }

    /** 取值 <== 额度占用标识 : 额度占用标识0：不占用；1：占用 [1,0] */
    public String getAmtOccFlag() {
        return amtOccFlag;
    }

    /** 赋值 ==> 额度占用标识 : 额度占用标识0：不占用；1：占用 [1,0] */
    public void setAmtOccFlag(String amtOccFlag) {
        this.amtOccFlag = amtOccFlag == null ? null : amtOccFlag.trim();
    }

    /** 取值 <== 创建时间 : 创建时间 [23,0] */
    public String getGmtCreate() {
        return gmtCreate;
    }

    /** 赋值 ==> 创建时间 : 创建时间 [23,0] */
    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate == null ? null : gmtCreate.trim();
    }

    /** 取值 <== 时间戳 : 时间戳 [19,0], Not NULL */
    public Date getTimestamp() {
        return timestamp;
    }

    /** 赋值 ==> 时间戳 : 时间戳 [19,0], Not NULL */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /** 取值 <== 版本 [10,0], Not NULL */
    public Integer getVersion() {
        return version;
    }

    /** 赋值 ==> 版本 [10,0], Not NULL */
    public void setVersion(Integer version) {
        this.version = version;
    }

    
    public String getBusinessProgramNo() {
        return businessProgramNo;
    }

    /** 赋值 ==> 业务项目 [9,0] */
    public void setBusinessProgramNo(String businessProgramNo) {
        this.businessProgramNo = businessProgramNo == null ? null : businessProgramNo.trim();
    }

    /** 取值 <== 产品对象代码 [15,0] */
    public String getProductObjectCode() {
        return productObjectCode;
    }

    /** 赋值 ==> 产品对象代码 [15,0] */
    public void setProductObjectCode(String productObjectCode) {
        this.productObjectCode = productObjectCode == null ? null : productObjectCode.trim();
    }

    /** 取值 <== 所属业务类型 [15,0] */
    public String getBusinessTypeCode() {
        return businessTypeCode;
    }

    /** 赋值 ==> 所属业务类型 [15,0] */
    public void setBusinessTypeCode(String businessTypeCode) {
        this.businessTypeCode = businessTypeCode == null ? null : businessTypeCode.trim();
    }

    /** 取值 <== 客户代码，外键关联客户主表主键 [20,0], Not NULL */
    public String getCustomerNo() {
        return customerNo;
    }

    /** 赋值 ==> 客户代码，外键关联客户主表主键 [20,0], Not NULL */
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo == null ? null : customerNo.trim();
    }

    /** 取值 <== 所属机构号 [10,0], Not NULL */
    public String getOrganNo() {
        return organNo;
    }

    /** 赋值 ==> 所属机构号 [10,0], Not NULL */
    public void setOrganNo(String organNo) {
        this.organNo = organNo == null ? null : organNo.trim();
    }

    /** 取值 <==  运营模式 [3,0], Not NULL */
    public String getOperationMode() {
        return operationMode;
    }

    /** 赋值 ==>  运营模式 [3,0], Not NULL */
    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode == null ? null : operationMode.trim();
    }

    /** 取值 <== 账户组织形式 R：循环 [1,0] */
    public String getAccountOrganForm() {
        return accountOrganForm;
    }

    /** 赋值 ==> 账户组织形式 R：循环 [1,0] */
    public void setAccountOrganForm(String accountOrganForm) {
        this.accountOrganForm = accountOrganForm == null ? null : accountOrganForm.trim();
    }

    /** 取值 <== 账户性质  D：借记账户  C ： 贷记账户 [1,0] */
    public String getBusinessDebitCreditCode() {
        return businessDebitCreditCode;
    }

    /** 赋值 ==> 账户性质  D：借记账户  C ： 贷记账户 [1,0] */
    public void setBusinessDebitCreditCode(String businessDebitCreditCode) {
        this.businessDebitCreditCode = businessDebitCreditCode == null ? null : businessDebitCreditCode.trim();
    }

    /** 取值 <== 周期模式标志 Y：周期模式 N：非周期模式 [1,0] */
    public String getCycleModeFlag() {
        return cycleModeFlag;
    }

    /** 赋值 ==> 周期模式标志 Y：周期模式 N：非周期模式 [1,0] */
    public void setCycleModeFlag(String cycleModeFlag) {
        this.cycleModeFlag = cycleModeFlag == null ? null : cycleModeFlag.trim();
    }

    /** 取值 <== 状态码 1：活跃账户 2：非活跃账户 3：冻结账户 8：关闭账户 9：待删除账户 [1,0] */
    public String getStatusCode() {
        return statusCode;
    }

    /** 赋值 ==> 状态码 1：活跃账户 2：非活跃账户 3：冻结账户 8：关闭账户 9：待删除账户 [1,0] */
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode == null ? null : statusCode.trim();
    }

    /** 取值 <== 状态更新日期 [10,0] */
    public String getStatusUpdateDate() {
        return statusUpdateDate;
    }

    /** 赋值 ==> 状态更新日期 [10,0] */
    public void setStatusUpdateDate(String statusUpdateDate) {
        this.statusUpdateDate = statusUpdateDate == null ? null : statusUpdateDate.trim();
    }

    /** 取值 <== 新建日期 [10,0] */
    public String getCreateDate() {
        return createDate;
    }

    /** 赋值 ==> 新建日期 [10,0] */
    public void setCreateDate(String createDate) {
        this.createDate = createDate == null ? null : createDate.trim();
    }

    /** 取值 <== 关闭日期 [10,0] */
    public String getClosedDate() {
        return closedDate;
    }

    /** 赋值 ==> 关闭日期 [10,0] */
    public void setClosedDate(String closedDate) {
        this.closedDate = closedDate == null ? null : closedDate.trim();
    }

    /** 取值 <== 计息处理日 [10,0] */
    public String getInterestProcessDate() {
        return interestProcessDate;
    }

    /** 赋值 ==> 计息处理日 [10,0] */
    public void setInterestProcessDate(String interestProcessDate) {
        this.interestProcessDate = interestProcessDate == null ? null : interestProcessDate.trim();
    }

    /** 取值 <== 最后还款日 [10,0] */
    public String getPaymentDueDate() {
        return paymentDueDate;
    }

    /** 赋值 ==> 最后还款日 [10,0] */
    public void setPaymentDueDate(String paymentDueDate) {
        this.paymentDueDate = paymentDueDate == null ? null : paymentDueDate.trim();
    }

    /** 取值 <== 滞纳金产生日 [10,0] */
    public String getDelinquencyDate() {
        return delinquencyDate;
    }

    /** 赋值 ==> 滞纳金产生日 [10,0] */
    public void setDelinquencyDate(String delinquencyDate) {
        this.delinquencyDate = delinquencyDate == null ? null : delinquencyDate.trim();
    }

    /** 取值 <== 核算状态码 [3,0] */
    public String getAccountingStatusCode() {
        return accountingStatusCode;
    }

    /** 赋值 ==> 核算状态码 [3,0] */
    public void setAccountingStatusCode(String accountingStatusCode) {
        this.accountingStatusCode = accountingStatusCode == null ? null : accountingStatusCode.trim();
    }

    /** 取值 <== 核算状态日期 [10,0] */
    public String getAccountingStatusDate() {
        return accountingStatusDate;
    }

    /** 赋值 ==> 核算状态日期 [10,0] */
    public void setAccountingStatusDate(String accountingStatusDate) {
        this.accountingStatusDate = accountingStatusDate == null ? null : accountingStatusDate.trim();
    }

    /** 取值 <== 上一核算状态码 [3,0] */
    public String getPrevAccountingStatusCode() {
        return prevAccountingStatusCode;
    }

    /** 赋值 ==> 上一核算状态码 [3,0] */
    public void setPrevAccountingStatusCode(String prevAccountingStatusCode) {
        this.prevAccountingStatusCode = prevAccountingStatusCode == null ? null : prevAccountingStatusCode.trim();
    }

    /** 取值 <== 上一核算状态日期 [10,0] */
    public String getPrevAccountingStatusDate() {
        return prevAccountingStatusDate;
    }

    /** 赋值 ==> 上一核算状态日期 [10,0] */
    public void setPrevAccountingStatusDate(String prevAccountingStatusDate) {
        this.prevAccountingStatusDate = prevAccountingStatusDate == null ? null : prevAccountingStatusDate.trim();
    }

    /** 取值 <==  约定扣款方式 1：账单余额  2：最低还款 [1,0] */
    public String getDirectDebitType() {
        return directDebitType;
    }

    /** 赋值 ==>  约定扣款方式 1：账单余额  2：最低还款 [1,0] */
    public void setDirectDebitType(String directDebitType) {
        this.directDebitType = directDebitType == null ? null : directDebitType.trim();
    }

    /** 取值 <== 逾期状态  [10,0] */
    public Integer getCycleDue() {
        return cycleDue;
    }

    /** 赋值 ==> 逾期状态  [10,0] */
    public void setCycleDue(Integer cycleDue) {
        this.cycleDue = cycleDue;
    }

    /** 取值 <== 上一逾期状态 [10,0] */
    public Integer getPrevCycleDue() {
        return prevCycleDue;
    }

    /** 赋值 ==> 上一逾期状态 [10,0] */
    public void setPrevCycleDue(Integer prevCycleDue) {
        this.prevCycleDue = prevCycleDue;
    }

    /** 取值 <==  ABS状态，资产担保证券或资产支撑证券（Asset-backed security） [2,0] */
    public String getAbsStatus() {
        return absStatus;
    }

    /** 赋值 ==>  ABS状态，资产担保证券或资产支撑证券（Asset-backed security） [2,0] */
    public void setAbsStatus(String absStatus) {
        this.absStatus = absStatus == null ? null : absStatus.trim();
    }

    /** 取值 <==  当前周期号 [10,0] */
    public Integer getCurrentCycleNumber() {
        return currentCycleNumber;
    }

    /** 赋值 ==>  当前周期号 [10,0] */
    public void setCurrentCycleNumber(Integer currentCycleNumber) {
        this.currentCycleNumber = currentCycleNumber;
    }

    /** 取值 <== 宽限日 [10,0] */
    public String getGraceDate() {
        return graceDate;
    }

    /** 赋值 ==> 宽限日 [10,0] */
    public void setGraceDate(String graceDate) {
        this.graceDate = graceDate == null ? null : graceDate.trim();
    }

	public String getProgramDesc() {
		return programDesc;
	}

	public void setProgramDesc(String programDesc) {
		this.programDesc = programDesc;
	}

	public String getOrganName() {
		return organName;
	}

	public void setOrganName(String organName) {
		this.organName = organName;
	}

	public String getBusinessDesc() {
		return businessDesc;
	}

	public void setBusinessDesc(String businessDesc) {
		this.businessDesc = businessDesc;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public String getCurrencyDesc() {
		return currencyDesc;
	}

	public void setCurrencyDesc(String currencyDesc) {
		this.currencyDesc = currencyDesc;
	}

}
