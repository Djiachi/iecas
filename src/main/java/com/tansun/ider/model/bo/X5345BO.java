package com.tansun.ider.model.bo;

import com.tansun.ider.dao.beta.entity.CoreActivityArtifactRel;
import com.tansun.ider.framwork.commun.BeanVO;
import com.tansun.ider.service.business.EventCommArea;

import java.util.List;

public class X5345BO extends BeanVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9079968645380120430L;
    /** 客户代码 [12,0] Not NULL */
	private String customerNo;
    /** 本次账单日期 yyyy-MM-dd [10,0] */
    private String statementDate;
    /** 本次最后还款日期 yyyy-MM-dd [10,0] */
    private String paymentDueDate;
    /** 本次宽限日期 yyyy-MM-dd [10,0] */
    private String graceDate;
    /** 本次迟缴费收取日期 yyyy-MM-dd [10,0] */
    private String delinquencyDate;
    /** 业务项目 [9,0] Not NULL */
    private String businessProgramNo;
    /** 周期号 [10,0] Not NULL */
    private Integer cycleNumber;
    /** 约定扣款日期 [10,0] */
    private String directDebitDate;
	/** 本周期修改次数 [10,0] */
	private Integer cycleModifyNo;
	/** 上一最后还款日 yyyy-MM-dd [8,0] */
	private String lastPaymentDueDate;
	/** 上一宽限日期yyyy-MM-dd [8,0] */
	private String lastGraceDate;
	/** 上一迟缴费收取日期yyyy-MM-dd [8,0] */
	private String lastDelinquencyDate;
	/** 上一约定扣款日期yyyy-MM-dd [8,0] */
	private String lastDirectDebitDate;
	/** 创建时间 yyyy-MM-dd HH:mm:ss [23,0] */
	private String gmtCreate;

	/** 版本号 [10,0] Not NULL */
	private Integer version;
    /** 业务项目代码 */
    private String programDesc;
    private String idNumber;
    private String idType;
    private String externalIdentificationNo;

	/** 全局事件编号 */
	private String globalEventNo;


	/** 事件编号 [14,0] Not NULL */
	protected String eventNo;
	/** 活动编号 [8,0] Not NULL */
	protected String activityNo;

	/** 公共区域信息 */
	private EventCommArea eventCommArea;
	/** 构件列表 */
	private List<CoreActivityArtifactRel> artifactList;
    
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getStatementDate() {
		return statementDate;
	}
	public void setStatementDate(String statementDate) {
		this.statementDate = statementDate;
	}
	public String getPaymentDueDate() {
		return paymentDueDate;
	}
	public void setPaymentDueDate(String paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}
	public String getGraceDate() {
		return graceDate;
	}
	public void setGraceDate(String graceDate) {
		this.graceDate = graceDate;
	}
	public String getDelinquencyDate() {
		return delinquencyDate;
	}
	public void setDelinquencyDate(String delinquencyDate) {
		this.delinquencyDate = delinquencyDate;
	}
	public String getBusinessProgramNo() {
		return businessProgramNo;
	}
	public void setBusinessProgramNo(String businessProgramNo) {
		this.businessProgramNo = businessProgramNo;
	}
	public Integer getCycleNumber() {
		return cycleNumber;
	}
	public void setCycleNumber(Integer cycleNumber) {
		this.cycleNumber = cycleNumber;
	}
	public String getDirectDebitDate() {
		return directDebitDate;
	}
	public void setDirectDebitDate(String directDebitDate) {
		this.directDebitDate = directDebitDate;
	}
	public String getProgramDesc() {
		return programDesc;
	}
	public void setProgramDesc(String programDesc) {
		this.programDesc = programDesc;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getExternalIdentificationNo() {
		return externalIdentificationNo;
	}
	public void setExternalIdentificationNo(String externalIdentificationNo) {
		this.externalIdentificationNo = externalIdentificationNo;
	}

	public Integer getCycleModifyNo() {
		return cycleModifyNo;
	}

	public void setCycleModifyNo(Integer cycleModifyNo) {
		this.cycleModifyNo = cycleModifyNo;
	}

	public String getLastPaymentDueDate() {
		return lastPaymentDueDate;
	}

	public void setLastPaymentDueDate(String lastPaymentDueDate) {
		this.lastPaymentDueDate = lastPaymentDueDate;
	}

	public String getLastGraceDate() {
		return lastGraceDate;
	}

	public void setLastGraceDate(String lastGraceDate) {
		this.lastGraceDate = lastGraceDate;
	}

	public String getLastDelinquencyDate() {
		return lastDelinquencyDate;
	}

	public void setLastDelinquencyDate(String lastDelinquencyDate) {
		this.lastDelinquencyDate = lastDelinquencyDate;
	}

	public String getLastDirectDebitDate() {
		return lastDirectDebitDate;
	}

	public void setLastDirectDebitDate(String lastDirectDebitDate) {
		this.lastDirectDebitDate = lastDirectDebitDate;
	}

	public String getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getGlobalEventNo() {
		return globalEventNo;
	}

	public void setGlobalEventNo(String globalEventNo) {
		this.globalEventNo = globalEventNo;
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

	public EventCommArea getEventCommArea() {
		return eventCommArea;
	}

	public void setEventCommArea(EventCommArea eventCommArea) {
		this.eventCommArea = eventCommArea;
	}

	public List<CoreActivityArtifactRel> getArtifactList() {
		return artifactList;
	}

	public void setArtifactList(List<CoreActivityArtifactRel> artifactList) {
		this.artifactList = artifactList;
	}
}
