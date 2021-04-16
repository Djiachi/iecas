package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.tansun.ider.framwork.commun.BeanVO;

public class X5615VO  extends BeanVO implements Serializable{

	private static final long serialVersionUID = -2563282886664492663L;
	private  BigDecimal totalBalance;
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
    /** 剩余期数 : 剩余期数 [10,0] */
    protected Integer remainPeriod;
    /** 剩余本金 : 剩余本金 [18,0] */
    protected BigDecimal remainPrincipalAmount;
    /** 手续费 : 手续费 [18,0] */
    protected BigDecimal feeAmount;
    /** 剩余手续费 : 剩余手续费 [18,0] */
    protected BigDecimal remainFeeAmount;
    /** 下次利率生效日期 消费信贷使用。根据利率生效方式获取。如果是实时生效时，此项不赋值 [10,0] */
    protected String nextIntAdjustDate;
    /** Pending生效利率 利率变更当日，下次利率生效日期不是立即生效时，此项记录变更后利率。当利率生效后，此项清空，生效后的利率记录到贷款利率项 [18,6] */
    protected BigDecimal effectIntRate;
    /** 创建时间 : 创建时间 [23,0] */
    protected String gmtCreate;
    /** 时间戳 : 时间戳 [19,0] Not NULL */
    protected Date timestamp;
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
    /** 还款日 [10,0] */
    protected Integer repayDay;
    /** 罚息利率上浮比例 [10,4] */
    protected BigDecimal penaltyUp;
    /** 罚息利率 [18,6] */
    protected BigDecimal penaltyInterRate;
    /** 总利息 [18,0] */
    protected BigDecimal interAmount;
    /** 剩余利息 [18,0] */
    protected BigDecimal remainInterAmount;
    /** 收款账户银行号 [7,0] */
    protected String directDebitBankNo;
    /** 约定收款账户号 [25,0] */
    protected String directDebitAccountNo;
    /** 状态(0：撤销；1：正常；2：逾期；3；结算) [2,0] */
    protected String status;
    /** 利息罚息利率 [18,6] */
    protected BigDecimal intPenaltyInterRate;
    /** 分期原交易流水号 [36,0] */
    protected String oldGlobalSerialNumbr;
    /** 首次费用收取期次 [10,0] */
    protected Integer feeStartPeriod;
    
    public Integer getFeeStartPeriod() {
		return feeStartPeriod;
	}

	public void setFeeStartPeriod(Integer feeStartPeriod) {
		this.feeStartPeriod = feeStartPeriod;
	}

	/** 取值 <== 交易账户代码 : 交易账户代码 [23,0], Not NULL */
    public String getAccountId() {
        return accountId;
    }

    /** 赋值 ==> 交易账户代码 : 交易账户代码 [23,0], Not NULL */
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

    /** 取值 <== 客户代码 : 客户代码 [12,0], Not NULL */
    public String getCustomerNo() {
        return customerNo;
    }

