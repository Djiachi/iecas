package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.List;

import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: X5755单位公务卡额度
 * @Author liuyanxi
 * @Date 2019年5月11日下午3:04:03
 */
public class X5755BO extends BeanVO implements Serializable {
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
    //是否是4055调用查询  0/空、null不是/1 是
    private String tansferFind;
    
	public String getTansferFind() {
		return tansferFind;
	}

	public void setTansferFind(String tansferFind) {
		this.tansferFind = tansferFind;
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
    
	@Override
	public String toString() {
		return "X5515BO [coreEventActivityRel=" + coreEventActivityRel + ", activityArtifactList="
				+ activityArtifactList + ", globalEventNo=" + globalEventNo + ", idNumber=" + idNumber + ", idType="
				+ idType +  ", operatorId=" 
				+ "]";
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
