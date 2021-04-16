package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.commun.BeanVO;

/**
 * @Desc:X5330异常交易查询
 * @Author lianhuan
 * @Date 2018年4月24日下午4:16:00
 */
public class X5330BO extends BeanVO implements Serializable {
    private static final long serialVersionUID = 5035069993911420241L;
	/** 活动与构件对应关系表 */
	CoreEventActivityRel coreEventActivityRel;
	/** 活动与构件对应关系表 */
	List<CoreActivityArtifactRel> activityArtifactList;
	/** 全局事件编号 */
	private String globalEventNo;
    /** 异常交易id */
    private String transId;
    /** 操作类型，是人工重发入账M，还是交易删除P */
    private String type;
    /** 证件号码 [30,0] */
    private String idNumber;
    /** 客户号 [12,0] */
    private String customerNo;
    /** 交易处理开始日期 [10,0] */
    private String startDate;
    /** 交易处理结束日期 [10,0] */
    private String endDate;
    /** 外部识别号 [19,0] */
    private String externalIdentificationNo;
    /** 交易币种 [3,0] */
    private String transCurrCode;
    /** 交易金额 [18,0] */
    private BigDecimal transAmount;
    /** 入账币种 [3,0] */
    private String postingCurrCode;
    /** 入账金额 [18,0] */
    private BigDecimal postingAmt;
    /** 交易日期 [10,0] */
    private String transDate;
    /** 交易状态 N-未处理 Y-重入账成功 P-交易删除 */
    private String transBillingState ;
    /** 失败原因 */
    private String failureReason ;
    
    private String operatorId;
    //重入账和强制入账
    private String transFlag;
    private String newExternalIdentificationNo;

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
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

    public String getExternalIdentificationNo() {
        return externalIdentificationNo;
    }

    public void setExternalIdentificationNo(String externalIdentificationNo) {
        this.externalIdentificationNo = externalIdentificationNo;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getTransCurrCode() {
        return transCurrCode;
    }

    public void setTransCurrCode(String transCurrCode) {
        this.transCurrCode = transCurrCode;
    }

    public BigDecimal getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(BigDecimal transAmount) {
        this.transAmount = transAmount;
    }

    public String getPostingCurrCode() {
        return postingCurrCode;
    }

    public void setPostingCurrCode(String postingCurrCode) {
        this.postingCurrCode = postingCurrCode;
    }

    public BigDecimal getPostingAmt() {
        return postingAmt;
    }

    public void setPostingAmt(BigDecimal postingAmt) {
        this.postingAmt = postingAmt;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

	public String getTransBillingState() {
		return transBillingState;
	}

	public void setTransBillingState(String transBillingState) {
		this.transBillingState = transBillingState;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

    @Override
    public String toString() {
        return "X5330BO [transId=" + transId + ", type=" + type + ", idNumber=" + idNumber + ", customerNo="
                + customerNo + ", startDate=" + startDate + ", endDate=" + endDate + ", externalIdentificationNo="
                + externalIdentificationNo + ", transCurrCode=" + transCurrCode + ", transAmount=" + transAmount
                + ", postingCurrCode=" + postingCurrCode + ", postingAmt=" + postingAmt + ", transDate=" + transDate+ ", transBillingState=" + transBillingState+ ", failureReason=" + failureReason
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

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getTransFlag() {
		return transFlag;
	}

	public void setTransFlag(String transFlag) {
		this.transFlag = transFlag;
	}

	public String getNewExternalIdentificationNo() {
		return newExternalIdentificationNo;
	}

	public void setNewExternalIdentificationNo(String newExternalIdentificationNo) {
		this.newExternalIdentificationNo = newExternalIdentificationNo;
	}
}
