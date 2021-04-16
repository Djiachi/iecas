package com.tansun.ider.model.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * CoreBudgetOrgAddInfo
 * 
 * @author PG(Auto Generator)
 * @version V1.0
 */
public class X5155VO  {

    /** 客户代码 [12,0] Not NULL */
    protected String id;
    /** 客户代码 [12,0] Not NULL */
    protected String customerNo;
    /** 预算管理层级CEN:中央级PRV:省级CIT:市级COT:区县级TWN:乡镇级 [3,0] Not NULL */
    protected String manageLevelCode;
    /** 单位公务卡总授信额度 [18,0] */
    protected BigDecimal orgAllQuota;
    /** 预算单位可用额度 [18,0] */
    protected BigDecimal orgRestQuota;
    /** 个人公务卡最大授信额度 [18,0] */
    protected BigDecimal personMaxQuota;
    /** 时间戳 : oralce使用触发器更新， mysql使用自动更新 [19,0] Not NULL */
    protected Date timestamp;
    /** 创建时间 yyyy-MM-dd HH:mm:ss.SSS [23,0] */
    protected String gmtCreate;
    /** 版本号 [10,0] Not NULL */
    protected Integer version;
    /** 客户姓名 [30,0] */
    protected String customerName;
    /** 个人公司标识 [1,0] */
    protected String customerType;
    /** 预算单位类型 [1,0] */
    protected String idType;
    
    public BigDecimal getOrgRestQuota() {
		return orgRestQuota;
	}

	public void setOrgRestQuota(BigDecimal orgRestQuota) {
		this.orgRestQuota = orgRestQuota;
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

	/** 预算单位编码 [30,0] */
    protected String idNumber;


    public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/** 取值 <== 客户代码 [12,0], Not NULL */
    public String getCustomerNo() {
        return customerNo;
    }

    /** 赋值 ==> 客户代码 [12,0], Not NULL */
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo == null ? null : customerNo.trim();
    }

    /** 取值 <== 预算管理层级CEN:中央级PRV:省级CIT:市级COT:区县级TWN:乡镇级 [3,0], Not NULL */
    public String getManageLevelCode() {
        return manageLevelCode;
    }

    /** 赋值 ==> 预算管理层级CEN:中央级PRV:省级CIT:市级COT:区县级TWN:乡镇级 [3,0], Not NULL */
    public void setManageLevelCode(String manageLevelCode) {
        this.manageLevelCode = manageLevelCode == null ? null : manageLevelCode.trim();
    }

    /** 取值 <== 单位公务卡总授信额度 [18,0] */
    public BigDecimal getOrgAllQuota() {
        return orgAllQuota;
    }

    /** 赋值 ==> 单位公务卡总授信额度 [18,0] */
    public void setOrgAllQuota(BigDecimal orgAllQuota) {
        this.orgAllQuota = orgAllQuota;
    }

    /** 取值 <== 个人公务卡最大授信额度 [18,0] */
    public BigDecimal getPersonMaxQuota() {
        return personMaxQuota;
    }

    /** 赋值 ==> 个人公务卡最大授信额度 [18,0] */
    public void setPersonMaxQuota(BigDecimal personMaxQuota) {
        this.personMaxQuota = personMaxQuota;
    }

    /** 取值 <== 时间戳 : oralce使用触发器更新， mysql使用自动更新 [19,0], Not NULL */
    public Date getTimestamp() {
        return timestamp;
    }

    /** 赋值 ==> 时间戳 : oralce使用触发器更新， mysql使用自动更新 [19,0], Not NULL */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /** 取值 <== 创建时间 yyyy-MM-dd HH:mm:ss.SSS [23,0] */
    public String getGmtCreate() {
        return gmtCreate;
    }

    /** 赋值 ==> 创建时间 yyyy-MM-dd HH:mm:ss.SSS [23,0] */
    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate == null ? null : gmtCreate.trim();
    }

    /** 取值 <== 版本号 [10,0], Not NULL */
    public Integer getVersion() {
        return version;
    }

    /** 赋值 ==> 版本号 [10,0], Not NULL */
    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "X5155VO [id = " + id + ", customerNo = " + customerNo + ", manageLevelCode = "
                + manageLevelCode + ", orgAllQuota = " + orgAllQuota + ", personMaxQuota = " + personMaxQuota
                + ", timestamp = " + timestamp + ", gmtCreate = " + gmtCreate + ", version = " + version + "]";
    }
}