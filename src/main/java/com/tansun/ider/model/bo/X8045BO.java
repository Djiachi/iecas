package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.Date;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: 授权场景
 * @Author kangxuw
 * @Date 2018年5月24日下午3:03:34
 */
public class X8045BO extends BeanVO implements Serializable {

	private static final long serialVersionUID = -2579101747930494850L;

	private String id;

	private String authSceneCode;

	private String authSceneMsg;

	private String triggerNo;

	private String transIdentifiNo;

	private Integer authUnreachDays;

	private String eventBookKeepingDirec;

	private String gmtCreate;

	private Date timestamp;

	private Integer version;

	private String outstandingFalg;

	private String authTyp;
	
	/** 容差百分比 [3,0] */
    protected Integer tolerance;
    /** 是否支持自动分期标识 [1,0] */
    protected String installFlag;
    /** 管控场景码 [6,0] */
    protected String contrlSceneCode;
    
	public Integer getTolerance() {
        return tolerance;
    }

    public void setTolerance(Integer tolerance) {
        this.tolerance = tolerance;
    }

    public String getInstallFlag() {
        return installFlag;
    }

    public void setInstallFlag(String installFlag) {
        this.installFlag = installFlag;
    }

    public String getContrlSceneCode() {
        return contrlSceneCode;
    }

    public void setContrlSceneCode(String contrlSceneCode) {
        this.contrlSceneCode = contrlSceneCode;
    }

    public String getAuthSceneCode() {
		return authSceneCode;
	}

	public String getAuthSceneMsg() {
		return authSceneMsg;
	}

	public String getAuthTyp() {
		return authTyp;
	}

	public Integer getAuthUnreachDays() {
		return authUnreachDays;
	}

	public String getEventBookKeepingDirec() {
		return eventBookKeepingDirec;
	}

	public String getGmtCreate() {
		return gmtCreate;
	}

	public String getId() {
		return id;
	}

	public String getOutstandingFalg() {
		return outstandingFalg;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getTransIdentifiNo() {
		return transIdentifiNo;
	}

	public String getTriggerNo() {
		return triggerNo;
	}

	public Integer getVersion() {
		return version;
	}

	public void setAuthSceneCode(String authSceneCode) {
		this.authSceneCode = authSceneCode;
	}

	public void setAuthSceneMsg(String authSceneMsg) {
		this.authSceneMsg = authSceneMsg;
	}

	public void setAuthTyp(String authTyp) {
		this.authTyp = authTyp;
	}

	public void setAuthUnreachDays(Integer authUnreachDays) {
		this.authUnreachDays = authUnreachDays;
	}

	public void setEventBookKeepingDirec(String eventBookKeepingDirec) {
		this.eventBookKeepingDirec = eventBookKeepingDirec;
	}

	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setOutstandingFalg(String outstandingFalg) {
		this.outstandingFalg = outstandingFalg;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public void setTransIdentifiNo(String transIdentifiNo) {
		this.transIdentifiNo = transIdentifiNo;
	}

	public void setTriggerNo(String triggerNo) {
		this.triggerNo = triggerNo;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "X8045BO [id=" + id + ", authSceneCode=" + authSceneCode + ", authSceneMsg=" + authSceneMsg
				+ ", triggerNo=" + triggerNo + ", transIdentifiNo=" + transIdentifiNo + ", authUnreachDays="
				+ authUnreachDays + ", eventBookKeepingDirec=" + eventBookKeepingDirec + ", gmtCreate=" + gmtCreate
				+ ", timestamp=" + timestamp + ", version=" + version + ", outstandingFalg=" + outstandingFalg
				+ ", authTyp=" + authTyp + "]";
	}

}
