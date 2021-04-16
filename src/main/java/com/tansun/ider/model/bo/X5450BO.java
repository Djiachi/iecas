package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.commun.BeanVO;

public class X5450BO extends BeanVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8505992800930785120L;
	/** 活动与构件对应关系表 */
	CoreEventActivityRel coreEventActivityRel;
	/** 活动与构件对应关系表 */
	List<CoreActivityArtifactRel> activityArtifactList;
	/** 全局事件编号 */
	private String globalEventNo;
    /** 事件编号 [14,0] Not NULL */
	private String eventNo;
    /** 活动编号 [8,0] Not NULL */
	private String activityNo;
    /** 发生日期 yyyy-MM-dd [10,0] Not NULL */
	private String occurrDate;
    /** 发生时间 HH:mm:ss.sss [12,0] Not NULL */
	private String occurrTime;
    /** 关联表主键ID [64,0] Not NULL */
	private String relativeTableId;
    /** 关联表名 [320,0] Not NULL */
	private String relativeTableName;
    /** 日志层级 C-客户级 A-账户级 P-产品级 M-媒介级 R-参数级 D-客户业务项目级 [4,0] Not NULL */
	private String logLevel;
    /** 修改类型 ADD-新增 UPD-更新 DEL-删除  [3,0] Not NULL */
	private String modifyType;
    /** 客户号 [12,0] */
	private String customerNo;
    /** 账户代码/产品代码/媒介单元代码/业务项目代码/地址类型 [18,0] */
	private String entityKey;
    /** 货币代码 [3,0] */
	private String currencyCode;
    /** 修改字段名 [64,0] Not NULL */
	private String modifyFieldName;
    /** 修改前内容 [4000,0] */
	private String modifyBefore;
    /** 修改后内容 [4000,0] */
	private String modifyAfter;
    /** 维护人员 [20,0] Not NULL */
	private String operatorId;
    /** 创建时间 yyyy-MM-dd HH:mm:ss [23,0] */
	private String gmtCreate;
    /** 时间戳 : oralce使用触发器更新， mysql使用自动更新 [19,0] Not NULL */
	private Date timestamp;
    /** 版本号 [10,0] */
	private Integer version;
    /** 针对非金融工程分页查询 第几页 */
    private int pageNum;
    /** 每页有多少条记录 */
    private int pageSize;
    /** 从第几天开始查询 */
    private int indexNo;
	/** 证件类型 */
	private String idType;
	/** 证件号码 */
	private String idNumber;
	/** 外部识别号 */
	private String externalIdentificationNo;
	/** 开始日期 */
	private String startDate;
	/** 结束日期 yyyy-MM-dd [10,0] */
	private String endDate;
	/**登录用户*/
    private String loginName;
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
	public String getRelativeTableId() {
		return relativeTableId;
	}
	public void setRelativeTableId(String relativeTableId) {
		this.relativeTableId = relativeTableId;
	}
	public String getRelativeTableName() {
		return relativeTableName;
	}
	public void setRelativeTableName(String relativeTableName) {
		this.relativeTableName = relativeTableName;
	}
	public String getLogLevel() {
		return logLevel;
	}
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}
	public String getModifyType() {
		return modifyType;
	}
	public void setModifyType(String modifyType) {
		this.modifyType = modifyType;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getEntityKey() {
		return entityKey;
	}
	public void setEntityKey(String entityKey) {
		this.entityKey = entityKey;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getModifyFieldName() {
		return modifyFieldName;
	}
	public void setModifyFieldName(String modifyFieldName) {
		this.modifyFieldName = modifyFieldName;
	}
	public String getModifyBefore() {
		return modifyBefore;
	}
	public void setModifyBefore(String modifyBefore) {
		this.modifyBefore = modifyBefore;
	}
	public String getModifyAfter() {
		return modifyAfter;
	}
	public void setModifyAfter(String modifyAfter) {
		this.modifyAfter = modifyAfter;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
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
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getIndexNo() {
		return indexNo;
	}
	public void setIndexNo(int indexNo) {
		this.indexNo = indexNo;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
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
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getGlobalEventNo() {
		return globalEventNo;
	}
	public void setGlobalEventNo(String globalEventNo) {
		this.globalEventNo = globalEventNo;
	}
    
}









