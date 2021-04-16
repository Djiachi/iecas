package com.tansun.ider.model.bo;

import java.io.Serializable;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * 分期账户信息查询
 * 
 * @author huangyayun 2018年10月19日
 */
public class X5610BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;

    /** 证件号码 [30,0] */
    private String idNumber;
    /** 外部识别号 [19,0] */
    private String externalIdentificationNo;
    /** 分期类型 */
    private String stageType;
    /** 事件id */
    private String eventId;
    /** 业务维度 */
    private String businessPattern;
    

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

	public String getStageType() {
		return stageType;
	}

	public void setStageType(String stageType) {
		this.stageType = stageType;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getBusinessPattern() {
		return businessPattern;
	}

	public void setBusinessPattern(String businessPattern) {
		this.businessPattern = businessPattern;
	}

}
