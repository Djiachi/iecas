package com.tansun.ider.model.vo;

import java.math.BigDecimal;
import java.util.List;

import com.tansun.ider.dao.issue.entity.CoreCustomerAddr;

/**
 * <p> Title: X4005VO </p>
 * <p> Description: 预算单位信息查询出参</p>
 * <p> Copyright: veredholdings.com Copyright (C) 2019 </p>
 *
 * @author cuiguangchao
 * @since 2019年4月24日
 */
public class X4005VO {
	
	private String corporation;
    /** 客户号 [12,0] */
	private String customerNo;
    /** 证件类型 */
    private String idType;
    /** 预算单位编码 */
    private String idNumber;
    /** 预算单位地址信息 */
    private List<CoreCustomerAddr> coreCoreCustomerAddrs;
    /** 客户姓名 */
    private String customerName;
    /** 账单日 */
    private String billDay;
    /** 客户类型 */
    private String customerType;
    /** 城市 */
    private String city;
    /** 预算管理层级 */
    private String manageLevelCode;
    /** 单位公务卡总授信额度 */
    private BigDecimal orgAllQuota;
    /** 个人公务卡最大授信额度 */
    private BigDecimal personMaxQuota;
    /** 所属机构号 [10,0] Not NULL */
    private String organNo;
    /** 片区编号 */
    private String gnsNumber;
    /** 机构号 */
    private String institutionId;

    public String getCorporation() {
		return corporation;
	}

	public void setCorporation(String corporation) {
		this.corporation = corporation;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
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

    public List<CoreCustomerAddr> getCoreCoreCustomerAddrs() {
        return coreCoreCustomerAddrs;
    }

    public void setCoreCoreCustomerAddrs(List<CoreCustomerAddr> coreCoreCustomerAddrs) {
        this.coreCoreCustomerAddrs = coreCoreCustomerAddrs;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBillDay() {
        return billDay;
    }

    public void setBillDay(String billDay) {
        this.billDay = billDay;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getManageLevelCode() {
        return manageLevelCode;
    }

    public void setManageLevelCode(String manageLevelCode) {
        this.manageLevelCode = manageLevelCode;
    }

    public BigDecimal getOrgAllQuota() {
        return orgAllQuota;
    }

    public void setOrgAllQuota(BigDecimal orgAllQuota) {
        this.orgAllQuota = orgAllQuota;
    }

    public BigDecimal getPersonMaxQuota() {
        return personMaxQuota;
    }

    public void setPersonMaxQuota(BigDecimal personMaxQuota) {
        this.personMaxQuota = personMaxQuota;
    }

    public String getOrganNo() {
        return organNo;
    }

    public void setOrganNo(String organNo) {
        this.organNo = organNo;
    }

    public String getGnsNumber() {
        return gnsNumber;
    }

    public void setGnsNumber(String gnsNumber) {
        this.gnsNumber = gnsNumber;
    }

}
