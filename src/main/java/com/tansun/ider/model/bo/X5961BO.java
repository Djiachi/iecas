package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.tansun.ider.framwork.commun.BeanVO;

public class X5961BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = -4235542980639676730L;
    private String id;
    /** 客户号 [12,0] */
    private String customerNo;
    /** 收费项目编号 [8,0] Not NULL */
    private String feeItemNo;
    /** 货币代码 [3,0] */
    private String currencyCode;
    /** 免除周期 [6,0] */
    private String waiveCycleNo;
    /** 已执行次数 [10,0] */
    private Integer executedNum;
    /** 已免除次数 [10,0] */
    private Integer waivedNum;
    /** 首次免除日期 [10,0] */
    private String firstWaiveDate;
    /** 上次免除日期 [10,0] */
    private String lastWaiveDate;
    /** 上次免除金额 [18,0] */
    private BigDecimal lastWaiveAmt;
    /** 累计免除金额 [18,0] */
    private BigDecimal accumultWaiveAmt;
    /** 实例代码1 [14,0] */
    private String instanCode1;
    /** 实例代码2 [14,0] */
    private String instanCode2;
    /** 实例代码3 [14,0] */
    private String instanCode3;
    /** 实例代码4 [14,0] */
    private String instanCode4;
    /** 实例代码5 [14,0] */
    private String instanCode5;
    /** 周期类费用标识 [1,0] */
    private String periodicFeeIdentifier;
    /** 收取频率 [1,0] */
    private String chargingFrequency;
    /** 下一执行日期 [10,0] */
    private String nextExecutionDate;
    /** 失效日期 [10,0] */
    private String expirationDate;
    /** 累计收取金额 [18,0] */
    private BigDecimal accumulatedCollectionAmount;
    /** 首次收取日期 [10,0] */
    private String dateOfInitialCollection;
    /** 上次收取日期 [10,0] */
    private String dateOfLastCollection;
    /** 创建时间 yyyy-MM-dd HH:mm:ss.SSS [23,0] */
    private String gmtCreate;
    /**  [19,0] Not NULL */
    private Date timestamp;
    /** 版本号 [10,0] Not NULL */
    private Integer version;
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
    public String getPeriodicFeeIdentifier() {
        return periodicFeeIdentifier;
    }
    public void setPeriodicFeeIdentifier(String periodicFeeIdentifier) {
        this.periodicFeeIdentifier = periodicFeeIdentifier;
    }
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
    public BigDecimal getAccumulatedCollectionAmount() {
        return accumulatedCollectionAmount;
    }
    public void setAccumulatedCollectionAmount(BigDecimal accumulatedCollectionAmount) {
        this.accumulatedCollectionAmount = accumulatedCollectionAmount;
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
    public String getGmtCreate() {
        return gmtCreate;
    }
    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
    public Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    public Integer getVersion() {
        return version;
    }
    public void setVersion(Integer version) {
        this.version = version;
    }
    
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "X5961BO [id=" + id + ", customerNo=" + customerNo + ", feeItemNo=" + feeItemNo + ", currencyCode="
				+ currencyCode + ", waiveCycleNo=" + waiveCycleNo + ", executedNum=" + executedNum + ", waivedNum="
				+ waivedNum + ", firstWaiveDate=" + firstWaiveDate + ", lastWaiveDate=" + lastWaiveDate
				+ ", lastWaiveAmt=" + lastWaiveAmt + ", accumultWaiveAmt=" + accumultWaiveAmt + ", instanCode1="
				+ instanCode1 + ", instanCode2=" + instanCode2 + ", instanCode3=" + instanCode3 + ", instanCode4="
				+ instanCode4 + ", instanCode5=" + instanCode5 + ", periodicFeeIdentifier=" + periodicFeeIdentifier
				+ ", chargingFrequency=" + chargingFrequency + ", nextExecutionDate=" + nextExecutionDate
				+ ", expirationDate=" + expirationDate + ", accumulatedCollectionAmount=" + accumulatedCollectionAmount
				+ ", dateOfInitialCollection=" + dateOfInitialCollection + ", dateOfLastCollection="
				+ dateOfLastCollection + ", gmtCreate=" + gmtCreate + ", timestamp=" + timestamp + ", version="
				+ version + "]";
	}



}
