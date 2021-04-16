package com.tansun.ider.model.vo;

import com.tansun.ider.dao.issue.entity.CoreAccount;

import java.math.BigDecimal;
import java.util.Date;

public class X5515VO {

	private String productLevelCodeDesc;
    /** id [64,0] Not NULL */
    private String id;
    /** 产品描述 [50,0] */
    private String productDesc;
    /** 客户号 [12,0] Not NULL */
    private String customerNo;
    /** 延滞层级 G-产品线级 P-产品对象级 A-账户级 [1,0] */
    private String delinquencyLevel;
    /** 层级代码  账户代码/产品对象代码/产品线代码 [23,0] */
    private String levelCode;
    /** 产品对象代码 [9,0] */
    private String productObjectNo;
    /** 币种 [3,0] Not NULL */
    private String currencyCode;
    /** 周期号 [10,0] */
    private Integer cycleNo;
    /** 当前核算状态 [4,0] */
    private String accountingStatusCode;
    /** 本周期当前最低还款 [18,0] */
    private BigDecimal currCyclePaymentMin;
    /** 逾期期数 [10,0] */
    private Integer cycleDueNum;
    /** 逾期天数 [10,0] */
    private Integer cycleDueDayNum;
    /** 创建时间 yyyy-MM-dd HH:mm:ss [23,0] */
    private String gmtCreate;
    /** 时间戳 : oralce使用触发器更新， mysql使用自动更新 [19,0] Not NULL */
    private Date timestamp;
    /** 版本号 [10,0] */
    private Integer version;
    /** 出入催标志 [1,0] */
    private String collectionFlag;
    /** 出入催日期 [10,0] */
    private String collectionDate;
    /** 催收员ID [32,0] */
    private String collectorId;
    /** 本期预期还款日期 [10,0] */
    private String currCyclePaymentDate;
    /** 本期期初最低应缴 [18,0] */
    private BigDecimal currCycleBeginPaymentMin;
    /** 还清日期 [10,0] */
    private String payOffDate;
    /** 最近一次评估日期 [10,0] */
    private String latestEstimateDate;
    /** 对应生效码 [3,0] */
    private String blockCode;
    /** 生效码设置日期 [10,0] */
    private String blockCodeSetDate;
    /** 核算状态设置日期 [10,0] */
    private String accountingStatusSetDate;
    /** 状态 [1,0] */
    private String status;
    
    private String BusinessDesc;
    
    private String programDesc;
    
    private String currencyDesc;
    private CoreAccount coreAccount;
    private Boolean haveChild;
    private String flag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getDelinquencyLevel() {
        return delinquencyLevel;
    }