    /** 赋值 ==> 客户代码 : 客户代码 [12,0], Not NULL */
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo == null ? null : customerNo.trim();
    }

    /** 取值 <== 外部识别号 : 外部识别号 [32,0] */
    public String getExternalIdentificationNo() {
        return externalIdentificationNo;
    }

    /** 赋值 ==> 外部识别号 : 外部识别号 [32,0] */
    public void setExternalIdentificationNo(String externalIdentificationNo) {
        this.externalIdentificationNo = externalIdentificationNo == null ? null : externalIdentificationNo.trim();
    }

    /** 取值 <== 贷款分类 : MERH：商户分期TXAT：自动分期CASH：现金分期STMT：账单分期TRAN：交易分期 [4,0], Not NULL */
    public String getLoanType() {
        return loanType;
    }

    /** 赋值 ==> 贷款分类 : MERH：商户分期TXAT：自动分期CASH：现金分期STMT：账单分期TRAN：交易分期 [4,0], Not NULL */
    public void setLoanType(String loanType) {
        this.loanType = loanType == null ? null : loanType.trim();
    }

    /** 取值 <== 贷款编号 : 贷款编号 [32,0], Not NULL */
    public String getLoanNum() {
        return loanNum;
    }

    /** 赋值 ==> 贷款编号 : 贷款编号 [32,0], Not NULL */
    public void setLoanNum(String loanNum) {
        this.loanNum = loanNum == null ? null : loanNum.trim();
    }

    /** 取值 <== 贷款期限 : 贷款期限 [10,0], Not NULL */
    public Integer getLoanTerm() {
        return loanTerm;
    }

    /** 赋值 ==> 贷款期限 : 贷款期限 [10,0], Not NULL */
    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
    }

    /** 取值 <== 贷款期限单位 : 贷款期限单位 [3,0], Not NULL */
    public String getTermUnit() {
        return termUnit;
    }

    /** 赋值 ==> 贷款期限单位 : 贷款期限单位 [3,0], Not NULL */
    public void setTermUnit(String termUnit) {
        this.termUnit = termUnit == null ? null : termUnit.trim();
    }

    /** 取值 <== 贷款起始日 : 贷款起始日 [10,0], Not NULL */
    public String getLoanStartDate() {
        return loanStartDate;
    }

    /** 赋值 ==> 贷款起始日 : 贷款起始日 [10,0], Not NULL */
    public void setLoanStartDate(String loanStartDate) {
        this.loanStartDate = loanStartDate == null ? null : loanStartDate.trim();
    }

    /** 取值 <== 贷款到期日 : 贷款到期日 [10,0], Not NULL */
    public String getLoanEndDate() {
        return loanEndDate;
    }

    /** 赋值 ==> 贷款到期日 : 贷款到期日 [10,0], Not NULL */
    public void setLoanEndDate(String loanEndDate) {
        this.loanEndDate = loanEndDate == null ? null : loanEndDate.trim();
    }

    /** 取值 <== 贷款金额 : 贷款金额 [18,0], Not NULL */
    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    /** 赋值 ==> 贷款金额 : 贷款金额 [18,0], Not NULL */
    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    /** 取值 <== 还款期数 : 还款期数 [10,0] */
    public Integer getRepayedPeriods() {
        return repayedPeriods;
    }

    /** 赋值 ==> 还款期数 : 还款期数 [10,0] */
    public void setRepayedPeriods(Integer repayedPeriods) {
        this.repayedPeriods = repayedPeriods;
    }

    /** 取值 <== 利率/费率调整日期 : 利率/费率调整日期 [10,0] */
    public String getRateAdjustDate() {
        return rateAdjustDate;
    }

    /** 赋值 ==> 利率/费率调整日期 : 利率/费率调整日期 [10,0] */
    public void setRateAdjustDate(String rateAdjustDate) {
        this.rateAdjustDate = rateAdjustDate == null ? null : rateAdjustDate.trim();
    }

    /** 取值 <== 放款日期 : 放款日期 [10,0] */
    public String getPayDate() {
        return payDate;
    }

    /** 赋值 ==> 放款日期 : 放款日期 [10,0] */
    public void setPayDate(String payDate) {
        this.payDate = payDate == null ? null : payDate.trim();
    }

    /** 取值 <== 执行利率 : 执行利率 [18,6] */
    public BigDecimal getLoanRate() {
        return loanRate;
    }

    /** 赋值 ==> 执行利率 : 执行利率 [18,6] */
    public void setLoanRate(BigDecimal loanRate) {
        this.loanRate = loanRate;
    }

    /** 取值 <== 贷款实际到期日 : 贷款实际到期日 [10,0] */
    public String getActualEndDate() {
        return actualEndDate;
    }

    /** 赋值 ==> 贷款实际到期日 : 贷款实际到期日 [10,0] */
    public void setActualEndDate(String actualEndDate) {
        this.actualEndDate = actualEndDate == null ? null : actualEndDate.trim();
    }

    /** 取值 <== 下次抛账日 : 下次抛账日 [10,0] */
    public String getNextThrowDate() {
        return nextThrowDate;
    }

    /** 赋值 ==> 下次抛账日 : 下次抛账日 [10,0] */
    public void setNextThrowDate(String nextThrowDate) {
        this.nextThrowDate = nextThrowDate == null ? null : nextThrowDate.trim();
    }

    /** 取值 <== 是否产生还款计划 : 是否产生还款计划 [1,0] */
    public String getIsMakePlan() {
        return isMakePlan;
    }

    /** 赋值 ==> 是否产生还款计划 : 是否产生还款计划 [1,0] */
    public void setIsMakePlan(String isMakePlan) {
        this.isMakePlan = isMakePlan == null ? null : isMakePlan.trim();
    }

    /** 取值 <== 生产未来还款计划期数 : 生产未来还款计划期数 [10,0] */
    public Integer getMakePlanPeriod() {
        return makePlanPeriod;
    }

    /** 赋值 ==> 生产未来还款计划期数 : 生产未来还款计划期数 [10,0] */
    public void setMakePlanPeriod(Integer makePlanPeriod) {
        this.makePlanPeriod = makePlanPeriod;
    }

    /** 取值 <== 首期抛账周期号 : 首期抛账周期号 [10,0] */
    public Integer getFirstCycleNo() {
        return firstCycleNo;
    }

    /** 赋值 ==> 首期抛账周期号 : 首期抛账周期号 [10,0] */
    public void setFirstCycleNo(Integer firstCycleNo) {
        this.firstCycleNo = firstCycleNo;
    }

    /** 取值 <== 起息日期 : 起息日期 [10,0] */
    public String getStartIntDate() {
        return startIntDate;
    }

    /** 赋值 ==> 起息日期 : 起息日期 [10,0] */
    public void setStartIntDate(String startIntDate) {
        this.startIntDate = startIntDate == null ? null : startIntDate.trim();
    }

    /** 取值 <== 手续费率 : 手续费率 [18,6] */
    public BigDecimal getFeeRate() {
        return feeRate;
    }

    /** 赋值 ==> 手续费率 : 手续费率 [18,6] */
    public void setFeeRate(BigDecimal feeRate) {
        this.feeRate = feeRate;
    }

    /** 取值 <== 已入账期数 : 已入账期数 [10,0] */
    public Integer getThrowedPeriod() {
        return throwedPeriod;
    }

    /** 赋值 ==> 已入账期数 : 已入账期数 [10,0] */
    public void setThrowedPeriod(Integer throwedPeriod) {
        this.throwedPeriod = throwedPeriod;
    }

    /** 取值 <== 剩余期数 : 剩余期数 [10,0] */
    public Integer getRemainPeriod() {
        return remainPeriod;
    }

    /** 赋值 ==> 剩余期数 : 剩余期数 [10,0] */
    public void setRemainPeriod(Integer remainPeriod) {
        this.remainPeriod = remainPeriod;
    }

    /** 取值 <== 剩余本金 : 剩余本金 [18,0] */
    public BigDecimal getRemainPrincipalAmount() {
        return remainPrincipalAmount;
    }

    /** 赋值 ==> 剩余本金 : 剩余本金 [18,0] */
    public void setRemainPrincipalAmount(BigDecimal remainPrincipalAmount) {
        this.remainPrincipalAmount = remainPrincipalAmount;
    }

    /** 取值 <== 手续费 : 手续费 [18,0] */
    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    /** 赋值 ==> 手续费 : 手续费 [18,0] */
    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    /** 取值 <== 剩余手续费 : 剩余手续费 [18,0] */
    public BigDecimal getRemainFeeAmount() {
        return remainFeeAmount;
    }

    /** 赋值 ==> 剩余手续费 : 剩余手续费 [18,0] */
    public void setRemainFeeAmount(BigDecimal remainFeeAmount) {
        this.remainFeeAmount = remainFeeAmount;
    }

    /** 取值 <== 下次利率生效日期 消费信贷使用。根据利率生效方式获取。如果是实时生效时，此项不赋值 [10,0] */
    public String getNextIntAdjustDate() {
        return nextIntAdjustDate;
    }

    /** 赋值 ==> 下次利率生效日期 消费信贷使用。根据利率生效方式获取。如果是实时生效时，此项不赋值 [10,0] */
    public void setNextIntAdjustDate(String nextIntAdjustDate) {
        this.nextIntAdjustDate = nextIntAdjustDate == null ? null : nextIntAdjustDate.trim();
    }

    /** 取值 <== Pending生效利率 利率变更当日，下次利率生效日期不是立即生效时，此项记录变更后利率。当利率生效后，此项清空，生效后的利率记录到贷款利率项 [18,6] */
    public BigDecimal getEffectIntRate() {
        return effectIntRate;
    }

    /** 赋值 ==> Pending生效利率 利率变更当日，下次利率生效日期不是立即生效时，此项记录变更后利率。当利率生效后，此项清空，生效后的利率记录到贷款利率项 [18,6] */
    public void setEffectIntRate(BigDecimal effectIntRate) {
        this.effectIntRate = effectIntRate;
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

    /** 取值 <== 版本号 : 版本号 [10,0], Not NULL */
    public Integer getVersion() {
        return version;
    }

    /** 赋值 ==> 版本号 : 版本号 [10,0], Not NULL */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /** 取值 <== 还款方式 [2,0] */
    public String getRepayMode() {
        return repayMode;
    }

    /** 赋值 ==> 还款方式 [2,0] */
    public void setRepayMode(String repayMode) {
        this.repayMode = repayMode == null ? null : repayMode.trim();
    }

    /** 取值 <== 还款日类型 [2,0] */
    public String getRepaymentDateType() {
        return repaymentDateType;
    }

    /** 赋值 ==> 还款日类型 [2,0] */
    public void setRepaymentDateType(String repaymentDateType) {
        this.repaymentDateType = repaymentDateType == null ? null : repaymentDateType.trim();
    }

    /** 取值 <== 免息天数 [10,0] */
    public Integer getFreeDays() {
        return freeDays;
    }

    /** 赋值 ==> 免息天数 [10,0] */
    public void setFreeDays(Integer freeDays) {
        this.freeDays = freeDays;
    }

    /** 取值 <== 还款周期 [10,0] */
    public Integer getRepayPrincipalCycle() {
        return repayPrincipalCycle;
    }

    /** 赋值 ==> 还款周期 [10,0] */
    public void setRepayPrincipalCycle(Integer repayPrincipalCycle) {
        this.repayPrincipalCycle = repayPrincipalCycle;
    }

    /** 取值 <== 还款日 [10,0] */
    public Integer getRepayDay() {
        return repayDay;
    }

    /** 赋值 ==> 还款日 [10,0] */
    public void setRepayDay(Integer repayDay) {
        this.repayDay = repayDay;
    }

    /** 取值 <== 罚息利率上浮比例 [10,4] */
    public BigDecimal getPenaltyUp() {
        return penaltyUp;
    }

    /** 赋值 ==> 罚息利率上浮比例 [10,4] */
    public void setPenaltyUp(BigDecimal penaltyUp) {
        this.penaltyUp = penaltyUp;
    }

    /** 取值 <== 罚息利率 [18,6] */
    public BigDecimal getPenaltyInterRate() {
        return penaltyInterRate;
    }

    /** 赋值 ==> 罚息利率 [18,6] */
    public void setPenaltyInterRate(BigDecimal penaltyInterRate) {
        this.penaltyInterRate = penaltyInterRate;
    }

    /** 取值 <== 总利息 [18,0] */
    public BigDecimal getInterAmount() {
        return interAmount;
    }

    /** 赋值 ==> 总利息 [18,0] */
    public void setInterAmount(BigDecimal interAmount) {
        this.interAmount = interAmount;
    }

    /** 取值 <== 剩余利息 [18,0] */
    public BigDecimal getRemainInterAmount() {
        return remainInterAmount;
    }

    /** 赋值 ==> 剩余利息 [18,0] */
    public void setRemainInterAmount(BigDecimal remainInterAmount) {
        this.remainInterAmount = remainInterAmount;
    }

    /** 取值 <== 收款账户银行号 [7,0] */
    public String getDirectDebitBankNo() {
        return directDebitBankNo;
    }

    /** 赋值 ==> 收款账户银行号 [7,0] */
    public void setDirectDebitBankNo(String directDebitBankNo) {
        this.directDebitBankNo = directDebitBankNo == null ? null : directDebitBankNo.trim();
    }

    /** 取值 <== 约定收款账户号 [25,0] */
    public String getDirectDebitAccountNo() {
        return directDebitAccountNo;
    }

    /** 赋值 ==> 约定收款账户号 [25,0] */
    public void setDirectDebitAccountNo(String directDebitAccountNo) {
        this.directDebitAccountNo = directDebitAccountNo == null ? null : directDebitAccountNo.trim();
    }

    /** 取值 <== 状态(0：撤销；1：正常；2：逾期；3；结算) [2,0] */
    public String getStatus() {
        return status;
    }

    /** 赋值 ==> 状态(0：撤销；1：正常；2：逾期；3；结算) [2,0] */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /** 取值 <== 利息罚息利率 [18,6] */
    public BigDecimal getIntPenaltyInterRate() {
        return intPenaltyInterRate;
    }

    /** 赋值 ==> 利息罚息利率 [18,6] */
    public void setIntPenaltyInterRate(BigDecimal intPenaltyInterRate) {
        this.intPenaltyInterRate = intPenaltyInterRate;
    }

    /** 取值 <== 分期原交易流水号 [36,0] */
    public String getOldGlobalSerialNumbr() {
        return oldGlobalSerialNumbr;
    }

    /** 赋值 ==> 分期原交易流水号 [36,0] */
    public void setOldGlobalSerialNumbr(String oldGlobalSerialNumbr) {
        this.oldGlobalSerialNumbr = oldGlobalSerialNumbr == null ? null : oldGlobalSerialNumbr.trim();
    }
	
	public BigDecimal getTotalBalance() {
		return totalBalance;
	}
	public void setTotalBalance(BigDecimal totalBalance) {
		this.totalBalance = totalBalance;
	}
	
}
