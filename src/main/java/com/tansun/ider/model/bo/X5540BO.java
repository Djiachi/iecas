package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.commun.BeanVO;

/**
 * 费用免除信息查询
 * 
 * @author lianhuan 2018年9月14日
 */
public class X5540BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;
	/** 活动与构件对应关系表 */
	CoreEventActivityRel coreEventActivityRel;
	/** 活动与构件对应关系表 */
	List<CoreActivityArtifactRel> activityArtifactList;
	/** 全局事件编号 */
	private String globalEventNo;
	 /** 产品对象代码 [9,0] Not NULL */
    protected String productObjectCode;
    /** 客户号 [12,0] */
    protected String customerNo;
    /** 收费项目编号 [8,0] Not NULL */
    protected String feeItemNo;
    /** 货币代码 [3,0] */
    protected String currencyCode;
    /** 免除周期 [6,0] */
    protected String waiveCycleNo;
    /** 已执行次数 [10,0] */
    protected Integer executedNum;
    /** 已免除次数 [10,0] */
    protected Integer waivedNum;
    /** 首次免除日期 [10,0] */
    protected String firstWaiveDate;
    /** 上次免除日期 [10,0] */
    protected String lastWaiveDate;
    /** 上次免除金额 [18,0] */
    protected BigDecimal lastWaiveAmt;
    /** 累计免除金额 [18,0] */
    protected BigDecimal accumultWaiveAmt;
    /** 累计收取金额 [18,0] */
    protected BigDecimal accumulatedCollectionAmount;
    /** 周期类费用标识 [1,0] */
    protected String periodicFeeIdentifier;
    /**  [19,0] Not NULL */
    protected Date timestamp;
    /** 创建时间 yyyy-MM-dd HH:mm:ss.SSS [23,0] */
    protected String gmtCreate;
    /** 实例代码1 [14,0] */
    protected String instanCode1;
    /** 实例代码2 [14,0] */
    protected String instanCode2;
    /** 实例代码3 [14,0] */
    protected String instanCode3;
    /** 实例代码4 [14,0] */
    protected String instanCode4;
    /** 实例代码5 [14,0] */
    protected String instanCode5;
    /** 证件号码 [30,0] */
    private String idNumber;
    /** 证件类型 */
    private String idType;
    /** 费用描述*/
    private String feeDesc;
    private String instanDesc1;
    private String instanDesc2;
    private String instanDesc3;
    private String instanDesc4;
    private String instanDesc5;
    private String currencyDesc;
    
    /** 收取频率 [1,0] */
    protected String chargingFrequency;
    /** 下一执行日期 [10,0] */
    protected String nextExecutionDate;
    /** 失效日期 [10,0] */
    protected String expirationDate;
    public String getChargingFrequency() {
        return chargingFrequency;
    }

    public void setChargingFrequency(String chargingFrequency) {
        this.chargingFrequency = chargingFrequency;
    }

    public String getNextExecutionDate() {
        return nextExecutionDate;
    }

    public void setNextExecutionDate(String nextExecutionDate) {
        this.nextExecutionDate = nextExecutionDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getDateOfInitialCollection() {
        return dateOfInitialCollection;
    }

    public void setDateOfInitialCollection(String dateOfInitialCollection) {
        this.dateOfInitialCollection = dateOfInitialCollection;
    }

    public String getDateOfLastCollection() {
        return dateOfLastCollection;
    }

    public void setDateOfLastCollection(String dateOfLastCollection) {
        this.dateOfLastCollection = dateOfLastCollection;
    }

    /** 首次收取日期 [10,0] */
    protected String dateOfInitialCollection;
    /** 上次收取日期 [10,0] */
    protected String dateOfLastCollection;
    
    public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	/** 外部识别号 [19,0] */
    private String externalIdentificationNo;
    private String operatorId;

    public String getProductObjectCode() {
        return productObjectCode;
    }

    public void setProductObjectCode(String productObjectCode) {
        this.productObjectCode = productObjectCode;
    }

    public String getPeriodicFeeIdentifier() {
        return periodicFeeIdentifier;
    }

    public void setPeriodicFeeIdentifier(String periodicFeeIdentifier) {
        this.periodicFeeIdentifier = periodicFeeIdentifier;
    }

    public BigDecimal getAccumulatedCollectionAmount() {
        return accumulatedCollectionAmount;
    }

    public void setAccumulatedCollectionAmount(BigDecimal accumulatedCollectionAmount) {
        this.accumulatedCollectionAmount = accumulatedCollectionAmount;
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

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getFeeItemNo() {
		return feeItemNo;
	}

	public void setFeeItemNo(String feeItemNo) {
		this.feeItemNo = feeItemNo;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getWaiveCycleNo() {
		return waiveCycleNo;
	}

	public void setWaiveCycleNo(String waiveCycleNo) {
		this.waiveCycleNo = waiveCycleNo;
	}

	public Integer getExecutedNum() {
		return executedNum;
	}

	public void setExecutedNum(Integer executedNum) {
		this.executedNum = executedNum;
	}

	public Integer getWaivedNum() {
		return waivedNum;
	}

	public void setWaivedNum(Integer waivedNum) {
		this.waivedNum = waivedNum;
	}

	public String getFirstWaiveDate() {
		return firstWaiveDate;
	}

	public void setFirstWaiveDate(String firstWaiveDate) {
		this.firstWaiveDate = firstWaiveDate;
	}

	public String getLastWaiveDate() {
		return lastWaiveDate;
	}

	public void setLastWaiveDate(String lastWaiveDate) {
		this.lastWaiveDate = lastWaiveDate;
	}

	public BigDecimal getLastWaiveAmt() {
		return lastWaiveAmt;
	}

	public void setLastWaiveAmt(BigDecimal lastWaiveAmt) {
		this.lastWaiveAmt = lastWaiveAmt;
	}

	public BigDecimal getAccumultWaiveAmt() {
		return accumultWaiveAmt;
	}

	public void setAccumultWaiveAmt(BigDecimal accumultWaiveAmt) {
		this.accumultWaiveAmt = accumultWaiveAmt;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public String getInstanCode1() {
		return instanCode1;
	}

	public void setInstanCode1(String instanCode1) {
		this.instanCode1 = instanCode1;
	}

	public String getInstanCode2() {
		return instanCode2;
	}

	public void setInstanCode2(String instanCode2) {
		this.instanCode2 = instanCode2;
	}

	public String getInstanCode3() {
		return instanCode3;
	}

	public void setInstanCode3(String instanCode3) {
		this.instanCode3 = instanCode3;
	}

	public String getInstanCode4() {
		return instanCode4;
	}

	public void setInstanCode4(String instanCode4) {
		this.instanCode4 = instanCode4;
	}

	public String getInstanCode5() {
		return instanCode5;
	}

	public void setInstanCode5(String instanCode5) {
		this.instanCode5 = instanCode5;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFeeDesc() {
		return feeDesc;
	}

	public void setFeeDesc(String feeDesc) {
		this.feeDesc = feeDesc;
	}

	public String getInstanDesc1() {
		return instanDesc1;
	}

	public void setInstanDesc1(String instanDesc1) {
		this.instanDesc1 = instanDesc1;
	}

	public String getInstanDesc2() {
		return instanDesc2;
	}

	public void setInstanDesc2(String instanDesc2) {
		this.instanDesc2 = instanDesc2;
	}

	public String getInstanDesc3() {
		return instanDesc3;
	}

	public void setInstanDesc3(String instanDesc3) {
		this.instanDesc3 = instanDesc3;
	}

	public String getInstanDesc4() {
		return instanDesc4;
	}

	public void setInstanDesc4(String instanDesc4) {
		this.instanDesc4 = instanDesc4;
	}

	public String getInstanDesc5() {
		return instanDesc5;
	}

	public void setInstanDesc5(String instanDesc5) {
		this.instanDesc5 = instanDesc5;
	}

	public String getCurrencyDesc() {
		return currencyDesc;
	}

	public void setCurrencyDesc(String currencyDesc) {
		this.currencyDesc = currencyDesc;
	}

}
