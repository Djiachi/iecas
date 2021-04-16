package com.tansun.ider.model.vo;

/**
 * <p> Title: X4070VO </p>
 * <p> Description: 预算单位已出账单查询出参</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年5月28日
 */
public class X4070VO {
    /** 个人公务卡账单汇总信息 */
    private X4070PeoCollectInfo peoCollectInfo;
    /** 单位公务卡账单汇总信息 */
    private X4070UnitCollectInfo unitCollectInfo;

    public X4070PeoCollectInfo getPeoCollectInfo() {
        return peoCollectInfo;
    }

    public void setPeoCollectInfo(X4070PeoCollectInfo peoCollectInfo) {
        this.peoCollectInfo = peoCollectInfo;
    }

    public X4070UnitCollectInfo getUnitCollectInfo() {
        return unitCollectInfo;
    }

    public void setUnitCollectInfo(X4070UnitCollectInfo unitCollectInfo) {
        this.unitCollectInfo = unitCollectInfo;
    }

}
