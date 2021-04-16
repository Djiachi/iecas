package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * @Desc:媒介标签信息维护
 * @Author wt
 * @Date 2018年5月8日 上午11:27:32
 */
public class X5170BO implements Serializable {

	private static final long serialVersionUID = -4775605999651410591L;
	/** 事件编号 [14,0] Not NULL */
    protected String eventNo;
    /** 活动编号 [8,0] Not NULL */
    protected String activityNo;
	/** 操作员Id */
	private String operatorId;
	/* id */
	private String id;
	/* 媒介单元代码 */
	private String mediaUnitCode;
	/* 标签号 */
	private String labelNumber;
	/* 生效日期 */
	private Date effectiveDate;
	/* 失效日期 */
	private Date expirationDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMediaUnitCode() {
		return mediaUnitCode;
	}

	public void setMediaUnitCode(String mediaUnitCode) {
		this.mediaUnitCode = mediaUnitCode;
	}

	public String getLabelNumber() {
		return labelNumber;
	}

	public void setLabelNumber(String labelNumber) {
		this.labelNumber = labelNumber;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
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

	@Override
	public String toString() {
		return "X5170BO [id=" + id + ", mediaUnitCode=" + mediaUnitCode + ", labelNumber=" + labelNumber
				+ ", effectiveDate=" + effectiveDate + ", expirationDate=" + expirationDate + "]";
	}

}
