package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.dao.issue.entity.CoreCustomerElement;
import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc:X5580客户个性化元件
 * @Author yanzhaofei
 * @Date 2019年1月24日14:25:34
 */
public class X5580BO extends BeanVO implements Serializable {

	private static final long serialVersionUID = 1237231529205939117L;
	/** 客户个性化元件列表 */
	private List<CoreCustomerElement> list;
	/** id */
	private String id;
	/** 事件编号 [14,0] NOT NULL */
	private String eventNo;
	/** 活动编号[8,0] NOT NULL */
	private String activityNo;
	/** 操作员Id */
	private String operatorId;
	/** 证件号 [30,0] NOT NULL */
	private String idNumber;
	/** 外部识别号 [19,0] NOT NULL */
	private String externalIdentificationNo;
	/** 客户代码 [12,0] NOT NULL KEY */
	private String customerNo;
	/** 元件编号 [10,0] NOT NULL */
	private String elementNo;
	/** 创建日期 [10,0] NOT NULL */
	// 文档中写的是8位，日期格式应为10位：YYYY-MM-DD
	private String gmtCreate;
	/** 生效日期 [10,0] NOT NULL */
	private String effectDate;
	/** 失效日期 [10,0] NOT NULL */
	// 时间戳和版本号文档中没有
	private String uneffectDate;
	/** 时间戳 : oralce使用触发器更新， mysql使用自动更新 [19,0] Not NULL */
	private Date timestamp;
	/** 版本号 [10,0] */
	private Integer version;
	/** 活动与构件对应关系表 */
	private CoreEventActivityRel coreEventActivityRel;
	/** 活动与构件对应关系表 */
	private List<CoreActivityArtifactRel> activityArtifactList;

	public String toString() {
		return "X5580BO [id:" + id + ",eventNo:" + eventNo + ",activityNo:" + activityNo + ",operatorId:"
				+ operatorId + ",idNumber:" + idNumber + ",externalIdentificationNo:" + externalIdentificationNo
				+ ",customerNo:" + customerNo + ",elementNo:" + elementNo + ",createDate:" + gmtCreate + ",effectDate:"
				+ effectDate + ",uneffectDate:" + uneffectDate + ",timestamp:" + timestamp + ",version:" + version
				+ "]";
	}

	public List<CoreCustomerElement> getList() {
		return list;
	}

	public void setList(List<CoreCustomerElement> list) {
		this.list = list;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
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

	public String getElementNo() {
		return elementNo;
	}

	public void setElementNo(String elementNo) {
		this.elementNo = elementNo;
	}

	public String getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public String getEffectDate() {
		return effectDate;
	}

	public void setEffectDate(String effectDate) {
		this.effectDate = effectDate;
	}

	public String getUneffectDate() {
		return uneffectDate;
	}

	public void setUneffectDate(String uneffectDate) {
		this.uneffectDate = uneffectDate;
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
}
