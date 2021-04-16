package com.tansun.ider.model.bo;

import java.io.Serializable;
import com.tansun.ider.framwork.commun.BeanVO;

public class X5960BO extends BeanVO implements Serializable {
    private static final long serialVersionUID = 3748414531769524033L;
    public String queryTableName;
    /** 查询交易登记表start */
    public String transBillingState;
    public Integer postingRetryNum;
    /** 查询交易登记表end */
    /** 查询账户表start */
    public String accountId;
    public String currencyCode;
    public String businessTypeCode;
    public String customerNo;
    public String productObjectCode;
    public String businessProgramNo;
    /** 查询账户表end */
    /** 查询媒介基本信息表start */
    public String externalIdentificationNo;
    public String invalidFlag;
    public String idNumber;
    public String mediaUnitCode;
    /** 查询媒介基本信息表end */
    /** 查询客户免除信息表start */
    public String feeItemNo;
    public String waiveCycleNo;
    public String EntityKey;
	private String instanCode1;
	private String instanCode2;
	private String instanCode3;
	private String instanCode4;
	private String instanCode5;
    /** 查询客户免除信息表end */
    /** 查询产品形式表start */
    public String productForm;
    public String productHolderNo;
    /** 查询产品形式表end */
    /** 查询G类活动日志start */
    public String occurrDate;
    public String transHisFlag;
    /** 查询G类活动日志end */
    public String globalSerialNumbr;
    public String occurrTime;
    public String logLevel;

    public String eventNo;
    public String activityNo;
    public String cycleNumber;
    public String pricingObjectCode;
    public String processingDate;
    /** 查询客户个性化元件start */
    public String elementNoPre;

    /** 定价层级 [1,0] */
    private String pricingLevel;
    /** 定价层级代码 [12,0] */
    private String pricingLevelCode;
    private String pricingScope;
    
    public String getPricingScope() {
		return pricingScope;
	}

	public void setPricingScope(String pricingScope) {
		this.pricingScope = pricingScope;
	}

	public String getPricingLevel() {
        return pricingLevel;
    }

    public void setPricingLevel(String pricingLevel) {
        this.pricingLevel = pricingLevel;
    }

    public String getPricingLevelCode() {
        return pricingLevelCode;
    }

    public void setPricingLevelCode(String pricingLevelCode) {
        this.pricingLevelCode = pricingLevelCode;
    }

    /** 查询客户个性化元件end */

    public String getTransBillingState() {
        return transBillingState;
    }

    public void setTransBillingState(String transBillingState) {
        this.transBillingState = transBillingState;
    }

    public Integer getPostingRetryNum() {
        return postingRetryNum;
    }

    public void setPostingRetryNum(Integer postingRetryNum) {
        this.postingRetryNum = postingRetryNum;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getBusinessTypeCode() {
        return businessTypeCode;
    }

    public void setBusinessTypeCode(String businessTypeCode) {
        this.businessTypeCode = businessTypeCode;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getProductObjectCode() {
        return productObjectCode;
    }

    public void setProductObjectCode(String productObjectCode) {
        this.productObjectCode = productObjectCode;
    }

    public String getBusinessProgramNo() {
        return businessProgramNo;
    }

    public void setBusinessProgramNo(String businessProgramNo) {
        this.businessProgramNo = businessProgramNo;
    }

    public String getQueryTableName() {
        return queryTableName;
    }

    public void setQueryTableName(String queryTableName) {
        this.queryTableName = queryTableName;
    }

    public String getExternalIdentificationNo() {
        return externalIdentificationNo;
    }

    public void setExternalIdentificationNo(String externalIdentificationNo) {
        this.externalIdentificationNo = externalIdentificationNo;
    }

    public String getInvalidFlag() {
        return invalidFlag;
    }

    public void setInvalidFlag(String invalidFlag) {
        this.invalidFlag = invalidFlag;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getFeeItemNo() {
        return feeItemNo;
    }

    public void setFeeItemNo(String feeItemNo) {
        this.feeItemNo = feeItemNo;
    }

    public String getWaiveCycleNo() {
        return waiveCycleNo;
    }

    public void setWaiveCycleNo(String waiveCycleNo) {
        this.waiveCycleNo = waiveCycleNo;
    }

    public String getEntityKey() {
        return EntityKey;
    }

    public void setEntityKey(String entityKey) {
        EntityKey = entityKey;
    }

    public String getMediaUnitCode() {
        return mediaUnitCode;
    }

    public void setMediaUnitCode(String mediaUnitCode) {
        this.mediaUnitCode = mediaUnitCode;
    }

    public String getProductForm() {
        return productForm;
    }

    public void setProductForm(String productForm) {
        this.productForm = productForm;
    }

    public String getProductHolderNo() {
        return productHolderNo;
    }

    public void setProductHolderNo(String productHolderNo) {
        this.productHolderNo = productHolderNo;
    }

    public String getOccurrDate() {
        return occurrDate;
    }

    public void setOccurrDate(String occurrDate) {
        this.occurrDate = occurrDate;
    }

    public String getTransHisFlag() {
        return transHisFlag;
    }

    public void setTransHisFlag(String transHisFlag) {
        this.transHisFlag = transHisFlag;
    }

    public String getGlobalSerialNumbr() {
        return globalSerialNumbr;
    }

    public void setGlobalSerialNumbr(String globalSerialNumbr) {
        this.globalSerialNumbr = globalSerialNumbr;
    }

    public String getOccurrTime() {
        return occurrTime;
    }

    public void setOccurrTime(String occurrTime) {
        this.occurrTime = occurrTime;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
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

    public String getCycleNumber() {
        return cycleNumber;
    }

    public void setCycleNumber(String cycleNumber) {
        this.cycleNumber = cycleNumber;
    }

    public String getPricingObjectCode() {
        return pricingObjectCode;
    }

    public void setPricingObjectCode(String pricingObjectCode) {
        this.pricingObjectCode = pricingObjectCode;
    }

    public String getProcessingDate() {
        return processingDate;
    }

    public void setProcessingDate(String processingDate) {
        this.processingDate = processingDate;
    }

    public String getElementNoPre() {
        return elementNoPre;
    }

    public void setElementNoPre(String elementNoPre) {
        this.elementNoPre = elementNoPre;
    }

	public String getInstanCode1() {
		return instanCode1;
	}

	public void setInstanCode1(String instanCode1) {
		this.instanCode1 = instanCode1;
	}

	public String getInstanCode2() {
		return instanCode2;
	}

	public void setInstanCode2(String instanCode2) {
		this.instanCode2 = instanCode2;
	}

	public String getInstanCode3() {
		return instanCode3;
	}

	public void setInstanCode3(String instanCode3) {
		this.instanCode3 = instanCode3;
	}

	public String getInstanCode4() {
		return instanCode4;
	}

	public void setInstanCode4(String instanCode4) {
		this.instanCode4 = instanCode4;
	}

	public String getInstanCode5() {
		return instanCode5;
	}

	public void setInstanCode5(String instanCode5) {
		this.instanCode5 = instanCode5;
	}


}
