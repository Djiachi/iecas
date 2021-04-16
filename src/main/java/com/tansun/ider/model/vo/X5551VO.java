package com.tansun.ider.model.vo;

import java.math.BigDecimal;

/**
 *
 */
public class X5551VO {
    private String accountId;

    private String customerNo;
    // 账单本金金额
    private BigDecimal principalForBill;
    // 账单利息金额
    private BigDecimal interestForBill;
    // 账单费用金额
    private BigDecimal feeForBill;
    // 当期本金金额
    private BigDecimal principalForCurrent;
    // 当期利息金额
    private BigDecimal interestForCurrent;
    // 当期费用金额
    private BigDecimal feeForCurrent;
    // 总金额
    private BigDecimal totalBalance;

    public BigDecimal getPrincipalForBill() {
        return principalForBill;
    }

    public void setPrincipalForBill(BigDecimal principalForBill) {
        this.principalForBill = principalForBill;
    }

    public BigDecimal getInterestForBill() {
        return interestForBill;
    }

    public void setInterestForBill(BigDecimal interestForBill) {
        this.interestForBill = interestForBill;
    }

    public BigDecimal getFeeForBill() {
        return feeForBill;
    }

    public void setFeeForBill(BigDecimal feeForBill) {
        this.feeForBill = feeForBill;
    }

    public BigDecimal getPrincipalForCurrent() {
        return principalForCurrent;
    }

    public void setPrincipalForCurrent(BigDecimal principalForCurrent) {
        this.principalForCurrent = principalForCurrent;
    }

    public BigDecimal getInterestForCurrent() {
        return interestForCurrent;
    }

    public void setInterestForCurrent(BigDecimal interestForCurrent) {
        this.interestForCurrent = interestForCurrent;
    }

    public BigDecimal getFeeForCurrent() {
        return feeForCurrent;
    }

    public void setFeeForCurrent(BigDecimal feeForCurrent) {
        this.feeForCurrent = feeForCurrent;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }
}
