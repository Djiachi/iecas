package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.util.List;

import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.commun.BeanVO;
import com.tansun.ider.model.X5120VO;

/**
 * @Desc:账户基本信息查询 查询账户基本信息
 * @Author wt
 * @Date 2018年4月28日 下午2:43:42
 */
public class X5115BO extends BeanVO implements Serializable {
    private static final long serialVersionUID = -4256912206267593510L;
	/** 活动与构件对应关系表 */
	CoreEventActivityRel coreEventActivityRel;
	/** 活动与构件对应关系表 */
	List<CoreActivityArtifactRel> activityArtifactList;
	/** 全局事件编号 */
	private String accountId;
	private String globalEventNo;
    private List<X5120VO> listX5120VOs;
    private String credentialNumber;
    private String externalIdentificationNo;
    private String idNumber;
    private String idType;
    private String accountOrganForm;
    private String flag;
    private String payFlag;
	private String accFlag;
	private String globalTransSerialNo;
	private String transIdentifiNo;
	private String fundNum;
	private String businessTypeCode;
	private String businessProgramNo;
	private String currencyCode;
	private String pageFlag;
	private String customer;
	private String operationMode;

	public String getPayFlag() {
		return payFlag;
	}

	public void setPayFlag(String payFlag) {
		this.payFlag = payFlag;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getIdType() {
		return idType;
	}

	public String getAccountOrganForm() {
        return accountOrganForm;
    }

    public void setAccountOrganForm(String accountOrganForm) {
        this.accountOrganForm = accountOrganForm;
    }

    public void setIdType(String idType) {
		this.idType = idType;
	}

	private String operatorId;

    public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public List<X5120VO> getListX5120VOs() {
        return listX5120VOs;
    }

    public void setListX5120VOs(List<X5120VO> listX5120VOs) {
        this.listX5120VOs = listX5120VOs;
    }

    public String getCredentialNumber() {
        return credentialNumber;
    }

    public void setCredentialNumber(String credentialNumber) {
        this.credentialNumber = credentialNumber;
    }

    public String getExternalIdentificationNo() {
        return externalIdentificationNo;
    }

    public void setExternalIdentificationNo(String externalIdentificationNo) {
        this.externalIdentificationNo = externalIdentificationNo;
    }

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getOperationMode() {
		return operationMode;
	}

	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}

	@Override
	public String toString() {
		return "X5115BO{" +
				"coreEventActivityRel=" + coreEventActivityRel +
				", activityArtifactList=" + activityArtifactList +
				", accountId='" + accountId + '\'' +
				", globalEventNo='" + globalEventNo + '\'' +
				", listX5120VOs=" + listX5120VOs +
				", credentialNumber='" + credentialNumber + '\'' +
				", externalIdentificationNo='" + externalIdentificationNo + '\'' +
				", idNumber='" + idNumber + '\'' +
				", idType='" + idType + '\'' +
				", accountOrganForm='" + accountOrganForm + '\'' +
				", flag='" + flag + '\'' +
				", payFlag='" + payFlag + '\'' +
				", accFlag='" + accFlag + '\'' +
				", globalTransSerialNo='" + globalTransSerialNo + '\'' +
				", transIdentifiNo='" + transIdentifiNo + '\'' +
				", fundNum='" + fundNum + '\'' +
				", businessTypeCode='" + businessTypeCode + '\'' +
				", businessProgramNo='" + businessProgramNo + '\'' +
				", currencyCode='" + currencyCode + '\'' +
				", pageFlag='" + pageFlag + '\'' +
				", customer='" + customer + '\'' +
				", operatorId='" + operatorId + '\'' +
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

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccFlag() {
		return accFlag;
	}

	public void setAccFlag(String accFlag) {
		this.accFlag = accFlag;
	}

	public String getGlobalTransSerialNo() {
		return globalTransSerialNo;
	}

	public void setGlobalTransSerialNo(String globalTransSerialNo) {
		this.globalTransSerialNo = globalTransSerialNo;
	}

	public String getTransIdentifiNo() {
		return transIdentifiNo;
	}

	public void setTransIdentifiNo(String transIdentifiNo) {
		this.transIdentifiNo = transIdentifiNo;
	}

	public String getFundNum() {
		return fundNum;
	}

	public void setFundNum(String fundNum) {
		this.fundNum = fundNum;
	}

	public String getBusinessTypeCode() {
		return businessTypeCode;
	}

	public void setBusinessTypeCode(String businessTypeCode) {
		this.businessTypeCode = businessTypeCode;
	}

	public String getBusinessProgramNo() {
		return businessProgramNo;
	}

	public void setBusinessProgramNo(String businessProgramNo) {
		this.businessProgramNo = businessProgramNo;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getPageFlag() {
		return pageFlag;
	}

	public void setPageFlag(String pageFlag) {
		this.pageFlag = pageFlag;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}
}
