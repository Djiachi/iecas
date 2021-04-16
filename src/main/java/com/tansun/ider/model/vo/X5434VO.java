package com.tansun.ider.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.tansun.ider.framwork.commun.BeanVO;

/**
 * 延滞冲减查询
 * @author qianyp 
 */
public class X5434VO extends BeanVO implements Serializable {

	private static final long serialVersionUID = 8234102741661521353L;
	
	private String id;
	/** 全局流水号 [36,0] Not NULL */
    private String globalSerialNumbr;
    /** 客户代码 [18,0] Not NULL */
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
    /** 事件编号 [14,0] Not NULL */
    private String eventNo;
    /** 活动编号 [8,0] Not NULL */
    private String activityNo;
    /** 发生日期 yyyy-MM-dd [10,0] */
    private String occurrDate;
    /** 发生时间 HH:mm:ss [12,0] */
    private String occurrTime;
    /** 最低还款冲减金额 [18,0] */
    private BigDecimal minPaymentChange;
    /** 交易状态 NOR-正常 INS-已分期 REV-已冲正 FRT-全额退货 PRT-部分退货 DIS-争议登记 [3,0] */
    private String transState;
    /** 逾期天数 [10,0] */
    private Integer cycleDueDayNum;
    /** 逾期期数/状态 [10,0] */
    private Integer cycleDueNum;
    /**  [19,0] Not NULL */
    private Date timestamp;
    /** 创建时间 yyyy-MM-dd HH:mm:ss.SSS [23,0] */
    private String gmtCreate;
    /** 版本号 [10,0] Not NULL */
    private Integer version;
    
    private String businessDesc;
    private String programDesc;
    private String productLevelCodeDesc;
    private String productDesc;
    private String currencyDesc;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGlobalSerialNumbr() {
		return globalSerialNumbr;
	}
	public void setGlobalSerialNumbr(String globalSerialNumbr) {
		this.globalSerialNumbr = globalSerialNumbr;
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
	public String getOccurrDate() {
		return occurrDate;
	}
	public void setOccurrDate(String occurrDate) {
		this.occurrDate = occurrDate;
	}
	public String getOccurrTime() {
		return occurrTime;
	}
	public void setOccurrTime(String occurrTime) {
		this.occurrTime = occurrTime;
	}
	public BigDecimal getMinPaymentChange() {
		return minPaymentChange;
	}
	public void setMinPaymentChange(BigDecimal minPaymentChange) {
		this.minPaymentChange = minPaymentChange;
	}
	public String getTransState() {
		return transState;
	}
	public void setTransState(String transState) {
		this.transState = transState;
	}
	public Integer getCycleDueDayNum() {
		return cycleDueDayNum;
	}
	public void setCycleDueDayNum(Integer cycleDueDayNum) {
		this.cycleDueDayNum = cycleDueDayNum;
	}
	public Integer getCycleDueNum() {
		return cycleDueNum;
	}
	public void setCycleDueNum(Integer cycleDueNum) {
		this.cycleDueNum = cycleDueNum;
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
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getBusinessDesc() {
		return businessDesc;
	}
	public void setBusinessDesc(String businessDesc) {
		this.businessDesc = businessDesc;
	}
	public String getProgramDesc() {
		return programDesc;
	}
	public void setProgramDesc(String programDesc) {
		this.programDesc = programDesc;
	}
	public String getProductLevelCodeDesc() {
		return productLevelCodeDesc;
	}
	public void setProductLevelCodeDesc(String productLevelCodeDesc) {
		this.productLevelCodeDesc = productLevelCodeDesc;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public String getCurrencyDesc() {
		return currencyDesc;
	}
	public void setCurrencyDesc(String currencyDesc) {
		this.currencyDesc = currencyDesc;
	}
	
}
