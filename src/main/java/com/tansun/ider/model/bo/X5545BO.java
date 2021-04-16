package com.tansun.ider.model.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.dao.beta.entity.CoreEventActivityRel;
import com.tansun.ider.framwork.commun.BeanVO;

/**
 * 客户交易统计查询
 * 
 * @author lianhuan 2018年9月14日
 */
public class X5545BO extends BeanVO implements Serializable {

    private static final long serialVersionUID = 5035068883811420241L;
	/** 活动与构件对应关系表 */
	CoreEventActivityRel coreEventActivityRel;
	/** 活动与构件对应关系表 */
	List<CoreActivityArtifactRel> activityArtifactList;
	/** 全局事件编号 */
	private String globalEventNo;
	private String operatorId;
    /** 证件号码 [30,0] */
    private String idNumber;
    /** 证件类型 */
    private String idType;
    /** 客户号 [12,0] */
    protected String customerNo;
    /** 产品对象代码 [18,0] Not NULL */
    protected String productObjectCode;
    /** 周期号 [10,0] */
    protected Integer cycleNo;
    /** 交易识别代码 [4,0] */
    protected String transIdentifiNo;
    /** 币种 [3,0] Not NULL */
    protected String currencyCode;
    /** 累计借记交易笔数 [10,0] */
    protected Integer accumultTransNumDebit;
    /** 累计借记交易金额 [18,0] */
    protected BigDecimal accumultTransAmtDebit;
    /** 累计贷记交易笔数 [10,0] */
    protected Integer accumultTransNumCredit;
    /** 累计贷记交易金额 [18,0] */
    protected BigDecimal accumultTransAmtCredit;
    /** 产品形式代码 [18,0] Not NULL */
    protected String productForm;
    /** 交易统计组别 [4,0] */
    protected String transStatisticGroup;
    
    private String productDesc;
    private String transIdentifiDesc;
    private String currencyDesc;
    
    public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	/** 外部识别号 [19,0] */
    private String externalIdentificationNo;

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

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
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

	public Integer getCycleNo() {
		return cycleNo;
	}

	public void setCycleNo(Integer cycleNo) {
		this.cycleNo = cycleNo;
	}

	public String getTransIdentifiNo() {
		return transIdentifiNo;
	}

	public void setTransIdentifiNo(String transIdentifiNo) {
		this.transIdentifiNo = transIdentifiNo;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public Integer getAccumultTransNumDebit() {
		return accumultTransNumDebit;
	}

	public void setAccumultTransNumDebit(Integer accumultTransNumDebit) {
		this.accumultTransNumDebit = accumultTransNumDebit;
	}

	public BigDecimal getAccumultTransAmtDebit() {
		return accumultTransAmtDebit;
	}

	public void setAccumultTransAmtDebit(BigDecimal accumultTransAmtDebit) {
		this.accumultTransAmtDebit = accumultTransAmtDebit;
	}

	public Integer getAccumultTransNumCredit() {
		return accumultTransNumCredit;
	}

	public void setAccumultTransNumCredit(Integer accumultTransNumCredit) {
		this.accumultTransNumCredit = accumultTransNumCredit;
	}

	public BigDecimal getAccumultTransAmtCredit() {
		return accumultTransAmtCredit;
	}

	public void setAccumultTransAmtCredit(BigDecimal accumultTransAmtCredit) {
		this.accumultTransAmtCredit = accumultTransAmtCredit;
	}

	public String getProductForm() {
		return productForm;
	}

	public void setProductForm(String productForm) {
		this.productForm = productForm;
	}

	public String getTransStatisticGroup() {
		return transStatisticGroup;
	}

	public void setTransStatisticGroup(String transStatisticGroup) {
		this.transStatisticGroup = transStatisticGroup;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public String getTransIdentifiDesc() {
		return transIdentifiDesc;
	}

	public void setTransIdentifiDesc(String transIdentifiDesc) {
		this.transIdentifiDesc = transIdentifiDesc;
	}

	public String getCurrencyDesc() {
		return currencyDesc;
	}

	public void setCurrencyDesc(String currencyDesc) {
		this.currencyDesc = currencyDesc;
	}

}
