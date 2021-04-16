package com.tansun.ider.model.bo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.tansun.ider.framwork.commun.BeanVO;

public class X5708BO extends BeanVO implements Serializable {

	private static final long serialVersionUID = 3759919703810893118L;
	/** 会计分录模板表id [64,0] Not NULL */
	@NotNull
    protected String acctTemplateId;
    /** 序号 [2,0] Not NULL */
	@NotNull
    protected String seqNbr;
    /** 借贷标识 借记：D 贷记：C [1,0] */
    protected String debitCreditFlag;
    /** 会计科目编码 [64,0] */
    protected String itemId;
    /** 会计科目名称 [128,0] */
    protected String itemNme;
    /** 业务类型 [64,0] */
    protected String businessTyp;
    /** 账号 [32,0] Not NULL */
    protected String accountId;
    /** 币种 [3,0] Not NULL */
    protected String currencyCde;
    /** 摘要配置方式(0-手工配置，1-动态配置) [1,0] Not NULL */
    protected String summaryConfigTyp;
    /** 资金方向 + 或 - [1,0] Not NULL */
    protected String fundDirectFlag;
    /** 金额类型   1，本金金额  2， 利息金额  3，交易金额  4，税后利息 5，税金金额 [1,0] Not NULL */
    protected String amountType;
    /** 摘要内容  [128,0] */
    protected String summaryContent;
    /** 余额方向  [8,0] */
    protected String balanceDirection;
	public String getAcctTemplateId() {
		return acctTemplateId;
	}
	public void setAcctTemplateId(String acctTemplateId) {
		this.acctTemplateId = acctTemplateId;
	}
	public String getSeqNbr() {
		return seqNbr;
	}
	public void setSeqNbr(String seqNbr) {
		this.seqNbr = seqNbr;
	}
	public String getDebitCreditFlag() {
		return debitCreditFlag;
	}
	public void setDebitCreditFlag(String debitCreditFlag) {
		this.debitCreditFlag = debitCreditFlag;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemNme() {
		return itemNme;
	}
	public void setItemNme(String itemNme) {
		this.itemNme = itemNme;
	}
	public String getBusinessTyp() {
		return businessTyp;
	}
	public void setBusinessTyp(String businessTyp) {
		this.businessTyp = businessTyp;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getCurrencyCde() {
		return currencyCde;
	}
	public void setCurrencyCde(String currencyCde) {
		this.currencyCde = currencyCde;
	}
	public String getSummaryConfigTyp() {
		return summaryConfigTyp;
	}
	@Override
	public String toString() {
		return "X5708BO [acctTemplateId=" + acctTemplateId + ", seqNbr=" + seqNbr + ", debitCreditFlag="
				+ debitCreditFlag + ", itemId=" + itemId + ", itemNme=" + itemNme + ", businessTyp=" + businessTyp
				+ ", accountId=" + accountId + ", currencyCde=" + currencyCde + ", summaryConfigTyp=" + summaryConfigTyp
				+ ", fundDirectFlag=" + fundDirectFlag + ", amountType=" + amountType + ", summaryContent="
				+ summaryContent + ", balanceDirection=" + balanceDirection + "]";
	}
	public void setSummaryConfigTyp(String summaryConfigTyp) {
		this.summaryConfigTyp = summaryConfigTyp;
	}
	public String getFundDirectFlag() {
		return fundDirectFlag;
	}
	public void setFundDirectFlag(String fundDirectFlag) {
		this.fundDirectFlag = fundDirectFlag;
	}
	public String getAmountType() {
		return amountType;
	}
	public void setAmountType(String amountType) {
		this.amountType = amountType;
	}
	public String getSummaryContent() {
		return summaryContent;
	}
	public void setSummaryContent(String summaryContent) {
		this.summaryContent = summaryContent;
	}
	public String getBalanceDirection() {
		return balanceDirection;
	}
	public void setBalanceDirection(String balanceDirection) {
		this.balanceDirection = balanceDirection;
	}
}
