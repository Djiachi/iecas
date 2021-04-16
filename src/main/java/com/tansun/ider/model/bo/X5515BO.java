package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.List;

import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc: X5515客户延滞状况
 * @Author lianhuan
 * @Date 2018年4月25日下午3:04:03
 */
public class X5515BO extends BeanVO implements Serializable {
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
	/** 延滞层级 G-产品线级 P-产品对象级 A-账户级 [1,0] */
	private String delinquencyLevel;
	/** 层级代码 账户代码/产品对象代码/产品线代码 [23,0] */
	private String levelCode;
	/** 产品对象代码 [9,0] */
	private String productObjectNo;
	/** 币种 [3,0] Not NULL */
	private String currencyCode;
	/** 查汇总记录还是查明细记录 */
	private String type;
	/** 延滞ID */
	private String id;

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	/** 外部识别号 [19,0] */
	private String externalIdentificationNo;

	private String operatorId;
	private String accFlag;
	private String pageFlag;

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

	@Override
	public String toString() {
		return "X5515BO{" +
				"coreEventActivityRel=" + coreEventActivityRel +
				", activityArtifactList=" + activityArtifactList +
				", globalEventNo='" + globalEventNo + '\'' +
				", idNumber='" + idNumber + '\'' +
				", idType='" + idType + '\'' +
				", delinquencyLevel='" + delinquencyLevel + '\'' +
				", levelCode='" + levelCode + '\'' +
				", productObjectNo='" + productObjectNo + '\'' +
				", currencyCode='" + currencyCode + '\'' +
				", type='" + type + '\'' +
				", id='" + id + '\'' +
				", externalIdentificationNo='" + externalIdentificationNo + '\'' +
				", operatorId='" + operatorId + '\'' +
				", accFlag='" + accFlag + '\'' +
				", pageFlag='" + pageFlag + '\'' +
				'}';
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

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public String getAccFlag() {
		return accFlag;
	}

	public void setAccFlag(String accFlag) {
		this.accFlag = accFlag;
	}

	public String getPageFlag() {
		return pageFlag;
	}

	public void setPageFlag(String pageFlag) {
		this.pageFlag = pageFlag;
	}
}
