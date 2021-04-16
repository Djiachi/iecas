package com.tansun.ider.model.bo;

import com.tansun.ider.framwork.commun.BeanVO;

import java.io.Serializable;
import java.util.Date;

public class X4095BO extends BeanVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 计划代码 */
	protected String planId;
	/** 计划代码描述 [255,0] */
    protected String planDesc;
	/** 运营模式 [3,0] Not NULL */
    protected String operationMode;
    /** 转出机构代码（债权ID） [25,0] Not NULL */
    protected String capitalOrganizationCode;
    /** 转出机构名称 [120,0] */
    protected String capitalOrganizationName;
    /** 资产转变阶段 PACK- 封包 TRSF-转让期 REPO- 回购 [4,0] Not NULL */
    protected String capitalStage;
    /** 资产转变子阶段(循环账户专用) TRS1-转循环期 TRS2- 转摊还期 [4,0] */
    protected String capitalSubStage;
    /** 账户类型 R-循环账户 T-交易账户 B-不良资产 [1,0] */
    protected String accountType;
    /** 操作员ID [10,0] */
    protected String userId;
    /** 创建时间 : 创建时间 [23,0] */
    protected String gmtCreate;
    /** 时间戳 : 时间戳 [19,0] */
    protected Date timestamp;
    /** 版本号 [10,0] */
    protected Integer version;
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getPlanDesc() {
		return planDesc;
	}
	public void setPlanDesc(String planDesc) {
		this.planDesc = planDesc;
	}
	public String getOperationMode() {
		return operationMode;
	}
	public void setOperationMode(String operationMode) {
		this.operationMode = operationMode;
	}
	public String getCapitalOrganizationCode() {
		return capitalOrganizationCode;
	}
	public void setCapitalOrganizationCode(String capitalOrganizationCode) {
		this.capitalOrganizationCode = capitalOrganizationCode;
	}
	public String getCapitalOrganizationName() {
		return capitalOrganizationName;
	}
	public void setCapitalOrganizationName(String capitalOrganizationName) {
		this.capitalOrganizationName = capitalOrganizationName;
	}
	public String getCapitalStage() {
		return capitalStage;
	}
	public void setCapitalStage(String capitalStage) {
		this.capitalStage = capitalStage;
	}
	public String getCapitalSubStage() {
		return capitalSubStage;
	}
	public void setCapitalSubStage(String capitalSubStage) {
		this.capitalSubStage = capitalSubStage;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
}
