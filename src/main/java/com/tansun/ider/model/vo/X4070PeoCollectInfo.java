package com.tansun.ider.model.vo;

import java.math.BigDecimal;

/**
 * <p> Title: X4070PeoCollectInfo </p>
 * <p> Description: 个人公务卡账单汇总信息</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年5月28日
 */
public class X4070PeoCollectInfo {

    /** 账单金额 */
    private BigDecimal postingAmount;
    /** 本金金额 */
    private BigDecimal principalAmount;
    /** 利息金额 */
    private BigDecimal interestAamount;
    /** 费用金额 */
    private BigDecimal feeAmount;

    public BigDecimal getPostingAmount() {
        return postingAmount;
    }

    public void setPostingAmount(BigDecimal postingAmount) {
        this.postingAmount = postingAmount;
    }

    public BigDecimal getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(BigDecimal principalAmount) {
        this.principalAmount = principalAmount;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public BigDecimal getInterestAamount() {
        return interestAamount;
    }

    public void setInterestAamount(BigDecimal interestAamount) {
        this.interestAamount = interestAamount;
    }

}