    public void setDelinquencyLevel(String delinquencyLevel) {
        this.delinquencyLevel = delinquencyLevel;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public String getProductObjectNo() {
        return productObjectNo;
    }

    public void setProductObjectNo(String productObjectNo) {
        this.productObjectNo = productObjectNo;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Integer getCycleNo() {
        return cycleNo;
    }

    public void setCycleNo(Integer cycleNo) {
        this.cycleNo = cycleNo;
    }

    public String getAccountingStatusCode() {
        return accountingStatusCode;
    }

    public void setAccountingStatusCode(String accountingStatusCode) {
        this.accountingStatusCode = accountingStatusCode;
    }

    public BigDecimal getCurrCyclePaymentMin() {
        return currCyclePaymentMin;
    }

    public void setCurrCyclePaymentMin(BigDecimal currCyclePaymentMin) {
        this.currCyclePaymentMin = currCyclePaymentMin;
    }

    public Integer getCycleDueNum() {
        return cycleDueNum;
    }

    public void setCycleDueNum(Integer cycleDueNum) {
        this.cycleDueNum = cycleDueNum;
    }

    public Integer getCycleDueDayNum() {
        return cycleDueDayNum;
    }

    public void setCycleDueDayNum(Integer cycleDueDayNum) {
        this.cycleDueDayNum = cycleDueDayNum;
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

    public String getCollectionFlag() {
        return collectionFlag;
    }

    public void setCollectionFlag(String collectionFlag) {
        this.collectionFlag = collectionFlag;
    }

    public String getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(String collectionDate) {
        this.collectionDate = collectionDate;
    }

    public String getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(String collectorId) {
        this.collectorId = collectorId;
    }

    public String getCurrCyclePaymentDate() {
        return currCyclePaymentDate;
    }

    public void setCurrCyclePaymentDate(String currCyclePaymentDate) {
        this.currCyclePaymentDate = currCyclePaymentDate;
    }

    public BigDecimal getCurrCycleBeginPaymentMin() {
        return currCycleBeginPaymentMin;
    }

    public void setCurrCycleBeginPaymentMin(BigDecimal currCycleBeginPaymentMin) {
        this.currCycleBeginPaymentMin = currCycleBeginPaymentMin;
    }

    public String getPayOffDate() {
        return payOffDate;
    }

    public void setPayOffDate(String payOffDate) {
        this.payOffDate = payOffDate;
    }

    public String getLatestEstimateDate() {
        return latestEstimateDate;
    }

    public void setLatestEstimateDate(String latestEstimateDate) {
        this.latestEstimateDate = latestEstimateDate;
    }

    public String getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }

    public String getBlockCodeSetDate() {
        return blockCodeSetDate;
    }

    public void setBlockCodeSetDate(String blockCodeSetDate) {
        this.blockCodeSetDate = blockCodeSetDate;
    }

    public String getAccountingStatusSetDate() {
        return accountingStatusSetDate;
    }

    public void setAccountingStatusSetDate(String accountingStatusSetDate) {
        this.accountingStatusSetDate = accountingStatusSetDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBusinessDesc() {
        return BusinessDesc;
    }

    public void setBusinessDesc(String businessDesc) {
        BusinessDesc = businessDesc;
    }

    public String getProgramDesc() {
        return programDesc;
    }

    public void setProgramDesc(String programDesc) {
        this.programDesc = programDesc;
    }

    public String getCurrencyDesc() {
        return currencyDesc;
    }

    public void setCurrencyDesc(String currencyDesc) {
        this.currencyDesc = currencyDesc;
    }
    
    public String getProductLevelCodeDesc() {
		return productLevelCodeDesc;
	}

	public void setProductLevelCodeDesc(String productLevelCodeDesc) {
		this.productLevelCodeDesc = productLevelCodeDesc;
	}

    public CoreAccount getCoreAccount() {
        return coreAccount;
    }

    public void setCoreAccount(CoreAccount coreAccount) {
        this.coreAccount = coreAccount;
    }

    public Boolean getHaveChild() {
        return haveChild;
    }

    public void setHaveChild(Boolean haveChild) {
        this.haveChild = haveChild;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "X5515VO{" +
                "productLevelCodeDesc='" + productLevelCodeDesc + '\'' +
                ", id='" + id + '\'' +
                ", productDesc='" + productDesc + '\'' +
                ", customerNo='" + customerNo + '\'' +
                ", delinquencyLevel='" + delinquencyLevel + '\'' +
                ", levelCode='" + levelCode + '\'' +
                ", productObjectNo='" + productObjectNo + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", cycleNo=" + cycleNo +
                ", accountingStatusCode='" + accountingStatusCode + '\'' +
                ", currCyclePaymentMin=" + currCyclePaymentMin +
                ", cycleDueNum=" + cycleDueNum +
                ", cycleDueDayNum=" + cycleDueDayNum +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", timestamp=" + timestamp +
                ", version=" + version +
                ", collectionFlag='" + collectionFlag + '\'' +
                ", collectionDate='" + collectionDate + '\'' +
                ", collectorId='" + collectorId + '\'' +
                ", currCyclePaymentDate='" + currCyclePaymentDate + '\'' +
                ", currCycleBeginPaymentMin=" + currCycleBeginPaymentMin +
                ", payOffDate='" + payOffDate + '\'' +
                ", latestEstimateDate='" + latestEstimateDate + '\'' +
                ", blockCode='" + blockCode + '\'' +
                ", blockCodeSetDate='" + blockCodeSetDate + '\'' +
                ", accountingStatusSetDate='" + accountingStatusSetDate + '\'' +
                ", status='" + status + '\'' +
                ", BusinessDesc='" + BusinessDesc + '\'' +
                ", programDesc='" + programDesc + '\'' +
                ", currencyDesc='" + currencyDesc + '\'' +
                ", coreAccount=" + coreAccount +
                ", haveChild=" + haveChild +
                ", flag='" + flag + '\'' +
                '}';
    }
}
