package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: X5520客户延滞状况
 * @Author lianhuan
 * @Date 2018年4月25日下午3:04:03
 */
public class X5520BO extends BeanVO implements Serializable {
    private static final long serialVersionUID = 5035068883811420241L;
	/** 活动与构件对应关系表 */
	CoreEventActivityRel coreEventActivityRel;
	/** 活动与构件对应关系表 */
	List<CoreActivityArtifactRel> activityArtifactList;
	/** 全局事件编号 */
	private String globalEventNo;
    /** 账户代码 [18,0] */
    private String accountId;
    /** 币种 [3,0] */
    private String currencyCode;
    /** 当前周期号 [10,0] */
    private Integer currentCycleNumber;
    
    private String operatorId;
    /** 往期贷方调整金额 [18,0] */
    protected BigDecimal billCreditAdjustAmount;
    /** 期初宽限余额 [18,0] */
    protected BigDecimal beginGraceBalance;
    /** 当前宽限余额 [18,0] */
    protected BigDecimal graceBalance;
    /** 宽限日前还款金额 [18,0] */
    protected BigDecimal paymentWithinGrace;
    /** 宽限日后还款金额 [18,0] */
    protected BigDecimal paymentAfterGrace;
    /** 贷方调整金额（包括退货） [18,0] */
    protected BigDecimal creditAdjustAmount;
    /** 还款还原宽限日前还款 [18,0] */
    protected BigDecimal paymentRevWithinGrace;
    /** 还款还原宽限日后还款 [18,0] */
    protected BigDecimal paymentRevAfterGrace;
    /** 全额容差金额 [18,0] */
    protected BigDecimal fullToleranceAmount;
    /** 本周期全额还款标志 Y：已满足 N：不满足 [1,0] */
    protected String fullPaymentFlag;
    /** 满足全额还款日期 [10,0] */
    protected String fullPaymentDate;
    /** 本周期宽限日 [10,0] */
    protected String graceDate;
    /** 本周期最后还款日 [10,0] */
    protected String paymentDueDate;
    /** 账单金额 [18,0] */
    protected BigDecimal postingAmount;
    /** 借方金额 [18,0] */
    protected BigDecimal debitAmount;
    /** 借方笔数 [10,0] */
    protected Integer debitNum;
    /** 贷方笔数 [10,0] */
    protected Integer creditNum;
    /** 币种描述[50,0] */
    private String currencyDesc;
    /** 满足动态结息日期 yyyy-MM-dd [10,0] */
    protected String dynamicInterestSettlementDate;
    /** 已还款比例 [8,7] */
    protected BigDecimal repaymRatio;
    
    public String getDynamicInterestSettlementDate() {
        return dynamicInterestSettlementDate;
    }

    public void setDynamicInterestSettlementDate(String dynamicInterestSettlementDate) {
        this.dynamicInterestSettlementDate = dynamicInterestSettlementDate;
    }

    public BigDecimal getRepaymRatio() {
        return repaymRatio;
    }

    public void setRepaymRatio(BigDecimal repaymRatio) {
        this.repaymRatio = repaymRatio;
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

    public Integer getCurrentCycleNumber() {
        return currentCycleNumber;
    }

    public void setCurrentCycleNumber(Integer currentCycleNumber) {
        this.currentCycleNumber = currentCycleNumber;
    }

    @Override
    public String toString() {
        return "X5520BO [accountId=" + accountId + ", currencyCode=" + currencyCode + ", currentCycleNumber="
                + currentCycleNumber + "]";
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

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public BigDecimal getBillCreditAdjustAmount() {
		return billCreditAdjustAmount;
	}

	public void setBillCreditAdjustAmount(BigDecimal billCreditAdjustAmount) {
		this.billCreditAdjustAmount = billCreditAdjustAmount;
	}

	public BigDecimal getBeginGraceBalance() {
		return beginGraceBalance;
	}

	public void setBeginGraceBalance(BigDecimal beginGraceBalance) {
		this.beginGraceBalance = beginGraceBalance;
	}

	public BigDecimal getGraceBalance() {
		return graceBalance;
	}

	public void setGraceBalance(BigDecimal graceBalance) {
		this.graceBalance = graceBalance;
	}

	public BigDecimal getPaymentWithinGrace() {
		return paymentWithinGrace;
	}

	public void setPaymentWithinGrace(BigDecimal paymentWithinGrace) {
		this.paymentWithinGrace = paymentWithinGrace;
	}

	public BigDecimal getPaymentAfterGrace() {
		return paymentAfterGrace;
	}

	public void setPaymentAfterGrace(BigDecimal paymentAfterGrace) {
		this.paymentAfterGrace = paymentAfterGrace;
	}

	public BigDecimal getCreditAdjustAmount() {
		return creditAdjustAmount;
	}

	public void setCreditAdjustAmount(BigDecimal creditAdjustAmount) {
		this.creditAdjustAmount = creditAdjustAmount;
	}

	public BigDecimal getPaymentRevWithinGrace() {
		return paymentRevWithinGrace;
	}

	public void setPaymentRevWithinGrace(BigDecimal paymentRevWithinGrace) {
		this.paymentRevWithinGrace = paymentRevWithinGrace;
	}

	public BigDecimal getPaymentRevAfterGrace() {
		return paymentRevAfterGrace;
	}

	public void setPaymentRevAfterGrace(BigDecimal paymentRevAfterGrace) {
		this.paymentRevAfterGrace = paymentRevAfterGrace;
	}

	public BigDecimal getFullToleranceAmount() {
		return fullToleranceAmount;
	}

	public void setFullToleranceAmount(BigDecimal fullToleranceAmount) {
		this.fullToleranceAmount = fullToleranceAmount;
	}

	public String getFullPaymentFlag() {
		return fullPaymentFlag;
	}

	public void setFullPaymentFlag(String fullPaymentFlag) {
		this.fullPaymentFlag = fullPaymentFlag;
	}

	public String getFullPaymentDate() {
		return fullPaymentDate;
	}

	public void setFullPaymentDate(String fullPaymentDate) {
		this.fullPaymentDate = fullPaymentDate;
	}

	public String getGraceDate() {
		return graceDate;
	}

	public void setGraceDate(String graceDate) {
		this.graceDate = graceDate;
	}

	public String getPaymentDueDate() {
		return paymentDueDate;
	}

	public void setPaymentDueDate(String paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}

	public BigDecimal getPostingAmount() {
		return postingAmount;
	}

	public void setPostingAmount(BigDecimal postingAmount) {
		this.postingAmount = postingAmount;
	}

	public BigDecimal getDebitAmount() {
		return debitAmount;
	}

	public void setDebitAmount(BigDecimal debitAmount) {
		this.debitAmount = debitAmount;
	}

	public Integer getDebitNum() {
		return debitNum;
	}

	public void setDebitNum(Integer debitNum) {
		this.debitNum = debitNum;
	}

	public Integer getCreditNum() {
		return creditNum;
	}

	public void setCreditNum(Integer creditNum) {
		this.creditNum = creditNum;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCurrencyDesc() {
		return currencyDesc;
	}

	public void setCurrencyDesc(String currencyDesc) {
		this.currencyDesc = currencyDesc;
	}

}
