package com.tansun.ider.model.bo;

import java.io.Serializable;

import com.tansun.ider.framwork.commun.BeanVO;

/**
 * <p> Title: X4005BO </p>
 * <p> Description: 预算单位信息查询</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年4月23日
 */
public class X4005BO extends BeanVO implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /** 所属机构 */
    private String institutionId;
    /** 事件编号 [14,0] Not NULL */
    protected String eventNo;
    /** 活动编号 [8,0] Not NULL */
    protected String activityNo;
    /** 全局事件编号 */
    private String globalEventNo;
    /** 证件类型 */
    private String idType;
    /** 预算单位编码 */
    private String idNumber;

	/** 证件号码 [19,0] */
    private String externalIdentificationNo;

    public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}

	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}

	public String getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
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

    public String getGlobalEventNo() {
        return globalEventNo;
    }

    public void setGlobalEventNo(String globalEventNo) {
        this.globalEventNo = globalEventNo;
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

	

   
}
