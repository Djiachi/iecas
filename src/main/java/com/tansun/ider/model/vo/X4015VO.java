package com.tansun.ider.model.vo;

import java.math.BigDecimal;

/**
 * <p> Title: X4015VO </p>
 * <p> Description: 查询个人公务卡最大限额接口出参</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年4月25日
 */
public class X4015VO {
    /** 预算单位代码 */
    private String budgetUnitCode;
    /** 个人公务卡最大授信额度 */
    private BigDecimal personMaxQuota;

    public String getBudgetUnitCode() {
        return budgetUnitCode;
    }

    public void setBudgetUnitCode(String budgetUnitCode) {
        this.budgetUnitCode = budgetUnitCode;
    }

    public BigDecimal getPersonMaxQuota() {
        return personMaxQuota;
    }

    public void setPersonMaxQuota(BigDecimal personMaxQuota) {
        this.personMaxQuota = personMaxQuota;
    }

}
