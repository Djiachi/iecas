package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.List;

import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.commun.BeanVO;

/**
 * 交易历史查询
 * 
 * @author lianhuan 2018年10月15日
 */
public class X5436BO extends BeanVO implements Serializable {
    private static final long serialVersionUID = -3110392235339871560L;
	/** 活动与构件对应关系表 */
	CoreEventActivityRel coreEventActivityRel;
	/** 活动与构件对应关系表 */
	List<CoreActivityArtifactRel> activityArtifactList;
	/** 全局事件编号 */
	private String globalEventNo;
    // 查询类型：同源交易查询1，入账情况查询2，客户还款历史查询3
    private String queryType;
    /** 证件号码 [30,0] */
    private String idNumber;
    /** 证件类型*/
    private String idType;
    public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	/** 证件号码 [19,0] */
    private String externalIdentificationNo;
    /** 客户号 [12,0] */
    private String customerNo;
    /** 事件编号 [14,0] */
    private String eventNo;
    /** 活动编号 [8,0] */
    private String activityNo;
    /** 账户代码 [18,0] */
    private String accountId;
    /** 币种 [3,0] */
    private String currencyCode;
    /** 全局流水号 [36,0] */
    private String globalSerialNumbr;
    /** 原交易全局流水号 [36,0] */
    private String globalSerialNumbrRelative;
    /** 日志层级 [1,0] */
    private String logLevel;
    /** 发生起始日期 yyyy-MM-dd [10,0] */
    private String startDate;
    /** 发生结束日期 yyyy-MM-dd [10,0] */
    private String endDate;
    /** 交易属性/产生来源 O-原生交易 D-衍生交易 [1,0] */
    private String transProperty;
    /** 余额类型 [1,0] */
    private String balanceType;
    
    private String operatorId;
    /**更改周期号标识*/
    private boolean changeCycleNumberMark;
    private List<String> eventNoList;
    
    public List<String> getEventNoList() {
        return eventNoList;
    }

    public void setEventNoList(List<String> eventNoList) {
        this.eventNoList = eventNoList;
    }

    public boolean isChangeCycleNumberMark() {
        return changeCycleNumberMark;
    }

    public void setChangeCycleNumberMark(boolean changeCycleNumberMark) {
        this.changeCycleNumberMark = changeCycleNumberMark;
    }
    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getExternalIdentificationNo() {
        return externalIdentificationNo;
    }

    public void setExternalIdentificationNo(String externalIdentificationNo) {
        this.externalIdentificationNo = externalIdentificationNo;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
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

    public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

    public String getGlobalSerialNumbr() {
        return globalSerialNumbr;
    }

    public void setGlobalSerialNumbr(String globalSerialNumbr) {
        this.globalSerialNumbr = globalSerialNumbr;
    }

    public String getGlobalSerialNumbrRelative() {
        return globalSerialNumbrRelative;
    }

    public void setGlobalSerialNumbrRelative(String globalSerialNumbrRelative) {
        this.globalSerialNumbrRelative = globalSerialNumbrRelative;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getTransProperty() {
        return transProperty;
    }

    public void setTransProperty(String transProperty) {
        this.transProperty = transProperty;
    }

    public String getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }

    @Override
    public String toString() {
        return "X5436BO [queryType=" + queryType + ", idNumber=" + idNumber + ", externalIdentificationNo="
                + externalIdentificationNo + ", customerNo=" + customerNo + ", eventNo=" + eventNo + ", activityNo="
                + activityNo + ", accountId=" + accountId + ", currencyCode=" + currencyCode + ", globalSerialNumbr="
                + globalSerialNumbr + ", globalSerialNumbrRelative=" + globalSerialNumbrRelative + ", logLevel="
                + logLevel + ", startDate=" + startDate + ", endDate=" + endDate + ", transProperty="
                + transProperty + ", balanceType=" + balanceType + "]";
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

}
