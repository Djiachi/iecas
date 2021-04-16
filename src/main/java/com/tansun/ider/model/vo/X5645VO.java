package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.tansun.ider.framwork.commun.BeanVO;

public class X5645VO  extends BeanVO implements Serializable{

	private static final long serialVersionUID = -2563282886664492663L;
	/** 分期交易账户号 : 贷款申请时生成的交易账户号 [23,0] Not NULL */
    protected String accountId;
    /** 支付日期 : 支付日期 [10,0] */
    protected String paymentDate;
    /** 支付金额 : 支付金额 [18,0] */
    protected BigDecimal paymentAmount;
    protected BigDecimal payableAmount;
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public BigDecimal getPayableAmount() {
		return payableAmount;
	}
	public void setPayableAmount(BigDecimal payableAmount) {
		this.payableAmount = payableAmount;
	}
	
}
