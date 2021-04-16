package com.tansun.ider.model.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 客户核算状态查询返回值
 *
 * @author PanQi
 */
public class X5465VO {

    /** id [64,0] Not NULL */
    private String id;
    /** 产品描述 [50,0] */
    private String productDesc;
    /** 客户号 [12,0] Not NULL */
    private String customerNo;
    /** 延滞层级 G-产品线级 P-产品对象级 A-账户级 [1,0] */
    private String delinquencyLevel;
    /** 层级代码 账户代码/产品对象代码/产品线代码 [23,0] */
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
    /** 本金 */
    private BigDecimal prinAmount;
    /** 利息 */
    private BigDecimal intAmount;
    /** 费用 */
    private BigDecimal feeAmount;

    private String programDesc;

    private String currencyDesc;

    private List<String> toBeCheckStatusCodes;

    private String prevAccountingStatus;

    private String nextAccountingStatus;

    private String operationMode;
    private String prevAccountingStatusDesc;

    private String nextAccountingStatusDesc;

    private String customerName;

    private String idNumber;
    // 业务项目
    private String businessProgramNo;
    // 业务项目描述
    private String businessDesc;
    //
    private String levelDesc;

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

    public BigDecimal getPrinAmount() {
        return prinAmount;
    }

    public void setPrinAmount(BigDecimal prinAmount) {
        this.prinAmount = prinAmount;
    }

    public BigDecimal getIntAmount() {
        return intAmount;
    }

    public void setIntAmount(BigDecimal intAmount) {
        this.intAmount = intAmount;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getBusinessDesc() {
        return businessDesc;
    }

    public void setBusinessDesc(String businessDesc) {
        businessDesc = businessDesc;
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

    public List<String> getToBeCheckStatusCodes() {
        return toBeCheckStatusCodes;
    }

    public void setToBeCheckStatusCodes(List<String> toBeCheckStatusCodes) {
        this.toBeCheckStatusCodes = toBeCheckStatusCodes;
    }

    public String getPrevAccountingStatus() {
        return prevAccountingStatus;
    }

    public void setPrevAccountingStatus(String prevAccountingStatus) {
        this.prevAccountingStatus = prevAccountingStatus;
    }

    public String getNextAccountingStatus() {
        return nextAccountingStatus;
    }

    public void setNextAccountingStatus(String nextAccountingStatus) {
        this.nextAccountingStatus = nextAccountingStatus;
    }

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    public String getPrevAccountingStatusDesc() {
        return prevAccountingStatusDesc;
    }

    public void setPrevAccountingStatusDesc(String prevAccountingStatusDesc) {
        this.prevAccountingStatusDesc = prevAccountingStatusDesc;
    }

    public String getNextAccountingStatusDesc() {
        return nextAccountingStatusDesc;
    }

    public void setNextAccountingStatusDesc(String nextAccountingStatusDesc) {
        this.nextAccountingStatusDesc = nextAccountingStatusDesc;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getBusinessProgramNo() {
        return businessProgramNo;
    }

    public void setBusinessProgramNo(String businessProgramNo) {
        this.businessProgramNo = businessProgramNo;
    }

    public String getLevelDesc() {
        return levelDesc;
    }

    public void setLevelDesc(String levelDesc) {
        this.levelDesc = levelDesc;
    }

    @Override
    public String toString() {
        return "X5465VO{" + "id='" + id + '\'' + ", productDesc='" + productDesc + '\'' + ", customerNo='" + customerNo
                + '\'' + ", delinquencyLevel='" + delinquencyLevel + '\'' + ", levelCode='" + levelCode + '\''
                + ", productObjectNo='" + productObjectNo + '\'' + ", currencyCode='" + currencyCode + '\''
                + ", cycleNo=" + cycleNo + ", accountingStatusCode='" + accountingStatusCode + '\''
                + ", currCyclePaymentMin=" + currCyclePaymentMin + ", cycleDueNum=" + cycleDueNum + ", cycleDueDayNum="
                + cycleDueDayNum + ", gmtCreate='" + gmtCreate + '\'' + ", timestamp=" + timestamp + ", version="
                + version + ", latestEstimateDate='" + latestEstimateDate + '\'' + ", blockCode='" + blockCode + '\''
                + ", blockCodeSetDate='" + blockCodeSetDate + '\'' + ", accountingStatusSetDate='"
                + accountingStatusSetDate + '\'' + ", status='" + status + '\'' + ", prinAmount=" + prinAmount
                + ", intAmount=" + intAmount + ", feeAmount=" + feeAmount + ", programDesc='" + programDesc + '\''
                + ", currencyDesc='" + currencyDesc + '\'' + ", toBeCheckStatusCodes=" + toBeCheckStatusCodes
                + ", prevAccountingStatus='" + prevAccountingStatus + '\'' + ", nextAccountingStatus='"
                + nextAccountingStatus + '\'' + ", operationMode='" + operationMode + '\''
                + ", prevAccountingStatusDesc='" + prevAccountingStatusDesc + '\'' + ", nextAccountingStatusDesc='"
                + nextAccountingStatusDesc + '\'' + ", customerName='" + customerName + '\'' + ", idNumber='" + idNumber
                + '\'' + ", businessProgramNo='" + businessProgramNo + '\'' + ", businessDesc='" + businessDesc + '\''
                + ", levelDesc='" + levelDesc + '\'' + '}';
    }
}
