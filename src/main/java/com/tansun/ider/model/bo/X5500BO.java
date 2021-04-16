package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.List;

import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc:查询账户余额对象
 * @Author lianhuan
 * @Date 2018年4月28日 下午2:43:42
 */
public class X5500BO extends BeanVO implements Serializable {
    private static final long serialVersionUID = -4256912206267593510L;
    /** 活动与构件对应关系表 */
    CoreEventActivityRel coreEventActivityRel;
    /** 活动与构件对应关系表 */
    List<CoreActivityArtifactRel> activityArtifactList;
    /** 全局事件编号 */
    private String globalEventNo;
    private String accountId;
    private String currencyCode;
    private String operatorId;
    private String operationMode;

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

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    @Override
    public String toString() {
        return "X5500BO [coreEventActivityRel=" + coreEventActivityRel + ", activityArtifactList="
                + activityArtifactList + ", globalEventNo=" + globalEventNo + ", accountId=" + accountId
                + ", currencyCode=" + currencyCode + ", operatorId=" + operatorId + ", operationMode=" + operationMode
                + "]";
    }

}
