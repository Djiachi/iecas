package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**账单日查询返回
 * 
 * @author sunyaoyao 2019.03.12
 *
 */
public class X5630VO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5123786561346277457L;
	 /** 交易账户代码 : 交易账户代码 [23,0] Not NULL */
    protected String id;
	/** 交易账户代码 : 交易账户代码 [23,0] Not NULL */
    protected String accountId;
    /** 币种 : 币种 [3,0] Not NULL */
    protected String currencyCode;
    /** 客户代码 : 客户代码 [12,0] Not NULL */
    protected String customerNo;
    /** 外部识别号 : 外部识别号 [32,0] */
    protected String externalIdentificationNo;
    /** 贷款分类 : MERH：商户分期TXAT：自动分期CASH：现金分期STMT：账单分期TRAN：交易分期 [4,0] Not NULL */
    protected String loanType;
    /** 贷款编号 : 贷款编号 [32,0] Not NULL */
    protected String loanNum;
    /** 贷款期限 : 贷款期限 [10,0] Not NULL */
    protected Integer loanTerm;
    /** 贷款期限单位 : 贷款期限单位 [3,0] Not NULL */
    protected String termUnit;
    /** 贷款起始日 : 贷款起始日 [10,0] Not NULL */
    protected String loanStartDate;
    /** 贷款到期日 : 贷款到期日 [10,0] Not NULL */
    protected String loanEndDate;
    /** 贷款金额 : 贷款金额 [18,0] Not NULL */
    protected BigDecimal loanAmount;
    /** 还款期数 : 还款期数 [10,0] */
    protected Integer repayedPeriods;
    /** 利率/费率调整日期 : 利率/费率调整日期 [10,0] */
    protected String rateAdjustDate;
    /** 放款日期 : 放款日期 [10,0] */
    protected String payDate;
    /** 执行利率 : 执行利率 [18,6] */
    protected BigDecimal loanRate;
    /** 贷款实际到期日 : 贷款实际到期日 [10,0] */
    protected String actualEndDate;
    /** 下次抛账日 : 下次抛账日 [10,0] */
    protected String nextThrowDate;
    /** 是否产生还款计划 : 是否产生还款计划 [1,0] */
    protected String isMakePlan;
    /** 生产未来还款计划期数 : 生产未来还款计划期数 [10,0] */
    protected Integer makePlanPeriod;
    /** 首期抛账周期号 : 首期抛账周期号 [10,0] */
    protected Integer firstCycleNo;
    /** 起息日期 : 起息日期 [10,0] */
    protected String startIntDate;
    /** 手续费率 : 手续费率 [18,6] */
    protected BigDecimal feeRate;
    /** 已入账期数 : 已入账期数 [10,0] */
    protected Integer throwedPeriod;
    /** 剩余未抛期数 : 剩余期数 [10,0] */
    protected Integer remainPeriod;
    /** 剩余未抛本金 : 剩余未抛本金 [18,0] */
    protected BigDecimal remainPrincipalAmount;
    /** 手续费 : 手续费 [18,0] */
    protected BigDecimal feeAmount;
    /** 剩余未抛手续费 : 剩余未抛手续费 [18,0] */
    protected BigDecimal remainFeeAmount;
    /** 下次利率生效日期 消费信贷使用。根据利率生效方式获取。如果是实时生效时，此项不赋值 [10,0] */
    protected String nextIntAdjustDate;
    /** Pending生效利率 利率变更当日，下次利率生效日期不是立即生效时，此项记录变更后利率。当利率生效后，此项清空，生效后的利率记录到贷款利率项 [18,6] */
    protected BigDecimal effectIntRate;
    /** 创建时间 : 创建时间 [23,0] */
    protected String gmtCreate;
    /** 时间戳 [19,0] Not NULL */
    protected Date timeStamp;
    /** 版本号 : 版本号 [10,0] Not NULL */
    protected Integer version;
    /** 还款方式 [2,0] */
    protected String repayMode;
    /** 还款日类型 [2,0] */
    protected String repaymentDateType;
    /** 免息天数 [10,0] */
    protected Integer freeDays;
    /** 还款周期 [10,0] */
    protected Integer repayPrincipalCycle;
    /** 还款周期单位 [25,0] */
    protected String repayPrincipalUnit;
    /** 还款日 [10,0] */
    protected Integer repayDay;
    /** 罚息利率上浮比例 [10,4] */
    protected BigDecimal penaltyUp;
    /** 罚息利率 [18,6] */
    protected BigDecimal penaltyInterRate;
    /** 总利息 [18,0] */
    protected BigDecimal interAmount;
    /** 剩余未抛利息 [18,0] */
    protected BigDecimal remainInterAmount;
    /** 收款账户银行号 [7,0] */
    protected String directDebitBankNo;
    /** 约定收款账户号 [25,0] */
    protected String directDebitAccountNo;
    /** 状态(0：撤销；1：正常；2：逾期；3；结算) [2,0] */
    protected String status;
    /** 利息罚息利率 [18,6] */
    protected BigDecimal intPenaltyInterRate;
    /** 原交易全局流水号 : 原交易全局流水号 [36,0] */
    protected String oldGlobalSerialNumbr;
    /** 计算用分期期次 [10,0] */
    protected Integer calInstallmentPeriod;
    /** 已支付金额 [18,0] */
    protected BigDecimal prepaidAmount;
    /** 1：未支付 2：已支付 3：部分支付 [2,0] */
    protected String payStatus;
    /** 支付计划 : 0: 当日实时一次性支付;1: 当日批量一次性支付;2:客户电话或在线申请实时支付贷款;3:客户设置支付日期一次或多次批量支付 [4,0] */
    protected String paymentPlan;
    /** 当前期次 [10,0] */
    protected Integer currentPeriod;
    /** 每期手续费 [18,0] */
    protected BigDecimal loanFixedFee;
    /** 首期手续费 [18,0] */
    protected BigDecimal loanFirstTermFee;
    /** 末期手续费 [18,0] */
    protected BigDecimal loanFinalTermFee;
    /** 每期本金 [18,0] */
    protected BigDecimal loanFixedPrincipal;
    /** 首期本金 [18,0] */
    protected BigDecimal loanFirstTermPrincipal;
    /** 末期本金 [18,0] */
    protected BigDecimal loanFinalTermPrincipal;
    /** 已生成还款计划本金 [18,0] */
    protected BigDecimal loanPrincipalDue;
    /** 未生成还款计划剩余本金 [18,0] */
    protected BigDecimal loanPrincipalUndue;
    /** 已生成还款计划手续费 [18,0] */
    protected BigDecimal loanFeeDue;
    /** 未生成还款计划剩余手续费 [18,0] */
    protected BigDecimal loanFeeUndue;
    /** 下次生成还款计划日期 : 下次生成还款计划日期 [10,0] */
    protected String nextMakePlanDate;
    /** 首次费用收取期次 [10,0] */
    protected Integer feeStartPeriod;
    /** 费用收取方式 [3,0] */
    protected String feeCollectType;
    /** 费用编号 [8,0] */
    protected String feeItemNo;
    /** 资金协议编号 [64,0] */
    protected String trustNum;
    /** 资金协议id [64,0] */
    protected String trustId;
    
    //account
    /** 业务项目 [9,0] */
    protected String businessProgramNo;
    /** 产品对象代码 [15,0] */
    protected String productObjectCode;
    /** 所属业务类型 [15,0] */
    protected String businessTypeCode;
    /** 分期类型描述[15,0] */
    protected String loanTypeDesc;
    
    /** 所属机构号 [10,0] Not NULL */
    protected String organNo;
    /** 运营模式 [3,0] Not NULL */
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
    /** 逾期状态 [10,0] */
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
    /** 交易识别代码 [4,0] */
    protected String transIdentifiNo;
    /** 资金方代码 [25,0] */
    protected String fundNum;
    /** 全局流水号（交易级账户使用） [32,0] */
    protected String globalTransSerialNo;
	/** 资产证券化计划代码 [6,0] */
	protected String absPlanId;
    /** 前债权ID [25,0] */
    protected String prevFundNum;
    /** 子账户标识 [1,0] Not NULL */
    protected String subAccIdentify;
    /** flag */
    protected boolean haveChild;
	/** 当前总余额 */
	protected BigDecimal currentTotalBalance;
	/** 是否可以操作 */
	private String operation;
	/** 分期類型 */
	private String stageType;
	/** 资金方名称 */
	private String fundName;
	/** 交易识别描述 [50,0] */
    protected String transIdentifiDesc;
    //abs plan
	/** abs资产属性 R-循环账户 T-交易账户 B-不良资产 [1,0] */
	protected String accountType;

	public static long getSerialVersionUID() {
		return serialVersionUID;
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

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}

	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public String getLoanNum() {
		return loanNum;
	}

	public void setLoanNum(String loanNum) {
		this.loanNum = loanNum;
	}

	public Integer getLoanTerm() {
		return loanTerm;
	}

	public void setLoanTerm(Integer loanTerm) {
		this.loanTerm = loanTerm;
	}

	public String getTermUnit() {
		return termUnit;
	}

	public void setTermUnit(String termUnit) {
		this.termUnit = termUnit;
	}

	public String getLoanStartDate() {
		return loanStartDate;
	}

	public void setLoanStartDate(String loanStartDate) {
		this.loanStartDate = loanStartDate;
	}

	public String getLoanEndDate() {
		return loanEndDate;
	}

	public void setLoanEndDate(String loanEndDate) {
		this.loanEndDate = loanEndDate;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public Integer getRepayedPeriods() {
		return repayedPeriods;
	}

	public void setRepayedPeriods(Integer repayedPeriods) {
		this.repayedPeriods = repayedPeriods;
	}

	public String getRateAdjustDate() {
		return rateAdjustDate;
	}

	public void setRateAdjustDate(String rateAdjustDate) {
		this.rateAdjustDate = rateAdjustDate;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public BigDecimal getLoanRate() {
		return loanRate;
	}

	public void setLoanRate(BigDecimal loanRate) {
		this.loanRate = loanRate;
	}

	public String getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(String actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	public String getNextThrowDate() {
		return nextThrowDate;
	}

	public void setNextThrowDate(String nextThrowDate) {
		this.nextThrowDate = nextThrowDate;
	}

	public String getIsMakePlan() {
		return isMakePlan;
	}

	public void setIsMakePlan(String isMakePlan) {
		this.isMakePlan = isMakePlan;
	}

	public Integer getMakePlanPeriod() {
		return makePlanPeriod;
	}

	public void setMakePlanPeriod(Integer makePlanPeriod) {
		this.makePlanPeriod = makePlanPeriod;
	}

	public Integer getFirstCycleNo() {
		return firstCycleNo;
	}

	public void setFirstCycleNo(Integer firstCycleNo) {
		this.firstCycleNo = firstCycleNo;
	}

	public String getStartIntDate() {
		return startIntDate;
	}

	public void setStartIntDate(String startIntDate) {
		this.startIntDate = startIntDate;
	}

	public BigDecimal getFeeRate() {
		return feeRate;
	}

	public void setFeeRate(BigDecimal feeRate) {
		this.feeRate = feeRate;
	}

	public Integer getThrowedPeriod() {
		return throwedPeriod;
	}

	public void setThrowedPeriod(Integer throwedPeriod) {
		this.throwedPeriod = throwedPeriod;
	}

	public Integer getRemainPeriod() {
		return remainPeriod;
	}

	public void setRemainPeriod(Integer remainPeriod) {
		this.remainPeriod = remainPeriod;
	}

	public BigDecimal getRemainPrincipalAmount() {
		return remainPrincipalAmount;
	}

	public void setRemainPrincipalAmount(BigDecimal remainPrincipalAmount) {
		this.remainPrincipalAmount = remainPrincipalAmount;
	}

	public BigDecimal getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}

	public BigDecimal getRemainFeeAmount() {
		return remainFeeAmount;
	}

	public void setRemainFeeAmount(BigDecimal remainFeeAmount) {
		this.remainFeeAmount = remainFeeAmount;
	}

	public String getNextIntAdjustDate() {
		return nextIntAdjustDate;
	}

	public void setNextIntAdjustDate(String nextIntAdjustDate) {
		this.nextIntAdjustDate = nextIntAdjustDate;
	}

	public BigDecimal getEffectIntRate() {
		return effectIntRate;
	}

	public void setEffectIntRate(BigDecimal effectIntRate) {
		this.effectIntRate = effectIntRate;
	}

	public String getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getRepayMode() {
		return repayMode;
	}

	public void setRepayMode(String repayMode) {
		this.repayMode = repayMode;
	}

	public String getRepaymentDateType() {
		return repaymentDateType;
	}

	public void setRepaymentDateType(String repaymentDateType) {
		this.repaymentDateType = repaymentDateType;
	}

	public Integer getFreeDays() {
		return freeDays;
	}

	public void setFreeDays(Integer freeDays) {
		this.freeDays = freeDays;
	}

	public Integer getRepayPrincipalCycle() {
		return repayPrincipalCycle;
	}

	public void setRepayPrincipalCycle(Integer repayPrincipalCycle) {
		this.repayPrincipalCycle = repayPrincipalCycle;
	}

	public String getRepayPrincipalUnit() {
		return repayPrincipalUnit;
	}

	public void setRepayPrincipalUnit(String repayPrincipalUnit) {
		this.repayPrincipalUnit = repayPrincipalUnit;
	}

	public Integer getRepayDay() {
		return repayDay;
	}

	public void setRepayDay(Integer repayDay) {
		this.repayDay = repayDay;
	}

	public BigDecimal getPenaltyUp() {
		return penaltyUp;
	}

	public void setPenaltyUp(BigDecimal penaltyUp) {
		this.penaltyUp = penaltyUp;
	}

	public BigDecimal getPenaltyInterRate() {
		return penaltyInterRate;
	}

	public void setPenaltyInterRate(BigDecimal penaltyInterRate) {
		this.penaltyInterRate = penaltyInterRate;
	}

	public BigDecimal getInterAmount() {
		return interAmount;
	}

	public void setInterAmount(BigDecimal interAmount) {
		this.interAmount = interAmount;
	}

	public BigDecimal getRemainInterAmount() {
		return remainInterAmount;
	}

	public void setRemainInterAmount(BigDecimal remainInterAmount) {
		this.remainInterAmount = remainInterAmount;
	}

	public String getDirectDebitBankNo() {
		return directDebitBankNo;
	}

	public void setDirectDebitBankNo(String directDebitBankNo) {
		this.directDebitBankNo = directDebitBankNo;
	}

	public String getDirectDebitAccountNo() {
		return directDebitAccountNo;
	}

	public void setDirectDebitAccountNo(String directDebitAccountNo) {
		this.directDebitAccountNo = directDebitAccountNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getIntPenaltyInterRate() {
		return intPenaltyInterRate;
	}

	public void setIntPenaltyInterRate(BigDecimal intPenaltyInterRate) {
		this.intPenaltyInterRate = intPenaltyInterRate;
	}

	public String getOldGlobalSerialNumbr() {
		return oldGlobalSerialNumbr;
	}

	public void setOldGlobalSerialNumbr(String oldGlobalSerialNumbr) {
		this.oldGlobalSerialNumbr = oldGlobalSerialNumbr;
	}

	public Integer getCalInstallmentPeriod() {
		return calInstallmentPeriod;
	}

	public void setCalInstallmentPeriod(Integer calInstallmentPeriod) {
		this.calInstallmentPeriod = calInstallmentPeriod;
	}

	public BigDecimal getPrepaidAmount() {
		return prepaidAmount;
	}

	public void setPrepaidAmount(BigDecimal prepaidAmount) {
		this.prepaidAmount = prepaidAmount;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getPaymentPlan() {
		return paymentPlan;
	}

	public void setPaymentPlan(String paymentPlan) {
		this.paymentPlan = paymentPlan;
	}

	public Integer getCurrentPeriod() {
		return currentPeriod;
	}

	public void setCurrentPeriod(Integer currentPeriod) {
		this.currentPeriod = currentPeriod;
	}

	public BigDecimal getLoanFixedFee() {
		return loanFixedFee;
	}

	public void setLoanFixedFee(BigDecimal loanFixedFee) {
		this.loanFixedFee = loanFixedFee;
	}

	public BigDecimal getLoanFirstTermFee() {
		return loanFirstTermFee;
	}

	public void setLoanFirstTermFee(BigDecimal loanFirstTermFee) {
		this.loanFirstTermFee = loanFirstTermFee;
	}

	public BigDecimal getLoanFinalTermFee() {
		return loanFinalTermFee;
	}

	public void setLoanFinalTermFee(BigDecimal loanFinalTermFee) {
		this.loanFinalTermFee = loanFinalTermFee;
	}

	public BigDecimal getLoanFixedPrincipal() {
		return loanFixedPrincipal;
	}

	public void setLoanFixedPrincipal(BigDecimal loanFixedPrincipal) {
		this.loanFixedPrincipal = loanFixedPrincipal;
	}

	public BigDecimal getLoanFirstTermPrincipal() {
		return loanFirstTermPrincipal;
	}

	public void setLoanFirstTermPrincipal(BigDecimal loanFirstTermPrincipal) {
		this.loanFirstTermPrincipal = loanFirstTermPrincipal;
	}

	public BigDecimal getLoanFinalTermPrincipal() {
		return loanFinalTermPrincipal;
	}

	public void setLoanFinalTermPrincipal(BigDecimal loanFinalTermPrincipal) {
		this.loanFinalTermPrincipal = loanFinalTermPrincipal;
	}

	public BigDecimal getLoanPrincipalDue() {
		return loanPrincipalDue;
	}

	public void setLoanPrincipalDue(BigDecimal loanPrincipalDue) {
		this.loanPrincipalDue = loanPrincipalDue;
	}

	public BigDecimal getLoanPrincipalUndue() {
		return loanPrincipalUndue;
	}

	public void setLoanPrincipalUndue(BigDecimal loanPrincipalUndue) {
		this.loanPrincipalUndue = loanPrincipalUndue;
	}

	public BigDecimal getLoanFeeDue() {
		return loanFeeDue;
	}

	public void setLoanFeeDue(BigDecimal loanFeeDue) {
		this.loanFeeDue = loanFeeDue;
	}

	public BigDecimal getLoanFeeUndue() {
		return loanFeeUndue;
	}

	public void setLoanFeeUndue(BigDecimal loanFeeUndue) {
		this.loanFeeUndue = loanFeeUndue;
	}

	public String getNextMakePlanDate() {
		return nextMakePlanDate;
	}

	public void setNextMakePlanDate(String nextMakePlanDate) {
		this.nextMakePlanDate = nextMakePlanDate;
	}

	public Integer getFeeStartPeriod() {
		return feeStartPeriod;
	}

	public void setFeeStartPeriod(Integer feeStartPeriod) {
		this.feeStartPeriod = feeStartPeriod;
	}

	public String getFeeCollectType() {
		return feeCollectType;
	}

	public void setFeeCollectType(String feeCollectType) {
		this.feeCollectType = feeCollectType;
	}

	public String getFeeItemNo() {
		return feeItemNo;
	}

	public void setFeeItemNo(String feeItemNo) {
		this.feeItemNo = feeItemNo;
	}

	public String getTrustNum() {
		return trustNum;
	}

	public void setTrustNum(String trustNum) {
		this.trustNum = trustNum;
	}

	public String getTrustId() {
		return trustId;
	}

	public void setTrustId(String trustId) {
		this.trustId = trustId;
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

	public String getLoanTypeDesc() {
		return loanTypeDesc;
	}

	public void setLoanTypeDesc(String loanTypeDesc) {
		this.loanTypeDesc = loanTypeDesc;
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

	public String getGlobalTransSerialNo() {
		return globalTransSerialNo;
	}

	public void setGlobalTransSerialNo(String globalTransSerialNo) {
		this.globalTransSerialNo = globalTransSerialNo;
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

	public boolean isHaveChild() {
		return haveChild;
	}

	public void setHaveChild(boolean haveChild) {
		this.haveChild = haveChild;
	}

	public BigDecimal getCurrentTotalBalance() {
		return currentTotalBalance;
	}

	public void setCurrentTotalBalance(BigDecimal currentTotalBalance) {
		this.currentTotalBalance = currentTotalBalance;
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
	public String getStageType() {
		return stageType;
	}

	public void setStageType(String stageType) {
		this.stageType = stageType;
	}

	public String getTransIdentifiDesc() {
		return transIdentifiDesc;
	}

	public void setTransIdentifiDesc(String transIdentifiDesc) {
		this.transIdentifiDesc = transIdentifiDesc;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	@Override
	public String toString() {
		return "X5630VO{" +
				"id='" + id + '\'' +
				", accountId='" + accountId + '\'' +
				", currencyCode='" + currencyCode + '\'' +
				", customerNo='" + customerNo + '\'' +
				", externalIdentificationNo='" + externalIdentificationNo + '\'' +
				", loanType='" + loanType + '\'' +
				", loanNum='" + loanNum + '\'' +
				", loanTerm=" + loanTerm +
				", termUnit='" + termUnit + '\'' +
				", loanStartDate='" + loanStartDate + '\'' +
				", loanEndDate='" + loanEndDate + '\'' +
				", loanAmount=" + loanAmount +
				", repayedPeriods=" + repayedPeriods +
				", rateAdjustDate='" + rateAdjustDate + '\'' +
				", payDate='" + payDate + '\'' +
				", loanRate=" + loanRate +
				", actualEndDate='" + actualEndDate + '\'' +
				", nextThrowDate='" + nextThrowDate + '\'' +
				", isMakePlan='" + isMakePlan + '\'' +
				", makePlanPeriod=" + makePlanPeriod +
				", firstCycleNo=" + firstCycleNo +
				", startIntDate='" + startIntDate + '\'' +
				", feeRate=" + feeRate +
				", throwedPeriod=" + throwedPeriod +
				", remainPeriod=" + remainPeriod +
				", remainPrincipalAmount=" + remainPrincipalAmount +
				", feeAmount=" + feeAmount +
				", remainFeeAmount=" + remainFeeAmount +
				", nextIntAdjustDate='" + nextIntAdjustDate + '\'' +
				", effectIntRate=" + effectIntRate +
				", gmtCreate='" + gmtCreate + '\'' +
				", timeStamp=" + timeStamp +
				", version=" + version +
				", repayMode='" + repayMode + '\'' +
				", repaymentDateType='" + repaymentDateType + '\'' +
				", freeDays=" + freeDays +
				", repayPrincipalCycle=" + repayPrincipalCycle +
				", repayPrincipalUnit='" + repayPrincipalUnit + '\'' +
				", repayDay=" + repayDay +
				", penaltyUp=" + penaltyUp +
				", penaltyInterRate=" + penaltyInterRate +
				", interAmount=" + interAmount +
				", remainInterAmount=" + remainInterAmount +
				", directDebitBankNo='" + directDebitBankNo + '\'' +
				", directDebitAccountNo='" + directDebitAccountNo + '\'' +
				", status='" + status + '\'' +
				", intPenaltyInterRate=" + intPenaltyInterRate +
				", oldGlobalSerialNumbr='" + oldGlobalSerialNumbr + '\'' +
				", calInstallmentPeriod=" + calInstallmentPeriod +
				", prepaidAmount=" + prepaidAmount +
				", payStatus='" + payStatus + '\'' +
				", paymentPlan='" + paymentPlan + '\'' +
				", currentPeriod=" + currentPeriod +
				", loanFixedFee=" + loanFixedFee +
				", loanFirstTermFee=" + loanFirstTermFee +
				", loanFinalTermFee=" + loanFinalTermFee +
				", loanFixedPrincipal=" + loanFixedPrincipal +
				", loanFirstTermPrincipal=" + loanFirstTermPrincipal +
				", loanFinalTermPrincipal=" + loanFinalTermPrincipal +
				", loanPrincipalDue=" + loanPrincipalDue +
				", loanPrincipalUndue=" + loanPrincipalUndue +
				", loanFeeDue=" + loanFeeDue +
				", loanFeeUndue=" + loanFeeUndue +
				", nextMakePlanDate='" + nextMakePlanDate + '\'' +
				", feeStartPeriod=" + feeStartPeriod +
				", feeCollectType='" + feeCollectType + '\'' +
				", feeItemNo='" + feeItemNo + '\'' +
				", trustNum='" + trustNum + '\'' +
				", trustId='" + trustId + '\'' +
				", businessProgramNo='" + businessProgramNo + '\'' +
				", productObjectCode='" + productObjectCode + '\'' +
				", businessTypeCode='" + businessTypeCode + '\'' +
				", loanTypeDesc='" + loanTypeDesc + '\'' +
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
				", transIdentifiNo='" + transIdentifiNo + '\'' +
				", fundNum='" + fundNum + '\'' +
				", globalTransSerialNo='" + globalTransSerialNo + '\'' +
				", absPlanId='" + absPlanId + '\'' +
				", prevFundNum='" + prevFundNum + '\'' +
				", subAccIdentify='" + subAccIdentify + '\'' +
				", haveChild=" + haveChild +
				", currentTotalBalance=" + currentTotalBalance +
				", operation='" + operation + '\'' +
				", stageType='" + stageType + '\'' +
				", fundName='" + fundName + '\'' +
				", transIdentifiDesc='" + transIdentifiDesc + '\'' +
				", accountType='" + accountType + '\'' +
				'}';
	}
}
