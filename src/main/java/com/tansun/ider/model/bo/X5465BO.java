package com.tansun.ider.model.bo;

import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.commun.BeanVO;

import java.io.Serializable;
import java.util.List;

/**
 * @Desc: X5465客户延滞状况维护
 * @Author PQ
 * @Date 2019年8月21日
 */
public class X5465BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;
	/** 活动与构件对应关系表 */
	CoreEventActivityRel coreEventActivityRel;
	/** 活动与构件对应关系表 */
	List<CoreActivityArtifactRel> activityArtifactList;
	/** 全局事件编号 */
	private String globalEventNo;
    /** 证件号码 [30,0] */
    private String idNumber;
    /** 证件类型 */
    private String idType;

	/** 外部识别号 [19,0] */
	private String externalIdentificationNo;

	private String operatorId;


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

	public String getGlobalEventNo() {
		return globalEventNo;
	}

	public void setGlobalEventNo(String globalEventNo) {
		this.globalEventNo = globalEventNo;
	}
	@Override
	public String getOperatorId() {
		return operatorId;
	}
	@Override
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	@Override
	public String toString() {
		return "X5465BO{" +
				"coreEventActivityRel=" + coreEventActivityRel +
				", activityArtifactList=" + activityArtifactList +
				", globalEventNo='" + globalEventNo + '\'' +
				", idNumber='" + idNumber + '\'' +
				", idType='" + idType + '\'' +
				", externalIdentificationNo='" + externalIdentificationNo + '\'' +
				", operatorId='" + operatorId + '\'' +
				'}';
	}
}
