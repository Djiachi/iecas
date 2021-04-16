package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.dao.issue.entity.CoreCustomerAddr;
import com.tansun.ider.framwork.commun.BeanVO;

/**
 * <p> Title: X4001BO </p>
 * <p> Description: 预算单位附件信息</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年4月23日
 */
public class X4001BO extends BeanVO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /** 活动与构件对应关系表 */
    private CoreEventActivityRel coreEventActivityRel;
    /** 预算管理层级 */
    private String manageLevelCode;
    /** 单位公务卡总授信额度 */
    private BigDecimal orgAllQuota;
    /** 个人公务卡最大授信额度 */
    private BigDecimal personMaxQuota;
    /** 全局事件编号 */
    private String globalEventNo;
    /** 事件编号 [14,0] Not NULL */
    protected String eventNo;
    /** 活动编号 [8,0] Not NULL */
    protected String activityNo;
    /** 当前日志标志 A/B [1,0] */
    protected String currLogFlag;
    /** 客户号 */
    private String customerNo;
    /** 地址信息 */
    private List<CoreCustomerAddr> coreCoreCustomerAddrs;

    public CoreEventActivityRel getCoreEventActivityRel() {
        return coreEventActivityRel;
    }

    public void setCoreEventActivityRel(CoreEventActivityRel coreEventActivityRel) {
        this.coreEventActivityRel = coreEventActivityRel;
    }

    public List<CoreCustomerAddr> getCoreCoreCustomerAddrs() {
        return coreCoreCustomerAddrs;
    }

    public void setCoreCoreCustomerAddrs(List<CoreCustomerAddr> coreCoreCustomerAddrs) {
        this.coreCoreCustomerAddrs = coreCoreCustomerAddrs;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getCurrLogFlag() {
        return currLogFlag;
    }

    public void setCurrLogFlag(String currLogFlag) {
        this.currLogFlag = currLogFlag;
    }

    public String getEventNo() {
        return eventNo;
    }

    public void setEventNo(String eventNo) {
        this.eventNo = eventNo;
    }

    public String getActivityNo() {
        return activityNo;
    }

    public void setActivityNo(String activityNo) {
        this.activityNo = activityNo;
    }

    public String getGlobalEventNo() {
        return globalEventNo;
    }

    public void setGlobalEventNo(String globalEventNo) {
        this.globalEventNo = globalEventNo;
    }

    public String getManageLevelCode() {
        return manageLevelCode;
    }

    public void setManageLevelCode(String manageLevelCode) {
        this.manageLevelCode = manageLevelCode;
    }

    public BigDecimal getOrgAllQuota() {
        return orgAllQuota;
    }

    public void setOrgAllQuota(BigDecimal orgAllQuota) {
        this.orgAllQuota = orgAllQuota;
    }

    public BigDecimal getPersonMaxQuota() {
        return personMaxQuota;
    }

    public void setPersonMaxQuota(BigDecimal personMaxQuota) {
        this.personMaxQuota = personMaxQuota;
    }
}
